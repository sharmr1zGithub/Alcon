package com.murho.db.utils;


import java.util.ArrayList;
import java.util.Hashtable;

import com.murho.dao.PalletGrpDAO;
import com.murho.utils.MLogger;
import com.murho.utils.XMLUtils;



public class PalletGrpUtil {
  com.murho.utils.XMLUtils xu = null;
  public PalletGrpUtil() {
  xu=new XMLUtils();
  }

public boolean isExistsPalletGrp(Hashtable ht) throws Exception{

    MLogger.log(0, "isExistsPalletGrp()  %%%%%%%%%%%%%%%%%%%%%%%%  Starts");

      boolean isExists = false;
      PalletGrpDAO dao = new PalletGrpDAO();
      try {
      isExists = dao.isExists(ht);   
    MLogger.log(0, "isExistsPalletGrp = " + isExists);
      }
      catch (Exception e) {
      MLogger.exception("isExistsPalletGrp()", e);
      throw e;
     }

    return isExists;
  }
  
   public boolean insertPalletGrp(Hashtable ht) throws Exception{
    boolean inserted = false;
    try{
      PalletGrpDAO dao = new PalletGrpDAO();
      inserted = dao.insert(ht);
    }catch(Exception e){ throw e;}
    return inserted;
  }
 
  public boolean deletePalletGrp(Hashtable ht) throws Exception{
    boolean deleted = false;
    try{
      PalletGrpDAO dao = new PalletGrpDAO();
      deleted = dao.delete(ht);
    }catch(Exception e){throw e; }
    return deleted;
  }
   public boolean updatePalletGrp(Hashtable htUpdate,Hashtable htCondition) throws Exception{
    boolean update = false;
    try{
       PalletGrpDAO dao = new PalletGrpDAO();
       update = dao.update(htUpdate,htCondition);

    }catch(Exception e){throw e; }
    return update;
  }
  

   public ArrayList getShipListStartsWith(String aShipTo) {
    String xmlStr = "";
    ArrayList al = null;
    PalletGrpDAO dao = new PalletGrpDAO();  
    try {
      al = dao.getShipToStartsWith(aShipTo);
      MLogger.log(0, "Record size() :: " + al.size());
    }
    catch (Exception e) {
       MLogger.exception(this, e);
    }
    return al;
  }
  
     public ArrayList getPalletGrpListStartsWith(String aPalletGrp) {
      String xmlStr = "";
      ArrayList al = null;
      PalletGrpDAO dao = new PalletGrpDAO();

      try {
        al = dao.getPalletGrpToStartsWith(aPalletGrp);
        MLogger.log(0, "Record size() :: " + al.size());
      }
      catch (Exception e) {
         MLogger.exception(this, e);
      }
     return al;
  }
}