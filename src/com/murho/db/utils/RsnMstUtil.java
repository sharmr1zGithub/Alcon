package com.murho.db.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.RsnMstDAO;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.XMLUtils;



public class RsnMstUtil {
  com.murho.utils.XMLUtils xu = null;
  public RsnMstUtil() {
  xu=new XMLUtils();
}

public boolean isExistsResaonCode(Hashtable htLoc) throws Exception{

    MLogger.log(0, "isExistsResaonCode()  %%%%%%%%%%%%%%%%%%%%%%%%  Starts");

      boolean isExists = false;
      RsnMstDAO Dao = new RsnMstDAO();
      try {
      isExists = Dao.isExists(htLoc);   
      MLogger.log(0, "isExistsResaonCode = " + isExists);
      }
      catch (Exception e) {
      MLogger.exception("isExistsResaonCode()", e);
      throw e;
     }

    return isExists;
  }
  
   public boolean insertRsnMst(Hashtable ht) throws Exception{
    boolean inserted = false;
    try{

    RsnMstDAO itemDao = new RsnMstDAO();
      inserted = itemDao.insertIntoRsnMst(ht);
    }catch(Exception e){ throw e;}
    return inserted;
  }
 
  public boolean deleteReasonMstId(Hashtable ht) throws Exception{
    boolean deleted = false;
    try{
       RsnMstDAO dao = new RsnMstDAO();
       deleted = dao.deleteReasonCode(ht);
    }catch(Exception e){throw e; }
    return deleted;
  }
   public boolean updateReasonMst(Hashtable htUpdate,Hashtable htCondition) throws Exception{
    boolean update = false;
    try{
    RsnMstDAO dao = new RsnMstDAO();
      update = dao.updateReasonCode(htUpdate,htCondition);

    }catch(Exception e){throw e; }
    return update;
  }


 public Map getReasonDetails(String aItemId ) throws Exception
                                 {
     RsnMstDAO dao = new RsnMstDAO();
     Hashtable ht=new Hashtable();
     ht.put(MDbConstant.RSNCODE,aItemId);
     Map map=new HashMap();
    try{

     String sql= " RSNCODE,RSNDESC";
     map= dao.selectRow(sql,ht);
    }catch(Exception e){MLogger.exception("getLocAsignedRuleDetails :: Exception :",e);
    throw e;
    }
    return map;
  }
  
     public ArrayList getReasonMstDetails(String aItemId) {
    
     ArrayList al = null;
     RsnMstDAO dao = new RsnMstDAO();
          
     try {
      al = dao.getReasonMstDetails(aItemId);
      MLogger.log(0, "Record size() :: " + al.size());
     }
    catch (Exception e) {
       MLogger.exception(this, e);
    }
     return al;
  }
  
  
  
}