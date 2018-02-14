package pl.phytia.ann.layers;

import java.util.ArrayList;
import java.util.List;

import pl.phytia.ann.networks.NetConfig;
import pl.phytia.ann.neurons.Perceptron;
import pl.phytia.model.conf.functions.neuron.NeuronFunctionConfiguration;
import pl.phytia.model.conf.layers.MLPLayerConfiguration;
import pl.phytia.model.sets.DoubleVector;

/**
 * Warstwa sieci perceptronowej.
 * 
 * @author Jarosław Protasiewicz
 */
public final class MLPLayer extends Layer<MLPLayer, MLPLayerConfiguration> {

	/**
	 * Neurony znajdujące się w danej warstwie
	 */
	private List<Perceptron> neurons;

	/**
	 * Konstruktor bezparametrowy.
	 */
	public MLPLayer() {
		super();
	}

	/**
	 * Konstruktor warstwy sieci MLP.
	 * 
	 * @param conf
	 *            Obiekt zwierający konfigurację warstwy.
	 */
	public MLPLayer(MLPLayerConfiguration conf) {
		super();
		/*
		 * Utworzenie warstwy sieci.
		 */
		neurons = new ArrayList<Perceptron>(conf.getNumberOfNeurons());
		for (int i = 0; i < conf.getNumberOfNeurons(); ++i) {
			neurons.add(i, new Perceptron<NeuronFunctionConfiguration>(conf
					.getNeuronFunctionConf(), conf.getNumberOfInputs()));
		}
		setConfig(conf);
	}

	@SuppressWarnings("unchecked")
	public void initialize(MLPLayerConfiguration conf) {
		setConfig(conf);
		for (int i = 0; i < conf.getNumberOfNeurons(); ++i) {
			conf.getNeuronFunctionConf().setNumberOfInputs(
					conf.getNumberOfInputs());
			neurons.get(i).initialize(conf.getNeuronFunctionConf());
		}
	}

	public void simulation(DoubleVector inputs) {
		/*
		 * Uwzględnienie wejść BIAS.
		 */
		DoubleVector inputsWithBias = new DoubleVector(inputs.size() + 1);
		inputsWithBias.add(NetConfig.BIAS_INPUT);
		for (Double d : inputs) {
			inputsWithBias.add(d);
		}

		/*
		 * Symulacja wyszystkich neuronów
		 */
		for (Perceptron n : neurons) {
			n.simulation(inputsWithBias);
		}
	}

	/**
	 * Ustawia najlepsze wagi sieci.
	 */
	public void setTheBestWeights(boolean reverse) {
		for (Perceptron n : neurons) {
			n.setTheBestWeights(reverse);
		}
	}

	@Override
	public DoubleVector getOutputs() {
		DoubleVector outputs = new DoubleVector(config.getNumberOfNeurons());
		int i = 0;
		for (Perceptron n : neurons) {
			outputs.add(i++, n.getOutput());
		}
		return outputs;
	}

	@Override
	public double maxReturnValue() {
		double max = Double.MIN_VALUE;
		for (Perceptron n : neurons) {
			if (max < n.getFunction().maxReturnValue()) {
				max = n.getFunction().maxReturnValue();
			}
		}
		return max;
	}

	@Override
	public double minReturnValue() {
		double min = Double.MAX_VALUE;
		for (Perceptron n : neurons) {
			if (min > n.getFunction().minReturnValue()) {
				min = n.getFunction().minReturnValue();
			}
		}
		return min;
	}

	/**
	 * @return wartość pola neurons
	 */
	public List<Perceptron> getNeurons() {
		return neurons;
	}

	/**
	 * @param neurons
	 *            jest przypisywany do pola neurons
	 */
	public void setNeurons(List<Perceptron> neurons) {
		this.neurons = neurons;
	}

}
