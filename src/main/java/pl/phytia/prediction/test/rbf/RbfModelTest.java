package pl.phytia.prediction.test.rbf;

import pl.phytia.model.conf.algorithms.RbfAlgorithmConfiguration;
import pl.phytia.model.conf.functions.centerSelect.CenterSelFunctionConfiguration;
import pl.phytia.model.conf.functions.neuron.AdderFunctionConfiguration;
import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.ann.algorithms.rbf.RbfMatrixInvAlgorithm;
import pl.phytia.ann.algorithms.rbf.RbfOfflineAlgorithm;
import pl.phytia.ann.networks.RBFNetwork;
import pl.phytia.model.conf.functions.neuron.RbfFunctionConfiguration;
import pl.phytia.model.conf.layers.AdderLayerConfiguration;
import pl.phytia.model.conf.layers.RbfLayerConfiguration;
import pl.phytia.model.conf.networks.RbfNetworkConfiguration;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.enums.EnumCenterSelectType;
import pl.phytia.model.enums.EnumCenterWidthType;
import pl.phytia.model.enums.EnumDistanceType;
import pl.phytia.model.enums.EnumFunctionType;
import pl.phytia.model.enums.EnumLayerType;
import pl.phytia.model.enums.EnumNetworkType;
import pl.phytia.prediction.models.RBFModel;

public class RbfModelTest {

	public static void main(String[] args) throws Exception {
		// Date predictionDay =
		// Localization.plDateFormatMedium.parse("2004-07-15");
		// Date predictionStartDay =
		// Localization.plDateFormatMedium.parse("2004-07-15");
		RbfModelTest test = new RbfModelTest();
		RBFModel model = test.prepareModel();
		// model.initialize(model.getConfig(), predictionDay,
		// predictionStartDay);
		model.initializeXor(model.getConfig());
		model.modeling();
		model.executionXOR();
	}

	protected RBFModel prepareModel() {
		AnnModelConfiguration modelConf = prepareModelConfigruration();
		return new RBFModel(prepareModelConfigruration(),
				prepareNetwork(modelConf), prepareFirstPhaseAlgorithm(),
				prepareSecondPhaseAlgorithm(), null, null, null, null);
	}

	protected AnnModelConfiguration prepareModelConfigruration() {
		return new AnnModelConfiguration(EnumNetworkType.RBF, 1, 1, 1, 24, -15,
				15, -2, 24, 0.1, null, null, false, true);

	}

	protected RBFNetwork prepareNetwork(AnnModelConfiguration modelConf) {
		/*
		 * int inputs = modelConf.getInputEnergyDelay() +
		 * modelConf.getInputHumidityDelay() +
		 * modelConf.getInputInsolationDelay() +
		 * modelConf.getInputTemperatureDelay();
		 */
		int inputs = 2;
		/*
		 * Konfiguracja funkcji RBF.
		 */
		RbfFunctionConfiguration rbfFunConf = new RbfFunctionConfiguration();
		rbfFunConf.setDefaultWidth(0.2);
		rbfFunConf.setDistanceType(EnumDistanceType.Euclidean);
		rbfFunConf.setNumberOfInputs(0);
		rbfFunConf.setPowerDegree(3);
		rbfFunConf.setTypeOfFunction(EnumFunctionType.GaussianRbf);
		/*
		 * Konfiguracja warstwy RBF
		 */
		RbfLayerConfiguration rbfLayerConf = new RbfLayerConfiguration();
		rbfLayerConf.setCenterWidthType(EnumCenterWidthType.ADAPTIVE);
		rbfLayerConf.setNumberOfInputs(inputs);
		rbfLayerConf.setRbfFunctionConf(rbfFunConf);
		rbfLayerConf.setTypeOfLayer(EnumLayerType.INPUT);

		/*
		 * Konfiguracja funkcji sumatora.
		 */
		AdderFunctionConfiguration adderFunConf = new AdderFunctionConfiguration();
		adderFunConf.setNumberOfInputs(0);
		adderFunConf.setTypeOfFunction(EnumFunctionType.Pipe);
		/*
		 * Konfiguracja warstwy sumatora
		 */
		AdderLayerConfiguration adderLayerConf = new AdderLayerConfiguration();
		adderLayerConf.setNumberOfInputs(0);
		adderLayerConf.setNumberOfNeurons(1);
		adderLayerConf.setTypeOfLayer(EnumLayerType.OUTPUT);
		adderLayerConf.setAdderConf(adderFunConf);

		/*
		 * Konfiguracja sieci RBF.
		 */
		RbfNetworkConfiguration confRbf = new RbfNetworkConfiguration();
		confRbf.setRbfLayerConf(rbfLayerConf);
		confRbf.setAdderLayerConf(adderLayerConf);
		confRbf.setTypeOfNetwork(EnumNetworkType.RBF);
		/*
		 * Sieć.
		 */
		return new RBFNetwork(confRbf);
	}

