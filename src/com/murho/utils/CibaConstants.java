package com.murho.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class CibaConstants {

  public CibaConstants() {

  }

  public static void sop(String str) {
    System.out.println("class :: MurhoConstants -> " + str);
  }


 public static String DB_PROPS_FILE = "c:/props/CibaVision/config/CibaVisionConstants.properties";
//  public static String DB_PROPS_FILE ="C:/props/CibaVision/config/CibaVision_testEnv.properties";

  private static  String _ReceiveScanningSample="";
  private static String _TrayScanningSample ="";
  private static Boolean refQueryPrint;
  private static Boolean refXmlPrint;
  private static Boolean refInfoPrint;
  private static Boolean refInputPrint;
  private static Boolean refBACKEND;
  public static String ReadFromServerFolder="";
  
 
  
  public static String WriteStktakeFileToFolderPath ="";
  //public  static String cibacompanyName="CH79";
  public  static String cibacompanyName="";

  static {
    Properties dbpr;
    InputStream dbip;
    try {
      dbip = new FileInputStream(new File(DB_PROPS_FILE));
      dbpr = new Properties();
      dbpr.load(dbip);
      //strDebug = dbpr.getProperty("DEBUG");
      //refBoolean = new Boolean(strDebug); 

      ReadFromServerFolder=dbpr.getProperty("ReadFromServerFolder");
      

      
      WriteStktakeFileToFolderPath = dbpr.getProperty("WriteStktakeFileToFolderPath");
     _ReceiveScanningSample = dbpr.getProperty("ReceiveScanningSample");
      _TrayScanningSample = dbpr.getProperty("TrayLabelScanningSample");
       cibacompanyName=dbpr.getProperty("cibacompanyName");
      
    //  companyName = dbpr.getProperty("CompanyName");
   
      System.out.println("BACKEND TRANSACTION : " + refBACKEND);
    
    }
    catch (FileNotFoundException fnfe) {
      sop("Exception : " + fnfe.getMessage());
    }
    catch (Exception e) {
      sop("Exception : " + e.getMessage());
    }
  }

   // Constant Declarations
 
   public static final String CReceiveScanningSample = _ReceiveScanningSample;
   public static final String  CTrayLabelScanningSample = _TrayScanningSample;
   public static final String  CReadFromServerFolder = ReadFromServerFolder;

   public static final String  CWriteStktakeFileToFolderPath = WriteStktakeFileToFolderPath;

   public static String getCompany()
   {
	   return cibacompanyName;
   }

}
