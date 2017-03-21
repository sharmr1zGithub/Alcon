<%@ include file="header.jsp" %>
<title>Displaying Result</title>
<link rel="stylesheet" href="css/style.css">

<%
 String result = (String)session.getAttribute("RESULT");
 if (result == null ) result = "";
 %>


<br><br><br>


<center>
<p>
<font face="arial"><%= result %></font>
<br><br><br>
 <% // Code added by Arun for #25046
 String expiryFlag = (String)request.getParameter("pwdAboutToExpire");
 	if( null != expiryFlag && expiryFlag.equalsIgnoreCase("true")){
%>	<form>
		<INPUT type="Hidden" name="pwdAboutToExpire" value="true">
		<input type="submit" value="Yes" onclick="javascript:document.forms[0].action='chngPwd.jsp';document.forms[0].submit();"/>&nbsp;&nbsp;&nbsp;
		<input type="submit" value="No" onclick="javascript:document.forms[0].action='indexPage.jsp';document.forms[0].submit();"/>
	</form>
<% }
%>
</center>
<%@ include file="footer.jsp" %>

