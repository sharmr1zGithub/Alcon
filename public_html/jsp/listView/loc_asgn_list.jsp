<%@ page import="java.util.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>

<html>
<head>
<title>Location Assigned Rule </title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
<INPUT type="hidden" name="RULE" value="">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">RULE ID</font></TH>
      <TH align="left"><font color="white">DESCRIPTION</font></TH>
      <TH align="left"><font color="white">PRD GRP ID</font></TH>
       <TH align="left"><font color="white">LOC ID</font></TH>
    </TR>
<%
    LocAssignRulesUtil locAsgnUtil = new LocAssignRulesUtil();
    StrUtils strUtils = new StrUtils();
    String sRule = strUtils.fString(request.getParameter("RULE"));

    String sBGColor = "";
   try{
   List listQry = locAsgnUtil.getLocAsgnListStartsWith(sRule);
    
   
    for (int i =0; i<listQry.size(); i++){
    Map map = (Map) listQry.get(i);
  
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
         sRule        = (String)map.get("RulesId");
        String sRuleDesc      = strUtils.removeQuotes((String)map.get("Description"));
        String sPrdGrp          = (String)map.get(MDbConstant.PRD_GRP_ID);
        String sLocId          = (String)map.get(MDbConstant.LOC_ID);

%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form1.RULE.disabled = true;window.opener.form1.RULE.value='<%=sRule%>';window.opener.form1.RULE1.value='<%=sRule%>';window.opener.form1.RULE_DESC.value='<%=sRuleDesc%>';window.opener.form1.PRD_GRP_ID.value='<%=sPrdGrp%>';window.opener.form1.LOC_ID.value='<%=sLocId%>';window.close();"><%=sRule%></a></td>
      <td class="main2"><%=sRuleDesc%></td>
      <td class="main2"><%=sPrdGrp%></td>
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





