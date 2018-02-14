package pl.phytia.utils;

import java.util.Calendar;
import java.util.Random;

import pl.phytia.model.sets.DoubleVector;

/**
 * Klasa pomocnicza zawierająca funkcje matematyczne.
 * 
 * @author Michał Jeleń, Jarosław Protasiewicz
 */
public class MathUtil {

	/* -------------- FIELDS ------------- */
	/**
	 * Gerator liczb.
	 */
	private Random random = null;

	/**
	 * Instancja klasy.
	 */
	private static MathUtil instance = null;

	/* --------------- METHODS -------------- */
	/**
	 * Domyślny konstruktor.
	 */
	private MathUtil() {
		random = new Random(Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * Tworzenie instacji.
	 * 
	 * @return Instancja klasy <code>MathUtil</code>.
	 */
	public static MathUtil getInstance() {
		if (instance == null)
			instance = new MathUtil();
		return instance;
	}

	/**
	 * Losuje pseudolosową wartość typu <code>double</code> z podanego zakresu
	 * 
	 * @param min
	 *            Dolna wartość zakresu
	 * @param max
	 *            Górna wartość zakresu
	 * @return Wartość wylosowana.
	 */
	public static double getRandomDouble(double min, double max) {
		return changeRange(getInstance().random.nextDouble(), 0, 1, min, max);
	}

	/**
	 * Losuje pseudolosową wartość typu <code>int</code> z podanego zakresu.
	 * 
	 * @param min
	 *            Dolna wartość zakresu
	 * @param max
	 *            Górna wartość zakresu
	 * @return Wartość wylosowana.
	 */
	public static int getRandomInt(int min, int max) {
		return getInstance().random.nextInt(max + 1 - min) + min;
	}

	/**
	 * Losuje pseudolosową wartość typu <code>int</code> z ograniczeniem
	 * górnym.
	 * 
	 * @param max
	 *            Górna wartość zakresu
	 * @return Wartość wylosowana.
	 */
	public static int getRandomInt(int max) {
		return getInstance().random.nextInt(max + 1);
	}

	/**
	 * Rzutuje wartość z jednego zakresu liczb na inny zakres liczb.
	 * 
	 * @param value
	 *            Rzutowana wartość
	 * @param minSrc
	 *            Dolna wartość przedziału źródłowego
	 * @param maxSrc
	 *            Górna wartość przedziału źródłowego
	 * @param minDest
	 *            Dolna wartość przedziału docelowego
	 * @param maxDest
	 *            Górna wartość przedziału docelowego
	 * @return Przekonwertowana wartość
	 */
	public static double changeRange(double value, double minSrc,
			double maxSrc, double minDest, double maxDest) {
		return (double) (value - minSrc) * (maxDest - minDest)
				/ (double) (maxSrc - minSrc) + minDest;
	}

	/**
	 * Podaje rónicę w procentach pomiędzy dwoma liczbami
	 */
	public static double percentDiff(double d1, double d2) {
		if (d1 == d2)
			return 0;
		if (d2 == 0)
			return d1 * 100.0;

		return (1.0 - (d1 / d2)) * 100.0;
	}

	/**
	 * Podaje różnicę w procentach pomiędzy dwoma wektorami liczb
	 */
	public static double percentDiff(DoubleVector p1, DoubleVector p2) {
		int len = Math.min(p1.size(), p2.size());
		double diff = 0;
		for (int i = 0; i < len; i++) {
			diff += Math.abs(percentDiff(p1.get(i), p2.get(i)));
		}
		diff += 100.0 * Math.abs(p1.size() - p2.size());

		return diff / (double) len;
	}

	public static double ape(double re, double pred) {
		// return pe(re,pred)*100;
		return Math.abs(pe(re, pred));
	}

	public static double pe(double re, double pred) {
		// return Math.abs((re - pred)/re);
		return error(re, pred) * 100;
	}

	public static double error(double re, double pred) {
		return (re - pred) / re;
	}
}
