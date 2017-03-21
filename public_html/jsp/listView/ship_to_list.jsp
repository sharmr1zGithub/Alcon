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
    String shipTo = strUtils.fString(request.getParameter("SHIP_TO"));

     mLogger.log(" shipTo :"+shipTo);
    String sBGColor = "";

   try{
    List listQry = destUtil.getShipToListStartsWith(shipTo);
 
    for (int i =0; i<listQry.size(); i++){
    Map map = (Map) listQry.get(i);
    sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
      String destn         = (String) map.get(MDbConstant.DESTINATION);
      shipTo           = (String) map.get(MDbConstant.SHIP_TO);
    String travPfx              = (String) map.get(MDbConstant.TRAV_PFX);
    


%>
    <TR bgcolor="<%=sBGColor%>">
    <td align ="center" class="main2"><%=destn%></td>
      <td class="main2"><a href="#" onClick="window.opener.form1.SHIP_TO.disabled = true;window.opener.form1.SHIP_TO.value='<%=shipTo%>';window.opener.form1.SHIP_TO1.value='<%=shipTo%>';window.opener.form1.TRAV_PFX.value='<%=travPfx%>';window.opener.form1.DESTINATION.value='<%=destn%>';window.close();"><%=shipTo%></a></td>

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





