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


public class ItemMstDAO extends BaseDAO {

 public static final String TABLE_NAME="ItemMst";
 
 //Added by Ranjana to implement the functionality of System Block 
 public static final String TABLE_NAME1="TBL_BLOCK_LOT";
 
 //Added by Ranjana to implement the functionality of System Block 
 public static final String TABLE_NAME2="TBL_DSL_STATUS";
 Connection con = null;

  public ItemMstDAO() {
  }
  
  // Added by Ranjana to implement the functionality of DSL
  public Map selectcountryRow(String query,Hashtable ht) throws Exception {
	   Map resultMap=null;
	   java.sql.Connection con=null;

	   try{
	   con=com.murho.gates.DbBean.getConnection();
	   StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + TABLE_NAME2);
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
	   }//
	 //  MLogger.log( -1, this.getClass() + " select()");
	   return resultMap;
	 }

  // Added by Ranjana to implement the functionality of System Block 
  public Map selectdestRow(String query,Hashtable ht) throws Exception {
	   boolean flag = false;
	   Map resultMap=null;
	   java.sql.Connection con=null;

	   try{
	   con=com.murho.gates.DbBean.getConnection();
	   StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + TABLE_NAME1);
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
	   }//
	 //  MLogger.log( -1, this.getClass() + " select()");
	   return resultMap;
	 }
  
  
   public Map selectRow(String query,Hashtable ht) throws Exception {
  // MLogger.log(1, this.getClass() + " select()");
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
   }//
 //  MLogger.log( -1, this.getClass() + " select()");
   return resultMap;

 }

  public ArrayList select(String query,Hashtable ht) throws Exception {
   MLogger.log(1, this.getClass() + " select()");
   boolean flag = false;
  ArrayList resultArrayList = new ArrayList();
   // connection
   java.sql.Connection con=null;
  try{
   con=com.murho.gates.DbBean.getConnection();
   StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + TABLE_NAME);
   String conditon="";
   if(ht.size()>0)
   {
      sql.append(" WHERE ");
      conditon=formCondition(ht);
      sql.append(conditon);
   }

   MLogger.query(" "+sql.toString());
   resultArrayList=selectData(con,sql.toString());

   } catch(Exception e)
   {
      MLogger.log(0,"######################### Exception :: select() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: select() : ######################### \n");
      MLogger.log(-1,"");
      throw e;
   }
   finally
   {
      if (con != null) {
        DbBean.closeConnection(con);
      }
   }
   MLogger.log( -1, this.getClass() + " select()");
   return resultArrayList;

 }


 public String getItemDescription(String company,String item) throws Exception
{
  String itemDesc="";
 // MLogger.log(1, this.getClass() + " getItemDescription()");
  try
  {
  String query=" ITEMDESC ";
  Hashtable ht=new Hashtable();
  ht.put(MDbConstant.COMPANY,company);
  ht.put(MDbConstant.MATERIAL_CODE,item);
  Map m=selectRow(query,ht);
  itemDesc=(String)m.get("ITEMDESC");
  
  System.out.println("ItemDesc........."+ itemDesc);
   
  if(itemDesc==null || itemDesc.length()==0)
   {
    throw new Exception("Details not found for Material : "  + item );
   }
  }
  catch(Exception e)
  {
     MLogger.log(0,"######################### Exception :: getItemDescription() : ######################### \n");
     MLogger.log(0,""+ e.getMessage());
     MLogger.log(0,"######################### Exception :: getItemDescription() : ######################### \n");
   //  MLogger.log(-1,"");
     throw e;
  }
 // MLogger.log(-1, this.getClass() + " getItemDescription()");
  return itemDesc;
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

 public boolean insertIntoItemMst(Hashtable ht) throws Exception
   {
     MLogger.log(1, this.getClass() + " insertIntoItemMst()");

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
          MLogger.log(0,"######################### Exception :: insertIntoItemMst() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: insertIntoItemMst() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
         }
         finally{
          if (con != null) {
          DbBean.closeConnection(con);
          }
         }//
          MLogger.log(-1, this.getClass() + " insertIntoItemMst()");
          return inserted;
     }
     
       public boolean deleteItemId(java.util.Hashtable ht) throws Exception
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
   }//
    MLogger.log(-1, this.getClass() + " deleteItemId()");

    return delete;
  }
 
    public boolean updateItemId(Hashtable htUpdate,Hashtable htCondition) throws Exception
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
     
      public ArrayList getItemDetails(String aItemId) throws Exception {
      PreparedStatement ps = null;
      ResultSet rs         = null;
      ArrayList arrItemDet   = new ArrayList();
      try{
         con = DbBean.getConnection();
         String sQry = "SELECT ITEM,ITEMDESC,PRD_CLS_ID FROM "+TABLE_NAME+" WHERE ITEM = '"+aItemId+"'";
         ps = con.prepareStatement(sQry);
         rs   =  ps.executeQuery();
         while(rs.next()){
           arrItemDet.add(0,strUtils.fString((String)rs.getString(1))); 
           arrItemDet.add(1,strUtils.fString((String)rs.getString(2))); 
           arrItemDet.add(2,strUtils.fString((String)rs.getString(3))); 
        }
     }catch(Exception e) {  throw e; }
        finally
        {
           DbBean.closeConnection(con, ps);
        }
        return arrItemDet;
     }
     
   public ArrayList getItemIdStartsWith(String aItemId ) throws Exception {          
   
   Connection con=null;
   ArrayList al=new ArrayList();
   try
   {
   con=DbBean.getConnection();

   boolean flag=false;
   /* String sQry = "SELECT ITEM,ITEMDESC,PRD_ClS_ID  AS CLASSID,isnull(STKUOM,'') as STKUOM,ISNULL(REFNO,'') AS REFNO FROM "+ TABLE_NAME+" WHERE ITEM LIKE '"+aItemId+"%'";
   
   
     Added By Ranjana Sharma on 22/05/2015 WO0000000471852 -UDI Part 2 Implementation*/
   
    String sQry = "SELECT ITEM,ITEMDESC,PRD_ClS_ID  AS CLASSID,isnull(STKUOM,'') as STKUOM,ISNULL(REFNO,'') AS REFNO,ISNULL(GTIN2,'') AS GTIN2 FROM "+ TABLE_NAME+" WHERE ITEM LIKE '"+aItemId+"%'";
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


  public boolean isValidUserExists(String userid) throws Exception {

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
   sql.append(" FROM  USER_INFO " );
   sql.append(" WHERE  USER_ID = '"+userid +"'  " );

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
  public String getUPCCode(String company,String item) throws Exception
  
  //This is for TrayLabeling getUPC_CODe
{
  String upccode="";
 // MLogger.log(1, this.getClass() + " getExpiryDateForLot()");
  try
  {
  String query=" DISTINCT REFNO AS UPCCODE ";
  Hashtable ht=new Hashtable();
  ht.put(MDbConstant.COMPANY,company);
  ht.put("ITEM",item);
  Map m=selectRow(query,ht);
  upccode=(String)m.get("REFNO");
  }
  catch(Exception e)
  {
     MLogger.log(0,"######################### Exception ::ItemMstDao: getUPCCode from Itmmst() : ######################### \n");
     MLogger.log(0,""+ e.getMessage());
     MLogger.log(0,"######################### Exception ::ItemMstDao: getUPCCode from Itmmst(): ######################### \n");
     throw e;
  }
 // MLogger.log(-1, this.getClass() + " getExpiryDateForLot()");
  return upccode;
}
 

}