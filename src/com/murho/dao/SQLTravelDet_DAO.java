package com.murho.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.DO.TravelDet_DO;
import com.murho.gates.DbBean;
import com.murho.utils.MLogger;

public class SQLTravelDet_DAO implements ITravelDet_DAO
{
  /*public SQLTravelDet_DAO()
  {
  }*/
  
  public ArrayList  getWriteFileDetById(TravelDet_DO  _TravelDet_DO) throws Exception{
    MLogger.log(1, this.getClass() + "getDetTrayLabelingWriteFile()");
    
    ArrayList arrList = new ArrayList();
    Connection con=null;
    con = DbBean.getConnection();
    Statement stmt = null;
    ResultSet rs = null;
    Map map = null;
    try {
     
          Hashtable ht = new Hashtable();
          MLogger.log(0, "LabelReportDeliveryNo" +_TravelDet_DO.getTraveler());
         
                
            /*  
             * Commanded on 20100409 for SAPTrayLabeling Generation File without PalletId.
             StringBuffer sql = new StringBuffer("SELECT  B.TRAVELER, B.SOLINE, B.SKU,B.Qty, B.TRAYLABLEQTY, B.PALLET, B.PALLETID,B.TRAYID, B.LOT, B.UOM,A.CRAT,A.SHIPDATE FROM");
             sql.append("(SELECT  Refno,TRAVELER,CRAT,isnull(Shipdate,'')Shipdate FROM OB_TRAVEL_HDR WHERE TRAVELER='"+ _TravelDet_DO.getTraveler() +"') A,");
             sql.append("(SELECT Refno,TRAVELER,SOLINE,SKU,QTY,TrayLableQty,PALLET,TRAYID,LOT,UOM,PALLETID,CRAT FROM OB_TRAVEL_DET  WHERE TRAVELER='"+ _TravelDet_DO.getTraveler() +"') B");
             sql.append(" WHERE  A.REFNO=B.REFNO AND A.TRAVELER=B.TRAVELER  AND  B.TRAVELER='"+ _TravelDet_DO.getTraveler() +"' ");
             sql.append("  AND  B.PALLETID='"+ _TravelDet_DO.getPalletid() +"' order by B.PALLETID,B.SOLINE,B.TRAYID ");
          
          //Commanded on 20100409 End */
          
          /*Commented and updated for LOT restriction under ticket WO0000000284867
           * 
            StringBuffer sql = new StringBuffer("SELECT  B.TRAVELER, B.SOLINE, B.SKU,B.Qty, B.TRAYLABLEQTY, B.PALLET, B.PALLETID,B.TRAYID, B.LOT, B.UOM,A.CRAT,A.SHIPDATE FROM");
          sql.append("(SELECT  Refno,TRAVELER,CRAT,isnull(Shipdate,'')Shipdate FROM OB_TRAVEL_HDR WHERE TRAVELER='"+ _TravelDet_DO.getTraveler() +"') A,");
          sql.append("(SELECT Refno,TRAVELER,SOLINE,SKU,QTY,TrayLableQty,PALLET,TRAYID,LOT,UOM,PALLETID,CRAT FROM OB_TRAVEL_DET  WHERE TRAVELER='"+ _TravelDet_DO.getTraveler() +"') B");
          sql.append(" WHERE  A.REFNO=B.REFNO AND A.TRAVELER=B.TRAVELER  AND  B.TRAVELER='"+ _TravelDet_DO.getTraveler() +"' ");
          sql.append(" order by B.PALLETID,B.SOLINE,B.TRAYID ");
           */
         
          
          /*Updated by Ranjana for LOT restriction under ticket WO0000000284867*/
          
          StringBuffer sql = new StringBuffer("SELECT  B.TRAVELER, B.SOLINE, B.SKU,B.Qty, B.TRAYLABLEQTY, B.PALLET, B.PALLETID,B.TRAYID,");
          sql.append(" B.LOT, B.UOM,A.CRAT,ISNULL(A.SHIPDATE,'')SHIPDATE FROM OB_TRAVEL_HDR A INNER JOIN");
          sql.append(" OB_TRAVEL_DET B ON  A.REFNO=B.REFNO AND A.TRAVELER=B.TRAVELER  WHERE  B.TRAVELER='"+ _TravelDet_DO.getTraveler() +"'");
          sql.append(" AND B.LOT NOT IN (SELECT L.LOT FROM TBL_LOTRESTRICTION L WHERE L.LOT=B.LOT AND L.STATUS ='BLOCK')");
          sql.append(" order by B.PALLETID,B.SOLINE,B.TRAYID ");
          
      
          stmt = con.createStatement();
          rs = stmt.executeQuery(sql.toString());  
         
          while (rs.next()) {
           map = new HashMap();
           for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
           }
           arrList.add(map);
        }
          MLogger.log(0,"getDetTrayLabelingWriteFile(aQuery)::"+arrList);
         MLogger.log(0,"getDetTrayLabelingWriteFile(aQuery)::"+sql);
      
      }
    catch (Exception e) {
      MLogger.log("Exception : getDetTrayLabelingWriteFile:" + e.toString());
      MLogger.log( -1, "");
    }
     finally{
      if (stmt != null) {
           stmt.close();
     }
     
       if (rs != null) {
           rs.close();
     }
             
     if (con != null) {
           DbBean.closeConnection(con);
        }
    }
    MLogger.log( -1, this.getClass() + " getDetTrayLabelingWriteFile()");
    
