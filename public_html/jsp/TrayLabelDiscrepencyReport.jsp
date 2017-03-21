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
		alert("Select Delivery No. ");
		form1.ITEM.focus();
		return false;
	}
document.form1.HiddenView.value = "Go";
document.form1.action = "TrayLabelDiscrepencyReport.jsp";   
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

function onGo1(){

	if(form1.ITEM.value=="" || form1.ITEM.value.length==0)
	{
		alert("Select Delivery No. ");
		form1.ITEM.focus();
		return false;
	}
//  alert("onGo1");
document.form1.HiddenView.value = "Go";
document.form1.action='/CibaVisionWms/ExcelGeneratorServlet?action=GenerateTrayExcel';   
document.form1.submit();
}
</script>
<title>Tray Labeling Discrepency Report</title>
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
String user_id          = session.getAttribute("LOGIN_USER").toString().toUpperCase();
String WHID ="";
String ITEM = "",REFNO ="", BATCH ="", BINNO ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="";
String html = "";

//new fields for LOT Restriction.

String LOT="",SKU="";

String HiddenView="",HiddenAprove="";
ITEM    = strUtils.fString(request.getParameter("ITEM"));
REFNO    = strUtils.fString(request.getParameter("REFNO"));

//new fields for LOT Restriction.
LOT    = "";//strUtils.fString(request.getParameter("LOT"));
SKU    = "";//strUtils.fString(request.getParameter("SKU"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove    = strUtils.fString(request.getParameter("HiddenAprove"));

