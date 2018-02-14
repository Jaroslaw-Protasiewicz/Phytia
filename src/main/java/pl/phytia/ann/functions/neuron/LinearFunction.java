package pl.phytia.ann.functions.neuron;

import pl.phytia.model.conf.functions.neuron.LinearFunctionConfiguration;

/**
 * Liniowa funkcja aktywacji neuronu z nasyceniem.
 * 
 * @author Jarosław Protasiewicz
 */
public final class LinearFunction extends
		NeuronActivationFunction<LinearFunctionConfiguration, LinearFunction> {

	private static final long serialVersionUID = 8502999274753104414L;

	/**
	 * Domyślny konstruktor
	 */
	public LinearFunction() {
		super();
		config = new LinearFunctionConfiguration();

	}

	@Override
	public double computeActivation(double sum) {
		double out = config.getGradeRatio() * sum;
		return Math.max(config.getMinActivation(), Math.min(out, config
				.getMaxActivation()));
	}

	@Override
	public double computeDerivative(double sum) {
		return config.getGradeRatio();
	}

	@Override
	public double maxReturnValue() {
		return config.getMaxActivation();
	}

	@Override
	public double minReturnValue() {
		return config.getMinActivation();
	}

}
