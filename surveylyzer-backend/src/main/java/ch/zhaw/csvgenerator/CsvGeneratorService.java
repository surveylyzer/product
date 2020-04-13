package ch.zhaw.csvgenerator;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CsvGeneratorService {

    public List<SurveyItem> listSurveyItems() {
//        List<SurveyItem> surveyItems = new ArrayList<>();
//
//        //create dummy survey todo: instead get the array with auswertung
//
//        surveyItems.add(new SurveyItem(0, "Question", "open answer", 10,1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
//        surveyItems.add(new SurveyItem(1, "Question 1", "", 4, 23, 47, 2, 5));
//        surveyItems.add(new SurveyItem(2, "Question 2", "",4, 24, 10, 40, 3));
//        surveyItems.add(new SurveyItem(3, "Question 3", "",4, 3, 57, 15, 1));
//        surveyItems.add(new SurveyItem(4, "Question 4", "",4, 67, 5, 3, 2));
//        surveyItems.add(new SurveyItem(5, "Question 5", "",4, 2, 5, 1, 69));
//        surveyItems.add(new SurveyItem(6, "Question 6", "",2, 80, 0));
//        surveyItems.add(new SurveyItem(7, "Question 7", "",2, 20, 60));
//        surveyItems.add(new SurveyItem(8, "Open question", "some answer... ", 0));

        return SurveyItemManager.createSurveyItems();
    }
}
