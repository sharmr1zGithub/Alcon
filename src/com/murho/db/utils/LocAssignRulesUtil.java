
package com.murho.db.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.LocationAssigningRulesDAO;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.XMLUtils;



public class LocAssignRulesUtil {
  com.murho.utils.XMLUtils xu = null;
  public LocAssignRulesUtil() {
  xu=new XMLUtils();
  }

public boolean isExistsRulesID(Hashtable ht) throws Exception{

    MLogger.log(0, "isExistsRulesID()  %%%%%%%%%%%%%%%%%%%%%%%%  Starts");

      boolean isExists = false;
      LocationAssigningRulesDAO locAssignDao = new LocationAssigningRulesDAO();
      try {
      isExists = locAssignDao.isExists(ht);   
      MLogger.log(0, "isExistsRulesID = " + isExists);
      }
      catch (Exception e) {
      MLogger.exception("isExistsRulesID()", e);
      throw e;
     }

    return isExists;
  }
  
   public boolean insertRulesID(Hashtable ht) throws Exception{
    boolean inserted = false;
    try{

      LocationAssigningRulesDAO dao = new LocationAssigningRulesDAO();
      inserted = dao.insertIntoLocAssignRules(ht);
    }catch(Exception e){ throw e;}
    return inserted;
  }
 
  public boolean deleteRulesID(Hashtable ht) throws Exception{
    boolean deleted = false;
    try{
     LocationAssigningRulesDAO dao = new LocationAssigningRulesDAO();
      deleted = dao.deleteLocAssignRules(ht);
    }catch(Exception e){throw e; }
    return deleted;
  }
   public boolean updateRulesID(Hashtable htUpdate,Hashtable htCondition) throws Exception{
    boolean update = false;
    try{
     LocationAssigningRulesDAO dao = new LocationAssigningRulesDAO();
      update = dao.updateLocAssignRule(htUpdate,htCondition);

    }catch(Exception e){throw e; }
    return update;
  }
  
  
    public Map getLocAsignedRuleDetails(String RuleId ) throws Exception
                                 {
   // MLogger.log(1, this.getClass() + " getLocAsignedRuleDetails()");
   LocationAssigningRulesDAO dao = new LocationAssigningRulesDAO();
     Hashtable ht=new Hashtable();
     ht.put(MDbConstant.RULE_ID,RuleId);
    
     Map map=new HashMap();
    try{
     String sql=MDbConstant.RULE_ID + "," + MDbConstant.RULE_DESC + "," + MDbConstant.PRD_GRP_ID + "," + MDbConstant.LOC_ID;
     map= dao.selectRow(sql,ht);
    }
          catch(Exception e){MLogger.exception("getLocAsignedRuleDetails :: Exception :",e);
    throw e;
    }
     
    return map;//(String)map.get("MFGLOTSIZE");

   // MLogger.log(-1, this.getClass() + " getOnHandQty()");

  }

  public ArrayList getLocAsgnListStartsWith(String aRuleId) {
 //   MLogger.log(1, this.getClass() + " getLocAsgnListStartsWith()");
    String xmlStr = "";
    ArrayList al = null;
     LocationAssigningRulesDAO dao = new LocationAssigningRulesDAO(); 
   
    try {
      al = dao.getRulesIdStartsWith(aRuleId);
      MLogger.log(0, "getLocAsgnListStartsWith :: Record size() :: " + al.size());
    }
    catch (Exception e) {
       MLogger.exception(this, e);
    }
  //  MLogger.log( -1, this.getClass() + " getLocAsgnListStartsWith()");
    return al;
  }
}