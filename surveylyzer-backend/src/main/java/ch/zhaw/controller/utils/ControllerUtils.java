package ch.zhaw.controller.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ControllerUtils {

    /**
     * Converts Multipart file into Bytearray
     * @param file
     * @return
     */
    public static byte[] multipartToByteArray(MultipartFile file){
        File pdf = null;
        byte[] pdfData = null;
        try {
            pdf = multipartToFile(file,file.getOriginalFilename());
            pdfData = new byte[(int)pdf.length()];
            FileInputStream in = new FileInputStream(pdf);
            in.read(pdfData);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdfData;

    }

    /**
     * Transforms a multipartFile (spring.io) into file (java.io)
     * @param multipart
     * @param fileName
     * @return
     */
    public static File multipartToFile(MultipartFile multipart, String fileName) {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
        try {
            multipart.transferTo(convFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convFile;
    }
}
