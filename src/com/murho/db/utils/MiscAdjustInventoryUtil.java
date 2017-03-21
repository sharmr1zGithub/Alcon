package com.murho.db.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.murho.dao.InvMstDAO;
import com.murho.dao.ItemMstDAO;
import com.murho.gates.DbBean;
import com.murho.tran.WmsTran;
import com.murho.utils.Generator;
import com.murho.utils.MConstants;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.XMLUtils;


public class MiscAdjustInventoryUtil {
  com.murho.utils.XMLUtils xu = null;
  public MiscAdjustInventoryUtil() {
  xu=new XMLUtils();
  }

  public String Load_PartNo_Details(String COMPANY, String JOBNO) {

    MLogger.log(1, this.getClass() + " Load_PartNo_Details()");

  //  XMLUtils xu = new XMLUtils();
    String xmlStr = "";
    ArrayList al = null;
    ItemMstDAO dao = new ItemMstDAO();
    xmlStr = xu.getXMLHeader();
    xmlStr = xmlStr + xu.getStartNode("jobNoDetails");
    try {
      al = dao.get_PartNoDetails(COMPANY, JOBNO);
      if (al.size() > 0) {
        for (int i = 0; i < al.size(); i++) {
          Map map = (Map) al.get(i);
          xmlStr = xmlStr + xu.getXMLNode("partNum", (String) map.get("ITEM"));
          xmlStr = xmlStr + xu.getXMLNode("partDescription", (String) map.get("ITEMDESC"));
          xmlStr = xmlStr + xu.getXMLNode("whs", (String) map.get("WHID"));
          xmlStr = xmlStr + xu.getXMLNode("tranDate", Generator.getDate());
        }
      }
      xmlStr = xmlStr + xu.getEndNode("jobNoDetails");

    }
    catch (Exception e) {

    }
    MLogger.log(-1, this.getClass() + " Load_PartNo_Details()");
    return xmlStr;
  }

  public String Load_BinDetails_Info(String item, String whs, String plant) {
     MLogger.log(1, this.getClass() + " Load_BinDetails_Info()");
    String xmlStr = "";
    ArrayList mtlList = null;
    XMLUtils xu = new XMLUtils();
    InvMstDAO dao = new InvMstDAO();
    try {
      mtlList = dao.getBinDetails(item, whs, plant);
      if (mtlList.size() > 0) {
        xmlStr += xu.getXMLHeader();
        xmlStr +=
            xu.getStartNode("mtlSeqDetails total='" +
                            String.valueOf(mtlList.size()) +
                            "'");
        for (int i = 0; i < mtlList.size(); i++) {
          Map map = (Map) mtlList.get(i);
          xmlStr += xu.getStartNode("record");
          xmlStr += xu.getXMLNode("binno", (String) map.get("BINNO"));
          xmlStr += xu.getXMLNode("desc", (String) map.get("USERFLD2"));
          xmlStr += xu.getXMLNode("qty", (String) map.get("QTY"));
          xmlStr += xu.getXMLNode("uom", (String) map.get("USERFLD1"));
          xmlStr += xu.getEndNode("record");
        }
        xmlStr += xu.getEndNode("mtlSeqDetails");
      }

    }
    catch (Exception e) {
      System.out.println(" ############# getPOHdrInfo" + e.getMessage());
    }
    MLogger.log(-1, this.getClass() + " Load_BinDetails_Info()");
    return xmlStr;
  }

   public String getAllWarehouse(String company,String item) throws Exception {
    MLogger.log(1, this.getClass() + " getAllWarehouse()");
    String xmlStr = "";
    ArrayList al = null;
    XMLUtils xu = new XMLUtils();
    InvMstDAO dao = new InvMstDAO();

    Hashtable ht = new Hashtable();
    MLogger.log(0,"company:"+company);
    MLogger.log(0,"item:"+item);
    //query
    String query = "DISTINCT " + MDbConstant.WHSE_ID;

    //condition

    ht.put(MDbConstant.COMPANY,company);
    ht.put(MDbConstant.MATERIAL_CODE,item);

    al = dao.selectInvMst(query, ht);

    // xmlStr = xu.getXMLHeader();
    // xmlStr = xmlStr + xu.getStartNode("AllWarehouses");
    try {

      al = dao.selectInvMst(query, ht);

      MLogger.log(0, "Record size() :: " + al.size());
      if (al.size() > 0) {
        xmlStr += xu.getXMLHeader();
        xmlStr +=
            xu.getStartNode("AllWarehouses total='" + String.valueOf(al.size()) +
                            "'");
        for (int i = 0; i < al.size(); i++) {
          Map map = (Map) al.get(i);
           MLogger.log(0, "Record size() :: " + al.get(i));
          xmlStr += xu.getStartNode("record");
          xmlStr +=
              xu.getXMLNode("warehouseCode", (String) map.get(MDbConstant.WHSE_ID));
          xmlStr += xu.getEndNode("record");
        }
      }
      xmlStr = xmlStr + xu.getEndNode("AllWarehouses");
      MLogger.log(0, "Value of xml : " + xmlStr);
    }
    catch (Exception e) {
      MLogger.exception("getAllWarehouse()", e);
    }
    MLogger.log( -1, this.getClass() + " getAllWarehouse()");
    return xmlStr;

  }

