package com.murho.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.DO.RecvDet_DO;
import com.murho.DO.TransactionDTO;
import com.murho.gates.DbBean;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;


public class RecvDetDAO  extends BaseDAO {
  public RecvDetDAO() {
  }

  public static final String TABLE_NAME = "RecvDet";

  
   public Map selectRow(String query,Hashtable ht) throws Exception {
   MLogger.log(1, this.getClass() + " select()");
   Map map=new HashMap();

   java.sql.Connection con=null;

   // connection
   try{
   con=com.murho.gates.DbBean.getConnection();
   StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + TABLE_NAME);
   sql.append(" WHERE ");
   String conditon=formCondition(ht);
   
   sql.append(conditon);
   
   MLogger.query(query.toString());

   map = getRowOfData(con,sql.toString());
   
  // System.out.println("Map........."+map.size());
    }catch(Exception e)
    {
          MLogger.log(0,"######################### Exception :: select() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: select() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
    }
    finally{
      if (con != null) {
           DbBean.closeConnection(con);
      }
    }

    MLogger.log( -1, this.getClass() + " select()");
    return map;
 }
  
  
  
  
  public boolean update(String query, Hashtable map, String extCond) throws
      Exception {
    MLogger.log(1, this.getClass() + " update()");
    boolean flag = false;
    java.sql.Connection con = null;
    try {
      // connection
      con = com.murho.gates.DbBean.getConnection();

      StringBuffer sql = new StringBuffer(" UPDATE " + TABLE_NAME);
      sql.append(" ");
      sql.append(query);
      sql.append(" WHERE ");

      String conditon = formCondition(map);
     
      sql.append(conditon);

      if (extCond.length() != 0) {
     
        sql.append(extCond);
      }

      MLogger.query(" "+sql.toString());

      flag = updateData(con, sql.toString());
    }
    catch (Exception e) {
      MLogger.log(0,
          "######################### Exception :: update() : ######################### \n");
      MLogger.log(0, "" + e.getMessage());
      MLogger.log(0,
          "######################### Exception :: update() : ######################### \n");
      MLogger.log( -1, "");
      throw e;
    }
    finally {
      if (con != null) {
        DbBean.closeConnection(con);
      }
    } 

    MLogger.log( -1, this.getClass() + " update()");

    return flag;

  }

  public boolean isExists(Hashtable ht) throws Exception {
    MLogger.log(1, this.getClass() + " isExisit()");
    boolean flag = false;
    java.sql.Connection con = null;

    try {
      // connection
      con = com.murho.gates.DbBean.getConnection();
      //query
      StringBuffer sql = new StringBuffer(" SELECT ");
      sql.append("COUNT(*) ");
      sql.append(" ");
      sql.append(" FROM " + TABLE_NAME);
      sql.append(" WHERE  " + formCondition(ht));
     
      MLogger.query(" "+sql.toString());
     
      flag = isExists(con, sql.toString());

    }
    catch (Exception e) {
      MLogger.log(0, "######################### Exception :: isExisit() : ######################### \n");
      MLogger.log(0, "" + e.getMessage());
      MLogger.log(0, "######################### Exception :: isExisit() : ######################### \n");
      MLogger.log( -1, "");
      throw e;
    }
    finally {
      if (con != null) {
        DbBean.closeConnection(con);
      }
    } 
    MLogger.log( -1, this.getClass() + " isExisit()");
    return flag;

  }


  public ArrayList getPallet(String Traveler) throws Exception {
   MLogger.log(1, this.getClass() + " getPallet()");
   ArrayList al=new ArrayList();
// connection
   java.sql.Connection con=null;
   try
   {
    con=com.murho.gates.DbBean.getConnection();
   //query
   StringBuffer sql = new StringBuffer(" SELECT distinct ");
   sql.append(" Pallet ");
   sql.append(" ");
   sql.append(" FROM " + TABLE_NAME );
   sql.append(" WHERE  Traveler = '" + Traveler + "' ");
   sql.append(" order by pallet asc ");

   MLogger.query(sql.toString());
       
   al=selectData(con,sql.toString());
   
   }
   catch(Exception e) {
          MLogger.exception(this,e);
          throw e;
   }
   finally{
        if (con != null) {
          DbBean.closeConnection(con);
        }
   }
   MLogger.log( -1, this.getClass() + " getPallet()");
   return al;

 }
 
