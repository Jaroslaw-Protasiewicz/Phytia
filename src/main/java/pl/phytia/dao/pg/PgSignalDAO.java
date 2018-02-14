package pl.phytia.dao.pg;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import pl.phytia.dao.ConnectionManager;
import pl.phytia.dao.SignalDAO;
import pl.phytia.model.db.SignalNameVO;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.PatternPairVO;
import pl.phytia.model.signals.DataVO;
import pl.phytia.model.signals.WindowVO;
import pl.phytia.utils.Localization;

/**
 * Klasa dostepu do bazy danych PostgreSQL, obsługująca sygnały przechowywane w
 * tabelach: <code>signals</code> i <code>signal_names</code>.
 * 
 * @author Jarosław Protasiewicz
 */
public class PgSignalDAO extends PgBaseDAO implements SignalDAO {

	private Calendar cal = Calendar.getInstance(Localization.plLocale);

	protected transient Logger logger = Logger.getLogger(PgSignalDAO.class);

	/**
	 * Konstruktor domyślny.
	 */
	public PgSignalDAO() {
		super();
	}

	public double getSumOfEnergy(Connection dbCon, String signalName,
			Date fromTime, Date toTime) throws SQLException {
		double sum = 0;
		ConnectionManager manager = getManager(dbCon);
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select sum(energy) from signals "
				+ " where	signal_name_id = (select signal_name_id  from signal_names where name = '"
				+ signalName + "')" + " and dtime >= ? and dtime <= ?";
		try {
			Calendar cal = Calendar.getInstance(Localization.plLocale);
			ps = manager.getConnection().prepareStatement(sql);
			cal.setTime(fromTime);
			ps.setTimestamp(1, new Timestamp(fromTime.getTime()));
			ps.setTimestamp(2, new Timestamp(toTime.getTime()));
			rs = ps.executeQuery();
			if (rs.next())
				sum = rs.getDouble(1);
		} finally {
			manager.close(rs, ps);
		}
		return sum;
	}

