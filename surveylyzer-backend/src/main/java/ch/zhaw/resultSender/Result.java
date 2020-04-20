package ch.zhaw.resultSender;

import ch.zhaw.csvgenerator.SurveyItemAbstract;
import ch.zhaw.csvgenerator.SurveyItemManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Get result data from database or error messages.
 */
public class Result {
    public String header;
    public int question;
    public int answer;

    public Result(String header, int question, int answer) {
        this.header = header;
        this.question = question;
        this.answer = answer;
    }

    public static Object[][] getResult() {
        /*
        Dummy Code:
        Get Result from DB.
        if null --> invalid id / id not found message
        if found but empty result --> still working message
        if found with result --> return result (like getFakeData())

        (Du kannst auch ein anderes Objekt zurückgeben, muss kein Object[][] sein...
        Es gibt für den Controller auch Wrapper-Klassen für HTTP Status Meldungen.
        Z.B. "Spring Boot ResponseEntity" --> Als Inspiration ;-))
        */
        return getFakeData();
//        return null; // "not found"
//        return simulateBusy(); // "found but empty --> result not yet ready"
    }


    private static Object[][] getFakeData() {
        try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) {}
        String[] header = {"City", "1", "2", "3", "4"};
        Object[] question1 = {"Question 1", 23, 47, 2, 5};
        Object[] question2 = {"Question 2", 24, 10, 40, 3};
        Object[] question3 = {"Question 3", 3, 57, 15, 1};
        Object[] question4 = {"Question 4", 333, 57, 15, 1};
        Object[] question5 = {"Question 5", 2, 5, 1, 69};
        Object[][] result = {header, question1, question2, question3, question4, question5};
        return result;
    }

    private static Object[][] simulateBusy(){
        Object[][] busy = {{"$$busy$$"}};
        return busy;
    }


    // TODO: Do we still need this (and if yes, like this - result isn't used...)?

    /**
     * Needed for mock testing the comunication of results
     *
     * @return
     */
    //  public static List<SurveyItemAbstract> createGoogleMockResult() {
    public static List<SurveyItemAbstract> createGoogleMockResult() {
        String[] header = {"City", "1", "2", "3", "4"};
        String[] question1 = {"Question 1", "23", "47", "2", "5"};
        String[] question2 = {"Question 2", "24", "10", "40", "3"};
        String[] question3 = {"Question 3", "3", "57", "15", "1"};
        String[] question4 = {"Question 4", "333", "57", "15", "1"};
        String[] question5 = {"Question 5", "2", "5", "1", "69"};
        String[][] result = {header, question1, question2, question3, question4, question5};
        return SurveyItemManager.createSurveyItemsGoogleCharts();
    }


}
