package com.murho.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.gates.DbBean;
import com.murho.utils.MLogger;



public class RecvHdrDAO    extends BaseDAO {
  public RecvHdrDAO() {
  }

  public static final String TABLE_NAME = "RecvHdr";

  
   public Map selectRow(String query,Hashtable ht) throws Exception {
   MLogger.log(1, this.getClass() + " selectRow()");
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
          MLogger.log(0,"######################### Exception :: selectRow() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: selectRow() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
    }
    finally{
      if (con != null) {
           DbBean.closeConnection(con);
      }
    }

    MLogger.log( -1, this.getClass() + " selectRow()");
    return map;
  }
  
  
  
  
   public boolean update(String query, Hashtable map, String extCond) throws   Exception {
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


  public ArrayList getOpenTraveler() throws Exception {
   MLogger.log(1, this.getClass() + " getOpenTraveler()");
   ArrayList al=new ArrayList();
   // connection
   java.sql.Connection con=null;
   try
   {
    con=com.murho.gates.DbBean.getConnection();
   //query
   StringBuffer sql = new StringBuffer(" SELECT ");
   sql.append(" Traveler ");
   sql.append(" ");
   sql.append(" FROM " + TABLE_NAME );
   sql.append(" WHERE  receiveStatus <> '" + "C" + "' ");
 
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
   MLogger.log( -1, this.getClass() + " getOpenTraveler()");
   return al;

 }
 
  
    public boolean UpdateRecvHdr(String traveler) throws Exception
    {
    MLogger.log(1, this.getClass() + " UpdateRecvHdr()");
    boolean update = false;
    java.sql.Connection con=null;
    try{
    con=DbBean.getConnection();

    //query
    StringBuffer sql = new StringBuffer(" UPDATE RECVHDR SET FILEGENERATED='Y' WHERE TRAVELER IN ("+traveler+") ");
    MLogger.query(sql.toString());
    update=updateData(con,sql.toString());
   }
   catch(Exception e) {
          MLogger.log(0,"######################### Exception :: UpdateRecvHdr() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: UpdateRecvHdr() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
    }
    finally{
        if (con != null) {
            DbBean.closeConnection(con);
    }
   }
    MLogger.log(-1, this.getClass() + " UpdateRecvHdr()");
    return update;
  }

 
}
