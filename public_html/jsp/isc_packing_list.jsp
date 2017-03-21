<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="com.murho.utils.MLogger"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.Date"%>
<%@ include file="header.jsp"%>

<html>
<head>
<script language="javascript">

function popUpWin(URL) {
 subWin = window.open(URL, 'PackSlip', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}


function onGenerate(){

	if(form1.INVOICE.value=="" || form1.INVOICE.value.length==0)
	{
		alert("Select Invoice no ");
		form1.INVOICE.focus();
		return false;
	}
  if(form1.ASN.value=="" || form1.ASN.value.length==0)
	{
		alert("Select Asn no ");
		form1.ASN.focus();
		return false;
	}
   if(form1.HAGENT.value=="" || form1.HAGENT.value.length==0)
	{
		alert("Select Handling Agent no ");
		form1.HAGENT.focus();
		return false;
	}
document.form1.HiddenAprove.value ="GenerateExcel";
   
document.form1.submit();
}


</script>
<title>Isc Packing List</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();

ShipDetDAO shipDetDAO=new ShipDetDAO();
CustMstDAO custDAO=new CustMstDAO();
ArrayList pckQryList  = new ArrayList();
ArrayList sumryQryList  = new ArrayList();
Map m = new HashMap();
Map mCon = new HashMap();
Map mShip = new HashMap();
Map mMfg = new HashMap();
Map mHand = new HashMap();
MLogger logger = new MLogger();
boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
boolean isupdated = false;

UserTransaction ut=null ;
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("usrHiddenAction")).trim();
String user_id          = session.getAttribute("LOGIN_USER").toString().toUpperCase();
String INVOICE ="",   HAGENT ="", ASN ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="";
String html = "",SHIP_TO="",strSub ="";
int totQty =0;int totalMulK =0;int kfactor =0;
float totPrice =0;int totQty1 =0;int totalMulK1 =0;
float totPrice1 =0;

Date dt = new Date();
Calendar calendar = new GregorianCalendar();
calendar.setTime(dt);
String date =  new SimpleDateFormat("mm/dd/yyyy").format(dt);

