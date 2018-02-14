package pl.phytia.dao.pg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.phytia.dao.ConnectionManager;
import pl.phytia.dao.PredictionDAO;
import pl.phytia.model.db.PredictionDbVO;

/**
 * Klasa dostepu do bazy danych PostgreSQL, obsługująca prognozy przechowywane w
 * tabeli: <code>prediction</code>.
 * 
 * @author Jarosław Protasiewicz
 */
public class PgPredictionDAO extends PgBaseDAO implements PredictionDAO {

	public void createOrUpdate(Connection dbCon, PredictionDbVO pred)
			throws SQLException {
		ConnectionManager manager = getManager(dbCon);
		PreparedStatement ps = null;
		// CallableStatement cs = null;
		StringBuilder sql = new StringBuilder("");
		try {
			PredictionDbVO predDb = read(dbCon, pred.getPredictionId() != null
					&& pred.getPredictionId().longValue() > 0 ? pred
					.getPredictionId() : null, pred.getSignalNameId() != null
					&& pred.getSignalNameId().longValue() > 0 ? pred
					.getSignalNameId() : null, pred.getPredDay() != null ? pred
					.getPredDay() : null, pred.getPredHour() != null ? pred
					.getPredHour() : null, pred.getDayAhead() != null ? pred
					.getDayAhead() : null);
			if (predDb != null) {
				// update
				pred.setPredictionId(pred.getPredictionId());
				sql.append("update " + pred.getTableName() + " set ");
				sql.append(pred.getColumnNameForPredDay() + " = ?, ");
				sql.append(pred.getColumnNameForPredHour() + " = ?, ");
				sql.append(pred.getColumnNameForDayAhead() + " = ?, ");
				sql.append(pred.getColumnNameForPredSeason() + " = ?, ");
				sql.append(pred.getColumnNameForPredTime() + " = ?, ");
				sql.append(pred.getColumnNameForPredValue() + " = ?, ");
				sql.append(pred.getColumnNameForSignalNameId() + " = ? ");
				sql.append(" where " + pred.getColumnNameForPredictionId()
						+ " = ? ");
				ps = manager.getConnection().prepareStatement(sql.toString());
				ps.setDate(1, new java.sql.Date(pred.getPredDay().getTime()));
				ps.setInt(2, pred.getPredHour().intValue());
				ps.setInt(3, pred.getDayAhead());
				ps.setInt(4, pred.getPredSeason().intValue());
				ps.setTimestamp(5, new java.sql.Timestamp(pred.getPredTime()
						.getTime()));
				ps.setDouble(6, pred.getPredValue());
				ps.setLong(7, pred.getSignalNameId().longValue());
				ps.setLong(8, predDb.getPredictionId().longValue());
				ps.executeUpdate();
			} else {
				sql.append(" insert into " + pred.getTableName() + " ( ");
				sql.append(pred.getColumnNameForPredDay() + ", ");
				sql.append(pred.getColumnNameForPredHour() + ", ");
				sql.append(pred.getColumnNameForDayAhead() + ", ");
				sql.append(pred.getColumnNameForPredSeason() + ", ");
				sql.append(pred.getColumnNameForPredTime() + ", ");
				sql.append(pred.getColumnNameForPredValue() + ", ");
				sql.append(pred.getColumnNameForSignalNameId() + ") ");
				sql.append("values (?,?,?,?,?,?,?)");
				ps = manager.getConnection().prepareStatement(sql.toString());
				ps.setDate(1, new java.sql.Date(pred.getPredDay().getTime()));
				ps.setInt(2, pred.getPredHour().intValue());
				ps.setInt(3, pred.getDayAhead());
				ps.setInt(4, pred.getPredSeason().intValue());
				ps.setTimestamp(5, new java.sql.Timestamp(pred.getPredTime()
						.getTime()));
				ps.setDouble(6, pred.getPredValue());
				ps.setLong(7, pred.getSignalNameId().longValue());
				ps.executeUpdate();

				/*
				 * sql.append("{? = call prediction_insert(?,?,?,?,?,?,?)}");
				 * //p_signal_name_id "int4", p_pred_time "timestamp",
				 * p_pred_season "int4", p_pred_hour "int4", p_pred_value
				 * "numeric", p_pred_day "date", p_day_ahead "int4") cs =
				 * manager.getConnection().prepareCall(sql.toString());
				 * cs.registerOutParameter(1, Types.INTEGER); cs.setLong(2,
				 * pred.getSignalNameId().longValue()); cs.setTimestamp(3, new
				 * java.sql.Timestamp(pred.getPredTime().getTime()));
				 * cs.setInt(4, pred.getPredSeason().intValue()); cs.setInt(5,
				 * pred.getPredHour().intValue()); cs.setDouble(6,
				 * pred.getPredValue()); cs.setDate(7, new
				 * java.sql.Date(pred.getPredDay().getTime())); cs.setInt(8,
				 * pred.getDayAhead()); cs.executeUpdate();
				 */
			}

		} finally {
			manager.close(null, ps);
			// manager.close(null, cs);
		}
	}

