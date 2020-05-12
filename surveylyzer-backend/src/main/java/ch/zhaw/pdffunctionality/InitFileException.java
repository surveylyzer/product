package ch.zhaw.pdffunctionality;

public class InitFileException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InitFileException(String errMsg) {
		super("InitFile: " + errMsg);
	}
	public InitFileException(Throwable err) {
		super(err);
	}
}
