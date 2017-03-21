package com.murho.tran;

import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.InvMstDAO;
import com.murho.dao.LocMstDAO;
import com.murho.dao.MovHisDAO;
import com.murho.db.utils.ItemMstUtil;
import com.murho.utils.CibaConstants;
import com.murho.utils.DateUtils;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;

public class WmsStockTransfer implements WmsTran
{
 DateUtils dateUtils=null;
  public WmsStockTransfer()
  {
  dateUtils=new DateUtils() ;
  }
  public boolean processWmsTran(Map map) throws Exception
  {
    MLogger.info("###############  WMS TRANSACTION : STOCKTRANSFER ###############");
    boolean tranFlag=false;
      try {
      
          Hashtable ht2=new Hashtable();
          String tType="";
          tType=(String)map.get(MDbConstant.TRANSTYPE);
          if(tType.equalsIgnoreCase("FRM_LOC"))
          {
            MLogger.info("1. before processInvMst_FrmLoc - XXXXXXXX Stage : 1"+tType);
            tranFlag=processInvMst_FrmLoc(map);
          }
          else if(tType.equalsIgnoreCase("TO_LOC"))
          { 
            MLogger.info("1. before processInvMst_ToLoc - XXXXXXXX Stage : 1"+tType);
            tranFlag=processInvMst_ToLoc(map);
          }

          if(tranFlag)
          {
            MLogger.info("1.process processMoveHis - XXXXXXXX Stage : 2");
            tranFlag=processMoveHis(map);
          }
        
      }
      catch (Exception ex) {
      tranFlag = false;
      throw ex;
    }
    return tranFlag;
  }
  
   private boolean processInvMst_FrmLoc(Map map) throws Exception
  {
    MLogger.log(0, this.getClass() + " processInvMst_FrmLoc()");

      Hashtable ht=new Hashtable();
      InvMstDAO _InvMstDAO=new InvMstDAO();
      boolean flag=false;
      boolean isDataPresent=false;
      try
      {

        ht.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
        ht.put(MDbConstant.MTID,map.get(MDbConstant.MTID));
        ht.put(MDbConstant.LOC,map.get(MDbConstant.LOC));
           
        isDataPresent=false;
        isDataPresent=_InvMstDAO.isExists(ht);
      
        MLogger.info("isExists : " + isDataPresent);
        
        if(isDataPresent)
        {  
          //query
          StringBuffer sql=new StringBuffer(" SET ");
          sql.append(MDbConstant.LOC + " = '" +   map.get(MDbConstant.TEMPLOC) + "' ");
          sql.append(","+MDbConstant.QTY + " = '" +   map.get(MDbConstant.QTY) + "'");
          String extCond="";
          flag=_InvMstDAO.updateInv(sql.toString(),ht,extCond);
          MLogger.info("update : " + flag);
          
          if(flag)
          {
           // Changed By Samatha on Jan 05 2009  for calculating the Space for Loc for URS -A-2
           // flag=processLocationSpace(map,"decrease");
            flag=processLocationSpace(map,"increase");
            MLogger.info("processLocationSpace : " + flag);
          }
        }else
        {
           throw new Exception("MTID : " + map.get(MDbConstant.MTID) + " not found ");
        }
      }
      catch (Exception e)
      {
         MLogger.exception(this,e);
         throw e;
      }
    return flag;
 }
 
