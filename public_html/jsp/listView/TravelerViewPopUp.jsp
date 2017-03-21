<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ page import="java.text.SimpleDateFormat"%>

<html>
<head>
<title>DeliveryNo's List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="center"><font color="white">Delivery No. List</font></TH>
     
    </TR>
<%
    StrUtils strUtils   = new StrUtils();
    DateUtils  dateUtil = new DateUtils();
    TRHeadDAO trHeadDAO=new TRHeadDAO();
    ArrayList soList    = new ArrayList();
    MLogger mLogger = new MLogger();
    
    String ITEM = strUtils.fString(request.getParameter("ITEM"));

     mLogger.log("ITEM slip :"+ITEM);
    String sBGColor = "";

   try{
    List listQry = trHeadDAO.getTravellerView(CibaConstants.cibacompanyName,ITEM);
    for (int i =0; i<listQry.size(); i++){
    Map map = (Map) listQry.get(i);
    sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
    String trno         = (String) map.get("traveler");
    
     %>
    <TR bgcolor="<%=sBGColor%>">
      <td align ="center" class="main2">
      <a href="#" onClick="window.opener.form1.ITEM.value='<%=trno%>';
      
       window.close();"><%=trno%></td>
      

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







