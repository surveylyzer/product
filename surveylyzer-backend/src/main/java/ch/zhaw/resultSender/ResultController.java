package ch.zhaw.resultSender;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/result")
public class ResultController {
    @GetMapping()
    public String[][]exportResult() {
        String[][] result = Result.createGoogleMockResult();
        return result;
    }
}
