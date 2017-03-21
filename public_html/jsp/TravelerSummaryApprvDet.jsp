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
document.form1.action = "TravellerSummaryApproval.jsp";
//alert(MTID);
document.form1.submit();
}
</script>
<title>Delivery No. Summary</title>
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
String LOT="",TRAVELER="",fieldDesc="",SKU="";
String HiddenView="",HiddenAprove="";
int orderQty =0;
int recvQty =0;

LOT    = strUtils.fString(request.getParameter("LOT"));
TRAVELER    = strUtils.fString(request.getParameter("TRAVELER"));
SKU    = strUtils.fString(request.getParameter("SKU"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove    = strUtils.fString(request.getParameter("HiddenAprove"));

if(action.equalsIgnoreCase("view")){
 try{
      invQryList = RecvDetDAO.getLotDetailByMTID(PLANT,TRAVELER,LOT);
      
      if(invQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no Delivery No. summary found to display <h3></td></tr>";
     //  fieldDesc=fieldDesc+ITEM;
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="TravellerSummaryApproval.jsp" >

  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Delivery No Summary</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

          <TR>
		     <TH ALIGN="RIGHT" > &nbsp;&nbsp;Delivery No. :</TH>
         <TD><INPUT name="TRAVELER" type = "TEXT" value="<%=TRAVELER%>" size="20"  MAXLENGTH=20 readonly >
      
      
         <TH ALIGN="RIGHT" > &nbsp;&nbsp;Lot No :</TH>
         <TD><INPUT name="LOT" type = "TEXT" value="<%=LOT%>" size="20"  MAXLENGTH=20 readonly >
      
      
         <TH ALIGN="RIGHT" > &nbsp;&nbsp;SKU  :</TH>
         <TD><INPUT name="SKU" type = "TEXT" value="<%=SKU%>" size="20"  MAXLENGTH=20 readonly >
                     
                      
		
                     
		</TR>

  </TABLE>
  <br>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <th width="12%"><font color="#ffffff" align="center">SNO</th>
           <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>MTID</STRONG>
         </center></td>
          <td width="13%"><font color="#ffffff" align="left"><center>
          <STRONG>LOT NO</STRONG>
         </center></td>
           <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>ORD QTY</STRONG>
         </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>RECV QTY</STRONG>
          </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>STATUS</STRONG>
         </center></td>
        	
	 </tr>
       <%
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
           int orqty=new Integer((String)lineArr.get("ORDQTY")).intValue();
        int rcvqty=new Integer((String)lineArr.get("RECVQTY")).intValue();
          orderQty = orderQty+orqty;
        recvQty = recvQty +rcvqty;
       %>
          <TR bgcolor = "<%=bgcolor%>">
            <TD align="center" width="12%"><%=iIndex%></TD>
             <TD align="center"> <a href = "TravelerUpdate.jsp?action=view&MTID=<%=(String)lineArr.get("MTID")%>&LOT=<%=(String)lineArr.get("LOT")%>")%><%=(String)lineArr.get("MTID")%></a></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("LOT")%></TD>
             <TD align="right" width="13%"><%=(String)lineArr.get("ORDQTY")%></TD>
             <TD align="right" width="13%"><%=(String)lineArr.get("RECVQTY")%></TD>
             <TD align="right" width="13%"><%=(String)lineArr.get("RECEIVESTATUS")%></TD>
           
        
           </TR>
       <%}%>
     <TR><TD></TD><TD></TD><TD align="center" ><B>Total :</B></TD><TD align="right"><B><%=orderQty%></B><TD align="right"><B><%=recvQty%></B></TD></TR>
    </TABLE>
    <P>&nbsp;
    </P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <!--   <input type="button" value="Approve" onClick="javascript:return onApprove();"/></TD> -->
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <TR>  <input type="button" value="Back" onClick="window.location.href='TravelerSummaryApprovalDelNoDetail.jsp?HiddenView=Go&ITEM=<%=TRAVELER%>'"> &nbsp
    </P>
      
  </FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>