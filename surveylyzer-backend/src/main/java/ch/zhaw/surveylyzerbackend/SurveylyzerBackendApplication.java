package ch.zhaw.surveylyzerbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ch.zhaw.pdffunctionality.PDFAnalyzer;

@SpringBootApplication
public class SurveylyzerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SurveylyzerBackendApplication.class, args);
		
		//Test the PDF Analyzer
    	PDFAnalyzer pa = new PDFAnalyzer();
    	pa.startTest();
    	// Test end
	}

}