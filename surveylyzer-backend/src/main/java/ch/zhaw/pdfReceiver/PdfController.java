package ch.zhaw.pdfReceiver;


import ch.zhaw.pdffunctionality.Util;
import net.sourceforge.tess4j.Tesseract;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/pdf")
public class PdfController {


    private static List<PdfFile> pdfList = new ArrayList<>();
    private final AtomicInteger pdfCounter = new AtomicInteger();
    private static final double KBFACTOR = 0.00095367432;
    private static String initPathTemplate  = "";
    private static String initPathSurvey  = "";
    private static String initPathDestination = "";

    private static void init() {
        Path currentRelativePath = Paths.get("");
        if (Util.isOS()) {
            initPathTemplate =  currentRelativePath.toAbsolutePath().toString().concat("/surveylyzer-backend/pdf_umfragen/pdf_template/");
            initPathSurvey =    currentRelativePath.toAbsolutePath().toString().concat("/surveylyzer-backend/pdf_umfragen/pdf_survey/");
            initPathDestination = currentRelativePath.toAbsolutePath().toString().concat("/surveylyzer-backend/pdf_umfragen/");
        } else {
            initPathTemplate =  currentRelativePath.toAbsolutePath().toString().concat("\\surveylyzer-backend\\pdf_umfragen\\pdf_template\\");
            initPathSurvey =    currentRelativePath.toAbsolutePath().toString().concat("\\surveylyzer-backend\\pdf_umfragen\\pdf_survey\\");
            initPathDestination = currentRelativePath.toAbsolutePath().toString().concat("\\surveylyzer-backend\\pdf_umfragen\\");
        }
        initExcption();
    }
    
    private static void initExcption() {
        Path currentRelativePath = Paths.get("");
        initPathTemplate =  currentRelativePath.toAbsolutePath().toString().concat("\\pdf_umfragen\\pdf_template\\");
        initPathSurvey =    currentRelativePath.toAbsolutePath().toString().concat("\\pdf_umfragen\\pdf_survey\\");
        initPathDestination = currentRelativePath.toAbsolutePath().toString().concat("\\pdf_umfragen\\");
    	
    }

    /**
     * GET METHOD -> Get static List of received PDFS
     * @return
     */
    @GetMapping
    public ResponseEntity<List<PdfFile>> getPDFs() {
        return new ResponseEntity<>(pdfList, HttpStatus.OK);
    }

    /**
     * POST METHOD -> Create new PDF File
     */
    @PostMapping()
    public ResponseEntity<PdfFile> createPDF(@RequestParam("file1") MultipartFile file1,@RequestParam("pdfType") String pdfType) {
        System.out.println("Received File: " + file1+" has type: "+pdfType);
        forwardMultipartFileToAnalyzer(file1, pdfType);
        //get Size and convert to mb
        int fileSizeKB = (int)(file1.getSize() * KBFACTOR);
        PdfFile pdfFile = new PdfFile(pdfCounter.incrementAndGet(), file1.getOriginalFilename(), fileSizeKB);
        pdfList.add(pdfFile);
        return new ResponseEntity<>(pdfFile, HttpStatus.CREATED);
    }


    /**
     * Save document as file
     * @param file
     */
    public void forwardMultipartFileToAnalyzer(MultipartFile file, String pdfType) {
        init();
        Path currentRelativePath = Paths.get("");
        String s = "";
        if(pdfType.equalsIgnoreCase("templateFile")){
            s = initPathTemplate;
        } else if (pdfType.equalsIgnoreCase("dataFile")){
            s = initPathSurvey;
        }
        Path filepath = Paths.get(s, file.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(file.getBytes());
        } catch (IOException e) {
        	
            e.printStackTrace();
        }
    }

    /**
     * Encodes a received File into a Base64 String in order to
     * be saved in database
     * @param file
     * @return
     * @throws IOException
     */
    public static String encodeFileToBase64(MultipartFile file) throws IOException {
        Base64 base64 = new Base64();
        String encodedString = new String(base64.encode(file.getBytes()));
        return encodedString;
    }

    /**
     * Transforms a Base64 encoded String into file and writes it to Analyzer
     * input channel
     * @param encodedString
     * @param filename
     * @throws IOException
     */
    public static void forwardBase64EncodedStringToAnalyzer(String encodedString, String filename) throws IOException {
        Base64 base64 = new Base64();
        byte[] decodedBytes = base64.decode(encodedString.getBytes());
        String path = getDestinationPath(filename);
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(decodedBytes);
        fos.flush();
        fos.close();
    }

    /**
     * Gets Destination Path
     * @param filename
     * @return
     */
    public static String getDestinationPath(String filename){
        init();
        String path = initPathDestination;
        String filepath = path+filename;
        return  filepath;
    }
}
