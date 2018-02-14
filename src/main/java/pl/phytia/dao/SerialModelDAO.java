package pl.phytia.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pl.phytia.model.db.SerialModelDbVO;

/**
 * Intefejs obiektów dostepu do bazy danych obsługujących szereg modeli
 * przechowywany w tabelt: <code>serialmodel</code>.
 * 
 * @author Jarosław Protasiewicz
 * @deprecated - do skasowania
 */
public interface SerialModelDAO extends BaseDAO {

	/**
	 * Utworzenie szeregu.
	 * 
	 * @param serialModel
	 *            SerialModel do zapisania w bazie danych.
	 * @return Identyfikator utworzenego rekordu.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract Long create(Connection dbCon, SerialModelDbVO serialModel)
			throws SQLException;

	/**
	 * Odczyt szeregu modeli z bazy danych.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param query
	 *            Warunki selekcji.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract List<SerialModelDbVO> read(Connection dbCon,
			SerialModelDbVO query) throws SQLException;

	/**
	 * Usunięcie szeregu metamodelu z bazy danych.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param query
	 *            Warunki selekcji.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract void delete(Connection dbCon, SerialModelDbVO query)
			throws SQLException;

}
