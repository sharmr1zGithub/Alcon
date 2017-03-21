<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>


<html>
<head>
<title>Pallet Group List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      
      <TH align="center"><font color="white">Ship To</font></TH>
      <TH align="center"><font color="white">Traveler Prefix</font></TH>
      <TH align="center"><font color="white">Pallet Group</font></TH>
      
     


    </TR>
<%
    StrUtils strUtils   = new StrUtils();
    PalletGrpUtil palletGrpUtil =  new PalletGrpUtil();
    DateUtils  dateUtil = new DateUtils();
   
    MLogger mLogger = new MLogger();
    String palletGrp = strUtils.fString(request.getParameter("PALLET_GRP"));

     mLogger.log(" palletGrp :"+palletGrp);
    String sBGColor = "";

   try{
    List listQry = palletGrpUtil.getPalletGrpListStartsWith(palletGrp);
 
    for (int i =0; i<listQry.size(); i++){
    Map map = (Map) listQry.get(i);
    sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
    
     String shipTo           = (String) map.get(MDbConstant.SHIP_TO);
    String travPfx              = (String) map.get(MDbConstant.TRAV_PFX);
      palletGrp         = (String) map.get(MDbConstant.PALLET_GRP);
    


%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form1.PALLET_GRP.disabled = true;window.opener.form1.PALLET_GRP.value='<%=palletGrp%>';window.opener.form1.PALLET_GRP1.value='<%=palletGrp%>';window.close();"><%=palletGrp%></a></td>
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





