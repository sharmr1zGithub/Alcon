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
		alert("Select Delivery No ");
		form1.ITEM.focus();
		return false;
	}
document.form1.HiddenView.value = "Go";
document.form1.action="qc_report.jsp";
document.form1.submit();
}

function onGo1(){

	if(form1.ITEM.value=="" || form1.ITEM.value.length==0)
	{
		alert("Select Delivery No ");
		form1.ITEM.focus();
		return false;
	}
//  alert("onGo1");
document.form1.HiddenView.value = "Go";
document.form1.action='/CibaVisionWms/ExcelGeneratorServlet?action=GenerateExcel';   
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
</script>
<title>QC Report </title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();
OBTravelerDetDAO obDetDAO = new OBTravelerDetDAO();
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
String WHID ="",  ITEM = "",REFNO ="", BATCH ="", BINNO ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="";
String html = "",yymm="";
String trayCnt ="";
String mlcount="";
int mlcounter = 0;
int TotCnt = 0;




String HiddenView="",HiddenAprove="";
ITEM    = strUtils.fString(request.getParameter("ITEM"));
REFNO    = strUtils.fString(request.getParameter("REFNO"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove    = strUtils.fString(request.getParameter("HiddenAprove"));

if(HiddenView.equalsIgnoreCase("Go")){
 try{
       invQryList = obDetDAO.get_Qc_ReportDetails(PLANT,ITEM);
      if(invQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no Delivery No.  summary found to display<h3></td></tr>";
      
      }else{
      trayCnt = obDetDAO.getTotalTraysPerPallet(ITEM);
       mlcount = obDetDAO.getCountofMultiLot(ITEM);
       mlcounter=Integer.parseInt(mlcount);
       
       MLogger.info("mlcount ############## "+mlcounter);
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="qc_report.jsp">
  <INPUT type="hidden" name="MTID" value="<%=MTID%>">
  <INPUT type="hidden" name="HiddenView" value="Go">
  <INPUT type="hidden" name="HiddenAprove" value="">
  <INPUT type="hidden" name="slcount" value="<%=mlcounter%>">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Delivery Report</FONT>
        &nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="95%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

  
          <TR>
       <TH ALIGN="RIGHT" >Reference No :</TH>
       <TD><INPUT name="REFNO" type = "TEXT" value="<%=REFNO%>" size="20"  MAXLENGTH=20>
                      <a href="#" onClick="javascript:popUpWin('listView/OB_Travel_Ref_View.jsp?REFNO='+form1.REFNO.value);"><img src="images/populate.gif" border="0"></a>
                  
       <TH ALIGN="RIGHT" > &nbsp; Delivery No :</TH>
       <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=20>
         <a href="#" onClick="javascript:popUpWin('listView/OB_TravelerView.jsp?ITEM='+form1.ITEM.value+'&REFNO=' +form1.REFNO.value);"><img src="images/populate.gif" border="0"></a>
        <input type="button" value="View" onClick="javascript:return onGo();"/></TD>
        
        <TD><B>TOTAL TRAYS : </B><B><%=trayCnt%></B></TD
		                 
		</TR>
  </TABLE>
  <br>
  
  <% if(mlcounter > 0) {%>
  
   <TABLE WIDTH="95%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <!--<th ><font color="#ffffff" align="center">SINO</th> -->
          <th ><font color="#ffffff" align="center">NO_TRAY</th>
          <td><font color="#ffffff" align="left"><center><STRONG>NO_PART</STRONG></center></td>
          <td><font color="#ffffff" align="left"><center><STRONG>DESCRIPTION</STRONG></center></td>
          <td><font color="#ffffff" align="left"><center><STRONG><STRONG>LOT#</STRONG></STRONG></center></td>
          <td><font color="#ffffff" align="left"><center><STRONG>EXP_DATE</STRONG></center></td>
          <td><font color="#ffffff" align="left"><center><STRONG>PO#</STRONG></center></td>
          <td><font color="#ffffff" align="left"><center><STRONG>QTY</STRONG></center></td>
          <td><font color="#ffffff" align="left"><center><STRONG>PALLET</STRONG></center></td>
          <td><font color="#ffffff" align="left"><center><STRONG>TL_PLT_ID</STRONG></center></td>
          <td><font color="#ffffff" align="left"><center><STRONG>DELIVERY</STRONG></center></td>
          <td><font color="#ffffff" align="left"><center><STRONG>EXPIRY</STRONG></center></td>
          <td><font color="#ffffff" align="left"><center><STRONG>USER</STRONG></center></td>
          <td><font color="#ffffff" align="left"><center><STRONG>TRAY COUNTER</STRONG></center></td>
     </tr>
       <%
          String strayid ="";
          int cntSlno =0;  
          
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
           String sno ="";
            String tray = (String)lineArr.get("DummyTrayID");
            
            int qty = new Integer((String)lineArr.get("qty")).intValue();
            TotCnt = TotCnt + qty; 
               if(strayid.equalsIgnoreCase(tray)){
                sno = new Integer(cntSlno).toString();  
                
               }else
               {
                  cntSlno = cntSlno+1;
                  sno = new Integer(cntSlno).toString();  
               }
             
          yymm=(String)lineArr.get("yy")+(String)lineArr.get("mm");
        
          int iIndex = iCnt + 1;
        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>

      
       <TR bgcolor = "<%=bgcolor%>">
            
               <!--<TD align="center" width="9%"><%=iIndex%></TD>-->
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
              <TD align="right" width="6%"><%=sno%></TD>
            
     
           </TR>
       <%
         strayid =tray;
        }%>
     <TR><TD></TD><TD></TD><TD></TD><TD></TD><TD><B>TOTAL :</B></TD><TD align="right" width="6%"><%=TotCnt%></TD><TD></TD></TR>
    </TABLE>
  
 <% }else if(mlcounter <= 0){%>
 
  <TABLE WIDTH="95%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <!--<th ><font color="#ffffff" align="center">SINO</th> -->
         <th ><font color="#ffffff" align="center">NO_TRAY</th>
         <td><font color="#ffffff" align="left"><center><STRONG>NO_PART</STRONG></center></td>
         <td><font color="#ffffff" align="left"><center><STRONG>DESCRIPTION</STRONG></center></td>
         <td><font color="#ffffff" align="left"><center><STRONG><STRONG>LOT#</STRONG></STRONG></center></td>
         <td><font color="#ffffff" align="left"><center><STRONG>EXP_DATE</STRONG></center></td>
         <td><font color="#ffffff" align="left"><center><STRONG>QTY</STRONG></center></td>
          <td><font color="#ffffff" align="left"><center><STRONG>PALLET</STRONG></center></td>
           <td><font color="#ffffff" align="left"><center><STRONG>TL_PLT_ID</STRONG></center></td>
            <td><font color="#ffffff" align="left"><center>
              <STRONG>DELIVERY</STRONG>
            </center></td>
             <td><font color="#ffffff" align="left"><center><STRONG>EXPIRY</STRONG></center></td>
              <td><font color="#ffffff" align="left"><center><STRONG>USER</STRONG></center></td>
             <td><font color="#ffffff" align="left"><center><STRONG>TRAY COUNTER</STRONG></center></td>
     </tr>
       <%
         String strayid ="";
          int cntSlno =0;  
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          yymm=(String)lineArr.get("yy")+(String)lineArr.get("mm");
                String sno ="";
            String tray = (String)lineArr.get("DummyTrayID");
            int qty = new Integer((String)lineArr.get("qty")).intValue();
            TotCnt = TotCnt + qty; 
            if(strayid.equalsIgnoreCase(tray)){
                sno = new Integer(cntSlno).toString();  
                
               }else
               {
               cntSlno = cntSlno+1;
                  sno = new Integer(cntSlno).toString();  
               }
          int iIndex = iCnt + 1;
        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>

      
       <TR bgcolor = "<%=bgcolor%>">
            
               <!--<TD align="center" width="9%"><%=iIndex%></TD>-->
              <TD align="center" width="9%"><%=(String)lineArr.get("trayid")%></TD>
              <TD align="center" width="4%"><%=(String)lineArr.get("sku")%></TD>
             <TD align="center" width="20%"><%=(String)lineArr.get("sku_desc")%></TD>
             <TD align="center" width="12%"><%=(String)lineArr.get("lot")%></TD>
             <TD align="center" width="12%"><%=(String)lineArr.get("expdate")%></TD>
              <TD align="right" width="6%"><%=(String)lineArr.get("qty")%></TD>
             <TD align="right" width="9%"><%=(String)lineArr.get("pallet")%></TD>
            <TD align="right" width="6%"><%=(String)lineArr.get("palletid")%></TD>
            <TD align="right" width="6%"><%=(String)lineArr.get("traveler")%></TD>
             <TD align="center" width="12%"><%=yymm%></TD>
              <TD align="right" width="6%"><%=(String)lineArr.get("upby")%></TD>
              <TD align="right" width="6%"><%=sno%></TD>
            
     
           </TR>
       <% strayid =tray; }%>
       <TR><TD></TD><TD></TD><TD></TD><TD></TD><TD><B>TOTAL :</B></TD><TD align="right" width="6%"><%=TotCnt%></TD><TD></TD></TR>
   
    </TABLE>
 
 <%}%>
 
    <P>&nbsp;
    </P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <TR>  <input type="button" value="Back" onClick="window.location.href='indexPage.jsp'">&nbsp
  <!--  <input type="button" value="Generate Excel" onClick="window.location.href='Qc_Report_test.jsp?HiddenView=Go&ITEM=<%=ITEM%>'"> -->
    <input maxlength="100"  type="button" value="GenerateExcel" onClick="javascript:return onGo1();"/>
    </P>
      
  </FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>