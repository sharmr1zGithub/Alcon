package com.murho.pda;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.murho.dao.InvMstDAO;
import com.murho.dao.LocMstDAO;
import com.murho.db.utils.PickingOutUtil;
import com.murho.db.utils.PutAwayUtil;
import com.murho.utils.DateUtils;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;

/**
 * <p>Title: Picking Out using PDA</p>
 * <p>Description: This servlet is used for Stock adjustment using PDA</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Murho</p>
 * @author not attributable
 * @version 1.0
 */

public class PickingOut extends HttpServlet {

	Context ctx = null;

	XMLUtils xu = new XMLUtils();

	MLogger logger = new MLogger();

	StrUtils strUtils = new StrUtils();

	PickingOutUtil _pOutUtil = null;

	InvMstDAO _InvMstDAO = null;

	LocMstDAO _LocMstDAO = null;

	String PLANT = "", USERNAME = "", BINNO = "", COMPANY = "";

	String xmlStr = "";

	String action = "";

	private static final String CONTENT_TYPE = "text/xml";

	public void init() throws ServletException {

		_InvMstDAO = new InvMstDAO();
		_LocMstDAO = new LocMstDAO();
		_pOutUtil = new PickingOutUtil();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		MLogger.log(1, "_______________________ " + this.getClass()
				+ " doGet()");
		try {
			action = request.getParameter("action").trim();
			MLogger.log(0, "------------------ Action = " + action
					+ " ----------------- **** ");

			if (action.equalsIgnoreCase("validateLoc")) {
				xmlStr = "";
				xmlStr = isValidLoc(request, response);
			}

			if (action.equalsIgnoreCase("validateSku")) {
				xmlStr = "";
				xmlStr = isValidSku(request, response);
			}
			if (action.equalsIgnoreCase("validateMTID")) {
				xmlStr = "";
				xmlStr = isValidMtid(request, response);
			}
			if (action.equalsIgnoreCase("validateLot")) {
				xmlStr = "";
				xmlStr = isValidLotNo(request, response);
			}
			if (action.equalsIgnoreCase("load_reason_codes")) {
				xmlStr = "";
				xmlStr = load_Reason_Codes(request, response);
			}
			if (action.equalsIgnoreCase("Load_MTID_Details")) {
				xmlStr = "";
				xmlStr = LoadMTIDDetails(request, response);
			}
			if (action.equalsIgnoreCase("process_PickingOut")) {
				xmlStr = "";
				xmlStr = process_PickingOut(request, response);

			}
			
			/*Added by Ranjana
			 * Purpose: To check the restricted lot at the time of pick Out*/ 

			if (action.equalsIgnoreCase("PickOut")) {
				xmlStr = "";
				xmlStr = PickOut(request, response);
			}

		} catch (Exception e) {
			MLogger.exception(" Exception :: doGet() : ", e);
			xmlStr = xu
					.getXMLMessage(1, "Unable to process? " + e.getMessage());
		}
		MLogger.log(0, "XML :" + xmlStr);
		MLogger.log(-1, "_______________________ " + this.getClass()
				+ " doGet()");
		out.write(xmlStr);
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

	private String isValidLoc(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", plant = "", loc = "";
		try {

			plant = strUtils.fString(request.getParameter("PLANT"));
			loc = strUtils.fString(request.getParameter("LOC"));
			str = _pOutUtil.isValidLoc(plant, loc);
		} catch (Exception e) {
			MLogger.exception(this, e);
			;
			throw e;
		}
		return str;
	}

	private String isValidMtid(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", plant = "", loc = "", mtid;
		try {

			plant = strUtils.fString(request.getParameter("PLANT"));
			loc = strUtils.fString(request.getParameter("LOC"));
			mtid = strUtils.fString(request.getParameter("MTID"));
			str = _pOutUtil.isValidMtid(plant, loc, mtid);
		} catch (Exception e) {
			MLogger.exception(this, e);
			;

			throw e;
		}
		return str;
	}

	private String isValidSku(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", plant = "", loc = "", mtid, sku;
		try {

			plant = strUtils.fString(request.getParameter("PLANT"));
			loc = strUtils.fString(request.getParameter("LOC"));
			mtid = strUtils.fString(request.getParameter("MTID"));
			sku = strUtils.fString(request.getParameter("SKU"));

			str = _pOutUtil.isValidSKU(plant, loc, mtid, sku);
		} catch (Exception e) {
			MLogger.exception(this, e);
			;
			throw e;
		}
		return str;
	}

	private String isValidLotNo(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", plant = "", loc = "", mtid, sku, lot;
		try {

			plant = strUtils.fString(request.getParameter("PLANT"));
			loc = strUtils.fString(request.getParameter("LOC"));
			mtid = strUtils.fString(request.getParameter("MTID"));
			sku = strUtils.fString(request.getParameter("SKU"));
			lot = strUtils.fString(request.getParameter("LOT"));

			str = _pOutUtil.isValidLotNo(plant, loc, mtid, sku, lot);
		} catch (Exception e) {
			MLogger.exception(this, e);
			;
			throw e;
		}
		return str;
	}

	private String load_Reason_Codes(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";
		String MODULE_NAME = "";
		try {

			PLANT = strUtils.fString(request.getParameter("PLANT"));
			MODULE_NAME = strUtils.fString(request.getParameter("MODULE_NAME"));
			str = _pOutUtil.getReasonCode(PLANT, MODULE_NAME);
			if (str.equalsIgnoreCase("")) {
				str = xu.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			MLogger
					.exception(
							" %%%%%%%%%%%%%%%%%%%%%%%%% Exception :: load_Reason_Codes() : ",
							e);
			throw e;
		}
		return str;
	}

	private String LoadMTIDDetails(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", plant = "", travelerNum = "", pallet = "", MTID = "", loginUser = "";

		try {

			plant = strUtils.fString(request.getParameter("PLANT"));
			loginUser = strUtils.fString(request.getParameter("LOGIN_USER"));
			MTID = strUtils.fString(request.getParameter("MTID"));

			MLogger.printInput("Plant       : " + plant);
			MLogger.printInput("Login user  : " + loginUser);
			MLogger.printInput("MTID        : " + MTID);

			str = _pOutUtil.getMTIDdetails(plant, MTID);

		} catch (Exception e) {
			MLogger.exception(this, e);
			;

			throw e;
		}
		return str;
	}

	private String process_PickingOut(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		MLogger.log(0, "process_PickingOut() Starts");

		Map pickOut_HM = null;
		String PLANT = "", MTID = "", SKU = "", LOC = "", LOT = "", QTY = "", LOGIN_USER = "", RESCODE = "", RESDESC = "", tranDate = "", tranTime = "";

		tranDate = DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
		tranTime = DateUtils.Time();
		try {
			PLANT = strUtils.fString(request.getParameter("PLANT"));
			MTID = strUtils.fString(request.getParameter("MTID"));
			SKU = strUtils.fString(request.getParameter("SKU"));
			LOC = strUtils.fString(request.getParameter("LOC"));
			LOT = strUtils.fString(request.getParameter("LOT"));
			QTY = strUtils.fString(request.getParameter("QTY"));
			RESCODE = strUtils.fString(request.getParameter("RESCODE"));
			RESDESC = strUtils.fString(request.getParameter("RESDESC"));
			LOGIN_USER = strUtils.fString(request.getParameter("LOGIN_USER"));

			pickOut_HM = new HashMap();
			pickOut_HM.put(MDbConstant.PLANT, PLANT);
			pickOut_HM.put(MDbConstant.LOC, LOC);
			pickOut_HM.put(MDbConstant.MTID, MTID);
			pickOut_HM.put(MDbConstant.ITEM, SKU);
			pickOut_HM.put(MDbConstant.LOT_NUM, LOT);
			pickOut_HM.put(MDbConstant.QTY, QTY);
			pickOut_HM.put("USERFLD5", RESCODE);
			pickOut_HM.put("USERFLD6", RESDESC);
			pickOut_HM.put("CRAT", tranDate);
			pickOut_HM.put("CRTIME", tranTime);
			pickOut_HM.put(MDbConstant.LOGIN_USER, LOGIN_USER);

			MLogger.log(0, "Input Starts");
			MLogger.log(0, "PLANT               : "
					+ pickOut_HM.get(MDbConstant.PLANT));
			MLogger.log(0, "MTID                : "
					+ pickOut_HM.get(MDbConstant.MTID));
			MLogger.log(0, "LOC                 : "
					+ pickOut_HM.get(MDbConstant.LOC));
			MLogger.log(0, "SKU                 : "
					+ pickOut_HM.get(MDbConstant.ITEM));
			MLogger.log(0, "LOT                 : "
					+ pickOut_HM.get(MDbConstant.LOT_NUM));
			MLogger.log(0, "QTY                 : "
					+ pickOut_HM.get(MDbConstant.QTY));
			MLogger.log(0, "LOGIN_USER          : "
					+ pickOut_HM.get(MDbConstant.LOGIN_USER));
			MLogger.log(0, "RESCODE             : "
					+ pickOut_HM.get("USERFLD5"));
			MLogger.log(0, "RESDESC             : "
					+ pickOut_HM.get("USERFLD6"));

			xmlStr = "";
			xmlStr = _pOutUtil.process_PickingOut(pickOut_HM);

		} catch (Exception e) {
			MLogger.exception(this, e);
			throw new Exception("Unable to do Picking Out() Exception : "
					+ e.getMessage());
		}
		return xmlStr;
	}


	/*Added by Ranjana
	 * Purpose: To check the restricted lot at the time of pick Out*/ 

	private String PickOut(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String xmlStr = "", LOT = "";
		PutAwayUtil pwUtil = null;
		pwUtil = new PutAwayUtil();
		try {
			LOT = strUtils.fString(request.getParameter("LOT"));

			xmlStr = _pOutUtil.PickOut(LOT);

			MLogger.info("************* " + xmlStr);
		} catch (Exception e) {
			MLogger.log(0,
					"Blindreceipt() Failed .............................");
			MLogger.exception(this, e);
			throw e;
		}
		MLogger.log(0, "Blindreceipt() Ends .............................");
		return xmlStr;
	}

	private static void sop(String str) {
		System.out.println(str);
	}
} //end of class
