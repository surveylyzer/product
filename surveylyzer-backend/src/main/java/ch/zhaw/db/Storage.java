package ch.zhaw.db;

import ch.zhaw.domain.Survey;
import ch.zhaw.domain.SurveyTemplate;
import org.bson.types.Binary;

import java.util.UUID;

public interface Storage {
    // Read
    Survey getSurveyResultById(String id);

    Survey getSurveyResultById(UUID id);

    SurveyTemplate getSurveyTemplateById(String id);

    SurveyTemplate getSurveyTemplateById(UUID id);

    // Write
    Survey saveOrUpdateSurveyResult(Survey survey);

    SurveyTemplate saveOrUpdateSurveyTemplate(SurveyTemplate surveyTemplate);

    Survey saveNewResult(Object[][] result, UUID templateID);

    Survey saveNewResult(Object[][] result, SurveyTemplate template);

    SurveyTemplate saveNewTemplate(Binary pdf);

    // Clear
    void emptyDatabase();
}
