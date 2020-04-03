package ch.zhaw.csvgenerator;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class CsvExoportController {

    private CsvGeneratorService csvGeneratorService;

    public CsvExoportController(CsvGeneratorService csvGeneratorService) {
        this.csvGeneratorService = csvGeneratorService;
    }

    @GetMapping("/export-survey-results")
    public void exportCSV(HttpServletResponse response) throws Exception {

        //set file name and content type
        String filename = "survey_results.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<SurveyItem> writer = new StatefulBeanToCsvBuilder<SurveyItem>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        //write all users to csv file
        writer.write(csvGeneratorService.listSurveyItems());

    }
}