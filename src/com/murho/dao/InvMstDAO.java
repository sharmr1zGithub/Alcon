package com.murho.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.gates.DbBean;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;

public class InvMstDAO  extends BaseDAO {

   public static final String TABLE_NAME="INVMST";

  public InvMstDAO() {
  }




  public boolean updateInv(String query, Hashtable htCondition, String extCond) throws Exception 
  {
   MLogger.log(1, this.getClass() + " updateInv()");
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
      
      
      System.out.println("Come in1");
      flag = updateData(con,sql.toString());
      System.out.println("Come in2");
    } catch(Exception e) {
          MLogger.log(0,"######################### Exception :: updateInv() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: updateInv() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
        }
        finally{
        if (con != null) {
           DbBean.closeConnection(con);
        }
   }//
   MLogger.log( -1, this.getClass() + " updateInv()");

   return flag;

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

 public boolean deleteItemFrmInvmst(java.util.Hashtable ht) throws Exception
 {
    MLogger.log(1, this.getClass() + " deleteItemFrmInvmst()");
    boolean deleteItemMst = false;
   java.sql.Connection con=null;
   try{
   con=DbBean.getConnection();

   //query
   StringBuffer sql = new StringBuffer(" DELETE ");
   sql.append(" ");
   sql.append(" FROM " + TABLE_NAME );
   sql.append(" WHERE " + formCondition(ht) );

    MLogger.query(sql.toString());
    deleteItemMst=updateData(con,sql.toString());
   }
   catch(Exception e) {
          MLogger.log(0,"######################### Exception :: deleteItemFrmInvmst() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: deleteItemFrmInvmst() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
    }
    finally{
        if (con != null) {
            DbBean.closeConnection(con);
    }
   }
    MLogger.log(-1, this.getClass() + " deleteItemFrmInvmst()");

    return deleteItemMst;
  }

   public boolean insertIntoInvMst(Hashtable ht) throws Exception
   {
     MLogger.log(1, this.getClass() + " insertIntoInvMst()");

          boolean insertedInv      = false;
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

           insertedInv=insertData(con,query);

          }catch(Exception e) {
          MLogger.log(0,"######################### Exception :: insertIntoInvMst() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: insertIntoInvMst() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
         }
         finally{
          if (con != null) {
          DbBean.closeConnection(con);
          }
         }
          MLogger.log(-1, this.getClass() + " insertIntoInvMst()");
          return insertedInv;
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
   }//
   MLogger.log( -1, this.getClass() + " select()");
   return resultArrayList;

 }

   
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
   
   MLogger.query(sql.toString());

   map = getRowOfData(con,sql.toString());
   
    }catch(Exception e)
    {
          MLogger.log(0,"######################### Exception :: select() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: select() : ######################### \n");
          MLogger.log(-1,"");
          throw new Exception(this.getClass().getName() + " :: select() : " + e.getMessage());
    }
    finally{
      if (con != null) {
           DbBean.closeConnection(con);
      }
    }

