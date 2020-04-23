package ch.zhaw.domain;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Document(collection = "template")
public class SurveyTemplate {
    @Id
    private String id;
    private Binary template;
    @Field("changed_date")
    private Date changedDate;

    @PersistenceConstructor
    public SurveyTemplate(Binary template) {
        this.setId(null);
        this.setTemplate(template);
        dataHasChanged();
    }

    private void dataHasChanged() {
        setChangedDate(new Date());
    }

    public UUID getId() { return id == null ? null : UUID.fromString(id); }

    public void setId(UUID id) {
        this.id = id == null ? null : id.toString();
        dataHasChanged();
    }

    public Binary getTemplate() {
        return template;
    }

    public void setTemplate(Binary template) {
        this.template = template;
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

    @Override
    public String toString() {
        return "SurveyTemplate{" +
                "id='" + id + '\'' +
                ", changedDate=" + changedDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyTemplate template1 = (SurveyTemplate) o;
        return Objects.equals(id, template1.id) &&
                Objects.equals(template, template1.template) &&
                Objects.equals(changedDate, template1.changedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, template, changedDate);
    }
}
