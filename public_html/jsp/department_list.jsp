<%@ page import="java.util.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>

<html>
<head>
<title>Department List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Department Code</font></TH>
      <TH align="left"><font color="white">Description</font></TH>
    </TR>
<%
    DeptUtil deptUtils = new DeptUtil();
    StrUtils strUtils = new StrUtils();
    String sDeptCode = strUtils.fString(request.getParameter("DEPT_CODE"));

    String sBGColor = "";
   try{
    ArrayList arrDept = deptUtils.getDeptListStartsWith(sDeptCode);
    for(int i =0; i<arrDept.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        ArrayList arrDeptLine = (ArrayList)arrDept.get(i);
        String sDeptCode1     = (String)arrDeptLine.get(0);
        String sDeptDesc1     = strUtils.removeQuotes((String)arrDeptLine.get(1));

%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form1.DEPT_CODE.disabled = true;window.opener.form1.DEPT_CODE.value='<%=sDeptCode1%>';window.opener.form1.DEPT_CODE1.value='<%=sDeptCode1%>';window.opener.form1.DEPT_DESC.value='<%=sDeptDesc1%>';window.close();"><%=sDeptCode1%></td>
      <td class="main2"><%=sDeptDesc1%></td>
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





