<%@page import="com.murho.gates.Generator"%>
<%//@page import="java.text.*"%>
<%@ include file="header.jsp" %>

<script language="JavaScript" type="text/javascript" src="js/validateLogin.js"></script>


<head>
<title>MuRho Supply Chain Execution System</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body bgcolor="#FFFFFF" background="#FFFFFF" text="#000000" link="#000033" alink="#9c8349" vlink="#535353" onload="ref()">

<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
  <td>
    <p align="center">
      <img src="images/welcome.png" border="0"/>
      <% 
  //Generator generator = new Generator();
  //String dt = generator.getDate();
%>
    </p>
    <form name="loginForm" method="POST" action="checkUser.jsp" onSubmit="return validate()">
      <% 
 
        Enumeration e = request.getParameterNames();
        while(e.hasMoreElements())
        {
            String s = e.nextElement().toString();

            if(s.equalsIgnoreCase("warn"))
                {
                    String msg = request.getParameter("warn");
                    out.write("<br><center><font color =\"red\"><b>"+msg+"</b></font></center>");
                }
        }
%>
      <table align="center" border="0" width="40%" cellpadding="0" bgcolor="#eeeeee" background="images/login_back.gif" bordercolor="cyan">
        <Br/>
        <Br/>
        <tr>
          <td align="right" width="40%">
            <font face="arial" size="2" color="#ffffff"><b>User ID :</b></font> 
          </td>
          <td width="60%">
            <input name="name" type="text" value="" size="20" maxlength="10"/>
          </td>
        </tr>
        <tr>
          <td align="right" width="40%">
            <font face="arial" size="2" color="#ffffff"><b>Password :</b></font> 
          </td>
          <td width="60%">
          <!--  Modified by Arun on 11 Aug 2011 for #25046 
            <input name="pwd" type="password" value="" size="20" maxlength="10"/>-->
            <input name="pwd" type="password" value="" size="20" maxlength="16"/>
          </td>
        </tr>
        <input type="hidden" name="PDA" value=""/>
        <tr>
          <td colspan="2" align="center">
            <br/>
            <input type="submit" class="Button" value=" Login "/>
          </td>
        </tr>
      </table>
      <!-- version changed from 2.2 to 2.3 for tickets 980, 981 and 982 -->
      <!-- version changed from 2.3 to 2.4 for JBOSS performance issue -->
      <!-- version changed from 2.4 to 2.5 for ticket 1956- LOT and Exp Date validation -->
      <!-- version changed from 2.5 to 2.6 for IRO optimization -->
      <!-- version changed from 2.6 to 2.7 for ticket #35550 -->
      <!-- version changed from 2.7 to 2.8 for ticket #TIBCO INC000002484471, New password policy INC000002484690 , UDI INC000002770547 -->
      <!-- version changed from 2.8 to 2.9 for ticket Bug fixes -->
      <!-- version changed from 2.9 to 3.0 for LOT Restrication --> 
       <!-- version changed from 3.0 to 4.0 for UDI Implementation -->         
         
              <!-- version changed from 4.0 to 5.0 -->   
      <!-- <b><font face="Verdana" size="1" ><center>  Version 5.0</center></font></b> -->
      
       <!-- version changed from 5.0 to 6.0 -->   
       <b><font face="Verdana" size="1" ><center>  Version 6.0</center></font></b>
      <div align="Right">
        <!--&lt;img align =right src=&quot;images/logon.png&quot; border=&quot;0&quot; width=&quot;350&quot; height=&quot;280&quot;&gt; --></div>
    </form>
    <Br/>
    <Br/>
    <Br/>
    <Br/>
    <table height="81" cellSpacing="0" cellPadding="0" width="780" border="0">
      <tbody>
        <tr>
          <td width="80%" height="21">
            <hr/>
          </td>
        </tr>
        <tr>
          <td width="100%" height="21">
            <font face="arial" size="2" color="#808080"><a href="http://www.murho.com" target="_blank">MuRho Pte Ltd - Singapore</a>
              <br/>&copy; Copyright 2000, MuRho Pte Ltd. All rights reserved.&nbsp; <a href="mailto:enquiries@murho.com">Contact Us</a>
            </font>
          </td>
        </tr>
        <tr>
          <td width="100%" height="21">
            <font face="arial" size="2" color="#808080">Designed by <a href="http://www.murho.com" target="_blank">MuRho Pte. Ltd.</a> 10 Anson Road #13-08 International Plaza Singapore - 079903</font>
          </td>
        </tr>
      </tbody>
    </table>
  </td>
  </dl>
</table>
</td></tr><tr><td>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
</table>
</td></tr>
</table>
</body>
