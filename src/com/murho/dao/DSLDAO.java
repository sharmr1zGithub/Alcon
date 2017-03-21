/* Created By: Ranjana
 * Purpose: To implement the functionality of DSL
 * under ticket WO0000001220930
 */

package com.murho.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.gates.DbBean;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;

public class DSLDAO {
	
	ResultSet rs = null;
	public int insertIntoDsl(Hashtable ht) throws Exception {

		CallableStatement cs = null;
		MLogger.log(1, this.getClass() + " insertIntoDsl()");
		int inserted = 0;
		String output = "";

		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			cs = con
					.prepareCall("{call dbo.[PROC_DSL_STATUS](?,?,?,?,?)}");
			cs.setString(1, (String) ht.get("COUNTRY"));
			cs.setString(2, (String) ht.get("PRD_CLS_ID"));
			cs.setString(3, (String) ht.get("STATUS"));
			cs.setString(4, (String) ht.get("LOGIN_USER"));
			cs.setString(5, (String)("INSERT"));

			rs = cs.executeQuery();
			
			while (rs.next()) {
				output = rs.getString("OUTPUT");
			}
			if (output.equals("PRD_CLS_ID")) {
				inserted = 1;
			} else if (output.equals("DESTINATION")) {
				inserted = 2;
			} else {
				inserted = 0;
			}

		} catch (Exception e) {
			MLogger
					.log("Exception :insertIntoDsl ::"
							+ e.toString());
			throw e;
		} finally {
			if (con != null)
				DbBean.closeConnection(con);
		}

		MLogger.log(-1, getClass() + " insertIntoDsl()");
		return inserted;
	}

	public boolean updateIntoDsl(Hashtable ht) throws Exception {
		CallableStatement cs = null;
		MLogger.log(1, this.getClass() + " updateIntoDsl()");
		boolean updated = false;

		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			cs = con
					.prepareCall("{call dbo.[PROC_DSL_STATUS](?,?,?,?,?)}");
			cs.setString(1, (String) ht.get("COUNTRY"));
			cs.setString(2, (String) ht.get("PRD_CLS_ID"));
			cs.setString(3, (String) ht.get("STATUS"));
			cs.setString(4, (String) ht.get("LOGIN_USER"));
			cs.setString(5, (String)("UPDATE"));

			rs = cs.executeQuery();
			updated = true;

		} catch (Exception e) {
			MLogger
					.log("Exception :updateIntoDsl ::"
							+ e.toString());
			throw e;
		} finally {
			if (con != null)
				DbBean.closeConnection(con);
		}

		MLogger.log(-1, getClass() + " updateIntoDsl()");
		return updated;
	}

	public Map selectRowForDsl(String sDestination ,String sPrdClsId) throws Exception {
		java.sql.Connection con = null;
		Map map = new HashMap();
		ItemMstDAO dao = new ItemMstDAO();

		try {
			Hashtable ht = new Hashtable();
			ht.put(MDbConstant.COUNTRYNAME, sDestination);
			ht.put(MDbConstant.PRD_CLS_ID, sPrdClsId);
			
			String sql = "ISNULL(COUNTRYNAME,'') AS COUNTRYNAME, ISNULL(PRD_CLS_ID,'') AS PRD_CLS_ID, ISNULL(DSL_STATUS,'') AS STATUS ";

			MLogger.log(0, "selectRowForDsl(aQuery)::" + sql);

			map = dao.selectcountryRow(sql, ht);
		} catch (Exception e) {
			MLogger.exception("selectRowForDsl :: Exception :", e);
			throw e;
		} finally {
			if (con != null)
				DbBean.closeConnection(con);
		}
		return map;
	}

}
