package pl.phytia.prediction.metaModels;

import java.util.ArrayList;
import java.util.List;

import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.ann.networks.MLPNetwork;
import pl.phytia.ann.networks.Network;
import pl.phytia.model.conf.functions.neuron.FahlmanFunctionConfiguration;
import pl.phytia.model.conf.layers.MLPLayerConfiguration;
import pl.phytia.model.conf.networks.MLPNetworkConfiguration;
import pl.phytia.model.enums.EnumLayerType;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.prediction.models.MLPModel;
import pl.phytia.utils.Normalizer;
import pl.phytia.ann.layers.MLPLayer;
import pl.phytia.ann.neurons.Perceptron;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.db.ModelDbVO;
import pl.phytia.model.enums.EnumNetworkType;
import pl.phytia.model.sets.SupervisedDataSet;

/**
 * Abstrakcyjny szereg modeli MLP.
 * 
 * @author Jarosław Protasiewicz
 */
public abstract class MLPSerialMetaModel extends SerialMetaModel<MLPModel> {

	@Override
	protected MLPModel preprareModel(ModelDbVO modelDb) {
		return new MLPModel(prepareModelConfiguration(modelDb.getConf()),
				prepareMLPNetwork(modelDb.getNetwork()),
				prepareFirstPhaseTrainAlgorithm(),
				prepareSecondPhaseTrainAlgorithm(), null, null, null, modelDb
						.getModelClass());
	}

	@Override
	protected MLPModel preprareMLPModel(AnnModelConfiguration config,
										Network network, Algorithm algorithm,
										Algorithm secondPhaseAlgorithm, SupervisedDataSet modelSet,
										SupervisedDataSet predictSet, Normalizer normalizer,
										String modelClass) {
		return new MLPModel(config, network, algorithm, secondPhaseAlgorithm,
				modelSet, predictSet, normalizer, modelClass);
	}

	@Override
	protected Network prepareMLPNetwork(String state) {
		Network net = new MLPNetwork();
		net.loadState(state);
		for (int l = 0; l < net.getLayers().size(); ++l) {
			MLPLayer layer = (MLPLayer) net.getLayers().get(l);
			for (int n = 0; n < layer.getNeurons().size(); ++n) {
				Perceptron neuron = (Perceptron) layer.getNeurons().get(n);
				DoubleVector weights = new DoubleVector(neuron.getWeights()
						.size());
				for (int tbw = 0; tbw < neuron.getWeights().size(); ++tbw) {
					weights.add(tbw, 0.0);
				}
				neuron.setTheBestWeights(weights);
				neuron.setDeltaSlope(weights);
			}
		}
		return net;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Network prepareNetwork(AnnModelConfiguration conf) {
		int inputs = conf.getInputEnergyDelay() + conf.getInputHumidityDelay()
				+ conf.getInputInsolationDelay()
				+ conf.getInputTemperatureDelay();
		int[] netArchConf = getPredictorStructure();
		/*
		 * Lista warstw sieci.
		 */
		List<MLPLayerConfiguration> cl = new ArrayList<MLPLayerConfiguration>();

		if (netArchConf.length == 1) {
			/*
			 * Konfiguracja warstwy wyjściowej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f1 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f1.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f1.setNumberOfNeurons(netArchConf[0]);
			f1.setNumberOfInputs(inputs);
			f1.setTypeOfLayer(EnumLayerType.INOUT);
			cl.add(f1);
		} else if (netArchConf.length == 2) {
			/*
			 * Konfiguracja 1 warstwy ukrytej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f1 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f1.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f1.setNumberOfNeurons(netArchConf[0]);
			f1.setNumberOfInputs(inputs);
			f1.setTypeOfLayer(EnumLayerType.INPUT);
			cl.add(f1);
			/*
			 * Konfiguracja warstwy wyjściowej.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f2 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f2.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f2.setNumberOfNeurons(netArchConf[1]);
			f2.setNumberOfInputs(netArchConf[0]);
			f2.setTypeOfLayer(EnumLayerType.OUTPUT);
			cl.add(f2);
		} else if (netArchConf.length == 3) {
			/*
			 * Konfiguracja 1 warstwy ukrytej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f1 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f1.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f1.setNumberOfNeurons(netArchConf[0]);
			f1.setNumberOfInputs(inputs);
			f1.setTypeOfLayer(EnumLayerType.INPUT);
			cl.add(f1);
			/*
			 * Konfiguracja 2 warstwy ukrytej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f2 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f2.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f2.setNumberOfNeurons(netArchConf[1]);
			f2.setNumberOfInputs(netArchConf[0]);
			f2.setTypeOfLayer(EnumLayerType.HIDDEN);
			cl.add(f2);
			/*
			 * Konfiguracja warstwy wyjściowej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f3 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f3.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f3.setNumberOfNeurons(netArchConf[2]);
			f3.setNumberOfInputs(netArchConf[1]);
			f3.setTypeOfLayer(EnumLayerType.OUTPUT);
			cl.add(f3);

		}
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
