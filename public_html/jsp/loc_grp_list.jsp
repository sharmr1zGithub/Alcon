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
      <TH align="left"><font color="white">LOC GRP ID</font></TH>
      <TH align="left"><font color="white">DESCRIPTION</font></TH>
      <TH align="left"><font color="white">TOTAL BAY</font></TH>
    </TR>
<%
    LocGrpMstUtil locGrpUtil = new LocGrpMstUtil();
    StrUtils strUtils = new StrUtils();
    String sLocGrpId = strUtils.fString(request.getParameter("LOC_GRP_ID"));

    String sBGColor = "";
   try{
    ArrayList arrLocGrp = locGrpUtil.getLocGrpListStartsWith(sLocGrpId);
    for(int i =0; i<arrLocGrp.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        ArrayList arrLocGrpLine  = (ArrayList)arrLocGrp.get(i);
        String sLocGrpId1        = (String)arrLocGrpLine.get(0);
        String sLocGrpDesc1      = strUtils.removeQuotes((String)arrLocGrpLine.get(1));
        String sTotBay1          = (String)arrLocGrpLine.get(2);

%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form1.LOC_GRP_ID.disabled = true;window.opener.form1.LOC_GRP_ID.value='<%=sLocGrpId1%>';window.opener.form1.LOC_GRP_DESC.value='<%=sLocGrpDesc1%>';window.opener.form1.LOC_GRP_ID1.value='<%=sLocGrpId1%>';window.opener.form1.TOTAL_BAY.value='<%=sTotBay1%>';window.close();"><%=sLocGrpId1%></a></td>
      <td class="main2"><%=sLocGrpDesc1%></td>
      <td class="main2"><%=sTotBay1%></td>
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





