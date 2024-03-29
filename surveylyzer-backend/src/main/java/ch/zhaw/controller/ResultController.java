package ch.zhaw.controller;

import ch.zhaw.controller.utils.ControllerUtils;
import ch.zhaw.db.Storage;
import ch.zhaw.domain.Survey;
import ch.zhaw.domain.SurveyTemplate;
import ch.zhaw.pdffunctionality.InitFileException;
import ch.zhaw.pdffunctionality.PDFAnalyzer;
import ch.zhaw.pdffunctionality.SurveyFileException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Provides mappings for
 * - starting survey calculation
 * - getting result data from database
 * returns error messages on failure.
 */
@RestController
@RequestMapping()
public class ResultController {

    @Autowired
    private Storage dataBase;

    private Object[][] results;

    @PostMapping("/resultObject")
    public ResponseEntity<Object[][]> calculateResult(@RequestParam("file") MultipartFile file, @RequestParam("surveyId") String surveyId) {
        PDFAnalyzer analyzer = new PDFAnalyzer();
        Survey survey = dataBase.getSurveyResultById(surveyId);
        String name = FilenameUtils.removeExtension(file.getOriginalFilename());

        SurveyTemplate surveyTemplate = survey.getSurveyTemplate();
        File template = new File("template_" + surveyId);
        File surveyFile = new File("survey_" + surveyId);
        HttpStatus status = HttpStatus.FAILED_DEPENDENCY;

        if (surveyTemplate != null) {
            Binary binaryTemplate = surveyTemplate.getTemplate();
            if (binaryTemplate != null) {
                byte[] byteTemplate = binaryTemplate.getData();
                try {
                    FileUtils.writeByteArrayToFile(template, byteTemplate);
                    FileUtils.writeByteArrayToFile(surveyFile, ControllerUtils.multipartToByteArray(file));
                    results = analyzer.startHighlightingExternalFile(template, surveyFile);
                    survey.setResult(results);
                    survey.setTitle(name);
                    dataBase.saveOrUpdateSurveyResult(survey);
                    status = HttpStatus.CREATED;

                } catch (IOException e) {
                    e.printStackTrace();
                    status = HttpStatus.INTERNAL_SERVER_ERROR;
                } catch (InitFileException e) {
                    e.printStackTrace();
                    status = HttpStatus.NOT_ACCEPTABLE;
                } catch (SurveyFileException e) {
                    e.printStackTrace();
                    status = HttpStatus.EXPECTATION_FAILED;
                } finally {
                    template.delete();
                    surveyFile.delete();
                }
            }
        }
        return new ResponseEntity<>(results, status);
    }

    @GetMapping("/resultTitle")
    public ResponseEntity<String> getSurveyTitle(@RequestParam("surveyId") String surveyId) {
        String title = "Survey Result";
        HttpStatus status = HttpStatus.OK;
        try {
            Survey s = dataBase.getSurveyResultById(surveyId);
            if (s != null && s.getTitle() != null && !s.getTitle().trim().isEmpty()) {
                title = s.getTitle();
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(title, status);
    }

    @GetMapping("/visualizeResults")
    public ResponseEntity<Object[][]> getResults(@RequestParam("surveyId") String surveyId) {
        if (surveyId != null) {
            Survey survey = dataBase.getSurveyResultById(surveyId);
            if (survey != null && survey.getResult() != null) {
                return new ResponseEntity<>(survey.getResult(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }

    @GetMapping("/rawResults")
    @ResponseBody
    public Object[][] getRawData(@RequestParam("surveyId") String surveyId) {
        if (surveyId != null) {
            Survey survey = dataBase.getSurveyResultById(surveyId);
            if (survey == null) {
                String[] header = {"Your URI is not correct! Insert correct URI to get your raw results."};
                return new Object[][]{header};
            } else if (survey.getResult() != null) {
                return survey.getResult();
            } else {
                String[] header = {"We are still processing your request."};
                return new Object[][]{header};
            }

        } else {
            String[] header = {"Your ID is empty! Ensure that your URI includes ID parameter."};
            return new Object[][]{header};
        }
    }
}
