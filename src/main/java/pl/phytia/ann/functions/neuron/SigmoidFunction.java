package pl.phytia.ann.functions.neuron;

import pl.phytia.model.conf.Configuration;

/**
 * Abstrakcyjna funkcja sigmoidalna
 * 
 * @param <C>
 *            Typ obiektu z parametrami konfiguracyjnymi
 * @param <S>
 *            Typ obietu zwaracanego przez <code>this</code>
 * @author Jarosław Protasiewicz
 */
public abstract class SigmoidFunction<C extends Configuration, S extends NeuronActivationFunction>
		extends NeuronActivationFunction<C, S> {

	/**
	 * Domyślny konstruktor
	 */
	public SigmoidFunction() {
		super();
	}

}
