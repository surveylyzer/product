package ch.zhaw.pdffunctionality;

import ch.zhaw.surveylyzerbackend.SurveylyzerBackendApplication;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Word;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SurveylyzerBackendApplication.class)
public class PDFAnalyzerTest {
    PDFAnalyzer pdfAnalyzer = new PDFAnalyzer();
    String initPath;
    File fileInit;
    File filePrc;
    PDDocument empty;
    PDDocument docInit;
    PDDocument docPrc;

/*
    @Before
    void init() throws Exception{
        initPath = "surveylyzer-backend/";
        File fileInit = new File(initPath + "../pdf_umfragen/initFile.pdf");
        File filePrc = new File(initPath + "../pdf_umfragen/prcFile.pdf");
        try {
            docInit = PDDocument.load(fileInit);
            docPrc = PDDocument.load(filePrc);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
*/
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

    @Test
    void startHighlightingExternalFileTest2() throws Exception {
        File fileone = new File("wrong Path");
        try {
            pdfAnalyzer.startHighlightingExternalFile(fileone, fileone);
        } catch (Exception e) {
        }
    }
    @Test
    void prcIniFileTest2() {
        try {
            pdfAnalyzer.prcInitFile(empty);
        } catch (Exception e) {
        }
    }

    @Test
    void findRectangle() throws Exception {
     /*   pdfAnalyzer = new PDFAnalyzer();
        init();
        pdfAnalyzer.prcInitFile(docInit);
        Rectangle r1 = new Rectangle(0, 0, 4, 2);
        Rectangle r2 = new Rectangle(5, 0, 4, 2);
        Rectangle r3 = new Rectangle(5, 0, 4, 2);
        Rectangle newRectangle = pdfAnalyzer.findRectangle(10, 0);
        Assert.assertEquals(r2, newRectangle);*/
    }

    @Test
    void makeQuestionsTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method makeQuestions = PDFAnalyzer.class.getDeclaredMethod("makeQuestions", List.class, ArrayList.class);
        makeQuestions.setAccessible(true);

        int range = 20;
        Rectangle r1 = new Rectangle(0, 0, 2, 4);
        Rectangle r2 = new Rectangle(5, 0, 2, 4);
        Rectangle r3 = new Rectangle(10, 30, 2, 4);
        Rectangle r4 = new Rectangle(0, 30, 2, 4);

        Word w1 = new Word("WordOne", 0, r1);
        Word w2 = new Word("WordTwo", 0, r2);
        Word w3 = new Word("WordThree", 0, r1);
        Word w4 = new Word("WordFour", 0, r3);
        Word w5 = new Word("a", 0, r3);

        List<Word> all = new ArrayList<>();
        ArrayList<List<Rectangle>> gR = new ArrayList<List<Rectangle>>();
        List<Rectangle> listOne = new ArrayList<>();
        List<Rectangle> listTwo = new ArrayList<>();
        all.add(w1);
        all.add(w2);
        all.add(w3);
        all.add(w4);
        all.add(w5);
        listOne.add(r1);
        listTwo.add(r3);
        gR.add(listOne);
        gR.add(listTwo);

        ArrayList<String> result = (ArrayList<String>) makeQuestions.invoke(pdfAnalyzer,all,gR);
        String first = " WordOne  WordTwo  WordThree ";
        String second = " WordFour ";
        ArrayList<String> expected = new ArrayList<>();
        expected.add(first);
        expected.add(second);
        Assert.assertEquals(expected,result);
    }
    @Test
    void singleWordsTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method singleWords = PDFAnalyzer.class.getDeclaredMethod("singleWords", List.class);
        singleWords.setAccessible(true);
        int range = 20;
        Rectangle r1 = new Rectangle(0, 0, 2, 4);
        Rectangle r2 = new Rectangle(5, 0, 2, 4);
        Rectangle r3 = new Rectangle(10, 30, 2, 4);
        Rectangle r4 = new Rectangle(0, 30, 2, 4);

        Word w1 = new Word("WordOne", 0, r1);
        Word w2 = new Word("WordOne", 0, r2);
        Word w3 = new Word("WordOne", 0, r1);
        Word w4 = new Word("WordFour", 0, r3);
        Word w5 = new Word("a", 0, r3);

        List<Word> all = new ArrayList<>();
        all.add(w1);
        all.add(w2);
        all.add(w3);
        all.add(w4);
        all.add(w5);

        HashMap<String, Word> result = (HashMap<String, Word>) singleWords.invoke(pdfAnalyzer,all);
        HashMap<String, Word> expected = new HashMap<String, Word>();
        expected.put("WordFour", w4);
        Assert.assertEquals(expected, result);
    }

    @Test
    void calcRotationTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method calcRotation = PDFAnalyzer.class.getDeclaredMethod("calcRotation", List.class, List.class);
        calcRotation.setAccessible(true);
        int range = 20;
        Rectangle r1 = new Rectangle(0, 0, 2, 4);
        Rectangle r2 = new Rectangle(10, 0, 2, 4);
        Rectangle r3 = new Rectangle(5, 10, 2, 4);

        Word w1 = new Word("WordOne", 0, r1);
        Word w2 = new Word("WordTwo", 10, r2);
        Word w3 = new Word("WordThree", 100, r1);
        Word w4 = new Word("WordFour", 200, r3);
        Word w5 = new Word("a", 90, r1);
        Word w6 = new Word("WordOne", 0, r1);
        Word w7 = new Word("WordTwo", 0, r2);

        List<Word> listOne = new ArrayList<>();
        List<Word> listTwo = new ArrayList<>();
        List<Word> listThree = new ArrayList<>();
        List<List<Word>> all = new ArrayList<>();
        List<List<Word>> allTwo = new ArrayList<>();
        listOne.add(w1);
        listOne.add(w2);
        listOne.add(w3);
        listTwo.add(w4);
        listTwo.add(w5);
        all.add(listOne);
        all.add(listTwo);

        double result = (Double) calcRotation.invoke(pdfAnalyzer,null,all);
        double expected = 1.1;
        Assert.assertEquals(expected, result, 0.01);

        listThree.add(w6);
        listThree.add(w7);
        allTwo.add(listThree);
        result = (Double) calcRotation.invoke(pdfAnalyzer,null,allTwo);
        expected = 0.0;
        Assert.assertEquals(expected, result, 0.01);
    }


}