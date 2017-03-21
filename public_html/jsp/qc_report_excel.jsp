<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import ="com.murho.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.math.BigDecimal"%>
<%

      response.setContentType("application/vnd.ms-excel");
     // response.setContentType("application/plain");
      response.setHeader("Content-Disposition", "attachment;filename=QC_ReportExcel.xls");
    //  response.setHeader("Content-Disposition", "attachment;filename=PutAwaySummaryExcel.txt");
%>
<%

StrUtils strUtils     = new StrUtils();
ReportUtil repUtil       = new ReportUtil();
ArrayList invQryList  = new ArrayList();
OBTravelerDetDAO obTravelDetDAO=new OBTravelerDetDAO();

String PLANT       ="",ITEM ="",MTID="",filesstatus="",TRAVELER="",TRAVELER1="",hiddenView="",PALLET="",yymm="";
String action         = strUtils.fString(request.getParameter("action")).trim();
String html = "",cntRec="false" ;

String TRN_DATE=DateUtils.getDate();

PLANT            = strUtils.fString(request.getParameter("PLANT"));
ITEM             = strUtils.fString(request.getParameter("ITEM"));
PALLET             = strUtils.fString(request.getParameter("PALLET"));
hiddenView       = strUtils.fString(request.getParameter("HiddenView"));
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
  <tr>
  <tr>
 
  
  <th ><font color="navy" align="right">DELIVERY NO :</th>
  <TD align="left" width="9%"><%=ITEM%></TD>
 </TABLE>
 
 <BR>
 <BR>
 
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
 
    <th ><font color="navy" align="center">DATE :</th>
   <TD align="left" width="9%"><%=TRN_DATE%></TD>
   
 </TABLE>
 <BR>
 <BR>
 
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <th ><font color="#ffffff" align="center">NO_TRAY</th>
         <td><font color="#ffffff" align="left"><center><STRONG>NO_PART</STRONG></center></td>
         <td><font color="#ffffff" align="left"><center><STRONG>DESCRIPTION</STRONG></center></td>
         <td><font color="#ffffff" align="left"><center><STRONG><STRONG>LOT#</STRONG></STRONG></center></td>
         <td><font color="#ffffff" align="left"><center><STRONG>EXP_DATE</STRONG></center></td>
         <td><font color="#ffffff" align="left"><center><STRONG>PO#</STRONG></center></td>
         <td><font color="#ffffff" align="left"><center><STRONG>QTY</STRONG></center></td>
         <td><font color="#ffffff" align="left"><center><STRONG>PALLET</STRONG></center></td>
         <td><font color="#ffffff" align="left"><center><STRONG>TL_PLT_ID</STRONG></center></td>
         <td><font color="#ffffff" align="left"><center>
           <STRONG>DELIVERY</STRONG>
         </center></td>
         <td><font color="#ffffff" align="left"><center><STRONG>EXPIRY</STRONG></center></td>
         <td><font color="#ffffff" align="left"><center><STRONG>USER</STRONG></center></td>
         <td><font color="#ffffff" align="left"><center><STRONG>SINO</STRONG></center></td>
         
	 </tr>
<%




if(hiddenView.equalsIgnoreCase("Go"))
{
          hiddenView="";
           invQryList = obTravelDetDAO.get_Qc_ReportDetails(PLANT,ITEM);
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
         
           yymm=(String)lineArr.get("yy")+(String)lineArr.get("mm"); 
         
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
             <TR bgcolor = "<%=bgcolor%>">
             <TD align="center" width="9%"><%=(String)lineArr.get("trayid")%></TD>
             <TD align="center" width="4%"><%=(String)lineArr.get("sku")%></TD>
             <TD align="center" width="20%"><%=(String)lineArr.get("sku_desc")%></TD>
             <TD align="center" width="12%"><%=(String)lineArr.get("lot")%></TD>
             <TD align="center" width="12%"><%=(String)lineArr.get("expdate")%></TD>
             <TD align="right" width="5%"><%=(String)lineArr.get("custpo")%></TD>
             <TD align="right" width="6%"><%=(String)lineArr.get("qty")%></TD>
             <TD align="right" width="9%"><%=(String)lineArr.get("pallet")%></TD>
             <TD align="right" width="6%"><%=(String)lineArr.get("palletid")%></TD>
             <TD align="right" width="6%"><%=(String)lineArr.get("traveler")%></TD>
             <TD align="center" width="12%"><%=yymm%></TD>
             <TD align="right" width="6%"><%=(String)lineArr.get("upby")%></TD>
             <TD align="right" width="6%"><%=iIndex%></TD>
       
           </TR>
       <%}%>
    
    </TABLE>

<%}%>
</form>
</body>
</html>

