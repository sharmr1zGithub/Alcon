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
import com.murho.db.utils.InvMstUtil;
import com.murho.db.utils.MovHisUtil;
import com.murho.gates.DbBean;
import com.murho.gates.Generator;
import com.murho.utils.DateUtils;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;

public class LocationTransferServlet
    extends HttpServlet {

  Context ctx = null;
  XMLUtils xu = new XMLUtils();
  MLogger logger = new MLogger();
  StrUtils strUtils = new StrUtils();
  Generator generator = new Generator();
  DateUtils dateUtils=null;
  InvMstUtil _InvMstUtil = null;
  MovHisUtil _MovHisUtil=null;

  String plant = "", receiptDate = "", SupplierID = "", packSlip = "", PONumber = "";

  String xmlStr = "";
  String action = "";
  String strLoginUser = "";

  private static final String CONTENT_TYPE = "text/xml";

  public void init() throws ServletException {
    _InvMstUtil = new InvMstUtil();
     _MovHisUtil =new MovHisUtil ();
    dateUtils = new DateUtils();
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException {

    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();
    try {

      action = request.getParameter("action").trim();
      strLoginUser = strUtils.fString(request.getParameter("LOGIN_USER"));


      MLogger.log(1, strLoginUser + " : " + this.getClass() + " doGet() ##################");
      MLogger.log(0, "Loggin user : " + strLoginUser);
      MLogger.log(0, "Action      : " + action);
      
      if (action.equalsIgnoreCase("Process_Location_Transfer")) {
        xmlStr = "";
        xmlStr = processLocationTransfer(request, response);
      }
    }
    catch (Exception e) {
      MLogger.exception(" Exception :: doGet() : ", e);
      xmlStr = xu.getXMLMessage(1, e.getMessage());
    }
    out.write(xmlStr);
    out.close();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException {
    doGet(request, response);
  }

  public void destroy() {
  }

  private String load_inv_det_for_loc_mtid(HttpServletRequest request,
                                    HttpServletResponse response) throws
      IOException, ServletException, Exception {
    String str = "";
    try {

      plant = strUtils.fString(request.getParameter("PLANT"));
      String loc = strUtils.fString(request.getParameter("LOC"));
      String mtid = strUtils.fString(request.getParameter("MTID"));
    
      str = _InvMstUtil.Load_Inv_Det_for_Loc_MTID(plant, loc, mtid);

    }
    catch (Exception e) {
      throw e;
    }
    return str;
  }
  
  private String processLocationTransfer(HttpServletRequest request,
         HttpServletResponse response) throws IOException,ServletException,Exception
  {
  MLogger.log(0, " processLocationTransfer() Starts ");
  Hashtable ht3 = null;
  Hashtable ht = null;
  String str = "";
  String company="",travelerNo="",mtid="",item="",lot="",qty="",status="",user= "",frLoc="",toLoc="",tranTime="";
 
   UserTransaction ut =null;

try {

  tranTime=DateUtils.Time();
   
  company = strUtils.fString(request.getParameter("PLANT"));
  mtid = strUtils.fString(request.getParameter("MTID"));
  item = strUtils.fString(request.getParameter("SKU"));
  lot = strUtils.fString(request.getParameter("LOT"));
  qty = strUtils.fString(request.getParameter("QTY"));
  frLoc = strUtils.fString(request.getParameter("FrLoc"));
  toLoc = strUtils.fString(request.getParameter("ToLoc"));
  user = strUtils.fString(request.getParameter("LOGIN_USER"));
  
  travelerNo=_InvMstUtil.getTravellerNo(company,frLoc,mtid,item,lot);
  
  MLogger.printInput("Traveller NO : " + travelerNo);
  
  ht3 = new Hashtable();
 
  Hashtable htMov = new Hashtable();
  
  htMov.put(MDbConstant.COMPANY, company);
  htMov.put(MDbConstant.MTID,mtid);
  htMov.put(MDbConstant.SKU,item);
  htMov.put("BATNO",lot);
  htMov.put(MDbConstant.QTY,qty);
  htMov.put(MDbConstant.LOC ,frLoc);
  htMov.put(MDbConstant.LOGIN_USER , user);
  htMov.put("PONO", travelerNo);
 
  ht = new Hashtable();
  ht.put(MDbConstant.COMPANY, company);
  ht.put("MTID",mtid);
  ht.put("ITEM",item);
  ht.put("LOT",lot);
  ht.put("LOC" ,frLoc);
 
  boolean isInserted=false;
   ht3 = new Hashtable();
  ut = DbBean.getUserTranaction();
  ut.begin();
  InvMstDAO _InvMstDAO = new InvMstDAO();
  String Query ="SET LOC = '"+toLoc+"' ";
  isInserted =  _InvMstDAO.updateInv(Query,ht,"");
  System.out.println("isInserted ::Inv ::"+isInserted);
      if(isInserted)
      {
        htMov.put("CRAT",dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
        htMov.put("CRTIME",tranTime);
        htMov.put("MOVTID","LOC_TR_OUT");
        htMov.put(MDbConstant.QTY,"-"+qty);
        isInserted =  _MovHisUtil.insertMovHis(htMov);
      }
       if(isInserted)
      {
        htMov.put("MOVTID","LOC_TR_IN");
        htMov.put(MDbConstant.QTY,qty);
        htMov.put(MDbConstant.LOC ,toLoc);
        htMov.put("CRAT",dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
        htMov.put("CRTIME",tranTime);
        isInserted =  _MovHisUtil.insertMovHis(htMov);
      }
      if(isInserted == true) {
        try {
        DbBean.CommitTran(ut);
        
        }catch (Exception ee) {}
        str = xu.getXMLMessage(0, "Location Transfer successful!"); 
      }
        else {
          try {DbBean.RollbackTran(ut); } catch (Exception ee) {}
          str = xu.getXMLMessage(1, "Location Transfer Failed");
        }
 
 }catch(Exception e){
  MLogger.exception(this,e);
  try {DbBean.RollbackTran(ut); } catch (Exception ee) {}
  throw e;
 }
return str;
}
  
 } 