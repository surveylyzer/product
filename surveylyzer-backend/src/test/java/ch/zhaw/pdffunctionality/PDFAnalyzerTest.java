package ch.zhaw.pdffunctionality;

import ch.zhaw.surveylyzerbackend.SurveylyzerBackendApplication;
import net.sourceforge.tess4j.Word;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PDFAnalyzer.class)
public class PDFAnalyzerTest {
    PDFAnalyzer pdfAnalyzer = new PDFAnalyzer();

    @Test
    public void isHighlightedColourTest(){
        int color1 = new Color(255, 200, 0).getRGB();
        int color2 = new Color(0, 0, 0).getRGB();
        Assert.assertTrue(pdfAnalyzer.isHighlightedColour(color1));
        Assert.assertFalse(pdfAnalyzer.isHighlightedColour(color2));
    }
    @Test
    public void getAngleTest() throws Exception{
        double ak = 1;
        double gk = 2;
        Method getAngle = PDFAnalyzer.class.getDeclaredMethod("getAngle", double.class, double.class);
        getAngle.setAccessible(true);
        double angleExpected =  Math.atan((gk) / (ak));
        //Without reflection
        //double angleActual = pdfAnalyzer.getAngle(ak, gk);
        double angleActual = (Double) getAngle.invoke(pdfAnalyzer, ak, gk);
        Assert.assertEquals(angleExpected, angleActual, 0);
    }
    @Test
    public void groupRectangleTest()throws Exception{
        Method groupRectangle = PDFAnalyzer.class.getDeclaredMethod("groupRectangle", int.class, List.class);
        groupRectangle.setAccessible(true);

        int range = 20;
        Rectangle r1 = new Rectangle(0, 0, 2, 4);
        Rectangle r2 = new Rectangle(5, 0, 2, 4);
        Rectangle r3 = new Rectangle(10, 0, 2, 4);
        Rectangle r4 = new Rectangle(0, 30, 2, 4);

        List<Rectangle> unsorted = new ArrayList<>();
        unsorted.add(r3);
        unsorted.add(r2);
        unsorted.add(r1);

        ArrayList<List<Rectangle>> sorted = new ArrayList<List<Rectangle>>();
        List<Rectangle> rectangles = new ArrayList<Rectangle>();
        rectangles.add(r3);
        rectangles.add(r2);
        rectangles.add(r1);
        sorted.add(rectangles);

        //Without reflection
        //ArrayList<List<Rectangle>> sortedFromMethod = pdfAnalyzer.groupRectangle(range,unsorted);
        ArrayList<List<Rectangle>> sortedFromMethod = (ArrayList<List<Rectangle>>) groupRectangle.invoke(pdfAnalyzer,range,unsorted);
        Assert.assertEquals(sorted,sortedFromMethod);

        unsorted.add(r4);
        sortedFromMethod = (ArrayList<List<Rectangle>>) groupRectangle.invoke(pdfAnalyzer,range,unsorted);
        Assert.assertNotEquals(sorted,sortedFromMethod);

        List<Rectangle> rectangles2 = new ArrayList<Rectangle>();
        rectangles2.add(r4);
        sorted.add(rectangles2);
        Assert.assertEquals(sorted,sortedFromMethod);
    }
    @Test
    public void groupWordsTest()throws Exception{
        Method groupWords = PDFAnalyzer.class.getDeclaredMethod("groupWords", int.class, List.class);
        groupWords.setAccessible(true);

        int range = 20;
        Rectangle r1 = new Rectangle(0, 0, 2, 4);
        Rectangle r2 = new Rectangle(5, 0, 2, 4);
        Rectangle r3 = new Rectangle(10, 30, 2, 4);
        Rectangle r4 = new Rectangle(0, 30, 2, 4);

        Word w1 = new Word("WordOne", 0, r1);
        Word w2 = new Word("WordTwo", 0, r2);
        Word w3 = new Word("WordThree", 0, r1);
        Word w4 = new Word("WordFour", 0, r3);
        Word w5 = new Word("WordFive", 0, r4);
        Word w6 = new Word("WordSic", 0, r3);

        List<Word> unsorted = new ArrayList<>();
        unsorted.add(w1);
        unsorted.add(w2);
        unsorted.add(w3);
        unsorted.add(w4);
        unsorted.add(w5);
        unsorted.add(w6);

        ArrayList<List<Word>> sorted = new ArrayList<List<Word>>();
        List<Word> wordsOne = new ArrayList<Word>();
        List<Word> wordsTwo = new ArrayList<Word>();
        wordsOne.add(w1);
        wordsOne.add(w2);
        wordsOne.add(w3);
        wordsTwo.add(w4);
        wordsTwo.add(w5);
        wordsTwo.add(w6);
        sorted.add(wordsOne);
        sorted.add(wordsTwo);

        //Without reflection
        //ArrayList<List<Word>> sortedFromMethod = pdfAnalyzer.groupWords(range,unsorted);
        ArrayList<List<Word>> sortedFromMethod = (ArrayList<List<Word>>) groupWords.invoke(pdfAnalyzer,range,unsorted);
        Assert.assertEquals(sorted,sortedFromMethod);

    }
    @Test
    public void isInRangeTest() throws Exception{
        int range = 5;
        int a = 10;
        int b = 12;
        int c = 16;
        Method isInRange = PDFAnalyzer.class.getDeclaredMethod("isInRange", int.class, int.class, int.class);
        isInRange.setAccessible(true);
        Boolean resultOne = (Boolean) isInRange.invoke(pdfAnalyzer, range, a, b);
        Boolean resultTwo = (Boolean) isInRange.invoke(pdfAnalyzer, range, a, c);

        Assert.assertTrue(resultOne);
        Assert.assertFalse(resultTwo);
    }
    @Test
    public void sameWordsTest() throws Exception{
        Method sameWords = PDFAnalyzer.class.getDeclaredMethod("sameWords", HashMap.class, HashMap.class);
        sameWords.setAccessible(true);
        List<List<Word>> output = new ArrayList<List<Word>>();
        List<Word> listOne = new ArrayList<Word>();
        List<Word> listTwo = new ArrayList<Word>();
        Rectangle r1 = new Rectangle(0, 0, 2, 4);
        Rectangle r2 = new Rectangle(5, 0, 2, 4);
        Rectangle r3 = new Rectangle(10, 30, 2, 4);

        Word w1 = new Word("WordOne", 0, r1);
        Word w2 = new Word("WordTwo", 0, r2);
        Word w3 = new Word("WordThree", 0, r1);
        Word w4 = new Word("WordFour", 0, r3);

        HashMap<String, Word> firstMap = new HashMap<>();
        HashMap<String, Word> secondMap = new HashMap<>();
        firstMap.put("WordOne", w1);
        firstMap.put("WordTwo", w2);
        firstMap.put("WordThree", w3);
        firstMap.put("WordFour", w4);
        secondMap.put("WordOne", w1);
        secondMap.put("WordTwo", w2);

        listOne.add(w1);
        listOne.add(w1);
        listTwo.add(w2);
        listTwo.add(w2);
        output.add(listOne);
        output.add(listTwo);

        List<List<Word>> resultat = (List<List<Word>>) sameWords.invoke(pdfAnalyzer, firstMap, secondMap);
        Assert.assertEquals(output, resultat);
    }
}
