<%@ include file="header.jsp" %>

<script language="JavaScript" type="text/javascript" src="js/maintUser.js"></script>
<%@ page import="com.murho.utils.*"%>
<title>Maintain User</title>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp" %>

<jsp:useBean id="gn"  class="com.murho.gates.Generator" />
<jsp:useBean id="sl"  class="com.murho.gates.selectBean" />
<jsp:useBean id="ub"  class="com.murho.gates.userBean" />
<jsp:useBean id="eb"  class="com.murho.gates.encryptBean" />

<%!     ArrayList al; %>
<%
        StrUtils  _strUtils     = new StrUtils();
		String caption  = "Maintain";
        String delParam = "";
        String disabled = "";
        String disabledInView="";


        Enumeration e = request.getParameterNames();
        while(e.hasMoreElements())
        {
            String s = e.nextElement().toString();

            if(s.equalsIgnoreCase("view"))  // If the option is to view disable partly
                {
                     caption = "View";
                     disabledInView = "disabled";
                }

            else if(s.equalsIgnoreCase("del")) // If the option is to delete disable all
                {
                     caption = "Delete";
                     delParam = "<input type=\"Hidden\" name=\"del\" value=\"\">"; //  To Indicate the delete function
                     disabled = "disabled";
                }

        }
%>

<FORM name="chooseForm" METHOD="post" action="maintUser.jsp">
  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">MAINTAIN USER</font>
  </table>
  <br>
 <%=delParam%>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
  <tr>
  <td width="100%">&nbsp;
<font face="Times New Roman" size="2">
  <center>
    <table border="0" width="90%">
    <tr>
    <td width="100%">
    <CENTER>

      <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
        <TR>
        <TH WIDTH="18%" >User ID
        <TD>
            <SELECT NAME ="USER_ID" size="1" <%=disabledInView%>>
            <OPTION selected value=''>< -- Choose -- > </OPTION>
            <%=sl.getUserIDs()%>
            </SELECT>&nbsp;
        </TD>
        <TH WIDTH="18%" >User Name&nbsp;&nbsp;&nbsp;&nbsp;
        <TD><INPUT type="text" name="USER_NAME" <%=disabledInView%>>&nbsp;
            <INPUT type="submit" value="Go" <%=disabledInView%>>&nbsp;
            <input type="button" value="Cancel" <%=disabledInView%> onClick ="window.location.href='indexPage.jsp'">
        </TD>
        </TR>
        </TABLE>