	public PredictionDbVO read(Connection dbCon, Long predictionId,
			Long signalNameId, Date predDay, Integer predHour, Integer dayAhead)
			throws SQLException {
		PredictionDbVO result = new PredictionDbVO();
		ConnectionManager manager = getManager(dbCon);
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("select * from "
				+ result.getTableName() + " where 1 = 1 ");
		if (predictionId != null && predictionId.longValue() > 0) {
			sql
					.append(" and " + result.getColumnNameForPredictionId()
							+ " = ?");
		} else {
			sql
					.append(" and " + result.getColumnNameForSignalNameId()
							+ " = ?");
			sql.append(" and " + result.getColumnNameForPredDay() + " = ?");
			sql.append(" and " + result.getColumnNameForPredHour() + " = ?");
			sql.append(" and " + result.getColumnNameForDayAhead() + " = ?");
		}
		try {
			ps = manager.getConnection().prepareStatement(sql.toString());
			if (predictionId != null && predictionId.longValue() > 0) {
				ps.setLong(1, predictionId.longValue());
			} else {
				ps.setLong(1, signalNameId.longValue());
				ps.setDate(2, new java.sql.Date(predDay.getTime()));
				ps.setInt(3, predHour.intValue());
				ps.setInt(4, dayAhead.intValue());
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				fillRow(rs, result);
			} else {
				result = null;
			}
		} finally {
			manager.close(rs, ps);
		}
		return result;
	}

	public List<PredictionDbVO> read(Connection dbCon, Long signalNameId,
			Date predDayForm, Date predDayTo, Integer dayAhead)
			throws SQLException {
		List<PredictionDbVO> results = new ArrayList<PredictionDbVO>();
		PredictionDbVO result = new PredictionDbVO();
		ConnectionManager manager = getManager(dbCon);
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("select * from "
				+ result.getTableName() + " where 1 = 1 ");
		sql.append(" and " + result.getColumnNameForSignalNameId() + " = ?");
		sql.append(" and " + result.getColumnNameForPredDay() + " >= ?");
		sql.append(" and " + result.getColumnNameForPredDay() + " <= ?");
		sql.append(" and " + result.getColumnNameForDayAhead() + " = ?");
		sql.append(" order by " + result.getColumnNameForPredTime());

		try {
			ps = manager.getConnection().prepareStatement(sql.toString());
			ps.setLong(1, signalNameId.longValue());
			ps.setDate(2, new java.sql.Date(predDayForm.getTime()));
			ps.setDate(3, new java.sql.Date(predDayTo.getTime()));
			ps.setInt(4, dayAhead.intValue());
			rs = ps.executeQuery();
			while (rs.next()) {
				result = new PredictionDbVO();
				fillRow(rs, result);
				results.add(result);
			}
		} finally {
			manager.close(rs, ps);
		}
		return results;
	}

	private void fillRow(ResultSet rs, PredictionDbVO result)
			throws SQLException {
		result.setPredDay(rs.getDate(result.getColumnNameForPredDay()));
		result.setPredHour(rs.getInt(result.getColumnNameForPredHour()));
		result.setDayAhead(rs.getInt(result.getColumnNameForDayAhead()));
		result.setPredictionId(rs
				.getLong(result.getColumnNameForPredictionId()));
		result.setPredSeason(rs.getInt(result.getColumnNameForPredSeason()));
		result.setPredTime(rs.getTimestamp(result.getColumnNameForPredTime()));
		result.setPredValue(rs.getDouble(result.getColumnNameForPredValue()));
		result.setSignalNameId(rs
				.getLong(result.getColumnNameForSignalNameId()));
	}
}
