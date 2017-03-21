<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<head>
<script language="javascript">
function onGo(){
alert("Process will take few minutes  to download")
   // var flag    = "false";

return true;
}
</script>
<title>Download Data</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="/SeikoWms/downloaddataservlet?">
  <TABLE border="0" width="100%" cellspacing="20" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">DATA DOWNLOAD</font></TH>
    </TR>
  </TABLE>
  <br>
     
 <table BGCOLOR="#dddddd"  cellspacing="50" cellpadding="3" border="0" width="" align="CENTER">
<tr>
     <TD>
        <input maxlength="100"  type="submit" value="JOB-DOWNLOAD" name="action" onClick="javascript:return onGo();"/>
     </TD>
     
    <TD>
        <input maxlength="100"  type="submit" value="PO-DOWNLOAD" name="action"  onClick="javascript:return onGo();"/>
     </TD>
     
      <TD >
        <input maxlength="100"  type="submit" value="DO-DOWNLOAD" name="action" onClick="javascript:return onGo();"/>
     </TD
</tr>

<tr>
    <TD>
        <input maxlength="100"   type="submit" value="BIN-DOWNLOAD" name="action" onClick="javascript:return onGo();"/>
     </TD>
   <TD>
        <input maxlength="100"  type="submit" value="WHS-DOWNLOAD" name="action" onClick="javascript:return onGo();"/>
     </TD>
     
     <TD>
        <input maxlength="100"    type="submit" value="RSN-DOWNLOAD" name="action" onClick="javascript:return onGo();"/>
     </TD>   
     
     
     
</tr>

<tr>
   <TD>
        <input  maxlength="100" type="submit" value="USER-DOWNLOAD" name="action" onClick="javascript:return onGo();"/>
     </TD>
   <TD>
        <input maxlength="100"   type="submit" value="ITEM-DOWNLOAD" name="action" onClick="javascript:return onGo();"/>
     </TD>
     
     <TD>
        <input maxlength="100"  type="submit" value="RSG-DOWNLOAD" name="action" onClick="window.location.href='indexPage.jsp'" />
     </TD>
</tr>

<tr>
   <TD>
        <input  maxlength="100" type="submit" value="INV-DOWNLOAD" name="action" onClick="javascript:return onGo();"/>
     </TD>
   <TD>
        <input maxlength="100"   type="submit" value="VEND-DOWNLOAD" name="action" onClick="javascript:return onGo();"/>
     </TD>
     
     <TD>
        <input maxlength="100"  type="submit" value="          BACK             " name="action" onClick="window.location.href='indexPage.jsp'" />
     </TD>
</tr>

</table>


  <br>
  
  </FORM>
<%@ include file="footer.jsp"%>