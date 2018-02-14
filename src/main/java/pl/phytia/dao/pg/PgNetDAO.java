package pl.phytia.dao.pg;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pl.phytia.dao.ConnectionManager;
import pl.phytia.dao.NetDAO;
import pl.phytia.model.net.NetVO;

/**
 * Obiektu DAO odpowiedzialnego za obsługę tabeli <code>net</code> w bazie
 * PostgreSQL.
 * 
 * @author Jarosław Protasiewicz
 */
public class PgNetDAO extends PgBaseDAO implements NetDAO {

	public NetVO getNet(Connection dbCon, NetVO query) throws SQLException {
		NetVO net = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		sql.append("select id, dclass, dday, weights from net where 1 = 1");
		if (query.getId() > 0) {
			sql.append(" and id = " + query.getId());
		} else if (query.getDClass() != null
				&& query.getDClass().trim().length() > 0) {
			sql.append(" and dclass = " + query.getDClass());
		} else if (query.getDDay() != null) {
			sql.append(" and dday = " + query.getDDay());
		}
		ConnectionManager manager = getManager(dbCon);
		try {
			ps = manager.getConnection().prepareStatement(sql.toString());
			rs = ps.executeQuery();
			if (rs.next()) {
				net = new NetVO();
				net.setId(rs.getLong("id"));
				net.setDClass(rs.getString("dclass"));
				net.setDDay(rs.getDate("dday"));
				net.setWeights(rs.getString("weights"));
			}
		} finally {
			manager.close(rs, ps);
		}
		return net;
	}

	public void updateOrSave(Connection dbCon, NetVO net) throws SQLException {
		CallableStatement cs = null;
		StringBuilder sql = new StringBuilder("");
		if (net.getId() > 0) {
			sql.append("update net set dclass = " + net.getDClass());
			sql.append(", set dday = " + net.getDDay());
			sql.append(", set weights = " + net.getWeights());
			sql.append(" where id = " + net.getId());
		} else {
			sql.append("insert into net (dclass, dday, weights) values ");
			sql.append(net.getDClass());
			sql.append(", " + net.getDDay());
			sql.append(", " + net.getWeights());
		}
		ConnectionManager manager = getManager(dbCon);
		try {
			cs = manager.getConnection().prepareCall(sql.toString());
			cs.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close(null, cs);
		}
	}
}
