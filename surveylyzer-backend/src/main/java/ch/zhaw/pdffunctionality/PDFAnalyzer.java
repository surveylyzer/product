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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	
	private List<Rectangle> allRectangles;
	private ArrayList<List<Rectangle>> groupedRectangles;
	private List<Word> allWords;
	private HashMap<String,Word> uniquWords;
	private ArrayList<List<Word>> groupedWords;
	private BufferedImage searchThroug;
	private int analysLevel = 3;
	private int resolutionLevel = 4;
	private int minWordLength =3;
	public PDFAnalyzer() {
		init();
	}

	private void init() {
		sentence = new HashMap<String, Integer>();
		allRectangles = new ArrayList<Rectangle>();
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
			BufferedImage image = renderer.renderImage(xx, resolutionLevel);
			List<Word> w = t.getWords(image, analysLevel);
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
		BufferedImage image = img.getSubimage(pos_x, pos_y, width_x, width_y);
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
	/**
	 * vorgegebenes PDF wird analysiert.
	 */
	public void startHighlightingTest() {
		debugen = true;
//		File file = new File(initPath+"pdf_umfragen/ScanBewertungen2_highlighted.pdf");
		File file = new File(initPath+"pdf_umfragen/ScanBewertungen2_highlighted2.pdf");
		try {
			PDDocument document = PDDocument.load(file);
			try {
				prcInitFile(document);
//				prcSurveyFile(document);
				
//				System.out.println(Arrays.deepToString(prcInitFile(document)));
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
	/**
	 * Init File wird analysiert:
	 *   - Highlighted Fields
	 *   - Grouping Fields
	 *   - Unique Words for alignment
	 * @param doc
	 * @throws Exception
	 */
	public void prcInitFile(PDDocument doc) throws Exception{
		PDFRenderer renderer = new PDFRenderer(doc);
		BufferedImage image = renderer.renderImage(0, 6);//Seite, Auflösung
				ImageIO.write(image, "JPEG",
						new File(initPath+"pdf_umfragen/Pics/PDF_Original.jpg"));
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.RED);
		/*
		 * Idee: die einzelnen Stücke der Analyse auf die gelbe Farbe abfragen.
		 * und dann alle mal aufs original einzeichnen und schauen, ob es funktioniert hat.
		 */
		//Do highlighting Stuff
		searchThroug = image;
		for (int y = 0; y < image.getHeight(); y++) {
		    for (int x = 0; x < image.getWidth(); x++) {
		        if(isHighlightedColour(image.getRGB(x, y))){
		            Rectangle rect = findRectangle(x, y);
		            if(rect != null) {
		            	if(debugen) {
		            		g2d.drawRect(x, y,rect.width, rect.height);
		            	}
			            x+=rect.width;
		            }
		        }
		    }
		}
		
		g2d.dispose();
		int level = 3;
		allWords = t.getWords(image, level);
			//@Todo: evtl. bereits wörter für die Aussrichtung raussuchen.
			//Ausrichtungs Stuff
			
				ImageIO.write(image, "JPEG",
						new File(initPath+"pdf_umfragen/Pics/PDF_Markiert.jpg"));
		groupedRectangles = groupRectangle(20,allRectangles);
		groupedWords = groupWords(20,allWords);
		uniquWords = singleWords(allWords);
		if(debugen) {
			System.out.println("Anzahl Rechtecke: " + allRectangles.size());
			System.out.println("Anzahl Fragen: " + groupedRectangles.size());		
			int asdf = 1;
			for(List<Rectangle> l : groupedRectangles) {
				System.out.println("Frage: " +(asdf++) + " Auswahl: " +l.size());
			}
			System.out.println("-------------------");
			System.out.println("Anzahl Fragen: " + groupedWords.size());		
			asdf = 1;
			for(List<Word> l : groupedWords) {
				System.out.println("Frage: " +(asdf++) + " Auswahl: " +l.size());
				for(Word r:l) {
					System.out.print(" " +r.getText());
				}
				System.out.println("");
			}
			System.out.println("-------------------");
			System.out.println("Anzahl uniqeWords: " + uniquWords.size());		
			asdf = 1;

			for(Map.Entry<String, Word> e : uniquWords.entrySet()) {
				System.out.println("Wort: "+ e.getValue().getText());
			}
		}
	}
	
	
	
	
	/**
	 * Range von gelber Farbe wird überprüft.
	 * @param color
	 * @return
	 */
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
		
 	    return r>min && g>min&& b <10;
	}
/**
 * Sucht auf der gegebenen Koordinaten (X/Y) ein Rechteck. Falls ein neues gefunden werden kann,
 * wird es zurück gegeben
 * @param x
 * @param y
 * @return
 */
	public Rectangle findRectangle(int x, int y){
	    // this could be optimized. You could keep a separate collection where
	    // you remove rectangles from, once your cursor is below that rectangle
		Rectangle toReturn = null;
	    for(Rectangle rectangle : allRectangles){ 
	        if(rectangle.contains(x, y)){ // verbesserungspotential bezüglich Performance vorhanden. x um weite erhöhen
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
		    allRectangles.add(toReturn);
	    }
	    return toReturn;
	}
	/**
	 * Gruppiert die übergebenen Rechte horizontal anhand des gegebenen Ranges
	 * @param range
	 * @param all
	 * @return
	 */
	private ArrayList<List<Rectangle>> groupRectangle(int range, List<Rectangle> all){
		if(all.size()==0) {return null;}//Leere Liste abfangen.
		ArrayList<List<Rectangle>> sorted = new ArrayList<List<Rectangle>> ();
		List<Rectangle> rl = new ArrayList<Rectangle>();
		
		for(Rectangle r : all) {
			if(rl.size() == 0) {
				//Initialisierung
				rl.add(r);
			}else {
				if(isInRange(range,rl.get(0).y,r.y)) {
					//Sind drin, fügen es hinzu
					rl.add(r);
				}else {
					//nicht im Range, somit neue Frage erstellen.
					sorted.add(rl);
					rl = new ArrayList<Rectangle>();
					rl.add(r);
				}
			}
		}
		sorted.add(rl);
		return sorted;
		
	}
	/**
	 * Gruppiert die übergebenen Wörter horizontal anhand des Ranges.
	 * @param range
	 * @param all
	 * @return
	 */
	private ArrayList<List<Word>> groupWords(int range, List<Word> all){
		if(all.size()==0) {return null;}//Leere Liste abfangen.
		ArrayList<List<Word>> sorted = new ArrayList<List<Word>> ();
		List<Word> rl = new ArrayList<Word>();
		
		for(Word w : all) {
			if(w.getText().length() <minWordLength) { //Wörter < 3 werden nicht berücksichtig
				continue;
			}
			if(rl.size() == 0) {
				//Initialisierung
				rl.add(w);
			}else {
				if(isInRange(range,rl.get(0).getBoundingBox().y,w.getBoundingBox().y)) {
					//Sind drin, fügen es hinzu
					rl.add(w);
				}else {
					//nicht im Range, somit neue Frage erstellen.
					sorted.add(rl);
					rl = new ArrayList<Word>();
					rl.add(w);
				}
			}
		}
		sorted.add(rl);
		//Alle EInträge mit weniger als z.B 5 Wörter rausschmeissen.
		Iterator<List<Word>> i = sorted.iterator();
		while(i.hasNext()) {
			if(i.next().size()<5) {
				i.remove();
			}
		}
		return sorted;
		
	}
	/**
	 * Befindet sich b im Range von a?
	 * @param range
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean isInRange(int range, int a, int b) {
		return (a-range)<b && (a+range)>b;
	}
	
	/**
	 * Analyse der auszuwertenden gescannten Dokumentes
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public int[][] prcSurveyFile(PDDocument doc) throws Exception{
		PDFRenderer renderer = new PDFRenderer(doc);
		ArrayList<Integer> auswertung = new ArrayList<Integer>();
		int [][] evaluation = new int [groupedRectangles.size()][]; 
		for(int xx = 0; xx< doc.getNumberOfPages();xx++){
			//Rendert die PDF-Seite, welche analysiert werden soll
			BufferedImage image = renderer.renderImage(xx, resolutionLevel);
			//Liste aller gefundenen Werte auf dem entsprechenden Analyse-Level
			List<Word> w = t.getWords(image, analysLevel);
			//Wir holen die Wörter, welche nur einmal vorkommen für die Orientierrung
			HashMap<String,Word> uWforRotation = singleWords(w);
			//Liste von Wörterpaaren (initFile-Word, ScannedSIteWord) welche unique sind.
			List<List<Word>> wordPairs = sameWords(this.uniquWords,uWforRotation);
			//Kalibrierung der Seite: Ausrichtung
			calibration(image,wordPairs);//@Todo: muss noch implementiert werden
			evaluation = doEvaluation(image,this.groupedRectangles);//@Todo: muss noch implementiert werden
			
			try {
				//@Todo: Rotieren
				
				//@TODO: Auswertung Starten
				
				//@TODO: Auswertung zusammenfassen
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
	 * Gibt eine Liste von den Wörter zurück, welche nur einmal im Dokument(Wörter-Liste) vorkommt
	 * @param list
	 * @return
	 */
	private HashMap<String,Word> singleWords(List<Word> list){
		HashMap<String,Word> singleWords = new HashMap<String,Word>();
		HashMap<String,String> deletWords = new HashMap<String,String>();
		for(Word w: list) {
			if(w.getText().length() <minWordLength) { //Wörter < 3 werden nicht berücksichtig
				continue;
			}
			if(singleWords.containsKey(w.getText())) {
				deletWords.put(w.getText(), w.getText());
			}else {
				singleWords.put(w.getText(), w);
			}
		}
		HashMap<String,Word> output = new HashMap<String,Word>();
		for(Map.Entry<String, Word> e : singleWords.entrySet()) {
		   	if(!deletWords.containsKey(e.getKey())) {
		   		output.put(e.getKey(),e.getValue());
		   	}
		}
		return output;
	}
	/**
	 * Gibt eine 2D-Array von Wörter zurück, welche in beiden übergebenen Listen vorkommen.
	 * @param orig
	 * @param scanned
	 * @return
	 */
	private List<List<Word>> sameWords(HashMap<String,Word> orig,HashMap<String,Word> scanned){
		List<List<Word>> output = new ArrayList<List<Word>>();

		for(Map.Entry<String, Word> e : scanned.entrySet()) {
			if(orig.containsKey(e.getKey())) {
				List<Word> temp = new ArrayList<Word>();
				temp.add(orig.get(e.getKey()));//orig
				temp.add(e.getValue());//scann
			}
		}
		return output;
	}
	/**
	 * @TODO: Implementierung der Ausrichtung des Bildes anhand der Wörter
	 * @param img
	 * @param cwl
	 */
	private void calibration(BufferedImage img,List<List<Word>> cwl) {
		
	}
	/**
	 * Gibt den Rotationswinkeln zurück.
	 * Anhand der Wortliste wird verglichen, wie das Original zum gescannten liegt. Dies anhand 3 Wörter(Anfang, Mitte, Schluss)
	 * @param cwl
	 * @return
	 */
	private double calcRotation(List<List<Word>> cwl) {
		int mid = (int)(cwl.size()/2);
		//cw.get(0) -> die ersten 2 Wörter .get(0) = Original   ,.get(1) = scannedPDF
		double w1a1 = getAngle(cwl.get(0).get(0).getBoundingBox().getCenterX() - cwl.get(mid).get(0).getBoundingBox().getCenterX()
							  ,cwl.get(0).get(0).getBoundingBox().getCenterY() - cwl.get(mid).get(0).getBoundingBox().getCenterY());
		double w1a2 = getAngle(cwl.get(mid).get(0).getBoundingBox().getCenterX() - cwl.get(cwl.size()).get(0).getBoundingBox().getCenterX()
				              ,cwl.get(mid).get(0).getBoundingBox().getCenterY() - cwl.get(cwl.size()).get(0).getBoundingBox().getCenterY());
		double w1a3 = getAngle(cwl.get(cwl.size()).get(0).getBoundingBox().getCenterX() - cwl.get(0).get(0).getBoundingBox().getCenterX()
                              ,cwl.get(cwl.size()).get(0).getBoundingBox().getCenterY() - cwl.get(0).get(0).getBoundingBox().getCenterY());

		double w2a1 = getAngle(cwl.get(0).get(1).getBoundingBox().getCenterX() - cwl.get(mid).get(0).getBoundingBox().getCenterX()
							  ,cwl.get(0).get(1).getBoundingBox().getCenterY() - cwl.get(mid).get(0).getBoundingBox().getCenterY());
		double w2a2 = getAngle(cwl.get(mid).get(1).getBoundingBox().getCenterX() - cwl.get(cwl.size()).get(0).getBoundingBox().getCenterX()
				              ,cwl.get(mid).get(1).getBoundingBox().getCenterY() - cwl.get(cwl.size()).get(0).getBoundingBox().getCenterY());
		double w2a3 = getAngle(cwl.get(cwl.size()).get(1).getBoundingBox().getCenterX() - cwl.get(0).get(0).getBoundingBox().getCenterX()
                              ,cwl.get(cwl.size()).get(1).getBoundingBox().getCenterY() - cwl.get(0).get(0).getBoundingBox().getCenterY());
		
		if(debugen) {
			System.out.println("----------: calcRotation");
			System.out.println("OrigWord: " + w1a1 + "-"+ w1a2 + "-"+ w1a3);
			System.out.println("ScanWord: " + w2a1 + "-"+ w2a2 + "-"+ w2a3);
			System.out.println("zu Drehen: " + (w1a1-w2a1) + "-"+ (w1a2-w2a2) + "-"+ (w1a3-w2a3));
		}
		return 3.1;//@Todo
	}
	private int[][] doEvaluation(BufferedImage img, ArrayList<List<Rectangle>> gR) {
		// TODO Auto-generated method stub
		return null;
	}
	
}