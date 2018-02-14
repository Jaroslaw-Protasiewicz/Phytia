package pl.phytia.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import pl.phytia.model.db.PredictionDbVO;

/**
 * Intefejs obiektów dostepu do bazy danych obsługujących prognozy przechowywane
 * w tabeli: <code>prediction</code>.
 * 
 * @author Jarosław Protasiewicz
 */
public interface PredictionDAO extends BaseDAO {

	/**
	 * Pobiera wiersz tabeli <code>prediction</code>.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param predictionId
	 *            Identyfikator prognozy.
	 * @param signalNameId
	 *            Identyfikator definicji sygnału.
	 * @param predDay
	 *            Dzień prognozy.
	 * @param predHour
	 *            Godzina prognozy.
	 * @param dayAhead
	 *            Wyprzedzenie prognozy.
	 * @return Obiekt reprezentujący wiersz tabeli.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public PredictionDbVO read(Connection dbCon, Long predictionId,
			Long signalNameId, Date predDay, Integer predHour, Integer dayAhead)
			throws SQLException;

	/**
	 * Pobiera wiersze z tabeli <code>prediction</code>.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param signalNameId
	 *            Identyfikator definicji sygnału.
	 * @param predDayForm
	 *            Dzień prognozy.
	 * @param predDayTo
	 *            Dzień prognozy.
	 * @param dayAhead
	 *            Wyprzedzenie.
	 * @return Lista obiektów reprezentujących wiersze tabeli. *
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public List<PredictionDbVO> read(Connection dbCon, Long signalNameId,
			Date predDayForm, Date predDayTo, Integer dayAhead)
			throws SQLException;

	/**
	 * Tworzy lub modyfikuje wiersz w tabeli <code>prediction</code>.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param pred
	 *            Dane do zapisania w bazie danych.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public void createOrUpdate(Connection dbCon, PredictionDbVO pred)
			throws SQLException;
}
