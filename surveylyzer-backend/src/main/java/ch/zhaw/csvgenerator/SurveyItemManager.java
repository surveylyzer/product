package ch.zhaw.csvgenerator;

import ch.zhaw.pdffunctionality.PDFAnalyzer;
import ch.zhaw.pdffunctionality.Question;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SurveyItemManager {

    @Getter
    @Setter
    private static List<SurveyItem> surveyItems = new ArrayList<>();


    public static List<SurveyItem> createSurveyItems() {
        ArrayList<Question> questionsList = PDFAnalyzer.questionList;

        SurveyItem header = new SurveyItem(0, "Question", 10, 1, 2,3,4,5,6,7,8,9,10);
        surveyItems.add(header);

        if(questionsList != null) {
            for (int i = 0; i < questionsList.size(); i++) {
                Question question =  questionsList.get(i);
                SurveyItem surveyItem = new SurveyItem(i+1, question.getQuestionText(),
                        question.getEval().length, question.getEval());
                surveyItems.add(surveyItem);
            }
        }
        return surveyItems;
    }
}
