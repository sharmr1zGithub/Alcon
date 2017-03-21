package com.murho.pda;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.murho.gates.Generator;
import com.murho.utils.DateUtils;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;

public class ReceiptEntryServlet
    extends HttpServlet {

  Context ctx = null;
  XMLUtils xu = new XMLUtils();
  MLogger logger = new MLogger();
  StrUtils strUtils = new StrUtils();
  Generator generator = new Generator();
  ReceiptEntryUtil receiptEntryUtil = null;

  String plant = "", receiptDate = "", SupplierID = "", packSlip = "",
      PONumber = "";

  //public static final boolean DEBUG = true;

  String xmlStr = "";
  String action = "";
  String strLoginUser = "";

  private static final String CONTENT_TYPE = "text/xml";

  public void init() throws ServletException {
    receiptEntryUtil = new ReceiptEntryUtil();
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException {

    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();
    try {

      action = request.getParameter("action").trim();
      strLoginUser = strUtils.fString(request.getParameter("LOGIN_USER"));


      MLogger.log(1,
                  strLoginUser + " : " + this.getClass() + " doGet() ##################");
       MLogger.log(0, "Loggin user : " + strLoginUser);
       MLogger.log(0, "Action      : " + action);
       MLogger.info("Testing");

      if (action.equalsIgnoreCase("load_PoHeader_Info")) {

        xmlStr = "";
        xmlStr = load_PoHeader_Info(request, response);

      }
      else if (action.equalsIgnoreCase("load_PoHeader_Info_SubCon")) {

        xmlStr = "";
        xmlStr = load_PoHeader_Info_SubCon(request, response);

      }
      else if (action.equalsIgnoreCase("load_PoHeader_For_WOutLot")) {

        xmlStr = "";
        xmlStr = getPODetailsInfo_For_WOLot(request, response);

      }
      else if (action.equalsIgnoreCase("load_PoDetails_Info")) {

        xmlStr = "";
        xmlStr = load_PoDetails_Info(request, response);

      }
      else if (action.equalsIgnoreCase("load_PoDetails_Info_SubCon")) {

        xmlStr = "";
        xmlStr = load_PoDetails_Info_SubCon(request, response);

      }
      else if (action.equalsIgnoreCase("GenerateLot")) {
        xmlStr = "";
        xmlStr = Process_Receiving_WO_Lot(request, response);

      }
      else if (action.equalsIgnoreCase("DownLoadPO")) {

        xmlStr = "";
        xmlStr = DownLoadPO(request, response);

      }
      else if (action.equalsIgnoreCase("load_Lot_List")) {

        xmlStr = "";
        xmlStr = load_Lot_Details(request, response);
      MLogger.info("TEsting ************** connecion not closed");
      }
      else if (action.equalsIgnoreCase("InsertReceiveDetWithLot")) {

        xmlStr = "";
        xmlStr = Process_Receiving_W_Lot(request, response);

      }

      else if (action.equalsIgnoreCase("is_valid_lno")) {

        xmlStr = "";
        xmlStr = is_valid_lno(request, response);

      }
      else if (action.equalsIgnoreCase("is_valid_HeaderInfo_Exist")) {

        xmlStr = "";
        xmlStr = is_valid_HeaderInfo_Exist(request, response);

      }

    }

    catch (Exception e) {

      MLogger.exception(" Exception :: doGet() : ", e);
   //   e.printStackTrace();
      xmlStr = xu.getXMLMessage(1, e.getMessage());

    }
    

    MLogger.log( -1,
                strLoginUser + " : " + this.getClass() + " doGet() ##################");
    out.write(xmlStr);
    out.close();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException {
    doGet(request, response);
  }

  public void destroy() {
  }

  private String load_PoHeader_Info(HttpServletRequest request,
                                    HttpServletResponse response) throws
      IOException, ServletException, Exception {
    String str = "";
    try {

      SupplierID = strUtils.fString(request.getParameter("PONO"));
      receiptDate = strUtils.fString(request.getParameter("DATE"));
      plant = strUtils.fString(request.getParameter("PLANT"));

      str = receiptEntryUtil.getPoHdrInfo(SupplierID, receiptDate, plant);

    }
    catch (Exception e) {
      //   MLogger.exception( "load_PoHeader_Info() : ", e);
      throw e;
    }
   // MLogger.log(0, "load_PoHeader_Info() Ends");
    return str;
  }

  private String load_PoHeader_Info_SubCon(HttpServletRequest request,
                                           HttpServletResponse response) throws
      IOException, ServletException {
    String str = "";
    try {

      MLogger.info("load_PoHeader_Info_SubCon() Starts");

      SupplierID = strUtils.fString(request.getParameter("PONO"));
      receiptDate = strUtils.fString(request.getParameter("DATE"));
      plant = strUtils.fString(request.getParameter("PLANT"));

      str = receiptEntryUtil.getPOHdrInfo_SubCon(SupplierID, receiptDate, plant);

      if (str.equalsIgnoreCase("")) {
        str = xu.getXMLMessage(1, "Details not found");
      }
    }
    catch (Exception e) {
      MLogger.exception(
          " %%%%%%%%%%%%%%%%%%%%%%%%% Exception :: load_PoHeader_Info_SubCon() : ",
          e);
    }
    MLogger.info("load_PoHeader_Info_SubCon() Ends");
    return str;
  }

  private String load_PoHeader_For_WOutLot(HttpServletRequest request,
                                           HttpServletResponse response) throws
      IOException, ServletException {
    String str = "";
    try {

      MLogger.info("load_PoHeader_Info() Starts");

      SupplierID = strUtils.fString(request.getParameter("PONO"));
      receiptDate = strUtils.fString(request.getParameter("DATE"));
      plant = strUtils.fString(request.getParameter("PLANT"));

      str = receiptEntryUtil.load_PoHeader_For_WOutLot(SupplierID, receiptDate,
          plant);

      if (str.equalsIgnoreCase("")) {
        str = xu.getXMLMessage(1, "Details not found");
      }
    }
    catch (Exception e) {
      MLogger.exception(
          " %%%%%%%%%%%%%%%%%%%%%%%%% Exception :: loadJobDetails() : ", e);
    }
    MLogger.info("load_PoHeader_Info() Ends");
    return str;
  }

  private String load_PoDetails_Info(HttpServletRequest request,
                                     HttpServletResponse response) throws
      IOException, ServletException {
    String str = "";
    try {

      MLogger.log(0, "loadJobDetails() Starts");

      PONumber = strUtils.fString(request.getParameter("PONO"));
      packSlip = strUtils.fString(request.getParameter("PACKSLIP"));
      plant = strUtils.fString(request.getParameter("PLANT"));

      str = receiptEntryUtil.getPODetailsInfo(PONumber, packSlip, plant);

      if (str.equalsIgnoreCase("")) {
        str = xu.getXMLMessage(1, "Details not found");
      }
    }
    catch (Exception e) {
      MLogger.exception(
          " %%%%%%%%%%%%%%%%%%%%%%%%% Exception :: load_PoDetails_Info() : ", e);
    }
    return str;
  }

  private String getPODetailsInfo_For_WOLot(HttpServletRequest request,
                                            HttpServletResponse response) throws
      IOException, ServletException, Exception {
    String str = "";
    try {

  //    MLogger.info("getPODetailsInfo_For_WOLot() Starts");

      PONumber = strUtils.fString(request.getParameter("PONO"));
      packSlip = strUtils.fString(request.getParameter("PACKSLIP"));
      plant = strUtils.fString(request.getParameter("PLANT"));

      str = receiptEntryUtil.getPODetailsInfo_For_WOLot(PONumber, packSlip,
          plant);
      /* removed 28-05-2007
        if (str.equalsIgnoreCase("")) {
          str = xu.getXMLMessage(1, "Details not found");
        }
       */
    }
    catch (Exception e) {
      MLogger.exception(
          " %%%%%%%%%%%%%%%%%%%%%%%%% Exception :: getPODetailsInfo_For_WOLot() : ",
          e);
      throw e;
    }
    return str;
  }

  private String load_PoDetails_Info_SubCon(HttpServletRequest request,
                                            HttpServletResponse response) throws
      IOException, ServletException {
    String str = "";
    try {

      MLogger.log(0, "load_PoDetails_Info_SubCon() Starts");

      PONumber = strUtils.fString(request.getParameter("PONO"));
      packSlip = strUtils.fString(request.getParameter("PACKSLIP"));
      plant = strUtils.fString(request.getParameter("PLANT"));

      str = receiptEntryUtil.getPODetailsInfo_SubCon(PONumber, packSlip, plant);

      if (str.equalsIgnoreCase("")) {
        str = xu.getXMLMessage(1, "Details not found");
      }
    }
    catch (Exception e) {
      MLogger.exception(
          " %%%%%%%%%%%%%%%%%%%%%%%%% Exception :: load_PoDetails_Info_SubCon() : ",
          e);
    }
    return str;
  }

  /**
   *
   * @param request
   * @param response
   * @return
   * @throws IOException
   * @throws ServletException
   */
  private String DownLoadPO(HttpServletRequest request,
                            HttpServletResponse response) throws
      IOException, ServletException {
    String str = "";
    MLogger.log(1, "DownLoadPO() Starts");
    try {
      str = receiptEntryUtil.DownLoadPO();
      if (str.equalsIgnoreCase("")) {
        str = xu.getXMLMessage(1, "Details not found");
      }
    }
    catch (Exception e) {
      MLogger.exception(
          " %%%%%%%%%%%%%%%%%%%%%%%%% Exception :: DownLoadPO() : ", e);
    }
    MLogger.log( -1, "DownLoadPO() Ends");
    return str;
  }

  private String Process_Receiving_WO_Lot(HttpServletRequest request,
                                          HttpServletResponse response) throws
      IOException,
      ServletException, Exception {
    MLogger.log(0,
        "*************** Process_Receiving_WO_Lot()  << STRATS >> ***************");
    Map receiveLot_HM = null;

    String COMPANY = "", PLANT = "", SUPPLIER = "", PACKSLIP = "", PONO = "",
        POLINENO = "", PART = "", WHS = "", BIN = "", RECV_QTY = "", UOM = "",
        LOT = "";

    String transNum = "1";
    String str = "";
    String datetimes = generator.getDateAtTime(); //  yyyy-MM-dd HH:mm:ss
    String date = datetimes.substring(0, 10);
    String orgDate = date.substring(0, 4) + date.substring(5, 7) +
        date.substring(8, 10);
    //String monthYear =  date.substring(2,4)+date.substring(5, 7);

    try {

      COMPANY = strUtils.fString(request.getParameter("COMPANY"));
      // PLANT = strUtils.fString(request.getParameter("PLANT"));
      SUPPLIER = strUtils.fString(request.getParameter("SUPPLIER"));
      PACKSLIP = strUtils.fString(request.getParameter("PACKSLIP"));
      PONO = strUtils.fString(request.getParameter("PONO"));
      POLINENO = strUtils.fString(request.getParameter("PO_LINENO"));
      PART = strUtils.fString(request.getParameter("PART"));
      WHS = strUtils.fString(request.getParameter("WHS"));
      BIN = strUtils.fString(request.getParameter("BIN"));
      RECV_QTY = strUtils.fString(request.getParameter("RECV_QTY"));
      UOM = strUtils.fString(request.getParameter("UOM"));
      LOT = strUtils.fString(request.getParameter("NUM_OF_LOT"));

      String PACK_SLIP_LNO = strUtils.fString(request.getParameter(
          "PACK_SLIP_LNNO"));
      String PUR_PT = strUtils.fString(request.getParameter("PURPOINT"));
      String LOGIN_USER = strUtils.fString(request.getParameter("LOGIN_USER"));
      String RECEIPT_DATE = strUtils.fString(request.getParameter(
          "RECEIPT_DATE"));

      String monthYear = RECEIPT_DATE.substring(2, 6);

      receiveLot_HM = new HashMap();
      receiveLot_HM.put(SConstant.COMPANY, COMPANY);
      receiveLot_HM.put(SConstant.VENDER_NUM, SUPPLIER);
      receiveLot_HM.put(SConstant.PACK_SLIP_NUM, PACKSLIP);
      receiveLot_HM.put(SConstant.PO_NUM, PONO);
      receiveLot_HM.put(SConstant.PO_LINE_NUM, POLINENO);

      receiveLot_HM.put(SConstant.PURCHASE_POINT, PUR_PT);
      receiveLot_HM.put(SConstant.PACK_LN_NO, PACK_SLIP_LNO);

      receiveLot_HM.put(SConstant.MATERIAL_CODE, PART);
      receiveLot_HM.put(SConstant.FR_WHSE, WHS);
      receiveLot_HM.put(SConstant.FR_BIN, BIN);
      receiveLot_HM.put(SConstant.RECV_QTY, RECV_QTY);
      receiveLot_HM.put(SConstant.UOM, UOM);
      receiveLot_HM.put(SConstant.NUM_OF_LOT, LOT);
      receiveLot_HM.put(SConstant.BATCH_DATE, orgDate);
      receiveLot_HM.put(SConstant.BATCH_DATE_MMDD, monthYear);
      receiveLot_HM.put(SConstant.LOGIN_USER, LOGIN_USER);
      receiveLot_HM.put(SConstant.RECEIPT_DATE, RECEIPT_DATE);
      String tempDate = com.murho.utils.Generator.getDate();
      tempDate = DateUtils.getDateinyyyy_mm_dd(tempDate);
      receiveLot_HM.put(SConstant.TRAN_DATE, tempDate);
      receiveLot_HM.put(SConstant.SYS_TIME, com.murho.utils.Generator.getTime());

      //this is for generating lot.
      receiveLot_HM.put("NEED_LOT_GENERATION", "true");

      MLogger.log("COMPANY    : " + receiveLot_HM.get(SConstant.COMPANY));
      MLogger.log("SUPPLIER   : " + receiveLot_HM.get(SConstant.VENDER_NUM));
      MLogger.log("PACKSLIP   : " + receiveLot_HM.get(SConstant.PACK_SLIP_NUM));
      MLogger.log("PONO       : " + receiveLot_HM.get(SConstant.PO_NUM));
      MLogger.log("LINENO     : " + receiveLot_HM.get(SConstant.PO_LINE_NUM));
      MLogger.log("PART       : " + receiveLot_HM.get(SConstant.MATERIAL_CODE));
      MLogger.log("WHS        : " + receiveLot_HM.get(SConstant.FR_WHSE));
      MLogger.log("BIN        : " + receiveLot_HM.get(SConstant.FR_BIN));
      MLogger.log("RECVQTY    : " + receiveLot_HM.get(SConstant.RECV_QTY));
      MLogger.log("UOM        : " + receiveLot_HM.get(SConstant.UOM));
      MLogger.log("NO OF LOT  : " + receiveLot_HM.get(SConstant.NUM_OF_LOT));
      MLogger.log("NEED_LOT_GENERATION      : " +
                  receiveLot_HM.get("NEED_LOT_GENERATION"));
      MLogger.log("RECEIPT_DATE             : " +
                  receiveLot_HM.get("RECEIPT_DATE"));
      MLogger.log("BATCH_DATE_MMDD          : " +
                  receiveLot_HM.get("BATCH_DATE_MMDD"));
    }

    catch (Exception e) {

      MLogger.exception("Process_Receiving_WO_Lot()", e);

      throw e;

    }

    ReceiptEntryUtil util = new ReceiptEntryUtil();

    xmlStr = util.process_Receiveing(receiveLot_HM);

    MLogger.log(0,
        "*************** Process_Receiving_WO_Lot()  << ENDS >> ***************");

    return xmlStr;
  }

  private String Process_Receiving_W_Lot(HttpServletRequest request,
                                         HttpServletResponse response) throws
      IOException,
      ServletException, Exception {

    MLogger.log(0,
        "*************** Process_Receiving()  << STRATS >> ***************");
    Map mapReWlot = null;
    String COMPANY = "", PLANT = "", SUPPLIER = "", PACKSLIP = "", PONO = "",
        LINENO = "",
        PART = "", WHS = "", BIN = "", TAB_RECV_QTY = "", UOM = "", LOT = "",
        RECV_QTY = "";
    String str = "";
    String datetimes = generator.getDateAtTime();
    String date = datetimes.substring(0, 10);
    String orgDate = date.substring(0, 4) + date.substring(5, 7) +
        date.substring(8, 10);
    String monthYear = date.substring(5, 7) + date.substring(8, 10);

    try {

      COMPANY = strUtils.fString(request.getParameter("COMPANY"));
      PLANT = strUtils.fString(request.getParameter("PLANT"));
      SUPPLIER = strUtils.fString(request.getParameter("SUPPLIER_ID"));
      PACKSLIP = strUtils.fString(request.getParameter("PACKSLIP_NUM"));
      String PACK_SLIP_LNO = strUtils.fString(request.getParameter(
          "PACK_SLIP_LN_NUM"));
      PONO = strUtils.fString(request.getParameter("PO_NUM"));
      LINENO = strUtils.fString(request.getParameter("PO_LINE_NUM"));
      PART = strUtils.fString(request.getParameter("PART_NUM"));
      WHS = strUtils.fString(request.getParameter("WHS_ID"));
      BIN = strUtils.fString(request.getParameter("BIN_NUM"));
      //TAB_RECV_QTY = strUtils.fString(request.getParameter("RECV_QTY"));
      UOM = strUtils.fString(request.getParameter("UOM"));
      LOT = strUtils.fString(request.getParameter("LOT_NUM"));
      RECV_QTY = strUtils.fString(request.getParameter("RECV_QTY"));
      String PUR_PT = strUtils.fString(request.getParameter("PUR_POINT"));
      String LOGIN_USER = strUtils.fString(request.getParameter("LOGIN_USER"));
      String RECEIPT_DATE = strUtils.fString(request.getParameter(
          "RECEIPT_DATE"));

      mapReWlot = new HashMap();

      mapReWlot.put(SConstant.RECV_QTY, RECV_QTY);
      mapReWlot.put(SConstant.COMPANY, COMPANY);
      mapReWlot.put(SConstant.PLANT, PLANT);
      mapReWlot.put(SConstant.VENDER_NUM, SUPPLIER);
      mapReWlot.put(SConstant.PACK_SLIP_NUM, PACKSLIP);
      mapReWlot.put(SConstant.PACK_LN_NO, PACK_SLIP_LNO);
      mapReWlot.put(SConstant.PO_NUM, PONO);
      mapReWlot.put(SConstant.PO_LINE_NUM, LINENO);

      mapReWlot.put(SConstant.PURCHASE_POINT, PUR_PT);

      mapReWlot.put(SConstant.MATERIAL_CODE, PART);
      mapReWlot.put(SConstant.FR_WHSE, WHS);
      mapReWlot.put(SConstant.FR_BIN, BIN);
      mapReWlot.put(SConstant.RECV_QTY, RECV_QTY);
      mapReWlot.put(SConstant.UOM, UOM);
      mapReWlot.put(SConstant.LOT_NUM, LOT);
      mapReWlot.put(SConstant.LOGIN_USER, LOGIN_USER);
      mapReWlot.put(SConstant.RECEIPT_DATE, RECEIPT_DATE);
      String tempDate = com.murho.utils.Generator.getDate();
      tempDate = DateUtils.getDateinyyyy_mm_dd(tempDate);
      mapReWlot.put(SConstant.TRAN_DATE, tempDate);
      mapReWlot.put(SConstant.SYS_TIME, com.murho.utils.Generator.getTime());
      //This is for lot generation
      mapReWlot.put("NEED_LOT_GENERATION", "false");

      MLogger.log("COMPANY         : " + mapReWlot.get(SConstant.COMPANY));
      MLogger.log("PLANT           : " + mapReWlot.get(SConstant.PLANT));
      MLogger.log("SUPPLIER _ID    : " + mapReWlot.get(SConstant.VENDER_NUM));
      MLogger.log("PACKSLIP_NUM    : " + mapReWlot.get(SConstant.PACK_SLIP_NUM));
      MLogger.log("PACKSLIP_LN_NUM : " + mapReWlot.get(SConstant.PACK_LN_NO));
      MLogger.log("PUR_POINT       : " + mapReWlot.get(SConstant.PURCHASE_POINT));
      MLogger.log("PO_NUM          : " + mapReWlot.get(SConstant.PO_NUM));
      MLogger.log("PO_LN_NO        : " + mapReWlot.get(SConstant.PO_LINE_NUM));
      MLogger.log("PART            : " + mapReWlot.get(SConstant.MATERIAL_CODE));
      MLogger.log("WHS_ID          : " + mapReWlot.get(SConstant.FR_WHSE));
      MLogger.log("BIN             : " + mapReWlot.get(SConstant.FR_BIN));
      MLogger.log("RECV_QTY        : " + mapReWlot.get(SConstant.RECV_QTY));
      MLogger.log("UOM             : " + mapReWlot.get(SConstant.UOM));
      MLogger.log("LOT_NUM         : " + mapReWlot.get(SConstant.LOT_NUM));
      MLogger.log("RECEIPT_DATE    : " + mapReWlot.get(SConstant.RECEIPT_DATE));

    }

    catch (Exception e) {
      MLogger.exception("InsertReceiveDetWithLot()", e);
    }

    ReceiptEntryUtil util = new ReceiptEntryUtil();

    xmlStr = "";
    xmlStr = util.process_Receiveing(mapReWlot);
    MLogger.log(0,
        "*************** Process_Receiving() << ENDS >> ***************");

    return xmlStr;

  }

  private String load_Lot_Details(HttpServletRequest request,
                                  HttpServletResponse response) throws
      IOException, ServletException {
    String str = "", company = "", poNo = "", PoLnno = "", packLine = "",
        packSlip = "", purPoint = "", vendorId = "";
    try {

      MLogger.log(0, "load_Lot_List() Starts");

      company = strUtils.fString(request.getParameter("COMPANY"));
      plant = strUtils.fString(request.getParameter("PLANT"));
      poNo = strUtils.fString(request.getParameter("PONO"));
      PoLnno = strUtils.fString(request.getParameter("POLNNO"));
      packSlip = strUtils.fString(request.getParameter("PACKSLIP"));
      packLine = strUtils.fString(request.getParameter("PACKLINE"));
      purPoint = strUtils.fString(request.getParameter("PURPOINT"));
      vendorId = strUtils.fString(request.getParameter("VENDORID"));

      MLogger.log(0, "company         :" + company);
      MLogger.log(0, "poNo            :" + poNo);
      MLogger.log(0, "PoLnNum         :" + PoLnno);
      MLogger.log(0, "packSlip        :" + packSlip);
      str = receiptEntryUtil.GetLotList4Line(company, plant, poNo, PoLnno,
                                             packSlip, packLine, purPoint,
                                             vendorId);
      if (str.equalsIgnoreCase("")) {

        MLogger.log(0, " ########## Detail Not found");

        str = xu.getXMLMessage(1, "Details not found");
      }
    }
    catch (Exception e) {
      MLogger.exception(
          " %%%%%%%%%%%%%%%%%%%%%%%%% Exception :: load_Lot_List() : ", e);
    }
    MLogger.log(0, "load_Lot_List() Ends");

    return str;
  }

  private String is_valid_lno(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException {
    String str = "", company = "", poNo = "", poLnNo = "", packNum = "",
        packLine = "", part = "";
    try {
      company = strUtils.fString(request.getParameter("PLANT"));
      poNo = strUtils.fString(request.getParameter("PONO"));
      poLnNo = strUtils.fString(request.getParameter("POLNNO"));
      packNum = strUtils.fString(request.getParameter("PACKNUM"));
      packLine = strUtils.fString(request.getParameter("PACKLINE"));
      part = strUtils.fString(request.getParameter("PARTNUM"));
    }
    catch (Exception e) {
      MLogger.log(" %%%%%%%%%%%%%%%%%%%%%%%%% Exception :: in Action : " +
                  action + e.getMessage());
    }
    str = "";
    boolean isexists = false;
    isexists = receiptEntryUtil.isValidLnoForPoReceive(company, poNo, poLnNo,
        packNum, packLine, part);
    if (isexists) {
      str = xu.getXMLMessage(0, " Open Line to issue");
    }
    else {
      str = xu.getXMLMessage(1, "Closed line");
    }

    MLogger.log(0, "is_valid_lno() End .............................");

    return str;
  }

  private String is_valid_HeaderInfo_Exist(HttpServletRequest request,
                                           HttpServletResponse response) throws
      IOException, ServletException {
    String str = "", company = "", poNo = "", poLnNo = "", packNum = "",
        packLine = "", date = "";
    try {
      company = strUtils.fString(request.getParameter("PLANT"));
      poNo = strUtils.fString(request.getParameter("PONO"));
      date = strUtils.fString(request.getParameter("DATE"));
      MLogger.log(0, "company         :" + company);
      MLogger.log(0, "poNo            : " + poNo);
      MLogger.log(0, "date            : " + date);
    }
    catch (Exception e) {
      MLogger.log(" %%%%%%%%%%%%%%%%%%%%%%%%% Exception :: in Action : " +
                  action + e.getMessage());
    }
    str = "";
    boolean isexists = false;
    isexists = receiptEntryUtil.is_valid_HeaderInfo_Exist(company, poNo, date);
    if (isexists) {
      str = xu.getXMLMessage(0, " Open Line to issue");
    }
    else {
      str = xu.getXMLMessage(1, "Closed line");
    }

    MLogger.log(0,
                "is_valid_HeaderInfo_Exist() End .............................");

    return str;
  }

} //end of classs
