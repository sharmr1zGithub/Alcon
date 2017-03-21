package com.murho.db.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.transaction.UserTransaction;

import com.murho.dao.BaseDAO;
import com.murho.dao.InvMstDAO;
import com.murho.dao.ItemMstDAO;
import com.murho.dao.MovHisDAO;
import com.murho.dao.OBTravelerDetDAO;
import com.murho.dao.PickingDAO;
import com.murho.dao.RestrictLotDAO;
import com.murho.dao.TblControlDAO;
import com.murho.dao.TrayDetDAO;
import com.murho.gates.DbBean;
import com.murho.tran.WmsPicking;
import com.murho.tran.WmsTran;
import com.murho.utils.CibaConstants;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;


public class PickingUtil {
  com.murho.utils.XMLUtils xu = null;
  com.murho.utils.StrUtils strUtils = null;
  public PickingUtil() {
    xu = new XMLUtils();
    strUtils = new StrUtils();
   
  }

  public String Load_Picking_Details(String company,String aUser) throws Exception{
    String xmlStr = "";
    ArrayList al = null;
    TrayDetDAO dao = new TrayDetDAO(); 
     //query
     String query= "select distinct a.sino,a.traveler_id,pallet,sku,a.lot, sum(a.Qty) as Qty,isnull(sum(pickQty),0) as PickQty,loc ,fulltray"+
      " from ob_travel_det a,ob_travel_hdr b where a.traveler_id =b.traveler_id and  "+
      " a.AssignedUser = '"+aUser+"' and a.status ='N' " +
      " group by a.sino,a.traveler_id,pallet,sku,lot,loc,fulltray order by a.traveler_id,pallet,loc ";

    //condition
    Hashtable ht=new Hashtable();
    try {
       al = dao.loadDetails(query,ht); 
        if (al.size() > 0) {
        xmlStr += xu.getXMLHeader();
        xmlStr +=
            xu.getStartNode("PickingDetails total='" + String.valueOf(al.size()) +
                            "'");
        for (int i = 0; i < al.size(); i++) {
          Map map = (Map) al.get(i);
          xmlStr += xu.getStartNode("record");
           xmlStr = xmlStr + xu.getXMLNode("SINO",(String) map.get("sino"));
            xmlStr = xmlStr + xu.getXMLNode("FULLTRAY",(String) map.get("fulltray"));
              xmlStr = xmlStr + xu.getXMLNode("TRAVELER",(String) map.get("traveler_id"));
              xmlStr = xmlStr + xu.getXMLNode("PALLET",(String) map.get("pallet"));
              xmlStr = xmlStr + xu.getXMLNode("SKU",(String) map.get("sku"));
              xmlStr = xmlStr + xu.getXMLNode("LOT",(String) map.get("lot"));
              xmlStr = xmlStr + xu.getXMLNode("QTY",(String) map.get("Qty"));
              xmlStr = xmlStr + xu.getXMLNode("LOC",(String) map.get("loc"));
              xmlStr = xmlStr + xu.getXMLNode("PKQTY",(String) map.get("PickQty"));
              xmlStr += xu.getEndNode("record");
        }
          xmlStr += xu.getEndNode("PickingDetails");
      }
     }
    catch (Exception e) {
      MLogger.exception("Load_Picking_Details()", e);
      throw e;
    }
    return xmlStr;
  }
   
   
    public ArrayList get_sino_line_Details(String trav,Hashtable ht) throws Exception{
      String xmlStr = "";
      ArrayList al = null;
      TrayDetDAO dao = new TrayDetDAO(); 
     //query
     String query= "select sku,lot,loc,sinoln,qty,PickQty,isnull(mtid,'') as mtid  from ob_travel_det where Traveler_Id ='"+trav+"' and status <> 'C' ";
      try {
       al = dao.loadOBDetails(query,ht);
       }
    catch (Exception e) {
      MLogger.exception("get_sino_line_Details()", e);
      throw e;
    }
    return al;
  }

    public ArrayList get_sku_line_Details(String trav,Hashtable ht) throws Exception{
      String xmlStr = "";
      ArrayList al = null;
     
      TrayDetDAO dao = new TrayDetDAO(); 
     //query
      String query= "select sku,lot,sinoln,pickqty as qty  from ob_travel_det where Traveler_Id ='"+trav+"'";
      try {
       al = dao.loadOBDetails(query,ht);
       }
       catch (Exception e) 
       {
        MLogger.exception("get_sku_line_Details()", e);
        throw e;
       }
    return al;
  }

