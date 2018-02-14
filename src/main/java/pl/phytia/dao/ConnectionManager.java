package pl.phytia.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * Abstracyjny zarządca połączeniami bazy danych.
 * 
 * @author Jarosław Protasiewicz
 */
public abstract class ConnectionManager {

	protected transient Logger logger = Logger
			.getLogger(ConnectionManager.class);

	/**
	 * Określa czy płączenie do bazy danych zostało utowrzone przez danego
	 * zarządcę połączeń.
	 */
	private boolean connectionCerated = false;

	/**
	 * Połączenie z bazą danych.
	 */
	private Connection connection;

	/**
	 * Parametr autoComit połaczenia z bazą danych.
	 */
	private Boolean autoCommit;

	/**
	 * Konstruktor zarządcy połączeń - nie tworzy połączenia.
	 */
	public ConnectionManager() {
		super();
	}

	/**
	 * Konstruktor zarządcy połączeń.
	 * 
	 * @param connection
	 *            Połączenie z bazą, jeżeli jest <code>null</code>, manager
	 *            utworzy nowe.
	 */
	public ConnectionManager(Connection connection) {
		if (connection != null) {
			setConnection(connection);
		} else {
			setConnection(createConnection());
			this.connectionCerated = true;
		}
	}

	/**
	 * Konstruktor zarządcy połączeń.
	 * 
	 * @param connection
	 *            Połączenie z bazą, jeżeli jest <code>null</code>, manager
	 *            utworzy nowe.
	 * @param autoCommit
	 *            Ustawiane na płączeniu.
	 */
	public ConnectionManager(Connection connection, boolean autoCommit) {
		this(connection);
		try {
			this.autoCommit = getConnection().getAutoCommit();
			getConnection().setAutoCommit(autoCommit);
		} catch (SQLException e) {
			logger.debug("Błąd ustawienia prametru autoCommit = " + autoCommit
					+ ".", e);
		}
	}

	/**
	 * Utworzenie połaczenia do bazy danych
	 * 
	 * @return Połączenie
	 */
	public Connection createConnection() {
		Connection db = null;
		try {
			Class.forName(getDbParamDriver());
			db = DriverManager.getConnection(getDbPramUrl(),
					getDbparamUserName(), getDbParamPassword());
		} catch (Exception e) {
			logger.error("Nieudane utworzenie połączenia z bazą danych.", e);
			return null;
		}
		return db;
	}

	/**
	 * Zwraca parametr bazy daych.
	 * 
	 * @return adres
	 */
	public abstract String getDbPramUrl();

	/**
	 * Zwraca parametr bazy daych.
	 * 
	 * @return login użytkownika
	 */
	public abstract String getDbparamUserName();

	/**
	 * Zwraca parametr bazy daych.
	 * 
	 * @return hasło użytkownika
	 */
	public abstract String getDbParamPassword();

	/**
	 * Zwraca parametr bazy daych.
	 * 
	 * @return sterownik klienta bazy danych
	 */
	public abstract String getDbParamDriver();

	/**
	 * Zatwierdzenie transakcji.
	 * 
	 * @throws SQLException
	 *             Błąd bazy danych.
	 */
	public void commit() throws SQLException {
		getConnection().commit();
	}

	/**
	 * Zamyka zarządzane połączenie, tylko jeśli zostało utworzone przez
	 * managera.
	 * 
	 * @param rs
	 *            ResultSet
	 * @param stmt
	 *            Statement
	 */
	public void close(ResultSet rs, Statement stmt) {
		try {
			if (rs != null)
				rs.close();
			rs = null;
		} catch (SQLException e) {
			logger.error("Nieudane zamknięcie ResultSet.", e);
		}
		try {
			if (stmt != null)
				stmt.close();
			stmt = null;
		} catch (SQLException e) {
			logger.error("Nieudane zamknięcie Statement.", e);
		}
		try {
			if (this.autoCommit != null) {
				getConnection().setAutoCommit(this.autoCommit.booleanValue());
			}
			if (connectionCerated) {
				if ((connection != null) && (connection.isClosed() == false)) {
					connection.close();
					connection = null;
				}
			}
		} catch (SQLException e) {
			logger.error("Nieudane zamknięcie połączenia.", e);
		}
	}

	/**
	 * Wycofuje transakcję.
	 */
	public void rollback() {
		try {
			connection.rollback();
		} catch (SQLException e) {
			logger.error("Nie udało się wycofać transakcji. ", e);
		}
	}

	/**
	 * @return połączenie z bazą danych.
	 */
	public Connection getConnection() {
		return connection;
	}

	private void setConnection(Connection connection) {
		this.connection = connection;
	}
}
