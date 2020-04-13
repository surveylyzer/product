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

import lombok.Getter;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Word;

public class PDFAnalyzer {

	// https://stackabuse.com/tesseract-simple-java-optical-character-recognition/
	private Tesseract t;
	private boolean debugen = false;
	private boolean debugDrawing = false;
	private String initPath;

	private BufferedImage initImg;

	@Getter
    @Setter
    public static List<int[]> evaluationResults;

	private List<Rectangle> allRectangles;
	private ArrayList<List<Rectangle>> groupedRectangles;
	private List<Word> allWords;
	private HashMap<String, Word> uniquWords;

    @Getter
    @Setter
	public static ArrayList<List<Word>> groupedWords;

    @Getter
    @Setter
    public static boolean evaluationReady = false;

	private BufferedImage searchThroug;
	private int analysLevel = 3;//Tiefe von Tesseract(3 = Wörter, 4=Buchstaben)
	private int resolutionLevel = 6;//Bild Auflösung beim Rendern
	private int minWordLength = 3;//Wie lang muss mind. ein Word sein.
	private double confidence = 90.0;//Die genauigkeit, mit welcher ein Wort bestimmt wurde.
	private int analyseIterations = 2;//Wie oft wird eine Seite analysiert.

	public PDFAnalyzer() {
		init();
	}