<%
    String user_id   = _strUtils.fString(request.getParameter("USER_ID"));
    String user_name = _strUtils.fString(request.getParameter("USER_NAME")).toUpperCase();


    if (user_id.length()<1 && user_name.length()<1)
    {
        out.write("<br><table width=\"100%\"><tr><td align=\"center\"><b>Please Select the User ID ( or ) Key in the User Name</b></td></tr></table></FORM>");
    }
    else
    {

        if(user_id.length()>0)
        al  = ub.getUserDetails(user_id,0);
        else
        al  = ub.getUserDetails(user_name,1);

        if(al.isEmpty())
        out.write("<br><table width=\"100%\"><tr><td align=\"center\"><font color=\"red\"<b>Invalid USER ID or USER NAME Specified</b></font><br>"+
                    "Please ensure the User ID or USER NAME is available in the database</td></tr></table>");
        else
        {
                   user_id      = al.get(0).toString();
            String authorise_by = al.get(7).toString();
            if((authorise_by==null) || (authorise_by.length()<=1)) authorise_by="Not Authorised";
            //below line is added on 11-june-2014 for user active/inactive ticket #WO0000000041526
           
            String old_status = al.get(10).toString();
            
            //decripting password 
            String encPwd = eb.decrypt(al.get(1).toString());
            %>
</FORM>
<FORM name="userForm" method="post" action="newUserSubmit.jsp" onSubmit="return validateUser(document.userForm)">
    <CENTER>
      <TABLE border="0" CELLSPACING=0 WIDTH="100%">
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" >User ID :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD><INPUT size="50"  MAXLENGTH=10 name="USER_ID" value="<%=al.get(0).toString()%>" disabled>
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" >Password :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD><INPUT type="password" size="50" MAXLENGTH=10 name="PASSWORD" <%=disabled%> value="<%=encPwd%>" onFocus="clearText(document.userForm);">
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" >Confirm Password :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD><INPUT type="password" size="50" MAXLENGTH=10 name="CPASSWORD" <%=disabled%> value="<%=encPwd%>">
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" >User Name :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD><INPUT size="50" MAXLENGTH=25 name="USER_NAME" <%=disabled%> value="<%=al.get(2).toString()%>">
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" >Job Title :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD><INPUT size="50" MAXLENGTH=40 name="RANK" <%=disabled%> value="<%=al.get(3).toString()%>">
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" >Remarks :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD><INPUT size="50" MAXLENGTH=60 name="REMARKS" <%=disabled%> value="<%=al.get(4).toString()%>">
        <TR>
        <!-- Updated on 6-june-2014 for user active/inactive ticket #WO0000000041526
        	Disabled the Effective Date 
         -->
        <TH WIDTH="35%" ALIGN="RIGHT" >Effective Date :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD><INPUT size="50" MAXLENGTH=10 name="EFFECTIVE_DATE"  value="<%=gn.getDB2UserDate(al.get(5).toString())%>" disabled="disabled">
        
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" >User Level :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD>
            <SELECT NAME ="USER_LEVEL" size="1" <%=disabled%>>
            <OPTION selected value="<%=al.get(6).toString()%>"><%=al.get(6).toString()%></OPTION>
            <%=sl.getUserLevels("0")%>
            </SELECT>
        </TD>
       <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" >Department :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD>
            <SELECT NAME ="DEPT" size="1" <%=disabled%>>
            <OPTION selected value="<%=al.get(9).toString()%>"><%=al.get(9).toString()%></OPTION>
            <%=sl.getDeptIDs()%>
            </SELECT>
        </TD>
        <TR>
        <!-- Below attribute is added on 27/May/2014 for user active/in active ticket #WO0000000041526 -->
       <TH WIDTH="35%" ALIGN="RIGHT" >Status :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD>
            <SELECT NAME ="USER_ACTIVE" size="1" onchange="show(this.options[this.selectedIndex].value)"<%=disabled%>>
            <OPTION selected value="<%=al.get(10).toString()%>"><%=al.get(10).toString()%></OPTION>
            <% String flag = al.get(10).toString();
            if(flag.equals("INACTIVE")) {%>
            <option value="ACTIVE">ACTIVE</option>
            <%}else{ %>
            <option value="INACTIVE">INACTIVE</option>
            <%} %>
            </SELECT>
        </TD>
        </TR>
        
		
         <TR id="hideTR">
        <TH WIDTH="35%" ALIGN="RIGHT" >Reason :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
        <TD>
           	<input  size="50" maxlength="60" name="REASON" <%=disabled%> value="<%=al.get(11).toString()%>" >
        </TD>
        </TR> 
        <script type="text/javascript">
		window.onload = hide('<%=al.get(10).toString()%>');
		
		</script>
		<!-- End -->
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" >Authorised By :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD><INPUT size="50" disabled name="AUTHORISE_BY" value="<%=authorise_by%>">
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" >Authorised On :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD><INPUT size="50" disabled name="AUTHORISE_ON" value="<%=gn.getDB2UserDateTime(al.get(8).toString())%>">
      </TABLE>
    </CENTER>
    </td>
    </tr>
    </table>
    <br>
    </center>
    <INPUT type="Hidden" name="USER_ID" value="<%=user_id%>">
    <!-- below line is added on 11-june-2014 for user active/inactive ticket ticket #WO0000000041526 -->
    <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
    <input type="hidden" name ="OLD_STATUS" value="<%=old_status %>">
    
    <div align="center"><center>
      <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
        <TR> <TD  ALIGN = "CENTER">
<%
        if(caption.equalsIgnoreCase("delete"))
        //below line is updated on 11-june-2014 for user active/inactive ticket ticket #WO0000000041526 -->
        //out.write("<INPUT type=\"Submit\" name=\"Submit\" Value=\"Delete\" onClick=\"return confirm('Are you sure to delete "+user_id+" user ID permanently ?');\">&nbsp;");
        out.write("<INPUT type=\"Button\" Value=\"Delete\" >&nbsp;");
        else if(caption.equalsIgnoreCase("view"))
        out.write("<INPUT type=\"Button\" Value=\"Back to List\" onClick=\"window.location.href='javascript:history.back()'\"> ");
        else
        out.write("<INPUT type=\"Submit\" name=\"Submit\" Value=\"Update\">&nbsp;");
%>
        <INPUT Type=Button name="C" Value="Cancel" Size = 10 onClick="window.location.href='indexPage.jsp'"></TD>
      </TABLE>
 </center></div>
 <div align="center">
 <center><p>&nbsp;</p>
 </center>
 </div></td></tr>
  </table>
<%
        }   //  Closing inner else
    }       //  Closing else
 %>
</FORM>

<%@ include file="footer.jsp" %>
