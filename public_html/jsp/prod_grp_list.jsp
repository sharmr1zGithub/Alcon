<%@ page import="java.util.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>

<html>
<head>
<title>Product Group List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
<INPUT type="hidden" name="PRD_GRP_ID" value="">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">PRD GRP ID</font></TH>
      <TH align="left"><font color="white">DESCRIPTION</font></TH>
      <TH align="left"><font color="white">NO. OF TRAYS</font></TH>
       <TH align="left"><font color="white">LOC ID</font></TH>
    </TR>
<%
    PrdGrpMstUtil prdGrpUtil = new PrdGrpMstUtil();
    StrUtils strUtils = new StrUtils();
    String sPrdGrpId = strUtils.fString(request.getParameter("PRD_GRP_ID"));

    String sBGColor = "";
   try{
    ArrayList arrPrdGrp = prdGrpUtil.getPrdGrpListStartsWith(sPrdGrpId);
    for(int i =0; i<arrPrdGrp.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        ArrayList arrPrdGrpLine  = (ArrayList)arrPrdGrp.get(i);
        String sPrdGrpId1        = (String)arrPrdGrpLine.get(0);
        String sPrdGrpDesc1      = strUtils.removeQuotes((String)arrPrdGrpLine.get(1));
        String sTotTray          = (String)arrPrdGrpLine.get(2);
        String sLocId          = (String)arrPrdGrpLine.get(3);

%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form1.PRD_GRP_ID.disabled = true;window.opener.form1.PRD_GRP_ID.value='<%=sPrdGrpId1%>';window.opener.form1.PRD_GRP_ID1.value='<%=sPrdGrpId1%>';window.opener.form1.PRD_GRP_DESC.value='<%=sPrdGrpDesc1%>';window.opener.form1.NO_TRAY.value='<%=sTotTray%>';window.opener.form1.LOC_ID.value='<%=sLocId%>';window.close();"><%=sPrdGrpId1%></a></td>
      <td class="main2"><%=sPrdGrpDesc1%></td>
      <td class="main2"><%=sTotTray%></td>
      <td class="main2"><%=sLocId%></td>
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





