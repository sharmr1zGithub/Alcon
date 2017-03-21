package com.murho.db.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

import javax.transaction.UserTransaction;

import com.murho.DO.TransactionDTO;
import com.murho.dao.BaseDAO;
import com.murho.dao.DAOFactory;
import com.murho.dao.MovHisDAO;
import com.murho.dao.RecvDetDAO;
import com.murho.dao.SQLRecvDet_DAO;
import com.murho.dao.SQLServerDAOFactory;
import com.murho.dao.TblControlDAO;
import com.murho.gates.DbBean;
import com.murho.utils.CibaConstants;
import com.murho.utils.DateUtils;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.TransactionTypes;
import com.murho.utils.XMLUtils;

public class TravelerSummaryUtil {

  public boolean isReceiveStatus=true;
  com.murho.utils.XMLUtils xu = null;
  com.murho.utils.StrUtils strUtils = null;
  public TravelerSummaryUtil() {
    xu = new XMLUtils();
    strUtils = new StrUtils();
   
  }
   

   public boolean UpdateReceiveprocess(Map mp) throws Exception{
    boolean updated = false;
    boolean isExists =false;
    try{
    RecvDetDAO recvDetDAO=new RecvDetDAO();
    Hashtable ht=new Hashtable();
    
    updated=UpdateReceiveDetail(mp);
    if(updated)
    {
       updated=processMoveHis(mp);
    }
    else
    {
      updated=false;
    }
    
   }catch(Exception e){ 
    
    throw e;
    }
    return updated;
   }
    public boolean UpdateReceiveDetail(Map mp) throws Exception{
    boolean inserted = false;
    try{
    MLogger.log("############## UpdateReceiveDetail #####################");
      RecvDetDAO dao= new RecvDetDAO();
      
      Hashtable ht = new Hashtable();
      
     ht.put("TRAVELER",mp.get(MDbConstant.TRAVELER_NUM));
     ht.put("MTID", mp.get(MDbConstant.MTID));
     //By Samatha for updateing order qty only if not received
     ht.put("ReceiveStatus","N");
     String Query = "set sku='"+(String)mp.get(MDbConstant.ITEM)+"', lot='"+(String)mp.get(MDbConstant.LOT_NUM)+"', ordqty='"+(String)mp.get(MDbConstant.RECV_QTY)+"' ";
    
      inserted = dao.update(Query,ht,"");
      
    }catch(Exception e){ 
    throw e;
    }
    return inserted;
  }
   public boolean RejectReceiveDetail(Map mp) throws Exception{
 
    MLogger.log("##################  RejectReceiveDetail ####################### ");
    boolean updated = false;
    UserTransaction ut=null ;
    try{
      ut = DbBean.getUserTranaction();
      ut.begin(); 
     RecvDetDAO recvDetDAO=new RecvDetDAO();
      
     Hashtable ht = new Hashtable();
     ht.put("TRAVELER",mp.get(MDbConstant.TRAVELER_NUM));
     ht.put("MTID",mp.get(MDbConstant.MTID));
          
      Hashtable ht1 = new Hashtable();
      ht1.put("MTID",mp.get(MDbConstant.MTID));
      //By samatha to reject only if it is not recevied
       ht1.put("ReceiveStatus","N");
       // end
      String Query1 = "set recstat = 'D' ";
      
      updated = recvDetDAO.deleteItemFrmInvmst(ht1);
       
      //MOVHIS
      if(updated)
      {
        updated=processMoveHis(mp);
      }
      
      if(updated)
      {
        //commit
        DbBean.CommitTran(ut);
      }else
      {
        throw new Exception("unable to reject MTID details");
        //rollback
      }
    }catch(Exception e){ 
    DbBean.RollbackTran(ut); 
    throw e;
    }
    return updated;
  }
  

