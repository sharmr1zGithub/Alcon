<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ include file="header.jsp"%>

<html>
<head>
<title>Item List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="center"><font color="white">Item Id</font></TH>
      <TH align="center"><font color="white">Desc</font></TH>
      <TH align="center"><font color="white">Uom</font></TH>
      <TH align="center"><font color="white">Prd grp Id</font></TH>
      
     


    </TR>
<%
    StrUtils strUtils   = new StrUtils();
    ItemMstUtil itemUtil = new ItemMstUtil();
    DateUtils  dateUtil = new DateUtils();
   
    MLogger mLogger = new MLogger();
    String item_id = strUtils.fString(request.getParameter("ITEM_ID"));

  //  mLogger.log("Item id :"+item_id);
    String sBGColor = "";

   try{
    List listQry = itemUtil.getItemListStartsWith(item_id);
 
    for (int i =0; i<listQry.size(); i++){
    Map map = (Map) listQry.get(i);
    sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     String prdclsid         = (String) map.get("PRD_CLS_ID");
     String desc           = (String) map.get("ITEMDESC");
     String uom        = (String) map.get("STKUOM");
     item_id              = (String) map.get("ITEM");
    
    


%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form1.ITEM_ID.disabled = true;window.opener.form1.ITEM_ID.value='<%=item_id%>';window.opener.form1.ITEM_DESC.value='<%=desc%>';window.opener.form1.UOM.value='<%=uom%>';window.opener.form1.ITEM_ID1.value='<%=item_id%>';window.opener.form1.PRD_CLS_ID.value='<%=prdclsid%>';window.close();"><%=item_id%></a></td>
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





