<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="com.murho.dao.*"%>
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
function onGo(){

	if(form1.TRAVELER.value=="" || form1.TRAVELER.value.length==0)
	{
		alert("Select Delivery No ");
		form1.TRAVELER.focus();
		return false;
	}
document.form1.HiddenView.value = "Go";
   
document.form1.submit();
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
  
document.form1.HiddenAprove.value ="GenerateExcel";
   
document.form1.submit();
}


</script>
    <title>Packing List</title>
  </head>
  <link rel="stylesheet" href="css/style.css"/>
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
boolean isupdated = false;

UserTransaction ut=null ;
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("usrHiddenAction")).trim();
String user_id          = session.getAttribute("LOGIN_USER").toString().toUpperCase();
String INVOICE ="",  TRAVELER = "", HAGENT ="", ASN ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="";
String html = "",SHIP_TO ="";int totQty =0;int totalMulK =0;int kfactor =0;
float totPrice =0;int totQty1 =0;int totalMulK1 =0;
float totPrice1 =0;
Date dt = new Date();
Calendar calendar = new GregorianCalendar();
calendar.setTime(dt);
String date =  new SimpleDateFormat("mm/dd/yyyy").format(dt);
String HiddenView="",HiddenAprove="";
TRAVELER    = strUtils.fString(request.getParameter("TRAVELER"));
INVOICE    = strUtils.fString(request.getParameter("INVOICE"));
HAGENT    = strUtils.fString(request.getParameter("HAGENT"));
ASN    = strUtils.fString(request.getParameter("ASN"));
SHIP_TO    = strUtils.fString(request.getParameter("SHIP_TO"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove    = strUtils.fString(request.getParameter("HiddenAprove"));

if(HiddenView.equalsIgnoreCase("View")){
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
      
      isupdated=shipDetDAO.UpdateGlobalInvoiceList(PLANT,TRAVELER);
      
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}

if(HiddenAprove.equalsIgnoreCase("GenerateExcel")){
System.out.println("HiddenAprove>>>>>>>>>>>>>>>>>>>>>>>>>>>>."+HiddenView);
 try{
    response.sendRedirect("invoice_list_excel.jsp?HiddenView=GenerateExcel&TRAVELER="+TRAVELER+"&INVOICE="+INVOICE+"&ASN="+ASN+"&SHIP_TO="+SHIP_TO+"");
 }catch(Exception e) {System.out.println("Exception :getShipDetails"+e.toString()); }
}
%>
  <%@ include file="body.jsp"%>
  <FORM name="form1" method="post">
    <INPUT type="hidden" name="HiddenView" value="Go"/>
    <INPUT type="hidden" name="TRAVELER" value="<%=TRAVELER%>"/>
    <INPUT type="hidden" name="INVOICE" value="<%=INVOICE%>"/>
    <INPUT type="hidden" name="ASN" value="<%=ASN%>/"/>
    <INPUT type="hidden" name="HAGENT" value="<%=HAGENT%>/"/>
    <INPUT type="hidden" name="HiddenAprove" value=""/>
    <br/>
    <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11">
          <FONT color="#ffffff">Invoice</FONT> 
        </TH>
      </TR>
    </TABLE>
    <br/>
    <!--  &lt;TABLE border=&quot;0&quot; width=&quot;90%&quot; cellspacing=&quot;0&quot; cellpadding=&quot;0&quot; align=&quot;center&quot; bgcolor=&quot;#dddddd&quot;&gt;
    &lt;TR&gt;
      &lt;TH ALIGN=&quot;RIGHT&quot;&gt;&amp;nbsp;&lt;/TH&gt;
      &lt;TD&gt;&amp;nbsp;&lt;/TD&gt;
      &lt;TH ALIGN=&quot;RIGHT&quot;&gt;&amp;nbsp;&amp;nbsp; Traveller No :&lt;/TH&gt;
      &lt;TD&gt;
        &lt;INPUT name=&quot;TRAVELER&quot; type=&quot;TEXT&quot; value=&quot;&lt;%=TRAVELER%&gt;&quot; size=&quot;20&quot; MAXLENGTH=&quot;20&quot;/&gt;
      &lt;/TD&gt;
    &lt;/TR&gt;
  &lt;/TABLE&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp; -->
    <table cellspacing="0" cellpadding="0" border="0" width="30%" align="Center">
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
          <DIV align="center">
            <%= INVOICE%>
          </DIV>
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
          <DIV align="center">
            <%= date%>
          </DIV>
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
          <DIV align="center">
            <%= ASN%>
          </DIV>
        </td>
      </tr>
    </table>
    <P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</P>
    <table cellspacing="0" cellpadding="0" border="0" width="90%" align="center">
      <tr>
        <td width="22%">
          <P>
            <b>CONSIGNEE:</b> 
          </P>
        </td>
        <td width="22%">
          <b><b>HANDLING AGENT:</b></b>
        </td>
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
        <td width="22%">
          <%= (String)mCon.get("COMPANY_NAME")%>
        </td>
        <td width="29%">
          <%= (String)mHand.get("COMPANY_NAME")%>
        </td>
        <td width="27%">
          <%= (String)mMfg.get("COMPANY_NAME")%>
        </td>
        <td width="22%">
          <%= (String)mBill.get("COMPANY_NAME")%>
        </td>
      </tr>
      <tr>
        <td width="22%">
          <%= (String)mBill.get("ADDR1")%>
        </td>
        <td width="22%">
          <%= (String)mCon.get("ADDR1")%>
        </td>
        <td width="29%">
          <%= (String)mHand.get("ADDR1")%>
        </td>
        <td width="27%">
          <%= (String)mMfg.get("ADDR1")%>
        </td>
      </tr>
      <tr>
        <td width="22%">
          <%= (String)mCon.get("ADDR2")%>
        </td>
        <td width="29%">
          <%= (String)mHand.get("ADDR2")%>
        </td>
        <td width="27%">
          <%= (String)mMfg.get("ADDR2")%>
        </td>
        <td width="22%">
          <%= (String)mBill.get("ADDR2")%>
        </td>
      </tr>
      <tr>
        <td width="22%">
          <%= (String)mCon.get("COUNTRY")%>
        </td>
        <td width="29%">
          <%= (String)mHand.get("COUNTRY")%>
        </td>
        <td width="27%">
          <%= (String)mMfg.get("COUNTRY")%>
        </td>
        <td width="22%">
          <%= (String)mBill.get("COUNTRY")%>
        </td>
      </tr>
      <tr>
        <td width="22%">
          <%= (String)mCon.get("PHONE")%>
        </td>
        <td width="29%">
          <%= (String)mHand.get("PHONE")%>
        </td>
        <td width="27%">
          <%= (String)mMfg.get("PHONE")%>
        </td>
        <td width="22%">
          <%= (String)mBill.get("PHONE")%>
        </td>
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
        <TD align="center" width="15%"/>
        <TD align="center" width="13%">
          <%= totalMulK%>
        </TD>
        <TD align="center" width="13%">EA</TD>
        <TD align="center" width="23%">
          <%= totPrice1%>
        </TD>
      </TR>
      <TR>
        <TD align="center" width="38%"/>
        <TD align="center" width="15%">
          <Strong>Total Unit Singles :<Strong/></Strong>
        </TD>
        <TD align="center" width="13%">
          <%= totalMulK%>
        </TD>
        <TD align="center" width="13%">EA</TD>
        <TD align="center" width="23%"/>
      </TR>
    </TABLE>
    <P>&nbsp;</P>
    <P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</P>
    <P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
      <STRONG>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Detail Invoice:</STRONG>
    </P>
    <TABLE WIDTH="90%" border="0" cellspacing="1" cellpadding="2" align="center">
      <tr bgcolor="navy">
        <th width="43%">
          <FONT color="#ffffff">Product Type</FONT>&nbsp;
        </th>
        <td width="14%">
          <font color="#ffffff" align="left">
            <center>
              <STRONG>Quantity</STRONG> 
            </center>
          </font>
        </td>
        <td width="11%">
          <font color="#ffffff" align="left">
            <center>
              <STRONG>Uom</STRONG> 
            </center>
          </font>
        </td>
        <td width="12%">
          <font color="#ffffff" align="left">
            <center>
              <STRONG>Price</STRONG>
            </center>
          </font>
        </td>
        <td width="20%">
          <font color="#ffffff" align="left">
            <center>
              <STRONG><STRONG>Amount(USD)</STRONG> </STRONG>
            </center>
          </font>
        </td>
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
      <TR bgcolor="<%=bgcolor%>">
        <TD align="center" width="43%">
          <%=  (String)lineArr1.get("PRD_TYPE")%>
        </TD>
        <TD align="center" width="14%">
          <%=  (String)lineArr1.get("PICKQTY")%>
        </TD>
        <TD align="center" width="11%">
          <%=  (String)lineArr1.get("UOM")%>
        </TD>
        <TD align="center" width="12%">
          <%= (String)lineArr1.get("PRICE")%>
        </TD>
        <TD align="right" width="20%">
          <%= PriceForQty%>
        </TD>
      </TR>
      <% 
       
         totQty = totQty+new Integer((String)lineArr1.get("PICKQTY")).intValue();
         totPrice = totPrice+ PriceForQty;
       }
       
        %>
      <TR>
        <TD align="center" width="43%">
          <Strong>Total :<Strong/></Strong>
        </TD>
        <TD align="center" width="14%">
          <%= totQty%>
        </TD>
        <TD align="center" width="11%">Units</TD>
        <TD align="center" width="12%"/>
        <TD align="right" width="20%">
          <%= totPrice%>
        </TD>
      </TR>
    </TABLE>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
    <STRONG>&nbsp;</STRONG>
    <P>&nbsp;</P>
    <table cellspacing="0" cellpadding="0" border="1" width="30%" align="center">
      <tr>
        <td>
          <table cellspacing="0" cellpadding="0" border="0" width="300">
            <tr>
              <td width="47%">
                <DIV align="left">
                  <OL>
                    <STRONG>Net Weight &nbsp;</STRONG> 
                  </OL>
                </DIV>
              </td>
              <td width="24%">
                <OL>
                  <%= m.get("QTY")%>
                </OL>
              </td>
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
              <td width="24%">
                <OL>
                  <%= m.get("TOTAL_GROSS")%>
                </OL>
              </td>
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
              <td width="24%">
                <OL>
                  <%= m.get("TOTAL_TRAYS")%>
                </OL>
              </td>
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
              <td width="24%">
                <OL>
                  <%=   m.get("TOTAL_PALLET")%>
                </OL>
              </td>
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
    <P>&nbsp;</P>
    <P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</P>
    <TR>
      <input type="button" value="Cancel" onClick="window.location.href='indexPage.jsp'"/>&amp;nbsp
      <TD>
        <input type="button" value="Generate Excel" onClick="javascript:return onGenerate();"/>
      </TD>
    </TR>
  </FORM>
  <font face="Times New Roman">
    <table border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%= fieldDesc%>
    </table>
  </font>
  <%@ include file="footer.jsp"%>
</html>
