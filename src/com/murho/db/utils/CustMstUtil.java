
package com.murho.db.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.CustMstDAO;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.XMLUtils;



public class CustMstUtil {
  com.murho.utils.XMLUtils xu = null;
  public CustMstUtil() {
  xu=new XMLUtils();
  }

  public boolean isExistsCustMst(Hashtable ht) throws Exception{
    MLogger.log(0, "isExistsCustMst()  %%%%%%%%%%%%%%%%%%%%%%%%  Starts");
      boolean isExists = false;
      CustMstDAO dao = new CustMstDAO();
      try {
      isExists = dao.isExists(ht);   
      MLogger.log(0, "isExistsCustMst = " + isExists);
      }
      catch (Exception e) {
      MLogger.exception("isExistsCustMst()", e);
      throw e;
     }
     return isExists;
  }
  
   public boolean insertCustMst(Hashtable ht) throws Exception{
    boolean inserted = false;
    try{
    CustMstDAO dao = new CustMstDAO();
      inserted = dao.insertCustMst(ht);
    }catch(Exception e){ throw e;}
    return inserted;
  }
 
  public boolean deleteCustMst(Hashtable ht) throws Exception{
    boolean deleted = false;
    try{
     CustMstDAO dao = new CustMstDAO();
      deleted = dao.deleteCustMst(ht);
    }catch(Exception e){throw e; }
    return deleted;
  }
  
   public boolean updateCustMst(Hashtable htUpdate,Hashtable htCondition) throws Exception{
    boolean update = false;
    try{
       CustMstDAO dao = new CustMstDAO();
       update = dao.updateCustMst(htUpdate,htCondition);

    }catch(Exception e){throw e; }
    return update;
  }
  

  
   public Map getCustMstDetails(String aShipTo,String Type ) throws Exception
   {
      CustMstDAO dao = new CustMstDAO();
      Hashtable ht=new Hashtable();
      ht.put(MDbConstant.SHIP_TO,aShipTo);
      ht.put(MDbConstant.CUST_TYPE,Type);
      Map map=new HashMap();
      try{
         String sql= " * ";
         map= dao.selectRow(sql,ht);
      }catch(Exception e){MLogger.exception("getCustMstDetails :: Exception :",e);
      throw e;
      }
    return map;
  }
   public ArrayList getCustMstListStartsWith() {
   ArrayList al = null;
   CustMstDAO dao = new CustMstDAO();
    try {
      al = dao.getTypeToStartsWith();
      MLogger.log(0, "Record size() :: " + al.size());
    }
    catch (Exception e) {
       MLogger.exception(this, e);
    }
      return al;
  }
 
}