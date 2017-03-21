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

import com.murho.db.utils.InvMstUtil;
import com.murho.db.utils.MovHisUtil;
import com.murho.db.utils.PodetUtil;
import com.murho.db.utils.RecevingUtil;
import com.murho.gates.DbBean;
import com.murho.gates.Generator;
import com.murho.utils.DateUtils;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;


public class RecevingServlet extends HttpServlet{


Context ctx = null;
XMLUtils xu = new XMLUtils();
MLogger logger = new MLogger();
StrUtils strUtils = new StrUtils();
Generator generator = new Generator();



// UserTransaction ut;
String plant = "";

public static final boolean DEBUG = true;

String xmlStr = "";
String action = "";
 
 PodetUtil poUtil =null;
 MovHisUtil movUtil =null;
 InvMstUtil invUtil=null;
 
 //
 RecevingUtil _RecevingUtil = null;
private static final String CONTENT_TYPE = "text/xml";

public void init() throws ServletException {

invUtil    = new InvMstUtil();
poUtil      = new PodetUtil();
movUtil     = new MovHisUtil();
///
_RecevingUtil    = new RecevingUtil();
///
}

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();

    try {
    action = request.getParameter("action").trim();
    MLogger.log(1, "_______________________ " + this.getClass() + " doGet()");
    MLogger.log(0," Action = " + action );
    }
    catch (Exception ex) {
        MLogger.exception(this, ex);
    }

    try {
      
      if (action.equalsIgnoreCase("Is_Valid_For_Receving")) {
       xmlStr = "";
       xmlStr = Load_Details_For_Receving(request, response);
      }
      else if (action.equalsIgnoreCase("Load_Details_For_Receving")) {
        xmlStr = "";
        xmlStr = Load_Details_For_Receving(request, response);
      }
      else if (action.equalsIgnoreCase("load_incomigTraveller_details")) {
        xmlStr = "";
        xmlStr = LoadOpenTraveler(request, response);
      }
      else if (action.equalsIgnoreCase("Load_MTID_Details")) {
        xmlStr = "";
        xmlStr = LoadMTIDDetails(request, response);
      }
      else if (action.equalsIgnoreCase("load_all_pallets")) {
        xmlStr = "";
        xmlStr = LoadPallet(request, response);
      }
      
      else if (action.equalsIgnoreCase("Process_Receving")) {
        xmlStr = "";
        xmlStr = processReceving(request, response);

      }
    }
    catch (Exception e) {
       xmlStr = xu.getXMLMessage(1,e.getMessage());
    }

    if (xmlStr.equalsIgnoreCase("")) {
      xmlStr = xu.getXMLMessage(1,"Some inputs were missing,Please check once again!!!");
    }
    MLogger.log(-1, "_______________________ " + this.getClass() + " doGet()");
    out.write(xmlStr);
    out.close();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws
  ServletException, IOException {
  doGet(request, response);
 }

 public void destroy() {}
 
 //#########################################################
 private String LoadOpenTraveler(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", 
      company   = "", 
      loginUser = "";
   
       
    try {
      
      loginUser = strUtils.fString(request.getParameter("LOGIN_USER"));
      company = strUtils.fString(request.getParameter("PLANT"));
     
      MLogger.printInput("company    : " + company);
      MLogger.printInput("login user : " + loginUser);
     
      
      str =_RecevingUtil.getOpenTravelers();   
      
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
                  
       throw e;
    }
     MLogger.log(0, "Load_Details_For_PutAway() End .............................");

    return str;
  }
  
  private String LoadPallet(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", 
      company   = "", 
      travelerNum="",
      loginUser = "";
   
       
    try {
      
      loginUser = strUtils.fString(request.getParameter("LOGIN_USER"));
      company = strUtils.fString(request.getParameter("PLANT"));
      travelerNum = strUtils.fString(request.getParameter("TRAVELER_NUM"));
     
      MLogger.printInput("company      : " + company);
      MLogger.printInput("login user   : " + loginUser);
      MLogger.printInput("TRAVELER_NUM : " + travelerNum);
     
      
      str =_RecevingUtil.getPallet(travelerNum);   
      
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
                  
       throw e;
    }
     MLogger.log(0, "Load_Details_For_PutAway() End .............................");

    return str;
  }
  
   private String LoadMTIDDetails(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", 
      plant   = "",
      travelerNum="",
      pallet="",
      MTID="",
      loginUser = "";
   
       
    try {
    
      plant = strUtils.fString(request.getParameter("PLANT"));
      loginUser = strUtils.fString(request.getParameter("LOGIN_USER"));
      travelerNum = strUtils.fString(request.getParameter("TRAVELLER_NUM"));
      pallet = strUtils.fString(request.getParameter("PALLET_NUM"));
      MTID = strUtils.fString(request.getParameter("MTID"));
     
      MLogger.printInput("Plant       : " + plant);
      MLogger.printInput("Login user  : " + loginUser);
      MLogger.printInput("TravelerNum : " + travelerNum);
      MLogger.printInput("Pallet      : " + pallet);
      MLogger.printInput("MTID        : " + MTID);
   
      str =_RecevingUtil.getMTIDDetails(travelerNum,pallet,MTID);   
      
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
                  
       throw e;
    }
     MLogger.log(0, "Load_Details_For_PutAway() End .............................");

    return str;
  }
 //###########################################################
 

