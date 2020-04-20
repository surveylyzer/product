package ch.zhaw.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SurveyTemplateRepository extends MongoRepository<SurveyTemplate, String> {
}
