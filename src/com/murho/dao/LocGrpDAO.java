package com.murho.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import com.murho.gates.DbBean;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;



public class LocGrpDAO  extends BaseDAO{
  StrUtils strUtils = new StrUtils();
  private String TABLE_NAME = "LOC_GROUP_MST";
  Connection con = null;

  public LocGrpDAO() {
  }

 
   
    public boolean insertIntolocGrpMst(Hashtable ht) throws Exception
   {
     MLogger.log(1, this.getClass() + " insertIntolocGrpMst()");

          boolean inserted      = false;
          java.sql.Connection con=null;
          try{
            con=DbBean.getConnection();
            String FIELDS = "", VALUES = "";
            Enumeration enum1 = ht.keys();
            for (int i = 0; i < ht.size(); i++) {
                String key   = StrUtils.fString((String) enum1.nextElement());
                String value = StrUtils.fString((String)ht.get(key));
                FIELDS += key + ",";
                VALUES += "'" + value + "',";
              }
              String query = "INSERT INTO " + TABLE_NAME + "("+FIELDS.substring(0,FIELDS.length()-1)+") VALUES ("+VALUES.substring(0,VALUES.length() -1) +")";

              MLogger.query(query);

           inserted=insertData(con,query);

          }catch(Exception e) {
          MLogger.log(0,"######################### Exception :: insertIntolocGrpMst() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: insertIntolocGrpMst() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
         }
         finally{
          if (con != null) {
          DbBean.closeConnection(con);
          }
         }//
          MLogger.log(-1, this.getClass() + " insertIntolocGrpMst()");
          return inserted;
     }
   /**
    * method  : deleteLocGrpId(String aLocGrpId)
    * description : Delete the existing record  for aLocGrpId
    * @param : String aLocGrpId
    * @return : boolean - true / false
    * @throws Exception
    */
    
     public boolean deleteLocGrpId(java.util.Hashtable ht) throws Exception
     {
    MLogger.log(1, this.getClass() + " deleteLocGrpId()");
    boolean deleteLocGrp = false;
   java.sql.Connection con=null;
   try{
   con=DbBean.getConnection();

   //query
   StringBuffer sql = new StringBuffer(" DELETE ");
   sql.append(" ");
   sql.append(" FROM " + TABLE_NAME );
   sql.append(" WHERE " + formCondition(ht) );

    MLogger.query(sql.toString());
    deleteLocGrp=updateData(con,sql.toString());
   }
   catch(Exception e) {
          MLogger.log(0,"######################### Exception :: deleteLocGrpId() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: deleteLocGrpId() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
    }
    finally{
        if (con != null) {
            DbBean.closeConnection(con);
    }
   }
    MLogger.log(-1, this.getClass() + " deleteLocGrpId()");

    return deleteLocGrp;
  }
  

 public boolean isExists(Hashtable ht) throws Exception {

  boolean flag=false;
   java.sql.Connection con=null;

   // connection
   try
   {
   con=com.murho.gates.DbBean.getConnection();

   //query
   StringBuffer sql = new StringBuffer(" SELECT ");
   sql.append(" 1 ");
   sql.append(" ");
   sql.append(" FROM " + TABLE_NAME );
   sql.append(" WHERE  " + formCondition(ht));

   MLogger.query(sql.toString());

    flag= isExists(con,sql.toString());
    
   }catch(Exception e) {
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

 
 
  
     public boolean updateLocGrpId(Hashtable htUpdate,Hashtable htCondition) throws Exception
     {
       boolean update      = false;
       PreparedStatement ps = null;

       try{
         con = DbBean.getConnection();
         String sUpdate = " ", sCondition = " ";


         // generate the condition string
         Enumeration enumUpdate = htUpdate.keys();
         for (int i = 0; i < htUpdate.size(); i++) {
              String key   = strUtils.fString((String) enumUpdate.nextElement());
              String value =  strUtils.fString((String)htUpdate.get(key));
                  sUpdate += key.toUpperCase() +" = '" + value + "',";
         }

         // generate the update string
         Enumeration enumCondition = htCondition.keys();
         for (int i = 0; i < htCondition.size(); i++) {
             String key   = strUtils.fString((String) enumCondition.nextElement());
             String value =  strUtils.fString((String)htCondition.get(key));
                 sCondition += key.toUpperCase() +" = '" + value.toUpperCase() + "' AND ";

         }
         sUpdate = (sUpdate.length() > 0) ? " SET " + sUpdate.substring(0,sUpdate.length() - 1): "";
         sCondition = (sCondition.length() > 0) ? " WHERE  "+sCondition.substring(0,sCondition.length() - 4): "";

         String stmt = "UPDATE "+TABLE_NAME + sUpdate +sCondition;
         ps = con.prepareStatement(stmt);
         int iCnt = ps.executeUpdate();
         if(iCnt > 0) update = true;

       }catch(Exception e) { throw e; }
       finally
       {
         DbBean.closeConnection(con, ps);
       }
       return update;
     }

  public boolean updateLocGrpId(String query, Hashtable htCondition, String extCond) throws Exception 
    {
   MLogger.log(1, this.getClass() + " updateLocGrpId()");
   boolean flag = false;
  java.sql.Connection con=null;
   // connection
   try
   {
      con=com.murho.gates.DbBean.getConnection();

      StringBuffer sql = new StringBuffer(" UPDATE " + TABLE_NAME);
      sql.append(" ");
      sql.append(query);
      sql.append(" WHERE  " + formCondition(htCondition));

      if(extCond.length()!=0){
        sql.append(extCond);
      }

      MLogger.query(sql.toString());
      
    

      flag = updateData(con,sql.toString());
    } catch(Exception e) {
          MLogger.log(0,"######################### Exception :: updateLocGrpId() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: updateLocGrpId() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
        }
        finally{
        if (con != null) {
           DbBean.closeConnection(con);
        }
   }
   MLogger.log( -1, this.getClass() + " updateLocGrpId()");

   return flag;

 }
 
   public Map selectRow(String query,Hashtable ht) throws Exception {
   boolean flag = false;
   Map resultMap=null;
   // connection
   java.sql.Connection con=null;

   try{
   con=com.murho.gates.DbBean.getConnection();
   StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + TABLE_NAME);
   sql.append(" WHERE ");

   String conditon=formCondition(ht);

   sql.append(conditon);

   MLogger.query(" "+sql.toString());

   resultMap=getRowOfData(con,sql.toString());

   }
  catch(Exception e)
   {
      MLogger.info("######################### Exception :: selectRow() : ######################### \n");
      MLogger.exception(this,e);
      MLogger.info("######################### Exception :: selectRow() : ######################### \n");
      throw e;
   }
   finally
   {
      if (con != null) {
        DbBean.closeConnection(con);
      }
   }
    return resultMap;

 }
 
 
       
  public ArrayList getLocGrpIdStartsWith(String aLocGrpId ) throws Exception {          
   
   Connection con=null;
   ArrayList al=new ArrayList();
   try
   {
   con=DbBean.getConnection();

   boolean flag=false;
   //query
    String sQry = "SELECT LOC_GRP_ID,LOC_GRP_DESC,TOTAL_BAY FROM "+ TABLE_NAME+" WHERE LOC_GRP_ID LIKE '"+aLocGrpId+"%'";
    MLogger.query(" "+sQry);

     al = selectData(con,sQry);

   } catch(Exception e)
   {
       MLogger.exception(this,e);
      throw e;
   }
   finally
   {
      if (con != null) {
         DbBean.closeConnection(con);
      }
   }
   return  al;
 }
  public ArrayList getTotalBay(String LocGrpId) throws Exception {
        boolean flag = false;
       ArrayList al= new ArrayList();
       // connection
       java.sql.Connection con=null;
       try{
           con=com.murho.gates.DbBean.getConnection();
    
          StringBuffer sql = new StringBuffer(" SELECT TOTAL_BAY ");
          sql.append(" ");
          sql.append(" FROM "+TABLE_NAME+" " );
          sql.append(" WHERE LOC_GRP_ID = '" + LocGrpId + "' ");
          MLogger.query(" "+sql.toString());
          al=selectData(con,sql.toString());
       }
       catch(Exception e)
        {
             MLogger.exception(this,e);
             throw e;
        }
    finally{
      if (con != null) {
       DbBean.closeConnection(con);
      }
    }
    return al;
  }
}