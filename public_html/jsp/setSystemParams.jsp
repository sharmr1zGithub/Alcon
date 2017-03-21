<!-- New page added by Arun for Login Synch change on 29 June 2011 :#25046 -->
<%@ include file="header.jsp" %>
<script language="JavaScript" type="text/javascript" src="js/pwd.js"></script>
<title>Set System Parameters</title>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp" %>
<jsp:useBean id="ub"  class="com.murho.gates.userBean" />
<%

String user_id = session.getAttribute("LOGIN_USER").toString();

Map sysParams = ub.getSystemParams();

String pwdAge = (String)sysParams.get("PWD_AGE");
session.setAttribute("PWD_AGE",pwdAge);
String rangeToRemind = (String)sysParams.get("RANGE_TO_REMIND");
session.setAttribute("RANGE_TO_REMIND",rangeToRemind);
String loginAttempts = (String)sysParams.get("LOGIN_ATTEMPTS");
session.setAttribute("LOGIN_ATTEMPTS",loginAttempts);

//below lines are added by jyoti for TIBCO INC000002484471 Confirmation file automatic saved on TIBCO
String sAPFilePath = (String)sysParams.get("SAP_FILE_PATH");
session.setAttribute("SAP_FILE_PATH",sAPFilePath);

String uploadFolderPath = (String)sysParams.get("UPLOAD_FOLDER_PATH");
session.setAttribute("UPLOAD_FOLDER_PATH",uploadFolderPath);

//below lines are added by Ranjana for the generation of Inbound File under ticket WO0000000356180
String deliveryNo = (String)sysParams.get("DELIVERY_NO");
session.setAttribute("DELIVERY_NO",deliveryNo);

String Palletid = (String)sysParams.get("PALLET_ID");
session.setAttribute("PALLET_ID",Palletid);

String mtid = (String)sysParams.get("MTID");
session.setAttribute("MTID",mtid);


%>
<FORM name="form" method="post" action="systemParamSubmit.jsp" onSubmit="return validateSystemParams(this)">
  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">SET SYSTEM PARAMETERS</font>
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
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Password Age :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50" MAXLENGTH=10 name="PWDAGE" value= <%=pwdAge %> onkeyup="this.value=this.value.replace(/[^\d]/,'')">
                       </TD>
                    </TR>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Range To Remind :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50" MAXLENGTH=10 name="MINRANGE" value= <%=rangeToRemind %> onkeyup="this.value=this.value.replace(/[^\d]/,'')">
                        </TD>
                     </TR>
                     <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Login Attempts :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50" MAXLENGTH=10 name="LOGIN_ATTEMPTS" value= <%=loginAttempts %> onkeyup="this.value=this.value.replace(/[^\d]/,'')">
                        </TD>
                     </TR>
                     <%-- Added by jyoti for TIBCO INC000002484471 Confirmation file automatic saved on TIBCO  --%>
					 <TR>
						<TH WIDTH="35%" ALIGN="RIGHT">SAP Confirmation File Path :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<TD>
						<INPUT size="50" MAXLENGTH=200 name="CONF_PATH" value="<%=sAPFilePath%>"  >
						
						
						</TD>
					 </TR>
					 <TR>
						<TH WIDTH="35%" ALIGN="RIGHT">WMS Upload Folder Path :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<TD>
						<INPUT size="50" MAXLENGTH=200 name="UPLOAD_PATH" value="<%=uploadFolderPath%>" >						
						</TD>
					 </TR>
					 <%--Added by Ranjana for the generation of Inbound File under Ticket WO0000000356180 --%>
					 <TR>
						<TH WIDTH="35%" ALIGN="RIGHT">Delivery No :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<TD>
						<INPUT size="50" MAXLENGTH=30 name="DELIVERY_NO" value="<%=deliveryNo%>" 
						onkeypress="return IsAlphaNumeric(event);" 
                        onpaste="return false;" >						
						</TD>
					 </TR>
					 <TR>
						<TH WIDTH="35%" ALIGN="RIGHT">Pallet id :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<TD>
						<INPUT size="50" MAXLENGTH=30 name="PALLET_ID" value="<%=Palletid%>" 
						onkeypress="return IsAlphaNumeric(event);" 
                        onpaste="return false;" >						
						</TD>
					 </TR>
					 <TR>
						<TH WIDTH="35%" ALIGN="RIGHT">Mtid :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<TD>
						<INPUT size="50" MAXLENGTH=30 name="MTID" value="<%=mtid%>" 
						onkeypress="return IsAlphaNumeric(event);" 
                        onpaste="return false;">						
						</TD>
					 </TR>
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
                  <input type="Submit" value="Update" name="Submit" onclick="validatePath()">
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