if(HiddenView.equalsIgnoreCase("Go")){
 try{
    
/*      Updated for Lot Restriction invQryList = obTravelDetDAO.TrayDescrepencyReport(PLANT,ITEM);*/
      
     //Commented to remove the filter condition of LOT and SKU from discrepancy report under WO0000000897371
     
     invQryList = obTravelDetDAO.TrayDescrepencyReport(PLANT,ITEM,LOT,SKU);

      if(invQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no discrepency records found to list <h3></td></tr>";
     //  fieldDesc=fieldDesc+ITEM;
      }
 }catch(Exception e) {System.out.println("Exception :TrayDescrepencyReport"+e.toString()); }
}

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="TrayLabelDiscrepencyReport.jsp" >
  <INPUT type="hidden" name="MTID" value="<%=MTID%>">
  <INPUT type="hidden" name="HiddenView" value="Go">
  <INPUT type="hidden" name="HiddenAprove" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff"> Discrepancy Report</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="90%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

          <TR>
		   <TH ALIGN="RIGHT" >&nbsp;</TH>
                      <TH ALIGN="RIGHT" >Reference No :&nbsp;</TH>
                      <TD><INPUT name="REFNO" type = "TEXT" value="<%=REFNO%>" size="20"  MAXLENGTH=20>
                      <a href="#" onClick="javascript:popUpWin('listView/OB_Travel_Ref_View.jsp?REFNO='+form1.REFNO.value);"><img src="images/populate.gif" border="0"></a>
         
                      <TH ALIGN="RIGHT" > &nbsp;&nbsp; Delivery No :&nbsp;</TH>
                      <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=20>
                     <a href="#" onClick="javascript:popUpWin('listView/OB_TravelerView.jsp?ITEM='+form1.ITEM.value+'&REFNO=' +form1.REFNO.value);"><img src="images/populate.gif" border="0"></a>
                     
                      <!-- Added two fields for including LOT and SKU in the discrepency report under ticket WO0000000204867 by Ranjana -->
                      <!--  Updated for Removing the filter condition from Report under ticket WO0000000897371 by Ranjana 
                      
                     <TH ALIGN="RIGHT" > &nbsp;&nbsp; LOT No :&nbsp;</TH>
                      <TD><INPUT name="LOT" type = "TEXT" value="<%=LOT%>" size="20"  MAXLENGTH=20>
                      
                      <TH ALIGN="RIGHT" > &nbsp;&nbsp; SKU :&nbsp;</TH>
                      <TD><INPUT name="SKU" type = "TEXT" value="<%=SKU%>" size="20"  MAXLENGTH=20>
                      -->
                      
                      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                       <input type="button" value="Go" onClick="javascript:return onGo();"/></TD>
		
                     
		</TR>

  </TABLE>
  <br>
  <TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <th width="6%"><font color="#ffffff" align="center">SNO</th>
         <td width="10%"><font color="#ffffff" align="left"><center>
           <STRONG>DELIVERY NO.</STRONG>
         </center></td>
        <!-- <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PALLET</STRONG></center></td> -->
         <td width="8%"><font color="#ffffff" align="left"><center><STRONG>SKU</STRONG> </center></td>
         <td width="8%"><font color="#ffffff" align="left"><center><STRONG>LOT</STRONG></center></td>
         <td width="9%"><font color="#ffffff" align="left"><center><STRONG>LOC</STRONG></center></td>
         <td width="7%"><font color="#ffffff" align="left"><center><STRONG>ORD QTY</STRONG></center></td>
         <td width="7%"><font color="#ffffff" align="left"><center><STRONG>PICK QTY</STRONG></center></td>
         <td width="7%"><font color="#ffffff" align="left"><center><STRONG>TRAY QTY</STRONG></center></td>
         <td width="7%"><font color="#ffffff" align="left"><center><STRONG>PICK DIFF</STRONG></center></td>
         <td width="7%"><font color="#ffffff" align="left"><center><STRONG>TRAY DIFF</STRONG></center></td>
          
          <!-- Added two columns for including LOT status and reason in the discrepency report under ticket WO0000000204867 by Ranjana -->
           <td width="9%"><font color="#ffffff" align="left"><center><STRONG>BLOCK STATUS</STRONG></center></td>
           <td width="20%"><font color="#ffffff" align="left" wrap="virtual"><center><STRONG>BLOCK REASON</STRONG></center></td>
          
          <!-- Added the text columns for entering free text under ticket WO0000001221311 by Ranjana --> 
          <td width="20%"><font color="#ffffff" align="left" wrap="TRUE"><center><STRONG>REMARKS</STRONG></center></td>
          
              		
		
	 </tr>
       <%
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          MTID=(String)lineArr.get("LNNO");
          int iIndex = iCnt + 1;
        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>

      
          <TR bgcolor = "<%=bgcolor%>">
             <TD align="center" width="6%"><%=iIndex%></TD>
              <TD align="center" width="10%"><%=(String)lineArr.get("traveler_id")%></TD>
              <!-- <TD align="center" width="13%"><%=(String)lineArr.get("pallet")%></TD>-->
              <TD align="center" width="8%"><%=(String)lineArr.get("sku")%></TD>
             <TD align="center" width="8%"><%=(String)lineArr.get("lot")%></TD>
              <TD align="right" width="9%"><%=(String)lineArr.get("loc")%></TD>
             <TD align="right" width="7%"><%=(String)lineArr.get("qty")%></TD>
               <TD align="right" width="7%"><%=(String)lineArr.get("pickqty")%></TD>
             <TD align="right" width="7%"><%=(String)lineArr.get("traylableqty")%></TD>
             <TD align="right" width="7%"><%=(String)lineArr.get("pickdiff")%></TD>
             <TD align="right" width="7%"><%=(String)lineArr.get("traydiff")%></TD>
             
             <!-- Added two columns for including LOT status and reason in the discrepency report under ticket WO0000000204867 by Ranjana-->
              <TD align="right" width="9%"><%=(String)lineArr.get("BlockStatus")%></TD>
             <TD align="right" width="20%"><%=(String)lineArr.get("BlockReason")%></TD>
             
            <!-- Added the text columns for entering free text under ticket WO0000001221311 by Ranjana --> 
             <TD><input name="remark" row="1" col="10" wrap="true"/></TD>
       
          
        
           </TR>
       <%}%>
   
    </TABLE>
    
    
    <P>&nbsp;
    </P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   <TR>   <input type="button" value="Back" onClick="window.location.href='indexPage.jsp'">&nbsp;
    <input type="button" value="GenerateExcel" onClick="javascript:return onGo1();"/>
    </P>
    
    
      
    
    <!-- Added image for signature under ticket WO0000001221311 by Ranjana650  400 -->
  <TABLE border="o" width="90%" cellspacing="0" cellpadding="0" align="center">
  <tr border="o" ><td border="o" >
   <img src="images/Signature.jpg" align="centre" height="400" width="100%" style="diplay:Block;" /></td></tr>
        </TABLE>
  </FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>