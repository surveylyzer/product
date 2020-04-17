package ch.zhaw.workflow;

public class Workflow {

    private String templateName;
    private boolean templateReceived;
    private String surveyName;
    private boolean surveyReceived;
    private boolean pdfAnalyzerStarted;
    private boolean pdfAnalyzerFinished;

    public Workflow(){
        this.templateReceived = false;
        this.surveyReceived = false;
        this.pdfAnalyzerStarted = false;
        this.pdfAnalyzerFinished = false;
    }

    public void updateWorkflow(Workflow workflow){
        this.templateName = workflow.templateName;
        this.templateReceived = workflow.isTemplateReceived()||this.templateReceived;
        this.surveyName = workflow.surveyName;
        this.surveyReceived = workflow.isSurveyReceived()||this.surveyReceived;
        this.pdfAnalyzerStarted = workflow.isPdfAnalyzerStarted()||this.pdfAnalyzerStarted;
        this.pdfAnalyzerFinished = workflow.isPdfAnalyzerFinished()||this.pdfAnalyzerFinished;
    }

    public boolean isTemplateReceived() {
        return templateReceived;
    }

    public void setTemplateReceived(boolean templateReceived) {
        this.templateReceived = templateReceived;
    }

    public boolean isSurveyReceived() {
        return surveyReceived;
    }

    public void setSurveyReceived(boolean surveyReceived) {
        this.surveyReceived = surveyReceived;
    }

    public boolean isPdfAnalyzerStarted() {
        return pdfAnalyzerStarted;
    }

    public void setPdfAnalyzerStarted(boolean pdfAnalyzerStarted) {
        this.pdfAnalyzerStarted = pdfAnalyzerStarted;
    }

    public boolean isPdfAnalyzerFinished() {
        return pdfAnalyzerFinished;
    }

    public void setPdfAnalyzerFinished(boolean pdfAnalyzerFinished) {
        this.pdfAnalyzerFinished = pdfAnalyzerFinished;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }
}
