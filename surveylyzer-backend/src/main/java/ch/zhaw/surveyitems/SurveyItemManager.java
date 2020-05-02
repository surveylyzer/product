package ch.zhaw.surveyitems;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;

public class SurveyItemManager {

    @Getter
    @Setter
    private ArrayList<SurveyItemAbstract> surveyItems = new ArrayList<>();

    @Getter
    @Setter
    private Object[][] results;

    private SurveyHeader createHeader() {
        return new SurveyHeader("ID", "Question", "Items Length", "first item",
                "second item", "third item", "fourth item", "fith item", "sixth item", "seventh item", "eighth item",
                "ninth item", "tenth item");
    }

    public ArrayList<SurveyItemAbstract> parseResults(Object[][] results) {
        surveyItems.add(createHeader());
        if(results != null) {
            for(int i = 1; i < results.length; i++) {
                String question = String.valueOf(results[i][0]).replace(",", "");
                int evalSize = results[i].length -1;
                int[] eval = new int[evalSize];
                for (int j=0; j<evalSize; j++) {
                    eval[j] = (int) results[i][j+1];
                }
                SurveyItem surveyItem = new SurveyItem(i, question,
                        eval.length, eval);
                surveyItems.add(surveyItem);
            }
        }
        return surveyItems;
    }

    public void clearItems() {
        surveyItems.clear();
    }
}
