package com.murho.db.utils;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.transaction.UserTransaction;

import com.murho.DO.TempDet_DO;
import com.murho.DO.TransactionDTO;
import com.murho.DO.TravelDet_DO;
import com.murho.DataDownloader.ExcelDownloader;
import com.murho.dao.BaseDAO;
import com.murho.dao.DAOFactory;
import com.murho.dao.SQLServerDAOFactory;
import com.murho.dao.SQLTempDet_DAO;
import com.murho.dao.SQLTravelDet_DAO;
import com.murho.gates.DbBean;
import com.murho.utils.MLogger;
public class SoDownloaderUtil 
{
  public SoDownloaderUtil()
  {
  }
  
  public static String DB_PROPS_FILE = "C:/props/CibaVision/config/CibaVisionConstants.properties";
  private static String NumberOfColumn;
  /*private static String ExcelSheetName;
  private static String TableFiled1;
  private static String TableFiled2;
  private static String TableFiled3;
  private static String TableFiled4;
  private static String TableFiled5;
  private static String TableFiled6;*/
  private static Map mapTableFileld=null;
  /*private static String ImportTableName=null;
  private static String ReadFromTray  =null;
  private static String CopyToInTray =null;*/
  private static String ReadFromServerFolder_so =null;
 // public boolean detOutflag=false;
 // public String rtnOutMsg="";
  public ArrayList outList;
   
  static {
    Properties dbpr;
    InputStream dbip;
    mapTableFileld=new HashMap();
    try {
      dbip = new FileInputStream(new File(DB_PROPS_FILE));
      dbpr = new Properties();
      dbpr.load(dbip);
     
      
      NumberOfColumn  = dbpr.getProperty("SO_NumberOfColumn");
      /*ImportTableName = dbpr.getProperty("SO_ImportTableName");
      ExcelSheetName  = dbpr.getProperty("ExcelSheetName");
      ReadFromTray           = dbpr.getProperty("ReadFromTray ");
      CopyToInTray    = dbpr.getProperty("CopyToInTray");*/
     
      ReadFromServerFolder_so  = dbpr.getProperty("ReadFromServerFolder_so");
      
      System.out.println(" *********************** ReadFromServerFolder_so : " + ReadFromServerFolder_so);
     
      for(int i=1; i<=Integer.parseInt(NumberOfColumn);i++)
      {
        mapTableFileld.put("TableFiled" + i,dbpr.getProperty("TableFiled" + i));
      }
      
      System.out.println("Total Number of column to import : " + NumberOfColumn);
      
       for(int i=1; i<=Integer.parseInt(NumberOfColumn);i++)
       {
         System.out.println("TableFiled" +i+ " : " + mapTableFileld.get("TableFiled" + i));
       }
    }
    catch (FileNotFoundException fnfe) {
      System.out.println("Exception : " + fnfe.getMessage());
    }
    catch (Exception e) {
      System.out.println("Exception : " + e.getMessage());
    }
  }
      