    private boolean processMoveHis(Map mp) throws Exception
   {
    boolean flag =false;
    TblControlDAO tblConDao=new TblControlDAO();

    MovHisDAO movHisDao=new MovHisDAO();
    try
    {
       MLogger.log(0, "Getting next seq no .");
        Hashtable htRecvHis = new Hashtable();
      
        htRecvHis.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
        htRecvHis.put(MDbConstant.TRAVELER_NUM,mp.get(MDbConstant.TRAVELER_NUM));
        htRecvHis.put(MDbConstant.PALLET,mp.get(MDbConstant.PALLET));
        htRecvHis.put(MDbConstant.MTID,mp.get(MDbConstant.MTID));
        htRecvHis.put(MDbConstant.ITEM,mp.get(MDbConstant.ITEM));
        htRecvHis.put(MDbConstant.LOT_NUM,mp.get(MDbConstant.LOT_NUM));
        htRecvHis.put(MDbConstant.LOGIN_USER,mp.get(MDbConstant.LOGIN_USER));
        htRecvHis.put("CRAT",mp.get(MDbConstant.MOVEHIS_CR_DATE));
        htRecvHis.put("MOVTID",mp.get(MDbConstant.MOVHIS_REF_NUM));
        htRecvHis.put("CRTIME",mp.get(MDbConstant.CRTIME));
        htRecvHis.put("REMARK",mp.get(MDbConstant.REMARK));
        htRecvHis.put(MDbConstant.MOVHIS_QTY,mp.get(MDbConstant.MOVHIS_QTY));
      
      flag = movHisDao.insertIntoMovHis(htRecvHis);

      MLogger.log(0, "insertIntoMovHis Transaction ::  " + flag);


    }
    catch (Exception e)
    {
       MLogger.exception(this,e);
       throw e;
    }
    return flag;
 }
 
 
  public boolean RemoveTraveler(String totalString,String user_id,String TRN_DATE) throws Exception{
    boolean deleted = false;
    String Query="";
    String sepratedtoken1="";
    BaseDAO _BaseDAO = new BaseDAO();
    java.sql.Connection con=null;
    Map mp = null;mp = new HashMap();
    try{
     
     String sepratedtoken="";
     
     
     StringTokenizer parser = new StringTokenizer(totalString,"=");
     con=DbBean.getConnection();    
    
     
        while (parser.hasMoreTokens())
        
              {
                 int count=1;                  
                 MLogger.info("countTokens ::"+parser.countTokens());
                
                 sepratedtoken = parser.nextToken();
                 
                 MLogger.info("###################### sepratedtoken #####################"+sepratedtoken);
                 
                 StringTokenizer parser1 = new StringTokenizer(sepratedtoken,",");
                  
                 while (parser1.hasMoreTokens())
                  
                    {
                       sepratedtoken1 = parser1.nextToken();
                           
                       mp.put("data"+count,sepratedtoken1);
                           
                       count++;
                           
                        MLogger.info("###################### sepratedtoken1 #####################"+sepratedtoken1);
                           
                   }
                          
                          Query="delete from tempdatatable where traveler='"+(String)mp.get("data1")+"' ";
                          deleted= _BaseDAO.DeleteRow(con,Query);
                          
                           if(deleted)
                          {
                           Map mp1 = null;mp1 = new HashMap();
                                                  
                          mp1.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
                          mp1.put(MDbConstant.TRAVELER_NUM,mp.get("data1"));
                         
                          mp1.put(MDbConstant.MTID,"");
                          mp1.put(MDbConstant.ITEM,"");
                          mp1.put(MDbConstant.LOT_NUM,"");
                          mp1.put(MDbConstant.PALLET,"");
                         
                          mp1.put(MDbConstant.MOVHIS_QTY,mp.get("data4"));
                          mp1.put("CRBY",user_id);
                          mp1.put("CRAT",TRN_DATE);
                         
                          mp1.put(MDbConstant.MOVHIS_REF_NUM,"REMOVE");
                          mp1.put("CRTIME","");
                          mp1.put("REMARK","");
                          mp1.put("QTY","0");
                          
                    
                          deleted=processMoveHis(mp1);
                          }
                         
                         MLogger.info("data1"+mp.get("data1"));
                         MLogger.info(" data2"+mp.get("data2"));
                         MLogger.info("data3"+mp.get("data3"));
                }
              
            
    }catch(Exception e){ 
    throw e;
    }
    finally
    {
      DbBean.closeConnection(con);
    }
    return deleted;
  }
  
  
   public boolean RemoveRecvDet(String totalString,String user_id,String TRN_DATE) throws Exception{
    
    boolean deletedHdr = false;
    boolean deletedDet = false;
    String QueryHdr="";
    String QueryDet="";
    String sepratedtoken1="";
    BaseDAO _BaseDAO = new BaseDAO();
    java.sql.Connection con=null;
    Map mp = null;mp = new HashMap();
    TransactionDTO  trnDTO;
    DataDownloaderUtil  _DataDownloaderUtil=new   DataDownloaderUtil();
    con=DbBean.getConnection();    

    boolean isExists=false;
    boolean isFlag=false;
    try{
     
     String sepratedtoken="";
     
     
     StringTokenizer parser = new StringTokenizer(totalString,"=");
   
    
     
        while (parser.hasMoreTokens())
        
              {
                 int count=1;                  
                 MLogger.info("countTokens ::"+parser.countTokens());
                
                 sepratedtoken = parser.nextToken();
                 
                 MLogger.info("###################### sepratedtoken #####################"+sepratedtoken);
                 
                StringTokenizer parser1 = new StringTokenizer(sepratedtoken,",");
                  
                 while (parser1.hasMoreTokens())
                  
                {
                  sepratedtoken1 = parser1.nextToken();
                           
                  mp.put("data"+count,sepratedtoken1);
                           
                  count++;
                           
                 MLogger.info("###################### sepratedtoken1 #####################"+sepratedtoken1);
                           
               }
                          
                //getRecvDetCount
                  SQLServerDAOFactory dao_factory= (SQLServerDAOFactory)DAOFactory.getDAOFactory(1);
                  SQLRecvDet_DAO _SQLRecvDet = new SQLRecvDet_DAO();
                          
                  isReceiveStatus  = _SQLRecvDet.findRecvStatus((String)mp.get("data1"));
                   
                  if(isReceiveStatus)
                  {      
                     int iCalcCnt=0;
                      
                      try
                        {
                          String strMode="1";                                                
                          ArrayList alCalcTransactionData=new ArrayList();
                          trnDTO =new TransactionDTO();
                          trnDTO.setTraveler((String)mp.get("data1"));
                          trnDTO.setMtid("");
                          trnDTO.setLot("");
                          trnDTO.setSku("");
                          trnDTO.setMode(strMode);
                          alCalcTransactionData.add(trnDTO);
                            
                          iCalcCnt =_DataDownloaderUtil.getDeCalcAllocation(alCalcTransactionData);
                        
                        }
                        catch (Exception e) {
                          MLogger.exception(this,e);;
                          System.out.print("Get Msg   "+ e.getMessage());
                          throw e;
                        }
                      
                       
                       QueryDet="delete from RecvDet where traveler='"+(String)mp.get("data1")+"' ";
                       deletedDet= _BaseDAO.DeleteRow(con,QueryDet);
                          
                          
                       isExists= _SQLRecvDet.getRecvDetCount((String)mp.get("data1"));
                       if(!isExists)
                       {
                         QueryHdr="delete from RecvHdr where traveler='"+(String)mp.get("data1")+"' ";
                         deletedHdr= _BaseDAO.DeleteRow(con,QueryHdr);
                         MLogger.log(0,"getRecevHdrDelete(QueryHdr)");
                       }
                           
                      if(deletedDet)
                      {
                        Map mp1 = null;mp1 = new HashMap();
                                                  
                        mp1.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
                        mp1.put(MDbConstant.TRAVELER_NUM,mp.get("data1"));
                         
                        mp1.put(MDbConstant.MTID,"");
                        mp1.put(MDbConstant.ITEM,"");
                        mp1.put(MDbConstant.LOT_NUM,"");
                        mp1.put(MDbConstant.PALLET,"");
                         
                         //int qty=0;
                        //mp1.put(MDbConstant.MOVHIS_QTY,mp.get("data2"));
                        mp1.put(MDbConstant.MOVHIS_QTY,"0");
                          
                        mp1.put("CRBY",user_id);
                       // mp1.put("CRAT",TRN_DATE);
                        mp1.put("CRAT",DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
                        mp1.put("CRTIME",DateUtils.Time());
                         //  sbMovHis.append("'" + DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()) +"',");
                        mp1.put(MDbConstant.MOVHIS_REF_NUM,TransactionTypes.Import_inbound_file_delete_tran_type);
                         mp1.put("REMARK","");
                        // mp1.put("QTY","0");
                          
                        deletedDet=processMoveHis(mp1);
                     }
                         
                      MLogger.info("data1"+mp.get("data1"));
                      MLogger.info(" data2"+mp.get("data2"));
                      MLogger.info("data3"+mp.get("data3"));
                }
              else
              {
                
                
              }
       }
    }catch(Exception e){ 
    throw e;
    }
    finally
    {
      DbBean.closeConnection(con);
    }
    return deletedDet;
  }
} //end of class





