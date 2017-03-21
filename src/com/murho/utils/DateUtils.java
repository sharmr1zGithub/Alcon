package com.murho.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

/********************************************************************************************************
* PURPOSE :  A class for dealing with various  -- Date & Time Formats
*  Generates time, unique ID, transfers time from one format to other etc
*******************************************************************************************************/

public class DateUtils extends Object {

  static SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
  static SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
  SimpleDateFormat formatter4 = new SimpleDateFormat("yyyyMMdd");
  SimpleDateFormat formatter5 = new SimpleDateFormat("yyMMddHHmmssSSS");
  SimpleDateFormat formatter6 = new SimpleDateFormat("dd/MM/yyyy HHmm");
  SimpleDateFormat formatter7 = new SimpleDateFormat("yyyyMMddHHmm");
  SimpleDateFormat formatter9 = new SimpleDateFormat("yyMMdd");

  static SimpleDateFormat formatter8 = new SimpleDateFormat("dd/mm/yyyy mmssSS");
  static SimpleDateFormat formatter10 = new SimpleDateFormat("dd/mm/yyyy");
    static SimpleDateFormat formatter11 = new SimpleDateFormat("dd-MM-yyyy");
  static int id=0;

  static Calendar calendar;

    public DateUtils()   {
     // calendar = new GregorianCalendar();
    }
    static
    {
      calendar = new GregorianCalendar();
    }

/********************************************************************************************************
*   PURPOSE           :  A Method to get the date in  dd/MM/yyyy format
*   PARAMETER 1 :  Nil
*   RETURNS          : Date String
*******************************************************************************************************/
 
    public static String getDate()
    {
    String time = "";
    Date dt = new Date();
    calendar.setTime(dt);
    time = formatter1.format(dt);
    return time;
    }

/********************************************************************************************************
*   PURPOSE           :  A Method to get the Date and Time in   yyyyMMddHHmmss format
*   PARAMETER 1 :  Nil
 *   RETURNS        : Date String
*******************************************************************************************************/
    public String getDateTime()
    {
        String time="";

        Date dt = new Date();
        calendar.setTime(dt);
        time = formatter3.format(dt);

        return time;
    }

    /********************************************************************************************************
    *   PURPOSE           :  A Method to get the Date and Time in   yyyyMMddHHmmss format
    *   PARAMETER 1 :  Nil
     *   RETURNS        : Date String
    *******************************************************************************************************/
        public String getDateFormatyyyyMMdd()
        {
            String time="";

            Date dt = new Date();
            calendar.setTime(dt);
            time = formatter4.format(dt);

            return time;
        }
        
        
            public String getDateFormat11()
        {
            String time="";

            Date dt = new Date();
            calendar.setTime(dt);
            time = formatter11.format(dt);

            return time;
        }

/********************************************************************************************************
*   PURPOSE           :   A Method to get the Date and Time in   dd/MM/yyyy 'at' HH:mm:ss format
*   PARAMETER 1 :   Nil
*   RETURNS         :   DateString
*******************************************************************************************************/
   public String getDateAtTime()
    {
        String time="";

        Date dt = new Date();
        calendar.setTime(dt);
        time = formatter2.format(dt);

        return time;
    }

/********************************************************************************************************
*   PURPOSE           :   A Method to get the Date and Time in the user preferred  format
*   PARAMETER 1 :   User preferred DateTime Format
*   RETURNS         :   DateTime String in User preferred format
*******************************************************************************************************/

