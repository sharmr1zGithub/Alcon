package com.murho.db.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.transaction.UserTransaction;

import com.murho.DataDownloader.ExcelDownloader;
import com.murho.dao.BaseDAO;
import com.murho.gates.DbBean;

public class InvoiceDownloadUtil 
{
  public InvoiceDownloadUtil()
  {
  }
  
  public static String DB_PROPS_FILE = "C:/props/CibaVision/config/CibaVisionConstants.properties";
  private static String NumberOfColumn;
  private static String ExcelSheetName;
  private static String TableFiled1;
  private static String TableFiled2;
  private static String TableFiled3;
  private static String TableFiled4;
  private static String TableFiled5;
  private static String TableFiled6;
   private static String TableFiled7;

  private static Map mapTableFileld=null;
  private static String ImportTableName=null;
  private static String ReadFromTray  =null;
  private static String CopyToInTray =null;
  private static String ReadFromServerFolder_shipment =null;
   
   
 
  static {
    Properties dbpr;
    InputStream dbip;
    mapTableFileld=new HashMap();
    try {
      dbip = new FileInputStream(new File(DB_PROPS_FILE));
      dbpr = new Properties();
      dbpr.load(dbip);
     
      
      NumberOfColumn  = dbpr.getProperty("NumberOfColumn");
      ImportTableName = dbpr.getProperty("ImportTableName");
      ExcelSheetName  = dbpr.getProperty("ExcelSheetName");
      ReadFromTray    = dbpr.getProperty("ReadFromTray ");
      CopyToInTray    = dbpr.getProperty("CopyToInTray");
     
      ReadFromServerFolder_shipment  = dbpr.getProperty("ReadFromServerFolder_shipment");
      
      System.out.println(" *********************** ReadFromServerFolder_shipment=  : " + ReadFromServerFolder_shipment);
     
      for(int i=1; i<=Integer.parseInt(NumberOfColumn);i++)
      {
        mapTableFileld.put("STableFiled" + i,dbpr.getProperty("STableFiled" + i));
      }
       
      System.out.println("Total Number of column to import : " + NumberOfColumn);
      
       for(int i=1; i<=Integer.parseInt(NumberOfColumn);i++)
       {
         System.out.println("STableFiled" +i+ " : " + mapTableFileld.get("STableFiled" + i));
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
    System.out.println("******** Invoice Import Starts *********");
   
    // to get file name
    File f=new File(filename);
    filename=f.getName();
    f=null;
    //
     System.out.println("After extraction filename : " + filename);
     System.out.println("ReadFromServerFolder_shipment  :" + ReadFromServerFolder_shipment);
     filename=ReadFromServerFolder_shipment + filename;
  
    System.out.println("Import File :" + filename);
    System.out.println("Import sheet :" + sheetName);
    
    StringBuffer sbQuery=new StringBuffer("");
    boolean tranFlag=false;
    String totalSku="";
    int TRAVELER=1;
    int SINO=0;
    String outFilename ="";
    
    UserTransaction ut=null ;
    java.sql.Connection con=null;
   
    try {  
               
            ut = DbBean.getUserTranaction();
            ut.begin(); 
            con=DbBean.getConnection();
            BaseDAO _BaseDAO = new BaseDAO();
            
            String[] str= ExcelDownloader.readExcelFile(filename, sheetName, Integer.parseInt(NumberOfColumn));
            long TotalRecord=str.length;
            System.out.println("Total number of record found in Excel sheet  :  " + str.length);
            for(int j=0;j<str.length;j++){
                String s1=str[j];
                SINO++;
              ///////  if(j<2)continue;
                StringTokenizer parser = new StringTokenizer(s1,",");
                
                System.out.println("Record " + j + "\t: " + s1);
               
                List list = new LinkedList();    // Doubly-linked list
                list = new ArrayList();   
                            
                 while (parser.hasMoreTokens()) {
                                   
                 list.add(parser.nextToken());     
                 
                }
                
              
               System.out.println(" list.size() " + list.size());
            
            
              if(list.size()<Integer.parseInt(NumberOfColumn)){
              
               TRAVELER++;
               SINO=0;
               System.out.println("TRAVELER :" + TRAVELER);
               continue;
               
              }
              
               
                sbQuery.append("insert into ");
                sbQuery.append(" TEMP_SHIP_DATA " + "(");
             
                for(int i=1; i<=Integer.parseInt(NumberOfColumn);i++)
                {
                       
                 sbQuery.append(((String)mapTableFileld.get("STableFiled"+i)).substring(1)+",");
            
                }
                //to remove last comma
                sbQuery.replace(sbQuery.length()-1,sbQuery.length(),"");
                sbQuery.append(" )VALUES(" );
                for(int i=0;i<list.size();i++)
                {
              
                char ch= ((String)mapTableFileld.get("STableFiled"+(i+1))).charAt(0);
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
                sbQuery.replace(sbQuery.length()-1,sbQuery.length(),"");
                sbQuery.append(")");
                // insert to db
                System.out.println("Query: " + sbQuery.toString());
                tranFlag=false;
                try{
                tranFlag=_BaseDAO.insertData(con,sbQuery.toString());
                
                }
                catch(Exception e)
                {
                 // throw new Exception("Unable to download Traveler : " + TRAVELER + " This may be downloded already" + " \nError :: " + e.getMessage());
                throw e;
                }
                System.out.println("Data inserted to TEMO_SHIP_DATA : " + tranFlag);
              
                sbQuery=new StringBuffer("");
            }//end of for loop for all traveler

             if(tranFlag)
             {
                DbBean.CommitTran(ut);
             }
             else
             {
               throw new Exception("Unable to download chech the excel file");
             }
             
        }catch (Exception ex) {
            ex.printStackTrace();
             DbBean.RollbackTran(ut);
            throw ex;
        }
         finally{
          if (con != null) {
          DbBean.closeConnection(con);
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
}