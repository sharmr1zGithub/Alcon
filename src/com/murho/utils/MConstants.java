package com.murho.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class MConstants {

  public MConstants() {

  }

  public static void sop(String str) {
    System.out.println("class :: MurhoConstants -> " + str);
  }


 
//public static String DB_PROPS_FILE ="C:/props/CibaVision/config/CibaVision_testEnv.properties";
 public static String DB_PROPS_FILE ="C:/props/CibaVision/config/CibaVision.properties";
  private static Boolean refDEBUG;
  private static Boolean refQueryPrint;
  private static Boolean refXmlPrint;
  private static Boolean refInfoPrint;
  private static Boolean refInputPrint;
  private static Boolean refBACKEND;

  static {
    Properties dbpr;
    InputStream dbip;
    try {
      dbip = new FileInputStream(new File(DB_PROPS_FILE));
      dbpr = new Properties();
      dbpr.load(dbip);
      //strDebug = dbpr.getProperty("DEBUG");
      //refBoolean = new Boolean(strDebug);


      refDEBUG = new Boolean(dbpr.getProperty("DEBUG"));
      refBACKEND = new Boolean(dbpr.getProperty("BACKEND"));
      refQueryPrint = new Boolean(dbpr.getProperty("FLAG_QUERY_PRINT"));
      refXmlPrint = new Boolean(dbpr.getProperty("FLAG_XML_PRINT"));
      refInfoPrint= new Boolean(dbpr.getProperty("FLAG_INFO_PRINT"));
      refInputPrint= new Boolean(dbpr.getProperty("FLAG_INPUT_PRINT"));
      
      System.out.println("BACKEND TRANSACTION : " + refBACKEND);
      System.out.println("DEBUG               : " + refDEBUG);
      System.out.println("QUERY PRINTING      : " + refQueryPrint);
      System.out.println("XML PRINTING        : " + refXmlPrint);
      System.out.println("INFO PRINTING       : " + refInfoPrint);
      System.out.println("INFO PRINTING       : " + refInputPrint);


    }
    catch (FileNotFoundException fnfe) {
      sop("Exception : " + fnfe.getMessage());
    }
    catch (Exception e) {
      sop("Exception : " + e.getMessage());
    }
  }

   // Constant Declarations
  public static final boolean DEBUG = refDEBUG.booleanValue();
  public static final boolean FLAG_QUERY_PRINT = refQueryPrint.booleanValue();
  public static final boolean FLAG_XML_PRINT = refXmlPrint.booleanValue();
  public static final boolean FLAG_INFO_PRINT = refInfoPrint.booleanValue();
  public static final boolean FLAG_INPUT_PRINT = refInputPrint.booleanValue();
  public static final boolean BACKEND=refBACKEND.booleanValue();




}
