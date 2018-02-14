package pl.phytia.prediction.crossValidation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pl.phytia.ann.networks.MLPNetwork;
import pl.phytia.ann.networks.Network;
import pl.phytia.api.SignalAPI;
import pl.phytia.model.conf.functions.neuron.FahlmanFunctionConfiguration;
import pl.phytia.model.conf.layers.MLPLayerConfiguration;
import pl.phytia.model.conf.networks.MLPNetworkConfiguration;
import pl.phytia.model.enums.EnumFunctionType;
import pl.phytia.model.enums.EnumLayerType;
import pl.phytia.model.pred.ValidationPairVO;
import pl.phytia.prediction.metaModels.CvMetaModel;
import pl.phytia.utils.Normalizer;
import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.ann.algorithms.bp.BPAlgorithm;
import pl.phytia.ann.algorithms.bp.QuickPropAlgorithm;
import pl.phytia.model.conf.algorithms.BackPropConfiguration;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.db.ModelDbVO;
import pl.phytia.model.enums.EnumLearningRatioType;
import pl.phytia.model.enums.EnumNetworkType;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.model.sets.SupervisedDataSet;
import pl.phytia.prediction.models.CVMLPModel;

public class MLPArchitectureTask extends CvMetaModel {

	protected static Logger logger = Logger
			.getLogger(MLPArchitectureTask.class);

	/**
	 * Wykonanie zadania.
	 */
	public static void main(String[] args) throws Exception {
		String kom = null;
		// String path = "E:\\phd\\cd\\walidacja-krzyzowa\\MLP-Architecture\\";
		String path = "c:\\tmp\\MLP-Architecture\\";
		MLPArchitectureTask task = new MLPArchitectureTask();
		/*
		 * Test dla każdej architektury sieci.
		 */
		task.prepareTestDays(taskDays);
		AnnModelConfiguration modelConfig = task
				.preparePredictorConfiguration();
		int inputs = modelConfig.getInputEnergyDelay()
				+ modelConfig.getInputHumidityDelay()
				+ modelConfig.getInputInsolationDelay()
				+ modelConfig.getInputTemperatureDelay();
		int[][] netArchConf = new int[][] { { inputs, 1 }, // 00
				{ inputs, 5, 1 }, // 01
				{ inputs, 10, 1 }, // 02
				{ inputs, 15, 1 }, // 03
				{ inputs, 20, 1 }, // 04
				{ inputs, 25, 1 }, // 05
				{ inputs, 5, 5, 1 }, // 06
				{ inputs, 10, 5, 1 }, // 07
		/*
		 * { inputs, 50, 1}, //08 { inputs, 75, 1}, //09 { inputs, 100, 1}, //10 {
		 * inputs, 125, 1}, //11 { inputs, 150, 1}, //12 { inputs, 175, 1}, //13 {
		 * inputs, 200, 1}, //14 { inputs, 15, 5, 1}, //15 { inputs, 25, 5, 1},
		 * //16 { inputs, 25, 10, 1}, //17 { inputs, 25, 15, 1}, //18 { inputs,
		 * 25, 20, 1}, //19 { inputs, 25, 25, 1}, //20 { inputs, 25, 35, 1} //21
		 */
		};
		for (int k = 13; k < netArchConf.length; ++k) {
			StringBuilder fileName = new StringBuilder("MLPArchitectureTask");
			if (k < 10) {
				fileName.append("-0" + k + ".txt");
			} else {
				fileName.append("-" + k + ".txt");
			}
			task.prepareModels(modelConfig, netArchConf[k]);
			BufferedWriter out = new BufferedWriter(new FileWriter(path
					+ fileName.toString()));
			out
					.write("\n-----------------------------------------------------------\n");
			kom = "Test dla konfiguracji sieci:\n"
					+ task.getPredictors().get(0).getNetwork().getConfig()
							.toString();
			System.out.println(kom);
			out.write(kom + "\n");
			out
					.write("-----------------------------------------------------------\n");

			task.prediction();
			List<String> resultTxt = task.crossValidationErrors();
			for (String res : resultTxt) {
				System.out.println(res);
				out.write(" " + res + "\n");
			}
			out.close();
		}

	}

