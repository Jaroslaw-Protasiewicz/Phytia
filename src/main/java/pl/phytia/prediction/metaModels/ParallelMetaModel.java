package pl.phytia.prediction.metaModels;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import pl.phytia.ann.algorithms.som.KohonenAlgorithm;
import pl.phytia.ann.networks.SOMNetwork;
import pl.phytia.api.MetaModelAPI;
import pl.phytia.api.SignalAPI;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.conf.pred.MetaModelConfiguration;
import pl.phytia.model.db.ModelDbVO;
import pl.phytia.model.db.PredictionDbVO;
import pl.phytia.model.enums.EnumNetworkType;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.prediction.models.Model;
import pl.phytia.prediction.models.SOMModel;
import pl.phytia.utils.Localization;

/**
 * Abstrakcyjny metamodel równoległy: klasyfikator - predyktory
 * 
 * @author Jarosław Protasiewicz
 * 
 * @param <M>
 *            Typ predyktora.
 */
public abstract class ParallelMetaModel<M extends Model> extends MetaModel<M> {

	/**
	 * Klasyfikator.
	 */
	private SOMModel classifier;

	@Override
	public void preparation() {
		/*
		 * Przygotowanie klasyfikatora.
		 */
		AnnModelConfiguration classifierConf = prepareClassifierConfiguration();
		prepareClassifier(classifierConf);
		getClassifier().setModelClass(getConfig().getForcastType().toString());
		/*
		 * Inicjalizacja : pobranie danych, normalizacja danych.
		 */
		getClassifier().initialize(classifierConf);
		/*
		 * Klasyfikacja danych.
		 */
		getClassifier().modeling();

		/*
		 * Zapis do bazy danych klasyfikatora oraz modeli
		 */
		MetaModelAPI.saveParallelMetaModel(this, true, 0, false);
	}

	/**
	 * Podręczna pamięć modeli - stosowana w celu minimalizacji zapisów do bazy
	 * danych.
	 */
	protected HashMap<String, M> predModelMap;

	/**
	 * Wyniki prognoz.
	 */
	protected List<PredictionDbVO> results;

	public ParallelMetaModel() {
		super();
		predModelMap = new HashMap<String, M>();
	}

	@Override
	public void initialize(MetaModelConfiguration conf) {
		super.initialize(conf);
	}

	/**
	 * Zwraca strukturę sieci SOM.
	 * 
	 * @return Wymiary matrycy.
	 */
	public abstract int[] getClasifierStructure();

	/**
	 * Przygotowuje konfigurację klasyfikatroa.
	 * 
	 * @return Konfiguracja modelu.
	 */
	protected abstract AnnModelConfiguration prepareClassifierConfiguration();

	/**
	 * Przygotowuje model klasyfikatora.
	 * 
	 * @param classifierConf
	 *            Konfiguracja.
	 */
	protected abstract void prepareClassifier(
			AnnModelConfiguration classifierConf);

	/**
	 * Przygotwanie algorytmu samoorganizacji klasyfikaotra.
	 * 
	 * @return Algorytm Kohonena
	 */
	protected abstract KohonenAlgorithm prepareClassifierAlgorithm();

	/**
	 * Przygotowuje listę predyktorów.
	 * 
	 * @param predictorConf
	 *            Konfiguracja.
	 */
	protected abstract void preparePredictors(
			AnnModelConfiguration predictorConf);

	/**
	 * Przygotwanie sieci SOM na podstawie stanu serializowanego obiektu.
	 * 
	 * @param state
	 *            Serializowany obiekt sieci SOM
	 * @return Obiekt zawierający sieć SOM.
	 */
	protected SOMNetwork prepareSOMNetwork(String state) {
		SOMNetwork net = new SOMNetwork();
		net.loadState(state);
		return net;
	}

	/**
	 * Przygotowuje model SOM na podstawie stanu z bazy danych.
	 * 
	 * @param modelDb
	 *            Obiekt pobrany z bazy danych
	 * @return Model.
	 */
	protected SOMModel prepareClassifier(ModelDbVO modelDb) {
		SOMModel model = new SOMModel(prepareSOMNetwork(modelDb.getNetwork()),
				prepareClassifierAlgorithm(), null, null);
		return model;
	}

	public SOMModel getClassifier() {
		return classifier;
	}

	public void setClassifier(SOMModel classifier) {
		this.classifier = classifier;
	}

	public HashMap<String, M> getPredModelMap() {
		return predModelMap;
	}

	public void setPredModelMap(HashMap<String, M> predMap) {
		this.predModelMap = predMap;
	}

	public List<PredictionDbVO> getResults() {
		return results;
	}

	public void setResults(List<PredictionDbVO> results) {
		this.results = results;
	}

