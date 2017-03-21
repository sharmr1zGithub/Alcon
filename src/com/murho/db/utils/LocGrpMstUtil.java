package com.murho.db.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.LocGrpDAO;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.XMLUtils;



public class LocGrpMstUtil {
  com.murho.utils.XMLUtils xu = null;
  public LocGrpMstUtil() {
  xu=new XMLUtils();
  }

  public boolean isExistsLocGrpId(Hashtable htLocGrp) throws Exception{
      boolean isExists = false;
      LocGrpDAO locGrpDao = new LocGrpDAO();
      try {
      isExists = locGrpDao.isExists(htLocGrp);   
      }
      catch (Exception e) {
      MLogger.exception("isValidLoc()", e);
      throw e;
      }
    return isExists;
  }
  
   public boolean insertLocGrpMst(Hashtable ht) throws Exception{
    boolean inserted = false;
    try{
      LocGrpDAO dao= new LocGrpDAO();
      inserted = dao.insertIntolocGrpMst(ht);
    }catch(Exception e){ throw e;}
    return inserted;
  }
 
  public boolean deleteLocGrpId(Hashtable ht) throws Exception{
    boolean deleted = false;
    try{
       LocGrpDAO dao= new LocGrpDAO();
       deleted = dao.deleteLocGrpId(ht);
    }catch(Exception e){throw e; }
    return deleted;
  }
  
   public boolean updateLocGrpId(Hashtable htUpdate,Hashtable htCondition) throws Exception{
    boolean update = false;
      try{
       LocGrpDAO dao= new LocGrpDAO();
        update = dao.updateLocGrpId(htUpdate,htCondition);
  
      }catch(Exception e){throw e; }
    return update;
   }
   
   public Map getLocGrpIdDetails(String aLocGrpId ) throws Exception   {
     LocGrpDAO dao= new LocGrpDAO();
     Hashtable ht=new Hashtable();
     ht.put(MDbConstant.LOC_GRP_ID,aLocGrpId);
     Map map=new HashMap();
    try{
        String sql= " LOC_GRP_ID,LOC_GRP_DESC,TOTAL_BAY ";
        map= dao.selectRow(sql,ht);
    }catch(Exception e){MLogger.exception("getLocGrpIdDetails :: Exception :",e);
        throw e;
    }
    return map;
  }
  
   public ArrayList getLocGrpListStartsWith(String aLocGrpId) {
    ArrayList al = null;
    LocGrpDAO dao= new LocGrpDAO();
    try {
      al = dao.getLocGrpIdStartsWith(aLocGrpId);
    }
    catch (Exception e) {
       MLogger.exception(this, e);
    }
    return al;
  }
 
}