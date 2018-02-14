package pl.phytia.model.conf.functions.neuron;

import pl.phytia.model.conf.Configuration;
import pl.phytia.model.enums.EnumFunctionType;

/**
 * Abstrakcyjna klasa konfiguracji funkcji i neuronu.
 * 
 * @author Jarosław Protasiewicz
 * 
 * @param <S>
 *            Obiekt, którego stan jest odczytywany lub zapisywany.
 */
public abstract class NeuronFunctionConfiguration<S extends NeuronFunctionConfiguration>
		extends Configuration<S> {

	/* ---------- FIELDS --------- */
	/**
	 * Typ funkcji aktywacji
	 */
	EnumFunctionType typeOfFunction;

	/**
	 * Ilość wejść do neuronu (bez Bias).
	 */
	private int numberOfInputs;

	/* -------- METHODS ------------ */
	/**
	 * Domyślny konstruktor
	 */
	public NeuronFunctionConfiguration() {
		super();

	}

	/* -------- GETTERS AND SETTERS ----------- */

	/**
	 * @return wartość pola typeOfFunction
	 */
	public EnumFunctionType getTypeOfFunction() {
		return typeOfFunction;
	}

	/**
	 * @param typeOfFunction
	 *            jest przypisywany do pola typeOfFunction
	 */
	public void setTypeOfFunction(EnumFunctionType typeOfFunction) {
		this.typeOfFunction = typeOfFunction;
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().toString() + "\n");
		sb.append("typeOfFunction = "
				+ (typeOfFunction != null ? typeOfFunction.toString() : "")
				+ "\n");
		sb.append("numberOfInputs = " + numberOfInputs + "\n");
		sb.append(super.toString());
		return sb.toString();
	}

}
