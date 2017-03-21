package com.murho.tran;
import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.InvMstDAO;
import com.murho.dao.MovHisDAO;
import com.murho.dao.RecvDetDAO;
import com.murho.utils.CibaConstants;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;

public class WmsReceiving implements WmsTran
{
  public WmsReceiving()
  {
  }
  public boolean processWmsTran(Map map) throws Exception
  {
    MLogger.info("###############  WMS TRANSACTION : RECEIVING ###############");
    boolean tranFlag=false;
      try {
        MLogger.info("1.process RecvHdr - XXXXXXXX Stage : 1");
        tranFlag=processRecvDet(map);
        MLogger.info("processRecvHdr : " + tranFlag);

        if(tranFlag)
        {
          MLogger.info("1.process InvMst - XXXXXXXX Stage : 2");
          tranFlag=processInvMst(map);
          MLogger.info("processInvMst : " + tranFlag);
        }
        if(tranFlag)
        {
          MLogger.info("1.process InvMst - XXXXXXXX Stage : 3");
          tranFlag=processMoveHis(map);
          MLogger.info("processInvMst : " + tranFlag);
        }
        
      }
      catch (Exception ex) {
      tranFlag = false;
      throw ex;
    }
    return tranFlag;
  }
  
  // 13/11/08 Commented the Query to Update Order Qty for the Bux fix reported 
  private boolean processRecvDet(Map map) throws Exception
  {
 
      Hashtable ht=new Hashtable();
      RecvDetDAO _RecvDetDAO=new RecvDetDAO();
      boolean flag=false;
      boolean isDataPresent=false;
      try
      {
    
        ht.put(MDbConstant.TRAVELER_NUM,map.get(MDbConstant.TRAVELER_NUM));
        ht.put(MDbConstant.PALLET,map.get(MDbConstant.PALLET));
        ht.put(MDbConstant.MTID,map.get(MDbConstant.MTID));
        ht.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
        ht.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM));
       
        isDataPresent=_RecvDetDAO.isExists(ht);
       
        MLogger.info("isExists : " + isDataPresent);
        if(isDataPresent)
        {
          //query
          StringBuffer sql=new StringBuffer(" SET ");
          sql.append(MDbConstant.RECV_QTY + " = " +   map.get(MDbConstant.RECV_QTY) + " ");
         // sql.append(","+MDbConstant.ORD_QTY + " = '" +   map.get(MDbConstant.RECV_QTY) + "'");
          sql.append(","+MDbConstant.ReceiveStatus + " = '" +   map.get(MDbConstant.ReceiveStatus) + "'");
          String extCond="";
          flag=_RecvDetDAO.update(sql.toString(),ht,extCond);
          MLogger.info("update : " + flag);
        }else
        {
           throw new Exception("Details not found for Delivery No : " + map.get(MDbConstant.TRAVELER_NUM) );
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

       // ht.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
        ht.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
        ht.put(MDbConstant.MTID,map.get(MDbConstant.MTID));
        ht.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
        ht.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM));
      
        isDataPresent=false;
        isDataPresent=_InvMstDAO.isExists(ht);
        if(isDataPresent)
        {
            throw new Exception("MTID : " + map.get(MDbConstant.MTID) + " Received");
        }else
        {
            ht.put(MDbConstant.QTY,map.get(MDbConstant.RECV_QTY));
            ht.put(MDbConstant.LOC,"Holding Area");  
            ht.put(MDbConstant.MOVEHIS_CR_DATE,map.get(MDbConstant.MOVEHIS_CR_DATE));
            //Modified By Samatha to Include Expiry Date on Jan 18 2010 in Invmst
            ht.put("USERFLD1",map.get("EXPIRYDT"));
//          below line is added on 19-march-2014 for UDI implementation
            ht.put("Product_2D",map.get("PRODUCTTYP"));
            flag= _InvMstDAO.insertIntoInvMst(ht);
            
            MLogger.info("insertIntoInvMst : " + isDataPresent);
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
        htRecvHis.put(MDbConstant.PALLET,map.get(MDbConstant.PALLET));
        htRecvHis.put(MDbConstant.MTID,map.get(MDbConstant.MTID));
        htRecvHis.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
        htRecvHis.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM));
        htRecvHis.put(MDbConstant.LOGIN_USER,map.get(MDbConstant.LOGIN_USER));
        htRecvHis.put(MDbConstant.MOVEHIS_CR_DATE,map.get(MDbConstant.MOVEHIS_CR_DATE));
        htRecvHis.put(MDbConstant.CRTIME,map.get(MDbConstant.CRTIME));
        htRecvHis.put(MDbConstant.MOVHIS_REF_ID,"RECV");
        htRecvHis.put(MDbConstant.MOVHIS_QTY,map.get(MDbConstant.RECV_QTY));
      
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