
package com.murho.pda;

//import com.murho.dao.OBTravelerDetDAO;
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

import com.murho.db.utils.InvMstUtil;
import com.murho.db.utils.MovHisUtil;
import com.murho.db.utils.PickingUtil;
import com.murho.db.utils.TrayLabelingUtil;
import com.murho.gates.Generator;
import com.murho.utils.DateUtils;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;


public class TrayLabelingServlet extends HttpServlet{


Context ctx = null;
XMLUtils xu = new XMLUtils();
MLogger logger = new MLogger();
//StrUtils strUtils = new StrUtils();
Generator generator = new Generator();

String plant = "";

public static final boolean DEBUG = true;

String xmlStr = "";
String action = "";
 
MovHisUtil movUtil =null;
InvMstUtil invUtil=null;
TrayLabelingUtil _trayLabelUtil = null;
private static final String CONTENT_TYPE = "text/xml";

public void init() throws ServletException {

invUtil    = new InvMstUtil();
movUtil     = new MovHisUtil();
_trayLabelUtil    = new TrayLabelingUtil();


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
      if (action.equalsIgnoreCase("load_Reference_List")) {
        xmlStr = "";
        xmlStr = load_Reference_List(request, response);
      }
      else if (action.equalsIgnoreCase("load_traveler_for_refno")) {
        xmlStr = "";
        xmlStr = load_traveler_for_refno(request, response);
      }
       else if (action.equalsIgnoreCase("load_tray_for_pallet")) {
        xmlStr = "";
        xmlStr = load_tray_for_pallet(request, response);
      }
     else if (action.equalsIgnoreCase("load_Traveller_List")) {
        xmlStr = "";
        xmlStr = load_Traveller_List(request, response);
      }
      else if (action.equalsIgnoreCase("Load_MTID_Details")) {
        xmlStr = "";
        xmlStr = LoadMTIDDetails(request, response);
      }
      else if (action.equalsIgnoreCase("is_valid_to_print")) {
        xmlStr = "";
        xmlStr = is_valid_to_print(request, response);
      }
       else if (action.equalsIgnoreCase("Qty_to_print")) {
        xmlStr = "";
        xmlStr = get_qty_to_print(request, response);
      }
       else if (action.equalsIgnoreCase("Lot_for_sl")) {
        xmlStr = "";
        xmlStr = get_Lot_for_Sl(request, response);
      }
      
      else if (action.equalsIgnoreCase("load_all_pallets")) {
        xmlStr = "";
        xmlStr = LoadPalletForTrayLabeling(request, response);
      }
      
        else if (action.equalsIgnoreCase("load_pallets")) {
        xmlStr = "";
        xmlStr = LoadPalletForRePrint(request, response);
      }
      
      else if (action.equalsIgnoreCase("Process_Tray_Labeling")) {
        xmlStr = "";
        xmlStr = Process_Tray_Labeling(request, response);

      }
      // Block added by Arun for #1848
      else if (action.equalsIgnoreCase("Process_Print_Shipping_Mark")) {
          xmlStr = "";
          xmlStr = Process_Print_Shipping_Mark(request, response);
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
 
 //#########################################################//
 
 
//Method added by Arun for #1848
 
 private String Process_Print_Shipping_Mark(HttpServletRequest request,
         HttpServletResponse response) throws
 IOException, ServletException,
			Exception {
		String str = "", company = "", travelerNum = "", loginUser = "", palletId="";
		
		try {

			plant = StrUtils.fString(request.getParameter("PLANT"));
			loginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
			travelerNum = StrUtils.fString(request.getParameter("TRAVELLER_NUM"));
			palletId = StrUtils.fString(request.getParameter("PALLET_NUM"));

			MLogger.printInput("company      : " + company);
			MLogger.printInput("LOGIN_USER   : " + loginUser);
			MLogger.printInput("TRAVELER_NUM : " + travelerNum);
			MLogger.printInput("PALLET : " + palletId);

			str = _trayLabelUtil.Process_Print_Shipping_Mark(plant, travelerNum, palletId);
			/*if(flag){
				xmlStr = xu.getXMLMessage(0,"Shipping Mark printed successfully!");
			}*/

		} catch (Exception e) {
			MLogger.exception(this, e);
			throw e;
		}
		return str;
}
 
 private String load_Reference_List(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", 
      company   = "", 
      loginUser = "";
   
       
    try {
      
      loginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
      company = StrUtils.fString(request.getParameter("PLANT"));
     
      MLogger.printInput("company    : " + company);
      MLogger.printInput("login user : " + loginUser);
     
      
      str =_trayLabelUtil.getReferenceList();   
      
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
                  
       throw e;
    }
     MLogger.log(0, "load_Traveller_List() End .............................");

    return str;
  }
  
 private String load_Traveller_List(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", 
      company   = "", 
      loginUser = "";
   
       
    try {
      
      loginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
      company = StrUtils.fString(request.getParameter("PLANT"));
     
      MLogger.printInput("company    : " + company);
      MLogger.printInput("login user : " + loginUser);
     
      
      str =_trayLabelUtil.getTravelersList();   
      
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
                  
       throw e;
    }
     MLogger.log(0, "load_Traveller_List() End .............................");

    return str;
  }
  
  private String load_traveler_for_refno(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
   String str  = "", 
   company     = "", 
   refNum      = "",
   loginUser   = "",
   traveler    ="";
 
       
    try {
      
      loginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
      company = StrUtils.fString(request.getParameter("PLANT"));
      refNum = StrUtils.fString(request.getParameter("REFNO"));
      
      traveler = StrUtils.fString(request.getParameter("TRAVELER"));
     
      MLogger.printInput("company      : " + company);
      MLogger.printInput("login user   : " + loginUser);
      MLogger.printInput("REFNO : " + traveler);
 
      str =_trayLabelUtil.getTravelerListForRef(refNum);  
    
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
      throw e;
    }
    return str;
  }
  
   private String load_tray_for_pallet(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
    String str  = "", company     = "", travelerNum="",loginUser = "", refno ="",  pallet = "", trayid ="" ;
          
    try {
      
      plant = StrUtils.fString(request.getParameter("PLANT"));
      loginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
      refno = StrUtils.fString(request.getParameter("REF_NUM"));
      travelerNum = StrUtils.fString(request.getParameter("TRAVELLER_NUM"));
      pallet = StrUtils.fString(request.getParameter("PALLET_NUM"));  
      trayid = StrUtils.fString(request.getParameter("TRAYID"));
     
      MLogger.printInput("company      : " + company);
      MLogger.printInput("login user   : " + loginUser);
      MLogger.printInput("REF_NUM : " + refno);
      MLogger.printInput("TRAVELER_NUM : " + travelerNum);
      MLogger.printInput("PALLET_NUM : " + pallet);
      MLogger.printInput("TRAYID : " + trayid);
      
      str =_trayLabelUtil.getTrayListForPallet(refno,travelerNum,pallet,trayid);   
      
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
      throw e;
    }
    return str;
  }
  
  private String LoadPalletForTrayLabeling(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "",  company   = "",travelerNum="",loginUser = "";
    try {
      
      loginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
      company = StrUtils.fString(request.getParameter("PLANT"));
      travelerNum = StrUtils.fString(request.getParameter("TRAVELER_NUM"));
     
      MLogger.printInput("company      : " + company);
      MLogger.printInput("login user   : " + loginUser);
      MLogger.printInput("TRAVELER_NUM : " + travelerNum);
      
      str =_trayLabelUtil.getPalletList(travelerNum,"N");   
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
      throw e;
    }
    return str;
  }
  
   private String LoadPalletForRePrint(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", company   = "", travelerNum="",loginUser = "";
  
      try {
      
      loginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
      company = StrUtils.fString(request.getParameter("PLANT"));
      travelerNum = StrUtils.fString(request.getParameter("TRAVELER_NUM"));
     
      MLogger.printInput("company      : " + company);
      MLogger.printInput("login user   : " + loginUser);
      MLogger.printInput("TRAVELER_NUM : " + travelerNum);
      
      str =_trayLabelUtil.getPalletList(travelerNum,"C");       
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
      String str  = "",  plant   = "",travelerNum="", pallet="",MTID="", loginUser = "";
        
    try {
    
      plant = StrUtils.fString(request.getParameter("PLANT"));
      loginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
      travelerNum = StrUtils.fString(request.getParameter("TRAVELLER_NUM"));
      pallet = StrUtils.fString(request.getParameter("PALLET_NUM"));
      MTID = StrUtils.fString(request.getParameter("MTID"));
     // LOT=StrUtils.fString(request.getParameter("LOT"));
     
      MLogger.printInput("Plant       : " + plant);
      MLogger.printInput("Login user  : " + loginUser);
      MLogger.printInput("TravelerNum : " + travelerNum);
      MLogger.printInput("Pallet      : " + pallet);
      MLogger.printInput("MTID        : " + MTID);
   
      str =_trayLabelUtil.getMTIDDetails(travelerNum,pallet,MTID);   
      
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
      throw e;
    }
    return str;
  }
 
 
    private String get_qty_to_print(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", 
      sku="",trayid="",palletNo="",travelNo ="";
     try {
    
      travelNo = StrUtils.fString(request.getParameter("TRAVELLER_NUM"));
      palletNo = StrUtils.fString(request.getParameter("PALLET_NUM"));
      sku = StrUtils.fString(request.getParameter("SKU"));
      trayid = StrUtils.fString(request.getParameter("TRAYID"));
      //OBTravelerDetDAO dao = new  OBTravelerDetDAO();
      
       Hashtable ht = new Hashtable();
       ht.put("Traveler_Id",travelNo);
       ht.put("PALLETID",palletNo);
       ht.put(MDbConstant.ITEM,sku);
       ht.put(MDbConstant.TRAYID,trayid);
       str =_trayLabelUtil.getSumOfSkuQty(ht);  
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
      throw e;
    }
    return str;
  }
  
    private String get_Lot_for_Sl(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", sku="",trayid="",palletNo="",travelNo ="",lottype="";
      try {
    
        travelNo = StrUtils.fString(request.getParameter("TRAVELLER_NUM"));
        palletNo = StrUtils.fString(request.getParameter("PALLET_NUM"));
        sku = StrUtils.fString(request.getParameter("SKU"));
        trayid = StrUtils.fString(request.getParameter("TRAYID"));
        lottype = StrUtils.fString(request.getParameter("LOTTYPE"));
        //OBTravelerDetDAO dao = new  OBTravelerDetDAO();
        
        Hashtable ht = new Hashtable();
        ht.put("Traveler_Id",travelNo);
        ht.put("PALLETID",palletNo);
        ht.put(MDbConstant.ITEM,sku);
        ht.put("LOTTYPE",lottype);
        ht.put(MDbConstant.TRAYID,trayid);
        str =_trayLabelUtil.getLotForSL(ht);  
     }
    catch (Exception e) {
      MLogger.exception(this,e);;
      throw e;
    }
    return str;
  }
 private String is_valid_to_print(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
        String str = "",  travelNo = "",palletNo ="",  item = "",trayid ="";
      try {
     
      travelNo = StrUtils.fString(request.getParameter("TRAVELLER_NUM"));
      palletNo = StrUtils.fString(request.getParameter("PALLET_NUM"));
      item = StrUtils.fString(request.getParameter("SKU"));
      trayid = StrUtils.fString(request.getParameter("TRAYID"));
      //OBTravelerDetDAO dao = new  OBTravelerDetDAO();
      
      Hashtable ht = new Hashtable();
      ht.put("Traveler_Id",travelNo);
      ht.put("PALLETID",palletNo);
      if(item.length() >0){
      ht.put(MDbConstant.ITEM,item);
      }
      ht.put(MDbConstant.TRAYID,trayid);
      str =new PickingUtil().isValidToPrint(ht);
    }
    catch (Exception e) {
      MLogger.exception(this,e);;            
      throw e;
    }
    return str;
  }
  

 private String Process_Tray_Labeling(HttpServletRequest request,
                           HttpServletResponse response) throws IOException,
      ServletException, Exception {
    
      MLogger.info("*************** Process_Tray_Labeling() << STARTS >> ***************");

  Map trayDet_HM=null;
  //String str = "";
  String travelerNo="",pallet="",mtid="",item="",lot="",status="",user= "";
  String ordQty="",recvQty="",tranDate="",tranTime="",trayId="";
  try {
  
  tranDate=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
  tranTime=DateUtils.Time();
    
  //company = StrUtils.fString(request.getParameter("PLANT"));
  user = StrUtils.fString(request.getParameter("LOGIN_USER"));
  
  travelerNo = StrUtils.fString(request.getParameter("TRAVELLER_NUM"));
  pallet     = StrUtils.fString(request.getParameter("PALLET"));
  trayId       = StrUtils.fString(request.getParameter("TRAYID"));
  mtid       = StrUtils.fString(request.getParameter("MTID"));
  item       = StrUtils.fString(request.getParameter("SKU"));
  lot        = StrUtils.fString(request.getParameter("LOT"));
  recvQty    = StrUtils.fString(request.getParameter("RECV_QTY"));
  ordQty     = StrUtils.fString(request.getParameter("ORD_QTY"));
  String fulltray     = StrUtils.fString(request.getParameter("FULLTRAY"));
  //Added BY samatha to store the Barcode value of the lot in ob_travle_det on Feb 02
  String lotBcode     = StrUtils.fString(request.getParameter("LOTBCODEVAL"));
  
  status="C";
  if(Integer.parseInt(ordQty)==Integer.parseInt(recvQty) ) status="C";
  MLogger.info("TrayDet status :" + status);
  //reasonCount = StrUtils.fString(request.getParameter("REASON_COUNT"));
  
  //reason1 = StrUtils.fString(request.getParameter("REASON1"));
  //reason2 = StrUtils.fString(request.getParameter("REASON2"));
  //reason3 = StrUtils.fString(request.getParameter("REASON3"));
  //reason4 = StrUtils.fString(request.getParameter("REASON3"));
  
  
  MLogger.printInput("TRAVELLER_NO :"+ travelerNo);
  MLogger.printInput("PALLET       :"+ pallet);
  MLogger.printInput("MTID         :"+ mtid);
  MLogger.printInput("TRAYID         :"+ trayId);
  MLogger.printInput("SKU          :"+ item);
  MLogger.printInput("LOT NO       :"+ lot);
  MLogger.printInput("ORD_QTY      :"+ ordQty);
  MLogger.printInput("RECV_QTY     :"+ recvQty);
  MLogger.printInput("FULLTRAY     :"+ fulltray);
  MLogger.printInput("RECV_STATUS  :"+ status);
    MLogger.printInput("LOTBCODEVAL  :"+ lotBcode);

    
  trayDet_HM = new HashMap();
  trayDet_HM.put(MDbConstant.TRAVELER_ID,travelerNo);
  trayDet_HM.put(MDbConstant.PALLET,pallet);
  trayDet_HM.put(MDbConstant.MTID,mtid);
  trayDet_HM.put(MDbConstant.TRAYID,trayId);
  trayDet_HM.put(MDbConstant.ITEM,item);
  trayDet_HM.put(MDbConstant.LOT_NUM,lot);
  trayDet_HM.put("LOTBARCODEVAL",lotBcode);
  trayDet_HM.put(MDbConstant.RECV_QTY,recvQty);
  trayDet_HM.put("FULLTRAY",fulltray);
  trayDet_HM.put(MDbConstant.LOGIN_USER , user);
  trayDet_HM.put(MDbConstant.TrayDetStatus , status);
  trayDet_HM.put(MDbConstant.MOVEHIS_CR_DATE , tranDate);
  trayDet_HM.put(MDbConstant.CRTIME , tranTime);
  trayDet_HM.put(MDbConstant.ORD_QTY, ordQty); // added for #35550
 
  xmlStr = "";
  xmlStr = _trayLabelUtil.Process_Tray_Labeling(trayDet_HM); 
        
 }
    catch (Exception e) {
      MLogger.exception(this,e);
      throw new Exception("Unable to Tray Label() Exception : " + e.getMessage());
    }
    MLogger.info( "*************** Process_Tray_Labeling() << ENDS >> ***************");
    return xmlStr;
  }

}
