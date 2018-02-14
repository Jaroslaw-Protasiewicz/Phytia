package pl.phytia.model.conf.layers;

import pl.phytia.model.conf.functions.neuron.AdderFunctionConfiguration;
import pl.phytia.model.enums.EnumLayerType;

/**
 * Konfiguracja sumującej warstwy sieci.
 * 
 * @author Jarosław Protasiewicz
 */
public class AdderLayerConfiguration extends
		LayerConfiguration<AdderLayerConfiguration> {

	private static final long serialVersionUID = 115122750380827676L;

	/**
	 * Konfiguracja sumatora.
	 */
	private AdderFunctionConfiguration adderConf;

	public AdderLayerConfiguration() {
		super();
		adderConf = new AdderFunctionConfiguration();
		setNumberOfNeurons(1);
		setTypeOfLayer(EnumLayerType.OUTPUT);
	}

	public AdderFunctionConfiguration getAdderConf() {
		return adderConf;
	}

	public void setAdderConf(AdderFunctionConfiguration adderConf) {
		this.adderConf = adderConf;
	}
}
