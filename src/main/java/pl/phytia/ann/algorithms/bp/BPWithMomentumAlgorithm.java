package pl.phytia.ann.algorithms.bp;

import pl.waw.ibspan.phytia.ann.layers.MLPLayer;
import pl.waw.ibspan.phytia.ann.networks.MLPNetwork;
import pl.waw.ibspan.phytia.ann.neurons.Perceptron;
import pl.waw.ibspan.phytia.model.conf.algorithms.BackPropConfiguration;
import pl.waw.ibspan.phytia.model.enums.EnumLayerType;
import pl.waw.ibspan.phytia.model.sets.DoubleVector;
import pl.waw.ibspan.phytia.model.sets.SupervisedDataSet;

/**
 * Algorytm wstecznej propagacji błędu z momentem.
 * 
 * @author Jarosław Protasiewicz
 */
public class BPWithMomentumAlgorithm extends
		BackPropAlgorithm<BPWithMomentumAlgorithm> {

	/**
	 * Konstruktor.
	 */
	public BPWithMomentumAlgorithm(BackPropConfiguration conf) {
		super();
		initialize(conf);
	}

	public void updateWeights(MLPNetwork net, DoubleVector inputs) {
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
					double deltaWeight = 0;
					if (w == 0) {
						deltaWeight = 2 * this.getConfig().getLearningRatio()
								* neuron.getError() * (-1.0);
					} else {
						deltaWeight = 2 * this.getConfig().getLearningRatio()
								* neuron.getError() * layerInputs.get(w - 1);
					}
					double deltaMomentum = 0;
					if (this.currEpoch > 0) {
						deltaMomentum = this.config.getMomentumRatio()
								* neuron.getRecentWeightsChange().get(w);
					} else {
						deltaMomentum = 0;
					}
					neuron.updateRecentWeightChange(w, deltaWeight
							+ deltaMomentum);
					neuron.updateRecentMomentumChange(w, deltaMomentum);
					neuron.updateWeight(w, deltaWeight + deltaMomentum);
				}
			}
		}
	}

	@Override
	public void updateParams(MLPNetwork net, SupervisedDataSet set) {
		super.updateParams(net, set);
		momentumAdaptaion(net, set);
	}

	/**
	 * Sprawdzenie czy momentum może być zaakceptowane
	 * 
	 * @param net
	 *            Sieć neuronowa
	 * @param set
	 *            Zbiór uczący
	 */
	public void momentumAdaptaion(MLPNetwork net, SupervisedDataSet set) {
		if (this.currEpoch > 0) {
			if (getErrorFunction().getError() < config.getMomentumGrowthRatio()
					* getErrorFunction().getRecentError()) {
				for (int l = 0; l < net.getLayers().size(); ++l) {
					MLPLayer layer = (MLPLayer) net.getLayers().get(l);
					for (Perceptron p : layer.getNeurons()) {
						for (int w = 0; w < p.getWeights().size(); ++w) {
							p.getWeights().set(
									w,
									p.getWeights().get(w)
											- p.getRecentMomentumChange()
													.get(w));
						}
					}
				}
				getErrorFunction().computeError(net, set);
			}
		}

	}

	@Override
	public void reset(MLPNetwork net) {
		super.reset(net);
		for (int l = 0; l < net.getLayers().size(); ++l) {
			MLPLayer layer = (MLPLayer) net.getLayers().get(l);
			for (Perceptron p : layer.getNeurons()) {
				for (int w = 0; w < p.getRecentMomentumChange().size(); ++w) {
					p.getRecentMomentumChange().set(w, 0.0d);
				}
			}
		}
	}
}
