package pl.phytia.dao.pg;

import java.sql.Connection;

import org.apache.log4j.Logger;

import pl.phytia.dao.ConnectionManager;

/**
 * Manager połączenia z bazą danych PostgreSQL.
 * 
 * @author Jarosław Protasiewicz
 */
public final class PostgreSqlConnectionManager extends ConnectionManager {

	protected transient Logger logger = Logger
			.getLogger(PostgreSqlConnectionManager.class);

	/**
	 * Konstruktor zarządcy połączeń - nie tworzy połączenia.
	 */
	public PostgreSqlConnectionManager() {
		super();
	}

	/**
	 * Konstruktor zarządcy połączeń.
	 * 
	 * @param connection
	 *            Połączenie z bazą, jeżeli jest <code>null</code>, manager
	 *            utworzy nowe.
	 */
	public PostgreSqlConnectionManager(Connection connection) {
		super(connection);
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
	public PostgreSqlConnectionManager(Connection connection, boolean autoCommit) {
		super(connection, autoCommit);
	}

	@Override
	public String getDbParamDriver() {
		return "org.postgresql.Driver";
	}

	@Override
	public String getDbParamPassword() {
		return "postgres";
	}

	@Override
	public String getDbPramUrl() {
		return "jdbc:postgresql://localhost:5432/phytia";
		// return "jdbc:postgresql://localhost:5432/postgres";
	}

	@Override
	public String getDbparamUserName() {
		return "postgres";
	}

}
