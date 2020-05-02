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


@RestController
@RequestMapping()
public class PdfController {

    @Autowired
    private Storage databaseAccess ;

    private Survey survey = null;
    /**
     * POST METHOD -> Get PDF Files from Frontend
     */
    @PostMapping("/template")
    public ResponseEntity<UUID> persistTemplate(@RequestParam("file1") MultipartFile file1,@RequestParam("pdfType") String pdfType) {
        System.out.println("--> Received Template : " + file1.getOriginalFilename());
        //templateFile are being persisted in db
        survey = persistTemplate(file1);
        UUID surveyId = survey.getId();
        return new ResponseEntity<>(surveyId,HttpStatus.CREATED);
    }


    /**
     * Persist received template File and get Survey
     * @param file
     * @return
     */
    private Survey persistTemplate(MultipartFile file){
        byte[] pdfData = ControllerUtils.multipartToByteArray(file);
        SurveyTemplate t = new SurveyTemplate(new Binary(pdfData));
        return databaseAccess.saveNewResult(null, t);
    }
}
