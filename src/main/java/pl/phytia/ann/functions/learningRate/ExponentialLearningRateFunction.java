package pl.phytia.ann.functions.learningRate;

import pl.phytia.model.conf.functions.learnRate.ExponentialLearningRateConfiguration;

/**
 * Ekspotencjalnie modyfikowany współczynnik uczenia.
 * 
 * @author Jarosław Protasiewicz
 */
public class ExponentialLearningRateFunction
		extends
		LearningRateFunction<ExponentialLearningRateConfiguration, ExponentialLearningRateFunction> {

	private static final long serialVersionUID = -7543195101601979883L;

	public ExponentialLearningRateFunction() {
		super();
	}

	public ExponentialLearningRateFunction(
			ExponentialLearningRateConfiguration conf) {
		super(conf.getInitLearningRatio());
		this.config = conf;
	}

	@Override
	public double computeLearningRatio(int epoch) {
		if (epoch > config.getStartEpochChange()
				&& epoch % config.getStepEpochChange() == 0
				&& getLearningRatio() > config.getMinLearnigRatio()
				&& getLearningRatio() < config.getMaxLearningRatio()) {
			setLearningRatio(getLearningRatio()
					* Math.exp(-config.getLearningRatioExpConst() * epoch));
			return getLearningRatio();
		} else {
			return getLearningRatio();
		}
	}
}
