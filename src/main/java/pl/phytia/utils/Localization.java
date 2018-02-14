package pl.phytia.utils;

import java.text.DateFormat;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Klasa zawierająca stałe lokalizacyjne.
 * 
 * @author Jarosław Protasiewicz
 */
public class Localization {

	/**
	 * Polska lokalizacja.
	 */
	public static Locale plLocale = new Locale("pl", "pl");

	/**
	 * Polski format daty.
	 */
	public static DateFormat plDateFormatMedium = DateFormat.getDateInstance(
			DateFormat.MEDIUM, plLocale);

	/**
	 * Logger komunikatów dla apliakcji.
	 */
	public static Logger logger = Logger.getLogger("Phytia");
}
