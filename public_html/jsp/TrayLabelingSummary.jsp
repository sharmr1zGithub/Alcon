
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

function onLabeling(){

	if(form1.PALLET.value=="" || form1.PALLET.value.length==0)
	{
		alert("Enter Pallet no ");
		form1.PALLET.focus();
		return false;
	}
 
document.form1.HiddenAprove.value = "Labeling";  
document.form1.submit();
}

function onWrite()
{
      if(form1.ITEM.value=="" || form1.ITEM.value.length==0)
     {
    
      	alert("Select Delivery No ");
      	form1.ITEM.focus();
        return false;
    	}
 
       var frmRoot=document.form1;
       
       var con = confirm ("Process will take few minutes  to download");
     
      // frmRoot.REFNO.value==frmRoot.HiddenRefno.value;
       if(con) 
       {
         var DeliveryNo=form1.itemhidden.value; 
         var DeliveryNo=form1.ITEM.value; 
         var sheet="test";
        frmRoot.action = "/CibaVisionWms/TrayLabelingWriteFileServlet?submit=writefile" + "&WriteFile=" + DeliveryNo +"&SheetName=" + sheet;                  
          frmRoot.submit();
         return true;
      }
      else
     { 
           return false;
       }   
   frmRoot.submit();
      
}

</script>
<title>Tray Labeling Summary</title>
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
String html = "",PALLET="",REFNO="";
String ITEM1="",PALLET1="";

String HiddenView="",HiddenAprove="",HiddenRefno,HiddenItem;
ITEM    = strUtils.fString(request.getParameter("ITEM"));
//ITEM1= strUtils.fString(request.getParameter("itemHidden"));
REFNO    = strUtils.fString(request.getParameter("REFNO"));
PALLET    = strUtils.fString(request.getParameter("PALLET"));
//PALLET1=PALLET;

HiddenItem=ITEM ;
HiddenRefno=REFNO;
boolean flag=false;

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove    = strUtils.fString(request.getParameter("HiddenAprove"));

session.setAttribute("LABELSUMMARY", request.getParameter("ITEM"));

session.setAttribute("LABELPALLET", PALLET);
session.setAttribute("LABELREFNO",REFNO);

if(HiddenView.equalsIgnoreCase("Go")){
 try{
   
       invQryList = obTravelDetDAO.getTrayLabelSummary(PLANT,ITEM,PALLET);
       session.setAttribute("DORECORD",invQryList);
      //  response.sendRedirect("TrayLabelingSummary.jsp?HiddenView=Go&PLANT="+PLANT+"&ITEM="+ITEM+"&PALLET="+PALLET+"");
      if(invQryList.size() < 1)
      {
      flag=true;
       fieldDesc = "<tr><td><B><h3>Currently no tray labeling summary found to list <h3></td></tr>";
     
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}

 else if(action1.equalsIgnoreCase("result")){ 
 
  ITEM    = strUtils.fString(request.getParameter("ITEM"));
  REFNO    = strUtils.fString(request.getParameter("REFNO"));
  PALLET    = strUtils.fString(request.getParameter("PALLET"));
  invQryList = obTravelDetDAO.getTrayLabelSummary(PLANT,ITEM,PALLET);
   fieldDesc=(String)request.getSession().getAttribute("RESULT");
   
}
 else if(action1.equalsIgnoreCase("resulterr")){
 
    ITEM    = strUtils.fString(request.getParameter("ITEM"));
    REFNO    = strUtils.fString(request.getParameter("REFNO"));
    PALLET    = strUtils.fString(request.getParameter("PALLET"));
    invQryList = obTravelDetDAO.getTrayLabelSummary(PLANT,ITEM,PALLET);
    fieldDesc=(String)request.getSession().getAttribute("RESULTERR"); 
}

//*******************************
//below else if is added by jyoti for TIBCO-INC000002484471
//To handling the case if Tray labeling for delivery is not completed.
//*******************************

else if (action1.equalsIgnoreCase("resultcount"))
{
	
	%> <script language="javascript">
	    alert("Cannot generate Confirmation file. Tray Labelling is in Progress!");
     	form1.ITEM.focus();
       
       </script>
   <%
}

if(HiddenAprove.equalsIgnoreCase("Labeling")){
 try{
    response.sendRedirect("TrayLabelingReport.jsp?HiddenView=Go&ITEM="+ITEM+"&PALLET="+PALLET+"");
 }catch(Exception e) {System.out.println("Exception :Labeling Report"+e.toString()); }
}
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
        <FONT color="#ffffff">Tray Labeling Summary</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="90%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

          <TR>
		   <TH ALIGN="RIGHT" >&nbsp;</TH>
                       <TH ALIGN="RIGHT" >Reference No :</TH>
                      <TD><INPUT name="REFNO" type = "TEXT" value="<%=REFNO%>" size="20"  MAXLENGTH=20>
                      <a href="#" onClick="javascript:popUpWin('listView/OB_Travel_Ref_View.jsp?REFNO='+form1.REFNO.value);"><img src="images/populate.gif" border="0"></a>
                  
                      <TH ALIGN="RIGHT" > &nbsp;&nbsp;Delivery No. :</TH>
                      <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=20>
                       <a href="#" onClick="javascript:popUpWin('listView/OB_TravelerView.jsp?ITEM='+form1.ITEM.value+'&REFNO=' +form1.REFNO.value);"><img src="images/populate.gif" border="0"></a>
                      <TH ALIGN="RIGHT" > &nbsp;&nbsp;Pallet :</TH>
                      <TD><INPUT name="PALLET" type = "TEXT" value="<%=PALLET%>" size="20"  MAXLENGTH=20>
                      <input type="button" value="View" onClick="javascript:return onGo();"/></TD>
		
                     
		</TR>

  </TABLE>
  <br>
  <TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
	  <!-- Added by Ranjana for 6.0 enhancement -->
	     <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PALLET S.NO.</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>DELIVERY NO.</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PALLET</STRONG> </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>NO OF TRAYS</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PALLETSTATUS</STRONG> </center></td>
        
	 </tr>
	 
	   <%     
          if (invQryList != null)
          {
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++)
          { 
          Map lineArr = (Map) invQryList.get(iCnt);
          MTID=(String)lineArr.get("LNNO");
          int iIndex = iCnt + 1;
          PALLET=(String)lineArr.get("pallet");
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>

       
       <TR bgcolor = "<%=bgcolor%>">
        <!-- Added by Ranjana for 6.0 enhancement -->
			 <TD align="center" width="13%"><%=(String)lineArr.get("pallet_id")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("traveler_id")%></TD>
             <TD align="center"> <a href = "PalletDetails.jsp?HiddenView=View&PALLET=<%=(String)lineArr.get("pallet")%>&ITEM=<%=(String)lineArr.get("traveler_id")%>")%><%=(String)lineArr.get("pallet")%></a></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("nooftray")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("palletstatus")%></TD>    
        
           </TR>
       <% } }%>
	 
	</TABLE>
      <P>&nbsp;
    </P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <TR>  <input type="button" value="Back" onClick="window.location.href='indexPage.jsp'">&nbsp
    <input type="button" value="Labeling Report" onClick="javascript:return onLabeling();"/>
    
     <input type="Button" name="Submit" value="Generate File To SAP"  onclick="javascript:return onWrite();"/>
      <INPUT TYPE=hidden NAME=itemhidden VALUE=value="<%= request.getParameter("ITEM")%> >
    
    </P>
      
  </FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>   <P>&nbsp;
    