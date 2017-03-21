package com.murho.db.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.murho.dao.InvMstDAO;
import com.murho.dao.ItemMstDAO;
import com.murho.dao.MovHisDAO;
import com.murho.dao.OBTravelerDetDAO;
import com.murho.dao.PickingDAO;
import com.murho.dao.RecvDetDAO;
import com.murho.dao.RecvHdrDAO;
import com.murho.dao.TblControlDAO;
import com.murho.gates.DbBean;
import com.murho.tran.WmsReceiving;
import com.murho.tran.WmsTran;
import com.murho.utils.CibaConstants;
import com.murho.utils.MConstants;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;

public class RecevingUtil {
  com.murho.utils.XMLUtils xu = null;
  com.murho.utils.StrUtils strUtils = null;
  public RecevingUtil() {
    xu = new XMLUtils();
    strUtils = new StrUtils();
}

   public String getOpenTravelers() throws Exception {
    String xmlStr = "";
    ArrayList al = null;
    RecvHdrDAO dao = new RecvHdrDAO();
    try {
      al = dao.getOpenTraveler();
      MLogger.info("Record size() :: " + al.size());
      if (al.size() > 0) {
        xmlStr += xu.getXMLHeader();
        xmlStr +=
            xu.getStartNode("TravelerDetails total='" + String.valueOf(al.size()) +
                            "'");
        for (int i = 0; i < al.size(); i++) {
          Map map = (Map) al.get(i);
          xmlStr += xu.getStartNode("record");
          xmlStr += xu.getXMLNode("Traveler", (String) map.get("Traveler"));
          xmlStr += xu.getEndNode("record");
        }
          xmlStr += xu.getEndNode("TravelerDetails");
      }
    }
    catch (Exception e) {
        throw new Exception("getOpenTravelers() " + e.getMessage());
    }
      return xmlStr;
  }
  
   public String getPallet(String Traveler) throws Exception {
    String xmlStr = "";
    ArrayList al = null;
    RecvDetDAO dao = new RecvDetDAO();
    try {
      al = dao.getPallet(Traveler);
      MLogger.info("Record size() :: " + al.size());
      if (al.size() > 0) {
        xmlStr += xu.getXMLHeader();
        xmlStr +=
            xu.getStartNode("TravelerDetails total='" + String.valueOf(al.size()) +
                            "'");
        for (int i = 0; i < al.size(); i++) {
          Map map = (Map) al.get(i);
          xmlStr += xu.getStartNode("record");
          xmlStr += xu.getXMLNode("Pallet", (String) map.get("Pallet"));
          xmlStr += xu.getEndNode("record");
        }
          xmlStr += xu.getEndNode("TravelerDetails");
      }
    }
    catch (Exception e) {
        throw new Exception("getOpenTravelers() " + e.getMessage());
    }
      return xmlStr;
  }
  
   public String getMTIDDetails(String TravelId,String PalletNo,String mtid) throws Exception{
    MLogger.log(1, this.getClass() + " getMTIDDetails()");
    String xmlStr = "";
        Map map = null;
    RecvDetDAO dao = new RecvDetDAO(); 
     
 
    //query
    // String query="SKU,LOT,OrdQty,ReceiveStatus";
//  added expdate column by Arun for #1956 on 27 Apr 2011
    String query="SKU,LOT,OrdQty,EXPIREDATE,ReceiveStatus";
    //condition
    Hashtable ht=new Hashtable();
    ht.put("Traveler",TravelId);
    ht.put("Pallet",PalletNo);
    ht.put("MTID",mtid);
    xmlStr = xu.getXMLHeader();
    xmlStr = xmlStr + xu.getStartNode("PickDetails");
    try {
       map = dao.selectRow(query,ht);
       MLogger.info("Record size() :: " + map.size());
       
     //  System.out.println("Map Size........"+map.size());
       if (map.size() > 0) {
          
            xmlStr = xmlStr + xu.getXMLNode("status", "0");
            xmlStr = xmlStr + xu.getXMLNode("description", "");
            xmlStr = xmlStr + xu.getXMLNode("SKU", (String) map.get("SKU"));
            xmlStr = xmlStr + xu.getXMLNode("LOT", (String) map.get("LOT"));
            xmlStr = xmlStr + xu.getXMLNode("QTY", (String) map.get("OrdQty"));
//	          added by Arun for #1956 on 27 Apr 2011
            xmlStr = xmlStr + xu.getXMLNode("EXPIREDATE", (String) map.get("EXPIREDATE"));
           // String skuDesc=(String) map.get("Description");
            //need to check why product group taken from desc
            String skuDesc=new ItemMstDAO().getItemDescription(CibaConstants.cibacompanyName,(String) map.get("SKU"));
            xmlStr = xmlStr + xu.getXMLNode("PRDGRP",skuDesc.substring(0,3));
            xmlStr = xmlStr + xu.getXMLNode("NoOfSampleScanning", CibaConstants.CReceiveScanningSample);
            xmlStr = xmlStr + xu.getXMLNode("RECEIVESTATUS", (String) map.get("ReceiveStatus"));
           
        }
      else
      {
        throw new Exception("Details not found for Delivery No : "  + TravelId +" MTID : " + mtid );
      }
     
      xmlStr = xmlStr + xu.getEndNode("PickDetails");
      MLogger.log(0, "Value of xml : " + xmlStr);
    }
    catch (Exception e) {
      MLogger.exception("getMTIDDetails()", e);
      throw e;
    }
    MLogger.log( -1, this.getClass() + " getMTIDDetails()");
    return xmlStr;
  }
  
  
  public synchronized String Process_Receiving(Map obj) throws Exception {
   boolean flag = false;
   try
   {
     MLogger.log(0,"Backend transaction :: " + MConstants.BACKEND );
     flag = process_Wms_Receiving(obj);
     MLogger.log(0,"After processing --> process_Wms_Receiving :: " + flag );
      
   }
   catch (Exception e) {
       flag = false;
       throw e;
   }
   
   String xmlStr="";
   if(flag==true){
      xmlStr = xu.getXMLMessage(0, "Material Received successfully!");
    }else{
         xmlStr = xu.getXMLMessage(1, "Error in Receiving Material");
    }
    return xmlStr;
   
  }
  
