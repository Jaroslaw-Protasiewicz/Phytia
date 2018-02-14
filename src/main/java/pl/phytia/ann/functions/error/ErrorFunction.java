package pl.phytia.ann.functions.error;

import pl.phytia.ann.functions.Function;
import pl.phytia.ann.networks.Network;
import pl.phytia.model.conf.Configuration;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.SupervisedDataSet;

/**
 * Abstracyjna klasa funkcji błędu.
 * @param <C>	Typ obiektu z parametrami konfiguracyjnymi
 * @param <S>	Typ obietu zwaracanego przez <code>this</code>
 * @author Jarosław Protasiewicz
 */
/**
 * @author jprotas
 * 
 * @param <C>
 * @param <S>
 */
public abstract class ErrorFunction<C extends Configuration, S extends Function>
		extends Function<C, S> {

	/**
	 * Domyślny konstruktor
	 */
	public ErrorFunction() {
		super();
		this.error = 9e10d;
		this.recentError = this.error;
		this.theBestError = this.error - 1;
	}

	/**
	 * Błąd wyznaczony przez funkcję.
	 */
	private double error;

	/**
	 * Poprzedni błąd wyznaczony przez funkcję.
	 */
	private double recentError;

	/**
	 * Błąd najlepszego dopasowania.
	 */
	private double theBestError;

	/**
	 * Wyznaczenie błędu pomiędzy odpowiedzią sieci a zbiorem uczącym
	 * 
	 * @param net
	 *            Sieć neuronowa.
	 * @param set
	 *            Zbiór danych.
	 * @return Błąd.
	 */
	public abstract double computeError(Network net, SupervisedDataSet set);

	/**
	 * Wyznaczenie błędu pomiędzy odpowiedzią sieci a wzorcem
	 * 
	 * @param net
	 *            Sieć neuronowa.
	 * @param pattern
	 *            Wzorzec.
	 * @return Błąd.
	 */
	public abstract double computeError(Network net, DoubleVector pattern);

	/**
	 * @return wartość pola error
	 */
	public double getError() {
		return error;
	}

	/**
	 * @param error
	 *            jest przypisywany do pola error
	 */
	public void setError(double error) {
		this.error = error;
	}

	/**
	 * @return wartość pola recentError
	 */
	public double getRecentError() {
		return recentError;
	}

	/**
	 * @param recentError
	 *            jest przypisywany do pola recentError
	 */
	public void setRecentError(double recentError) {
		this.recentError = recentError;
	}

	/**
	 * @return wartość pola theBestError
	 */
	public double getTheBestError() {
		return theBestError;
	}

	/**
	 * @param theBestError
	 *            jest przypisywany do pola theBestError
	 */
	public void setTheBestError(double theBestError) {
		this.theBestError = theBestError;
	}

}