	protected Algorithm prepareFirstPhaseAlgorithm() {
		/*
		 * Konfiguracja funkcji doboru centrów.
		 */
		CenterSelFunctionConfiguration funConf = new CenterSelFunctionConfiguration();
		// funConf.setCenterSelectType(EnumCenterSelectType.K_MEAN_CENTER_SELECT);
		funConf.setCenterSelectType(EnumCenterSelectType.RANDOM_CENTER_SELECT);
		funConf.setDistanceTypeKMean(EnumDistanceType.Euclidean);
		funConf.setInitLearnRatioKMean(0.1);
		funConf.setInitNumberOfCenters(4);
		funConf.setMaxStepKMean(100);
		/*
		 * Konfigurajca algorytmu.
		 */
		RbfAlgorithmConfiguration conf = new RbfAlgorithmConfiguration();
		conf.setCenterSelFunctionConf(funConf);
		conf.setMaxIteration(1);
		conf.setMaxNotFoundBetterSolution(1);
		conf.setNumerOfRepeat(1);
		conf.setErrorFunctionType(EnumFunctionType.DIFF);
		conf.setStopCondFunctionType(EnumFunctionType.MAPE);
		conf.setMinError(1);
		conf.setCentersAdaptation(false);
		conf.setCentersLearningRatio(0.1);
		conf.setWeightsAdaptation(false);
		conf.setWeightsLearnigRatio(0.1);
		conf.setWidthAdaptation(false);
		conf.setWidthLearnigRatio(0.1);

		return new RbfMatrixInvAlgorithm(conf);
	}

	protected Algorithm prepareSecondPhaseAlgorithm() {

		CenterSelFunctionConfiguration funConf = new CenterSelFunctionConfiguration();
		funConf.setCenterSelectType(EnumCenterSelectType.STRICT_CENTER_SELECT);
		funConf.setDistanceTypeKMean(EnumDistanceType.Euclidean);
		funConf.setInitLearnRatioKMean(0.1);
		funConf.setInitNumberOfCenters(4);
		funConf.setMaxStepKMean(1000);

		/*
		 * Konfiguracja algorytmu.
		 */
		RbfAlgorithmConfiguration conf = new RbfAlgorithmConfiguration();
		conf.setCenterSelFunctionConf(funConf);
		conf.setMaxIteration(1000);
		conf.setMaxNotFoundBetterSolution(10);
		conf.setNumerOfRepeat(1);
		conf.setErrorFunctionType(EnumFunctionType.DIFF);
		conf.setStopCondFunctionType(EnumFunctionType.SSE);
		conf.setMinError(0.001);
		conf.setCentersAdaptation(false);
		conf.setCentersLearningRatio(0.1);
		conf.setWeightsAdaptation(true);
		conf.setWeightsLearnigRatio(0.1);
		conf.setWidthAdaptation(false);
		conf.setWidthLearnigRatio(0.1);

		return new RbfOfflineAlgorithm(conf);
	}
}
