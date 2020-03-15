package ch.zhaw.surveylyzerbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"ch.zhaw"})
public class SurveylyzerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SurveylyzerBackendApplication.class, args);
	}

}
