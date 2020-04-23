package ch.zhaw.workflow;

public class Workflow {

    private boolean pdfAnalyzerStarted;
    private boolean pdfAnalyzerFinished;

    public Workflow(){
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
        this.pdfAnalyzerStarted = workflow.isPdfAnalyzerStarted();
        this.pdfAnalyzerFinished = workflow.isPdfAnalyzerFinished();
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