 private String Load_Details_For_Receving(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
    String str = "", company = "", travelNo = "", mtid = "", item = "",
        lot = "";
       
    try {
    
      company = strUtils.fString(request.getParameter("PLANT"));
      travelNo = strUtils.fString(request.getParameter("TRAVELLER_NUM"));
      mtid = strUtils.fString(request.getParameter("MTID"));
      
      MLogger.printInput("company  : " + company);
      MLogger.printInput("travelNo : " + travelNo);
      MLogger.printInput("mtid     : " + mtid);
      
      str =poUtil.Load_Recv_Details(company,travelNo,mtid);   
      
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
                  
       throw e;
    }
     MLogger.log(0, "Load_Details_For_PutAway() End .............................");

    return str;
  }

 private String Is_Valid_For_Receving(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException {
    String str = "", company = "", travelNo = "", mtid = "", item = "",
        lot = "";
    try {
      company = strUtils.fString(request.getParameter("PLANT"));
      mtid = strUtils.fString(request.getParameter("MTID"));
      item = strUtils.fString(request.getParameter("SKU"));
      lot = strUtils.fString(request.getParameter("LOT"));
    
    }
    catch (Exception e) {
      MLogger.log(" %%%%%%%%%%%%%%%%%%%%%%%%% Exception :: in Action : " +
                  action + e.getMessage());
    }
    str = "";
    boolean isexists = false;
    isexists = poUtil.isValidMtidToRecv(company, mtid,item, lot);
    if (isexists) {
      str = xu.getXMLMessage(0, "Sucess");
    }
    else {
      str = xu.getXMLMessage(1, "Fail");
    }

    MLogger.log(0, "Is_Valid_For_Receving() End .............................");

    return str;
  }


private String processReceving(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException,Exception {
  MLogger.log(0, " processReceving() Starts ");
  Hashtable ht = null;

  String str = "";
  String company="",travelerNo="",mtid="",item="",lot="",qty="",status="",user= "",reasonCount="";
  String reason1="",reason2="",reason3="",reason4="",tranDate="",tranTime="";
  UserTransaction ut=null ;
  try {
  
  tranDate=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
  tranTime=DateUtils.Time();

  company = strUtils.fString(request.getParameter("PLANT"));
  user = strUtils.fString(request.getParameter("LOGIN_USER"));
  
  travelerNo = strUtils.fString(request.getParameter("TRAVELLER_NUM"));
  mtid = strUtils.fString(request.getParameter("MTID"));
  item = strUtils.fString(request.getParameter("SKU"));
  lot = strUtils.fString(request.getParameter("LOT"));
  qty = strUtils.fString(request.getParameter("QTY"));
  
  status = strUtils.fString(request.getParameter("STATUS"));
  reasonCount = strUtils.fString(request.getParameter("REASON_COUNT"));
  
  reason1 = strUtils.fString(request.getParameter("REASON1"));
  reason2 = strUtils.fString(request.getParameter("REASON2"));
  reason3 = strUtils.fString(request.getParameter("REASON3"));
  reason4 = strUtils.fString(request.getParameter("REASON3"));
  
  
  MLogger.printInput("TRAVELLER_NO :"+ travelerNo);
  MLogger.printInput("MTID         :"+ mtid);
  MLogger.printInput("SKU          :"+ item);
  MLogger.printInput("LOT NO       :"+ lot);
  MLogger.printInput("QTY          :"+ qty);
  MLogger.printInput("STATUS       :"+ status);
  MLogger.printInput("REASON_COUNT :"+ reasonCount);
  MLogger.printInput("REASON1      :"+ reason1);
  MLogger.printInput("REASON2      :"+ reason2);
  MLogger.printInput("REASON3      :"+ reason3);
  MLogger.printInput("REASON4      :"+ reason4);
  
  if(reasonCount.equalsIgnoreCase("0"))
  {
    status="SUCCESS";
  }
  else
  {
    status="FAIL";
  }
  
  ht = new Hashtable();

  ht.put("PLANT", company);
  ht.put("PONO",travelerNo);
  ht.put("LNNO",mtid);
  ht.put("ITEM",item);
  ht.put("BATNO",lot);
  ht.put("QTY",qty);
  ht.put("STATUS" ,status);
  ht.put("USERFLD1" ,"A");
  ht.put("USERFLD2" ,reason1);
  ht.put(MDbConstant.LOGIN_USER , user);
 
  
  ut = DbBean.getUserTranaction();
  ut.begin();
  boolean isInserted=false;
 // isInserted = _RecevingUtil.insertRecvHis(ht);
  
   if(isInserted)
   {
        ht.put("MOVTID","RECV");
        String date=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
        MLogger.info("After getting date : " + date);
        ht.put("CRAT",date);
        ht.put("CRTIME",tranTime);
        isInserted =  movUtil.insertMovHis(ht);
   }
   if(isInserted == true) {
        try {DbBean.CommitTran(ut);}catch (Exception ee) {}
        str = xu.getXMLMessage(0, "Receving Sucessfully"); 
   }
   else {
          try {
          DbBean.RollbackTran(ut); 
          } catch (Exception ee) {
          
          }
          str = xu.getXMLMessage(1, "Receving Failed");
   }
   
  }
  catch (Exception e) {
      try {
          DbBean.RollbackTran(ut); 
          }catch (Exception ee) {
           throw ee;
          }

   MLogger.exception("save_Data()", e);
   throw e;
  }

    MLogger.log(0, "processReceving() End .............................");


return str;
}

}
