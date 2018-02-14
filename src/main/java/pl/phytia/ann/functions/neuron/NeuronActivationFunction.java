package pl.phytia.ann.functions.neuron;

import pl.phytia.model.conf.Configuration;

/**
 * Abstrakcyjna klasa bazowa dla funkcji aktywacji neuronów.
 * 
 * @author Jarosław Protasiewicz
 * @param <C>
 *            Typ obiektu z parametrami konfiguracyjnymi
 * @param <S>
 *            Typ obietu zwaracanego przez <code>this</code>
 */
public abstract class NeuronActivationFunction<C extends Configuration, S extends NeuronActivationFunction>
		extends NeuronFunction<C, S> {

	/**
	 * Wylicza wartość pochodnej funkcji aktywacji w punkcie.
	 * 
	 * @param sum
	 *            Wartość sumy neuronu.
	 * @return Wartość pochodnej
	 */
	public abstract double computeDerivative(double sum);

	/**
	 * Wylicza warrtość funkcji aktywacji dla sumy neuronu
	 * 
	 * @param sum
	 *            Wartość sumy neuronu.
	 * @return Wartość aktywacji funkcji
	 */
	public abstract double computeActivation(double sum);

}
