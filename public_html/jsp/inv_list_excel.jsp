
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import ="com.murho.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.math.BigDecimal"%>
<%

      response.setContentType("application/vnd.ms-excel");
     // response.setContentType("application/plain");
    //  response.setHeader("Content-Disposition", "attachment;filename=Inv_List_Excel.xls");
    //  response.setHeader("Content-Disposition", "attachment;filename=PutAwaySummaryExcel.txt");
%>
<html>
<head>
<title>Inventory List Excel</title>
    <style>
        td, th { font-family: Tahoma, Verdana, sans-serif; font-size: 10px; }
    </style>
</head>



<body bgcolor="#ffffff"  text="#000000" link="#efe7c6" alink="#9c8349" vlink="#535353">
<form>

  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
        <th width="12%"><font color="#ffffff" align="center">SNO</th>
        
        
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>LOT</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>LOC</STRONG></center></td>
          <td width="13%"><font color="#ffffff" align="left"><center><STRONG>SKU</STRONG> </center></td>
           <td width="13%"><font color="#ffffff" align="left"><center><STRONG>SKU_DESC</STRONG> </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>QTY</STRONG></center></td>
		     <td width="13%"><font color="#ffffff" align="left"><center><b>UOM</center></td></center></td>
		
	 </tr>
<%
StrUtils strUtils     = new StrUtils();
ReportUtil repUtil       = new ReportUtil();
ArrayList invQryList  = new ArrayList();


String   ITEM = "", BATCH ="",  QTY ="", LOC="",hiddenView ="";
String action         = strUtils.fString(request.getParameter("action")).trim();
String html = "",cntRec="false" ;

BATCH    = strUtils.fString(request.getParameter("BATCH"));
ITEM    = strUtils.fString(request.getParameter("ITEM"));
LOC   = strUtils.fString(request.getParameter("LOC"));
hiddenView       = strUtils.fString(request.getParameter("HiddenView"));

MLogger.log("hiddenView ::::::::::::::::::::"+hiddenView);



if(hiddenView.equalsIgnoreCase("GenerateExcel"))
{
          hiddenView="";
          invQryList = repUtil.getInvListReport(CibaConstants.cibacompanyName, ITEM,BATCH, LOC);
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
         
       %>
          <TR bgcolor = "<%=bgcolor%>">
            <TD align="center" width="12%"><%=iIndex%></TD>
            <TD align="center" width="13%"><%=(String)lineArr.get("LOT")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("SKU")%></TD>
              <TD align="center" width="13%"><%=(String)lineArr.get("LOC")%></TD> 
              <TD align="right" width="13%"><%=(String)lineArr.get("SKU_DESC")%></TD>
             <TD align="right" width="13%"><%=(String)lineArr.get("QTY")%></TD>
             <TD align="right" width="13%"><%=(String)lineArr.get("UOM")%></TD>
            
       
           </TR>
       <%}%>
    
    </TABLE>

<%}%>
</form>
</body>
</html>

