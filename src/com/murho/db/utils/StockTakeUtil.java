
package com.murho.db.utils;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.murho.dao.InvMstDAO;
import com.murho.dao.MovHisDAO;
import com.murho.dao.PrdClsDAO;
import com.murho.dao.StockTakeDAO;
import com.murho.gates.DbBean;
import com.murho.tran.WmsStockTake;
import com.murho.tran.WmsTran;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;

public class StockTakeUtil {
  XMLUtils _xmlUtils = null;
  StrUtils _strUtils = null;
  
  public StockTakeUtil() {
     _xmlUtils = new XMLUtils();
     _strUtils = new StrUtils();
  }


   
 
  public ArrayList getStockTakeSummary(String aCond,String sRow,String eRow) throws Exception{
      CallableStatement cs =  null;
      ResultSet rs         = null;
      ArrayList arrList    = new ArrayList();
      Connection con = null;
      try{
         con = DbBean.getConnection(); 
         cs = con.prepareCall("{call dbo.[spGetListingsPagination](?, ?,?)}");
          cs.setString(1, aCond);
          cs.setString(2, sRow);
          cs.setString(3, eRow);
         
          rs = cs.executeQuery();
         
         
          while (rs.next()) {
       
                Map map  = new HashMap();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
                }
                arrList.add(map);
          }
          
          System.out.println("array Size"+ arrList.size());
      
          System.out.println("spGetListingsPagination :: arrList.size " + arrList.size());
      }catch(Exception e) {System.out.println("StockTake ::getStockTakeSummary :  Exception :: "+e.toString() ); }
      finally
      {
           DbBean.closeConnection(con, cs);
       }
      return arrList;
    }

 //CCODE-04
 
 
  public void getGeneratedExcel(String fileName,String Qry)
    throws Exception
  {
    MLogger.log(1, getClass() + " getGeneratedExcel()");
    StrUtils _strUtils = new StrUtils();
    
    Connection con = null;
    try
    {
      ResultSet rs = null;
      con = DbBean.getConnection();
       System.out.println("Print what a print with in print");
        System.out.println(fileName);
        System.out.println(Qry);
      CallableStatement cstmt =
     
      con.prepareCall("{call dbo.[EXPORTTOEXCEL](?, ?)}");
      cstmt.setString(1, fileName);
      cstmt.setString(2, Qry);
    
      cstmt.execute();
      
    
    }
    catch (Exception e)
    {
      MLogger.log("Exception :getGeneratedExcel ::" + e.toString());
      throw e;
    }
    finally {
      if (con != null)
        DbBean.closeConnection(con);
    }

    MLogger.log(-1, getClass() + " getGeneratedExcel()");
  
  }
  //Old 
    public ArrayList getStockTakeDetails()
   {
     ArrayList arrList = new ArrayList();
     String sCondition ="";
     Hashtable ht = new Hashtable();
   
     try{
      MovHisDAO movHisDAO= new MovHisDAO();
      StringBuffer sql = new StringBuffer("SELECT DISTINCT A.SKU AS ITEM,ISNULL(B.USERFLD1,'') AS DESCRIP,A.LOC AS LOC,ISNULL(A.QTY,'0') AS INVQTY,ISNULL(B.QTY,'0') AS QTY,(ISNULL(B.QTY,'0')-ISNULL(A.QTY,'0')) AS DIFFQTY,ISNULL(A.MTID,'') AS MTID,B.CRBY,B.CRAT FROM  INVMST A,STKTAKE B WHERE A.PLANT=B.PLANT AND A.SKU=B.ITEM AND A.LOC=B.LOC AND A.MTID=B.MTID");
      arrList = movHisDAO.selectForReport(sql.toString(),ht);
    }catch(Exception e){MLogger.log(0,"Exception :repportUtil :: getMovHisList:"+e.toString() );}
    return arrList;
  }
  
  
  //New : modified by samatha based on URS- A-40 
  
  
   //New : modified by samatha based on URS- A-40 
  
    public long getRecCountForStockTake(String plant,String sku,String lotno,String loc, String logGrp)
   {
     long RecCount =0;
     String sCondition ="";
     Hashtable ht = new Hashtable();
   	StringBuffer query = new StringBuffer();
   
     try{
      StockTakeDAO stockDAO= new StockTakeDAO();
      
    
      query.append("select  Count (*)  FROM ");
      query.append(" ( SELECT    ISNULL(dbo.STKTAKE.PLANT, Invmst.Plant) AS Plant,");
      query.append("  ISNULL(dbo.STKTAKE.MTID, Invmst.MTID) AS MTID, ");
      query.append(" ISNULL(dbo.STKTAKE.ITEM, Invmst.SKU) AS Item,  ");
      query.append(" (SELECT ITEMDESC FROM ITEMMST WHERE ITEM = Invmst.SKU ) AS DESCRIP, ");
      query.append(" ISNULL(dbo.STKTAKE.BATCH, Invmst.LOT) AS BATCH,");
      query.append("  ISNULL(dbo.STKTAKE.LOC, Invmst.LOC) AS LOC, ");
      query.append("  ISNULL(Invmst.Qty , 0) AS InvQty,    ISNULL(dbo.STKTAKE.QTY, 0) AS StkQty,ISNULL(dbo.STKTAKE.Qty, 0)-ISNULL(Invmst.QTY, 0) AS DIFFQTY,");
      query.append(" ISNULL(dbo.STKTAKE.CRBY,'') AS CRBY,ISNULL(dbo.STKTAKE.CRAT,'') AS CRAT");
      query.append("  FROM         dbo.STKTAKE FULL OUTER JOIN ");
      query.append("    (SELECT     Plant, SKU, LOT, MTID,loc, SUM(CAST(Qty AS INT)) Qty   FROM  InvMst WHERE QTY>0 ");
      query.append("  GROUP BY Plant, SKU, LOT,MTID, loc) Invmst ON dbo.STKTAKE.PLANT = Invmst.Plant AND dbo.STKTAKE.ITEM = Invmst.SKU AND   ");
      query.append("  dbo.STKTAKE.BATCH = Invmst.LOT  AND dbo.STKTAKE.MTID = Invmst.MTID  AND dbo.STKTAKE.LOC = Invmst.LOC ) AS A WHERE A.PLANT='"+plant+"'");
       
       
      if (sku != null && !sku.trim().equals(""))
				query.append(" AND A.ITEM LIKE '" + sku.trim() + "%'");
      if (lotno != null && !lotno.trim().equals(""))
				query.append(" AND A.BATCH LIKE '" + lotno.trim() + "%'");
      if (loc != null && !loc.trim().equals(""))
				query.append(" AND A.LOC LIKE '" + loc.trim() + "%'");
			if (logGrp != null && !logGrp.trim().equals(""))
				query.append(" AND A.LOC IN(select  LOC_ID  from locmst where LOC_GRP_ID = '"+logGrp+"') ");
        
        	RecCount = stockDAO.CountForReport(query.toString(), ht);
     
    }catch(Exception e){MLogger.log(0,"Exception :repportUtil :: getStockTakeDetails:"+e.toString() );}
    return RecCount;
  }
  
    public ArrayList getStockTakeDetails(String plant,String sku,String lotno,String loc, String logGrp)
   {
     ArrayList arrList = new ArrayList();
     String sCondition ="";
     Hashtable ht = new Hashtable();
   	StringBuffer query = new StringBuffer();
   
     try{
      StockTakeDAO stockDAO= new StockTakeDAO();
      
    
      query.append("select A.PLANT,A.MTID,A.ITEM,A.DESCRIP,A.BATCH,A.LOC,A.INVQTY,A.STKQTY,A.DIFFQTY,A.CRBY,A.CRAT FROM ");
      query.append(" ( SELECT    ISNULL(dbo.STKTAKE.PLANT, Invmst.Plant) AS Plant,");
      query.append("  ISNULL(dbo.STKTAKE.MTID, Invmst.MTID) AS MTID, ");
      query.append(" ISNULL(dbo.STKTAKE.ITEM, Invmst.SKU) AS Item,  ");
      query.append(" (SELECT ITEMDESC FROM ITEMMST WHERE ITEM = Invmst.SKU ) AS DESCRIP, ");
      query.append(" ISNULL(dbo.STKTAKE.BATCH, Invmst.LOT) AS BATCH,");
      query.append("  ISNULL(dbo.STKTAKE.LOC, Invmst.LOC) AS LOC, ");
      query.append("  ISNULL(Invmst.Qty , 0) AS InvQty,    ISNULL(dbo.STKTAKE.QTY, 0) AS StkQty,ISNULL(dbo.STKTAKE.Qty, 0)-ISNULL(Invmst.QTY, 0) AS DIFFQTY,");
      query.append(" ISNULL(dbo.STKTAKE.CRBY,'') AS CRBY,ISNULL(dbo.STKTAKE.CRAT,'') AS CRAT");
      query.append("  FROM         dbo.STKTAKE FULL OUTER JOIN ");
      query.append("    (SELECT     Plant, SKU, LOT, MTID,loc, SUM(CAST(Qty AS INT)) Qty   FROM  InvMst WHERE QTY>0 ");
      query.append("  GROUP BY Plant, SKU, LOT,MTID, loc) Invmst ON dbo.STKTAKE.PLANT = Invmst.Plant AND dbo.STKTAKE.ITEM = Invmst.SKU AND   ");
      query.append("  dbo.STKTAKE.BATCH = Invmst.LOT  AND dbo.STKTAKE.MTID = Invmst.MTID  AND dbo.STKTAKE.LOC = Invmst.LOC ) AS A WHERE A.PLANT='"+plant+"'");
       
       
      if (sku != null && !sku.trim().equals(""))
				query.append(" AND A.ITEM LIKE '" + sku.trim() + "%'");
      if (lotno != null && !lotno.trim().equals(""))
				query.append(" AND A.BATCH LIKE '" + lotno.trim() + "%'");
      if (loc != null && !loc.trim().equals(""))
				query.append(" AND A.LOC LIKE '" + loc.trim() + "%'");
			if (logGrp != null && !logGrp.trim().equals(""))
				query.append(" AND A.LOC IN(select  LOC_ID  from locmst where LOC_GRP_ID = '"+logGrp+"') ");
        
        	arrList = stockDAO.selectForReport(query.toString(), ht);
     
    }catch(Exception e){MLogger.log(0,"Exception :repportUtil :: getStockTakeDetails:"+e.toString() );}
    return arrList;
  }





 public boolean ValidateLoc(String aCompany, String aLoc){
      boolean isValid = false;
      InvMstDAO invDao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      ht.put(MDbConstant.COMPANY,aCompany );
      ht.put(MDbConstant.INV_LOC,aLoc);
     
      try {
        isValid = invDao.isExists(ht);
      }
      catch (Exception e) {
        MLogger.exception("ValidateLoc()", e);
        isValid = false;
      }

      return isValid;
  }
  
   public String getValidLocation(String plant,String loc) throws Exception{
    MLogger.log(1, this.getClass() + " getValidLocation()");
    String xmlStr = "";
    Map map = null;
    InvMstDAO invDao = new InvMstDAO();
 
    //query
     String query="LOC";
    //condition
    Hashtable ht=new Hashtable();
    ht.put("PLANT",plant);
    ht.put("LOC",loc);
    
    xmlStr = _xmlUtils.getXMLHeader();
    xmlStr = xmlStr + _xmlUtils.getStartNode("PickDetails");
    try {
       map = invDao.selectRow(query,ht);
       MLogger.info("Record size() :: " + map.size());
       if (map.size() > 0) {
            xmlStr = xmlStr + _xmlUtils.getXMLNode("status", "0");
            xmlStr = xmlStr + _xmlUtils.getXMLNode("description", "");
            xmlStr = xmlStr + _xmlUtils.getXMLNode("LOC","qwqwqw");
        }
      else
      {
        throw new Exception("Invalid Location :"+loc );
      }
     
      xmlStr = xmlStr + _xmlUtils.getEndNode("PickDetails");
      MLogger.log(0, "Value of xml : " + xmlStr);
    }
    catch (Exception e) {
      MLogger.exception("getValidLocation()", e);
      throw e;
    }
    MLogger.log( -1, this.getClass() + " getValidLocation()");
    return xmlStr;
  }
  
  
  //Modified By Samatha based on URS -A-30 to show the default product grp Qty
     public String getMTIDdetails(String plant,String mtid) throws Exception{
    String xmlStr = "";
    Map map = null;
    InvMstDAO invDao = new InvMstDAO();
    PrdClsDAO prdClsDao = new PrdClsDAO();
 
    //query
     String query="SKU,LOT";
    //condition
    Hashtable ht=new Hashtable();
    ht.put("PLANT",plant);
    ht.put("MTID",mtid);
    
    xmlStr = _xmlUtils.getXMLHeader();
    xmlStr = xmlStr + _xmlUtils.getStartNode("PickDetails");
    try {
       map = invDao.selectRow(query,ht);
       MLogger.info("Record size() :: " + map.size());
       if (map.size() > 0) {   
            xmlStr = xmlStr + _xmlUtils.getXMLNode("status", "0");
            xmlStr = xmlStr + _xmlUtils.getXMLNode("description", "");
            xmlStr = xmlStr + _xmlUtils.getXMLNode("SKU", (String) map.get("SKU"));
            xmlStr = xmlStr + _xmlUtils.getXMLNode("LOT", (String) map.get("LOT"));
            xmlStr = xmlStr + _xmlUtils.getXMLNode("QTY",prdClsDao.getQtyPerTray((String) map.get("SKU")));
            
        }
      else
      {
        throw new Exception("Invalid MTID :"+mtid );
      }
     
      xmlStr = xmlStr + _xmlUtils.getEndNode("PickDetails");
      MLogger.log(0, "Value of xml : " + xmlStr);
    }
    catch (Exception e) {
      MLogger.exception("getValidLocation()", e);
      throw e;
    }
    return xmlStr;
  }
  
   public String process_StockCount(Map obj) throws Exception {
   boolean flag = false;
   UserTransaction ut = null;
   try
   {
     ut = com.murho.gates.DbBean.getUserTranaction();
     ut.begin();
     flag = process_Wms_StockCount(obj);
     MLogger.log(0,"After processing --> process_Wms_StockCount :: " + flag );
    
     if (flag == true){
         DbBean.CommitTran(ut);
         flag= true;
     }
     else{
        DbBean.RollbackTran(ut);
        flag= false;
     }
   }
   catch (Exception e) {
       flag = false;
       DbBean.RollbackTran(ut);
       throw e;
   }
   
   String xmlStr="";
   if(flag==true){
      xmlStr = _xmlUtils.getXMLMessage(0, "StockTake successfull");
    }else{
         xmlStr = _xmlUtils.getXMLMessage(1, "Error in StockTake");
    }
    return xmlStr;
   
  }
  
    private boolean process_Wms_StockCount(Map map) throws Exception{
    MLogger.info(" process_Wms_StockCount() starts");
    boolean flag=false;
    flag=true;
    WmsTran tran=new WmsStockTake();
    flag= tran.processWmsTran(map);
    MLogger.info(" process_Wms_StockCount() ends");
    return flag;
  }
 
 
 
}
