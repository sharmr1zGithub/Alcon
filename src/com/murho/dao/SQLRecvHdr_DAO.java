package com.murho.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.murho.DO.RecvHdr_DO;
import com.murho.gates.DbBean;
import com.murho.utils.MLogger;



public class SQLRecvHdr_DAO implements IRecvHdr_DAO
{
  public SQLRecvHdr_DAO()
  {
  }
  
    public int insertRecvHdr(RecvHdr_DO  _RecvHdr) throws Exception
  {
    Connection con=null;
    con = DbBean.getConnection();
    PreparedStatement pStmt = null;
    UserTransaction ut = null;
    int iCnt=0;
     
     try
      {
           StringBuffer sbRecvHdr=new StringBuffer("");
           sbRecvHdr.append("INSERT INTO RECVHDR(TRAVELER,ReceiveStatus,PutawayStatus,fileGenerated,STATUS,CRAT,CRBY,HDRFILENAME)VALUES(");
           sbRecvHdr.append("'" + _RecvHdr.getTraveler() +"',");
           sbRecvHdr.append("'" + _RecvHdr.getReceivestatus() +"',");
           sbRecvHdr.append("'" + _RecvHdr.getPutawaystatus() +"',");
           sbRecvHdr.append("'" + _RecvHdr.getFilegenerated() +"',");
           sbRecvHdr.append("'" + _RecvHdr.getStatus() +"',");
           sbRecvHdr.append("'" + _RecvHdr.getCrat() +"',");
           sbRecvHdr.append("'" + _RecvHdr.getCrby() +"',");
           sbRecvHdr.append("'" + _RecvHdr.getFilename() +"'");
           sbRecvHdr.append(")");
         
           pStmt = con.prepareStatement(sbRecvHdr.toString());
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
  
  
  public boolean updateRecvHdr(RecvHdr_DO  _RecvHdr)
   {
      return false;
   }
  
   public boolean deleteRecvHdr(RecvHdr_DO  _RecvHdr)
   {
       return false;
   }
   
    public boolean findByRecvHdrId(String traveler) throws Exception
    {
     Connection con=null;
     con = DbBean.getConnection();
     PreparedStatement pStmt = null;
     ResultSet rs = null;
     boolean flag=false;
          try
            {
              
                String sql="SELECT COUNT(TRAVELER) FROM RECVHDR  WHERE  TRAVELER = '" + traveler + "'";
                MLogger.log(0,"SQL....."+sql);
                pStmt = con.prepareStatement(sql);
                rs = pStmt.executeQuery();
                 
                while (rs.next()) {
                	MLogger.log(0,"value of header.."+rs.getInt(1));
                 if (rs.getInt(1) > 0) {
                  flag = true;
                 }
                   
                }
            }
             catch(Exception e) {
                  MLogger.log(0,"######################### Exception :: RECVHDR :: isExisit() : ######################### \n");
                  MLogger.log(0,""+ e.getMessage());
                  MLogger.log(0,"######################### Exception :: RECVHDR :: isExisit() : ######################### \n");
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
    
      public ArrayList getFileView(RecvHdr_DO  _RecvHdr) throws Exception{
      MLogger.log(1, this.getClass() + " getTravellerHdrList()");
      ArrayList arrList = new ArrayList();
      Connection con=null;
      con = DbBean.getConnection();
      Statement stmt = null;
      ResultSet rs = null;
      Map map = null;
     try 
     {
     
      MLogger.log(0, "FileName");
     
     String FileName=_RecvHdr.getFilename();
     if (FileName.equals(""))
     {
        String aQuery =  "Select distinct isnull(HDRFILENAME,'') HDRFILENAME  From RECVHDR Order by HDRFILENAME";
      stmt = con.createStatement();
      rs = stmt.executeQuery(aQuery.toString());  
      MLogger.log(0,"getFileList(aQuery)::"+aQuery);
       while (rs.next()) {
           map = new HashMap();
           for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
           }
           arrList.add(map);
      }
     }
    
    else
    {
     String aQuery =  "Select distinct isnull(HDRFILENAME,'') HDRFILENAME   From RECVHDR   Where HDRFileName like '" + _RecvHdr.getFilename() + "%'order by  HDRFILENAME " ;
      stmt = con.createStatement();
      rs = stmt.executeQuery(aQuery.toString());  
      MLogger.log(0,"getFileList(aQuery)::"+aQuery);
       while (rs.next()) {
           map = new HashMap();
           for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
           }
           arrList.add(map);
      }
    }  
      
         
    }
    
    catch (Exception e) {
      MLogger.log("Exception :getFileList :: getFileList:" + e.toString());
      MLogger.log( -1, "");
      throw e;
    }
    finally{
      if (rs != null) {
           rs.close();
         }
             
       if (con != null) {
           DbBean.closeConnection(con);
        }
     }
    MLogger.log( -1, this.getClass() + " getFileList()");
    return arrList;
  }

}