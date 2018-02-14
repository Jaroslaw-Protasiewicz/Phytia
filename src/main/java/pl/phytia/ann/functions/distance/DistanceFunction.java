package pl.phytia.ann.functions.distance;

import pl.phytia.ann.functions.Function;
import pl.phytia.model.conf.Configuration;
import pl.phytia.model.sets.DoubleVector;

/**
 * Abstrakcyjna miara odległości pomiędzy wektorami N wymiarowymi.
 * 
 * @author Jarosław Protasiewicz
 * 
 * @param <S>
 *            Typ obietu zwaracanego przez <code>this</code>
 */
public abstract class DistanceFunction<S extends DistanceFunction> extends
		Function<Configuration, S> {

	/**
	 * Wyznacza odległość pomiędzy dwoma punktami.
	 * 
	 * @param pointOne
	 *            Pierwszy punkt.
	 * @param pointTwo
	 *            Drugi punkt
	 * @return Wyznaczona odległość.
	 */
	public abstract double computeDistance(DoubleVector pointOne,
			DoubleVector pointTwo);

	/**
	 * Sprawdza poprawność argumentów funkcji.
	 * 
	 * @param pointOne
	 *            Pierwszy punkt.
	 * @param pointTwo
	 *            Drugi punkt
	 */
	protected void checkArgumets(DoubleVector pointOne, DoubleVector pointTwo) {
		if (pointOne.size() != pointTwo.size()) {
			throw new IllegalArgumentException(
					"Wektory muszą być tego samego rozmiaru :"
							+ pointOne.size() + "!=" + pointTwo.size());
		}
	}
}
