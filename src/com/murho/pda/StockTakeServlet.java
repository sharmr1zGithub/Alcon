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
import com.murho.db.utils.PrdClsUtil;
import com.murho.db.utils.StockTakeUtil;
import com.murho.utils.DateUtils;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;


/**
 * <p>Title: Stock Take  using PDA</p>
 * <p>Description: This servlet is used for Stock Take using PDA</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Murho</p>
 * @author not attributable
 * @version 1.0
 */

public class StockTakeServlet
    extends HttpServlet {

  Context ctx = null;
  XMLUtils xu = new XMLUtils();
  MLogger logger = new MLogger();
  StrUtils strUtils = new StrUtils();
  StockTakeUtil _StockTakeUtil=null;
  InvMstDAO _InvMstDAO= null; 
  LocMstDAO _LocMstDAO= null; 
  String PLANT = "", REFTYPE = "", REFLNNO = "", USERNAME = "",BINNO = "", COMPANY = "";

  String xmlStr = "";
  String action = "";


  private static final String CONTENT_TYPE = "text/xml";

  public void init() throws ServletException {
   _InvMstDAO=new InvMstDAO();
   _LocMstDAO= new LocMstDAO();
   _StockTakeUtil=new StockTakeUtil();
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
      } if (action.equalsIgnoreCase("Process_StockCount")) {
        xmlStr = "";
        xmlStr = process_StockCount(request, response);
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
      String str  = "",  plant   = "",loc="";
     try {
    
        plant = strUtils.fString(request.getParameter("PLANT"));
        loc = strUtils.fString(request.getParameter("LOC"));
        str =_StockTakeUtil.getValidLocation(plant,loc);
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
      plant   = "",mtid="";
       try {
      
        plant = strUtils.fString(request.getParameter("PLANT"));
        mtid = strUtils.fString(request.getParameter("MTID"));
        str =_StockTakeUtil.getMTIDdetails(plant,mtid);
      }
      catch (Exception e) {
        MLogger.exception(this,e);;
        throw e;
      }
  
    return str;
  }

  
   private String process_StockCount(HttpServletRequest request,
                           HttpServletResponse response) throws IOException,
      ServletException, Exception {
    
      MLogger.info("*************** process_StockCount() << STARTS >> ***************");

  Map Stktake_HM=null;
  String str = "";
  String company="",travelerNo="",pallet="",mtid="",item="",lot="",status="",user= "",reasonCount="";
  String loc="",sku="",qty="",tranDate="",tranTime="";
  try {
  
      tranDate=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
      tranTime=DateUtils.Time();
      company = strUtils.fString(request.getParameter("PLANT"));
      user = strUtils.fString(request.getParameter("LOGIN_USER"));
      loc = strUtils.fString(request.getParameter("LOC"));
      mtid = strUtils.fString(request.getParameter("MTID"));
      sku = strUtils.fString(request.getParameter("SKU"));
      lot = strUtils.fString(request.getParameter("LOT"));
      qty = strUtils.fString(request.getParameter("QTY"));
     
     MLogger.log(0, "PLANT"+company);
     MLogger.log(0, "user"+user);
     MLogger.log(0, "loc"+loc);
     MLogger.log(0, "mtid"+mtid);
     MLogger.log(0, "sku"+sku);
     MLogger.log(0, "lot"+lot);
     MLogger.log(0, "qty"+qty);
     MLogger.log(0, "tranDate"+tranDate);
 
     String prodClass=new PrdClsUtil().getProductClass(sku);
     //get tray Qty  
      String trayQty= new PrdClsUtil().getQtyPerTray(prodClass);
      if((Integer.parseInt(qty) > Integer.parseInt(trayQty) ))
       {
         throw new Exception("Maximum of "+ trayQty + " qty allowed per MTID for the product class : " + prodClass);   
       }
  
      Stktake_HM = new HashMap();
      
      Stktake_HM.put(MDbConstant.LOC,loc);
      Stktake_HM.put(MDbConstant.MTID,mtid);
      Stktake_HM.put(MDbConstant.ITEM,sku);
      Stktake_HM.put(MDbConstant.LOT_NUM,lot);
      Stktake_HM.put(MDbConstant.QTY,qty);
      Stktake_HM.put(MDbConstant.PLANT,company);
      Stktake_HM.put(MDbConstant.LOGIN_USER,user);
      Stktake_HM.put(MDbConstant.CREATED_AT,tranDate);
      Stktake_HM.put(MDbConstant.CRTIME,tranTime);
      MLogger.info( "Input values END ...................");
      xmlStr = ""; 
      xmlStr = _StockTakeUtil.process_StockCount(Stktake_HM);
        
 }
    catch (Exception e) {
      MLogger.exception(this,e);
      throw new Exception(" " + e.getMessage());
    }
    MLogger.info( "*************** process_StockCount() << ENDS >> ***************");
    return xmlStr;
  }

 
  
  
} 