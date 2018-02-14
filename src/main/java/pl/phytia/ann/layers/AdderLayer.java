package pl.phytia.ann.layers;

import java.util.ArrayList;
import java.util.List;

import pl.phytia.model.conf.layers.AdderLayerConfiguration;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.ann.neurons.AdderNeuron;

/**
 * Warstwa sumująca sieci neuronowej.
 * 
 * @author Jarosław Protasiewicz
 * 
 */
public class AdderLayer extends Layer<AdderLayer, AdderLayerConfiguration> {

	/**
	 * Neurony - sumatory.
	 */
	private List<AdderNeuron> neurons;

	public AdderLayer() {
		super();
	}

	/**
	 * Tworzy warstwę sumatorów sieci z zainicjalozowanymi neuronami.
	 * 
	 * @param conf
	 */
	public AdderLayer(AdderLayerConfiguration conf) {
		super();
		setConfig(conf);
	}

	public void initialize(AdderLayerConfiguration conf) {
		config = conf;
		neurons = new ArrayList<AdderNeuron>();
		for (int i = 0; i < conf.getNumberOfNeurons(); ++i) {
			neurons.add(new AdderNeuron(conf.getAdderConf()));
		}
	}

	public void simulation(DoubleVector inputs) {
		for (AdderNeuron n : getNeurons()) {
			n.simulation(inputs);
		}
	}

	@Override
	public DoubleVector getOutputs() {
		DoubleVector outs = new DoubleVector(config.getNumberOfNeurons(), 0.0);
		for (int i = 0; i < outs.size(); ++i) {
			outs.set(i, neurons.get(i).getOutput());
		}
		return outs;
	}

	@Override
	public double maxReturnValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double minReturnValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<AdderNeuron> getNeurons() {
		return neurons;
	}

	public void setNeurons(List<AdderNeuron> neurons) {
		this.neurons = neurons;
	}

}