    public String getGeneralDate(String format)
    {
        String time="";

        Date dt = new Date();
        calendar.setTime(dt);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        time = formatter.format(dt);

        return time;
    }

/********************************************************************************************************
*   PURPOSE           :   A Method to generate Unique ID based on current timestamp
*   PARAMETER 1 :   Nil
*   RETURNS         :   Unique ID
*******************************************************************************************************/
    public String getUniqueId()
    {
        String time="";

        Date dt = new Date();
        calendar.setTime(dt);
        time = formatter5.format(dt);
        if(id==1000) id=0; id++;
        return time+id;
    }

/********************************************************************************************************
*   PURPOSE           :   A Method to transform date in yyyyMMdd  format to dd/MM/yyyy format
*   PARAMETER 1 :   Date String to be transformed
*   RETURNS         :   DateString in dd/MM/yyyy format
*******************************************************************************************************/
    public String getDB2UserDate(String temp)
    {
        String time="";
        if(temp==null) temp=""; else temp = temp.trim();
        if (temp.length()>5)
        time = temp.substring(6)+"/"+ temp.substring(4,6)+"/"+temp.substring(0,4);

        return time;
    }

/********************************************************************************************************
*   PURPOSE           :   A Method to transform date in yyyyMMddHHmmss to dd/MM/yyyy 'at' HH:mm:ss format
*   PARAMETER 1 :   Date String to be transformed
*   RETURNS         :   DateString in dd/MM/yyyy 'at'  HH :mm:ss format
*******************************************************************************************************/
    public String getDB2UserDateTime(String temp)
    {
        String time="";
        if(temp==null) temp=""; else temp = temp.trim();
        if (temp.length()==12) temp+="00";  //  Adding seconds
        if (temp.length()>12)
        time = temp.substring(6,8)+"/"+ temp.substring(4,6)+"/"+temp.substring(0,4)+" at "+temp.substring(8,10)+":"+temp.substring(10,12)+":"+temp.substring(12);

        return time;
    }
    
