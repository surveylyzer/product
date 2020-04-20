package ch.zhaw.resultSender;

import ch.zhaw.pdfReceiver.PdfFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pdfResult")
public class ResultController {

    @GetMapping()
   public String exportsResult(){
       return Result.getResult();
    }

}
