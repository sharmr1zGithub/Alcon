<%@ include file="header.jsp" %>

<script language="JavaScript" type="text/javascript" src="js/pwd.js"></script>
<title>Change Password</title>
<link rel="stylesheet" href="css/style.css">

<%

//Code added by Arun for #25046
String resetFlag = (String)request.getParameter("resetPwdByAdmin");
String expiryFlag = (String)request.getParameter("pwdAboutToExpire");
boolean showOldPwdField = true;
if( null != resetFlag && resetFlag.equalsIgnoreCase("true")){
	String result = (String)session.getAttribute("RESULT");
	showOldPwdField = false;
	if(null != result && result != ""){
		%>
		<font color="red"><h4><br><%= result %></h4></font>
		<%
	}
}else if(null != expiryFlag && expiryFlag.equalsIgnoreCase("true")){
	showOldPwdField = true;
}else{%>
	<%@ include file="body.jsp" %>
<%} %>



<%

String user_id = session.getAttribute("LOGIN_USER").toString();
String pwd       = (String)request.getParameter("password");


%>
<FORM name="form" method="post" action="pwdSubmit.jsp" onSubmit="return validatePwd(this)">
  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">CHANGE PASSWORD</font>
  </table>
  <br>

  <INPUT type="Hidden" name="USER_ID" value="<%=user_id%>">
 
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
      <td width="100%">&nbsp;
        <font face="Times New Roman" size="2">
        <center>
          <table border="0" width="90%">
            <tr>
              <td width="100%">
                <CENTER>
                  <TABLE BORDER="0" CELLSPACING=0 WIDTH="100%">
                  <!-- Below if condition added by Arun for #25046 -->
                  <% if(showOldPwdField){ %>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Enter Old Password :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                      	<!-- Maxlength modifed by Arun for #25046 
                        <INPUT type="password" size="50" MAXLENGTH=10 name="PASSWORD">-->
                        <INPUT type="password" size="50" MAXLENGTH=16 name="PASSWORD">
                          <!-- Below line added by Arun for #25046: set OLD_PASSWORD_FLAG = Y in case of manual change pwd -->
                        <INPUT type="hidden" size="50" MAXLENGTH=16 name="OLD_PASSWORD_FLAG" value="Y">
                       <%} else{%> 
                       <INPUT type="hidden" size="50" MAXLENGTH=16 name="PASSWORD" value=<%=pwd %>>
                       <%} %>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Enter New Password :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                      	<!-- Maxlength modifed by Arun for #25046 
                        <INPUT type="password" size="50" MAXLENGTH=10 name="NPASSWORD">-->
                         <INPUT type="password" size="50" MAXLENGTH=16 name="NPASSWORD">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Confirm Password :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                     	 <!-- Maxlength modifed by Arun for #25046 
                        <INPUT type="password" size="50" MAXLENGTH=10 name="CPASSWORD">-->
                        <INPUT type="password" size="50" MAXLENGTH=16 name="CPASSWORD">
                      </TD>
                  </TABLE>
                </CENTER>
              </td>
            </tr>
          </table>
          <br>
        </center>
        <div align="center">
          <center>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
                  <input type="Submit" value="Update" name="Submit">
                  &nbsp;
                  <!-- Below if condition added by Arun for #25046 -->
                  <% if(showOldPwdField){ %>
                  	<input type="Button" value="Cancel" onClick ="window.location.href='indexPage.jsp'">
                  <%} %>
                </td>
              </tr>
            </table>
          </center>
        </div>
        <div align="center">
          <center>
            <p>&nbsp;</p>
          </center>
        </div>
        </font></td>
    </tr>
  </table>
</FORM>
<%@ include file="footer.jsp" %>
