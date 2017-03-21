 /*  Author: Ranjana 
  *  Purpose: LOT restriction in PDA under ticket WO0000000284867 
  *           for the process of Lot Restriction Module
  *  Version: 1.0*/

package com.murho.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.gates.DbBean;
import com.murho.utils.MLogger;

public class RestrictLotDAO {

	public static final String TABLE_NAME = "tbl_LotRestriction";
	ResultSet rs = null;
	public boolean insertIntoLotRestriction(Hashtable ht) throws Exception {

		CallableStatement cs = null;
		MLogger.log(1, this.getClass() + " insertIntoLotRestriction()");
		boolean inserted = false;

		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			

			cs = con
					.prepareCall("{call dbo.[PROC_INSERT_LOTRESTRICTION ](?,?,?,?)}");
					cs.setString(1, (String) ht.get("LOT"));
					cs.setString(2, (String) ht.get("STATUS"));
					cs.setString(3, (String) ht.get("REASON"));
					cs.setString(4, (String) ht.get("LOGIN_USER"));

			rs = cs.executeQuery();
			inserted = true;
		} catch (Exception e) {
			MLogger
					.log("Exception :insertIntoLotRestriction ::"
							+ e.toString());
			throw e;
		} finally {
			if (con != null)
				DbBean.closeConnection(con);
		}

		MLogger.log(-1, getClass() + " insertIntoLotRestriction()");
		return inserted;
	}

	public boolean updateIntoLotRestriction(Hashtable ht) throws Exception {

		CallableStatement cs = null;
		MLogger.log(1, this.getClass() + " updateIntoLotRestriction()");
		boolean updated = false;

		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			cs = con
					.prepareCall("{call dbo.[PROC_INSERT_LOTRESTRICTION ](?,?,?,?)}");
			cs.setString(1, (String) ht.get("LOT"));
			cs.setString(2, (String) ht.get("STATUS"));
			cs.setString(3, (String) ht.get("REASON"));
			cs.setString(4, (String) ht.get("LOGIN_USER"));

			rs = cs.executeQuery();
			updated = true;

		} catch (Exception e) {
			MLogger
					.log("Exception :updateIntoLotRestriction ::"
							+ e.toString());
			throw e;
		} finally {
			if (con != null)
				DbBean.closeConnection(con);
		}

		MLogger.log(-1, getClass() + " updateIntoLotRestriction()");
		return updated;
	}

	public Map selectRow(String aLOT) throws Exception {
		
		MLogger.log(-1, aLOT + " value of lot passed");
		CallableStatement cs = null;
		ResultSet rs = null;
		Map map = new HashMap();
		MLogger.log(1, this.getClass() + " selectRow()");

		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			cs = con.prepareCall("{call dbo.[PROC_VIEWRESTRICTEDLOT ](?)}");
			cs.setString(1, aLOT);
			rs = cs.executeQuery();

			while (rs.next()) {		
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					MLogger.log(1, rs.getMetaData().getColumnLabel(i) + " selected KEY ");
					MLogger.log(1, rs.getString(i) + " selected value ");
					map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
				}
			}MLogger.log(1,  " size of the map" + map.size() );			
		} catch (Exception e) {
			MLogger.log("Exception :selectRow ::" + e.toString());
			throw e;
		} finally {
			if (con != null)
				DbBean.closeConnection(con);
		}
		MLogger.log(-1, getClass() + " selectRow()");
		return map;
	}

}
