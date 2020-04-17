package ch.zhaw.pdffunctionality;

public class Question {
	private String questionText;
	private int[] eval;
	public Question(String s, int[] eval) {
		this.setQuestionText(s);
		this.setEval(eval);
	}
	/**
	 * @return the eval
	 */
	public int[] getEval() {
		return eval;
	}
	/**
	 * @param eval the eval to set
	 */
	public void setEval(int[] eval) {
		this.eval = eval;
	}
	/**
	 * @return the questionText
	 */
	public String getQuestionText() {
		return questionText;
	}
	/**
	 * @param questionText the questionText to set
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	
	
	
}
