package pl.phytia.dao.pg;

import java.sql.Connection;

import pl.phytia.dao.ConnectionManager;
import pl.phytia.dao.DAOFactory;
import pl.phytia.dao.MetaModelDAO;
import pl.phytia.dao.ModelDAO;
import pl.phytia.dao.PredictionDAO;
import pl.phytia.dao.SerialModelDAO;
import pl.phytia.dao.SignalDAO;

/**
 * Fabryka DAO dla bazy danych PostgreSQL
 * 
 * @author Jarosław Protasiewicz
 */
public class PostgreSqlDAOFactory extends DAOFactory {

	/**
	 * Konstruktor.
	 */
	public PostgreSqlDAOFactory() {
		super();
	}

	@Override
	public SignalDAO getSignalDAO() {
		return new PgSignalDAO();
	}

	@Override
	public MetaModelDAO getMetaModelDAO() {
		return new PgMetaModelDAO();
	}

	@Override
	public ModelDAO getModelDAO() {
		return new PgModelDAO();
	}

	@Override
	public SerialModelDAO getSerialModelDAO() {
		return new PgSerialModelDAO();
	}

	@Override
	public PredictionDAO getPredictionDAO() {
		return new PgPredictionDAO();
	}

	/**
	 * Tworzy zarządcę połączenia.
	 * 
	 * @param connection
	 *            połączenie do przekazania zarządcy (może być <code>null</code>).
	 * @return zarządca połączenia.
	 */
	public static ConnectionManager getManager(Connection connection) {
		return new PostgreSqlConnectionManager(connection);
	}

	/**
	 * Tworzy zarządcę połączenia.
	 * 
	 * @param connection
	 *            połączenie do przekazania zarządcy (może być <code>null</code>).
	 * @param autoCommit
	 *            flaga auto-commit do ustawienia na nowym połączeniu.
	 * @return zarządca połączenia.
	 */
	public static ConnectionManager getManager(Connection connection,
			boolean autoCommit) {
		return new PostgreSqlConnectionManager(connection, autoCommit);
	}
}
