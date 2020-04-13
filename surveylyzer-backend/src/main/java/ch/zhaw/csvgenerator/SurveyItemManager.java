package ch.zhaw.csvgenerator;

import ch.zhaw.pdffunctionality.PDFAnalyzer;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.tess4j.Word;

import java.util.ArrayList;
import java.util.List;

public class SurveyItemManager {

    @Getter
    @Setter
    private static List<SurveyItem> surveyItems = new ArrayList<>();

    @Getter
    @Setter
    private static ArrayList<String> questions;

    private static ArrayList<String> createQuestions() {
        ArrayList<List<Word>> groupedWords = PDFAnalyzer.groupedWords;
        questions = new ArrayList<>();

        for (List<Word> sentence : groupedWords) {
            String question = "";
            for (Word word : sentence) {
                question = question.concat(word.getText() + " ");
            }
            questions.add(question);
        }
        return questions;
    }

    public static List<SurveyItem> createSurveyItems() {
        List<int[]> evaluationResults = PDFAnalyzer.evaluationResults;
        ArrayList<String> questionsList = createQuestions();

        SurveyItem header = new SurveyItem(0, "Question", "Open answer", 10, 1, 2,3,4,5,6,7,8,9,10);
        surveyItems.add(header);

        for (int i = 0; i < evaluationResults.size(); i++) {
            String question = "";
            if (questionsList.get(i) != null) {
                question = questionsList.get(i);
            }
            SurveyItem surveyItem = new SurveyItem(i, question, "", evaluationResults.get(i).length, evaluationResults.get(i));
            surveyItems.add(surveyItem);
        }
        return surveyItems;
    }
}
