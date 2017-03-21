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

import com.murho.dao.RecvDetDAO;
import com.murho.dao.SQLRecvDet_DAO;
import com.murho.db.utils.InvMstUtil;
import com.murho.db.utils.MovHisUtil;
import com.murho.db.utils.RecevingUtil;
import com.murho.gates.Generator;
import com.murho.utils.CibaConstants;
import com.murho.utils.DateUtils;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;


public class ReceivingServlet extends HttpServlet{


Context ctx = null;
XMLUtils xu = new XMLUtils();
MLogger logger = new MLogger();
StrUtils strUtils = new StrUtils();
Generator generator = new Generator();

String plant = "";

public static final boolean DEBUG = true;

String xmlStr = "";
String action = "";

 MovHisUtil movUtil =null;
 InvMstUtil invUtil=null;
 RecevingUtil _RecevingUtil = null;
 private static final String CONTENT_TYPE = "text/xml";

public void init() throws ServletException {

invUtil    = new InvMstUtil();
movUtil     = new MovHisUtil();
_RecevingUtil    = new RecevingUtil();


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
      
      
      if (action.equalsIgnoreCase("load_incomigTraveller_details")) {
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
      
      /*  Added by Ranjana 
       *  Purpose: LOT restriction in PDA under ticket WO0000000284867 
       *  for the process of RECEIVING*/
      
     if(action.equalsIgnoreCase("LOT_Blocked_Receiving")){
   	   xmlStr = "";
          xmlStr = LOT_Blocked_Receiving(request, response);
      }
      
      else if (action.equalsIgnoreCase("Process_Receving")) {
        xmlStr = "";
        xmlStr = process_Receiving(request, response);

      }
    }
    catch (Exception e) {
       MLogger.info("Generating Exception message for PDA");
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
 
 //######################################################### // 
 
 private String LoadOpenTraveler(HttpServletRequest request,
                              HttpServletResponse response) throws IOException, ServletException , Exception{
    String str  = "",  company   = "", loginUser = "";
       
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
    return str;
  }
  
  private String LoadPallet(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "",  company   = "",  travelerNum="", loginUser = "";
        
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
    return str;
  }
  
   private String LoadMTIDDetails(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", plant   = "",travelerNum="", pallet="", MTID="",loginUser = "";
       
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
         MLogger.exception(this,e);          
         throw e;
    }
     return str;
  }
 
  private String process_Receiving(HttpServletRequest request,
                           HttpServletResponse response) throws IOException, ServletException, Exception {
    
      MLogger.info("*************** process_Receiving() << STARTS >> ***************");
      
      boolean chkPUV=false;
      SQLRecvDet_DAO _SQLRecvDet = new SQLRecvDet_DAO();	
      Map RecvMaterial_HM=null;
      String str = "";
      String company="",travelerNo="",pallet="",mtid="",item="",lot="",status="",user= "",reasonCount="";
      String reason1="",reason2="",reason3="",reason4="",ordQty="",recvQty="",tranDate="",tranTime="",product_type="";
      int calqty=0,calqty1=0;
      try {
      
      tranDate=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
      tranTime=DateUtils.Time();
     // company = strUtils.fString(request.getParameter("PLANT"));
      company =CibaConstants.cibacompanyName;
      user = strUtils.fString(request.getParameter("LOGIN_USER"));
      travelerNo = strUtils.fString(request.getParameter("TRAVELLER_NUM"));
      pallet     = strUtils.fString(request.getParameter("PALLET"));
      mtid       = strUtils.fString(request.getParameter("MTID"));
      item       = strUtils.fString(request.getParameter("SKU"));
      lot        = strUtils.fString(request.getParameter("LOT"));
      recvQty    = strUtils.fString(request.getParameter("RECV_QTY"));
      ordQty     = strUtils.fString(request.getParameter("ORD_QTY"));
      //below line is added on 19-march-2014 for UDI implementation
      product_type = strUtils.fString(request.getParameter("Product_2D"));
      
      String expiryDate =strUtils.fString(new RecvDetDAO().getExpiryDateForLot(item,lot,mtid,travelerNo));
    
      chkPUV= _SQLRecvDet.checkPUV(item);
         
      MLogger.info("ordQty==recvQty " + ordQty+ " == "+ recvQty);
      status="D";
    //  if(Integer.parseInt(ordQty)==Integer.parseInt(recvQty) ) status="C";
      if(chkPUV==true)
      {
         
        calqty=Integer.parseInt(ordQty)+5;
    
        if(Integer.parseInt(ordQty)==Integer.parseInt(recvQty) || Integer.parseInt(recvQty)==calqty || Integer.parseInt(recvQty)==calqty-1 || Integer.parseInt(recvQty)==calqty-2 || Integer.parseInt(recvQty)==calqty-3|| Integer.parseInt(recvQty)==calqty-4)
        {
           status="C";
        }
        else
        {
          status="D";
        }
        
      }
      else if(chkPUV==false)
      {
        if(Integer.parseInt(ordQty)==Integer.parseInt(recvQty) ) status="C";
      }
    
      
      MLogger.info("recvDet status :" + status);
      reasonCount = strUtils.fString(request.getParameter("REASON_COUNT"));
      
      reason1 = strUtils.fString(request.getParameter("REASON1"));
      reason2 = strUtils.fString(request.getParameter("REASON2"));
      reason3 = strUtils.fString(request.getParameter("REASON3"));
      reason4 = strUtils.fString(request.getParameter("REASON3"));
      
      
      MLogger.printInput("TRAVELLER_NO :"+ travelerNo);
      MLogger.printInput("PALLET       :"+ pallet);
      MLogger.printInput("MTID         :"+ mtid);
      MLogger.printInput("SKU          :"+ item);
      MLogger.printInput("LOT NO       :"+ lot);
      MLogger.printInput("ORD_QTY      :"+ ordQty);
      MLogger.printInput("RECV_QTY     :"+ recvQty);
      MLogger.printInput("RECV_STATUS  :"+ status);
      MLogger.printInput("REASON_COUNT :"+ reasonCount);
      MLogger.printInput("REASON1      :"+ reason1);
      MLogger.printInput("REASON2      :"+ reason2);
      MLogger.printInput("REASON3      :"+ reason3);
      MLogger.printInput("REASON4      :"+ reason4);
      MLogger.printInput("EXPDATE      :"+ expiryDate);
      MLogger.printInput("Product_2D   :"+ product_type);
        
      RecvMaterial_HM = new HashMap();
    
      MLogger.info( "Input values START ...................");
      RecvMaterial_HM.put(MDbConstant.TRAVELER_NUM,travelerNo);
      RecvMaterial_HM.put(MDbConstant.PALLET,pallet);
      RecvMaterial_HM.put(MDbConstant.MTID,mtid);
      RecvMaterial_HM.put(MDbConstant.ITEM,item);
      RecvMaterial_HM.put(MDbConstant.LOT_NUM,lot);
      RecvMaterial_HM.put(MDbConstant.RECV_QTY,recvQty);
      RecvMaterial_HM.put(MDbConstant.LOGIN_USER , user);
      RecvMaterial_HM.put(MDbConstant.ReceiveStatus , status);
      RecvMaterial_HM.put(MDbConstant.MOVEHIS_CR_DATE , tranDate);
      RecvMaterial_HM.put(MDbConstant.CRTIME , tranTime);
       RecvMaterial_HM.put("EXPIRYDT" , expiryDate);
       //below line is added on 19-march-2014 for UDI implementation
       RecvMaterial_HM.put("PRODUCTTYP",product_type);
      MLogger.info( "Input values END ...................");
      
      xmlStr = "";
      xmlStr = _RecevingUtil.Process_Receiving(RecvMaterial_HM);
    }
    catch (Exception e) {
      MLogger.exception(this,e);
      throw new Exception("Unable to Receive() Exception : " + e.getMessage());
    }
    MLogger.info( "*************** process_issueMaterial() << ENDS >> ***************");
    return xmlStr;
  }

  /*  Added by Ranjana 
   *  Purpose: LOT restriction in PDA under ticket WO0000000284867 
   *  for the process of RECEIVING*/
  
  private String LOT_Blocked_Receiving(HttpServletRequest request, HttpServletResponse response) {
	  String str = "", Traveler = "", lot = "",TYPE="";
		try {
			  Traveler = strUtils.fString(request.getParameter("TRAVELER"));
		      lot = strUtils.fString(request.getParameter("LOT"));
		      TYPE = "INBOUND";
		   			 
			Hashtable ht = new Hashtable();
			ht.put("TRAVELER", Traveler);
			ht.put("LOT", lot);
			ht.put("TYPE","INBOUND");
						
			str = _RecevingUtil.LOT_Blocked_Receiving(ht);
			
			MLogger.info("************* " + str);
		} catch (Exception e) {
			MLogger.log(0, "LOT_Blocked_Receiving() Failed .............................");
			MLogger.exception(this, e);
			
		}
		MLogger.log(0, "LOT_Blocked_Receiving() Ends .............................");
		return str;
	}

}
