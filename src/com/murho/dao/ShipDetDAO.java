package com.murho.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

import com.murho.gates.DbBean;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;



public class ShipDetDAO  extends BaseDAO{
  StrUtils strUtils = new StrUtils();
  private String TABLE_NAME = "SHIP_DET";
  Connection con = null;

  public ShipDetDAO() {
  }
   
    public boolean insert(Hashtable ht) throws Exception
   {
     MLogger.log(1, this.getClass() + " insert()");

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
          MLogger.log(0,"######################### Exception :: insert() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: insert() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
         }
         finally{
          if (con != null) {
          DbBean.closeConnection(con);
          }
         }
          MLogger.log(-1, this.getClass() + " insert()");
          return inserted;
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

   public ArrayList getShipDetails(String aPlant,String travelId) {
    MLogger.log(1, this.getClass() + " getShipDetails()");
    ArrayList arrList = new ArrayList();
    try {
      //ShipDetDAO idao = new ShipDetDAO();
      Hashtable ht = new Hashtable();
      MLogger.log(0, "travelID" + travelId);
      

      StringBuffer sql = new StringBuffer(" SELECT     DISTINCT TRAVELER_ID, PALLET,(SELECT     COUNT( distinct trayid) as NO_OF_TRAYS  FROM  OB_TRAVEL_DET  WHERE");  
      sql.append(" traveler_id = '"+travelId+"'  and palletId =a.pallet GROUP BY pallet)AS NO_OF_TRAYS ");
      sql.append(" , LENGTH, WIDTH, HEIGHT, GROSS_WEIGHT, REMARKS1, REMARKS2 ");
       sql.append(" FROM  SHIP_DET a Where traveler_id = '"+travelId+"' GROUP BY TRAVELER_ID, PALLET,LENGTH, WIDTH, HEIGHT, GROSS_WEIGHT, REMARKS1, REMARKS2 ");
      
      MLogger.log(0,"getShipDetails(aQuery)::"+sql);
      
      arrList = selectForReport(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getShipDetails :: getShipDetails:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getShipDetails()");
    return arrList;
  }
  
  
   public Map getSumOfGrossWeight(String aPlant,String travelId) {
    MLogger.log(1, this.getClass() + " getSumOfGrossWeight()");
    Map m = new HashMap();
    try {
      ShipDetDAO dao = new ShipDetDAO();
      Hashtable ht = new Hashtable();
      ht.put("TRAVELER_ID",travelId);
      MLogger.log(0, "travelID" + travelId);
      

      StringBuffer sql = new StringBuffer("       ( select  count( distinct TrayId) from ob_travel_det  WHERE TRAVELER_ID = '"+travelId+"' ) as TOTAL_TRAYS,SUM(CAST(GROSS_WEIGHT as INTEGER)) as TOTAL_GROSS  ");  
       MLogger.log(0,"getSumOfGrossWeight(aQuery)::"+sql);
      
      m = dao.selectRow(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getSumOfGrossWeight :: getSumOfGrossWeight:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getSumOfGrossWeight()");
    return m;
  }
  
  
  
  
    public ArrayList getPalletDetailsForTraveler(String aPlant,String traveler,String Pallet) {
    MLogger.log(1, this.getClass() + " getPalletDetailsForTraveler()");
    ArrayList arrList = new ArrayList();
    try {
     // InvMstDAO invdao = new InvMstDAO();
      Hashtable ht = new Hashtable();
      if(Pallet.length()>0){
      ht.put("PALLET",Pallet);
      }
      
      StringBuffer sql=new StringBuffer(" SELECT TRAVELER_ID,PALLET,TRAYID,QTY FROM SHIP_DET WHERE TRAVELER_ID='"+traveler+"' ");
      MLogger.log(0,"getPalletDetailsForTraveler(aQuery)::"+sql.toString());
      arrList = selectForReport(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getPalletDetailsForTraveler :: getPalletDetailsForTraveler:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getPalletDetailsForTraveler()");
    return arrList;
  }
  
    public Map getQuantityDetails(String aPlant,String travelId) {
    MLogger.log(1, this.getClass() + " getQuantityDetails()");
    Map m = new HashMap();
    try {
      ShipDetDAO dao = new ShipDetDAO();
      Hashtable ht = new Hashtable();
      ht.put("TRAVELER_ID",travelId);
    
      StringBuffer sql = new StringBuffer(" count(distinct pallet) as TOTAL_PALLET ,  ");  
      sql.append("   (select count(TrayId) from Ob_travel_det where traveler_id ='"+travelId+"') as TOTAL_TRAYS, ");
      sql.append("    SUM(CAST(GROSS_WEIGHT as INTEGER)) as TOTAL_GROSS ,  SUM(CAST(GROSS_WEIGHT as INTEGER))- count(distinct pallet) *10 as QTY ");
     
     
      MLogger.log(0,"getQuantityDetails(aQuery)::"+sql);
      
      m = dao.selectRow(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getQuantityDetails :: getQuantityDetails:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getQuantityDetails()");
    return m;
  }
  
   
  
  
  
    public ArrayList getSummaryPackingListForTraveler(String aPlant,String traveler) {
    MLogger.log(1, this.getClass() + " getSummaryPackingListForTraveler()");
    ArrayList arrList = new ArrayList();
    try {
     
      Hashtable ht = new Hashtable();
      StringBuffer sql=new StringBuffer(" select prd_cls_desc as PRD_TYPE,UOM,KFACTOR,PRICE, (select  sum(isnull(pickqty,0)) from ob_travel_det where  prd_class = a.prd_cls_id   ");
      
      sql.append("  and   traveler_id= '"+traveler+"' group by prd_class) as PICKQTY from prd_class_mst a where prd_cls_id in  ");
      sql.append("  ( select prd_class from ob_travel_det where traveler_id='"+traveler+"'  group by prd_class  ) ");
    
      
      MLogger.log(0,"getSummaryPackingListForTraveler(aQuery)::"+sql.toString());
      arrList = selectForReport(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getSummaryPackingListForTraveler :: getSummaryPackingListForTraveler:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getSummaryPackingListForTraveler()");
    return arrList;
  }
  
  
  public ArrayList getPackingListForTraveler(String aPlant,String traveler) {
    MLogger.log(1, this.getClass() + " getPackingListForTraveler()");
    ArrayList arrList = new ArrayList();
    try {
     
      Hashtable ht = new Hashtable();
      StringBuffer sql=new StringBuffer(" select SKU,isnull(SKU_DESC,'') as SKU_DESC,LOT,a.QTY,a.UOM,a.LOT,a.CUSTPO from ob_travel_det a ,Ship_det b ");
      sql.append("  where a.traveler_id=b.traveler_id and a.palletId=b.pallet  AND  b.traveler_id='"+traveler+"'  ");
      
      MLogger.log(0,"getPackingListForTraveler(aQuery)::"+sql.toString());
      arrList = selectForReport(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getPackingListForTraveler :: getPackingListForTraveler:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getPackingListForTraveler()");
    return arrList;
  }
  
   public ArrayList selectForReport(String query,Hashtable ht) throws Exception {
       MLogger.log(1, this.getClass() + " selectForReport()");
       //boolean flag = false;
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

         MLogger.query(" "+sql.toString());
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
 
    public boolean insertIntoShipDet(Hashtable ht) throws Exception
   {
     MLogger.log(1, this.getClass() + " insertIntoShipDet()");

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
          MLogger.log(0,"######################### Exception :: insertIntoShipDet() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: insertIntoShipDet() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
         }
         finally{
          if (con != null) {
          DbBean.closeConnection(con);
          }
         }
          MLogger.log(-1, this.getClass() + " insertIntoShipDet()");
          return inserted;
     }
     
     
     
      public ArrayList getIscPackingListForInvoice(String aPlant,String invoice) {
      MLogger.log(1, this.getClass() + " getIscPackingListForInvoice()");
      ArrayList arrList = new ArrayList();
      try {
     
      Hashtable ht = new Hashtable();
      StringBuffer sql=new StringBuffer(" select SKU,[Description] as SKU_DESC,LOT,QTY,UOM,LOT,CUSTPO,substring(EXPDATE,0,9) as EXPDATE from TEMP_SHIP_DATA Where InvoiceNo ='"+invoice+"'  ");
          
      MLogger.log(0,"getPackingListForTraveler(aQuery)::"+sql.toString());
      arrList = selectForReport(sql.toString(),ht);
     }
     catch (Exception e) {
      MLogger.log("Exception :getIscPackingListForInvoice :: getIscPackingListForInvoice:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getIscPackingListForInvoice()");
    return arrList;
  }
 
 
    public ArrayList getIscSummaryPackingListForInvoice(String aPlant,String invoice) {
      MLogger.log(1, this.getClass() + " getIscSummaryPackingListForInvoice()");
      ArrayList arrList = new ArrayList();
    try {
     
      Hashtable ht = new Hashtable();
      StringBuffer sql=new StringBuffer(" select prd_cls_desc1 as PRD_TYPE,UOM,KFACTOR,PRICE, (select  sum(qty) from TEMP_SHIP_DATA where  ProductClass = a.prd_cls_id and   ");
      sql.append(" InvoiceNo= '"+invoice+"' group by ProductClass) as PICKQTY from prd_class_mst a where prd_cls_id in  ");
      sql.append("  ( select ProductClass from TEMP_SHIP_DATA where InvoiceNo= '"+invoice+"'  group by ProductClass )  ");
       
      MLogger.log(0,"getIscSummaryPackingListForInvoice(aQuery)::"+sql.toString());
      arrList = selectForReport(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getIscSummaryPackingListForInvoice :: getIscSummaryPackingListForInvoice:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getIscSummaryPackingListForInvoice()");
    return arrList;
   } 
  
  
     public Map getIscQuantityDetails(String aPlant,String invoice) {
     MLogger.log(1, this.getClass() + " getIscQuantityDetails()");
     Map m = new HashMap();
     try {
      ShipDetDAO dao = new ShipDetDAO();
      Hashtable ht = new Hashtable();
      ht.put("INVOICE",invoice);
      MLogger.log(0, "INVOICE" + invoice);
      

      StringBuffer sql = new StringBuffer("  count(distinct pallet) as TOTAL_PALLET , SUM(NO_OF_TRAY) as TOTAL_TRAYS,  ");  
      sql.append("  SUM(CAST(GROSS_WEIGHT as INTEGER)) as TOTAL_GROSS ,  SUM(CAST(GROSS_WEIGHT as INTEGER))- count(distinct pallet) *10 as QTY ");
     
      MLogger.log(0,"getIscQuantityDetails(aQuery)::"+sql);
      m = dao.selectRow(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getQuantityDetails :: getIscQuantityDetails:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getIscQuantityDetails()");
    return m;
   }
  
  
  public Map getShipToForInvoice(String invoice) {
    MLogger.log(1, this.getClass() + " getShipToForInvoice()");
    Map m = new HashMap();
    try {
      String sql =" select ShipToCust  AS SHIPTO from TEMP_SHIP_DATA where invoiceNo ='"+invoice+"'" ;  
      m = getRowOfData(sql.toString());
    }
    catch (Exception e) {
      MLogger.log("Exception :getShipToForInvoice :: getShipToForInvoice:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getShipToForInvoice()");
    return m;
   }
 
 
   public ArrayList getIscShipDetails(String aPlant,String invoice) {
    MLogger.log(1, this.getClass() + " getIscShipDetails()");
    ArrayList arrList = new ArrayList();
    try {
      //ShipDetDAO idao = new ShipDetDAO();
      Hashtable ht = new Hashtable();
      StringBuffer sql = new StringBuffer(" SELECT     DISTINCT TRAVELER_ID, PALLET,   SUM(NO_OF_TRAY) as NO_OF_TRAYS ");  
      sql.append(" , LENGTH, WIDTH, HEIGHT, GROSS_WEIGHT, REMARKS1, REMARKS2  ");
      sql.append("   FROM  SHIP_DET WHERE INVOICE ='"+invoice+"' GROUP BY TRAVELER_ID, PALLET,LENGTH, ");
      sql.append(" WIDTH, HEIGHT, GROSS_WEIGHT, REMARKS1, REMARKS2  ");
      
      MLogger.log(0,"getIscShipDetails(aQuery)::"+sql);
      
      arrList = selectForReport(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getIscShipDetails :: getIscShipDetails:" + e.toString());
    }
    MLogger.log( -1, this.getClass() + " getIscShipDetails()");
    return arrList;
  }
  
   public Map getIscSumOfGrossWeight(String aPlant,String invoice) {
    MLogger.log(1, this.getClass() + " getIscSumOfGrossWeight()");
    Map m = new HashMap();
    try {
      ShipDetDAO dao = new ShipDetDAO();
      Hashtable ht = new Hashtable();
      ht.put("INVOICE",invoice);
      StringBuffer sql = new StringBuffer("  SUM(NO_OF_TRAY)  as TOTAL_TRAYS,  ");  
      sql.append("  SUM(CAST(GROSS_WEIGHT as INTEGER)) as TOTAL_GROSS   ");
       
      MLogger.log(0,"getIscSumOfGrossWeight(aQuery)::"+sql);
      
      m = dao.selectRow(sql.toString(),ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getIscSumOfGrossWeight :: getIscSumOfGrossWeight:" + e.toString());
    }
    MLogger.log( -1, this.getClass() + " getIscSumOfGrossWeight()");
    return m;
   }
  
  
   public Map getRowOfData( String query) throws Exception {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    Map map = null;
    map = new HashMap();

    try {
      conn=com.murho.gates.DbBean.getConnection();
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      MLogger.query(" "+query.toString());
      while (rs.next()) {
        map = new HashMap();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
          map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
        }
      }
    }
    catch (Exception e) {
        throw e;
    }
    finally {
      if (rs != null) {
        rs.close();
      }
      if (stmt != null) {
        stmt.close();
      }
    }
    return map;
  } 
  
     public ArrayList getInvoiceList(String aPlant,String invoice) {
      MLogger.log(1, this.getClass() + " getInvoiceList()");
      ArrayList arrList = new ArrayList();
      try {
     
      Hashtable ht = new Hashtable();
      MLogger.log(0, "invoice" + invoice);
      String aQuery = " select distinct invoiceno from temp_ship_data where  invoiceno like '"+invoice+"%'" ;
      
      MLogger.log(0,"getInvoiceList(aQuery)::"+aQuery);
      arrList = selectForReport(aQuery,ht);
    }
    catch (Exception e) {
      MLogger.log("Exception :getInvoiceList :: getInvoiceList:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getInvoiceList()");
    return arrList;
  }
  
  
     public ArrayList getInvoiceListForShiping(String aPlant,String invoice) {
      MLogger.log(1, this.getClass() + " getInvoiceListForShiping()");
      ArrayList arrList = new ArrayList();
      try {
     
      Hashtable ht = new Hashtable();  
      String aQuery = " SELECT DISTINCT obTr.invoiceNo REF_NUM FROM TEMP_SHIP_DATA obTr, ship_det sdet where(obTr.InvoiceNo = sdet.Invoice) and  obTr.invoiceno like '"+invoice+"%'" ;
      
      MLogger.log(0,"getInvoiceListForShiping(aQuery)::"+aQuery);
      arrList = selectForReport(aQuery,ht);
     }
     catch (Exception e) {
      MLogger.log("Exception ::: getInvoiceListForShiping:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getInvoiceListForShiping()");
    return arrList;
  }
  
   public boolean UpdatePackingList(String plant,String invoice) throws Exception{
    boolean updated = false;
    try{
      Hashtable ht = new Hashtable();
      ht.put("INVOICE",invoice);
      String Query = "set REMARKS1='YES' ";
      updated = update(Query,ht,"");
    }catch(Exception e){ 
    throw e;
    }
    return updated;
  }
  
    public boolean UpdateInvoiceList(String plant,String invoice) throws Exception{
    boolean updated = false;
    try{
      Hashtable ht = new Hashtable();
      ht.put("INVOICE",invoice);
      String Query = "set REMARKS2='YES' ";
      updated = update(Query,ht,"");
    }catch(Exception e){ 
    throw e;
    }
    return updated;
  }
  
  
   public boolean UpdateGlobalPackingList(String plant,String traveler) throws Exception{
    boolean updated = false;
    try{
      Hashtable ht = new Hashtable();
      ht.put("TRAVELER_ID",traveler);
      String Query = "set REMARKS1='YES' ";
      updated = update(Query,ht,"");
    }catch(Exception e){ 
    throw e;
    }
    return updated;
  }
  
    public boolean UpdateGlobalInvoiceList(String plant,String traveler) throws Exception{
    boolean updated = false;
    try{
      Hashtable ht = new Hashtable(); 
      ht.put("TRAVELER_ID",traveler);
      String Query = "set REMARKS2='YES' ";
      updated = update(Query,ht,"");
    }catch(Exception e){ 
    throw e;
    }  
    return updated;
  }
  
    public boolean update(String query, Hashtable map, String extCond) throws       Exception {
    MLogger.log(1, this.getClass() + " update()");
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
  
  
  
  	
	public boolean Print_Pallet(String pallet) throws Exception {
		MLogger.info(" Print_Pallet() starts");
		boolean flag = false;
		try
		{   
			  MLogger.printInput("PALLET ::  "+pallet);
			
		    //StrUtils su=new StrUtils();
	    	Runtime run = Runtime.getRuntime();
				StringBuffer argPrinter= new StringBuffer("C:\\props\\CibaVision\\PrintPallet\\LabelPrint.exe ");
        MLogger.printInput("PRINTER NAME ::  "+DbBean.PALLET_PRINTER_NAME);
				argPrinter.append(DbBean.PALLET_PRINTER_NAME);
				argPrinter.append( " " + pallet);
        MLogger.printInput("PRINTER NAME ::  "+argPrinter.toString());
				Process python = run.exec(argPrinter.toString());
				flag = true;
				}
				
	
		catch (Exception e) {
    System.out.println(e.toString());
			flag = false;
			throw e;
		}
		
		
		return flag;
	}
  
	
	/*public boolean Print_Shipmark(String printerName,String shipmark) throws Exception {
		MLogger.info(" Print_Shipmark() starts");
		boolean flag = false;
    String url="";
		try
		{   
			  MLogger.printInput("Shipmark ::  "+shipmark);
			
		    StrUtils su=new StrUtils();
	    	Runtime run = Runtime.getRuntime();
				StringBuffer argPrinter= new StringBuffer("C:\\props\\CibaVision\\PrintShipmark\\LabelShipMarkPrint.exe ");
        MLogger.printInput("PRINTER NAME ::  "+DbBean.SHIPMARK_PRINTER_NAME);
				argPrinter.append(DbBean.SHIPMARK_PRINTER_NAME);
        
				argPrinter.append( " " + shipmark);
       //// System.out.println("shipmark............"+shipmark);
        MLogger.printInput("PRINTER NAME ::  "+argPrinter.toString());
				Process python = run.exec(argPrinter.toString());
        //Runtime.getRuntime().exec("cmd.exe /c start " +argPrinter.toString() );
				flag = true;
        
				}
		catch (Exception e) {
    System.out.println(e.toString());
			flag = false;
			throw e;
		}
		
		
		return flag;
	}*/
	
	
	// Method modified by Arun for #1848-- original method commented above
	public boolean Print_Shipmark(String shipmark) throws Exception {
		MLogger.info(" Print_Shipmark() starts");
		boolean flag = false;
		try
		{   
	    	Runtime run = Runtime.getRuntime();
			// StringBuffer argPrinter= new StringBuffer("C:\\props\\CibaVision\\PrintShipmark\\LabelShipMarkPrint.exe ");
			StringBuffer argPrinter= new StringBuffer("C:\\props\\CibaVision\\PrintShipmark\\LabelShipMarkPrint.exe ");
			argPrinter.append( " " + shipmark);
			MLogger.printInput("Ship Mark Command ::::::::::::::::::::::::  "+argPrinter.toString());
			Process python = run.exec(argPrinter.toString());
			flag = true;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			flag = false;
			throw e;
		}
		return flag;
	}
  
  
  	// Commented by Arun and modifed for #1848
   /*public boolean GetPrintShippingMarkDetail(String totalString,String user_id,String TRN_DATE,int loopsize) throws Exception{
   
    //Created By:Bruhanudeen
   // Create date: 20100305
   // For:Cibavavision WMS SAP Integration  PhaseII 
    //Description:To PrintShipmark based on DeliveryNo and Pallet  
  
      MLogger.log(1, this.getClass() + " GetPrintShippingMarkDetail");
    boolean deletedHdr = false;
    boolean deletedDet = true;
    String QueryHdr="";
    String QueryDet="";
    String sepratedtoken1="";
    BaseDAO _BaseDAO = new BaseDAO();
    java.sql.Connection con=null;
    Map mp = null;mp = new HashMap();

    con=DbBean.getConnection();
    ArrayList aList=null;
    StrUtils su=new StrUtils();
    String shipto="";
    String contactname="";
    String companyname="";
    String addr1="";
    String addr2="";
    String addr3="";
    String state="";
    String DefaultBin="bin";
    
   String printername=DbBean.SHIPMARK_PRINTER_NAME;
  //String printername= "CutePDF Writer";
         

    boolean isExists=false;
    boolean isFlag=false;
    try{
     
     String sepratedtoken="";
     
     
     StringTokenizer parser = new StringTokenizer(totalString,"=");
   
    
     
        while (parser.hasMoreTokens())
        
              {
                 int count=1;                  
                 MLogger.info("countTokens ::"+parser.countTokens());
                
                 sepratedtoken = parser.nextToken();
                 
                 MLogger.info("###################### sepratedtoken #####################"+sepratedtoken);
                 
                StringTokenizer parser1 = new StringTokenizer(sepratedtoken,",");
                  
                 while (parser1.hasMoreTokens())
                  
                {
                  sepratedtoken1 = parser1.nextToken();
                           
                  mp.put("data"+count,sepratedtoken1);
                           
                  count++;
                           
                  MLogger.info("###################### sepratedtoken1 #####################"+sepratedtoken1);
                           
                }
                   Runtime run = Runtime.getRuntime();
                   aList= getPrintingMarkDet((String)mp.get("data1"));
                
                   boolean flag=false;
                   for (int iCnt =0; iCnt<aList.size(); iCnt++){
                       
                        Map lineArr = (Map) aList.get(iCnt);
                        companyname=(su.replaceSpace2Send((String)lineArr.get("COMPANYNAME")));
                        addr1=(su.replaceSpace2Send((String)lineArr.get("ADDR1")));
                        addr2=(su.replaceSpace2Send((String)lineArr.get("ADDR2")));
                        addr3=(su.replaceSpace2Send((String)lineArr.get("ADDR3")));
                        shipto=(su.replaceSpace2Send((String)lineArr.get("COUNTRY")));
                        contactname=(su.replaceSpace2Send((String)lineArr.get("CONTACTNAME")));
                        int iIndex = iCnt + 1;
                                  
                        StringBuffer argPrinter= new StringBuffer("Ship To:"+" ");
                        argPrinter.append(companyname+" ");
                        argPrinter.append(addr1+" ");
                        argPrinter.append(addr2+" ");
                        argPrinter.append(addr3+" ");
                        argPrinter.append(shipto+" ");
                        argPrinter.append("Attn : "+ contactname+" ");
                        // argPrinter.append((String)mp.get("data3") + " " + "#"+ " " + ":"+ "" + aList.size() + "" + "of" + ""+ iIndex);
                        argPrinter.append((String)mp.get("data4")+" ");
                        argPrinter.append((String)mp.get("data3")+" ");
                        argPrinter.append(loopsize);
                      
                        flag= Print_Shipmark(argPrinter.toString());
                  
         
                   }
                          
              
       }
    }catch(Exception e){ 
    throw e;
    }
    finally
    {
      DbBean.closeConnection(con);
       MLogger.log( -1, this.getClass() + "  GetPrintShippingMarkDetail()");
    }
    return deletedDet;
  }*/
  	
  	public boolean GetPrintShippingMarkDetail(String totalString,String user_id,String TRN_DATE,int loopsize, String printerName, String printCopies) throws Exception{
  	   /*
  	    Created By:Bruhanudeen
  	    Create date: 20100305
  	    For:Cibavavision WMS SAP Integration  PhaseII 
  	    Description:To PrintShipmark based on DeliveryNo and Pallet  */
  	  
  	      MLogger.log(1, this.getClass() + " GetPrintShippingMarkDetail");
  	      System.out.println(" Total String++++++++++++++++++++++++++++"+totalString);
  	   // boolean deletedHdr = false;
  	    boolean deletedDet = true;
  	    //String QueryHdr="";
  	    //String QueryDet="";
  	    String sepratedtoken1="";
  	    //BaseDAO _BaseDAO = new BaseDAO();
  	    java.sql.Connection con=null;
  	    Map mp = null;mp = new HashMap();

  	    con=DbBean.getConnection();
  	    ArrayList aList=null;
  	    StrUtils su=new StrUtils();
  	    String shipto="";
  	    String contactname="";
  	    String companyname="";
  	    String printername="";
  	    String addr1="";
  	    String addr2="";
  	    String addr3="";
  	    //String state="";
  	    //String DefaultBin="bin";
  	    
  	   //String printername=DbBean.SHIPMARK_PRINTER_NAME;
  	  //String printername= "CutePDF Writer";
  	         

  	   // boolean isExists=false;
  	   // boolean isFlag=false;
  	    try{
  	     
  	     String sepratedtoken="";
  	     
  	     
  	     StringTokenizer parser = new StringTokenizer(totalString,"=");
  	   
  	    
  	     
  	        while (parser.hasMoreTokens())
  	        
  	              {
  	                 int count=1;                  
  	                 MLogger.info("countTokens ::"+parser.countTokens());
  	                
  	                 sepratedtoken = parser.nextToken();
  	                 
  	                 MLogger.info("###################### sepratedtoken #####################"+sepratedtoken);
  	                 
  	                StringTokenizer parser1 = new StringTokenizer(sepratedtoken,",");
  	                  
  	                 while (parser1.hasMoreTokens())
  	                  
  	                {
  	                  sepratedtoken1 = parser1.nextToken();
  	                           
  	                  mp.put("data"+count,sepratedtoken1);
  	                           
  	                  count++;
  	                           
  	                  MLogger.info("###################### sepratedtoken1 #####################"+sepratedtoken1);
  	                           
  	                }
  	                   //Runtime run = Runtime.getRuntime();
  	                   aList= getPrintingMarkDet((String)mp.get("data1"));
  	                
  	                   boolean flag=false;
  	                   for (int iCnt =0; iCnt<aList.size(); iCnt++){
  	                       
  	                        Map lineArr = (Map) aList.get(iCnt);
//  	                    Printer name added by Arun for #1848
  	                        printername=(su.replaceSpace2Send(printerName));
  	                        printername = StrUtils.forHTMLTag(printername);
  	                        
  	                        companyname=(su.replaceSpace2Send((String)lineArr.get("COMPANYNAME")));
  	                        companyname = StrUtils.forHTMLTag(companyname);
  	                        
  	                        addr1=(su.replaceSpace2Send((String)lineArr.get("ADDR1")));
  	                        addr1 = StrUtils.forHTMLTag(addr1);
  	                      
  	                        addr2=(su.replaceSpace2Send((String)lineArr.get("ADDR2")));
  	                        addr2 = StrUtils.forHTMLTag(addr2);
  	                        
  	                        addr3=(su.replaceSpace2Send((String)lineArr.get("ADDR3")));
  	                        addr3 = StrUtils.forHTMLTag(addr3);
  	                      
  	                        shipto=(su.replaceSpace2Send((String)lineArr.get("COUNTRY")));
  	                        shipto = StrUtils.forHTMLTag(shipto);
  	                        
  	                        contactname=(su.replaceSpace2Send((String)lineArr.get("CONTACTNAME")));
  	                        contactname = StrUtils.forHTMLTag(contactname);
  	                        //int iIndex = iCnt + 1;
  	                        
  	                        // Printer name added by Arun for #1848
  	                        StringBuffer argPrinter= new StringBuffer(printername+" ");
  	                        argPrinter.append("Ship To:"+" ");
  	                        argPrinter.append(companyname+" ");
  	                        argPrinter.append(addr1+" ");
  	                        argPrinter.append(addr2+" ");
  	                        argPrinter.append(addr3+" ");
  	                        argPrinter.append(shipto+" ");
  	                        argPrinter.append("Attn : "+ contactname+" ");
  	                        // argPrinter.append((String)mp.get("data3") + " " + "#"+ " " + ":"+ "" + aList.size() + "" + "of" + ""+ iIndex);
  	                        argPrinter.append((String)mp.get("data4")+" ");// pallet/box
  	                        System.out.println("data4"+(String)mp.get("data4")); 
  	                        argPrinter.append((String)mp.get("data3")+" ");// 1 form 1 of 4
  	                        System.out.println("data3"+(String)mp.get("data3"));
  	                        argPrinter.append(loopsize); // 4 from 1 of 4
  	                        // Added by Arun for #1848
  	                        argPrinter.append(" Delivery"+" ");
  	                        argPrinter.append((String)mp.get("data1")+" "); // Append Delivery No.
  	                        argPrinter.append("Pallet ID"+" ");
	                        argPrinter.append((String)mp.get("data2")+" "); // Append Pallet Id
	                        argPrinter.append(printCopies); // Append Pallet Id
  	                      // method call modifed by Arun for #1848
  	                        //flag= Print_Shipmark(argPrinter.toString());
  	                        System.out.println("Printer Name------------------->"+printerName);
  	                        flag= Print_Shipmark(argPrinter.toString());
  	                  
  	         
  	                   }
  	                          
  	              
  	       }
  	    }catch(Exception e){ 
  	    throw e;
  	    }
  	    finally
  	    {
  	      DbBean.closeConnection(con);
  	       MLogger.log( -1, this.getClass() + "  GetPrintShippingMarkDetail()");
  	    }
  	    return deletedDet;
  	  }
  
  	
  public ArrayList getPrintingMarkDet(String traveler) throws Exception{
  /*
   Created By:Bruhanudeen
   Create date: 20100305
   For:Cibavavision WMS SAP Integration  PhaseII 
   Description:To getCustomerDetail  based on DeliveryNo   */
    MLogger.log(1, this.getClass() + "  getPintingMarkDet()");
    
    ArrayList arrList = new ArrayList();
    Connection con=null;
    con = DbBean.getConnection();
    Statement stmt = null;
    ResultSet rs = null;
    Map map = null;
    
    try {
  
         //Hashtable ht = new Hashtable();
      
         StringBuffer sql = new StringBuffer("SELECT A.SHIP_TO AS SHIPTO,A.CONTACT_NAME AS CONTACTNAME,A.COMPANY_NAME AS COMPANYNAME,  ");
         sql.append(" ISNULL(A.ADDR1,'')ADDR1,ISNULL(A.ADDR2,'') ADDR2,ISNULL(A.ADDR3,'') ADDR3,ISNULL(A.STATE,'')STATE,A.COUNTRY ");
         sql.append(" FROM CUSTMST A,OB_TRAVEL_HDR B WHERE A.SHIP_TO=B.COUNTRY AND A.TYPE='" + MDbConstant.SHIPMARKTYPE  +"' AND B.TRAVELER= '" + traveler + "'   ");
               
         
      
         stmt = con.createStatement();
         rs = stmt.executeQuery(sql.toString());  
         
          while (rs.next()) {
           map = new HashMap();
           for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
           }
           arrList.add(map);
         }
        MLogger.log(0," getPintingMarkDet(aQuery)::"+sql);
    
      }
  
    catch (Exception e) {
      MLogger.log("Exception : getPintingMarkDet ::  getPintingMarkDet:" + e.toString());
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
    MLogger.log( -1, this.getClass() + "  getPintingMarkDet()");
    return arrList;
  }
  
}