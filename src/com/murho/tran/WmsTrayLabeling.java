package com.murho.tran;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.MovHisDAO;
import com.murho.dao.OBTravelerDetDAO;
import com.murho.db.utils.PickingUtil;
import com.murho.utils.CibaConstants;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;

public class WmsTrayLabeling implements WmsTran
{
  public WmsTrayLabeling()
  {
  }
  public boolean processWmsTran(Map map) throws Exception
  {
    MLogger.info("###############  WMS TRANSACTION : TRAY LABELING ###############");
    boolean tranFlag=false;
      try {
        MLogger.info("1.process OB_TRAVEL_DET - XXXXXXXX Stage : 1");
        tranFlag=processOBTravelDet(map);
        
        if(tranFlag)
        {
          MLogger.info("2.process Movhis - XXXXXXXX Stage : 2");
          tranFlag=processMoveHis(map);
        }       
      }
      catch (Exception ex) {
      tranFlag = false;
      throw ex;
    }
    return tranFlag;
  }
  

  //Commanded by deen due to change by Samatha on Aug 26 2010
 /* private boolean processOBTravelDet(Map map) throws Exception
  {
      Hashtable ht=new Hashtable();
      OBTravelerDetDAO dao = new OBTravelerDetDAO(); 
      boolean flag=false;
      boolean isDataPresent=false;

      try
      {
        ht.put(MDbConstant.TRAVELER_ID,map.get(MDbConstant.TRAVELER_ID));
        ht.put("PALLETID",map.get(MDbConstant.PALLET));
        ht.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
        ht.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM));
        ht.put(MDbConstant.MTID,map.get(MDbConstant.MTID));
        ht.put(MDbConstant.TRAYID,map.get(MDbConstant.TRAYID));
        String extCond = "and mtid = '"+map.get(MDbConstant.MTID)+"' ";
        isDataPresent=dao.isExist(ht);
       
        MLogger.info("isExists : " + isDataPresent);
        if(isDataPresent)
        {
          //query 
          String ftray =(String)map.get("FULLTRAY");
          if (ftray.equalsIgnoreCase("1"))
          {
          StringBuffer sql=new StringBuffer(" SET ");
          sql.append(" TrayLableQty = " +   map.get(MDbConstant.RECV_QTY) + " ");
          sql.append(",TrayStatus  = '" +   map.get(MDbConstant.TrayDetStatus) + "'");
          sql.append(",PalletStatus  = 'C'");
          //Added by samatha on Feb02 2010 to update lotbarcode value for lot
          sql.append(",LOT_BARCODE  =  '" +   map.get("LOTBARCODEVAL") + "'");
          sql.append(",UPBY  = '" +   map.get(MDbConstant.LOGIN_USER) + "'");
          flag=dao.update(sql.toString(),ht,extCond);
          MLogger.info("update : " + flag);
          }else
          {
              Hashtable htQuery = new Hashtable();
              htQuery.put("PALLETID",map.get(MDbConstant.PALLET));
              htQuery.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
              htQuery.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM));
              htQuery.put(MDbConstant.TRAYID,map.get(MDbConstant.TRAYID));
              int trqty =new Integer((String)map.get(MDbConstant.RECV_QTY)).intValue();
              
             ArrayList al = new PickingUtil().get_sku_line_Details( (String)map.get(MDbConstant.TRAVELER_ID),htQuery);
             if (al.size() > 0) {
                 for (int i = 0; i < al.size(); i++) {
                  Map mapLn = (Map) al.get(i);
                  int Lnqty =new Integer((String)mapLn.get("qty")).intValue();
                  
                  if (trqty >=Lnqty){
                   MLogger.log("Count Entered"+i);
                    StringBuffer sql1=new StringBuffer(" SET ");
                    sql1.append(" TrayLableQty = " +  Lnqty + " ");
                    sql1.append(",TrayStatus  = '" +   map.get(MDbConstant.TrayDetStatus) + "'");
                    //Added by samatha on Feb02 2010 to update lotbarcode value for lot
                    sql1.append(",LOT_BARCODE  =  '" +   map.get("LOTBARCODEVAL") + "'");
                    sql1.append(",PalletStatus  = 'C'");
                    sql1.append(",UPBY  = '" +   map.get(MDbConstant.LOGIN_USER) + "'");
                 
                  
                    Hashtable htsinoUp = new Hashtable();
                    htsinoUp.put("Traveler_Id",map.get(MDbConstant.TRAVELER_ID));
                    htsinoUp.put("PALLETID",map.get(MDbConstant.PALLET));
                    htsinoUp.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
                    htsinoUp.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM)); 
                    htsinoUp.put("sinoln",mapLn.get("sinoln")); 
                    htsinoUp.put("fulltray",map.get("FULLTRAY")); 
                    String extCond1="";
                    flag=dao.update(sql1.toString(),htsinoUp,extCond1);
                  }else
                  {
                    StringBuffer sql1=new StringBuffer(" SET ");
                    sql1.append(" TrayLableQty = " +  trqty + " ");
                    sql1.append(",TrayStatus  = '" +   map.get(MDbConstant.TrayDetStatus) + "'");
                      //Added by samatha on Feb02 2010 to update lotbarcode value for lot
                    sql1.append(",LOT_BARCODE  =  '" +   map.get("LOTBARCODEVAL") + "'");
                    sql1.append(",PalletStatus  = 'C'");
                    sql1.append(",UPBY  = '" +   map.get(MDbConstant.LOGIN_USER) + "'");
                 
                  
                    Hashtable htsinoUp = new Hashtable();
                    htsinoUp.put("Traveler_Id",map.get(MDbConstant.TRAVELER_ID));
                    htsinoUp.put("PALLETID",map.get(MDbConstant.PALLET));
                    htsinoUp.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
                    htsinoUp.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM)); 
                    htsinoUp.put("sinoln",mapLn.get("sinoln")); 
                    htsinoUp.put("fulltray",map.get("FULLTRAY")); 
                    String extCond1="";
                    flag=dao.update(sql1.toString(),htsinoUp,extCond1);
               
                  }
       trqty = trqty-Lnqty;
       }
    
          }
          }
        }else
        {
           throw new Exception("Details not found for Delivery No : " + map.get(MDbConstant.TRAVELER_ID) );
        }
      }
      catch (Exception e)
      {
         MLogger.exception(this,e);
         throw e;
      }
    return flag;
 }*/
 
