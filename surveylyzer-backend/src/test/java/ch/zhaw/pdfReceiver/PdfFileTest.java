package ch.zhaw.pdfReceiver;

import ch.zhaw.surveylyzerbackend.SurveylyzerBackendApplication;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SurveylyzerBackendApplication.class)
public class PdfFileTest {
    PdfFile pdfFile = new PdfFile(1, "testPDF", 10);
    @Test
    void getId() {
        int id = pdfFile.getId();
        Assert.assertEquals(1, id);
    }

    @Test
    void setId() {
        pdfFile.setId(2);
        int id = pdfFile.getId();
        Assert.assertEquals(2, id);
    }

    @Test
    void getPdfName() {
        Assert.assertEquals("testPDF", pdfFile.getPdfName());
    }

    @Test
    void setPdfName() {
        pdfFile.setPdfName("newName");
        Assert.assertEquals("newName", pdfFile.getPdfName());
    }

    @Test
    void getFileSizeKB() {
        Assert.assertEquals(10, pdfFile.getFileSizeKB());
    }

    @Test
    void setFileSizeKB() {
        pdfFile.setFileSizeKB(20);
        Assert.assertEquals(20, pdfFile.getFileSizeKB());
    }
}