  public  boolean downloadData(String filename,String sheetName) throws Exception
  {
    System.out.println("******** SO Import Starts *********");
   
    // to get file name
    File f=new File(filename);
    filename=f.getName();
    f=null;
    //
     System.out.println("After extraction filename : " + filename);
     System.out.println("ReadFromServerFolder_so :" + ReadFromServerFolder_so);
     filename=ReadFromServerFolder_so + filename;
  
    System.out.println("Import File :" + filename);
    System.out.println("Import sheet :" + sheetName);
    
    StringBuffer sbQuery=new StringBuffer("");
    boolean tranFlag=false;
    //String totalSku="";
    int TRAVELER=1;
    int SINO=0;
    //String outFilename ="";
    
    UserTransaction ut=null ;
    java.sql.Connection con=null;
    java.sql.Connection con1=null;
   
    try {  
               
            ut = DbBean.getUserTranaction();
            ut.begin(); 
            con=DbBean.getConnection();
            con1=DbBean.getConnection();
            BaseDAO _BaseDAO = new BaseDAO();
            String delQuery  = "delete from tempdatatable" ;
            boolean delFlag = _BaseDAO.DeleteRow(con1,delQuery);
            String[] str= ExcelDownloader.readExcelFile(filename, sheetName, Integer.parseInt(NumberOfColumn));
            long TotalRecord=str.length;
            System.out.println("Total number of record found in Excel sheet  :  " + str.length);
            for(int j=0;j<str.length;j++){
                String s1=str[j];
                SINO++;
                StringTokenizer parser = new StringTokenizer(s1,",");
                
                System.out.println("Record " + j + "\t: " + s1);
               
                List list = new LinkedList();    // Doubly-linked list
                list = new ArrayList();   
                            
                 while (parser.hasMoreTokens()) {                   
                 list.add(parser.nextToken());     
                 
                }          
            
              if(list.size()<Integer.parseInt(NumberOfColumn)){
              
               TRAVELER++;
               SINO=0;
               continue;
               
              }
             
                sbQuery.append("insert into ");
                sbQuery.append(" TempDataTable " + "(");
             
                for(int i=1; i<=Integer.parseInt(NumberOfColumn);i++)
                {
                       
                 sbQuery.append(((String)mapTableFileld.get("TableFiled"+i)).substring(1)+",");
                
                }
                 sbQuery.append("Traveler,");
                 sbQuery.append("SNO,");
                 sbQuery.append("LOTTYPE");
                 sbQuery.append(" )VALUES(" );
                for(int i=0;i<list.size();i++)
                {
              
                char ch= ((String)mapTableFileld.get("TableFiled"+(i+1))).charAt(0);
                String chartemp = String.valueOf(ch);
            
                if(chartemp.equalsIgnoreCase("i"))
                {
                   sbQuery.append("" + list.get(i) +",");
                }
                else if(chartemp.equalsIgnoreCase("s"))
                {
                   sbQuery.append("'" + list.get(i) +"',");
                }
                   
                }
                String aprdclass = (String)list.get(15);
                String aDestn = (String)list.get(9);
              
                String lottype = getLotType(aprdclass,aDestn);
                sbQuery.append("'" + TRAVELER +"'");
                sbQuery.append("," + (SINO) +",");
                sbQuery.append("'" + lottype +"'");
                sbQuery.append(")");
                tranFlag=false;
                try{
                tranFlag=_BaseDAO.insertData(con,sbQuery.toString());
                
                }
                catch(Exception e)
                {
                throw e;
                }
                System.out.println("Data inserted to RECVDET : " + tranFlag);
              
                sbQuery=new StringBuffer("");
            }

             if(tranFlag)
             {
                DbBean.CommitTran(ut);
             }
             else
             {
               throw new Exception("Unable to download check the excel file");
             }
             
        }catch (Exception ex) {
            ex.printStackTrace();
             DbBean.RollbackTran(ut);
            throw ex;
        }
         finally{
          if (con != null) {
          DbBean.closeConnection(con);
          DbBean.closeConnection(con1);
          }
         }
       return tranFlag;
    
  }
  
  
  
  
  public static void main(String[] args)
  {
    DataDownloaderUtil _DataDownloaderUtil = new DataDownloaderUtil();
    try
    {
      _DataDownloaderUtil.downloadData("","","");
    }
    catch (Exception e)
    {
      
    }
  }
  
   public String getLotType(String prdclass,String shipTo) throws Exception {
   MLogger.log(1, this.getClass() + " getLotType()");
   String lottype="";
   //String xmlStr ="";
   try
   {
      StringBuffer sbQuery=new StringBuffer("");   
      sbQuery.append("SELECT type from  rules_mst where SHIP_TO='"+shipTo+"' and prd_cls_id='"+prdclass+"' group by type ");
      java.sql.Connection con=null;
      con=com.murho.gates.DbBean.getConnection();
      
      MLogger.query(" "+sbQuery.toString());
      Map m=new BaseDAO().getRowOfData(con,sbQuery.toString());
     
     lottype=(String)m.get("type");
    
    if(lottype=="" || lottype==null)
    {
      lottype="";
    }
     
  }
  catch(Exception e) {
          MLogger.log(0,"######################### Exception :: getLotType() : ######################### \n");
          MLogger.log(0,""+ e.getMessage());
          MLogger.log(0,"######################### Exception :: getLotType() : ######################### \n");
          MLogger.log(-1,"");
          throw e;
        }
    MLogger.log( -1, this.getClass() + " getLotType()");
    return lottype;
 }  
 
