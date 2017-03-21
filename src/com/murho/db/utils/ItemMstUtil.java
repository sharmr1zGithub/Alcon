package com.murho.db.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.BaseDAO;
import com.murho.dao.ItemMstDAO;
import com.murho.dao.PrdClsDAO;
import com.murho.dao.RsnMstDAO;
import com.murho.gates.DbBean;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.XMLUtils;

public class ItemMstUtil {
	com.murho.utils.XMLUtils xu = null;

	public ItemMstUtil() {
		xu = new XMLUtils();
	}

	public boolean isExistsItemId(Hashtable htLoc) throws Exception {

		MLogger.log(0, "isExistsItemId()  %%%%%%%%%%%%%%%%%%%%%%%%  Starts");

		boolean isExists = false;
		ItemMstDAO itemDao = new ItemMstDAO();
		try {
			isExists = itemDao.isExists(htLoc);
			MLogger.log(0, "isExistsItemId = " + isExists);
		} catch (Exception e) {
			MLogger.exception("isExistsItemId()", e);
			throw e;
		}

		return isExists;
	}

	public boolean insertItemMst(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {

			ItemMstDAO itemDao = new ItemMstDAO();
			inserted = itemDao.insertIntoItemMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	public boolean deleteItemId(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			ItemMstDAO dao = new ItemMstDAO();
			deleted = dao.deleteItemId(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}

	public boolean updateItemId(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		try {
			ItemMstDAO dao = new ItemMstDAO();
			update = dao.updateItemId(htUpdate, htCondition);

		} catch (Exception e) {
			throw e;
		}
		return update;
	}

	public ArrayList getReasonMstDetails(String aItemId) {
		//   MLogger.log(1, this.getClass() + " getItemListStartsWith()");

		ArrayList al = null;
		RsnMstDAO dao = new RsnMstDAO();

		MLogger.info("Input Values");

		try {
			al = dao.getReasonMstDetails(aItemId);
			MLogger.log(0, "Record size() :: " + al.size());
		} catch (Exception e) {
			MLogger.exception(this, e);
		}
		//  MLogger.log( -1, this.getClass() + " getItemListStartsWith()");
		return al;
	}

	public Map getItemDetails(String aItemId) throws Exception {
		// MLogger.log(1, this.getClass() + " getItemDetails()");
		ItemMstDAO dao = new ItemMstDAO();
		Hashtable ht = new Hashtable();
		ht.put(MDbConstant.ITEMMST_ITEM, aItemId);
		Map map = new HashMap();
		try {

			//Commented for implementing the UDI changes. 
			//String sql= " ITEM,ITEMDESC,PRD_CLS_ID as CLASSID,isnull(STKUOM,'') AS STKUOM,ISNULL(REFNO,'') AS REFNO ";

			//Added By Ranjana Sharma on 22/05/2015 WO0000000471852 -UDI Part 2 Implementation
			String sql = " ITEM,ITEMDESC,PRD_CLS_ID as CLASSID,isnull(STKUOM,'') AS STKUOM,ISNULL(REFNO,'') AS REFNO,ISNULL(GTIN2,'') AS GTIN2 ";

			map = dao.selectRow(sql, ht);
		} catch (Exception e) {
			MLogger.exception("getLocAsignedRuleDetails :: Exception :", e);
			throw e;
		}
		return map;

		// MLogger.log(-1, this.getClass() + " getOnHandQty()");

	}

	public ArrayList getItemListStartsWith(String aItemId) {
		//   MLogger.log(1, this.getClass() + " getItemListStartsWith()");

		ArrayList al = null;
		ItemMstDAO dao = new ItemMstDAO();

		MLogger.info("Input Values");

		try {
			al = dao.getItemIdStartsWith(aItemId);
			MLogger.log(0, "Record size() :: " + al.size());
		} catch (Exception e) {
			MLogger.exception(this, e);
		}
		//  MLogger.log( -1, this.getClass() + " getItemListStartsWith()");
		return al;
	}

	public String getProductGroup(String sku) throws Exception {
		MLogger.log(1, this.getClass() + " getProductGroup()");
		String _productClass = "";
		String _productGroup = "";
		try {
			String _prodClass = getProductClass(sku);

			String query = " " + " prd_grp_id " + " ";
			Hashtable ht = new Hashtable();

			ht.put("prd_cls_id", _prodClass);

			PrdClsDAO dao = new PrdClsDAO();
			Map m = dao.selectRow(query, ht);

			_productGroup = (String) m.get("prd_grp_id");

			if (_productGroup == "" || _productGroup == null) {
				_productGroup = "";
			}
			MLogger.info("getProductGroup() : ProductGroup = " + _productGroup);
		} catch (Exception e) {
			MLogger
					.log(
							0,
							"######################### Exception :: getProductGroup() : ######################### \n");
			MLogger.log(0, "" + e.getMessage());
			MLogger
					.log(
							0,
							"######################### Exception :: getProductGroup() : ######################### \n");
			MLogger.log(-1, "");
			throw e;
		}
		MLogger.log(-1, this.getClass() + " getProductGroup()");
		return _productGroup;
	}

	public String getProductClass(String sku) throws Exception {
		MLogger.log(1, this.getClass() + " getProductClass()");
		String _productClass = "";
		try {
			String query = " " + " prd_cls_id " + " ";
			Hashtable ht = new Hashtable();

			//  ht.put("PLANT",company);
			ht.put("ITEM", sku);

			ItemMstDAO dao = new ItemMstDAO();
			Map m = dao.selectRow(query, ht);

			_productClass = (String) m.get("prd_cls_id");

			if (_productClass == "" || _productClass == null) {
				_productClass = "";
			}
			MLogger.info("getProductClass() : TravellerNO = " + _productClass);
		} catch (Exception e) {
			MLogger
					.log(
							0,
							"######################### Exception :: getProductClass() : ######################### \n");
			MLogger.log(0, "" + e.getMessage());
			MLogger
					.log(
							0,
							"######################### Exception :: getProductClass() : ######################### \n");
			MLogger.log(-1, "");
			throw e;
		}
		MLogger.log(-1, this.getClass() + " getProductClass()");
		return _productClass;
	}

	public String getSpcacePerTray(String sku) throws Exception {
		MLogger.log(1, this.getClass() + " getSpcacePerTray()");
		String _spacePerTray = "";
		java.sql.Connection con = null;
		try {
			StringBuffer sbQuery = new StringBuffer("");

			sbQuery
					.append("SELECT pcnt_space_per_tray FROM PRD_GROUP_MST where prd_grp_id=");
			sbQuery
					.append("(SELECT prd_grp_id FROM PRD_CLASS_MST WHERE PRD_CLS_ID=");
			sbQuery.append("(SELECT PRD_CLS_ID FROM ITEMMST where item='" + sku
					+ "'))");

			/*sbQuery.append("SELECT pcnt_space_per_tray FROM PRD_GROUP_MST where prd_grp_id=");
			 sbQuery.append("(SELECT prd_grp_id FROM PRD_CLASS_MST WHERE PRD_CLS_ID=");
			 sbQuery.append("(SELECT PRD_CLS_ID FROM ITEMMST where REFNO='"+sku+"'))");*/

			con = com.murho.gates.DbBean.getConnection();

			MLogger.query(" " + sbQuery.toString());
			Map m = new BaseDAO().getRowOfData(con, sbQuery.toString());

			_spacePerTray = (String) m.get("pcnt_space_per_tray");

			if (_spacePerTray == "" || _spacePerTray == null) {
				_spacePerTray = "";
			}
			MLogger.info("getSpcacePerTray() : SpacePerTray= " + _spacePerTray);
		} catch (Exception e) {
			MLogger
					.log(
							0,
							"######################### Exception :: getSpcacePerTray() : ######################### \n");
			MLogger.log(0, "" + e.getMessage());
			MLogger
					.log(
							0,
							"######################### Exception :: getSpcacePerTray() : ######################### \n");
			MLogger.log(-1, "");
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		MLogger.log(-1, this.getClass() + " getSpcacePerTray()");
		return _spacePerTray;
	}

	public String GetSKUForRefNo(Hashtable ht) throws Exception {
		ItemMstDAO dao = new ItemMstDAO();
		String xmlStr = "";
		boolean isValid = false;
		try {
			String sQry = " ITEM ";
			Map map = dao.selectRow(sQry, ht);

			if (map.size() > 0) {
				xmlStr = xu.getXMLMessage(0, (String) map.get("ITEM"));
			} else {
				xmlStr = xu.getXMLMessage(1,
						" Please Scan a Valid SKU/UPC Code");
			}

			MLogger.log(0, " GetSKUForRefNo = " + xmlStr);
		} catch (Exception e) {
			MLogger.exception("GetSKUForRefNo()", e);

			throw e;
		}
		return xmlStr;
	}

}