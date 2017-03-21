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

public class LocationAssigningRulesDAO extends BaseDAO
{
  
  public static final String TABLE_NAME = "LOC_ASSIGNING_RULES";
   StrUtils strUtils = new StrUtils();
   Connection con = null;
 
  public LocationAssigningRulesDAO()
  {
  }
  
 
  
  public boolean isExist(Hashtable ht,String extCond) throws Exception {
    MLogger.log(1, this.getClass() + " isExisit()");
    boolean flag = false;
    java.sql.Connection con = null;

    try {
      // connection
      con = com.murho.gates.DbBean.getConnection();

      //inputs

      //query
      StringBuffer sql = new StringBuffer(" SELECT ");
      sql.append("COUNT(*) ");
      sql.append(" ");
      sql.append(" FROM " + TABLE_NAME);
      sql.append(" WHERE  " + formCondition(ht));
      
      if (extCond.length() != 0) {
          sql.append(extCond);
      }

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

  public boolean insertIntoLocAssignRules(Hashtable ht) throws Exception
   {
     MLogger.log(1, this.getClass() + " insertIntoLocAssignRules()");

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
          MLogger.log(0,"######################### Exception :: insertIntoLocAssignRules() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: insertIntoLocAssignRules() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
         }
         finally{
          if (con != null) {
          DbBean.closeConnection(con);
          }
         }
          MLogger.log(-1, this.getClass() + " insertIntoLocAssignRules()");
          return inserted;
     }
   /**
    * method  : deleteocAssignRules(String aRuleId)
    * description : Delete the existing record  for aRuleId
    * @param : String aPrdGrpId,aLocGrpId
    * @return : boolean - true / false
    * @throws Exception
    */
    
     public boolean deleteLocAssignRules(java.util.Hashtable ht) throws Exception
     {
    MLogger.log(1, this.getClass() + " LocAssignRules()");
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
          MLogger.log(0,"######################### Exception :: LocAssignRules() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: LocAssignRules() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
    }
    finally{
        if (con != null) {
            DbBean.closeConnection(con);
    }
   }
    MLogger.log(-1, this.getClass() + " LocAssignRules()");

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

   

  
     public boolean updateLocAssignRule(Hashtable htUpdate,Hashtable htCondition) throws Exception
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

  public boolean updateLocAssignRule(String query, Hashtable htCondition, String extCond) throws Exception 
    {
   MLogger.log(1, this.getClass() + " UpdateLocAssignRule()");
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
          MLogger.log(0,"######################### Exception :: UpdateLocAssignRule() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: UpdateLocAssignRule() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
        }
        finally{
        if (con != null) {
           DbBean.closeConnection(con);
        }
   }
   return flag;

 }
 
 
  
  public ArrayList selectLocAssignRules(String query,Hashtable ht,String extCondi) throws Exception {
   MLogger.log(1, this.getClass() + " selectLocAssignRules()");
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
      MLogger.log(0,"######################### Exception :: selectLocAssignRules() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: selectLocAssignRules() : ######################### \n");
      MLogger.log(-1,"");
      throw e;
    } finally{
      if (con != null) {
         DbBean.closeConnection(con);
      }
   }
   MLogger.log( -1, this.getClass() + " selectLocAssignRules()");

   return alData;
 }
 
    public ArrayList getRulesIdStartsWith(String aRuleId ) throws Exception {          
   
   Connection con=null;
   ArrayList al=new ArrayList();
   try
   {
   con=DbBean.getConnection();

   boolean flag=false;
   //query
    String sQry = " SELECT  "+ MDbConstant.RULE_ID + "," + MDbConstant.RULE_DESC + "," + MDbConstant.PRD_GRP_ID + "," + MDbConstant.LOC_ID + "  FROM "+ TABLE_NAME+" WHERE "+ MDbConstant.RULE_ID+" LIKE '"+aRuleId+"%'";
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




 public ArrayList getLocAssignedRules( String ruleId) throws Exception {

// connection
   java.sql.Connection con=null;
   ArrayList al=new ArrayList();

   try{
    con=com.murho.gates.DbBean.getConnection();
   //query
   StringBuffer sql = new StringBuffer(" SELECT ");
   sql.append(MDbConstant.RULE_ID + "," + MDbConstant.RULE_DESC + "," + MDbConstant.PRD_GRP_ID + "," );
   sql.append( MDbConstant.LOC_ID);
   sql.append(" ");
   sql.append(" FROM " + TABLE_NAME );
  
   sql.append(" AND RulesId = '" + ruleId + "' ");
 
  MLogger.query(sql.toString());

   al = selectData(con,sql.toString());

   }catch(Exception e)
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
  
}