package ch.zhaw.resultSender;

public class Result {

    /**
     * Needed for mock testing the comunication of results
     * @return
     */
    public static String[][] createGoogleMockResult() {
        String[] header = {"City", "1", "2", "3", "4"};
        String[] question1 = {"Question 1", "23", "47", "2", "5"};
        String[] question2 = {"Question 2", "24", "10", "40", "3"};
        String[] question3 = {"Question 3", "3", "57", "15", "1"};
        String[] question4 = {"Question 4", "67", "5", "3", "2"};
        String[] question5 = {"Question 5", "2", "5", "1", "69"};
        String[][] result = {header, question1, question2, question3, question4, question5};
        return result;
    }

}
