package pl.phytia.model.conf.algorithms;

import pl.phytia.model.enums.EnumFunctionType;
import pl.phytia.model.enums.EnumLearningRatioType;

/**
 * Konfiguracja algorytmu wstecznej propagacji błędu.
 * 
 * @author Jarosław Protasiewicz
 */
public class BackPropConfiguration extends
		AlgorithmConfiguration<BackPropConfiguration> {

	private static final long serialVersionUID = 7724810283905982183L;

	/**
	 * Konstruktor
	 * 
	 * @param learningRatioType
	 *            Współczynnik uczenia.
	 * @param enumFunctionType
	 *            Typ funkcji błędu.
	 * @param stopCondFunctionType
	 *            Typ funkcji błędu dla warunku stop.
	 */
	public BackPropConfiguration(EnumLearningRatioType learningRatioType,
			EnumFunctionType enumFunctionType,
			EnumFunctionType stopCondFunctionType) {
		super(enumFunctionType, stopCondFunctionType);
		this.momentumRatio = 0.5d;
		this.learningRatio = 0.5d;
		this.learningRatioModyf = 1.04d;
		this.learnigRationIncrement = 1.05d;
		this.learnigRationDecrement = 0.7d;
		this.learningRatioType = learningRatioType;
		this.maximumGrowthRatio = 1.75d;
		this.momentumGrowthRatio = 1.04d;
		this.weightRatio = -0.0001d;
	}

	/**
	 * Współczynnik uczenia.
	 */
	private double learningRatio;

	/**
	 * Współczynnik zmian współczynnika uczenia.
	 */
	private double learningRatioModyf;

	/**
	 * Współczynnik zwiększania współczynnika uczenia.
	 */
	private double learnigRationIncrement;

	/**
	 * Współczynnik zmniejszania współczynnika uczenia.
	 */
	private double learnigRationDecrement;

	/**
	 * Rodzaj - strategia zamian współczynnika uczenia.
	 */
	private EnumLearningRatioType learningRatioType;

	/**
	 * Współczynnik momentum - uzywany w algorytmie BP z momentum.
	 */
	private double momentumRatio;

	/**
	 * Współczynnik kontrolujący człon momentu - uzywany w algorytmie BP z
	 * momentum.
	 */
	private double momentumGrowthRatio;

	/**
	 * Współczynnik maksymalnego wzrostu - używany w aglorytmie QuickProp.
	 */
	private double maximumGrowthRatio;

	/**
	 * Współczynnik zaburzenia wagi - używany w aglorytmie QuickProp.
	 */
	private double weightRatio;

	/**
	 * @return wartość pola learningRatio
	 */
	public double getLearningRatio() {
		return learningRatio;
	}

	/**
	 * @param learningRatio
	 *            jest przypisywany do pola learningRatio
	 */
	public void setLearningRatio(double learningRatio) {
		this.learningRatio = learningRatio;
	}

	/**
	 * @return wartość pola learnigRationDecrement
	 */
	public double getLearnigRationDecrement() {
		return learnigRationDecrement;
	}

	/**
	 * @param learnigRationDecrement
	 *            jest przypisywany do pola learnigRationDecrement
	 */
	public void setLearnigRationDecrement(double learnigRationDecrement) {
		this.learnigRationDecrement = learnigRationDecrement;
	}

	/**
	 * @return wartość pola learnigRationIncrement
	 */
	public double getLearnigRationIncrement() {
		return learnigRationIncrement;
	}

	/**
	 * @param learnigRationIncrement
	 *            jest przypisywany do pola learnigRationIncrement
	 */
	public void setLearnigRationIncrement(double learnigRationIncrement) {
		this.learnigRationIncrement = learnigRationIncrement;
	}

	/**
	 * @return wartość pola learningRatioModyf
	 */
	public double getLearningRatioModyf() {
		return learningRatioModyf;
	}

	/**
	 * @param learningRatioModyf
	 *            jest przypisywany do pola learningRatioModyf
	 */
	public void setLearningRatioModyf(double learningRatioModyf) {
		this.learningRatioModyf = learningRatioModyf;
	}

	/**
	 * @return wartość pola learningRatioType
	 */
	public EnumLearningRatioType getLearningRatioType() {
		return learningRatioType;
	}

	/**
	 * @param learningRatioType
	 *            jest przypisywany do pola learningRatioType
	 */
	public void setLearningRatioType(EnumLearningRatioType learningRatioType) {
		this.learningRatioType = learningRatioType;
	}

	/**
	 * @return wartość pola momentumRatio
	 */
	public double getMomentumRatio() {
		return momentumRatio;
	}

	/**
	 * @param momentumRatio
	 *            jest przypisywany do pola momentumRatio
	 */
	public void setMomentumRatio(double momentumRatio) {
		this.momentumRatio = momentumRatio;
	}

	/**
	 * @return wartość pola maximumGrowthRatio
	 */
	public double getMaximumGrowthRatio() {
		return maximumGrowthRatio;
	}

	/**
	 * @param maximumGrowthRatio
	 *            jest przypisywany do pola maximumGrowthRatio
	 */
	public void setMaximumGrowthRatio(double maximumGrowthRatio) {
		this.maximumGrowthRatio = maximumGrowthRatio;
	}

	/**
	 * @return wartość pola momentumGrowthRatio
	 */
	public double getMomentumGrowthRatio() {
		return momentumGrowthRatio;
	}

	/**
	 * @param momentumGrowthRatio
	 *            jest przypisywany do pola momentumGrowthRatio
	 */
	public void setMomentumGrowthRatio(double momentumGrowthRatio) {
		this.momentumGrowthRatio = momentumGrowthRatio;
	}

	/**
	 * @return wartość pola weightRatio
	 */
	public double getWeightRatio() {
		return weightRatio;
	}

	/**
	 * @param weightRatio
	 *            jest przypisywany do pola weightRatio
	 */
	public void setWeightRatio(double weightRatio) {
		this.weightRatio = weightRatio;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\n" + this.getClass().toString()
				+ "\n");
		sb.append("learningRatio = " + this.learningRatio + "\n");
		sb.append("learningRatioModyf = " + this.learningRatioModyf + "\n");
		sb.append("learnigRationIncrement = " + this.learnigRationIncrement
				+ "\n");
		sb.append("learnigRationDecrement = " + this.learnigRationDecrement
				+ "\n");
		sb.append("learningRatioType = " + this.learningRatioType.toString()
				+ "\n");
		sb.append("momentumRatio = " + this.momentumRatio + "\n");
		sb.append("momentumGrowthRatio = " + this.momentumGrowthRatio + "\n");
		sb.append("maximumGrowthRatio = " + this.maximumGrowthRatio + "\n");
		sb.append("weightRatio = " + this.weightRatio + "\n");
		sb.append("momentumGrowthRatio = " + this.momentumGrowthRatio + "\n");
		sb.append(super.toString());
		return sb.toString();
	}

}
