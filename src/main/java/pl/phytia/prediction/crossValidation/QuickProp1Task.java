package pl.phytia.prediction.crossValidation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.ann.algorithms.bp.BPAlgorithm;
import pl.phytia.ann.algorithms.bp.QuickPropAlgorithm;
import pl.phytia.ann.networks.MLPNetwork;
import pl.phytia.ann.networks.Network;
import pl.phytia.api.SignalAPI;
import pl.phytia.model.conf.algorithms.BackPropConfiguration;
import pl.phytia.model.conf.functions.neuron.FahlmanFunctionConfiguration;
import pl.phytia.model.conf.layers.MLPLayerConfiguration;
import pl.phytia.model.conf.networks.MLPNetworkConfiguration;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.db.ModelDbVO;
import pl.phytia.model.enums.EnumFunctionType;
import pl.phytia.model.enums.EnumLayerType;
import pl.phytia.model.enums.EnumLearningRatioType;
import pl.phytia.model.enums.EnumNetworkType;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.model.pred.ValidationPairVO;
import pl.phytia.model.sets.SupervisedDataSet;
import pl.phytia.prediction.metaModels.CvMetaModel;
import pl.phytia.prediction.models.CVMLPModel;
import pl.phytia.utils.Normalizer;

public class QuickProp1Task extends CvMetaModel {

	public static void doIt() throws Exception {
		String kom = null;
		String path = "E:\\home\\phd\\QuickProp\\";

		QuickProp1Task task = new QuickProp1Task();
		task.prepareTestDays(taskDays);
		double maximumGrowthRatio[] = new double[] { 0.001, 0.01, 0.1, 0.5,
				1.0, 1.5, 1.75, 2, 5 };
		for (int k = 0; k < maximumGrowthRatio.length; ++k) {
			StringBuilder fileName = new StringBuilder("QuickProp1Task");
			if (k < 10) {
				fileName.append("-0" + k + ".txt");
			} else {
				fileName.append("-" + k + ".txt");
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(path
					+ fileName.toString()));
			task.prepareModels(maximumGrowthRatio[k]);
			out
					.write("\n-----------------------------------------------------------\n");
			kom = "Test dla algorytmu:\n"
					+ task.getPredictors().get(0).getAlgorithm().getConfig()
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

	public AnnModelConfiguration preparePredictorConfiguration() {
		return new AnnModelConfiguration(EnumNetworkType.MLP, 1, 0, 0, 24, -15,
				+15, -2, 24, 0.1, null, null, true, false);
	}

	@SuppressWarnings("unchecked")
	public void prepareModels(double ratio) {
		try {
			AnnModelConfiguration modelConf = preparePredictorConfiguration();
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
							prepareNetwork(modelConf),
							prepareFirstPhaseMLPTrainAlgorithm(ratio),
							prepareSecondPhaseTrainAlgorithm(), valSets.get(j)
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

	@SuppressWarnings("unchecked")
	public Network prepareMLPNetwork(AnnModelConfiguration conf) {
		/*
		 * Struktura sieci.
		 */
		int inputs = conf.getInputEnergyDelay() + conf.getInputHumidityDelay()
				+ conf.getInputInsolationDelay()
				+ conf.getInputTemperatureDelay();
		int[] netArchConf = new int[] { inputs, 10, 1 };

		FahlmanFunctionConfiguration fconf = new FahlmanFunctionConfiguration();
		fconf.setDerivativeRatio(0.1);
		/*
		 * Lista warstw sieci.
		 */
		List<MLPLayerConfiguration> cl = new ArrayList<MLPLayerConfiguration>();

		if (netArchConf.length == 2) {
			/*
			 * Konfiguracja warstwy wyjściowej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f1 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f1.setNeuronFunctionConf(fconf);
			f1.setNumberOfNeurons(netArchConf[1]);
			f1.setNumberOfInputs(netArchConf[0]);
			f1.setTypeOfLayer(EnumLayerType.INOUT);
			cl.add(f1);
		} else if (netArchConf.length == 3) {
			/*
			 * Konfiguracja 1 warstwy ukrytej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f1 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f1.setNeuronFunctionConf(fconf);
			f1.setNumberOfNeurons(netArchConf[1]);
			f1.setNumberOfInputs(netArchConf[0]);
			f1.setTypeOfLayer(EnumLayerType.INPUT);
			cl.add(f1);
			/*
			 * Konfiguracja warstwy wyjściowej .
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f2 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f2.setNeuronFunctionConf(fconf);
			f2.setNumberOfNeurons(netArchConf[2]);
			f2.setNumberOfInputs(netArchConf[1]);
			f2.setTypeOfLayer(EnumLayerType.OUTPUT);
			cl.add(f2);
		} else if (netArchConf.length == 4) {
			/*
			 * Konfiguracja 1 warstwy ukrytej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f1 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f1.setNeuronFunctionConf(fconf);
			f1.setNumberOfNeurons(netArchConf[1]);
			f1.setNumberOfInputs(netArchConf[0]);
			f1.setTypeOfLayer(EnumLayerType.INPUT);
			cl.add(f1);
			/*
			 * Konfiguracja 2 warstwy ukrytej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f2 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f2.setNeuronFunctionConf(fconf);
			f2.setNumberOfNeurons(netArchConf[2]);
			f2.setNumberOfInputs(netArchConf[1]);
			f2.setTypeOfLayer(EnumLayerType.HIDDEN);
			cl.add(f2);
			/*
			 * Konfiguracja warstwy wyjściowej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f3 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f3.setNeuronFunctionConf(fconf);
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
	public int[] getPredictorStructure() {
		return null;
	}

	@Override
	public void preparation() {
		// TODO Auto-generated method stub

	}

	protected Algorithm prepareFirstPhaseMLPTrainAlgorithm(
			double maximumGrowthRatio) {
		BackPropConfiguration conf = new BackPropConfiguration(
				EnumLearningRatioType.CONSTANT, EnumFunctionType.SSE,
				EnumFunctionType.MAPE);
		conf.setMaximumGrowthRatio(maximumGrowthRatio);
		conf.setMomentumRatio(0.3);
		conf.setLearningRatio(0.1);
		conf.setWeightRatio(-1.0e-4);
		conf.setMaxIteration(500);
		conf.setMaxNotFoundBetterSolution(500);
		conf.setMinError(1.0);
		Algorithm<QuickPropAlgorithm, BackPropConfiguration, SupervisedDataSet, MLPNetwork> alg = new QuickPropAlgorithm(
				conf);
		alg.initialize(conf);
		return alg;
	}

	@Override
	protected Algorithm prepareFirstPhaseTrainAlgorithm() {
		return null;
	}

	@Override
	protected Algorithm prepareSecondPhaseTrainAlgorithm() {
		BackPropConfiguration conf = new BackPropConfiguration(
				EnumLearningRatioType.CONSTANT, EnumFunctionType.SSE,
				EnumFunctionType.MAPE);
		conf.setLearningRatio(0.1);
		conf.setMaxIteration(0);
		conf.setMinError(1.0);
		conf.setMaximumGrowthRatio(1.04);
		Algorithm<BPAlgorithm, BackPropConfiguration, SupervisedDataSet, MLPNetwork> alg = new BPAlgorithm(
				conf);
		alg.initialize(conf);
		return alg;
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
