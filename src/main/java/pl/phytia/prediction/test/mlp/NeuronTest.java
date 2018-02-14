package pl.phytia.prediction.test.mlp;

import pl.phytia.model.sets.DoubleVector;
import pl.phytia.ann.functions.neuron.NeuronActivationFunction;
import pl.phytia.ann.neurons.Perceptron;
import pl.phytia.model.conf.functions.neuron.SUnipolarFunctionConfiguration;

/**
 * Test neuronu.
 * 
 * @author Jarosław Protasiewicz
 */
public class NeuronTest<F extends NeuronActivationFunction> {

	/**
	 * @param args
	 *            aa
	 */
	public static void main(String[] args) {
		SUnipolarFunctionConfiguration conf = new SUnipolarFunctionConfiguration();
		conf.setBetaRatio(0.7);
		Perceptron<SUnipolarFunctionConfiguration> n = new Perceptron<SUnipolarFunctionConfiguration>(
				conf, 5);
		DoubleVector inputs = new DoubleVector(5);
		inputs.add(1.0);
		inputs.add(1.0);
		inputs.add(1.0);
		inputs.add(1.0);
		inputs.add(1.0);
		n.simulation(inputs);
		System.out.println(n.getOutput());

	}

}