    public String Load_Lot_Details(String Traveler,String Pallet,String Sku,String aLot) throws Exception{
    String xmlStr = "";
    ArrayList al = null;
    TrayDetDAO dao = new TrayDetDAO(); 
     //query
     String query= "select distinct mtid,sku,(select sum(Qty) from ob_travel_det where sino= a.sino and traveler_id = a.traveler_id  and palletid =a.palletid group by sku,lot) as Qty,isnull(pickQty,0)as PickQty,loc,lot,sino from ob_travel_det a WHERE  traveler_id ='"+Traveler+"' and pallet ='"+Pallet+"' and Lot='"+aLot+"' and sku='"+Sku+"'";   
    //condition
    Hashtable ht=new Hashtable();
    try {
       al = dao.loadDetails(query,ht);
       MLogger.info("Record size() :: " + al.size());
  
      if (al.size() > 0) {
        xmlStr += xu.getXMLHeader();
        xmlStr +=
            xu.getStartNode("LotDetails total='" + String.valueOf(al.size()) +
                            "'");
        for (int i = 0; i < al.size(); i++) {
          Map map = (Map) al.get(i);
          xmlStr += xu.getStartNode("record");
         xmlStr = xmlStr + xu.getXMLNode("MTID",(String) map.get("mtid"));
            xmlStr = xmlStr + xu.getXMLNode("SKU",(String) map.get("sku"));
            xmlStr = xmlStr + xu.getXMLNode("QTY",(String) map.get("Qty"));
            xmlStr = xmlStr + xu.getXMLNode("LOC",(String) map.get("loc"));
            xmlStr = xmlStr + xu.getXMLNode("SRNO",(String) map.get("sino"));
            xmlStr = xmlStr + xu.getXMLNode("PickQty",(String) map.get("PickQty"));
          
              xmlStr += xu.getEndNode("record");
        }
          xmlStr += xu.getEndNode("LotDetails");
      }
    
    }
    catch (Exception e) {
      MLogger.exception("Load_Lot_Details()", e);
      throw e;
    }
    return xmlStr;
  }
   

 public String Load_Picking_Details(String company,String TravelId,String PalletNo,String mtid) throws Exception{
    String xmlStr = "";
    Map map = null;
    PickingDAO dao = new PickingDAO(); 
    Hashtable ht1=new Hashtable();
    ht1.put("PLANT",company);
    ht1.put("MTID",mtid);
    String InvQty = this.getInventoryQty(ht1);
 
    //query
     String query="SERIAL_NO,ITEM , LOT , QTY";
    //condition
    Hashtable ht=new Hashtable();
    ht.put("PLANT",company);
    ht.put("TRAVEL_NO",TravelId);
    ht.put("PALLET_NO",PalletNo);
    ht.put("MTID",mtid);
    xmlStr = xu.getXMLHeader();
    xmlStr = xmlStr + xu.getStartNode("PickDetails");
    try {
       map = dao.selectRow(query,ht);
       MLogger.info("Record size() :: " + map.size());
       if (map.size() > 0) {
          
            xmlStr = xmlStr + xu.getXMLNode("status", "0");
            xmlStr = xmlStr + xu.getXMLNode("description", "");
            xmlStr = xmlStr + xu.getXMLNode("SRNO",(String) map.get("SERIAL_NO"));
            xmlStr = xmlStr + xu.getXMLNode("ITEM",(String) map.get("ITEM"));
            xmlStr = xmlStr + xu.getXMLNode("LOT",(String) map.get("LOT"));
            xmlStr = xmlStr + xu.getXMLNode("QTY",(String) map.get("QTY"));
             xmlStr = xmlStr + xu.getXMLNode("INVQTY",InvQty);
            
  
      }
      else
      {
        throw new Exception("Details not found for Delivery No : "  + TravelId +" MTID : " + mtid );
      }
     
      xmlStr = xmlStr + xu.getEndNode("PickDetails");
     // MLogger.log(0, "Value of xml : " + xmlStr);
    }
    catch (Exception e) {
      MLogger.exception("Load_Picking_Details()", e);
      throw e;
    }
    return xmlStr;
  }

public String isValidToPick(Hashtable ht)throws Exception{
    OBTravelerDetDAO dao = new OBTravelerDetDAO(); 
    String xmlStr ="";
    boolean isValid = false;      
    try {
    String extracon ="  AND STATUS <> 'C' " ;
      isValid = dao.isExisit(ht,extracon);
       if(isValid==true){
      xmlStr = xu.getXMLMessage(0, "Valid To Pick ");
    }else{
         xmlStr = xu.getXMLMessage(1, "Not a Valid Pick");
    }
    }
    catch (Exception e) {
      MLogger.exception("isValidToPick()", e);
      isValid = false;
        xmlStr = xu.getXMLMessage(1, e.toString());
      throw e;
    }
    return xmlStr;
  }
  
  
  public String isExistsPartialTray(String sku,Hashtable ht)throws Exception{
    InvMstDAO dao = new InvMstDAO(); 
    String mtidStr ="";
    boolean isValid = false;      
    try {
    String sQry =" MTID ";
    String QtyPerTray =getQtyPerTray(sku);
    String extraCon = " AND  QTY < "+QtyPerTray + " and QTY > 0 " ;
      Map map =dao.selectRow(sQry,ht,extraCon);
      if (map.size() > 0) {
       mtidStr =(String) map.get("MTID");
      }
     MLogger.log(0, " isExistsPartialTray = " + mtidStr);
    }
    catch (Exception e) {
      MLogger.exception("isExistsPartialTray()", e);
           
      throw e;
    }
    return mtidStr;
  }
  



