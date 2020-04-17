package ch.zhaw.workflow;

public class Workflow {

    private String templateName;
    private boolean templateReceived;
    private String surveyName;
    private boolean surveyReceived;
    private boolean pdfAnalyzerStarted;
    private boolean pdfAnalyzerFinished;

    public Workflow(){
        this.templateName = "";
        this.templateReceived = false;
        this.surveyName = "";
        this.surveyReceived = false;
        this.pdfAnalyzerStarted = false;
        this.pdfAnalyzerFinished = false;
    }

    /**
     * Update workflow
     * String -> only if contains actual content
     * boolean -> keep Status if already set
     * @param workflow
     */
    public void updateWorkflow(Workflow workflow){
        if (!workflow.getTemplateName().isEmpty()){
            this.templateName = workflow.getTemplateName();
        }
        this.templateReceived = workflow.isTemplateReceived()||this.templateReceived;
        if (!workflow.getSurveyName().isEmpty()){
            this.surveyName= workflow.getSurveyName();
        }
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