	private void init() {
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
	 * Tangens(Gegenkathete/Ankathete)
	 *
	 * @param ak
	 *            Ankathete
	 * @param gk
	 *            Gegenkathete
	 * @return
	 */
	private double getAngle(double ak, double gk) {
		return Math.atan((gk) / (ak));
	}

	/**
	 * vorgegebenes PDF wird analysiert.
	 */
	public void startHighlightingTest() {
		debugen = true;
		File fileInit = new File(initPath + "pdf_umfragen/initFile.pdf");
		File filePrc = new File(initPath + "pdf_umfragen/prcFile.pdf");
		try {
			PDDocument docInit = PDDocument.load(fileInit);
			PDDocument docPrc = PDDocument.load(filePrc);
			try {
				prcInitFile(docInit);
                evaluationResults = Arrays.asList(prcSurveyFile(docPrc));
				if (!evaluationResults.isEmpty()) {
				    evaluationReady = true;
                }
//				System.out.println(Arrays.deepToString(prcSurveyFile(docPrc)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			docInit.close();
			docPrc.close();
		} catch (IOException e) {
			System.out.println("lokales PDF konnten nicht gefunden werden.");
		}
	}

	/**
	 * Init File wird analysiert: - Highlighted Fields - Grouping Fields - Unique
	 * Words for alignment
	 *
	 * @param doc
	 * @throws Exception
	 */
	public void prcInitFile(PDDocument doc) throws Exception {
		PDFRenderer renderer = new PDFRenderer(doc);
		initImg = renderer.renderImage(0, resolutionLevel);// Seite, Auflösung
		ImageIO.write(initImg, "JPEG", new File(initPath + "pdf_umfragen/Pics/PDF_Original.jpg"));
		Graphics2D g2d = initImg.createGraphics();
		g2d.setColor(Color.RED);
		/*
		 * Idee: die einzelnen Stücke der Analyse auf die gelbe Farbe abfragen. und dann
		 * alle mal aufs original einzeichnen und schauen, ob es funktioniert hat.
		 */
		// Do highlighting Stuff
		searchThroug = initImg;
		for (int y = 0; y < initImg.getHeight(); y++) {
			for (int x = 0; x < initImg.getWidth(); x++) {
				if (isHighlightedColour(initImg.getRGB(x, y))) {
					Rectangle rect = findRectangle(x, y);
					if (rect != null) {
						if (debugen) {
							g2d.drawRect(x, y, rect.width, rect.height);
						}
						x += rect.width;
					}
				}
			}
		}

		g2d.dispose();

		allWords = t.getWords(initImg, this.analysLevel);

		ImageIO.write(initImg, "JPEG", new File(initPath + "pdf_umfragen/Pics/PDF_Markiert.jpg"));
		groupedRectangles = groupRectangle(20, allRectangles);
		groupedWords = groupWords(20, allWords);
		uniquWords = singleWords(allWords);
		if (debugen) {
			System.out.println("Anzahl Rechtecke: " + allRectangles.size());
			System.out.println("Anzahl Fragen: " + groupedRectangles.size());
			int asdf = 1;
			for (List<Rectangle> l : groupedRectangles) {
				System.out.println("Frage: " + (asdf++) + " Auswahl: " + l.size());
			}
			System.out.println("-------------------");
			System.out.println("Anzahl Fragen: " + groupedWords.size());
			asdf = 1;
			for (List<Word> l : groupedWords) {
				System.out.println("Frage: " + (asdf++) + " Auswahl: " + l.size());
				for (Word r : l) {
					System.out.print(" " + r.getText());
				}
				System.out.println("");
			}
			System.out.println("-------------------");
			System.out.println("Anzahl uniqeWords: " + uniquWords.size());
			asdf = 1;

			for (Map.Entry<String, Word> e : uniquWords.entrySet()) {
				System.out.println("Wort: " + e.getValue().getText());
			}
		}
	}

	/**
	 * Range von gelber Farbe wird überprüft.
	 *
	 * @param color
	 * @return
	 */
	public boolean isHighlightedColour(int color) {
		// here you should actually check for a range of colours, since you can
		// never expect to get a nicely encoded image..
		final int maxColor = new Color(255, 191, 0).getRGB();
		final int minColor = new Color(191, 255, 0).getRGB();

		final int min = 191;
		final int max = 255;

		int r = (color & 0x00ff0000) >> 16;
		int g = (color & 0x0000ff00) >> 8;
		int b = (color & 0x000000ff) >> 0;

		return r > min && g > min && b < 10;
	}

	/**
	 * Sucht auf der gegebenen Koordinaten (X/Y) ein Rechteck. Falls ein neues
	 * gefunden werden kann, wird es zurück gegeben
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public Rectangle findRectangle(int x, int y) {
		Rectangle toReturn = null;
		for (Rectangle rectangle : allRectangles) {
			if (rectangle.contains(x, y)) { // verbesserungspotential bezüglich Performance vorhanden. x um weite
											// erhöhen
				return null;
			}
		}
		int xD = 1;
		while (isHighlightedColour(searchThroug.getRGB(x + xD, y))) {
			xD++;
		}

		int yD = 1;
		while (isHighlightedColour(searchThroug.getRGB(x, y + yD))) {
			yD++;
		}

		if (yD > 20 && xD > 20) {
			toReturn = new Rectangle(x, y, xD, yD);
			allRectangles.add(toReturn);
		}
		return toReturn;
	}

	/**
	 * Gruppiert die übergebenen Rechte horizontal anhand des gegebenen Ranges
	 *
	 * @param range
	 * @param all
	 * @return
	 */
	private ArrayList<List<Rectangle>> groupRectangle(int range, List<Rectangle> all) {
		if (all.size() == 0) {
			return null;
		} // Leere Liste abfangen.
		ArrayList<List<Rectangle>> sorted = new ArrayList<List<Rectangle>>();
		List<Rectangle> rl = new ArrayList<Rectangle>();

		for (Rectangle r : all) {
			if (rl.size() == 0) {
				// Initialisierung
				rl.add(r);
			} else {
				if (isInRange(range, rl.get(0).y, r.y)) {
					// Sind drin, fügen es hinzu
					rl.add(r);
				} else {
					// nicht im Range, somit neue Frage erstellen.
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
	 *
	 * @param range
	 * @param all
	 * @return
	 */
	private ArrayList<List<Word>> groupWords(int range, List<Word> all) {
		if (all.size() == 0) {
			return null;
		} // Leere Liste abfangen.
		ArrayList<List<Word>> sorted = new ArrayList<List<Word>>();
		List<Word> rl = new ArrayList<Word>();

		for (Word w : all) {
			if (w.getText().length() < minWordLength) { // Wörter < 3 werden nicht berücksichtig
				continue;
			}
			if (rl.size() == 0) {
				// Initialisierung
				rl.add(w);
			} else {
				if (isInRange(range, rl.get(0).getBoundingBox().y, w.getBoundingBox().y)) {
					// Sind drin, fügen es hinzu
					rl.add(w);
				} else {
					// nicht im Range, somit neue Frage erstellen.
					sorted.add(rl);
					rl = new ArrayList<Word>();
					rl.add(w);
				}
			}
		}
		sorted.add(rl);
		// Alle EInträge mit weniger als z.B 5 Wörter rausschmeissen.
		Iterator<List<Word>> i = sorted.iterator();
		while (i.hasNext()) {
			if (i.next().size() < 3) {
				i.remove();
			}
		}
		return sorted;

	}

	/**
	 * Befindet sich b im Range von a?
	 *
	 * @param range
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean isInRange(int range, int a, int b) {
		return (a - range) < b && (a + range) > b;
	}

	/**
	 * Analyse der auszuwertenden gescannten Dokumentes
	 *
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public int[][] prcSurveyFile(PDDocument doc) throws Exception {

		PDFRenderer renderer = new PDFRenderer(doc);
		ArrayList<Integer> auswertung = new ArrayList<Integer>();
		int[][] evaluation = new int[groupedRectangles.size()][];
		for (int i = 0; i < groupedRectangles.size(); i++) {
			evaluation[i] = new int[groupedRectangles.get(i).size()];
		}
		if (debugen) {
			System.out.println("Anzahl Fragen: " + groupedRectangles.size());
			System.out.println("Anzahl Iterationen: " + this.analyseIterations);
		}
		ArrayList<BufferedImage> bestTry = new ArrayList<BufferedImage>();
		BufferedImage bestTryPerSiteText = null;
		String bestTryText = "";
		for (int xx = 0; xx < doc.getNumberOfPages(); xx++) {
			// for(int xx = 4; xx< 5;xx++){
			BufferedImage image = renderer.renderImage(xx, resolutionLevel);
			if (debugen) {
				System.out.println(
						"-------------------------------##########################################-- Seite: " + xx);
				ImageIO.write(image, "JPEG", new File(initPath + "pdf_umfragen/Pics/P" + xx + ".jpg"));
			}
			List<Word> w = null;
			HashMap<String, Word> uWforRotation = null;
			List<List<Word>> wordPairs = null;
			for (int i = 0; i < analyseIterations; i++) {
				// Rendert die PDF-Seite, welche analysiert werden soll
				// Liste aller gefundenen Werte auf dem entsprechenden Analyse-Level
				w = t.getWords(image, analysLevel);
				// Wir holen die Wörter, welche nur einmal vorkommen für die Orientierrung
				uWforRotation = singleWords(w);
				// Liste von Wörterpaaren (initFile-Word, ScannedSIteWord) welche unique sind.
				wordPairs = sameWords(this.uniquWords, uWforRotation);// bisher
				if (debugen) {
					System.out.println("w " + w.size() + " - " + "uWforRotation " + uWforRotation.size() + " - "
							+ "wordPairs " + wordPairs.size());
				}
				if (wordPairs.size() == 0) {
					break;
				}

				for (List<Word> wl : wordPairs) {

					int Swl_x = (int) wl.get(1).getBoundingBox().getX();
					int Swl_y = (int) wl.get(1).getBoundingBox().getY();
					int Swl_w = (int) wl.get(1).getBoundingBox().getWidth();
					int Swl_h = (int) wl.get(1).getBoundingBox().getHeight();
					BufferedImage bi = image.getSubimage(Swl_x, Swl_y, Swl_w, Swl_h);
					if (debugDrawing) {
						Graphics2D g2d = image.createGraphics();
						g2d.setColor(Color.RED);
						g2d.drawRect(Swl_x, Swl_y, Swl_w, Swl_h);
						g2d.dispose();
					}
				}
				image = calibration(image, w, wordPairs);
				if (image == null) {
					continue;
				}
				w = t.getWords(image, analysLevel);
				uWforRotation = singleWords(w);
				wordPairs = sameWords(this.uniquWords, uWforRotation);// bisher

				if (debugDrawing) {
					for (List<Word> wl : wordPairs) {
						int Owl_x = (int) wl.get(0).getBoundingBox().getX();
						int Owl_y = (int) wl.get(0).getBoundingBox().getY();
						int Owl_w = (int) wl.get(0).getBoundingBox().getWidth();
						int Owl_h = (int) wl.get(0).getBoundingBox().getHeight();

						Graphics2D g2do = image.createGraphics();
						g2do.setColor(Color.BLACK);
						g2do.drawRect(Owl_x, Owl_y, Owl_w, Owl_h);
						g2do.dispose();
					}
				}
			}
			auswertung.addAll(doEvaluation(image, this.groupedRectangles, wordPairs));

			try {
				for (int i = 0; i < auswertung.size(); i++) {
					evaluation[i][auswertung.get(i)]++;
				}
				if (debugen) {
					ImageIO.write(image, "JPEG", new File(initPath + "pdf_umfragen/Pics/P" + xx + "Final.jpg"));
				}
				auswertung.clear();
			} catch (Exception e) {
				System.out.println("Exception");
			}
		}
		return evaluation;
		// https://stackoverflow.com/questions/39420986/java-tesseract-return-co-ordinates-of-text-location
		// https://stackabuse.com/tesseract-simple-java-optical-character-recognition/
	}

	/**
	 * Gibt eine Liste von den Wörter zurück, welche nur einmal im
	 * Dokument(Wörter-Liste) vorkommt
	 *
	 * @param list
	 * @return
	 */
	private HashMap<String, Word> singleWords(List<Word> list) {
		HashMap<String, Word> singleWords = new HashMap<String, Word>();
		HashMap<String, String> deletWords = new HashMap<String, String>();
		for (Word w : list) {
			if (w.getText().length() < minWordLength) { // Wörter < 3 werden nicht berücksichtig
				continue;
			}
			if (singleWords.containsKey(w.getText())) {
				deletWords.put(w.getText(), w.getText());
			} else {
				singleWords.put(w.getText(), w);
			}
		}
		HashMap<String, Word> output = new HashMap<String, Word>();
		for (Map.Entry<String, Word> e : singleWords.entrySet()) {
			if (!deletWords.containsKey(e.getKey())) {
				output.put(e.getKey(), e.getValue());
			}
		}
		return output;
	}

	/**
	 * Gibt eine 2D-Array von Wörter zurück, welche in beiden übergebenen Listen
	 * vorkommen.
	 *
	 * @param orig
	 * @param scanned
	 * @return
	 */
	private List<List<Word>> sameWords(HashMap<String, Word> orig, HashMap<String, Word> scanned) {
		List<List<Word>> output = new ArrayList<List<Word>>();
		for (Map.Entry<String, Word> e : scanned.entrySet()) {
			if (orig.containsKey(e.getKey())) {
				List<Word> temp = new ArrayList<Word>();
				temp.add(orig.get(e.getKey()));// orig
				temp.add(e.getValue());// scann
				output.add(temp);
			}
		}
		return output;
	}

	/**
	 * @TODO: Implementierung der Ausrichtung des Bildes anhand der Wörter
	 * @param img
	 * @param cwl
	 */
	private BufferedImage calibration(BufferedImage img, List<Word> w, List<List<Word>> cwl) {
		BufferedImage bi = img;
		if (cwl.size() > 1) {
			Double rotation = calcRotation(w, cwl);
			bi = rotate(bi, rotation, cwl.get(0).get(1).getBoundingBox().getX(),
					cwl.get(0).get(1).getBoundingBox().getY());
		} else {
			System.out.println("!!!!!!!!!!!!!!!!!!! keine Wortpaare zum Ausrichten!");
			bi = null;
		}
		return bi;
	}

	/**
	 * Gibt den Rotationswinkeln zurück. Anhand der Wortliste wird verglichen, wie
	 * das Original zum gescannten liegt. Dies anhand 3 Wörter(Anfang, Mitte,
	 * Schluss)
	 *
	 * @param cwl
	 * @return
	 */
	private double calcRotation(List<Word> wl, List<List<Word>> cwl) {
		Double finalRotation = 0.0;
		if (debugen) {
			System.out.println("calcRotation    --> start " + cwl.size());
		}
		/*
		 * 1. Anhand dem selben Wörtern aus cwl eine Rotation berechnen.
		 */
		Word wO1 = cwl.get(0).get(0);
		Word wS1 = cwl.get(0).get(1);
		ArrayList<Double> diff = new ArrayList<Double>();
		int i = 0;
		for (int j = 1; j < cwl.size(); j++) {
			Word wO = cwl.get(j).get(0);
			Word wS = cwl.get(j).get(1);
			if (wS.getConfidence() < confidence) {
				continue;
			}
			i++;
			double angleO = getWordAngle(wO1, wO);
			double angleS = getWordAngle(wS1, wS);
			diff.add(angleO - angleS);
			finalRotation += angleO - angleS;
		}
		finalRotation = finalRotation / i;
		if(finalRotation.isNaN()) {
			finalRotation = 0.0;
		}
		return finalRotation;
	}

	private double getWordAngle(Word w1, Word w2) {
		return getAngle(w1.getBoundingBox().getCenterX() - w2.getBoundingBox().getCenterX(),
				w1.getBoundingBox().getCenterY() - w2.getBoundingBox().getCenterY());
	}

	private BufferedImage resize(BufferedImage img, List<List<Word>> cwl) {
		BufferedImage bi = img;
		// Distanz
		Word wOrig1 = cwl.get(0).get(0);
		Word wOrig2 = cwl.get(cwl.size() - 1).get(0);
		Word wScan1 = cwl.get(0).get(1);
		Word wScan2 = cwl.get(cwl.size() - 1).get(1);

		Double distanceBetwOrig = distWords(wOrig1, wOrig2);
		Double distanceBetwScan = distWords(wScan1, wScan2);

		Double calVal = distanceBetwScan * 100 / distanceBetwOrig;
		calVal = 100 / calVal;
		if (!calVal.isNaN()) {
			bi = scale(bi, calVal);
		}
		return bi;
	}

	/**
	 * Gibt ein rotiertes Bild zur�ck. Anhand des Winkels angle an der Position vom
	 * bilt (xCenter/yCenter)
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
/**
 * Gibt die Distanz zwischen den zwei übergebenen Wörter zurück
 * @param w1
 * @param w2
 * @return
 */
	private double distWords(Word w1, Word w2) {// Pythagoras
		return Math.sqrt(Math.pow(w1.getBoundingBox().getCenterX() - w2.getBoundingBox().getCenterX(), 2)
				+ Math.pow(w1.getBoundingBox().getCenterY() - w2.getBoundingBox().getCenterY(), 2));
	}


	/**
	 * scale image
	 */
	public BufferedImage scale(BufferedImage sbi, Double scale) {
		System.out.println("                             scale " + scale);
		int newH = (int) (sbi.getHeight() * scale);
		int newW = (int) (sbi.getWidth() * scale);
		Image tmp = sbi.getScaledInstance((int) (sbi.getWidth() * scale), (int) (sbi.getHeight() * scale),
				Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_3BYTE_BGR);// Sonsts wirds Rot mit einem
																							// ander Type
		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

	private ArrayList<Integer> doEvaluation(BufferedImage img, ArrayList<List<Rectangle>> gR,
			List<List<Word>> wordPairs) {
		ArrayList<Integer> eval = new ArrayList<Integer>();
		if (wordPairs.size() < 2) {
			return eval;
		}
		Rectangle wO = wordPairs.get(wordPairs.size() - 1).get(0).getBoundingBox();
		Rectangle wS = wordPairs.get(wordPairs.size() - 1).get(1).getBoundingBox();
		Point ausgleich = new Point((int) ((wO.getX() - wS.getX()) * 1), (int) ((wO.getY() - wS.getY()) * 1));
		for (List<Rectangle> lr : gR) {
			eval.add(getChecked(img, lr, ausgleich));
		}

		return eval;
	}
/**
 * Wendet die übergebenen Rechtecke auf das übergebene Bild an. Richtet hierbei die Position der Auswertung
 * anhand der Korrektur aus.
 * @param img
 * @param gR
 * @param ausrichtung
 * @return
 */
	private int getChecked(BufferedImage img, List<Rectangle> gR, Point ausrichtung) {
		// Um Probleme mit den R�ndern zu entgehen, schauen wir nur den inneren
		// Teil an.
		// Somit schauen wir nur 60% des Feldes an.
		long which_is_choosed = 255 * 3;// Weiss
		int position = 0;
		int i = 0;
		for (Rectangle r : gR) {
			BufferedImage zelle = img.getSubimage((int) (r.getX() - ausrichtung.getX() + (r.getWidth() * 0.2)),
					(int) (r.getY() - ausrichtung.getY() + (r.getHeight() * 0.2)), (int) (r.getWidth() * 0.6),
					(int) (r.getHeight() * 0.6));
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
			if (debugen) {
				try {
					Graphics2D g2d = img.createGraphics();
					g2d.setColor(Color.RED);
					g2d.drawRect((int) (r.getX() - ausrichtung.getX()), (int) (r.getY() - ausrichtung.getY()),
							(int) r.getWidth(), (int) r.getHeight());
					g2d.dispose();
					ImageIO.write(zelle, "JPEG", new File(initPath + "pdf_umfragen/Pics/" + i + ".jpg"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return position;
	}

}