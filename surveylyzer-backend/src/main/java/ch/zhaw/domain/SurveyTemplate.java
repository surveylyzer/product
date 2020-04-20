package ch.zhaw.domain;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.UUID;

//@EnableMongoRepositories
//@EnableAutoConfiguration
@Document(collection = "template")
public class SurveyTemplate {
    @Id
    private String id;
    private Binary template;
    @Field("changed_date")
    private Date changedDate;

    @PersistenceConstructor
    public SurveyTemplate(Binary template) {
        this.setId(UUID.randomUUID());
        setChangedDate(new Date());
        this.setTemplate(template);
    }

    public UUID getId() { return UUID.fromString(id); }
    public void setId(UUID id) { this.id = id.toString(); }

    public Binary getTemplate() {
        return template;
    }
    public void setTemplate(Binary template) {
        this.template = template;
    }

    public Date getChangedDate() { return changedDate; }
    public void setChangedDate(Date changedDate) { this.changedDate = changedDate; }
}
