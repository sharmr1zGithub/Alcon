package com.murho.pda;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Hashtable;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import com.murho.db.utils.InvMstUtil;
import com.murho.db.utils.MovHisUtil;
import com.murho.db.utils.PickingUtil;
import com.murho.db.utils.PrdClsUtil;
import com.murho.gates.Generator;
import com.murho.utils.DateUtils;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;


public class PickingServlet  extends HttpServlet implements SingleThreadModel{


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
 
 PickingUtil pickUtil =null;
 MovHisUtil movUtil =null;
 InvMstUtil invUtil=null;
private static final String CONTENT_TYPE = "text/xml";
private static final String PUV_PRD_GROUP = "PUV";


public void init() throws ServletException {

invUtil    = new InvMstUtil();
pickUtil      = new PickingUtil();
movUtil     = new MovHisUtil();
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
             if (action.equalsIgnoreCase("Load_Details_For_Picking")) {
        xmlStr = "";
        xmlStr = Load_Details_For_Picking(request, response);
        if (xmlStr.equalsIgnoreCase("")) {
        xmlStr = xu.getXMLMessage(1,"Details Not Found!!!");
      }
      }
       if (action.equalsIgnoreCase("Load_Lot_Details")) {
        xmlStr = "";
        xmlStr = Load_Lot_Details(request, response);

      }
       
       /*  Added by Ranjana 
        *  Purpose: LOT restriction in PDA under ticket WO0000000284867 
        *  for the process of PICKING*/
       
       if(action.equalsIgnoreCase("LOT_Blocked_Picking")){
     	   xmlStr = "";
            xmlStr = LOT_Blocked_Picking(request, response);
        }
       if (action.equalsIgnoreCase("is_valid_to_pick")) {
        xmlStr = "";
        xmlStr = is_valid_to_pick(request, response);

      }
       if (action.equalsIgnoreCase("is_valid_mtid")) {
        xmlStr = "";
        xmlStr = is_valid_mtid(request, response);

      }
       if (action.equalsIgnoreCase("load_line_details_for_Picking")) {
        xmlStr = "";
        xmlStr = load_line_details_for_Picking(request, response);

      }
       if (action.equalsIgnoreCase("load_next_line_details")) {
        xmlStr = "";
        xmlStr = load_next_line_details(request, response);

      }
      else if (action.equalsIgnoreCase("Qty_Per_Tray")) {
        xmlStr = "";
        xmlStr = get_qty_per_tray(request, response);

      }else if (action.equalsIgnoreCase("Process_Picking")) {
        xmlStr = "";
      xmlStr = ProcessPicking(request, response);

      }
      else if (action.equalsIgnoreCase("load_productgroup_details_for_Picking")) {
        xmlStr = "";
      xmlStr =  load_productgroup_details_for_Picking(request, response);

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



 private String Load_Details_For_Picking(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
    String str = "", company = "", travelNo = "",palletNo ="", mtid = "", item = "",
        lot = "",user="";
       
    try {
    
      company = strUtils.fString(request.getParameter("PLANT"));
      user = strUtils.fString(request.getParameter("LOGIN_USER")); 
      MLogger.printInput("company  : " + company);
      MLogger.printInput("user : " + user);
    
      
        str =pickUtil.Load_Picking_Details(company,user);   
      
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
                  
       throw e;
    }
   //  MLogger.log(0, "Load_Details_For_Picking() End .............................");

    return str;
  }
 
 /*  Added by Ranjana 
  *  Purpose: LOT restriction in PDA under ticket WO0000000284867 
  *  for the process of PICKING*/
 
 private String LOT_Blocked_Picking(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", travelNo = "", lot = "",TYPE="";
		try {
			  travelNo = strUtils.fString(request.getParameter("TRAVELLER"));
		      lot = strUtils.fString(request.getParameter("LOT"));
		      TYPE = "OUTBOUND";
		      
			 MLogger.printInput("LOT....."+lot);
			 MLogger.printInput("TRAVELLER....."+travelNo);
			 MLogger.printInput("TYPE....."+TYPE);			 
			 
			Hashtable ht = new Hashtable();
			ht.put("TRAVELER", travelNo);
			ht.put("LOT", lot);
			ht.put("TYPE","OUTBOUND");
			
			
			str = pickUtil.LOT_Blocked_Picking(ht);
			
			MLogger.info("************* " + str);
		} catch (Exception e) {
			MLogger.log(0, "LOT_Blocked_Picking() Failed .............................");
			MLogger.exception(this, e);
			throw e;
		}
		MLogger.log(0, "LOT_Blocked_Picking() Ends .............................");
		return str;
	}

 private String Load_Lot_Details(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
    String str = "", company = "", travelNo = "",palletNo ="", mtid = "", item = "",
        lot = "",user="";
       
    try {
    
      //company = strUtils.fString(request.getParameter("PLANT"));
     // user = strUtils.fString(request.getParameter("LOGIN_USER"));
  
      travelNo = strUtils.fString(request.getParameter("TRAVELLER_NUM"));
      palletNo = strUtils.fString(request.getParameter("PALLET"));
      item = strUtils.fString(request.getParameter("SKU"));
      lot = strUtils.fString(request.getParameter("LOT"));

      MLogger.printInput("travelNo  : " + travelNo);
      MLogger.printInput("palletNo : " + palletNo);
      MLogger.printInput("item  : " + item);
      MLogger.printInput("lot : " + lot);
    
      str =pickUtil.Load_Lot_Details(travelNo,palletNo,item,lot);   
      
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
                  
       throw e;
    }
     MLogger.log(0, "Load_Lot_Details() End .............................");

    return str;
  }


 private String is_valid_to_pick(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
        String str = "", company = "", travelNo = "",palletNo ="", mtid = "", item = "",sino="",
        lot = "",user="",loc ="",fulltray ="",full="";
     try {
     mtid  = strUtils.fString(request.getParameter("MTID"));
      travelNo = strUtils.fString(request.getParameter("TRAVELLER_NUM"));
      palletNo = strUtils.fString(request.getParameter("PALLET"));
      item = strUtils.fString(request.getParameter("SKU"));
      lot = strUtils.fString(request.getParameter("LOT"));
      loc = strUtils.fString(request.getParameter("LOC"));
       full = strUtils.fString(request.getParameter("FULL"));
       fulltray = strUtils.fString(request.getParameter("FULL_TRAY"));
       sino = strUtils.fString(request.getParameter("SINO"));
      
      Hashtable ht = new Hashtable();
       ht.put("Traveler_Id",travelNo);
       ht.put(MDbConstant.PALLET,palletNo);
        
       ht.put(MDbConstant.ITEM,item);
       ht.put(MDbConstant.LOT_NUM,lot);
       ht.put(MDbConstant.LOC,loc); 
        ht.put("SINO",sino); 
        ht.put("FULLTRAY",full); 
    //   ht.put(MDbConstant.STATUS,"<> C");
      str =pickUtil.isValidToPick(ht);
  
      
       MLogger.info("************* " +  str);
      
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
                  
       throw e;
    }
   
    return str;
  }
 private String is_valid_mtid(HttpServletRequest request,
         HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", company = "", travelNo = "", palletNo = "", mtid = "", item = "", fulltray = "", lot = "", user = "", loc = "";
		try {
			mtid = strUtils.fString(request.getParameter("MTID"));
			item = strUtils.fString(request.getParameter("SKU"));
			lot = strUtils.fString(request.getParameter("LOT"));
			fulltray = strUtils.fString(request.getParameter("FULL_TRAY"));
			loc = strUtils.fString(request.getParameter("LOC"));

			MLogger.printInput("MTID :" + mtid);
			MLogger.printInput("SKU :" + item);
			MLogger.printInput("LOT         :" + lot);
			MLogger.printInput("LOC       :" + loc);
			MLogger.printInput("FULL_TRAY :" + fulltray);

			Hashtable ht = new Hashtable();

			ht.put(MDbConstant.ITEM, item);
			ht.put(MDbConstant.LOT_NUM, lot);
			ht.put(MDbConstant.LOC, loc);
			if (fulltray.equalsIgnoreCase("1")) {

				ht.put(MDbConstant.MTID, mtid);
				str = pickUtil.isValidFullTray(item, ht);

			} else {
				// if not PUV product, validate for partial tray
				// #981 change.. allow to pick any tray for PUV products added
				// by Arun on 18 Oct 2010
				String item_prd_grp = pickUtil.getProductGroup(item);
				// String puv_prd_grp ="PUV";
				// String item_prd_cls = "PS0009";
				if (!item_prd_grp.equalsIgnoreCase(PUV_PRD_GROUP)) {

					String smtid = pickUtil.isExistsPartialTray(item, ht);

					if (smtid.length() > 0) {
						MLogger.log("mtid::::::::;;;" + mtid);
						MLogger.log("smtid::::::::;;;" + smtid);
						if (strUtils.fString(smtid).equalsIgnoreCase(mtid)) {
							ht.put(MDbConstant.MTID, mtid);
							str = pickUtil.isValidMtid(ht);
						} else {
							str = xu.getXMLMessage(1,
									" Pick the Partial Tray First !");
						}
					} else {
						ht.put(MDbConstant.MTID, mtid);
						str = pickUtil.isValidMtid(ht);
					}
				} else { // prodct is type of PUV
					MLogger.log("mtid::::::::;;;" + mtid);
					ht.put(MDbConstant.MTID, mtid);
					str = pickUtil.isValidMtid(ht);
				}
			}

			MLogger.info("************* " + str);

		} catch (Exception e) {
			MLogger.exception(this, e);
			;

			throw e;

		}

		return str;
	}
  
 
   private String get_qty_per_tray(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", 
      plant   = "",sku="";
     try {
    
      plant = strUtils.fString(request.getParameter("PLANT"));
      sku = strUtils.fString(request.getParameter("SKU"));
      str =new PrdClsUtil().GetQtyPerTray(plant,sku);  
       MLogger.info("************* " +  str);    
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
      throw e;
    }
    return str;
  }
  
   private String load_line_details_for_Picking(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", 
      plant   = "",sku="";
       String  company = "", travelNo = "",palletNo ="", mtid = "", item = "",
        lot = "",user="",loc="",sino="";
     try {
    
       travelNo = strUtils.fString(request.getParameter("TRAVELLER_NUM"));
      palletNo = strUtils.fString(request.getParameter("PALLET"));
      item = strUtils.fString(request.getParameter("SKU"));
      lot = strUtils.fString(request.getParameter("LOT"));
       loc = strUtils.fString(request.getParameter("LOC"));
        sino = strUtils.fString(request.getParameter("SINO"));
      str =pickUtil.Load_Line_Details_For_Picking(travelNo,palletNo,lot,item,loc,sino);  
       MLogger.info("************* " +  str);    
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
      throw e;
    }
    return str;
  }
  
    private String load_next_line_details(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", 
      plant   = "",sku="";
       String  company = "", travelNo = "",palletNo ="", mtid = "", item = "",
        lot = "",user="",loc="",sino="";
     try {
    
       travelNo = strUtils.fString(request.getParameter("TRAVELLER_NUM"));
       palletNo = strUtils.fString(request.getParameter("PALLET"));
       sino = strUtils.fString(request.getParameter("SINO"));
       user = strUtils.fString(request.getParameter("LOGIN_USER"));
      str =pickUtil.load_next_line_details(travelNo,palletNo,sino,user);  
       MLogger.info("************* " +  str);    
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
      throw e;
    }
    return str;
  }
  
  
   private String load_productgroup_details_for_Picking(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", 
      plant   = "",sku="";
       String  company = "", item = "";
        
     try {
    
      // plant = strUtils.fString(request.getParameter("PLANT"));
      item = strUtils.fString(request.getParameter("SKU"));
    
      str =pickUtil.Load_PrdouctGroup_Details_For_Picking(item);  
       MLogger.info("************* " +  str);    
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
      throw e;
    }
    return str;
  }
  
private String ProcessPicking(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException,Exception {
  MLogger.log(0, " Process_Picking() Starts ");
  HashMap picking_HM = null;

  String str = "";
  String company="",travelerNo="",mtid="",item="",lot="",qty="",qtyInv="",status="",user= "",palletNo="",loc="",tranDate="",srlnno="";
  String reason1="",reason2="",reason3="",reason4="",srno="",qtyPk="",fullTray="",tranTime="";
  UserTransaction ut=null ;
  try {
 tranDate=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
 tranTime=DateUtils.Time();
 
 company = strUtils.fString(request.getParameter("PLANT"));

//company = CibaConstants.cibacompanyName;

  user = strUtils.fString(request.getParameter("LOGIN_USER"));
  
  travelerNo = strUtils.fString(request.getParameter("TRAVELLER_NUM"));
  palletNo = strUtils.fString(request.getParameter("PALLET"));
  item = strUtils.fString(request.getParameter("SKU"));
  lot = strUtils.fString(request.getParameter("LOT"));
   loc = strUtils.fString(request.getParameter("LOC"));
   srno = strUtils.fString(request.getParameter("SRNO"));
   srlnno = strUtils.fString(request.getParameter("SRLNO"));
     fullTray = strUtils.fString(request.getParameter("FULL_TRAY"));
   mtid = strUtils.fString(request.getParameter("MTID"));
  qty = strUtils.fString(request.getParameter("QTY"));
  qtyPk = strUtils.fString(request.getParameter("PICK_QTY"));
  String invQty = strUtils.fString(request.getParameter("INVQTY"));
  
  status = strUtils.fString(request.getParameter("STATUS"));

 
  
  
  MLogger.printInput("TRAVELLER_NO :"+ travelerNo);
   MLogger.printInput("PALLET_NUM :"+ palletNo);
  MLogger.printInput("MTID         :"+ mtid);
  MLogger.printInput("SKU          :"+ item);
  MLogger.printInput("LOT NO       :"+ lot);
  MLogger.printInput("LOC       :"+ loc);
  MLogger.printInput("SRNO       :"+ srno);
  MLogger.printInput("SRLNO       :"+ srlnno);
  MLogger.printInput("fullTray       :"+ fullTray);
  MLogger.printInput("QTY          :"+ qty);
  MLogger.printInput("QTYPK        :"+ qtyPk);
    MLogger.printInput("INVQTY        :"+ invQty);
  MLogger.printInput("STATUS       :"+ status);
  
 
    
  picking_HM = new HashMap();

  MLogger.info( "Input values START ...................");
 // issueMaterial_HM.put(MDbConstant.PLANT, company);
  picking_HM.put("sino",srno);
  picking_HM.put("sinoln",srlnno);
  picking_HM.put("FULL_TRAY",fullTray);
  picking_HM.put(MDbConstant.TRAVELER_NUM,travelerNo);
  picking_HM.put(MDbConstant.PALLET,palletNo);
  picking_HM.put(MDbConstant.ITEM,item);
  picking_HM.put(MDbConstant.LOT_NUM,lot);
  picking_HM.put(MDbConstant.LOC,loc);
   picking_HM.put(MDbConstant.MTID,mtid);
  picking_HM.put("PickQty",qtyPk);
   picking_HM.put("INVQTY",invQty);
  picking_HM.put(MDbConstant.LOGIN_USER , user);
  picking_HM.put("Status" , status);
  picking_HM.put(MDbConstant.MOVEHIS_CR_DATE , tranDate);
  picking_HM.put("CRTIME", tranTime);
  
          
  MLogger.info( "Input values END ...................");
  xmlStr = "";
   
  xmlStr = pickUtil.Process_Picking(picking_HM);
        
 }
    catch (Exception e) {
      MLogger.exception(this,e);
      throw new Exception("Unable to Picking() Exception : " + e.getMessage());
    }
    MLogger.info( "*************** process_Picking() << ENDS >> ***************");
    return xmlStr;
  }

}
