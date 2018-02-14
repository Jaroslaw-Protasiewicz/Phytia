package pl.phytia.dao.pg;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.phytia.dao.ConnectionManager;
import pl.phytia.dao.SerialModelDAO;
import pl.phytia.model.db.SerialModelDbVO;
import pl.phytia.utils.Localization;

/**
 * Klasa dostepu do bazy danych PostgreSQL, obsługująca szereg modeli
 * przechowywany w tabeli: <code>SERIALMODEL</code>.
 * 
 * @author Jarosław Protasiewicz
 * @deprecated - do skasowania
 */
public class PgSerialModelDAO extends PgBaseDAO implements SerialModelDAO {

	public Long create(Connection dbCon, SerialModelDbVO serialModel)
			throws SQLException {
		Long SerialModelId = null;
		ConnectionManager manager = getManager(dbCon);
		CallableStatement cs = null;
		StringBuilder sql = new StringBuilder(
				"{? = call serialmodel_insert(?,?,?)}");
		try {
			cs = manager.getConnection().prepareCall(sql.toString());
			cs.registerOutParameter(1, Types.INTEGER);
			if (serialModel.getMetaModelId() != null
					&& serialModel.getMetaModelId().longValue() > 0) {
				cs.setInt(2, serialModel.getMetaModelId().intValue());
			} else {
				cs.setNull(2, Types.INTEGER);
			}
			if (serialModel.getModelId() != null
					&& serialModel.getModelId().longValue() > 0) {
				cs.setInt(3, serialModel.getModelId().intValue());
			} else {
				cs.setNull(3, Types.INTEGER);
			}
			if (serialModel.getModelDate() != null) {
				Calendar cal = Calendar.getInstance(Localization.plLocale);
				cal.setTime(serialModel.getModelDate());
				cs.setDate(4, new java.sql.Date(cal.getTimeInMillis()));
			} else {
				cs.setNull(4, Types.DATE);
			}
			cs.execute();
			SerialModelId = (long) cs.getInt(1);
		} finally {
			manager.close(null, cs);
		}
		return SerialModelId;

	}

	public void delete(Connection dbCon, SerialModelDbVO query)
			throws SQLException {
		ConnectionManager manager = getManager(dbCon);
		CallableStatement cs = null;
		StringBuilder sql = new StringBuilder("delete from "
				+ query.getTableName());
		sql.append(" where " + query.getColumnNameForModelId() + " = ?");
		sql.append(" and " + query.getColumnNameForMetaModelId() + " = ?");

		try {
			cs = manager.getConnection().prepareCall(sql.toString());
			cs.setLong(1, query.getModelId());
			cs.setLong(2, query.getMetaModelId());
			cs.execute();
		} finally {
			manager.close(null, cs);
		}
	}

	public List<SerialModelDbVO> read(Connection dbCon, SerialModelDbVO query)
			throws SQLException {
		List<SerialModelDbVO> result = new ArrayList<SerialModelDbVO>();
		ConnectionManager manager = getManager(dbCon);
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("select * from "
				+ query.getTableName() + " where 1 = 1");
		List<String> mQuery = new ArrayList<String>();
		List<String> mField = new ArrayList<String>();
		if (query.getModelId() != null && query.getModelId().longValue() > 0) {
			mQuery.add(query.getModelId().toString());
			mField.add(query.getColumnNameForModelId());
		}
		if (query.getMetaModelId() != null
				&& query.getMetaModelId().longValue() > 0) {
			mQuery.add(query.getMetaModelId().toString());
			mField.add(query.getColumnNameForMetaModelId());
		}
		/* Prepare SQL */
		for (String field : mField) {
			sql.append(" and " + field + " = ?");
		}
		try {
			ps = manager.getConnection().prepareStatement(sql.toString());
			int indx = 0;
			for (String value : mQuery) {
				ps.setString(++indx, value);
			}
			rs = ps.executeQuery();
			SerialModelDbVO sm = null;
			while (rs.next()) {
				sm = new SerialModelDbVO();
				sm.setSerialModelId(rs.getLong(query
						.getColumnNameForSerialModelId()));
				sm.setMetaModelId(rs.getLong(query
						.getColumnNameForMetaModelId()));
				sm.setModelId(rs.getLong(query.getColumnNameForModelId()));
				sm.setModelDate(rs.getDate(query.getColumnNameForModelDate()));
				result.add(sm);
			}
		} finally {
			manager.close(rs, ps);
		}
		return result;
	}

}
