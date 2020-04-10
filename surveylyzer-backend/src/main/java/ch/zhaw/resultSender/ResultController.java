package ch.zhaw.resultSender;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pdfResult")
public class ResultController {
    @GetMapping()
   public String exportsResult(){
        String resultSet = Result.getResult();
       return resultSet;
    }


}
