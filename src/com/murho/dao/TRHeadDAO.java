package com.murho.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.DO.RecvHdr_DO;
import com.murho.DO.TransactionDTO;
import com.murho.gates.DbBean;
import com.murho.utils.MLogger;


public class TRHeadDAO
    extends BaseDAO {
  public TRHeadDAO() {
  }

  public static final String TABLE_NAME = "TRHDR";


  public boolean update(String query, Hashtable map, String extCond) throws     Exception {
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

  public boolean isExist(Hashtable ht) throws Exception {
    MLogger.log(1, this.getClass() + " isExisit()");
    boolean flag = false;
    java.sql.Connection con = null;

    try {
      // connection
      con = com.murho.gates.DbBean.getConnection();
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

  
   public ArrayList getTravellerHdrList(String aPlant,String travelId) {
    MLogger.log(1, this.getClass() + " getTravellerHdrList()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "travelID" + travelId);
     
     
      String aQuery = "  select distinct  isnull(traveler,'') traveler from recvdet WHERE traveler LIKE '"+travelId+"%'";
    
      
      MLogger.log(0,"getTravellerHdrList(aQuery)::"+aQuery);
      arrList = invdao.selectForReport(aQuery,ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getTravellerHdrList :: getTravellerHdrList:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getTravellerHdrList()");
    return arrList;
  }
  
   public ArrayList getTravellerHdrList1(String aPlant,String travelId,String FileName) {
    MLogger.log(1, this.getClass() + " getTravellerHdrList()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
     // MLogger.log(0, "travelID" + travelId);
     
    
      String aQuery = "SELECT DISTINCT ISNULL(TRAVELER,'') traveler FROM RECVDET  WHERE TRAVELER  LIKE '"+travelId+"%' AND TRAVELER IN(SELECT TRAVELER FROM RECVHDR WHERE HDRFILENAME='" + FileName +"')";
      
      MLogger.log(0,"getTravellerHdrList(aQuery)::"+aQuery);
      arrList = invdao.selectForReport(aQuery,ht);
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
   ht.put("plant",aPlant);
   ht.put("Part",aItem);
     
   String query=" [description] "; 
   
   Map m=selectRow(query,ht);
   
   itemDesc=  (String) m.get("description");
  
   if(itemDesc.equalsIgnoreCase(null) || itemDesc.length()==0)
   {
     MLogger.log(0, "Item Desc is Empty");
     itemDesc="";
   }
   return itemDesc;
  }
  
  
  
     public ArrayList getTravellerView(String aPlant,String travelId) throws Exception{
      MLogger.log(1, this.getClass() + " getTravellerHdrList()");
      ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "travelID" + travelId);
     // String aQuery = "  select distinct cast(traveler as integer) as traveler  from tempdatatable WHERE traveler LIKE '"+travelId+"%' order by cast(traveler as integer) " ;
     
     String aQuery =  "select distinct traveler  as traveler  from tempdatatable WHERE traveler LIKE '"+travelId+"%' order by traveler " ;
      
      MLogger.log(0,"getTravellerHdrList(aQuery)::"+aQuery);
      arrList = invdao.selectForReport(aQuery,ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getTravellerHdrList :: getTravellerHdrList:" + e.toString());
      MLogger.log( -1, "");
      throw e;
    }
    MLogger.log( -1, this.getClass() + " getTravellerHdrList()");
    return arrList;
  }
    public ArrayList getFileView(ArrayList al) throws Exception{
      MLogger.log(1, this.getClass() + " getFileView()");
      ArrayList arrList = new ArrayList();
    try 
        {
          SQLServerDAOFactory dao_factory= (SQLServerDAOFactory)DAOFactory.getDAOFactory(1);
          SQLRecvHdr_DAO _SQLRecvHdr = new SQLRecvHdr_DAO();
          RecvHdr_DO  _RecvHdr_DO = new RecvHdr_DO();
       
          TransactionDTO  _TransactionDTO= new TransactionDTO();
          
          for(int i=0;i<al.size();i++)
          {
            _TransactionDTO= (TransactionDTO)al.get(i);
            
           _RecvHdr_DO.setFilename(_TransactionDTO.getFilename());
                       
           arrList=_SQLRecvHdr.getFileView(_RecvHdr_DO);
          }
        System.out.println("TRHeaderArrList...."+ arrList);
        }
        
  
    catch (Exception e) {
      MLogger.log("Exception :getFileList :: getFileList:" + e.toString());
      MLogger.log( -1, "");
      throw e;
    }
    MLogger.log( -1, this.getClass() + " getFileList()");
    return arrList;
  }
  
   public ArrayList get_ob_travel_hdr(String aPlant,String travelId,String RefNo) throws Exception {
    MLogger.log(1, this.getClass() + " get_ob_travel_hdr()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      
      String aQuery = "  select distinct traveler_id from ob_travel_hdr WHERE traveler_id LIKE '"+travelId+"%' and RefNo ='"+RefNo+"' order by traveler_id " ;
      
      MLogger.log(0,"get_ob_travel_hdr(aQuery)::"+aQuery);
      arrList = invdao.selectForReport(aQuery,ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :get_ob_travel_hdr :: get_ob_travel_hdr:" + e.toString());
      MLogger.log( -1, "");
      throw e;
    }
    MLogger.log( -1, this.getClass() + " getTravellerHdrList()");
    return arrList;
  }

  public ArrayList get_ob_travel_for_Shiping(String aPlant,String travelId,String RefNo) throws Exception {
    MLogger.log(1, this.getClass() + " get_ob_travel_for_Shiping()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
    
      String aQuery = "  select distinct sdet.traveler_id  , obTr.Country SHIP_TO from ship_det sdet, OB_TRAVEL_HDR obTr where obTr.traveler_id =sdet.traveler_id and obTr.refNo='"+ RefNo + "' and sdet.traveler_id like '"+travelId+"%' order by sdet.traveler_id " ;
      
      MLogger.log(0,"get_ob_travel_for_Shiping(aQuery)::"+aQuery);
      arrList = invdao.selectForReport(aQuery,ht);
    }
    catch (Exception e) {
      MLogger.log("Exception ::: get_ob_travel_for_Shiping:" + e.toString());
      MLogger.log( -1, "");
      throw e;
    }
    MLogger.log( -1, this.getClass() + " getTravellerHdrList()");
    return arrList;
  }


  public ArrayList get_ob_travel_Reference(String aPlant,String refno) throws Exception{
    MLogger.log(1, this.getClass() + " get_ob_travel_Reference()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      String aQuery = "  select distinct RefNo from ob_travel_hdr WHERE RefNo LIKE '"+refno+"%' order by refno desc" ;
      
      MLogger.log(0,"get_ob_travel_Reference(aQuery)::"+aQuery);
      arrList = invdao.selectForReport(aQuery,ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :get_ob_travel_Reference :: get_ob_travel_Reference:" + e.toString());
      throw e;
    }
    MLogger.log( -1, this.getClass() + " get_ob_travel_Reference()");
    return arrList;
  }
  
  
  
  public ArrayList get_ob_travel_Ref_Shiping(String aPlant,String refno) throws Exception{
    MLogger.log(1, this.getClass() + " get_ob_travel_Ref_Shiping()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      String aQuery = "  SELECT DISTINCT obTr.refno RefNo FROM OB_TRAVEL_HDR obTr, ship_det sdet where obTr.traveler_id = sdet.traveler_id and obTr.RefNo LIKE '"+refno+"%'" ;
      
      MLogger.log(0,"get_ob_travel_Ref_Shiping(aQuery)::"+aQuery);
      arrList = invdao.selectForReport(aQuery,ht);
    }
    catch (Exception e) {
      MLogger.log("Exception ::: get_ob_travel_Ref_Shiping:" + e.toString());
      throw e;
    }
    MLogger.log( -1, this.getClass() + " get_ob_travel_Ref_Shiping()");
    return arrList;
  } 
     public ArrayList getPickerHistoryView(String aPlant,String Refno) {
    MLogger.log(1, this.getClass() + " getPickerHistoryView()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
   
      String aQuery = "  select distinct cast(refno as BigInt) as Refno  from PICKER_LIST_HISTORY WHERE Refno LIKE '"+Refno+"%' order by cast(Refno as BigInt) desc " ;
      
      MLogger.log(0,"getPickerHistoryView(aQuery)::"+aQuery);
      arrList = invdao.selectForReport(aQuery,ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getPickerHistoryView :: getPickerHistoryView:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getPickerHistoryView()");
    return arrList;
  }

}
