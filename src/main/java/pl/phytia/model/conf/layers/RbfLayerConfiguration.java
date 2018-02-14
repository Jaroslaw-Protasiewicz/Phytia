package pl.phytia.model.conf.layers;

import pl.phytia.model.conf.functions.neuron.RbfFunctionConfiguration;
import pl.phytia.model.enums.EnumLayerType;
import pl.phytia.model.enums.EnumCenterWidthType;

/**
 * Konfiguracja warstwy sieci RBF
 * 
 * @author Jarosław Protasiewicz
 */
public class RbfLayerConfiguration extends
		LayerConfiguration<RbfLayerConfiguration> {

	private static final long serialVersionUID = -7662590605554981346L;

	/**
	 * Konfiguracja neuronu - funkcji RBF.
	 */
	private RbfFunctionConfiguration rbfFunctionConf;

	/**
	 * Sposób określania rozycia funcji radialnych.
	 */
	private EnumCenterWidthType centerWidthType;

	public RbfLayerConfiguration() {
		super();
		rbfFunctionConf = new RbfFunctionConfiguration();
		centerWidthType = EnumCenterWidthType.ADAPTIVE;
		setTypeOfLayer(EnumLayerType.HIDDEN);
	}

	public RbfFunctionConfiguration getRbfFunctionConf() {
		return rbfFunctionConf;
	}

	public void setRbfFunctionConf(RbfFunctionConfiguration rbfFunctionConf) {
		this.rbfFunctionConf = rbfFunctionConf;
	}

	public EnumCenterWidthType getCenterWidthType() {
		return centerWidthType;
	}

	public void setCenterWidthType(EnumCenterWidthType centerWidthType) {
		this.centerWidthType = centerWidthType;
	}

}
