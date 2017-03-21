/* Created By: Ranjana
 * Purpose: To implement the functionality of System Blocking Master
 * under ticket WO0000001221106
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

public class DestinationLotBlockDAO  {

	public static final String TABLE_NAME = "TBL_BLOCK_LOT";
	ResultSet rs = null;
	public int insertIntoDestLotBlock(Hashtable ht) throws Exception {

		CallableStatement cs = null;
		MLogger.log(1, this.getClass() + " insertIntoDestLotBlock()");
		int inserted = 0;
		String output ="";
		
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			cs = con
					.prepareCall("{call dbo.[PROC_BLOCK_LOT](?,?,?,?,?,?)}");
			cs.setString(1, (String) ht.get("SHIP_TO"));
			cs.setString(2, (String) ht.get("PRD_CLS_ID"));
			
			//6.0 ENHANCEMENT
			cs.setString(3, (String) ht.get("SKU"));
			
			cs.setString(4, (String) ht.get("LOT_START_WITH"));
			cs.setString(5, (String) ht.get("LOGIN_USER"));
			cs.setString(6, ("INSERT"));

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
					.log("Exception :insertIntoDestLotBlock ::"
							+ e.toString());
			throw e;
		} finally {
			if (con != null)
				DbBean.closeConnection(con);
		}

		MLogger.log(-1, getClass() + " insertIntoDestLotBlock()");
		return inserted;
	}

	public Map selectRowForDest(String aDestination, String aPrdClsId) throws Exception {

		MLogger.log(-1, aPrdClsId + " value of Destination passed");
		java.sql.Connection con = null;
		Map map = new HashMap();
		ItemMstDAO dao = new ItemMstDAO();

		try {
			Hashtable ht = new Hashtable();
			ht.put(MDbConstant.DESTINATIONCODE, aDestination);
			ht.put(MDbConstant.PRD_CLS_ID, aPrdClsId);
			
			String sql = "ISNULL(DESTINATIONCODE,'') AS DESTINATION_CODE, ISNULL(PRD_CLS_ID,'') AS PRDCLSID," +
					"ISNULL(LOTSTARTWITH,'') AS LOT_START_WITH, ISNULL(SKU,'') AS SKU ";

			MLogger.log(0, "selectRowForDest(aQuery)::" + sql);

			map = dao.selectdestRow(sql, ht);
		} catch (Exception e) {
			MLogger.exception("selectRowForDest :: Exception :", e);
			throw e;
		} finally {
			if (con != null)
				DbBean.closeConnection(con);
		}
		return map;
	}

	public boolean deleteDestLotBlock(Hashtable ht) throws Exception{

		CallableStatement cs = null;
		MLogger.log(1, this.getClass() + " deleteDestLotBlock()");
		boolean inserted = false;

		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			cs = con
					.prepareCall("{call dbo.[PROC_BLOCK_LOT_DELETE](?,?)}");
			cs.setString(1, (String) ht.get("SHIP_TO"));
			cs.setString(2, (String) ht.get("PRD_CLS_ID"));
			
			rs = cs.executeQuery();
			inserted = true;

		} catch (Exception e) {
			MLogger
					.log("Exception :deleteDestLotBlock ::"
							+ e.toString());
			throw e;
		} finally {
			if (con != null)
				DbBean.closeConnection(con);
		}

		MLogger.log(-1, getClass() + " deleteDestLotBlock()");
		return inserted;
	}
}

