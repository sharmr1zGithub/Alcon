<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="com.murho.utils.MLogger"%>
<%@ include file="header.jsp"%>

<html>
<head>
<script language="javascript">

function popUpWin(URL) {
 subWin = window.open(URL, 'Summary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onGo(){

	if(form1.SHIP_TO.value=="" || form1.SHIP_TO.value.length==0)
	{
		alert("Select Ship To ");
		form1.SHIP_TO.focus();
		return false;
	}
document.form1.HiddenView.value = "Go";
   
document.form1.submit();
}


</script>
<title>Rules Summary</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();
RulesUtil rulesUtil = new RulesUtil();

ArrayList QryList  = new ArrayList();
MLogger logger = new MLogger();

UserTransaction ut=null ;
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("usrHiddenAction")).trim();
String   SHIP_TO = "",fieldDesc="",actionApprove="";
String html = "";

String HiddenView="",HiddenAprove="";
SHIP_TO    = strUtils.fString(request.getParameter("SHIP_TO"));
if(SHIP_TO.length() <= 0) SHIP_TO  = strUtils.fString(request.getParameter("SHIP_TO1"));
String sDestn    = strUtils.fString(request.getParameter("DESTINATION"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));


if(HiddenView.equalsIgnoreCase("Go")){
 try{
      QryList = rulesUtil.getRulesSummary(SHIP_TO);
      if(QryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no Rules summary found to display <h3></td></tr>";
     //  fieldDesc=fieldDesc+ITEM;
      }
 }catch(Exception e) {System.out.println("Exception :getRulesSummaryList"+e.toString()); }
}

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="rules_summary.jsp" >
  
  <INPUT type="hidden" name="HiddenView" value="Go">
 
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">RULES SUMMARY</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

          <TR>
		   <TH ALIGN="RIGHT" >&nbsp;</TH>
                      <TD>&nbsp;</TD>
                      <TH ALIGN="RIGHT" > &nbsp;&nbsp;Destination :</TH>
                      <TD><INPUT name="SHIP_TO" type = "TEXT" value="<%=SHIP_TO%>" size="20"  MAXLENGTH=20>
                        <INPUT type = "hidden" name="SHIP_TO1" value = "">
                         <INPUT type = "hidden" name="DESTINATION" value = "">
                          <INPUT type = "hidden" name="TRAV_PFX" value = "">
                      <a href="#" onClick="javascript:popUpWin('listView/ship_to_list.jsp?SHIP_TO='+form1.SHIP_TO.value);"><img src="images/populate.gif" border="0"></a>
                      <input type="button" value="Go" onClick="javascript:return onGo();"/></TD>
		
                     
		</TR>

  </TABLE>
  <br>
  <TABLE WIDTH="100%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <th width="12%"><font color="#ffffff" align="center">SNO</th>
           <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>Destination</STRONG>
         </center></td>
           <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>Ship To</STRONG>
         </center></td>
           <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>Prefix</STRONG>
         </center></td>
          <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>Pallet Group</STRONG>
         </center></td>
           <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>Tray group</STRONG>
         </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>Product class</STRONG>
          </center></td>
        
         <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>Type</STRONG>
         </center></td>
          <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>Uom</STRONG>
         </center></td>
         
         <td width="13%"><font color="#ffffff" align="left"><center><b>Qty/Col</center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><b>No Of Col</center></td>
          <td width="13%"><font color="#ffffff" align="left"><center><b>Qty/Tray</center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><b>No of Trays/Layer</center></td>
           <td width="13%"><font color="#ffffff" align="left"><center><b>No Of Layers</center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><b>No of Trays/Pallet</center></td>
        
         </center></td>
		
	 </tr>
       <%
          for (int iCnt =0; iCnt<QryList.size(); iCnt++){
          Map lineArr = (Map) QryList.get(iCnt);
        
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
          <TR bgcolor = "<%=bgcolor%>">
            <TD align="center" width="12%"><%=iIndex%></TD>
            <TD align="center" width="13%"><%=sDestn%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("SHIP_TO")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("PREFIX")%></TD>
            <TD align="center"  width="13%"><%=(String)lineArr.get("PALLET_GRP")%></a></TD>
            <TD align="center" width="13%"><%=(String)lineArr.get("TRAY_GRP_ID")%></TD>
			       <TD align="right" width="11%"><%=(String)lineArr.get("PRD_CLS_ID")%></TD>
             
			      <TD align="right" width="13%"><%=(String)lineArr.get("TYPE")%></TD>
            <TD align="right" width="13%"><%=(String)lineArr.get("UOM")%></TD>
            <TD align="center" width="13%"><%=(String)lineArr.get("QTY_PER_COLUMN")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("NO_OF_COLUMN")%></TD>
            <TD align="center"  width="13%"><%=(String)lineArr.get("QTY_PER_TRAY")%></a></TD>
            <TD align="center" width="13%"><%=(String)lineArr.get("NO_OF_TRAY_PER_LAYER")%></TD>
			       <TD align="right" width="11%"><%=(String)lineArr.get("NO_OF_LAYERS")%></TD>
			      <TD align="right" width="13%"><%=(String)lineArr.get("NO_OF_LAYERS_PER_PALLET")%></TD>
            
           
           
        
           </TR>
       <%}%>
    
    </TABLE>
    <P>&nbsp;
    </P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="button" value="Back" onClick="window.location.href='indexPage.jsp'">&nbsp
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </P>
      
  </FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>