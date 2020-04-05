package ch.zhaw.pdfReceiver;



public class PdfFile {
    private int id;
    private String pdfName;
    private int fileSizeKB;


    public PdfFile(int id, String pdfName, int fileSizeKB){
        this.id = id;
        this.pdfName = pdfName;
        this.fileSizeKB = fileSizeKB;

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

    public int getFileSizeKB() {
        return fileSizeKB;
    }

    public void setFileSizeKB(int fileSize) {
        this.fileSizeKB = fileSize;
    }
}
