
package com.murho.tran;

import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.InvMstDAO;
import com.murho.dao.ItemMstDAO;
import com.murho.dao.MovHisDAO;
import com.murho.dao.StockTakeDAO;
import com.murho.utils.CibaConstants;
import com.murho.utils.DateUtils;
import com.murho.utils.MLogger;

public class WmsStockTake implements WmsTran
{
  StockTakeDAO _StockTakeDAO=null;
  InvMstDAO _InvMstDAO=null;
  DateUtils dateUtils=null;
  
  public WmsStockTake()
  {
      _StockTakeDAO=new StockTakeDAO();
      _InvMstDAO =new InvMstDAO();
      dateUtils=new DateUtils() ;
  }
  
  public boolean processWmsTran(Map m) throws Exception
  {
    boolean flag=false;
      MLogger.log(0, "1.insert stocktake - XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX Stage : 1");
      flag=processStockTake(m);
      MLogger.log(0, "processStockTake() : Transaction : " + flag);
   
    if (flag)
    {
      MLogger.log(0, "2.insert movhis - XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX Stage : 2");
      flag=processMovHis(m);
      MLogger.log(0, "processMovHis() : Transaction : " + flag);
    }
    return flag;
  }
  
    public boolean processStockTake(Map map)throws Exception
    {
      MLogger.log(1, this.getClass() + " processStockTake()");
      boolean flag=false;
    
      try{
      Hashtable htInvMst = new Hashtable();
      Hashtable stktake = new Hashtable();
      htInvMst.clear();
      
      
      htInvMst.put("PLANT",CibaConstants.cibacompanyName);
      htInvMst.put("ITEM",map.get("SKU"));
      htInvMst.put("LOC",map.get("LOC"));
      htInvMst.put("MTID",map.get("MTID"));
      flag= _StockTakeDAO.isExisit(htInvMst,"");
       MLogger.log(0, "isExisit() : "+ flag);  
       if(flag)
       {
         StringBuffer sql=new StringBuffer(" SET ");
         sql.append(""+"QTY" + " = '" + map.get("QTY") + "',"); 
         sql.append(""+"CRAT" + " = '" + dateUtils.getDateFormat11() + "',"); 
         sql.append(""+"CRBY" + " = '" + map.get("CRBY") + "'"); 
         
         flag = _StockTakeDAO.update(sql.toString(),htInvMst,"");
       }
       else
       {
         htInvMst.put("QTY",map.get("QTY"));
         htInvMst.put("LOC",map.get("LOC"));
         htInvMst.put("STATUS","C");
         htInvMst.put("USERFLD1",new ItemMstDAO().getItemDescription((String)map.get("PLANT"),(String)map.get("SKU")));
         htInvMst.put("MTID",map.get("MTID"));
         htInvMst.put("BATCH",map.get("LOT"));
         htInvMst.put("CRBY",map.get("CRBY"));
         htInvMst.put("CRAT",dateUtils.getDateFormat11()); 
      
         flag =  _StockTakeDAO.insertStkTake(htInvMst);    
       }   
      }catch(Exception e)
      {
        MLogger.log(-1,"Exception :: " + e.getMessage());
        throw e;
      }
      MLogger.log(-1, this.getClass() + " processStockTake()");
    return flag;
  }
  
  
  public boolean processMovHis(Map map)throws Exception
  {
      boolean flag=false;
      MovHisDAO movHisDao=new MovHisDAO();
      try{
      Hashtable htRecvHis = new Hashtable();
      htRecvHis.clear();
      htRecvHis.put("PLANT",map.get("PLANT"));
      htRecvHis.put("MTID",map.get("MTID"));
      htRecvHis.put("SKU",map.get("SKU"));
      htRecvHis.put("MOVTID","STOCK_TAKE");
      htRecvHis.put("RECID","");
      htRecvHis.put("LOC",map.get("LOC"));
      htRecvHis.put("QTY",map.get("QTY"));
      htRecvHis.put("LOT",map.get("LOT"));
      htRecvHis.put("CRBY",map.get("CRBY"));
      htRecvHis.put("CRAT",dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate())); 
      
      htRecvHis.put("CRTIME",map.get("CRTIME"));
      flag = movHisDao.insertIntoMovHis(htRecvHis);
      }catch(Exception e){
      throw e;
      }
    return flag;
  }
  
}