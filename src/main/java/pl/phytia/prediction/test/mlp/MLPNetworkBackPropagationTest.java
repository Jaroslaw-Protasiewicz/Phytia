package pl.phytia.prediction.test.mlp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.phytia.model.conf.networks.MLPNetworkConfiguration;
import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.ann.algorithms.bp.BPAlgorithm;
import pl.phytia.ann.networks.MLPNetwork;
import pl.phytia.ann.networks.Network;
import pl.phytia.model.conf.algorithms.BackPropConfiguration;
import pl.phytia.model.conf.functions.neuron.SUnipolarFunctionConfiguration;
import pl.phytia.model.conf.layers.MLPLayerConfiguration;
import pl.phytia.model.enums.EnumFunctionType;
import pl.phytia.model.enums.EnumIterationType;
import pl.phytia.model.enums.EnumLayerType;
import pl.phytia.model.enums.EnumLearningRatioType;
import pl.phytia.model.enums.EnumNetworkType;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.PatternPairVO;
import pl.phytia.model.sets.SupervisedDataSet;

public class MLPNetworkBackPropagationTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		MLPNetworkBackPropagationTest test = new MLPNetworkBackPropagationTest();
		Network net = test.prepareNet();
		SupervisedDataSet set = test.prepareSet();
		Algorithm algorithm = test.prepareAlgorithm();
		algorithm.training(net, set);
		test.test(net, set);

	}

	public void test(Network net, SupervisedDataSet set) {
		Iterator<PatternPairVO> it = set.iterator();
		while (it.hasNext()) {
			PatternPairVO p = it.next();
			net.simulation(p.getInputs());
			System.out.println("y = " + net.getOutputs().get(0) + " d = "
					+ p.getOutputs().get(0));
		}
	}

	public Algorithm prepareAlgorithm() {
		BackPropConfiguration conf = new BackPropConfiguration(
				EnumLearningRatioType.CONSTANT, EnumFunctionType.SSE,
				EnumFunctionType.SSE);
		conf.setLearningRatio(0.4);
		conf.setMaxIteration(2000);
		conf.setMinError(0.0001);
		conf.setMaximumGrowthRatio(1.04);
		Algorithm<BPAlgorithm, BackPropConfiguration, SupervisedDataSet, MLPNetwork> alg = new BPAlgorithm(
				conf);
		alg.initialize(conf);
		return alg;
	}

	public SupervisedDataSet prepareSet() {
		/*
		 * Zbiór testowy = XOR.
		 */
		List<PatternPairVO> patterns = new ArrayList<PatternPairVO>(4);

		DoubleVector x1 = new DoubleVector(2);
		DoubleVector y1 = new DoubleVector(1);
		x1.add(0, new Double(0.0));
		x1.add(1, new Double(0.0));
		y1.add(0, new Double(0.0));
		patterns.add(new PatternPairVO(x1, y1));

		DoubleVector x2 = new DoubleVector(2);
		DoubleVector y2 = new DoubleVector(1);
		x2.add(0, new Double(0.0));
		x2.add(1, new Double(1.0));
		y2.add(0, new Double(1.0));
		patterns.add(new PatternPairVO(x2, y2));

		DoubleVector x3 = new DoubleVector(2);
		DoubleVector y3 = new DoubleVector(1);
		x3.add(0, new Double(1.0));
		x3.add(1, new Double(0.0));
		y3.add(0, new Double(1.0));
		patterns.add(new PatternPairVO(x3, y3));

		DoubleVector x4 = new DoubleVector(2);
		DoubleVector y4 = new DoubleVector(1);
		x4.add(0, new Double(1.0));
		x4.add(1, new Double(1.0));
		y4.add(0, new Double(0.0));
		patterns.add(new PatternPairVO(x4, y4));

		SupervisedDataSet set = new SupervisedDataSet(patterns,
				EnumIterationType.RANDOM);
		set.setNormalized(true);
		return set;
	}

	@SuppressWarnings("unchecked")
	public Network prepareNet() {
		/*
		 * Lista warstw sieci.
		 */
		List<MLPLayerConfiguration> cl = new ArrayList<MLPLayerConfiguration>();
		/*
		 * Konfiguracja 1 warstwy sieci.
		 */
		MLPLayerConfiguration<SUnipolarFunctionConfiguration> f1 = new MLPLayerConfiguration<SUnipolarFunctionConfiguration>();
		f1.setNeuronFunctionConf(new SUnipolarFunctionConfiguration());
		f1.setNumberOfNeurons(3);
		f1.setNumberOfInputs(2);
		f1.setTypeOfLayer(EnumLayerType.INPUT);
		cl.add(f1);
		/*
		 * Konfiguracja 2 warstwy sieci.
		 */
		MLPLayerConfiguration<SUnipolarFunctionConfiguration> f2 = new MLPLayerConfiguration<SUnipolarFunctionConfiguration>();
		f2.setNeuronFunctionConf(new SUnipolarFunctionConfiguration());
		f2.setNumberOfNeurons(2);
		f2.setNumberOfInputs(3);
		f2.setTypeOfLayer(EnumLayerType.HIDDEN);
		cl.add(f2);
		/*
		 * Konfiguracja 3 warstwy sieci.
		 */
		MLPLayerConfiguration<SUnipolarFunctionConfiguration> f3 = new MLPLayerConfiguration<SUnipolarFunctionConfiguration>();
		f3.setNeuronFunctionConf(new SUnipolarFunctionConfiguration());
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