    return arrList;
  }
  
   public boolean IsExistsOBDeliveryDetById(TravelDet_DO  _TravelDet_Do) throws Exception
    {
     Connection con=null;
     con = DbBean.getConnection();
     PreparedStatement pStmt = null;
     ResultSet rs = null;
     boolean flag=false;
       try
            {
                
                String sql="SELECT COUNT(*) FROM OB_TRAVEL_DET WHERE TRAVELER = '" + _TravelDet_Do.getTraveler() + "'";
                MLogger.log(sql);
                pStmt = con.prepareStatement(sql);
                rs = pStmt.executeQuery();
                
                while (rs.next()) {
                 if (rs.getInt(1) > 0) {
                  flag = true;
                 }
                }
            }
             catch(Exception e) {
                  MLogger.log(0,"######################### Exception :: CheckOutBound DeliveryNo. Exist :: isExisit() : ######################### \n");
                  MLogger.log(0,""+ e.getMessage());
                  MLogger.log(0,"######################### Exception :: CheckOutBound DeliveryNo. Exist:: isExisit() : ######################### \n");
                  throw e;
            }
            finally{
              if (rs != null) {
                 rs.close();
               }
             if (pStmt != null) {
                 pStmt.close();
               }
             if (con != null) {
                 DbBean.closeConnection(con);
              }
           }
         return flag;
    }
//*******************************************************
//Below function is added by jyoti for TIBCO-INC000002484471(WMS 2.8)
//get count of MTIDs in a delivery
//*******************************************************
 public int getMTIDCount(TravelDet_DO  _TravelDet_Do) throws Exception
   {
	   int count=0;
	   Connection con=null;
	   con = DbBean.getConnection();
	   PreparedStatement pStmt = null;
	   ResultSet rs = null;
	   
	   try
	      {
		    MLogger.log(_TravelDet_Do.getTraveler());
	        String sql="SELECT COUNT(1) MTIDcount FROM OB_TRAVEL_DET WHERE TRAVELER_ID='"+ _TravelDet_Do.getTraveler()+"' AND   TRAYSTATUS <>'C'";
	        MLogger.log(sql);
	        pStmt = con.prepareStatement(sql);
	        rs = pStmt.executeQuery();
	        while (rs.next()) {
	        count =	rs.getInt("MTIDcount");
	        }
	       }
	   catch(Exception e) {
	         MLogger.log(0,"######################### Exception :: getMTIDCount() : ######################### \n");
	         MLogger.log(0,""+ e.getMessage());
	         MLogger.log(0,"######################### Exception :: getMTIDCount() : ######################### \n");
	         throw e;
	         }
	   finally{
	          if (rs != null) {
	          rs.close();
	          }
	          if (pStmt != null) {
	          pStmt.close();
	          }
	          if (con != null) {
	          DbBean.closeConnection(con);
	          }
	           }
	   return count;
   }
}