package ch.zhaw.csvgenerator;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CsvGeneratorService {

    public List<SurveyItemAbstract> listSurveyItems() {
        return SurveyItemManager.createSurveyItemsCsv();
    }
}
