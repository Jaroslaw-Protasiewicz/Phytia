package pl.phytia.ann.neurons;

import pl.phytia.ann.functions.neuron.PipeFunction;
import pl.phytia.model.conf.functions.neuron.AdderFunctionConfiguration;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.utils.MathUtil;

/**
 * Neuron - sumator.
 * 
 * @author Jarosław Protasiewicz
 */
public class AdderNeuron extends Adder<AdderNeuron, AdderFunctionConfiguration> {

	private static final long serialVersionUID = 8434296890861816941L;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public AdderNeuron() {
		super();
	}

	/**
	 * Konstruktor tworzący i inicjalizujący sumator.
	 * 
	 * @param conf
	 *            Konfiguracja.
	 */
	public AdderNeuron(AdderFunctionConfiguration conf) {
		setFunction(new PipeFunction());
		weights = new DoubleVector(conf.getNumberOfInputs(), 0.0);
		initialize(conf);
	}

	@SuppressWarnings("static-access")
	@Override
	public void initializeWeights(AdderFunctionConfiguration conf) {
		MathUtil m = MathUtil.getInstance();
		for (int i = 0; i < conf.getNumberOfInputs(); ++i) {
			weights.set(i, m.getRandomDouble(function.minReturnValue(),
					function.maxReturnValue()));
		}
	}
}
