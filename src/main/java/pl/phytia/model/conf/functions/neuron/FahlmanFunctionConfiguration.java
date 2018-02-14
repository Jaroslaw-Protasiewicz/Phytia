package pl.phytia.model.conf.functions.neuron;

import pl.phytia.model.enums.EnumFunctionType;

/**
 * Parametry konfiguracyjne funkcji sigmoidalnej z modyfikacją Fahlmana.
 * 
 * @author Jarosław Protasiewicz
 */
public final class FahlmanFunctionConfiguration extends
		SigmiodFunctionConfiguration<FahlmanFunctionConfiguration> {

	private static final long serialVersionUID = -6644117645105524952L;

	/**
	 * Domyślny konstruktor
	 */
	public FahlmanFunctionConfiguration() {
		super();
		this.betaRatio = 1.0;
		this.setDerivativeRatio(0.1);
		this.setTypeOfFunction(EnumFunctionType.FahlmanUnipolar);
	}

	/**
	 * Współczynnik, o który jest modyfikowana pochodną funkcji aktywacji
	 * neuronu.
	 */
	private double derivativeRatio;

	/**
	 * @return wartość pola derivativeRatio
	 */
	public double getDerivativeRatio() {
		return derivativeRatio;
	}

	/**
	 * @param derivativeRatio
	 *            jest przypisywany do pola derivativeRatio
	 */
	public void setDerivativeRatio(double derivativeRatio) {
		this.derivativeRatio = derivativeRatio;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().toString() + "\n");
		sb.append("derivativeRatio = " + derivativeRatio + "\n");
		sb.append(super.toString());
		return sb.toString();
	}
}
