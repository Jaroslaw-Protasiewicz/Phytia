package pl.phytia.model.conf.algorithms;

public class QuickPropConfiguration {

	/**
	 * Współczynnik maksymalnego wzrostu.
	 */
	private double maxIncreaseRatio;

	/**
	 * Współczynnik zaburzenia wagi.
	 */
	private double weightDecayRatio;

	/**
	 * @return wartość pola maxIncreaseRatio
	 */
	public double getMaxIncreaseRatio() {
		return maxIncreaseRatio;
	}

	/**
	 * @param maxIncreaseRatio
	 *            jest przypisywany do pola maxIncreaseRatio
	 */
	public void setMaxIncreaseRatio(double maxIncreaseRatio) {
		this.maxIncreaseRatio = maxIncreaseRatio;
	}

	/**
	 * @return wartość pola weightDecayRatio
	 */
	public double getWeightDecayRatio() {
		return weightDecayRatio;
	}

	/**
	 * @param weightDecayRatio
	 *            jest przypisywany do pola weightDecayRatio
	 */
	public void setWeightDecayRatio(double weightDecayRatio) {
		this.weightDecayRatio = weightDecayRatio;
	}

}