    MLogger.log( -1, this.getClass() + " select()");
    return map;
 }


   public Map selectRow(String query,Hashtable ht,String condition) throws Exception {
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
  
 public boolean isExisit(Hashtable ht,String extraCon) throws Exception {

 boolean flag=false;

  // connection
  java.sql.Connection con=null;

  try{
  con=com.murho.gates.DbBean.getConnection();
  StringBuffer sql = new StringBuffer(" SELECT ");
  sql.append(" 1 ");
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

public boolean insertTravellerDetail(String plant,String trID,String trandate,String userID) throws Exception {
       MLogger.log(1, this.getClass() + " insertTravellerDetail()");
       boolean flag = false;
       ArrayList al= new ArrayList();
       // connection
        int iCnt  = 0;
        boolean flg               = false;
        String str = "";
       java.sql.Connection con=null;
        CallableStatement colStmt = null;
        PreparedStatement statement = null;
       try{
       con=com.murho.gates.DbBean.getConnection();
   
        String sp = " EXEC Proc_TravellerApprove '"+plant+"' ,'"+trID+"','"+trandate+"','"+userID+"' ";
        colStmt   = con.prepareCall(sp);
        
         iCnt = colStmt.executeUpdate();
         MLogger.log(0, "DownLoadShipment : " + iCnt);
         
      if(iCnt > 0){
      MLogger.log(0, "PO Downloaded Successfully!");
      flg = true;
      
    }
    else
    {
      throw new Exception("Unable to download");
    }
      
       }
       
       
       catch(Exception e)
        {
          MLogger.log(0,"######################### Exception :: insertTravellerDetail() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: insertTravellerDetail() : ######################### \n");
          MLogger.log(-1, "");
          throw e;
        }
        
    finally{
      if (con != null) {
       DbBean.closeConnection(con);
      }
    }
  MLogger.log(-1, this.getClass() + " insertTravellerDetail()");
 return flg;
  }
  
   public ArrayList selectForReport(String query,Hashtable ht) throws Exception {
       MLogger.log(1, this.getClass() + " selectForReport--()");
       boolean flag = false;
       ArrayList al= new ArrayList();
       // connection
       java.sql.Connection con=null;
       try{
      
       StringBuffer sql = new StringBuffer(  query );
       String conditon="";
       if(ht.size()>0)
       {
          sql.append(" AND ");
          conditon=formConditionLike(ht);
          sql.append( " " + conditon);
       }

         MLogger.query(" "+sql.toString());
         con=com.murho.gates.DbBean.getConnection();
         al=selectData(con,sql.toString());
       }
       catch(Exception e)
        {
         
          MLogger.log(0,""+ e.getMessage());
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


  public ArrayList selectForReportCond(String query,Hashtable ht,String extCond) throws Exception {
       MLogger.log(1, this.getClass() + " selectForReportCond()");
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

         if (extCond.length() != 0) {
     
        sql.append(extCond);
      }
         MLogger.query(" "+sql.toString());
          al=selectData(con,sql.toString());
       }
       catch(Exception e)
        {
          MLogger.log(0,"######################### Exception :: selectForReportCond() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: selectForReportCond() : ######################### \n");
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
 public boolean UpdatePutAwayDetail(String plant,String trID,String trandate,String userID) throws Exception {
       MLogger.log(1, this.getClass() + " UpdatePutAwayDetail()");
       boolean flag = false;
       ArrayList al= new ArrayList();
       // connection
        int iCnt  = 0;
        boolean flg               = false;
        String str = "";
       java.sql.Connection con=null;
        CallableStatement colStmt = null;
        PreparedStatement statement = null;
       try{
       con=com.murho.gates.DbBean.getConnection();
   
        String sp = " EXEC Proc_PutAwayApprove '"+plant+"' ,'"+trID+"','"+trandate+"','"+userID+"' ";
        colStmt   = con.prepareCall(sp);
        
         iCnt = colStmt.executeUpdate();
         MLogger.log(0, "UpdatePutAwayDetail : " + iCnt);
         
      if(iCnt > 0){
      MLogger.log(0, " UpdatePutAwayDetail !");
      flg = true;
      
    }
    else
    {
      throw new Exception("Unable to download");
    }
      
       }
       
       
       catch(Exception e)
        {
          MLogger.log(0,"######################### Exception :: insertTravellerDetail() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: insertTravellerDetail() : ######################### \n");
          MLogger.log(-1, "");
          throw e;
        }
        
    finally{
      if (con != null) {
       DbBean.closeConnection(con);
      }
    }
  MLogger.log(-1, this.getClass() + " insertTravellerDetail()");
 return flg;
  }

 public boolean update(String query, Hashtable map, String extCond) throws
      Exception {
    MLogger.log(1, this.getClass() + " update()");
    boolean flag = false;
    java.sql.Connection con = null;
    try {
      // connection
      con = com.murho.gates.DbBean.getConnection();

      //inputs

      StringBuffer sql = new StringBuffer(" UPDATE " + TABLE_NAME);
      sql.append(" ");
      sql.append(query);
      sql.append(" WHERE ");

      String conditon = formCondition(map);
      // conditon=conditon + " and " +  MDbConstant.TRAN_QTY +" >= " + (String) map.get(SConstant.ISSUE_QTY);
      //query

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
  
  public boolean updateInventory(String mtid,String item,String qty) throws Exception {
    MLogger.log(1, this.getClass() + " updateInventory()");
    boolean flag = false;
   java.sql.Connection con = null;
    try {
      // connection
      con = DbBean.getConnection();
   

   
      StringBuffer sql = new StringBuffer(" UPDATE INVMST SET qty = '"+qty+"'  WHERE MTID='"+mtid+"'  AND ITEM='"+item+"' ");
      
      flag = updateData(con, sql.toString());
    }
    catch (Exception e) {
      MLogger.log(0, "######################### Exception :: updatePutAwayLineDet() : ######################### \n");
      MLogger.log(0, "" + e.getMessage());
      MLogger.log(0, "######################### Exception :: updatePutAwayLineDet() : ######################### \n");
      MLogger.log( -1, "");
      throw e;
    }
    finally {
      if (con != null) {
          DbBean.closeConnection(con);
      }
    } 
    MLogger.log( -1, this.getClass() + " updateInventory()");

    return flag;

  } 
  
  //CCODE
  
  public ArrayList getDistinctLotInINV(String lot ) throws Exception {          
   
   java.sql.Connection con = null;
   ArrayList al=new ArrayList();
   try
   {
   con=DbBean.getConnection();


   boolean flag=false;
   //query
    String sQry = "SELECT DISTINCT LOT FROM "+ TABLE_NAME+" WHERE LOT LIKE '"+lot+"%' AND QTY>0 ";
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
 
 //CCODE
 
 
  public ArrayList getDistinctSKUInINV(String sku ) throws Exception {          
   
   java.sql.Connection con = null;
   ArrayList al=new ArrayList();
   try
   {
   con=DbBean.getConnection();


   boolean flag=false;
   //query
    String sQry = "SELECT DISTINCT SKU FROM "+ TABLE_NAME+" WHERE SKU LIKE '"+sku+"%' AND QTY>0";
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
 
 
 // method Added to retrive Expiry Date from Invmst 
 // By Samatha on Jan 18 2010
 public String getExpiryDateForLot(String company,String item,String lot) throws Exception
{
  String expDate="";
 // MLogger.log(1, this.getClass() + " getExpiryDateForLot()");
  try
  {
  String query=" DISTINCT USERFLD1 AS EXPDATE ";
  Hashtable ht=new Hashtable();
  ht.put(MDbConstant.COMPANY,company);
  ht.put(MDbConstant.SKU,item);
  ht.put(MDbConstant.LOT,lot);
  Map m=selectRow(query,ht);
  expDate=(String)m.get("EXPDATE");
  }
  catch(Exception e)
  {
     MLogger.log(0,"######################### Exception :: getExpiryDateForLot from Invmst() : ######################### \n");
     MLogger.log(0,""+ e.getMessage());
     MLogger.log(0,"######################### Exception :: getExpiryDateForLot from Invmst () : ######################### \n");
     throw e;
  }
 // MLogger.log(-1, this.getClass() + " getExpiryDateForLot()");
  return expDate;
}
//Below function is added on 7-Apr-2014 for UDI implementation.
 public String getTypeofProduct(String MTID) throws Exception
 {
   String prdType="";
   try
   {
   String query=" Product_2D ";
   Hashtable ht=new Hashtable();
   ht.put("MTID",MTID);
   Map m=selectRow(query,ht);
   prdType=(String)m.get("Product_2D");
   }
   catch(Exception e)
   {
      MLogger.log(0,"######################### Exception :: getTypeofProduct from Invmst() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: getTypeofProduct from Invmst () : ######################### \n");
      throw e;
   }
 
   return prdType;
 }


public ArrayList Load_Inv_Details(String LOC, String MTID, String LOT) throws SQLException {
	
	ArrayList mtlList = null;
	MLogger.log(1, " Load_Inv_Details ");
		
	Map map = null;
	Connection con = null;
	ResultSet rs = null;
	CallableStatement cs = null;
	
	try {
		con = DbBean.getConnection();
		
		cs = con.prepareCall("{call dbo.[PROC_QUERY_INVENTORY](?,?,?)}");
		cs.setString(1, LOC);
		cs.setString(2, MTID);
		cs.setString(3, LOT);
	
		rs = cs.executeQuery();
	
		MLogger.log(1, " size of column " + rs.getRow());
		
		while (rs.next())
		{	
			map = new HashMap();
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) 
			{	
				map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
			}
			mtlList.add(map);				
		}
		MLogger.log(1, " size of the map" + map.size());
	
	} catch (Exception e) {
		MLogger.log(1, " PROC_QUERY_INVENTORY Fail.....");
	} finally {
		if(rs!=null){
			rs.close();
		}
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return mtlList;	
}

}//end of class