 private boolean processOBTravelDet(Map map) throws Exception
  {
      Hashtable ht=new Hashtable();
      OBTravelerDetDAO dao = new OBTravelerDetDAO(); 
      boolean flag=false;
      boolean isDataPresent=false;

      try
      {
        ht.put(MDbConstant.TRAVELER_ID,map.get(MDbConstant.TRAVELER_ID));
        ht.put("PALLETID",map.get(MDbConstant.PALLET));
        ht.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
        ht.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM));
        ht.put(MDbConstant.MTID,map.get(MDbConstant.MTID));
        ht.put(MDbConstant.TRAYID,map.get(MDbConstant.TRAYID));
        String extCond = "and mtid = '"+map.get(MDbConstant.MTID)+"' ";
        isDataPresent=dao.isExist(ht);
       
        MLogger.info("isExists : " + isDataPresent);
        if(isDataPresent)
        {
          //query 
          String ftray =(String)map.get("FULLTRAY");
          if (ftray.equalsIgnoreCase("1"))
          {
          StringBuffer sql=new StringBuffer(" SET ");
          sql.append(" TrayLableQty = " +   map.get(MDbConstant.RECV_QTY) + " ");
          sql.append(",TrayStatus  = '" +   map.get(MDbConstant.TrayDetStatus) + "'");
          sql.append(",PalletStatus  = 'C'");
          //Added by samatha on Feb02 2010 to update lotbarcode value for lot
          sql.append(",LOT_BARCODE  =  '" +   map.get("LOTBARCODEVAL") + "'");
          sql.append(",UPBY  = '" +   map.get(MDbConstant.LOGIN_USER) + "'");
          flag=dao.update(sql.toString(),ht,extCond);

          MLogger.info("update : " + flag);
          }else
          {
              Hashtable htQuery = new Hashtable();
              htQuery.put("PALLETID",map.get(MDbConstant.PALLET));
              htQuery.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
              htQuery.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM));
              htQuery.put(MDbConstant.TRAYID,map.get(MDbConstant.TRAYID));
              // Added by Samatha on Aug 26 2010 to add in the Condition with Mtid to avioding update the tray status to all other same item and lot
              htQuery.put(MDbConstant.MTID,map.get(MDbConstant.MTID));
              int trqty =new Integer((String)map.get(MDbConstant.RECV_QTY)).intValue();
              
