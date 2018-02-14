package pl.phytia.prediction.tasks.serial;

import java.text.ParseException;

import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.ann.algorithms.bp.BPAlgorithm;
import pl.phytia.ann.networks.MLPNetwork;
import pl.phytia.model.conf.algorithms.BackPropConfiguration;
import pl.phytia.prediction.metaModels.MLPSerialMetaModel;
import pl.phytia.prediction.models.MLPModel;
import pl.phytia.utils.Localization;
import pl.phytia.utils.Tools;
import pl.phytia.ann.algorithms.bp.QuickPropAlgorithm;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.conf.pred.MetaModelConfiguration;
import pl.phytia.model.enums.EnumForecastType;
import pl.phytia.model.enums.EnumFunctionType;
import pl.phytia.model.enums.EnumLearningRatioType;
import pl.phytia.model.enums.EnumMetaModelType;
import pl.phytia.model.enums.EnumNetworkType;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.model.sets.SupervisedDataSet;
import pl.phytia.prediction.metaModels.MetaModel;

/**
 * Definicja zdania wykonania prognozy szeregiem modeli MLP z uwzględnieniem
 * parametrów: energia, nasłoneczenienie.
 * 
 * @author Jarosław Protasiewicz
 */
public class MLPSerialTask_E24_I0 extends MLPSerialMetaModel {

	/*
	 * Definicja okresu prognozy
	 */
	private static java.util.Date dateStart = null;

	private static java.util.Date dateStop = null;
	static {
		try {
			dateStart = Localization.plDateFormatMedium.parse("2004-01-06");
			dateStop = Localization.plDateFormatMedium.parse("2004-12-25");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void doIt() {

		MetaModel<MLPModel> task = new MLPSerialTask_E24_I0();
		MetaModelConfiguration confMetaModel = new MetaModelConfiguration(
				EnumMetaModelType.SERIAL_MLP, EnumForecastType.E24_I0, 7,
				dateStart, dateStop, EnumSignalType.MODELLING_DATA_NORMALIZED,
				Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
						EnumForecastType.E24_I0));
		task.initialize(confMetaModel);
		task.preparation();
		task.prediction();

	}

	@Override
	protected AnnModelConfiguration preparePredictorConfiguration() {
		AnnModelConfiguration conf = new AnnModelConfiguration(
				EnumNetworkType.MLP, 0, 0, 1, 24, -15, 15, -2, 24, 0.1, null,
				null, false, false);
		return conf;
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
		conf.setMaxNotFoundBetterSolution(250);
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
