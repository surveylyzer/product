package ch.zhaw.pdffunctionality;

import ch.zhaw.surveylyzerbackend.SurveylyzerBackendApplication;
import net.sourceforge.tess4j.Word;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SurveylyzerBackendApplication.class)
public class PDFAnalyzerTest {
    PDFAnalyzer pdfAnalyzer = new PDFAnalyzer();
    String initPath;
    PDDocument empty;
    PDDocument docInit;
    PDDocument docPrc;
    Boolean runTest = true;


    void init() throws Exception{
        String tessdataPath = "/app/.apt/usr/share/tesseract-ocr/4.00/tessdata";
        runTest=false;
        if (Files.notExists(Paths.get(tessdataPath))) {
            runTest = true;
            initPath = "../surveylyzer-backend/";
            File fileInit = new File(initPath + "pdf_umfragen/HerokuTestdaten/initPostcardV5.pdf");
            File filePrc = new File(initPath + "pdf_umfragen/HerokuTestdaten/prcPostcardV5_S1.pdf");
            try {
                docInit = PDDocument.load(fileInit);
                docPrc = PDDocument.load(filePrc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    void isHighlightedColourTest(){
        int color1 = new Color(255, 200, 0).getRGB();
        int color2 = new Color(0, 0, 0).getRGB();
        Assert.assertTrue(pdfAnalyzer.isHighlightedColour(color1));
        Assert.assertFalse(pdfAnalyzer.isHighlightedColour(color2));
    }
    @Test
    void getAngleTest() {
        double ak = 1;
        double gk = 2;
        double angleExpected =  Math.atan((gk) / (ak));
        double angleActual = pdfAnalyzer.getAngle(ak, gk);
        Assert.assertEquals(angleExpected, angleActual, 0);
    }
    @Test
    void groupRectangleTest(){
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

        ArrayList<List<Rectangle>> sortedFromMethod =  pdfAnalyzer.groupRectangle(range,unsorted);
        Assert.assertEquals(sorted,sortedFromMethod);

        unsorted.add(r4);
        sortedFromMethod = pdfAnalyzer.groupRectangle(range,unsorted);
        Assert.assertNotEquals(sorted,sortedFromMethod);

        List<Rectangle> rectangles2 = new ArrayList<Rectangle>();
        rectangles2.add(r4);
        sorted.add(rectangles2);
        Assert.assertEquals(sorted,sortedFromMethod);
    }
    @Test
    void groupWordsTest(){
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

        ArrayList<List<Word>> sortedFromMethod = pdfAnalyzer.groupWords(range,unsorted);
        Assert.assertEquals(sorted,sortedFromMethod);
    }
    @Test
    void isInRangeTest(){
        int range = 5;
        int a = 10;
        int b = 12;
        int c = 16;
        Boolean resultOne = pdfAnalyzer.isInRange(range, a, b);
        Boolean resultTwo = pdfAnalyzer.isInRange(range, a, c);

        Assert.assertTrue(resultOne);
        Assert.assertFalse(resultTwo);
    }
    @Test
    void sameWordsTest() {
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

        List<List<Word>> resultat = pdfAnalyzer.sameWords(firstMap, secondMap);
        Assert.assertEquals(output, resultat);
    }
    @Test
    void distWordsTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Rectangle r1 = new Rectangle(0, 0, 2, 4);
        Rectangle r2 = new Rectangle(5, 0, 2, 4);
        Word w1 = new Word("WordOne", 0, r1);
        Word w2 = new Word("WordTwo", 0, r2);

        double distExpected = Math.sqrt(Math.pow(w1.getBoundingBox().getCenterX() - w2.getBoundingBox().getCenterX(), 2)
                + Math.pow(w1.getBoundingBox().getCenterY() - w2.getBoundingBox().getCenterY(), 2));
        double distActual = pdfAnalyzer.distWords(w1,w2);
        Assert.assertEquals(distExpected, distActual, 0);
    }
    @Test
    public void getWordAngleTest() throws InvocationTargetException, IllegalAccessException {
        Rectangle r1 = new Rectangle(0, 0, 2, 4);
        Rectangle r2 = new Rectangle(5, 0, 2, 4);
        Word w1 = new Word("WordOne", 0, r1);
        Word w2 = new Word("WordTwo", 0, r2);

        double ak = w1.getBoundingBox().getCenterX() - w2.getBoundingBox().getCenterX();
        double gk = w1.getBoundingBox().getCenterY() - w2.getBoundingBox().getCenterY();

        double angleExpected = Math.atan(gk)/(ak);
        double angleActual = pdfAnalyzer.getWordAngle(w1,w2);

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
    void makeQuestionsTest() throws InvocationTargetException, IllegalAccessException {
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

        ArrayList<String> result = pdfAnalyzer.makeQuestions(all,gR);
        String first = " WordOne  WordTwo  WordThree ";
        String second = " WordFour ";
        ArrayList<String> expected = new ArrayList<>();
        expected.add(first);
        expected.add(second);
        Assert.assertEquals(expected,result);
    }
    @Test
    void singleWordsTest() throws InvocationTargetException, IllegalAccessException {
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

        HashMap<String, Word> result = pdfAnalyzer.singleWords(all);
        HashMap<String, Word> expected = new HashMap<String, Word>();
        expected.put("WordFour", w4);
        Assert.assertEquals(expected, result);
    }
    @Test
    void calcRotationTest() throws InvocationTargetException, IllegalAccessException {
        int range = 20;
        Rectangle r1 = new Rectangle(0, 0, 2, 4);
        Rectangle r2 = new Rectangle(10, 0, 2, 4);
        Rectangle r3 = new Rectangle(5, 10, 2, 4);

        Word w1 = new Word("WordOne", 95, r1);
        Word w2 = new Word("WordTwo", 95, r2);
        Word w3 = new Word("WordThree", 100, r1);
        Word w4 = new Word("WordFour", 98, r3);
        Word w5 = new Word("WordFive", 94, r1);
        Word w6 = new Word("WordOne", 92, r1);
        Word w7 = new Word("WordTwo", 91, r2);

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

        double result = pdfAnalyzer.calcRotation(null,all);
        double expected = 1.1;
        Assert.assertEquals(expected, result, 0.01);

        listThree.add(w6);
        listThree.add(w7);
        allTwo.add(listThree);
        result = pdfAnalyzer.calcRotation(null,allTwo);
        Assert.assertTrue(Double.isNaN(result));
    }
    @Test
    void prcInitTest() throws Exception {
        init();
        if(runTest){
        pdfAnalyzer = new PDFAnalyzer();
        pdfAnalyzer.prcInitFile(docInit);
        ArrayList<List<Rectangle>> groupedRectanglesExpected = pdfAnalyzer.groupedRectangles;
        List<Word> allWordsExpected = pdfAnalyzer.allWords;
        HashMap<String, Word> uniquWordsExpected = pdfAnalyzer.uniquWords;
        ArrayList<List<Word>> groupedWordsExpected = pdfAnalyzer.groupedWords;
        ArrayList<String> questionsExpected = pdfAnalyzer.questions;

        Rectangle rectangle1 = new Rectangle(1283, 435, 325, 116);
        Rectangle rectangle2 = new Rectangle(1612, 435, 307, 116);
        Rectangle rectangle3 = new Rectangle(1923, 435, 250, 116);
        List<Rectangle> rectangles = new ArrayList<>();
        rectangles.add(rectangle1);
        rectangles.add(rectangle2);
        rectangles.add(rectangle3);
        Assert.assertEquals(groupedRectanglesExpected.get(0), rectangles);
        Assert.assertTrue(groupedRectanglesExpected.size() == 5);

        List<Word> allWords = new ArrayList<>();
        Word word = new Word("Meine", 0, rectangle1);
        Assert.assertEquals(allWordsExpected.get(0).getText(), word.getText());
        Assert.assertTrue(allWordsExpected.size() == 23);

        Assert.assertTrue(uniquWordsExpected.containsKey("Mittel"));
        Assert.assertTrue(uniquWordsExpected.containsKey("Bewertung:"));
        Assert.assertTrue(uniquWordsExpected.size() == 14);

        ArrayList<List<Word>> groupedWords = new ArrayList<>();
        Assert.assertEquals(groupedWordsExpected.get(1).get(0).getText(), "Meine");
        Assert.assertEquals(groupedWordsExpected.get(1).get(1).getText(), "zweite");
        Assert.assertEquals(groupedWordsExpected.get(1).get(2).getText(), "Frage");
        Assert.assertTrue(groupedWordsExpected.size() == 6);

        ArrayList<String> questions = new ArrayList<>();
        Assert.assertEquals(questionsExpected.get(0), " Meine  zweite  Frage ");
        Assert.assertTrue(questionsExpected.size() == 5);

        ArrayList<Question> result = pdfAnalyzer.prcSurveyFile(docPrc);
        Question q1 = new Question(" Meine  zweite  Frage ", new int[] {0, 0, 0} );
        Question q2 = new Question(" Meine  dritte  Frage ", new int[] {0, 0, 0} );
        Question q3 = new Question(" Meine  vierte  Frage ", new int[] {0, 0, 0} );
        Assert.assertEquals(result.get(0).getQuestionText(), q1.getQuestionText());
        Assert.assertEquals(result.get(1).getQuestionText(), q2.getQuestionText());
        Assert.assertEquals(result.get(2).getQuestionText(), q3.getQuestionText());
        }
        else {Assert.assertTrue(true);}
    }
}