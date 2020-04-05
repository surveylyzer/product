package ch.zhaw.pdfReceiver;

import ch.zhaw.surveylyzerbackend.SurveylyzerBackendApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SurveylyzerBackendApplication.class)
@AutoConfigureMockMvc
public class PdfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void returnDefaultEmptyPdfArray() throws Exception {
        this.mockMvc.perform(get("/pdf")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    /* TEMPLATE FOR TEST TO BE WRITTEN*/
    /*
    @Test
    public void sendPDF() {
        this.mockMvc.perform(post("/pdf").param("file1", "1")
                .param("name", "John Doe")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.message").value("Hello World John Doe!!!"))
                .andExpect(jsonPath("$.id").value(1));
    }
    */

}
