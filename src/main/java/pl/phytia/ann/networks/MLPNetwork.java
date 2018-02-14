package pl.phytia.ann.networks;

import java.util.ArrayList;

import pl.phytia.ann.layers.Layer;
import pl.phytia.model.conf.networks.MLPNetworkConfiguration;
import pl.phytia.ann.layers.MLPLayer;
import pl.phytia.model.sets.DoubleVector;

/**
 * Wielowarstwowa sieć perceptronowa.
 * 
 * @author Jarosław Protasiewicz
 */
public final class MLPNetwork extends
		Network<MLPNetwork, MLPNetworkConfiguration> {

	/**
	 * Konstruktor bezparametrowy.
	 */
	public MLPNetwork() {
		super();
	}

	/**
	 * Konstruktor wielowarstwowej sieci perceptronowej. <br>
	 * Tworzy sieć i dokonuje jej inicjalizacji.
	 * 
	 * @param conf
	 *            Obiekt zawierający prarametry konfiguracyjne.
	 */
	public MLPNetwork(MLPNetworkConfiguration conf) {
		super();
		/*
		 * Utworzenie sieci.
		 */
		layers = new ArrayList<Layer>(conf.getLayersConf().size());
		for (int i = 0; i < conf.getLayersConf().size(); ++i) {
			layers.add(i, new MLPLayer(conf.getLayersConf().get(i)));
		}
		setConfig(conf);
		/*
		 * Inicjalizacja sieci.
		 */
		initialize(conf);
	}

	@SuppressWarnings("unchecked")
	public void initialize(MLPNetworkConfiguration conf) {
		setConfig(conf);
		for (int i = 0; i < conf.getLayersConf().size(); ++i) {
			layers.get(i).initialize(conf.getLayersConf().get(i));
		}
	}

	public void simulation(DoubleVector inputs) {
		for (int i = 0; i < layers.size(); ++i) {
			layers.get(i).simulation(inputs);
			inputs = layers.get(i).getOutputs();
		}
	}

	@Override
	public DoubleVector getOutputs() {
		return layers.get(config.getLayersConf().size() - 1).getOutputs();
	}

	@Override
	public double maxReturnValue() {
		double max = Double.MIN_VALUE;
		for (Layer n : getLayers()) {
			if (max < n.maxReturnValue()) {
				max = n.maxReturnValue();
			}
		}
		return max;
	}

	@Override
	public double minReturnValue() {
		double min = Double.MAX_VALUE;
		for (Layer n : getLayers()) {
			if (min > n.minReturnValue()) {
				min = n.minReturnValue();
			}
		}
		return min;
	}

	@Override
	public void setTheBestWeights(boolean reverse) {
		for (int i = 0; i < this.layers.size(); ++i) {
			((MLPLayer) this.layers.get(i)).setTheBestWeights(reverse);
		}

	}
}
