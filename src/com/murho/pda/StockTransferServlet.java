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
import com.murho.db.utils.StockTakeUtil;
import com.murho.db.utils.StockTransferUtil;
import com.murho.utils.DateUtils;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;


/**
 * <p>Title: Stock Transfer  using PDA</p>
 * <p>Description: This servlet is used for Stock Transfer using PDA</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Murho</p>
 * @author not attributable
 * @version 1.0
 */

public class StockTransferServlet
    extends HttpServlet {

  Context ctx = null;
  XMLUtils xu = new XMLUtils();
  MLogger logger = new MLogger();
  StrUtils strUtils = new StrUtils();

  StockTakeUtil _StockTakeUtil=null;
  StockTransferUtil _StockTransferUtil=null;
  InvMstDAO _InvMstDAO= null; 
  LocMstDAO _LocMstDAO= null; 
  // UserTransaction ut;
  String PLANT = "", REFTYPE = "", REFLNNO = "", USERNAME = "",
      BINNO = "", COMPANY = "";

  String xmlStr = "";
  String action = "";


  private static final String CONTENT_TYPE = "text/xml";

  public void init() throws ServletException {
 
   _InvMstDAO=new InvMstDAO();
   _LocMstDAO= new LocMstDAO();
   _StockTakeUtil=new StockTakeUtil();
   _StockTransferUtil=new StockTransferUtil();
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
    
      if(action.equalsIgnoreCase("validateLoc"))
       {
         xmlStr = "";
         xmlStr = getLocation(request, response);
      }
         if(action.equalsIgnoreCase("validateMTID"))
       {
         xmlStr = "";
         xmlStr = getMTIDdetails(request, response);
       } if (action.equalsIgnoreCase("Process_StockTransfer")) {
        xmlStr = "";
        xmlStr = Process_StockTransfer(request, response);
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
  
 
  
    private String getLocation(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", plant   = "",loc="";
     try {
    
      plant = strUtils.fString(request.getParameter("PLANT"));
      loc = strUtils.fString(request.getParameter("LOC"));
      str =_StockTransferUtil.getValidLocation(plant,loc);
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
      throw e;
    }
    return str;
  }
  
      private String getMTIDdetails(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", 
      plant   = "",mtid="",loc="",transfer="";
     try {
    
      plant = strUtils.fString(request.getParameter("PLANT"));
      mtid = strUtils.fString(request.getParameter("MTID"));
      loc = strUtils.fString(request.getParameter("LOC"));
      transfer = strUtils.fString(request.getParameter("TRANSFER"));
      
      MLogger.info("plant     :"+plant );   
      MLogger.info("mtid      :"+mtid );
      MLogger.info("loc       :"+loc );  
      MLogger.info("transfer  :"+transfer );
      
      str =_StockTransferUtil.getMTIDdetails(plant,mtid,loc,transfer);
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
                  
       throw e;
    }
    return str;
  }
 
  private String Process_StockTransfer(HttpServletRequest request,
                           HttpServletResponse response) throws IOException,
      ServletException, Exception 
  {
  MLogger.log(0, "Process_StockTransfer() Starts");

    Map StkTransfer_HM = null;
    String PLANT = "", MTID = "",FRMLOC = "",TOLOC="",BATCH="",QTY="",LOGIN_USER="",TRANSTYPE="",LOC="";
    String SKU="",LOT="",tranDate="",tranTime="";
 
    try 
    {
      tranDate=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
      tranTime=DateUtils.Time();
    
      PLANT = strUtils.fString(request.getParameter("PLANT"));
      LOGIN_USER = strUtils.fString(request.getParameter("LOGIN_USER"));
      TRANSTYPE = strUtils.fString(request.getParameter("TRANSTYPE"));
      LOC = strUtils.fString(request.getParameter("LOC"));
      FRMLOC = strUtils.fString(request.getParameter("LOC"));
      TOLOC = strUtils.fString(request.getParameter("TOLOC"));
      MTID = strUtils.fString(request.getParameter("MTID"));
      QTY = strUtils.fString(request.getParameter("QTY"));
      SKU = strUtils.fString(request.getParameter("SKU"));
      LOT = strUtils.fString(request.getParameter("LOT"));
      
     
      StkTransfer_HM = new HashMap();
      StkTransfer_HM.put("PLANT",PLANT);
      StkTransfer_HM.put("MTID",MTID);
      StkTransfer_HM.put("FRMLOC",FRMLOC);
      StkTransfer_HM.put("TOLOC",TOLOC);
      StkTransfer_HM.put("LOC",LOC);
      StkTransfer_HM.put("TRANSTYPE",TRANSTYPE);
      StkTransfer_HM.put("QTY",QTY);
      StkTransfer_HM.put("CRBY",LOGIN_USER);
      StkTransfer_HM.put("TEMPLOC","TEMPLOC");
      StkTransfer_HM.put("SKU",SKU);
      StkTransfer_HM.put("LOT",LOT);
      StkTransfer_HM.put("CRTIME",tranTime);
        
      MLogger.log(0, "Input Starts");   
      MLogger.log(0, "PLANT                   : " + StkTransfer_HM.get("PLANT"));
      MLogger.log(0, "TRANSTYPE               : " + StkTransfer_HM.get("TRANSTYPE"));
      MLogger.log(0, "FRMLOC                  : " + StkTransfer_HM.get("FRMLOC"));
      MLogger.log(0, "TOLOC                   : " + StkTransfer_HM.get("TOLOC"));
      MLogger.log(0, "LOC                    : " + StkTransfer_HM.get("LOC"));
      MLogger.log(0, "MTID                    : " + StkTransfer_HM.get("MTID"));
      MLogger.log(0, "QTY                     : " + StkTransfer_HM.get("QTY"));
      MLogger.log(0, "LOGIN_USER              : " + StkTransfer_HM.get("CRBY"));
      MLogger.log(0, "Input Ends");
      xmlStr = "";
      
     xmlStr = _StockTransferUtil.process_StockTransfer(StkTransfer_HM);
        
    }
    catch (Exception e) {
      MLogger.exception("process_StockTransfer()", e);
      throw e;
    }
  MLogger.log(0, "process_StockTransfer() Ends");
 
  return xmlStr;
  }
  
  
} //end of class
