package com.murho.db.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.HashMap;

import javax.transaction.UserTransaction;

import com.murho.dao.InvMstDAO;
import com.murho.dao.ItemMstDAO;
import com.murho.dao.RsnMstDAO;
import com.murho.gates.DbBean;
import com.murho.tran.WmsStockAdjustment;
import com.murho.tran.WmsTran;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;

public class StockAdjustmentUtil {
	XMLUtils _xmlUtils = null;

	StrUtils _strUtils = null;

	public StockAdjustmentUtil() {
		_xmlUtils = new XMLUtils();
		_strUtils = new StrUtils();
	}

	public String isValidMtid(String aCompany, String aMtid) throws Exception {

		String xmlStr = "";
		boolean isValid = false;
		InvMstDAO invDao = new InvMstDAO();
		Hashtable ht = new Hashtable();
		ht.put(MDbConstant.COMPANY, aCompany);
		ht.put(MDbConstant.MTID, aMtid);

		try {
			isValid = invDao.isExists(ht);

			if (isValid == true) {
				xmlStr = _xmlUtils.getXMLMessage(0, "Valid Mtid ");
			} else {
				xmlStr = _xmlUtils.getXMLMessage(1, "Not a Valid Mtid");
			}
		} catch (Exception e) {
			MLogger.exception("isValidMtid()", e);
			throw e;
		}

		return xmlStr;
	}

	public String isValidSKU(String aCompany, String aMtid, String aSku)
			throws Exception {

		String xmlStr = "";
		boolean isValid = false;
		InvMstDAO invDao = new InvMstDAO();
		Hashtable ht = new Hashtable();
		ht.put(MDbConstant.COMPANY, aCompany);
		ht.put(MDbConstant.MTID, aMtid);
		ht.put(MDbConstant.ITEM, aSku);
		//ht.put(MDbConstant.REFNO ,aSku);

		try {
			isValid = invDao.isExists(ht);
			if (isValid == true) {
				xmlStr = _xmlUtils.getXMLMessage(0, "Valid Sku ");
			} else {
				xmlStr = _xmlUtils.getXMLMessage(1, "Not a Valid Sku");
			}
		} catch (Exception e) {
			MLogger.exception("isValidSKU()", e);
			throw e;
		}

		return xmlStr;
	}

	public String isValidSKU1(String aCompany, String aMtid, String aSku)
			throws Exception {

		String xmlStr = "";
		boolean isValid = false;
		InvMstDAO invDao = new InvMstDAO();
		Hashtable ht = new Hashtable();
		ht.put(MDbConstant.COMPANY, aCompany);
		ht.put(MDbConstant.MTID, aMtid);
		//ht.put(MDbConstant.ITEM,aSku);
		ht.put(MDbConstant.REFNO, aSku);

		try {
			isValid = invDao.isExists(ht);
			if (isValid == true) {
				xmlStr = _xmlUtils.getXMLMessage(0, "Valid Sku ");
			} else {
				xmlStr = _xmlUtils.getXMLMessage(1, "Not a Valid Sku");
			}
		} catch (Exception e) {
			MLogger.exception("isValidSKU()", e);
			throw e;
		}

		return xmlStr;
	}

	public String isValidLotNo(String aCompany, String aMtid, String aSku,
			String aLot) throws Exception {

		String xmlStr = "";
		boolean isValid = false;
		InvMstDAO invDao = new InvMstDAO();
		Hashtable ht = new Hashtable();
		ht.put(MDbConstant.COMPANY, aCompany);
		ht.put(MDbConstant.MTID, aMtid);
		ht.put(MDbConstant.ITEM, aSku);
		ht.put(MDbConstant.LOT_NUM, aLot);

		try {
			isValid = invDao.isExists(ht);
			if (isValid == true) {
				xmlStr = _xmlUtils.getXMLMessage(0, "Valid LotNo ");
			} else {
				xmlStr = _xmlUtils.getXMLMessage(1, "Not a Valid LotNo");
			}
		} catch (Exception e) {
			MLogger.exception("isValidLotNo()", e);
			throw e;
		}

		return xmlStr;
	}

	public String isValidLoc(String aCompany, String aLoc) throws Exception {

		boolean isValid = false;
		String xmlStr = "";
		ItemMstDAO itemDao = new ItemMstDAO();
		Hashtable ht = new Hashtable();
		ht.put(MDbConstant.COMPANY, aCompany);
		ht.put(MDbConstant.INV_LOC, aLoc);

		try {
			isValid = itemDao.isExists(ht);
			if (isValid == true) {
				xmlStr = _xmlUtils.getXMLMessage(0, "Valid Loc ");
			} else {
				xmlStr = _xmlUtils.getXMLMessage(1, "Not a Valid Loc");
			}
		} catch (Exception e) {
			MLogger.exception("isValidLoc()", e);
			throw e;
		}

		return xmlStr;
	}

