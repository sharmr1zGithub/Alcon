<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ page import="java.text.SimpleDateFormat"%>

<html>
<head>
<title>Traveler Reference List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
    <TH align="center"><font color="white">Reference No. List</font></TH>

    </TR>
<%
    StrUtils strUtils   = new StrUtils();
    DateUtils  dateUtil = new DateUtils();
    TRHeadDAO trHeadDAO=new TRHeadDAO();
    ArrayList soList    = new ArrayList();
    MLogger mLogger = new MLogger();
    
    String REFNO = strUtils.fString(request.getParameter("REFNO"));

     mLogger.log("REFNO slip :"+REFNO);
    String sBGColor = "";

   try{
    List listQry = trHeadDAO.get_ob_travel_Ref_Shiping(CibaConstants.cibacompanyName,REFNO);
    for (int i =0; i<listQry.size(); i++){
    Map map = (Map) listQry.get(i);
    sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
    String refno         = (String) map.get("RefNo");
    
     %>
    <TR bgcolor="<%=sBGColor%>">
      <td align ="center" class="main2">
      <a href="#" onClick="window.opener.form1.REFNO.value='<%=refno%>';
      
       window.close();"><%=refno%></td>
      

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