  public String getAllBin_For_WareHouse(String company,String item,String whid) throws Exception {
   MLogger.log(1, this.getClass() + " getAllBin_For_WareHouse()");
   String xmlStr = "";
   ArrayList al = null;
   XMLUtils xu = new XMLUtils();
   InvMstDAO dao = new InvMstDAO();

   Hashtable ht = new Hashtable();
   MLogger.log(0,"company:"+company);
   MLogger.log(0,"item:"+item);
   //query
   String query = "DISTINCT " + MDbConstant.BIN_NO;

   //condition

   ht.put(MDbConstant.COMPANY,company);
   ht.put(MDbConstant.MATERIAL_CODE,item);
   ht.put(MDbConstant.WHSE_ID,whid);

   al = dao.selectInvMst(query, ht);

   // xmlStr = xu.getXMLHeader();
   // xmlStr = xmlStr + xu.getStartNode("AllWarehouses");
   try {

     al = dao.selectInvMst(query, ht);

     MLogger.log(0, "Record size() :: " + al.size());
     if (al.size() > 0) {
       xmlStr += xu.getXMLHeader();
       xmlStr +=
           xu.getStartNode("AllWarehouses total='" + String.valueOf(al.size()) +
                           "'");
       for (int i = 0; i < al.size(); i++) {
         Map map = (Map) al.get(i);
          MLogger.log(0, "Record size() :: " + al.get(i));
         xmlStr += xu.getStartNode("record");
         xmlStr +=
             xu.getXMLNode("warehouseCode", (String) map.get(MDbConstant.BIN_NO));
         xmlStr += xu.getEndNode("record");
       }
     }
     xmlStr = xmlStr + xu.getEndNode("AllWarehouses");
     MLogger.log(0, "Value of xml : " + xmlStr);
   }
   catch (Exception e) {
     MLogger.exception("getAllBin_For_WareHouse()", e);
   }
   MLogger.log( -1, this.getClass() + " getAllBin_For_WareHouse()");
   return xmlStr;

 }