	public String getReasonCode(String COMPANY, String MODULE_NAME)
			throws Exception {
		String xmlStr = "";
		ArrayList al = null;
		RsnMstDAO dao = new RsnMstDAO();
		MLogger.info("Input Values");
		MLogger.info("COMPANY = " + COMPANY);
		MLogger.info("MODULE_NAME = " + MODULE_NAME);
		//
		try {
			al = dao.getReasonCode(COMPANY, MODULE_NAME);
			MLogger.log(0, "Record size() :: " + al.size());
			if (al.size() > 0) {
				xmlStr += _xmlUtils.getXMLHeader();
				xmlStr += _xmlUtils.getStartNode("reasonList total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += _xmlUtils.getStartNode("record");
					xmlStr += _xmlUtils.getXMLNode("code", (String) map
							.get("rsnCode"));
					xmlStr += _xmlUtils.getXMLNode("description", (String) map
							.get("rsnDesc"));
					xmlStr += _xmlUtils.getEndNode("record");
				}
				xmlStr += _xmlUtils.getEndNode("reasonList");
			}

		} catch (Exception e) {
			MLogger.exception("getReasonCode()", e);
			throw e;
		}
		return xmlStr;
	}

	public String getMTIDdetails(String plant, String mtid) throws Exception {
		MLogger.log(1, this.getClass() + " getMTIDdetails()");
		String xmlStr = "";
		Map map = null;
		InvMstDAO invDao = new InvMstDAO();

		//query
		String query = "SKU,LOT";
		//condition
		Hashtable ht = new Hashtable();
		ht.put("PLANT", plant);
		ht.put("MTID", mtid);

		xmlStr = _xmlUtils.getXMLHeader();
		xmlStr = xmlStr + _xmlUtils.getStartNode("PickDetails");
		try {
			map = invDao.selectRow(query, ht);
			MLogger.info("Record size() :: " + map.size());
			if (map.size() > 0) {

				xmlStr = xmlStr + _xmlUtils.getXMLNode("status", "0");
				xmlStr = xmlStr + _xmlUtils.getXMLNode("description", "");
				xmlStr = xmlStr
						+ _xmlUtils.getXMLNode("SKU", (String) map.get("SKU"));
				xmlStr = xmlStr
						+ _xmlUtils.getXMLNode("LOT", (String) map.get("LOT"));
			} else {
				throw new Exception("Invalid MTID :" + mtid);
			}

			xmlStr = xmlStr + _xmlUtils.getEndNode("PickDetails");
			MLogger.log(0, "Value of xml : " + xmlStr);
		} catch (Exception e) {
			MLogger.exception("getValidLocation()", e);
			throw e;
		}
		MLogger.log(-1, this.getClass() + " getMTIDdetails()");
		return xmlStr;
	}

	public String process_StockAdjustment(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.murho.gates.DbBean.getUserTranaction();
			ut.begin();
			flag = process_Wms_StockAdjustment(obj);
			MLogger.log(0,
					"After processing --> process_Wms_StockAdjustment :: "
							+ flag);

			if (flag == true) {
				DbBean.CommitTran(ut);
				flag = true;
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			throw e;
		}

		String xmlStr = "";
		if (flag == true) {
			xmlStr = _xmlUtils.getXMLMessage(0, "Blind Receipt is successfull");
		} else {
			xmlStr = _xmlUtils.getXMLMessage(1, "Error in Blind Receipt");
		}
		return xmlStr;

	}

	private boolean process_Wms_StockAdjustment(Map map) throws Exception {
		MLogger.info(" process_Wms_StockAdjustment() starts");
		boolean flag = false;
		flag = true;
		WmsTran tran = new WmsStockAdjustment();
		flag = tran.processWmsTran(map);
		MLogger.info(" process_Wms_StockAdjustment() ends");
		return flag;
	}

	//added for lot restriction 

	public String BlindReceipt(String aLot) throws Exception {

		String XmlStr = "";
		Connection con = null;
		Map map = new HashMap();
		CallableStatement cs = null;
		ResultSet rs = null;
		XmlStr = _xmlUtils.getXMLHeader();
		try {
			MLogger.log(0, "dbo.PROC_CHECK_LOTRESTRICTION_INBOUND_UPLOAD");

			con = DbBean.getConnection();
			cs = con
					.prepareCall("{call dbo.[PROC_CHECK_LOTRESTRICTION_INBOUND_UPLOAD](?)}");
			cs.setString(1, aLot);

			rs = cs.executeQuery();

			while (rs.next()) {
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					MLogger.log(1, rs.getMetaData().getColumnLabel(i)
							+ " selected KEY ");
					MLogger.log(1, rs.getString(i) + " selected value ");
					map
							.put(rs.getMetaData().getColumnLabel(i), rs
									.getString(i));
				}
			}
			MLogger.log(1, map.size() + " map size ");
			if (map.size() > 0) {
				XmlStr += _xmlUtils.getStartNode("BlindReceipt");
				XmlStr += _xmlUtils.getXMLNode("STATUS", (String) map.get(
						"STATUS").toString());

				MLogger.log(1, XmlStr + "XMLSTR.........");
			}
			XmlStr += _xmlUtils.getEndNode("BlindReceipt");
		} catch (Exception e) {
			MLogger.log(0,
					"BlindReceipt() Failed .............................");
			throw e;
		}finally{
		    if (rs != null) {
		        rs.close();
		     }
		     if (con != null) {
		        DbBean.closeConnection(con);
		      }
		     }
		return XmlStr;

	}

}
