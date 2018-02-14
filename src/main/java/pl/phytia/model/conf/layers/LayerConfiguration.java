package pl.phytia.model.conf.layers;

import pl.phytia.model.conf.Configuration;
import pl.phytia.model.enums.EnumLayerType;

/**
 * Abstrakcyjna klasa - nośnik parametrów konfiguracyjnych warstwy sieci
 * neuronowej.
 * 
 * @param <S>
 *            Obiekt, którego stan jest odczytywany lub zapisywany
 * @author Jarosław Protasiewicz
 */

public abstract class LayerConfiguration<S extends LayerConfiguration> extends
		Configuration<S> {

	/**
	 * Typ warstwy sieci neuronowej. <br>
	 * Pole ma znaczenie informacyjne
	 */
	private EnumLayerType typeOfLayer;

	/**
	 * Ilość neuronów / wyjść w warstwie sieci neuronowej.
	 */
	private int numberOfNeurons;

	/**
	 * Ilość wejść do warstwy sieci neuronowej (bez Bias).
	 */
	private int numberOfInputs;

	/**
	 * Domyślny konstruktor
	 */
	public LayerConfiguration() {
		super();
	}

	/**
	 * @return wartość pola numberOfNeurons
	 */
	public int getNumberOfNeurons() {
		return numberOfNeurons;
	}

	/**
	 * @param numberOfNeurons
	 *            jest przypisywany do pola numberOfNeurons
	 */
	public void setNumberOfNeurons(int numberOfNeurons) {
		this.numberOfNeurons = numberOfNeurons;
	}

	/**
	 * @return wartość pola numberOfInputs
	 */
	public int getNumberOfInputs() {
		return numberOfInputs;
	}

	/**
	 * @param numberOfInputs
	 *            jest przypisywany do pola numberOfInputs
	 */
	public void setNumberOfInputs(int numberOfInputs) {
		this.numberOfInputs = numberOfInputs;
	}

	/**
	 * @return wartość pola typeOfLayer
	 */
	public EnumLayerType getTypeOfLayer() {
		return typeOfLayer;
	}

	/**
	 * @param typeOfLayer
	 *            jest przypisywany do pola typeOfLayer
	 */
	public void setTypeOfLayer(EnumLayerType typeOfLayer) {
		this.typeOfLayer = typeOfLayer;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().toString() + "\n");
		sb.append("typeOfLayer = "
				+ (typeOfLayer != null ? typeOfLayer.toString() : "") + "\n");
		sb.append("numberOfNeurons = " + numberOfNeurons + "\n");
		sb.append("numberOfInputs = " + numberOfInputs + "\n");
		sb.append(super.toString());
		return sb.toString();
	}
}
