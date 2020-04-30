//package ch.zhaw.csvgenerator;
//
//import ch.zhaw.pdffunctionality.PDFAnalyzer;
//import ch.zhaw.pdffunctionality.Question;
//import ch.zhaw.surveylyzerbackend.SurveylyzerBackendApplication;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SurveyItemManager {
//
//    @Getter
//    @Setter
//    private static List<SurveyItemAbstract> surveyItems = new ArrayList<>();
//
//    @Getter
//    @Setter
//    private static List<SurveyItemAbstract> surveyData = new ArrayList<>();
//
//
//    public static List<SurveyItemAbstract> createSurveyItemsCsv() {
//        ArrayList<Question> questionsList = SurveylyzerBackendApplication.pdfAnalyzer.getQuestionList();
//
//        surveyItems.add(createHeader());
//
//        if(questionsList != null) {
//            for (int i = 0; i < questionsList.size(); i++) {
//                Question question =  questionsList.get(i);
//                String questionText = question.getQuestionText().replace(",", "");
//                SurveyItem surveyItem = new SurveyItem(i+1, questionText,
//                        question.getEval().length, question.getEval());
//                surveyItems.add(surveyItem);
//            }
//        }
//        return surveyItems;
//    }
//
//    public static List<SurveyItemAbstract> createSurveyItemsGoogleCharts() {
//        ArrayList<Question> questionsList = SurveylyzerBackendApplication.pdfAnalyzer.getQuestionList();
//        surveyData.add(createHeaderCharts());
//
//        if(questionsList != null) {
//
//            for (int i = 0; i < questionsList.size(); i++) {
//                Question question =  questionsList.get(i);
//                SurveyItem surveyItem = new SurveyItem(question.getQuestionText(),question.getEval());
//                surveyData.add(surveyItem);
//            }
//        }
//        return surveyData;
//    }
//
//    public static void clearList() {
//        surveyItems.clear();
//    }
//
//    public static void clearDataList() {
//        surveyData.clear();
//    }
//
//    private static SurveyHeader createHeaderCharts() {
//        return new SurveyHeader("Question", "first item",
//                "second item", "third item", "fourth item", "fith item", "sixth item", "seventh item", "eighth item",
//                "ninth item", "tenth item");
//    }
//
//    private static SurveyHeader createHeader() {
//        return new SurveyHeader("ID", "Question", "Items Length", "first item",
//                "second item", "third item", "fourth item", "fith item", "sixth item", "seventh item", "eighth item",
//                "ninth item", "tenth item");
//    }
//}
