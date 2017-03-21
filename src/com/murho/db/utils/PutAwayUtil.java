package com.murho.db.utils;


import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.murho.dao.InvMstDAO;
import com.murho.dao.PickingDAO;
import com.murho.dao.RecvDetDAO;
import com.murho.gates.DbBean;
import com.murho.tran.WmsPutAway;
import com.murho.tran.WmsPutAwayGenerateFile;
import com.murho.tran.WmsTran;
import com.murho.utils.MLogger;
import com.murho.utils.XMLUtils;



public class PutAwayUtil {
  com.murho.utils.XMLUtils xu = null;
  public PutAwayUtil() {
  xu=new XMLUtils();
  }


  
  
 
  public boolean getReceiveCount(String aPlant,String mtid,String TR) {
    MLogger.log(1, this.getClass() + " getReceiveCount()");
    ArrayList arrList = new ArrayList();
    boolean flag=false;
    try {
      RecvDetDAO recvDAO = new RecvDetDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "mtid" + mtid);
  
      String aQuery = " SELECT RECEIVESTATUS FROM RECVDET WHERE MTID='"+mtid+"' AND RECEIVESTATUS IN ('N','O','D')";
     
      arrList = recvDAO.selectFor(aQuery,ht);
     
      if(arrList.size()> 0 )
      {
        flag=true;
      }
    
    }
    catch (Exception e) {
      MLogger.log("Exception :getReceiveCount :: getReceiveCount:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getReceiveCount()");
    return flag;
  }

 public String Load_PutAway_Details(String company,String mtid) throws Exception{
    MLogger.log(1, this.getClass() + " Load_PutAway_Details(************)");
    String xmlStr = "";
    Map map = null;
    InvMstDAO dao = new InvMstDAO(); 
    RecvDetDAO recvDetDAO=new RecvDetDAO();
    
    //query
     String query="TRAVELER,MTID,SKU,LOT,RECVQTY,LOC,PUTAWAYSTATUS";
     
     //String queryInv="LOC";
    //condition
    Hashtable ht=new Hashtable();
    ht.put("MTID",mtid);
    
   //  Hashtable htInv=new Hashtable();
   //  htInv.put("MTID",mtid);
   
     
    xmlStr = xu.getXMLHeader();
    xmlStr = xmlStr + xu.getStartNode("PutAway_Details");
    try {
      map = recvDetDAO.selectRow(query,ht);
      MLogger.log(0, "Record size() :: " + map.size());
      if (map.size() > 0) {
            xmlStr = xmlStr + xu.getXMLNode("status","0");
            xmlStr = xmlStr + xu.getXMLNode("description","");
            
            xmlStr = xmlStr + xu.getXMLNode("TRAVEL_NO",(String) map.get("TRAVELER"));
            xmlStr = xmlStr + xu.getXMLNode("SKU",(String) map.get("SKU"));
            xmlStr = xmlStr + xu.getXMLNode("MTID",(String) map.get("MTID"));
            xmlStr = xmlStr + xu.getXMLNode("LOT",(String) map.get("LOT"));
            xmlStr = xmlStr + xu.getXMLNode("LOC",(String) map.get("LOC"));
            xmlStr = xmlStr + xu.getXMLNode("QTY",(String) map.get("RECVQTY"));
            xmlStr = xmlStr + xu.getXMLNode("PUTAWAYSTATUS",(String) map.get("PUTAWAYSTATUS"));
         
      }else
      {
        throw new Exception("Not a Valid MTID");
      }
     
      xmlStr = xmlStr + xu.getEndNode("PutAway_Details");
    }
    catch (Exception e) {
      xmlStr = xmlStr + xu.getXMLNode("1", e.toString());
      MLogger.exception("Load_PutAway_Details()", e);
      throw e;
    }
    return xmlStr;
  }
  
