package com.murho.dao;



import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.NamingException;

import com.murho.DO.RecvDet_DO;
import com.murho.gates.DbBean;
import com.murho.utils.MLogger;


public class SQLRecvDet_DAO  implements IRecvDet_DAO
{
  public SQLRecvDet_DAO()
  {
  }
   public int insertRecvDet(RecvDet_DO  _RecvDet) throws Exception
  {
    Connection con=null;
    con = DbBean.getConnection();
    PreparedStatement pStmt = null;
   // CallableStatement colStmt = null;
    int iCnt=0;
    int iMovHisCnt=0;
   
     try{
             
          StringBuffer sbRecDet=new StringBuffer("");
          sbRecDet.append("INSERT INTO  RECVDET(Traveler, Pallet, MTID, SKU,LOT,OrdQty,RecvQty,PutAwayQty,PutAwayStatus,ExpireDate,USERFLD1,RECEIVESTATUS,CRAT,CRBY,CREATEDON,LINENOS,RSTATUS)VALUES(");
          sbRecDet.append("'" + _RecvDet.getTraveler() +"',");
          sbRecDet.append("'"  + _RecvDet.getPallet() +"',");
          sbRecDet.append("'" + _RecvDet.getMtid() +"',");
          sbRecDet.append("'" + _RecvDet.getSku()  +"',");
          sbRecDet.append("'" + _RecvDet.getLot()  +"',");
          sbRecDet.append(""  + _RecvDet.getOrdqty() +",");
          sbRecDet.append(""  + _RecvDet.getRecvqty() +",");
          sbRecDet.append(""  + _RecvDet.getPutawayqty() +",");
          sbRecDet.append("'" + _RecvDet.getPutawaystatus()+"',");
           sbRecDet.append("'" + _RecvDet.getCreatedon()+"',");
          sbRecDet.append("'" + _RecvDet.getUserfld1()+"',");
          sbRecDet.append("'" + _RecvDet.getReceivestatus()+"',");
          sbRecDet.append("'" + _RecvDet.getCrat()+"',");
          sbRecDet.append("'" + _RecvDet.getCrby()+"',");
          sbRecDet.append("'" + _RecvDet.getCreatedon() +"',");
          sbRecDet.append("'" + _RecvDet.getLinenos() +"',");
          sbRecDet.append("'" + "N"  +"'");
          sbRecDet.append(")");
          pStmt = con.prepareStatement(sbRecDet.toString());
          iCnt = pStmt.executeUpdate();
          
        
         
     }
    catch(Exception e)
     {
          //DbBean.RollbackTran(ut);
          MLogger.log(0,"######################### Exception :: insertIntoRecvHdr() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: insertIntoRecvHdr() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
     }
      finally{
         
         if (pStmt != null) {
          pStmt.close();
         }
        
        if (con != null) {
       
           DbBean.closeConnection(con);
        }
      }
         
     return iCnt;
    
  }
  
//Modified on 3-Apr-2014 for ticket Import Inbound File Process #INC000003097020   
public int CalcAllocation(String traveler) throws Exception{
      
    Connection con=null;
    con = DbBean.getConnection();
    CallableStatement colStmt = null;
    int iCnt=0;
    try{
             
          String sp ="";
          //sp = "exec Proc_CALL_ALLOCATELOCATION '" +""+ "'"; //commented for passing delivery nos.
          sp = "{ call Proc_CALL_ALLOCATELOCATION(?,?) }";
          colStmt   = con.prepareCall(sp);
          colStmt.setString(1,"");
          colStmt.setString(2,traveler);
          iCnt = colStmt.executeUpdate();
          MLogger.info("PROC_CALL_ALLOCATELOCATION : " + iCnt);
                    
        
     }
    catch(Exception e)
     {
         // DbBean.RollbackTran(ut);
          MLogger.log(0,"######################### Exception :: ALLOCATELOCATIONSPACE() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: ALLOCATELOCATIONSPACE() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
     }
      finally{
         
        
         if ( colStmt != null) {
          colStmt.close();
         }
        if (con != null) {
       
           DbBean.closeConnection(con);
        }
      }
         
     return iCnt;
    
  }
  
   public ArrayList getCalcTraveler() throws Exception{
  //  MLogger.log(1, this.getClass() + " getSPCalcTraveler()");
    
    ArrayList arrList = new ArrayList();
    Connection con=null;
    con = DbBean.getConnection();
    Statement stmt = null;
    ResultSet rs = null;
    Map map = null;
    try {
  
      Hashtable ht = new Hashtable();
     // MLogger.log(0,"getSPCalcTraveler()");
          
      StringBuffer sql = new StringBuffer("SELECT  TRAVELER FROM RECVDET GROUP BY TRAVELER");
    
      stmt = con.createStatement();
      rs = stmt.executeQuery(sql.toString());  
         
      while (rs.next()) {
         map = new HashMap();
         arrList.add(rs.getString(1));
       
      }
         
         MLogger.log(0,"getSPCalcTraveler(aQuery)::"+sql);
    }
  
    catch (Exception e) {
      MLogger.log("Exception :getSPCalctraveler :: getSPCalcTraveler:" + e.toString());
      MLogger.log( -1, "");
    }
     finally{
           if (stmt  != null) {
             stmt.close();
          }
          if (rs != null) {
             rs.close();
          }
             
         if (con != null) {
            DbBean.closeConnection(con);
         }
     }
    MLogger.log( -1, this.getClass() + "  getSPCalcTraveler:()");
    
    return arrList;
  }
  
  public int deCalcAllocation(RecvDet_DO _RecvDet) throws Exception{
      
    Connection con=null;
    con = DbBean.getConnection();
  
    CallableStatement colStmt = null;
    int iCnt=0;
   
  
     
     try{
             
          String sp ="";
          sp = "exec Proc_CALL_DEALLOCATELOCATION '" +""+ "','"+ _RecvDet.getTraveler() +"','"+ _RecvDet.getMtid() +"','"+ _RecvDet.getLot() +"','"+ _RecvDet.getSku() +"','"+ _RecvDet.getMode() +"'";
          colStmt   = con.prepareCall(sp);
          iCnt = colStmt.executeUpdate();
          MLogger.info("PROC_CALL_DEALLOCATELOCATION : " + iCnt);
         
         
     }
    catch(Exception e)
     {
        //  DbBean.RollbackTran(ut);
          MLogger.log(0,"######################### Exception :: DEALLOCATELOCATIONSPACE() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: DEALLOCATELOCATIONSPACE() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
     }
      finally{
         
        
         if ( colStmt != null) {
          colStmt.close();
         }
        if (con != null) {
       
           DbBean.closeConnection(con);
        }
      }
         
     return iCnt;
    
  }
  
  
  
   public boolean updateRecvDet(RecvDet_DO  _RecvDet)
   {
      return false;
   }
  
   public boolean deleteRecvDet(RecvDet_DO  _RecvDet)
   {
       return false;
   }
   
    public boolean findByRecvDetId(String traveler,String mtid) throws Exception
    {
     Connection con=null;
     con = DbBean.getConnection();
     PreparedStatement pStmt = null;
     ResultSet rs = null;
     boolean flag=false;
       try
            {
                
                String sql="SELECT COUNT(*) FROM " + TABLE_NAME  +" WHERE TRAVELER = '" + traveler + "' AND MTID='" + mtid + "'";
                MLogger.log(0,"SQL........."+ sql);
                pStmt = con.prepareStatement(sql);
                rs = pStmt.executeQuery();
                while (rs.next()) {
                	MLogger.log(0,"value of det..."+rs.getInt(1));
                 if (rs.getInt(1) > 0) {
                  flag = true;
                 }
                }
            }
             catch(Exception e) {
                  MLogger.log(0,"######################### Exception :: RECVDET :: isExisit() : ######################### \n");
                  MLogger.log(0,""+ e.getMessage());
                  MLogger.log(0,"######################### Exception :: RECVDET :: isExisit() : ######################### \n");
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
    
    
    public boolean findRecvStatus(String traveler) throws Exception
    {
     Connection con=null;
     con = DbBean.getConnection();
     PreparedStatement pStmt = null;
     ResultSet rs = null;
     boolean flag=true;
       try
            {
                
                String sql="SELECT COUNT(*)  FROM RECVDET WHERE TRAVELER = '" + traveler + "' AND RECEIVESTATUS <> '" + "N" + "' GROUP BY TRAVELER";
                pStmt = con.prepareStatement(sql);
                rs = pStmt.executeQuery();
                while (rs.next()) {
                 if (rs.getInt(1) > 0) {
                  flag = false;
                 }
                }
            }
             catch(Exception e) {
                  MLogger.log(0,"######################### Exception :: RECVDET :: findRecevStatus : ######################### \n");
                  MLogger.log(0,""+ e.getMessage());
                  MLogger.log(0,"######################### Exception :: RECVDET :: findRecevStatus : ######################### \n");
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
    
    
    public boolean findRecvStatusforUpdate(RecvDet_DO _RecvDet) throws Exception
    {
     Connection con=null;
     con = DbBean.getConnection();
     PreparedStatement pStmt = null;
     ResultSet rs = null;
     boolean flag=true;
       try
            {
                
                String sql="SELECT ReceiveStatus  FROM RECVDET WHERE TRAVELER = '" +  _RecvDet.getTraveler() + "' AND MTID  '" +  _RecvDet.getMtid()  + "'";
                pStmt = con.prepareStatement(sql);
                rs = pStmt.executeQuery();
               
                 if (rs.getString(1).toString().equals("N")) {
                  flag = true;
               }

            }
             catch(Exception e) {
                  MLogger.log(0,"######################### Exception :: RECVDET :: findRecevStatusForUpdate : ######################### \n");
                  MLogger.log(0,""+ e.getMessage());
                  MLogger.log(0,"######################### Exception :: RECVDET :: findRecevStatusForUpdate : ######################### \n");
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
    
    
   public ArrayList getRecvDetById(RecvDet_DO  _RecvDet) throws Exception{
    MLogger.log(1, this.getClass() + " getReceiveDetailsById()");
    
    ArrayList arrList = new ArrayList();
    Connection con=null;
    con = DbBean.getConnection();
    Statement stmt = null;
    ResultSet rs = null;
    Map map = null;
    try {
  
      Hashtable ht = new Hashtable();
      MLogger.log(0, "travelId:" +_RecvDet.getTraveler() );
       String strtravler=_RecvDet.getTraveler();   
      String FileName=  _RecvDet.getFilename();
      
     
     if (FileName.equals("")||FileName.equals(null))
      {
     
         StringBuffer sql = new StringBuffer("SELECT MAX(TRAVELER) AS TRAVELER,");
         sql.append(" MAX(SKU)  AS SKU,SUM(ORDQTY) AS ORDQTY ,SUM(RECVQTY) AS RECVQTY ");
         sql.append(",MAX(RECEIVESTATUS) AS RECEIVESTATUS,MAX(ISNULL(LOC,'')) AS LOC  FROM RECVDET ");
         sql.append(" WHERE TRAVELER='"+_RecvDet.getTraveler() +"' GROUP BY Traveler Order  by Traveler  ");
      
         stmt = con.createStatement();
         rs = stmt.executeQuery(sql.toString());  
         
          while (rs.next()) {
           map = new HashMap();
           for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
           }
           arrList.add(map);
        }
         
         MLogger.log(0,"getReceiveDetailsById(aQuery)::"+sql);
      }
      else if(strtravler.equals("") || strtravler.equals(null))
      {
        StringBuffer sql = new StringBuffer("SELECT MAX(TRAVELER) AS TRAVELER,");
         sql.append(" MAX(SKU)  AS SKU,SUM(ORDQTY) AS ORDQTY ,SUM(RECVQTY) AS RECVQTY ");
         sql.append(",MAX(RECEIVESTATUS) AS RECEIVESTATUS,MAX(ISNULL(LOC,'')) AS LOC  FROM RECVDET ");
         sql.append(" WHERE TRAVELER IN"); //GROUP BY pallet,LOT Order  by pallet   ");
         sql.append("(SELECT TRAVELER FROM RECVHDR WHERE HDRFILENAME='"+_RecvDet.getFilename() +"') GROUP BY Traveler Order  by Traveler  ");
         stmt = con.createStatement();
      
         stmt = con.createStatement();
         rs = stmt.executeQuery(sql.toString());  
         
          while (rs.next()) {
           map = new HashMap();
           for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
           }
           arrList.add(map);
        }
         
         MLogger.log(0,"getReceiveDetailsById(aQuery)::"+sql);
      }
      else 
      {
        
         StringBuffer sql = new StringBuffer("SELECT MAX(TRAVELER) AS TRAVELER,");
         sql.append(" MAX(SKU)  AS SKU,SUM(ORDQTY) AS ORDQTY ,SUM(RECVQTY) AS RECVQTY ");
         sql.append(",MAX(RECEIVESTATUS) AS RECEIVESTATUS,MAX(ISNULL(LOC,'')) AS LOC  FROM RECVDET ");
         sql.append(" WHERE TRAVELER='"+_RecvDet.getTraveler() +"' AND TRAVELER IN"); //GROUP BY pallet,LOT Order  by pallet   ");
         sql.append("(SELECT TRAVELER FROM RECVHDR WHERE TRAVELER='"+_RecvDet.getTraveler() +"' AND HDRFILENAME='"+_RecvDet.getFilename() +"') GROUP BY Traveler Order  by Traveler  ");
         stmt = con.createStatement();
         rs = stmt.executeQuery(sql.toString());  
         
          while (rs.next()) {
           map = new HashMap();
           for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
           }
           arrList.add(map);
        }
         
         MLogger.log(0,"getReceiveDetailsById(aQuery)::"+sql);
      }
     
      
    }
    catch (Exception e) {
      MLogger.log("Exception :getTravellerSummary :: getTravellerSummaryById:" + e.toString());
      MLogger.log( -1, "");
    }
     finally{
              if (rs != null) {
                 rs.close();
               }
             
             if (con != null) {
                 DbBean.closeConnection(con);
              }
           }
    MLogger.log( -1, this.getClass() + " getTravellerSummaryById()");
    return arrList;
  }
  
  
  
  public ArrayList getRecvDet() throws Exception{
    MLogger.log(1, this.getClass() + " getReceiveDetails()");
    
    ArrayList arrList = new ArrayList();
    Connection con=null;
    con = DbBean.getConnection();
    Statement stmt = null;
    ResultSet rs = null;
    Map map = null;
    try {
  
         Hashtable ht = new Hashtable();
      
         StringBuffer sql = new StringBuffer("SELECT MAX(TRAVELER) AS TRAVELER,PALLET AS PALLET,");
         sql.append(" MAX(SKU)  AS SKU,LOT,SUM(ORDQTY) AS ORDQTY ,SUM(RECVQTY) AS RECVQTY ");
         sql.append(",MAX(RECEIVESTATUS) AS RECEIVESTATUS,MAX(ISNULL(LOC,'')) AS LOC  FROM RECVDET ");
         sql.append(" GROUP BY pallet,LOT Order  by pallet   ");
      
         stmt = con.createStatement();
         rs = stmt.executeQuery(sql.toString());  
         
          while (rs.next()) {
           map = new HashMap();
           for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
           }
           arrList.add(map);
         }
        MLogger.log(0,"getReceiveDetails(aQuery)::"+sql);
    
      }
  
    catch (Exception e) {
      MLogger.log("Exception :getTravellerSummary :: getTravellerSummary:" + e.toString());
      MLogger.log( -1, "");
    }
     finally{
              if (rs != null) {
                 rs.close();
               }
             
             if (con != null) {
                 DbBean.closeConnection(con);
              }
           }
    MLogger.log( -1, this.getClass() + " getTravellerSummary()");
    return arrList;
  }
 
  
  public ArrayList  getRecvTravelerDetById(RecvDet_DO  _RecvDet) throws Exception{
    MLogger.log(1, this.getClass() + " getRecevTravelerDetById()");
    
    ArrayList arrList = new ArrayList();
    Connection con=null;
    con = DbBean.getConnection();
    Statement stmt = null;
    ResultSet rs = null;
    Map map = null;
    try {
  
        Hashtable ht = new Hashtable();
        MLogger.log(0, "travelID" +_RecvDet.getTraveler() );
      
        StringBuffer sql = new StringBuffer("SELECT MAX(TRAVELER) AS TRAVELER,PALLET AS PALLET,");
        sql.append(" MAX(SKU)  AS SKU,LOT,SUM(ORDQTY) AS ORDQTY ,SUM(RECVQTY) AS RECVQTY ");
        sql.append(",MAX(RECEIVESTATUS) AS RECEIVESTATUS,MAX(ISNULL(LOC,'')) AS LOC  FROM RECVDET ");
        sql.append(" WHERE TRAVELER='"+ _RecvDet.getTraveler() +"'  GROUP BY pallet,LOT Order  by pallet   ");
            
      
         stmt = con.createStatement();
         rs = stmt.executeQuery(sql.toString());  
         
          while (rs.next()) {
           map = new HashMap();
           for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
           }
           arrList.add(map);
        }
         
         MLogger.log(0,"getRecevTravelerDetById(aQuery)::"+sql);
      
      }


    catch (Exception e) {
    //  MLogger.log("Exception : getRecevTravelerDetById :: getRecevTravelerDetById:" + e.toString());
      MLogger.log("Exception : getRecevTravelerDetById:" + e.toString());
      MLogger.log( -1, "");
    }
     finally{
              if (rs != null) {
                 rs.close();
               }
             
             if (con != null) {
                 DbBean.closeConnection(con);
              }
           }
    MLogger.log( -1, this.getClass() + " getRecevTravelerDetById()");
    return arrList;
  }
  
  public boolean getRecvDetCount(String travelId) throws Exception{
  
    Connection con=null;
    con = DbBean.getConnection();
    PreparedStatement ps     = null;
    ResultSet rs = null;
    boolean isExists=false;
    boolean recvCnt;
    // System.out.println("StringHdrTraveler.........."+travelId);
      try {
            //con = DbBean.getConnection();
            String sQry = "SELECT COUNT(*) FROM RECVDET WHERE  TRAVELER='"+ travelId +"'";
            ps = con.prepareStatement(sQry);
            rs   =  ps.executeQuery();
            while(rs.next()){
               if( rs.getInt(1) > 0 ) isExists = true;
               
          }
          
      }
        catch (Exception e) {
         MLogger.log("Exception : getRecevTravelerCount:" + e.toString());
         MLogger.log( -1, "");
    }
     finally{
              if (ps != null) {
                 ps.close();
               }
              if (rs != null) {
                 rs.close();
               }
             
             if (con != null) {
                 DbBean.closeConnection(con);
              }
           }
    MLogger.log( -1, this.getClass() + " getRecevTravelerCount()");
    return isExists;
  }
  
  
   public ArrayList getRecvTravelerDet() throws Exception{
    MLogger.log(1, this.getClass() + " getReceiveDetails()");
    
    ArrayList arrList = new ArrayList();
    Connection con=null;
    con = DbBean.getConnection();
    Statement stmt = null;
    ResultSet rs = null;
    Map map = null;
    try {
  
        Hashtable ht = new Hashtable();
       StringBuffer sql = new StringBuffer("SELECT MAX(TRAVELER) AS TRAVELER,PALLET AS PALLET,");
        sql.append(" MAX(SKU)  AS SKU,LOT,SUM(ORDQTY) AS ORDQTY ,SUM(RECVQTY) AS RECVQTY ");
        sql.append(",MAX(RECEIVESTATUS) AS RECEIVESTATUS,MAX(ISNULL(LOC,'')) AS LOC  FROM RECVDET ");
        sql.append("  GROUP BY pallet,LOT Order  by pallet   ");
        
         stmt = con.createStatement();
         rs = stmt.executeQuery(sql.toString());  
         
          while (rs.next()) {
           map = new HashMap();
           for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
           }
           arrList.add(map);
         }
        MLogger.log(0,"getRecevTravelerDet(aQuery)::"+sql);
    
      }
  
    catch (Exception e) {
      MLogger.log("Exception:getRecevTravelerDet :" + e.toString());
      MLogger.log( -1, "");
    }
     finally{
              if (rs != null) {
                 rs.close();
               }
             
             if (con != null) {
                 DbBean.closeConnection(con);
              }
           }
    MLogger.log( -1, this.getClass() + " getRecevTravelerDet()");
    return arrList;
  }
  
  
   public boolean checkPUV(String sku)  throws Exception
   {
     Connection con=null;
     con = DbBean.getConnection();
     Map map = null;
     ArrayList arrList = new ArrayList();
     Statement stmt = null;
     ResultSet rs = null;
     boolean flag=false;
     String clsPUV="PUV";
     try
     {
        String sql= "select COUNT(B.PRD_GRP_ID) AS PRODUCTGROUP From ItemMst A ,Prd_Class_mst  B Where A.Item='"+sku+"' AND A.PRD_CLS_ID = B.PRD_CLS_ID AND B.PRD_GRP_ID='"+clsPUV+"' ";               
        MLogger.log(sql);
        stmt = con.createStatement();
        rs = stmt.executeQuery(sql);  
      
          
          while (rs.next()) {
           if (rs.getInt(1) > 0) {
             flag = true;
          }
        }
     }
            
    catch(Exception e) {
        MLogger.log(0,"######################### Exception ::  checkPUV :: isExisit() : ######################### \n");
        MLogger.log(0,""+ e.getMessage());
         MLogger.log(0,"######################### Exception ::  checkPUV  :: isExisit() : ######################### \n");
        throw e;
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
      return  flag;
   }
  
/* Method Added by Ranjana 
 * Purpose: to check whether the LOT is Blocked */
   
public String checkLOT(String lot) throws Exception {

		Connection con = null;
		String output = "";

		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			MLogger.log(0, "dbo.PROC_CHECK_LOTRESTRICTION_INBOUND_UPLOAD");

			con = DbBean.getConnection();
			cs = con
					.prepareCall("{call dbo.[PROC_CHECK_LOTRESTRICTION_INBOUND_UPLOAD](?)}");
			cs.setString(1, lot);

			rs = cs.executeQuery();

			while (rs.next()) {
				output = rs.getString("STATUS");
			}

			MLogger.log(0, "output......" + output);
		} catch (Exception e) {
			MLogger.log(0, "############ Exception ::  checkLOT:: ####### \n");
			MLogger.log(0, "" + e.getMessage());
			throw e;
		} finally{
            if (rs != null) {
                rs.close();
              }
            
            if (con != null) {
                DbBean.closeConnection(con);
             }
          }
		return output;
	}

/* Method Added by Ranjana 
 * Purpose: To check whether the LOT is System Blocked or not
 * and to exclude the Restricted LOT from getting imported in to the Database.
 * under ticket WO0000001221106
 */
	public String blockLOT(String destination, String description, String lot)
			throws Exception {
		Connection con = null;
		String output = "";
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			MLogger.log(0, "dbo.PROC_CHECK_BLOCKLOTSTATUS");

			con = DbBean.getConnection();
			
			/*
			 * cs = con.prepareCall("{call dbo.[PROC_CHECK_BLOCKLOTSTATUS](?,?,?)}");
			 * cs.setString(1, destination);
			 * cs.setString(2, lot);
			 * cs.setString(3, description);			
			*/
			cs = con
					.prepareCall("{call dbo.[PROC_CHECK_BLOCKLOTSTATUS](?,?,?)}");
			cs.setString(1, destination);
			cs.setString(2, lot);
			cs.setString(3, description);
			rs = cs.executeQuery();

			while (rs.next()) {
				output = rs.getString("lotstatus");
			}

			MLogger.log(0, "output......" + output);
		} catch (Exception e) {
			MLogger.log(0, "############ Exception ::  BlockLOT:: ####### \n");
			MLogger.log(0, "" + e.getMessage());
			throw e;
		} finally{
            if (rs != null) {
                rs.close();
              }
            
            if (con != null) {
                DbBean.closeConnection(con);
             }
          }
		return output;
}  
}