<script language="javascript" src="js/sniffer.js"></script>
<%@ page import="com.murho.util.*"%>
<jsp:useBean id="miscbean"  class="com.murho.gates.miscBean" />
<jsp:useBean id="ubean"  class="com.murho.gates.userBean" />
<%

session = request.getSession();
String expirydate=ubean.getExpiryDate(session.getAttribute("PLANT").toString());
String expirydate2arr[]= expirydate.split("-");
                      String expirydate2= StrUtils.arrayToString(expirydate2arr,"");
String  actualexpirydate = DateUtils.addByMonth(expirydate);
String expirydate1[]= actualexpirydate.split("-");
                      String actexpirydate2= StrUtils.arrayToString(expirydate1,"");
  boolean bflag = miscbean.iSExistStartndEND(expirydate2,actexpirydate2);
String s3 = session.getAttribute("EXPIRYDATE").toString();
      String [] test = null;
      test = s3.split("-");
    StrUtils.reverse(test);
    String dateres=StrUtils.arrayToString( test,  "-");
    String company1=session.getAttribute("PLANT").toString();
    company1 = company1.toUpperCase();
%>
<script>
function onforward()
{
document.homeform.action="body.jsp?action="+hme.value;
document.homeform.submit();
}
</script>
<%
String serverName = request.getServerName();
String contxtName = request.getContextPath();
if(request.getParameter("action")!=null){
  if(request.getParameter("action").equalsIgnoreCase("Home"))
  {
 
  response.sendRedirect("http://"+serverName+":8080/Wms/jsp/indexPage.jsp");
  }}
    %>
<%@ include file="links/sessionMenuLinks.jsp"%>
<script language="javascript1.2" src="js/style.js"></script>
</head>
<body bgcolor="#ffffff"  text="#000000" link="#efe7c6" alink="#9c8349" vlink="#535353" >
<!--<body bgcolor="#006699"  text="#000000" link="#efe7c6" alink="#9c8349" vlink="#535353">-->
<jsp:useBean id="dbean"  class="com.murho.gates.DbBean" />
<!-- background="images/frame2_tile.gif"-->
<font face="Times New Roman">
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
  <tr>
    <td width="70%" align="left" >
    <form name="homeform" action="" >
    <table border="0" cellpadding="0" cellspacing="0" width="100%"><tr><td align="left" width="15%">
	<A href ='#' ><img border="0" src="images/MuRho Logo.jpg" width="80" height="50"></A><A href ="indexPage1.jsp" ><img border="0" src="images/home.jpg" height='47'></A>
	
  <td  valign="bottom" width="15%">
   <INPUT type="hidden" name="action"  value="Home" onClick="return onforward();"> 
</td>
  <td align="center" width="70%"><font class="mainred"><%=dbean.getBroadCastMessage()%></font></td>
  </tr>
   </table></form>
  </td>
  
        <td width="30%" align="right" >
	<table border="0" cellpadding="0" cellspacing="0" width="360" align="right">
        <tr align="right" valign="top">
         <td width="78%" align="right"><font color="#666666" size="2" face="Arial, Helvetica, sans-serif"><b>User&nbsp;ID : </b></font></td>
         <td width="22%" align="left">&nbsp;<font color="#666666" size="2" face="Arial, Helvetica, sans-serif"><b><%=session.getAttribute("LOGIN_USER").toString()%></b></font></td>
       </tr>
       <tr align="right" valign="top">
         <td width="78%" align="right"><font color="#666666" size="2" face="Arial, Helvetica, sans-serif"><b>Company  : </b></font></td>
         <td width="22%" align="left">&nbsp;<font color="#666666" size="2" face="Arial, Helvetica, sans-serif"><b><%=company1%></b></font></td>
       </tr>
<%if(bflag==true) {%>
      <tr align="right" valign="top">
        <td width="78%" align="right"><font  color="Red"><b><font size="2" face="Arial, Helvetica, sans-serif">Expiry&nbsp;Date </font>: </b></font></td>
        <td width="22%" align="left">&nbsp;<font color="Red" size="2" face="Arial, Helvetica, sans-serif"><b><%=dateres%></b></font></td>
      </tr><%}else{%>
        <tr align="right" valign="top">
        <td width="78%" align="right"><font  color="#666666"><b><font size="2" face="Arial, Helvetica, sans-serif">Expiry&nbsp;Date </font>: </b></font></td>
        <td width="22%" align="left">&nbsp;<font color="#666666" size="2" face="Arial, Helvetica, sans-serif"><b><%=dateres%></b></font></td>
      </tr><%}%>
    </table>
   </td>
  </tr>
</table>
</font>
