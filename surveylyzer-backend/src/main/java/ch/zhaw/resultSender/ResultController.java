package ch.zhaw.resultSender;

import ch.zhaw.pdffunctionality.PDFAnalyzer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pdfResult")
public class ResultController {

    @PostMapping()
    public Object[][] exportsResult(@RequestParam("id") MultipartFile surveyId, @RequestParam("file") MultipartFile file){

//        PDFAnalyzer analyzer = new PDFAnalyzer();
//        survey = db.getSurvey(ID)
//        template = survey.template
//        analyzer.startHighlightingExternalFile(template, file);

        return Result.getResult();
    }


    @GetMapping()
   public Object[][] exportsResult(){
       return Result.getResult();
    }

}
