package com.murho.db.utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.transaction.UserTransaction;

import com.murho.DO.RecvDet_DO;
import com.murho.DO.RecvHdr_DO;
import com.murho.DO.TransactionDTO;
import com.murho.DO.TravelDet_DO;
import com.murho.DataDownloader.ExcelDownloader;
import com.murho.dao.BaseDAO;
import com.murho.dao.DAOFactory;
import com.murho.dao.SQLRecvDet_DAO;
import com.murho.dao.SQLRecvHdr_DAO;
import com.murho.dao.SQLServerDAOFactory;
import com.murho.dao.SQLTempDet_DAO;
import com.murho.dao.SQLTravelDet_DAO;
import com.murho.dao.SQLTravelHdr_DAO;
import com.murho.dao.TrayDetDAO;
import com.murho.gates.DbBean;
import com.murho.gates.userBean;
import com.murho.utils.CibaConstants;
import com.murho.utils.DateUtils;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;

public class DataDownloaderUtil
{
  public DataDownloaderUtil()
  {
  }
  public static String DB_PROPS_FILE = "C:/props/CibaVision/config/CibaVisionConstants.properties";
  private static String In_NumberOfColumn;
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
  private static String ReadFromServerFolder =null;
  public static String ReadFromServerFolder_so =null;
  
  public static String rtnMsg="";
  public static boolean detflag=false;
  
  public ArrayList fileList=null;
   
   
 
