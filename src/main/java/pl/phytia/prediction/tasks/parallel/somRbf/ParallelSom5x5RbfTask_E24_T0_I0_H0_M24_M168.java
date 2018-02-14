package pl.phytia.prediction.tasks.parallel.somRbf;

import java.text.ParseException;

import pl.phytia.ann.algorithms.rbf.RbfOfflineAlgorithm;
import pl.phytia.ann.algorithms.som.KohonenAlgorithm;
import pl.phytia.model.conf.algorithms.KohonenConfiguration;
import pl.phytia.model.conf.algorithms.RbfAlgorithmConfiguration;
import pl.phytia.model.conf.functions.centerSelect.CenterSelFunctionConfiguration;
import pl.phytia.model.enums.EnumDistanceType;
import pl.phytia.model.enums.EnumForecastType;
import pl.phytia.model.enums.EnumFunctionType;
import pl.phytia.model.enums.EnumMetaModelType;
import pl.phytia.utils.Localization;
import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.ann.algorithms.rbf.RbfMatrixInvAlgorithm;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.conf.pred.MetaModelConfiguration;
import pl.phytia.model.enums.EnumCenterSelectType;
import pl.phytia.model.enums.EnumNetworkType;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.prediction.metaModels.ParallelSomRbfMetaModel;
import pl.phytia.utils.Tools;

public class ParallelSom5x5RbfTask_E24_T0_I0_H0_M24_M168 extends
		ParallelSomRbfMetaModel {

	private static java.util.Date dateStart = null;

	private static java.util.Date dateStop = null;

	private static java.util.Date dateStartTrain = null;

	private static java.util.Date dateStopTrain = null;
	static {
		try {
			dateStart = Localization.plDateFormatMedium.parse("2004-08-31");
			dateStop = Localization.plDateFormatMedium.parse("2004-10-01");
			dateStartTrain = Localization.plDateFormatMedium
					.parse("2002-01-07");
			dateStopTrain = Localization.plDateFormatMedium.parse("2004-08-31");
			;
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void doIt() throws Exception {
		ParallelSom5x5RbfTask_E24_T0_I0_H0_M24_M168 task = new ParallelSom5x5RbfTask_E24_T0_I0_H0_M24_M168();
		MetaModelConfiguration conf = new MetaModelConfiguration();
		conf.setForcastType(EnumForecastType.E24_T0_H0_I0_M24_M168);
		conf.setInputSignal(EnumSignalType.MODELLING_DATA_NORMALIZED);
		conf.setOutputSignal(Tools.getForecastName(
				EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R5,
				EnumForecastType.E24_T0_H0_I0_M24_M168));
		conf.setMetamodelType(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R5);
		conf.setPredictionStart(dateStart);
		conf.setPredictionStop(dateStop);
		conf.setDaysAhead(2);
		task.initialize(conf);
		task.prediction();
		/*
		 * task.testClasses(EnumForecastType.E24_T0_H0_I0_D_H.toString(),
		 * EnumMetaModelType.PARALLEL_SOM_5x5_RBF.toString(), dateStartTrain,
		 * dateStopTrain, "e:\\tmp\\SOM_" +
		 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF,
		 * EnumForecastType.E24_T0_H0_I0_D_H) + ".xls");
		 */
	}

	@Override
	public int[] getClasifierStructure() {
		return new int[] { 5, 5 };
	}

	@Override
	public int[] getPredictorStructure() {
		return new int[] { 1, 1 };
	}

	@Override
	protected AnnModelConfiguration prepareClassifierConfiguration() {
		AnnModelConfiguration conf = new AnnModelConfiguration();
		conf.setNetworkType(EnumNetworkType.SOM);
		conf.setInputEnergyDelay(24);
		conf.setInputHumidityDelay(1);
		conf.setInputInsolationDelay(1);
		conf.setInputTemperatureDelay(1);
		conf.setInputDayType(false);
		conf.setInputHoliday(false);
		conf.setInputEMean24Delay(1);
		conf.setInputEMean168Delay(1);
		conf.setMaxTrainDataStart(dateStartTrain);
		conf.setMaxTrainDataStop(dateStopTrain);
		conf.setModelType(EnumNetworkType.SOM.toString());
		return conf;
	}

	@Override
	protected AnnModelConfiguration preparePredictorConfiguration() {
		AnnModelConfiguration conf = new AnnModelConfiguration();
		conf.setNetworkType(EnumNetworkType.RBF);
		conf.setInputEnergyDelay(24);
		conf.setInputHumidityDelay(1);
		conf.setInputInsolationDelay(1);
		conf.setInputTemperatureDelay(1);
		conf.setInputDayType(false);
		conf.setInputHoliday(false);
		conf.setInputEMean24Delay(1);
		conf.setInputEMean168Delay(1);
		conf.setOutputPredictionSteps(1);
		conf.setModelType(EnumNetworkType.RBF.toString());
		return conf;
	}

	@Override
	protected KohonenAlgorithm prepareClassifierAlgorithm() {
		KohonenConfiguration algConf = new KohonenConfiguration();
		algConf.setMinClasses(5);
		algConf.setMinGlobalWeightsChange(1E-3);
		algConf.setMaxIteration(500);
		KohonenAlgorithm alg = new KohonenAlgorithm();
		alg.setConfig(algConf);
		return alg;
	}

	@Override
	protected Algorithm prepareFirstPhaseTrainAlgorithm() {
		/*
		 * Konfiguracja funkcji doboru centrów.
		 */
		CenterSelFunctionConfiguration funConf = new CenterSelFunctionConfiguration();
		// funConf.setCenterSelectType(EnumCenterSelectType.K_MEAN_CENTER_SELECT);
		funConf.setCenterSelectType(EnumCenterSelectType.RANDOM_CENTER_SELECT);
		funConf.setDistanceTypeKMean(EnumDistanceType.Euclidean);
		funConf.setInitLearnRatioKMean(0.1);
		funConf.setInitNumberOfCenters(getPredictorStructure()[0]);
		funConf.setMaxStepKMean(1000);
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

	@Override
	protected Algorithm prepareSecondPhaseTrainAlgorithm() {

		CenterSelFunctionConfiguration funConf = new CenterSelFunctionConfiguration();
		funConf.setCenterSelectType(EnumCenterSelectType.RANDOM_CENTER_SELECT);
		funConf.setDistanceTypeKMean(EnumDistanceType.Euclidean);
		funConf.setInitLearnRatioKMean(0.1);
		funConf.setInitNumberOfCenters(getPredictorStructure()[0]);
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
		conf.setStopCondFunctionType(EnumFunctionType.MAPE);
		conf.setMinError(1);
		conf.setCentersAdaptation(false);
		conf.setCentersLearningRatio(0.1);
		conf.setWeightsAdaptation(true);
		conf.setWeightsLearnigRatio(0.001);
		conf.setWidthAdaptation(false);
		conf.setWidthLearnigRatio(0.1);

		return new RbfOfflineAlgorithm(conf);
	}

}
