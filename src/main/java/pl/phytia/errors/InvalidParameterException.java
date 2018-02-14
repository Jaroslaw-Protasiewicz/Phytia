package pl.phytia.errors;

public class InvalidParameterException extends PhytiaException {

	/**
	 * @param message
	 */
	public InvalidParameterException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 4748806134174066385L;

	public InvalidParameterException(String message, Throwable cause) {
		super(message, cause);
	}

}
