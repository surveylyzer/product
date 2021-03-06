package ch.zhaw.controller;

import ch.zhaw.surveyitems.SurveyItemAbstract;
import ch.zhaw.surveyitems.SurveyItemManager;
import ch.zhaw.db.Storage;
import ch.zhaw.domain.Survey;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides mappings for returning results as csv.
 */
@Controller
public class CsvExportController {

    @Autowired
    private Storage dataBase;

    private SurveyItemManager itemManager = new SurveyItemManager();
    private List<SurveyItemAbstract> surveyItems = new ArrayList<>();
    private String id = "";
    private int counter = 0;

    @PostMapping("/get-results-csv")
    public ResponseEntity<String> prepareResultForExport(@RequestParam("surveyId") String surveyId) {

        if (!id.equals(surveyId)) {
            counter = 1;
        } else {
            counter++;
        }

        if (counter == 1) {
            Object[][] results;
            if (surveyId != null) {
                this.id = surveyId;
                itemManager.clearItems();
                surveyItems.clear();
                Survey survey = dataBase.getSurveyResultById(id);
                if (survey != null && survey.getResult() != null) {
                    results = survey.getResult();
                    itemManager.setResults(results);
                    surveyItems = itemManager.parseResults(results);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/export-survey-results")
    public ResponseEntity<String> exportCSV(HttpServletResponse response) throws Exception {
        String filename = "survey_results_" + id + ".csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<SurveyItemAbstract> writer = new StatefulBeanToCsvBuilder<SurveyItemAbstract>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .build();

        //write all survey items to csv file
        writer.write(surveyItems);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}