package pl.phytia.model.conf.networks;

import java.util.List;

import pl.phytia.model.conf.layers.MLPLayerConfiguration;
import pl.phytia.model.enums.EnumNetworkType;

/**
 * Nośnik parametrów konfiguracyjnych sieci neuronowej MLP.
 * 
 * @author Jarosław Protasiewicz
 */
public final class MLPNetworkConfiguration extends
		NetworkConfiguration<MLPNetworkConfiguration> {

	private static final long serialVersionUID = 6735822101588631527L;

	/**
	 * Konfiguracja każdej z warstw sieci neuronowej.
	 */
	private List<MLPLayerConfiguration> layersConf;

	/**
	 * Konstruktor domyślny.
	 */
	public MLPNetworkConfiguration() {
		super();
		setTypeOfNetwork(EnumNetworkType.MLP);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().toString() + "\n");
		sb.append("--- Warstwy sieci --\n");
		for (MLPLayerConfiguration l : layersConf) {
			sb.append(l.toString() + "\n");
		}
		sb.append(super.toString());
		return sb.toString();
	}

	public void setLayersConf(List<MLPLayerConfiguration> layersConf) {
		this.layersConf = layersConf;
	}

	public List<MLPLayerConfiguration> getLayersConf() {
		return layersConf;
	}

}