      public  String getTime() {
    Calendar cal = new GregorianCalendar();
    //int hour12 = cal.get(Calendar.HOUR);            // 0..11
    int hour24 = cal.get(Calendar.HOUR_OF_DAY); // 0..23
    int min = cal.get(Calendar.MINUTE); // 0..59
    int sec = cal.get(Calendar.SECOND); // 0..59
    //int ms = cal.get(Calendar.MILLISECOND);         // 0..999
    //int ampm = cal.get(Calendar.AM_PM);
    // String s1=String.valueOf(hour24);

    return String.valueOf(hour24) + String.valueOf(min) + String.valueOf(sec);
  }
    /********************************************************************************************************
*   PURPOSE           :   A Method to transform date in yyyyMMddHHmmss to dd/MM/yyyy
*   PARAMETER 1 :   Date String to be transformed
*   RETURNS         :   DateString in dd/MM/yyyy
*******************************************************************************************************/
    public String getDB2UserDate_New(String temp)
    {
        String time="";
        if(temp==null) temp=""; else temp = temp.trim();
        if (temp.length()==12) temp+="00";  //  Adding seconds
        if (temp.length()>12)
        time = temp.substring(6,8)+"/"+ temp.substring(4,6)+"/"+temp.substring(0,4);

        return time;
    }

/********************************************************************************************************
*   PURPOSE           :   A Method to transform date in  dd/MM/yyyy 'at' HH:mm:ss format to yyyyMMddHHmmss format
*   PARAMETER 1 :   Date String to be transformed
*   RETURNS         :   Date String in yyyy-MM-ddHHmmss format
*******************************************************************************************************/
    public String getDBDate(String htmldt) throws ParseException
    {
        String time="";

        if((!htmldt.equalsIgnoreCase("")) || (htmldt!=null))
        {

        ParsePosition pos = new ParsePosition(0);
        Date dt = formatter2.parse(htmldt,pos);
        calendar.setTime(dt);

        int iyyyy = calendar.get(Calendar.YEAR);
        int iMM = calendar.get(Calendar.MONTH);
        int idd = calendar.get(Calendar.DAY_OF_MONTH);
        int iHH = calendar.get(Calendar.HOUR_OF_DAY);
        int imm = calendar.get(Calendar.MINUTE);
        int iss = calendar.get(Calendar.SECOND);

        String yyyy = new Integer(iyyyy).toString();
        String MM = new Integer(iMM).toString();
        String dd = new Integer(idd).toString();
        String HH = new Integer(iHH).toString();
        String mm = new Integer(imm).toString();
        String ss = new Integer(iss).toString();

        time = yyyy+MM+dd+HH+mm+ss;
        }
    return time;
    }
    
  
    
/********************************************************************************************************
*   PURPOSE           :   A Method to transform date in  dd/MM/yyyy HHmm format to yyyyMMddHHmm format
*   PARAMETER 1 :   Date String to be transformed
*   RETURNS         :   Date String in yyyyMMddHHmm format
*******************************************************************************************************/
    public String getDBDateMid(String htmldt) throws ParseException
    {
     String time="";

     if((!htmldt.equalsIgnoreCase("")) || (htmldt!=null))
     {
        ParsePosition pos = new ParsePosition(0);
        Date dt = formatter6.parse(htmldt,pos);
        calendar.setTime(dt);
        time = formatter7.format(dt);
     }
    return time;
    }
/********************************************************************************************************
*   PURPOSE           :   A Method to transform date in  dd/MM/yyyy 'format to yyyyMMdd format
*   PARAMETER 1 :   Date String to be transformed
*   RETURNS         :   Date String in yyyyMMdd format
*******************************************************************************************************/
    public String getDBDateShort(String htmldt) throws ParseException
    {
     String time="";

        if((!htmldt.equalsIgnoreCase("")) || (htmldt!=null))
        {

        ParsePosition pos = new ParsePosition(0);
        Date dt = formatter1.parse(htmldt,pos);
        calendar.setTime(dt);

        int iyyyy = calendar.get(Calendar.YEAR);
        int iMM = calendar.get(Calendar.MONTH);
        int idd = calendar.get(Calendar.DAY_OF_MONTH);

        iMM++;

        String yyyy = new Integer(iyyyy).toString();
        String MM = new Integer(iMM).toString();
        String dd = new Integer(idd).toString();

        if(MM.length()==1) MM="0"+MM;
        if(dd.length()==1) dd="0"+dd;

        time = yyyy+MM+dd;
    }
    return time;
    }

/********************************************************************************************************
*   PURPOSE           :   A Method to add user specified amount of days to dd/MM/yyyy 'format and return yyyyMMdd format
*   PARAMETER 1 :   Current Date in dd/MM/yyyy format
*   PARAMETER 2 :   Number of days to be added to current date
*   RETURNS         :   Date String in yyyyMMdd format ..returned after adding the given number of days
*******************************************************************************************************/
    public String addDay(String curdate, int amount) throws ParseException
    {
     String time="";

        if(!curdate.equalsIgnoreCase(""))
        {

        ParsePosition pos = new ParsePosition(0);
        Date dt = formatter1.parse(curdate,pos);
        calendar.setTime(dt);
        calendar.add(Calendar.DATE,amount);

        int iyyyy = calendar.get(Calendar.YEAR);
        int iMM = calendar.get(Calendar.MONTH);
        int idd = calendar.get(Calendar.DAY_OF_MONTH);

        iMM++;

        String yyyy = new Integer(iyyyy).toString();
        String MM = new Integer(iMM).toString();
        String dd = new Integer(idd).toString();

        if(MM.length()==1) MM="0"+MM;
        if(dd.length()==1) dd="0"+dd;

        time = yyyy+MM+dd;
        }
        return time;
    }

/********************************************************************************************************
*   PURPOSE           :   A Method to add user specified number of days to dd/MM/yyyy 'at' HH:mm:ss 'format
*   PARAMETER 1 :   Current Date in dd/MM/yyyy 'at' HH:mm:ss 'format
*   PARAMETER 2 :   Number of days to be added to current date
*   RETURNS         :   Date String in dd/MM/yyyy 'at' HH:mm:ss 'format  ..returned after adding the given number of days
*******************************************************************************************************/
   public String addDayAt(String curdate, int amount) throws ParseException
    {
     String time="";

        if(!curdate.equalsIgnoreCase(""))
        {

        ParsePosition pos = new ParsePosition(0);
        Date dt = formatter2.parse(curdate,pos);
        calendar.setTime(dt);
        calendar.add(Calendar.DATE,amount);
        Date newdt = calendar.getTime();
        time = formatter2.format(newdt);

        }
    return time;
    }

/********************************************************************************************************
*   PURPOSE           :   A Method to add user specified number of days to general 'format
*   PARAMETER 1 :   Date Format to be used
*   PARAMETER 2 :   Current date
*   PARAMETER 3 :   Number to be added to current date
*   RETURNS         :   Modified date in the same format
*******************************************************************************************************/
    public String addToGeneralFormat(String format, String curdate, int amount) throws ParseException
    {
     String time="";

        if((curdate.length()>0) && (format.length()>0))
        {

        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date dt = formatter.parse(curdate,pos);
        calendar.setTime(dt);
        calendar.add(Calendar.DATE,amount);
        Date newdt = calendar.getTime();
        time = formatter.format(newdt);

        }
    return time;
    }

/********************************************************************************************************
*   PURPOSE           :   A Method to add user specified number of  months to dd/MM/yyyy' format
*   PARAMETER 1 :   Current date
*   PARAMETER 1 :   Number of months to add
*   RETURNS         :   Modified date
*******************************************************************************************************/
   public String addMonth(String curdate, int amount) throws ParseException
    {
     String time="";

        if(!curdate.equalsIgnoreCase(""))
        {

        ParsePosition pos = new ParsePosition(0);
        Date dt = formatter1.parse(curdate,pos);
        calendar.setTime(dt);
        calendar.add(Calendar.MONTH,amount);
        Date newdt = calendar.getTime();
        time = formatter1.format(newdt);
        }
    return time;
    }


/********************************************************************************************************
*   PURPOSE           :   Parsing the date with default  format  of  dd/mm/yyyy
*   PARAMETER 1 :   Date to be checked
*   RETURNS         :   True if it is in valid date format else false
*******************************************************************************************************/
    public boolean isValidDateFormat(String chkDate)
    {
        try
        {
            Date dt = formatter1.parse(chkDate);
        }
        catch(ParseException pe)
        {
            return false;
        }
        return true;
    }

/********************************************************************************************************
*   PURPOSE           :   Parsing the date with specified date  format  of  "dd/MM/yyyy HHmm"
*   PARAMETER 1 :   Date to be checked
*   PARAMETER 2 :   Just an integer to override method name
*   RETURNS         :   True if it is in valid date format else false
*******************************************************************************************************/
    public boolean isValidDateFormat(String chkDate, int n)
    {
        try
        {
            Date dt = formatter6.parse(chkDate);
        }
        catch(ParseException pe)
        {
            return false;
        }
        return true;
    }

