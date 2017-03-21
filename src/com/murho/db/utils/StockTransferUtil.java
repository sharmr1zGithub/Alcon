package com.murho.db.utils;


import java.util.Hashtable;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.murho.dao.InvMstDAO;
import com.murho.dao.LocMstDAO;
import com.murho.gates.DbBean;
import com.murho.tran.WmsStockTransfer;
import com.murho.tran.WmsTran;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;

public class StockTransferUtil {
  XMLUtils _xmlUtils = null;
  StrUtils _strUtils = null;
  
  public StockTransferUtil() {
     _xmlUtils = new XMLUtils();
     _strUtils = new StrUtils();
  }

  


  
   public String process_StockTransfer(Map obj) throws Exception {
   boolean flag = false;
   UserTransaction ut = null;
   try
   {
     ut = com.murho.gates.DbBean.getUserTranaction();
     ut.begin();
     flag = process_Wms_StockTransfer(obj);
     MLogger.log(0,"After processing --> process_StockTransfer :: " + flag );
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
      xmlStr = _xmlUtils.getXMLMessage(1, "Stocktransfer for MTID : "+ obj.get("MTID") + " is successful!");
    }else{
         xmlStr = _xmlUtils.getXMLMessage(0, "Error in stocktransfer for MTID : "+ obj.get("MTID") +" ");
    }
    return xmlStr;
  }
  
  private boolean process_Wms_StockTransfer(Map map) throws Exception {
  MLogger.log(1, this.getClass() + " process_Wms_StockTransfer()");
  boolean flag=false;
 
    WmsTran tran=new WmsStockTransfer();
    flag= tran.processWmsTran(map);
    MLogger.log(-1, this.getClass() + " process_Wms_StockTransfer()");
    return flag;
  }


 public boolean ValidateLoc(String aCompany, String aLoc){

     boolean isValid = false;
     InvMstDAO invDao = new InvMstDAO();
     Hashtable ht = new Hashtable();
     ht.put(MDbConstant.COMPANY,aCompany );
     ht.put(MDbConstant.INV_LOC,aLoc);
       
    try {
      isValid = invDao.isExists(ht);
    }
    catch (Exception e) {
      MLogger.exception("ValidateLoc()", e);
      isValid = false;
    }

    return isValid;
  }
  
   public String getValidLocation(String plant,String loc) throws Exception{
    String xmlStr = "";
    Map map = null;
    LocMstDAO locDao = new LocMstDAO();
 
    //query
     String query="LOC_ID";
    //condition
    Hashtable ht=new Hashtable();
    ht.put("LOC_ID",loc);
    
    xmlStr = _xmlUtils.getXMLHeader();
    xmlStr = xmlStr + _xmlUtils.getStartNode("PickDetails");
    try {
       map = locDao.selectRow(query,ht);
       MLogger.info("Record size() :: " + map.size());
       if (map.size() > 0) {
          
            xmlStr = xmlStr + _xmlUtils.getXMLNode("status", "0");
            xmlStr = xmlStr + _xmlUtils.getXMLNode("description", "");
            xmlStr = xmlStr + _xmlUtils.getXMLNode("LOC","qwqwqw");
        }
      else
      {
        throw new Exception("Invalid Location :"+loc );
      }
     
      xmlStr = xmlStr + _xmlUtils.getEndNode("PickDetails");
      MLogger.log(0, "Value of xml : " + xmlStr);
    }
    catch (Exception e) {
      MLogger.exception("getValidLocation()", e);
      throw e;
    }
    MLogger.log( -1, this.getClass() + " getValidLocation()");
    return xmlStr;
  }
  
     public String getMTIDdetails(String plant,String mtid,String loc,String transfer) throws Exception{
     String xmlStr = "";
     Map map = null;
     InvMstDAO invDao = new InvMstDAO();
 
      //query
       String query="SKU,LOT,QTY";
      //condition
      Hashtable ht=new Hashtable();
      ht.put("PLANT",plant);
      ht.put("MTID",mtid);
    
    if(transfer.equalsIgnoreCase("FRM_LOC"))
    {
      ht.put("LOC",loc);
    }
    if(transfer.equalsIgnoreCase("TO_LOC"))
    {
      ht.put("LOC","TEMPLOC");
    }
      
    xmlStr = _xmlUtils.getXMLHeader();
    xmlStr = xmlStr + _xmlUtils.getStartNode("PickDetails");
    try {
       map = invDao.selectRow(query,ht);
       MLogger.info("Record size() :: " + map.size());
       if (map.size() > 0) {
          
            xmlStr = xmlStr + _xmlUtils.getXMLNode("status", "0");
            xmlStr = xmlStr + _xmlUtils.getXMLNode("description", "");
            xmlStr = xmlStr + _xmlUtils.getXMLNode("SKU", (String) map.get("SKU"));
            xmlStr = xmlStr + _xmlUtils.getXMLNode("LOT", (String) map.get("LOT"));
            xmlStr = xmlStr + _xmlUtils.getXMLNode("QTY", (String) map.get("QTY"));

        }
      else
      {
        throw new Exception("MTID :"+mtid + " not available at the LOC: "+ (String)ht.get("LOC"));
      }
      xmlStr = xmlStr + _xmlUtils.getEndNode("PickDetails");
      MLogger.log(0, "Value of xml : " + xmlStr);
    }
    catch (Exception e) {
      MLogger.exception("getValidLocation()", e);
      throw e;
    }
    MLogger.log( -1, this.getClass() + " getMTIDdetails()");
    return xmlStr;
  }
  
 
  
  
 
}
