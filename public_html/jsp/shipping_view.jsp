<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="com.murho.utils.MLogger"%>
<%@ include file="header.jsp"%>

<html>
<head>
<script language="javascript">

function popUpWin(URL) {
 subWin = window.open(URL, 'PackSlip', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onGo(){

	if(form1.ITEM.value=="" || form1.ITEM.value.length==0)
	{
		alert("Select traveler no ");
		form1.ITEM.focus();
		return false;
	}
document.form1.HiddenView.value = "Go";
   
document.form1.submit();
}


function onPacking(){

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
 
document.form1.HiddenView.value = "PackingList";
   
document.form1.submit();
}

function onInvoice(){

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
   
document.form1.HiddenView.value = "Invoice";
   
document.form1.submit();
}
function onApprove(){
document.form1.HiddenAprove.value = "Approve";
var MTID    = document.form1.MTID.value;
 document.form1.submit();
}

function onSubmit(MTID){

var ITEM    = document.form1.ITEM.value;
document.form1.ITEM.value;
document.form1.MTID.value=MTID;
document.form1.action = "ReceivingAprroval.jsp";
//alert(MTID);
document.form1.submit();
}
</script>
<title>Shipping</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();

ShipDetDAO shipDetDAO=new ShipDetDAO();
OBTravelerHdrDAO obHdrDAO = new OBTravelerHdrDAO();
ArrayList invQryList  = new ArrayList();
Map m = new HashMap();
Map shipMap = new HashMap();
MLogger logger = new MLogger();
boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
UserTransaction ut=null ;
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("usrHiddenAction")).trim();
String user_id          = session.getAttribute("LOGIN_USER").toString().toUpperCase();
String  REFNO= "", ITEM = "", ASN ="", INVOICE ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="";
String html = "",  shipTo  ="";



String HiddenView="",HiddenAprove="";
ITEM    = strUtils.fString(request.getParameter("ITEM"));
INVOICE    = strUtils.fString(request.getParameter("INVOICE"));
ASN    = strUtils.fString(request.getParameter("ASN"));
shipTo = strUtils.fString(request.getParameter("SHIP_TO"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove    = strUtils.fString(request.getParameter("HiddenAprove"));

if(HiddenView.equalsIgnoreCase("Go")){
 try{
    
       invQryList = shipDetDAO.getShipDetails(PLANT,ITEM);
       
      if(invQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no Shipping details found for Traveller to display<h3></td></tr>";
   
      }else{
      m = shipDetDAO.getSumOfGrossWeight(PLANT,ITEM);
      shipMap = obHdrDAO.getShipTo(ITEM);
       shipTo =(String)shipMap.get("SHIPTO");

      }
 }catch(Exception e) {System.out.println("Exception :getShipDetails"+e.toString()); }
}

if(HiddenView.equalsIgnoreCase("PackingList")){
 try{
    response.sendRedirect("packing_list.jsp?HiddenView=View&TRAVELER="+ITEM+"&INVOICE="+INVOICE+"&ASN="+ASN+"&SHIP_TO="+shipTo+"");
 }catch(Exception e) {System.out.println("Exception :getShipDetails"+e.toString()); }
}

if(HiddenView.equalsIgnoreCase("Invoice")){
 try{
    response.sendRedirect("invoice_list.jsp?HiddenView=View&TRAVELER="+ITEM+"&INVOICE="+INVOICE+"&ASN="+ASN+"&SHIP_TO="+shipTo+"");
 }catch(Exception e) {System.out.println("Exception :getShipDetails"+e.toString()); }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="shipping_view.jsp" >
  <!--<INPUT type="hidden" name="MTID" value="<%=MTID%>"> -->
  <INPUT type="hidden" name="HiddenView" value="Go">
  <INPUT type="hidden" name="SHIP_TO" value=<%=shipTo%>>
  <INPUT type="hidden" name="HiddenAprove" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Global Shipping</FONT>
      </TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="90%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

       <TR>
            <TH ALIGN="RIGHT" width="17%">
             Reference No : </TH>
		   <TH  width="28%" align="left">
		     <INPUT name="REFNO" type="TEXT" value="<%=REFNO%>" size="20" MAXLENGTH="20"/>
		     <a href="#" onClick="javascript:popUpWin('listView/OB_Travel_Ref.jsp?REFNO='+form1.REFNO.value);">
		       <img src="images/populate.gif" border="0"/>
		     </a>
		   </TH>
		   <TH ALIGN="RIGHT" width="18%">Delivery No.:</TH>
                   
       <TD width="37%"><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=20>
       <a href="#" onClick="javascript:popUpWin('listView/OB_Traveler.jsp?ITEM='+form1.ITEM.value+'&REFNO=' +form1.REFNO.value);"><img src="images/populate.gif" border="0"></a>
               
        <input type="button" value="View" onClick="javascript:return onGo();"/></TD>
		
                     
		</TR>

  </TABLE>
  <br>
  <TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center" >
      
	    
	    <tr bgcolor="navy">
       <th width="12%"><font color="#ffffff" align="center">DELIVERY NO.</th>
        
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PALLET</STRONG> </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>NO OF TRAYS</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>LXBXH</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>GROSS WEIGHT</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PackingList Generated</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>InvoiceList Generated</STRONG></center></td>
  </tr>
       <%
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
            String lbh  = (String)lineArr.get("LENGTH") + "X"+(String)lineArr.get("WIDTH")+ "X"+ (String)lineArr.get("HEIGHT");
       //   MTID=(String)lineArr.get("LNNO");
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>

       
       <TR bgcolor = "<%=bgcolor%>">
             <TD align="center" width="13%"><%=(String)lineArr.get("TRAVELER_ID")%></TD>
           <!--   <TD align="center" width="13%"><a href = "ship_pallet_details.jsp?action=view&TRAVELER=<%=(String)lineArr.get("TRAVELER_ID")%>&PALLET=<%=(String)lineArr.get("PALLET")%>")%><%=(String)lineArr.get("PALLET")%></a></TD> -->
             <TD align="center" width="13%" ><%=(String)lineArr.get("PALLET")%></TD>
             <TD align="center" width="13%" ><%=(String)lineArr.get("NO_OF_TRAYS")%></TD>
             <TD align="right" width="13%"><%=lbh%></TD>
             <TD align="right" width="13%"><%=(String)lineArr.get("GROSS_WEIGHT")%></TD>
             <TD align="right" width="13%"><%=(String)lineArr.get("REMARKS1")%></TD>
             <TD align="right" width="13%"><%=(String)lineArr.get("REMARKS2")%></TD>
           
       
           </TR>
       <%}%>
       <% 
       if(m.size()>0){%>
        <TR>
              <TD align="center" width="13%" ></TD>
              <TD align="center" width="13%" bgcolor = "#dddddd"> Total Trays </TD>
              <TD align="center" width="13%"><%=(String)m.get("TOTAL_TRAYS")%></TD>
              <TD align="center" width="13%" bgcolor = "#dddddd">Gross Weight</TD>
              <TD align="right" width="13%"><%=(String)m.get("TOTAL_GROSS")%></TD>
              <TD align="right" width="13%"></TD>
              <TD align="right" width="13%"></TD>
           
       
           </TR>
   <%  }  %>
       
    </TABLE>
    <P>&nbsp;
    </P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <TR>  <input type="button" value="Back" onClick="window.location.href='indexPage.jsp'"> 

    </P>
      
  </FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>