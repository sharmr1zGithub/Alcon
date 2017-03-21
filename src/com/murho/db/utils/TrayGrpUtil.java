package com.murho.db.utils;


import java.util.ArrayList;
import java.util.Hashtable;

import com.murho.dao.TrayGrpDAO;
import com.murho.utils.MLogger;
import com.murho.utils.XMLUtils;



public class TrayGrpUtil {
  com.murho.utils.XMLUtils xu = null;
  public TrayGrpUtil() {
  xu=new XMLUtils();
  }

public boolean isExistsTrayGrp(Hashtable ht) throws Exception{

      boolean isExists = false;
      TrayGrpDAO dao = new TrayGrpDAO();
      try {
      isExists = dao.isExists(ht);   
      }
      catch (Exception e) {
      MLogger.exception("isExistsTrayGrp()", e);
      throw e;
     }

    return isExists;
  }
  
   public boolean insertTrayGrp(Hashtable ht) throws Exception{
    boolean inserted = false;
    try{
      TrayGrpDAO dao = new TrayGrpDAO();
      inserted = dao.insert(ht);
    }catch(Exception e){ throw e;}
    return inserted;
  }
 
  public boolean deleteTrayGrp(Hashtable ht) throws Exception{
    boolean deleted = false;
    try{
      TrayGrpDAO dao = new TrayGrpDAO();
      deleted = dao.delete(ht);
    }catch(Exception e){throw e; }
    return deleted;
  }
   public boolean updateTrayGrp(Hashtable htUpdate,Hashtable htCondition) throws Exception{
    boolean update = false;
    try{
       TrayGrpDAO dao = new TrayGrpDAO();
       update = dao.update(htUpdate,htCondition);

    }catch(Exception e){throw e; }
    return update;
  }
  

   public ArrayList getTrayGrpListStartsWith(String aTray) {
    String xmlStr = "";
    ArrayList al = null;
    TrayGrpDAO dao = new TrayGrpDAO();
    try {
        al = dao.getTrayGrpStartsWith(aTray);
      }
      catch (Exception e) {
         MLogger.exception(this, e);
      }
     return al;
  }
  
}