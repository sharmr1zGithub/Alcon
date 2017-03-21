package com.murho.db.utils;

import java.util.Hashtable;
import java.util.Map;

import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;

public class PodetUtil {
  com.murho.utils.XMLUtils xu = null;
  com.murho.utils.StrUtils strUtils = null;
  public PodetUtil() {
    xu = new XMLUtils();
    strUtils = new StrUtils();
   
  }

   public boolean isValidMtidToRecv(String aCompany, String aMtid,
                                        String aSku, String aLot){

 //   MLogger.log(0, "isPackNumExists()  %%%%%%%%%%%%%%%%%%%%%%%%  Starts");

    boolean isValid = false;
     PoDetDAO poDao = new PoDetDAO();
     Hashtable ht = new Hashtable();
      ht.put(MDbConstant.COMPANY,aCompany );
      ht.put(MDbConstant.MTID,aMtid);
      ht.put(MDbConstant.SKU,aSku );
      ht.put(MDbConstant.LOT, aLot);
      
    try {
      isValid = poDao.isExist(ht);
    //  MLogger.log(0, "is Valid Lno = " + isValid);
    }
    catch (Exception e) {
      MLogger.exception("isValidMtidToRecv()", e);
      isValid = false;
    }

    return isValid;
  }

 public String Load_Recv_Details(String company,String TravelId,String mtid) throws Exception{
    MLogger.log(1, this.getClass() + " Load_Recv_Details()");
    String xmlStr = "";
        Map map = null;
    PoDetDAO dao = new PoDetDAO(); 
     
    //query
     String query="PART , LOT , QTY";
    //condition
    Hashtable ht=new Hashtable();
    ht.put("PLANT",company);
    ht.put(MDbConstant.TRAVELER_NUM,TravelId);
    ht.put("MTID",mtid);
    xmlStr = xu.getXMLHeader();
    xmlStr = xmlStr + xu.getStartNode("RecvDetails");
    try {
       map = dao.selectRow(query,ht);
       MLogger.info("Record size() :: " + map.size());
       if (map.size() > 0) {
          
            xmlStr = xmlStr + xu.getXMLNode("status", "0");
            xmlStr = xmlStr + xu.getXMLNode("description", "");

            xmlStr = xmlStr + xu.getXMLNode("ITEM",(String) map.get("PART"));
            xmlStr = xmlStr + xu.getXMLNode("LOT",(String) map.get("LOT"));
            xmlStr = xmlStr + xu.getXMLNode("QTY",(String) map.get("QTY"));
            
  
      }
      else
      {
        throw new Exception("Details not found for Delivery No : "  + TravelId +" MTID : " + mtid );
      }
     
      xmlStr = xmlStr + xu.getEndNode("RecvDetails");
      MLogger.log(0, "Value of xml : " + xmlStr);
    }
    catch (Exception e) {
      MLogger.exception("Load_Recv_Details()", e);
      throw e;
    }
    MLogger.log( -1, this.getClass() + " Load_Recv_Details()");
    return xmlStr;
  }
 

} //end of class
