package pl.phytia.ann.algorithms.rbf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import pl.phytia.model.conf.algorithms.RbfAlgorithmConfiguration;
import pl.phytia.model.sets.PatternPairVO;
import pl.phytia.ann.layers.RbfLayer;
import pl.phytia.ann.networks.RBFNetwork;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.SupervisedDataSet;

/**
 * Algorytm trenigu nadzorowanego (online) sieci RBF dokonujący modyfikacji:
 * <li>centrów funcji bazowej,</li>
 * <li>rozmycia funcji bazowej,</li>
 * <li>wag sieci.</li>
 * <br>
 * <br>
 * 
 * @author Jarosław Protasiewicz
 */
public class RbfOnlineAlgorithm extends
		RbfSupervisedAlgorithm<RbfOnlineAlgorithm> {

	Logger looger = Logger.getLogger(RbfOnlineAlgorithm.class);

	public RbfOnlineAlgorithm() {
		super();
	}

	public RbfOnlineAlgorithm(RbfAlgorithmConfiguration conf) {
		super(conf);
		initialize(conf);
	}

	public void training(RBFNetwork net, SupervisedDataSet set) {
		Iterator<PatternPairVO> it = null;
		PatternPairVO pattern = null;
		DoubleVector errors = new DoubleVector(1, 0.0);
		List<DoubleVector> inputs = new ArrayList<DoubleVector>();
		inputs.add(new DoubleVector());
		DoubleVector[] rbfOutputs = new DoubleVector[((RbfLayer) net
				.getLayers().get(0)).getNeurons().size()];
		for (int i = 0; i < rbfOutputs.length; ++i) {
			rbfOutputs[i] = new DoubleVector(1, 0.0);
		}
		currEpoch = 0;
		do {
			currEpoch++;
			it = set.iterator();
			while (it.hasNext()) {
				pattern = it.next();
				net.simulation(pattern.getInputs());
				getErrorFunction().setError(0.0);
				errors.set(0, getErrorFunction().computeError(net,
						pattern.getOutputs()) / 2);
				inputs.set(0, pattern.getInputs());
				DoubleVector outs = ((RbfLayer) net.getLayers().get(0))
						.getOutputs();
				for (int i = 0; i < outs.size(); ++i) {
					rbfOutputs[i].set(0, outs.get(i));
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
			}

		} while (!checkStopCondition(net, set));
	}

	/*
	 * @Override public DoubleVector computeCentersGradient(RbfNeuron neuron,
	 * DoubleVector errors, double weight, DoubleVector inputs) { return
	 * neuron.getRadialFunction().computeOnlineCentersGradient(errors.get(0),
	 * weight, inputs); }
	 * 
	 * @Override public double computeWeightGradient(RbfNeuron neuron,
	 * DoubleVector errors) { return
	 * (neuron.getRadialFunction().computeOnlineWeightGradient(errors.get(0))); }
	 * 
	 * @Override public double computeWidthGradient(RbfNeuron neuron,
	 * DoubleVector errors, double weight, DoubleVector inputs) { return
	 * neuron.getRadialFunction().computeOnlineWidthGradient(errors.get(0),
	 * weight, inputs); }
	 */
}
