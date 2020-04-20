package ch.zhaw.resultSender;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pdfResult")
public class ResultController {

    @GetMapping()
   public Object[][] exportsResult(){
       return Result.getResult();
    }

}
