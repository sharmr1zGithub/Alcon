package com.murho.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.gates.DbBean;
import com.murho.utils.MLogger;



public class TrayDetDAO
    extends BaseDAO {
  public TrayDetDAO() {
  }

  public static final String TABLE_NAME = "TRAY_DET";

  
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
public boolean isExisit(Hashtable ht,String extraCon) throws Exception {

 boolean flag=false;

  // connection
  java.sql.Connection con=null;

  try{
  con=com.murho.gates.DbBean.getConnection();
  StringBuffer sql = new StringBuffer(" SELECT ");
  sql.append("COUNT(*) ");
  sql.append(" ");
  sql.append(" FROM " + TABLE_NAME );
  sql.append(" WHERE  " + formCondition(ht));
  sql.append( " " + extraCon);

  MLogger.query(" "+sql.toString());

   flag= isExists(con,sql.toString());
  }
  catch(Exception e)
  {
    MLogger.log(0,"######################### Exception :: isExisit() : ######################### \n");
    MLogger.log(0,""+ e.getMessage());
    MLogger.log(0,"######################### Exception :: isExisit() : ######################### \n");
    throw e;
  }
  finally{
    if (con != null) {
       DbBean.closeConnection(con);
    }
  }
  return flag;
}


  public boolean isExists(Hashtable ht) throws Exception {
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
    return flag;

  }

public Map getMtidDetails(String aTravelId,String aPallet,String aMtid,String TrayId) throws Exception {
   Map map=null;
// connection
   java.sql.Connection con=null;
   try
   {
    con=com.murho.gates.DbBean.getConnection();
   //query

   StringBuffer sql = new StringBuffer(" select SLNO, MTID,SKU,LOT,QTY from tray_det where Traveler_id ='"+aTravelId+"' ");
   sql.append( "  and (mtid = '"+aMtid+"' or slno ='"+aMtid+"') and trayid ='"+TrayId+"'  ");
  
   MLogger.query(sql.toString());
       
   map= getRowOfData(con,sql.toString());
   
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
   return map;

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
    }
  MLogger.log(-1, this.getClass() + " selectForReport()");
  return al;
  }
  
  public ArrayList loadDetails(String query,Hashtable ht) throws Exception {
       MLogger.log(1, this.getClass() + " loadDetails()");
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
          MLogger.log(0,"######################### Exception :: loadDetails() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: loadDetails() : ######################### \n");
          MLogger.log(-1, "");
          throw e;
        }
    finally{
      if (con != null) {
      DbBean.closeConnection(con);
      }
    }
  MLogger.log(-1, this.getClass() + " loadPickingDetails()");
  return al;
  }
 
 
 public ArrayList loadOBDetails(String query,Hashtable ht) throws Exception {
       MLogger.log(1, this.getClass() + " loadDetails()");
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
          conditon=formCondition(ht);
          sql.append( " " + conditon);
       }

         MLogger.query(" "+sql.toString());
          al=selectData(con,sql.toString());
       }
       catch(Exception e)
        {
          MLogger.log(0,"######################### Exception :: loadDetails() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: loadDetails() : ######################### \n");
          MLogger.log(-1, "");
          throw e;
        }
    finally{
      if (con != null) {
      DbBean.closeConnection(con);
      }
    }
  MLogger.log(-1, this.getClass() + " loadPickingDetails()");
  return al;
  }
  public Map getRowOfData( String query) throws Exception {
   Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    Map map = null;
    map = new HashMap();

    try {
      conn=com.murho.gates.DbBean.getConnection();
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      MLogger.query(" "+query.toString());
        while (rs.next()) {
        map = new HashMap();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
          map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
        }
      }
    }
    catch (Exception e) {
        throw e;
    }
    finally {
      if (rs != null) {
        rs.close();
      }
      if (stmt != null) {
        stmt.close();
      }
        if (conn != null) {
      DbBean.closeConnection(conn);
      }
    }
    return map;
  } 
}
