package ch.zhaw.controller;


import ch.zhaw.controller.utils.ControllerUtils;
import ch.zhaw.db.Storage;
import ch.zhaw.domain.Survey;
import ch.zhaw.domain.SurveyTemplate;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

/**
 * Provides mappings for saving a template file and init a new survey.
 */
@RestController
@RequestMapping()
public class PdfController {

    @Autowired
    private Storage databaseAccess ;

    private Survey survey = null;
    /**
     * POST METHOD -> Get PDF Template from Frontend
     */
    @PostMapping("/template")
    public ResponseEntity<UUID> persistTemplate(@RequestParam("file1") MultipartFile file,@RequestParam("pdfType") String pdfType) {
        System.out.println("--> Received Template : " + file.getOriginalFilename());
        //templateFile are being persisted in db
        survey = persist(file);
        UUID surveyId = survey.getId();
        return new ResponseEntity<>(surveyId,HttpStatus.CREATED);
    }


    /**
     * Persist received File and returns Survey
     * @param file
     * @return
     */
    private Survey persist(MultipartFile file){
        byte[] pdfData = ControllerUtils.multipartToByteArray(file);
        SurveyTemplate t = new SurveyTemplate(new Binary(pdfData));
        return databaseAccess.saveNewResult("",null, t);
    }
}
