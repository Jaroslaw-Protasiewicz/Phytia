package pl.phytia.model.conf.algorithms;

public final class KohonenConfiguration extends
		AlgorithmConfiguration<KohonenConfiguration> {

	private static final long serialVersionUID = -2529819781362445647L;

	/**
	 * Minimalna zmiana wag - jeżeli ta wartość zostanie osiągnięta to algorytm
	 * jest zatrzymywany.
	 */
	private double minGlobalWeightsChange;

	/**
	 * Minimalna ilość klas - jeżeli ta wartość zostanie osiągnięta to algorytm
	 * jest zatrzymywany.
	 */
	private int minClasses;

	public KohonenConfiguration() {
		super();
		this.minGlobalWeightsChange = 0.0;
		this.minClasses = 100;
	}

	public KohonenConfiguration(double minGlobalWeightsChange, int minClasses) {
		this.minGlobalWeightsChange = minGlobalWeightsChange;
		this.minClasses = minClasses;
	}

	/**
	 * @return wartość pola minGlobalWeightsChange
	 */
	public double getMinGlobalWeightsChange() {
		return minGlobalWeightsChange;
	}

	/**
	 * @param minGlobalWeightsChange
	 *            jest przypisywany do pola minGlobalWeightsChange
	 */
	public void setMinGlobalWeightsChange(double minGlobalWeightsChange) {
		this.minGlobalWeightsChange = minGlobalWeightsChange;
	}

	/**
	 * @return wartość pola minClasses
	 */
	public int getMinClasses() {
		return minClasses;
	}

	/**
	 * @param minClasses
	 *            jest przypisywany do pola minClasses
	 */
	public void setMinClasses(int minClasses) {
		this.minClasses = minClasses;
	}

}