   public String isValidFullTray(String sku,Hashtable ht)throws Exception{
    InvMstDAO dao = new InvMstDAO(); 
    String qty ="";
     String xmlStr ="";
    boolean isValid = false;      
    try {
    String sQry =" QTY ";
    String QtyPerTray =getQtyPerTray(sku);
    String extraCon = " AND  QTY >= "+QtyPerTray   ;
      Map map =dao.selectRow(sQry,ht,extraCon);
      if (map.size() > 0) {
       qty =(String) map.get("QTY");
       xmlStr = xu.getXMLMessage(0, qty); 
      }else
      {
        xmlStr = xu.getXMLMessage(1, "  Not a valid trayid/Pick the Full Tray First !"); 
      }
    }
    catch (Exception e) {
      MLogger.exception("isValidFullTray()", e);
           
      throw e;
    }
    return xmlStr;
  }
  
  public String isValidMtid(Hashtable ht)throws Exception{
    InvMstDAO dao = new InvMstDAO(); 
    String xmlStr ="";
    boolean isValid = false;  
  
    String sQry = " QTY ";
    String extraCon =  " AND QTY  >  0 ";
    try {
       Map map =dao.selectRow(sQry,ht,extraCon);
       if (map.size() > 0) {
         
         xmlStr = xu.getXMLMessage(0, (String) map.get("QTY"));
       }
      else{
         xmlStr = xu.getXMLMessage(1, " Please Enter a Valid Mtid");
    }
   
    }
    catch (Exception e) {
      MLogger.exception("isValidMtid()", e);
      xmlStr = xu.getXMLMessage(1, e.toString());
      throw e;
    }
    return xmlStr;
  }

 public String Load_Line_Details_For_Picking(String TravelId,String PalletNo,String lot,String sku,String loc,String asino) throws Exception{
    String xmlStr = "";
    Map map = null;
    TrayDetDAO dao = new TrayDetDAO();
    String query="select sum(Qty) as Qty,sino,FullTray from ob_travel_det  where Traveler_id ='"+TravelId+"' and pallet ='"+PalletNo+"' and Lot='"+lot+"' and sku='"+sku+"' and loc ='"+loc+"' and sino ='"+asino+"' and status ='N' group by sino,FullTray";
 
    MLogger.query("Load_Line_Details_For_Picking:: "+query);
    xmlStr = xu.getXMLHeader();
    xmlStr = xmlStr + xu.getStartNode("PickDetails");
    try {
       map = dao.getRowOfData(query);
       MLogger.info("Record size() :: " + map.size());
       if (map.size() > 0) {
          
            xmlStr = xmlStr + xu.getXMLNode("status", "0");
            xmlStr = xmlStr + xu.getXMLNode("description", "");
            xmlStr = xmlStr + xu.getXMLNode("QTY",(String) map.get("Qty"));
            xmlStr = xmlStr + xu.getXMLNode("SRNO",(String) map.get("sino"));
            xmlStr = xmlStr + xu.getXMLNode("FULLTRAY",(String) map.get("FullTray"));
        
      }
      else
      {
        throw new Exception("Details not found for Delivery No : "  + TravelId +" SKU : " + sku );
      }
      xmlStr = xmlStr + xu.getEndNode("PickDetails");
    }
    catch (Exception e) {
      MLogger.exception("Load_Line_Details_For_Picking()", e);
      throw e;
    }
    return xmlStr;
  }
  
