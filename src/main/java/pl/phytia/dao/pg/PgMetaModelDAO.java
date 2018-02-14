package pl.phytia.dao.pg;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import pl.phytia.dao.ConnectionManager;
import pl.phytia.dao.MetaModelDAO;
import pl.phytia.model.db.MetaModelDbVO;

/**
 * Klasa dostepu do bazy danych PostgreSQL, obsługująca metamodele przechowywane
 * w tabelach: <code>METAMODEL</code> i <code></code>.
 * 
 * @author Jarosław Protasiewicz
 */
public class PgMetaModelDAO extends PgBaseDAO implements MetaModelDAO {

	public Long create(Connection dbCon, MetaModelDbVO metaModel)
			throws SQLException {
		Long idMetaModelu = null;
		ConnectionManager manager = getManager(dbCon);
		CallableStatement cs = null;
		StringBuilder sql = new StringBuilder(
				"{? = call metamodel_insert(?,?,?,?,?)}");
		try {
			cs = manager.getConnection().prepareCall(sql.toString());
			cs.registerOutParameter(1, Types.INTEGER);
			if (metaModel.getConf() != null) {
				cs.setString(2, metaModel.getConf());
			} else {
				cs.setNull(2, Types.VARCHAR);
			}
			if (metaModel.getForecastType() != null) {
				cs.setString(3, metaModel.getForecastType());
			} else {
				cs.setNull(3, Types.VARCHAR);
			}
			if (metaModel.getInputSignal() != null) {
				cs.setLong(4, metaModel.getInputSignal());
			} else {
				cs.setNull(4, Types.NUMERIC);
			}
			if (metaModel.getOutputSignal() != null) {
				cs.setLong(5, metaModel.getOutputSignal());
			} else {
				cs.setNull(5, Types.NUMERIC);
			}
			if (metaModel.getMetaModelType() != null) {
				cs.setString(6, metaModel.getMetaModelType());
			} else {
				cs.setNull(6, Types.VARCHAR);
			}
			cs.execute();
			idMetaModelu = (long) cs.getInt(1);
		} finally {
			manager.close(null, cs);
		}
		return idMetaModelu;
	}

	public void delete(Connection dbCon, MetaModelDbVO query)
			throws SQLException {
		ConnectionManager manager = getManager(dbCon);
		CallableStatement cs = null;
		StringBuilder sql = new StringBuilder("delete from "
				+ query.getTableName());
		if (query.getIdMetamodelu() != null
				&& query.getIdMetamodelu().longValue() > 0) {
			sql.append(" where " + query.getColumnNameForIdMetamodelu()
					+ " = ?");
		} else {
			sql.append(" where " + query.getColumnNameForMetaModelType()
					+ " = ?");
			sql.append(" and " + query.getColumnNameForForecastType() + " = ?");
		}
		try {
			cs = manager.getConnection().prepareCall(sql.toString());
			if (query.getIdMetamodelu() != null
					&& query.getIdMetamodelu().longValue() > 0) {
				cs.setLong(1, query.getIdMetamodelu().longValue());
			} else {
				cs.setString(1, query.getMetaModelType());
				cs.setString(2, query.getForecastType());
			}
			cs.execute();
		} finally {
			manager.close(null, cs);
		}
	}

	public List<MetaModelDbVO> read(Connection dbCon, MetaModelDbVO query)
			throws SQLException {
		List<MetaModelDbVO> result = new ArrayList<MetaModelDbVO>();
		ConnectionManager manager = getManager(dbCon);
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("select * from "
				+ query.getTableName());
		if (query.getIdMetamodelu() != null
				&& query.getIdMetamodelu().longValue() > 0) {
			sql.append(" where " + query.getColumnNameForIdMetamodelu()
					+ " = ? ");
		} else {
			sql.append(" where " + query.getColumnNameForMetaModelType()
					+ " = ? ");
			sql
					.append(" and " + query.getColumnNameForForecastType()
							+ " = ? ");
		}
		try {
			ps = manager.getConnection().prepareStatement(sql.toString());
			if (query.getIdMetamodelu() != null
					&& query.getIdMetamodelu().longValue() > 0) {
				ps.setLong(1, query.getIdMetamodelu());
			} else {
				ps.setString(1, query.getMetaModelType());
				ps.setString(2, query.getForecastType());
			}
			rs = ps.executeQuery();
			MetaModelDbVO mm = null;
			while (rs.next()) {
				mm = new MetaModelDbVO();
				mm.setConf(rs.getString(query.getColumnNameForConf()));
				mm.setForcastType(rs.getString(query
						.getColumnNameForForecastType()));
				mm.setIdMetamodelu(rs.getLong(query
						.getColumnNameForIdMetamodelu()));
				mm.setInputSignal(rs.getLong(query
						.getColumnNameForInputSignal()));
				mm.setMetaModelType(rs.getString(query
						.getColumnNameForMetaModelType()));
				mm.setOutputSignal(rs.getLong(query
						.getColumnNameForOutputSignal()));
				result.add(mm);
			}

		} finally {
			manager.close(rs, ps);
		}
		return result;
	}

	public void update(Connection dbCon, MetaModelDbVO metaModel)
			throws SQLException {
		ConnectionManager manager = getManager(dbCon);
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder("update "
				+ metaModel.getTableName() + " set ");
		sql.append(metaModel.getColumnNameForConf() + " = ?, ");
		sql.append(metaModel.getColumnNameForForecastType() + " = ?, ");
		sql.append(metaModel.getColumnNameForInputSignal() + " = ?, ");
		sql.append(metaModel.getColumnNameForOutputSignal() + " = ?, ");
		sql.append(metaModel.getColumnNameForMetaModelType() + " = ? ");
		if (metaModel.getIdMetamodelu() != null
				&& metaModel.getIdMetamodelu().longValue() > 0) {
			sql.append(" where " + metaModel.getColumnNameForIdMetamodelu()
					+ " = ?");
		} else {
			sql.append(" where " + metaModel.getColumnNameForMetaModelType()
					+ " = ?");
			sql.append(" and " + metaModel.getColumnNameForForecastType()
					+ " = ?");
		}
		try {
			ps = manager.getConnection().prepareStatement(sql.toString());
			if (metaModel.getConf() != null) {
				ps.setString(1, metaModel.getConf());
			} else {
				ps.setNull(1, Types.VARCHAR);
			}
			if (metaModel.getForecastType() != null) {
				ps.setString(2, metaModel.getForecastType());
			} else {
				ps.setNull(2, Types.VARCHAR);
			}
			if (metaModel.getInputSignal() != null) {
				ps.setLong(3, metaModel.getInputSignal());
			} else {
				ps.setNull(3, Types.NUMERIC);
			}
			if (metaModel.getOutputSignal() != null) {
				ps.setLong(4, metaModel.getOutputSignal());
			} else {
				ps.setNull(4, Types.NUMERIC);
			}
			if (metaModel.getMetaModelType() != null) {
				ps.setString(5, metaModel.getMetaModelType());
			} else {
				ps.setNull(5, Types.VARCHAR);
			}
			if (metaModel.getIdMetamodelu() != null
					&& metaModel.getIdMetamodelu().longValue() > 0) {
				ps.setLong(6, metaModel.getIdMetamodelu().longValue());
			} else {
				ps.setString(6, metaModel.getMetaModelType());
				ps.setString(7, metaModel.getForecastType());
			}
			ps.executeUpdate();

		} finally {
			manager.close(null, ps);
		}
	}

}
