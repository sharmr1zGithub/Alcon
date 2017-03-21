package com.murho.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import com.murho.gates.DbBean;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;

public class LocMstDAO extends BaseDAO
{
  public static final String TABLE_NAME="LocMst";
  StrUtils _StrUtils = null;
  Connection con = null;
  public LocMstDAO()
  { 
    StrUtils _StrUtils = new StrUtils();
  }
  
   public boolean insertIntolocMst(Hashtable ht) throws Exception
   {
     MLogger.log(1, this.getClass() + " insertIntolocMst()");

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
          MLogger.log(0,"######################### Exception :: insertIntolocMst() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: insertIntolocMst() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
         }
         finally{
          if (con != null) {
          DbBean.closeConnection(con);
          }
         }//
          MLogger.log(-1, this.getClass() + " insertIntolocMst()");
          return inserted;
     }
   /**
    * method  : deleteLocId(String aLocId)
    * description : Delete the existing record  for aLocId
    * @param : String aLocId
    * @return : boolean - true / false
    * @throws Exception
    */
    
     public boolean deleteLocId(java.util.Hashtable ht) throws Exception
     {
    MLogger.log(1, this.getClass() + " deleteLocId()");
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
          MLogger.log(0,"######################### Exception :: deleteLocId() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: deleteLocId() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
    }
    finally{
        if (con != null) {
            DbBean.closeConnection(con);
    }
   }//
    MLogger.log(-1, this.getClass() + " deleteLocId()");

    return delete;
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


   
   /**
    * @method : getLocDetails(String aLocId)
    * @return ArrayList
    * @throws Exception
    */
  public String getRemainingSpace(String LocId) throws Exception
  {
    MLogger.log( 1, this.getClass() + " getRemainingSpace()");
     String _spaceRemaining="";
     String query=" " +"Space_rem"+ " " ;
    //condintion
    Hashtable ht=new Hashtable();
    ht.put(MDbConstant.LOC_ID,LocId);

    Map m=selectRow(query,ht);

    _spaceRemaining=(String)m.get("Space_rem");

    MLogger.info("_spaceRemaining : " + _spaceRemaining );

    MLogger.log( -1, this.getClass() + " getRemainingSpace()");

    return _spaceRemaining;

  }

     public boolean updateLocId(Hashtable htUpdate,Hashtable htCondition) throws Exception
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

  public boolean updateLocId(String query, Hashtable htCondition, String extCond) throws Exception 
    {
   MLogger.log(1, this.getClass() + " updateLocId()");
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
          MLogger.log(0,"######################### Exception :: updateLocId() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: updateLocId() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
        }
        finally{
        if (con != null) {
           DbBean.closeConnection(con);
        }
   }
   MLogger.log( -1, this.getClass() + " updateLocId()");

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
  //    MLogger.log(-1,"");
      throw e;
   }
   finally
   {
      if (con != null) {
        DbBean.closeConnection(con);
      }
   }//
 //  MLogger.log( -1, this.getClass() + " select()");
   return resultMap;

 }
  
    public ArrayList getLocIdStartsWith(String aLocId ) throws Exception {          
   
   Connection con=null;
   ArrayList al=new ArrayList();
   try
   {
   con=DbBean.getConnection();

   boolean flag=false;
   //query
    String sQry = "SELECT LOC_ID,LOC_DESC,LOC_GRP_ID,SPACE_REM FROM "+ TABLE_NAME+" WHERE LOC_ID LIKE '"+aLocId+"%'";
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
//     MLogger.log( -1, this.getClass() + " getLocIdStartsWith()");
   return  al;
 }
}//end of class