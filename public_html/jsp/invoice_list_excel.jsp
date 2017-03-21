<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import ="com.murho.dao.ShipDetDAO"%>
<%@ page import ="com.murho.dao.CustMstDAO"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.math.BigDecimal"%>
<%

      response.setContentType("application/vnd.ms-excel");
     // response.setContentType("application/plain");
      response.setHeader("Content-Disposition", "attachment;filename=InvoiceListExcel.xls");
    //  response.setHeader("Content-Disposition", "attachment;filename=PutAwaySummaryExcel.txt");
%>
<html>
<head>
<title>Packing List Summary Excel</title>
    <style>
        td, th { font-family: Tahoma, Verdana, sans-serif; font-size: 10px; }
    </style>
</head>

<link rel="stylesheet" href="css/style.css">
<body bgcolor="#ffffff"  text="#000000" link="#efe7c6" alink="#9c8349" vlink="#535353">
<form>
<%
StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();

ShipDetDAO shipDetDAO=new ShipDetDAO();
CustMstDAO custDAO = new CustMstDAO();
ArrayList pckQryList  = new ArrayList();
ArrayList sumryQryList  = new ArrayList();
Map m = new HashMap();
Map mCon = new HashMap();
Map mBill = new HashMap();
Map mMfg = new HashMap();
Map mHand = new HashMap();
MLogger logger = new MLogger();
boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
Date dt = new Date();
Calendar calendar = new GregorianCalendar();
calendar.setTime(dt);
String date =  new SimpleDateFormat("mm/dd/yyyy").format(dt);
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("usrHiddenAction")).trim();
String user_id          = session.getAttribute("LOGIN_USER").toString().toUpperCase();
String INVOICE ="",  TRAVELER = "", HAGENT ="", ASN ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="";
String html = "",SHIP_TO ="";int totQty =0;int totalMulK =0;int kfactor =0;
float totPrice =0;int totQty1 =0;int totalMulK1 =0;
float totPrice1 =0;

