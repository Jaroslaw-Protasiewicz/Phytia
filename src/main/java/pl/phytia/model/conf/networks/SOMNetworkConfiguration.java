package pl.phytia.model.conf.networks;

import java.util.List;

import pl.phytia.model.conf.layers.SOMLayerConfiguration;

/**
 * KOnfiguracja sieci SOM
 * 
 * @author Jarosław Protasiewicz
 */
public class SOMNetworkConfiguration extends
		NetworkConfiguration<NetworkConfiguration> {

	private static final long serialVersionUID = -8314472282756390320L;

	/**
	 * Dolny zakres normalizacji danych.
	 */
	private double minNormValue;

	/**
	 * Górny zakres normalizacji danych.
	 */
	private double maxNormValue;

	/**
	 * Konfiguracja każdej z warstw sieci neuronowej.
	 */
	private List<SOMLayerConfiguration> layersConf;

	public SOMNetworkConfiguration() {
		super();
		this.minNormValue = 0;
		this.maxNormValue = 1;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().toString() + "\n");
		sb.append("--- Warstwy sieci --\n");
		for (SOMLayerConfiguration l : layersConf) {
			sb.append(l.toString() + "\n");
		}
		sb.append(super.toString());
		return sb.toString();
	}

	/**
	 * @return wartość pola maxNormValue
	 */
	public double getMaxNormValue() {
		return maxNormValue;
	}

	/**
	 * @param maxNormValue
	 *            jest przypisywany do pola maxNormValue
	 */
	public void setMaxNormValue(double maxNormValue) {
		this.maxNormValue = maxNormValue;
	}

	/**
	 * @return wartość pola minNormValue
	 */
	public double getMinNormValue() {
		return minNormValue;
	}

	/**
	 * @param minNormValue
	 *            jest przypisywany do pola minNormValue
	 */
	public void setMinNormValue(double minNormValue) {
		this.minNormValue = minNormValue;
	}

	public void setLayersConf(List<SOMLayerConfiguration> layersConf) {
		this.layersConf = layersConf;
	}

	public List<SOMLayerConfiguration> getLayersConf() {
		return layersConf;
	}

}