   private boolean processInvMst_ToLoc(Map map) throws Exception
  {
      Hashtable ht=new Hashtable();
      InvMstDAO _InvMstDAO=new InvMstDAO();
      boolean flag=false;
      boolean isDataPresent=false;
      try
      {

        ht.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
        ht.put(MDbConstant.MTID,map.get(MDbConstant.MTID));
        ht.put(MDbConstant.LOC,"TEMPLOC"); //added by farid to restrict more than one time submition
       
        isDataPresent=false;
        isDataPresent=_InvMstDAO.isExists(ht);
        if(isDataPresent)
        {
          
          //query
          StringBuffer sql=new StringBuffer(" SET ");
          sql.append(MDbConstant.LOC + " = '" +   map.get(MDbConstant.LOC) + "' ");
          sql.append(","+MDbConstant.QTY + " = '" +   map.get(MDbConstant.QTY) + "'");
          String extCond="";
          flag=_InvMstDAO.updateInv(sql.toString(),ht,extCond);
          if(flag)
          {
            // Changed By Samatha on Jan 05 2009  for calculating the Space for Loc for URS -A-2
            // flag=processLocationSpace(map,"increase");
            flag=processLocationSpace(map,"decrease");
            MLogger.info("processLocationSpace : " + flag);
          }
        }else
        {
           throw new Exception("MTID : " + map.get(MDbConstant.MTID) + " not found");
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
    String tType="";
    tType=(String)map.get(MDbConstant.TRANSTYPE);
  
    MovHisDAO movHisDao=new MovHisDAO();
    try
    {
         Hashtable htMovHis = new Hashtable();
         htMovHis.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
         htMovHis.put(MDbConstant.MTID,map.get(MDbConstant.MTID));
         htMovHis.put(MDbConstant.LOC,map.get(MDbConstant.LOC));
         htMovHis.put(MDbConstant.QTY,map.get(MDbConstant.QTY));
         htMovHis.put(MDbConstant.LOGIN_USER,map.get(MDbConstant.LOGIN_USER));
         htMovHis.put("CRAT",dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
         htMovHis.put(MDbConstant.SKU,map.get(MDbConstant.SKU));
         htMovHis.put(MDbConstant.LOT,map.get(MDbConstant.LOT));
         htMovHis.put(MDbConstant.CRTIME,map.get(MDbConstant.CRTIME));
         
         if(tType.equalsIgnoreCase("FRM_LOC"))
          {
           htMovHis.put(MDbConstant.MOVHIS_REF_ID,"TRANSFER-OUT");
          } else
          {
            htMovHis.put(MDbConstant.MOVHIS_REF_ID,"TRANSFER-IN");
          }
        
           flag = movHisDao.insertIntoMovHis(htMovHis);
    }
    catch (Exception e)
    {
       MLogger.exception(this,e);
       throw e;
    }
    return flag;
 }
 
 
 private boolean processLocationSpace (Map map,String type)throws Exception
 {
   boolean flag=false;
   try{      
    String spacePerTray=new ItemMstUtil().getSpcacePerTray((String)map.get(MDbConstant.SKU));
    double tempSpacePerTray=Double.parseDouble(spacePerTray);
    MLogger.info("Space per Tray : " + tempSpacePerTray);
    double totalSpace=tempSpacePerTray;
    MLogger.info("Toatal space to increase/decrease " + totalSpace);
    if(type.equalsIgnoreCase("increase"))
    {
       MLogger.info("1.process LocMst to Reassign "+map.get(MDbConstant.LOC)+" - Increase" );
       Hashtable ht=new Hashtable();
       ht.put(MDbConstant.LOC_ID,map.get(MDbConstant.LOC));
       StringBuffer sb=new StringBuffer(" set SPACE_REM=SPACE_REM+");
       sb.append(totalSpace);
       flag=new LocMstDAO().updateLocId(sb.toString(),ht,"");
     }
     else
     {
        MLogger.info("1.process LocMst to Reassign "+map.get(MDbConstant.LOC_ID1)+" - Decrease" );
        Hashtable ht=new Hashtable();
        ht.put(MDbConstant.LOC_ID,map.get(MDbConstant.LOC));
        StringBuffer sb=new StringBuffer(" set SPACE_REM=SPACE_REM-");
        sb.append(totalSpace);
        flag=new LocMstDAO().updateLocId(sb.toString(),ht,"");
     }
             
     }catch(Exception e)
     {
       throw e;
     }
          
       
   return flag;
 }

}