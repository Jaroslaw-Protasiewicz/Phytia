package pl.phytia.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pl.phytia.model.db.ModelDbVO;

/**
 * Intefejs obiektów dostepu do bazy danych obsługujących modele przechowywane w
 * tabeli: <code>model</code>
 * 
 * @author Jarosław Protasiewicz
 */
public interface ModelDAO extends BaseDAO {

	/**
	 * Utworzenie modelu.
	 * 
	 * @param model
	 *            Model do zapisania w bazie danych.
	 * @return Identyfikator utworzonego rekordu.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract Long create(Connection dbCon, ModelDbVO model)
			throws SQLException;

	/**
	 * Odczytamodelu z bazy danych.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param query
	 *            Warunki selekcji.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract List<ModelDbVO> read(Connection dbCon, ModelDbVO query)
			throws SQLException;

	/**
	 * Modyfikacja modelu.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param model
	 *            Model do modyfikacji w bazie danych.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract void update(Connection dbCon, ModelDbVO model)
			throws SQLException;

	/**
	 * Usunięcie modelu z bazy danych.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param query
	 *            Warunki selekcji.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract void delete(Connection dbCon, ModelDbVO query)
			throws SQLException;

}