  public  ArrayList downloadTextData(String filename) throws Exception
  {
   outList=new ArrayList();
   FileReader fr = null;
   BufferedReader br = null;
   FileInputStream fstream=null;
   DataInputStream in=null;
       
  //  InputStreamReader isr = new InputStreamReader();
  try
  {
    fr = new FileReader(new File(filename));
    fstream = new FileInputStream(filename);
    in = new DataInputStream(fstream);
    
    /* below line coding for convert Import Text from byte to Character Using InputStreamReader with Unicode Transformation format 16(UTF-16)
       UTF-16 Format used in Java&C 
       Other two  UTF-8 used in web,UTF-32 mostly used in Unix*/
    br = new BufferedReader(new InputStreamReader(in,"UTF-16"));

    String aLine = null;
  
        while((aLine = br.readLine()) != null) {
        // aLine = br.readLine();
           ArrayList tempList = new ArrayList();     
           if(aLine != null && !aLine.equals("")) {
       
              String[] aArray = aLine.split("\t");
              for(int j=0; j < aArray.length; j++) {
                String value = aArray[j];
                tempList.add(value);
              }
            outList.add(tempList);
          }
         }
    return  outList;
  
  }
  catch (Exception ex) {
    ex.printStackTrace();
    throw ex;
  }
  finally{ 
      try
          { 
            fr.close();
            br.close();
            fstream.close();
            in.close();
          }
           catch (Exception ex) 
           {
            ex.printStackTrace();
             throw ex;
           } 
   } 
}

public int process_import(ArrayList al) throws Exception {
   boolean deleteflag=false;
   int iCntDet=0;
      boolean isExists=false;

	// UserTransaction ut = null;
		try 
      {
      
         SQLServerDAOFactory dao_factory= (SQLServerDAOFactory)DAOFactory.getDAOFactory(1);
         //dao - hdr,det
         SQLTempDet_DAO _SQLTempDet = new SQLTempDet_DAO();
         SQLTravelDet_DAO _SQLTravelDet_DAO =new  SQLTravelDet_DAO();
         //do - hdr,det
         TempDet_DO  _TempDet_DO = new TempDet_DO();
         TravelDet_DO  _TravelDet_DO =new TravelDet_DO();
         
         //delete temptable
         try
         {
           deleteflag=_SQLTempDet.deleteTempDet();
        	 
         }
         catch (Exception e) {
          System.out.println("Exception : " + e.getMessage());
          throw e;
         }
         TransactionDTO  _TransactionDTO= new TransactionDTO();
         for(int i=0;i<al.size();i++)
         {
           // detOutflag=false;
           // rtnOutMsg=" ";
            String productid="";
            String lottype="";
            _TransactionDTO= (TransactionDTO)al.get(i);
      
           //set TempDetails
           _TempDet_DO.setSno(_TransactionDTO.getSno());
           _TempDet_DO.setTraveler(_TransactionDTO.getTraveler());
           // _TravelDet_DO to Check if outbound delivery no already exist
           _TravelDet_DO.setTraveler(_TransactionDTO.getTraveler());
             // _TravelDet_DO to Check if outbound delivery no already exist end
           _TempDet_DO.setShipdate(_TransactionDTO.getShipdate());
           _TempDet_DO.setDestination(_TransactionDTO.getDestination());
           _TempDet_DO.setPt(_TransactionDTO.getPt());
           _TempDet_DO.setShipparty(_TransactionDTO.getShipparty());
           _TempDet_DO.setSku(_TransactionDTO.getSku());
           _TempDet_DO.setMaterial(_TransactionDTO.getMaterial());
           _TempDet_DO.setLotno(_TransactionDTO.getLotno());
           _TempDet_DO.setQty(_TransactionDTO.getQty());
           _TempDet_DO.setUom(_TransactionDTO.getUom());
           _TempDet_DO.setDescription(_TransactionDTO.getDescription());
           _TempDet_DO.setField15(_TransactionDTO.getField15());
           
            isExists=_SQLTravelDet_DAO.IsExistsOBDeliveryDetById(_TravelDet_DO);
            
            if(isExists==true)
            {
                 throw new Exception("Delivery No."  + " " + (_TravelDet_DO.getTraveler())+ "  Exists already");
            }
      
            //GetProductId  &LotType
            productid= _SQLTempDet.findProductClassByItem(_TransactionDTO.getMaterial()); //material = sku 
           
            if(productid.equals(""))
            {
                 throw new Exception("Error In Import Outbound Delivery No: Item" + " " +  _TransactionDTO.getMaterial()+ " " + "not found,Imports Delivery No. Failed" ) ;
            }
            
            lottype=_SQLTempDet.findLotTypeByProductClass(productid,_TempDet_DO.getDestination());
           
            if(lottype.equals(""))
            {
                 throw new Exception("Error In Importing Outbound Delivery No: Palletizing Rules for Product Class " +  " " + productid+  " " + " and Shipt To "+ _TempDet_DO.getDestination() +" is not created!" ) ;
            }
            _TempDet_DO.setProductclass(productid);
            _TempDet_DO.setLottype(lottype);
      
            iCntDet=_SQLTempDet.insertTempDet(_TempDet_DO);
            
            if (iCntDet==0)
            {
              throw new Exception("Error In Importing Outbound Delivery No:" + _TransactionDTO.getTraveler()+ " " + "Imports Delivery No. Failed") ;
               
            }
                              
          //  i=i+1;
         }
      
      return  iCntDet;
    }
     catch (Exception e) {
     System.out.println("Exception : " + e.getMessage());
     throw e;
   }
           
 }
// new method added by Arun #1851 on 15 June 2011
public int insert_IROTempData(ArrayList al) throws Exception {
	MLogger.log("start:insert_IROTempData()");   
	int iCntDet=0;
 
			try 
	      {
	      
	         SQLTempDet_DAO _SQLTempDet = new SQLTempDet_DAO();
	         TempDet_DO  _TempDet_DO = new TempDet_DO();
	        
	        
	         
	         TransactionDTO  _TransactionDTO= new TransactionDTO();
	         for(int i=0;i<al.size();i++)
	         {
	            _TransactionDTO= (TransactionDTO)al.get(i);
	      
	           //set TempDetails
	           _TempDet_DO.setSno(_TransactionDTO.getSno());
	           _TempDet_DO.setTraveler(_TransactionDTO.getTraveler());
	           _TempDet_DO.setShipdate(_TransactionDTO.getShipdate());
	           _TempDet_DO.setDestination(_TransactionDTO.getDestination());
	           _TempDet_DO.setPt(_TransactionDTO.getPt());
	           _TempDet_DO.setShipparty(_TransactionDTO.getShipparty());
	           _TempDet_DO.setSku(_TransactionDTO.getSku());
	           _TempDet_DO.setMaterial(_TransactionDTO.getMaterial());
	           _TempDet_DO.setLotno(_TransactionDTO.getLotno());
	           _TempDet_DO.setQty(_TransactionDTO.getQty());
	           _TempDet_DO.setUom(_TransactionDTO.getUom());
	           _TempDet_DO.setDescription(_TransactionDTO.getDescription());
	           _TempDet_DO.setField15(_TransactionDTO.getField15());
	           
	            iCntDet=_SQLTempDet.insertTempDet(_TempDet_DO);
	            if (iCntDet==0)
	            {
	              throw new Exception("Error In Importing Outbound Delivery No:" + _TransactionDTO.getTraveler()+ " " + "Imports Delivery No. Failed") ;
	               
	            }
	            
	         }

		     MLogger.log("end:insert_IROTempData()");  
	      return  iCntDet;
	    }
	     catch (Exception e) {
	     System.out.println("Exception : " + e.getMessage());
	     throw e;
	   }
	           
	 }
}