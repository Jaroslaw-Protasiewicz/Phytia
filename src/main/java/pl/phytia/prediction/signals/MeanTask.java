package pl.phytia.prediction.signals;

import java.util.List;

import org.apache.log4j.Logger;

import pl.phytia.dao.ConnectionManager;
import pl.phytia.dao.DAOFactory;
import pl.phytia.dao.SignalDAO;
import pl.phytia.model.db.SignalNameVO;
import pl.phytia.model.enums.EnumDataBase;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.model.signals.DataVO;
import pl.phytia.utils.Localization;

/**
 * Wyznacza średnią ruchomą (-24) i (-168)
 * 
 * @author Jarosław Protasiewicz
 */
public class MeanTask {

	private static Logger logger = Logger.getLogger(MeanTask.class);

	private DAOFactory factory = DAOFactory
			.getDAOFactory(EnumDataBase.PostgreSQL);

	public static void main(String[] args) {

		logger.debug("MeanTask - start");

		MeanTask task = new MeanTask();
		/*
		 * Odczyt danych z bazy.
		 */
		List<DataVO> srcData = task.getDataFormDataBase();

		int indx = 0;
		double sum24 = 0;
		double sum168 = 0;
		for (DataVO dat : srcData) {
			sum24 = 0;
			for (int i = 1; i <= 24; ++i) {
				if (indx > i) {
					sum24 += ((DataVO) srcData.get(indx - i)).getEnergy();
				}
			}
			dat.setEMean24(sum24 / 24);
			sum168 = 0;
			for (int i = 1; i <= 168; ++i) {
				if (indx > i) {
					sum168 += ((DataVO) srcData.get(indx - i)).getEnergy();
				}
			}
			dat.setEMean168(sum168 / 168);
			indx++;
			logger.debug("Obliczenia = " + dat.getTime().toString());
		}

		/*
		 * Zapis danych do bazy.
		 */
		task.saveDataToDataBase(srcData);
		logger.debug("MeanTask - STOP");
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
					EnumSignalType.MODELLING_DATA_NORMALIZED.toString(),
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
			List<SignalNameVO> lst = signal.read(manager.getConnection(),
					signalName);
			signalName = lst.get(0);
			signal.updateMean(manager.getConnection(), srcData, signalName
					.getSignalNameId());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close(null, null);
		}
	}
}
