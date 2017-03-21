
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
function onView(){

	if(form1.ITEM.value=="" || form1.ITEM.value.length==0)
	{
		alert("Select traveler no ");
		form1.ITEM.focus();
		return false;
	}
document.form1.HiddenView.value = "View";
   
document.form1.submit();
}

function onApprove(){
document.form1.HiddenAprove.value = "Approve";
var MTID    = document.form1.MTID.value;
 document.form1.submit();
}

</script>
<title>Pick List Summary</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();

PickingUtil pUtil=new PickingUtil();
ArrayList invQryList  = new ArrayList();
MLogger logger = new MLogger();
OBTravelerDetDAO obTravelDetDAO=new OBTravelerDetDAO();


String PLANT        = (String)session.getAttribute("PLANT");if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String user_id          = session.getAttribute("LOGIN_USER").toString().toUpperCase();


String ITEM="",SKU="",LOT="",USER="";
String HiddenView="";
String fieldDesc="";
String  sttime="";
String endtime="";


ITEM    = strUtils.fString(request.getParameter("ITEM"));
SKU    = strUtils.fString(request.getParameter("SKU"));
LOT    = strUtils.fString(request.getParameter("LOT"));
USER    = strUtils.fString(request.getParameter("USER"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));



if(HiddenView.equalsIgnoreCase("JobProgress")){
 try{
      MLogger.log("HiddenView :::::::::::"+HiddenView);
      invQryList = obTravelDetDAO.getJobProgressList(ITEM,SKU,LOT,USER);
     if(invQryList.size() < 1)
      {
    fieldDesc = "<tr><td><B><h3><centre>" +"Details Not found for Delivery No. :" + ITEM + "</centre><h3></td></tr>";   
    
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString());
 
 fieldDesc = "<tr><td><B><h3><centre>" + e.toString() + "!</centre><h3></td></tr>";
 }
}

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="PickerListSummary.jsp" >
  
  <INPUT type="hidden" name="HiddenView" value="Go">
  <INPUT type="hidden" name="HiddenAprove" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Job Progress</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
   <TABLE WIDTH="100%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	       <tr bgcolor="navy">
         
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>DELIVERY NO.</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>USER</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>START TIME</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>LAST PICK TIME</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>TOTAL QTY</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>PICK QTY</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>% PACKING</STRONG></center></td>
        
        
         
      
         
        	
	 </tr>
       <%
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          
          sttime=(String)lineArr.get("sttime");
          endtime=(String)lineArr.get("endtime");
          
         /* if(sttime.length() > 4)
          {
          sttime=sttime.substring(0,2)+":"+sttime.substring(2,4)+":"+sttime.substring(4,6);
          }else
          {
          sttime="";
          }
          
          if(endtime.length() > 4)
          {
             endtime=endtime.substring(0,2)+":"+endtime.substring(2,4)+":"+endtime.substring(4,6);
          }else
          {
          endtime="";
          }
      */
          int iIndex = iCnt + 1;
        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>

      
       <TR bgcolor = "<%=bgcolor%>">
           
            
             <TD align="center" width="12%"><%=(String)lineArr.get("traveler_id")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("assigneduser")%></TD>
              <TD align="center" width="13%"><%=sttime%></TD>
             <TD align="center" width="13%"><%= endtime%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("qty")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("pickqty")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("picked")%></TD>
           
       
           </TR>
       <%}%>
    
    </TABLE>
    <P>&nbsp;
    </P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <!--   <input type="button" value="Approve" onClick="javascript:return onApprove();"/></TD> -->
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
      <TR>  <input type="button" value="Back" onClick="window.location.href='javascript:history.back()'">&nbsp
          
    </P>
      
  </FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>