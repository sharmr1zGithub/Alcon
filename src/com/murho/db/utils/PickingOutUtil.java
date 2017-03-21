package com.murho.db.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.murho.dao.InvMstDAO;
import com.murho.dao.RsnMstDAO;
import com.murho.gates.DbBean;
import com.murho.tran.WmsPickingOut;
import com.murho.tran.WmsTran;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;

public class PickingOutUtil {
	XMLUtils _xmlUtils = null;

	StrUtils _strUtils = null;

	public PickingOutUtil() {
		_xmlUtils = new XMLUtils();
		_strUtils = new StrUtils();
	}

	public String isValidLoc(String aCompany, String aLoc) throws Exception {

		// MLogger.log(0, "isValidLoc() %%%%%%%%%%%%%%%%%%%%%%%% Starts");

		boolean isValid = false;
		String xmlStr = "";
		InvMstDAO invDao = new InvMstDAO();
		Hashtable ht = new Hashtable();
		ht.put(MDbConstant.COMPANY, aCompany);
		ht.put(MDbConstant.INV_LOC, aLoc);

		try {
			isValid = invDao.isExists(ht);
			if (isValid == true) {
				xmlStr = _xmlUtils.getXMLMessage(0, "Valid Loc ");
			} else {
				xmlStr = _xmlUtils.getXMLMessage(1, "Not a Valid Loc");
			}
			// MLogger.log(0, "is Valid Loc = " + isValid);
		} catch (Exception e) {
			MLogger.exception("isValidLoc()", e);
			throw e;
		}

		return xmlStr;
	}

	public String isValidMtid(String aCompany, String aLoc, String aMtid)
			throws Exception {

		// MLogger.log(0, "isValid Mtid() %%%%%%%%%%%%%%%%%%%%%%%% Starts");
		String xmlStr = "";
		boolean isValid = false;
		InvMstDAO invDao = new InvMstDAO();
		Hashtable ht = new Hashtable();
		ht.put(MDbConstant.COMPANY, aCompany);
		ht.put(MDbConstant.INV_LOC, aLoc);
		ht.put(MDbConstant.MTID, aMtid);

		try {
			isValid = invDao.isExists(ht);
			// MLogger.log(0, "is Valid Mtid = " + isValid);
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

	public String isValidSKU(String aCompany, String aLoc, String aMtid,
			String aSku) throws Exception {

		// MLogger.log(0, "isValidSKU() %%%%%%%%%%%%%%%%%%%%%%%% Starts");
		String xmlStr = "";
		boolean isValid = false;
		InvMstDAO invDao = new InvMstDAO();
		Hashtable ht = new Hashtable();
		ht.put(MDbConstant.COMPANY, aCompany);
		ht.put(MDbConstant.INV_LOC, aLoc);
		ht.put(MDbConstant.MTID, aMtid);
		ht.put(MDbConstant.ITEM, aSku);

		try {
			isValid = invDao.isExists(ht);
			// MLogger.log(0, "is Valid Sku = " + isValid);
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

	public String isValidLotNo(String aCompany, String aLoc, String aMtid,
			String aSku, String aLot) throws Exception {

		// MLogger.log(0, "isValidLotNo() %%%%%%%%%%%%%%%%%%%%%%%% Starts");
		String xmlStr = "";
		boolean isValid = false;
		InvMstDAO invDao = new InvMstDAO();
		Hashtable ht = new Hashtable();
		ht.put(MDbConstant.COMPANY, aCompany);
		ht.put(MDbConstant.INV_LOC, aLoc);
		ht.put(MDbConstant.MTID, aMtid);
		ht.put(MDbConstant.ITEM, aSku);
		ht.put(MDbConstant.LOT_NUM, aLot);

		try {
			isValid = invDao.isExists(ht);
			// MLogger.log(0, "is Valid Lno = " + isValid);
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

	public String getReasonCode(String COMPANY, String MODULE_NAME)
			throws Exception {
		// MLogger.log(1, this.getClass() + " getReasonCode()");
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
		// MLogger.log( -1, this.getClass() + " getReasonCode()");
		return xmlStr;
	}

	public String getMTIDdetails(String plant, String mtid) throws Exception {
		MLogger.log(1, this.getClass() + " getMTIDdetails()");
		String xmlStr = "";
		Map map = null;
		InvMstDAO invDao = new InvMstDAO();

		// query
		// Modified on 8-Apr-2014 for UDI implemetation for select Product 2D
		// flag and expiration date
		String query = "SKU,LOT,QTY,Product_2D,USERFLD1";
		// condition
		Hashtable ht = new Hashtable();
		// ht.put("PLANT",company);
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
				xmlStr = xmlStr
						+ _xmlUtils.getXMLNode("QTY", (String) map.get("QTY"));
				// Modified on 8-Apr-2014 for UDI implemetation for select
				// Product 2D flag and expiration date
				xmlStr = xmlStr
						+ _xmlUtils.getXMLNode("Product_2D", (String) map
								.get("Product_2D"));
				xmlStr = xmlStr
						+ _xmlUtils.getXMLNode("USERFLD1", (String) map
								.get("USERFLD1"));
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

	public String process_PickingOut(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.murho.gates.DbBean.getUserTranaction();
			ut.begin();

			flag = process_Wms_PickingOut(obj);
			MLogger.log(0, "After processing --> process_Wms_PickingOut :: "
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
			xmlStr = _xmlUtils.getXMLMessage(0, "Picking Out is successfull");
		} else {
			xmlStr = _xmlUtils.getXMLMessage(1, "Error in Picking Out");
		}
		return xmlStr;

	}

	private boolean process_Wms_PickingOut(Map map) throws Exception {
		MLogger.info(" process_Wms_PickingOut() starts");
		boolean flag = false;
		flag = true;
		WmsTran tran = new WmsPickingOut();
		flag = tran.processWmsTran(map);
		MLogger.info(" process_Wms_PickingOut() ends");
		return flag;
	}

	// added for lot restriction

	public String PickOut(String aLot) throws Exception {

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
					map
							.put(rs.getMetaData().getColumnLabel(i), rs
									.getString(i));
				}
			}
			MLogger.log(1, map.size() + " map size ");
			if (map.size() > 0) {
				XmlStr += _xmlUtils.getStartNode("PickOut");
				XmlStr += _xmlUtils.getXMLNode("STATUS", (String) map.get(
						"STATUS").toString());

				MLogger.log(1, XmlStr + "XMLSTR.........");
			}
			XmlStr += _xmlUtils.getEndNode("PickOut");
		} catch (Exception e) {
			MLogger.log(0, "PickOut() Failed .............................");
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
