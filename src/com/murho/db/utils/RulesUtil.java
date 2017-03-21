
package com.murho.db.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.LocationAssigningRulesDAO;
import com.murho.dao.RulesDAO;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.XMLUtils;



public class RulesUtil {
  com.murho.utils.XMLUtils xu = null;
  public RulesUtil() {
  xu=new XMLUtils();
  }

public boolean isExistsRules(Hashtable ht) throws Exception{

    MLogger.log(0, "isExistsRules()  %%%%%%%%%%%%%%%%%%%%%%%%  Starts");

      boolean isExists = false;
      RulesDAO dao = new RulesDAO();
      try {
      isExists = dao.isExists(ht);   
      MLogger.log(0, "isExistsRules = " + isExists);
      }
      catch (Exception e) {
      MLogger.exception("isExistsRules()", e);
      throw e;
     }

    return isExists;
  }
  
   public boolean insertRules(Hashtable ht) throws Exception{
    boolean inserted = false;
    try{

        RulesDAO dao = new RulesDAO();
        inserted = dao.insertIntoRules(ht);
    }catch(Exception e){ throw e;}
    return inserted;
  }
 
  public boolean deleteRules(Hashtable ht) throws Exception{
    boolean deleted = false;
    try{
       RulesDAO dao = new RulesDAO();
      deleted = dao.deleteRules(ht);
    }catch(Exception e){throw e; }
    return deleted;
  }
   public boolean updateRules(Hashtable htUpdate,Hashtable htCondition) throws Exception{
    boolean update = false;
    try{
     RulesDAO dao = new RulesDAO();
     update = dao.updateRule(htUpdate,htCondition);

    }catch(Exception e){throw e; }
    return update;
  }
  

  
  
    public Map getRuleDetails(String Ship_To ,String prdClass) throws Exception
                                 {
   // MLogger.log(1, this.getClass() + " getRuleDetails()");
     RulesDAO dao = new RulesDAO();
     Hashtable ht=new Hashtable();
     ht.put(MDbConstant.SHIP_TO,Ship_To);
     ht.put(MDbConstant.PRD_CLS_ID,prdClass);
     Map map=new HashMap();
    try{

     String sql=MDbConstant.SHIP_TO + "," + MDbConstant.PREFIX + "," + MDbConstant.PRD_CLS_ID + "," + MDbConstant.PALLET_GRP+","+MDbConstant.TYPE+","+MDbConstant.TRAY_GRP_ID+","+ "CATEGORY";
     map= dao.selectRow(sql,ht);
    }catch(Exception e){MLogger.exception("getRuleDetails :: Exception :",e);
    throw e;
    }
    return map;

   // MLogger.log(-1, this.getClass() + " getRuleDetails()");

  }

 

 public ArrayList getRulesSummary(String aShip_To) {
    MLogger.log(1, this.getClass() + " getRulesSummary()");
    ArrayList arrList = new ArrayList();
    try {
      RulesDAO dao = new RulesDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "aShip_To" + aShip_To);
  
      String aQuery = "  select a.SHIP_TO,a.PREFIX,a.PRD_CLS_ID,a.PALLET_GRP,a.TRAY_GRP_ID,a.TYPE,b.UOM,ISNULL(b.QTY_PER_COLUMN,'') as QTY_PER_COLUMN,isnull(b.NO_OF_COLUMN,'') as NO_OF_COLUMN,isnull(b.QTY_PER_TRAY,'') as QTY_PER_TRAY,"+
      "isnull(b.NO_OF_TRAY_PER_LAYER,'') as NO_OF_TRAY_PER_LAYER,b.NO_OF_LAYERS,b.NO_OF_LAYERS_PER_PALLET from rules_mst a,prd_class_mst b where a.prd_cls_id = b.prd_cls_id and a.ship_to ='"+aShip_To+"' ";

      
      MLogger.log(0,"getRulesSummary(aQuery)::"+aQuery);
      arrList = dao.selectForReport(aQuery,ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getRulesSummary :: getRulesSummary:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getRulesSummary()");
    return arrList;
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