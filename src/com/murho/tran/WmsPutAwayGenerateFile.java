package com.murho.tran;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import com.murho.dao.MovHisDAO;
import com.murho.dao.RecvDetDAO;
import com.murho.dao.RecvHdrDAO;
import com.murho.utils.CibaConstants;
import com.murho.utils.DateUtils;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;

public class WmsPutAwayGenerateFile implements WmsTran
{
 DateUtils dateUtils=null;
 StrUtils strUtils =null;
  public static String DB_PROPS_FILE = "C:/props/CibaVision/config/CibaVisionConstants.properties";
  public WmsPutAwayGenerateFile()
  {
   dateUtils=new DateUtils() ;
   strUtils = new StrUtils();
  }
  public boolean processWmsTran(Map map) throws Exception
  {
    MLogger.info("###############  WMS TRANSACTION : PUT AWAY ###############");
    boolean tranFlag=false;
      try {
      
          MLogger.info("1.process generate txt File - XXXXXXXX Stage : 1");
          tranFlag=processGenTxtFile(map);
          MLogger.info("processGenTxtFile : " + tranFlag);
          
       if(tranFlag)
       {
         MLogger.info("1.process RecvHdr - XXXXXXXX Stage : 1");
          tranFlag=processRecvHdr(map);
          MLogger.info("processRecvHdr : " + tranFlag);
       }
          if(tranFlag)
        {
          MLogger.info("1.process MovHIs - XXXXXXXX Stage : 2");
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
  
  
   private boolean processGenTxtFile(Map map) throws Exception
  {
 //   MLogger.log(0, this.getClass() + " processGenTxtFile()");
    boolean flag =false;
    RecvDetDAO recvDAO = new RecvDetDAO();
    Properties dbpr;
    InputStream dbip;
    ArrayList invQryList1  = new ArrayList();
    try{
     dbip = new FileInputStream(new File(DB_PROPS_FILE));
      dbpr = new Properties();
      dbpr.load(dbip);
      String  PutAwayFilePath   = dbpr.getProperty("PutAwayFilePath");
          int lTrType =3;
          int TranID=10;
          int Date=6;
          int time =6;
          int Orno = 7;
          int LineNo =3;
          int PartNo =15;
          int Carton =3;
          int iQty =10;
          int Company =3;
          int Warehouse=3;
          int Location=7;
          int BalType=1;
          int UoM=2;
          int LotNo =12; 
          int ExpDate=8;
          int LotNo1 =20;
          String Traveler =(String)map.get("TRAVELER");
          String plant =(String)map.get("PLANT");
          String filesstatus =(String)map.get("filesstatus");
          String PONUM =(String)map.get("PONUM");
          if(Traveler.length() > 0)
          {
          Traveler=Traveler.substring(0,Traveler.length()-1);
          Traveler=strUtils.getStringSeparatedByQuots(Traveler);
          }
          invQryList1 = recvDAO.getPutAwayDetailsForExcel(plant,Traveler,filesstatus);
          String sitem ="";
           int cntSlno =0;
           FileWriter fstream = new FileWriter(PutAwayFilePath);
           BufferedWriter bout = new BufferedWriter(fstream);
           for (int i =0; i<invQryList1.size(); i++){
           Map lineArr = (Map) invQryList1.get(i);
          String sno ="";
             String tranId = (String)lineArr.get("CRBY");
             String date = (String)lineArr.get("CRAT");
 
             date = new DateUtils().getDateFormatYYMMDD();
             String stime =DateUtils.Time();
             stime =new DateUtils().getTimeinhhmmss(stime);
               String sku = (String)lineArr.get("SKU");
               String lot = (String)lineArr.get("LOT");
               if(sitem.equalsIgnoreCase(sku)){
                sno = new Integer(cntSlno).toString();  
                
               }else
               {
               cntSlno = cntSlno+1;
                  sno = new Integer(cntSlno).toString();  
               }
               
               String loc = (String)lineArr.get("LOC1");
                System.out.println("loc"+loc);
               String qty = (String)lineArr.get("PUTAWAYQTY");
               System.out.println("qty"+qty);
               String expdate = (String)lineArr.get("EXPIREDATE");
               
               expdate = new DateUtils().getDateinmmddyy(expdate);
               
             
              System.out.println("expdate"+expdate);
            
               String uom = strUtils.fString((String)lineArr.get("UOM"));
                  bout.write( strUtils.rightPad("R70"," ",lTrType) +strUtils.rightPad(tranId," ",TranID) +strUtils.rightPad(date," ",Date) 
                  +strUtils.rightPad(stime," ",time) +strUtils.rightPad(PONUM," ",Orno) +strUtils.leftPad(sno," ",LineNo) 
                  +strUtils.rightPad(sku," ",PartNo)   +strUtils.rightPad("001"," ",Carton)+ strUtils.leftPad(qty," ",iQty)
                  +strUtils.rightPad("110"," ",Company) +strUtils.rightPad("368"," ",Warehouse) + strUtils.rightPad(loc," ",Location)+strUtils.rightPad("D"," ",BalType) 
                  + strUtils.rightPad(uom," ",UoM) + strUtils.rightPad(lot," ",LotNo)+ strUtils.rightPad(expdate," ",ExpDate)+ strUtils.rightPad(lot," ",LotNo1) );
                  bout.newLine();
         sitem =sku;
          //Close the output stream
        
          }
          bout.close();
          flag = true;
    }
      catch (Exception e)
      {
         MLogger.exception(this,e);
         flag = false;
         throw e;
      }

    return flag;
 }
 
 private boolean processRecvHdr(Map map) throws Exception
  {
      Hashtable ht=new Hashtable();
      RecvHdrDAO _RecvhdrDAO=new RecvHdrDAO();
      boolean flag=false;
    
      try
      {
        String Traveler =(String)map.get("TRAVELER");
         if(Traveler.length() > 0)
          {
          Traveler=Traveler.substring(0,Traveler.length()-1);
          Traveler=strUtils.getStringSeparatedByQuots(Traveler);
          }
          flag =_RecvhdrDAO.UpdateRecvHdr(Traveler);
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
        String Traveler =(String)map.get("TRAVELER");
        if(Traveler.length() > 0)
        {
          Traveler=Traveler.substring(0,Traveler.length()-1);
        }
      
        htRecvHis.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
        htRecvHis.put(MDbConstant.TRAVELER_NUM,Traveler);
        htRecvHis.put("PONO",map.get("PONUM"));
        htRecvHis.put(MDbConstant.LOGIN_USER,map.get(MDbConstant.LOGIN_USER));
        htRecvHis.put(MDbConstant.MOVHIS_REF_ID,"PUTAWAY_GENERATE");   
        htRecvHis.put("CRAT",dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate())); 
        htRecvHis.put("CRTIME",DateUtils.Time()); 
      
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