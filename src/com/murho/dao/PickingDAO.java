package com.murho.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.gates.DbBean;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;


public class PickingDAO
    extends BaseDAO {
  public PickingDAO() {
  }
  com.murho.utils.XMLUtils xu = null;
  public static final String TABLE_NAME = "SO_PICKING";


  public boolean update(String query, Hashtable map, String extCond) throws
      Exception {
      boolean flag = false;
      java.sql.Connection con = null;
    try {
      // connection
      con = com.murho.gates.DbBean.getConnection();
      StringBuffer sql = new StringBuffer(" UPDATE " + TABLE_NAME);
      sql.append(" ");
      sql.append(query);
      sql.append(" WHERE ");

      String conditon = formCondition(map);
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

  public boolean isExist(Hashtable ht) throws Exception {
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

  public boolean insertIntoPICKING_HIS(Hashtable ht) throws Exception
   {
     MLogger.log(1, this.getClass() + " insertIntoPICKING_HIS()");

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
              String query = "INSERT INTO PICKING_HIS "+ "("+FIELDS.substring(0,FIELDS.length()-1)+") VALUES ("+VALUES.substring(0,VALUES.length() -1) +")";

              MLogger.query(query);

           insertedInv=insertData(con,query);

          }catch(Exception e) {
          MLogger.log(0,"######################### Exception :: insertIntoPICKING_HIS() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: insertIntoPICKING_HIS() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
         }
         finally{
          if (con != null) {
          DbBean.closeConnection(con);
          }
         }
          MLogger.log(-1, this.getClass() + " insertIntoPICKING_HIS()");
          return insertedInv;
     }
     
     
  public String formCondition(Hashtable ht) {
    String sCondition = "";
    Enumeration enum1 = ht.keys();
    for (int i = 0; i < ht.size(); i++) {
      String key = strUtils.fString( (String) enum1.nextElement());
      String value = strUtils.fString( (String) ht.get(key));
      sCondition += key.toUpperCase() + " = '" + value.toUpperCase() + "' AND ";
    }
    sCondition = (sCondition.length() > 0) ?
        sCondition.substring(0, sCondition.length() - 4) : "";
    return sCondition;
  }
  
   
  public boolean updateSoPicking(String query, Hashtable htCondition, String extCond) throws Exception {
   MLogger.log(1, this.getClass() + " updateSoPicking()");
   boolean flag = false;
  java.sql.Connection con=null;
   // connection
   try
   {
      con=com.murho.gates.DbBean.getConnection();

      StringBuffer sql = new StringBuffer(" UPDATE SO_PICKING "  );
      sql.append(" ");
      sql.append(query);
      sql.append(" WHERE  " + formCondition(htCondition));

      if(extCond.length()!=0){
        sql.append(extCond);
      }

      MLogger.query(query.toString());

      flag = updateData(con,sql.toString());
    } catch(Exception e) {
          MLogger.log(0,"######################### Exception :: updateSoPicking() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: updateSoPicking() : ######################### \n");
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

 public boolean updatePicking_His(String query, Hashtable htCondition, String extCond) throws Exception {
   MLogger.log(1, this.getClass() + " updateSoPicking()");
   boolean flag = false;
  java.sql.Connection con=null;
   // connection
   try
   {
      con=com.murho.gates.DbBean.getConnection();
      StringBuffer sql = new StringBuffer(" UPDATE PICKING_HIS "  );
      sql.append(" ");
      sql.append(query);
      sql.append(" WHERE  " + formCondition(htCondition));

      if(extCond.length()!=0){
        sql.append(extCond);
      }

      MLogger.query(query.toString());

      flag = updateData(con,sql.toString());
    } catch(Exception e) {
          MLogger.log(0,"######################### Exception :: updatePicking_His() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: updatePicking_His() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
        }
        finally{
        if (con != null) {
           DbBean.closeConnection(con);
        }
   }//
   MLogger.log( -1, this.getClass() + " updatePicking_His()");

   return flag;

 }
 public String getShelfLocId(String company,String travelNo,String palletNo,String mtid) throws Exception
{
  String SelfLoc ="";
  MLogger.log(1, this.getClass() + " getShelfLocId()");
  try{
  String query=" LOC ";
  
  Hashtable ht=new Hashtable();
  ht.put(MDbConstant.COMPANY,company);
  ht.put("TRAVEL_NO",travelNo);
  ht.put("PALLET_NO",palletNo);
  ht.put("MTID",mtid);
  Map m=selectRow(query,ht);
   SelfLoc = (String)m.get("LOC");
  MLogger.log(0,"SelfLoc : " + SelfLoc);
  }
   catch(Exception e) 
   {
      MLogger.log(0,"######################### Exception :: getShelfLocId() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: getShelfLocId() : ######################### \n");
      MLogger.log(-1,"");
      throw e;
   }
MLogger.log(-1, this.getClass() + " getShelfLocId()");
return SelfLoc;
}

public Map selectRowForInvQty(String query,Hashtable ht) throws Exception {
   MLogger.log(1, this.getClass() + " selectRowForInvQty()");
   Map map=new HashMap();
   java.sql.Connection con=null;

   // connection
   try{
   con=com.murho.gates.DbBean.getConnection();
   StringBuffer sql = new StringBuffer(" SELECT " + query + " from INVMST ");
   sql.append(" WHERE ");
   String conditon=formCondition(ht);
   sql.append(conditon);
   
   MLogger.query(query.toString());

   map = getRowOfData(con,sql.toString());
    }catch(Exception e)
    {
          MLogger.log(0,"######################### Exception :: selectRowForInvQty() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: selectRowForInvQty() : ######################### \n");
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


public Map selectRowForGenTray(String query,Hashtable ht) throws Exception {
   MLogger.log(1, this.getClass() + " select()");
   Map map=new HashMap();
   java.sql.Connection con=null;

   // connection
   try{
   con=com.murho.gates.DbBean.getConnection();
   StringBuffer sql = new StringBuffer(" SELECT " + query + " from PICKING_HIS ");
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

 public ArrayList getTrayLabelSummary(String aPlant,String travelId) {
    MLogger.log(1, this.getClass() + " getTravellerSummary()");
    ArrayList arrList = new ArrayList();
    try {
      InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "travelID" + travelId);
      String aQuery= " SELECT TRAVEL_NO,PALLET_NO,MTID,ITEM,LOT,LOC,ISNULL(GEN_TRAY,'') AS GEN_TRAY,QTY,STATUS,CRBY FROM PICKING_HIS WHERE TRAVEL_NO = '"+travelId+"' AND STATUS <> 'N' " ;
      
      MLogger.log(0,"getTravellerSummary(aQuery)::"+aQuery);
      arrList = invdao.selectForReport(aQuery,ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getTravellerSummary :: getTravellerSummary:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getTravellerSummary()");
    return arrList;
  }
  
   public boolean updateUser(String query, Hashtable map, String extCond) throws
      Exception {
    MLogger.log(1, this.getClass() + " update()");
    boolean flag = false;
    java.sql.Connection con = null;
    try {
      // connection
      con = com.murho.gates.DbBean.getConnection();

      //inputs
       String TABLE = "ob_travel_hdr";
      StringBuffer sql = new StringBuffer(" UPDATE " + TABLE);
      sql.append(" ");
      sql.append(query);
      sql.append(" WHERE ");

      String conditon = formCondition(map);
     
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

   /*  Added by Ranjana 
    *  Purpose: LOT restriction in PDA under ticket WO0000000284867 
    *  for the process of PICKING*/
  
	public Map isPickingLotStatus(Hashtable ht) throws Exception {
		
		ResultSet rs = null;
		CallableStatement cs = null;
		Map map = new HashMap();
		java.sql.Connection con = null;
		
		MLogger.log(1, this.getClass() + "call proc_check_lotrestriction");
		try {
			con = DbBean.getConnection();
			cs = con.prepareCall("{call dbo.[PROC_CHECK_LOTRESTRICTION](?,?,?)}");
			cs.setString(1, (String) ht.get("TRAVELER"));
			cs.setString(2, (String) ht.get("LOT"));
			cs.setString(3,(String) ht.get("TYPE"));

			rs = cs.executeQuery();
					
			while (rs.next()) {		
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					MLogger.log(1, rs.getMetaData().getColumnLabel(i) + " selected KEY ");
					MLogger.log(1, rs.getString(i) + " selected value ");
					map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
				}
			}
		} catch (Exception e) {
			MLogger.log(1, this.getClass() + "call isPickingLotStatus.");
			throw e;
		}finally{
		    if (rs != null) {
		        rs.close();
		     }
		     if (con != null) {
		        DbBean.closeConnection(con);
		      }
		     }
		return map;
	}

}