   public String load_next_line_details(String TravelId,String PalletNo,String asino,String aUser) throws Exception{
      String xmlStr = "";
      Map map = null;
      TrayDetDAO dao = new TrayDetDAO();
      //Modified the below SQL query to add one more filed as PickQty WO0000000202247
      String query=" SELECT     TOP 1 sku, lot, SUM(Qty) AS qty, loc, FullTray, sino, MAX(Status) AS Expr2, PickQty " + 
                   "  FROM   OB_TRAVEL_DET WHERE     (Traveler_Id = '"+TravelId+"') AND (Pallet = '"+PalletNo+"') AND assigneduser ='"+aUser+"'  and (Status = 'N') " + 
                   " GROUP BY Sku, Lot, Loc, FullTray, SiNo, PickQty order by loc ";
 
    MLogger.query("load_next_line_details:: "+query);
    xmlStr = xu.getXMLHeader();
    xmlStr = xmlStr + xu.getStartNode("PickDetails");
    try {
       map = dao.getRowOfData(query);
       MLogger.info("Record size() :: " + map.size());
       if (map.size() > 0) {
    	   	  //Below line is added on 10-10-2014 to calculate the balance qty for picking more than order qty bug WO0000000202247.
    	   	  int balQty = Integer.parseInt((String) map.get("qty")) - Integer.parseInt((String) map.get("PickQty"));
              xmlStr = xmlStr + xu.getXMLNode("status", "0");
              xmlStr = xmlStr + xu.getXMLNode("description", "");
              xmlStr = xmlStr + xu.getXMLNode("SKU",(String) map.get("sku"));
              xmlStr = xmlStr + xu.getXMLNode("LOT",(String) map.get("lot"));
              xmlStr = xmlStr + xu.getXMLNode("LOC",(String) map.get("loc"));
              //xmlStr = xmlStr + xu.getXMLNode("QTY",(String) map.get("qty"));
              xmlStr = xmlStr + xu.getXMLNode("QTY",String.valueOf(balQty));
              xmlStr = xmlStr + xu.getXMLNode("SRNO",(String) map.get("sino"));
              xmlStr = xmlStr + xu.getXMLNode("FULLTRAY",(String) map.get("FullTray"));
    
      }
      else
      {
        throw new Exception("Details not found for Delivery No : "  + TravelId +"PALLET : "+PalletNo  +" Sino : " + asino );
      }
     
      xmlStr = xmlStr + xu.getEndNode("PickDetails");
    }
    catch (Exception e) {
      MLogger.exception("load_next_line_details", e);
      throw e;
    }
    return xmlStr;
  }
  
  
  
