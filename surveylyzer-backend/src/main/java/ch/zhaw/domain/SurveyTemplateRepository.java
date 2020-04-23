package ch.zhaw.domain;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyTemplateRepository extends MongoRepository<SurveyTemplate, String> {
// We can use: save(), findOne(), findById(), findAll(), count(), delete(), deleteById()
// ...without implementation!
// More supported query methods:
// https://docs.spring.io/spring-data/data-document/docs/current/reference/html/#mongodb.repositories.queries
}
