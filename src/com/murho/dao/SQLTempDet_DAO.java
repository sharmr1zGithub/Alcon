package com.murho.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.murho.DO.TempDet_DO;
import com.murho.gates.DbBean;
import com.murho.utils.MLogger;

//import java.util.*;

//import javax.transaction.UserTransaction;

public class SQLTempDet_DAO implements ITempDet_DAO
{
  public SQLTempDet_DAO()
  {
  }
  //Below funtion is added on 30 Oct 2014 for duplicate delivery issue to check data in temp data table #WO0000000202247
  public boolean checkTempDet() throws Exception
  {
	  Connection con = DbBean.getConnection();
	  Statement stmt = null;
	  ResultSet rs=null;
	  boolean flag=false;
	  try
	  {
		  String sql = "select * from  TEMPDATATABLE";
		  stmt = con.createStatement();
		  rs = stmt.executeQuery(sql);
		  if(rs.next())
		  
			flag=true;
		  
		  else
			 
			flag=false;
	  }catch(Exception e)
	  {
		  MLogger.log(0,"######################### Exception :: SELECT TEMPDATATABLE :: checkTempDet() : ######################### \n");
	      MLogger.log(0,""+ e.getMessage());
	      MLogger.log(0,"######################### Exception :: SELECT TEMPDATATABLE  :: checkTempDet() : ######################### \n");
	      throw e;
	  }
	  finally
	  {
		  if(stmt!=null)
			  {
			  stmt.close();
			  }
		  if(con!=null)
		  {
			  DbBean.closeConnection(con);
		  }
	  }
	return flag;
  }
  
