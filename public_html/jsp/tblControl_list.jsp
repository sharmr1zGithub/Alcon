<%@ page import="java.util.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>

<html>
<head>
<title>Table ControlList</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Function Code</font></TH>
       <TH align="left"><font color="white">Prefix</font></TH>
      <TH align="left"><font color="white">Function Description</font></TH>
    </TR>
<%
    TblControlUtil tblUtils = new TblControlUtil();
    StrUtils strUtils = new StrUtils();
    String sFuncCode = strUtils.fString(request.getParameter("FUNCTION_NAME"));

    String sBGColor = "";
   try{
    ArrayList arrTbl = tblUtils.getTblControlList(sFuncCode);
    for(int i =0; i<arrTbl.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        Map arrLine = (Map)arrTbl.get(i);
        String sFuncCode1     = (String)arrLine.get("FUNC");
        String sPrefix      = (String)arrLine.get("PREFIX");
        String sFuncDesc      = strUtils.removeQuotes("DESCRIPTION");

%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form1.FUNCTION_NAME.disabled = true;window.opener.form1.FUNCTION_NAME.value='<%=sFuncCode1%>';window.opener.form1.FUNCTION_NAME1.value='<%=sFuncCode1%>';window.opener.form1.FN_DESC.value='<%=sFuncDesc%>';window.opener.form1.PREFIX.value='<%=sPrefix%>';window.close();"><%=sFuncCode1%></td>
      <td class="main2"><%=sPrefix%></td>
      <td class="main2"><%=sFuncDesc%></td>
</TR>
<%
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
   <TR>
        <TH COLSPAN="8">&nbsp;</TH>
   </TR>
    <TR>
      <TH COLSPAN="8" align="center"><a href="#" onclick="window.close();"><input type="submit" value="Close"></a></TH>
    </TR>
  </table>

</body>
</html>





