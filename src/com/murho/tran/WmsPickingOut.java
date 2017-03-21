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

public class WmsPickingOut implements WmsTran
{
  public WmsPickingOut()
  {
  }
  public boolean processWmsTran(Map map) throws Exception
  {
    MLogger.info("###############  WMS TRANSACTION : PICKING OUT ###############");
    boolean tranFlag=false;
      try {
        MLogger.info("1.process Invmst - XXXXXXXX Stage : 1");
        tranFlag=processInvMst(map);
        MLogger.info("processInvMst : " + tranFlag);
      
        if(tranFlag)
        {
          MLogger.info("1.process Movhis - XXXXXXXX Stage : 2");
          tranFlag=processMoveHis(map);
          MLogger.info("processMoveHis : " + tranFlag);
        }
        
      }
      catch (Exception ex) {
      tranFlag = false;
      throw ex;
    }
    return tranFlag;
  }
  

 
  private boolean processInvMst(Map map) throws Exception
  {
 
      Hashtable ht=new Hashtable();
      InvMstDAO _InvMstDAO=new InvMstDAO();
      boolean flag=false;
      boolean isDataPresent=false;
      try
      {

        ht.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
        ht.put(MDbConstant.MTID,map.get(MDbConstant.MTID));
        ht.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
        ht.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM));
        ht.put(MDbConstant.INV_LOC,map.get(MDbConstant.LOC));
      
        isDataPresent=false;
        isDataPresent=_InvMstDAO.isExists(ht);
      
        MLogger.info("isExists : " + isDataPresent);
        
        if(isDataPresent)
        {
        
          //query
          StringBuffer sql=new StringBuffer(" SET ");
        
          sql.append(MDbConstant.QTY + " = " + MDbConstant.QTY +
                     " -  " + map.get(MDbConstant.QTY) + " ");

          String extCond= " and " +  MDbConstant.QTY +" >= " + (String) map.get(MDbConstant.QTY);
          flag=_InvMstDAO.update(sql.toString(),ht,extCond);
          MLogger.info("update : " + flag);
           if(flag)
          {
            /* By samatha for URS -A-2
              * To Calculate space for Location While Picking Out
              * on Jan 05 2009
              */
             isDataPresent=_InvMstDAO.isExisit(ht," AND CAST(ISNULL(QTY,0) AS INTEGER) >0 ");
             if(!isDataPresent){
            flag=processLocationSpace(map,"increase");
            MLogger.info("processLocationSpace : increase " + flag);
             }
          }
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
        Hashtable htRecvHis = new Hashtable();
      
        htRecvHis.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
        htRecvHis.put(MDbConstant.MTID,map.get(MDbConstant.MTID));
        htRecvHis.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
        htRecvHis.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM));
        htRecvHis.put(MDbConstant.LOGIN_USER,map.get(MDbConstant.LOGIN_USER));
        htRecvHis.put(MDbConstant.LOC,map.get(MDbConstant.LOC));
        htRecvHis.put("CRAT",DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
        htRecvHis.put(MDbConstant.MOVHIS_REF_ID,"STK_ADJ_OUT");
        htRecvHis.put(MDbConstant.MOVHIS_QTY,"-"+map.get(MDbConstant.QTY));
        htRecvHis.put("USERFLD5",map.get("USERFLD5"));
        htRecvHis.put("USERFLD6",map.get("USERFLD6"));
        htRecvHis.put("CRTIME",map.get(MDbConstant.CRTIME));
      
        flag = movHisDao.insertIntoMovHis(htRecvHis);
        MLogger.log(0, "insertIntoMovHis Transaction ::  " + flag);


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
   MLogger.info("1.pprocessLocationSpace()");
   try{      
    String spacePerTray=new ItemMstUtil().getSpcacePerTray((String)map.get(MDbConstant.ITEM));
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