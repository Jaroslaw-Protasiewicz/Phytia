package pl.phytia.prediction.signals;

import java.util.List;

import pl.phytia.dao.DAOFactory;
import pl.phytia.api.SignalAPI;
import pl.phytia.dao.ConnectionManager;
import pl.phytia.dao.SignalDAO;
import pl.phytia.model.db.SignalNameVO;
import pl.phytia.model.enums.EnumDataBase;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.model.signals.DataVO;
import pl.phytia.utils.MyLogger;

/**
 * Import historycznych danych pomiarowych do bazy danych. Opcja wykonywana jest
 * jednorazowo!
 * 
 * @author Jarosław Protasiewicz
 * 
 */
public class ImportHistoricalData {

	public static void main(String[] args) throws Exception {

		final String srcFileWithPath = "e:\\phd\\cd\\dane\\dane-2002-2004-org-klas-import.xls";

		/*
		 * Przygotowanie obiektów bazy danych.
		 */
		DAOFactory factory = DAOFactory.getDAOFactory(EnumDataBase.PostgreSQL);
		SignalDAO signalDao = factory.getSignalDAO();

		ConnectionManager manager = signalDao.getManager(null, false);

		try {
			/*
			 * Odczyt danych z pliku.
			 */
			MyLogger.logger.info("Start importu");
			List<DataVO> srcData = SignalAPI.readDataFromFile(srcFileWithPath);
			MyLogger.logger.info("Przeczytano dane z pliku");
			/*
			 * Odczytanie / utworzenie rekordu nazwy sygnału.
			 */
			SignalNameVO signalName = new SignalNameVO(null,
					EnumSignalType.MODELLING_DATA.toString());
			List<SignalNameVO> signalNames = signalDao.read(manager
					.getConnection(), signalName);
			if (signalNames != null && signalNames.size() == 1) {
				signalName
						.setSignalNameId(signalNames.get(0).getSignalNameId());
				MyLogger.logger.info("Istnieje definicja sygnału "
						+ signalName.getName() + " o id = "
						+ signalName.getSignalNameId());
			} else {
				signalName.setSignalNameId(signalDao.create(manager
						.getConnection(), signalName));
				MyLogger.logger.info("Utowrzono definicję sygnału "
						+ signalName.getName() + " o id = "
						+ signalName.getSignalNameId());
			}
			/*
			 * Zapis sygnałów do bazy danych.
			 */
			signalDao.saveToDataBase(manager.getConnection(), srcData,
					signalName.getSignalNameId());
			manager.commit();
			MyLogger.logger.info("Koniec importu - sukces");
		} catch (Exception e) {
			MyLogger.logger.info("Koniec importu - error");
			manager.rollback();
			e.printStackTrace();
		} finally {
			manager.close(null, null);
		}

	}

}
