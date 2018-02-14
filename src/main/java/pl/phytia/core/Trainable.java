package pl.phytia.core;

import pl.phytia.ann.networks.Network;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.DataSet;

/**
 * Interfejs dla obiektó, które mogą być trenowane.
 * 
 * @author Jarosław Protasiewicz
 * 
 * @param <D>
 *            Typ zbioru danych treningowych.
 * @param <N>
 *            Typ sieci neronowej.
 */
public interface Trainable<D extends DataSet, N extends Network> {

	/**
	 * 
	 * Trening sieci neuronowej
	 * 
	 * @param net
	 *            Sieć neuronowa.
	 * @param set
	 *            Zbiór danych treningowych.
	 * @author Jarosław Protasiewicz
	 */
	public abstract void training(N net, D set);

	/**
	 * Wyznaczenie nowych wag sieci.
	 */
	public abstract void updateWeights(N net, DoubleVector inputs);

	/**
	 * Sprawdzenie warunku stop.
	 * 
	 * @return
	 * <li>true - spełniony</li>
	 * <li>false - nie spełniony</li>
	 */
	public abstract boolean checkStopCondition(N net, D set);

	/**
	 * Aktualizuje parametry treningu sieci.
	 */
	public abstract void updateParams(N net, D set);

}
