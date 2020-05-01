package ch.zhaw.surveyitems;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

public class SurveyItem extends SurveyItemAbstract {

    @Getter
    @Setter
    @CsvBindByPosition(position = 0)
    private long id;

    @Getter
    @Setter
    @CsvBindByName(column = "Question")
    @CsvBindByPosition(position = 1)
    private String question;

    @Getter
    @Setter
    @CsvBindByPosition(position = 2)
    private int length;

    @Getter
    @Setter
    @CsvBindByPosition(position = 3)
    private int item_1;

    @Getter
    @Setter
    @CsvBindByPosition(position = 4)
    private int item_2;

    @Getter
    @Setter
    @CsvBindByPosition(position = 5)
    private int item_3;

    @Getter
    @Setter
    @CsvBindByPosition(position = 6)
    private int item_4;

    @Getter
    @Setter
    @CsvBindByPosition(position = 7)
    private int item_5;

    @Getter
    @Setter
    @CsvBindByPosition(position = 8)
    private int item_6;

    @Getter
    @Setter
    @CsvBindByPosition(position = 9)
    private int item_7;

    @Getter
    @Setter
    @CsvBindByPosition(position = 10)
    private int item_8;

    @Getter
    @Setter
    @CsvBindByPosition(position = 11)
    private int item_9;

    @Getter
    @Setter
    @CsvBindByPosition(position = 12)
    private int item_10;

    public SurveyItem(long id, String question, int length, int ... item) {
        this.id = id;
        this.question = question;
        this.length = length;
        setItems(item);
    }

    public SurveyItem(String question, int ... item) {
        this.question = question;
        setItems(item);
    }

    private void setItems(int ... item) {
        if (length == 0) {
            set0items();
        } else if (length == 1) {
            set1items(item);
        } else if (length == 2) {
            set2items(item);
        }else if (length == 3) {
            set3items(item);
        }else if (length == 4) {
            set4items(item);
        }else if (length == 5) {
            set5items(item);
        }else if (length == 6) {
            set6items(item);
        }else if (length == 7) {
            set7items(item);
        }else if (length == 8) {
            set8items(item);
        }else if (length == 9) {
            set9items(item);
        }else  {
            set10items(item);
        }
    }

    private void set0items() {
        this.item_1 = 0;
        this.item_2 = 0;
        this.item_3 = 0;
        this.item_4 = 0;
        this.item_5 = 0;
        setLast5itemsToNull();
    }

    private void set1items(int ... item) {
        this.item_1 = item[0];
        this.item_2 = 0;
        this.item_3 = 0;
        this.item_4 = 0;
        this.item_5 = 0;
        setLast5itemsToNull();
    }

    private void set2items(int ... item) {
        this.item_1 = item[0];
        this.item_2 = item[1];
        this.item_3 = 0;
        this.item_4 = 0;
        this.item_5 = 0;
        setLast5itemsToNull();
    }

    private void set3items(int ... item) {
        this.item_1 = item[0];
        this.item_2 = item[1];
        this.item_3 = item[2];
        this.item_4 = 0;
        this.item_5 = 0;
        setLast5itemsToNull();
    }

    private void set4items(int ... item) {
        this.item_1 = item[0];
        this.item_2 = item[1];
        this.item_3 = item[2];
        this.item_4 = item[3];
        this.item_5 = 0;
        setLast5itemsToNull();
    }

    private void set5items(int ... item) {
        setForst5items(item);
        setLast5itemsToNull();
    }

    private void setLast5itemsToNull() {
        this.item_6 = 0;
        this.item_7 = 0;
        this.item_8 = 0;
        this.item_9 = 0;
        this.item_10 = 0;
    }

    private void setForst5items(int ... item) {
        this.item_1 = item[0];
        this.item_2 = item[1];
        this.item_3 = item[2];
        this.item_4 = item[3];
        this.item_5 = item[4];
    }

    private void set6items(int ... item) {
        setForst5items(item);
        this.item_6 = item[5];
        this.item_7 = 0;
        this.item_8 = 0;
        this.item_9 = 0;
        this.item_10 = 0;
    }

    private void set7items(int ... item) {
        setForst5items(item);
        this.item_6 = item[5];
        this.item_7 = item[6];
        this.item_8 = 0;
        this.item_9 = 0;
        this.item_10 = 0;
    }

    private void set8items(int ... item) {
        setForst5items(item);
        this.item_6 = item[5];
        this.item_7 = item[6];
        this.item_8 = item[7];
        this.item_9 = 0;
        this.item_10 = 0;
    }

    private void set9items(int ... item) {
        setForst5items(item);
        this.item_6 = item[5];
        this.item_7 = item[6];
        this.item_8 = item[7];
        this.item_9 = item[8];
        this.item_10 = 0;
    }

    private void set10items(int ... item) {
        setForst5items(item);
        this.item_6 = item[5];
        this.item_7 = item[6];
        this.item_8 = item[7];
        this.item_9 = item[8];
        this.item_10 = item[9];
    }

}
