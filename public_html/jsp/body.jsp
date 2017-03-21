<script language="javascript" src="js/sniffer.js"></script>

<%@ include file="links/sessionMenuLinks.jsp"%>
<script language="javascript1.2" src="js/style.js"></script>
</head>
<body bgcolor="#ffffff"  text="#000000" link="#efe7c6" alink="#9c8349" vlink="#535353">
<!--<body bgcolor="#006699"  text="#000000" link="#efe7c6" alink="#9c8349" vlink="#535353">-->

<!-- background="images/frame2_tile.gif"-->
<font face="Times New Roman">
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
  <tr>
    <td width="20%" align="left" >
  <a href="http://www.murho.com/pages/7/index.htm">
	<img border="0" src="images/MurhoLogo.jpg" width="100" height="45">
  </a> 
  </td>
 <!-- <td width="50%" align="right">
    <DIV align="center">
    
     <FONT color="#ff3366" size="5"><STRONG>Test Environment</STRONG></FONT> 
        <STRONG><FONT color="#ff3366">Ver 1.2</FONT></STRONG>   
    </DIV>
  </td>-->
        <td>
	<table border="0" cellpadding="0" cellspacing="0" width="360" align="right">
        <tr align="center">
         <td width="68%" align="right"><font color="#666666" size="2" face="Arial, Helvetica, sans-serif"><b>Login Name : </b></font></td>
         <td width="32%" align="left">&nbsp;<font color="#666666" size="2" face="Arial, Helvetica, sans-serif"><b><%=session.getAttribute("LOGIN_USER").toString()%></b></font></td>
       </tr>
       <!--
       <tr align="center">
         <td width="68%" align="right"><font color="#666666" size="2" face="Arial, Helvetica, sans-serif"><b>Department  : </b></font></td>
         <td width="32%" align="left">&nbsp;<font color="#666666" size="2" face="Arial, Helvetica, sans-serif"><b><%=session.getAttribute("PLANT").toString()%></b></font></td>
       </tr>
       -->
      <tr align="center">
        <td width="68%" align="right"><font color="#666666"><b><font size="2" face="Arial, Helvetica, sans-serif">Server Name </font>: </b></font></td>
        <td width="32%" align="left">&nbsp;<font color="#666666" size="2" face="Arial, Helvetica, sans-serif"><b><%=request.getServerName()%></b></font></td>
      </tr>
    </table>
   </td>
  </tr>
</table>
</font>

