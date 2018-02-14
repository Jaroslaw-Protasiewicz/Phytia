package pl.phytia.dao;

import java.sql.Connection;

/**
 * Abstrakcyjny obiekt dostępu do bazy danych.
 * 
 * @author Jarosław Protasiewicz
 */
public abstract interface BaseDAO {

	// / public BaseDAO() {
	// /super();
	// }

	/**
	 * Tworzy zarządcę połączenia.
	 * 
	 * @param connection
	 *            połączenie do przekazania zarządcy (może być <code>null</code>).
	 * @return zarządca połączenia.
	 */
	public abstract ConnectionManager getManager(Connection connection);

	/**
	 * Tworzy zarządcę połączenia.
	 * 
	 * @param connection
	 *            połączenie do przekazania zarządcy (może być <code>null</code>).
	 * @param autoCommit
	 *            flaga auto-commit do ustawienia na nowym połączeniu.
	 * @return zarządca połączenia.
	 */
	public abstract ConnectionManager getManager(Connection connection,
			boolean autoCommit);
}
