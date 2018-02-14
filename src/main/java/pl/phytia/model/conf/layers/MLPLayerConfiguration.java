package pl.phytia.model.conf.layers;

import pl.phytia.model.conf.functions.neuron.NeuronFunctionConfiguration;

/**
 * Konfiguracja warstwy sieci perceptornowej.
 * 
 * @param <N>
 *            Obiekt, zawierający konfigurację neuronu, który jest używany w
 *            warstwie.
 * @author Jarosław Protasiewicz
 */
public final class MLPLayerConfiguration<N extends NeuronFunctionConfiguration>
		extends LayerConfiguration<MLPLayerConfiguration> {

	private static final long serialVersionUID = -2060532072067482202L;

	/**
	 * Konfiguracja neuronu i funkcji aktywacji.
	 */
	private N neuronFunctionConf;

	/**
	 * Domyślny konstruktor.
	 */
	public MLPLayerConfiguration() {
		super();
	}

	/**
	 * @return wartość pola neuronFunctionConf
	 */
	public N getNeuronFunctionConf() {
		return neuronFunctionConf;
	}

	/**
	 * @param neuronFunctionConf
	 *            jest przypisywany do pola neuronFunctionConf
	 */
	public void setNeuronFunctionConf(N neuronFunctionConf) {
		this.neuronFunctionConf = neuronFunctionConf;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().toString() + "\n");
		sb.append(super.toString());
		sb.append(neuronFunctionConf.toString());
		return sb.toString();
	}
}
