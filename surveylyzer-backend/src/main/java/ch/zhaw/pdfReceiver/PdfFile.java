package ch.zhaw.pdfReceiver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PdfFile {
    private int id;
    private String pdfName;
    private int fileSizeKB;
    private String uploadDate;

    private static SimpleDateFormat uploadDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static Calendar calendar = Calendar.getInstance();;



    public PdfFile(int id, String pdfName, int fileSizeKB){
        this.id = id;
        this.pdfName = pdfName;
        this.fileSizeKB = fileSizeKB;
        uploadDate = uploadDateFormat.format(calendar.getTime());
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

    public String getUploadDate() {
        return uploadDate;
    }
}
