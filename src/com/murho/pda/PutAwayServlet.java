

package com.murho.pda;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import com.murho.dao.InvMstDAO;
import com.murho.dao.LocationAssigningRulesDAO;
import com.murho.db.utils.InvMstUtil;
import com.murho.db.utils.ItemMstUtil;
import com.murho.db.utils.MovHisUtil;
import com.murho.db.utils.PutAwayUtil;
import com.murho.gates.Generator;
import com.murho.utils.DateUtils;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;


public class PutAwayServlet extends HttpServlet{


Context ctx = null;
XMLUtils xu = new XMLUtils();
MLogger logger = new MLogger();
StrUtils strUtils = new StrUtils();
Generator generator = new Generator();

String plant = "";

public static final boolean DEBUG = true;

String xmlStr = "";
String action = "";
 InvMstUtil invutil = null;
 PutAwayUtil pwUtil =null;
 MovHisUtil movUtil =null;
private static final String CONTENT_TYPE = "text/xml";

public void init() throws ServletException {

invutil      = new InvMstUtil();
pwUtil       = new PutAwayUtil();
movUtil       = new MovHisUtil();
}

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();

    try {
    action = request.getParameter("action").trim();
    MLogger.log(1, "_______________________ " + this.getClass() + " doGet()");
    MLogger.log(0," Action = " + action );

    }
    catch (Exception ex) { MLogger.exception(this, ex);
    }

    try {
        if (action.equalsIgnoreCase("Load_Details_For_PutAway")) {
        xmlStr = "";
        xmlStr = Load_Details_For_PutAway(request, response);

      }else if (action.equalsIgnoreCase("Process_PutAway")) {
        xmlStr = "";
        xmlStr = processPutAway(request, response);

      }
        
        /*  Added by Ranjana 
         *  Purpose: LOT restriction in PDA under ticket WO0000000284867 
         *  for the process of PUTAWAY*/
        
      else if (action.equalsIgnoreCase("Lot_Blocked_PutAway")) {
          xmlStr = "";
          xmlStr = Lot_Blocked_PutAway(request, response);

        }
    }
    catch (Exception e) {
        MLogger.exception(" Exception :: doGet() : ", e);
        xmlStr = xu.getXMLMessage(1, e.getMessage());
    }

    if (xmlStr.equalsIgnoreCase("")) {
      xmlStr = xu.getXMLMessage(1,"Some inputs were missing,Please check once again!!!");
    }
    
    
    out.write(xmlStr);
    out.close();
  }

 

public void doPost(HttpServletRequest request, HttpServletResponse response) throws
  ServletException, IOException {
  doGet(request, response);
 }

 public void destroy() {}


 private String Load_Details_For_PutAway(HttpServletRequest request,
                              HttpServletResponse response) throws  IOException, ServletException , Exception {
    String str = "", company = "", travelNo = "", mtid = "", item = "",lot = "";
    boolean isexists= false;
    InvMstDAO invDAO = new InvMstDAO();
     
    try {
      company = strUtils.fString(request.getParameter("PLANT"));
      travelNo = strUtils.fString(request.getParameter("TRAVELLER_NUM"));
      mtid = strUtils.fString(request.getParameter("MTID"));
      
      isexists=pwUtil.getReceiveCount(company,mtid,"");
      
      if(isexists)
      {
          throw new Exception("Cannot perform PutAway either MTID : " + mtid + " is not Received or Received with discripancy");
      }else
      {
          str =pwUtil.Load_PutAway_Details(company,mtid);
      }
     
      
    }
    catch (Exception e) {
      MLogger.log(" %%%%%%%%%%%%%%%%%%%%%%%%% Exception :: in Action : " +action + e.getMessage());
      throw e;
    }
    return str;
  }

