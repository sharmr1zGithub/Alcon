package com.murho.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.gates.DbBean;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;



public class MovHisDAO
    extends BaseDAO {

  public static final String TABLE_NAME = "MOVHIS";

  public MovHisDAO() {
  }
 
   public boolean insertIntoMovHis(Hashtable ht)  throws Exception{
    boolean insertRecvHis = false;
    java.sql.Connection con=null;
    try{

      con=DbBean.getConnection();
      String FIELDS = "", VALUES = "";
      Enumeration enum1 = ht.keys();
      for (int i = 0; i < ht.size(); i++) {
          String key   = StrUtils.fString((String) enum1.nextElement());
          String value = StrUtils.fString((String)ht.get(key));
          FIELDS += key.toUpperCase() + ",";
          VALUES += "'" + value + "',";
        }
        String sql = "INSERT INTO "+TABLE_NAME+" ("+FIELDS.substring(0,FIELDS.length()-1)+") VALUES ("+VALUES.substring(0,VALUES.length() -1) +")";

        MLogger.query(" "+sql.toString());

        insertRecvHis = insertData(con,sql.toString());

    }
    catch(Exception e)
    {
      MLogger.log(0,"######################### Exception :: insertIntoMovHis() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: insertIntoMovHis() : ######################### \n");
      MLogger.log(-1,"");
      throw e;
   }
   finally
   {
      if (con != null) {
        DbBean.closeConnection(con);
      }
   }

    return insertRecvHis;
  }
 

  public boolean updateMovhis(String query,Hashtable map,String extCond) throws Exception {
  MLogger.log(1, this.getClass() + " updateMovhis()");
  boolean flag = false;
  java.sql.Connection con=null;
  try
  {
    con = DbBean.getConnection();
    StringBuffer sql = new StringBuffer(" UPDATE " + TABLE_NAME);
    sql.append(" ");
    sql.append(query);
    sql.append(" WHERE ");
    String conditon=formCondition(map);
    sql.append(conditon);
    if(extCond.length()!=0){
    sql.append(extCond);
    }
     MLogger.query(" "+sql.toString());
    flag = updateData(con,sql.toString());
  }
  catch(Exception e)
  {
      MLogger.log(0,"######################### Exception :: updateMovhis() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: updateMovhis() : ######################### \n");
      MLogger.log(-1,"");
      throw e;
   }
   finally
   {
      if (con != null) {
        DbBean.closeConnection(con);
      }
   }//
   MLogger.log( -1, this.getClass() + " updateMovhis()");
   return flag;
       }

    
   public ArrayList selectForReport(String query,Hashtable ht) throws Exception {
    MLogger.log(1, this.getClass() + " selectForReport()");
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
          conditon=formCondition(ht);
          sql.append( " " + conditon);
       }

          MLogger.log(0, "Sql : " + sql.toString());
          al=selectData(con,sql.toString());
       }
       catch(Exception e)
        {
          MLogger.log(0,"######################### Exception :: selectForReport() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
        MLogger.log(0,"######################### Exception :: selectForReport() : ######################### \n");
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
  

public boolean RecordRejectDetail(String plant,String TRID,String MTID,String SKU,String LOT,String QTY,String Movmt,String user,String crat) throws Exception {
    MLogger.log(1, this.getClass() + " RecordRejectDetail()");
    boolean flag = true;
    double quantity = 0.0;
   
    java.sql.Connection con=null;

    try
    {
    // connection
    con=DbBean.getConnection();

   String sql="";
   sql = "insert into movhis(plant,lnno,pono,item,qty,batno,movtid,crby,crat)values('"+plant+"','"+MTID+"','"+TRID+"','"+SKU+"',CAST('"+QTY+"' AS DECIMAL),'"+LOT+"','"+Movmt+"','"+user+"','"+crat+"')";
    //query
    MLogger.query(" "+sql);
    flag= insertData(con,sql);
    }

    catch(Exception e)
    {
      MLogger.log(0,"######################### Exception :: insertData() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: insertData() : ######################### \n");
      MLogger.log(-1,"");
      throw e;
   }
   finally
   {
      if (con != null) {
       DbBean.closeConnection(con);
      }
   }//
    MLogger.log( -1, this.getClass() + " insertData()");

    return flag;

  }

    public String getMOVTID() throws Exception {

          String selectStr="";
          Connection con = null;
          
          con=DbBean.getConnection();
          try{

            Statement stmt = con.createStatement();
            String q;
            q = "select distinct MOVTID from MOVHIS";
            ResultSet rs   = stmt.executeQuery(q);

            if(rs!=null)
            {
              String val;
              while(rs.next())
              {
                val=rs.getString("MOVTID").trim();
                selectStr+="<option value='"+val+"'>"+val+"</option>";
              }
            }
          } 
          catch (Exception e)
          {
            DbBean.writeError("getMOVTID","getMOVTID()",e);
          }
          finally
          {
            DbBean.closeConnection(con);
          }
          return selectStr;
        }
    
    //Added for 6.0 enhancement

    public ArrayList LotRestriction(String lot) throws Exception {
   	 MLogger.log(1,"call TrayDescrepencyReport");
 	
	 	ArrayList invQryList = new ArrayList();
		HashMap map = null;
		Connection con = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		
		try {
			con = DbBean.getConnection();
			
			cs = con.prepareCall("{call dbo.[PROC_LOTRESTRICTIONMOVMENT](?)}");
			cs.setString(1, lot);

			rs = cs.executeQuery();
			while (rs.next())
			{	
				map = new HashMap();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) 
				{	
					map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
				}
				invQryList.add(map);				
			}
			MLogger.log(1, " size of the map" + map.size());
			
		} catch (Exception e) {
			MLogger.log(1, " LotRestriction Fail....." );
		}finally{
		    if (rs != null) {
		        rs.close();
		     }
		     if (con != null) {
		        DbBean.closeConnection(con);
		      }
		     }
		return invQryList;
    }
}//end of class