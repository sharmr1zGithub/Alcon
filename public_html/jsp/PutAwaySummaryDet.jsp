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
<title>PutAway Summary Detail</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();
RecvDetDAO RecvDetDAO=new RecvDetDAO();
ArrayList invQryList  = new ArrayList();
MLogger logger = new MLogger();
boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
UserTransaction ut=null ;
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("action")).trim();

String html = "";
String LOT="",TRAVELER="",fieldDesc="",SKU="",PALLET="",LOC="",PUTAWAYSTATUS=""; // PUTAWAYSTATUS variable added by hemant for HPQC ticket # 29836
String HiddenView="",HiddenAprove="";
int putawayQty =0;

LOT    = strUtils.fString(request.getParameter("LOT"));
TRAVELER    = strUtils.fString(request.getParameter("TRAVELER"));
SKU    = strUtils.fString(request.getParameter("SKU"));
PALLET    = strUtils.fString(request.getParameter("PALLET"));
// updated by hemant for HPQC ticket #29836
// LOC    = strUtils.fString(request.getParameter("LOC1"));
LOC    = strUtils.fString(request.getParameter("LOC"));

//variable added by hemant for HPQC ticket # 29836
PUTAWAYSTATUS    = strUtils.fString(request.getParameter("PUTAWAYSTATUS"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove    = strUtils.fString(request.getParameter("HiddenAprove"));

if(action.equalsIgnoreCase("view")){
 try{
      // updated by hemant for HPQC ticket #29836
	 // invQryList = RecvDetDAO.getPutawayDetailByLOT(PLANT,TRAVELER,LOT,PALLET,LOC);
	 invQryList = RecvDetDAO.getPutawayDetailByLOT(PLANT,TRAVELER,LOT,PALLET,LOC,PUTAWAYSTATUS);
      if(invQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no traveller summary found to display <h3></td></tr>";
     //  fieldDesc=fieldDesc+ITEM;
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="PutAwaySummary.jsp">&nbsp;&nbsp;&nbsp; 
  <br/>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">PutAway Summary Detail</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br/>
  <TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH ALIGN="RIGHT">&nbsp;&nbsp;Delivery No :</TH>
      <TD>
        <INPUT name="TRAVELER" type="TEXT" value="<%=TRAVELER%>" size="20" MAXLENGTH="20" readonly/>
        <TH ALIGN="RIGHT">&nbsp;&nbsp;Lot No :</TH>
      </TD>
      <TD>
        <INPUT name="LOT" type="TEXT" value="<%=LOT%>" size="20" MAXLENGTH="20" readonly/>
        <TH ALIGN="RIGHT">&nbsp;&nbsp;SKU :</TH>
      </TD>
      <TD>
        <INPUT name="SKU" type="TEXT" value="<%=SKU%>" size="20" MAXLENGTH="20" readonly/>
      </TD>
    </TR>
  </TABLE>
  <br/>
  <TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding="2" align="center">
    <tr bgcolor="navy">
      <th width="12%">
        <font color="#ffffff" align="center">SNO</font>
      </th>
      <td width="13%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>MTID</STRONG> 
          </center>
        </font>
      </td>
      <td width="13%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>LOT NO</STRONG> 
          </center>
        </font>
      </td>
      <td width="13%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>PUTAWAY QTY</STRONG> 
          </center>
        </font>
      </td>
      <td width="13%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>LOC</STRONG> 
          </center>
        </font>
      </td>
      <td width="13%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>PUTAWAYSTATUS</STRONG> 
          </center>
        </font>
      </td>
    </tr>
    <% 
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
          
          
        int putqty=new Integer((String)lineArr.get("PUTAWAYQTY")).intValue();
        putawayQty = putawayQty +putqty;
       %>
    <TR bgcolor="<%=bgcolor%>">
      <TD align="center" width="12%">
        <%= iIndex%>
      </TD>
      <!-- &lt;TD align=&quot;center&quot;&gt; &lt;a href = &quot;ReceivingUpdate.jsp?action=view&amp;MTID=&lt;%=(String)lineArr.get(&quot;MTID&quot;)%&gt;&amp;LOT=&lt;%=(String)lineArr.get(&quot;LOT&quot;)%&gt;&quot;)%&gt;&lt;%=(String)lineArr.get(&quot;MTID&quot;)%&gt;&lt;/a&gt;&lt;/TD&gt; -->
      <TD align="center" width="13%">
        <%= (String)lineArr.get("MTID")%>
      </TD>
      <TD align="center" width="13%">
        <%= (String)lineArr.get("LOT")%>
      </TD>
      <TD align="right" width="13%">
        <%= (String)lineArr.get("PUTAWAYQTY")%>
      </TD>
      <TD align="right" width="13%">
        <%= (String)lineArr.get("LOC1")%>
      </TD>
      <TD align="right" width="13%">
        <%= (String)lineArr.get("PUTAWAYSTATUS")%>
      </TD>
    </TR>
    <% }%>
    
    <TR><TD></TD><TD></TD><TD align="center"><B>Total :</B></TD><TD align="right"><B><%=putawayQty%></B></TD></TR>
  </TABLE>
  
      <P>&nbsp;
    </P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="button" value="Back" onClick="window.location.href='PutAwaySummary.jsp?HiddenView=Go&ITEM=<%=TRAVELER%>'"/>
  
    </P>
      
  
  
  <font face="Times New Roman">
    <table border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
     
    <%= fieldDesc%>
    </table>
  </font>
  <%@ include file="footer.jsp"%>
</FORM>
