package ch.zhaw.db;

import ch.zhaw.domain.Survey;
import ch.zhaw.domain.SurveyRepository;
import ch.zhaw.domain.SurveyTemplate;
import ch.zhaw.domain.SurveyTemplateRepository;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Functionality for storing and retrieving data from database.
 */
@Configurable
@Component(value = "storageImpl")
public class StorageImpl implements Storage {

    @Autowired
    private SurveyRepository surveyRepo;
    @Autowired
    private SurveyTemplateRepository templateRepo;

    /**
     * Returns the Survey with this id or null if not found.
     * Note: Survey contains relation to its template...
     *
     * @param id
     * @return survey
     */
    @Override
    public Survey getSurveyResultById(String id) {
        if (id == null) { return null; }
        return surveyRepo.findById(id).orElse(null);
    }

    /**
     * Returns the Survey with this id or null if not found.
     * Note: Survey contains relation to its template...
     *
     * @param id
     * @return survey
     */
    @Override
    public Survey getSurveyResultById(UUID id) {
        if (id == null) { return null; }
        return getSurveyResultById(id.toString());
    }

    /**
     * Returns the SurveyTemplate with this id or null if not found.
     *
     * @param id
     * @return template
     */
    @Override
    public SurveyTemplate getSurveyTemplateById(String id) {
        if (id == null) { return null; }
        return templateRepo.findById(id).orElse(null);
    }

    /**
     * Returns the SurveyTemplate with this id or null if not found.
     *
     * @param id
     * @return template
     */
    @Override
    public SurveyTemplate getSurveyTemplateById(UUID id) {
        if (id == null) { return null; }
        return getSurveyTemplateById(id.toString());
    }

    /**
     * Saves new Survey or updates existing.
     * If the template does not already exists, it will save the new template too.
     *
     * @param survey
     * @return id when successfully saved or null
     */
    @Override
    public Survey saveOrUpdateSurveyResult(Survey survey) {
        if (survey == null || survey.getSurveyTemplate() == null) {return null;}
        Survey res = null;
        try {
            survey.setSurveyTemplate(saveOrUpdateSurveyTemplate(survey.getSurveyTemplate()));
            if (survey.getSurveyTemplate() != null) {
                res = surveyRepo.save(survey);
            }
        } catch (Exception e) { e.printStackTrace();}
        return res;
    }

    /**
     * Saves new SurveyTemplate or updates existing.
     *
     * @param surveyTemplate
     * @return id when successfully saved or null
     */
    @Override
    public SurveyTemplate saveOrUpdateSurveyTemplate(SurveyTemplate surveyTemplate) {
        if (surveyTemplate == null) {return null;}
        surveyTemplate.setNewIdIfNull();
        SurveyTemplate res = null;
        try {
            res = templateRepo.save(surveyTemplate);
        } catch (Exception e) { e.printStackTrace();}
        return res;
    }

    /**
     * Creates new Survey object and saves it.
     *
     * @param title
     * @param result
     * @return survey when successfully saved or null
     */
    @Override
    public Survey saveNewResult(String title, Object[][] result, UUID templateID) {
        if (templateID == null) {return null;}
        SurveyTemplate template = templateRepo.findById(templateID.toString()).orElse(null);
        if (template == null) {return null;}
        return saveNewResult(title, result, template);
    }

    /**
     * Creates new Survey object and saves it with template.
     * If template isn't already in db, it will save the new template too.
     *
     * @param title
     * @param result
     * @param template
     * @return
     */
    @Override
    public Survey saveNewResult(String title, Object[][] result, SurveyTemplate template) {
        if (template == null) {return null;}
        Survey res = new Survey(title, template, result);
        res.setNewIdIfNull();
        try {
            res.setSurveyTemplate(saveOrUpdateSurveyTemplate(template));
            if (res.getSurveyTemplate() != null) {
                res = surveyRepo.save(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = null;
        }
        return res;
    }

    /**
     * Creates new SurveyTemplate object and saves it.
     *
     * @param pdf
     * @return template when successfully saved or null
     */
    @Override
    public SurveyTemplate saveNewTemplate(Binary pdf) {
        if (pdf == null) {return null;}
        SurveyTemplate res = new SurveyTemplate(pdf);
        res.setNewIdIfNull();
        try {
            res = templateRepo.save(res);
        } catch (Exception e) {
            e.printStackTrace();
            res = null;
        }
        return res;
    }

    /**
     * Resets databse and truncates all tables
     */
    @Override
    public void emptyDatabase() {
        surveyRepo.deleteAll();
        templateRepo.deleteAll();
    }
}
