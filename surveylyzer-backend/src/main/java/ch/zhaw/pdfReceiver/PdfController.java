package ch.zhaw.pdfReceiver;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<PdfFile> createPDF(@RequestParam("file1") MultipartFile file1) {
        System.out.print("Received File: " + file1);
        write(file1 );
        PdfFile pdfFile = new PdfFile(pdfCounter.incrementAndGet(), file1.getOriginalFilename());
        pdfList.add(pdfFile);
        return new ResponseEntity<>(pdfFile, HttpStatus.CREATED);
    }

    /**
     * Save document as file
     * @param file
     */
    public void write(MultipartFile file) {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString().concat("\\surveylyzer-backend\\pdf_umfragen\\");
        System.out.println("Current relative path is: " + s);

        Path filepath = Paths.get(s, file.getOriginalFilename());

        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
