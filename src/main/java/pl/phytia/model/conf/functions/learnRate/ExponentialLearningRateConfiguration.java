package pl.phytia.model.conf.functions.learnRate;

import pl.phytia.model.enums.EnumLearningRatioType;

/**
 * Konfiguracja expotencjalnej funkcji współczynnika uczenia.
 * 
 * @author Jarosław Protasiewicz
 */
public class ExponentialLearningRateConfiguration extends
		LearningRateConfiguration<ExponentialLearningRateConfiguration> {

	private static final long serialVersionUID = -3393995796869017050L;

	/**
	 * Wartośc stałej expotencjalnego wsp. uczenia. Im wartość ta jest miejsza,
	 * tym bardziej stroma jest krzywa zmian wsp. uczenia (szybciej dąży do
	 * zera).
	 */
	private double learningRatioExpConst;

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
	public ExponentialLearningRateConfiguration() {
		super();
		this.learningRatioExpConst = 1.E-6;
		this.stepEpochChange = 1;
	}

	/**
	 * Konstruktor inicjalizujący wszystkie pola.
	 * 
	 * @param learningRatioExpConst
	 *            Wartość stałej expotencjalnego wsp. uczenia.
	 * @param startEpochChange
	 *            Decyduje od której epoki będzie zmieniany współczynnik
	 *            uczenia.
	 * @param stepEpochChange
	 *            Określa co ile epok jest zmieniany wsp. uczenia.
	 * @param initLearningRatio
	 *            Początkowa wartość współczynnika uczenia.
	 * @param minLearnigRatio
	 *            Minimalna, dopuszczalna wartość współczynnika uczenia.
	 * @param maxLearningRatio
	 *            Maksymalna, dopuszczalna wartość współczynnika uczenia.
	 * @param learningRatioType
	 *            Typ - nazwa funkcji.
	 */
	public ExponentialLearningRateConfiguration(double learningRatioExpConst,
			int startEpochChange, int stepEpochChange,
			double initLearningRatio, double minLearnigRatio,
			double maxLearningRatio, EnumLearningRatioType learningRatioType) {
		super(initLearningRatio, minLearnigRatio, maxLearningRatio,
				learningRatioType);
		this.learningRatioExpConst = learningRatioExpConst;
		this.startEpochChange = startEpochChange;
		this.stepEpochChange = stepEpochChange;
	}

	/**
	 * @return wartość pola learningRatioExpConst
	 */
	public double getLearningRatioExpConst() {
		return learningRatioExpConst;
	}

	/**
	 * @param learningRatioExpConst
	 *            jest przypisywany do pola learningRatioExpConst
	 */
	public void setLearningRatioExpConst(double learningRatioExpConst) {
		this.learningRatioExpConst = learningRatioExpConst;
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

}
