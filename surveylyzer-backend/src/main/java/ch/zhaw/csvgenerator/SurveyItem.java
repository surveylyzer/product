package ch.zhaw.csvgenerator;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

public class SurveyItem {


    @Getter
    @Setter
    @CsvBindByName(column = "ID")
    @CsvBindByPosition(position = 0)
    private long id;

    @Getter
    @Setter
    @CsvBindByName(column = "Question")
    @CsvBindByPosition(position = 1)
    private String question;

    @Getter
    @Setter
    @CsvBindByName(column = "First X")
    @CsvBindByPosition(position = 2)
    private int firstItem;

    @Getter
    @Setter
    @CsvBindByName(column = "Second X")
    @CsvBindByPosition(position = 3)
    private int secondItem;

    @Getter
    @Setter
    @CsvBindByName(column = "Third X")
    @CsvBindByPosition(position = 4)
    private int thirdItem;

    @Getter
    @Setter
    @CsvBindByName(column = "Fourth X")
    @CsvBindByPosition(position = 5)
    private int fourthItem;

    @Getter
    @Setter
    @CsvBindByName(column = "Open Answer")
    @CsvBindByPosition(position = 6)
    private String answer;

    public SurveyItem(long id, String question, int firstItem, int secondItem, int thirdItem, int fourthItem, String answer) {
        this.id = id;
        this.question = question;
        this.firstItem = firstItem;
        this.secondItem = secondItem;
        this.thirdItem = thirdItem;
        this.fourthItem = fourthItem;
        this.answer = answer;
    }

    public SurveyItem(long id, String question, int firstItem, int secondItem, int thirdItem, int fourthItem) {
        this.id = id;
        this.question = question;
        this.firstItem = firstItem;
        this.secondItem = secondItem;
        this.thirdItem = thirdItem;
        this.fourthItem = fourthItem;
    }

    public SurveyItem(long id, String question, int firstItem, int secondItem) {
        this.id = id;
        this.question = question;
        this.firstItem = firstItem;
        this.secondItem = secondItem;
    }

    public SurveyItem(long id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

}
