

package com.murho.db.utils;


import java.util.ArrayList;
import java.util.Hashtable;

import com.murho.dao.DestinationDAO;
import com.murho.utils.MLogger;
import com.murho.utils.XMLUtils;



public class DestinationUtil {
  com.murho.utils.XMLUtils xu = null;
  public DestinationUtil() {
  xu=new XMLUtils();
  }

public boolean isExistsDest(Hashtable htLoc) throws Exception{
   boolean isExists = false;
      DestinationDAO dao = new DestinationDAO();
      try {
      isExists = dao.isExists(htLoc);   
    MLogger.log(0, "isExistsDest = " + isExists);
      }
      catch (Exception e) {
      MLogger.exception("isExistsDest()", e);
      throw e;
     }

    return isExists;
  }
  
   public boolean insertDest(Hashtable ht) throws Exception{
    boolean inserted = false;
    try{

    DestinationDAO dao = new DestinationDAO();
      inserted = dao.insert(ht);
    }catch(Exception e){ throw e;}
    return inserted;
  }
 
  public boolean deleteLocId(Hashtable ht) throws Exception{
    boolean deleted = false;
    try{
    DestinationDAO dao = new DestinationDAO();
      deleted = dao.delete(ht);
    }catch(Exception e){throw e; }
    return deleted;
  }
   public boolean updateDest(Hashtable htUpdate,Hashtable htCondition) throws Exception{
    boolean update = false;
    try{
   DestinationDAO dao = new DestinationDAO();
      update = dao.update(htUpdate,htCondition);

    }catch(Exception e){throw e; }
    return update;
  }
  
 
   public ArrayList getDestListStartsWith(String aDest) {
    String xmlStr = "";
    ArrayList al = null;
    DestinationDAO dao = new DestinationDAO();
    try {
      al = dao.getDestStartsWith(aDest);
      MLogger.log(0, "Record size() :: " + al.size());
    }
    catch (Exception e) {
       MLogger.exception(this, e);
    }
     return al;
  }
  
    public ArrayList getShipToListStartsWith(String aShipTo) {
    String xmlStr = "";
    ArrayList al = null;
    DestinationDAO dao = new DestinationDAO();
    try {
      al = dao.getShip_To_StartsWith(aShipTo);
      MLogger.log(0, "Record size() :: " + al.size());
    }
    catch (Exception e) {
       MLogger.exception(this, e);
    }
    return al;
  }
}