private String processPutAway(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException ,Exception{

  Hashtable ht = null;
  String str = "";
  String company="",travelerNo="",mtid="",item="",lot="",
  qty="",status="",user= "",frLoc="",toLoc="",locReAssign="",tranDate="",tranTime="";
  UserTransaction ut =null;
  try {

    tranDate=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
    tranTime=DateUtils.Time();
  
    company = strUtils.fString(request.getParameter("PLANT"));
    travelerNo = strUtils.fString(request.getParameter("TRAVELLER_NUM"));
    mtid = strUtils.fString(request.getParameter("MTID"));
    item = strUtils.fString(request.getParameter("SKU"));
    lot = strUtils.fString(request.getParameter("LOT"));
    qty = strUtils.fString(request.getParameter("QTY"));
    frLoc = strUtils.fString(request.getParameter("FRLOC"));
    toLoc = strUtils.fString(request.getParameter("TOLOC"));
    locReAssign = strUtils.fString(request.getParameter("LOC_RE_ASSIGN"));
    status = strUtils.fString(request.getParameter("STATUS"));
    user = strUtils.fString(request.getParameter("LOGIN_USER"));
    
    MLogger.printInput("PLANT       :"+ company);
    MLogger.printInput("travelerNo  :"+ travelerNo);
    MLogger.printInput("mtid        :"+ mtid);
    MLogger.printInput("travelerNo  :"+ travelerNo);
    MLogger.printInput("sku         :"+ item);
    MLogger.printInput("lot         :"+ lot);
    MLogger.printInput("qty         :"+ qty);
    MLogger.printInput("locReAssign :"+ locReAssign);
    MLogger.printInput("frLoc       :"+ frLoc);
    MLogger.printInput("toLoc       :"+ toLoc);
    
    
    ht = new Hashtable();
    Hashtable htMov = new Hashtable();
    htMov.put(MDbConstant.COMPANY, company);
    htMov.put(MDbConstant.TRAVELER_NUM,travelerNo);
    htMov.put(MDbConstant.MTID,mtid);
    htMov.put(MDbConstant.SKU,item);
    htMov.put(MDbConstant.LOT,lot);
    htMov.put(MDbConstant.QTY,qty);
    htMov.put(MDbConstant.LOC ,toLoc);
    htMov.put(MDbConstant.LOGIN_USER , user);
    ht.put(MDbConstant.COMPANY, company);
    ht.put(MDbConstant.TRAVELER,travelerNo);
    ht.put(MDbConstant.MTID,mtid);
    ht.put(MDbConstant.SKU,item);
    ht.put(MDbConstant.LOT,lot);
    ht.put(MDbConstant.RECV_QTY,qty);
    ht.put(MDbConstant.LOC ,toLoc);
    ht.put(MDbConstant.LOC_ID ,frLoc);
    ht.put("toLocation" ,toLoc);
    ht.put(MDbConstant.LOGIN_USER , user);
    ht.put("LocReAssign" , locReAssign);
    ht.put("CRAT" , tranDate);
    ht.put(MDbConstant.CRTIME , tranTime);
   
   
     xmlStr = "";
     
     // CHECK ALL LOT RECEIVED in recvdet
     Hashtable htRecvDet = new Hashtable();
     htRecvDet.put(MDbConstant.TRAVELER,travelerNo);
     htRecvDet.put(MDbConstant.LOT,lot);
     htRecvDet.put("ReceiveStatus","N");
     //CONDITION 1 ALL LOT MUST BE RECEIVED
     boolean flag= pwUtil.isExists(htRecvDet);
     
     if(flag)
     {
       throw new Exception("All lot : " + lot + " must be received before PutAway");
     }
     //CONDITION 2 THIS SKU CAN BE PUT TO THIS LOCATION - LOCATION ASSIGNING RULES
    
     Hashtable htLocCheck = new Hashtable();
     ItemMstUtil _ItemMstUtil = new ItemMstUtil();
     String prodGroup= _ItemMstUtil.getProductGroup(item);
     
     htLocCheck.put(MDbConstant.LOC_ID,toLoc);
     htLocCheck.put(MDbConstant.PRD_GRP_ID,prodGroup);
     
     LocationAssigningRulesDAO _LocationAssigningRulesDAO=new LocationAssigningRulesDAO();
     flag=_LocationAssigningRulesDAO.isExists(htLocCheck);
     MLogger.info("isExist : "+ flag);
     if(flag==false)
     {
       throw new Exception("Sku : " + item + " cannot put to this location : " + toLoc);
     }
     
     xmlStr = pwUtil.Process_PutAway(ht);
  
 }catch(Exception e){
   System.out.println("Exception :"+e.toString());
     
   throw new Exception("Unable to Putaway() Exception : " + e.getMessage());
 }
return xmlStr;
}

/*  Added by Ranjana 
 *  Purpose: LOT restriction in PDA under ticket WO0000000284867 
 *  for the process of PUTAWAY*/

private String Lot_Blocked_PutAway(HttpServletRequest request,
		HttpServletResponse response) throws IOException, ServletException,
		Exception {
	String xmlStr = "", TRAVELER = "", LOT = "";
	try {
		TRAVELER = strUtils.fString(request.getParameter("TRAVELER"));
		LOT = strUtils.fString(request.getParameter("LOT"));

		Hashtable ht = new Hashtable();
		ht.put("TRAVELER", TRAVELER);
		ht.put("LOT", LOT);

		xmlStr = pwUtil.LOT_Blocked_PutAway(ht);

		MLogger.info("************* " + xmlStr);
	} catch (Exception e) {
		MLogger
				.log(0,
						"LOT_Blocked_PutAway() Failed .............................");
		MLogger.exception(this, e);
		throw e;
	}
	MLogger.log(0,
			"LOT_Blocked_PutAway() Ends .............................");
	return xmlStr;
}
}

