package pl.phytia.errors;

public class PhytiaException extends RuntimeException {

	/**
	 * @param message
	 */
	public PhytiaException(String message) {
		super(message);
	}

	private static final long serialVersionUID = -5728385546283075413L;

	/**
	 * @param message
	 * @param cause
	 */
	public PhytiaException(String message, Throwable cause) {
		super(message, cause);
	}

}