  private boolean process_Wms_Receiving(Map map) throws Exception{
    MLogger.info(" process_Wms_Receiving() starts");
    boolean flag=false;
    flag=true;
    WmsTran tran=new WmsReceiving();
    flag= tran.processWmsTran(map);
    MLogger.info(" process_Wms_Receiving() ends");
    return flag;
  }
  
 
 

 public boolean UpdateTravelerDetail(Map mp) throws Exception{
    boolean inserted = false;
    try{

      RecvDetDAO dao= new RecvDetDAO();
      
      Hashtable ht = new Hashtable();
      
     ht.put("TRAVELER",mp.get("TRAVELER"));
     ht.put("MTID", mp.get("MTID"));
     
     String Query = "set sku='"+(String)mp.get("PART01")+"', lot='"+(String)mp.get("LOT01")+"', ordqty='"+(String)mp.get("QTY01")+"',remark='"+(String)mp.get("REMARK")+"' ";
     inserted = dao.update(Query,ht,"");
    }catch(Exception e){ 
    throw e;
    }
    return inserted;
  }
  
   public boolean UpdateReceiveprocess(Map mp) throws Exception{
    boolean updated = false;
    boolean isExists =false;
    try{
    InvMstDAO invMstDAO=new InvMstDAO();
    Hashtable ht=new Hashtable();
    
    updated=UpdateReceiveDetail(mp);
    
    if(updated)
    {
    ht.put("MTID",mp.get(MDbConstant.MTID));
    
    isExists=invMstDAO.isExists(ht);
    System.out.println("isExists::"+isExists);
    
    if(isExists)
    {
       String Query1 = "set sku = '"+(String)mp.get(MDbConstant.ITEM)+"' ,qty='"+(String)mp.get(MDbConstant.RECV_QTY)+"'";
       updated = invMstDAO.updateInv(Query1,ht,"");  
       
       MLogger.log("############## UpdateReceiveDetail #####################" + Query1);
    
    }
    
    }
       
    if(updated)
    {
       updated=processMoveHis(mp);
    }
    else
    {
      updated=false;
    }
   
   }catch(Exception e){ 
    
    throw e;
    }
    return updated;
   }
    public boolean UpdateReceiveDetail(Map mp) throws Exception{
    boolean inserted = false;
    try{
    MLogger.log("############## UpdateReceiveDetail #####################");
      RecvDetDAO dao= new RecvDetDAO();
      
      Hashtable ht = new Hashtable();
      
     ht.put("TRAVELER",mp.get(MDbConstant.TRAVELER_NUM));
     ht.put("MTID", mp.get(MDbConstant.MTID));
     
      String Query = "set sku='"+(String)mp.get(MDbConstant.ITEM)+"', lot='"+(String)mp.get(MDbConstant.LOT_NUM)+"', recvqty='"+(String)mp.get(MDbConstant.RECV_QTY)+"',remark='"+(String)mp.get(MDbConstant.REMARK)+"',ReceiveStatus='C' ";

     String extCond=" AND RECEIVESTATUS<>'N' ";
      inserted = dao.update(Query,ht,extCond);
      
      MLogger.log("############## UpdateReceiveDetail Query: #####################" + Query);
      
    }catch(Exception e){ 
    throw e;
    }
    return inserted;
  }
 public boolean RejectReceiveDetail(Map mp) throws Exception{
 
    MLogger.log("##################  RejectReceiveDetail ####################### ");
    boolean updated = false;
    UserTransaction ut=null ;
    try{
      ut = DbBean.getUserTranaction();
      ut.begin(); 
     InvMstDAO invMstDAO=new InvMstDAO();
     RecvDetDAO recvDetDAO=new RecvDetDAO();
     RecvHdrDAO recvHdrDAO=new RecvHdrDAO();
     
     Hashtable ht = new Hashtable();
      
     ht.put("TRAVELER",mp.get(MDbConstant.TRAVELER_NUM));
     ht.put("MTID",mp.get(MDbConstant.MTID));
          
      Hashtable ht1 = new Hashtable();
      ht1.put("MTID",mp.get(MDbConstant.MTID));
       
      String Query1 = "set recstat = 'D' ";

      updated = invMstDAO.deleteItemFrmInvmst(ht1);
        
      
     //INVMST
      if(updated)
      {
        String Query2 = "set receivestatus = 'N' , RecvQty=0";
        updated = recvDetDAO.update(Query2,ht,"");
      }
      
      //RECVHDR
      if(updated)
      {
       Hashtable ht2 = new Hashtable();
       ht2.put("TRAVELER",mp.get(MDbConstant.TRAVELER_NUM));
       
        String Query2 = "set receivestatus = 'N' ";
        updated = recvHdrDAO.update(Query2,ht2,"");
      }
      
      //MOVHIS
      if(updated)
      {
        updated=processMoveHis(mp);
      }
      
      if(updated)
      {
        //commit
        DbBean.CommitTran(ut);
      }else
      {
        throw new Exception("unable to reject MTID details");
        //rollback
      }
    }catch(Exception e){ 
    DbBean.RollbackTran(ut); 
    throw e;
    }
    return updated;
  }
  

