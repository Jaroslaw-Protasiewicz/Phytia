package pl.phytia.ann.functions.neuron;

import pl.phytia.model.conf.functions.neuron.FahlmanFunctionConfiguration;

/**
 * Sigmoidalna funkcja aktyacji neuronu z modyfikacją Fahlmana.
 * 
 * @author Jarosław Protasiewicz
 */
public final class SigmoidFahlmanFunction extends
		SigmoidFunction<FahlmanFunctionConfiguration, SigmoidFahlmanFunction> {

	private static final long serialVersionUID = 9044759666084013208L;

	/**
	 * Domyślny konstruktor.
	 */
	public SigmoidFahlmanFunction() {
		super();
		config = new FahlmanFunctionConfiguration();
	}

	/**
	 * @return wartość pola config
	 */
	public FahlmanFunctionConfiguration getConfig() {
		return config;
	}

	/**
	 * @param config
	 *            jest przypisywany do pola config
	 */
	public void setConfig(FahlmanFunctionConfiguration config) {
		this.config = config;
	}

	@Override
	public double computeActivation(double sum) {
		return (1 / (1 + Math.exp(-sum * config.getBetaRatio())));
	}

	@Override
	public double computeDerivative(double sum) {
		double output = computeActivation(sum);
		return (output * (1 - output)) + config.getDerivativeRatio();
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