 public String Load_On_Hand_Qty(String jobNo,String whs,String bin,String lot,String plant) {

   MLogger.log(1, this.getClass() + " Load_On_Hand_Qty()");

   XMLUtils xu = new XMLUtils();
  String xmlStr = "";
  ArrayList al = null;
  InvMstDAO dao = new InvMstDAO();
  xmlStr = xu.getXMLHeader();
  xmlStr = xmlStr + xu.getStartNode("jobNoDetails");
  try {
    al = dao.Load_On_Hand_Qty(jobNo,whs,bin,lot,plant);
    if (al.size() > 0) {
      for (int i = 0; i < al.size(); i++) {
        Map map = (Map) al.get(i);
        xmlStr = xmlStr + xu.getXMLNode("qty", (String) map.get("QTY"));
        xmlStr = xmlStr + xu.getXMLNode("uom", (String) map.get("USERFLD1"));

      }
    }
    xmlStr = xmlStr + xu.getEndNode("jobNoDetails");

  }
  catch (Exception e) {

  }
  MLogger.log(-1, this.getClass() + " Load_On_Hand_Qty()");
  return xmlStr;
}

public String getOnHandQty_xml(String ITEM, String PLANT,String LOT,String WHS,String BIN) throws Exception
{

  InvMstDAO dao = new InvMstDAO();
   MLogger.log(1, this.getClass() + " getOnHandQty_xml()");
   boolean isQtyExists=false;
    XMLUtils xu = new XMLUtils();
    String xmlStr = "";
    ArrayList al = null;

    String onHandQty="";
    String uom="uom";
    String lotSize="";


    xmlStr = xu.getXMLHeader();
    xmlStr = xmlStr + xu.getStartNode("jobNoDetails");
    Hashtable ht=new Hashtable();

    ht.put(MDbConstant.MATERIAL_CODE,ITEM);
    ht.put(MDbConstant.PLANT,PLANT);
    ht.put(MDbConstant.LOT_NUM,LOT);
    ht.put(MDbConstant.WHSE_ID,WHS);
    ht.put(MDbConstant.BIN_NO,BIN);

    isQtyExists=dao.isExisit(ht);
    MLogger.log(0,"isQtyExists : " + isQtyExists);
    Map map=new HashMap();

    String sql=MDbConstant.TRAN_QTY + ","+ MDbConstant.UOM;

    if(isQtyExists)
    {
    //getonhandqty

     map=getOnHandQty(ITEM,PLANT,LOT,WHS,BIN);

    onHandQty=(String)map.get(MDbConstant.TRAN_QTY);
    uom=(String)map.get(MDbConstant.UOM);

    }
    else
    {
      Hashtable ht_batMst=new Hashtable();

      ht_batMst.put(MDbConstant.MATERIAL_CODE,ITEM);
      ht_batMst.put("PLANT",PLANT);
      ht_batMst.put(MDbConstant.LOT_NUM,LOT);

     BatMstDAO _BatMstDAO = new BatMstDAO();
     isQtyExists=_BatMstDAO.isExisit(ht_batMst);

     if(isQtyExists)
     {
        MLogger.log(0,"Receiving qty for lot : " + "RECV_LOT");
       // ht.put(SConstant.LOT_NUM,"RECV_LOT");
        map=getOnHandQty(ITEM,PLANT,"RECV_LOT",WHS,BIN);

        onHandQty=(String)map.get(MDbConstant.TRAN_QTY);
        uom=(String)map.get(MDbConstant.UOM);
     }
     else
     {
       throw new Exception("Invalid Lot");
     }
    }

    //getLotSize()

      //getuom()
      uom=new ItemMstDAO().getItemUOM(PLANT,ITEM);

          xmlStr = xu.getXMLHeader();
          xmlStr = xmlStr + xu.getStartNode("onHandQty");
          xmlStr = xmlStr + xu.getXMLNode("qty", onHandQty);
          xmlStr = xmlStr + xu.getXMLNode("uom", uom);
          xmlStr = xmlStr + xu.getXMLNode("lotSize", getLotSize(PLANT,ITEM));
          xmlStr = xmlStr + xu.getEndNode("onHandQty");

     MLogger.log(0, "XML : " + xmlStr);

     MLogger.log(-1, this.getClass() + " getOnHandQty_xml()");

    return xmlStr;
  }


  public Map getOnHandQty(String ITEM, String PLANT,
                                 String LOT,String WHS,String BIN) throws Exception
                                 {
   // MLogger.log(1, this.getClass() + " getOnHandQty()");
      InvMstDAO dao = new InvMstDAO();
     Hashtable ht=new Hashtable();

    ht.put(MDbConstant.MATERIAL_CODE,ITEM);
    ht.put(MDbConstant.PLANT,PLANT);
    ht.put(MDbConstant.LOT_NUM,LOT);
    ht.put(MDbConstant.WHSE_ID,WHS);
    ht.put(MDbConstant.BIN_NO,BIN);

     Map map=new HashMap();

    String sql=MDbConstant.TRAN_QTY + ","+ MDbConstant.UOM;


    return dao.selectRow(sql,ht);

   // MLogger.log(-1, this.getClass() + " getOnHandQty()");

  }

