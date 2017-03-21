<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>


<html>
<head>
<title>Item List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="center"><font color="white">Reason Code</font></TH>
      <TH align="center"><font color="white">Reason Description</font></TH>
     
      
     


    </TR>
<%
    StrUtils strUtils   = new StrUtils();
    ItemMstUtil itemUtil = new ItemMstUtil();
    DateUtils  dateUtil = new DateUtils();
   
    MLogger mLogger = new MLogger();
    String item_id = strUtils.fString(request.getParameter("ITEM_ID"));

     mLogger.log("Item id :"+item_id);
    String sBGColor = "";

   try{
    List listQry = itemUtil.getReasonMstDetails(item_id);
 
    for (int i =0; i<listQry.size(); i++){
    Map map = (Map) listQry.get(i);
    sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     String prdclsid         = (String) map.get("RSNCODE");
     String desc             = (String) map.get("RSNDESC");
     
    


%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form1.ITEM_ID.disabled = true;window.opener.form1.ITEM_ID.value='<%=prdclsid%>';window.opener.form1.ITEM_DESC.value='<%=desc%>';window.close();"><%=item_id%></a></td>
      <td align ="center" class="main2"><%=desc%></td>
      <td align ="center" class="main2"><%=prdclsid%></td>
      

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





