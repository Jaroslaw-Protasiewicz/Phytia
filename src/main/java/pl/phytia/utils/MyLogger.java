package pl.phytia.utils;

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * Definicja loggera wraz z konfiguracją.
 * 
 * @author Jarosław Protasiewicz
 * 
 */
public class MyLogger {

	/**
	 * Logger publiczny.
	 */
	public static Logger logger = Logger.getLogger("Phytia");

	/**
	 * Ustawia parametry loggera.
	 */
	public static void setLogger() {
		ConsoleAppender consoleAppender = new ConsoleAppender(
				new PatternLayout());
		FileAppender fileAppender = null;
		try {
			fileAppender = new FileAppender(new PatternLayout(),
					"e:\\tmp\\phytia.log", true);

		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.addAppender(consoleAppender);
		logger.addAppender(fileAppender);
		logger.setLevel((Level) Level.INFO);
		System.out.println("---- PYTHIA v. 1.0 START -- ");
	}
}
