package pl.phytia.ann.functions.neuron;

import pl.phytia.ann.functions.Function;
import pl.phytia.model.conf.Configuration;

/**
 * Abstrakcyjna klasa reprezentująca funkcję neuronu. <br>
 * Modeluje właściwości jądra neuronu bologicznego.
 * 
 * @author Jarosław Protasiewicz
 * @param <C>
 *            Typ obiektu z parametrami konfiguracyjnymi
 * @param <S>
 *            Typ obietu zwaracanego przez <code>this</code>
 */
public abstract class NeuronFunction<C extends Configuration, S extends NeuronFunction>
		extends Function<C, S> {

	/**
	 * Zwraca minimalną wartość zwracaną przez funkcję.
	 */
	public abstract double minReturnValue();

	/**
	 * Zwraca maksymalną wartość zwracaną przez funkcję.
	 */
	public abstract double maxReturnValue();

}
