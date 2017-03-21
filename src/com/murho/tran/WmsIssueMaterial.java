package com.murho.tran;
import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.InvMstDAO;
import com.murho.dao.MovHisDAO;
import com.murho.dao.TblControlDAO;
import com.murho.utils.Generator;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
//implements WmsTran

public class WmsIssueMaterial  {
  public WmsIssueMaterial() {
  }


  /* 
  public boolean processWmsTran(Map map)
 {
 MLogger.log(0, "############################  WMS TRANSACTION : ISSUEMATERIAL NEW ###################################################");
    MLogger.log(1, this.getClass() + " ProcessReceiving()");
    boolean flag;
    String xmlStr = "";
    try {
      MLogger.log(0, "1.Insert/update InvMst   - XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX Stage : 1");
      flag=false;
      flag=processInvMstTran(map);
      MLogger.log(0, "processInvMstTran() : Transaction : " + flag);
      MLogger.log(0, "2.Insert/update WipMst - XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX Stage : 2");
      if(flag)
      {
      flag=false;
      flag=processWipMstTran(map);
      MLogger.log(0, "processWipMstTran() : Transaction : " + flag);
      }

       MLogger.log(0, "3.Insert movHis - XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX Stage : 3");
      if(flag)
      {
        flag=processMoveHis(map);
         MLogger.log(0, "processMoveHis() : Transaction : " + flag);
        
      }
   }
    catch(Exception ex)
    {
      MLogger.exception(this,ex);
      flag = false;
    }
    MLogger.log(-1, this.getClass() + " processIssue()");
    return flag;
 }

 


private boolean processInvMstTran(Map map)
  {
    MLogger.log(0, this.getClass() + " processInvMstTran()");

      Hashtable ht=new Hashtable();
      InvMstDAO invMstDAO=new InvMstDAO();
      boolean flag=false;
      boolean isDataPresent=false;
      try
      {
        ht.put(MDbConstant.COMPANY,map.get(SConstant.COMPANY));
        ht.put(MDbConstant.PLANT,map.get(SConstant.PLANT));
        ht.put(MDbConstant.MATERIAL_CODE,map.get(SConstant.MATERIAL_CODE));
        ht.put(MDbConstant.WHSE_ID,map.get(SConstant.FR_WHSE));
        ht.put(MDbConstant.BIN_NO,map.get(SConstant.FR_BIN));
        ht.put(MDbConstant.LOT_NUM,StrUtils.fString((String)map.get(SConstant.LOT_NUM)));

        isDataPresent=invMstDAO.isExisit(ht);

        if(isDataPresent)
        {
          MLogger.log(0, "Data Present in invmst need to update");
          //query
           StringBuffer sql=new StringBuffer(" SET ");
          sql.append(MDbConstant.TRAN_QTY + " = " + MDbConstant.TRAN_QTY + " -  " +   map.get(SConstant.TRAN_QTY) + " ");

           String extCond= " and " +  MDbConstant.TRAN_QTY +" >= " + (String) map.get(SConstant.TRAN_QTY);
          //flag=invMstDAO.updateInvQty(map);
          flag=invMstDAO.updateInv(sql.toString(),ht,extCond);
        }else
        {

         Hashtable ht_batMst=new Hashtable();

         ht_batMst.put(MDbConstant.MATERIAL_CODE,map.get(SConstant.MATERIAL_CODE));
         ht_batMst.put("PLANT",map.get(SConstant.COMPANY));
         ht_batMst.put(MDbConstant.LOT_NUM,StrUtils.fString((String)map.get(SConstant.LOT_NUM)));

         BatMstDAO _BatMstDAO = new BatMstDAO();

           MLogger.log(0, " **** ITEM NOT PRESENT  *** ");
           MLogger.log(0, " **** BATCH ='RECV_LOT'  *** ");

           ht.put(MDbConstant.LOT_NUM,"RECV_LOT");
             isDataPresent=_BatMstDAO.isExisit(ht_batMst);
         //  isDataPresent=invMstDAO.isExisit(ht);
            if(isDataPresent)
            {
                StringBuffer sql=new StringBuffer(" SET ");
                sql.append(MDbConstant.TRAN_QTY + " = " + MDbConstant.TRAN_QTY + " -  " +   map.get(SConstant.TRAN_QTY) + " ");

                String extCond= " and " +  MDbConstant.TRAN_QTY +" >= " + (String) map.get(SConstant.TRAN_QTY);
                flag=invMstDAO.updateInv(sql.toString(),ht,extCond);
            }
           MLogger.log(0, " **** For IssueMaterial data should not be inserted into invmst *** ");
          //flag=invMstDAO.insertData(map);
          return flag;
        }
      }
      catch (Exception e)
      {
         MLogger.exception(this,e);
      }

    MLogger.log(0, this.getClass() + " processInvMstTran()");
    return flag;
 }

 private boolean processWipMstTran(Map map)
 {
    MLogger.log(0, this.getClass() + " processWipMstTran()");

      Hashtable ht=new Hashtable();
      WipMstDAO wipMstDAO=new WipMstDAO();
      boolean flag=false;
      boolean isDataPresent=false;
      try
      {
        ht.put(MDbConstant.COMPANY,map.get(SConstant.COMPANY));
        ht.put(MDbConstant.PLANT,map.get(SConstant.PLANT));
        ht.put(MDbConstant.MATERIAL_CODE,map.get(SConstant.MATERIAL_CODE));
        ht.put(MDbConstant.WHSE_ID,map.get(SConstant.TO_WHSE));
        ht.put(MDbConstant.BIN_NO,map.get(SConstant.TO_BIN));
        ht.put(MDbConstant.LOT_NUM,StrUtils.fString((String)map.get(SConstant.LOT_NUM)));
        ht.put(MDbConstant.WIPMST_OP_SEQ,map.get(SConstant.OP_SEQ));
        ht.put(MDbConstant.WIPMST_OP_CODE,StrUtils.fString((String)map.get(SConstant.OP_CODE)));


        isDataPresent=wipMstDAO.isExisit(ht);

        if(isDataPresent)
        {
          MLogger.log(0, "Data Present in invmst need to update");

          StringBuffer sql = new StringBuffer(" ");

          sql.append(" SET ");
          sql.append(MDbConstant.TRAN_QTY + "=" + MDbConstant.TRAN_QTY + " + " + (String) map.get(SConstant.TRAN_QTY) );

          String extCondi="";

          flag=wipMstDAO.update(sql.toString(),ht,"");
        }else
        {
          ht.put(MDbConstant.TRAN_QTY,map.get(SConstant.TRAN_QTY));
          ht.put(MDbConstant.UOM,map.get(SConstant.UOM));
          ht.put("CRAT",Generator.getDateTime());
          ht.put(MDbConstant.LOGIN_USER,map.get(SConstant.LOGIN_USER));
          flag=wipMstDAO.insert(ht);
        }
      }
      catch (Exception e)
      {
        MLogger.exception(this,e);
      }

    MLogger.log(0, this.getClass() + " processWipMstTran()");
    return flag;
 }

  private boolean processMoveHis(Map map)
  {
    MLogger.log(1, this.getClass() + " processWipMstTran()");
    boolean flag =false;
    TblControlDAO tblConDao=new TblControlDAO();

    MovHisDAO movHisDao=new MovHisDAO();
    try
    {
       MLogger.log(0, "Getting next seq no .");
       String nextSeqNo= tblConDao.getNextSeqNo("RECVID");
       MLogger.log(0, "Next Seq no = " + nextSeqNo);


      Hashtable htRecvHis = new Hashtable();
      htRecvHis.clear();
      htRecvHis.put(MDbConstant.COMPANY,map.get(SConstant.COMPANY));
      htRecvHis.put("RECID",nextSeqNo);
      htRecvHis.put("DIRTYPE","ISS_MAT");
      htRecvHis.put(MDbConstant.MATERIAL_CODE,map.get(SConstant.MATERIAL_CODE));
      htRecvHis.put("MOVTID",map.get(SConstant.JOB_NUM));
      htRecvHis.put("PONO",map.get(SConstant.OP_SEQ));
      htRecvHis.put("LOC",map.get(SConstant.PLANT));
      htRecvHis.put("QTY",map.get(SConstant.TRAN_QTY));
      htRecvHis.put("BATNO",map.get(SConstant.LOT_NUM));
      htRecvHis.put(MDbConstant.MOVEHIS_CR_DATE,map.get(SConstant.TRAN_DATE));
      htRecvHis.put(MDbConstant.MOVEHIS_CR_TIME,map.get(SConstant.SYS_TIME));
      htRecvHis.put("WHID",map.get(SConstant.TO_WHSE));
      htRecvHis.put("DEFBIN",map.get(SConstant.TO_BIN));
      htRecvHis.put(MDbConstant.LOGIN_USER,map.get(SConstant.LOGIN_USER));

      flag = movHisDao.insertIntoMovHis(htRecvHis);

      MLogger.log(0, "insertIntoMovHis Transaction ::  " + flag);


    }
    catch (Exception e)
    {
       MLogger.exception(this,e);
    }
    MLogger.log(-1, this.getClass() + " processWipMstTran()");
    return flag;
 }*/


}//end of class