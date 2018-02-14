package pl.phytia.ann.networks;

import java.util.ArrayList;
import java.util.List;

import pl.phytia.ann.layers.AdderLayer;
import pl.phytia.ann.layers.Layer;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.ann.layers.RbfLayer;
import pl.phytia.model.conf.networks.RbfNetworkConfiguration;

/**
 * Sieć o radialnych funkcjach bazowych.
 * 
 * @author Jarosław Protasiewicz
 */
public class RBFNetwork extends Network<RBFNetwork, RbfNetworkConfiguration> {

	/**
	 * Konstruktor bezparametrowy.
	 */
	public RBFNetwork() {
		super();
	}

	/**
	 * Konstruktor sieci o radialnych funkcjach bazowych. <br>
	 * Tworzy sieć i dokonuje jej inicjalizacji.
	 * 
	 * @param conf
	 *            Obiekt zawierający prarametry konfiguracyjne.
	 */
	public RBFNetwork(RbfNetworkConfiguration conf) {
		super();
		// Utworzenie sieci.
		layers = new ArrayList<Layer>(2);
		layers.add(new RbfLayer(conf.getRbfLayerConf()));
		layers.add(new AdderLayer(conf.getAdderLayerConf()));
		// Inicjalizacja sieci.
		initialize(conf);
	}

	public void initialize(RbfNetworkConfiguration conf) {
		config = conf;
	}

	/**
	 * Ponowna inicjalizacja sieci.
	 * 
	 * @param centers
	 *            Lista centrów.
	 */
	public void reInitialize(List<DoubleVector> centers) {
		/*
		 * Neuronów w warstwie RBF jest tyle ile centrów.
		 */
		int noCenters = centers.size();
		getConfig().getRbfLayerConf().setNumberOfNeurons(noCenters);
		/*
		 * Wejść jest tyle samo ile wynosi rozmiar przestrzeni, wkórej połorzone
		 * są centa.
		 */
		int noInputs = centers.get(0).size();
		getConfig().getRbfLayerConf().setNumberOfInputs(noInputs);
		getConfig().getRbfLayerConf().getRbfFunctionConf().setNumberOfInputs(
				noInputs);
		/*
		 * Wejść w sumatorze jest tyle ile centrów.
		 */
		getConfig().getAdderLayerConf().setNumberOfInputs(noCenters);
		getConfig().getAdderLayerConf().getAdderConf().setNumberOfInputs(
				noCenters);

		/*
		 * Re-Inicjalizacja warstwy RBF
		 */
		((RbfLayer) getLayers().get(0)).initialize(getConfig()
				.getRbfLayerConf(), centers);
		((AdderLayer) getLayers().get(1)).initialize(getConfig()
				.getAdderLayerConf());
	}

	public void simulation(DoubleVector inputs) {
		// Symulacja warstwy rbf
		RbfLayer rbfLayer = (RbfLayer) getLayers().get(0);
		rbfLayer.simulation(inputs);
		rbfLayer.getOutputs();
		// Symulacja warstwy wyjściowej
		((AdderLayer) getLayers().get(1)).simulation(rbfLayer.getOutputs());
	}

	@Override
	public DoubleVector getOutputs() {
		return ((AdderLayer) getLayers().get(1)).getOutputs();
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
		// TODO Auto-generated method stub
	}

}
