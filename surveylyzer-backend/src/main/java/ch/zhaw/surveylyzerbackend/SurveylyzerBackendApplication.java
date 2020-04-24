package ch.zhaw.surveylyzerbackend;

import ch.zhaw.domain.Survey;
import ch.zhaw.domain.SurveyTemplate;
import ch.zhaw.pdffunctionality.PDFAnalyzer;
import ch.zhaw.results.ResultController;
import ch.zhaw.workflow.Workflow;
import ch.zhaw.workflow.WorkflowController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpEntity;

import java.io.*;
import java.util.UUID;

@SpringBootApplication
//@ComponentScan(basePackages = {"ch.zhaw.pdfReceiver","ch.zhaw.results", "ch.zhaw.workflow", "ch.zhaw.csvgenerator"})
@ComponentScan(basePackages = {"ch.zhaw"})
@EnableMongoRepositories("ch.zhaw.domain")
public class SurveylyzerBackendApplication {

    public static PDFAnalyzer pdfAnalyzer;
    public static UUID surveyId;
    public static Object[][] results;
    private static WorkflowController workflowController = new WorkflowController();
    private static HttpEntity<Workflow> workflowResponseEntity = workflowController.getWorkflow();
    private static Workflow workflow = workflowResponseEntity.getBody();


    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(SurveylyzerBackendApplication.class, args);
    }

    public static boolean startAnalzyerIfNotBusy(Survey survey, File dataFile){
        //check if Analyzer is busy
        if(workflow.isPdfAnalyzerStarted()){
            System.out.println("Currently busy -> Handling to be implemented");
            return false;
        }
        else {
            if (survey != null) {
                surveyId = survey.getId();
            }
            startAnalyzer(survey, dataFile);
            return true;
        }
    }

    public static void startAnalyzer(Survey survey, File datafile){
        //Analyzer not busy -> set states
        workflow.setPdfAnalyzerFinished(false);
        workflow.setPdfAnalyzerStarted(true);

        //start analyzing
        pdfAnalyzer = new PDFAnalyzer();
        //pdfAnalyzer.startHighlightingTest();
        File templateFile = getTemplateFile(survey);
        results = pdfAnalyzer.startHighlightingExternalFile(templateFile, datafile);
        // todo: find solution to initialise data base correctly at this point within resultController, at the moment it throws Null Pointer Exception
//        ResultController resultController = new ResultController();
//        resultController.saveResult(surveyId, results);

        //Analzer finished reset state
        workflow.setPdfAnalyzerFinished(true);
        workflow.setPdfAnalyzerStarted(false);
    }

    //Todo Check if create empty file really correct
    public static File getTemplateFile(Survey survey)  {
        SurveyTemplate surveyTemplate = survey.getSurveyTemplate();
        File templateFile = new File((System.getProperty("java.io.tmpdir")+"/templateFile"));
        try{
            OutputStream os = new FileOutputStream(templateFile);
            os.write(surveyTemplate.getTemplate().getData());
        } catch(IOException e){
            e.printStackTrace();
        }
        return templateFile;
    }

}
