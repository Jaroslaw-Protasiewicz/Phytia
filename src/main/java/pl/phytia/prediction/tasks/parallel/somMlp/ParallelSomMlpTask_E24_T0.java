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
 * Definicja zdania wykonania prognozy układem klasyfikator - modele MLP z
 * uwzględnieniem parametrów: energia, temperatura.
 * 
 * @author Jarosław Protasiewicz
 */
public class ParallelSomMlpTask_E24_T0 extends ParallelSomMlpMetaModel {

	private static java.util.Date dateStart = null;

	private static java.util.Date dateStop = null;
	static {
		try {
			dateStart = Localization.plDateFormatMedium.parse("2004-01-01");
			dateStop = Localization.plDateFormatMedium.parse("2004-12-31");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void doIt() throws Exception {
		ParallelSomMlpTask_E24_T0 task = new ParallelSomMlpTask_E24_T0();
		MetaModelConfiguration conf = new MetaModelConfiguration();
		conf.setForcastType(EnumForecastType.E24_T0);
		conf.setInputSignal(EnumSignalType.MODELLING_DATA_NORMALIZED);
		conf.setOutputSignal(Tools.getForecastName(
				EnumMetaModelType.PARALLEL_SOM_MLP, EnumForecastType.E24_T0));
		conf.setMetamodelType(EnumMetaModelType.PARALLEL_SOM_MLP);
		conf.setPredictionStart(dateStart);
		conf.setPredictionStop(dateStop);
		conf.setDaysAhead(7);
		task.initialize(conf);
		// task.preparation();
		/*
		 * task.preparation(); String path = "c:\\tmp\\Koch-7-7.txt";
		 * BufferedWriter out = new BufferedWriter(new FileWriter(path));
		 * out.write("\n-----------------------------------------------------------\n");
		 * Map<String, List<Date>> res =
		 * task.getClassifier().getClassResults(); Iterator it =
		 * res.keySet().iterator(); while (it.hasNext()) { String key = (String)
		 * it.next(); List<Date> val = res.get(key); key += "; \n"; for (Date d :
		 * val) { d.toString(); key += d.toString() + "; \n"; }
		 * System.out.println(key); out.write(key);
		 *  } out.close();
		 */
		task.prediction();
	}

	@Override
	protected AnnModelConfiguration prepareClassifierConfiguration() {
		AnnModelConfiguration conf = new AnnModelConfiguration();
		conf.setNetworkType(EnumNetworkType.SOM);
		conf.setInputEnergyDelay(24);
		conf.setInputHumidityDelay(0);
		conf.setInputInsolationDelay(0);
		conf.setInputTemperatureDelay(1);
		// conf.setMaxTrainDataStart(dateStart);
		// conf.setMaxTrainDataStop(dateStop);
		conf.setModelType(EnumNetworkType.SOM.toString());
		return conf;
	}

	protected AnnModelConfiguration preparePredictorConfiguration() {
		AnnModelConfiguration conf = new AnnModelConfiguration();
		conf.setNetworkType(EnumNetworkType.MLP);
		conf.setInputEnergyDelay(24);
		conf.setInputHumidityDelay(0);
		conf.setInputInsolationDelay(0);
		conf.setInputTemperatureDelay(1);
		// conf.setMaxTrainDataStart(dateStart);
		// conf.setMaxTrainDataStop(dateStop);
		// conf.setNormRatio(0.1);
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
		algConf.setMaxIteration(300);
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

	@Override
	public int[] getClasifierStructure() {
		return new int[] { 7, 7 };
	}

}
