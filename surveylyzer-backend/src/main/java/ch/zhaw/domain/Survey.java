package ch.zhaw.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
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
        dataHasChanged();
    }

    private void dataHasChanged() {
        setChangedDate(new Date());
    }

    @PersistenceConstructor
    public Survey(SurveyTemplate surveyTemplate, Object[][] result) {
        this();
        this.setSurveyTemplate(surveyTemplate);
        this.setResult(result);
    }

    public UUID getId() { return id == null ? null : UUID.fromString(id); }

    public void setId(UUID id) {
        this.id = id == null ? null : id.toString();
        dataHasChanged();
    }

    public SurveyTemplate getSurveyTemplate() { return surveyTemplate; }

    public void setSurveyTemplate(SurveyTemplate surveyTemplate) {
        this.surveyTemplate = surveyTemplate;
        dataHasChanged();
    }

    public Object[][] getResult() { return result; }

    public void setResult(Object[][] result) {
        this.result = result;
        dataHasChanged();
    }

    public Date getChangedDate() { return changedDate; }

    protected void setChangedDate(Date changedDate) { this.changedDate = changedDate; }

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

    @Override
    public String toString() {
        return "Survey{" +
                "id='" + id + '\'' +
                ", changedDate=" + changedDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Survey survey = (Survey) o;
        return Objects.equals(id, survey.id) &&
                Objects.equals(surveyTemplate, survey.surveyTemplate) &&
                Arrays.equals(result, survey.result) &&
                Objects.equals(changedDate, survey.changedDate);
    }

    @Override
    public int hashCode() {
        int result1 = Objects.hash(id, surveyTemplate, changedDate);
        result1 = 31 * result1 + Arrays.hashCode(result);
        return result1;
    }
}
