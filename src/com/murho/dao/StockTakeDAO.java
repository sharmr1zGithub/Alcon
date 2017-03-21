package com.murho.dao;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.gates.DbBean;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;


public class StockTakeDAO extends BaseDAO
{
  public static final String TABLE_NAME="STKTAKE";
  StrUtils _StrUtils = null;
  public StockTakeDAO()
  { 
    StrUtils _StrUtils = new StrUtils();
  }
  
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
   MLogger.log(0, "Sql : " + sql.toString());

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
          con.close();
      }
    } 
    
    MLogger.log( -1, this.getClass() + " selectRow()");
    return map;
 }
 
  public String getItemLoc(String aPlant,String aItem) throws Exception
  {
   MLogger.log(1, this.getClass() + " getItemLoc()");
   String itemLoc="";
   
   Hashtable ht=new Hashtable();
   ht.put("plant",aPlant);
   ht.put("item",aItem);
     
   String query=" loc "; 
   
   Map m=selectRow(query,ht);
   
   itemLoc=  (String) m.get("loc");

   MLogger.log(-1, this.getClass() + " getItemLoc()");

   return itemLoc;
  }
  
   public ArrayList selectInvMst(String query,Hashtable ht) throws Exception {
   MLogger.log(1, this.getClass() + " selectInvMst()");
   boolean flag = false;
   ArrayList alData=new ArrayList();
   // connection
   java.sql.Connection con=null;
 
   StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + TABLE_NAME);
   String conditon="";
   
   try
   {
   con=com.murho.gates.DbBean.getConnection();
      
    if(ht.size()>0)
    {
      sql.append(" WHERE ");
      conditon=formCondition(ht);
      sql.append(conditon);
    }
      MLogger.log(0, "Sql :\n " + sql.toString());
      alData=selectData(con,sql.toString());
   }catch(Exception e) 
    {
      MLogger.log(0,"######################### Exception :: selectInvMst() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: selectInvMst() : ######################### \n");
      MLogger.log(-1,"");
      throw e;
    } finally{
      if (con != null) {
        con.close();
      }
   }
   MLogger.log( -1, this.getClass() + " selectInvMst()");

   return alData;
 }
 
  public ArrayList selectInvMst(String query,Hashtable ht,String extCondi) throws Exception {
   MLogger.log(1, this.getClass() + " selectInvMst()");
   boolean flag = false;
   ArrayList alData=new ArrayList();
   // connection
   java.sql.Connection con=null;
 
   StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + TABLE_NAME);
   String conditon="";
   
   try
   {
   con=com.murho.gates.DbBean.getConnection();
      
    if(ht.size()>0)
    {
      sql.append(" WHERE ");
      conditon=formCondition(ht);
      sql.append(conditon);
    }
    if(extCondi.length()>0) sql.append(" and " + extCondi);
      MLogger.log(0, "Sql :\n " + sql.toString());
      alData=selectData(con,sql.toString());
   }catch(Exception e) 
    {
      MLogger.log(0,"######################### Exception :: selectInvMst() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: selectInvMst() : ######################### \n");
      MLogger.log(-1,"");
      throw e;
    } finally{
      if (con != null) {
         con.close();
      }
   }
   MLogger.log( -1, this.getClass() + " selectInvMst()");

   return alData;
 }
 
 //Query Inventory - 29-03-2007
 
  public ArrayList selectInvMstDetails(String query,Hashtable ht,String extCondi) throws Exception {
   MLogger.log(1, this.getClass() + " selectInvMstDetails()");
   boolean flag = false;
   ArrayList alData=new ArrayList();
   // connection
   java.sql.Connection con=null;
   
      StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + TABLE_NAME);
   String conditon="";
   
   try
   {
   con=com.murho.gates.DbBean.getConnection();
      
    if(ht.size()>0)
    {
      sql.append(" WHERE ");
      conditon=formCondition(ht);
      sql.append(conditon);
    }
    if(extCondi.length()>0) sql.append(" and " + extCondi);
      MLogger.log(0, "Sql :\n " + sql.toString());
      alData=selectData(con,sql.toString());
   }catch(Exception e) 
    {
      MLogger.log(0,"######################### Exception :: selectInvMstDetails() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: selectInvMstDetails() : ######################### \n");
      MLogger.log(-1,"");
      throw e;
    } finally{
      if (con != null) {
        con.close();
      }
   }
   MLogger.log( -1, this.getClass() + " selectInvMstDetails()");
   return alData;
 }
 
  public boolean isExisit(Hashtable ht,String extCond) throws Exception {
  MLogger.log(1, this.getClass() + " isExisit()");
  boolean flag=false;
  java.sql.Connection con=null;
   try
   {
      con=com.murho.gates.DbBean.getConnection();
      
      StringBuffer sql = new StringBuffer(" SELECT ");
      sql.append("COUNT(*) ");
      sql.append(" ");
      sql.append(" FROM " + TABLE_NAME );
      sql.append(" WHERE  " + formCondition(ht));
      
      if(extCond.length() >0 ) sql.append(" and " +  extCond);
      MLogger.log(0, "Sql : " + sql.toString());
      flag= isExists(con,sql.toString());
      
   }
   catch(Exception e) 
   {
      MLogger.log(0,"######################### Exception :: isExisit() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: isExisit() : ######################### \n");
      MLogger.log(-1,"");
      throw e;
   } finally{
      if (con != null) {
          DbBean.closeConnection(con);
      }
   }
    MLogger.log(-1, this.getClass() + " isExisit()");
    return flag;

 }
 
  public boolean isExisit(String sql) throws Exception {
  MLogger.log(1, this.getClass() + " isExisit()");
  boolean flag=false;
  java.sql.Connection con=null;
   try
   {
      con=com.murho.gates.DbBean.getConnection();
      MLogger.log(0, "Sql : " + sql.toString());
      flag= isExists(con,sql.toString());
      
   }
   catch(Exception e) 
   {
      MLogger.log(0,"######################### Exception :: isExisit() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: isExisit() : ######################### \n");
      MLogger.log(-1,"");
      throw e;
   } finally{
      if (con != null) {
        con.close();
      }
   }
    MLogger.log(-1, this.getClass() + " isExisit()");
    return flag;

 }
   
   
   
   public boolean insertInvMst(Hashtable ht) throws Exception
   {
     MLogger.log( 1, this.getClass() + " insertInvMst()");
     boolean insertFlag      = false;
     java.sql.Connection conn=null;
     try
     {
       conn=DbBean.getConnection();
       String FIELDS = "", VALUES = "";
       Enumeration enum1 = ht.keys();
       for (int i = 0; i < ht.size(); i++)
       {
         String key   = _StrUtils.fString((String) enum1.nextElement());
         String value = _StrUtils.fString((String)ht.get(key));
         FIELDS += key + ",";
         VALUES += "'" + value + "',";
       }
       String query = "INSERT INTO " + TABLE_NAME + "("+FIELDS.substring(0,FIELDS.length()-1)+") VALUES ("+VALUES.substring(0,VALUES.length() -1) +")";
       
       MLogger.log(0, "Query : \n" + query);  
       insertFlag=insertData(conn,query);
          
    }catch(Exception e) 
     {
      MLogger.log(0,"######################### Exception :: insertInvMst() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: insertInvMst() : ######################### \n");
      MLogger.log(-1,"");
      throw e;
     }
    finally{
      if (conn != null) {
        conn.close();
      }
   }
   MLogger.log( -1, this.getClass() + " insertInvMst()");
   return insertFlag;
  }
  
 public boolean update(String query,Hashtable htCondition,String extCond) throws Exception 
 {
    boolean flag = false;
   java.sql.Connection con=null;
   try
   {
      con=com.murho.gates.DbBean.getConnection();
      StringBuffer sql = new StringBuffer(" UPDATE " + TABLE_NAME);
      sql.append(" ");
      sql.append(query);
      sql.append(" WHERE ");
      String conditon=formCondition(htCondition);
      sql.append(conditon);
  
      if(extCond.length()!=0){
       sql.append(extCond);
      }
      MLogger.log(0, "Sql : " + sql.toString());
      flag = updateData(con,sql.toString());
   

   }
   catch(Exception e) 
   {
      MLogger.log(0,"######################### Exception :: update() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: update() : ######################### \n");
      MLogger.log(-1,"");
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
  
 public boolean insertStkTake(Hashtable ht)  throws Exception{
    boolean insertRecvHis = false;
    java.sql.Connection con=null;
    try{
    
      con=DbBean.getConnection();
      String FIELDS = "", VALUES = "";
      Enumeration enum1 = ht.keys();
      for (int i = 0; i < ht.size(); i++) {
          String key   = _StrUtils.fString((String) enum1.nextElement());
          String value = _StrUtils.fString((String)ht.get(key));
          FIELDS += key.toUpperCase() + ",";
          VALUES += "'" + value.toUpperCase() + "',";
        }
        String sql = "INSERT INTO "+TABLE_NAME+" ("+FIELDS.substring(0,FIELDS.length()-1)+") VALUES ("+VALUES.substring(0,VALUES.length() -1) +")";

        MLogger.log(0, "Query : \n" + sql);

        insertRecvHis = insertData(con,sql.toString());

    } 
    catch(Exception e) 
    {
      MLogger.log(0,"######################### Exception :: insertIntoMovHis() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: insertIntoMovHis() : ######################### \n");
      MLogger.log(-1,"");
      throw e;
   }
   finally
   {
      if (con != null) {
       con.close();
      }
   }
    return insertRecvHis;
  }   
  
  
  
  
   public ArrayList selectForReport(String query,Hashtable ht) throws Exception {
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
          conditon=formCondition(ht);
          sql.append( " " + conditon);
       }

          MLogger.log(0, "Sql : " + sql.toString());
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
  
  
   public long CountForReport(String query,Hashtable ht) throws Exception {
    MLogger.log(1, this.getClass() + " CountForReport()");
       boolean flag = false;
      long recCnt =0;
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

          MLogger.log(0, "Sql : " + sql.toString());
          recCnt=CountofData(con,sql.toString());
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
  return recCnt;
  }
  
  
 
}