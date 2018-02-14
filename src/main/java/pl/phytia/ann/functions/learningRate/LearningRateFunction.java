package pl.phytia.ann.functions.learningRate;

import pl.phytia.ann.functions.Function;
import pl.phytia.model.conf.functions.learnRate.LearningRateConfiguration;

/**
 * Abstrakcyjna funkcja współczynnika uczenia.
 * 
 * @author Jarosław Protasiewicz
 * @param <C>
 *            Typ obiektu z parametrami konfiguracyjnymi
 * @param <S>
 *            Typ obietu zwaracanego przez <code>this</code>
 */
public abstract class LearningRateFunction<C extends LearningRateConfiguration, S extends LearningRateFunction>
		extends Function<C, S> {

	/**
	 * Współczynnik uczenia.
	 */
	private double learningRatio;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public LearningRateFunction() {
		super();
		this.learningRatio = 1;
	}

	/**
	 * Konstruktor wypełniający pola.
	 * 
	 * @param learningRatio
	 *            Współczynnik uczenia.
	 */
	public LearningRateFunction(double learningRatio) {
		super();
		this.learningRatio = learningRatio;
	}

	/**
	 * Wyznacza współczynnik uczenia dla danej eopki treningu sieci.
	 * 
	 * @param epoch
	 *            Epoka treningu sieci.
	 * @return Współczynnik uczenia.
	 */
	public abstract double computeLearningRatio(int epoch);

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

}
