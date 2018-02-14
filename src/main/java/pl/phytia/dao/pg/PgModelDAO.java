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
import pl.phytia.dao.ModelDAO;
import pl.phytia.model.db.ModelDbVO;

/**
 * Klasa dostepu do bazy danych PostgreSQL, obsługująca modele przechowywane w
 * tabeli: <code>MODEL</code>.
 * 
 * @author Jarosław Protasiewicz
 */
public class PgModelDAO extends PgBaseDAO implements ModelDAO {

	public Long create(Connection dbCon, ModelDbVO model) throws SQLException {
		Long modelId = null;
		ConnectionManager manager = getManager(dbCon);
		CallableStatement cs = null;
		StringBuilder sql = new StringBuilder(
				"{? = call model_insert(?,?,?,?,?)}");
		try {
			cs = manager.getConnection().prepareCall(sql.toString());
			cs.registerOutParameter(1, Types.INTEGER);
			if (model.getMetamodelId() != null) {
				cs.setInt(2, model.getMetamodelId().intValue());
			} else {
				cs.setNull(2, Types.NUMERIC);
			}
			if (model.getModelType() != null) {
				cs.setString(3, model.getModelType());
			} else {
				cs.setNull(3, Types.VARCHAR);
			}
			if (model.getModelClass() != null) {
				cs.setString(4, model.getModelClass());
			} else {
				cs.setNull(4, Types.VARCHAR);
			}
			if (model.getConf() != null) {
				cs.setString(5, model.getConf());
			} else {
				cs.setNull(5, Types.VARCHAR);
			}
			if (model.getNetwork() != null) {
				cs.setString(6, model.getNetwork());
			} else {
				cs.setNull(6, Types.VARCHAR);
			}
			cs.execute();
			modelId = (long) cs.getInt(1);
		} finally {
			manager.close(null, cs);
		}
		return modelId;
	}

	public void delete(Connection dbCon, ModelDbVO query) throws SQLException {
		ConnectionManager manager = getManager(dbCon);
		CallableStatement cs = null;
		StringBuilder sql = new StringBuilder("delete from "
				+ query.getTableName());
		if (query.getModelId() != null && query.getModelId().longValue() > 0) {
			sql.append(" where " + query.getColumnNameForModelId() + " = ?");
		} else {
			sql
					.append(" where " + query.getColumnNameForMetamodelId()
							+ " = ?");
			sql.append(" and " + query.getColumnNameForModelClass() + " = ?");
			sql.append(" and " + query.getColumnNameForModelType() + " = ?");
		}
		try {
			cs = manager.getConnection().prepareCall(sql.toString());
			if (query.getModelId() != null
					&& query.getModelId().longValue() > 0) {
				cs.setLong(1, query.getModelId().longValue());
			} else {
				cs.setLong(1, query.getMetamodelId());
				cs.setString(2, query.getModelClass());
				cs.setString(3, query.getModelType());
			}
			cs.execute();
		} finally {
			manager.close(null, cs);
		}
	}

	public List<ModelDbVO> read(Connection dbCon, ModelDbVO query)
			throws SQLException {
		List<ModelDbVO> result = new ArrayList<ModelDbVO>();
		ConnectionManager manager = getManager(dbCon);
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("select * from "
				+ query.getTableName());
		if (query.getModelId() != null && query.getModelId().longValue() > 0) {
			sql.append(" where " + query.getColumnNameForModelId() + " = ?");
		} else {
			sql
					.append(" where " + query.getColumnNameForMetamodelId()
							+ " = ?");
			sql.append(" and " + query.getColumnNameForModelClass() + " = ?");
			sql.append(" and " + query.getColumnNameForModelType() + " = ?");
		}
		try {
			ps = manager.getConnection().prepareStatement(sql.toString());
			if (query.getModelId() != null
					&& query.getModelId().longValue() > 0) {
				ps.setLong(1, query.getModelId().longValue());
			} else {
				ps.setLong(1, query.getMetamodelId());
				ps.setString(2, query.getModelClass());
				ps.setString(3, query.getModelType());
			}
			rs = ps.executeQuery();
			ModelDbVO m = null;
			while (rs.next()) {
				m = new ModelDbVO();
				m.setConf(rs.getString(query.getColumnNameForConf()));
				m.setModelClass(rs
						.getString(query.getColumnNameForModelClass()));
				m.setModelId(rs.getLong(query.getColumnNameForModelId()));
				m.setModelType(rs.getString(query.getColumnNameForModelType()));
				m.setNetwork(rs.getString(query.getColumnNameForNetwork()));
				m.setMetamodelId(rs
						.getLong(query.getColumnNameForMetamodelId()));
				result.add(m);
			}
		} finally {
			manager.close(rs, ps);
		}
		return result;
	}

	public void update(Connection dbCon, ModelDbVO model) throws SQLException {
		ConnectionManager manager = getManager(dbCon);
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder("update " + model.getTableName()
				+ " set ");
		sql.append(model.getColumnNameForConf() + " = ?, ");
		sql.append(model.getColumnNameForModelClass() + " = ?, ");
		sql.append(model.getColumnNameForMetamodelId() + " = ?,");
		sql.append(model.getColumnNameForModelId() + " = ?, ");
		sql.append(model.getColumnNameForModelType() + " = ?, ");
		sql.append(model.getColumnNameForNetwork() + " = ? ");
		if (model.getModelId() != null && model.getModelId().longValue() > 0) {
			sql.append(" where " + model.getColumnNameForModelId() + " = ?");
		} else {
			sql
					.append(" where " + model.getColumnNameForMetamodelId()
							+ " = ?");
			sql.append(" and " + model.getColumnNameForModelClass() + " = ?");
			sql.append(" and " + model.getColumnNameForModelType() + " = ?");
		}
		try {
			ps = manager.getConnection().prepareStatement(sql.toString());
			if (model.getConf() != null) {
				ps.setString(1, model.getConf());
			} else {
				ps.setNull(1, Types.VARCHAR);
			}
			if (model.getModelClass() != null) {
				ps.setString(2, model.getModelClass());
			} else {
				ps.setNull(2, Types.VARCHAR);
			}
			if (model.getMetamodelId() != null) {
				ps.setLong(3, model.getMetamodelId());
			} else {
				ps.setNull(3, Types.NUMERIC);
			}
			if (model.getModelId() != null) {
				ps.setLong(4, model.getModelId());
			} else {
				ps.setNull(4, Types.NUMERIC);
			}
			if (model.getModelType() != null) {
				ps.setString(5, model.getModelType());
			} else {
				ps.setNull(5, Types.NUMERIC);
			}
			if (model.getNetwork() != null) {
				ps.setString(6, model.getNetwork());
			} else {
				ps.setNull(6, Types.VARCHAR);
			}
			if (model.getModelId() != null
					&& model.getModelId().longValue() > 0) {
				ps.setLong(7, model.getModelId().longValue());
			} else {
				ps.setLong(7, model.getMetamodelId());
				ps.setString(8, model.getModelClass());
				ps.setString(9, model.getModelType());
			}
			ps.executeUpdate();
		} finally {
			manager.close(null, ps);
		}
	}
}