    public String parseDate(String chkDate)
    {
      String s = "";
      String y="",m="",d ="";
      String yy="",mm="",dd = "";
      int cnt;
      try{
        if (chkDate.length() > 0) {
          StrUtils stUtil = new StrUtils();
          Vector vecDate = stUtil.parseString(chkDate, "/");
          yy = vecDate.elementAt(2).toString();
          mm = vecDate.elementAt(1).toString();
          dd = vecDate.elementAt(0).toString();
          cnt = yy.length();
          if (cnt == 4) {
            y = yy;
          }
          else if (cnt == 3) {
            y = "2" + yy;
          }
          else if (cnt == 2) {
            y = "20" + yy;
          }
          else if (cnt == 1) {
            y = "200" + yy;
          }
          cnt = mm.length();
          if (cnt == 2) {
            m = mm;
          }
          else if (cnt == 1) {
            m = "0" + mm;
          }
          cnt = dd.length();
          if (cnt == 2) {
            d = dd;
          }
          else if (cnt == 1) {
            d = "0" + dd;
          }
          s = y + m + d;
        }
      }
    catch(Exception e){}
        return s;
    }

    public String getSQLDate(String nd){
        String fDate = "";
        //nd.trim();
        fDate = nd.substring(6,10)+ nd.substring(3,5)+nd.substring(0,2) ;//nd.substring(6,10)+ nd.substring(3,5)+nd.substring(0,2) ;
        return fDate;
      }

