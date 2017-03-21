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

function onView(){

	if(form1.ITEM.value=="" || form1.ITEM.value.length==0)
	{
		alert("Select Delivery No ");
		form1.ITEM.focus();
		return false;
	}
document.form1.HiddenView.value = "View";
   
document.form1.submit();
}


</script>
<title>TrayID Details</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();
InvMstDAO invMstDAO = new InvMstDAO();
PickingUtil pUtil=new PickingUtil();
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

String ITEM="",fieldDesc="",PALLET="",TRAYID="";

String HiddenView="",HiddenAprove="";
ITEM    = strUtils.fString(request.getParameter("ITEM"));
TRAYID    = strUtils.fString(request.getParameter("TRAYID"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove    = strUtils.fString(request.getParameter("HiddenAprove"));

if(HiddenView.equalsIgnoreCase("View")){
 try{
    
       invQryList = obTravelDetDAO.getTrayDetails(ITEM,TRAYID);
     if(invQryList.size() < 1)
      {
    fieldDesc = "<tr><td><B><h3><centre>" +"Details Not found for Deliver no. :" + ITEM + "</centre><h3></td></tr>";   
    
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString());
 
 fieldDesc = "<tr><td><B><h3><centre>" + e.toString() + "!</centre><h3></td></tr>";
 }
}

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="TrayIDDetails.jsp" >
 
  <INPUT type="hidden" name="HiddenView" value="">
  <INPUT type="hidden" name="HiddenAprove" value="">
  <INPUT type="hidden" name="TRAVELER" value="<%=ITEM%>">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">TrayID Details</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0"  align = "center"  bgcolor="#dddddd">

          <TR>
		   <TH ALIGN="RIGHT" >&nbsp;</TH>
                      <TD>&nbsp;</TD>
                     
                      <TH ALIGN="RIGHT" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Tray ID :</TH>
                      <TD><INPUT name="TRAYID" type = "TEXT" value="<%=TRAYID%>" size="20"  MAXLENGTH=20>
                      
    
                     
		</TR>

 
  </TABLE>
  <br>
  <TABLE WIDTH="100%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	       <tr bgcolor="navy">
         
         
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>MTID</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>SKU</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>LOT</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>QUANTITY</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>STATUS</STRONG></center></td>
          
    
		
	 </tr>
       <%
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
       
          int iIndex = iCnt + 1;
        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>

      
       <TR bgcolor = "<%=bgcolor%>">
           
              
              <TD align="center"> <a href = "MTIDQtyUpdate.jsp?HiddenView=go&ITEM=<%=ITEM%>&TRAYID=<%=TRAYID%>&MTID=<%=(String)lineArr.get("mtid")%>&SKU=<%=(String)lineArr.get("sku")%>&LOT=<%=(String)lineArr.get("lot")%>&QTY=<%=(String)lineArr.get("qty")%>")%><%=(String)lineArr.get("mtid")%></a></TD>
              <TD align="center" width="10%"><%=(String)lineArr.get("sku")%></TD>
              <TD align="center" width="10%"><%=(String)lineArr.get("lot")%></TD>
              <TD align="center" width="10%"><%=(String)lineArr.get("qty")%></TD>
              <TD align="center" width="10%"><%=(String)lineArr.get("traystatus")%></TD>
                   
          
        
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