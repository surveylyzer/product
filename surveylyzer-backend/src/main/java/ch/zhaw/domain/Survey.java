package ch.zhaw.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.UUID;

@Document(collection = "survey")
public class Survey {

    @Id
    private String id;
    @DBRef
    private SurveyTemplate surveyTemplate;
    private Object[][] result;
    @Field("changed_date")
    private Date changedDate;

    @PersistenceConstructor
    public Survey() {
        setId(null);
        setChangedDate(new Date());
    }

    @PersistenceConstructor
    public Survey(SurveyTemplate surveyTemplate, Object[][] result) {
        this();
        this.setSurveyTemplate(surveyTemplate);
        this.setResult(result);
    }

    public UUID getId() { return id == null ? null : UUID.fromString(id); }

    public void setId(UUID id) { this.id = id == null ? null : id.toString(); }

    public SurveyTemplate getSurveyTemplate() { return surveyTemplate; }

    public void setSurveyTemplate(SurveyTemplate surveyTemplate) { this.surveyTemplate = surveyTemplate; }

    public Object[][] getResult() { return result; }

    public void setResult(Object[][] result) { this.result = result; }

    public Date getChangedDate() { return changedDate; }

    public void setChangedDate(Date changedDate) { this.changedDate = changedDate; }

    /**
     * Generates and sets a new id if current id is null
     */
    public void setNewIdIfNull() {
        if (getId() == null) {
            setId(UUID.randomUUID());
        }
    }

    /**
     * Get id form template
     *
     * @return
     */
    public UUID getTemplateId() {
        SurveyTemplate t = getSurveyTemplate();
        return t == null ? null : t.getId();
    }
}
