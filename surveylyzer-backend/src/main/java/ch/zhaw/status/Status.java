package ch.zhaw.status;

public class Status {

    private boolean templateReceived;
    private boolean surveyReceived;
    private boolean pdfAnalyzerStarted;
    private boolean pdfAnalyzerFinished;

    public Status(){
        this.templateReceived = false;
        this.surveyReceived = false;
        this.pdfAnalyzerStarted = false;
        this.pdfAnalyzerFinished = false;
    }

    public void updateStatus(Status status){
        this.templateReceived = status.isTemplateReceived()||this.templateReceived;
        this.surveyReceived = status.isSurveyReceived()||this.surveyReceived;
        this.pdfAnalyzerStarted = status.isPdfAnalyzerStarted()||this.pdfAnalyzerStarted;
        this.pdfAnalyzerFinished = status.isPdfAnalyzerFinished()||this.pdfAnalyzerFinished;
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

}
