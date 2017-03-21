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
		alert("Select delivery no. ");
		form1.ITEM.focus();
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
</script>
<title>PutAway Summary</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();
InvMstDAO invMstDAO = new InvMstDAO();
RecvDetDAO recvDetDAO=new RecvDetDAO();
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
String WHID ="",  ITEM = "", BATCH ="", BINNO ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="";
String html = "";
int putawayQty =0,orderQty =0;

String HiddenView="",HiddenAprove="";
ITEM    = strUtils.fString(request.getParameter("ITEM"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove    = strUtils.fString(request.getParameter("HiddenAprove"));

if(HiddenView.equalsIgnoreCase("Go")){
 try{
  
       invQryList = recvDetDAO.getPutAwayDetails(PLANT,ITEM);
      if(invQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no putaway summary found to display<h3></td></tr>";
     //  fieldDesc=fieldDesc+ITEM;
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="PutAwaySummary.jsp" >
  <INPUT type="hidden" name="MTID" value="<%=MTID%>">
  <INPUT type="hidden" name="HiddenView" value="Go">
  <INPUT type="hidden" name="HiddenAprove" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">PutAway Summary</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="90%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

          <TR>
		   <TH ALIGN="RIGHT" >&nbsp;</TH>
                      <TD>&nbsp;</TD>
                      <TH ALIGN="RIGHT" > &nbsp;&nbsp;Inbound Delivery No :</TH>
                      <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=20>
                      <a href="#" onClick="javascript:popUpWin('TravellerPopUp.jsp?ITEM='+form1.ITEM.value);"><img src="images/populate.gif" border="0"></a>
                      <input type="button" value="Go" onClick="javascript:return onGo();"/></TD>
		
                     
		</TR>

  </TABLE>
  <br>
  <TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <th width="12%"><font color="#ffffff" align="center">SNO</th>
         <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>Delivery No.</STRONG>
         </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PALLET</STRONG> </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>SKU</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>LOT NO</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>LOC</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>ORDER QTY</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><b>PUTAWAY QTY</center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><b>PutAway Status</center></td></center></td>
		
	 </tr>
       <%
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          MTID=(String)lineArr.get("LNNO");
          int iIndex = iCnt + 1;
        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
        int orqty=new Integer((String)lineArr.get("ORDQTY")).intValue();
        int putqty=new Integer((String)lineArr.get("PUTAWAYQTY")).intValue();
        orderQty = orderQty+orqty;
        putawayQty = putawayQty +putqty;
     
       %>

       
       <TR bgcolor = "<%=bgcolor%>">
            <TD align="center" width="12%"><%=iIndex%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("TRAVELER")%></TD>
              <TD align="center" width="13%"><%=(String)lineArr.get("PALLET")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("SKU")%></TD>
             <!-- updated by hemant for HPQC ticket #29836
             <TD align="center"> <a href = "PutAwaySummaryDet.jsp?action=view&LOT=<%=(String)lineArr.get("LOT")%>&TRAVELER=<%=(String)lineArr.get("TRAVELER")%>&SKU=<%=(String)lineArr.get("SKU")%>&PALLET=<%=(String)lineArr.get("PALLET")%>&LOC=<%=(String)lineArr.get("LOC1")%>")%><%=(String)lineArr.get("LOT")%></a></TD>
             -->
             <TD align="center"> <a href = "PutAwaySummaryDet.jsp?action=view&LOT=<%=(String)lineArr.get("LOT")%>&TRAVELER=<%=(String)lineArr.get("TRAVELER")%>&SKU=<%=(String)lineArr.get("SKU")%>&PALLET=<%=(String)lineArr.get("PALLET")%>&LOC=<%=(String)lineArr.get("LOC1")%>&PUTAWAYSTATUS=<%=(String)lineArr.get("PUTAWAYSTATUS")%>")%><%=(String)lineArr.get("LOT")%></a></TD>
             <TD align="right" width="13%"><%=(String)lineArr.get("LOC1")%></TD>
             <TD align="right" width="13%"><%=(String)lineArr.get("ORDQTY")%></TD>
             <TD align="right" width="13%"><%=(String)lineArr.get("PUTAWAYQTY")%></TD>
            <TD align="right" width="13%"><%=(String)lineArr.get("PUTAWAYSTATUS")%></TD>
       
       
          
        
           </TR>
       <%}%>
     <TR><TD></TD><TD></TD><TD></TD><TD></TD><TD></TD><TD align="center"><B>Total :</B></TD><TD align="right"><B><%=orderQty%></B><TD align="right"><B><%=putawayQty%></B></TD></TR>
    </TABLE>
    <P>&nbsp;
    </P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <TR>  <input type="button" value="Back" onClick="window.location.href='indexPage.jsp'">&nbsp
    <input type="button" value="Generate" onClick="window.location.href='PutAwaySummaryList.jsp?HiddenView=Go&ITEM=<%=ITEM%>'">
  
    </P>
      
  </FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>