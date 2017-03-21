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

	if(form1.TRAYID.value=="" || form1.TRAYID.value.length==0)
	{
		alert("Scan Tray ID");
		form1.TRAYID.focus();
		return false;
	}
document.form1.HiddenView.value = "Go";
   
document.form1.submit();
}

function onApprove(){
document.form1.HiddenAprove.value = "Approve";
var MTID    = document.form1.MTID.value;
 document.form1.submit();
}

function onSubmit(MTID){

var ITEM    = document.form1.ITEM.value;
document.form1.ITEM.value;
document.form1.MTID.value=MTID;
document.form1.action = "ReceivingAprroval.jsp";
//alert(MTID);
document.form1.submit();
}

function onPrint(){

 var chkdDoNo =  document.form1.chkdDoNo.value;

 document.form1.HiddenPrint.value="Print";
 document.form1.chkdDoNo.value=chkdDoNo;

 var ischeck = false;
 if( document.form1.chkdDoNo){
  if(document.form1.chkdDoNo.length){
   var i = 0;
   var noofcheckbox = document.form1.chkdDoNo.length;
   for (i = 0; i < noofcheckbox; i++ ) {
    ischeck = document.form1.chkdDoNo[i].checked;
    if(ischeck) break;
   }
  }else {
   ischeck = document.form1.chkdDoNo.checked;
  }
 }
 if(! ischeck){
  alert("please check the checkbox to Confirm!");
  return;
 }else {
  document.form1.action = "ReprintMTID.jsp";
  document.form1.submit();
 }

}
</script>
<title>Reprinting MTID</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();
InvMstDAO invMstDAO = new InvMstDAO();
RecvDetDAO recvdao = new RecvDetDAO();
ArrayList invQryList  = new ArrayList();
MLogger logger = new MLogger();

boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
UserTransaction ut=null ;

String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("usrHiddenAction")).trim();
String user_id          = session.getAttribute("LOGIN_USER").toString().toUpperCase();

String WHID ="",  TRAYID = "", LOT ="", BINNO ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="";
String html = "";

String HiddenView="",HiddenAprove="",HiddenPrint="";
TRAYID    = strUtils.fString(request.getParameter("TRAYID"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenPrint    = strUtils.fString(request.getParameter("HiddenPrint"));

if(HiddenView.equalsIgnoreCase("Go")){
 try{
           invQryList = recvdao.getReceiveDetails(PLANT,TRAYID);
      if(invQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no Details for Tray ID <h3></td></tr>";
     //  fieldDesc=fieldDesc+ITEM;
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}
if(HiddenPrint.equalsIgnoreCase("Print")){
 try{
    String[] chkdDoNo = request.getParameterValues("chkdDoNo");
    if(chkdDoNo.length > 0){

for(int i=0;i<chkdDoNo.length;i++){
 MLogger.info(" Boxes selected :"+chkdDoNo[i]);}
}
       
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="ReprintMTID.jsp" >
  <INPUT type="hidden" name="MTID" value="<%=MTID%>">
  <INPUT type="hidden" name="HiddenView" value="Go">
  <INPUT type="hidden" name="HiddenPrint" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">RE-PRINT MTID</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="60%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

          <TR>
		   <TH ALIGN="RIGHT" >&nbsp;</TH>
                      <TD>&nbsp;</TD>
                      <TH ALIGN="RIGHT" > &nbsp;&nbsp;Scan Tray ID :</TH>
                      <TD><INPUT name="TRAYID" type = "TEXT" value="<%=TRAYID%>" size="20"  MAXLENGTH=20>
                  <!--    <a href="#" onClick="javascript:popUpWin('TravellerPopUp.jsp?ITEM='+form1.ITEM.value);"><img src="images/populate.gif" border="0"></a> -->
                      <input type="button" value="View" onClick="javascript:return onGo();"/></TD>
		
                     
		</TR>

  </TABLE>
  <br>
  <TABLE WIDTH="20%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
       <td width="13%"><font color="#ffffff" align="left"><center><b>SiNo</center></td>
       <td width="13%"><font color="#ffffff" align="left"><center><b>MTID/SiNo</center></td>
     	 </tr>
       <%
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          MTID=(String)lineArr.get("LNNO");
          int iIndex = iCnt + 1;
        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>

       
           <TR bgcolor = "<%=bgcolor%>">
            <TD align="CENTER"><font color="black"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="chkdDoNo" value="<%=iIndex%>"></font></TD>
            <TD align="center" width="13% "><%=(String)lineArr.get("PALLET")%></TD>
       </TR>
       <%}%>
    </TABLE>
    <br>
  <table  cellspacing="0" cellpadding="0" border="0" width="60%" align="center">
<tr>
     <td align="right"><input type="button" value="Print" onClick="javascript:return onPrint();"></td>
</tr>
</table>
  
 <br>
 <br>
 <br>
 <table  cellspacing="0" cellpadding="0" border="0" width="40%" align="center" bgcolor="#dddddd" >
<tr>
     <TH ALIGN="RIGHT" > &nbsp;&nbsp;Scan Lot :</TH>
     <TD align="center"><INPUT name="LOT" type = "TEXT" value="<%=LOT%>" size="20"  MAXLENGTH=20>
    <td align="center"><input type="button" value="Gen MTID" onClick="window.location.href='indexPage.jsp'"></td>
    <td align="center"><input type="button" value="Print MTID" onClick="window.location.href='indexPage.jsp'"></td>
</tr>
</table>   
  <br>  
<table  cellspacing="0" cellpadding="0" border="0" width="60%" align="center">
<tr>
    <td align="center" ><input type="button" value="Back" onClick="window.location.href='indexPage.jsp'"></td>
</tr>
</table>

  
      
  </FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>