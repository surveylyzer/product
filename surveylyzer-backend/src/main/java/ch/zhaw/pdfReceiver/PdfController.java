package ch.zhaw.pdfReceiver;


import ch.zhaw.db.Storage;
import ch.zhaw.domain.Survey;
import ch.zhaw.domain.SurveyTemplate;
import org.bson.types.Binary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;


@RestController
@RequestMapping("/pdf")
public class PdfController {

    private Storage db ;

    /**
     * POST METHOD -> Create new PDF File
     */
    @PostMapping()
    public ResponseEntity<HttpStatus> createPDF(@RequestParam("file1") MultipartFile file1,@RequestParam("pdfType") String pdfType) {
        System.out.println("Received File: " + file1.getOriginalFilename()+" of type: "+pdfType);
        forwardMultipartFiletoDB(file1, pdfType);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // No Methodiscription since refocatoring required
    private void forwardMultipartFiletoDB(MultipartFile file, String pdfType){
        System.out.println("forwardMultipartFiletoDB -> started");
        db.emptyDatabase();
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
        if(pdfType.equalsIgnoreCase("templateFile")){
            SurveyTemplate t = new SurveyTemplate(new Binary(pdfData));
            Survey s = db.saveNewResult(null, t);
        } else if (pdfType.equalsIgnoreCase("dataFile")){
            System.out.println("Not yet implmented");
        }
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
