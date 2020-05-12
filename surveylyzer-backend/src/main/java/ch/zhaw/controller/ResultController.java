package ch.zhaw.controller;

import ch.zhaw.controller.utils.ControllerUtils;
import ch.zhaw.db.Storage;
import ch.zhaw.domain.Survey;
import ch.zhaw.domain.SurveyTemplate;
import ch.zhaw.pdffunctionality.PDFAnalyzer;
import org.apache.commons.io.FileUtils;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Get result data from database or error messages.
 */
@RestController
@RequestMapping()
public class ResultController {

    @Autowired
    private Storage dataBase;

    private Object[][] results;

    @PostMapping("/resultObject")
    public ResponseEntity<Object[][]> getSurveyPdf(@RequestParam("file") MultipartFile file, @RequestParam("surveyId") String surveyId) {
        PDFAnalyzer analyzer = new PDFAnalyzer();
        Survey survey = dataBase.getSurveyResultById(surveyId);

        SurveyTemplate surveyTemplate = survey.getSurveyTemplate();
        File template = new File("template_" + surveyId);
        File surveyFile = new File("survey_" + surveyId);

        if (surveyTemplate != null) {
            Binary binaryTemplate = surveyTemplate.getTemplate();
            if (binaryTemplate != null) {
                byte[] byteTemplate = binaryTemplate.getData();
                try {
                    FileUtils.writeByteArrayToFile(template, byteTemplate);
                    FileUtils.writeByteArrayToFile(surveyFile, ControllerUtils.multipartToByteArray(file));
                    results = analyzer.startHighlightingExternalFile(template, surveyFile);
                    survey.setResult(results);
                    dataBase.saveOrUpdateSurveyResult(survey);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    template.delete();
                    surveyFile.delete();
                }
            }

        }
        return new ResponseEntity<>(results, HttpStatus.CREATED);
    }

    @PostMapping("/visualizeResults")
    public ResponseEntity<Object [][]> getResults(@RequestParam("surveyId") String surveyId) {
        // todo: error handling + user infos / alerts
        String[] header = {"Questions", "1", "2", "3"};
        Object[][] dummyResult = {header};
        if (surveyId != null) {
            Survey survey = dataBase.getSurveyResultById(surveyId);
            if (survey != null && survey.getResult() != null) {
                return  new ResponseEntity<>(survey.getResult(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(dummyResult, HttpStatus.CREATED);
            }

        } else {
            return new ResponseEntity<>(dummyResult, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/rawResults", method = RequestMethod.GET)
    @ResponseBody
    public Object [][] getRawData(@RequestParam("surveyId") String surveyId) {
        //todo: error handling must be still implemented
        String[] header = {"Questions", "1", "2", "3"};
        Object[][] dummyResult = {header};

        if (surveyId != null) {
            Survey survey = dataBase.getSurveyResultById(surveyId);
            if (survey.getResult() != null) {
                return survey.getResult();
            } else {
                return dummyResult;
            }

        } else {
            return dummyResult;
        }
    }

    private static Object[][] simulateBusy() {
        // return null; // "not found"
        // return simulateBusy(); // "found but empty --> result not yet ready"
        Object[][] busy = {{"$$busy$$"}};
        return busy;
    }

}
