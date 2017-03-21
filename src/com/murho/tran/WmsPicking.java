package com.murho.tran;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.InvMstDAO;
import com.murho.dao.LocMstDAO;
import com.murho.dao.MovHisDAO;
import com.murho.dao.OBTravelerDetDAO;
import com.murho.dao.TblControlDAO;
import com.murho.db.utils.ItemMstUtil;
import com.murho.db.utils.PickingUtil;
import com.murho.utils.CibaConstants;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;

public class WmsPicking implements WmsTran
{
  public WmsPicking()
  {
  }
  public boolean processWmsTran(Map map) throws Exception
  {
    MLogger.info("###############  WMS TRANSACTION : PICKING ###############");
    boolean tranFlag=false;
      try {
            MLogger.info("1.process OB_TRAVEL_DET - XXXXXXXX Stage : 1");
            tranFlag=processOBTravelDet(map);
            MLogger.info("processOBTravelDet : " + tranFlag);
    
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
  
  
  private  boolean processOBTravelDet(Map map) throws Exception
  {
      Hashtable ht=new Hashtable();
      OBTravelerDetDAO _obtravelDetDAO=new OBTravelerDetDAO();
      boolean flag=false;
      boolean isDataPresent=false;

      try
      {
    
   
        ht.put("Traveler_Id",map.get(MDbConstant.TRAVELER_NUM));
        ht.put(MDbConstant.PALLET,map.get(MDbConstant.PALLET));
        
        ht.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
        ht.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM));
        ht.put(MDbConstant.LOC,map.get(MDbConstant.LOC));
        ht.put("sino",map.get("sino"));
        ht.put("fulltray",map.get("FULL_TRAY"));
  
        isDataPresent=_obtravelDetDAO.isExist(ht);
       
        MLogger.info("isExists : " + isDataPresent);
        if(isDataPresent)
        {
          //query
          
          StringBuffer sql=new StringBuffer(" SET ");
          String ftray =(String)map.get("FULL_TRAY");
          if (ftray.equalsIgnoreCase("1"))
          {
          
             String Sinoln = new PickingUtil().Load_Line_Details_For_fulltray ((String)map.get(MDbConstant.TRAVELER_NUM),(String)map.get(MDbConstant.PALLET),(String)map.get(MDbConstant.LOT_NUM),(String)map.get(MDbConstant.ITEM),(String)map.get(MDbConstant.LOC),(String)map.get("sino"));           
            
             sql.append("MTID = '" +   map.get(MDbConstant.MTID) + "' ");
             sql.append(", PickQty =  " +   map.get("PickQty") + " ");
             sql.append(", Status = 'C' ");
             sql.append(", crtime = '"+map.get(MDbConstant.CRTIME)+"' ");
             sql.append(", crat = '"+map.get(MDbConstant.MOVEHIS_CR_DATE)+"' ");
             sql.append(", crby = '"+map.get(MDbConstant.LOGIN_USER)+"' ");
           
             String extCond=" and sinoln = '"+Sinoln+"'";
             flag=_obtravelDetDAO.update(sql.toString(),ht,extCond);
           
          }else
          {
              
              Hashtable htQuery = new Hashtable();
              htQuery.put("Traveler_Id",map.get(MDbConstant.TRAVELER_NUM));
              htQuery.put(MDbConstant.PALLET,map.get(MDbConstant.PALLET));
              htQuery.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
              htQuery.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM));
              htQuery.put(MDbConstant.LOC,map.get(MDbConstant.LOC));
             
              htQuery.put("sino",map.get("sino")); 
              htQuery.put("fulltray",map.get("FULL_TRAY")); 
              int Pkqty =new Integer((String)map.get("PickQty")).intValue();
                  int inQty  =new Integer((String)map.get("INVQTY")).intValue();
                  int pQty  =new Integer((String)map.get("PickQty")).intValue();
                        
              
             ArrayList al = new PickingUtil().get_sino_line_Details( (String)map.get(MDbConstant.TRAVELER_NUM),htQuery);
            
             if (al.size() > 1) {
                 for (int i = 0; i < al.size(); i++) {
                  Map mapLn = (Map) al.get(i);
                  int Lnqty =new Integer((String)mapLn.get("qty")).intValue();
                   int pkQty  =new Integer((String)mapLn.get("PickQty")).intValue();
                   String strMtid = new PickingUtil().getMtid((String)map.get(MDbConstant.TRAVELER_NUM),(String)map.get(MDbConstant.PALLET),(String)map.get(MDbConstant.LOT_NUM),(String)map.get(MDbConstant.ITEM),(String)map.get(MDbConstant.LOC),(String)map.get("sino"));           
                   Lnqty= Lnqty-pkQty;
                   int ordQty = new Integer((String)mapLn.get("qty")).intValue();
                   int picktotQty = 0;
                   if ((Pkqty >=Lnqty) && (Pkqty >0)  ){                   
                      StringBuffer sql1=new StringBuffer(" SET ");
                      sql1.append(" PickQty = PickQty + "+Lnqty);
                      sql1.append(", crtime = '"+map.get(MDbConstant.CRTIME)+"' ");
                      sql1.append(", crat = '"+map.get(MDbConstant.MOVEHIS_CR_DATE)+"' ");
                      sql1.append(", crby = '"+map.get(MDbConstant.LOGIN_USER)+"' ");
                          if(strMtid.length() == 0)
                          {
                            if (inQty == pQty){
                              sql1.append(" , MTID = '" +  map.get(MDbConstant.MTID) + "' ");
                             }else if   ( pQty < inQty ){
                              sql1.append(" , MTID = '" +   map.get("sino") + "' ");
                           }
                          }else
                          {
                            sql1.append(" , MTID = '" + strMtid + "' ");
                          }
                          
                          picktotQty = Lnqty + pkQty;
                        
                          if ( ordQty == picktotQty )
                          {
                              
                             sql1.append(" , STATUS = 'C' ");
                          }else
                          {
                           // System.out.println("....sdffffffffffffff.........1");
                            sql1.append(" , STATUS = 'N' ");
                          }
                   Hashtable htsinoUp = new Hashtable();
                    htsinoUp.put("Traveler_Id",map.get(MDbConstant.TRAVELER_NUM));
                    htsinoUp.put(MDbConstant.PALLET,map.get(MDbConstant.PALLET));
                    htsinoUp.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
                    htsinoUp.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM));
                    htsinoUp.put(MDbConstant.LOC,map.get(MDbConstant.LOC));
                    htsinoUp.put("sino",map.get("sino")); 
                    htsinoUp.put("sinoln",mapLn.get("sinoln")); 
                    htsinoUp.put("fulltray",map.get("FULL_TRAY")); 
                    String extCond="";
                         
                   flag=_obtravelDetDAO.update(sql1.toString(),htsinoUp,extCond);
                   
                    
              
            
                        Pkqty = Pkqty-Lnqty;
                   
                   
                  }else if ((Pkqty >0) )
                  {
                  
                    int pkQty1  =new Integer((String)mapLn.get("PickQty")).intValue();
                    StringBuffer sql1=new StringBuffer(" SET ");
                     Lnqty= Lnqty-pkQty1;
                  if (Pkqty >=Lnqty)
                  {
                   sql1.append(" PickQty =   PickQty + "+ Lnqty); 
                   picktotQty = Lnqty + pkQty1;
                  }else{
                   sql1.append(" PickQty =   PickQty + "+ Pkqty);
                   picktotQty = Pkqty + pkQty1;
                  }
                    sql1.append(", crtime = '"+map.get(MDbConstant.CRTIME)+"' ");
                    sql1.append(", crat = '"+map.get(MDbConstant.MOVEHIS_CR_DATE)+"' ");
                    sql1.append(", crby = '"+map.get(MDbConstant.LOGIN_USER)+"' ");
                      if(strMtid.length() == 0)
                          {
                              if (inQty == pQty){
                              sql1.append(" , MTID = '" +  map.get(MDbConstant.MTID) + "' ");
                             }else if   ( pQty < inQty ){
                              sql1.append(", MTID = '" +   map.get("sino") + "' ");
                           }
                          }else
                          {
                            sql1.append(" , MTID = '" + strMtid + "' ");
                          }
                           
                       if ( ordQty == picktotQty)
                          {
                              
                             sql1.append(" , STATUS = 'C' ");
                          }else
                          {
                           // System.out.println("...sdfffffffffff..........2");
                            sql1.append(" , STATUS = 'N' ");
                          }
                   String extCond="";
                   Hashtable htsinoUp = new Hashtable();
                    htsinoUp.put("Traveler_Id",map.get(MDbConstant.TRAVELER_NUM));
                    htsinoUp.put(MDbConstant.PALLET,map.get(MDbConstant.PALLET));
                    htsinoUp.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
                    htsinoUp.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM));
                    htsinoUp.put(MDbConstant.LOC,map.get(MDbConstant.LOC));
                    htsinoUp.put("sino",map.get("sino")); 
                    htsinoUp.put("sinoln",mapLn.get("sinoln")); 
                    htsinoUp.put("fulltray",map.get("FULL_TRAY")); 
                   
                   flag=_obtravelDetDAO.update(sql1.toString(),htsinoUp,extCond);
                   Pkqty = Pkqty - Lnqty;
                  }
                 }
             }else if(al.size() == 1)
                  {
                    
                    
                  for (int i1 = 0; i1 < al.size(); i1++) {
                  Map mapLn1 = (Map) al.get(i1);
                  int Lnqty1 =new Integer((String)mapLn1.get("qty")).intValue();
                  int pkQty2  =new Integer((String)mapLn1.get("PickQty")).intValue();
                  String smtid = (String)mapLn1.get("mtid");
                  pkQty2= Pkqty + pkQty2;
                  StringBuffer sql1=new StringBuffer(" SET ");
                  sql1.append(" PickQty =  " + pkQty2);
                  sql1.append(", crtime = '"+map.get(MDbConstant.CRTIME)+"' ");
                  sql1.append(", crat = '"+map.get(MDbConstant.MOVEHIS_CR_DATE)+"' ");
                  sql1.append(", crby = '"+map.get(MDbConstant.LOGIN_USER)+"' ");
                  if(smtid.length() == 0)
                     {
                            if (inQty == pQty){
                              sql1.append(" , MTID = '" +  map.get(MDbConstant.MTID) + "' ");
                             }else if   ( pQty < inQty ){
                              sql1.append(", MTID = '" +   map.get("sino") + "' ");
                           }
                      }
                      else
                       {
                             sql1.append(" , MTID = '" + smtid + "' ");
                       }
                      if ( Lnqty1 == pkQty2 ||  pkQty2 > Lnqty1)
                          {
                              
                             sql1.append(" , STATUS = 'C' ");
                          }else
                          {
                           
                            sql1.append(" , STATUS = 'N' ");
                          }
                    Hashtable htsinoUp = new Hashtable();
                    htsinoUp.put("Traveler_Id",map.get(MDbConstant.TRAVELER_NUM));
                    htsinoUp.put(MDbConstant.PALLET,map.get(MDbConstant.PALLET));
                    htsinoUp.put(MDbConstant.ITEM,map.get(MDbConstant.ITEM));
                    htsinoUp.put(MDbConstant.LOT_NUM,map.get(MDbConstant.LOT_NUM));
                    htsinoUp.put(MDbConstant.LOC,map.get(MDbConstant.LOC));
                    htsinoUp.put("sino",map.get("sino")); 
                    htsinoUp.put("sinoln",mapLn1.get("sinoln")); 
                    htsinoUp.put("fulltray",map.get("FULL_TRAY")); 
                    String extCond="";
                   flag=_obtravelDetDAO.update(sql1.toString(),htsinoUp,extCond);
                  
                    }   
                  }    
         
        }
        }
        else
        {
           throw new Exception("Details not found for Serial No : " + map.get("sino") );
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
 //   MLogger.log(0, this.getClass() + " processInvMst()");

      Hashtable ht=new Hashtable();
      InvMstDAO _InvMstDAO=new InvMstDAO();
      boolean flag=false;
      boolean isDataPresent=false;
      try
      {
       //CibaConstants.cibacompanyName;
        //ht.put(MDbConstant.PLANT,CibaConstants.cibacompanyName); 
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
         
         StringBuffer sql=new StringBuffer(" SET ");
        
          sql.append(MDbConstant.QTY + " = " + MDbConstant.QTY +
                     " -  " + map.get("PickQty") + " ");

          String extCond= " and" + " " +  MDbConstant.QTY +" >= " + (String) map.get("PickQty");
          flag=_InvMstDAO.update(sql.toString(),ht,extCond);
          MLogger.info("update : " + flag);
          
           if(flag)
          {
             /* By samatha for URS -A-2
              * To Calculate space for Location While Picking
              * on Jan 05 2009
              */
             isDataPresent=_InvMstDAO.isExisit(ht," AND CAST(ISNULL(QTY,0) AS INTEGER) >0 ");
             if(!isDataPresent){
              flag=processLocationSpace(map,"increase");
              MLogger.info("processLocationSpace : increase " + flag);
             }
          }
          
        }else
        {
            throw new Exception("MTID : " + map.get(MDbConstant.MTID) + " in Picking");
           
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
    TblControlDAO tblConDao=new TblControlDAO();

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
        htRecvHis.put(MDbConstant.LOC,map.get(MDbConstant.LOC));
        htRecvHis.put(MDbConstant.LOGIN_USER,map.get(MDbConstant.LOGIN_USER));
        htRecvHis.put(MDbConstant.MOVEHIS_CR_DATE,map.get(MDbConstant.MOVEHIS_CR_DATE));
        htRecvHis.put(MDbConstant.CRTIME,map.get(MDbConstant.CRTIME));
        htRecvHis.put(MDbConstant.MOVHIS_REF_ID,"PICKING");
        htRecvHis.put(MDbConstant.MOVHIS_QTY,map.get("PickQty"));
      
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