	/**
	 * Wyznacza średnia na zadany okres
	 * 
	 * @param predTime
	 *            Chwila startu prognozy
	 * @param i
	 *            Kolejna godzina od statru prognozy
	 * @param delay
	 *            Zakres godzin do średniej
	 * @return Średnia energii
	 */
	protected double computeAvg(Date predTime, int i, int delay) {
		double avg = 0;
		Calendar avgCal = Calendar.getInstance(Localization.plLocale);
		avgCal.setTime(predTime);
		avgCal.add(Calendar.HOUR_OF_DAY, -1);
		Date toTime = new Date(avgCal.getTimeInMillis());
		avgCal.setTime(predTime);
		avgCal.add(Calendar.HOUR_OF_DAY, -delay + i);
		Date formTime = new Date(avgCal.getTimeInMillis());
		avg = SignalAPI.computeSumOfEnergy(
				EnumSignalType.MODELLING_DATA_NORMALIZED.toString(), formTime,
				toTime);
		int steps = 0;
		if (i >= delay) {
			steps = delay;
		} else {
			steps = i;
		}
		for (int r = results.size(); r > 0; r--) {
			if (steps == 0)
				break;
			avg += results.get(r - 1).getPredValue();
			--steps;
		}
		return (avg / delay);
	}

	/**
	 * Test kalsufikacji
	 * 
	 * @param forecastType
	 *            Typ prognozy
	 * @param metamodelType
	 *            Typ metamodelu
	 * @param dateStartTrain
	 *            Start okresu klasyfikowanego
	 * @param dateStopTrain
	 *            Stop okresu klasyfikowanego
	 * @param fileNameWithPath
	 *            Plik + ścieżka
	 * @throws Exception
	 */
	protected void testClasses(String forecastType, String metamodelType,
			Date dateStartTrain, Date dateStopTrain, String fileNameWithPath)
			throws Exception {
		ModelDbVO modelDb = MetaModelAPI.readModel(forecastType, metamodelType,
				EnumNetworkType.SOM.getType(), forecastType);
		setClassifier(prepareClassifier(modelDb));
		AnnModelConfiguration conf = prepareClassifierConfiguration();
		getClassifier().initialize(conf, dateStartTrain, dateStopTrain);
		getClassifier().preparation();
		Map<String, List<Date>> results = getClassifier().getClassResults();

		TreeMap<Date, List<String>> mapXLS = new TreeMap<Date, List<String>>();
		Iterator<String> it = results.keySet().iterator();
		String keyClass = null;
		List<Date> dates = null;
		while (it.hasNext()) {
			keyClass = it.next();
			dates = (List<Date>) results.get(keyClass);
			for (Date d : dates) {
				if (mapXLS.containsKey(d)) {
					mapXLS.get(d).add(keyClass);
				} else {
					List<String> l = new ArrayList<String>();
					l.add(keyClass);
					mapXLS.put(d, l);
				}
			}
		}

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("SomClasses");
		short rowIndx = 0;
		short colIndx = 0;
		HSSFRow row = sheet.createRow(++rowIndx);
		row.createCell(colIndx++).setCellValue("Data");
		for (int i = 0; i < 24; i++) {
			row.createCell(colIndx).setCellValue("h" + colIndx);
			colIndx++;
		}
		Calendar cal = Calendar.getInstance(Localization.plLocale);
		Calendar calStop = Calendar.getInstance(Localization.plLocale);
		calStop.setTime(dateStopTrain);
		calStop.add(Calendar.DAY_OF_YEAR, +1);
		cal.setTime(dateStartTrain);
		colIndx = 0;
		List<String> classes = null;
		do {
			if (colIndx == 0) {
				row = sheet.createRow(++rowIndx);
				row.createCell(colIndx++).setCellValue(
						Localization.plDateFormatMedium.format(cal.getTime()));
			}
			classes = mapXLS.get(cal.getTime());
			StringBuilder sb = new StringBuilder("");
			boolean first = true;
			if (classes != null) {
				for (String s : classes) {
					if (first) {
						sb.append(s.substring(s.lastIndexOf("_") + 1, s
								.length()));
					} else {
						sb.append(" "
								+ s.substring(s.lastIndexOf("_"), s.length()));
					}
				}
			} else {
				sb.append(" ");
			}
			row.createCell(colIndx++).setCellValue(sb.toString());
			if (colIndx == 25) {
				colIndx = 0;
			}
			cal.add(Calendar.HOUR_OF_DAY, +1);

		} while (calStop.getTime().after(cal.getTime()));
		FileOutputStream fileOut = new FileOutputStream(fileNameWithPath);
		wb.write(fileOut);
		fileOut.close();
	}

}
