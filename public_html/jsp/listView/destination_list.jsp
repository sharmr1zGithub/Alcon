<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>


<html>
<head>
<title>Destination List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="center"><font color="white">Destination</font></TH>
      <TH align="center"><font color="white">Ship To</font></TH>
      <TH align="center"><font color="white">Traveler Prefix</font></TH>
      
     


    </TR>
<%
    StrUtils strUtils   = new StrUtils();
    DestinationUtil destUtil =  new DestinationUtil();
    DateUtils  dateUtil = new DateUtils();
   
    MLogger mLogger = new MLogger();
    String destn = strUtils.fString(request.getParameter("DESTINATION"));

     mLogger.log(" destn :"+destn);
    String sBGColor = "";

   try{
    List listQry = destUtil.getDestListStartsWith(destn);
 
    for (int i =0; i<listQry.size(); i++){
    Map map = (Map) listQry.get(i);
    sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
      destn         = (String) map.get(MDbConstant.DESTINATION);
     String shipTo           = (String) map.get(MDbConstant.SHIP_TO);
    String travPfx              = (String) map.get(MDbConstant.TRAV_PFX);
    


%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form1.DESTINATION.disabled = true;window.opener.form1.DESTINATION.value='<%=destn%>';window.opener.form1.SHIP_TO.value='<%=shipTo%>';window.opener.form1.DESTINATION1.value='<%=destn%>';window.opener.form1.TRAV_PFX.value='<%=travPfx%>';window.close();"><%=destn%></a></td>
      <td align ="center" class="main2"><%=shipTo%></td>
      <td align ="center" class="main2"><%=travPfx%></td>
      

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





