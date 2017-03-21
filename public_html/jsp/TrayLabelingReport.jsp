
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
 subWin = window.open(URL, 'PackSlip', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onGo(){

	if(form1.ITEM.value=="" || form1.ITEM.value.length==0)
	{
		alert("Select Delivery no ");
		form1.ITEM.focus();
		return false;
	}
document.form1.HiddenView.value = "Go";
   
document.form1.submit();
}

/*function onWrite()
{
 
 
       var frmRoot=document.form1;
       var con = confirm ("Process will take few minutes  to download");
       if(con) {
         
         // var DeliveryNo=frmRoot.ITEM
          var DeliveryNo=form1.itemhidden.value; 
          
          var sheet="test";
          frmRoot.action = "/CibaVisionWms/TrayLabelingWriteFileServlet?submit=writefile" + "&WriteFile=" + DeliveryNo +"&SheetName=" + sheet;
          frmRoot.submit();
          return true;
          }else
          { return false;}
   
}*/

</script>
<title>Tray Labeling Report</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();
InvMstDAO invMstDAO = new InvMstDAO();
OBTravelerDetDAO obTravelDetDAO=new OBTravelerDetDAO();

ArrayList invQryList  = new ArrayList();
MLogger logger = new MLogger();
boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
UserTransaction ut=null ;
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("usrHiddenAction")).trim();
String action1         = strUtils.fString(request.getParameter("action")).trim();
String user_id          = session.getAttribute("LOGIN_USER").toString().toUpperCase();
String WHID ="",  ITEM = "", BATCH ="", BINNO ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="";
String html = "",PALLET="",REFNO="",STRITEM="",UPDATESTATUS="";

String HiddenView="",HiddenAprove="";
ITEM    = strUtils.fString(request.getParameter("ITEM"));
PALLET    = strUtils.fString(request.getParameter("PALLET"));
STRITEM=session.getAttribute("LABELSUMMARY").toString();
REFNO=session.getAttribute("LABELREFNO").toString();

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove    = strUtils.fString(request.getParameter("HiddenAprove"));

if(HiddenView.equalsIgnoreCase("Go")){
 try{
   
       invQryList = obTravelDetDAO.TrayLabelingReport(PLANT,ITEM,PALLET);
      if(invQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no delivery no. summary found to display<h3></td></tr>";     
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}
/*else if(action1.equalsIgnoreCase("result")){
    
    fieldDesc=(String)request.getSession().getAttribute("RESULT");
   invQryList = obTravelDetDAO.TrayLabelingReport(PLANT,session.getAttribute("LABELSUMMARY").toString(),session.getAttribute("LABELPALLET").toString());
 //  response.sendRedirect("TrayLabelingReport.jsp?HiddenView=Go&ITEM="+ITEM+"&PALLET="+PALLET+"");
   
}
else if(action1.equalsIgnoreCase("resulterr")){
   
    fieldDesc=(String)request.getSession().getAttribute("RESULTERR");
    invQryList = obTravelDetDAO.TrayLabelingReport(PLANT,session.getAttribute("LABELSUMMARY").toString(),session.getAttribute("LABELPALLET").toString());
   
}*/

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="TrayLabelingSummary.jsp" >
  <INPUT type="hidden" name="MTID" value="<%=MTID%>">
  <INPUT type="hidden" name="HiddenView" value="Go">
  <INPUT type="hidden" name="HiddenAprove" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Tray Labeling Report</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <font face="Times New Roman" size="4">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
     <%=fieldDesc%>
    </TABLE>
    </font>
  <br>

  <TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>DELIVERY NO.</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PALLET</STRONG> </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>TRAY ID</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>MTID</STRONG> </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>LOT</STRONG> </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PICK QTY</STRONG> </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PACK QTY</STRONG> </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>STATUS</STRONG> </center></td>
        
	 </tr>
       <%
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
         
          int iIndex = iCnt + 1;
        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
        PALLET=(String)lineArr.get("pallet");
        UPDATESTATUS =(String)lineArr.get("status");
             
       %>

       
       <TR bgcolor = "<%=bgcolor%>">
             <TD align="center" width="13%"><%=(String)lineArr.get("traveler_id")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("pallet")%></TD>
             <TD align="center"> <a href = "TrayIDDetails.jsp?HiddenView=View&TRAYID=<%=(String)lineArr.get("trayid")%>&ITEM=<%=ITEM%>")%><%=(String)lineArr.get("trayid")%></a></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("mtid")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("lot")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("pickqty")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("traylableqty")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("palletstatus")%></TD>
            
             </TR>
       <%}%>
    
    </TABLE>
    <P>&nbsp;
    </P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <TR>  <input type="button" value="Back" onClick="window.location.href='javascript:history.back()'">&nbsp-->
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <TR>  <input type="button" value="Back"  onClick="window.location.href='TrayLabelingSummary.jsp?HiddenView=Go&PLANT=<%=PLANT%>&ITEM=<%=STRITEM%>&PALLET=<%=PALLET%>&REFNO=<%=REFNO%>'">&nbsp
     <input type="button" value="Confirm" onClick="window.location.href='tray_label_confirm.jsp?HiddenView=Go&TRAVELERNO=<%=ITEM%>&PALLET=<%=PALLET%>'">
   <!-- <input type="button" value="Generate Excel" onClick="window.location.href='TrayLabelingExcelReport.jsp?HiddenView=Go&ITEM=<%=ITEM%>'">-->
     <!--<input type="Button" name="Submit" value="Generate File To SAP"  onclick="javascript:return onWrite();"/>
        <INPUT TYPE=hidden NAME=itemhidden VALUE=value="<%=ITEM%>" >-->
       
      
    </P>
      
  </FORM>
  

<%@ include file="footer.jsp"%>