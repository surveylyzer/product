package ch.zhaw.db;

import ch.zhaw.domain.Survey;
import ch.zhaw.domain.SurveyTemplate;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;

@Component
public class TmpTestDB implements CommandLineRunner {
    @Autowired
    private Storage db;

    private final Binary pdf = ReadPdf("surveylyzer-backend/pdf_umfragen/initFile.pdf");
    private static final Object[][] result = {
            {"City", "1", "2", "3", "4"},
            {"Question 1", 23, 47, 2, 5},
            {"Question 2", 24, 10},
            {"Question 3", 3, 57, 15, 1}
    };

    @Override
    public void run(String... args) throws Exception {
        test();
    }

    private void test() throws Exception {
        // init test
        db.emptyDatabase();

        // Store new empty survey (to get an id) and its template
        SurveyTemplate t = new SurveyTemplate(pdf);
        Survey s = db.saveNewResult(null, t);

        // Update survey with result
        s.setResult(result);
        s = db.saveOrUpdateSurveyResult(s);

        // Read Survey by id
        Survey s2 = db.getSurveyResultById(s.getId());
//        s2 = db.getSurveyResultById(s.getId().toString()); // same

        // Read Template by id and add another Survey result
        SurveyTemplate t2 = db.getSurveyTemplateById(s.getTemplateId());
        Survey s3 = db.saveNewResult(result, t2);
//        Survey s3 = db.saveNewResult(result, t2.getId()); // same


        // TEST saveOrUpdateSurveyResult with empty template
//        s.setId(null); // --> creates a new survey... ok
        s.setResult(null);
        s = db.saveOrUpdateSurveyResult(s);

        // TEST saveOrUpdateSurveyTemplate with empty pdf
        t.setTemplate(null);
        t = db.saveOrUpdateSurveyTemplate(t);

    }

    private Binary ReadPdf(String path) {
        File pdf = null;
        byte[] pdfData = null;
        try {
            pdf = new File(path);
            pdfData = new byte[(int) pdf.length()];
            FileInputStream in = new FileInputStream(pdf);
            in.read(pdfData);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Binary(pdfData);
    }
}