String HiddenView="",HiddenAprove="";
//TRAVELER    = strUtils.fString(request.getParameter("TRAVELER"));
INVOICE    = strUtils.fString(request.getParameter("INVOICE"));
HAGENT    = strUtils.fString(request.getParameter("HAGENT"));
ASN    = strUtils.fString(request.getParameter("ASN"));
SHIP_TO    = strUtils.fString(request.getParameter("SHIP_TO"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove    = strUtils.fString(request.getParameter("HiddenAprove"));

if(HiddenView.equalsIgnoreCase("View")){
 try{
    
       pckQryList = shipDetDAO.getIscPackingListForInvoice(PLANT,INVOICE);
      if(pckQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no Invoice summary found to display<h3></td></tr>";
     //  fieldDesc=fieldDesc+ITEM;
      } 
      sumryQryList  =shipDetDAO.getIscSummaryPackingListForInvoice(PLANT,INVOICE);
    
      if(sumryQryList.size() < 1)
      {
        fieldDesc = "<tr><td><B><h3>Currently no Invoice summary found to display<h3></td></tr>";
      }else{
      m= shipDetDAO.getIscQuantityDetails(PLANT,INVOICE);
      mCon=custDAO.getAdressDetails(SHIP_TO,"CONSIGNEE");
      mShip=custDAO.getAdressDetails(SHIP_TO,"SHIP_TO");
      mMfg=custDAO.getAdressDetails(SHIP_TO,"MFG_SHIPPER");
      mHand=custDAO.getAdressDetails(SHIP_TO,"HANDLING_AGENT");
      
      isupdated=shipDetDAO.UpdatePackingList(PLANT,INVOICE);
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}

if(HiddenAprove.equalsIgnoreCase("GenerateExcel")){
System.out.println("HiddenAprove>>>>>>>>>>>>>>>>>>>>>>>>>>>>."+HiddenView);
 try{
    response.sendRedirect("isc_packing_excel.jsp?HiddenView=GenerateExcel&INVOICE="+INVOICE+"&ASN="+ASN+"&SHIP_TO="+SHIP_TO+"");
 }catch(Exception e) {System.out.println("Exception :Isc_packing_list"+e.toString()); }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <INPUT type="hidden" name="HiddenView" value="Go"/>
  <INPUT type="hidden" name="INVOICE" value=<%=INVOICE%> />
  <INPUT type="hidden" name="ASN" value=<%=ASN%>/>
  <INPUT type="hidden" name="HAGENT" value=<%=HAGENT%>/>
  <INPUT type="hidden" name="HiddenAprove" value=""/>
  <br/>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Isc Packing list</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br/>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
          <b>SHIP TO:</b>
        </P>
      </td>
      <td width="22%"><b>CONSIGNEE:</b></td>
      <td width="29%"><b>HANDLING AGENT:</b></td>
      <td width="27%">
        <P>
          <b>MFG/ 
          <b>SHIPPER:</b>
        </P>
      </td>
    </tr>
    <tr>
      <td width="22%"><%=(String)mShip.get("COMPANY_NAME")%></td>
      <td width="22%"><%=(String)mCon.get("COMPANY_NAME")%></td>
      <td width="29%"><%=(String)mHand.get("COMPANY_NAME")%></td>
      <td width="27%"><%=(String)mMfg.get("COMPANY_NAME")%></td>
    </tr>
    <tr>
      <td width="22%"><%=(String)mShip.get("ADDR1")%></td>
      <td width="22%"><%=(String)mCon.get("ADDR1")%></td>
      <td width="29%"><%=(String)mHand.get("ADDR1")%></td>
      <td width="27%"><%=(String)mMfg.get("ADDR1")%></td>
    </tr>
    <tr>
      <td width="22%"><%=(String)mShip.get("ADDR2")%></td>
      <td width="22%"><%=(String)mCon.get("ADDR2")%></td>
      <td width="29%"><%=(String)mHand.get("ADDR2")%></td>
      <td width="27%"><%=(String)mMfg.get("ADDR2")%></td>
    </tr>
    <tr>
      <td width="22%"><%=(String)mShip.get("COUNTRY")%></td>
      <td width="22%"><%=(String)mCon.get("COUNTRY")%></td>
      <td width="29%"><%=(String)mHand.get("COUNTRY")%></td>
      <td width="27%"><%=(String)mMfg.get("COUNTRY")%></td>
    </tr>
    <tr>
      <td width="22%"><%=(String)mShip.get("PHONE")%></td>
      <td width="22%"><%=(String)mCon.get("PHONE")%></td>
      <td width="29%"><%=(String)mHand.get("PHONE")%></td>
      <td width="27%"><%=(String)mMfg.get("PHONE")%></td>
    </tr>
  </table>
  <P>&nbsp;</P>
  <TABLE WIDTH="90%" border="0" cellspacing="1" cellpadding="2" align="center">
    <tr bgcolor="navy">
      <th width="12%">
        <FONT color="#ffffff">Part#</FONT>&nbsp;
      </th>
      <td width="27%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>Description</STRONG> 
          </center>
        </font>
      </td>
      <td width="9%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>PO#</STRONG> 
          </center>
        </font>
      </td>
      <td width="10%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>Lot#</STRONG>
          </center>
        </font>
      </td>
      <td width="14%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>Qty</STRONG> 
          </center>
        </font>
      </td>
      <td width="14%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>UOM</STRONG>
          </center>
        </font>
      </td>
      <td width="14%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>Expiration Date</STRONG>          </center>
        </font>
      </td>
    </tr>
    <% 
          for (int iCnt =0; iCnt<pckQryList.size(); iCnt++){
          Map lineArr = (Map) pckQryList.get(iCnt);
        
          int iIndex = iCnt + 1;
        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
        strSub = (String)lineArr.get("UOM");
        if(!strSub.equalsIgnoreCase("EA")){
        String s=  strSub.substring(0,1);
        kfactor = new Integer(s).intValue();
        }else{
        kfactor =1;
        }
       %>
    <TR bgcolor="<%=bgcolor%>">
      <TD align="center" width="12%">
        <%=(String)lineArr.get("SKU")%>
      </TD>
      <TD align="center" width="27%">
        <%=(String)lineArr.get("SKU_DESC")%>
      </TD>
      <TD align="center" width="9%">
        <%=(String)lineArr.get("CUSTPO")%>
      </TD>
      <TD align="center" width="10%">
        <%=(String)lineArr.get("LOT")%>
      </TD>
      <TD align="right" width="14%">
        <%=(String)lineArr.get("QTY")%>
      </TD>
      <TD align="right" width="14%">
        <%=(String)lineArr.get("UOM")%>
      </TD>
      <TD align="right" width="14%">
       <%=(String)lineArr.get("EXPDATE")%>
      </TD>
    </TR>
    <% 
    totQty1 = totQty1 + new Integer((String)lineArr.get("QTY")).intValue();
    }
    totalMulK1 = totQty1 * kfactor;
    %>
     <TR >
      <TD align="center" width="12%"></TD>
      <TD align="center" width="27%"><Strong>Total Units :</Strong></TD>
      <TD align="center" width="9%"></TD>
      <TD align="center" width="10%"></TD>
      <TD align="right" width="14%"><%=totQty1%></TD>
      <TD align="right" width="14%">Units</TD>
      <TD align="right" width="14%"></TD>
    </TR>
       <TR >
      <TD align="center" width="12%"></TD>
      <TD align="center" width="27%"><Strong>Total Unit Singles :</Strong></TD>
      <TD align="center" width="9%"></TD>
      <TD align="center" width="10%"></TD>
      <TD align="right" width="14%"><%=totalMulK1%></TD>
      <TD align="right" width="14%">Units</TD>
      <TD align="right" width="14%"></TD>
    </TR>
  </TABLE>
  <P>&nbsp;</P>
  <P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Remarks &nbsp;:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Summary&nbsp;of&nbsp;Packing&nbsp;List&nbsp;:</P>
  <table align="center" cellspacing="0" cellpadding="0" border="0" width="50%">
  
    <tr bgcolor="navy">
      <td width="62%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>Product Type</STRONG>
          </center>
        </font>
      </td>
      <td width="23%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>Quantity</STRONG>
          </center>
        </font>
      </td>
      <td width="15%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>UOM</STRONG>          </center>
        </font>
      </td>
    </tr>
    <% 
          for (int iCnt =0; iCnt<sumryQryList.size(); iCnt++){
          Map lineArr1 = (Map) sumryQryList.get(iCnt);
        
          int iIndex = iCnt + 1;
        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
    <TR bgcolor="<%=bgcolor%>">
      <TD align="left" width="62%">
        <%= (String)lineArr1.get("PRD_TYPE")%>
      </TD>
      <TD align="center" width="23%">
        <%= (String)lineArr1.get("PICKQTY")%>
      </TD>
      <TD align="center" width="15%">
        <%= (String)lineArr1.get("UOM")%>
      </TD>
    </TR>
    <% 
      totQty = totQty+new Integer((String)lineArr1.get("PICKQTY")).intValue();
    }%>
    <TR >
            <TD align="center" width="43%" align ="Right"><Strong>Total Quantity : <Strong></TD>
             <TD align="center" width="14%"><%=totQty%></TD>
              <TD align="center" width="11%">Units</TD>
           
           </TR>
  </table>
  <P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</P>
  <P>&nbsp;</P>
  <P>&nbsp;</P>
  <table cellspacing="0" cellpadding="0" border="1" width="30%" align="center">
    <tr>
      <td>
        <table  cellspacing="0" cellpadding="0" border="0" width="300">
          <tr>
            <td width="47%"><OL><STRONG>Net Weight &nbsp;</STRONG></OL></td>
            <td width="24%"><OL><%=m.get("QTY")%></OL></td>
            <td width="30%"><OL>Kgs</OL></td>
          </tr>
         
          <tr>
            <td width="47%"><OL><STRONG>Gross Weight</STRONG></OL></td>
            <td width="24%"><OL><%=  m.get("TOTAL_GROSS")%></OL></td>
            <td width="30%"><OL>Kgs</OL></td>
          </tr>
          <tr>
            <td width="47%"><OL><STRONG>Total Tray</STRONG></OL></td>
            <td width="24%"><OL><%=m.get("TOTAL_TRAYS")%></OL></td>
            <td width="30%"><OL>Trays</OL></td>
          </tr>
          <tr>
            <td width="47%"><OL><STRONG>Total Pallet</STRONG> </OL></td>
            <td width="24%"><OL><%=  m.get("TOTAL_PALLET")%></OL> </td>
            <td width="30%"><OL>Box</OL></td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  <P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</P>
  <P>&nbsp;</P>
  <P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</P>
  <TR>
    <input type="button" value="Cancel" onClick="window.location.href='indexPage.jsp'"/>
      <input type="button" value="Generate Excel" onClick="javascript:return onGenerate();"/></TD>
  </TR>
  <font face="Times New Roman">
    <table border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%= fieldDesc%>
    </table>
  </font>
  <%@ include file="footer.jsp"%>
</FORM>
