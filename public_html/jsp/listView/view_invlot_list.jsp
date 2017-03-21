<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>

<html>
<head>
<title>Lot List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="center"><font color="white"> LOT NO</font></TH>
  
     


    </TR>
<%
    StrUtils strUtils   = new StrUtils();
    InvMstDAO  invDao = new InvMstDAO();
     DateUtils  dateUtil = new DateUtils();
    MLogger mLogger = new MLogger();
    String lotno = strUtils.fString(request.getParameter("LOTNO"));

    mLogger.log("lotno id :"+lotno);
    String sBGColor = "";

   try{
    List listQry = invDao.getDistinctLotInINV(lotno);
 
    for (int i =0; i<listQry.size(); i++){
    Map map = (Map) listQry.get(i);
    sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     String lot         = (String) map.get("LOT");
   


%>
    <TR bgcolor="<%=sBGColor%>">
      <td align ="center" class="main2">
      <a href="#" onClick="window.opener.form1.LOTNO.value='<%=lot%>';window.close();"><%=lot%></td>
     
      

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





