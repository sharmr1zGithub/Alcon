
package com.murho.db.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.LocMstDAO;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.XMLUtils;



public class LocMstUtil {
  com.murho.utils.XMLUtils xu = null;
  public LocMstUtil() {
  xu=new XMLUtils();
  }

public boolean isExistsLocId(Hashtable htLoc) throws Exception{

    MLogger.log(0, "isExistsLocId()  %%%%%%%%%%%%%%%%%%%%%%%%  Starts");

      boolean isExists = false;
      LocMstDAO locDao = new LocMstDAO();
      try {
      isExists = locDao.isExists(htLoc);   
       }
      catch (Exception e) {
      MLogger.exception("isExistsLocId()", e);
      throw e;
     }

    return isExists;
  }
  
   public boolean insertLocMst(Hashtable ht) throws Exception{
    boolean inserted = false;
    try{

    LocMstDAO locDao = new LocMstDAO();
      inserted = locDao.insertIntolocMst(ht);
    }catch(Exception e){ throw e;}
    return inserted;
  }
 
  public boolean deleteLocId(Hashtable ht) throws Exception{
    boolean deleted = false;
    try{
      LocMstDAO dao = new LocMstDAO();
      deleted = dao.deleteLocId(ht);
    }catch(Exception e){throw e; }
    return deleted;
  }
   public boolean updateLocId(Hashtable htUpdate,Hashtable htCondition) throws Exception{
    boolean update = false;
    try{
    LocMstDAO dao = new LocMstDAO();
      update = dao.updateLocId(htUpdate,htCondition);

    }catch(Exception e){throw e; }
    return update;
  }
  
 /* public ArrayList getLocIdDetails(String aLocId) throws Exception{
    ArrayList arrList = new ArrayList();
    try{
      LocMstDAO dao = new LocMstDAO();
      arrList = dao.getLocDetails(aLocId);
    }catch(Exception e){throw e; }
    return arrList;
  }*/
  
    public Map getLocIdDetails(String aLocId ) throws Exception
                                 {
   // MLogger.log(1, this.getClass() + " getLocIdDetails()");
      LocMstDAO dao = new LocMstDAO();
     Hashtable ht=new Hashtable();
     ht.put(MDbConstant.LOC_ID,aLocId);
     Map map=new HashMap();
    try{

     String sql= " LOC_ID ,LOC_DESC,LOC_GRP_ID AS LOCGRPID ,SPACE_REM  ";
     map= dao.selectRow(sql,ht);
    }catch(Exception e){MLogger.exception("getLocIdDetails :: Exception :",e);
    throw e;
    }
    return map;//(String)map.get("MFGLOTSIZE");

   // MLogger.log(-1, this.getClass() + " getOnHandQty()");

  }
  
  
   public ArrayList getLocListStartsWith(String aLocId) {
 //   MLogger.log(1, this.getClass() + " getLocListStartsWith()");
    String xmlStr = "";
    ArrayList al = null;
    LocMstDAO dao = new LocMstDAO();
    
    MLogger.info("Input Values");
   
    try {
      al = dao.getLocIdStartsWith(aLocId);
      MLogger.log(0, "Record size() :: " + al.size());
    }
    catch (Exception e) {
       MLogger.exception(this, e);
    }
  //  MLogger.log( -1, this.getClass() + " getLocListStartsWith()");
    return al;
  }
}