  public synchronized String Process_PutAway(Map obj) throws Exception {
   boolean flag = false;
   UserTransaction ut = null;
   try
   {
        ut = com.murho.gates.DbBean.getUserTranaction();
        ut.begin();
        flag = process_Wms_PutAway(obj);
        MLogger.log(0,"After processing --> process_Wms_PutAway :: " + flag );
    
  
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
      xmlStr = xu.getXMLMessage(0, "Putaway successfull!");
    }else{
         xmlStr = xu.getXMLMessage(1, "Error in putaway ");
    }
    return xmlStr;
   
  }  
  
    private boolean process_Wms_PutAway(Map map) throws Exception{
    MLogger.info(" process_Wms_PutAway() starts");
    boolean flag=false;
    flag=true;
    WmsTran tran=new WmsPutAway();
    flag= tran.processWmsTran(map);
    MLogger.info(" process_Wms_PutAway() ends");
    return flag;
  }
  

  
  public boolean isExists(Hashtable ht) throws Exception
  {
      boolean flag=false;
      RecvDetDAO dao= new RecvDetDAO();
      flag = dao.isExists(ht);
      return flag;
  }
  
   
  
       public ArrayList getLotDetail(String lot,String traveler) {
    MLogger.log(1, this.getClass() + " getPickerLotDetail()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
    
    
    String   aQuery="select lot,mtid,sku,qty,loc,status from ob_travel_det where lot='"+lot+"' and traveler_id='"+traveler+"'"  ;  
        
    arrList = invdao.selectForReport(aQuery, ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getPickerLotDetail :: getPickerLotDetail:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getPickerLotDetail()");
    return arrList;
  }


public boolean Process_PutAway_GenTxtFile(Map obj) throws Exception {
   boolean flag = false;
   UserTransaction ut = null;
   try
   {
     ut = com.murho.gates.DbBean.getUserTranaction();
     ut.begin();
     flag = process_Wms_PutAwayGenTxtFile(obj);
     
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
   return flag;
  }  
  
    private boolean process_Wms_PutAwayGenTxtFile(Map map) throws Exception{
    MLogger.info(" process_Wms_PutAwayGenTxtFile() starts");
    boolean flag=false;
    flag=true;
    WmsTran tran=new WmsPutAwayGenerateFile();
    flag= tran.processWmsTran(map);
    MLogger.info(" process_Wms_PutAwayGenTxtFile() ends");
    return flag;
  }

    /*  Added by Ranjana 
     *  Purpose: LOT restriction in PDA under ticket WO0000000284867 
     *  for the process of PUTAWAY*/
    
	public String LOT_Blocked_PutAway(Hashtable ht) throws Exception {
		ResultSet rs = null;
		CallableStatement cs = null;
		Map map = new HashMap();
		java.sql.Connection con = null;
		String XmlStr = "";
		
		MLogger.log(1, this.getClass() + "call PROC_CHECK_LOTRESTRICTION_PUTAWAY");
		XmlStr = xu.getXMLHeader();
		try {
			con = DbBean.getConnection();
			cs = con.prepareCall("{call dbo.[PROC_CHECK_LOTRESTRICTION_PUTAWAY](?,?)}");
			cs.setString(1, (String) ht.get("TRAVELER"));
			cs.setString(2, (String) ht.get("LOT"));
			
			rs = cs.executeQuery();
					
			while (rs.next()) {		
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					MLogger.log(1, rs.getMetaData().getColumnLabel(i) + " selected KEY ");
					MLogger.log(1, rs.getString(i) + " selected value ");
					map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
				}
			}
			MLogger.log(1, map.size() + " map size ");
			if(map.size()>0){
			XmlStr += xu.getStartNode("PutAwayDetails");
			XmlStr += xu.getXMLNode("LOTSTATUS", (String) map.get("LOTSTATUS").toString());
			
			MLogger.log(1, XmlStr + "XMLSTR.........");
			}
			XmlStr+=xu.getEndNode("PutAwayDetails");
		} catch (Exception e) {
			MLogger.log(0, "LOT_Blocked_PutAway() Failed .............................");
			throw e;
		}finally{
		    if (rs != null) {
		        rs.close();
		     }
		     if (con != null) {
		        DbBean.closeConnection(con);
		      }
		     }
		return XmlStr;
	}
  

}