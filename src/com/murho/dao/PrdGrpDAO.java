  
 package com.murho.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.gates.DbBean;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;



public class PrdGrpDAO  extends BaseDAO{
  StrUtils strUtils = new StrUtils();
  private String TABLE_NAME = "PRD_GROUP_MST";
  Connection con = null;

  public PrdGrpDAO() {
  }

 
   
    public boolean insertIntoPrdGrpMst(Hashtable ht) throws Exception
   {
     MLogger.log(1, this.getClass() + " insertIntoPrdGrpMst()");

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
          MLogger.log(0,"######################### Exception :: insertIntoPrdGrpMst() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: insertIntoPrdGrpMst() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
         }
         finally{
          if (con != null) {
          DbBean.closeConnection(con);
          }
         }//
          MLogger.log(-1, this.getClass() + " insertIntoPrdGrpMst()");
          return inserted;
     }
   /**
    * method  : deletePrdGrpId(String aPrdGrpId,String aLocGrpId)
    * description : Delete the existing record  for aPrdGrpId,aLocGrpId
    * @param : String aPrdGrpId,aLocGrpId
    * @return : boolean - true / false
    * @throws Exception
    */
    
     public boolean deletePrdGrpId(java.util.Hashtable ht) throws Exception
     {
    MLogger.log(1, this.getClass() + " deletePrdGrpId()");
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
          MLogger.log(0,"######################### Exception :: deletePrdGrpId() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: deletePrdGrpId() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
    }
    finally{
        if (con != null) {
            DbBean.closeConnection(con);
    }
   }
    MLogger.log(-1, this.getClass() + " deletePrdGrpId()");

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
   }//
   return flag;
 }


  
     public boolean updatePrdGrpId(Hashtable htUpdate,Hashtable htCondition) throws Exception
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
         MLogger.query(stmt);
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

  public boolean updatePrdGrpId(String query, Hashtable htCondition, String extCond) throws Exception 
    {
   MLogger.log(1, this.getClass() + " updatePrdGrpId()");
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
          MLogger.log(0,"######################### Exception :: updatePrdGrpId() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: updatePrdGrpId() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
        }
        finally{
        if (con != null) {
           DbBean.closeConnection(con);
        }
   }//
   MLogger.log( -1, this.getClass() + " updatePrdGrpId()");

   return flag;

 }
    

//check and delete
  public ArrayList getLocGrpIdForPrdgrp(String aPrdGrp) throws Exception{
         PreparedStatement ps = null;
         ResultSet rs         = null;
         ArrayList arrList    = new ArrayList();
         try{
            con = DbBean.getConnection();
            String sQry = "SELECT LOC_GRP_ID FROM "+ TABLE_NAME+" WHERE PRD_GRP_ID = '"+aPrdGrp+"%'";
            ps = con.prepareStatement(sQry);
            rs   =  ps.executeQuery();
            while(rs.next()){
                 ArrayList arrLine    = new ArrayList();
                 arrLine.add(0,strUtils.fString((String)rs.getString(1))); // 
                  arrList.add(arrLine);
            }
          }catch(Exception e) { throw e; }
         finally
         {
            DbBean.closeConnection(con, ps);
         }
        return arrList;

       }
  

       
   public Map selectRow(String query,Hashtable ht,String condition) throws Exception {
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
   if(condition.length()!=0){
        sql.append(condition);
      }
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
 
 
       
  public ArrayList getPrdGrpIdStartsWith(String aPrdGrpId ) throws Exception {          
   
   Connection con=null;
   ArrayList al=new ArrayList();
   try
   {
   con=DbBean.getConnection();

   boolean flag=false;
   //query
    String sQry = "SELECT PRD_GRP_ID,PRD_GRP_DESC,TRAYS_PER_BAY FROM "+ TABLE_NAME+" WHERE PRD_GRP_ID LIKE '"+aPrdGrpId+"%'";
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
   

}