package pl.phytia.model.conf.functions.learnRate;

import pl.phytia.model.conf.Configuration;
import pl.phytia.model.enums.EnumLearningRatioType;

/**
 * Konfiguracja funkcji współczynnika uczenia.
 * 
 * @author Jarosław Protasiewicz
 * 
 * @param <S>
 *            Obiekt, którego stan jest odczytywany lub zapisywany.
 */
public class LearningRateConfiguration<S extends LearningRateConfiguration>
		extends Configuration<S> {

	private static final long serialVersionUID = 6596651980068445354L;

	/**
	 * Początkowa wartość współczynnika uczenia.
	 */
	private double initLearningRatio;

	/**
	 * Maksymalna, dopuszczalna wartość współczynnika uczenia.
	 */
	private double maxLearningRatio;

	/**
	 * Minimalna, dopuszczalna wartość współczynnika uczenia.
	 */
	private double minLearnigRatio;

	/**
	 * Typ funkcji współczynnika uczenia (pole informacyjne).
	 */
	private EnumLearningRatioType learningRatioType;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public LearningRateConfiguration() {
		super();
		this.initLearningRatio = 0.1;
		this.minLearnigRatio = 10e-12;
		this.maxLearningRatio = 10;
		this.learningRatioType = EnumLearningRatioType.LINEAR;
	}

	/**
	 * Konstruktor wypełniający pola.
	 * 
	 * @param initLearningRatio
	 *            Początkowa wartość współczynnika uczenia.
	 * @param minLearnigRatio
	 *            Minimalna, dopuszczalna wartość współczynnika uczenia.
	 * @param maxLearningRatio
	 *            Maksymalna, dopuszczalna wartość współczynnika uczenia.
	 * @param learningRatioType
	 *            Typ funkcji współczynnika uczenia (pole informacyjne).
	 */
	public LearningRateConfiguration(double initLearningRatio,
			double minLearnigRatio, double maxLearningRatio,
			EnumLearningRatioType learningRatioType) {
		super();
		this.initLearningRatio = initLearningRatio;
		this.minLearnigRatio = minLearnigRatio;
		this.maxLearningRatio = maxLearningRatio;
		this.learningRatioType = learningRatioType;
	}

	/**
	 * @return wartość pola initLearningRatio
	 */
	public double getInitLearningRatio() {
		return initLearningRatio;
	}

	/**
	 * @param initLearningRatio
	 *            jest przypisywany do pola initLearningRatio
	 */
	public void setInitLearningRatio(double initLearningRatio) {
		this.initLearningRatio = initLearningRatio;
	}

	/**
	 * @return wartość pola maxLearningRatio
	 */
	public double getMaxLearningRatio() {
		return maxLearningRatio;
	}

	/**
	 * @param maxLearningRatio
	 *            jest przypisywany do pola maxLearningRatio
	 */
	public void setMaxLearningRatio(double maxLearningRatio) {
		this.maxLearningRatio = maxLearningRatio;
	}

	/**
	 * @return wartość pola minLearnigRatio
	 */
	public double getMinLearnigRatio() {
		return minLearnigRatio;
	}

	/**
	 * @param minLearnigRatio
	 *            jest przypisywany do pola minLearnigRatio
	 */
	public void setMinLearnigRatio(double minLearnigRatio) {
		this.minLearnigRatio = minLearnigRatio;
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

}
