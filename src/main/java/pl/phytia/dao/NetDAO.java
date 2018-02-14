package pl.phytia.dao;

import java.sql.Connection;
import java.sql.SQLException;

import pl.phytia.model.net.NetVO;

/**
 * Intefejs obiektu DAO odpowiedzialnego za obsługę tabeli <code>net</code>
 * 
 * @author Jarosław Protasiewicz
 */
public interface NetDAO extends BaseDAO {

	/**
	 * Pobiera sieć z tabeli <code>net</code>.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param query
	 *            Obiekt zawierający kryteria zapytania/
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract NetVO getNet(Connection dbCon, NetVO query)
			throws SQLException;

	/**
	 * Zapisuje lub aktualizuje sieć w tabeli <code>net</code>.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param net
	 *            Obiekt do zapisu w bazie.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public void updateOrSave(Connection dbCon, NetVO net) throws SQLException;

}
