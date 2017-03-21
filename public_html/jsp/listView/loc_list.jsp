<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>

<html>
<head>
<title>Location List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="center"><font color="white">Loc Id</font></TH>
      <TH align="center"><font color="white">Desc</font></TH>
      <TH align="center"><font color="white">Loc grp Id</font></TH>
      
     


    </TR>
<%
    StrUtils strUtils   = new StrUtils();
    LocMstUtil locUtil = new LocMstUtil();
    DateUtils  dateUtil = new DateUtils();
    ArrayList soList    = new ArrayList();
    MLogger mLogger = new MLogger();
    String loc_id = strUtils.fString(request.getParameter("LOC_ID"));

     mLogger.log("location id :"+loc_id);
    String sBGColor = "";

   try{
    List listQry = locUtil.getLocListStartsWith(loc_id);
 
    for (int i =0; i<listQry.size(); i++){
    Map map = (Map) listQry.get(i);
    sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     String locgrpid         = (String) map.get("LOC_GRP_ID");
     String desc           = (String) map.get("LOC_DESC");
     loc_id              = (String) map.get("LOC_ID");
    


%>
    <TR bgcolor="<%=sBGColor%>">
      <td align ="center" class="main2">
      <a href="#" onClick="window.opener.form1.LOC_ID.value='<%=loc_id%>';window.opener.form1.LOC_ID1.value='<%=loc_id%>';window.close();"><%=loc_id%></td>
       <td align ="center" class="main2"><%=desc%></td>
       <td align ="center" class="main2"><%=locgrpid%></td>
      

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





