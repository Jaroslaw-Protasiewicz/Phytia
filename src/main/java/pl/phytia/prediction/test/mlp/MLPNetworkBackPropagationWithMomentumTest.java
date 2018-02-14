package pl.phytia.prediction.test.mlp;

import pl.phytia.ann.networks.Network;
import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.ann.algorithms.bp.BPWithMomentumAlgorithm;
import pl.phytia.ann.networks.MLPNetwork;
import pl.phytia.model.conf.algorithms.BackPropConfiguration;
import pl.phytia.model.enums.EnumFunctionType;
import pl.phytia.model.enums.EnumLearningRatioType;
import pl.phytia.model.sets.SupervisedDataSet;

public class MLPNetworkBackPropagationWithMomentumTest extends
		MLPNetworkBackPropagationTest {

	public static void main(String[] args) {

		MLPNetworkBackPropagationWithMomentumTest test = new MLPNetworkBackPropagationWithMomentumTest();
		Network net = test.prepareNet();
		net.storeState();
		SupervisedDataSet set = test.prepareSet();
		Algorithm algorithm = test.prepareAlgorithm();
		algorithm.training(net, set);
		test.test(net, set);
	}

	@Override
	public Algorithm prepareAlgorithm() {
		BackPropConfiguration conf = new BackPropConfiguration(
				EnumLearningRatioType.CONSTANT, EnumFunctionType.SSE,
				EnumFunctionType.SSE);
		conf.setLearningRatio(0.4);
		conf.setMomentumRatio(0.3);
		conf.setMaxIteration(6000);
		conf.setMinError(0.001);
		Algorithm<BPWithMomentumAlgorithm, BackPropConfiguration, SupervisedDataSet, MLPNetwork> alg = new BPWithMomentumAlgorithm(
				conf);
		alg.initialize(conf);
		return alg;
	}

}
