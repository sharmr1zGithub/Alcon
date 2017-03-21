package com.murho.db.utils;

import java.util.ArrayList;
import java.util.Hashtable;

import com.murho.dao.InvMstDAO;
import com.murho.utils.CibaConstants;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;

public class ReportUtil {
  XMLUtils _xmlUtils = new XMLUtils();
  StrUtils _strUtils = new StrUtils();
  public ReportUtil() {
  }


      public ArrayList getInvList(String aPlant, String aItem, String aBatch,
                              String aLoc, String aMtid,String aTRNO) {
    MLogger.log(1, this.getClass() + " getInvList()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "aItem" + aItem);
      MLogger.log(0, "aBatch" + aBatch);
      MLogger.log(0, "aLoc" + aLoc);
      MLogger.log(0, "MTID" + aMtid);
      MLogger.log(0, "TRNO" + aTRNO);

      if (_strUtils.fString(aItem).length() > 0) {
        ht.put("SKU", aItem+"%");
      }
      if (_strUtils.fString(aBatch).length() > 0) {
        ht.put("LOT", aBatch+"%");
      }
      if (_strUtils.fString(aLoc).length() > 0) {
        ht.put("LOC", aLoc+"%");
      }
      if (_strUtils.fString(aMtid).length() > 0) {
        ht.put("MTID", aMtid+"%");

      }
        if (_strUtils.fString(aTRNO).length() > 0) {
        ht.put("USERFLD1", aTRNO+"%");

      }
      
      //String   aQuery="SELECT SKU,LOT,LOC,QTY,ISNULL(QTYALLOC,0) AS QTYALLOC,MTID FROM INVMST WHERE PLANT ='CH79' and cast(qty as decimal) > 0"  ;  
      String   aQuery="SELECT SKU,LOT,LOC,QTY,ISNULL(QTYALLOC,0) AS QTYALLOC,MTID FROM INVMST WHERE PLANT ='" + CibaConstants.cibacompanyName + "' and cast(qty as decimal) > 0"  ;  
      arrList = invdao.selectForReport(aQuery, ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :repportUtil :: getInvList:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getInvList()");
    return arrList;
  }
  
    public ArrayList getInvListReport(String aPlant, String aItem, String aBatch, String aLoc) {
    MLogger.log(1, this.getClass() + " getInvList()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "aItem" + aItem);
      MLogger.log(0, "aBatch" + aBatch);
      MLogger.log(0, "aLoc" + aLoc);
   

      if (_strUtils.fString(aItem).length() > 0) {
        ht.put("SKU", aItem+"%");
      }
      if (_strUtils.fString(aBatch).length() > 0) {
        ht.put("LOT", aBatch+"%");
      }
      if (_strUtils.fString(aLoc).length() > 0) {
        ht.put("LOC", aLoc+"%");
      }
   
      
      // String   aQuery="SELECT SKU,LOT,LOC,sum(cast(QTY as integer)) as QTY,(select stkuom from itemmst where item =a.sku) as UOM,(select itemdesc from itemmst where item =a.sku) as SKU_DESC FROM INVMST a  WHERE PLANT ='CH79' and cast(qty as decimal) > 0 "  ;  
      // String extraCond =" GROUP BY LOT,SKU,LOC,uom " ;
      
       String   aQuery="SELECT SKU,LOT,LOC,sum(cast(QTY as integer)) as QTY,(select stkuom from itemmst where item =a.sku) as UOM,(select itemdesc from itemmst where item =a.sku) as SKU_DESC FROM INVMST a  WHERE PLANT ='" + CibaConstants.cibacompanyName + "' and cast(qty as decimal) > 0 "  ;  
       String extraCond =" GROUP BY LOT,SKU,LOC,uom " ;
       
       
          arrList = invdao.selectForReportCond(aQuery, ht,extraCond );
    }
    catch (Exception e) {
      MLogger.log("Exception :repportUtil :: getInvList:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getInvList()");
    return arrList;
  }
 
  public ArrayList getMovHisList( Hashtable ht, String afrmDate, String atoDate) {
    MLogger.log(1, this.getClass() + " getMovHisList()");
    ArrayList arrList = new ArrayList();
    String sCondition = "";
    try {
      InvMstDAO invdao = new InvMstDAO();
      if (afrmDate.length() > 0) {
        sCondition = sCondition + " AND CRAT  >= '" + afrmDate + "'  ";
        if (atoDate.length() > 0) {
          sCondition = sCondition + "AND CRAT  <= '" + atoDate + "'  ";
        }
      }
      else {
        if (atoDate.length() > 0) {
          sCondition = sCondition + " AND CRAT  <= '" + atoDate + "'  ";
        }
      }

    /* StringBuffer sql=new StringBuffer(" SELECT ISNULL(CRAT,'') AS CRAT,ISNULL(MOVTID,'') AS MOVTID,ISNULL(SKU,'') AS SKU,ISNULL(TRAVELER,'') AS TRAVELER, ");
     sql.append(" ISNULL(MTID,'') AS MTID,ISNULL(LOT,'') AS LOT,ISNULL(CRBY,'') AS CRBY,ISNULL(QTY,0) AS QTY,isnull(loc,'') as loc,isnull(remark,'') as remark,isnull(crtime,'') as crtime,");   
     sql.append(" ISNULL(USERFLD6,'')AS REASONCODE FROM MOVHIS WHERE PLANT = 'CH79' " );*/
    
     StringBuffer sql=new StringBuffer(" SELECT ISNULL(CRAT,'') AS CRAT,ISNULL(MOVTID,'') AS MOVTID,ISNULL(SKU,'') AS SKU,ISNULL(TRAVELER,'') AS TRAVELER, ");
     sql.append(" ISNULL(MTID,'') AS MTID,ISNULL(LOT,'') AS LOT,ISNULL(CRBY,'') AS CRBY,ISNULL(QTY,0) AS QTY,isnull(loc,'') as loc,isnull(remark,'') as remark,isnull(crtime,'') as crtime,");   
     sql.append(" ISNULL(USERFLD6,'')AS REASONCODE FROM MOVHIS WHERE PLANT = '" + CibaConstants.cibacompanyName + "' " );
     sql.append(sCondition);
     arrList = invdao.selectForReport(sql.toString(), ht);
     
    }
    catch (Exception e) {
      MLogger.log("Exception :repportUtil :: getMovHisList:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getMovHisList()");
    return arrList;
  }


public ArrayList getMovHisList_stocktransfer(Hashtable ht, String afrmDate, String atoDate) {
     ArrayList arrList = new ArrayList();
    String sCondition = "";
    try {
      InvMstDAO invdao = new InvMstDAO();
      if (afrmDate.length() > 0) {
        sCondition = sCondition + " AND CRAT  >= '" + afrmDate + "'  ";
        if (atoDate.length() > 0) {
          sCondition = sCondition + "AND CRAT  <= '" + atoDate + "'  ";
        }
      }
      else {
        if (atoDate.length() > 0) {
          sCondition = sCondition + " AND CRAT  <= '" + atoDate + "'  ";
        }
      }

       
    /* StringBuffer sql=new StringBuffer(" SELECT  MOVTID,MTID,SKU,LOT,CRBY,cast(QTY as decimal) as QTY,CRAT,LOC from movhis a ");   
     sql.append("  WHERE PLANT = 'CH79'  AND MOVTID in('TRANSFER-IN','TRANSFER-OUT') " );*/
     StringBuffer sql=new StringBuffer(" SELECT  MOVTID,MTID,SKU,LOT,CRBY,cast(QTY as decimal) as QTY,CRAT,LOC from movhis a ");   
     sql.append("  WHERE PLANT = '" + CibaConstants.cibacompanyName + "'  AND MOVTID in('TRANSFER-IN','TRANSFER-OUT') " );
     sql.append(sCondition);
     sql.append(" order by mtid,crtime ");
        
      arrList = invdao.selectForReport(sql.toString(), ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :repportUtil :: getMovHisList:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getMovHisList()");
    return arrList;
  }


public ArrayList getMovHisList_stockAdjust(Hashtable ht, String afrmDate, String atoDate) {

    ArrayList arrList = new ArrayList();
    String sCondition = "";
    try {
      InvMstDAO invdao = new InvMstDAO();
      if (afrmDate.length() > 0) {
        sCondition = sCondition + " AND CRAT  >= '" + afrmDate + "'  ";
        if (atoDate.length() > 0) {
          sCondition = sCondition + "AND CRAT  <= '" + atoDate + "'  ";
        }
      }
      else {
        if (atoDate.length() > 0) {
          sCondition = sCondition + " AND CRAT  <= '" + atoDate + "'  ";
        }
      }

     /*StringBuffer sql=new StringBuffer(" SELECT ISNULL(CRAT,'') AS CRAT,ISNULL(MOVTID,'') AS MOVTID,ISNULL(SKU,'') AS SKU,ISNULL(TRAVELER,'') AS TRAVELER, ");
     sql.append(" ISNULL(MTID,'') AS MTID,ISNULL(LOT,'') AS LOT,ISNULL(CRBY,'') AS CRBY,ISNULL(CRAT,'') AS CRAT,cast(ISNULL(QTY,0) as integer) AS QTY,isnull(loc,'') as loc,isnull(remark,'') as remark,isnull(crtime,'') as crtime,");
     sql.append("ISNULL(USERFLD6,'') REASONCODE FROM MOVHIS WHERE PLANT = 'CH79'  AND MOVTID in('STK_ADJ_OUT','STK_ADJ_IN') " );*/
     
       StringBuffer sql=new StringBuffer(" SELECT ISNULL(CRAT,'') AS CRAT,ISNULL(MOVTID,'') AS MOVTID,ISNULL(SKU,'') AS SKU,ISNULL(TRAVELER,'') AS TRAVELER, ");
     sql.append(" ISNULL(MTID,'') AS MTID,ISNULL(LOT,'') AS LOT,ISNULL(CRBY,'') AS CRBY,ISNULL(CRAT,'') AS CRAT,cast(ISNULL(QTY,0) as integer) AS QTY,isnull(loc,'') as loc,isnull(remark,'') as remark,isnull(crtime,'') as crtime,");
     sql.append("ISNULL(USERFLD6,'') REASONCODE FROM MOVHIS WHERE PLANT = '" + CibaConstants.cibacompanyName + "'  AND MOVTID in('STK_ADJ_OUT','STK_ADJ_IN') " );
     
     sql.append(sCondition);
      
      arrList = invdao.selectForReport(sql.toString(), ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :repportUtil :: getMovHisList:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getMovHisList()");
    return arrList;
  }

 public ArrayList getStockDetails(String aPlant,String MTID1) {
    MLogger.log(1, this.getClass() + " getStockDetails()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      String aQuery= "SELECT MTID,ITEM,LOT,LOC,QTY FROM INVMST WHERE MTID = '"+MTID1+"' " ;
      arrList = invdao.selectForReport(aQuery,ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :repportUtil :: getStockDetails:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getStockDetails()");
    return arrList;
  }

}
