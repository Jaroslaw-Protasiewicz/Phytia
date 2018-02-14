package pl.phytia.ann.functions.neighbourhood;

import pl.phytia.ann.functions.Function;
import pl.phytia.model.conf.functions.neighbourhood.NeighbourhoodFunctionConfiguration;

/**
 * Abstrakcyjna funkcja sąsiedztwa.
 * 
 * @author Jarosław Protasiewicz
 * @param <C>
 *            Typ obiektu z parametrami konfiguracyjnymi
 * @param <S>
 *            Typ obietu zwaracanego przez <code>this</code>
 */
public abstract class NeighbourhoodFunction<C extends NeighbourhoodFunctionConfiguration, S extends NeighbourhoodFunction>
		extends Function<C, S> {

	/**
	 * Aktualny rozmiar sąsiedztwa.
	 */
	private double currNeighbourhoodSize;

	/**
	 * Zmiana (zmniejszenie) rozmiaru sąsiedztwa topologicznego.
	 * 
	 * @param currEpoch
	 *            Numer epoki pracy algorytmu.
	 */
	public abstract void changeNeighbourhood(int currEpoch);

	/**
	 * @return wartość pola currNeighbourhoodSize
	 */
	public double getCurrNeighbourhoodSize() {
		return currNeighbourhoodSize;
	}

	/**
	 * @param currNeighbourhoodSize
	 *            jest przypisywany do pola currNeighbourhoodSize
	 */
	public void setCurrNeighbourhoodSize(double currNeighbourhoodSize) {
		this.currNeighbourhoodSize = currNeighbourhoodSize;
	}
}
