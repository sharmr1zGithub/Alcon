package com.murho.db.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.CallableStatement;
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
import com.murho.utils.CibaConstants;
import com.murho.utils.DateUtils;
import com.murho.utils.MLogger;

public class DataDownloaderUtil1
{
  public DataDownloaderUtil1()
  {
  }
  public static String DB_PROPS_FILE = "c:/props/CibaVision/config/CibaVisionConstants.properties";
  private static String NumberOfColumn;
  private static String ExcelSheetName;
  private static String TableFiled1;
  private static String TableFiled2;
  private static String TableFiled3;
  private static String TableFiled4;
  private static String TableFiled5;
  private static String TableFiled6;
  private static Map mapTableFileld=null;
  private static String ImportTableName=null;
  private static String ReadFromTray  =null;
  private static String CopyToInTray =null;
  private static String ReadFromClient =null;
   
   
 
  static {
    Properties dbpr;
    InputStream dbip;
    mapTableFileld=new HashMap();
    try {
      dbip = new FileInputStream(new File(DB_PROPS_FILE));
      dbpr = new Properties();
      dbpr.load(dbip);
      //strDebug = dbpr.getProperty("DEBUG");
      //refBoolean = new Boolean(strDebug);
      
      NumberOfColumn  = dbpr.getProperty("NumberOfColumn");
      ImportTableName = dbpr.getProperty("ImportTableName");
      ExcelSheetName  = dbpr.getProperty("ExcelSheetName");
      ReadFromTray           = dbpr.getProperty("ReadFromTray ");
      CopyToInTray    = dbpr.getProperty("CopyToInTray");
      ReadFromClient  = dbpr.getProperty("ReadFromClient");
      System.out.println("ExcelSheetName : " + ExcelSheetName);
      for(int i=1; i<=Integer.parseInt(NumberOfColumn);i++)
      {
        mapTableFileld.put("TableFiled" + i,dbpr.getProperty("TableFiled" + i));
      }
    //  TableFiled1 = dbpr.getProperty("TableFiled1"); //  Database Driver
    //  TableFiled2 = dbpr.getProperty("TableFiled2");
    //  TableFiled3 = dbpr.getProperty("TableFiled3");
    //  TableFiled4 = dbpr.getProperty("TableFiled4");
    //  TableFiled5 = dbpr.getProperty("TableFiled5");
   //   TableFiled6 = dbpr.getProperty("TableFiled6");
     
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
  
 //  String  filename="D:\\DEVELOPMENT\\CIBA-VISION\\DOCUMENTS\\CVAML1338-1340_Trav.xls\\CVAML1338-1340_Trav2.xls";
 // String sheetName="1338TRAV";
 //  int NumberOfColumn1=8;
  public static String getExcelSheetName()
  {
    return ExcelSheetName;
  }
    
  public  boolean downloadData(String filename,String sheetName) throws Exception
  {
    System.out.println("******** Data Import Starts *********");
   // filename="D:\\DEVELOPMENT\\CIBA-VISION\\DOCUMENTS\\Inbound-Travelers\\CVAML1338-1340_Trav.xls\\CVAML1338-1340_Trav2.xls";
   //sheetName="1338TRAV";
    System.out.println("Import File :" + filename);
    System.out.println("Import sheet :" + sheetName);
    StringBuffer sbQuery=new StringBuffer("");
    boolean tranFlag=false;
    String totalSku="";
    String TRAVELER="";
    String outFilename ="";
    
     UserTransaction ut=null ;
     java.sql.Connection con=null;
     
     File newFile=new File(filename);
     String fname=newFile.getName();
     int chr;

     try {


   //   java.net.InetAddress i = java.net.InetAddress.getLocalHost();
   //   String ServerName =  i.getHostName();
   //   System.out.println("Host Name for InTray :"+ServerName);
   //   outFilename = "//"+ServerName+CopyToInTray+fname;
   //   System.out.println("Copy to outFilename  :"+outFilename);
  //    BufferedInputStream in = new BufferedInputStream ( new FileInputStream (filename) );
  //    BufferedOutputStream out = new BufferedOutputStream ( new FileOutputStream (outFilename) );
   //   while( (chr = in.read()) != -1 ) {
    //    out.write(chr);
   //   }
   //   in.close();
   //   out.flush();
   //   out.close();
           
   filename="D:\\MURHO\\WMS_UPLOAD\\" + filename;
  
   
   //  String dir="D:\\MURHO\\WMS_UPLOAD\\" + filename;
   
     // filename = ReadFromTray +fname;
      
      System.out.println("Server InTray Path : "+filename);
    } catch (Exception e) {
      System.out.println("Coping file Exception " + e.toString());
    }
    
    try {  
    
           
            ut = DbBean.getUserTranaction();
            ut.begin(); 
            con=DbBean.getConnection();
            BaseDAO _BaseDAO = new BaseDAO();
            
            String[] str= ExcelDownloader.readExcelFile(filename, sheetName, Integer.parseInt(NumberOfColumn));
            System.out.println("Total number of record found in Excel sheet  :  " + str.length);
            for(int j=0;j<str.length-1;j++){
                String s1=str[j];
                System.out.println("Record111 " + j + "\t: " + s1);
                //  String aString = "word1 word2 word3";
                if(j<5)continue;
                StringTokenizer parser = new StringTokenizer(s1,",");
                
            //     if(j>str.length-4)break;
                System.out.println("Record " + j + "\t: " + s1);
               
                List list = new LinkedList();    // Doubly-linked list
                list = new ArrayList();   
                            
                 while (parser.hasMoreTokens()) {
                                   
                 list.add(parser.nextToken());     
                 
                }
                
              
               System.out.println(" list.size() " + list.size());
            
            
              if(list.size()<Integer.parseInt(NumberOfColumn)){
              System.out.println(list.size()  + " < " + NumberOfColumn);
             //   System.out.println(" *********** Now system need to break **********");
                totalSku=(String)list.get(1);
                System.out.println("********* Total SKU found in Traveler  : " + totalSku);
                   break;
             //   if(j>str.length-4)break;
                }
             
              
                TRAVELER=(String)list.get(5);
              //  System.out.println(" TRAVELER : " + TRAVELER);  
               System.out.println("\n");  
                sbQuery.append("insert into ");
                sbQuery.append(" RECVDET " + "(");
             
                for(int i=1; i<=Integer.parseInt(NumberOfColumn);i++)
                {
                       
                 sbQuery.append(((String)mapTableFileld.get("TableFiled"+i)).substring(1)+",");
                 
                 //mapTableFileld.get("TableFiled" + i
                }
                //to remove last comma
                sbQuery.replace(sbQuery.length()-1,sbQuery.length(),"");
                sbQuery.append(" )VALUES(" );
                for(int i=0;i<list.size();i++)
                {
              
                char ch= ((String)mapTableFileld.get("TableFiled"+(i+1))).charAt(0);
                String chartemp = String.valueOf(ch);
            //    MLogger.info("chartemp : " +  chartemp);
            
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
              //  sbQuery.append(");");
               sbQuery.append(")");
                // insert to db
               // System.out.println("Query: " + sbQuery.toString());
                tranFlag=false;
                try{
                tranFlag=_BaseDAO.insertData(con,sbQuery.toString());
                }
                catch(Exception e)
                {
                  throw new Exception("Unable to download Traveler : " + TRAVELER + " This may be downloded already" + " \nError :: " + e.getMessage());
                }
                System.out.println("Data inserted to RECVDET : " + tranFlag);
              
                sbQuery=new StringBuffer("");
            }
            
            System.out.println("TRAVELER :: " + TRAVELER);
            System.out.println("Total Number of SKU :: " + totalSku);
         
            
            tranFlag=_BaseDAO.insertData(con,"update recvDet set receiveStatus='N', recvQty=0 where Traveler='" + TRAVELER + "'");
            ArrayList al=null;
            al=_BaseDAO.selectData(con,"select sum(OrdQty) as qty from recvdet where Traveler ='"+TRAVELER+"'");
            String totalSkuDownloaded=(String)((Map)(al.get(0))).get("qty");
            System.out.println("Total SKU found in RECVDET  : " + totalSkuDownloaded);
            // REMOVED SOME TIME GIVES PROBLEM
           /*
            if(Float.parseFloat(totalSkuDownloaded)==Float.parseFloat(totalSku))
            {
             System.out.println(Float.parseFloat(totalSkuDownloaded) + "==" +  Float.parseFloat(totalSku));
            }
            else
            {
              System.out.println(Float.parseFloat(totalSkuDownloaded) + "!=" +  Float.parseFloat(totalSku));
              throw new Exception("Check SKU in Traveler file which is not tally with imported SKU");
            }
            */
            //inserting to RECVHDR
            
             StringBuffer sbRecvHdr=new StringBuffer("");
             sbRecvHdr.append("INSERT INTO RECVHDR(TRAVELER,ReceiveStatus,PutawayStatus,STATUS,CRAT,CRBY)VALUES(");
            // sbRecvHdr.append("'" + +"'");
             sbRecvHdr.append("'" + TRAVELER +"',");
             sbRecvHdr.append("'" + "N" +"',");
             sbRecvHdr.append("'" + "N" +"',");
             sbRecvHdr.append("'" + "N" +"',");
             sbRecvHdr.append("'" + DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()) +"',");
             sbRecvHdr.append("'" + "ADMIN1" +"'");
             sbRecvHdr.append(")");
             //inserting data to recvHdr
             if(tranFlag)   tranFlag=_BaseDAO.insertData(con,sbRecvHdr.toString());
             System.out.println("Data inserted to RECVHDR : " + tranFlag);
            
             // ALLOCATING LOCATION
              
               System.out.println("********* ALLOCATING LOCATION " + tranFlag);
            
                CallableStatement colStmt = null;
             
                String sp = "exec PROC_CALL_ALLOCATELOCATION '" +""+ "','"+TRAVELER +"' ";
                System.out.println("Executing procedure : " + sp);
                colStmt   = con.prepareCall(sp);
                int iCnt=0;
                
                 try{
                    iCnt = colStmt.executeUpdate();
                  MLogger.info("PROC_CALL_ALLOCATELOCATION : " + iCnt);
        
                 }catch(Exception e) {
    
                       MLogger.exception(this,e);
                   throw e;
                  }
              
           //NEED TO INSERT INTO MOVHIS
            
             StringBuffer sbMovHis=new StringBuffer("");
             sbMovHis.append("INSERT INTO MOVHIS(PLANT,TRAVELER,MOVTID,QTY,CRAT,CRBY)VALUES(");
            // sbRecvHdr.append("'" + +"'");
             sbMovHis.append("'" + CibaConstants.cibacompanyName +"',");
             sbMovHis.append("'" + TRAVELER +"',");
             sbMovHis.append("'" + "IMPORT-TRAVELER" +"',");
             sbMovHis.append("'" + totalSkuDownloaded +"',");
             sbMovHis.append("'" + DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()) +"',");
             sbMovHis.append("'" + "ADMIN1" +"'");
             sbMovHis.append(")");
             //inserting data to recvHdr
             if(tranFlag)   tranFlag=_BaseDAO.insertData(con,sbMovHis.toString());
             System.out.println("Data inserted to RECVHDR : " + tranFlag);
             
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