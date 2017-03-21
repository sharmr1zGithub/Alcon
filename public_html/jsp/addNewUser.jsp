<%@ include file="header.jsp" %>


<script language="JavaScript" type="text/javascript" src="js/newUser.js"></script>

<title>Add New User</title>
<link rel="stylesheet" href="css/style.css">

<%@ include file="body.jsp" %>
<jsp:useBean id="gn"  class="com.murho.gates.Generator" />
<jsp:useBean id="sl"  class="com.murho.gates.selectBean" />

<FORM name="form" method="post" action="newUserSubmit.jsp" onSubmit="return validateUser(document.form)">
<!--<FORM name="form" method="post" onSubmit="return validateUser(document.form);">-->
  <br>

  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">CREATE NEW USER</font>

  </table>
  <br>
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
                      <TH WIDTH="35%" ALIGN="RIGHT" >User ID :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50"  MAXLENGTH=10 name="USER_ID" onFocus="nextfield ='PASSWORD';">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Password :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                      <!-- Maxlength modfied by Arun on 21 Sept 2011 for #25046 >
                        <INPUT type="password" size="50" MAXLENGTH=10 name="PASSWORD" onFocus="nextfield ='CPASSWORD';"-->
                        <INPUT type="password" size="50" MAXLENGTH=16 name="PASSWORD" onFocus="nextfield ='CPASSWORD';">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Confirm Password :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                      <!-- Maxlength modfied by Arun on 21 Sept 2011 for #25046 >
                        <INPUT type="password" size="50" MAXLENGTH=10 name="CPASSWORD" onFocus="nextfield ='USER_NAME';"-->
                        <INPUT type="password" size="50" MAXLENGTH=16 name="CPASSWORD" onFocus="nextfield ='USER_NAME';">                        
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >User Name :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50" MAXLENGTH=25 name="USER_NAME" onFocus="nextfield ='RANK';">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Job Title :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50" MAXLENGTH=40 name="RANK" onFocus="nextfield ='REMARKS';">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Remarks :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50" MAXLENGTH=60 name="REMARKS" onFocus="nextfield ='EFFECTIVE_DATE';">
                    <TR>      
                      <TH WIDTH="35%" ALIGN="RIGHT" >Effective Date :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50" MAXLENGTH=10 name="EFFECTIVE_DATE" value="<%=gn.getDate()%>" onFocus="nextfield ='USER_LEVEL';">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >User Level :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <SELECT NAME ="USER_LEVEL" size="1">
                          <OPTION selected value=''>< -- Choose -- > </OPTION>
                          <%=sl.getUserLevels("0")%>
                        </SELECT>
                      </TD>
                     
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Department :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <SELECT NAME ="DEPT" size="1">
                          <OPTION selected value=''>< -- Choose -- > </OPTION>
                          <%=sl.getDeptIDs()%>
                        </SELECT>  
                      </TD>
                 
                  </TABLE>
                </CENTER>
              </td>
            </tr>
          </table>
          <br>
        </center>
        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <!-- Below attribute is added on 27/may/2014 for WMS 2.8 -->
  		<input type="hidden"  value="ACTIVE" name="USER_ACTIVE">
        
        <div align="center"><center>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
                  <input type="Submit" value=" Save " name="Submit">
                  &nbsp;
                  <input type="Button" value="Cancel" onClick="window.location.href='indexPage.jsp';">
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