      public String getDate(String nd){
        String fDate = "";
        //nd.trim();

        fDate = nd.substring(3,5) + "/" + nd.substring(0,2)+"/"+nd.substring(6,10);
        return fDate;
      }

     public static String getDateInYYYYMMDD (String YYYY_MM_DD){
     String fDate = "";
     YYYY_MM_DD=YYYY_MM_DD.trim();
     
     // 2006-10-24
     fDate = YYYY_MM_DD.substring(0,4)+ YYYY_MM_DD.substring(5,7)+ YYYY_MM_DD.substring(8,10);
     return fDate;
    }
      
    public static String getDateinyyyymmdd(String nd){
     String fDate = "";
     //nd.trim();

     fDate = nd.substring(6,10)+nd.substring(3,5)+nd.substring(0,2);
     return fDate;
   }
   
   public static String getDateinyyyy_mm_dd(String nd){
     String fDate = "";
     //nd.trim();
     //01-10-2006
     fDate = nd.substring(6,10)+ "-" + nd.substring(3,5)+ "-"+ nd.substring(0,2);
     return fDate;
   }

     public static String getDateinyyyy_mm_dd_01(String nd){
     String fDate = "";
     //nd.trim();
     //01-10-2006
     fDate = nd.substring(0,4)+ "-" + nd.substring(4,6)+ "-"+ nd.substring(6,8);
     return fDate;
   }

public String getDate(SimpleDateFormat formatter){
        String retStr="";
         Date dt = new Date();
         calendar.setTime(dt);
         retStr = formatter.format(dt);
        return retStr;
      }
      /********************************************************************************************************
 *   PURPOSE           :  A Method to get the Date and Time in   yyyyMMddHHmmss format
 *   PARAMETER 1 :  Nil
  *   RETURNS        : Date String
 *******************************************************************************************************/
     public String getDateFormatYYMMDD()
     {
         String time="";
         Date dt = new Date();
         calendar.setTime(dt);
         time = formatter9.format(dt);

         return time;
     }
     
      public String getDateFormatYYMMDDHHMMSS()//--------
     {
         String time="";
         Date dt = new Date();
         calendar.setTime(dt);
         time = formatter3.format(dt);

         return time;
     }
     
       //mm/dd/yy
    public  String getDateinmmddyy(String nd){
     String fDate = "";
     



     fDate = nd.substring(0,2)+"/"+nd.substring(3,5)+"/"+nd.substring(8,10);
     return fDate;
   }
   
  
   
       //yymmdd
    public  String getDateinyymmdd(String nd){
     String fDate = "";
     //yyyy-mm-dd
     fDate = nd.substring(2,4)+nd.substring(5,7)+nd.substring(8,10);
     return fDate;
   }
   
    public  String getDatemmddyy(String nd){
     String fDate = "";
     //yyyy-mm-dd
     fDate = nd.substring(5,7)+"/"+nd.substring(8,10)+"/"+nd.substring(2,4);
     return fDate;
   }
   
  
   
     public static String Time()
    {
        String time="";

        Date dt = new Date();
        calendar.setTime(dt);
        time = formatter2.format(dt);
        
        time=time.substring(10,19);

        return time;
    }
  
     public static String getTimeinhhmmss(String nd){
     String ftime = "";
     //yyyy-mm-dd
     //hh:mm:ss
     ftime = nd.substring(1,3)+nd.substring(4,6)+nd.substring(7,9);
     return ftime;
   }
     
     public static void main(String args[])
     {
     try
     {
        DateUtils dateUtils = new DateUtils();
        String dt=getTimeinhhmmss(" hh:mm:ss");
       String smtid ="74541919-01"; 
       String mtid ="74541919-01";
       if (smtid.equalsIgnoreCase(mtid))
       {
         System.out.println("Date :");
       }
      
      
      // String txt ="\""+"SAMTHA"+"\"";
      //System.out.println("txt : "+ txt);
      
     }catch(Exception e)
     {
        e.printStackTrace();
     }
      
     }
}
