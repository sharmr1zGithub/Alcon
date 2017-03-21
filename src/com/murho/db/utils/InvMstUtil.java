package com.murho.db.utils;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.InvMstDAO;
import com.murho.gates.DbBean;
import com.murho.utils.CibaConstants;
import com.murho.utils.MLogger;
import com.murho.utils.XMLUtils;



public class InvMstUtil {
  com.murho.utils.XMLUtils xu = null;
  public InvMstUtil() {
  xu=new XMLUtils();
  }


  
   public String Load_Inv_Det_for_Loc_MTID(String company,String loc,String mtid) throws Exception{
    MLogger.log(1, this.getClass() + " Load_Inv_Det_for_Loc_MTID()");
    String xmlStr = "";
        Map map = null;
    InvMstDAO dao = new InvMstDAO(); 
    
    
    //query
     String query=" ITEM,LOC,MTID,LOT,QTY ";
    //condition
    Hashtable ht=new Hashtable();
    ht.put("PLANT",CibaConstants.cibacompanyName);
    ht.put("MTID",mtid);
    ht.put("LOC",loc);
    
    xmlStr = xu.getXMLHeader();
    xmlStr = xmlStr + xu.getStartNode("PutAway_Details");
    try {
      map = dao.selectRow(query,ht);
      MLogger.log(0, "Record size() :: " + map.size());
      if (map.size() > 0) {
            xmlStr = xmlStr + xu.getXMLNode("status","0");
            xmlStr = xmlStr + xu.getXMLNode("description","");
   
            xmlStr = xmlStr + xu.getXMLNode("ITEM",(String) map.get("ITEM"));
            xmlStr = xmlStr + xu.getXMLNode("LOT",(String) map.get("LOT"));
            xmlStr = xmlStr + xu.getXMLNode("LOC",(String) map.get("LOC"));
            xmlStr = xmlStr + xu.getXMLNode("QTY",(String) map.get("QTY"));
  
      }
      else
      {
        throw new Exception("Detail not found for the given input");
      }
     
      xmlStr = xmlStr + xu.getEndNode("PutAway_Details");
      MLogger.log(0, "Value of xml : " + xmlStr);
    }
    catch (Exception e) {
      MLogger.exception("Load_Inv_Det_for_Loc_MTID()", e);
      throw e;
    }
    MLogger.log( -1, this.getClass() + " Load_Inv_Det_for_Loc_MTID()");
    return xmlStr;
  }
  
 /*
    public String Load_Inv_Det(String query, Hashtable ht) throws Exception{
    String xmlStr = "";
    ArrayList mtlList = null;

    InvMstDAO dao = new InvMstDAO(); 
    try {
      mtlList = dao.select(query,ht);
      MLogger.info("Load_Inv_Det() Size : " + mtlList.size());
      if (mtlList.size() > 0) {
        xmlStr += xu.getXMLHeader();
        xmlStr +=
            xu.getStartNode("mtlSeqDetails total='" +
                            String.valueOf(mtlList.size()) +
                            "'");
        for (int i = 0; i < mtlList.size(); i++) {
          Map map = (Map) mtlList.get(i);
          xmlStr += xu.getStartNode("record");
          xmlStr += xu.getXMLNode("LOC", (String) map.get("LOC"));
          xmlStr += xu.getXMLNode("MTID", (String) map.get("MTID"));
          xmlStr += xu.getXMLNode("LOT", (String) map.get("LOT"));
          xmlStr += xu.getXMLNode("ITEM", (String) map.get("SKU"));
          xmlStr += xu.getXMLNode("QTY", (String) map.get("QTY"));
          xmlStr += xu.getEndNode("record");
        }
        xmlStr += xu.getEndNode("mtlSeqDetails");
      }
      else {
        throw new Exception("Detail not found for given inputs");
      }
   
    }
    catch (Exception e) {
      MLogger.exception( "Load_Inv_Det() ",e);
      throw e;
    }

    return xmlStr;
  }
 */
   
   
   public String Load_Inv_Detail(String LOC, String MTID, String LOT) throws Exception{
	    String xmlStr = "";
	    ArrayList mtlList = null;

	    InvMstDAO dao = new InvMstDAO(); 
	    try {
	      mtlList = dao.Load_Inv_Details( LOC, MTID, LOT);
	      MLogger.info("Load_Inv_Detail() Size : " + mtlList.size());
	      if (mtlList.size() > 0) {
	        xmlStr += xu.getXMLHeader();
	        xmlStr +=
	            xu.getStartNode("mtlSeqDetails total='" +
	                            String.valueOf(mtlList.size()) +
	                            "'");
	        for (int i = 0; i < mtlList.size(); i++) {
	          Map map = (Map) mtlList.get(i);
	          xmlStr += xu.getStartNode("record");
	          xmlStr += xu.getXMLNode("LOC", (String) map.get("LOC"));
	          xmlStr += xu.getXMLNode("MTID", (String) map.get("MTID"));
	          xmlStr += xu.getXMLNode("LOT", (String) map.get("LOT"));
	          xmlStr += xu.getXMLNode("ITEM", (String) map.get("SKU"));
	          xmlStr += xu.getXMLNode("QTY", (String) map.get("QTY"));
	          xmlStr += xu.getEndNode("record");
	        }
	        xmlStr += xu.getEndNode("mtlSeqDetails");
	      }
	      else {
	        throw new Exception("Detail not found for given inputs");
	      }
	   
	    }
	    catch (Exception e) {
	      MLogger.exception( "Load_Inv_Detail() ",e);
	      throw e;
	    }

	    return xmlStr;
	  }
   