             ArrayList al = new PickingUtil().get_sku_line_Details( (String)map.get(MDbConstant.TRAVELER_ID),htQuery);
             if (al.size() > 0) {
                 for (int i = 0; i < al.size(); i++) {
                  Map mapLn = (Map) al.get(i);
                  int Lnqty =new Integer((String)mapLn.get("qty")).intValue();
                  
                  if (trqty >=Lnqty){
                   MLogger.log("Count Entered"+i);
                    StringBuffer sql1=new StringBuffer(" SET ");
                    sql1.append(" TrayLableQty = " +  Lnqty + " ");
                    sql1.append(",TrayStatus  = '" +   map.get(MDbConstant.TrayDetStatus) + "'");
                    //Added by samatha on Feb02 2010 to update lotbarcode value for lot
                    sql1.append(",LOT_BARCODE  =  '" +   map.get("LOTBARCODEVAL") + "'");
                    sql1.append(",PalletStatus  = 'C'");
                    sql1.append(",UPBY  = '" +   map.get(MDbConstant.LOGIN_USER) + "'");
                   
                    Hashtable htsinoUp = new Hashtable();
                    htsinoUp.put("Traveler_Id",map.get(MDbConstant.TRAVELER_ID));
                    htsinoUp.put("PALLETID",map.get(MDbConstant.PALLET));
                    htsinoUp.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
                    htsinoUp.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM)); 
                    // Added by Samatha on Aug 26 2010 to add in the Condition with Mtid to avioding update the tray status to all other same item and lot
                    htsinoUp.put(MDbConstant.MTID,map.get(MDbConstant.MTID));
                    htsinoUp.put("sinoln",mapLn.get("sinoln")); 
                    htsinoUp.put("fulltray",map.get("FULLTRAY")); 
                    
                    String extCond1="";
                    flag=dao.update(sql1.toString(),htsinoUp,extCond1);
                  }else
                  {
                    StringBuffer sql1=new StringBuffer(" SET ");
                    sql1.append(" TrayLableQty = " +  trqty + " ");
                    sql1.append(",TrayStatus  = '" +   map.get(MDbConstant.TrayDetStatus) + "'");
                      //Added by samatha on Feb02 2010 to update lotbarcode value for lot
                    sql1.append(",LOT_BARCODE  =  '" +   map.get("LOTBARCODEVAL") + "'");
                    sql1.append(",PalletStatus  = 'C'");
                    sql1.append(",UPBY  = '" +   map.get(MDbConstant.LOGIN_USER) + "'");
                 
                  
                    Hashtable htsinoUp = new Hashtable();
                    htsinoUp.put("Traveler_Id",map.get(MDbConstant.TRAVELER_ID));
                    htsinoUp.put("PALLETID",map.get(MDbConstant.PALLET));
                    htsinoUp.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
                    htsinoUp.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM)); 
                     // Added by Samatha on Aug 26 2010 to add in the Condition with Mtid to avioding update the tray status to all other same item and lot
                    htsinoUp.put(MDbConstant.MTID,map.get(MDbConstant.MTID));
                    htsinoUp.put("sinoln",mapLn.get("sinoln")); 
                    htsinoUp.put("fulltray",map.get("FULLTRAY")); 
                    String extCond1="";
                    flag=dao.update(sql1.toString(),htsinoUp,extCond1);
               
                  }
       trqty = trqty-Lnqty;
       }
    
          }
          }
        }else
        {
           throw new Exception("Details not found for Delivery No : " + map.get(MDbConstant.TRAVELER_ID) );
        }
      }
      catch (Exception e)
      {
         MLogger.exception(this,e);
         throw e;
      }
    return flag;
 }
 
 

 
  private boolean processMoveHis(Map map) throws Exception
  {
    boolean flag =false;
    MovHisDAO movHisDao=new MovHisDAO();
    try
    {
        Hashtable htMovHis = new Hashtable();
      
        htMovHis.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
        htMovHis.put(MDbConstant.TRAVELER_NUM,map.get(MDbConstant.TRAVELER_ID));
        htMovHis.put(MDbConstant.PALLET,map.get(MDbConstant.PALLET));
        htMovHis.put(MDbConstant.MTID,map.get(MDbConstant.MTID));
        htMovHis.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
        htMovHis.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM));
        htMovHis.put(MDbConstant.LOGIN_USER,map.get(MDbConstant.LOGIN_USER));
        htMovHis.put(MDbConstant.MOVEHIS_CR_DATE,map.get(MDbConstant.MOVEHIS_CR_DATE));
        htMovHis.put(MDbConstant.CRTIME,map.get(MDbConstant.CRTIME));
        htMovHis.put(MDbConstant.MOVHIS_REF_ID,"TRAY_LABEL");
        htMovHis.put(MDbConstant.MOVHIS_QTY,map.get(MDbConstant.RECV_QTY));
      
        flag = movHisDao.insertIntoMovHis(htMovHis);
    }
    catch (Exception e)
    {
       MLogger.exception(this,e);
       throw e;
    }
    return flag;
 }


}