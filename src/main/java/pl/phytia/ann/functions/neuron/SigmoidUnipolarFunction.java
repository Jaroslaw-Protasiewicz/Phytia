package pl.phytia.ann.functions.neuron;

import pl.phytia.model.conf.functions.neuron.SUnipolarFunctionConfiguration;

/**
 * Sigmoidalna unipolarna funkcja aktywacji neuronu.
 * 
 * @author jprotas
 */
public final class SigmoidUnipolarFunction
		extends
		SigmoidFunction<SUnipolarFunctionConfiguration, SigmoidUnipolarFunction> {

	private static final long serialVersionUID = -8745793028969729924L;

	/**
	 * Domyślny konstruktor.
	 */
	public SigmoidUnipolarFunction() {
		super();
	}

	@Override
	public double computeActivation(double sum) {
		return (1 / (1 + Math.exp(-sum * config.getBetaRatio())));

	}

	@Override
	public double computeDerivative(double sum) {
		double output = 1 / (1 + Math.exp(-sum * config.getBetaRatio()));
		return (output * (1 - output));
	}

	@Override
	public double maxReturnValue() {
		return 1;
	}

	@Override
	public double minReturnValue() {
		return 0;
	}
}
