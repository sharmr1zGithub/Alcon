package com.murho.pda;



import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.murho.dao.InvMstDAO;
import com.murho.dao.LocMstDAO;
import com.murho.db.utils.LocMstUtil;
import com.murho.db.utils.PrdClsUtil;
import com.murho.db.utils.PutAwayUtil;
import com.murho.db.utils.StockAdjustmentUtil;
import com.murho.utils.DateUtils;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;


/**
 * <p>Title: Blind Receipt In  using PDA</p>
 * <p>Description: This servlet is used for Stock Adjustment using PDA</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Murho</p>
 * @author not attributable
 * @version 1.0
 */

public class StockAdjustment
    extends HttpServlet {

  Context ctx = null;
  XMLUtils xu = new XMLUtils();
  MLogger logger = new MLogger();
  StrUtils strUtils = new StrUtils();

  StockAdjustmentUtil _sAdjUtil=null;
  InvMstDAO _InvMstDAO= null; 
  LocMstDAO _LocMstDAO= null; 
  String PLANT = "",  USERNAME = "", BINNO = "", COMPANY = "";

  String xmlStr = "";
  String action = "";


  private static final String CONTENT_TYPE = "text/xml";

  public void init() throws ServletException {
  
   _InvMstDAO=new InvMstDAO();
   _LocMstDAO= new LocMstDAO();
   _sAdjUtil=new StockAdjustmentUtil();
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException 
  {
    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();
    MLogger.log(1, "_______________________ " + this.getClass() + " doGet()");
    try 
    {
      action = request.getParameter("action").trim();
      MLogger.log(0,"------------------ Action = " + action + " ----------------- **** ");
      
      if(action.equalsIgnoreCase("validateMTID"))
        {
         xmlStr = "";
         xmlStr = isValidMtid(request, response);
        }
       else if(action.equalsIgnoreCase("validateSku"))
       {
        xmlStr = "";
        xmlStr = isValidSku(request, response);
       }
      else if (action.equalsIgnoreCase("load_reason_codes")) {
        xmlStr = "";
        xmlStr = load_Reason_Codes(request, response);
      }
      
    //Bliend Receipt in 
      
      else if (action.equalsIgnoreCase("Blindreceipt")) {
          xmlStr = "";
          xmlStr = Blindreceipt(request, response);
        }
      
     else if (action.equalsIgnoreCase("process_stockAdjustment")) {
        xmlStr = "";
        xmlStr = process_stockAdjustment(request, response);
      }
       else if(action.equalsIgnoreCase("validateLoT"))
       {
         xmlStr = "";
        xmlStr = isValidLotNo(request, response);
      }
      else if(action.equalsIgnoreCase("validateLoc"))
       {
         xmlStr = "";
        xmlStr = isValidLoc(request, response);
      }
      

    }
    catch (Exception e) 
    {
      MLogger.exception(" Exception :: doGet() : ", e);
      xmlStr = xu.getXMLMessage(1,"Error : " + e.getMessage());
    }
    MLogger.log( 0, "XML :" + xmlStr);
    MLogger.log( -1, "_______________________ " + this.getClass() + " doGet()");
    out.write(xmlStr);
    out.close();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException {
    doGet(request, response);
  }

  public void destroy() {
  }
  
  

 
  private String isValidMtid(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", 
      plant   = "",loc="",mtid;
     try {
    
      plant = strUtils.fString(request.getParameter("PLANT"));
      loc   = strUtils.fString(request.getParameter("LOC"));
      mtid  = strUtils.fString(request.getParameter("MTID"));

      str =_sAdjUtil.isValidMtid(plant,mtid);
     }
    catch (Exception e) {
      MLogger.exception(this,e);;
      throw e;
    }
      return str;
  }
  
   private String isValidSku(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", 
      plant   = "",loc="",mtid,sku;
     try {
    
      plant = strUtils.fString(request.getParameter("PLANT"));
      loc   = strUtils.fString(request.getParameter("LOC"));
      mtid  = strUtils.fString(request.getParameter("MTID"));
      sku  = strUtils.fString(request.getParameter("SKU")); 
   
      str =_sAdjUtil.isValidSKU(plant,mtid,sku);  
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
      throw e;
    }
    return str;
  }
  
   private String isValidSku1(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", 
      plant   = "",loc="",mtid,sku;
     try {
    
      plant = strUtils.fString(request.getParameter("PLANT"));
      loc   = strUtils.fString(request.getParameter("LOC"));
      mtid  = strUtils.fString(request.getParameter("MTID"));
      sku  = strUtils.fString(request.getParameter("SKU")); 
   
      str =_sAdjUtil.isValidSKU1(plant,mtid,sku);  
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
      throw e;
    }
    return str;
  }
  
   private String isValidLotNo(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", 
      plant   = "",loc="",mtid,sku,lot;
     try {
    
      plant = strUtils.fString(request.getParameter("PLANT"));
      loc   = strUtils.fString(request.getParameter("LOC"));
      mtid  = strUtils.fString(request.getParameter("MTID"));
      sku  = strUtils.fString(request.getParameter("SKU"));
      lot  = strUtils.fString(request.getParameter("LOT"));
      str =_sAdjUtil.isValidLotNo(plant,mtid,sku,lot);
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
                  
       throw e;
    }
   
    return str;
  }
  
      private String isValidLoc(HttpServletRequest request,
                              HttpServletResponse response) throws  IOException, ServletException , Exception{
      String str  = "", 
      plant   = "",loc="";
     try {
    
     // plant = strUtils.fString(request.getParameter("PLANT"));
      plant = strUtils.fString(request.getParameter("PLANT"));
      loc = strUtils.fString(request.getParameter("LOC"));
    
      str =_sAdjUtil.isValidLoc(plant,loc);
           
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
      throw e;
    }
      return str;
  }
  
    private String load_Reason_Codes(HttpServletRequest request,
                                    HttpServletResponse response) throws  IOException, ServletException ,Exception{
     String str = "";
     String MODULE_NAME = "";
     try {
       PLANT = strUtils.fString(request.getParameter("PLANT"));
       MODULE_NAME = strUtils.fString(request.getParameter("MODULE_NAME"));
       str = _sAdjUtil.getReasonCode(PLANT, MODULE_NAME);
       if (str.equalsIgnoreCase("")) {
          MLogger.info(" ########## Detail Not found");
          str = xu.getXMLMessage(1, "Details not found");
       }
     }
     catch (Exception e) {
       MLogger.exception(
           " %%%%%%%%%%%%%%%%%%%%%%%%% Exception :: load_Reason_Codes() : ", e);
           throw e;
     }
     return str;
   }

  private String process_stockAdjustment(HttpServletRequest request,
                           HttpServletResponse response) throws IOException,ServletException, Exception 
  {
    Map stkAdj_HM = null;
    String PLANT = "", MTID="",SKU = "",LOC = "",LOT="",QTY="",LOGIN_USER="",RESCODE="",RESDESC="",tranDate="",tranTime="";
 
    try 
    {
      tranDate=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
      tranTime=DateUtils.Time();
      PLANT = strUtils.fString(request.getParameter("PLANT"));
      MTID = strUtils.fString(request.getParameter("MTID"));
      SKU = strUtils.fString(request.getParameter("SKU"));
      LOC = strUtils.fString(request.getParameter("LOC"));
      LOT = strUtils.fString(request.getParameter("LOT"));
      QTY = strUtils.fString(request.getParameter("QTY"));
      RESCODE = strUtils.fString(request.getParameter("RESCODE"));
      RESDESC = strUtils.fString(request.getParameter("RESDESC"));
      LOGIN_USER = strUtils.fString(request.getParameter("LOGIN_USER"));
      stkAdj_HM = new HashMap();
      stkAdj_HM.put(MDbConstant.PLANT,PLANT);
      stkAdj_HM.put(MDbConstant.LOC,LOC);
      stkAdj_HM.put(MDbConstant.MTID,MTID);
      stkAdj_HM.put(MDbConstant.ITEM,SKU);
      stkAdj_HM.put(MDbConstant.LOT_NUM,LOT);
      stkAdj_HM.put(MDbConstant.QTY,QTY);
      stkAdj_HM.put("USERFLD5",RESCODE);
      stkAdj_HM.put("USERFLD6",RESDESC);
      stkAdj_HM.put("CRTIME",tranTime);
      stkAdj_HM.put(MDbConstant.LOGIN_USER,LOGIN_USER);
  
      MLogger.log(0, "Input Starts");   
      MLogger.log(0, "PLANT               : " + stkAdj_HM.get(MDbConstant.PLANT));
      MLogger.log(0, "MTID               : " + stkAdj_HM.get(MDbConstant.MTID));
      MLogger.log(0, "LOC                 : " + stkAdj_HM.get(MDbConstant.LOC));
      MLogger.log(0, "SKU                 : " + stkAdj_HM.get(MDbConstant.ITEM));
      MLogger.log(0, "LOT                 : " + stkAdj_HM.get(MDbConstant.LOT_NUM));
      MLogger.log(0, "QTY                 : " + stkAdj_HM.get(MDbConstant.QTY));
      MLogger.log(0, "LOGIN_USER          : " + stkAdj_HM.get(MDbConstant.LOGIN_USER));
      MLogger.log(0, "RESCODE          : " + stkAdj_HM.get("USERFLD5"));
      MLogger.log(0, "RESDESC          : " + stkAdj_HM.get("USERFLD6"));


      // CHECK FOR VALID SKU
      Hashtable ht=new Hashtable();
      ht.put("LOC_ID",LOC);
       boolean flag = new LocMstUtil().isExistsLocId(ht);
         MLogger.log(0, "Location : " + LOC   +" : " + flag);
         if(flag==false)
         {
             throw new Exception("Loc : "+ LOC + " not available");
         }
         
      String prodClass=new PrdClsUtil().getProductClass(SKU);
      
      String trayQty= new PrdClsUtil().getQtyPerTray(prodClass);
      
       if((Integer.parseInt(QTY) > Integer.parseInt(trayQty) ))
       {
         throw new Exception("Maximum of : "+ trayQty + " qty allowed per MTID");
       }
      xmlStr = "";
      xmlStr = _sAdjUtil.process_StockAdjustment(stkAdj_HM);
        
    }
    catch (Exception e) {
      MLogger.exception(this,e);
      throw new Exception(" " + e.getMessage());
    }
    MLogger.info( "*************** process_StockAdjustment() << ENDS >> ***************");
    return xmlStr;
  }
  
 //Added by Ranjana for LOT Restriction under ticket WO0000000284867 
  
  private String Blindreceipt(HttpServletRequest request,
          HttpServletResponse response) throws  IOException, ServletException , Exception{		
	  	String xmlStr = "", LOT = "";
	  	PutAwayUtil pwUtil =null;
	  	pwUtil       = new PutAwayUtil();
	  	try {
	  		LOT = strUtils.fString(request.getParameter("LOT"));

	  		xmlStr = _sAdjUtil.BlindReceipt(LOT);

	  		MLogger.info("************* " + xmlStr);
	  	} catch (Exception e) {
	  		MLogger
	  				.log(0,
	  						"Blindreceipt() Failed .............................");
	  		MLogger.exception(this, e);
	  		throw e;
	  	}
	  	MLogger.log(0,
	  			"Blindreceipt() Ends .............................");
	  	return xmlStr;
	  }

  private static void sop(String str) {
    System.out.println(str);
  }
} //end of class
