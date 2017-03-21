package com.murho.db.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.TblControlDAO;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;

public class TblControlUtil {
  public TblControlUtil() {
  }
  public synchronized String getNextSeqNo(String func,String prefix) throws Exception{
    String nextSeqno = "";
    try{
          TblControlDAO dao = new TblControlDAO();
         nextSeqno = dao.getNextSeqNo(func,prefix);
    }catch(Exception e) {e.printStackTrace(); throw e;}
    return nextSeqno;
  }
 

  public boolean isExistInTblControl(Hashtable ht) throws Exception{
   boolean isExists = false;
   TblControlDAO dao = new TblControlDAO();
   try {
      isExists = dao.isExists(ht);   
   }
   catch (Exception e) {
      MLogger.exception("isExistInTblControl()", e);
      throw e;
  }
   return isExists;
  }
  
  /**
   * method : insertTblControl(Hashtable ht)
   * description : insert new record into TBLCONTROL
   * @param ht
   * @return
   */
 public boolean insertTblControl(Hashtable ht) throws Exception{
    boolean inserted = false;
    try{

    TblControlDAO dao = new TblControlDAO();
      inserted = dao.insert(ht);
    }catch(Exception e){ throw e;}
    return inserted;
  }
  /**
   * method : deleteTblControl(String aFunc)
   * description : insert new record into TBLCONTROL
   * @param ht
   * @return
   */
  public boolean deleteTblControl(Hashtable ht)throws Exception{
    boolean deleted = false;
    try{
     TblControlDAO dao = new TblControlDAO();
      deleted = dao.delete(ht);
    }catch(Exception e){ throw e;}
    return deleted;
  }
  /**
   * method : upDateTblControl(String aFunc)
   * description : insert new record into TBLCONTROL
   * @param ht
   * @return
   */
  public boolean updateTblControl(Hashtable htUpdate,Hashtable htCondition) throws Exception{
    boolean updated = false;
    try{
       TblControlDAO dao = new TblControlDAO();
      updated = dao.update(htUpdate,htCondition);
    }catch(Exception e){ throw  e; }
    return updated;
  }
 

  
     public Map getTblControlDetails(String aFunc,String aPfx ) throws Exception
     {
      TblControlDAO dao = new TblControlDAO();
      Hashtable ht=new Hashtable();
      ht.put(MDbConstant.TBL_FUNCTION,aFunc);
      ht.put(MDbConstant.TBL_PREFIX,aPfx);
      Map map=new HashMap();
      try{
         String sQry =  MDbConstant.TBL_FUNCTION+","+MDbConstant.TBL_PREFIX+","+MDbConstant.DESCRIPTION+","+MDbConstant.TBL_MIN_SEQ+","+MDbConstant.TBL_MAX_SEQ+","+MDbConstant.TBL_NEXT_SEQ ;
         map= dao.selectRow(sQry,ht);
      }catch(Exception e){MLogger.exception("getLocIdDetails :: Exception :",e);
        throw e;
      }
     return map;
    }
  
  
  public ArrayList getTblControlList(String aFunc) {   
    ArrayList al = null;
    TblControlDAO dao = new TblControlDAO();
    try {
      al = dao.getTblControlStartsWith(aFunc);
    }
    catch (Exception e) {
       MLogger.exception(this, e);
    }
    return al;
  }
}