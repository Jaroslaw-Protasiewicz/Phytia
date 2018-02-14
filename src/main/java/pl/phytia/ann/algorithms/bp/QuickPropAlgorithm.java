package pl.phytia.ann.algorithms.bp;

import java.util.Iterator;

import pl.phytia.ann.functions.error.MapeErrorFunction;
import pl.phytia.ann.networks.MLPNetwork;
import pl.phytia.ann.neurons.Perceptron;
import pl.phytia.model.conf.algorithms.BackPropConfiguration;
import pl.phytia.model.enums.EnumLayerType;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.PatternPairVO;
import pl.phytia.model.sets.SupervisedDataSet;
import pl.phytia.ann.layers.MLPLayer;

/**
 * Algorytm Quick-propagation.
 * 
 * @author Jarosław Protasiewicz
 */
public class QuickPropAlgorithm extends BackPropAlgorithm<QuickPropAlgorithm> {

	public QuickPropAlgorithm(BackPropConfiguration conf) {
		super();
		initialize(conf);
		setStopCondFunction(new MapeErrorFunction());
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
				computeGradinetAndSlope(net, in);
				getErrorFunction().computeError(net, in);
				updateWeights(net, null);
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
	 * Wyznaczenie gradientu
	 */
	public void computeGradinetAndSlope(MLPNetwork net, DoubleVector inputs) {
		for (int l = 0; l < net.getLayers().size(); ++l) {
			MLPLayer currLayer = (MLPLayer) net.getLayers().get(l);
			DoubleVector layerInputs = null;
			if (currLayer.getConfig().getTypeOfLayer().equals(
					EnumLayerType.INPUT)
					|| currLayer.getConfig().getTypeOfLayer().equals(
							EnumLayerType.INOUT)) {
				layerInputs = inputs;
			} else {
				layerInputs = ((MLPLayer) net.getLayers().get(l - 1))
						.getOutputs();
			}
			Perceptron neuron = null;
			for (int n = 0; n < currLayer.getNeurons().size(); ++n) {
				neuron = currLayer.getNeurons().get(n);
				for (int w = 0; w < neuron.getWeights().size(); ++w) {
					double slope = 0;
					if (w == 0) {
						slope = (2 * neuron.getError() * (-1.0))
								+ (config.getWeightRatio() * neuron
										.getWeights().get(w));
					} else {
						slope = (2 * neuron.getError() * layerInputs.get(w - 1))
								+ (config.getWeightRatio() * neuron
										.getWeights().get(w));
					}
					neuron
							.updateRecentSlope(w, neuron.getSlope().get(w),
									false);
					neuron.updateSlope(w, slope, false);
					neuron.computeDeltaSlope(w);
				}
			}
		}
	}

	public void updateWeights(MLPNetwork net, DoubleVector inputs) {
		for (int l = net.getLayers().size() - 1; l >= 0; l--) {
			MLPLayer currLayer = (MLPLayer) net.getLayers().get(l);
			Perceptron neuron = null;
			for (int n = 0; n < currLayer.getNeurons().size(); ++n) {
				neuron = currLayer.getNeurons().get(n);
				for (int w = 0; w < neuron.getWeights().size(); ++w) {

					// double deltaMomentum = 0;
					double slope = neuron.getSlope().get(w);
					double deltaSlope = neuron.getDeltaSlope().get(w);
					double recentDeltaWeight = neuron.getRecentWeightsChange()
							.get(w);
					/*
					 * Wyznaczenie współczynnika uczenia.
					 */
					double learnRatio = 0.0;
					if (recentDeltaWeight == 0 || slope * recentDeltaWeight > 0) {
						learnRatio = config.getLearningRatio();
					}
					/*
					 * Wyznaczenie współczynnika momentu.
					 */
					double momentum = 0.0;
					if ((slope * recentDeltaWeight * deltaSlope) < 0
							|| deltaSlope > config.getMaximumGrowthRatio()) {
						momentum = config.getMaximumGrowthRatio();
					} else {
						momentum = deltaSlope;
					}
					double deltaWeight = (learnRatio * slope)
							+ (momentum * recentDeltaWeight);
					neuron.updateRecentWeightChange(w, deltaWeight);
					neuron.updateWeight(w, deltaWeight);
				}
			}
		}
	}
	/*
	 * @Override public void updateParams(Network net, DataSetGK set) {
	 * super.updateParams(net, set); momentumAdaptaion(net, set); }
	 */

}
