package pl.phytia.ann.functions.neuron;

import pl.phytia.model.conf.functions.neuron.NeuronFunctionConfiguration;

/**
 * Funkcja aktywacji neuronu nie zmieniająca sygnału.
 * 
 * @author Jarosław Protasiewicz
 */
public final class PipeFunction extends
		NeuronActivationFunction<NeuronFunctionConfiguration, PipeFunction> {

	private static final long serialVersionUID = -8713283002212901570L;

	/**
	 * Domyślny konstruktor.
	 */
	public PipeFunction() {
		super();
	}

	@Override
	public double maxReturnValue() {
		// return Double.MAX_VALUE;
		return 0;
	}

	@Override
	public double minReturnValue() {
		// return Double.MIN_VALUE;
		return 1;
	}

	@Override
	public double computeActivation(double sum) {
		return sum;
	}

	@Override
	public double computeDerivative(double sum) {
		return 0;
	}

}
