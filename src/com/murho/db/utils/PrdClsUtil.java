
package com.murho.db.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.ItemMstDAO;
import com.murho.dao.PrdClsDAO;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;



public class PrdClsUtil {
  com.murho.utils.XMLUtils xu = null;
   com.murho.utils.StrUtils _strUtils = null;
  public PrdClsUtil() {
  xu=new XMLUtils();
  _strUtils  = new StrUtils();
  }

public boolean isExistsPrdClsId(Hashtable htPrdCls) throws Exception{

      boolean isExists = false;
      PrdClsDAO dao = new PrdClsDAO();
      try {
      isExists = dao.isExists(htPrdCls);   
      }
      catch (Exception e) {
      MLogger.exception("isExistsPrdClsId()", e);
      throw e;
     }

    return isExists;
  }
  
   public boolean insertPrdClsMst(Hashtable ht) throws Exception{
    boolean inserted = false;
    try{
    PrdClsDAO dao = new PrdClsDAO();
      inserted = dao.insertIntoPrdClsMst(ht);
    }catch(Exception e){ throw e;}
    return inserted;
  }
 
  public boolean deletePrdClsId(Hashtable ht) throws Exception{
    boolean deleted = false;
    try{
       PrdClsDAO dao = new PrdClsDAO();
      deleted = dao.deletePrdClsId(ht);
    }catch(Exception e){throw e; }
    return deleted;
  }
   public boolean updatePrdClsId(Hashtable htUpdate,Hashtable htCondition) throws Exception{
    boolean update = false;
    try{
     PrdClsDAO dao = new PrdClsDAO();
     update = dao.updatePrdClsId(htUpdate,htCondition);

    }catch(Exception e){throw e; }
    return update;
  }
  

  
   
  public Map getPrdClsIdDetails(String aPrdClsId ) throws Exception{
      PrdClsDAO dao = new PrdClsDAO();
     Hashtable ht=new Hashtable();
     ht.put(MDbConstant.PRD_CLS_ID,aPrdClsId);
     Map map=new HashMap();
    try{

     String sql= MDbConstant.PRD_CLS_ID +"," + MDbConstant.PRD_CLS_DESC +","+ MDbConstant.PRD_GRP_ID+","+ MDbConstant.PRICE+","+ MDbConstant.KFACTOR+","+ MDbConstant.PRD_CLS_DESC1+","
     +MDbConstant.PRD_CLS_UOM+","+MDbConstant.QTY_PER_COL+","+MDbConstant.NO_OF_COLUMN+","+MDbConstant.QTY_PER_TRAY+ ","
     +MDbConstant.NO_OF_TRAY_PER_LAYER+","+MDbConstant.NO_OF_LAYERS+","+MDbConstant.NO_OF_LAYERS_PER_PALLET ;
     map= dao.selectRow(sql,ht);
    }catch(Exception e){MLogger.exception("getPrdClsIdDetails :: Exception :",e);
    throw e;
    }
    return map;
  }
  
   public ArrayList getPrdClsListStartsWith(String aPrdClsId) {
   ArrayList al = null;
   PrdClsDAO dao = new PrdClsDAO();
    
   
    try {
      al = dao.getPrdClsIdStartsWith(aPrdClsId);
      MLogger.log(0, "Record size() :: " + al.size());
    }
    catch (Exception e) {
       MLogger.exception(this, e);
    }
     return al;
  }
  
   public String getProductClass(String sku) throws Exception {
   MLogger.log(1, this.getClass() + " getProductClass()");
   String _productClass="";
   try
   {
     String query=" " +" prd_cls_id "+ " " ;
     Hashtable ht=new Hashtable();
     ht.put("ITEM",sku);
     //ht.put("REFNO",sku);
   
     ItemMstDAO dao = new ItemMstDAO(); 
     Map m=dao.selectRow(query,ht);

     _productClass=(String)m.get("prd_cls_id");
    
    if(_productClass=="" || _productClass==null)
    {
      _productClass="";
      throw new Exception("Product Class not defiend for SKU :" + sku);
    }
    MLogger.info("getProductClass() : prd_cls_id = " + _productClass);
  }
  catch(Exception e) {
        
          
         
          throw e;
        }
    MLogger.log( -1, this.getClass() + " getProductClass()");
    return _productClass;
 }  
 
 
  public String GetQtyPerTray(String company,String item) throws Exception{
    MLogger.log(1, this.getClass() + " GetQtyPerTray()");
    String xmlStr = "";
    Map map = null;
    String prodClass=getProductClass(item);
    MLogger.log(0, "prodClass          : " + prodClass);
    String trayQty= getQtyPerTray(prodClass);
      
    xmlStr = xu.getXMLHeader();
    xmlStr = xmlStr + xu.getStartNode("qtypertray");
    try {
      
      if (_strUtils.fString(trayQty).length() > 0) {
            xmlStr = xmlStr + xu.getXMLNode("status","0");
            xmlStr = xmlStr + xu.getXMLNode("description","");
          xmlStr = xmlStr + xu.getXMLNode("QTY_PER_TRAY",trayQty);
  
      }
      else
      {
        throw new Exception("Qty Per tray Not found for Item :"+item);
      }
     
      xmlStr = xmlStr + xu.getEndNode("qtypertray");
      MLogger.log(0, "Value of xml : " + xmlStr);
    }
    catch (Exception e) {
      MLogger.exception("Getqtypertray()", e);
      throw e;
    }
    MLogger.log( -1, this.getClass() + " Getqtypertray()");
    return xmlStr;
  }
  

  public String getQtyPerTray(String prdClass) throws Exception {
   MLogger.log(1, this.getClass() + " getQtyPerTray()");
   String _productClass="";
   try
   {
     String query=" " +" qty_per_tray "+ " " ;
     Hashtable ht=new Hashtable();
     ht.put("prd_cls_id",prdClass);
   
     PrdClsDAO dao = new PrdClsDAO(); 
     Map m=dao.selectRow(query,ht);

     _productClass=(String)m.get("qty_per_tray");
    
    if(_productClass=="" || _productClass==null)
    {
      _productClass="";
    }
    MLogger.info("getQtyPerTray() : QtyPerTray = " + _productClass);
  }
  catch(Exception e) {
          MLogger.log(0,"######################### Exception :: getQtyPerTray() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: getQtyPerTray() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
        }
    MLogger.log( -1, this.getClass() + " getQtyPerTray()");
    return _productClass;
 }  
 

}