  static {
    Properties dbpr;
    InputStream dbip;
    mapTableFileld=new HashMap();
    try {
      dbip = new FileInputStream(new File(DB_PROPS_FILE));
      dbpr = new Properties();
      dbpr.load(dbip);
     
      
      In_NumberOfColumn  = dbpr.getProperty("In_NumberOfColumn");
      ImportTableName = dbpr.getProperty("ImportTableName");
      ExcelSheetName  = dbpr.getProperty("ExcelSheetName");
      ReadFromTray           = dbpr.getProperty("ReadFromTray");
      CopyToInTray    = dbpr.getProperty("CopyToInTray");
      ReadFromServerFolder  = dbpr.getProperty("ReadFromServerFolder");
      ReadFromServerFolder_so  = dbpr.getProperty("ReadFromServerFolder_so");
      System.out.println("ReadFromServerFolder_so -------- : " + ReadFromServerFolder_so);
     
      for(int i=1; i<=Integer.parseInt(In_NumberOfColumn);i++)
      {
        mapTableFileld.put("In_TableFiled" + i,dbpr.getProperty("In_TableFiled" + i));
      }
    //  TableFiled1 = dbpr.getProperty("TableFiled1"); //  Database Driver
    //  TableFiled2 = dbpr.getProperty("TableFiled2");
    //  TableFiled3 = dbpr.getProperty("TableFiled3");
    //  TableFiled4 = dbpr.getProperty("TableFiled4");
    //  TableFiled5 = dbpr.getProperty("TableFiled5");
   //   TableFiled6 = dbpr.getProperty("TableFiled6");
     
      System.out.println("Total Number of column to import : " + In_NumberOfColumn);
      
       for(int i=1; i<=Integer.parseInt(In_NumberOfColumn);i++)
       {
         System.out.println("In_TableFiled" +i+ " : " + mapTableFileld.get("In_TableFiled" + i));
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
  
  
  public  ArrayList downloadTextData(String filename) throws Exception
  {
 
   fileList=new ArrayList();
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
       UTF-16 Format used in Java&C for conveting 
       Other two  UTF-8 used in web,UTF-32 mostly used in Unix*/
    br = new BufferedReader(new InputStreamReader(in,"UTF-16"));  
    String aLine = null;
  
     while((aLine = br.readLine()) != null) {
        ArrayList tempList = new ArrayList();     
        if(aLine != null && !aLine.equals("")) {
          String[] aArray = aLine.split("\t");
         
          for(int j=0; j < aArray.length; j++) {
           
            String value = aArray[j];
           
            tempList.add(value);
          }
        fileList.add(tempList);
        }
     }
     /*System.out.println("ArraySize......."+ fileList.size()+ "<br>");
      for(int k=0;k< fileList.size();k++)
         {
            ArrayList tempLists = (ArrayList) fileList.get(k);
            for(int j = 0; j < tempLists.size(); j++ ) { 
               if(tempLists.get(j).equals(""))
               {
                 System.out.println("Derwerwer......."+tempLists.get(j)+ "<br>");
              //   throw new Exception("Unable Download Data's,Pls check the inbound text file");
               }
            }
         } */   
    return fileList;
 
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
    
  public  boolean downloadData(String filename,String sheetName,String LOGIN_USER) throws Exception
  {
  StrUtils su = new StrUtils ();
    System.out.println("******** Data Import Starts *********");
   // filename="D:\\DEVELOPMENT\\CIBA-VISION\\DOCUMENTS\\Inbound-Travelers\\CVAML1338-1340_Trav.xls\\CVAML1338-1340_Trav2.xls";
   //sheetName="1338TRAV";
   
    // to get file name
    File f=new File(filename);
    filename=f.getName();
    f=null;
    //
    
   // filename="D:\\MURHO\\WMS_UPLOAD\\" + filename;
  //  filename="D:\\APPS\\WMS_TEST\\MURHO\\WMS_UPLOAD\\" + filename;
    filename=ReadFromServerFolder + filename;
  
    System.out.println("Import File :" + filename);
    System.out.println("Import sheet :" + sheetName);
    
    StringBuffer sbQuery=new StringBuffer("");
    boolean tranFlag=false;
    double totalSku=0.0;
    String TRAVELER="";
    String outFilename ="";
    
    UserTransaction ut=null ;
    java.sql.Connection con=null;
   
    try {  
               
            ut = DbBean.getUserTranaction();
            ut.begin(); 
            con=DbBean.getConnection();
            BaseDAO _BaseDAO = new BaseDAO();
            boolean exists =  new File(filename).exists();
            if(!exists)
            {
              throw new Exception ("File Not Found in Path " + filename);
            }
            String[] str= ExcelDownloader.readExcelFile(filename, sheetName, Integer.parseInt(In_NumberOfColumn));
            MLogger.log("LOGIN_USER :  " +LOGIN_USER);
            MLogger.log("Total number of record found in Excel sheet  :  " + str.length);
           
            for(int j=0;j<str.length-1;j++){
                String s1=str[j];
               
                if(j<5)continue;
                StringTokenizer parser = new StringTokenizer(s1,",");
                
                System.out.println("Record " + j + "\t: " + s1);
               
                List list = new LinkedList();    // Doubly-linked list
                list = new ArrayList();   
                            
                 while (parser.hasMoreTokens()) {
                                   
                 list.add(parser.nextToken());     
                 
                }
                
              
               System.out.println(" list.size() " + list.size());
            
            
              if(list.size()<Integer.parseInt(In_NumberOfColumn)){
              System.out.println(list.size()  + " < " + In_NumberOfColumn);
             //   System.out.println(" *********** Now system need to break **********");
                totalSku=Float.parseFloat((String)list.get(1));
                System.out.println("********* Total SKU found in Traveler  : " + totalSku);
                   break;
             //   if(j>str.length-4)break;
                }
             
              
                TRAVELER=(String)list.get(5);
              //  System.out.println(" TRAVELER : " + TRAVELER);  
               System.out.println("\n");  
                sbQuery.append("insert into ");
                sbQuery.append(" RECVDET " + "(");
             
                for(int i=1; i<=Integer.parseInt(In_NumberOfColumn);i++)
                {
                       
                 sbQuery.append(((String)mapTableFileld.get("In_TableFiled"+i)).substring(1)+",");
                 
                 //mapTableFileld.get("TableFiled" + i
                }
                //to remove last comma
                sbQuery.replace(sbQuery.length()-1,sbQuery.length(),"");
                sbQuery.append(" )VALUES(" );
                for(int i=0;i<list.size();i++)
                {
              
                char ch= ((String)mapTableFileld.get("In_TableFiled"+(i+1))).charAt(0);
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
              //  System.out.println("Query: " + sbQuery.toString());
                tranFlag=false;
                try{
                tranFlag=_BaseDAO.insertData(con,sbQuery.toString());
                }
                catch(Exception e)
                {
                  throw new Exception("Unable to download Traveler : " + TRAVELER + " Cannot have duplicate Traveler or MTID" + " \nError :: " + e.getMessage());
                }
                System.out.println("Data inserted to RECVDET : " + tranFlag);
              
                sbQuery=new StringBuffer("");
            }
            
            System.out.println("TRAVELER :: " + TRAVELER);
            System.out.println("Total Number of SKU :: " + totalSku);
         
            
            tranFlag=_BaseDAO.insertData(con,"update recvDet set receiveStatus='N',putAwayStatus='N', recvQty=0,putAwayQty=0,LOC1='' where Traveler='" + TRAVELER + "'");
            ArrayList al=null;
            al=_BaseDAO.selectData(con,"select sum(OrdQty) as qty from recvdet where Traveler ='"+TRAVELER+"'");
            String totalSkuDownloaded=(String)((Map)(al.get(0))).get("qty");
        /*    filename=  su.makeDoubleSlash(filename);
              System.out.println("filename ::::  : " + filename);
             POIReadExcel ex =new POIReadExcel(filename, sheetName);
             System.out.println("POI Qty : " + ex.getQty());
             System.out.println("Total SKU found in RECVDET  : " + totalSkuDownloaded);
            // REMOVED SOME TIME GIVES PROBLEM
              totalSku =   ex.getQty();
            if(Float.parseFloat(totalSkuDownloaded)==totalSku)
            {
             System.out.println(Float.parseFloat(totalSkuDownloaded) + "==" +  totalSku);
            }
            else
            {
              System.out.println(Float.parseFloat(totalSkuDownloaded) + "!=" + totalSku);
              throw new Exception("Check SKU in Traveler file which is not tally with imported SKU");
            }*/
           
            //inserting to RECVHDR
            
             StringBuffer sbRecvHdr=new StringBuffer("");
             sbRecvHdr.append("INSERT INTO RECVHDR(TRAVELER,ReceiveStatus,PutawayStatus,fileGenerated,STATUS,CRAT,CRBY)VALUES(");
            // sbRecvHdr.append("'" + +"'");
             sbRecvHdr.append("'" + TRAVELER +"',");
             sbRecvHdr.append("'" + "N" +"',");
             sbRecvHdr.append("'" + "N" +"',");
             sbRecvHdr.append("'" + "N" +"',");
             sbRecvHdr.append("'" + "N" +"',");
             sbRecvHdr.append("'" + DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()) +"',");
             sbRecvHdr.append("'" + LOGIN_USER +"'");
             sbRecvHdr.append(")");
             //inserting data to recvHdr
             
             if(tranFlag)   tranFlag=_BaseDAO.insertData(con,sbRecvHdr.toString());
             
             System.out.println("Data inserted to RECVHDR : " + tranFlag);
            
             // ALLOCATING LOCATION
             
            System.out.println("********* ALLOCATING LOCATION " + tranFlag);
            
                CallableStatement colStmt = null;
                String sp ="";
                sp = "exec PROC_CALL_ALLOCATELOCATION '" +""+ "','"+TRAVELER +"' ";
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
             sbMovHis.append("INSERT INTO MOVHIS(PLANT,TRAVELER,MOVTID,QTY,CRAT,CRTIME,CRBY)VALUES(");
            // sbRecvHdr.append("'" + +"'");
             sbMovHis.append("'" + CibaConstants.cibacompanyName +"',");
             sbMovHis.append("'" + TRAVELER +"',");
             sbMovHis.append("'" + "IMPORT-TRAVELER" +"',");
             sbMovHis.append("'" + totalSkuDownloaded +"',");
             sbMovHis.append("'" + DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()) +"',");
             sbMovHis.append("'" + DateUtils.Time() +"',");
             sbMovHis.append("'" + LOGIN_USER +"'");
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
          }
         }
       return tranFlag;
    
  }
    
  public int process_import(ArrayList al) throws Exception {
	 boolean flag = false;
   boolean hdrflag=false;
   boolean hdrflag1=false;
   int iCntHdr=0;
   int iCntDet=0;
	//UserTransaction ut = null;
		try {
         // ut = com.murho.gates.DbBean.getUserTranaction();
      
          SQLServerDAOFactory dao_factory= (SQLServerDAOFactory)DAOFactory.getDAOFactory(1);
        
          //dao - hdr,det
          SQLRecvHdr_DAO _SQLRecvHdr = new SQLRecvHdr_DAO();
          SQLRecvDet_DAO _SQLRecvDet = new SQLRecvDet_DAO();
     
         //do - hdr,det
          RecvDet_DO  _RecvDet_DO = new RecvDet_DO();
          RecvHdr_DO  _RecvHdr_DO = new RecvHdr_DO();
       
          TransactionDTO  _TransactionDTO= new TransactionDTO();
     
          for(int i=0;i<al.size();i++)
          {
            rtnMsg=" ";
           
            _TransactionDTO= (TransactionDTO)al.get(i);
          
            //set into hdrdo 
           _RecvHdr_DO.setTraveler(_TransactionDTO.getTraveler());
           _RecvHdr_DO.setReceivestatus(_TransactionDTO.getReceivestatus());
           _RecvHdr_DO.setPutawaystatus(_TransactionDTO.getPutawaystatus());
           _RecvHdr_DO.setFilegenerated(_TransactionDTO.getFilegenerated());
           _RecvHdr_DO.setStatus(_TransactionDTO.getStatus());
           _RecvHdr_DO.setCrat(_TransactionDTO.getCrat());
           _RecvHdr_DO.setCrby(_TransactionDTO.getCrby());
           _RecvHdr_DO.setFilename(_TransactionDTO.getFilename());
    
         
          hdrflag= _SQLRecvHdr.findByRecvHdrId(_RecvHdr_DO.getTraveler());
          MLogger.log(0,hdrflag+"....value of iCntHdr");
           if(!hdrflag)
            {
             iCntHdr= _SQLRecvHdr.insertRecvHdr( _RecvHdr_DO);           
                 if(iCntHdr==0)
                  {
                  throw new Exception("Err In Insert RecvHdr Delivery No:" + _TransactionDTO.getTraveler()+ "Imports Delivery No Failed") ; 
                  }
                             
              }
        
          //set into  detdo
     
          _RecvDet_DO.setTraveler(_TransactionDTO.getTraveler());
          _RecvDet_DO.setPallet(_TransactionDTO.getPallet());
          _RecvDet_DO.setMtid(_TransactionDTO.getMtid());
          _RecvDet_DO.setSku(_TransactionDTO.getSku());
          _RecvDet_DO.setLot(_TransactionDTO.getLot());
          _RecvDet_DO.setOrdqty(_TransactionDTO.getOrdqty());
          _RecvDet_DO.setRecvqty(_TransactionDTO.getRecvqty());
          _RecvDet_DO.setPutawayqty(_TransactionDTO.getPutawayqty());
          _RecvDet_DO.setPutawaystatus(_TransactionDTO.getPutawaystatus());
          _RecvDet_DO.setUserfld1(_TransactionDTO.getUserfld1());
          _RecvDet_DO.setReceivestatus(_TransactionDTO.getReceivestatus());
          _RecvDet_DO.setCreatedon(_TransactionDTO.getCreatedon());
          _RecvDet_DO.setCrat(_TransactionDTO.getCrat());
          _RecvDet_DO.setCrby(_TransactionDTO.getCrby());
          _RecvDet_DO.setLinenos(_TransactionDTO.getLinenos());
          _RecvDet_DO.setFilename(_TransactionDTO.getFilename());
           _RecvDet_DO.setCreatedon(_TransactionDTO.getCreatedon());
          
          detflag=_SQLRecvDet.findByRecvDetId( _RecvDet_DO.getTraveler(), _RecvDet_DO.getMtid());
          MLogger.log(0,detflag+"....value of detflag");
          if(!detflag)
            {
              iCntDet=_SQLRecvDet.insertRecvDet(_RecvDet_DO);
              if(iCntDet==0)
              {
              throw new Exception("Err In Insert RecvDet Delivery No:" + _TransactionDTO.getTraveler()+ " " + "Imports Delivery No Failed") ;
               //  rtnMsg="Err In Insert RecvDet Delivery No:" + _TransactionDTO.getTraveler()+ " " + "Imports Delivery No Failed" ;
              }
            } 
           if(detflag)
           {
             throw new Exception("Delivery No."  + " " + (_RecvDet_DO.getTraveler())+ "  Exists already");
              // rtnMsg=" Delivery No."  + " " + (_RecvDet_DO.getTraveler())+ "  Exists already";
             
           }
      
      }
 
    }
    catch (Exception e) {
      System.out.println("Exception : " + e.getMessage());
      throw e;
   }
 
  return  iCntDet;    
  }
  //Modified on 3-Apr-2014 for ticket Import Inbound File Process #INC000003097020
    public int getCalcAllocation(ArrayList al) {
    MLogger.log(1, this.getClass() + " getCalcAllocation()");
   int iCnt=0;
    try {
        
         SQLServerDAOFactory dao_factory= (SQLServerDAOFactory)DAOFactory.getDAOFactory(1);
        SQLRecvDet_DAO _SQLRecvDet = new SQLRecvDet_DAO();
        RecvDet_DO  _RecvDet_DO = new RecvDet_DO();
        TransactionDTO  _TransactionDTO= new TransactionDTO();
        //for loop is added on 3-Apr-2014 for iterating al arraylist
        String traveler="";
        for(int i=0;i<al.size();i++)
        {
        	_TransactionDTO = (TransactionDTO)al.get(i);
        	int j=traveler.indexOf(_TransactionDTO.getTraveler());
        	if(j<0)
        	{        	
        	traveler=traveler+(_TransactionDTO.getTraveler()+"$");
        	}
        }
     
       //iCnt =_SQLRecvDet.CalcAllocation();// _SQLRecvDet.CalcAllocation(_RecvDet_DO.getTraveler());
        iCnt =_SQLRecvDet.CalcAllocation(traveler);
          
     
    }
    catch (Exception e) {
      MLogger.log("Exception : getCalcAllocationl :: getCalcAllocation:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getCalcAllocation()");
    return iCnt;
  }
  public ArrayList getCalcTraveler() {
    MLogger.log(1, this.getClass() + " getCalcTraveler()");
    ArrayList arrList = new ArrayList();
    try {
       
        SQLServerDAOFactory dao_factory= (SQLServerDAOFactory)DAOFactory.getDAOFactory(1);
      
        SQLRecvDet_DAO _SQLRecvDet = new SQLRecvDet_DAO();
            
        RecvDet_DO  _RecvDet_DO = new RecvDet_DO();
       
        arrList=_SQLRecvDet.getCalcTraveler();
      
                
    }
    catch (Exception e) {
      MLogger.log("Exception :getCalcTraveler :: getCalcTraveler:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + "getCalcTraveler()");
    
    return arrList;
  }
    public int getDeCalcAllocation(ArrayList al) {
    MLogger.log(1, this.getClass() + " getCalcAllocation()");
   int iCnt=0;
    try {
        
        SQLServerDAOFactory dao_factory= (SQLServerDAOFactory)DAOFactory.getDAOFactory(1);
        SQLRecvDet_DAO _SQLRecvDet = new SQLRecvDet_DAO();
        RecvDet_DO  _RecvDet_DO = new RecvDet_DO();
        TransactionDTO  _TransactionDTO= new TransactionDTO();
      
        for(int i=0;i<al.size();i++)
        {
           _TransactionDTO= (TransactionDTO)al.get(i);
           _RecvDet_DO.setTraveler(_TransactionDTO.getTraveler());
           _RecvDet_DO.setMtid(_TransactionDTO.getMtid());
           _RecvDet_DO.setLot(_TransactionDTO.getLot());
           _RecvDet_DO.setSku(_TransactionDTO.getSku());
           _RecvDet_DO.setMode(_TransactionDTO.getMode());
               
           iCnt =_SQLRecvDet.deCalcAllocation(_RecvDet_DO);
        
        }
     
    }
    catch (Exception e) {
      MLogger.log("Exception : getDeCalcAllocationl :: getDeCalcAllocation:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getDeCalcAllocation()");
    return iCnt;
  }
  
    public ArrayList getTrayLabelingWriteFileById(ArrayList al) {
    MLogger.log(1, this.getClass() + " getLineDataDetailsById()");
    
    ArrayList arrList = new ArrayList();
    try {
  
         SQLServerDAOFactory dao_factory= (SQLServerDAOFactory)DAOFactory.getDAOFactory(1);
      
         SQLTravelHdr_DAO _SQLTravelHdr_DAO = new SQLTravelHdr_DAO();
         SQLTravelDet_DAO _SQLTravelDet_DAO = new SQLTravelDet_DAO();
         
         TravelDet_DO  _TravelDet_DO = new  TravelDet_DO ();
         TransactionDTO  _TransactionDTO= new TransactionDTO();
         
        for(int i=0;i<al.size();i++)
          {
             _TransactionDTO= (TransactionDTO)al.get(i);
             _TravelDet_DO.setTraveler(_TransactionDTO.getTraveler());
            // _TravelDet_DO.setPalletid(_TransactionDTO.getPalletid());
              arrList= _SQLTravelDet_DAO.getWriteFileDetById(_TravelDet_DO);
          }
   
    }
    catch (Exception e) {
      MLogger.log("Exception :getLineDataDetailsById :: getLineDataDetailsById:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getLineDataDetailsById()");
     //System.out.println("arrList..SDF..............."+arrList.size());
    return arrList;
  }
  
  
   public boolean  process_writefile(ArrayList al) throws Exception
  {
   MLogger.log("Enter process_writefile : : + DataDownloaderUtil");
    DateUtils dateUtils=null;
    TravelDet_DO  _TravelDet_DO = new  TravelDet_DO ();
    
    userBean ub;
   
    dateUtils= new DateUtils();
    String fileDate=dateUtils.getDateFormatyyyyMMdd();
    String fileDate1=dateUtils.getDateFormatYYMMDDHHMMSS();//+dateUtils.getTimeinhhmmss();//+dateUtils.getDateAtTime();
    StrUtils strUtils = new StrUtils ();
    Map map = null;
    TrayDetDAO dao = new TrayDetDAO();
    String prdclass=" ";
    int puvqty=0;
    
    SQLTempDet_DAO _SQLTempDet_DAO =new  SQLTempDet_DAO(); 
    boolean  updateSHIP_DETRemarks=false;
    
     boolean flag =false;
     int lendelivery=10;
     int lenline=6;
     int lensku=8;
     int lenqty=8;
     int lenpallet=10;
     int lentray=10;
     int lenbatch=10;
     int lenuom=3;
     int len=0;
     String emptyPalletId="";
     String emptyTrayId="";
     String iqty="";
     
     try    
     {
        
        String filename="";
     
         filename="SRC"+ fileDate1+".txt";
         
        //Below lines are commented by jyoti for TIBCO-INC000002484471(WMS 2.8)
        //************************************************************
        //String FilePath= CibaConstants.CReadFromServerFolder+filename;
        //File f=new File(CibaConstants.CReadFromServerFolder+filename);
        //************************************************************
        
        //Below lines are added by jyoti for TIBCO-INC000002484471(WMS 2.8)
        //fetching the sap file path from database
        //*********************************************************************
        ub = new userBean();
        Map sysParams = ub.getSystemParams();
        String sAPFilePath = (String)sysParams.get("SAP_FILE_PATH");
        System.out.println("Path of SAP file..."+sAPFilePath);
        String FilePath= sAPFilePath+filename;
        File f=new File(sAPFilePath+filename);
        //*********************************************************************
        
        FileWriter fstream = new FileWriter(FilePath);
        BufferedWriter bout = new BufferedWriter(fstream);
    
       if (f.exists()  )
       {
         
       
        /*for(int j=0; j<1; j++)
        {
         bout.newLine();
        }*/
      
        for(int i=0; i<al.size(); i++)
        {
           Map lineArr = (Map) al.get(i);
         //  String  sl=("SL");
           String  traveler=(String)lineArr.get("TRAVELER");
           String  soline=(String)lineArr.get("SOLINE");
           String  sku=(String)lineArr.get("SKU");
           String  qty=(String)lineArr.get("TRAYLABLEQTY");
           String  pallet=(String)lineArr.get("PALLETID");
           String trayid=(String)lineArr.get("TRAYID");
           String  lot=(String)lineArr.get("LOT");
           String  uom=(String)lineArr.get("UOM");
                         
          //Check class is PUV or not
               String query= "select B.PRD_GRP_ID AS PRODUCTGROUP From ItemMst A ,Prd_Class_mst  B Where A.Item='"+sku+"' AND A.PRD_CLS_ID = B.PRD_CLS_ID ";               
               map = dao.getRowOfData(query);
               prdclass=(String) map.get("PRODUCTGROUP");
           //End Check class is PUV or not 
            
            //for updating remarks1 and remarks in ship_det table
               updateSHIP_DETRemarks=_SQLTempDet_DAO.updateSHIP_DETRemarks(traveler);
            // //for updating remarks1 and remarks in ship_det table End
      
        
         //Chekc Qty is 0
          if(qty.equals("0"))
          {
       
             bout.write((strUtils.rightPad(traveler," ",lendelivery)+strUtils.leftPad(soline,"0",lenline)
             +strUtils.leftPad(sku,"0",lensku)+strUtils.leftPad(qty,"0",lenqty)+strUtils.rightPad(emptyPalletId," ",lenpallet)
             +strUtils.rightPad(emptyTrayId," ",lentray) +strUtils.rightPad(lot," ",lenbatch)+strUtils.rightPad(uom," ",lenuom)));
          }
          else
          {
              //if puv class then we divide qty by 6                 
               if(prdclass.equals("PUV"))
                {
                  puvqty= Integer.parseInt(((String)qty).trim().toString());
                  iqty=puvqty/6+"";
                  qty=iqty;
                              
                }
          
                   bout.write((strUtils.rightPad(traveler," ",lendelivery)+strUtils.leftPad(soline,"0",lenline)
                  +strUtils.leftPad(sku,"0",lensku)+strUtils.leftPad(qty,"0",lenqty)+strUtils.rightPad(pallet," ",lenpallet)
                  +strUtils.rightPad(trayid," ",lentray) +strUtils.rightPad(lot," ",lenbatch)+strUtils.rightPad(uom," ",lenuom)));
          }
          bout.newLine();
        }
        
        bout.close();
       
        flag=true;
    }
     }
     catch (Exception e)
      {
         MLogger.log("Exception :process_writefile :: process_writefile:" + e.toString());
         MLogger.exception(this,e);
         flag = false;
         throw e;
      }
    return  flag;
  }
  
  
  public boolean getRecvStatusforUpdate(ArrayList al) {
    MLogger.log(1, this.getClass() + " getRecvStatusForUpdate()");
    
    boolean flag=false;
    ArrayList arrList = new ArrayList();
    try {
  
         SQLServerDAOFactory dao_factory= (SQLServerDAOFactory)DAOFactory.getDAOFactory(1);
      
          
          SQLRecvDet_DAO _SQLRecvDet_DAO = new SQLRecvDet_DAO();
          RecvDet_DO  _RecvDet_DO = new RecvDet_DO();
        
         TransactionDTO  _TransactionDTO= new TransactionDTO();
         
        for(int i=0;i<al.size();i++)
          {
            _TransactionDTO= (TransactionDTO)al.get(i);
            _RecvDet_DO.setTraveler(_TransactionDTO.getTraveler());
            _RecvDet_DO.setMtid(_TransactionDTO.getMtid());
            flag = _SQLRecvDet_DAO.findRecvStatusforUpdate(_RecvDet_DO);
              
          }
      
    }
    catch (Exception e) {
      MLogger.log("Exception :getRecvStatusForUpdate :: getRecvStatusForUpdate:" + e.toString());
      MLogger.log( -1, "");
    }
    MLogger.log( -1, this.getClass() + " getRecvStatusForUpdate()");
     //System.out.println("arrList..SDF..............."+arrList.size());
    return flag;
  }
 
  public static void main(String[] args)
  {
    DataDownloaderUtil _DataDownloaderUtil = new DataDownloaderUtil();
    try
    {
   //   POIReadExcel ex =new POIReadExcel("D:\\MURHO\\WMS_UPLOAD\\CVAML 2593,2594,2596-2598.xls","CV-AML2598");
   //   System.out.println("Qty : " + ex.getQty());
    _DataDownloaderUtil.downloadData("","","");
    }
    catch (Exception e)
    {
      
    }
  }
  
  
  
}