String HiddenView="",HiddenAprove="";
TRAVELER    = strUtils.fString(request.getParameter("TRAVELER"));
INVOICE    = strUtils.fString(request.getParameter("INVOICE"));
HAGENT    = strUtils.fString(request.getParameter("HAGENT"));
ASN    = strUtils.fString(request.getParameter("ASN"));
SHIP_TO    = strUtils.fString(request.getParameter("SHIP_TO"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove    = strUtils.fString(request.getParameter("HiddenAprove"));

if(HiddenView.equalsIgnoreCase("GenerateExcel")){
 try{
    
    /*pckQryList = shipDetDAO.getPackingListForTraveler(PLANT,TRAVELER);
      if(pckQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no traveller summary found to display<h3></td></tr>";
     //  fieldDesc=fieldDesc+ITEM;
      } */
      sumryQryList  =shipDetDAO.getSummaryPackingListForTraveler(PLANT,TRAVELER);
      if(sumryQryList.size() < 1)
      {
        fieldDesc = "<tr><td><B><h3>Currently no traveller summary found to display<h3></td></tr>";
      }else{
      m= shipDetDAO.getQuantityDetails(PLANT,TRAVELER);
      mCon=custDAO.getAdressDetails(SHIP_TO,"CONSIGNEE");
      mBill=custDAO.getAdressDetails(SHIP_TO,"BILL_TO");
      mMfg=custDAO.getAdressDetails(SHIP_TO,"MFG_SHIPPER");
      mHand=custDAO.getAdressDetails(SHIP_TO,"HANDLING_AGENT");
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}

%>

  <table cellspacing="0" cellpadding="0" border="0" width="30%" align ="Center">
    <tr>
      <td width="46%">
        <STRONG>INVOICE #</STRONG>
      </td>
      <td width="11%">
        <DIV align="center">
          <STRONG>:</STRONG>
        </DIV>
      </td>
      <td width="43%">
        <DIV align="center"><%=INVOICE%></DIV>
      </td>
    </tr>
    <tr>
      <td width="46%">
        <STRONG>INVOICE DATE</STRONG>
      </td>
      <td width="11%">
        <DIV align="center">
          <STRONG>:</STRONG>
        </DIV>
      </td>
      <td width="43%">
        <DIV align="center"><%=date%></DIV>
      </td>
    </tr>
    <tr>
      <td width="46%">
        <STRONG>ASN/Traveler #</STRONG>
      </td>
      <td width="11%">
        <DIV align="center">
          <STRONG>:</STRONG>
        </DIV>
      </td>
      <td width="43%">
        <DIV align="center"><%=ASN%></DIV>
      </td>
    </tr>
  </table>
  <P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</P>
  <table cellspacing="0" cellpadding="0" border="0" width="90%" align ="center">
   <tr >
      <td width="22%" >
        <P>
          <b>CONSIGNEE:</b>
        </P>
      </td>
      <td width="22%"><b><b>HANDLING AGENT:</b></b></td>
      <td width="29%">
        <b>MFG/ </b>
        <b/>
        <b>SHIPPER:</b> 
      </td>
      <td width="27%">
        <P>
          <b>BILL TO :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>
        </P>
      </td>
    </tr>
    <tr>
      <td width="22%">&nbsp;</td>
      <td width="22%">&nbsp;</td>
      <td width="29%">&nbsp;</td>
      <td width="27%">&nbsp;</td>
    </tr>
     <tr>
    
      <td width="22%"><%=(String)mCon.get("COMPANY_NAME")%></td>
      <td width="29%"><%=(String)mHand.get("COMPANY_NAME")%></td>
      <td width="27%"><%=(String)mMfg.get("COMPANY_NAME")%></td>
       <td width="22%"><%=(String)mBill.get("COMPANY_NAME")%></td>
    </tr>
    <tr>
      <td width="22%"><%=(String)mBill.get("ADDR1")%></td>
      <td width="22%"><%=(String)mCon.get("ADDR1")%></td>
      <td width="29%"><%=(String)mHand.get("ADDR1")%></td>
      <td width="27%"><%=(String)mMfg.get("ADDR1")%></td>
    </tr>
    <tr>
      <td width="22%"><%=(String)mCon.get("ADDR2")%></td>
      <td width="29%"><%=(String)mHand.get("ADDR2")%></td>
      <td width="27%"><%=(String)mMfg.get("ADDR2")%></td>
      <td width="22%"><%=(String)mBill.get("ADDR2")%></td>
    </tr>
    <tr>
    
      <td width="22%"><%=(String)mCon.get("COUNTRY")%></td>
      <td width="29%"><%=(String)mHand.get("COUNTRY")%></td>
      <td width="27%"><%=(String)mMfg.get("COUNTRY")%></td>
       <td width="22%"><%=(String)mBill.get("COUNTRY")%></td>
    </tr>
    <tr>
     
      <td width="22%"><%=(String)mCon.get("PHONE")%></td>
      <td width="29%"><%=(String)mHand.get("PHONE")%></td>
      <td width="27%"><%=(String)mMfg.get("PHONE")%></td>
       <td width="22%"><%=(String)mBill.get("PHONE")%></td>
    </tr>
  </table>
  <P>&nbsp;</P>
    <%
      for (int iCnt =0; iCnt<sumryQryList.size(); iCnt++){
       Map lineArr1 = (Map) sumryQryList.get(iCnt);
        
          int iIndex = iCnt + 1;
        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
        float  PriceForQty1   = new Float((String)lineArr1.get("PICKQTY")).floatValue() * new Float((String)lineArr1.get("PRICE")).floatValue();
       
        kfactor = new Integer((String)lineArr1.get("KFACTOR")).intValue();
       %>
       <%
        totQty1 = totQty1+new Integer((String)lineArr1.get("PICKQTY")).intValue();
        totPrice1 = totPrice1+ PriceForQty1;
       }
        totalMulK =totQty1 * kfactor;
       %>

  <TABLE WIDTH="90%" border="0" cellspacing="1" cellpadding="2" align="center">
    <tr bgcolor="navy">
      <th width="38%">
        <FONT color="#ffffff">Description</FONT> 
      </th>
      <td width="15%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>Tariff#</STRONG> 
          </center>
        </font>
      </td>
      <td width="13%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>Quantity</STRONG> 
          </center>
        </font>
      </td>
      <td width="13%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>Uom</STRONG> 
          </center>
        </font>
      </td>
      <td width="23%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>Total Amount(USD)</STRONG> 
          </center>
        </font>
      </td>
    </tr>
    <TR>
      <TD align="center" width="38%">Soft Contact Lenses</TD>
      <TD align="center" width="15%"></TD>
      <TD align="center" width="13%"><%=totalMulK%></TD>
      <TD align="center" width="13%">EA</TD>
      <TD align="center" width="23%"><%=totPrice1%></TD>
    </TR>
     <TR>
      <TD align="center" width="38%"></TD>
      <TD align="center" width="15%"><Strong>Total Unit Singles :<Strong></TD>
      <TD align="center" width="13%"><%=totalMulK%></TD>
      <TD align="center" width="13%">EA</TD>
      <TD align="center" width="23%"></TD>
    </TR>
  </TABLE>
  <P>&nbsp;</P>
  <P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</P>
  <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <STRONG> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Detail Invoice: </STRONG></P>
  <TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <th width="43%">
           <FONT color="#ffffff">Product Type</FONT>&nbsp;
         </th>
         <td width="14%"><font color="#ffffff" align="left"><center>
           <STRONG>Quantity</STRONG> 
                    </center></td>
         <td width="11%"><font color="#ffffff" align="left"><center>
           <STRONG>Uom</STRONG>
         </center></td>
         <td width="12%"><font color="#ffffff" align="left"><center><STRONG>Price</STRONG></center></td>
         <td width="20%"><font color="#ffffff" align="left"><center><STRONG><STRONG>Amount(USD)</STRONG> </STRONG></center></td>
         </center></td>
		
	 </tr>
       <%
       
          for (int iCnt =0; iCnt<sumryQryList.size(); iCnt++){
          Map lineArr1 = (Map) sumryQryList.get(iCnt);
        
          int iIndex = iCnt + 1;
        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
        float  PriceForQty   = new Float((String)lineArr1.get("PICKQTY")).floatValue() * new Float((String)lineArr1.get("PRICE")).floatValue();
        System.out.println("PriceForQty ::::::::::::"+PriceForQty);
        kfactor = new Integer((String)lineArr1.get("KFACTOR")).intValue();
       %>

      
       <TR bgcolor = "<%=bgcolor%>">
            <TD align="center" width="43%"> <%= (String)lineArr1.get("PRD_TYPE")%></TD>
             <TD align="center" width="14%"><%= (String)lineArr1.get("PICKQTY")%></TD>
              <TD align="center" width="11%"><%= (String)lineArr1.get("UOM")%></TD>
             <TD align="center" width="12%"><%=(String)lineArr1.get("PRICE")%></TD>
             <TD align="right" width="20%"><%=PriceForQty%></TD>
     
           </TR>
         
       <%
       
         totQty = totQty+new Integer((String)lineArr1.get("PICKQTY")).intValue();
         totPrice = totPrice+ PriceForQty;
       }
       
        %>
     
       <TR >
            <TD align="center" width="43%" align ="Right"><Strong>Total : <Strong></TD>
             <TD align="center" width="14%"><%=totQty%></TD>
              <TD align="center" width="11%">Units</TD>
             <TD align="center" width="12%"></TD>
             <TD align="right" width="20%"><%=totPrice%></TD>
     
           </TR>
    </TABLE>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <STRONG>&nbsp;</STRONG>
    <P>&nbsp;</P>
    
   
    <table cellspacing="0" cellpadding="0" border="1" width="30%" align="center">
    <tr>
      <td>
        <table  cellspacing="0" cellpadding="0" border="0" width="300">
          <tr>
            <td width="47%">
              <DIV align="left">
                <OL>
                  <STRONG>Net Weight &nbsp;</STRONG>
                </OL>
              </DIV>
            </td>
            <td width="24%"><OL><%=m.get("QTY")%></OL></td>
            <td width="30%">
              <DIV align="left">
                <OL>Kgs</OL>
              </DIV>
            </td>
          </tr>
         
          <tr>
            <td width="47%">
              <DIV align="left">
                <OL>
                  <STRONG>Gross Weight</STRONG>
                </OL>
              </DIV>
            </td>
            <td width="24%"><OL><%=m.get("TOTAL_GROSS")%></OL></td>
            <td width="30%">
              <DIV align="left">
                <OL>Kgs</OL>
              </DIV>
            </td>
          </tr>
          <tr>
            <td width="47%">
              <DIV align="left">
                <OL>
                  <STRONG>Total Tray</STRONG>
                </OL>
              </DIV>
            </td>
            <td width="24%"><OL><%=m.get("TOTAL_TRAYS")%></OL></td>
            <td width="30%">
              <DIV align="left">
                <OL>Trays</OL>
              </DIV>
            </td>
          </tr>
          <tr>
            <td width="47%">
              <DIV align="left">
                <OL>
                  <STRONG>Total Pallet</STRONG> 
                </OL>
              </DIV>
            </td>
            <td width="24%"><OL><%=  m.get("TOTAL_PALLET")%></OL> </td>
            <td width="30%">
              <DIV align="left">
                <OL>Box</OL>
              </DIV>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  <font face="Times New Roman">
    <table border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%= fieldDesc%>
    </table>
  </font>
 <!-- <%@ include file="footer.jsp"%>-->
</FORM>
</body>
</html>
