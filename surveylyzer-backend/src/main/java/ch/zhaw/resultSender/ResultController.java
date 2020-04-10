package ch.zhaw.resultSender;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pdfResult")
public class ResultController {
    @GetMapping()
   public String exportsResult(){
       String json = Result.fakeResult();
       return json;
    }

       /* public String[][]exportResult() {
        String[][] result = Result.createGoogleMockResult();
        return result;
    }*/
}
