package pl.phytia.utils;

/**
 * Normalizator realizujący normalizację i denormalizację danych.
 * 
 * @author Jarosław Protasiewicz
 */
public class Normalizer {
	/**
	 * Minimalna wartość ze zbioru przed normalizacją.
	 */
	private double minBeforeNorm;

	/**
	 * Maksymalna wartość ze zbioru przed normalizacją.
	 */
	private double maxBeforeNorm;

	/**
	 * Minimalna wartość ze zbioru znormalizowanego.
	 */
	private double minAfterNorm;

	/**
	 * Maksymalna wartość ze zbioru znormalizowanego.
	 */
	private double maxAfterNorm;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public Normalizer() {
		super();
	}

	/**
	 * Inicjalizacja.
	 * 
	 * @param minAfterNorm
	 *            Minimalna wartość ze zbioru znormalizowanego.
	 * @param maxAfterNorm
	 *            Maksymalna wartość ze zbioru znormalizowanego.
	 * @param minBeforeNorm
	 *            Minimalna wartość ze zbioru przed normalizacją.
	 * @param maxBeforeNorm
	 *            Maksymalna wartość ze zbioru przed normalizacją.
	 */
	public void initialize(double minAfterNorm, double maxAfterNorm,
			double minBeforeNorm, double maxBeforeNorm) {
		this.minBeforeNorm = minBeforeNorm;
		this.maxBeforeNorm = maxBeforeNorm;
		this.minAfterNorm = minAfterNorm;
		this.maxAfterNorm = maxAfterNorm;
	}

	/**
	 * Normalizacja liczby zmiennoprzecinkowej.
	 * 
	 * @param value
	 *            Wartość przed normalizacją.
	 * @return Wartość po normalizacji.
	 */
	public double normalize(double value) {
		return MathUtil.changeRange(value, minAfterNorm, maxAfterNorm,
				minBeforeNorm, maxBeforeNorm);
	}

	/**
	 * Denormalizacja liczby zmiennoprzecinkowej.
	 * 
	 * @param value
	 *            Wartość przed denormalizacją.
	 * @return Wartość po denormalizacji.
	 */
	public double denormalize(double value) {
		return MathUtil.changeRange(value, minBeforeNorm, maxBeforeNorm,
				minAfterNorm, maxAfterNorm);
	}
}
