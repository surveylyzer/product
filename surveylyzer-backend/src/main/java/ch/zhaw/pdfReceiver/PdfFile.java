package ch.zhaw.pdfReceiver;

import java.io.File;

public class PdfFile {
    private int id;
    private String pdfName;
    private File pdfFile;

    public PdfFile(int id, String pdfName){
        this.id = id;
        this.pdfName = pdfName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public File getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(File pdfFile) {
        this.pdfFile = pdfFile;
    }
}
