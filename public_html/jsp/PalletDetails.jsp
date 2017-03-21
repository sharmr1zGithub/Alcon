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






</script>
<title>Pallet Details</title>
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

String ITEM="",fieldDesc="",PALLET="";

String HiddenView="",HiddenAprove="";
ITEM    = strUtils.fString(request.getParameter("ITEM"));
PALLET    = strUtils.fString(request.getParameter("PALLET"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove    = strUtils.fString(request.getParameter("HiddenAprove"));

if(HiddenView.equalsIgnoreCase("View")){
 try{
    
       invQryList = obTravelDetDAO.getPalletDetails(ITEM,PALLET);
     if(invQryList.size() < 1)
      {
    fieldDesc = "<tr><td><B><h3><centre>" +"Details Not found for Traveler :" + ITEM + "</centre><h3></td></tr>";   
    
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString());
 
 fieldDesc = "<tr><td><B><h3><centre>" + e.toString() + "!</centre><h3></td></tr>";
 }
}

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="PickerListSummary.jsp" >
 
  <INPUT type="hidden" name="HiddenView" value="">
  <INPUT type="hidden" name="HiddenAprove" value="">
  <INPUT type="hidden" name="TRAVELER" value="<%=ITEM%>">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Pallet Details</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0"  align = "center"  bgcolor="#dddddd">

          <TR>
		   <TH ALIGN="RIGHT" >&nbsp;</TH>
                      <TD>&nbsp;</TD>
                     
                      <TH ALIGN="RIGHT" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Pallet :</TH>
                      <TD><INPUT name="PALLET" type = "TEXT" value="<%=PALLET%>" size="20"  MAXLENGTH=20>
                      
    
                     
		</TR>

 
  </TABLE>
  <br>
  <TABLE WIDTH="100%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	       <tr bgcolor="navy">
         
       <!-- <th width="3%"><font color="#ffffff" align="center">CHK</th>   -->   
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>PALLET</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>TRAY ID</STRONG></center></td>
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
           
             <!-- <TD BGCOLOR="#eeeeee" align="CENTER"><font color="black"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="chkdDoNo" value="<%=(String)lineArr.get("pallet")%>"></font></TD>-->
              <TD align="center" width="10%"><%=(String)lineArr.get("pallet")%></TD>
              <TD align="center"> <a href = "TrayIDDetails.jsp?HiddenView=View&TRAYID=<%=(String)lineArr.get("trayid")%>&ITEM=<%=ITEM%>")%><%=(String)lineArr.get("trayid")%></a></TD>
              <TD align="center" width="10%"><%=(String)lineArr.get("qty")%></TD>
              <TD align="center" width="10%"><%=(String)lineArr.get("palletstatus")%></TD>
                   
          
        
           </TR>
       <%}%>
    
    </TABLE>
    <P>&nbsp;
    </P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <!--   <input type="button" value="Approve" onClick="javascript:return onApprove();"/></TD> -->
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
     <TR>  <input type="button" value="Back" onClick="window.location.href='javascript:history.back()'">&nbsp
        <!--  <input type="button" value="Print Shipping Mart" onClick="javascript:return onAssign();">
          
    </P>
      
  </FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>