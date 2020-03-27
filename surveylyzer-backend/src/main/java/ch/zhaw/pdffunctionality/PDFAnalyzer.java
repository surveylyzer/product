package ch.zhaw.pdffunctionality;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Word;

public class PDFAnalyzer {

	private HashMap<String, Integer> sentence;
	// https://stackabuse.com/tesseract-simple-java-optical-character-recognition/
	private Tesseract t;
	private boolean debugen = false;
	private String initPath;
	
	List<Rectangle> mazeRectangles;
	BufferedImage searchThroug;
	public PDFAnalyzer() {
		init();
	}

	private void init() {
		sentence = new HashMap<String, Integer>();
		mazeRectangles = new ArrayList<Rectangle>();
		// Initalisierung vom OCR-Tesseract
		t = new Tesseract();
		if (Util.isOS()) {
			initPath = "surveylyzer-backend/";
		   t.setDatapath("surveylyzer-backend/tess/tessdata/");
		} else {
			initPath = "../surveylyzer-backend/";
		   t.setDatapath("../surveylyzer-backend/tess/tessdata/");
		}
	}
	/**
	 * Initialisierung des Wort-Vektors für die Ausrichtung
	 * @param word
	 * @param distance
	 */
	public void setWordAlignment (String word[], int distance[]) {
		if(word.length != distance.length) {
			return;
		}
		for(int i = 0; i < word.length;i++) {
			sentence.put(word[i], distance[i]);
		}
	}
	/**
	 * Starten vom Test.
	 * Zunächst wird der Wort-Vektor initalisiert, welche für die Ausrichtung der Seiten benötigt wird.
	 * Anschliessend wird das PDF-File geladen
	 * @return
	 */
	public boolean startTest() {
		debugen = true;
		// ACHTUNG: f�r die Ausrichtung m�ssen die W�rter die selbe h�he haben
		// gut: talk vs understandable
		// schlecht: talk vs. was
		/*
		 * y = -10 und x jeweils: x Ziel = 520+614 = 1'134 understandable:520
		 */
		String word[] = new String[] {"talk","held","clear","and","understandable"};
		int distance[] = new int[] {842,742,632,570,520};
		setWordAlignment(word,distance);

		File file = new File(initPath+"pdf_umfragen/ScanBewertungen2.pdf");
//		File file = new File("../surveylyzer-backend/pdf_umfragen/ScanBewertungen2.pdf");
		
		
		try {
			PDDocument document = PDDocument.load(file);
			try {
				System.out.println(Arrays.deepToString(analyzeFile(document)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			document.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("lokales PDF konnten nicht gefunden werden.");
			return false;
		}
		return true;
	}
	/**
	 * Analyse des Dokuements
	 * Bisher findet eine statische Analyse statt. Dies bedeutet, dass das PDF im Ordner pdf_umfragen analysiert wird.
	 * Hierbei wird zurzeit Statisch im Code folgendes hinterlegt:
	 * - Anzahl Tabellen und deren Grössen/Positionen zueinandern.
	 * - Anzahl Fragen und Antwortmöglichkeiten
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public int[][] analyzeFile(PDDocument doc) throws Exception{
		PDFRenderer renderer = new PDFRenderer(doc);
		ArrayList<Integer> auswertung = new ArrayList<Integer>();
		int [][] evaluation = new int [3*4][4]; //Zuerzeit Statisch - 12 Fragen, mit je 4 Antowrtmöglichkeiten
		for(int xx = 0; xx< doc.getNumberOfPages();xx++){
//		for (int xx = 0; xx < 2; xx++) {

			// BufferedImage image = renderer.renderImageWithDPI(xx, 300);
			BufferedImage image = renderer.renderImage(xx, 4);
			int level = 3;

			List<Word> w = t.getWords(image, level);
			// Koordinaten von 2 W�rter abspeichern f�r die Ausrichtung
			int x1 = 0;
			int x2 = 0;
			int x2_add = 0;
			int y1 = 0;
			int y2 = 0;
			for (int i = 0; i < w.size(); i++) {
				Word word = w.get(i);
				if (sentence.containsKey(word.getText())) {
					Rectangle rect = word.getBoundingBox();
					if (x1 == 0) {// first word
						x1 = rect.x;
						y1 = rect.y;

						if(debugen){
						System.out.println(
								word.getBoundingBox().x + "/" + word.getBoundingBox().y + " " + word.getText());
						}
					} else if (x2 == 0) {// second word
						x2 = rect.x;
						x2_add = sentence.get(word.getText());
						y2 = rect.y;

						if(debugen){
						System.out.println(
								word.getBoundingBox().x + "/" + word.getBoundingBox().y + " " + word.getText());
						}
					} else {
						// wir haben bereits 2 w�rter f�r die orientierung
						break;
					}
				}
			}
			if(debugen){
				System.out.println("Differenz: " + (x2 - x1) + "/" + (y2 - y1));
				System.out.println("x2: " + x2 + "/" + y2);
			}
			try {

				BufferedImage rotated_image = rotate(image, getAngle(x2 - x1, y2 - y1), x1, y1);
				// Tabellenbreite = 294(4 Spalten)
				// Tabellenh�he = 197 (5 Zeilen)
				// Tabelle 1
				
				auswertung.addAll(whichRating(rotated_image, xx + 10, x2 + x2_add, y2 - 10, 294, 197, 4, 5));
//				whichRating(rotated_image, xx + 10, x2 + x2_add, y2 - 10, 294, 197, 4, 5);
				// Tabelle 2; y2 korrektur um 360, h�he: ca. 110
				auswertung.addAll(whichRating(rotated_image, xx + 100, x2 + x2_add, y2 + 360, 294, 110, 4, 3));
				// Tabelle 3: y2 korrektur um 665
				auswertung.addAll(whichRating(rotated_image, xx + 1000, x2 + x2_add, y2 + 665, 294, 160, 4, 4));
				// whichRating(image,xx+10,x2+520,y2-10,294,197);
				
				// Auswertung in Evaluation einfügen, damit wir die Einzelnen Fragen pro Blatt aufsummieren können.
				for(int i = 0; i< auswertung.size();i++) {
					evaluation[i][auswertung.get(i)-1]++;
				}
				auswertung.clear();
			} catch (Exception e) {
				System.out.println("Exception");
				e.printStackTrace();
			}
		}
		
		// Die AUswertung nun aufsplitten auf die einzelnen Pages. etvl. bereits oben auf 
		return evaluation;
		// https://stackoverflow.com/questions/39420986/java-tesseract-return-co-ordinates-of-text-location
		// https://stackabuse.com/tesseract-simple-java-optical-character-recognition/
	}
	/**
	 * Das Übergebene Bild beinhaltet die PDF Seite. anhand der Koordinaten von x/y und dessen grösse wird dann die Tabelle ausgeschnitten
	 * Mittels "spalten" und "zeilen" können wir dann das dynamsiche Rating in einer andern Funktion beginnen. 
	 * @param img
	 * @param number
	 * @param pos_x
	 * @param pos_y
	 * @param width_x
	 * @param width_y
	 * @param spalten
	 * @param zeilen
	 * @return
	 */
	private ArrayList<Integer> whichRating(BufferedImage img, int number, int pos_x, int pos_y, int width_x, int width_y, int spalten,
			int zeilen) {
		BufferedImage image = img.getSubimage(pos_x, pos_y, width_x, width_y); // Here
																				// you
																				// draw
																				// a
																				// rectangle
																				// around
																				// the
																				// area
																				// you
																				// want
																				// to
																				// specify
		if(debugen){
			String fileName = "S"+number +"A";
			for (int i : rateDynamic(image, spalten, zeilen)) {
				System.out.println("i: " + (i));
				fileName = fileName+(i);
			}
			try {

				System.out.println("fileName: " + fileName);
				ImageIO.write(image, "JPEG",
						new File(initPath+"pdf_umfragen/Pics/"	+ fileName + ".jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return rateDynamic(image, spalten, zeilen);
	}

	/**
	 * Tangens(Gegenkathete/Ankathete)
	 *
	 * @param ak
	 *            Ankathete
	 * @param gk
	 *            Gegenkathete
	 * @return
	 */
	private double getAngle(double ak, double gk) {
		return Math.tan(gk / ak);
	}

	/**
	 * Gibt ein rotiertes Bild zur�ck. Anhand des Winkels angle an der Position
	 * vom bilt (xCenter/yCenter)
	 *
	 * @param img
	 * @param angle
	 * @param xCenter
	 * @param yCenter
	 * @return
	 */
	private BufferedImage rotate(BufferedImage img, double angle, int xCenter, int yCenter) {
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.rotate(-angle, xCenter, yCenter);// Rotation around the
															// x and y Center
		AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage result;
		result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		affineTransformOp.filter(img, result);
		return result;
	}

	/**
	 * Das Uebergebene Bild wird als Tabell mit Spalten und Zeilen interpretiert.
	 * Hierbei ist jede Zelle gleich gross. Um die Genauigkeit der Auswertung zu
	 * erhoehen, werden die inneren 60% einer Zelle ausgewertet. Dadurch werden
	 * Fehlinterpretationen durch die Zellen-Linien vermindert.
	 *
	 * @param img
	 * @param spalten
	 * @param zeilen
	 * @return
	 */
	private ArrayList<Integer> rateDynamic(BufferedImage img, int spalten, int zeilen) {
		ArrayList<Integer> positions = new ArrayList<Integer>();
		double breite = img.getWidth() / spalten;
		double hohe = img.getHeight() / zeilen;
		// Um Probleme mit den R�ndern zu entgehen, schauen wir nur den inneren
		// Teil an.
		// Somit schauen wir nur 60% des Feldes an.
		int focus_breite = (int) (breite * 0.2);
		int focus_hohe = (int) (hohe * 0.2);
		for (int j = 0; j < zeilen; j++) {
			long which_is_choosed = 255 * 3;// Weiss
			int position = 0;// Weiss
			for (int i = 0; i < spalten; i++) {
				BufferedImage zelle = img.getSubimage((int) (focus_breite + (i * breite)),
						(int) (focus_hohe + (j * hohe)), (int) (breite * 0.6), (int) (hohe * 0.6));
				int red = 0;
				int green = 0;
				int blue = 0;
				for (int x = 0; x < zelle.getWidth(); x++) {
					for (int y = 0; y < zelle.getHeight(); y++) {
						int clr = zelle.getRGB(x, y);
						red += (clr & 0x00ff0000) >> 16;
						green += (clr & 0x0000ff00) >> 8;
						blue += clr & 0x000000ff;
					}
				}
				red = red / zelle.getHeight() / zelle.getWidth();
				green = green / zelle.getHeight() / zelle.getWidth();
				blue = blue / zelle.getHeight() / zelle.getWidth();
				int isChoice = red + green + blue;

				if (which_is_choosed > isChoice) {
					which_is_choosed = isChoice;
					position = i+1;
				}
			}

			positions.add(position);
		}
		return positions;
	}
	
	public void startHighlightingTest() {
		debugen = true;
		File file = new File(initPath+"pdf_umfragen/ScanBewertungen2_highlighted.pdf");
		try {
			PDDocument document = PDDocument.load(file);
			try {
				System.out.println(Arrays.deepToString(analyzeFileHighlight(document)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			document.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("lokales PDF konnten nicht gefunden werden.");
		}
	}
	public int[][] analyzeFileHighlight(PDDocument doc) throws Exception{
		PDFRenderer renderer = new PDFRenderer(doc);
		ArrayList<Integer> auswertung = new ArrayList<Integer>();
		int [][] evaluation = new int [3*4][4]; //Zuerzeit Statisch - 12 Fragen, mit je 4 Antowrtmöglichkeiten


		for (int xx = 0; xx < 1; xx++) {
			for(int render = 0; render<1;render++) {
				render = 6;
				BufferedImage image = renderer.renderImage(xx, render);
				ImageIO.write(image, "JPEG",
						new File(initPath+"pdf_umfragen/Pics/PDF"+1+"_Markiert.jpg"));
				Graphics2D g2d = image.createGraphics();

				g2d.setColor(Color.RED);
				int level = 4;
				/*
				 * Idee: die einzelnen Stücke der Analyse auf die gelbe Farbe abfragen.
				 * und dann alle mal aufs original einzeichnen und schauen, ob es funktioniert hat.
				 */
				////3
				searchThroug = image;
				for (int y = 0; y < image.getHeight(); y++) {
				    for (int x = 0; x < image.getWidth(); x++) {
				        //check if current pixel has maze colour
				        if(isHighlightedColour(image.getRGB(x, y))){
				            Rectangle rect = findRectangle(x, y);
				            if(rect != null) {

					            g2d.drawRect(x, y,rect.width, rect.height);
					            x+=rect.width;
				            }
				        }
				    }
				}
				System.out.println("Anzahl Rechtecke: " + mazeRectangles.size());

				g2d.dispose();
				ImageIO.write(image, "JPEG",
						new File(initPath+"pdf_umfragen/Pics/PDF"+2+"_Markiert.jpg"));
				ImageIO.write(image, "JPEG",
						new File(initPath+"pdf_umfragen/Pics/PDF"+xx+"_Markiert.jpg"));
			}
		}
		return evaluation;
	}
	
	
	
	
	
	public boolean isHighlightedColour(int color){
	    // here you should actually check for a range of colours, since you can
	    // never expect to get a nicely encoded image..
		final int maxColor = new Color(255,191,0).getRGB();
		final int minColor = new Color(191,255,0).getRGB();
		
		final int min = 191;
		final int max = 255;
		
		int r = (color & 0x00ff0000) >> 16;
		int g = (color & 0x0000ff00) >> 8;
		int b = (color & 0x000000ff) >> 0;
		
		if(r>min && g>min && b <10) {
//			System.out.println("-----------------");
//			System.out.println("r " + r);
//			System.out.println("g " + g);
//			System.out.println("b " + b);
		}
 	    return r>min && g>min&& b <10;
	}
//https://stackoverflow.com/questions/35376726/how-to-detect-a-colored-rectangles-in-an-image
	public Rectangle findRectangle(int x, int y){
	    // this could be optimized. You could keep a separate collection where
	    // you remove rectangles from, once your cursor is below that rectangle
		Rectangle toReturn = null;
	    for(Rectangle rectangle : mazeRectangles){ 
	        if(rectangle.contains(x, y)){
	        	System.out.println("Rectangle bereits vorhanden");
	            return null;
	        }
	    }
	    //find the width of the `Rectangle`
	    int xD = 1;
	    while(isHighlightedColour(searchThroug.getRGB(x+xD, y))){
	        xD++;
	    }

	    int yD = 1; //todo: find height of rect..
	    while(isHighlightedColour(searchThroug.getRGB(x, y+yD))){
	    	yD++;
	    }
	    
	    if(yD >20 && xD>20) {
		    toReturn = new Rectangle(x, y, xD, yD);
		    mazeRectangles.add(toReturn);
	    }
	    System.out.println("---------");
	    System.out.println("yD " + yD);
	    System.out.println("xD " + xD);
	    return toReturn;
	}
}