 public ArrayList getMTIDDeatails(String Traveler,String Pallet,String MTID) throws Exception {
   MLogger.log(1, this.getClass() + " getPallet()");
   ArrayList al=new ArrayList();
// connection
   java.sql.Connection con=null;
   try
   {
    con=com.murho.gates.DbBean.getConnection();
   //query
   StringBuffer sql = new StringBuffer(" SELECT ");
   sql.append(" SKU, ");
   sql.append(" LOT, ");
   sql.append(" QTY");
   sql.append(" ");
   sql.append(" FROM " + TABLE_NAME );
   sql.append(" WHERE  Traveler = '" + Traveler + "'");
   sql.append(" and  Pallet = '" + Pallet + "'");
   sql.append(" and  MTID = '" + MTID + "' ");


   MLogger.query(sql.toString());
       
   al=selectData(con,sql.toString());
   
   }
   catch(Exception e) {
           MLogger.exception(this,e);
           throw e;
        }
        finally{
        if (con != null) {
          DbBean.closeConnection(con);
        }
   }
   MLogger.log( -1, this.getClass() + " getPallet()");
   return al;

 }
 
  
     public ArrayList getLotDetailByMTID(String aPlant,String traveler,String lot) {
    MLogger.log(1, this.getClass() + " getLotDetailByMTID()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "travelID" + traveler);
     
      StringBuffer sql=new StringBuffer(" SELECT MTID,LOT,ORDQTY,RECVQTY,RECEIVESTATUS,REMARK FROM RECVDET ");
      
      sql.append("  WHERE TRAVELER='"+traveler+"' AND LOT='"+lot+"' ");
    
      
      MLogger.log(0,"getTravellerHdrList(aQuery)::"+sql.toString());
      arrList = invdao.selectForReport(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getTravellerHdrList :: getTravellerHdrList:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getTravellerHdrList()");
    return arrList;
  } 
  
   public String getItemDesc(String aPlant,String aItem) throws Exception
   {
   String itemDesc="";
   
   Hashtable ht=new Hashtable();
   ht.put("MTID",aItem);
     
   String query=" distinct [description] "; 
   
   Map m=selectRow(query,ht);
   
   itemDesc=  (String) m.get("description");
  
   if(itemDesc.equalsIgnoreCase(null) || itemDesc.length()==0)
   {
     MLogger.log(0, "Item Desc is Empty");
     itemDesc="";
   }
   MLogger.log(0, "Item Desc = " + itemDesc);

   return itemDesc;
  }

 public ArrayList selectFor(String query,Hashtable ht) throws Exception {
       MLogger.log(1, this.getClass() + " selectForReport()");
       boolean flag = false;
       ArrayList al= new ArrayList();
       // connection
       java.sql.Connection con=null;
       try{
       con=com.murho.gates.DbBean.getConnection();
       StringBuffer sql = new StringBuffer(  query );
       String conditon="";
       if(ht.size()>0)
       {
          sql.append(" AND ");
          conditon=formConditionLike(ht);
          sql.append( " " + conditon);
       }

         MLogger.query(" "+sql.toString());
          al=selectData(con,sql.toString());
       }
       catch(Exception e)
        {
          MLogger.log(0,"######################### Exception :: selectForReport() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: selectForReport() : ######################### \n");
          MLogger.log(-1, "");
          throw e;
        }
    finally{
      if (con != null) {
      DbBean.closeConnection(con);
      }
    }//
  MLogger.log(-1, this.getClass() + " selectForReport()");
  return al;
  }
  
   /* public ArrayList getPutawayDetailByLOT(String aPlant,String traveler,String lot) {
      ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "travelID" + traveler);
     
      StringBuffer sql=new StringBuffer(" SELECT MTID,LOT,PUTAWAYQTY,isnull(LOC1,'') as LOC1,PUTAWAYSTATUS FROM RECVDET ");
      
      sql.append("  WHERE TRAVELER='"+traveler+"' AND LOT='"+lot+"' ");
    
      
      MLogger.log(0,"getTravellerHdrList(aQuery)::"+sql.toString());
      arrList = invdao.selectForReport(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getPutawayDetailByLOT :: getPutawayDetailByLOT:" + e.toString());
      MLogger.log( -1, "");
    }
   
    return arrList;
  }*/
  
  public ArrayList getPutawayDetailByLOT(String aPlant,String traveler,String lot,String PALLET,String LOC,String putAwaystatus) {
      ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "travelID" + traveler);
     
      StringBuffer sql=new StringBuffer(" SELECT MTID,LOT,PUTAWAYQTY,isnull(LOC1,'') as LOC1,PUTAWAYSTATUS FROM RECVDET ");
      
      //added by hemant for HPQC #29836
      if(putAwaystatus.equals("N")){
    	  LOC = "";
      }
      
      sql.append("  WHERE TRAVELER='"+traveler+"' AND LOT='"+lot+"' AND PALLET='"+PALLET+"' AND isnull(LOC1,'')='"+LOC+"' ");
    
      
      MLogger.log(0,"getTravellerHdrList(aQuery)::"+sql.toString());
      arrList = invdao.selectForReport(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getPutawayDetailByLOT :: getPutawayDetailByLOT:" + e.toString());
      MLogger.log( -1, "");
    }
   
    return arrList;
  }
  
  
     public ArrayList getPutAwayDetailsForExcel(String aPlant,String travelId) {
      MLogger.log(1, this.getClass() + " getPutAwayDetailsForExcel()");
      ArrayList arrList = new ArrayList();
      try {
        InvMstDAO invdao = new InvMstDAO();
        Hashtable ht = new Hashtable();
        
        StringBuffer sql = new StringBuffer(" SELECT TRAVELER,MTID,PALLET,SKU,LOT,ORDQTY, ");  
        sql.append( " PUTAWAYQTY,LOC1,PUTAWAYSTATUS " );
        sql.append(" FROM RECVDET WHERE TRAVELER='"+travelId+"'");
         
        MLogger.log(0,"getPutAwayDetailsForExcel(aQuery)::"+sql);
        
        arrList = invdao.selectForReport(sql.toString(),ht);
     }
     catch (Exception e) {
      MLogger.log("Exception :getPutAwayDetailsForExcel :: getPutAwayDetailsForExcel:" + e.toString());
      MLogger.log( -1, "");
     }
      return arrList;
    }
  
  
     public ArrayList getPutAwayList(String aPlant,String travelId) {
      MLogger.log(1, this.getClass() + " getPutAwayList()");
      ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "travelID" + travelId);
      
      StringBuffer sql = new StringBuffer(" select a.traveler as traveler,a.receivestatus as receivestatus,a.filegenerated as filegenerated, ");
      
      sql.append(" a.putawaystatus as putawaystatus,b.lot as lot,b.mtid as mtid from recvhdr a,recvdet b where a.traveler=b.traveler ");
     
      sql.append(" and a.traveler='"+travelId+"' ");
       
      MLogger.log(0,"getPutAwayList(aQuery)::"+sql);
      
      arrList = invdao.selectForReport(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getPutAwayDetails :: getPutAwayDetails:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getPutAwayDetails()");
    return arrList;
  }
  
   public boolean deleteItemFrmInvmst(java.util.Hashtable ht) throws Exception
 {
    MLogger.log(1, this.getClass() + " deleteItemFrmInvmst()");
    boolean deleteItemMst = false;
   java.sql.Connection con=null;
   try{
   con=DbBean.getConnection();

   //query
   StringBuffer sql = new StringBuffer(" DELETE ");
   sql.append(" ");
   sql.append(" FROM " + TABLE_NAME );
   sql.append(" WHERE " + formCondition(ht) );

    MLogger.query(sql.toString());
    deleteItemMst=updateData(con,sql.toString());
   }
   catch(Exception e) {
          MLogger.log(0,"######################### Exception :: deleteItemFrmInvmst() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: deleteItemFrmInvmst() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
    }
    finally{
        if (con != null) {
            DbBean.closeConnection(con);
    }
   }
    MLogger.log(-1, this.getClass() + " deleteItemFrmInvmst()");

    return deleteItemMst;
  }
  
  public ArrayList get_Qc_ReportDetails(String aPlant,String travelId) {
    MLogger.log(1, this.getClass() + " get_Qc_ReportDetails()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "travelID" + travelId);
      
      StringBuffer sql = new StringBuffer("SELECT ");
      sql.append("  trayid,sku,sku_desc,lot,expdate,custpo,isnull(traylableqty,0) as qty,pallet,palletid,traveler_id as traveler,sino,substring(expdate,1,2) as mm ,substring(expdate,9,2) as yy,isnull(upby,'') as upby ");
      sql.append("FROM ob_travel_det ");
      sql.append(" WHERE TRAVELER_ID='"+travelId+"' order by cast(pallet as integer),trayid ,sku ");
       
      MLogger.log(0,"getReceiveDetails(aQuery)::"+sql);
      
      arrList = invdao.selectForReport(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :get_Qc_ReportDetails :: get_Qc_ReportDetails:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " get_Qc_ReportDetails()");
    return arrList;
  }
  
 public ArrayList getPutAwayDetailsForExcel(String aPlant,String travelId,String filestatus) {
    MLogger.log(1, this.getClass() + " getPutAwayDetailsForExcel()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "travelID" + travelId);
      
    /*  StringBuffer sql = new StringBuffer("SELECT  ");
      sql.append("   distincT  a.SKU,a.LOT,sum(ORDQTY)AS ORDQTY,sum(PUTAWAYQTY) AS PUTAWAYQTY ,LOC1,[EXPIREDATE], ");
      sql.append(" (select isnull(stkuom,'') from itemmst where item =a.sku) as UOM,MAX(isnull(a.CRBY,''))as CRBY,MAX(isnull(a.CRAT,'')) as CRAT,MAX(isnull(a.CRTIME,'')) as CRTIME   ");
      sql.append(" FROM RECVDET a  WHERE a.TRAVELER IN ("+travelId+") GROUP BY  a.SKU,a.LOT,LOC1,[EXPIREDATE] ORDER BY SKU asc,LOT ASC ");
       */
      
      // Modified the Expiry Date to CreatedOn as Expiry Date By Samatha on JAN 28 2010 
        StringBuffer sql = new StringBuffer("SELECT  ");
      sql.append("   distincT  a.SKU,a.LOT,sum(ORDQTY)AS ORDQTY,sum(PUTAWAYQTY) AS PUTAWAYQTY ,LOC1,CREATEDON AS [EXPIREDATE], ");
      sql.append(" (select isnull(stkuom,'') from itemmst where item =a.sku) as UOM,MAX(isnull(a.CRBY,''))as CRBY,MAX(isnull(a.CRAT,'')) as CRAT,MAX(isnull(a.CRTIME,'')) as CRTIME   ");
      sql.append(" FROM RECVDET a  WHERE a.TRAVELER IN ("+travelId+") GROUP BY  a.SKU,a.LOT,LOC1,CREATEDON ORDER BY SKU asc,LOT ASC ");
      
      
      MLogger.log(0,"getReceiveDetails(aQuery)::"+sql);
      
      arrList = invdao.selectForReport(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getPutAwayDetailsForExcel :: getPutAwayDetailsForExcel:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getPutAwayDetailsForExcel()");
    return arrList;
  }
  
  
 public ArrayList getReceiveDetails(String aPlant,String travelId) {
    MLogger.log(1, this.getClass() + " getReceiveDetails()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "travelID" + travelId);
      
   
      
      StringBuffer sql = new StringBuffer("SELECT MAX(TRAVELER) AS TRAVELER,PALLET AS PALLET,");
      sql.append(" MAX(SKU)  AS SKU,LOT,SUM(ORDQTY) AS ORDQTY ,SUM(RECVQTY) AS RECVQTY ");
      sql.append(",MAX(RECEIVESTATUS) AS RECEIVESTATUS,MAX(ISNULL(LOC,'')) AS LOC  FROM RECVDET ");
      sql.append(" WHERE TRAVELER='"+travelId+"' GROUP BY pallet,LOT Order  by cast(pallet as integer)  ");
      MLogger.log(0,"getReceiveDetails(aQuery)::"+sql);
      
      arrList = invdao.selectForReport(sql.toString(),ht);
       
    }
    catch (Exception e) {
      MLogger.log("Exception :getTravellerSummary :: getTravellerSummary:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getTravellerSummary()");
    return arrList;
  }
  
  
  public ArrayList getReceiveDetailsById(ArrayList al) {
    MLogger.log(1, this.getClass() + " getReceiveDetailsById()");
    ArrayList arrList = new ArrayList();
    try {
         // ut = com.murho.gates.DbBean.getUserTranaction();
      
          SQLServerDAOFactory dao_factory= (SQLServerDAOFactory)DAOFactory.getDAOFactory(1);
        
      //dao - hdr,det
        
        SQLRecvDet_DAO _SQLRecvDet = new SQLRecvDet_DAO();
     
        //do - hdr,det
        RecvDet_DO  _RecvDet_DO = new RecvDet_DO();
       
        TransactionDTO  _TransactionDTO= new TransactionDTO();
        for(int i=0;i<al.size();i++)
          {
            _TransactionDTO= (TransactionDTO)al.get(i);
             _RecvDet_DO.setPlant(_TransactionDTO.getPlant());
             _RecvDet_DO.setTraveler(_TransactionDTO.getTraveler());
             _RecvDet_DO.setFilename(_TransactionDTO.getFilename());
          
             
              arrList=_SQLRecvDet.getRecvDetById(_RecvDet_DO);
          }
        
      // System.out.println("Plant+TravelerDAO........"+_RecvDet_DO.getPlant()+","+_RecvDet_DO.getTraveler());
      
    }
    catch (Exception e) {
      MLogger.log("Exception :getTravellerSummaryById :: getTravellerSummaryById:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getTravellerSummaryById()");
    return arrList;
  }
  
  public ArrayList getReceiveDetails1() {
    MLogger.log(1, this.getClass() + " getReceiveDetails()");
    ArrayList arrList = new ArrayList();
    try {
         // ut = com.murho.gates.DbBean.getUserTranaction();
      
        SQLServerDAOFactory dao_factory= (SQLServerDAOFactory)DAOFactory.getDAOFactory(1);
        SQLRecvDet_DAO _SQLRecvDet = new SQLRecvDet_DAO();
        RecvDet_DO  _RecvDet_DO = new RecvDet_DO();
        arrList=_SQLRecvDet.getRecvDet();
     
    }
    catch (Exception e) {
      MLogger.log("Exception :getTravellerSummary :: getTravellerSummary:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getTravellerSummary()");
    return arrList;
  }
  
  
  
   public ArrayList getReceiveTravelerDetailsById(ArrayList al) {
    MLogger.log(1, this.getClass() + " getReceiveTravelerDetailsById()");
    ArrayList arrList = new ArrayList();
    try {
       
        SQLServerDAOFactory dao_factory= (SQLServerDAOFactory)DAOFactory.getDAOFactory(1);
      
        SQLRecvDet_DAO _SQLRecvDet = new SQLRecvDet_DAO();
     
       
        RecvDet_DO  _RecvDet_DO = new RecvDet_DO();
       
        TransactionDTO  _TransactionDTO= new TransactionDTO();
        for(int i=0;i<al.size();i++)
          {
            _TransactionDTO= (TransactionDTO)al.get(i);
             _RecvDet_DO.setPlant(_TransactionDTO.getPlant());
             _RecvDet_DO.setTraveler(_TransactionDTO.getTraveler());
           
            arrList=_SQLRecvDet.getRecvTravelerDetById(_RecvDet_DO);
          }
        
    }
    catch (Exception e) {
      MLogger.log("Exception :getReceiveTravelerDetailsById :: getReceiveTravelerDetailsById:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + "getReceiveTravelerDetailsById()");
    return arrList;
  }
  public ArrayList getReceiveTravelerDetails() {
    MLogger.log(1, this.getClass() + " getReceiveTravelerDetails()");
    ArrayList arrList = new ArrayList();
    try {
         // ut = com.murho.gates.DbBean.getUserTranaction();
      
        SQLServerDAOFactory dao_factory= (SQLServerDAOFactory)DAOFactory.getDAOFactory(1);
        SQLRecvDet_DAO _SQLRecvDet = new SQLRecvDet_DAO();
        RecvDet_DO  _RecvDet_DO = new RecvDet_DO();
        arrList=_SQLRecvDet.getRecvTravelerDet();
     
    }
    catch (Exception e) {
      MLogger.log("Exception :getReceiveTravelerDetails :: getReceiveTravelerDetails:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + "getReceiveTravelerDetails()");
    return arrList;
  }
  
  
  public ArrayList getRecevingApprovalDetails(String aPlant,String travelId) {
    MLogger.log(1, this.getClass() + " getReceiveDetails()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "travelID" + travelId);
      
      
      //Command on 20100222 change Loc1 to Loc
     /* StringBuffer sql = new StringBuffer("SELECT MAX(TRAVELER)AS TRAVELER,MAX(PALLET) AS PALLET,");
      sql.append(" MAX(SKU) AS SKU,LOT,SUM(ORDQTY) AS ORDQTY ,SUM(RECVQTY) AS RECVQTY ");
      sql.append(",MAX(RECEIVESTATUS) AS RECEIVESTATUS,MAX(ISNULL(LOC1,'')) AS LOC FROM RECVDET ");
      sql.append(" WHERE TRAVELER='"+travelId+"'  GROUP BY LOT ");*/
      
      
      StringBuffer sql = new StringBuffer("SELECT MAX(TRAVELER)AS TRAVELER,MAX(PALLET) AS PALLET,");
      sql.append(" MAX(SKU) AS SKU,LOT,SUM(ORDQTY) AS ORDQTY ,SUM(RECVQTY) AS RECVQTY ");
      sql.append(",MAX(RECEIVESTATUS) AS RECEIVESTATUS,MAX(ISNULL(LOC,'')) AS LOC FROM RECVDET ");
      sql.append(" WHERE TRAVELER='"+travelId+"'  GROUP BY LOT ");
       
      MLogger.log(0,"getReceiveDetails(aQuery)::"+sql);
      
      arrList = invdao.selectForReport(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getTravellerSummary :: getTravellerSummary:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getTravellerSummary()");
    return arrList;
  }
  
    public ArrayList getTravellerSummary(String aPlant,String travelId) {
    MLogger.log(1, this.getClass() + " getTravellerSummary()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      String aQuery = "  select traveler,pallet,mtid,sku,description,lot,qty,isnull(loc,''),isnull(receivestatus,'') from  recvdet where traveler = '"+travelId+"'";
      
      MLogger.log(0,"getTravellerSummary(aQuery)::"+aQuery);
      arrList = invdao.selectForReport(aQuery,ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getTravellerSummary :: getTravellerSummary:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getTravellerSummary()");
    return arrList;
  }
  
    public ArrayList getTravellerSummaryWithOutId() {
    MLogger.log(1, this.getClass() + " getTravellerSummary()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      String aQuery = "  select traveler,pallet,mtid,sku,description,lot,Ordqty,isnull(loc,''),isnull(receivestatus,'') from  recvdet Order by traveler";
      
      MLogger.log(0,"getTravellerSummary(aQuery)::"+aQuery);
      arrList = invdao.selectForReport(aQuery,ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getTravellerSummary :: getTravellerSummary:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getTravellerSummary()");
    return arrList;
  }
  
 
     public ArrayList getPutAwayDetails(String aPlant,String travelId) {
      MLogger.log(1, this.getClass() + " getPutAwayDetails()");
      ArrayList arrList = new ArrayList();
      try {
        InvMstDAO invdao = new InvMstDAO();
        Hashtable ht = new Hashtable();
        MLogger.log(0, "travelID" + travelId);
        
        /*StringBuffer sql = new StringBuffer("SELECT MAX(TRAVELER)AS TRAVELER,MAX(PALLET) AS PALLET,");
        
        sql.append(" MAX(SKU) AS SKU,LOT,SUM(ORDQTY) AS ORDQTY ,SUM(PUTAWAYQTY) AS PUTAWAYQTY ");
        
        sql.append(",MAX(PUTAWAYSTATUS) AS PUTAWAYSTATUS,MAX(isnull(LOC1,'')) AS LOC1 FROM RECVDET ");
        
        sql.append(" WHERE TRAVELER='"+travelId+"' GROUP BY LOT");*/
        
        /*  original query

        StringBuffer sql = new StringBuffer("SELECT TRAVELER AS TRAVELER,PALLET AS PALLET,");
        
        sql.append(" SKU AS SKU,LOT,SUM(ORDQTY) AS ORDQTY ,SUM(PUTAWAYQTY) AS PUTAWAYQTY ");
        
        sql.append(",MAX(PUTAWAYSTATUS) AS PUTAWAYSTATUS,isnull(LOC1,'') AS LOC1 FROM RECVDET ");
        
        sql.append(" WHERE TRAVELER='"+travelId+"' AND PUTAWAYSTATUS='C' GROUP BY TRAVELER,PALLET,SKU,LOT,LOC1");
        
         arn */
         
        
        // Change for #980 -- putaway summary display with actaul order qty and actaul putaway qty  -- query with defect
        /*StringBuffer sql = new StringBuffer("SELECT b.TRAVELER AS TRAVELER,b.PALLET AS PALLET,");
        
        sql.append(" b.SKU AS SKU,b.LOT,(SELECT SUM(a.ORDQTY) FROM RECVDET a WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT and (a.LOC1 = b.LOC1 or a.LOC1 is null)) AS ORDQTY ,SUM(b.PUTAWAYQTY) AS PUTAWAYQTY ");
        
        sql.append(",isnull(b.LOC1,'') AS LOC1,  ");
        
        sql.append("PUTAWAYSTATUS = CASE WHEN (SELECT SUM(a.ORDQTY) FROM RECVDET a WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT and (a.LOC1 = b.LOC1 or a.LOC1 is null)) <> sum(b.PUTAWAYQTY) THEN  'N'");
        sql.append(" WHEN (SELECT SUM(a.ORDQTY) FROM RECVDET a WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT and (a.LOC1 = b.LOC1 or a.LOC1 is null)) = sum(b.PUTAWAYQTY) THEN  'C'	END ");
        
        sql.append(" FROM RECVDET b WHERE b.TRAVELER='"+travelId+"' GROUP BY b.TRAVELER,b.PALLET,b.SKU,b.LOT,b.LOC,b.LOC1,b.PUTAWAYSTATUS");
        sql.append(" HAVING b.LOC1 IS NOT NULL");*/
        
     // defect resolved
        // modified by Arun for #980 fix
        
        /* StringBuffer sql = new StringBuffer("SELECT b.TRAVELER AS TRAVELER,b.PALLET AS PALLET, b.SKU AS SKU,b.LOT, ");
         sql.append("(SELECT SUM(a.ordqty) FROM RECVDET a WHERE a.TRAVELER='"+travelId+"' and "); 
         sql.append("a.LOT=b.LOT and (a.LOC1 = b.LOC1  or (a.loc1 is null and a.loc=b.loc1))) AS ORDQTY, ");
         sql.append("SUM(b.PUTAWAYQTY) AS PUTAWAYQTY ,isnull(b.LOC1,'') AS LOC1, ");  
         sql.append("PUTAWAYSTATUS = CASE 	WHEN (SELECT SUM(a.ordqty) "); 
         sql.append("FROM RECVDET a WHERE a.TRAVELER='"+travelId+"' "); 
         sql.append("and a.LOT=b.LOT and (a.LOC1 = b.LOC1  or (a.loc1 is null and a.loc=b.loc1))) <> sum(b.PUTAWAYQTY) THEN  'N' ");
         sql.append("WHEN (SELECT SUM(a.ordqty) "); 
         sql.append("FROM RECVDET a WHERE a.TRAVELER='"+travelId+"' "); 
    		sql.append("and a.LOT=b.LOT and (a.LOC1 = b.LOC1  or (a.loc1 is null and a.loc=b.loc1))) = sum(b.PUTAWAYQTY) THEN  'C'	END "); 
         sql.append("FROM RECVDET b WHERE b.TRAVELER='"+travelId+"' "); 
         sql.append("GROUP BY b.TRAVELER,b.PALLET,b.SKU,b.LOT,b.loc,b.LOC1,b.PUTAWAYSTATUS ");
         sql.append("HAVING b.LOC1 IS NOT NULL ");
         sql.append("union ");
         sql.append("select b.TRAVELER AS TRAVELER,b.PALLET AS PALLET, b.SKU AS SKU,b.LOT, ");
         sql.append("(select sum(a.ordqty ) from recvdet a where a.TRAVELER='"+travelId+"' and a.LOT=b.LOT and a.loc1 is null),'','','N' ");
         sql.append("FROM RECVDET b WHERE b.TRAVELER='"+travelId+"' "); 
         sql.append("and b.loc1 is null and b.loc not in (select loc1 from recvdet where TRAVELER='"+travelId+"' and loc1 is not null ) ");
         sql.append("group by traveler,PALLET,SKU,b.LOT ");*/
        			        
//        code commented by Arun for #1849 fix and added below new query
       
  /*       StringBuffer sql = new StringBuffer("SELECT b.TRAVELER AS TRAVELER,b.PALLET AS PALLET, b.SKU AS SKU,b.LOT, ");
         sql.append("(SELECT SUM(a.ordqty) FROM RECVDET a WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT  and ((a.loc = b.loc1 and a.loc1 is null) or (a.loc is null and a.loc1 is not null)or (a.loc is not null and a.loc1 is not null)) and "); 
         sql.append("(a.LOC1 = b.LOC1  or a.loc1 is null)  ) AS ORDQTY, SUM(b.PUTAWAYQTY)  ");
         sql.append("AS PUTAWAYQTY ,isnull(b.LOC1,'') AS LOC1, ");
         sql.append("PUTAWAYSTATUS = CASE ");
         sql.append("WHEN (SELECT SUM(a.ordqty) "); 
         sql.append("FROM RECVDET a WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT and (a.loc = b.loc1 or a.loc1 is null or (a.loc is null and b.loc1 is not null)) and (a.LOC1 = b.LOC1 ");  
         sql.append("or a.loc1 is null)) <> sum(b.PUTAWAYQTY) THEN  'N' "); 
         sql.append("WHEN "); 
         sql.append("(SELECT SUM(a.ordqty) FROM RECVDET a WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT and (a.loc = b.loc1 or a.loc1 is null or (a.loc is null and b.loc1 is not null)) and "); 
         sql.append("(a.LOC1 = b.LOC1  or a.loc1 is null)) = sum(b.PUTAWAYQTY) THEN  'C' ");
         sql.append("END  ");
         sql.append("FROM RECVDET b WHERE b.TRAVELER='"+travelId+"' ");
         sql.append("GROUP BY b.TRAVELER,b.PALLET,b.SKU,b.LOT,b.loc,b.LOC1,b.PUTAWAYSTATUS HAVING b.LOC1 IS NOT NULL "); 
         sql.append("union  ");
         sql.append("SELECT b.TRAVELER AS TRAVELER,b.PALLET AS PALLET, b.SKU AS SKU,b.LOT, "); 
         sql.append("(SELECT SUM(a.ordqty) FROM RECVDET a WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT  and (a.loc is null and a.loc1 is null)) AS ORDQTY, SUM(b.PUTAWAYQTY) "); 
         sql.append("AS PUTAWAYQTY ,isnull(b.LOC1,'') AS LOC1, ");
         sql.append("PUTAWAYSTATUS = CASE ");
         sql.append("WHEN (SELECT SUM(a.ordqty) "); 
         sql.append("FROM RECVDET a WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT and (a.loc is null and a.loc1 is null)) <> sum(b.PUTAWAYQTY) THEN  'N' "); 
         sql.append("WHEN "); 
         sql.append("(SELECT SUM(a.ordqty) FROM RECVDET a WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT and (a.loc is null and a.loc1 is null)) = sum(b.PUTAWAYQTY) THEN  'C' ");
         sql.append("END  ");
         sql.append("FROM RECVDET b WHERE b.TRAVELER='"+travelId+"' ");
         sql.append("GROUP BY b.TRAVELER,b.PALLET,b.SKU,b.LOT,b.loc,b.LOC1,b.PUTAWAYSTATUS HAVING b.LOC1 IS NULL and b.loc is null ");
         sql.append("union "); 
         sql.append("select b.TRAVELER AS TRAVELER,b.PALLET AS PALLET, b.SKU AS SKU,b.LOT, (select sum(a.ordqty ) "); 
         sql.append("from recvdet a where a.TRAVELER='"+travelId+"' and a.LOT=b.LOT and a.loc1 is null),'','','N' FROM RECVDET b "); 
         sql.append("WHERE b.TRAVELER='"+travelId+"' and b.loc1 is null group by traveler,PALLET,SKU,b.LOT ");*/
        
//      New updated query for #1849 added by arun on 9 Nov 2011
       
        // to show records for which Put away is done and LOC assinged  is not null
        StringBuffer sql = new StringBuffer("SELECT b.TRAVELER as TRAVELER,b.PALLET as PALLET, b.SKU as SKU,b.LOT,");
        sql.append("(SELECT SUM(a.ORDQTY) From RECVDET a WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT "); 
        sql.append("and ((a.LOC = b.LOC1 and a.LOC1 is null) ");
        sql.append("or (a.LOC is null and a.LOC1 is not null) ");
        sql.append("or (a.LOC is not null and a.LOC1 is not null)) "); 
        sql.append("and (a.LOC1 = b.LOC1  or a.LOC1 is null)  ) as ORDQTY, sum(b.PUTAWAYQTY) "); 
        sql.append("as PUTAWAYQTY ,isnull(b.LOC1,'') as LOC1, ");
		 sql.append("PUTAWAYSTATUS = CASE ");
		 sql.append("WHEN (SELECT SUM(a.ORDQTY) From RECVDET a WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT  "); 
		 sql.append("and ((a.LOC = b.LOC1 and a.LOC1 is null) ");
		 sql.append("or (a.LOC is null and a.LOC1 is not null) ");
		 sql.append("or (a.LOC is not null and a.LOC1 is not null)) ");  
		 sql.append("and (a.LOC1 = b.LOC1  or a.LOC1 is null) ) > sum(b.PUTAWAYQTY) then  'N' "); 
		 sql.append("WHEN "); 
		 sql.append("(SELECT SUM(a.ORDQTY) From RECVDET a WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT ");  
		 sql.append("and ((a.LOC = b.LOC1 and a.LOC1 is null) ");
		 sql.append("or (a.LOC is null and a.LOC1 is not null) ");
		 sql.append("or (a.LOC is not null and a.LOC1 is not null)) "); 
	     sql.append("and (a.LOC1 = b.LOC1  or a.LOC1 is null) ) = SUM(b.PUTAWAYQTY) THEN  'C' ");  
		 sql.append("END "); 
		 sql.append("FROM RECVDET b "); 
		 sql.append("WHERE b.TRAVELER='"+travelId+"' ");
		 sql.append("GROUP BY b.TRAVELER,b.PALLET,b.sku,b.LOT,b.LOC,b.LOC1,b.PUTAWAYSTATUS having b.LOC1 is not null "); 
		 sql.append("UNION ");
        //show records - put away is done but LOC assigned was null
		 sql.append("SELECT b.TRAVELER as TRAVELER,b.PALLET as PALLET, b.SKU as SKU,b.LOT, "); 
		 sql.append("(SELECT SUM(a.ORDQTY) FROM RECVDET a  ");
		 sql.append("WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT  and (a.LOC is null and a.LOC1 is null)) as ORDQTY, sum(b.putawayqty) "); 
		 sql.append("as PUTAWAYQTY ,isnull(b.LOC1,'') as LOC1, ");
		 sql.append("PUTAWAYSTATUS = CASE ");
		 sql.append("WHEN (SELECT SUM(a.ORDQTY) "); 
		 sql.append("FROM RECVDET a  ");
		 sql.append("WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT and (a.LOC is null and a.LOC1 is null)) > SUM(b.PUTAWAYQTY) THEN  'N' "); 
		 sql.append("WHEN  ");
		 sql.append("(SELECT SUM(a.ORDQTY) from RECVDET a "); 
		 sql.append("WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT and (a.LOC is null and a.LOC1 is null)) = SUM(b.PUTAWAYQTY) THEN  'C' ");
		 sql.append("END  ");
		 sql.append("FROM RECVDET b "); 
		 sql.append("WHERE b.TRAVELER='"+travelId+"' ");
		 sql.append("GROUP BY b.TRAVELER,b.PALLET,b.SKU,b.LOT,b.LOC,b.LOC1,b.PUTAWAYSTATUS having b.LOC1 is null and b.LOC is null ");
		 sql.append("UNION ");
        // show records - put away is not done but LOC assigned was not null
		 sql.append("SELECT b.TRAVELER as TRAVELER,b.PALLET as PALLET, b.SKU as SKU,b.LOT, "); 
		 sql.append("(SELECT SUM(a.ORDQTY) FROM RECVDET a  ");
		 sql.append("WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT  and  (a.LOC1 is null and a.LOC is not null)) as ORDQTY, sum(b.putawayqty) "); 
		 sql.append("as PUTAWAYQTY ,isnull(b.LOC1,'') as LOC1, ");
		 sql.append("PUTAWAYSTATUS = CASE ");
		 sql.append("WHEN (SELECT SUM(a.ORDQTY) "); 
		 sql.append("FROM RECVDET a  ");
		 sql.append("WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT and   (a.LOC1 is null and a.LOC is not null)) > SUM(b.PUTAWAYQTY) THEN  'N' "); 
		 sql.append("WHEN  ");
		 sql.append("(SELECT SUM(a.ORDQTY) from RECVDET a "); 
        sql.append("WHERE a.TRAVELER='"+travelId+"' and a.LOT=b.LOT and  (a.LOC1 is null and a.LOC is not null)) = SUM(b.PUTAWAYQTY) THEN  'C' ");
        sql.append("END  ");
        sql.append("FROM RECVDET b "); 
        sql.append("WHERE b.TRAVELER='"+travelId+"' ");  
        sql.append("and b.LOC not in (select distinct isnull(c.LOC1,' ') ");  // case for LOC assigned A0101, 2 trays put away in A0102, A0103: 3 rows shuld be displayed 
        sql.append("from RECVDET c  ");
        sql.append("where c.TRAVELER='"+travelId+"' "); 
        sql.append("and b.LOT = c.LOT) ");
        sql.append("GROUP BY b.TRAVELER,b.PALLET,b.SKU,b.LOT,b.LOC,b.LOC1,b.PUTAWAYSTATUS having b.LOC1 is null and b.LOC is not null ");

          
                  
         MLogger.log(0,"getPutAwayDetails(aQuery)::"+sql);
        
        arrList = invdao.selectForReport(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getPutAwayDetails :: getPutAwayDetails:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getPutAwayDetails()");
    return arrList;
  }
  
   public ArrayList getMTIDDetails(String aPlant,String mtid) {
    MLogger.log(1, this.getClass() + " getMTIDDetails()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
    
      String aQuery = "  select traveler,pallet,mtid,sku,description,lot,Ordqty,isnull(loc,'') as loc,isnull(receivestatus,'') as receivestatus from  recvdet where mtid = '"+mtid+"'";
      
      MLogger.log(0,"getTravellerSummary(aQuery)::"+aQuery);
      arrList = invdao.selectForReport(aQuery,ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getMTIDDetails :: getMTIDDetails:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getMTIDDetails()");
    return arrList;
  }
  
  
    public ArrayList getPutAwayList(String aPlant,String travelId,String filestatus) {
    MLogger.log(1, this.getClass() + " getPutAwayList()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "travelID" + travelId);
      
      StringBuffer sql = new StringBuffer(" select traveler,receivestatus,putawaystatus,filegenerated");
      
      sql.append(" from recvhdr where traveler <> '' and putawaystatus = 'C' ");
      
       if(filestatus.equalsIgnoreCase(""))
      {
      } else
      {
        sql.append(" and filegenerated='"+filestatus+"' ");
      }
      
      MLogger.log(0,"getPutAwayList(aQuery)::"+sql);
      
      arrList = invdao.selectForReport(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getPutAwayDetails :: getPutAwayDetails:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getPutAwayDetails()");
    return arrList;
  }
 
 public ArrayList getReceivedDetails(String aPlant,String lot,String mtid) {
    MLogger.log(1, this.getClass() + " getReceivedDetails()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "mtid" + mtid);
  
      StringBuffer sql = new StringBuffer(" SELECT TRAVELER,PALLET,MTID,SKU,LOT,ORDQTY,RECVQTY,ISNULL(REMARK,'') as REMARK ");
      sql.append(" FROM RECVDET WHERE MTID='"+mtid+"' AND LOT='"+lot+"'");
             
      MLogger.log(0,"getReceivedDetails(aQuery)::"+sql.toString());
      
      arrList = invdao.selectForReport(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getMTIDDetails :: getMTIDDetails:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getMTIDDetails()");
    return arrList;
  } 
  
  
  
 public String getExpiryDateForLot(String item,String lot,String Mtid,String Traveler) throws Exception
{
  String expDate="";
 // MLogger.log(1, this.getClass() + " getExpiryDateForLot()");
  try
  {
  String query=" CREATEDON ";
  Hashtable ht=new Hashtable();
  ht.put(MDbConstant.SKU,item);
  ht.put(MDbConstant.LOT,lot);
  ht.put(MDbConstant.MTID,Mtid);
  ht.put(MDbConstant.TRAVELER,Traveler);
  Map m=selectRow(query,ht);
  expDate=(String)m.get("CREATEDON");
  }
  catch(Exception e)
  {
     MLogger.log(0,"######################### Exception :: getExpiryDateForLot() : ######################### \n");
     MLogger.log(0,""+ e.getMessage());
     MLogger.log(0,"######################### Exception :: getExpiryDateForLot() : ######################### \n");
     throw e;
  }
 // MLogger.log(-1, this.getClass() + " getExpiryDateForLot()");
  return expDate;
}

 public int chkUpdatedQty (String  item) throws Exception
  {
     Connection con=null;
     con = DbBean.getConnection();
     PreparedStatement pStmt = null;
     ResultSet rs = null;
     int qty=0;
         
    try
    {
                
      String sql=" select Qty_Per_tray from  PRD_CLASS_MST A,ItemMst B where A. Prd_Cls_Id =B.Prd_Cls_Id  and B.Item='"+item+"'";;
      pStmt = con.prepareStatement(sql);
      rs = pStmt.executeQuery();
      while (rs.next()) {
         qty=rs.getInt(1);
      }
    }
    catch(Exception e) {
      MLogger.log(0,"######################### Exception :: GETTEMITEMPRODUCTCLASS :: isExisit() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: GETTEMITEMPRODUCTCLASS :: isExisit() : ######################### \n");
      throw e;
    }
    finally{
    if (rs != null) {
       rs.close();
    }
    if (pStmt != null) {
       pStmt.close();
    }
    if (con != null) {
       DbBean.closeConnection(con);
     }
    }
   return  qty;
    }  
 
 /* Method Added by Ranjana
  * Purpose: Generation of Inbound Discrepancy Report.*/ 
 
 public ArrayList TrayDescrepencyReport(Hashtable ht) throws Exception{
	 
	 MLogger.log(1,"call TrayDescrepencyReport");
	
	 	ArrayList invQryList = new ArrayList();
		Map map = null;
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		
		try {
			con = DbBean.getConnection();

			cs = con.prepareCall("{call dbo.[PROC_INBOUNDDISCREPANCY](?,?,?)}");
			cs.setString(1, (String)ht.get("ITEM"));
			cs.setString(2, (String)ht.get("LOT"));
			cs.setString(3, (String)ht.get("SKU"));

			rs = cs.executeQuery();
			MLogger.log(1, " size of column" + rs.getMetaData().getColumnCount());
			while (rs.next())
			{	
				map = new HashMap();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) 
				{	
					map
							.put(rs.getMetaData().getColumnLabel(i), rs
									.getString(i));
				}
				invQryList.add(map);				
			}
			MLogger.log(1, " size of the invQryList" + invQryList.size());
			
		} catch (Exception e) {
			MLogger.log(1, " TrayDescrepencyReport Fail....." );
		}finally{
		    if (rs != null) {
		        rs.close();
		     }
		     if (con != null) {
		        DbBean.closeConnection(con);
		      }
		     }
		return invQryList;
	}


 /*  Added by Ranjana 
  *  Purpose: LOT restriction in PDA under ticket WO0000000284867 
  *  for the process of RECEIVING*/
 
 public Map isReceivingLotStatus(Hashtable ht) throws Exception {
		
		ResultSet rs = null;
		CallableStatement cs = null;
		Map map = new HashMap();
		java.sql.Connection con = null;
		
		MLogger.log(1, this.getClass() + "call proc_check_lotrestriction");
		try {
			con = DbBean.getConnection();
			cs = con.prepareCall("{call dbo.[PROC_CHECK_LOTRESTRICTION](?,?,?)}");
			cs.setString(1, (String) ht.get("TRAVELER"));
			cs.setString(2, (String) ht.get("LOT"));
			cs.setString(3,(String) ht.get("TYPE"));

			rs = cs.executeQuery();
					
			while (rs.next()) {		
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
				}
			}
		} catch (Exception e) {
			MLogger.log(1, this.getClass() + "call isReceivingLotStatus.");
			throw e;
		}finally{
		    if (rs != null) {
		        rs.close();
		     }
		     if (con != null) {
		        DbBean.closeConnection(con);
		      }
		     }
		return map;
	}

  
}
