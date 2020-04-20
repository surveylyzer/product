package ch.zhaw.domain;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

// We can use: save(), findOne(), findById(), findAll(), count(), delete(), deleteById()
// ...without implementation!
@Repository
public interface SurveyRepository extends MongoRepository<Survey, String> {
// More supported query methods:
// https://docs.spring.io/spring-data/data-document/docs/current/reference/html/#mongodb.repositories.queries
}