  public String getLotSize(String PLANT,String ITEM ) throws Exception
                                 {
   // MLogger.log(1, this.getClass() + " getOnHandQty()");
     ItemMstDAO _ItemMstDAO = new ItemMstDAO();
     Hashtable ht=new Hashtable();

    ht.put(MDbConstant.MATERIAL_CODE,ITEM);
    ht.put("PLANT",PLANT);


     Map map=new HashMap();

    String sql="MFGLOTSIZE";

     map= _ItemMstDAO.selectRow(sql,ht);

    return (String)map.get("MFGLOTSIZE");

   // MLogger.log(-1, this.getClass() + " getOnHandQty()");

  }

/*

  public String process_Adjust_Inventory(Map obj) {

     MLogger.log(1, this.getClass() + " process_Adjust_Inventory()");

      XMLUtils xu=new XMLUtils();
      boolean flag = false;
        MLogger.log(0,"Need Backend transaction :: " + MConstants.BACKEND );
      if (MConstants.BACKEND) {
        MLogger.log(0," process_Adjust_Inventory :: " + "Starts" );

        flag = process_Seiko_Adjust_Inventory(obj);

        MLogger.log(0," process_Adjust_Inventory :: " + flag );
        if (flag) {
          MLogger.log(0," process_Adjust_Inventory :: " + "Starts" );

          flag = process_Wms_Adjust_Inventory(obj);

          MLogger.log(0," process_Adjust_Inventory :: " + flag );
        }
      }
      else {

         MLogger.log(0," process_Wms_Adjust_Inventory :: " + "Starts" );

         flag = process_Wms_Adjust_Inventory(obj);
          MLogger.log(0, "process_Adjust_Inventory()" +  flag);
         MLogger.log(0," process_Wms_Adjust_Inventory :: " + flag );
      }

      String xmlStr="";
      if(flag==true){

         xmlStr = xu.getXMLMessage(1, "adjust Inventory successfully!");
      }else{

         xmlStr = xu.getXMLMessage(0, "Error in adjusting the Inventory,Try Again");
      }
       MLogger.log(-1, this.getClass() + " process_Adjust_Inventory()");
      return xmlStr;
    }

    */

  public String process_Misc_Adjust_Inventory(Map obj) {
   boolean flag = false;
   UserTransaction ut = null;
   try
   {
     ut = com.murho.gates.DbBean.getUserTranaction();
     ut.begin();
     if (MConstants.BACKEND){
       MLogger.log(0,"Backend transaction :: " + MConstants.BACKEND );
         flag = process_Seiko_Misc_Adjust_Inventory(obj);
       MLogger.log(0,"After processing --> process_Seiko_AdjustWip :: " + flag );
       if (flag){
        flag = process_Wms_Misc_Adjust_Inventory(obj);
         MLogger.log(0,"After processing --> process_Wms_AdjustWip :: " + flag );
       }
     }
     else{
     flag = process_Wms_Misc_Adjust_Inventory(obj);
     }
     if (flag == true){
         DbBean.CommitTran(ut);
         flag= true;
     }
     else{
        DbBean.RollbackTran(ut);
        flag= false;
     }
   }
   catch (Exception e) {
       flag = false;
       DbBean.RollbackTran(ut);
   }
   String xmlStr="";
   if(flag==true){
       xmlStr = xu.getXMLMessage(1, "Inventory adjusted successfully!");
    }else{
       xmlStr = xu.getXMLMessage(0, "Error in adjusting inventory ?");
    }
    return xmlStr;
  }





     private boolean process_Seiko_Misc_Adjust_Inventory(Map issueMaterial_HM) throws Exception{
         MLogger.log( 1, this.getClass() + " process_Seiko_Misc_Adjust_Inventory()");
         boolean flag=false;
         SeikoTran tran=new com.seiko.tran.SeikoMiscAdjustInventory();

         flag= tran.processSeikoTran(issueMaterial_HM);

         MLogger.log( -1, this.getClass() + " process_Seiko_Misc_Adjust_Inventory()");
      return flag;
    }

    private boolean process_Wms_Misc_Adjust_Inventory(Map map)  throws Exception {
      MLogger.log(1, this.getClass() + " process_Wms_Misc_Adjust_Inventory()");
      boolean flag=false;

      WmsTran tran=new com.murho.tran.WmsMiscAdjustInventory();

      flag= tran.processWmsTran(map);

      MLogger.log(0, "process_Wms_Misc_Adjust_Inventory()" +  flag);
      MLogger.log(-1, this.getClass() + " process_Wms_Misc_Adjust_Inventory()");

      return flag;

    }

  //baseer 14-12-2006

