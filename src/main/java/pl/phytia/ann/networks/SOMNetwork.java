package pl.phytia.ann.networks;

import java.util.ArrayList;

import pl.phytia.ann.layers.Layer;
import pl.phytia.ann.layers.Som2DimLayer;
import pl.phytia.ann.layers.Som3DimLayer;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.conf.networks.SOMNetworkConfiguration;

public class SOMNetwork extends Network<SOMNetwork, SOMNetworkConfiguration> {

	/**
	 * Konstruktor bezparametrowy.
	 */
	public SOMNetwork() {
		super();
	}

	/**
	 * Konstruktor tworzący sieć SOM.
	 * 
	 * @param conf
	 *            Konfiguracja sieci.
	 */
	public SOMNetwork(SOMNetworkConfiguration conf) {
		switch (conf.getLayersConf().get(0).getDimension()) {
		case 1:
			throw new UnsupportedOperationException(
					"Nie obsługiwany typ matrycy!");
		case 2:
			layers = new ArrayList<Layer>();
			layers.add(new Som2DimLayer(conf.getLayersConf().get(0)));
			break;
		case 3:
			layers = new ArrayList<Layer>();
			layers.add(new Som3DimLayer(conf.getLayersConf().get(0)));
			break;
		default:
			throw new UnsupportedOperationException(
					"Nie obsługiwany typ matrycy!");
		}
		initialize(conf);
	}

	public void initialize(SOMNetworkConfiguration conf) {
		config = conf;

	}

	@Override
	public DoubleVector getOutputs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double maxReturnValue() {
		return config.getMaxNormValue();
	}

	@Override
	public double minReturnValue() {
		return config.getMinNormValue();
	}

	public void simulation(DoubleVector inputs) {
		for (Layer layer : getLayers()) {
			layer.simulation(inputs);
		}
	}

	@Override
	public void setTheBestWeights(boolean reverse) {
		// TODO Auto-generated method stub

	}

}
