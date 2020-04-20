package ch.zhaw.surveylyzerbackend;

import ch.zhaw.domain.Survey;
import ch.zhaw.domain.SurveyRepository;
import ch.zhaw.domain.SurveyTemplate;
import ch.zhaw.domain.SurveyTemplateRepository;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.UUID;

@Component
public class TmpTestDB implements CommandLineRunner {

    @Autowired
    private SurveyRepository surveyRepo;
    @Autowired
    private SurveyTemplateRepository templateRepo;
    private Binary pdf;
    private Object[][] result = {
            {"City", "1", "2", "3", "4"},
            {"Question 1", 23, 47, 2, 5},
            {"Question 2", 24, 10, 40, 3},
            {"Question 3", 3, 57, 15, 1}
    };

    @Override
    public void run(String... args) throws Exception {
        testDBFunctionality();
    }

    private void testDBFunctionality(){
//        surveyRepo.deleteAll();
        Binary b = ReadPdf("surveylyzer-backend/pdf_umfragen/initFile.pdf");

        // select by id
        Survey sFound = surveyRepo.findById("5fc937cd-e8fd-47f7-97fe-3b38f3b13c32").orElse(null);

        // save new template
        SurveyTemplate t = new SurveyTemplate(b);
        t = templateRepo.save(t);

        // save new survey for this template to db
        Survey s = new Survey(t, result);
        s.setId(UUID.randomUUID());
        s = surveyRepo.save(s);

        // save already existing to db --> performs update
//        s = new Survey();
//        s.setId(sFound.getId());
//        s = surveyRepo.save(s);

        // select all
        List<Survey> sAll = surveyRepo.findAll();
        List<SurveyTemplate> tAll = templateRepo.findAll();
        for(Survey item : sAll){
            System.out.println(item);
        }
    }

    private Binary ReadPdf(String path){
        File pdf = null;
        byte[] pdfData = null;
        try {
            pdf = new File(path);
            pdfData = new byte[(int)pdf.length()];
            FileInputStream in = new FileInputStream(pdf);
            in.read(pdfData);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Binary(pdfData);
    }
}
