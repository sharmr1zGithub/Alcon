<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import ="com.murho.dao.PickingDAO"%>
<%@ page import="java.util.*"%>
<%@ page import="java.math.BigDecimal"%>
<%

      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Content-Disposition", "attachment;filename=TraySummaryExcel.xls");
%>
<html>
<head>
<title>Tray Summary Excel</title>
    <style>
        td, th { font-family: Tahoma, Verdana, sans-serif; font-size: 10px; }
    </style>
</head>



<body bgcolor="#ffffff"  text="#000000" link="#efe7c6" alink="#9c8349" vlink="#535353">
<form>

    <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <th width="12%"><font color="#ffffff" align="center">SNO</th>
         <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>MTID</STRONG>
         </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>Gen Tray Lbl</STRONG>
         </center></td> 
         <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>SKU</STRONG>
         </center></td>
          <td width="20%"><font color="#ffffff" align="left"><center>
           <STRONG>LOT</STRONG>
         </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>LOC</STRONG>
         </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><b>PALLET</center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><b>QTY</center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><b>STATUS</center></td>
         </center></td>
		
	 </tr>
<%
      StrUtils strUtils     = new StrUtils();
ReportUtil repUtil       = new ReportUtil();
ArrayList trayQryList  = new ArrayList();
PickingDAO pickingDAO=new PickingDAO();
String PLANT       ="",ITEM ="";
String action         = strUtils.fString(request.getParameter("action")).trim();
String html = "",cntRec="false" ;
PLANT    = strUtils.fString(request.getParameter("PLANT"));
ITEM    = strUtils.fString(request.getParameter("ITEM"));


 trayQryList  = pickingDAO.getTrayLabelSummary(PLANT,ITEM);
    
      String bgcolor="";
	  for (int index = 0; index < trayQryList.size(); index++) {
               bgcolor = ((index == 0) || (index % 2 == 0)) ? "#FFFFFF" : "#dddddd";
                Map lineArr = (Map) trayQryList.get(index);
%>

<TR bgcolor = "<%=bgcolor%>">
            <TD align="center" width="12%"><%=index%></TD>
            <TD width="13%"><%=(String)lineArr.get("MTID")%></TD>
            <% 
            String temp="";
            temp=(String)lineArr.get("STATUS");
            if(temp.equalsIgnoreCase("C")  ){ %>
            <TD width="13%"><%=(String)lineArr.get("GEN_TRAY")%></TD>
           <%}else{%>
            <TD width="13%"><%=""%></TD>
           <%}%>
            <TD width="13%"><%=(String)lineArr.get("ITEM")%></TD>
			      <TD width="15%"><%=(String)lineArr.get("LOT")%></TD>
			      <TD width="11%"><%=(String)lineArr.get("LOC")%></TD>
			      <TD width="13%"><%=(String)lineArr.get("PALLET_NO")%></TD>
            <TD width="13%"><%=(String)lineArr.get("QTY")%></TD>
            <TD width="13%"><%=(String)lineArr.get("STATUS")%></TD>
           
          
           </TR>
       <%}%>
       </TABLE>
	       


</form>
</body>
</html>
