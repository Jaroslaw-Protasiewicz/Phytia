package pl.phytia.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pl.phytia.model.db.MetaModelDbVO;

/**
 * Intefejs obiektów dostepu do bazy danych obsługujących metamodele
 * przechowywane w tabelach: <code>METAMODEL</code> i <code></code>.
 * 
 * @author Jarosław Protasiewicz
 */
public abstract interface MetaModelDAO extends BaseDAO {

	/**
	 * Utworzenie metamodelu.
	 * 
	 * @param metaModel
	 *            Metamodel do zapisania w bazie danych.
	 * @return Identyfikator utworzenego rekordu.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract Long create(Connection dbCon, MetaModelDbVO metaModel)
			throws SQLException;

	/**
	 * Odczyt metamodelu z bazy danych.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param query
	 *            Warunki selekcji.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract List<MetaModelDbVO> read(Connection dbCon,
			MetaModelDbVO query) throws SQLException;

	/**
	 * Modyfikacja metamodelu.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param metaModel
	 *            Metamodel do modyfikacji w bazie danych.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract void update(Connection dbCon, MetaModelDbVO metaModel)
			throws SQLException;

	/**
	 * Usunięcie metamodelu z bazy danych.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param query
	 *            Warunki selekcji.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract void delete(Connection dbCon, MetaModelDbVO query)
			throws SQLException;
}
