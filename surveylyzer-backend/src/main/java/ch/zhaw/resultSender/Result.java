package ch.zhaw.resultSender;


import ch.zhaw.csvgenerator.SurveyItemAbstract;
import ch.zhaw.csvgenerator.SurveyItemManager;
import ch.zhaw.pdffunctionality.Question;
import ch.zhaw.surveylyzerbackend.SurveylyzerBackendApplication;
import com.google.gson.Gson;

import java.util.List;

public class Result {
    public String header;
    public int question;
    public int answer;

    public Result(String header, int question, int answer){
        this.header = header;
        this.question = question;
        this.answer = answer;
    }

    public static String getResult(){
        Gson gson = new Gson();
        List<Question> results = SurveylyzerBackendApplication.pdfAnalyzer.getQuestionList();
        return gson.toJson(results);
    }


    public static Result createFakeResult(String header,int questionId, int answerid ){
        return new Result(header,questionId,answerid);
    }


    /**
     * Needed for mock testing the comunication of results
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
