<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ include file="header.jsp"%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">


function onGo(){
	
   var flag    = "false";
   var sDono     = document.form1.DONO.value;
   var sCustno    = document.form1.CUSTNO.value;
   var sShipDate    = document.form1.SHIPDATE.value;
   if(sDono != null     && sDono != "") { flag = true;}
   if(sCustno != null    && sCustno != "") { flag = true;}
   if(sShipDate != null     && sShipDate != "") { flag = true;}
   if(flag == "false"){ alert("Please define any one search criteria"); return false;}
   document.form1.action = "dohdr_list.jsp?action=Go";
   document.form1.submit();
}

</script>
<title>Shipment Reversal </title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils   = new StrUtils();
//Generator generator = new Generator();
ShipmentUtil spUtil       = new ShipmentUtil();
DateUtils  dateUtil = new DateUtils();
ArrayList soList    = new ArrayList();
//SimpleDateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy");
String action         = strUtils.fString(request.getParameter("action")).trim();
String res    = "";
String dono ="",  custno = "", shipdate ="";
String shipDateToDisplay = "";
String PLANT = (String)session.getAttribute("PLANT");
dono        = strUtils.fString(request.getParameter("DONO"));
custno      = strUtils.fString(request.getParameter("CUSTNO"));
shipdate     = strUtils.fString(request.getParameter("SHIPDATE"));

if(action.equalsIgnoreCase("Go")){
 try{
	 if (shipdate.length() >0){

 // shipDateToDisplay = shipdate;
//	shipDateToDisplay = dateUtil.getDate(shipdate);
	System.out.println("shipdate:::::::::::::::::::::::::::"+shipdate);
	 }
   soList = spUtil.getDoHdrList(PLANT,dono,shipdate,custno,"");
   if (soList.size()==0)
   res = "No Records found for this Criteria";
 }catch(Exception e) {System.out.println("dohdr_list ::Exeption"+e.toString() ); }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" >
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">SHIPMENT REVERSAL</font></TH>
    </TR>
  </TABLE>
<br>
  <TABLE border="0" width="50%" height="15%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
           <TH ALIGN="left" >SHIP DATE : </TH>
           <TD><INPUT name="SHIPDATE" type = "TEXT" value="<%=shipdate%>" size="20"  MAXLENGTH=20></TH><a href="javascript:show_calendar('form1.SHIPDATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a>
    </TR>
    <TR>
          <TH ALIGN="left" >SONO : </TH>
		  
          <TD><INPUT name="DONO" type = "TEXT" value="<%=dono%>" size="20"  MAXLENGTH=20 ></TH>
    </TR>
    <TR>
          <TH ALIGN="left">CUSTOMER ID: </TH>
		 
          <TD><INPUT name="CUSTNO" type = "TEXT" value="<%=custno%>" size="20"  MAXLENGTH=20></TD>
          <td><td><input type="Submit" value="View"  onClick="javascript:return onGo();"></TD>
	
   </TR>
  </TABLE>
  <br>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
        <TH><font color="#ffffff" align="center">#</TH>
         <TH><font color="#ffffff" align="center">SHIP DATE</TH>
         <TH><font color="#ffffff" align="left"><b>CUSTOMER ID</TH>
         <TH><font color="#ffffff" align="left"><b>SONO</TH>
         <TH><font color="#ffffff" align="left"><b>PACKING SLIP NO</TH>
   </TR>
       <%
	   
     
          for (int iCnt =0; iCnt<soList.size(); iCnt++){
           Map map = (Map) soList.get(iCnt);
        //  lineArr = (ArrayList)soList.get(iCnt);
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
          <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=iIndex%></TD>
			 <TD><a href = "do_packslip_details.jsp?DONO=<%=(String)map.get("DONO")%>&PACKNUM=<%=(String)map.get("LBLGROUP")%>"><%=(String)map.get("DELDATE")%></a></TD>
			
             <TD align="center"><%=(String) map.get("CUSTNO")%></TD>
             <TD align="center"><%=(String) map.get("DONO")%></TD>
             <TD align="center"><%=(String) map.get("LBLGROUP")%></TD>
          </TR>
       <%}%>
       <tr><td colspan= 10>&nbsp;</td></tr>
       <tr><td colspan= 10><center><b><%=res%></b></td></tr>
    </TABLE>
  </FORM>
<%@ include file="footer.jsp"%>