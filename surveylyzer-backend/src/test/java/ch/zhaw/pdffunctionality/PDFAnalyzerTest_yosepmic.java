package ch.zhaw.pdffunctionality;


import ch.zhaw.surveylyzerbackend.SurveylyzerBackendApplication;
import net.sourceforge.tess4j.Word;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SurveylyzerBackendApplication.class)
public class PDFAnalyzerTest_yosepmic {
    PDFAnalyzer pdfAnalyzer = new PDFAnalyzer();

    @Test
    public void distWordsTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Rectangle r1 = new Rectangle(0, 0, 2, 4);
        Rectangle r2 = new Rectangle(5, 0, 2, 4);
        Word w1 = new Word("WordOne", 0, r1);
        Word w2 = new Word("WordTwo", 0, r2);

        Method distWords = PDFAnalyzer.class.getDeclaredMethod("distWords",Word.class, Word.class);
        distWords.setAccessible(true);
        double distExpected = Math.sqrt(Math.pow(w1.getBoundingBox().getCenterX() - w2.getBoundingBox().getCenterX(), 2)
                    + Math.pow(w1.getBoundingBox().getCenterY() - w2.getBoundingBox().getCenterY(), 2));
        double distActual = (Double) distWords.invoke(pdfAnalyzer,w1,w2);
        Assert.assertEquals(distExpected, distActual, 0);
    }

    @Test
    public void getWordAngleTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Rectangle r1 = new Rectangle(0, 0, 2, 4);
        Rectangle r2 = new Rectangle(5, 0, 2, 4);
        Word w1 = new Word("WordOne", 0, r1);
        Word w2 = new Word("WordTwo", 0, r2);

        Method getWordAngle = PDFAnalyzer.class.getDeclaredMethod("getWordAngle", Word.class, Word.class);
        getWordAngle.setAccessible(true);

        double ak = w1.getBoundingBox().getCenterX() - w2.getBoundingBox().getCenterX();
        double gk = w1.getBoundingBox().getCenterY() - w2.getBoundingBox().getCenterY();

        double angleExpected = Math.atan(gk)/(ak);
        double angleActual = (Double) getWordAngle.invoke(pdfAnalyzer,w1,w2);

        Assert.assertEquals(angleExpected, angleActual,0);
    }


}
