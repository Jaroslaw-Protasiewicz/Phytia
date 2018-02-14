package pl.phytia.errors;

public class ConfigurationException extends PhytiaException {

	/**
	 * @param message
	 */
	public ConfigurationException(String message) {
		super(message);
	}

	private static final long serialVersionUID = -815557460981391064L;

	/**
	 * @param message
	 * @param cause
	 */
	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
