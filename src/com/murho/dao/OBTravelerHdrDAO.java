
package com.murho.dao;

import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.gates.DbBean;
import com.murho.utils.MLogger;
import java.sql.ResultSet;



public class OBTravelerHdrDAO
    extends BaseDAO {
  public OBTravelerHdrDAO() {
  }

  public static final String TABLE_NAME = "OB_TRAVEL_HDR";
    
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
   
   MLogger.query(query.toString());

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
    } //

    MLogger.log( -1, this.getClass() + " update()");

    return flag;

  }

  public boolean isExist(Hashtable ht) throws Exception {
    MLogger.log(1, this.getClass() + " isExisit()");
    boolean flag = false;
    java.sql.Connection con = null;

    try {
      // connection
      con = com.murho.gates.DbBean.getConnection();
      //query
      StringBuffer sql = new StringBuffer(" SELECT ");
      sql.append("COUNT(*) ");
      sql.append(" ");
      sql.append(" FROM " + TABLE_NAME);
      sql.append(" WHERE  " + formCondition(ht));
     
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

   public ArrayList getReferenceList() throws Exception {
   MLogger.log(1, this.getClass() + " getReferenceList()");
   ArrayList al=new ArrayList();
// connection
   java.sql.Connection con=null;
   try
   {
    con=com.murho.gates.DbBean.getConnection();
   //query
   StringBuffer sql = new StringBuffer(" SELECT distinct  ");
   sql.append(" RefNo ");
   sql.append(" ");
   sql.append(" FROM " + TABLE_NAME + " order by refno desc " );
  // sql.append(" WHERE  plant = '" + company + "' ");
  
   MLogger.query(sql.toString());
       
   al=selectData(con,sql.toString());
   
   }
   catch(Exception e) {
           MLogger.exception(this,e);
           throw e;
        }
        finally{
        if (con != null) {
          DbBean.closeConnection(con);
        }
   }
   MLogger.log( -1, this.getClass() + " getReferenceList()");
   return al;

 }

   
   /*
   CREATED BY: RANJANA SHARMA
   CREATED ON: 3/2/2017
   PURPOSE: TO SHOW TRAVELER ON TRAY LABELLING SCREEN
   */  
   public ArrayList getTravelerListForRefNo(String refNum) throws Exception {
   MLogger.log(1, this.getClass() + " getTravelerListForRefNo()");
   ArrayList al = new ArrayList();
   ResultSet rs = null;
   Map map = null;
   
   // connection
   java.sql.Connection con=null;
   try
   {
    con=com.murho.gates.DbBean.getConnection();
   //query
  /* StringBuffer sql = new StringBuffer(" SELECT ");
	 * sql.append(" distinct traveler_id ,isnull(Traveler_Desc,'') as TRAVDESC
	 * "); sql.append(" "); sql.append(" FROM " + TABLE_NAME ); sql.append("
	 * WHERE REFNO = '" + refNum + "' ");
	 
   
   sql.append(" Traveler_Id as Traveler ");
   sql.append(" from " + TABLE_NAME + " ");  
   sql.append(" WHERE Status='N' ORDER BY CRAT DESC ");*/
    
    con = DbBean.getConnection();
	CallableStatement cs = null;
	
	cs = con
			.prepareCall("{call dbo.[PROC_TRAYLABELLING](?)}");
	cs.setString(1, refNum);
	rs = cs.executeQuery();
	
	MLogger.log(1, " size of column " + rs.getRow());
	
	while (rs.next()) {
		map = new HashMap();
		for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
			map
					.put(rs.getMetaData().getColumnLabel(i), rs
							.getString(i));
		}
		al.add(map);
	}
	MLogger.log(1, " size of map "+ map.size());

} catch (Exception e) {
	MLogger.log(1, "PROC_TRAYLABELLING Fail.....");
} finally {
	if(rs!=null){
		rs.close();
	}
	if (con != null) {
		DbBean.closeConnection(con);
	}
}
   MLogger.log( -1, this.getClass() + " getTravelerListForRefNo()");
   return al;

 } 

   
  public ArrayList getTravelerList() throws Exception {
   MLogger.log(1, this.getClass() + " getTravelerList()");
   ArrayList al=new ArrayList();
   // connection
   java.sql.Connection con=null;
   try
   {
    con=com.murho.gates.DbBean.getConnection();
   //query
   StringBuffer sql = new StringBuffer(" SELECT ");
   sql.append(" Traveler_Id,isnull(Traveler_Desc,'') as TRAVDESC ");
   sql.append(" ");
   sql.append(" FROM " + TABLE_NAME + " order by Traveler_Id desc  ");  
   MLogger.query(sql.toString());
       
   al=selectData(con,sql.toString());
   
   }
   catch(Exception e) {
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
 
 public ArrayList getPalletListForTraveler() throws Exception {
   ArrayList al=new ArrayList();
// connection
   java.sql.Connection con=null;
   try
   {
    con=com.murho.gates.DbBean.getConnection();
   //query
   StringBuffer sql = new StringBuffer(" SELECT ");
   sql.append(" Traveler_Id,isnull(Traveler_Desc,'') ");
   sql.append(" ");
   sql.append(" FROM " + TABLE_NAME );
          
   al=selectData(con,sql.toString());
   
   }
   catch(Exception e) {
           MLogger.exception(this,e);
          throw e;
    }
   finally{
        if (con != null) {
          DbBean.closeConnection(con);
        }
   }
   MLogger.log( -1, this.getClass() + " getPalletListForTraveler()");
   return al;

 } 

    public Map getShipTo(String Traveller) {
    MLogger.log(1, this.getClass() + " getShipTo()");
    Map m = new HashMap();
    try {
      Hashtable ht = new Hashtable();
      ht.put("Traveler_id",Traveller);      

      String sql =" COUNTRY  AS SHIPTO " ;
       
      m = selectRow(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getShipTo :: getShipTo:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getShipTo()");
    return m;
  }
 
  
 
}
