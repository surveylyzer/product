package ch.zhaw.surveylyzerbackend;

import ch.zhaw.pdffunctionality.PDFAnalyzer;
import ch.zhaw.workflow.Workflow;
import ch.zhaw.workflow.WorkflowController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@ComponentScan(basePackages = {"ch.zhaw.pdfReceiver","ch.zhaw.resultSender", "ch.zhaw.workflow"})
public class SurveylyzerBackendApplication {

	private static PDFAnalyzer pdfAnalyzer;
	private static WorkflowController workflowController = new WorkflowController();


	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(SurveylyzerBackendApplication.class, args);
		
		//Initiate PDFAnalyzer
		pdfAnalyzer = new PDFAnalyzer();
		HttpEntity<Workflow> workflowResponseEntity = workflowController.getWorkflow();
		Workflow workflow= workflowResponseEntity.getBody();
		while(!(workflow.isTemplateReceived()&& workflow.isSurveyReceived())){
			System.out.println("Got Template: "+workflowResponseEntity.getBody().isTemplateReceived());
			System.out.println("Got Survey: "+workflowResponseEntity.getBody().isSurveyReceived());
			TimeUnit.SECONDS.sleep(3);
		}
		System.out.println("Got all the files -> starting to analyze");
		pdfAnalyzer.startHighlightingExternalFile(workflow.getTemplateName(), workflow.getSurveyName());
	}

}