	public List<String> getDiffClasses(Connection dbCon) throws SQLException {
		ConnectionManager manager = getManager(dbCon);
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> lst = new ArrayList<String>();
		String sql = "select distinct day_type as dayClass from signals where signal_name_id = 1 "
				+ "union "
				+ "select distinct holiday as dayClass from signals where signal_name_id = 1 ";
		try {
			ps = manager.getConnection().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				lst.add(rs.getString("dayClass"));
			}
		} finally {
			manager.close(rs, ps);
		}
		return lst;
	}

	public Map<Date, List<DataVO>> getAllDayProfiles(Connection dbCon,
													 String signalName) throws SQLException {
		Map<Date, List<DataVO>> map = new HashMap<Date, List<DataVO>>();
		ConnectionManager manager = getManager(dbCon);
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select s.dtime, s.dday, s.dhour, s.season, "
				+ "s.energy, s.temperature, s.temperature_min, s.temperature_max, "
				+ "s.humidity, s.insolation, "
				+ "s.day_type, s.holiday "
				+ "from signals s "
				+ "where	s.signal_name_id = (select signal_name_id  from signal_names where name = ?) "
				+
				// " and dday <= '2003-12-31'" +
				"order by s.dtime";

		Date dBufHelper = null;
		Date dLocHelper = null;
		List<DataVO> lHelper = null;
		try {
			ps = manager.getConnection().prepareStatement(sql);
			ps.setString(1, signalName);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.isFirst()) {
					lHelper = new ArrayList<DataVO>();
				}
				dLocHelper = rs.getDate("dday");
				if (!dLocHelper.equals(dBufHelper)) {
					map.put(dBufHelper, lHelper);
					lHelper = new ArrayList<DataVO>();
				}
				DataVO d = new DataVO();
				d.setTime(rs.getDate("dtime"));
				d.setData(dLocHelper);
				d.setHour(rs.getShort("dhour"));
				d.setTimeType(rs.getShort("season"));
				d.setEnergy(rs.getDouble("energy"));
				d.setTemperature(rs.getDouble("temperature"));
				d.setTempMin(rs.getDouble("temperature_min"));
				d.setTempMax(rs.getDouble("temperature_max"));
				d.setInsolation(rs.getDouble("insolation"));
				d.setHumidity(rs.getDouble("humidity"));
				d.setDayType(rs.getString("day_type"));
				d.setHoliday(rs.getString("holiday"));
				lHelper.add(d);
				dBufHelper = dLocHelper;
				if (rs.isLast()) {
					map.put(dLocHelper, lHelper);
				}
			}
		} finally {
			manager.close(rs, ps);
		}
		return map;
	}

	public List<DataVO> readSignal(Connection dbCon, String signalName,
			Date from, Date to) throws SQLException {
		ArrayList<DataVO> lst = new ArrayList<DataVO>();
		ConnectionManager manager = getManager(dbCon);
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select s.dtime, s.dday, s.dhour, s.season, "
				+ "s.energy, s.temperature, s.temperature_min, s.temperature_max, "
				+ "s.humidity, s.insolation, "
				+ "s.day_type, s.holiday "
				+ "from signals s "
				+ "where	s.signal_name_id = (select signal_name_id  from signal_names where name = ?) "
				+ " and s.dday >= ? and s.dday <= ? " + "order by s.dtime";
		try {
			ps = manager.getConnection().prepareStatement(sql);
			ps.setString(1, signalName);
			ps.setDate(2, new java.sql.Date(from.getTime()));
			ps.setDate(3, new java.sql.Date(to.getTime()));
			rs = ps.executeQuery();
			while (rs.next()) {
				DataVO d = new DataVO();
				d.setTime(rs.getTimestamp("dtime"));
				d.setData(rs.getDate("dday"));
				d.setHour(rs.getShort("dhour"));
				d.setTimeType(rs.getShort("season"));
				d.setEnergy(rs.getDouble("energy"));
				d.setTemperature(rs.getDouble("temperature"));
				d.setTempMin(rs.getDouble("temperature_min"));
				d.setTempMax(rs.getDouble("temperature_max"));
				d.setInsolation(rs.getDouble("insolation"));
				d.setHumidity(rs.getDouble("humidity"));
				d.setDayType(rs.getString("day_type"));
				d.setHoliday(rs.getString("holiday"));
				lst.add(d);
			}
		} finally {
			manager.close(rs, ps);
		}
		return lst;

	}

	public ArrayList<Date> getWindowsClasses(Connection dbCon,
											 String signalName, ArrayList<WindowVO> windows, String[] cl,
											 Date dzienPrognozy) throws SQLException {
		ArrayList<Date> lst = new ArrayList<Date>();
		ConnectionManager manager = getManager(dbCon);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuilder sql = new StringBuilder(
					"select distinct dday from signals ");
			sql
					.append("where signal_name_id = (select signal_name_id  from signal_names where name = '"
							+ signalName + "')");
			if (cl[0].equals("d")) {
				sql.append(" and holiday is null and day_type = ?");
				sql.append(" and (");
				for (int i = 0; i < windows.size(); ++i) {
					if (i != windows.size() - 1) {
						sql.append("(dday >= ? and dday <= ?) or");
					} else {
						sql.append("(dday >= ? and dday <= ?)");
					}
				}
				sql.append(")");
			} else if (cl[0].equals("h")) {
				sql.append(" and holiday = ?");
				if (dzienPrognozy != null) {
					sql.append("and dday != ?");
				}
			} else {
				throw new SQLException("Błąd generacji sql = " + sql.toString());
			}
			sql.append(" order by dday");
			// System.out.println("SQL: " + sql);
			ps = manager.getConnection().prepareStatement(sql.toString());
			ps.setString(1, cl[1]);
			if (cl[0].equals("d")) {
				int ix = 1;
				for (WindowVO win : windows) {
					ps.setDate(++ix, new java.sql.Date(win.getDateFromLong()));
					ps.setDate(++ix, new java.sql.Date(win.getDateToLong()));
				}
			} else if (cl[0].equals("h")) {
				if (dzienPrognozy != null) {
					cal.setTime(dzienPrognozy);
					ps.setDate(2, new java.sql.Date(cal.getTimeInMillis()));
				} else {
					// ps.setNull(2, Types.DATE);
				}

			} else {
				throw new SQLException("Błąd generacji sql = " + sql.toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				lst.add(rs.getDate("dday"));
			}
		} finally {
			manager.close(rs, ps);
		}
		return lst;
	}

	public String[] getTypeOfDay(Connection dbCon, String signalName,
			long predictionDay) throws SQLException {
		ConnectionManager manager = getManager(dbCon);
		String[] clDay = new String[2];
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select day_type, holiday from signals "
				+ "where	signal_name_id = (select signal_name_id  from signal_names where name = '"
				+ signalName + "')" + " and dday = ? and dhour = 1";
		try {
			ps = manager.getConnection().prepareStatement(sql);
			ps.setDate(1, new java.sql.Date(predictionDay));
			rs = ps.executeQuery();
			if (rs.next()) {
				String d = rs.getString("day_type");
				String h = rs.getString("holiday");
				if (h != null) {
					clDay[0] = "h";
					clDay[1] = h;
				} else {
					clDay[0] = "d";
					clDay[1] = d;
				}
			}
		} finally {
			manager.close(rs, ps);
		}
		return clDay;
	}

	public long getSignalId(Connection dbCon, String signalName, Date day,
			int hour) throws SQLException {
		long signalId = -1;
		ConnectionManager manager = getManager(dbCon);
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select signal_id from signals "
				+ " where	signal_name_id = (select signal_name_id  from signal_names where name = '"
				+ signalName + "')" + " and dday = ? and dhour = ?";
		try {
			ps = manager.getConnection().prepareStatement(sql);
			cal.setTime(day);
			ps.setDate(1, new java.sql.Date(cal.getTimeInMillis()));
			ps.setInt(2, hour);
			rs = ps.executeQuery();
			if (rs.next()) {
				signalId = rs.getLong("signal_id");
			}
		} finally {
			manager.close(rs, ps);
		}
		return signalId;
	}

	public PatternPairVO getSetForHour(Connection dbCon, String signalName,
									   Date timePoint, int eLag, int tLag, int hLag, int iLag)
			throws SQLException {
		PatternPairVO res = null;
		ConnectionManager manager = getManager(dbCon);
		long signalId = -1;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		String sql1 = "select signal_id from signals "
				+ " where	signal_name_id = (select signal_name_id  from signal_names where name = '"
				+ signalName + "')" + " and dday = ? and dhour = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = " select s.dtime, s.dday, s.dhour, s.season, "
				+ " s.energy, s.temperature, s.temperature_min, s.temperature_max, "
				+ " s.humidity, s.insolation, " + " s.day_type, s.holiday "
				+ " from signals s"
				+ "	where s.signal_id >= ? and s.signal_id <= ? "
				+ "  order by s.signal_id desc ";
		try {
			ps1 = manager.getConnection().prepareStatement(sql1);
			cal.setTime(timePoint);
			ps1.setDate(1, new java.sql.Date(cal.getTimeInMillis()));
			ps1.setInt(2, cal.get(Calendar.HOUR_OF_DAY) + 1);
			rs1 = ps1.executeQuery();
			if (rs1.next()) {
				DoubleVector inputs = new DoubleVector(eLag + tLag + hLag
						+ iLag, 0.0);
				DoubleVector outpus = new DoubleVector(1, 0.0);
				Date dtime = null;
				signalId = rs1.getLong("signal_id");
				ps = manager.getConnection().prepareStatement(sql);
				ps.setLong(1, signalId - eLag);
				ps.setLong(2, signalId);
				rs = ps.executeQuery();
				int indx = 0;
				while (rs.next()) {
					if (indx == 0) {
						double e = rs.getDouble("energy");
						outpus.set(indx, e);
						dtime = rs.getTimestamp("dtime");
					}
					if (indx > 0 & indx <= eLag) {
						inputs.set(indx - 1, rs.getDouble("energy"));
					}
					if (indx >= 0 & indx < tLag) {
						inputs.set(indx + eLag, rs.getDouble("temperature"));
					}
					if (indx >= 0 & indx < hLag) {
						inputs
								.set(indx + eLag + tLag, rs
										.getDouble("humidity"));
					}
					if (indx >= 0 & indx < iLag) {
						inputs.set(indx + eLag + tLag + hLag, rs
								.getDouble("insolation"));
					}
					++indx;
				}
				res = new PatternPairVO(inputs, outpus, dtime);
			}
		} finally {
			manager.close(rs1, ps1);
			manager.close(rs, ps);
		}
		return res;
	}

	public PatternPairVO getSetForHour(Connection dbCon, String signalName,
			Date timePoint, int eLag, int tLag, int hLag, int iLag,
			boolean dayType, boolean holidayType, int eMean24Lag,
			int eMean168Lag) throws SQLException {
		int dayTypeLag = (dayType ? 1 : 0);
		int holidayTypeLag = (holidayType ? 1 : 0);
		int inSize = eLag + tLag + hLag + iLag + dayTypeLag + holidayTypeLag
				+ eMean24Lag + eMean168Lag;
		PatternPairVO res = null;
		ConnectionManager manager = getManager(dbCon);
		long signalId = -1;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		String sql1 = "select signal_id from signals "
				+ " where	signal_name_id = (select signal_name_id  from signal_names where name = '"
				+ signalName + "')" + " and dday = ? and dhour = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = " select s.dtime, s.dday, s.dhour, s.season, "
				+ " s.energy, s.temperature, s.temperature_min, s.temperature_max, "
				+ " s.humidity, s.insolation, " + " s.day_type, s.holiday, "
				+ " s.day_val, s.holiday_val," + " s.e_mean_24, s.e_mean_168 "
				+ " from signals s"
				+ "	where s.signal_id >= ? and s.signal_id <= ? "
				+ "  order by s.signal_id desc ";
		try {
			ps1 = manager.getConnection().prepareStatement(sql1);

			cal.setTime(timePoint);
			ps1.setDate(1, new java.sql.Date(cal.getTimeInMillis()));
			ps1.setInt(2, cal.get(Calendar.HOUR_OF_DAY) + 1);
			// logger.debug(cal.getTime().toString());
			// logger.debug(cal.get(Calendar.HOUR_OF_DAY));
			rs1 = ps1.executeQuery();
			if (rs1.next()) {
				DoubleVector inputs = new DoubleVector(inSize, 0.0);
				DoubleVector outpus = new DoubleVector(1, 0.0);
				Date dtime = null;
				signalId = rs1.getLong("signal_id");
				ps = manager.getConnection().prepareStatement(sql);
				ps.setLong(1, signalId - eLag);
				ps.setLong(2, signalId);
				rs = ps.executeQuery();
				int indx = 0;
				while (rs.next()) {
					if (indx == 0) {
						double e = rs.getDouble("energy");
						outpus.set(indx, e);
						dtime = rs.getTimestamp("dtime");
						if (eMean24Lag == 1)
							inputs.set(indx + eLag + tLag + hLag + iLag
									+ dayTypeLag + holidayTypeLag, rs
									.getDouble("e_mean_24"));
						if (eMean168Lag == 1)
							inputs.set(eLag + tLag + hLag + iLag + dayTypeLag
									+ holidayTypeLag + eMean24Lag, rs
									.getDouble("e_mean_168"));
					}
					if (indx > 0 & indx <= eLag) {
						inputs.set(indx - 1, rs.getDouble("energy"));
					}
					if (indx >= 0 & indx < tLag) {
						inputs.set(indx + eLag, rs.getDouble("temperature"));
					}
					if (indx >= 0 & indx < hLag) {
						inputs
								.set(indx + eLag + tLag, rs
										.getDouble("humidity"));
					}
					if (indx >= 0 & indx < iLag) {
						inputs.set(indx + eLag + tLag + hLag, rs
								.getDouble("insolation"));
					}
					if (indx >= 0 & indx < dayTypeLag) {
						inputs.set(indx + eLag + tLag + hLag + iLag, rs
								.getDouble("day_val"));
					}
					if (indx >= 0 & indx < holidayTypeLag) {
						inputs.set(indx + eLag + tLag + hLag + iLag
								+ dayTypeLag, rs.getDouble("holiday_val"));
					}
					++indx;
				}
				res = new PatternPairVO(inputs, outpus, dtime);
			}
		} finally {
			manager.close(rs1, ps1);
			manager.close(rs, ps);
		}
		return res;
	}

	public List<PatternPairVO> getSetForDay(Connection dbCon,
			String signalName, Date day, int eLag, int tLag, int hLag, int iLag)
			throws SQLException {
		ConnectionManager manager = getManager(dbCon);
		List<PatternPairVO> lst = new ArrayList<PatternPairVO>(24);
		long signalId = -1;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		String sql1 = "select signal_id from signals "
				+ " where	signal_name_id = (select signal_name_id  from signal_names where name = '"
				+ signalName + "')" + " and dday = ? order by dhour";

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = " select s.dtime, s.dday, s.dhour, s.season, "
				+ " s.energy, s.temperature, s.temperature_min, s.temperature_max, "
				+ " s.humidity, s.insolation, " + " s.day_type, s.holiday "
				+ " from signals s"
				+ "	where s.signal_id >= ? and s.signal_id <= ? "
				+ "  order by s.signal_id desc ";

		try {
			ps1 = manager.getConnection().prepareStatement(sql1);
			cal.setTime(day);
			ps1.setDate(1, new java.sql.Date(cal.getTimeInMillis()));
			rs1 = ps1.executeQuery();
			while (rs1.next()) {
				DoubleVector inputs = new DoubleVector(eLag + tLag + hLag
						+ iLag, 0.0);
				DoubleVector outpus = new DoubleVector(1, 0.0);
				Date dtime = null;
				signalId = rs1.getLong("signal_id");
				ps = manager.getConnection().prepareStatement(sql);
				ps.setLong(1, signalId - eLag);
				ps.setLong(2, signalId);
				rs = ps.executeQuery();
				int indx = 0; // p = new double[size];
				boolean isAdd = true;
				while (rs.next()) {
					if (indx == 0) {
						double e = rs.getDouble("energy");
						if (rs.wasNull()) {
							isAdd = false;
							break;
						} else {
							outpus.set(indx, e);
							dtime = rs.getTimestamp("dtime");
							// p[indx] = e;
						}
					}
					if (indx > 0 & indx <= eLag) {
						inputs.set(indx - 1, rs.getDouble("energy"));
						// p[indx] = rs.getDouble("energy");
					}
					if (indx >= 0 & indx < tLag) {
						inputs.set(indx + eLag, rs.getDouble("temperature"));
						// p[indx + 1 + eLag] = rs.getDouble("temperature");
					}
					if (indx >= 0 & indx < hLag) {
						inputs
								.set(indx + eLag + tLag, rs
										.getDouble("humidity"));
						// p[indx + 1 + eLag + tLag] = rs.getDouble("humidity");
					}
					if (indx >= 0 & indx < iLag) {
						inputs.set(indx + eLag + tLag + hLag, rs
								.getDouble("insolation"));
						// p[indx + 1 + eLag + tLag + hLag] =
						// rs.getDouble("insolation");
					}
					++indx;
				}
				if (isAdd) {
					lst.add(new PatternPairVO(inputs, outpus, dtime));
				}
				manager.close(rs, ps);
			}
		} finally {
			manager.close(rs1, ps1);
		}
		return lst;
	}

	public List<PatternPairVO> getSetForDay(Connection dbCon,
			String signalName, Date day, int eLag, int tLag, int hLag,
			int iLag, boolean dayType, boolean holidayType, int eMean24Lag,
			int eMean168Lag) throws SQLException {
		int dayTypeLag = (dayType ? 1 : 0);
		int holidayTypeLag = (holidayType ? 1 : 0);
		int inSize = eLag + tLag + hLag + iLag + dayTypeLag + holidayTypeLag
				+ eMean24Lag + eMean168Lag;
		ConnectionManager manager = getManager(dbCon);
		List<PatternPairVO> lst = new ArrayList<PatternPairVO>(24);
		long signalId = -1;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		String sql1 = "select signal_id from signals "
				+ " where	signal_name_id = (select signal_name_id  from signal_names where name = '"
				+ signalName + "')" + " and dday = ? order by dhour";

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = " select s.dtime, s.dday, s.dhour, s.season, "
				+ " s.energy, s.temperature, s.temperature_min, s.temperature_max, "
				+ " s.humidity, s.insolation, " + " s.day_type, s.holiday, "
				+ " s.day_val, s.holiday_val," + " s.e_mean_24, s.e_mean_168 "
				+ " from signals s"
				+ "	where s.signal_id >= ? and s.signal_id <= ? "
				+ "  order by s.signal_id desc ";

		try {
			ps1 = manager.getConnection().prepareStatement(sql1);
			cal.setTime(day);
			ps1.setDate(1, new java.sql.Date(cal.getTimeInMillis()));
			rs1 = ps1.executeQuery();
			while (rs1.next()) {
				DoubleVector inputs = new DoubleVector(inSize, 0.0);
				DoubleVector outpus = new DoubleVector(1, 0.0);
				Date dtime = null;
				signalId = rs1.getLong("signal_id");
				ps = manager.getConnection().prepareStatement(sql);
				ps.setLong(1, signalId - eLag);
				ps.setLong(2, signalId);
				rs = ps.executeQuery();
				int indx = 0;
				boolean isAdd = true;
				while (rs.next()) {
					if (indx == 0) {
						double e = rs.getDouble("energy");
						if (rs.wasNull()) {
							isAdd = false;
							break;
						} else {
							outpus.set(indx, e);
							dtime = rs.getTimestamp("dtime");
							if (eMean24Lag == 1)
								inputs.set(indx + eLag + tLag + hLag + iLag
										+ dayTypeLag + holidayTypeLag, rs
										.getDouble("e_mean_24"));
							if (eMean168Lag == 1)
								inputs.set(eLag + tLag + hLag + iLag
										+ dayTypeLag + holidayTypeLag
										+ eMean24Lag, rs
										.getDouble("e_mean_168"));
						}
					}
					if (indx > 0 & indx <= eLag) {
						inputs.set(indx - 1, rs.getDouble("energy"));
					}
					if (indx >= 0 & indx < tLag) {
						inputs.set(indx + eLag, rs.getDouble("temperature"));
					}
					if (indx >= 0 & indx < hLag) {
						inputs
								.set(indx + eLag + tLag, rs
										.getDouble("humidity"));
					}
					if (indx >= 0 & indx < iLag) {
						inputs.set(indx + eLag + tLag + hLag, rs
								.getDouble("insolation"));
					}
					if (indx >= 0 & indx < dayTypeLag) {
						inputs.set(indx + eLag + tLag + hLag + iLag, rs
								.getDouble("day_val"));
					}
					if (indx >= 0 & indx < holidayTypeLag) {
						inputs.set(indx + eLag + tLag + hLag + iLag
								+ dayTypeLag, rs.getDouble("holiday_val"));
					}
					++indx;
				}
				if (isAdd) {
					lst.add(new PatternPairVO(inputs, outpus, dtime));
				}
				manager.close(rs, ps);
			}
		} finally {
			manager.close(rs1, ps1);
		}
		return lst;
	}

	@SuppressWarnings("deprecation")
	public void saveToDataBase(Connection dbCon, List<DataVO> lst,
			Long signalNameId) throws SQLException {
		ConnectionManager manager = getManager(dbCon);
		PreparedStatement ps = null;
		String sql = "insert into signals (signal_name_id," + " dtime,"
				+ " season," + " dday," + " dhour," + " energy,"
				+ " temperature," + " insolation," + " humidity,"
				+ " day_type," + " holiday) " + "values(?,?,?,?,?,?,?,?,?,?,?)";
		try {
			for (DataVO d : lst) {
				ps = manager.getConnection().prepareStatement(sql);
				ps.setInt(1, signalNameId.intValue());
				ps.setTimestamp(2, new Timestamp(d.getTime().getTime()));
				ps.setShort(3, d.getTimeType());
				ps.setDate(4, new java.sql.Date(d.getTime().getTime()));
				ps.setInt(5, d.getTime().getHours() + 1);
				ps.setDouble(6, d.getEnergy());
				ps.setDouble(7, d.getTemperature());
				ps.setDouble(8, d.getInsolation());
				ps.setDouble(9, d.getHumidity());
				ps.setString(10, d.getDayType());
				ps.setString(11, d.getHoliday());
				ps.executeUpdate();
				ps.close();
			}
		} finally {
			manager.close(null, ps);
		}
	}

	@SuppressWarnings("deprecation")
	public void updateMean(Connection dbCon, List<DataVO> lst, Long signalNameId)
			throws SQLException {
		ConnectionManager manager = getManager(dbCon);
		PreparedStatement ps = null;
		String sql = "update signals set " + " e_mean_24 = ?,"
				+ " e_mean_168 = ?" + " where "
				+ " signal_name_id = ? and dtime = ?";
		try {
			for (DataVO d : lst) {
				ps = manager.getConnection().prepareStatement(sql);
				ps.setDouble(1, d.getEMean24());
				ps.setDouble(2, d.getEMean168());
				ps.setInt(3, signalNameId.intValue());
				ps.setTimestamp(4, new Timestamp(d.getTime().getTime()));
				ps.executeUpdate();
				ps.close();
			}
		} finally {
			manager.close(null, ps);
		}
	}

	public List<SignalNameVO> read(Connection dbCon, SignalNameVO query)
			throws SQLException {
		List<SignalNameVO> result = new ArrayList<SignalNameVO>();
		ConnectionManager manager = getManager(dbCon);
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("select * from "
				+ query.getTableName() + " where 1 = 1 ");
		List<String> mQuery = new ArrayList<String>();
		List<String> mField = new ArrayList<String>();
		if (query.getSignalNameId() != null
				&& query.getSignalNameId().longValue() > 0) {
			mQuery.add(query.getSignalNameId().toString());
			mField.add(query.getColumnNameForSignalNameId());
		}
		if (query.getName() != null) {
			mQuery.add(query.getName());
			mField.add(query.getColumnNameForName());
		}
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
			SignalNameVO sn = null;
			while (rs.next()) {
				sn = new SignalNameVO();
				sn.setSignalNameId(rs.getLong(query
						.getColumnNameForSignalNameId()));
				sn.setName(rs.getString(query.getColumnNameForName()));
				result.add(sn);
			}
		} finally {
			manager.close(rs, ps);
		}
		return result;
	}

	public Long create(Connection dbCon, SignalNameVO signalName)
			throws SQLException {
		Long id = null;
		ConnectionManager manager = getManager(dbCon);
		CallableStatement cs = null;
		StringBuilder sql = new StringBuilder(
				"{? = call signal_names_insert(?)}");
		try {
			cs = manager.getConnection().prepareCall(sql.toString());
			cs.registerOutParameter(1, Types.INTEGER);
			if (signalName.getName() != null) {
				cs.setString(2, signalName.getName());
			} else {
				cs.setNull(2, Types.VARCHAR);
			}
			cs.execute();
			id = (long) cs.getInt(1);
		} finally {
			manager.close(null, cs);
		}
		return id;

	}

}
