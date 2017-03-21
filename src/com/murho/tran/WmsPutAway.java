package com.murho.tran;

import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.InvMstDAO;
import com.murho.dao.LocMstDAO;
import com.murho.dao.MovHisDAO;
import com.murho.dao.RecvDetDAO;
import com.murho.db.utils.ItemMstUtil;
import com.murho.utils.CibaConstants;
import com.murho.utils.DateUtils;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;

public class WmsPutAway implements WmsTran
{
 DateUtils dateUtils=null;
  public WmsPutAway()
  {
   dateUtils=new DateUtils() ;
  }
  public boolean processWmsTran(Map map) throws Exception
  {
    MLogger.info("###############  WMS TRANSACTION : PUT AWAY ###############");
    boolean tranFlag=false;
      try {
      
          tranFlag=processRecvDet(map);
          tranFlag=processInvMst(map);
          MLogger.info("processInvMst : " + tranFlag);
          
          if(tranFlag && ((String)(map.get("LocReAssign"))).equalsIgnoreCase("true"))
          {
             String spacePerTray=new ItemMstUtil().getSpcacePerTray((String)map.get(MDbConstant.SKU));
             
             double tempSpacePerTray=Double.parseDouble(spacePerTray);
          
             long tempQty=Long.parseLong((String)map.get(MDbConstant.RECV_QTY));
             double totalSpace=tempSpacePerTray;

             Hashtable ht=new Hashtable();
             String frmLoc =(String)map.get(MDbConstant.LOC_ID);
             if (frmLoc.length()>0){
             ht.put(MDbConstant.LOC_ID,map.get(MDbConstant.LOC_ID));
             StringBuffer sb=new StringBuffer(" set SPACE_REM=SPACE_REM+");
             sb.append(totalSpace);
             tranFlag=new LocMstDAO().updateLocId(sb.toString(),ht,"");
             }
             Hashtable ht1=new Hashtable();
             ht1.put(MDbConstant.LOC_ID,map.get("toLocation"));
             StringBuffer sb1=new StringBuffer(" set SPACE_REM=SPACE_REM-");
             sb1.append(totalSpace);
             tranFlag=new LocMstDAO().updateLocId(sb1.toString(),ht1,"");
          }
       
        if(tranFlag)
        {
          tranFlag=processMoveHis(map);      
        }
        
      }
      catch (Exception ex) {
      tranFlag = false;
      throw ex;
    }
    return tranFlag;
  }
  
 
 
  private boolean processRecvDet(Map map) throws Exception
  {
      Hashtable ht=new Hashtable();
      RecvDetDAO _RecvDetDAO=new RecvDetDAO();
      boolean flag=false;
      boolean isDataPresent=false;
      try
      {
    
        ht.put(MDbConstant.MTID,map.get(MDbConstant.MTID));
        ht.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
        ht.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM));
       
        isDataPresent=_RecvDetDAO.isExists(ht);
        if(isDataPresent)
        {
          //query
          StringBuffer sql=new StringBuffer(" SET ");
          sql.append(MDbConstant.PUTAWAY_QTY + " = " +   map.get(MDbConstant.RECV_QTY) + " ");
          sql.append(","+MDbConstant.PutAwayStatus + " = '" +   "C" + "'");
          sql.append(","+"LOC1" + " = '" +   map.get(MDbConstant.LOC) + "'");       
          sql.append(","+"CRTIME" + " = '" +   map.get(MDbConstant.CRTIME) + "'");
          sql.append(","+"CRAT" + " = '" +   map.get(MDbConstant.CREATED_AT) + "'");
          sql.append(","+"CRBY" + " = '" +   map.get(MDbConstant.LOGIN_USER) + "'");
          
          String extCond="";
          flag=_RecvDetDAO.update(sql.toString(),ht,extCond);
        }else
        {
           throw new Exception("Details not found for MTID : " + map.get(MDbConstant.MTID) );
        }
      }
      catch (Exception e)
      {
         MLogger.exception(this,e);
         throw e;
      }

    return flag;
 }
  private boolean processInvMst(Map map) throws Exception
  {

      Hashtable ht=new Hashtable();
      InvMstDAO _InvMstDAO=new InvMstDAO();
      boolean flag=false;
      boolean isDataPresent=false;
      try
      {
        //ht.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
        ht.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
        ht.put(MDbConstant.MTID,map.get(MDbConstant.MTID));
        //AFTER RECEIVING COMES TO HOLDING AREA
        ht.put(MDbConstant.LOC,"HOLDING AREA");   
        isDataPresent=false;
        isDataPresent=_InvMstDAO.isExists(ht);
      
        MLogger.info("isExists : " + isDataPresent);
        
        if(isDataPresent)
        {
          StringBuffer sql=new StringBuffer(" SET ");
          sql.append(MDbConstant.LOC + " = '" +   map.get(MDbConstant.LOC) + "' ");
         
          String extCond="";
          flag=_InvMstDAO.updateInv(sql.toString(),ht,extCond);
          MLogger.info("update : " + flag);
         }else
        {
            throw new Exception("MTID : " + map.get(MDbConstant.MTID) + " not in HOLDING AREA");
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
        htRecvHis.put(MDbConstant.TRAVELER_NUM,map.get(MDbConstant.TRAVELER_NUM));
        htRecvHis.put(MDbConstant.PALLET,"");       
        htRecvHis.put(MDbConstant.MTID,map.get(MDbConstant.MTID));
        htRecvHis.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
        htRecvHis.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM));
        htRecvHis.put(MDbConstant.LOC,map.get(MDbConstant.LOC));
        htRecvHis.put(MDbConstant.LOGIN_USER,map.get(MDbConstant.LOGIN_USER));
        htRecvHis.put(MDbConstant.MOVHIS_REF_ID,"PUTAWAY");
        htRecvHis.put(MDbConstant.MOVHIS_QTY,map.get(MDbConstant.RECV_QTY));
        htRecvHis.put("CRAT",dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
        htRecvHis.put(MDbConstant.CRTIME,map.get(MDbConstant.CRTIME)); 
      
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



}