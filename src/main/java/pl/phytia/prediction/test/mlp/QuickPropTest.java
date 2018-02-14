package pl.phytia.prediction.test.mlp;

import java.util.ArrayList;
import java.util.List;

import pl.phytia.ann.networks.MLPNetwork;
import pl.phytia.ann.networks.Network;
import pl.phytia.model.conf.functions.neuron.FahlmanFunctionConfiguration;
import pl.phytia.model.conf.networks.MLPNetworkConfiguration;
import pl.phytia.model.enums.EnumLayerType;
import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.ann.algorithms.bp.QuickPropAlgorithm;
import pl.phytia.model.conf.algorithms.BackPropConfiguration;
import pl.phytia.model.conf.layers.MLPLayerConfiguration;
import pl.phytia.model.enums.EnumFunctionType;
import pl.phytia.model.enums.EnumLearningRatioType;
import pl.phytia.model.enums.EnumNetworkType;
import pl.phytia.model.sets.SupervisedDataSet;

public class QuickPropTest extends MLPNetworkBackPropagationTest {

	public static void main(String[] args) {

		QuickPropTest test = new QuickPropTest();
		Network net = test.prepareNet();
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
		conf.setMaximumGrowthRatio(0.5);
		conf.setWeightRatio(-0.000001);
		conf.setMinError(0.001);
		conf.setMaxIteration(2000);
		conf.setMaxNotFoundBetterSolution(2000);
		conf.setLearningRatio(0.5);
		Algorithm<QuickPropAlgorithm, BackPropConfiguration, SupervisedDataSet, MLPNetwork> alg = new QuickPropAlgorithm(
				conf);
		alg.initialize(conf);
		return alg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Network prepareNet() {
		/*
		 * Lista warstw sieci.
		 */
		List<MLPLayerConfiguration> cl = new ArrayList<MLPLayerConfiguration>();
		/*
		 * Konfiguracja 1 warstwy sieci.
		 */
		MLPLayerConfiguration<FahlmanFunctionConfiguration> f1 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
		f1.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
		f1.setNumberOfNeurons(3);
		f1.setNumberOfInputs(2);
		f1.setTypeOfLayer(EnumLayerType.INPUT);
		cl.add(f1);
		/*
		 * Konfiguracja 2 warstwy sieci.
		 */
		MLPLayerConfiguration<FahlmanFunctionConfiguration> f2 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
		f2.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
		f2.setNumberOfNeurons(2);
		f2.setNumberOfInputs(3);
		f2.setTypeOfLayer(EnumLayerType.HIDDEN);
		cl.add(f2);
		/*
		 * Konfiguracja 3 warstwy sieci.
		 */
		MLPLayerConfiguration<FahlmanFunctionConfiguration> f3 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
		f3.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
		f3.setNumberOfNeurons(1);
		f3.setNumberOfInputs(2);
		f3.setTypeOfLayer(EnumLayerType.OUTPUT);
		cl.add(f3);
		/*
		 * Konfiguracja sieci neuronowej.
		 */
		MLPNetworkConfiguration netConf = new MLPNetworkConfiguration();
		netConf.setTypeOfNetwork(EnumNetworkType.MLP);
		netConf.setLayersConf(cl);
		/*
		 * Utworzenie sieci neuronowej.
		 */
		Network n = new MLPNetwork(netConf);
		n.initialize(netConf);
		return n;
	}

}
