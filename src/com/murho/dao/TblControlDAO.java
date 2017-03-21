package com.murho.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import com.murho.gates.DbBean;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;



public class TblControlDAO extends BaseDAO{

  public TblControlDAO() {
  }

   public static final String TABLE_NAME="TBLCONTROL";


   public  static String getNextSeqNo(String aFunc)  throws Exception{
    String nextSeqno      = "";
    PreparedStatement ps1 = null;
    PreparedStatement ps2 = null;
    Connection con1       = null;
    Connection con2       = null;
    ResultSet rs          = null;
    try{
      con1 = DbBean.getConnection();
      con2 = DbBean.getConnection();
      // update the next seq no
      String sQry2Update = "UPDATE "+TABLE_NAME+" SET NXTSEQ = NXTSEQ+1 WHERE FUNC = '"+aFunc+"'";
      ps1 = con1.prepareStatement(sQry2Update);
      int i = ps1.executeUpdate();
      if(i <= 0) return "";

      // reterieve the seq no
      String sQry2 = "SELECT NXTSEQ FROM "+ TABLE_NAME +" WHERE FUNC = '"+aFunc+"'";
      ps2 = con2.prepareStatement(sQry2);
      rs = ps2.executeQuery();
      while(rs.next()){
        nextSeqno = StrUtils.fString((String)rs.getString(1));
      }
    }catch(Exception e)
    {
     MLogger.log(0, "" + e.getMessage());
      throw e;
   }
    finally
    {
      DbBean.closeConnection(con1, ps1);
      DbBean.closeConnection(con2, ps2);
    }
    return nextSeqno;
  }

public boolean insert(Hashtable ht) throws Exception
   {
     MLogger.log(1, this.getClass() + " insert()");

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
          MLogger.log(0,"######################### Exception :: insert() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: insert() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
         }
         finally{
          if (con != null) {
          DbBean.closeConnection(con);
          }
         }
          MLogger.log(-1, this.getClass() + " insert()");
          return inserted;
     }
     
   public boolean delete(java.util.Hashtable ht) throws Exception
   {
    MLogger.log(1, this.getClass() + " delete()");
    boolean deletePrdCls = false;
    java.sql.Connection con=null;
   try{
   con=DbBean.getConnection();

   //query
   StringBuffer sql = new StringBuffer(" DELETE ");
   sql.append(" ");
   sql.append(" FROM " + TABLE_NAME );
   sql.append(" WHERE " + formCondition(ht) );

    MLogger.query(sql.toString());
    deletePrdCls=updateData(con,sql.toString());
   }
   catch(Exception e) {
          MLogger.log(0,"######################### Exception :: delete() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: delete() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
    }
    finally{
        if (con != null) {
            DbBean.closeConnection(con);
    }
   }
    MLogger.log(-1, this.getClass() + " delete()");

    return deletePrdCls;
  }
      public boolean update(Hashtable htUpdate,Hashtable htCondition) throws Exception
     {
       boolean update      = false;
       PreparedStatement ps = null;
       java.sql.Connection con=null;

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
 
  public ArrayList getTblControlStartsWith(String aFunc) throws Exception {          
   
   Connection con=null;
   ArrayList al=new ArrayList();
   try
   {
   con=DbBean.getConnection();

   boolean flag=false;
   //query
    String sQry = "SELECT " + MDbConstant.TBL_FUNCTION+","+MDbConstant.TBL_PREFIX+","+MDbConstant.DESCRIPTION+" FROM "+ TABLE_NAME+" WHERE "+MDbConstant.TBL_FUNCTION+" LIKE '"+aFunc+"%'";
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
 
  public ArrayList getTblControlDetails(String aFunc,String apfx) throws Exception {          
   
   Connection con=null;
   ArrayList al=new ArrayList();
   try
   {
   con=DbBean.getConnection();

   boolean flag=false;
   //query
    String sQry = "SELECT " + MDbConstant.TBL_FUNCTION+","+MDbConstant.TBL_PREFIX+","+MDbConstant.DESCRIPTION+","+MDbConstant.TBL_MIN_SEQ+","+MDbConstant.TBL_MAX_SEQ+","+MDbConstant.TBL_NEXT_SEQ+" FROM "+ TABLE_NAME+" WHERE "+MDbConstant.TBL_FUNCTION+" LIKE '"+aFunc+"%'";
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
 
  public String getNextSeqNo(String aFunc,String aPfx)  throws Exception{
    String nextSeqno      = "";
    PreparedStatement ps1 = null;
    PreparedStatement ps2 = null;
    Connection con1       = null;
    Connection con2       = null;
    ResultSet rs          = null;
    StrUtils _strUtils = new StrUtils();
    try{
      con1 = DbBean.getConnection();
      con2 = DbBean.getConnection();
      // update the next seq no
      String sQry2Update = "UPDATE "+TABLE_NAME+" SET NXTSEQ = NXTSEQ+1 WHERE FUNC = '"+aFunc+"' AND PREFIX ='"+aPfx+"'";
      ps1 = con1.prepareStatement(sQry2Update);
      int i = ps1.executeUpdate();
      if(i <= 0) return "";

      // reterieve the seq no
      String sQry2 = "SELECT NXTSEQ FROM "+TABLE_NAME+" WHERE FUNC = '"+aFunc+"' AND PREFIX ='"+aPfx+"'";
      ps2 = con2.prepareStatement(sQry2);
      rs = ps2.executeQuery();
      while(rs.next()){
        nextSeqno = _strUtils.fString((String)rs.getString(1));
      }
    }catch(Exception e) { throw e;}
    finally
    {
      DbBean.closeConnection(con1, ps1);
      DbBean.closeConnection(con2, ps2);
    }
    return nextSeqno;
  }
}