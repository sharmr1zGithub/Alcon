package com.murho.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


public class MLogger {

  public static int previousLevel = 0;
  public static int currentLoggerLevel = 0;
  private static String spaceString = "->";

  private static StringBuffer strLevel = new StringBuffer("");

  private static void setSpace(String strSpaceString) {
    spaceString = strSpaceString;
  }

  public static void exception(Object obj, Exception ex) {
    System.out.println("################ Exception :: " + obj.getClass() +
                       " :: " + ex.getMessage());
  }

  public static void exception(String str, Exception ex) {
    System.out.println("################ Exception :: " + str + " :: " +
                       ex.getMessage());
  }

  private static void stepUpLoggerLevel() {
    try {
      //System.out.println("Entering :: stepUpLoggerLevel()");
      //System.out.println("Before inc currentLoggerLevel :" + currentLoggerLevel);
      //System.out.println("Before inc incerment strLevel :" + strLevel.length());
      previousLevel = currentLoggerLevel;
      currentLoggerLevel = currentLoggerLevel + spaceString.length();
      strLevel.append(spaceString);
      //System.out.println("After inc currentLoggerLevel :" + currentLoggerLevel);
      //System.out.println("After inc incerment strLevel :" + strLevel.length());
      // System.out.println("Entering :: stepUpLoggerLevel()");
    }
    catch (Exception e) {
      System.out.println("%%%%%%%%%%%%%%%%%%%Exception :: stepUpLoggerLevel() " +
                         e.getMessage());
    }
  }

  private static void stepDownLoggerLevel() {
    try {
      // System.out.println("Entering :: stepDownLoggerLevel()");
      // System.out.println("Before Dec currentLoggerLevel :" + currentLoggerLevel);
      //  System.out.println("Before Dec incerment strLevel :" + strLevel.length());
      previousLevel = currentLoggerLevel;
      currentLoggerLevel = currentLoggerLevel - spaceString.length();
      if (strLevel.length() != 0) {
        // strLevel.deleteCharAt(currentLoggerLevel);
        strLevel.delete(strLevel.length() - spaceString.length(),
                        strLevel.length());
        //System.out.println("After Dec currentLoggerLevel :" + currentLoggerLevel);
        //System.out.println("After Dec incerment strLevel :" + strLevel.length());
      }
      //System.out.println("Exiting ::  stepDownLoggerLevel()");
    }
    catch (Exception e) {
      System.out.println(
          "%%%%%%%%%%%%%%%%%%%Exception :: stepDownLoggerLevel() " +
          e.getMessage());
    }
  }

  public static void log(String s, boolean debug) {
    if (debug) {
      System.out.println(s);
    }
  }
  
  public static void log(String s) {
    if (MConstants.DEBUG) {
      System.out.println(strLevel.toString() + s);
    }
  }
  
   public static void query(String s) {
    if (MConstants.FLAG_QUERY_PRINT) {
      System.out.println("  ");
      System.out.println(" ******************* << Query >> ******************* \n" + s);
      //System.out.println("\n"+s);
      System.out.println(" ******************* << Query >> ******************* \n");
      System.out.println("  ");
    }
  }
  
  public static void xmlPrint(String s) {
    if (MConstants.FLAG_XML_PRINT) {
      System.out.println("  ");
      System.out.println(" ******************* << Xml >> ******************* \n" + s);
    //  System.out.println(strLevel.toString() +" " + s);
      System.out.println(" ******************* << Xml >> ******************* \n");
      System.out.println("  ");
    }
  }
  
   public static void info(String s) {
    if (MConstants.FLAG_INFO_PRINT) {
       System.out.println(" INFO : " + s);
     }
  }
  
   public static void printInput(String s) {
    if (MConstants.FLAG_INPUT_PRINT) {
       System.out.println(" INPUT : " + s);
     }
  }
  
  

  
  public static void log(String user,String s) {
    if (MConstants.DEBUG) {
      System.out.println(user +" : "+strLevel.toString() + s);
    }
  }

  public static void log(int l, String s) {
    if (MConstants.DEBUG) {
      switch (l) {
        case 1:
       //   stepUpLoggerLevel(); //to remove stepping up 30-05-2007
          System.out.println(strLevel.toString() + " Enter : " + s);

          break;

        case -1:
          System.out.println(strLevel.toString() + " Exit  : " + s);
        //  stepDownLoggerLevel(); //to remove stepping up 30-05-2007
          break;

        case 0:
          System.out.println(strLevel.toString() + " " + s);
          break;
      }
    }
  }
  
   public static void log(int l, String user,String s) {
    if (MConstants.DEBUG) {
      switch (l) {
        case 1:
          stepUpLoggerLevel();
          System.out.println(user +" : " + strLevel.toString() + " Enter : " + s);

          break;

        case -1:
          System.out.println(user +" : " + strLevel.toString() + " Exit  : " + s);
          stepDownLoggerLevel();
          break;

        case 0:
          System.out.println(user +" : " + strLevel.toString() + " " + s);
          break;
      }
    }
  }

  public static void display(ArrayList al){

    for (int i = 0; i < al.size(); i++) {
          Map map = (Map) al.get(i);
           Collection c=map.values();
           Iterator itr=c.iterator();

            while(itr.hasNext()){
              System.out.print((String)itr.next());
              System.out.print(" ");

           }
           System.out.println("");
           System.out.println("-----------------------------");
        }


  }

}
