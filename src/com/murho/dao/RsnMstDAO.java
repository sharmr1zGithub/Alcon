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

public class RsnMstDAO extends com.murho.dao.BaseDAO{

  public static final String TABLE_NAME="RsnMst";
   Connection con = null;

  public RsnMstDAO() {
  }


  public boolean insertIntoRsnMst(Hashtable ht) throws Exception
   {
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
              MLogger.log(0,"######################### Exception :: insertIntoRsnMst() : ######################### \n");
              MLogger.log(0,""+ e.getMessage());
              MLogger.log(0,"######################### Exception :: insertIntoRsnMst() : ######################### \n");
              MLogger.log(-1,"");
              throw e;
         }
         finally{
            if (con != null) {
            DbBean.closeConnection(con);
          }
         }
            MLogger.log(-1, this.getClass() + " insertIntoRsnMst()");
            return inserted;
     }
     
  public ArrayList getReasonCode(String company, String moduleName) throws     Exception {
    ArrayList al=new ArrayList();
    //connection
    Connection con=null;
    try
    {
      con=DbBean.getConnection();
      StringBuffer sql = new StringBuffer("  SELECT  ");
      sql.append("rsnCode,");
      sql.append("rsnDesc,");
      sql.append("userflg1");
      sql.append(" ");
      sql.append(" FROM " + TABLE_NAME);
      sql.append(" WHERE  plant = '" + company + "'");
      sql.append(" AND isnull(comments1,'') = '" + moduleName + "'");
      sql.append(" ORDER BY rsnCode");

      MLogger.query(" "+sql.toString());

      al=selectData(con,sql.toString());

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
    return al;

  }


 public ArrayList getReasonMstDetails(String aItemId ) throws Exception {          
   
   Connection con=null;
   ArrayList al=new ArrayList();
   try
   {
   con=DbBean.getConnection();

   boolean flag=false;
   //query
    String sQry = "SELECT RSNCODE,RSNDESC FROM "+ TABLE_NAME+" WHERE RSNCODE LIKE '"+aItemId+"%'";
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
 
     public boolean updateReasonCode(Hashtable htUpdate,Hashtable htCondition) throws Exception
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
     
    public boolean deleteReasonCode(java.util.Hashtable ht) throws Exception
     {
    MLogger.log(1, this.getClass() + " deleteItemId()");
    boolean delete = false;
   java.sql.Connection con=null;
   try{
   con=DbBean.getConnection();

   //query
   StringBuffer sql = new StringBuffer(" DELETE ");
   sql.append(" ");
   sql.append(" FROM " + TABLE_NAME );
   sql.append(" WHERE " + formCondition(ht) );

    MLogger.query(sql.toString());
    delete=updateData(con,sql.toString());
   }
   catch(Exception e) {
          MLogger.log(0,"######################### Exception :: deleteItemId() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: deleteItemId() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
    }
    finally{
        if (con != null) {
            DbBean.closeConnection(con);
    }
   }
    MLogger.log(-1, this.getClass() + " deleteItemId()");

    return delete;
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
      MLogger.log(0,"######################### Exception :: selectRow() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: selectRow() : ######################### \n");
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


}