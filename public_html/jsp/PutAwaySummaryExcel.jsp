<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import ="com.murho.dao.PoDetDAO"%>
<%@ page import ="com.murho.dao.RecvDetDAO"%>
<%@ page import="java.util.*"%>
<%@ page import="java.math.BigDecimal"%>
<%

      response.setContentType("application/vnd.ms-excel");
     // response.setContentType("application/plain");
      response.setHeader("Content-Disposition", "attachment;filename=PutAwaySummaryExcel.xls");
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
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PONUM</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>TRAVELER</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>MTID</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PALLET</STRONG> </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>SKU</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>LOT NO</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PUTAWAY LOC</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>ORDER QTY</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><b>PUTAWAY QTY</center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><b>PutAway Status</center></td></center></td>
		
	 </tr>
<%
StrUtils strUtils     = new StrUtils();
ReportUtil repUtil       = new ReportUtil();
PutAwayUtil putAwayUtil=new PutAwayUtil ();
ArrayList invQryList  = new ArrayList();
PoDetDAO poDetDAO=new PoDetDAO();
RecvDetDAO recvDetDAO =new RecvDetDAO();
String PLANT       ="",ITEM ="",MTID="",filesstatus="",TRAVELER="",TRAVELER1="",hiddenView="",PONUM="";
String action         = strUtils.fString(request.getParameter("action")).trim();
String html = "",cntRec="false" ;
boolean isupdated=false;

Hashtable ht=new Hashtable();


PLANT            = strUtils.fString(request.getParameter("PLANT"));
ITEM             = strUtils.fString(request.getParameter("ITEM"));
filesstatus      = strUtils.fString(request.getParameter("filestatus"));
TRAVELER         = strUtils.fString(request.getParameter("TRAVELER"));
hiddenView       = strUtils.fString(request.getParameter("HiddenView"));
PONUM            = strUtils.fString(request.getParameter("PONUM"));


MLogger.log("hiddenView ::::::::::::::::::::"+hiddenView);
if(TRAVELER.length() > 0)
{
TRAVELER=TRAVELER.substring(0,TRAVELER.length()-1);

TRAVELER=strUtils.getStringSeparatedByQuots(TRAVELER);
}


if(hiddenView.equalsIgnoreCase("Go"))
{
          hiddenView="";
          invQryList = poDetDAO.getPutAwayDetailsForExcel(PLANT,TRAVELER,filesstatus);
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          MTID=(String)lineArr.get("LNNO");
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
          
       
       %>
          <TR bgcolor = "<%=bgcolor%>">
             <TD align="center" width="12%"><%=iIndex%></TD>
             <TD align="center" width="13%"><%=PONUM%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("TRAVELER")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("MTID")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("PALLET")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("SKU")%></TD>
             <TD align="right" width="13%"><%=(String)lineArr.get("LOT")%></TD>
             <TD align="right" width="13%"><%=(String)lineArr.get("LOC1")%></TD>
             <TD align="right" width="13%"><%=(String)lineArr.get("ORDQTY")%></TD>
             <TD align="right" width="13%"><%=(String)lineArr.get("PUTAWAYQTY")%></TD>
             <TD align="right" width="13%"><%=(String)lineArr.get("PUTAWAYSTATUS")%></TD>
       
           </TR>
       <%}%>
       
    
    
    </TABLE>

<%}%>

  <%   if(invQryList.size()> 0){
       //isupdated=putAwayUtil.UpdateReceiveHdr(TRAVELER);
  }%>

</form>
</body>
</html>
