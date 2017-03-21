<%@ page import="java.util.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>

<html>
<head>
<title>Location Group List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">PRD CLS ID</font></TH>
      <TH align="left"><font color="white">DESCRIPTION</font></TH>
      <TH align="left"><font color="white">PRD GRP ID</font></TH>
    </TR>
<%
    PrdClsUtil prdclsUtil = new PrdClsUtil();
    StrUtils strUtils = new StrUtils();
    String sPrdClsId = strUtils.fString(request.getParameter("PRD_CLS_ID"));

    String sBGColor = "";
   try{
    ArrayList arrPrdCls = prdclsUtil.getPrdClsListStartsWith(sPrdClsId);
    for(int i =0; i<arrPrdCls.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        ArrayList arrPrdClsLine  = (ArrayList)arrPrdCls.get(i);
        String sPrdClsId1        = (String)arrPrdClsLine.get(0);
        String sPrdClsDesc1      = strUtils.removeQuotes((String)arrPrdClsLine.get(1));
        String sPrdGrpId1          = (String)arrPrdClsLine.get(2);

%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form1.PRD_CLS_ID.disabled = true;window.opener.form1.PRD_CLS_ID.value='<%=sPrdClsId1%>';window.opener.form1.PRD_CLS_DESC.value='<%=sPrdClsDesc1%>';window.opener.form1.PRD_CLS_ID1.value='<%=sPrdClsId1%>';window.opener.form1.PRD_GRP_ID.value='<%=sPrdGrpId1%>';window.close();"><%=sPrdGrpId1%></a></td>
      <td class="main2"><%=sPrdClsDesc1%></td>
      <td class="main2"><%=sPrdGrpId1%></td>
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





