package ch.zhaw.surveylyzerbackend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@ComponentScan(basePackages = {"ch.zhaw"})
@EnableMongoRepositories("ch.zhaw.domain")
public class SurveylyzerBackendApplication {


    public static void main(String[] args) {
        SpringApplication.run(SurveylyzerBackendApplication.class, args);
    }

}
