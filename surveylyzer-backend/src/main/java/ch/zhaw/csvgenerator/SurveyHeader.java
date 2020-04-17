package ch.zhaw.csvgenerator;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.Setter;

public class SurveyHeader extends SurveyItemAbstract {

    @Getter
    @Setter
    @CsvBindByPosition(position = 0)
    private String id;

    @Getter
    @Setter
    @CsvBindByPosition(position = 1)
    private String question;

    @Getter
    @Setter
    @CsvBindByPosition(position = 2)
    private String length;

    @Getter
    @Setter
    @CsvBindByPosition(position = 3)
    private String item_1;

    @Getter
    @Setter
    @CsvBindByPosition(position = 4)
    private String item_2;

    @Getter
    @Setter
    @CsvBindByPosition(position = 5)
    private String item_3;

    @Getter
    @Setter
    @CsvBindByPosition(position = 6)
    private String item_4;

    @Getter
    @Setter
    @CsvBindByPosition(position = 7)
    private String item_5;

    @Getter
    @Setter
    @CsvBindByPosition(position = 8)
    private String item_6;

    @Getter
    @Setter
    @CsvBindByPosition(position = 9)
    private String item_7;

    @Getter
    @Setter
    @CsvBindByPosition(position = 10)
    private String item_8;

    @Getter
    @Setter
    @CsvBindByPosition(position = 11)
    private String item_9;

    @Getter
    @Setter
    @CsvBindByPosition(position = 12)
    private String item_10;

    public SurveyHeader(String id, String question, String length, String ... item) {
        this.id = id;
        this.question = question;
        this.length = length;
        this.item_1 = item[0];
        this.item_2 = item[1];
        this.item_3 = item[2];
        this.item_4 = item[3];
        this.item_5 = item[4];
        this.item_6 = item[5];
        this.item_7 = item[6];
        this.item_8 = item[7];
        this.item_9 = item[8];
        this.item_10 = item[9];
    }
}
