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
<title>Lot Restriction Movement</title>
</head>
<link rel="stylesheet" href="css/style.css">

<!-- Added for testing -->
<SCRIPT LANGUAGE="JavaScript">
function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}

function onGo(){
   var LOT = document.form1.LOT.value;
   document.form1.action  = "lotRestrictionMovHis.jsp?action=Go";
   document.form1.submit();
}
</SCRIPT>

<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
StrUtils strUtils     = new StrUtils();
MovHisDAO movDAO = new MovHisDAO();
ArrayList invQryList = new ArrayList();

String action     =   "" , LOT = "" , fieldDesc = "";

action  = strUtils.fString(request.getParameter("action"));
LOT    = strUtils.fString(request.getParameter("LOT"));

if(action.equalsIgnoreCase("Go")){	    

    invQryList = movDAO.LotRestriction(LOT);
    try{
    if(invQryList.size()<=0){
    	fieldDesc = "<tr><td><B><h3>Currently No Records Found to List <h3></td></tr>";
    }
}catch(Exception e) {System.out.println("Exception :LotRestriction"+e.toString()); }
	}

%>
<%@include file="body.jsp" %>
<FORM name="form1" method="post" action="MovHisDAO.java">
   <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Lot Restriction Movement</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
	
    <TR>
       <TD> <TH ALIGN="centre" >MOVEMENT :  <input type="text" value="LOT_RESTRICTION"><br>
        </TD>
      
      <TD>  
      <TH ALIGN="left" > &nbsp;&nbsp; LOT No :&nbsp;
      <INPUT name="LOT" type = "TEXT" value="<%=LOT%>" size="20"  MAXLENGTH=20> </TH>
      </TD>
       
       <TD><input type="button" value="Go" onClick="javascript:return onGo();"/></TD>	
    </TR>                   
              
  </TABLE>
  
   <br>
  <TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <th width="6%"><font color="#ffffff" align="center">SNO</th>
         <td width="10%"><font color="#ffffff" align="left"><center>
           <STRONG>Date&Time</STRONG>
         </center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>Movement</STRONG> </center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>Lot No</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>Quantity</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>Remarks</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>ReasonCode</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>User</STRONG></center></td>     		
		
	 </tr>
	<%
	for(int i = 0; i<invQryList.size(); i++){
		Map map = (Map) invQryList.get(i);
		int iCnt =i + 1;	
	%>
          <TR >
             <TD align="center" width="6%"><%=iCnt%></TD>
             <TD align="center" width="6%"><%=(String)map.get("Date")%></TD>
             <TD align="center" width="8%"><%=(String)map.get("Movement")%></TD>
             <TD align="center" width="6%"><%=(String)map.get("LotNo")%></TD>
             <TD align="right" width="5%"><%=(String)map.get("Quantity")%></TD>
             <TD align="right" width="7%" ><%=(String)map.get("Remarks")%></TD>
             <TD align="right" width="6%"><%=(String)map.get("ReasonCode")%></TD>
             <TD align="right" width="6%"><%=(String)map.get("CreatedBy")%></TD> 
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
    </P>
   
      
  </FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>

</html>