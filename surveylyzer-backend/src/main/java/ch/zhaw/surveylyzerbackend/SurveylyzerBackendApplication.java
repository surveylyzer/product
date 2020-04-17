package ch.zhaw.surveylyzerbackend;

import ch.zhaw.pdffunctionality.PDFAnalyzer;
import ch.zhaw.status.Status;
import ch.zhaw.status.StatusController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@ComponentScan(basePackages = {"ch.zhaw.pdfReceiver","ch.zhaw.resultSender", "ch.zhaw.status"})
public class SurveylyzerBackendApplication {

	private static PDFAnalyzer pdfAnalyzer;
	private static Status status;
	private static StatusController statusController = new StatusController();

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(SurveylyzerBackendApplication.class, args);
		
		//Initiate PDFAnalyzer
		pdfAnalyzer = new PDFAnalyzer();
		HttpEntity<Status> statusResponseEntity = statusController.getStatus();
		System.out.println("Body: "+statusResponseEntity.getBody().isTemplateReceived());
		while(!(statusResponseEntity.getBody().isTemplateReceived()&&statusResponseEntity.getBody().isSurveyReceived())){
			//just wait
			System.out.println("Got Template: "+statusResponseEntity.getBody().isTemplateReceived());
			System.out.println("Got Survey: "+statusResponseEntity.getBody().isSurveyReceived());
			TimeUnit.SECONDS.sleep(3);
		}
		System.out.println("Got all the files starting work");
		pdfAnalyzer.startHighlightingTest();




//    	////pa.startTest();
//    	pa.startHighlightingTest();
//    	// Test end
	}

}