    private boolean processMoveHis(Map mp) throws Exception
  {

    boolean flag =false;
    TblControlDAO tblConDao=new TblControlDAO();

    MovHisDAO movHisDao=new MovHisDAO();
    try
    {
       MLogger.log(0, "Getting next seq no .");

        Hashtable htRecvHis = new Hashtable();
      
        htRecvHis.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
        htRecvHis.put(MDbConstant.TRAVELER_NUM,mp.get(MDbConstant.TRAVELER_NUM));
        htRecvHis.put(MDbConstant.PALLET,mp.get(MDbConstant.PALLET));
        htRecvHis.put(MDbConstant.MTID,mp.get(MDbConstant.MTID));
        htRecvHis.put(MDbConstant.ITEM,mp.get(MDbConstant.ITEM));
        htRecvHis.put(MDbConstant.LOT_NUM,mp.get(MDbConstant.LOT_NUM));
        htRecvHis.put(MDbConstant.LOGIN_USER,mp.get(MDbConstant.LOGIN_USER));
        htRecvHis.put("CRAT",mp.get(MDbConstant.MOVEHIS_CR_DATE));
        
        htRecvHis.put("CRTIME",mp.get(MDbConstant.CRTIME));
        htRecvHis.put("REMARK",mp.get(MDbConstant.REMARK));
        
        htRecvHis.put("MOVTID",mp.get(MDbConstant.MOVHIS_REF_NUM));
        htRecvHis.put(MDbConstant.MOVHIS_QTY,mp.get(MDbConstant.MOVHIS_QTY));
      
        flag = movHisDao.insertIntoMovHis(htRecvHis);

        MLogger.log(0, "insertIntoMovHis Transaction ::  " + flag);


    }
    catch (Exception e)
    {
       MLogger.exception(this,e);
       throw e;
    }
    return flag;
 }
 
    public boolean UpdateLot(Map mp) throws Exception{
    boolean updated = false;
    boolean isExists =false;
    try{
    OBTravelerDetDAO oBTravelerDetDAO=new OBTravelerDetDAO();
    Hashtable ht=new Hashtable();
    
    String qty = "";
    
    qty=oBTravelerDetDAO.getSumOfLotQty(mp);
    
    updated=UpdateLotDetails(mp);
    
    if(updated)
    {
   
    mp.put("QTY",qty);
  
    updated=processMoveHis(mp);
    }
    else
    {
      updated=false;
    }
    
   }catch(Exception e){ 
    
    throw e;
    }
    return updated;
   }


    public boolean UpdateLotDetails(Map mp) throws Exception{
    boolean inserted = false;
    try{
    MLogger.log("############## UpdateLotDetails #####################");
      OBTravelerDetDAO dao= new OBTravelerDetDAO();
      
      Hashtable ht = new Hashtable();
      
     ht.put("TRAVELER_ID",mp.get(MDbConstant.TRAVELER_NUM));
     ht.put("LOT", mp.get(MDbConstant.LOT));
     ht.put("PALLET", mp.get(MDbConstant.PALLET));
     
      String Query = "set status='C' ";

      String extCond=" AND status ='N' ";
      inserted = dao.update(Query,ht,extCond);
      
    }catch(Exception e){ 
    throw e;
    }
    return inserted;
  }

    /*  Added by Ranjana 
     *  Purpose: LOT restriction in PDA under ticket WO0000000284867 
     *  for the process of RECEIVING*/
    
    public String LOT_Blocked_Receiving(Hashtable ht) throws Exception {

    	String XmlStr = "";
			Map map = new HashMap();
			 RecvDetDAO dao = new RecvDetDAO();
				
			XmlStr = xu.getXMLHeader();
			try {
				map = dao.isReceivingLotStatus(ht);
				XmlStr += xu.getStartNode("ReceiveDetails");
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
} //end of class
