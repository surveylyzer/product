package ch.zhaw.pdfReceiver;


import ch.zhaw.db.Storage;
import ch.zhaw.domain.Survey;
import ch.zhaw.domain.SurveyTemplate;
import ch.zhaw.surveylyzerbackend.SurveylyzerBackendApplication;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.UUID;


@RestController
@RequestMapping()
public class PdfController {
    @Autowired
    private Storage databaseAccess ;

    private Survey survey = null;
    private File dataFile = null;

    /**
     * POST METHOD -> Get PDF Files from Frontend
     */
    @PostMapping("/template")
    // --> COM 1 Ende <---
    public ResponseEntity<UUID> persistTemplate(@RequestParam("file1") MultipartFile file1,@RequestParam("pdfType") String pdfType) {
        System.out.println("--> Received Template : " + file1.getOriginalFilename());
        //templateFile are being persisted in db
        survey = persistTemplate(file1);
        UUID surveyId = survey.getId();
        // --> COM 2 Start <---
        return new ResponseEntity<>(surveyId,HttpStatus.CREATED);
    }


    /**
     * Persist received template File and get Survey
     * @param file
     * @return
     */
    private Survey persistTemplate(MultipartFile file){
        byte[] pdfData = multipartToByteArray(file);
        SurveyTemplate t = new SurveyTemplate(new Binary(pdfData));
        Survey survey = databaseAccess.saveNewResult(null, t);
        System.out.println("SurveyID:" + survey.getId());
        return survey;
    }


    /**
     * Converts Multipart file into Bytearray
     * @param file
     * @return
     */
    private byte[] multipartToByteArray(MultipartFile file){
        File pdf = null;
        byte[] pdfData = null;
        try {
            pdf = multipartToFile(file,file.getOriginalFilename());
            pdfData = new byte[(int)pdf.length()];
            FileInputStream in = new FileInputStream(pdf);
            in.read(pdfData);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdfData;

    }

    /**
     * Transforms a multipartFile (spring.io) into file (java.io)
     * @param multipart
     * @param fileName
     * @return
     */
    public  static File multipartToFile(MultipartFile multipart, String fileName) {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
        try {
            multipart.transferTo(convFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convFile;
    }

}