  public String loadToWareHouse(String company,String item) throws Exception {
    MLogger.log(1, this.getClass() + " getAllWarehouse()");
    String xmlStr = "";
    ArrayList al = null;
    XMLUtils xu = new XMLUtils();
    InvMstDAO dao = new InvMstDAO();

    Hashtable ht = new Hashtable();
    MLogger.log(0,"company:"+company);
    MLogger.log(0,"item:"+item);
    //query
    String query = "DISTINCT " + MDbConstant.WHSE_ID;

    //condition

    ht.put(MDbConstant.COMPANY,company);
    ht.put(MDbConstant.MATERIAL_CODE,item);

    al = dao.GetAllWareHouse(company);

    // xmlStr = xu.getXMLHeader();
    // xmlStr = xmlStr + xu.getStartNode("AllWarehouses");
    try {

      al = dao.GetAllWareHouse(company);

      MLogger.log(0, "Record size() :: " + al.size());
      if (al.size() > 0) {
        xmlStr += xu.getXMLHeader();
        xmlStr +=
            xu.getStartNode("AllWarehouses total='" + String.valueOf(al.size()) +
                            "'");
        for (int i = 0; i < al.size(); i++) {
          Map map = (Map) al.get(i);
           MLogger.log(0, "Record size() :: " + al.get(i));
          xmlStr += xu.getStartNode("record");
          xmlStr +=
              xu.getXMLNode("warehouseCode", (String) map.get(MDbConstant.WHSE_ID));
          xmlStr += xu.getEndNode("record");
        }
      }
      xmlStr = xmlStr + xu.getEndNode("AllWarehouses");
      MLogger.log(0, "Value of xml : " + xmlStr);
    }
    catch (Exception e) {
      MLogger.exception("getAllWarehouse()", e);
    }
    MLogger.log( -1, this.getClass() + " getAllWarehouse()");
    return xmlStr;

  }


  /////////////////////////////////////////////////////////////////////////////


  public String LoadToBin(String company,String whs) throws Exception {
    MLogger.log(1, this.getClass() + " LoadToBin()");
    String xmlStr = "";
    ArrayList al = null;
    XMLUtils xu = new XMLUtils();
    InvMstDAO dao = new InvMstDAO();

    Hashtable ht = new Hashtable();
    MLogger.log(0,"company:"+company);
    MLogger.log(0,"whs:"+whs);
     al = dao.GetBinForWareHouse(company,whs);

    // xmlStr = xu.getXMLHeader();
    // xmlStr = xmlStr + xu.getStartNode("AllWarehouses");
    try {

      al = dao.GetBinForWareHouse(company,whs);

      MLogger.log(0, "Record size() :: " + al.size());
      if (al.size() > 0) {
        xmlStr += xu.getXMLHeader();
        xmlStr +=
            xu.getStartNode("AllWarehouses total='" + String.valueOf(al.size()) +
                            "'");
        for (int i = 0; i < al.size(); i++) {
          Map map = (Map) al.get(i);
           MLogger.log(0, "Record size() :: " + al.get(i));
          xmlStr += xu.getStartNode("record");
          xmlStr +=
              xu.getXMLNode("warehouseCode", (String) map.get("BINNO"));
          xmlStr += xu.getEndNode("record");
        }
      }
      xmlStr = xmlStr + xu.getEndNode("AllWarehouses");
      MLogger.log(0, "Value of xml : " + xmlStr);
    }
    catch (Exception e) {
      MLogger.exception("getAllWarehouse()", e);
    }
    MLogger.log( -1, this.getClass() + " LoadToBin()");
    return xmlStr;

  }


 public String Load_Default_Details(String company, String partNum) throws Exception {
    MLogger.log(1, this.getClass() + " Load_Default_Details() *********** ");
    String xmlStr = "";
    ArrayList al = null;
   // FmRouteDAO dao = new FmRouteDAO();
    
    ItemMstDAO _ItemMstDAO=new ItemMstDAO();
    
    xmlStr = xu.getXMLHeader();
   
    xmlStr = xmlStr + xu.getStartNode("DefaultDetails");

 // String tranDate=  com.murho.utils.Generator.getDate();
 String tranDate=  Generator.getDate();

     MLogger.log(0,"Stage : 4");

    MLogger.log(0,"tranDate " + tranDate);

    String partDesc= _ItemMstDAO.getItemDescription(company,partNum);
     MLogger.log(0,"partDesc " + partDesc);
    Map m=_ItemMstDAO.get_Default_FrWh_FrBin(company,partNum);
     MLogger.log(0,"wh :  " +(String)m.get(MDbConstant.WHSE_ID));
      MLogger.log(0,"bin :  " + (String)m.get(MDbConstant.BIN_NO));

    String uom=_ItemMstDAO.getItemUOM(company,partNum);

   MLogger.log(0,"uom " + uom);

    try {
   //   al = dao.getToWhseAndToBin(company, jobNum);
    //  MLogger.log(0, "Record size() :: " + al.size());


          xmlStr = xmlStr + xu.getXMLNode("partNum",partNum );
          xmlStr = xmlStr + xu.getXMLNode("partDesc",partDesc );
          xmlStr = xmlStr + xu.getXMLNode("frWhse",(String)m.get(MDbConstant.WHSE_ID) );
          xmlStr = xmlStr + xu.getXMLNode("frBin", (String)m.get(MDbConstant.BIN_NO));
          xmlStr = xmlStr + xu.getXMLNode("tranDate",tranDate);
          xmlStr = xmlStr + xu.getXMLNode("onHandQty",new InvMstDAO().get_OnHand_Qty(company,partNum,(String)m.get(MDbConstant.WHSE_ID),(String)m.get(MDbConstant.BIN_NO)));
          xmlStr = xmlStr + xu.getXMLNode("uom",uom  );


      xmlStr = xmlStr + xu.getEndNode("DefaultDetails");
      MLogger.log(0, "Value of xml : " + xmlStr);
    }
    catch (Exception e) {
      MLogger.exception("Load_Default_Details()", e);
    }
    MLogger.log( -1, this.getClass() + " Load_Default_Details()");
    return xmlStr;
  }

