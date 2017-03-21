

package com.murho.db.utils;

import java.util.Hashtable;

import com.murho.dao.MovHisDAO;
import com.murho.utils.CibaConstants;
import com.murho.utils.DateUtils;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;

public class MovHisUtil {
  com.murho.utils.XMLUtils xu = null;
  com.murho.utils.StrUtils strUtils = null;
  public MovHisUtil() {
    xu = new XMLUtils();
    strUtils = new StrUtils();
   
  }

   public boolean insertMovHis(Hashtable ht) throws Exception{
    boolean inserted = false;
    try{

     MovHisDAO dao= new MovHisDAO();
     inserted = dao.insertIntoMovHis(ht);
    }catch(Exception e){ 
    throw e;
    }
    return inserted;
  }


   public boolean insertMovHisLogger(String enrolledBy,String module,String  result) throws Exception{
    boolean inserted = false;
    try{

     Hashtable ht=new Hashtable();
     
       String  formattedRemarks;        StringBuffer  sb = new StringBuffer();
       int i=0;

        while(i< result.length())
        {
                if(result.charAt(i) == '<')
                {
                    while(result.charAt(i) !='>')
                    i++;
                    i++;
                }
                else
                {
                	// Below line modified by Arun on 3 Oct 2011 for #25046: URS8-12
                    //if(sb.length()<100 && ((int)result.charAt(i)!= 39))  // Checking for ' Character and length
                	if(sb.length()<200 && ((int)result.charAt(i)!= 39))  // Checking for ' Character and length
                    sb.append(result.charAt(i));
                    i++;
                }
        }
     formattedRemarks = new String(sb);  
        
    
    
    String TRN_DATE=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
    String tranTime=DateUtils.Time();
    
     ht.put("PLANT",CibaConstants.cibacompanyName);
     ht.put("CRBY",enrolledBy);
     ht.put("MOVTID",module.toUpperCase());
     ht.put("REMARK",formattedRemarks.toString());
     ht.put("CRAT",TRN_DATE);
     ht.put("CRTIME",tranTime);
        
     MovHisDAO dao= new MovHisDAO();
     inserted = dao.insertIntoMovHis(ht);
    }catch(Exception e){ 
    throw e;
    }
    return inserted;
  }
 
   /*Created new method to enter LOT into MovHis by Ranjana under 6.0 requirement*/
   
   public boolean insertMovHisLogger1(String enrolledBy,String module,String  result, String LOT) throws Exception{
    boolean inserted = false;
    try{

     Hashtable ht=new Hashtable();
     
       String  formattedRemarks;        StringBuffer  sb = new StringBuffer();
       int i=0;

        while(i< result.length())
        {
                if(result.charAt(i) == '<')
                {
                    while(result.charAt(i) !='>')
                    i++;
                    i++;
                }
                else
                {
                	// Below line modified by Arun on 3 Oct 2011 for #25046: URS8-12
                    //if(sb.length()<100 && ((int)result.charAt(i)!= 39))  // Checking for ' Character and length
                	if(sb.length()<200 && ((int)result.charAt(i)!= 39))  // Checking for ' Character and length
                    sb.append(result.charAt(i));
                    i++;
                }
        }
     formattedRemarks = new String(sb);  
        
    
    
    String TRN_DATE=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
    String tranTime=DateUtils.Time();
    
     ht.put("PLANT",CibaConstants.cibacompanyName);
     ht.put("CRBY",enrolledBy);
     ht.put("MOVTID",module.toUpperCase());
     ht.put("LOT",LOT);
     ht.put("REMARK",formattedRemarks.toString());
     ht.put("CRAT",TRN_DATE);
     ht.put("CRTIME",tranTime);
        
     MovHisDAO dao= new MovHisDAO();
     inserted = dao.insertIntoMovHis(ht);
    }catch(Exception e){ 
    throw e;
    }
    return inserted;
  }
  
} //end of class
