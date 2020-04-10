package ch.zhaw.resultSender;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Result {
    public String header;
    public int question;
    public int answer;

    public Result(String header, int question, int answer){
        this.header = header;
        this.question = question;
        this.answer = answer;
    }

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

    public static String createJson(String[][] result){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(result);
        return json;
    }


    public static String fakeGoogleResult(){
        String[][] result = createGoogleMockResult();
        return createJson(result);
    }

    public static String fakeResult(){
        Result result = new Result("Speaker",1,4);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(result);
        return json;
    }

}