   public String Load_Lot_Qty(String company, String partNum,String whid,String binno,String lotno) throws Exception {
    MLogger.log(1, this.getClass() + " Load_Lot_Qty() 2222222 ");
    String xmlStr = "";
     xmlStr = xu.getXMLHeader();
     xmlStr = xmlStr + xu.getStartNode("LotQty");
     String qty=new InvMstDAO().get_Lot_Qty(company,partNum,whid,binno,lotno);
     MLogger.log(0,"Lot qty " + qty);
    try {
      xmlStr = xmlStr + xu.getXMLNode("qty",qty);
      xmlStr = xmlStr + xu.getEndNode("LotQty");
      MLogger.log(0, "Value of xml : " + xmlStr);
    }
    catch (Exception e) {
      MLogger.exception("Load_Lot_Qty()", e);
    }
    MLogger.log( -1, this.getClass() + " Load_Lot_Qty()");
    return xmlStr;
  }


        public boolean is_valid_PackNumber_Exist(String aCompany, String aPartNum,String aPackNum) {

         MLogger.log(0, "is_valid_PackNumber_Exist()  %%%%%%%%%%%%%%%%%%%%%%%%  Starts");

         boolean isValid = false;
            MscShpDtDAO dao = new MscShpDtDAO();
           try {
             isValid = dao.is_valid_PackNumber_Exist(aCompany,aPartNum,aPackNum);
           MLogger.log(0,"is_valid_PackNumber_Exist = " + isValid);
          }
         catch (Exception e) {
           MLogger.exception("is_valid_PackNumber_Exist()", e);
           isValid = false;
         }
         MLogger.log(0, "is_valid_PackNumber_Exist()  %%%%%%%%%%%%%%%%%%%%%%%%  ends");
         return isValid;
       }

        public boolean is_valid_InvoiceNumber_Exist(String aCompany, String aPartNum,String aPackNum) {

         MLogger.log(0, "is_valid_InvoiceNumber_Exist()  %%%%%%%%%%%%%%%%%%%%%%%%  Starts");

         boolean isValid = false;
            MscShpDtDAO dao = new MscShpDtDAO();
           try {
             isValid = dao.is_valid_InvoiceNumber_Exist(aCompany,aPartNum,aPackNum);
           MLogger.log(0,"is_valid_InvoiceNumber_Exist = " + isValid);
          }
         catch (Exception e) {
           MLogger.exception("is_valid_InvoiceNumber_Exist()", e);
           isValid = false;
         }
         MLogger.log(0, "is_valid_InvoiceNumber_Exist()  %%%%%%%%%%%%%%%%%%%%%%%%  ends");
         return isValid;
       }


   public String LoadOnHandQty(String company, String partNum,String whid,String binno) throws Exception {
    MLogger.log(1, this.getClass() + " LoadOnHandQty() 2222222 ");
    String xmlStr = "";
     xmlStr = xu.getXMLHeader();
     xmlStr = xmlStr + xu.getStartNode("LotQty");
     String qty=new InvMstDAO().get_OnHand_Qty(company,partNum,whid,binno);
     MLogger.log(0,"Lot qty " + qty);
    try {
      xmlStr = xmlStr + xu.getXMLNode("qty",qty);
      xmlStr = xmlStr + xu.getEndNode("LotQty");
      MLogger.log(0, "Value of xml : " + xmlStr);
    }
    catch (Exception e) {
      MLogger.exception("LoadOnHandQty()", e);
    }
    MLogger.log( -1, this.getClass() + " LoadOnHandQty()");
    return xmlStr;
  }

}
