package pl.phytia.prediction.signals;

import java.util.List;

import pl.phytia.dao.ConnectionManager;
import pl.phytia.dao.DAOFactory;
import pl.phytia.dao.SignalDAO;
import pl.phytia.model.enums.EnumDataBase;
import pl.phytia.model.signals.DataVO;
import pl.phytia.utils.Normalizer;
import pl.phytia.model.db.SignalNameVO;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.utils.Localization;
import pl.phytia.utils.MyLogger;

/**
 * Wykonanie normalizacji danych.
 * 
 * @author Jarosław Protasiewicz
 */
public class NormTask {

	private static final double minDest = 0.1;

	private static final double maxDest = 0.9;

	private DAOFactory factory = DAOFactory
			.getDAOFactory(EnumDataBase.PostgreSQL);

	public static void main(String[] args) {

		MyLogger.logger.info("Normalizacja - START");

		NormTask task = new NormTask();
		/*
		 * Odczyt danych z bazy.
		 */
		List<DataVO> srcData = task.getDataFormDataBase();
		/*
		 * Wyzmaczenie min, max zbioru
		 */
		double[] valMinMax = task.findMixMax(srcData);
		MyLogger.logger.info("Wartość min = " + valMinMax[0]
				+ "   Wartość max = " + valMinMax[1]);
		/*
		 * Inicjalizacja normalizatora i normalizacja danych.
		 */
		Normalizer normalizer = new Normalizer();
		normalizer.initialize(valMinMax[0], valMinMax[1], minDest, maxDest);
		task.normalization(srcData, normalizer);
		/*
		 * Zapis danych do bazy.
		 */
		task.saveDataToDataBase(srcData);
		MyLogger.logger.info("Normalizacja - STOP");
	}

	/**
	 * Odczyt danych do normalizacji z bazy danych.
	 * 
	 * @return Dane do normalizacji.
	 */
	private List<DataVO> getDataFormDataBase() {
		ConnectionManager manager = null;
		List<DataVO> srcData = null;
		try {
			SignalDAO signal = factory.getSignalDAO();
			manager = signal.getManager(null);
			srcData = signal.readSignal(manager.getConnection(),
					EnumSignalType.MODELLING_DATA.toString(),
					Localization.plDateFormatMedium.parse("2002-01-01"),
					Localization.plDateFormatMedium.parse("2004-12-31"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close(null, null);
		}
		return srcData;
	}

	/**
	 * Zapis znormalizowanych danych w bazie danych.
	 * 
	 * @param srcData
	 *            Dane znormalizowane.
	 */
	private void saveDataToDataBase(List<DataVO> srcData) {
		ConnectionManager manager = null;
		try {
			SignalDAO signal = factory.getSignalDAO();
			manager = signal.getManager(null);
			SignalNameVO signalName = new SignalNameVO(null,
					EnumSignalType.MODELLING_DATA_NORMALIZED.toString());
			signalName.setSignalNameId(signal.create(manager.getConnection(),
					signalName));
			signal.saveToDataBase(manager.getConnection(), srcData, signalName
					.getSignalNameId());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close(null, null);
		}
	}

	/**
	 * Znajduje wartośc min i max w zbiorze danych.
	 * 
	 * @param srcData
	 *            Zbiór danych
	 * @return Tablica 2 elementowa zawierająca wartości: min i max w kolejnych
	 *         indeksach.
	 */
	private double[] findMixMax(List<DataVO> srcData) {
		double[] minMax = new double[] { Double.MAX_VALUE, Double.MIN_VALUE };
		for (DataVO d : srcData) {
			if (minMax[0] > d.getEnergy()) {
				minMax[0] = d.getEnergy();
			}
			if (minMax[0] > d.getHumidity()) {
				minMax[0] = d.getHumidity();
			}
			if (minMax[0] > d.getTemperature()) {
				minMax[0] = d.getTemperature();
			}
			if (minMax[0] > d.getInsolation()) {
				minMax[0] = d.getInsolation();
			}
			if (minMax[1] < d.getEnergy()) {
				minMax[1] = d.getEnergy();
			}
			if (minMax[1] < d.getHumidity()) {
				minMax[1] = d.getHumidity();
			}
			if (minMax[1] < d.getTemperature()) {
				minMax[1] = d.getTemperature();
			}
			if (minMax[1] < d.getInsolation()) {
				minMax[1] = d.getInsolation();
			}
		}
		return minMax;
	}

	/**
	 * Nomrmalizacja zbioru danych.
	 * 
	 * @param srcData
	 *            Zbiór danych do normalizacji.
	 * @param norm
	 *            Normalizator.
	 */
	private void normalization(List<DataVO> srcData, Normalizer norm) {
		for (DataVO d : srcData) {
			d.setTemperature(norm.normalize(d.getTemperature()));
			d.setEnergy(norm.normalize(d.getEnergy()));
			d.setHumidity(norm.normalize(d.getHumidity()));
			d.setInsolation(norm.normalize(d.getInsolation()));
		}
	}

}
