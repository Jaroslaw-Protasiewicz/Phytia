package pl.phytia.ann.algorithms.bp;

import java.util.Iterator;

import pl.waw.ibspan.phytia.ann.functions.error.MapeErrorFunction;
import pl.waw.ibspan.phytia.ann.layers.MLPLayer;
import pl.waw.ibspan.phytia.ann.networks.MLPNetwork;
import pl.waw.ibspan.phytia.ann.neurons.Perceptron;
import pl.waw.ibspan.phytia.model.conf.algorithms.BackPropConfiguration;
import pl.waw.ibspan.phytia.model.enums.EnumLayerType;
import pl.waw.ibspan.phytia.model.sets.DoubleVector;
import pl.waw.ibspan.phytia.model.sets.PatternPairVO;
import pl.waw.ibspan.phytia.model.sets.SupervisedDataSet;

/**
 * Algorytm Quick-propagation.
 * 
 * @author Jarosław Protasiewicz
 */
public class QuckPropBackup extends BackPropAlgorithm<QuickPropAlgorithm> {

	public QuckPropBackup(BackPropConfiguration conf) {
		super();
		initialize(conf);
		growthRatio = config.getMaximumGrowthRatio()
				/ (1 + config.getMaximumGrowthRatio());
		setStopCondFunction(new MapeErrorFunction());
	}

	private double growthRatio;

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
				changeSlope(net, in);
				getErrorFunction().computeError(net, in);
			}
			updateWeights(net, null);
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
	public void changeSlope(MLPNetwork net, DoubleVector inputs) {
		for (int l = net.getLayers().size() - 1; l >= 0; l--) {
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
						slope = (neuron.getError() * (-1.0))
								+ (config.getWeightRatio() * neuron
										.getWeights().get(w));
					} else {
						slope = (neuron.getError() * layerInputs.get(w - 1))
								+ (config.getWeightRatio() * neuron
										.getWeights().get(w));
					}
					neuron.updateSlope(w, slope, true);
				}
			}
		}
	}

	public void updateWeights(MLPNetwork net, DoubleVector inputs) {
		for (int l = net.getLayers().size() - 1; l >= 0; l--) {
			MLPLayer currLayer = (MLPLayer) net.getLayers().get(l);
			/*
			 * DoubleVectorGK layerInputs = null; if
			 * (currLayer.getConfig().getTypeOfLayer().equals(EnumLayerType.INPUT) ||
			 * currLayer.getConfig().getTypeOfLayer().equals(EnumLayerType.INOUT)) {
			 * layerInputs = inputs; } else { layerInputs =
			 * ((MLPLayer)net.getLayers().get(l-1)).getOutputs(); }
			 */
			Perceptron neuron = null;
			for (int n = 0; n < currLayer.getNeurons().size(); ++n) {
				neuron = currLayer.getNeurons().get(n);
				for (int w = 0; w < neuron.getWeights().size(); ++w) {
					double deltaWeight = 0;
					double deltaMomentum = 0;
					double slope = neuron.getSlope().get(w);
					if (neuron.getRecentWeightsChange().get(w) > 0) {
						if (slope > 0) {
							deltaWeight = 2 * getConfig().getLearningRatio()
									* slope;
						}
						if (slope > (growthRatio * neuron.getRecentSlope().get(
								w))) {
							deltaWeight += getConfig().getMaximumGrowthRatio()
									* neuron.getRecentWeightsChange().get(w);
						} else {
							deltaWeight += (slope / (neuron.getRecentSlope()
									.get(w) - slope))
									* neuron.getRecentWeightsChange().get(w);
						}
					} else if (neuron.getRecentWeightsChange().get(w) < 0) {
						if (slope < 0) {
							deltaWeight = 2 * getConfig().getLearningRatio()
									* slope;
						}
						if (slope < (growthRatio * neuron.getRecentSlope().get(
								w)))
							deltaWeight += getConfig().getMaximumGrowthRatio()
									* neuron.getRecentWeightsChange().get(w);
						else
							deltaWeight += (slope / (neuron.getRecentSlope()
									.get(w) - slope))
									* neuron.getRecentWeightsChange().get(w);
					} else {
						deltaMomentum = config.getMomentumRatio()
								* neuron.getRecentWeightsChange().get(w);
						deltaWeight = 2 * getConfig().getLearningRatio()
								* slope;
					}

					neuron.updateRecentWeightChange(w, deltaWeight
							+ deltaMomentum);
					// neuron.updateRecentMomentumChange(w, deltaMomentum);
					neuron.updateWeight(w, deltaWeight + deltaMomentum);
					neuron.updateRecentSlope(w, slope, false);
					neuron.updateSlope(w, config.getWeightRatio()
							* neuron.getWeights().get(w), false);
					// System.out.println("slope prv " + slope + " waga " +
					// neuron.getWeights().get(w) + " zmiana " +
					// neuron.getRecentWeightsChange().get(w));
				}
			}
		}
	}
	/*
	 * @Override public void updateParams(Network net, DataSetGK set) {
	 * super.updateParams(net, set); momentumAdaptaion(net, set); }
	 */

}