   public int insertTempDet(TempDet_DO  _TempDet) throws Exception
   {
    Connection con=null;
    con = DbBean.getConnection();
    PreparedStatement pStmt = null;
    int iCnt=0;
    try
    {
          StringBuffer sbRecDet=new StringBuffer("");
          //change by Arun for #1851  
          //sbRecDet.append("INSERT INTO  TEMPDATATABLE(SNO,TRAVELER,DESTINATION,SKU,LOTNO,QTY,UOM,DESCRIPTION,PRODUCTCLASS,LOTTYPE,SHIPDATE,FIELD15)VALUES(");
          sbRecDet.append("INSERT INTO  TEMPDATATABLE(SNO,TRAVELER,DESTINATION,SKU,LOTNO,QTY,UOM,DESCRIPTION,SHIPDATE,FIELD15)VALUES(");
          sbRecDet.append("" + _TempDet.getSno() +",");
          sbRecDet.append("'" + _TempDet.getTraveler() +"',");
          sbRecDet.append("'"  + _TempDet.getDestination() +"',");
          sbRecDet.append("'" + _TempDet.getMaterial() +"',");   //SKU
          sbRecDet.append("'" + _TempDet.getLotno()  +"',");
          sbRecDet.append(""  +  _TempDet.getQty()  +",");
          sbRecDet.append("'" + _TempDet.getUom() +"',");
          sbRecDet.append("'" + _TempDet.getDescription()+"',");
          //Commented by arun for #1851 on 15 June 2011, PRODUCTCLASS and LOTTYPE taken care in proc: proc_IRO_IPMOIRT
          //sbRecDet.append("'" + _TempDet.getProductclass()+"',");
          //sbRecDet.append("'" + _TempDet.getLottype() +"',");
          sbRecDet.append("'" + _TempDet.getShipdate() +"',");
          sbRecDet.append(""  +  _TempDet.getField15() +"");
          sbRecDet.append(")");
          pStmt = con.prepareStatement(sbRecDet.toString());
          iCnt = pStmt.executeUpdate();
         
     }
    catch(Exception e)
     {
         
          MLogger.log(0,"######################### Exception :: insertTEMPTABLE() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: insertTEMPTABLE() : ######################### \n");
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
   
    
   public boolean updateTempDet(TempDet_DO  _TempDet)
   {
     return false;
   }
  
   public boolean deleteTempDetById(TempDet_DO  _TempDet)
   {
     return false;
   }
    public boolean deleteTempDet()  throws Exception
   {
     MLogger.log("Delete TempDet Table");
     Connection con=null;
     con = DbBean.getConnection();
     Statement stmt = null;
     //ResultSet rs = null;
     int updateCount=0;
     boolean flag=false;
     try
     {
        String sql="DELETE FROM TEMPDATATABLE";
        stmt = con.createStatement();
        updateCount = stmt.executeUpdate(sql);
        if (updateCount <= 0) {
          flag=false;
        }
        else
        {
          flag=true;
        }
     }
            
    catch(Exception e) {
        MLogger.log(0,"######################### Exception :: DELETE TEMPDATATABLE :: deleteTempDet() : ######################### \n");
        MLogger.log(0,""+ e.getMessage());
         MLogger.log(0,"######################### Exception :: DELETE TEMPDATATABLE  :: deleteTempDet() : ######################### \n");
        throw e;
    }
    finally{
      if (stmt != null) {
         stmt.close();
      }
             
      if (con != null) {
         DbBean.closeConnection(con);
      }
    }
      return  flag;
   }
  
    public boolean updateSHIP_DETRemarks(String traveler)  throws Exception
   {
      MLogger.log("updateSHIP_DETRemarks");
     Connection con=null;
     con = DbBean.getConnection();
     Statement stmt = null;
     //ResultSet rs = null;
     int updateCount=0;
     boolean flag=false;
     try
     {
     
        StringBuffer sql = new StringBuffer("UPDATE SHIP_DET SET REMARKS1='Yes',REMARKS2='Yes' WHERE");
        sql.append(" TRAVELER_ID IN(SELECT TRAVELER FROM OB_TRAVEL_DET WHERE PICKERLISTGENERATIONSTATUS='C' ");
        sql.append("AND TRAYSTATUS='C' AND STATUS='C' AND TRAVELER='" + traveler +"')");
    
        MLogger.log(sql.toString());
        stmt = con.createStatement();
        updateCount = stmt.executeUpdate(sql.toString());
        if (updateCount <= 0) {
          flag=false;
        }
        else
        {
          flag=true;
        }
     }
            
    catch(Exception e) {
        MLogger.log(0,"######################### Exception :: updateSHIP_DETRemarks :: isExisit() : ######################### \n");
        MLogger.log(0,""+ e.getMessage());
         MLogger.log(0,"######################### Exception :: updateSHIP_DETRemarks  :: isExisit() : ######################### \n");
        throw e;
    }
    finally{
      if (stmt != null) {
         stmt.close();
      }
             
      if (con != null) {
         DbBean.closeConnection(con);
      }
    }
      return  flag;
   }
  
   public boolean findByTempDetById(int sno) throws Exception 
   {
     return false;
   }
   
  public String findProductClassByItem(String  item) throws Exception
  {
     Connection con=null;
     con = DbBean.getConnection();
     PreparedStatement pStmt = null;
     ResultSet rs = null;
     String productid="";
         
    try
    {
                
      String sql="SELECT DISTINCT(PRD_CLS_ID) FROM  ITEMMST  WHERE ITEM = '" + item + "'";
      
      MLogger.query(sql);
     
      pStmt = con.prepareStatement(sql);
     
      rs = pStmt.executeQuery();
      while (rs.next()) {
         productid=rs.getString(1);
      }
    }
    catch(Exception e) {
      MLogger.log(0,"######################### Exception :: GETTEMITEMPRODUCTCLASS :: isExisit() : ######################### \n");
      MLogger.log(0,""+ e.getMessage());
      MLogger.log(0,"######################### Exception :: GETTEMITEMPRODUCTCLASS :: isExisit() : ######################### \n");
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
   return  productid;
    }

    public String findLotTypeByProductClass(String  productid,String shipto) throws Exception
    {
     Connection con=null;
     con = DbBean.getConnection();
     PreparedStatement pStmt = null;
     ResultSet rs = null;
     String lottype="";
         
       try
            {
                
                String sql="SELECT DISTINCT(TYPE) FROM  RULES_MST  WHERE PRD_CLS_ID = '" +  productid + "' AND SHIP_TO ='" + shipto + "'"; 
               
                MLogger.query(sql);
               
                pStmt = con.prepareStatement(sql);
                rs = pStmt.executeQuery();
              
                while (rs.next()) {
                  lottype=rs.getString(1);
                }
            }
             catch(Exception e) {
                  MLogger.log(0,"######################### Exception :: GETTEMLOTTYPE :: isExisit() : ######################### \n");
                  MLogger.log(0,""+ e.getMessage());
                  MLogger.log(0,"######################### Exception :: GETTEMLOTTYPE :: isExisit() : ######################### \n");
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
         return lottype;
   
    }
    
    // new method added by Arun #1851
    public boolean Call_Proc_IRO_Import() throws Exception {
        MLogger.log(1, this.getClass() + " Call_Proc_IRO_Import()");
        boolean flag = false;
        Connection con = null;
        try {
			// connection
			con = DbBean.getConnection();
			CallableStatement colStmt = null;
			String sp = "";
			sp = "exec Proc_IRO_IMPORT";
			System.out.println("Executing procedure : " + sp);
			colStmt = con.prepareCall(sp);

			int iCnt = 0;

			try {
				iCnt = colStmt.executeUpdate();
				MLogger.info("Proc_IRO_IMPORT  : " + iCnt);

				if (iCnt > 0) {
					flag = true;

				} else {
					flag = false;
				}

			} catch (Exception e) {

				MLogger.exception(this, e);
				throw e;
			}

		}
        catch (Exception e) {
          MLogger.log(0, "######################### Exception :: Call_Proc_IRO_Import() : ######################### \n");
          MLogger.log(0, "" + e.getMessage());
          MLogger.log(0, "######################### Exception :: Call_Proc_IRO_Import() : ######################### \n");
          MLogger.log( -1, "");
          throw e;
        }
        finally {
          if (con != null) {
              DbBean.closeConnection(con);
          }
        } 
        MLogger.log( -1, this.getClass() + " Call_Proc_IRO_Import()");

        return flag;

      }
 
  
}