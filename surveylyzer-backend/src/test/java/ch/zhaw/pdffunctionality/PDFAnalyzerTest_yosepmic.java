package ch.zhaw.pdffunctionality;


import ch.zhaw.surveylyzerbackend.SurveylyzerBackendApplication;
import net.sourceforge.tess4j.Word;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;






    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SurveylyzerBackendApplication.class)
    public class PDFAnalyzerTest_yosepmic {
        PDFAnalyzer pdfAnalyzer = new PDFAnalyzer();

        @Test
        public void distWordsTest(){
            Word w1;
            Word w2;
            //provoke test commit


        }
}
