package pl.phytia.prediction.statistics;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.ann.algorithms.som.KohonenAlgorithm;
import pl.phytia.api.MetaModelAPI;
import pl.phytia.model.enums.EnumForecastType;
import pl.phytia.model.enums.EnumMetaModelType;
import pl.phytia.prediction.metaModels.ParallelSomMlpMetaModel;
import pl.phytia.utils.Localization;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.db.ModelDbVO;
import pl.phytia.model.enums.EnumNetworkType;

/**
 * Wyniki klasyfikacji za pomocą sieci SOM.
 * 
 * @author Jarosław Protasiewicz
 * @deprecated - zaimplementowano metodę test w ParallelSomMlpMetaModel
 */
public class SomClassResults extends ParallelSomMlpMetaModel {

	private static Logger logger = Logger.getLogger(SomClassResults.class);

	private static java.util.Date dateStartTrain = null;

	private static java.util.Date dateStopTrain = null;
	static {
		try {
			dateStartTrain = Localization.plDateFormatMedium
					.parse("2002-01-01");
			dateStopTrain = Localization.plDateFormatMedium.parse("2004-09-30");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private static final String[][] tasks = {
			{ EnumMetaModelType.PARALLEL_SOM_5x5_MLP.toString(),
					EnumForecastType.E24_T0_H0_I0.toString() },
			{ EnumMetaModelType.PARALLEL_SOM_5x5_MLP.toString(),
					EnumForecastType.E24_T0_H0_I0_D_H.toString() },
			{ EnumMetaModelType.PARALLEL_SOM_5x5_MLP.toString(),
					EnumForecastType.E24_D_H_M24_M168.toString() },
			{ EnumMetaModelType.PARALLEL_SOM_5x5_MLP.toString(),
					EnumForecastType.E24_T0_D_H_M24_M168.toString() },
			{ EnumMetaModelType.PARALLEL_SOM_5x5_MLP.toString(),
					EnumForecastType.E24_T0_I0_D_H_M24_M168.toString() },
			{ EnumMetaModelType.PARALLEL_SOM_5x5_MLP.toString(),
					EnumForecastType.E24_T0_H0_I0_D_H_M24_M168.toString() },
			{ EnumMetaModelType.PARALLEL_SOM_5x5_MLP.toString(),
					EnumForecastType.E24_T0_H0_I0_M24_M168.toString() }, };

	public static void main(String[] args) {

		SomClassResults stat = new SomClassResults();

		for (String s[] : tasks) {
			String metamodelType = s[0];
			String forecastType = s[1];
			ModelDbVO modelDb = MetaModelAPI.readModel(forecastType,
					metamodelType, EnumNetworkType.SOM.getType(), forecastType);
			stat.setClassifier(stat.prepareClassifier(modelDb));
			AnnModelConfiguration conf = stat.getClassifier().getConfig();
			stat.getClassifier()
					.initialize(conf, dateStartTrain, dateStopTrain);
			stat.getClassifier().preparation();
			Map<String, List<Date>> results = stat.getClassifier()
					.getClassResults();

			// TreeMap<Date, List<String>> map = new TreeMap<Date,
			// List<String>>();
			TreeMap<Date, String> map = new TreeMap<Date, String>();
			Calendar cal = Calendar.getInstance(Localization.plLocale);
			cal.setTime(dateStartTrain);

			Iterator<String> it = results.keySet().iterator();
			String key = null;
			List<Date> dates = null;
			while (it.hasNext()) {
				key = it.next();
				dates = (List<Date>) results.get(key);
				for (Date d : dates) {
					if (map.containsKey(d)) {
						logger.error("JEDNAK SIĘ POWARZA" + d.toString());
					}
					map.put(d, key);
				}
			}
		}
	}

	@Override
	public int[] getClasifierStructure() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected KohonenAlgorithm prepareClassifierAlgorithm() {
		return null;
	}

	@Override
	protected AnnModelConfiguration prepareClassifierConfiguration() {
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
	protected AnnModelConfiguration preparePredictorConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Algorithm prepareSecondPhaseTrainAlgorithm() {
		// TODO Auto-generated method stub
		return null;
	}
}
