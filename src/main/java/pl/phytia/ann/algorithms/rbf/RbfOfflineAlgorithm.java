package pl.phytia.ann.algorithms.rbf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.waw.ibspan.phytia.ann.layers.RbfLayer;
import pl.waw.ibspan.phytia.ann.networks.RBFNetwork;
import pl.waw.ibspan.phytia.model.conf.algorithms.RbfAlgorithmConfiguration;
import pl.waw.ibspan.phytia.model.sets.DoubleVector;
import pl.waw.ibspan.phytia.model.sets.PatternPairVO;
import pl.waw.ibspan.phytia.model.sets.SupervisedDataSet;

/**
 * Algorytm trenigu nadzorowanego (offline) sieci RBF dokonujący modyfikacji:
 * <li>centrów funcji bazowej,</li>
 * <li>rozmycia funcji bazowej,</li>
 * <li>wag sieci.</li>
 * <br>
 * <br>
 * 
 * @author Jarosław Protasiewicz
 */
public class RbfOfflineAlgorithm extends
		RbfSupervisedAlgorithm<RbfOfflineAlgorithm> {

	public RbfOfflineAlgorithm() {
		super();
	}

	public RbfOfflineAlgorithm(RbfAlgorithmConfiguration conf) {
		super(conf);
		initialize(conf);
	}

	public void training(RBFNetwork net, SupervisedDataSet set) {
		Iterator<PatternPairVO> it = null;
		PatternPairVO pattern = null;
		DoubleVector errors = null;
		DoubleVector[] rbfOutputs = new DoubleVector[((RbfLayer) net
				.getLayers().get(0)).getNeurons().size()];
		for (int i = 0; i < rbfOutputs.length; ++i) {
			rbfOutputs[i] = new DoubleVector(set.getSet().size(), 0.0);
		}
		List<DoubleVector> inputs = null;
		currEpoch = 0;
		do {
			currEpoch++;
			it = set.iterator();
			errors = new DoubleVector();
			inputs = new ArrayList<DoubleVector>();
			int indx = 0;
			while (it.hasNext()) {
				pattern = it.next();
				net.simulation(pattern.getInputs());
				errors.add(getErrorFunction().computeError(net,
						pattern.getOutputs()));
				DoubleVector outs = ((RbfLayer) net.getLayers().get(0))
						.getOutputs();
				for (int i = 0; i < outs.size(); ++i) {
					rbfOutputs[i].set(indx, outs.get(i));
				}
				inputs.add(pattern.getInputs());
				indx++;
			}
			if (getConfig().isWeightsAdaptation()) {
				updateWeights(net, errors, rbfOutputs);
			}
			if (getConfig().isCentersAdaptation()) {
				updateCenters(net, errors, rbfOutputs, inputs);
			}
			if (getConfig().isWidthAdaptation()) {
				updateWidth(net, errors, rbfOutputs, inputs);
			}
		} while (!checkStopCondition(net, set));
	}

	/*
	 * @Override public double computeWidthGradient(RbfNeuron neuron,
	 * DoubleVector errors, double weight, DoubleVector inputs) { return
	 * neuron.getRadialFunction().computeOfflineWidthGradient(errors, weight,
	 * inputs); }
	 * 
	 * @Override public DoubleVector computeCentersGradient(RbfNeuron neuron,
	 * DoubleVector errors, double weight, DoubleVector inputs) { return
	 * neuron.getRadialFunction().computeOfflineCentersGradient(errors, weight,
	 * inputs); }
	 * 
	 * @Override public double computeWeightGradient(RbfNeuron neuron,
	 * DoubleVector errors) { return
	 * (neuron.getRadialFunction().computeOfflineWeightGradient(errors)); }
	 */
}
