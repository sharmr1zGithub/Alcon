<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import ="com.murho.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.math.BigDecimal"%>
<%

      response.setContentType("application/vnd.ms-excel");
     // response.setContentType("application/plain");
      response.setHeader("Content-Disposition", "attachment;filename=TrayLabelingReportExcel.xls");
    //  response.setHeader("Content-Disposition", "attachment;filename=PutAwaySummaryExcel.txt");
%>
<html>
<head>
<title>PutAway Summary Excel</title>
    <style>
        td, th { font-family: Tahoma, Verdana, sans-serif; font-size: 10px; }
    </style>
</head>



<body bgcolor="#ffffff"  text="#000000" link="#efe7c6" alink="#9c8349" vlink="#535353">
<form>

  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <th width="12%"><font color="#ffffff" align="center">SNO</th>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>TRAVELER</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PALLET</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>TRAYID</STRONG> </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>MTID</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>SKU</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PICK QTY</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PACK QTY</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><b>STATUS</center></td>
         
	 </tr>
<%
StrUtils strUtils     = new StrUtils();
ReportUtil repUtil       = new ReportUtil();
ArrayList invQryList  = new ArrayList();
OBTravelerDetDAO obTravelDetDAO=new OBTravelerDetDAO();

String PLANT       ="",ITEM ="",MTID="",filesstatus="",TRAVELER="",TRAVELER1="",hiddenView="",PALLET="";
String action         = strUtils.fString(request.getParameter("action")).trim();
String html = "",cntRec="false" ;


PLANT            = strUtils.fString(request.getParameter("PLANT"));
ITEM             = strUtils.fString(request.getParameter("ITEM"));
PALLET             = strUtils.fString(request.getParameter("PALLET"));
hiddenView       = strUtils.fString(request.getParameter("HiddenView"));

MLogger.log("hiddenView ::::::::::::::::::::"+hiddenView);



if(hiddenView.equalsIgnoreCase("Go"))
{
          hiddenView="";
           invQryList = obTravelDetDAO.TrayLabelingReport(PLANT,ITEM,PALLET);
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
          <TR bgcolor = "<%=bgcolor%>">
             <TD align="center" width="12%"><%=iIndex%></TD>
              <TD align="center" width="13%"><%=(String)lineArr.get("traveler_id")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("pallet")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("trayid")%></TD> 
             <TD align="center" width="13%"><%=(String)lineArr.get("mtid")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("sku")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("pickqty")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("traylableqty")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("status")%></TD>
       
           </TR>
       <%}%>
    
    </TABLE>

<%}%>
</form>
</body>
</html>

