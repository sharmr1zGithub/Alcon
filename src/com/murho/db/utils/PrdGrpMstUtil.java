
package com.murho.db.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.LocGrpDAO;
import com.murho.dao.PrdGrpDAO;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.XMLUtils;



public class PrdGrpMstUtil {
  com.murho.utils.XMLUtils xu = null;
  public PrdGrpMstUtil() {
  xu=new XMLUtils();
  }

public boolean isExistsPrdGrpId(Hashtable htLocGrp) throws Exception{
     boolean isExists = false;
      PrdGrpDAO prdGrpDao = new PrdGrpDAO();
      try {
      isExists = prdGrpDao.isExists(htLocGrp);   
    MLogger.log(0, "isExistsPrdGrpId = " + isExists);
      }
      catch (Exception e) {
      MLogger.exception("isExistsPrdGrpId()", e);
      throw e;
     }

    return isExists;
  }
  
   public boolean insertPrdGrpMst(Hashtable ht) throws Exception{
    boolean inserted = false;
    try{

    PrdGrpDAO dao = new PrdGrpDAO();
      inserted = dao.insertIntoPrdGrpMst(ht);
    }catch(Exception e){ throw e;}
    return inserted;
  }
 
  public boolean deletePrdGrpId(Hashtable ht) throws Exception{
    boolean deleted = false;
    try{
       PrdGrpDAO dao = new PrdGrpDAO();
      deleted = dao.deletePrdGrpId(ht);
    }catch(Exception e){throw e; }
    return deleted;
  }
   public boolean updatePrdGrpId(Hashtable htUpdate,Hashtable htCondition) throws Exception{
    boolean update = false;
    try{
      PrdGrpDAO dao = new PrdGrpDAO();
      update = dao.updatePrdGrpId(htUpdate,htCondition);

    }catch(Exception e){throw e; }
    return update;
  }
  
 
  
  
  public Map getPrdGrpIdDetails(String aPrdGrpId ) throws Exception   {
     PrdGrpDAO dao = new PrdGrpDAO();
     Hashtable ht=new Hashtable();
     ht.put(MDbConstant.PRD_GRP_ID,aPrdGrpId);
     Map map=new HashMap();
    try{

     String sql= " PRD_GRP_ID,PRD_GRP_DESC,TRAYS_PER_BAY ";
     map= dao.selectRow(sql,ht);
    }catch(Exception e){MLogger.exception("getLocGrpIdDetails :: Exception :",e);
    throw e;
    }
    return map;
  }
  
   public ArrayList getPrdGrpListStartsWith(String aPrdGrpId) {
     ArrayList al = null;
    PrdGrpDAO dao = new PrdGrpDAO();
    
    try {
      al = dao.getPrdGrpIdStartsWith(aPrdGrpId);
      MLogger.log(0, "Record size() :: " + al.size());
    }
    catch (Exception e) {
       MLogger.exception(this, e);
    }
     return al;
  }
  
  public String GetTotalBayForLocGrpId( String aLocGrpId) throws Exception {
    
    ArrayList al = null;
    String totalBay ="";
    LocGrpDAO dao = new LocGrpDAO();

    try {
      al = dao.getTotalBay(aLocGrpId);
      MLogger.log(0, "Record size() :: " + al.size());
      if (al.size() > 0) {
             for (int i = 0; i < al.size(); i++) {
          Map map = (Map) al.get(i);
         totalBay =  (String) map.get("TOTAL_BAY");
        }
              }
          }
    catch (Exception e) {
       MLogger.exception(this, e);
      throw e;
    }
    return totalBay;
  }
}