 public String getQtyPerTray(String sku) throws Exception {
   String _qtyPerTray="";
   String xmlStr ="";
    java.sql.Connection con=null;
   try
   {
     StringBuffer sbQuery=new StringBuffer("");
    
    
     sbQuery.append("SELECT ISNULL(QTY_PER_TRAY,0) as QTY_PER_TRAY FROM PRD_CLASS_MST WHERE PRD_CLS_ID in ");
     sbQuery.append("(SELECT PRD_CLS_ID FROM ITEMMST where item='"+sku+"')");
     
     
      con=com.murho.gates.DbBean.getConnection();
      
      MLogger.query(" "+sbQuery.toString());
      Map m=new BaseDAO().getRowOfData(con,sbQuery.toString());
     
     _qtyPerTray=(String)m.get("QTY_PER_TRAY");
    
    if(_qtyPerTray=="" || _qtyPerTray==null)
    {
      _qtyPerTray="";
    }
    

    MLogger.info("getQtyPerTray() : getQtyPerTray= " + _qtyPerTray);
  }
  catch(Exception e) {
          MLogger.log(0,"######################### Exception :: getQtyPerTray() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: getQtyPerTray() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
        }finally{
        DbBean.closeConnection(con);   
        }
    return _qtyPerTray;
 }  
 
public String getProductClass(String sku) throws Exception {
   String _productClass="";
   try
   {
     String query=" " +" prd_cls_id "+ " " ;
     Hashtable ht=new Hashtable();
  
   //  ht.put("PLANT",company);
     ht.put("ITEM",sku);
   
     ItemMstDAO dao = new ItemMstDAO(); 
     Map m=dao.selectRow(query,ht);

     _productClass=(String)m.get("prd_cls_id");
    
    if(_productClass=="" || _productClass==null)
    {
      _productClass="";
    }
    MLogger.info("getProductClass() : PRODUCT CLASS = " + _productClass);
  }
  catch(Exception e) {
          MLogger.log(0,"######################### Exception :: getProductClass() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: getProductClass() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
        }
    return _productClass;
 }  
// new method added by Arun on 18 Oct 2010
//#981 change.. allow to pick any tray for PUV products
	public String getProductGroup(String sku) throws Exception {
	   String _productGroup="";
	   java.sql.Connection con=null;
	   try
	   {
			StringBuffer sbQuery = new StringBuffer("");

			sbQuery.append("SELECT A.PRD_GRP_ID FROM PRD_CLASS_MST A, ITEMMST B WHERE A.PRD_CLS_ID=B.PRD_CLS_ID AND B.ITEM='"+sku+"'");

			con = com.murho.gates.DbBean.getConnection();

			MLogger.query(" " + sbQuery.toString());
			Map m = new BaseDAO().getRowOfData(con, sbQuery.toString());

			_productGroup = (String) m.get("PRD_GRP_ID");

			if (_productGroup == "" || _productGroup == null) {
				_productGroup = "";
			}
			MLogger
					.info("getProductGroup() : PRODUCT GROUP = "
							+ _productGroup);
		}
	  catch(Exception e) {
	          MLogger.log(0,"######################### Exception :: getProductGroup() : ######################### \n");
	          MLogger.log(0,""+ e.getMessage());
	          MLogger.log(0,"######################### Exception :: getProductGroup() : ######################### \n");
	          MLogger.log(-1,"");
	          throw e;
	        }
	  // Finally block added for connection close, resolving JBOSS performance issue by Arun on 25th Mar 2011 #2020
	  finally
	   {
	      if (con != null) {
	        DbBean.closeConnection(con);
	      }
	   }
	    return _productGroup;
	 }  	
 

 public boolean isPickedForTrayLabel(Hashtable ht)throws Exception{
    PickingDAO dao = new PickingDAO(); 
    boolean isValid = false;      
    try {
      isValid = dao.isExist(ht);
    }
    catch (Exception e) {
      MLogger.exception("isPickedForTrayLabel()", e);
      isValid = false;
      throw e;
    }
    return isValid;
  }
    
   public String getInventoryQty(Hashtable ht) throws Exception
{
  String Qty ="";
  PickingDAO dao= new PickingDAO();
  try{
  String query=" QTY=cast(qty as Decimal)-isnull(cast(qtyalloc as decimal),0) ";
  Map m=dao.selectRowForInvQty(query,ht);
  Qty = (String)m.get("QTY");
  }
   catch(Exception e) 
   {
      MLogger.log(0,"######################### Exception :: getInventoryQty() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: getInventoryQty() : ######################### \n");
      MLogger.log(-1,"");
      throw e;
   }
return Qty;
}



 public  synchronized String Process_Picking(Map obj) throws Exception {
   boolean flag = false;
   UserTransaction ut = null;
   try
   {
     ut = com.murho.gates.DbBean.getUserTranaction();
     ut.begin();
     flag = process_Wms_Picking(obj);
   
     if (flag == true){
         DbBean.CommitTran(ut);
         flag= true;
     }
     else{
        DbBean.RollbackTran(ut);
        flag= false;
     }
   }
   catch (Exception e) {
       flag = false;
       DbBean.RollbackTran(ut);
       throw e;
   }
  
   
   String xmlStr="";
   if(flag==true){
      xmlStr = xu.getXMLMessage(0, "Material Picked successfully!");
    }else{
         xmlStr = xu.getXMLMessage(1, "Error in Picking Material");
    }
    return xmlStr;
   
  }
  
  private boolean process_Wms_Picking(Map map) throws Exception{
    MLogger.info(" process_Wms_Picking() starts");
    boolean flag=false;
    flag=true;
    WmsTran tran=new WmsPicking();
    flag= tran.processWmsTran(map);
    MLogger.info(" process_Wms_Picking() ends");
    return flag;
  }
  
 
  
   public ArrayList getSummaryList(String traveler, String sku, String lot,String user) {
    MLogger.log(1, this.getClass() + " getInvList()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "traveler" + traveler);
      MLogger.log(0, "lot" + lot);
      MLogger.log(0, "sku" + sku);
      MLogger.log(0, "user" + user);
     
   
      if (strUtils.fString(lot).length() > 0) {
        ht.put("b.lot",lot+"%");
      }
      if (strUtils.fString(sku).length() > 0) {
        ht.put("b.sku",sku+"%");
      }
      if (strUtils.fString(user).length() > 0) {
        ht.put("a.assignedUser",user+"%");

      }
      if (strUtils.fString(traveler).length() > 0) {
        ht.put("a.traveler_id",traveler+"%");

      }
      StringBuffer aQuery=new StringBuffer(" ");
      
      aQuery.append(" select a.traveler_id as traveler_id,b.pallet as pallet, ");
      
      aQuery.append(" b.sku as sku,b.lot as lot,b.qty as qty,b.fulltray as fulltray,b.pickqty as pickqty ");
      
       aQuery.append(" ,b.partialqty as partialqty,isnull(a.assigneduser,'') as assigneduser,");
       
       aQuery.append(" isnull((select min(c.crtime) from ob_travel_det c where c.traveler_id='"+traveler+"'),'') as sttime, ");
        
       aQuery.append(" isnull((select max(d.crtime) from ob_travel_det d where d.traveler_id='"+traveler+"'),'') as endtime, ");
       
       aQuery.append("(((b.qty-b.pickqty)/b.qty)*100) as picked  ");
      
       aQuery.append(" from ob_travel_hdr a,ob_travel_det b where a.traveler_id=b.traveler_id  ");
      
         arrList = invdao.selectForReport(aQuery.toString(), ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :repportUtil :: getInvList:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getInvList()");
    return arrList;
  }
  
  
   public ArrayList getAssignJobDet(String traveler, String sku, String lot,String user) {
    MLogger.log(1, this.getClass() + " getAssignJobDet()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      StringBuffer aQuery=new StringBuffer(" ");
      
      aQuery.append(" select a.traveler_id as traveler_id,b.pallet as pallet, ");
      
      aQuery.append(" b.sku as sku,b.lot as lot,b.qty as qty,b.sino as slno ");
      
      aQuery.append(",isnull(a.assigneduser,'') as assigneduser ");
      
      aQuery.append(" from ob_travel_hdr a,ob_travel_det b where a.traveler_id=b.traveler_id and b.lot in ("+traveler+") ");
      
      arrList = invdao.selectForReport(aQuery.toString(), ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :repportUtil :: getAssignJobDet:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getAssignJobDet()");
    return arrList;
  }

 public boolean UpdateAssaignUser(Map mp) throws Exception{
    boolean updated = false;
    boolean isExists =false;
    try{
    InvMstDAO invMstDAO=new InvMstDAO();
    OBTravelerDetDAO PickingDAO=new OBTravelerDetDAO();
    List list = new ArrayList();
     List listline = new ArrayList();
     String listDet ="";
    String travDeatils =(String)mp.get("Traveler_Update");
    StringTokenizer parser = new StringTokenizer(travDeatils,",");
     while (parser.hasMoreTokens()) {
     list.add(parser.nextToken());                     
     }

    for (int iCnt =0; iCnt<list.size(); iCnt++){
           listDet=(String)list.get(iCnt);
           StringTokenizer parser1 = new StringTokenizer(listDet,"||");
   // Doubly-linked list
   listline.clear();
while (parser1.hasMoreTokens()) {
listline.add(parser1.nextToken());                     
}
  Hashtable ht=new Hashtable();
  ht.put("TRAVELER_ID",listline.get(0));
  ht.put("PALLET",listline.get(1));
  ht.put("LOT",listline.get(2));
  ht.put("SKU",listline.get(3));
  String Query1 = "set ASSIGNEDUSER = '"+(String)mp.get(MDbConstant.ASSIGNEDUSER)+"'"; 
  String extcond="";
  updated = PickingDAO.updateUser(Query1,ht,extcond);
  if(updated)
    {
        boolean flag =false;
        TblControlDAO tblConDao=new TblControlDAO();
        MovHisDAO movHisDao=new MovHisDAO();
  
        Hashtable htRecvHis = new Hashtable();
      
        htRecvHis.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
        htRecvHis.put(MDbConstant.TRAVELER_NUM,listline.get(1));
        htRecvHis.put(MDbConstant.PALLET,listline.get(2));
        htRecvHis.put(MDbConstant.MTID,"");
        htRecvHis.put(MDbConstant.ITEM,listline.get(4));
        htRecvHis.put(MDbConstant.LOT_NUM,listline.get(3));
        htRecvHis.put(MDbConstant.LOGIN_USER,mp.get(MDbConstant.LOGIN_USER));
        htRecvHis.put("CRAT",mp.get(MDbConstant.MOVEHIS_CR_DATE));
        htRecvHis.put("crtime",mp.get(MDbConstant.MOVEHIS_CR_TIME));
        htRecvHis.put("MOVTID",mp.get(MDbConstant.MOVHIS_REF_NUM));
        flag = movHisDao.insertIntoMovHis(htRecvHis);

        MLogger.log(0, "insertIntoMovHis Transaction ::  " + flag);
    }
    else
    {
      updated=false;
    }
    }
   
    
    
   }catch(Exception e){ 
    
    throw e;
    }
    return updated;
   }


   
   private boolean processMoveHis(Map mp) throws Exception
   {
    boolean flag =false;
    MovHisDAO movHisDao=new MovHisDAO();
    try
    {
    
        Hashtable htRecvHis = new Hashtable();
      
        htRecvHis.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
        htRecvHis.put(MDbConstant.TRAVELER_NUM,mp.get(MDbConstant.TRAVELER_NUM));
        htRecvHis.put(MDbConstant.PALLET,"");
        htRecvHis.put(MDbConstant.MTID,"");
        htRecvHis.put(MDbConstant.ITEM,"");
        htRecvHis.put(MDbConstant.LOT_NUM,"");
        htRecvHis.put(MDbConstant.LOGIN_USER,mp.get(MDbConstant.LOGIN_USER));
        htRecvHis.put("CRAT",mp.get(MDbConstant.MOVEHIS_CR_DATE));
        htRecvHis.put("MOVTID",mp.get(MDbConstant.MOVHIS_REF_NUM));
        flag = movHisDao.insertIntoMovHis(htRecvHis);
    }
    catch (Exception e)
    {
       MLogger.exception(this,e);
       throw e;
    }
    return flag;
 }
 
    public ArrayList getLotDetail(String lot) {
    MLogger.log(1, this.getClass() + " getPickerLotDetail()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      String   aQuery="select lot,mtid,sku,qty,loc,status from ob_travel_det where lot='"+lot+"'"  ;  
      arrList = invdao.selectForReport(aQuery, ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getPickerLotDetail :: getPickerLotDetail:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getPickerLotDetail()");
    return arrList;
  }
 public ArrayList getLotDetail(String lot,String traveler,String aPallet) {
    MLogger.log(1, this.getClass() + " getPickerLotDetail()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
    
    
    String   aQuery="select lot,mtid,sku,(select sum(qty) from ob_travel_det where sino =a.sino and  fulltray =a.fulltray and "+
                   " traveler_id = a.traveler_id and pallet =a.pallet and lot =a.lot group by sino) as qty,loc,status from ob_travel_det a where lot='"+lot+"'  and traveler_id='"+traveler+"' and pallet ='"+aPallet+"' group by a.traveler_id,a.pallet,sino,sku,a.lot,mtid,loc,status,a.fulltray " ;
        
    arrList = invdao.selectForReport(aQuery, ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getPickerLotDetail :: getPickerLotDetail:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getPickerLotDetail()");
    return arrList;
  }


public String Load_Line_Details_For_fulltray(String TravelId,String PalletNo,String lot,String sku,String loc,String asino) throws Exception{
    MLogger.log(1, this.getClass() + " Load_Line_Details_For_fulltray()");
    String snoln = "";
    Map map = null;
    TrayDetDAO dao = new TrayDetDAO();
    String query="select Top 1 sinoln from ob_travel_det  where Traveler_id ='"+TravelId+"' and pallet ='"+PalletNo+"' and Lot='"+lot+"' and sku='"+sku+"' and loc ='"+loc+"' and sino ='"+asino+"' and status ='N' ";
 
    MLogger.query("Load_Line_Details_For_fulltray:: "+query);
    try {
       map = dao.getRowOfData(query);
       MLogger.info("Record size() :: " + map.size());
       if (map.size() > 0) {
          snoln= (String) map.get("sinoln");
      }
    }
    catch (Exception e) {
      MLogger.exception("Load_Line_Details_For_fulltray()", e);
      throw e;
    }
    MLogger.log( -1, this.getClass() + " Load_Line_Details_For_fulltray()");
    return snoln;
  }
  
  
  public String getMtid(String TravelId,String PalletNo,String lot,String sku,String loc,String asino) throws Exception{
    String smtid = "";
    Map map = null;
    TrayDetDAO dao = new TrayDetDAO();
    String query="select distinct isnull(mtid,'') as mtid  from ob_travel_det  where Traveler_id ='"+TravelId+"' and pallet ='"+PalletNo+"' and Lot='"+lot+"' and sku='"+sku+"' and loc ='"+loc+"' and sino ='"+asino+"' ";
    try {
       map = dao.getRowOfData(query);
       MLogger.info("Record size() :: " + map.size());
       if (map.size() > 0) {
          smtid= (String) map.get("mtid");
      }
    
    }
    catch (Exception e) {
      MLogger.exception("getMtid()", e);
      throw e;
    }
    return smtid;
  }

   public String isValidToPrint(Hashtable ht)throws Exception{
    OBTravelerDetDAO dao = new OBTravelerDetDAO(); 
    String xmlStr ="";
    boolean isValid = false;      
    try { 
    String extracon ="  AND traystatus = 'N' " ;
      isValid = dao.isExisit(ht,extracon);
       if(isValid==true){
      xmlStr = xu.getXMLMessage(0, "Not Valid To Print ");
    }else{
         xmlStr = xu.getXMLMessage(1, "Valid  to Print");
    }
    }
    catch (Exception e) {
      MLogger.exception("isValidToPrint()", e);
      isValid = false;
        xmlStr = xu.getXMLMessage(1, e.toString());
      throw e;
    }
    return xmlStr;
  }
  
  public String getLotType(String prdclass,String shipTo) throws Exception {
   MLogger.log(1, this.getClass() + " getLotType()");
   String lottype="";
   String xmlStr ="";
   java.sql.Connection con=null;
   try
   {
      StringBuffer sbQuery=new StringBuffer("");   
      sbQuery.append("SELECT type from  rules_mst where SHIP_TO='"+shipTo+"' and prd_cls_id='"+prdclass+"' group by type ");
      
      con=com.murho.gates.DbBean.getConnection();
      
      MLogger.query(" "+sbQuery.toString());
      Map m=new BaseDAO().getRowOfData(con,sbQuery.toString());
     
     lottype=(String)m.get("type");
    
    if(lottype=="" || lottype==null)
    {
      lottype="";
    }
     
  }
  catch(Exception e) {
          MLogger.log(0,"######################### Exception :: getLotType() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: getLotType() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
        }finally
        {
          DbBean.closeConnection(con);
        }
    MLogger.log( -1, this.getClass() + " getLotType()");
    return lottype;
 }  
 
 
  public String Load_PrdouctGroup_Details_For_Picking(String sku) throws Exception{
    String xmlStr = "";
    Map map = null;
    TrayDetDAO dao = new TrayDetDAO();
    String query= "select B.PRD_GRP_ID AS PRODUCTGROUP From ItemMst A ,Prd_Class_mst  B Where A.Item='"+sku+"' AND A.PRD_CLS_ID = B.PRD_CLS_ID ";
 
    MLogger.query("Load_PrdouctGroup_Details_For_Picking:: "+query);
    xmlStr = xu.getXMLHeader();
    xmlStr = xmlStr + xu.getStartNode("PickDetails");
    try {
       map = dao.getRowOfData(query);
       MLogger.info("Record size() :: " + map.size());
       if (map.size() > 0) {
          
            xmlStr = xmlStr + xu.getXMLNode("status", "0");
            xmlStr = xmlStr + xu.getXMLNode("description", "");
            //Commanded and Added on Aug 16 2010  to handle  Special character in ProductGroup
            //xmlStr = xmlStr + xu.getXMLNode("PRODUCTGROUP",(String) map.get("PRODUCTGROUP"));
            xmlStr = xmlStr + xu.getXMLNode("PRODUCTGROUP",(String) strUtils.replaceCharacters2Send(map.get("PRODUCTGROUP").toString()));
  
      }
      else
      {
        throw new Exception("PrdouctGroup not found for Sku  : " + sku );
      }
      xmlStr = xmlStr + xu.getEndNode("PickDetails");
    }
    catch (Exception e) {
      MLogger.exception("Load_ProductGroup_Details_For_Picking()", e);
      throw e;
    }
    return xmlStr;
  }

  /*  Added by Ranjana 
   *  Purpose: LOT restriction in PDA under ticket WO0000000284867 
   *  for the process of PICKING*/
  
	public String LOT_Blocked_Picking(Hashtable ht) throws Exception {
		String XmlStr = "";
		Map map = new HashMap();
		PickingDAO lotDao = new PickingDAO();
			
		XmlStr = xu.getXMLHeader();
		try {
			map = lotDao.isPickingLotStatus(ht);
			XmlStr += xu.getStartNode("PickDetails");
			if(map.size()>0){
			XmlStr += xu.getXMLNode("LOTSTATUS", (String) map.get("lotstatus").toString());
			
			MLogger.log(1, XmlStr + "XMLSTR.........");
			}
			XmlStr+=xu.getEndNode("PickDetails");
		} catch (Exception e) {
			MLogger.log(0, "isPicking() Failed .............................");
			throw e;
		}
		return XmlStr;
	}
} 