package ch.zhaw.pdffunctionality;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
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
	private boolean debugDrawing = false;
	private String initPath;
	
	private BufferedImage initImg;
	
	private List<Rectangle> allRectangles;
	private ArrayList<List<Rectangle>> groupedRectangles;
	private List<Word> allWords;
	private HashMap<String,Word> uniquWords;
	private ArrayList<List<Word>> groupedWords;
	private BufferedImage searchThroug;
	private int analysLevel = 3;
	private int resolutionLevel = 6;
	private int minWordLength =3;
	private double wToHrangePercent = 4.0;//Verhältnis von Höhe/Breite von 2 Wörter, Verhältniss dazu
	private double confidence = 90.0;
	private int analyseIterations = 2;
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
		return Math.atan((gk) /(ak));
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
	public void startHighlightingTest() { //@TODO: Testing für Elda, UserStory erstellen und ihr zuweisen - funktionen beschreiben die einen Unit test benötigen
		debugen = true;
//		File file = new File(initPath+"pdf_umfragen/ScanBewertungen2_highlighted.pdf");
//		File file = new File(initPath+"pdf_umfragen/ScanBewertungen2_highlighted2.pdf");
//		File fileInit = new File(initPath+"pdf_umfragen/InitFile2Speakers.pdf");
//		File filePrc = new File(initPath+"pdf_umfragen/prcFile2Speakers.pdf");
		File fileInit = new File(initPath+"pdf_umfragen/initFile.pdf");
		File filePrc = new File(initPath+"pdf_umfragen/prcFile.pdf");
		try {
			PDDocument docInit = PDDocument.load(fileInit);
			PDDocument docPrc = PDDocument.load(filePrc);
			try {
				prcInitFile(docInit);
				System.out.println(Arrays.deepToString(prcSurveyFile(docPrc)));
				
//				System.out.println(Arrays.deepToString(prcInitFile(document)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			docInit.close();
			docPrc.close();
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
		initImg = renderer.renderImage(0, resolutionLevel);//Seite, Auflösung
				ImageIO.write(initImg, "JPEG",
						new File(initPath+"pdf_umfragen/Pics/PDF_Original.jpg"));
		Graphics2D g2d = initImg.createGraphics();
		g2d.setColor(Color.RED);
		/*
		 * Idee: die einzelnen Stücke der Analyse auf die gelbe Farbe abfragen.
		 * und dann alle mal aufs original einzeichnen und schauen, ob es funktioniert hat.
		 */
		//Do highlighting Stuff
		searchThroug = initImg;
		for (int y = 0; y < initImg.getHeight(); y++) {
		    for (int x = 0; x < initImg.getWidth(); x++) {
		        if(isHighlightedColour(initImg.getRGB(x, y))){
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
		
		

		
		allWords = t.getWords(initImg, this.analysLevel);
			//@Todo: evtl. bereits wörter für die Aussrichtung raussuchen.
			//Ausrichtungs Stuff
			
				ImageIO.write(initImg, "JPEG",
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
			if(i.next().size()<3) {
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
		for(int i = 0; i< groupedRectangles.size();i++) {
			evaluation[i] = new int[groupedRectangles.get(i).size()];
		}
		System.out.println("Anzahl Fragen: " + groupedRectangles.size());
		System.out.println("Anzahl Iterationen: " + this.analyseIterations);
//		for(int xx = 0; xx< doc.getNumberOfPages();xx++){
		ArrayList<BufferedImage> bestTry = new ArrayList<BufferedImage>();
		BufferedImage bestTryPerSiteText = null;
		String bestTryText ="";
//		for(int xx = 0; xx< doc.getNumberOfPages();xx++){
			for(int xx = 4; xx< 5;xx++){
				int bestTryPerSite = 0;

				System.out.println("-------------------------------##########################################-- Seite: " + xx);
				BufferedImage image = renderer.renderImage(xx, resolutionLevel);

				ImageIO.write(image, "JPEG",
						new File(initPath+"pdf_umfragen/Pics/P"+xx+".jpg"));
				List<Word> w = null;
				HashMap<String,Word> uWforRotation = null;
				List<List<Word>> wordPairs = null;
				for(int i = 0; i< analyseIterations;i++) {
					//Rendert die PDF-Seite, welche analysiert werden soll
					//Liste aller gefundenen Werte auf dem entsprechenden Analyse-Level
					w = t.getWords(image, analysLevel);
					//Wir holen die Wörter, welche nur einmal vorkommen für die Orientierrung
					uWforRotation = singleWords(w);
					//Liste von Wörterpaaren (initFile-Word, ScannedSIteWord) welche unique sind.
//					wordPairs = sameWords(this.uniquWords,uWforRotation,this.wToHrangePercent);//bisher
					wordPairs = sameWords(this.uniquWords,uWforRotation);//bisher

					System.out.println("w " + w.size() + " - " +"uWforRotation " + uWforRotation.size()+ " - " + "wordPairs " + wordPairs.size());
					
					if(wordPairs.size()== 0) {
						break;
					}
			//		System.out.println("Scanned Site: " + xx + " Durchlauf: " + i);
					
					if(wordPairs.size() > bestTryPerSite) {
						bestTryPerSite = wordPairs.size();
						bestTryPerSiteText = image;
						bestTryText = "P"+xx+"D"+i;
					}
					//////////////////////////////////////////////////////////////////////////


for(List<Word> wl:wordPairs) {
	
	int Swl_x = (int) wl.get(1).getBoundingBox().getX();
	int Swl_y = (int) wl.get(1).getBoundingBox().getY();
	int Swl_w = (int) wl.get(1).getBoundingBox().getWidth();
	int Swl_h = (int) wl.get(1).getBoundingBox().getHeight();
//	System.out.println(Swl_x + " - " + Swl_y + " - " + Swl_w + " - " + Swl_h );
	BufferedImage bi = image.getSubimage(Swl_x
										,Swl_y
										,Swl_w
										,Swl_h);
	

//	System.out.println(Owl_x + " - " + Owl_y + " - " + Owl_w + " - " + Owl_h );
//						BufferedImage biO = initImg.getSubimage(Owl_x
//															,Owl_y
//															,Owl_w
//															,Owl_h);
	if(debugDrawing) {
	Graphics2D g2d = image.createGraphics();
	g2d.setColor(Color.RED);
	g2d.drawRect(Swl_x, Swl_y,Swl_w, Swl_h);
	g2d.dispose();
	}
	
//						try {
//							ImageIO.write(biO, "JPEG",
//									new File(initPath+"pdf_umfragen/Pics/"	+ wl.get(0).getText() + "OO.jpg"));
//							ImageIO.write(bi, "JPEG",
//									new File(initPath+"pdf_umfragen/Pics/"	+ wl.get(0).getText() + ""+i+"SS.jpg"));
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
}
//for(List<Word> wl:wordPairs) {
//	int Owl_x = (int) wl.get(0).getBoundingBox().getX();
//	int Owl_y = (int) wl.get(0).getBoundingBox().getY();
//	int Owl_w = (int) wl.get(0).getBoundingBox().getWidth();
//	int Owl_h = (int) wl.get(0).getBoundingBox().getHeight();
//	Graphics2D g2do = image.createGraphics();
//	g2do.setColor(Color.BLUE);
//	g2do.drawRect(Owl_x, Owl_y,Owl_w, Owl_h);
//	g2do.dispose();
//}
//image = calibration(image,wordPairs);//@Todo: muss noch implementiert werden
image = calibration(image,w,wordPairs);//@Todo: muss noch implementiert werden
if(image == null) {
	continue;
}
w = t.getWords(image, analysLevel);
uWforRotation = singleWords(w);
wordPairs = sameWords(this.uniquWords,uWforRotation);//bisher


if(debugDrawing) {
for(List<Word> wl:wordPairs) {
	int Owl_x = (int) wl.get(0).getBoundingBox().getX();
	int Owl_y = (int) wl.get(0).getBoundingBox().getY();
	int Owl_w = (int) wl.get(0).getBoundingBox().getWidth();
	int Owl_h = (int) wl.get(0).getBoundingBox().getHeight();
	
	Graphics2D g2do = image.createGraphics();
	g2do.setColor(Color.BLACK);
	g2do.drawRect(Owl_x, Owl_y,Owl_w, Owl_h);
	g2do.dispose();
}
}

//Nun neu analysieren und die evalutierung anfangen.
//w = t.getWords(image, analysLevel);
////Wir holen die Wörter, welche nur einmal vorkommen für die Orientierrung
//uWforRotation = singleWords(w);
////Liste von Wörterpaaren (initFile-Word, ScannedSIteWord) welche unique sind.
//wordPairs = sameWords(this.uniquWords,uWforRotation);//bisher
//System.out.println("w " + w.size() + " - " +"uWforRotation " + uWforRotation.size()+ " - " + "wordPairs " + wordPairs.size());



//					try {
//						ImageIO.write(image, "JPEG",
//								new File(initPath+"pdf_umfragen/Pics/P"+xx+"D"+i+".jpg"));
////						ImageIO.write(initImg, "JPEG",
////								new File(initPath+"pdf_umfragen/Pics/Original.jpg"));
//						
//					}catch (Exception e) {
//						// TODO: handle exception
//					}
					
					
					//	System.out.println("image " + image.getWidth());
						//	System.out.println("image " + image.getHeight());
						


					///////////////////////////////////////////////////////////////////////////
				//	evaluation = doEvaluation(image,this.groupedRectangles);//@Todo: muss noch implementiert werden
					
				}
//				bestTry.add(bestTryPerSiteText);
				auswertung.addAll(doEvaluation(image, this.groupedRectangles, wordPairs));
			
			
			try {//@TODO: Wety hier weitermachen, nun die felder evaluieren!
				//@TODO: Auswertung Starten

				//@TODO: Auswertung zusammenfassen
				// Auswertung in Evaluation einfügen, damit wir die Einzelnen Fragen pro Blatt aufsummieren können.
				System.out.println("auswertun : " + auswertung.toString());
				for(int i = 0; i< auswertung.size();i++) {
					evaluation[i][auswertung.get(i)]++;
				}
				ImageIO.write(image, "JPEG",
						new File(initPath+"pdf_umfragen/Pics/P"+xx+"Final.jpg"));
				auswertung.clear();
			} catch (Exception e) {
				System.out.println("Exception");
			}
		}
			
//		int seiteee = 0;
//		for(BufferedImage bi:bestTry) {
//			ImageIO.write(bi, "JPEG",
//					new File(initPath+"pdf_umfragen/Pics/BestTry"+seiteee+++ "Name" +bestTryText+".jpg"));
//		}
		
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

		//System.out.println("sameWords----------------------------------");
		for(Map.Entry<String, Word> e : scanned.entrySet()) {
			if(orig.containsKey(e.getKey())) {
				List<Word> temp = new ArrayList<Word>();
				temp.add(orig.get(e.getKey()));//orig
				temp.add(e.getValue());//scann
				output.add(temp);
			}
		}
		return output;
	}
	//Mit range funktioniert so nicht -->  da wenn das wort leit schräg ist, natürlich das verhältnis nicht stimmen kann
	private List<List<Word>> sameWords(HashMap<String,Word> orig,HashMap<String,Word> scanned, double range){
		List<List<Word>> output = new ArrayList<List<Word>>();
		Word wO = null;
		Word wS = null;
		//System.out.println("sameWords----------------------------------");
		for(Map.Entry<String, Word> e : scanned.entrySet()) {
			if(orig.containsKey(e.getKey())) {//die selben Wörter vorhanden
				wO = orig.get(e.getKey());
				wS = e.getValue();
				System.out.println("getAbweichungBtoA(getWtoH(wO),getWtoH(wS))  " + getAbweichungBtoA(getWtoH(wO),getWtoH(wS)) );
				if(getAbweichungBtoA(getWtoH(wO),getWtoH(wS)) < range) {
					List<Word> temp = new ArrayList<Word>();
					temp.add(wO);//orig
					temp.add(wS);//scann
					output.add(temp);	
				}
			}
		}
		return output;
	}
	
	/**
	 * @TODO: Implementierung der Ausrichtung des Bildes anhand der Wörter
	 * @param img
	 * @param cwl
	 */
	private BufferedImage calibration(BufferedImage img,List<Word> w,List<List<Word>> cwl) {
		BufferedImage bi = img;
		if(cwl.size() >1) {
			Double rotation = calcRotation(w,cwl);
			bi = rotate(bi,
					rotation,cwl.get(0).get(1).getBoundingBox().getX()
					,cwl.get(0).get(1).getBoundingBox().getY());
			
		//	bi = resize(bi,cwl);
			
			
		}else {
			System.out.println("!!!!!!!!!!!!!!!!!!! keine Wortpaare zum Ausrichten!");
			bi = null;
		}
		return bi;
	}
	/**
	 * Gibt den Rotationswinkeln zurück.
	 * Anhand der Wortliste wird verglichen, wie das Original zum gescannten liegt. Dies anhand 3 Wörter(Anfang, Mitte, Schluss)
	 * @param cwl
	 * @return
	 */
	private double calcRotation(List<Word> wl,List<List<Word>> cwl) {
		double finalRotation = 0.0;
		if(debugen) {
			System.out.println("calcRotation    --> start " + cwl.size());
		}
		/*
		 * 1. Anhand dem selben Wörtern aus cwl eine Rotation berechnen.
		 */
		Word wO1 = cwl.get(0).get(0);
		Word wS1 = cwl.get(0).get(1);
		ArrayList<Double> diff = new ArrayList<Double>();
		int i = 0;
		for(int j = 1; j< cwl.size();j++) {
			Word wO = cwl.get(j).get(0);
			Word wS = cwl.get(j).get(1);
			if(wS.getConfidence()<confidence) {
				continue;
			}
			i++;
			double angleO = getWordAngle(wO1, wO);
			double angleS = getWordAngle(wS1, wS);
			diff.add(angleO-angleS);
			finalRotation += angleO-angleS;
		}
		finalRotation = finalRotation/i;
		return finalRotation;
	}

	private double getWordAngle(Word w1, Word w2) {
		
		return getAngle(w1.getBoundingBox().getCenterX() - w2.getBoundingBox().getCenterX(),
		           		w1.getBoundingBox().getCenterY() - w2.getBoundingBox().getCenterY());
	} 
	
	private BufferedImage resize(BufferedImage img, List<List<Word>> cwl) {//@TODO: wety hier weitermachen
		BufferedImage bi = img;
		//Distanz
		Word wOrig1 = cwl.get(0).get(0);
		Word wOrig2 = cwl.get(cwl.size()-1).get(0);
		Word wScan1 = cwl.get(0).get(1);
		Word wScan2 = cwl.get(cwl.size()-1).get(1);
		
		Double distanceBetwOrig = distWords(wOrig1, wOrig2);
		Double distanceBetwScan = distWords(wScan1, wScan2);
		
		Double calVal = distanceBetwScan * 100 /distanceBetwOrig;
		calVal = 100/calVal;
		if(!calVal.isNaN()) {
			bi =  scale(bi,calVal);
		}
//		System.out.println("SCALE "+calVal );
		return bi;
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
	private BufferedImage rotate(BufferedImage img, double angle, double xCenter, double yCenter) {
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.rotate(angle, xCenter, yCenter);
		AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage result;
		result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		affineTransformOp.filter(img, result);
		return result;
	}
	private double distWords(Word w1, Word w2) {//Pythagoras
		return Math.sqrt(Math.pow(w1.getBoundingBox().getCenterX() - w2.getBoundingBox().getCenterX(),2)
		 	             + Math.pow(w1.getBoundingBox().getCenterY() - w2.getBoundingBox().getCenterY(),2));
	}
	private double getWtoH(Word w) {//Pythagoras
		return Math.round((w.getBoundingBox().getWidth()/w.getBoundingBox().getHeight())*100.0)/100.0;
	}
	private double getAbweichungBtoA(double a, double b) {
		return Math.abs(100.0 - (b*100.0/a));
	}
	/**
	 * scale image
	 * 
	 * @param sbi image to scale
	 * @param imageType type of image
	 * @param dWidth width of destination image
	 * @param dHeight height of destination image
	 * @param fWidth x-factor for transformation / scaling
	 * @param fHeight y-factor for transformation / scaling
	 * @return scaled image
	 */
	public BufferedImage scale(BufferedImage sbi, Double scale) {
		System.out.println("                             scale " + scale);
		int newH = (int)(sbi.getHeight()*scale);
		int newW = (int)(sbi.getWidth()*scale);
		Image tmp = sbi.getScaledInstance((int)(sbi.getWidth()*scale), (int)(sbi.getHeight()*scale), Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_3BYTE_BGR);//Sonsts wirds Rot mit einem ander Type
	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}
	
	private ArrayList<Integer> doEvaluation(BufferedImage img, ArrayList<List<Rectangle>> gR, List<List<Word>> wordPairs) {
//		System.out.println("doEvaluationdoEvaluationdoEvaluationdoEvaluationdoEvaluationdoEvaluationdoEvaluationdoEvaluationdoEvaluationdoEvaluation " + wordPairs.size());
		
		ArrayList<Integer> eval = new ArrayList<Integer>();
		if(wordPairs.size()<2) {
			return eval;
		}
		Rectangle wO = wordPairs.get(wordPairs.size()-1).get(0).getBoundingBox();
		Rectangle wS = wordPairs.get(wordPairs.size()-1).get(1).getBoundingBox();
		Point ausgleich = new Point((int)((wO.getX()-wS.getX())*1)
				                   ,(int)((wO.getY()-wS.getY())*1));
//		System.out.println(wordPairs.get(wordPairs.size()-1).get(0));
//		System.out.println(wordPairs.get(wordPairs.size()-1).get(1));
//
//		System.out.println("ausgleich " + ausgleich);
		for(List<Rectangle> lr: gR) {
			eval.add(getChecked(img,lr,ausgleich));
		}

//		System.out.println("doEvalua " + eval.toString());
		return eval;
	}
	private int getChecked(BufferedImage img, List<Rectangle> gR, Point ausrichtung) {
		// Um Probleme mit den R�ndern zu entgehen, schauen wir nur den inneren
		// Teil an.
		// Somit schauen wir nur 60% des Feldes an.
		long which_is_choosed = 255 * 3;// Weiss
		int position = 0;
		int i = 0;
		for(Rectangle r: gR) {
			BufferedImage zelle = img.getSubimage((int) (r.getX()-ausrichtung.getX() + (r.getWidth() *0.2))
												 ,(int) (r.getY()-ausrichtung.getY() + (r.getHeight() *0.2))
												 ,(int) (r.getWidth() * 0.6)
												 ,(int) (r.getHeight() * 0.6));
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
				position = i;
			}
			i++;

			try {	
//				if(debugDrawing) {
				Graphics2D g2d = img.createGraphics();
			g2d.setColor(Color.RED);
			g2d.drawRect((int)(r.getX()-ausrichtung.getX()),(int)( r.getY()-ausrichtung.getY()), (int) r.getWidth(),(int) r.getHeight());
			g2d.dispose();
//				}
				ImageIO.write(zelle, "JPEG",
						new File(initPath+"pdf_umfragen/Pics/"	+ i + ".jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
//System.out.println("getChecked: " + (position+1));
		return position;
	}
	
	
}