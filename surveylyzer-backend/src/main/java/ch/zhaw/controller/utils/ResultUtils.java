package ch.zhaw.controller.utils;

import ch.zhaw.pdffunctionality.Question;

import java.util.ArrayList;

public class ResultUtils {

    public static Object[][] getResults(ArrayList<Question> questionList) {

        int objectLength = getObjectLength(questionList) + 1;
        int listLength = questionList.size() + 1;
        Object[][] result = new Object[listLength][objectLength];

        // create header
        result[0][0] = "Question";
        for (int i = 1; i < objectLength; i++) {
            result[0][i] = String.valueOf(i);
        }

        // fill the results list
        for (int i = 1; i < listLength; i++) {
            result[i][0] = questionList.get(i-1).getQuestionText();
            int [] items = questionList.get(i-1).getEval();
            for (int k = 1; k < objectLength; k++) {
                if (items.length < k) {
                    result[i][k] = 0;
                } else {
                    result[i][k] = items[k-1];
                }
            }
        }
        return result;
    }

    private static int getObjectLength(ArrayList<Question> questionList) {
        int max = 0;
        for (Question q : questionList) {
            if (q.getEval() != null) {
                int tmp = q.getEval().length;
                if (tmp > max) {
                    max = tmp;
                }
            }
        }
        return max;
    }
}
