<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>


<html>
<head>
<title>Tray group List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="center"><font color="white">Tray Group</font></TH>
      <TH align="center"><font color="white">Description</font></TH>
      
      
     


    </TR>
<%
    StrUtils strUtils   = new StrUtils();
    TrayGrpUtil trayUtil =  new TrayGrpUtil();
    DateUtils  dateUtil = new DateUtils();
   
    MLogger mLogger = new MLogger();
    String trayGrp = strUtils.fString(request.getParameter("TRAY_GRP_ID"));

     mLogger.log(" trayGrp :"+trayGrp);
    String sBGColor = "";

   try{
    List listQry = trayUtil.getTrayGrpListStartsWith(trayGrp);
 
    for (int i =0; i<listQry.size(); i++){
    Map map = (Map) listQry.get(i);
    sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
      trayGrp         = (String) map.get(MDbConstant.TRAY_GRP);
     String desc           = (String) map.get(MDbConstant.TRAY_DESC);
  
    


%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form1.TRAY_GRP_ID.disabled = true;window.opener.form1.TRAY_GRP_ID.value='<%=trayGrp%>';window.opener.form1.TRAY_GRP_ID1.value='<%=trayGrp%>';window.close();"><%=trayGrp%></a></td>
      <td align ="center" class="main2"><%=desc%></td>
      
      

</TR>
<%
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in Tray Grp ");}
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