	@SuppressWarnings("unchecked")
	public void prepareModels(AnnModelConfiguration modelConf, int[] netArchConf) {
		try {

			List<CVMLPModel> models = new ArrayList<CVMLPModel>();
			for (int i = 0; i < taksDays.length; ++i) {
				List<ValidationPairVO> valSets = SignalAPI
						.prepareLOOCossValidationData(
								EnumSignalType.MODELLING_DATA_NORMALIZED
										.toString(), taksDays[i], modelConf
										.getInputWindowSizeFrom(), modelConf
										.getInputWindowSizeTo(), modelConf
										.getInputWindowYearsDelay(), modelConf
										.getInputEnergyDelay(), modelConf
										.getInputTemperatureDelay(), modelConf
										.getInputHumidityDelay(), modelConf
										.getInputInsolationDelay());
				for (int j = 0; j < valSets.size(); ++j) {

					CVMLPModel m = new CVMLPModel(modelConf,
							prepareNetwork(netArchConf),
							prepareFirstPhaseAlgorithm(),
							prepareSecondPhaseAlgorithm(), valSets.get(j)
									.getTraningSet(), valSets.get(j)
									.getValidationSet(), new Normalizer(),
							valSets.get(j).getDayClass());
					m.setPredictionDay(taksDays[i]);
					m.initialize(modelConf);
					models.add(m);
				}
				setPredictors(models);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AnnModelConfiguration preparePredictorConfiguration() {
		return new AnnModelConfiguration(EnumNetworkType.MLP, 1, 0, 0, 24, -15,
				+15, -2, 24, 0.1, null, null, true, false);

	}

	public Algorithm prepareFirstPhaseAlgorithm() {
		BackPropConfiguration conf = new BackPropConfiguration(
				EnumLearningRatioType.CONSTANT, EnumFunctionType.SSE,
				EnumFunctionType.MAPE);
		conf.setMaximumGrowthRatio(1.75);
		conf.setMomentumRatio(0.3);
		conf.setLearningRatio(0.2);
		conf.setWeightRatio(-0.00001);
		conf.setMaxIteration(0);
		conf.setMinError(2);
		Algorithm<QuickPropAlgorithm, BackPropConfiguration, SupervisedDataSet, MLPNetwork> alg = new QuickPropAlgorithm(
				conf);
		alg.initialize(conf);
		return alg;
	}

	public Algorithm prepareSecondPhaseAlgorithm() {
		BackPropConfiguration conf = new BackPropConfiguration(
				EnumLearningRatioType.CONSTANT, EnumFunctionType.SSE,
				EnumFunctionType.MAPE);
		conf.setLearningRatio(0.1);
		conf.setMaxIteration(3000);
		conf.setMaxNotFoundBetterSolution(500);
		conf.setMinError(1.0);
		Algorithm<BPAlgorithm, BackPropConfiguration, SupervisedDataSet, MLPNetwork> alg = new BPAlgorithm(
				conf);
		alg.initialize(conf);
		return alg;
	}

	@SuppressWarnings("unchecked")
	public Network prepareNetwork(int[] netArchConf) {
		/*
		 * Lista warstw sieci.
		 */
		List<MLPLayerConfiguration> cl = new ArrayList<MLPLayerConfiguration>();

		if (netArchConf.length == 2) {
			/*
			 * Konfiguracja warstwy wyjściowej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f1 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f1.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f1.setNumberOfNeurons(netArchConf[1]);
			f1.setNumberOfInputs(netArchConf[0]);
			f1.setTypeOfLayer(EnumLayerType.INOUT);
			cl.add(f1);
		} else if (netArchConf.length == 3) {
			/*
			 * Konfiguracja 1 warstwy ukrytej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f1 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f1.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f1.setNumberOfNeurons(netArchConf[1]);
			f1.setNumberOfInputs(netArchConf[0]);
			f1.setTypeOfLayer(EnumLayerType.INPUT);
			cl.add(f1);
			/*
			 * Konfiguracja warstwy wyjściowej .
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f2 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f2.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f2.setNumberOfNeurons(netArchConf[2]);
			f2.setNumberOfInputs(netArchConf[1]);
			f2.setTypeOfLayer(EnumLayerType.OUTPUT);
			cl.add(f2);
		} else if (netArchConf.length == 4) {
			/*
			 * Konfiguracja 1 warstwy ukrytej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f1 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f1.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f1.setNumberOfNeurons(netArchConf[1]);
			f1.setNumberOfInputs(netArchConf[0]);
			f1.setTypeOfLayer(EnumLayerType.INPUT);
			cl.add(f1);
			/*
			 * Konfiguracja 2 warstwy ukrytej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f2 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f2.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f2.setNumberOfNeurons(netArchConf[2]);
			f2.setNumberOfInputs(netArchConf[1]);
			f2.setTypeOfLayer(EnumLayerType.HIDDEN);
			cl.add(f2);
			/*
			 * Konfiguracja warstwy wyjściowej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f3 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f3.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f3.setNumberOfNeurons(netArchConf[3]);
			f3.setNumberOfInputs(netArchConf[2]);
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
		Network net = new MLPNetwork(netConf);
		net.initialize(netConf);
		return net;
	}

	@Override
	public void preparation() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Network prepareMLPNetwork(String State) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getPredictorStructure() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Algorithm prepareFirstPhaseTrainAlgorithm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Algorithm prepareSecondPhaseTrainAlgorithm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected CVMLPModel preprareModel(ModelDbVO modelDb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Network prepareNetwork(AnnModelConfiguration conf) {
		// TODO Auto-generated method stub
		return null;
	}

}
