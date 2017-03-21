<html>
<head>
<script language="JavaScript" type="text/javascript" src="js/pwd.js"></script>
<title>Change Password</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body>
<%
String user_id = session.getAttribute("LOGIN_USER").toString();
String pwd     = request.getParameter("pwd");
%>

<FORM name="form" method="post" action="pwdSubmit.jsp" onSubmit="return validatePwd(this)">
  <p>
  <INPUT type="Hidden" name="USER_ID" value="<%=user_id%>">
  <INPUT type="Hidden" name="PASSWORD" value="<%=pwd%>">
  <br>
  <font face=arial size=2 color=blue><b>Hello <%=user_id%>, Welcome to Ciba Vision 
  <br>
  <br>
 <b> As this is your first visit to the system, we recommend you to change your password.
  <br>Please do change the password and start using the system.</b></font>
  <br><br>
  <center>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">CHANGE PASSWORD</font>
  </table>
  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
      <td width="100%">&nbsp;
        <font face="Times New Roman" size="2">
        <center>
          <table border="0" width="100%">
            <tr>
              <td width="100%">
                <CENTER>
                  <TABLE BORDER="0" CELLSPACING=0 WIDTH="80%">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Enter New Password :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                      	<!-- Maxlength modifed by Arun for #25046 
                        <INPUT type="password" size="30" MAXLENGTH=10 name="NPASSWORD">-->
                        <INPUT type="password" size="30" MAXLENGTH=16 name="NPASSWORD">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Confirm Password :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
	                    <!-- Maxlength modifed by Arun for #25046 
                        <INPUT type="password" size="30" MAXLENGTH=10 name="CPASSWORD">-->
                        <INPUT type="password" size="30" MAXLENGTH=16 name="CPASSWORD">
                      </TD>
                  </TABLE>
                </CENTER>
              </td>
            </tr>
          </table>
         <br><input type="Submit" value="Update" name="Submit">
         <p>&nbsp;</p>
        </font></td>
    </tr>
  </table>
</FORM>
<%@ include file="footer.jsp" %>
