package pl.phytia.prediction.tasks.parallel.somMlp;

import java.text.ParseException;

import pl.phytia.ann.algorithms.som.KohonenAlgorithm;
import pl.phytia.ann.networks.MLPNetwork;
import pl.phytia.model.conf.algorithms.KohonenConfiguration;
import pl.phytia.model.enums.EnumForecastType;
import pl.phytia.prediction.metaModels.ParallelSomMlpMetaModel;
import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.ann.algorithms.bp.BPAlgorithm;
import pl.phytia.ann.algorithms.bp.QuickPropAlgorithm;
import pl.phytia.model.conf.algorithms.BackPropConfiguration;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.conf.pred.MetaModelConfiguration;
import pl.phytia.model.enums.EnumFunctionType;
import pl.phytia.model.enums.EnumLearningRatioType;
import pl.phytia.model.enums.EnumMetaModelType;
import pl.phytia.model.enums.EnumNetworkType;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.model.sets.SupervisedDataSet;
import pl.phytia.utils.Localization;
import pl.phytia.utils.Tools;

/**
 * @author Jarosław Protasiewicz
 */
public class ParallelSom5x5MlpTask_E24_T0_H0_I0_D_H_M24_M168 extends
		ParallelSomMlpMetaModel {

	private static java.util.Date dateStart = null;

	private static java.util.Date dateStop = null;

	private static java.util.Date dateStartTrain = null;

	private static java.util.Date dateStopTrain = null;
	static {
		try {
			dateStart = Localization.plDateFormatMedium.parse("2004-08-31");
			dateStop = Localization.plDateFormatMedium.parse("2004-10-01");
			dateStartTrain = Localization.plDateFormatMedium
					.parse("2002-01-01");
			dateStopTrain = Localization.plDateFormatMedium.parse("2004-10-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public int[] getClasifierStructure() {
		return new int[] { 5, 5 };
	}

	public static void doIt() throws Exception {
		ParallelSom5x5MlpTask_E24_T0_H0_I0_D_H_M24_M168 task = new ParallelSom5x5MlpTask_E24_T0_H0_I0_D_H_M24_M168();
		MetaModelConfiguration conf = new MetaModelConfiguration();
		conf.setForcastType(EnumForecastType.E24_T0_H0_I0_D_H_M24_M168);
		conf.setInputSignal(EnumSignalType.MODELLING_DATA_NORMALIZED);
		conf.setOutputSignal(Tools.getForecastName(
				EnumMetaModelType.PARALLEL_SOM_5x5_MLP,
				EnumForecastType.E24_T0_H0_I0_D_H_M24_M168));
		conf.setMetamodelType(EnumMetaModelType.PARALLEL_SOM_5x5_MLP);
		conf.setPredictionStart(dateStart);
		conf.setPredictionStop(dateStop);
		conf.setDaysAhead(2);
		task.initialize(conf);
		// /task.preparation();
		// task.prediction();
		task.testClasses(EnumForecastType.E24_T0_H0_I0_D_H_M24_M168.toString(),
				EnumMetaModelType.PARALLEL_SOM_5x5_MLP.toString(),
				dateStartTrain, dateStopTrain, "e:\\tmp\\23-"
						+ Tools.getForecastName(
								EnumMetaModelType.PARALLEL_SOM_5x5_MLP,
								EnumForecastType.E24_T0_H0_I0_D_H_M24_M168)
						+ ".xls");
	}

	@Override
	protected AnnModelConfiguration prepareClassifierConfiguration() {
		AnnModelConfiguration conf = new AnnModelConfiguration();
		conf.setNetworkType(EnumNetworkType.SOM);
		conf.setInputEnergyDelay(24);
		conf.setInputHumidityDelay(1);
		conf.setInputInsolationDelay(1);
		conf.setInputTemperatureDelay(1);
		conf.setInputDayType(true);
		conf.setInputHoliday(true);
		conf.setInputEMean24Delay(1);
		conf.setInputEMean168Delay(1);
		conf.setMaxTrainDataStart(dateStartTrain);
		conf.setMaxTrainDataStop(dateStopTrain);
		conf.setModelType(EnumNetworkType.SOM.toString());
		return conf;
	}

	protected AnnModelConfiguration preparePredictorConfiguration() {
		AnnModelConfiguration conf = new AnnModelConfiguration();
		conf.setNetworkType(EnumNetworkType.MLP);
		conf.setInputEnergyDelay(24);
		conf.setInputHumidityDelay(1);
		conf.setInputInsolationDelay(1);
		conf.setInputTemperatureDelay(1);
		conf.setInputDayType(true);
		conf.setInputHoliday(true);
		conf.setInputEMean24Delay(1);
		conf.setInputEMean168Delay(1);
		conf.setOutputPredictionSteps(1);
		conf.setModelType(EnumNetworkType.MLP.toString());
		return conf;
	}

	protected KohonenAlgorithm prepareClassifierAlgorithm() {
		/*
		 * Utworzenie algorytmu.
		 */
		KohonenConfiguration algConf = new KohonenConfiguration();
		algConf.setMinClasses(5);
		algConf.setMinGlobalWeightsChange(1E-3);
		algConf.setMaxIteration(500);
		KohonenAlgorithm alg = new KohonenAlgorithm();
		alg.setConfig(algConf);
		return alg;
	}

	@Override
	public int[] getPredictorStructure() {
		return new int[] { 10, 1 };
	}

	@Override
	protected Algorithm prepareFirstPhaseTrainAlgorithm() {
		BackPropConfiguration conf = new BackPropConfiguration(
				EnumLearningRatioType.CONSTANT, EnumFunctionType.SSE,
				EnumFunctionType.MAPE);
		conf.setMomentumRatio(0.3);
		conf.setLearningRatio(0.1);
		conf.setWeightRatio(-1.0e-6);
		conf.setMaximumGrowthRatio(2.0);
		conf.setMaxIteration(1000);
		conf.setMaxNotFoundBetterSolution(100);
		conf.setMinError(1.0);
		Algorithm<QuickPropAlgorithm, BackPropConfiguration, SupervisedDataSet, MLPNetwork> alg = new QuickPropAlgorithm(
				conf);
		alg.initialize(conf);
		return alg;
	}

	@Override
	protected Algorithm prepareSecondPhaseTrainAlgorithm() {
		BackPropConfiguration conf = new BackPropConfiguration(
				EnumLearningRatioType.ADAPTATIVE, EnumFunctionType.SSE,
				EnumFunctionType.MAPE);
		conf.setLearningRatio(0.001);
		conf.setMaxIteration(1000);
		conf.setMaxNotFoundBetterSolution(250);
		conf.setMinError(1.0);
		conf.setMaximumGrowthRatio(1.04);
		Algorithm<BPAlgorithm, BackPropConfiguration, SupervisedDataSet, MLPNetwork> alg = new BPAlgorithm(
				conf);
		alg.initialize(conf);
		return alg;
	}

}
