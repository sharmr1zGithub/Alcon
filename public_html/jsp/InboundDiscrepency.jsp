<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Inbound Discrepency Report</title>
</head>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">
function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}

function onGo(){
	if(form1.ITEM.value=="" || form1.ITEM.value.length==0)
	{
		alert("Select Delivery No. ");
		form1.ITEM.focus();
		return false;
	}
document.form1.action = "InboundDiscrepency.jsp?action=Go";   
document.form1.submit();
}

function onGo1(){

	if(form1.ITEM.value=="" || form1.ITEM.value.length==0)
	{
		alert("Select Delivery No. ");
		form1.ITEM.focus();
		return false;
	}
//  alert("onGo1");
document.form1.value = "Go";
document.form1.action='/CibaVisionWms/ExcelGeneratorServlet?action=GenerateInboundExcel';   
document.form1.submit();
}
</SCRIPT>

<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
StrUtils strUtils     = new StrUtils();
RecvDetDAO recvDet = new RecvDetDAO();
ArrayList invQryList = new ArrayList();

String action     =   "" , ITEM = "" , LOT = "" , SKU = "" ,fieldDesc = "";

action  = strUtils.fString(request.getParameter("action"));

ITEM = strUtils.fString(request.getParameter("ITEM"));
LOT    = strUtils.fString(request.getParameter("LOT"));
SKU = strUtils.fString(request.getParameter("SKU"));

if(action.equalsIgnoreCase("Go")){
	    
	Hashtable ht = new Hashtable();
	ht.put("ITEM",ITEM);	
	ht.put("LOT",LOT);
	ht.put("SKU",SKU);
	
    invQryList = recvDet.TrayDescrepencyReport(ht);
    try{
    if(invQryList.size()<=0){
    	fieldDesc = "<tr><td><B><h3>Currently no discrepency records found to list <h3></td></tr>";
    }
}catch(Exception e) {System.out.println("Exception :TrayDescrepencyReport"+e.toString()); }
	}

%>
<%@include file="body.jsp" %>
<FORM name="form1" method="post" action="InboundDiscrepency.jsp" >
   <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Inbound Discrepancy Report</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="90%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

          <TR>
		   <TH ALIGN="RIGHT" >&nbsp;</TH>
                      <TH ALIGN="RIGHT" > &nbsp;&nbsp; Delivery No :&nbsp;</TH>
                      <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=20>
                    
                    <!--    
                     <TH ALIGN="RIGHT" > &nbsp;&nbsp; LOT No :&nbsp;</TH>
                     <TD><INPUT name="LOT" type = "TEXT" value="<%=LOT%>" size="20"  MAXLENGTH=20>
                      
                     <TH ALIGN="RIGHT" > &nbsp;&nbsp; SKU :&nbsp;</TH>
                     <TD><INPUT name="SKU" type = "TEXT" value="<%=SKU%>" size="20"  MAXLENGTH=20>
                     -->
                    
                      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <input type="button" value="Go" onClick="javascript:return onGo();"/></TD>	
                     
		</TR>

  </TABLE>
  
   <br>
  <TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <th width="6%"><font color="#ffffff" align="center">SNO</th>
         <td width="10%"><font color="#ffffff" align="left"><center>
           <STRONG>DELIVERY NO.</STRONG>
         </center></td>
         <td width="8%"><font color="#ffffff" align="left"><center><STRONG>SKU</STRONG> </center></td>
         <td width="8%"><font color="#ffffff" align="left"><center><STRONG>LOT</STRONG></center></td>
         <td width="9%"><font color="#ffffff" align="left"><center><STRONG>LOC</STRONG></center></td>
         <td width="7%"><font color="#ffffff" align="left"><center><STRONG>ORDER QTY</STRONG></center></td>
         <td width="7%"><font color="#ffffff" align="left"><center><STRONG>RECV QTY</STRONG></center></td>
         <td width="7%"><font color="#ffffff" align="left"><center><STRONG>PUTAWAY QTY</STRONG></center></td>
         <td width="7%"><font color="#ffffff" align="left"><center><STRONG>RECV DIFF</STRONG></center></td>
         <td width="7%"><font color="#ffffff" align="left"><center><STRONG>PUTAWAY DIFF</STRONG></center></td>
         <td width="9%"><font color="#ffffff" align="left"><center><STRONG>BLOCK STATUS</STRONG></center></td>
         <td width="20%"><font color="#ffffff" align="left"><center><STRONG>BLOCK REASON</STRONG></center></td>
      		
		
	 </tr>
<%
for(int i = 0; i<invQryList.size(); i++){
	Map map = (Map) invQryList.get(i);

%>
          <TR >
             <TD align="center" width="6%"><%=(String)map.get("traveler")%></TD>
             <TD align="center" width="10%"><%=(String)map.get("traveler")%></TD>
             <TD align="center" width="8%"><%=(String)map.get("sku")%></TD>
             <TD align="center" width="8%"><%=(String)map.get("lot")%></TD>
             <TD align="right" width="9%"><%=(String)map.get("LOC")%></TD>
             <TD align="right" width="7%"><%=(String)map.get("orderqty")%></TD>
             <TD align="right" width="7%"><%=(String)map.get("receiveqty")%></TD>
             <TD align="right" width="7%"><%=(String)map.get("putawayqty")%></TD>
             <TD align="right" width="7%"><%=(String)map.get("recevdiff")%></TD>
             <TD align="right" width="7%"><%=(String)map.get("putawaydiff")%></TD>
             <TD align="right" width="9%"><%=(String)map.get("BlockStatus")%></TD>
             <TD align="right" width="20%"><%=(String)map.get("BlockReason")%></TD>       
           </TR>
  <% } %>
    </TABLE>
   <P>&nbsp;
    </P>
      <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <TR>  <input type="button" value="Back" onClick="window.location.href='indexPage.jsp'">&nbsp;
    <input type="button" value="GenerateExcel" onClick="javascript:return onGo1();"/>
    </P>
   
      
  </FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>

</html>