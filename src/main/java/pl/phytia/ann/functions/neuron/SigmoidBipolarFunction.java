package pl.phytia.ann.functions.neuron;

import pl.phytia.model.conf.functions.neuron.SBipolarFunctionConfiguration;

/**
 * Bibolarna sigmoidalna funkcja aktywacji neuronu.
 * 
 * @author Jarosław Protasiewicz
 */
public final class SigmoidBipolarFunction extends
		SigmoidFunction<SBipolarFunctionConfiguration, SigmoidBipolarFunction> {

	private static final long serialVersionUID = -3979965333229917194L;

	/**
	 * Domyślny konstruktor.
	 */
	public SigmoidBipolarFunction() {
		super();
	}

	@Override
	public double computeActivation(double sum) {
		return ((1 - Math.exp(-sum * config.getBetaRatio())) / (1 + Math
				.exp(-sum * config.getBetaRatio())));
	}

	@Override
	public double computeDerivative(double sum) {
		double output = (1 - Math.exp(-sum * config.getBetaRatio()))
				/ (1 + Math.exp(-sum * config.getBetaRatio()));
		return (1 - Math.pow(output, 2));
	}

	@Override
	public double maxReturnValue() {
		return 1;
	}

	@Override
	public double minReturnValue() {
		return -1;
	}

}
