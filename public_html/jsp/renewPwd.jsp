<%@ include file="header.jsp" %>

<script language="JavaScript" >
function onReset()
{
 var userid=document.form.USERID.value;
 var pwd=document.form.NPASSWORD.value;
 // Modified by Arun for #25046
 	//if(form.USERID.value=="" || form.USERID.value.length < 2)
 	if(form.USERID.value.replace(/^\s+/g, '').replace(/\s+$/g, '')=="" || form.USERID.value.replace(/^\s+/g, '').replace(/\s+$/g, '').length < 2)
	{
	
		//alert("Please enter USERID ");
		alert("Please fill in the user id with 2-10 characters");
		form.USERID.value="";
		form.NPASSWORD.value="";
		form.USERID.focus();
		return false;
	} //else if(form.NPASSWORD.value=="" || form.NPASSWORD.value.length==0 || form.NPASSWORD.value.length < 8)
	
	if(/[\s]/.test(form.NPASSWORD.value)) {
    	alert("Space character is not allowed.");
    	form.NPASSWORD.value="";
    	form.NPASSWORD.focus();
    	return false;
    }
    
	if(form.NPASSWORD.value=="" || form.NPASSWORD.value.length==0 || form.NPASSWORD.value.length < 8)
	{
		//alert("Please enter NPASSWORD ");
		alert("Please fill in the new password with 8-16 characters");
		form.NPASSWORD.value="";
		form.NPASSWORD.focus();
		return false;
	}/*else if (form.NPASSWORD.value.length < 8)   // this block commented by Arun on 12 oct 2011 for #25046, taken care in above condition
	{
		alert("Please fill in the new password with 8 - 16 characters.");
		form.NPASSWORD.focus();
		return false;
	}*/
// If block added by Arun for #25046 to not allow password same as userid
 	if (form.USERID.value.toUpperCase() == form.NPASSWORD.value.toUpperCase())
    {
	    alert("Please choose a password different from User ID.");
	    form.NPASSWORD.value="";
	    form.NPASSWORD.focus();
	    return false;
    }
    
    //Below if condition is added by jyoti for Password Policy INC000002484690 
    var passwd = form.NPASSWORD.value;
    var policy_passwd = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~`!@#$%^&;*.\_\(\)\-+=}{\":?'\/\\><\[\]|,]).{4,16}/;
    if(!passwd.match(policy_passwd))
    {
    	alert("Password is invalid. It should contain:\n(i)Atleast one number(0-9).\n(ii)Atleast one upper case letter(A-Z).\n(iii)Atleast one lower case letter(a-z).\n(iv)Atleast one special charater (~!@#$%^&\"*_-+=`|\(){}[]:;'<>,.?/).");
    	form.NPASSWORD.value = "";
	    form.NPASSWORD.focus();
	    return false;
    }
   return true;

}
</script>
<!-- Title modified by Arun for #25046 -->
<!-- 
<title>Change Password</title>
-->
<title>Reset Password</title>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp" %>
<%

String user_id = session.getAttribute("LOGIN_USER").toString();

%>
<FORM name="form" method="post" action="resetPwdSubmit.jsp"  onSubmit="return onReset(this)" >
  <br>
  <!--  Below table commented by Arun for #25046 : to remove duplicate title -->
  <!-- <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">RESET PASSWORD</font>
  </table>-->
  <br>
 
 <INPUT type="Hidden" name="LOGIN_USER" value="<%=user_id%>"> 
 <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">RESET PASSWORD</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
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
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" > UserId:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT type="USERID" size="50" MAXLENGTH=10 name="USERID">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Enter New Password :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                      <!-- Maxlength modifed by Arun  on 29 Sept 2011 for #25046 
                        <INPUT type="password" size="50" MAXLENGTH=10 name="NPASSWORD">-->
                         <INPUT type="password" size="50" MAXLENGTH=16 name="NPASSWORD">
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
                  <input type="Submit" value="Reset" name="Submit">
                  &nbsp;
                  <input type="Button" value="Cancel" onClick ="window.location.href='indexPage.jsp'">
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
