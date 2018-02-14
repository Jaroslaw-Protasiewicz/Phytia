package pl.phytia.dao.pg;

import java.sql.Connection;

import pl.phytia.dao.ConnectionManager;
import pl.phytia.dao.BaseDAO;

/**
 * Bazowe DAO dla bazy danych PostgreSQL.
 * 
 * @author Jarosław Protasiewicz
 */
public abstract class PgBaseDAO implements BaseDAO {

	public ConnectionManager getManager(Connection connection) {
		return PostgreSqlDAOFactory.getManager(connection);
	}

	public ConnectionManager getManager(Connection connection,
			boolean autoCommit) {
		return PostgreSqlDAOFactory.getManager(connection, autoCommit);
	}

}
