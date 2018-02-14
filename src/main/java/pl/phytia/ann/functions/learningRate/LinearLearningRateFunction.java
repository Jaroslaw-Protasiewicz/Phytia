package pl.phytia.ann.functions.learningRate;

import pl.phytia.model.conf.functions.learnRate.LinearLearnigRateConfiguration;

/**
 * Liniowy modyfikowany współczynnik uczenia.
 * 
 * @author Jarosław Protasiewicz
 */
public final class LinearLearningRateFunction
		extends
		LearningRateFunction<LinearLearnigRateConfiguration, LinearLearningRateFunction> {

	private static final long serialVersionUID = 6110800407810320859L;

	public LinearLearningRateFunction() {
		super();
	}

	public LinearLearningRateFunction(LinearLearnigRateConfiguration conf) {
		super(conf.getInitLearningRatio());
		this.config = conf;
	}

	@Override
	public double computeLearningRatio(int epoch) {
		/*
		 * Zmiana współczynnika uczenia następuje gdy: epoka jest odpowiednia i
		 * wsp. uczenia nie przekroczył minimum
		 */
		if (epoch > config.getStartEpochChange()
				&& epoch % config.getStepEpochChange() == 0
				&& getLearningRatio() > config.getMinLearnigRatio()
				&& getLearningRatio() < config.getMaxLearningRatio()) {
			setLearningRatio(getLearningRatio()
					- config.getLearningRatioChage());
			return getLearningRatio();
		} else {
			return getLearningRatio();
		}
	}

}
