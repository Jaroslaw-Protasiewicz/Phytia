package pl.phytia.ann.algorithms.bp;

import java.util.Iterator;

import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.ann.layers.MLPLayer;
import pl.phytia.ann.networks.MLPNetwork;
import pl.phytia.ann.neurons.Perceptron;
import pl.phytia.model.conf.algorithms.BackPropConfiguration;
import pl.phytia.model.enums.EnumLayerType;
import pl.phytia.model.enums.EnumLearningRatioType;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.PatternPairVO;
import pl.phytia.model.sets.SupervisedDataSet;

public abstract class BackPropAlgorithm<S extends BackPropAlgorithm> extends
		Algorithm<S, BackPropConfiguration, SupervisedDataSet, MLPNetwork> {

	/**
	 * Okresla ila razy nie znaleziono lepszego rozwązania od od ostatniego
	 * najlepszego.
	 */
	private int notFoundBetterSoulution = 0;

	/**
	 * Konstruktor
	 */
	public BackPropAlgorithm() {
		super();
	}

	public void training(MLPNetwork net, SupervisedDataSet set) {
		/*
		 * Dopóki nie spełnione są warunki STOP, ale conajmniej jeden raz.
		 */
		do {
			/*
			 * Dopóki istnieją wektory wejściowe.
			 */
			reset(net);
			Iterator<PatternPairVO> it = set.iterator();
			while (it.hasNext()) {
				PatternPairVO pattern = it.next();
				DoubleVector in = pattern.getInputs();
				DoubleVector out = pattern.getOutputs();
				net.simulation(in);
				backPropagation(net, out);
				updateWeights(net, in);
				getErrorFunction().computeError(net, out);
			}
			/*
			 * Parametryzacja.
			 */
			updateParams(net, set);
			increaseCurrEpoch();
			System.out.println("Iteracja : " + this.currEpoch);
		} while (!checkStopCondition(net, set));
	}

	/**
	 * Wsteczna propagacja błędu.
	 */
	public void backPropagation(MLPNetwork net, DoubleVector pattern) {

		Perceptron neuron = null;
		/*
		 * Od warstwy wyjściowej do wejściowej.
		 */
		for (int i = net.getLayers().size() - 1; i >= 0; i--) {
			MLPLayer currLayer = (MLPLayer) net.getLayers().get(i);
			if (currLayer.getConfig().getTypeOfLayer().equals(
					EnumLayerType.OUTPUT)
					|| currLayer.getConfig().getTypeOfLayer().equals(
							EnumLayerType.INOUT)) {
				/*
				 * Jeżeli jest to warstwa wyjściowa, błąd pośredni jest róznicą
				 * pomiędzy wzorcem a wyjściem neuronu.
				 */
				double error = 0;
				for (int n = 0; n < currLayer.getNeurons().size(); ++n) {
					neuron = currLayer.getNeurons().get(n);
					error = pattern.get(n) - neuron.getOutput();
					neuron.setError(error
							* neuron.getFunction().computeDerivative(
									neuron.getSum()));
				}
			} else {
				/*
				 * Dla pozostałych warstw, błąd pośredni jest ważoną sumą błędów
				 * neuronów z porzedniej warstwy.
				 */
				MLPLayer prvLayer = (MLPLayer) net.getLayers().get(i + 1);
				for (int n = 0; n < currLayer.getNeurons().size(); ++n) {
					neuron = currLayer.getNeurons().get(n);
					double error = 0;
					for (int k = 0; k < prvLayer.getNeurons().size(); ++k) {
						Perceptron prvNeuron = prvLayer.getNeurons().get(k);
						error += prvNeuron.getWeights().get(n + 1)
								* prvNeuron.getError();
					}
					neuron.setError(error
							* neuron.getFunction().computeDerivative(
									neuron.getSum()));
				}
			}
		}
	}

	public boolean checkStopCondition(MLPNetwork net, SupervisedDataSet set) {
		if (this.currEpoch - 1 >= this.config.getMaxIteration()) {
			net.setTheBestWeights(true);
			double error = getStopCondFunction().computeError(net, set);
			System.out.println("Stop cond error = " + error);
			return true;
		} else {
			double error = getStopCondFunction().computeError(net, set);
			if (error < getStopCondFunction().getTheBestError()) {
				net.setTheBestWeights(false);
				notFoundBetterSoulution = 0;
				getStopCondFunction().setTheBestError(error);
				System.out.println("Znaleziono lepsze rozwiązanie");
			} else {
				++notFoundBetterSoulution;
				System.out.println("Gorsze rozwiązanie nr : "
						+ notFoundBetterSoulution);
			}
			System.out.println("Aktulany błąd = " + error);
			if (error < this.config.getMinError()
					|| notFoundBetterSoulution > this.config
							.getMaxNotFoundBetterSolution()) {
				net.setTheBestWeights(true);
				error = getStopCondFunction().computeError(net, set);
				System.out.println("Stop cond error = "
						+ getStopCondFunction().getError());
				return true;
			} else {
				return false;
			}
		}
	}

	public void updateParams(MLPNetwork net, SupervisedDataSet set) {
		/*
		 * Adaptacyjna zmiana współczynnika uczenia
		 */
		if (EnumLearningRatioType.ADAPTATIVE.equals(this.config
				.getLearningRatioType())
				&& this.currEpoch > 1) {
			double learningRatio = this.getConfig().getLearningRatio();
			String com;
			if (getErrorFunction().getError() > this.config
					.getLearningRatioModyf()
					* getErrorFunction().getRecentError()) {
				this.getConfig().setLearningRatio(
						learningRatio
								* this.getConfig().getLearnigRationDecrement());
				com = "Zminiejszam";
			} else {
				this.getConfig().setLearningRatio(
						learningRatio
								* this.getConfig().getLearnigRationIncrement());
				com = "Zwiększam";
			}

			System.out.println("Wsp. uczenia.:"
					+ this.getConfig().getLearningRatio() + " Error: "
					+ getErrorFunction().getError() + "ErrorPrv: "
					+ getErrorFunction().getRecentError() + " " + com);
		}
	}

	/**
	 * Reset parametrów i zmiennych.
	 * 
	 * @param net
	 *            Sieć neuronowa.
	 */
	public void reset(MLPNetwork net) {
		getErrorFunction().setRecentError(getErrorFunction().getError());
		getErrorFunction().setError(0.0d);
	}
}
