  
 package com.murho.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.gates.DbBean;
import com.murho.gates.TableList;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;



public class CustMstDAO  extends BaseDAO{
  //StrUtils strUtils = new StrUtils();
  private String TABLE_NAME = "CUSTMST";
  Connection con = null;

    public CustMstDAO() {
    }
      
    public boolean insertCustMst(Hashtable ht) throws Exception
    {
           MLogger.log(1, this.getClass() + " insertCustMst()");
           boolean inserted      = false;
           java.sql.Connection con=null;
//         Added by Arun f0r #1848
           TableList list = new TableList();
           try{
               con=DbBean.getConnection();
               String FIELDS = "", VALUES = "";
               Enumeration enum1 = ht.keys();
                 for (int i = 0; i < ht.size(); i++) {
                   String key   = StrUtils.fString((String) enum1.nextElement());
                   String value = StrUtils.fString((String)ht.get(key));
                   //Below line added by Arun for #1848 on 20 Oct 2011 for accepting single quotes
                   String formattedValue = list.formatString(value);
                   FIELDS += key + ",";
                   //VALUES += "'" + value + "',";
                   VALUES += "'" + formattedValue + "',";
                 }
                String query = "INSERT INTO " + TABLE_NAME + "("+FIELDS.substring(0,FIELDS.length()-1)+") VALUES ("+VALUES.substring(0,VALUES.length() -1) +")";
                MLogger.query(query);
                inserted=insertData(con,query);
      
                }catch(Exception e) {
                  MLogger.log(0,"######################### Exception :: insertCustMst() : ######################### \n");
                  MLogger.log(0,""+ e.getMessage());
                  MLogger.log(0,"######################### Exception :: insertCustMst() : ######################### \n");
                  MLogger.log(-1,"");
                  throw e;
               }
               finally{
                  if (con != null) {
                  DbBean.closeConnection(con);
                  }
               }
                MLogger.log(-1, this.getClass() + " insertCustMst()");
                return inserted;
     }
     
      
  
     public boolean deleteCustMst(java.util.Hashtable ht) throws Exception
     {
          MLogger.log(1, this.getClass() + " deleteCustMst()");
          boolean deleteLocGrp = false;
          java.sql.Connection con=null;
          try{
               con=DbBean.getConnection();
               StringBuffer sql = new StringBuffer(" DELETE ");
               sql.append(" ");
               sql.append(" FROM " + TABLE_NAME );
               sql.append(" WHERE " + formCondition(ht) );
            
                MLogger.query(sql.toString());
                deleteLocGrp=updateData(con,sql.toString());
          }
          catch(Exception e) {
                MLogger.log(0,"######################### Exception :: deleteCustMst() : ######################### \n");
                MLogger.log(0,""+ e.getMessage());
                MLogger.log(0,"######################### Exception :: deleteCustMst() : ######################### \n");
                MLogger.log(-1,"");
                throw e;
           }
           finally{
              if (con != null) {
                  DbBean.closeConnection(con);
           }
          }
          MLogger.log(-1, this.getClass() + " deleteCustMst()");
      
          return deleteLocGrp;
     }
    
  
     public boolean isExists(Hashtable ht) throws Exception {
          boolean flag=false;
          java.sql.Connection con=null;
          try
            {
                 con=com.murho.gates.DbBean.getConnection();
                 StringBuffer sql = new StringBuffer(" SELECT ");
                 sql.append(" 1 ");
                 sql.append(" ");
                 sql.append(" FROM " + TABLE_NAME );
                 sql.append(" WHERE  " + formCondition(ht));
                 MLogger.query(sql.toString());
              
                  flag= isExists(con,sql.toString());
            
           }catch(Exception e) {
                  MLogger.log(0,"######################### Exception :: CustMstDAO :: isExisit() : ######################### \n");
                  MLogger.log(0,""+ e.getMessage());
                  MLogger.log(0,"######################### Exception :: CustMstDAO :: isExisit() : ######################### \n");
                  throw e;
            }
            finally{
                  if (con != null) {
                     DbBean.closeConnection(con);
              }
           }
         return flag;
    }
  
   
     public boolean updateCustMst(Hashtable htUpdate,Hashtable htCondition) throws Exception
         {
           boolean update      = false;
           PreparedStatement ps = null;
           //Added by Arun f0r #1848
           TableList list = new TableList();
    
           try{
             con = DbBean.getConnection();
             String sUpdate = " ", sCondition = " ";
             // generate the condition string
             Enumeration enumUpdate = htUpdate.keys();
             for (int i = 0; i < htUpdate.size(); i++) {
                  String key   = StrUtils.fString((String) enumUpdate.nextElement());
                  String value =  StrUtils.fString((String)htUpdate.get(key));
//                Below line added by Arun for #1848 on 20 Oct 2011 for accepting single quotes
                  String formattedValue = list.formatString(value);
                  //sUpdate += key.toUpperCase() +" = '" + value + "',";
                  sUpdate += key.toUpperCase() +" = '" + formattedValue + "',";
             }
             System.out.println("sCondition.."+sUpdate);
             // generate the update string
             Enumeration enumCondition = htCondition.keys();
             for (int i = 0; i < htCondition.size(); i++) {
                 String key   = StrUtils.fString((String) enumCondition.nextElement());
                 String value =  StrUtils.fString((String)htCondition.get(key));
                 sCondition += key.toUpperCase() +" = '" + value.toUpperCase() + "' AND ";
   
             }
            
             
             sUpdate = (sUpdate.length() > 0) ? " SET " + sUpdate.substring(0,sUpdate.length() - 1): "";
             sCondition = (sCondition.length() > 0) ? " WHERE  "+sCondition.substring(0,sCondition.length() - 4): "";
    
             String stmt = "UPDATE "+TABLE_NAME + sUpdate +sCondition;
             //below line Added by Arun for #1848
             System.out.println("Update statement..."+stmt);
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
  
           
    public Map selectRow(String query,Hashtable ht,String condition) throws Exception {
           MLogger.log(1, this.getClass() + " selectRow()");
           Map map=new HashMap();
           java.sql.Connection con=null;
      
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
  
   public Map selectRow(String query,Hashtable ht) throws Exception {
            
             //boolean flag = false;
             Map resultMap=null;
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
            
   public ArrayList getTypeToStartsWith( ) throws Exception {          
             Connection con=null;
             ArrayList al=new ArrayList();
               try
               {
                   con=DbBean.getConnection();
                   //boolean flag=false;
                   String sQry = "SELECT DISTINCT TYPE FROM "+ TABLE_NAME+" ";
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
       
    public Map getAdressDetails(String ShipTo,String Type) {
          MLogger.log(1, this.getClass() + " getAdressDetails()");
          Map m = new HashMap();
            try {
             
                Hashtable ht = new Hashtable();
                ht.put("Ship_To",ShipTo);
                ht.put("Type",Type);
                StringBuffer sql = new StringBuffer("  *  ");  
                MLogger.log(0,"getAdressDetails(aQuery)::"+sql);
                m = selectRow(sql.toString(),ht);
            }
            catch (Exception e) {
                MLogger.log("Exception :getAdressDetails :: getAdressDetails:" + e.toString());
                
            }
            MLogger.log( -1, this.getClass() + " getAdressDetails()");
            return m;
      }

}