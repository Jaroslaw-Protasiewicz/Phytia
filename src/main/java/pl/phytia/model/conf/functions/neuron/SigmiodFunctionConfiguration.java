package pl.phytia.model.conf.functions.neuron;

/**
 * Abstrkacyjne klasa - parametry konfiguracyjne funkcji sigmoidalnej.
 * 
 * @param <S>
 *            Obiekt, którego stan jest odczytywany lub zapisywany
 * @author Jarosław Protasiewicz
 */
public abstract class SigmiodFunctionConfiguration<S extends SigmiodFunctionConfiguration>
		extends NeuronFunctionConfiguration<S> {

	/**
	 * Współczynnik nachylenia funkcji sigmoidalnej.
	 */
	protected double betaRatio;

	/**
	 * Domyślny konstruktor
	 */
	public SigmiodFunctionConfiguration() {
		super();
		this.betaRatio = 1.0;
	}

	/**
	 * @return wartość pola betaRatio
	 */
	public double getBetaRatio() {
		return betaRatio;
	}

	/**
	 * @param betaRatio
	 *            jest przypisywany do pola betaRatio
	 */
	public void setBetaRatio(double betaRatio) {
		this.betaRatio = betaRatio;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().toString() + "\n");
		sb.append("betaRatio = " + betaRatio + "\n");
		sb.append(super.toString());
		return sb.toString();
	}

}
