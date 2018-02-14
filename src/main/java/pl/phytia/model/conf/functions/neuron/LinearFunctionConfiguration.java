package pl.phytia.model.conf.functions.neuron;

/**
 * Parametry konfiguracyjne dla liniowej funkcji aktywacji neuronu.
 * 
 * @author Jarosław Protasiewicz
 */
public final class LinearFunctionConfiguration extends
		NeuronFunctionConfiguration<LinearFunctionConfiguration> {

	private static final long serialVersionUID = 4453787926420771315L;

	/**
	 * Domyślny konstruktor
	 */
	public LinearFunctionConfiguration() {
		super();
		this.maxActivation = 1;
		this.minActivation = 0;
		this.gradeRatio = 0.5;
	}

	/**
	 * Współczynnik nachylenia funkcji.
	 */
	private double gradeRatio;

	/**
	 * Maksymalna wartość aktywacji funkcji.
	 */
	private double maxActivation;

	/**
	 * MInimalna wartość aktywacji funkcji
	 */
	private double minActivation;

	/**
	 * @return wartość pola gradeRatio
	 */
	public double getGradeRatio() {
		return gradeRatio;
	}

	/**
	 * @param gradeRatio
	 *            jest przypisywany do pola gradeRatio
	 */
	public void setGradeRatio(double gradeRatio) {
		this.gradeRatio = gradeRatio;
	}

	/**
	 * @return wartość pola maxActivation
	 */
	public double getMaxActivation() {
		return maxActivation;
	}

	/**
	 * @param maxActivation
	 *            jest przypisywany do pola maxActivation
	 */
	public void setMaxActivation(double maxActivation) {
		this.maxActivation = maxActivation;
	}

	/**
	 * @return wartość pola minActivation
	 */
	public double getMinActivation() {
		return minActivation;
	}

	/**
	 * @param minActivation
	 *            jest przypisywany do pola minActivation
	 */
	public void setMinActivation(double minActivation) {
		this.minActivation = minActivation;
	}

}
