package pl.phytia.model.sets;

import pl.phytia.utils.Normalizer;

/**
 * Interfejs dla obiektów, które mogą być normalizowane.
 * 
 * @author Jarosław Protasiewicz
 */
public interface Normalizable {

	/**
	 * Zapytanie o stan normalizacji
	 * 
	 * @return true - obiekt jest znormalizowany, false - nie jest
	 *         znormalizwoany.
	 */
	public abstract boolean isNormalized();

	/**
	 * Normalizacja danych
	 * 
	 * @param normalizer
	 *            Normalizator.
	 */
	public abstract void normalize(Normalizer normalizer);

	/**
	 * Denormalizacja danych
	 * 
	 * @param normalizer
	 *            Normalizator.
	 */
	public abstract void denormalize(Normalizer normalizer);

	/**
	 * Znajduje wartość minimalną zbioru danych.
	 * 
	 * @return Najmnijesza wartość.
	 * 
	 */
	public abstract double min();

	/**
	 * Znajduje wartość maksymalną zbioru danych.
	 * 
	 * @return Największa wartość.
	 */
	public abstract double max();
}