	public Map getInvData(String query, Hashtable ht) throws Exception {
		String qty = "";
		Map map = null;

		InvMstDAO dao = new InvMstDAO();
		try {
			map = dao.selectRow(query, ht);
			MLogger.info("getInvData() Size : " + map.size());
			if (map.size() > 0) {
				return map;
			} else {
				throw new Exception("Detail not found for given input");
			}

		} catch (Exception e) {
			MLogger.exception("getInvData() ", e);
			throw e;
		}

	}

  
  public boolean insertInvMst(Hashtable ht){
    boolean inserted = false;
    try{

     InvMstDAO dao= new InvMstDAO();
      inserted = dao.insertIntoInvMst(ht);
    }catch(Exception e){ }
    return inserted;
  }
  
   public String getTravellerNo(String company, String loc,String mtid,String sku,String lotNum) throws Exception {
   MLogger.log(1, this.getClass() + " getTravellerNo()");
   String _strTravellerNo="";
   try
   {
     String query=" " +" USERFLD1 "+ " " ;
     Hashtable ht=new Hashtable();
  
     ht.put("PLANT",company);
     ht.put("ITEM",sku);
     ht.put("LOC",loc);
     ht.put("MTID",mtid);
     ht.put("LOT",lotNum);

     InvMstDAO dao = new InvMstDAO(); 
     Map m=dao.selectRow(query,ht);

     _strTravellerNo=(String)m.get("USERFLD1");
    
    if(_strTravellerNo=="" || _strTravellerNo==null)
    {
      _strTravellerNo="";
    }
    MLogger.info("getTravellerNo() : TravellerNO = " + _strTravellerNo);
  }
  catch(Exception e) {
          MLogger.log(0,"######################### Exception :: getTravellerNo() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: getTravellerNo() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
        }
       MLogger.log( -1, this.getClass() + " getTravellerNo()");
    return _strTravellerNo;

 }  
 
 
 public String getInvQty(String mtid) throws Exception {
   MLogger.log(1, this.getClass() + " getInvQty()");
   String _productClass="";
   try
   {
     String query=" " +" QTY "+ " " ;
     Hashtable ht=new Hashtable();
  
   //  ht.put("PLANT",company);
     ht.put("MTID",mtid);
   
     InvMstDAO dao = new InvMstDAO(); 
     Map m=dao.selectRow(query,ht);

     _productClass=(String)m.get("QTY");
    
    if(_productClass=="" || _productClass==null)
    {
      _productClass="0";
    }
    MLogger.info("getInvQty() : InvQty = " + _productClass);
  }
  catch(Exception e) {
          MLogger.log(0,"######################### Exception :: getInvQty() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: getInvQty() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
        }
    MLogger.log( -1, this.getClass() + " getInvQty()");
    return _productClass;
 }  
 
}