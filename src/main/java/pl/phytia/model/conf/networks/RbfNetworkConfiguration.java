package pl.phytia.model.conf.networks;

import pl.phytia.model.conf.layers.AdderLayerConfiguration;
import pl.phytia.model.conf.layers.RbfLayerConfiguration;
import pl.phytia.model.enums.EnumNetworkType;

/**
 * Konfiguracja sieci RBF.
 * 
 * @author Jarosław Protasiewicz
 */
public class RbfNetworkConfiguration extends
		NetworkConfiguration<RbfNetworkConfiguration> {

	private static final long serialVersionUID = -106973222558190816L;

	public RbfNetworkConfiguration() {
		super();
		rbfLayerConf = new RbfLayerConfiguration();
		adderLayerConf = new AdderLayerConfiguration();
		setTypeOfNetwork(EnumNetworkType.RBF);
	}

	/**
	 * Konfiguracja warstwy RBF
	 */
	private RbfLayerConfiguration rbfLayerConf;

	/**
	 * Konfiguracja wartstwy sumatorów.
	 */
	private AdderLayerConfiguration adderLayerConf;

	public RbfLayerConfiguration getRbfLayerConf() {
		return rbfLayerConf;
	}

	public void setRbfLayerConf(RbfLayerConfiguration rbfLayerConf) {
		this.rbfLayerConf = rbfLayerConf;
	}

	public AdderLayerConfiguration getAdderLayerConf() {
		return adderLayerConf;
	}

	public void setAdderLayerConf(AdderLayerConfiguration adderLayerConf) {
		this.adderLayerConf = adderLayerConf;
	}

}
