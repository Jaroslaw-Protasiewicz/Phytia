package pl.phytia.ann.algorithms.bp;

import pl.waw.ibspan.phytia.ann.layers.MLPLayer;
import pl.waw.ibspan.phytia.ann.networks.MLPNetwork;
import pl.waw.ibspan.phytia.ann.neurons.Perceptron;
import pl.waw.ibspan.phytia.model.conf.algorithms.BackPropConfiguration;
import pl.waw.ibspan.phytia.model.enums.EnumLayerType;
import pl.waw.ibspan.phytia.model.sets.DoubleVector;

/**
 * Podstawowy algorytm wstecznej propagacji błędu.
 * 
 * @author Jarosław Protasiewicz
 */
public class BPAlgorithm extends BackPropAlgorithm<BPAlgorithm> {

	/**
	 * Konstruktor.
	 */
	public BPAlgorithm(BackPropConfiguration conf) {
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
					neuron.updateWeight(w, deltaWeight);
				}
			}
		}
	}
}
