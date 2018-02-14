package pl.phytia.model.conf.functions.learnRate;

import pl.phytia.model.enums.EnumLearningRatioType;

/**
 * 
 * Konfiguracja liniowego współczynnika uczenia.
 * 
 * @author Jarosław Protasiewicz
 */
public class LinearLearnigRateConfiguration extends
		LearningRateConfiguration<LinearLearnigRateConfiguration> {

	private static final long serialVersionUID = 9208699479151359309L;

	/**
	 * Wartość zmiany współczynnika uczenia.
	 */
	private double learningRatioChage;

	/**
	 * Decyduje od której epoki będzie zmieniany współczynnik uczenia.
	 */
	private int startEpochChange;

	/**
	 * Określa co ile epok jest zmieniany wsp. uczenia.
	 */
	private int stepEpochChange;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public LinearLearnigRateConfiguration() {
		super();
		this.learningRatioChage = 0.01;
		this.stepEpochChange = 1;
		this.startEpochChange = 1;
	}

	/**
	 * Konstruktor inicjalizujący wszystkie pola.
	 * 
	 * @param stepEpochChange
	 *            Określa co ile epok jest zmieniany wsp. uczenia.
	 * @param learningRatioChage
	 *            Wartość zmiany współczynnika uczenia.
	 * @param startEpochChange
	 *            Decyduje od której epoki będzie zmieniany współczynnik
	 *            uczenia.
	 * @param initLearningRatio
	 *            Początkowa wartość współczynnika uczenia.
	 * @param minLearnigRatio
	 *            Minimalna, dopuszczalna wartość współczynnika uczenia.
	 * @param maxLearningRatio
	 *            Maksymalna, dopuszczalna wartość współczynnika uczenia.
	 * @param learningRatioType
	 *            Typ - nazwa funkcji.
	 */
	public LinearLearnigRateConfiguration(int stepEpochChange,
			double learningRatioChage, int startEpochChange,
			double initLearningRatio, double minLearnigRatio,
			double maxLearningRatio, EnumLearningRatioType learningRatioType) {
		super(initLearningRatio, minLearnigRatio, maxLearningRatio,
				learningRatioType);
		this.stepEpochChange = stepEpochChange;
		this.learningRatioChage = learningRatioChage;
		this.startEpochChange = startEpochChange;
	}

	/**
	 * @return wartość pola learningRatioChage
	 */
	public double getLearningRatioChage() {
		return learningRatioChage;
	}

	/**
	 * @param learningRatioChage
	 *            jest przypisywany do pola learningRatioChage
	 */
	public void setLearningRatioChage(double learningRatioChage) {
		this.learningRatioChage = learningRatioChage;
	}

	/**
	 * @return wartość pola stepEpochChange
	 */
	public int getStepEpochChange() {
		return stepEpochChange;
	}

	/**
	 * @param stepEpochChange
	 *            jest przypisywany do pola stepEpochChange
	 */
	public void setStepEpochChange(int stepEpochChange) {
		this.stepEpochChange = stepEpochChange;
	}

	/**
	 * @return wartość pola startEpochChange
	 */
	public int getStartEpochChange() {
		return startEpochChange;
	}

	/**
	 * @param startEpochChange
	 *            jest przypisywany do pola startEpochChange
	 */
	public void setStartEpochChange(int startEpochChange) {
		this.startEpochChange = startEpochChange;
	}

}
