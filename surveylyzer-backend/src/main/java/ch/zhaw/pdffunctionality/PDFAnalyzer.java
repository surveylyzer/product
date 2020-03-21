package ch.zhaw.pdffunctionality;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
	public PDFAnalyzer() {
		init();
	}

	private void init() {

		sentence = new HashMap<String, Integer>();
		// ACHTUNG: f�r die Ausrichtung m�ssen die W�rter die selbe h�he haben
		// gut: talk vs understandable
		// schlecht: talk vs. was
		// sentence.add("talk was held in a clear and understandable way");
		sentence.put("talk", 842);
		// sentence.put("was",795);
		sentence.put("held", 742);
		// sentence.put("in",680);
		sentence.put("clear", 632);
		sentence.put("and", 570);
		sentence.put("understandable", 520);

		// sentence.put("way",341);
		/*
		 * y = -10 und x jeweils: x Ziel = 520+614 = 1'134 understandable:520
		 */
		t = new Tesseract();
		if (Util.isOS()) {
			initPath = "surveylyzer-backend/";
		   t.setDatapath("surveylyzer-backend/tess/tessdata/");
		} else {
			initPath = "../surveylyzer-backend/";
		   t.setDatapath("../surveylyzer-backend/tess/tessdata/");
		}
	}

	public boolean startTest() {
		debugen = true;

		File file = new File(initPath+"pdf_umfragen/ScanBewertungen2.pdf");
//		File file = new File("../surveylyzer-backend/pdf_umfragen/ScanBewertungen2.pdf");

		try {
			PDDocument document = PDDocument.load(file);
			try {
				System.out.println(analyzeFile(document));
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

	public ArrayList<Integer> analyzeFile(PDDocument doc) throws Exception{
		PDFRenderer renderer = new PDFRenderer(doc);
		ArrayList<Integer> auswertung = new ArrayList<Integer>();
		for(int xx = 0; xx< doc.getNumberOfPages();xx++){
//		for (int xx = 0; xx < 4; xx++) {

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
				// whichRating(rotated_image,xx+10,x2+520,y2-10,294,197);
				// Tabelle 1
				auswertung.addAll(whichRating(rotated_image, xx + 10, x2 + x2_add, y2 - 10, 294, 197, 4, 5));
//				whichRating(rotated_image, xx + 10, x2 + x2_add, y2 - 10, 294, 197, 4, 5);
				// Tabelle 2; y2 korrektur um 360, h�he: ca. 110
				auswertung.addAll(whichRating(rotated_image, xx + 100, x2 + x2_add, y2 + 360, 294, 110, 4, 3));
				// Tabelle 3: y2 korrektur um 665
				auswertung.addAll(whichRating(rotated_image, xx + 1000, x2 + x2_add, y2 + 665, 294, 160, 4, 4));
				// whichRating(image,xx+10,x2+520,y2-10,294,197);
			} catch (Exception e) {
				System.out.println("Exception");
				e.printStackTrace();
			}
		}
		return auswertung;
		// https://stackoverflow.com/questions/39420986/java-tesseract-return-co-ordinates-of-text-location
		// /https://stackabuse.com/tesseract-simple-java-optical-character-recognition/
	}

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
	 * Das �bergebene Bild wird als Tabell mit Spalten und Zeilen interpretiert.
	 * Hierbei ist jede Zelle gleich gross. Um die Genauigkeit der Auswertung zu
	 * erh�hen, werden die inneren 60% einer Zelle ausgewertet. Dadurch werden
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
		System.out.println("groesse positions " + positions.size());
		return positions;
	}

}