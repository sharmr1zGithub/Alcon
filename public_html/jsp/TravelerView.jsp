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

function SetChecked(val)
{
dml=document.form1;
len = dml.elements.length;
var i=0;

for( i=0; i<len; i++)
 {
	 dml.elements[i].checked=val;

 }
}
 
function onGo(){

	if(form1.ITEM.value=="" || form1.ITEM.value.length==0)
	{
		alert("Select Delivery No ");
		form1.ITEM.focus();
		return false;
	}
  
document.form1.HiddenAprove.value ="View";
   
document.form1.submit();
}

function onConfirm(){

 document.form1.HiddenConfirm.value ="Confirm"; 

 document.form1.submit();

}

//added show and hide function for duplicate traveler issue #WO0000000202453 -->
function show(){
var div = document.getElementById("hideTR");
div.style.display = 'block';
}
function hide(){
var div = document.getElementById("hideTR");
div.style.display = 'none';
}

function onRemove(){
 var ischeck = false;
 var Traveler ;
 var concatTraveler="";
 
   var i = 0;
   var noofcheckbox=1;
   
   
    var noofcheckbox = document.form1.chkdDoNo.length;
    document.form1.HiddenRemove.value="Remove";
    
    if(form1.chkdDoNo.length == undefined)
    {
            
            if(form1.chkdDoNo.checked)
            {
            document.form1.TRAVELER.value=form1.chkdDoNo.value+"=";
            document.form1.action = "TravelerView.jsp";
            document.form1.submit();
            }
    
    }else
    {
             for (i = 0; i < noofcheckbox; i++ )
              {
               ischeck = document.form1.chkdDoNo[i].checked;
                   if(ischeck)
                    {
                    Traveler=document.form1.chkdDoNo[i].value;
                    concatTraveler=concatTraveler+Traveler+"=";
                    }
    
               }
             
              document.form1.TRAVELER.value=concatTraveler;
              document.form1.action = "TravelerView.jsp";
              document.form1.submit();  
    }
  

}
</script>
<title>Traveler View</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();
InvMstDAO invMstDAO = new InvMstDAO();
OBTravelerDetDAO obTravelDetDAO=new OBTravelerDetDAO();
TravelerSummaryUtil travelerSummaryUtil =new TravelerSummaryUtil();
ArrayList invQryList  = new ArrayList();

SQLRecvDet_DAO _SQLRecvDet = new SQLRecvDet_DAO();

MLogger logger = new MLogger();
boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
boolean confirm=false;
UserTransaction ut=null ;
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("usrHiddenAction")).trim();
String user_id          = session.getAttribute("LOGIN_USER").toString().toUpperCase();
String TRN_DATE = DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
String WHID ="",  ITEM = "", BATCH ="", BINNO ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="",HiddenRemove="";
String html = "";

String chkLot = "", LOT = "",StrLot="";
String TotalString="",TRAVELER="";
  boolean btnConfirm = false;

String HiddenView="Go",HiddenAprove="",HiddenConfirm="";
ITEM    = strUtils.fString(request.getParameter("ITEM"));

//HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove     = strUtils.fString(request.getParameter("HiddenAprove"));
HiddenConfirm    = strUtils.fString(request.getParameter("HiddenConfirm"));
HiddenRemove     = strUtils.fString(request.getParameter("HiddenRemove"));

TRAVELER     = strUtils.fString(request.getParameter("TRAVELER"));

//Added For displaying and clearing LOT restriction message on UI.

if ((String)request.getSession().getAttribute("BlockLot") != null && (String)request.getSession().getAttribute("BlockLot") != "") {
	    StrLot = "<tr><td><B><h3>"+(String)request.getSession().getAttribute("BlockLot")+"</tr></td><B><h3>";}
		session.removeAttribute("BlockLot");

if(HiddenView.equalsIgnoreCase("Go")){
 try{
    
      invQryList = obTravelDetDAO.getTravelerView();
      if(invQryList.size() < 1)
      {
      fieldDesc = "<tr><td><B><h3>Currently no Delivery No. summary found to display<h3></td></tr>";
     
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}

if(HiddenAprove.equalsIgnoreCase("View")){
 try{
    
       invQryList = obTravelDetDAO.getTravelerViewByTraveler(ITEM);
      if(invQryList.size() < 1)
      {
     // fieldDesc = "<tr><td><B><h3>Currently no Delivery No. found to display<h3></td></tr>";
      fieldDesc = "<tr><td><B><h3>Delivery No. Details Removed Successfully<h3></td></tr>";
     
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}
else if(HiddenConfirm.equalsIgnoreCase("Confirm")){
    
	
     try{
    	 
      			btnConfirm = true;
      			confirm = obTravelDetDAO.Call_Proc_Confirm("");
      
      			if(confirm)
      				{
       						fieldDesc = "<tr><td><B><h3>Delivery No. Confirmed Successfully !<h3></td></tr>";  
      				} 
      			else
      				{
	      						fieldDesc = "<tr><td><B><h3> Unable to Confirm <h3></td></tr>";
	      			}
    	
 	}
     
     catch(Exception e) {System.out.println("Exception :getInvList"+e.toString());
  			fieldDesc = "<tr><td><B><h3>"+e.getMessage()+"<h3></td></tr>";}
    
}else if(HiddenRemove.equalsIgnoreCase("Remove")){

     try{
     
       Hashtable ht = null;
       ht = new Hashtable();

       confirm = travelerSummaryUtil.RemoveTraveler(TRAVELER,user_id,TRN_DATE);
      if(confirm)
      {
      
       response.sendRedirect("TravelerView.jsp?HiddenAprove=View&ITEM="+ITEM+"");
       fieldDesc = "<tr><td><B><h3> Delivery No. Details Removed Successfully<h3></td></tr>";
    
      }
      else
      {
      fieldDesc = "<tr><td><B><h3>Error in deleting Delivery No. details <h3></td></tr>";
     
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }

}

%>

<%@ include file="body.jsp"%>

<FORM name="form1" method="post" action="TravelerView.jsp">
  <INPUT type="hidden" name="MTID" value="<%=MTID%>">
  <INPUT type="hidden" name="HiddenView" value="Go">
  <INPUT type="hidden" name="HiddenRemove" value="">
  <INPUT type="hidden" name="HiddenAprove" value="">
  <INPUT type="hidden" name="HiddenConfirm" value="">
  <INPUT type="hidden" name="TRAVELER" value="">
    
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">SO Summary</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

          <TR>
		   <TH ALIGN="RIGHT" >&nbsp;</TH>
                      <TD>&nbsp;</TD>
                      <TH ALIGN="RIGHT" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Delivery No. :</TH>
                      <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=20>
                      <a href="#" onClick="javascript:popUpWin('listView/TravelerViewPopUp.jsp?ITEM='+form1.ITEM.value);"><img src="images/populate.gif" border="0"></a>
                      <input type="button" value="Go" onClick="javascript:return onGo();"/></TD>
		                     
		</TR>

  </TABLE>
  <br>
  <TABLE WIDTH="50%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <th width="3%"><font color="#ffffff" align=="CENTER">CHK</th>
         <td width="8%"><font color="#ffffff" align=="CENTER"><STRONG>DELIVERY NO.</STRONG></td>
   
         <td width="8%"><font color="#ffffff" align=="CENTER"><STRONG>SHIP TO</STRONG></td>
         <td width="8%"><font color="#ffffff" align=="CENTER"><STRONG>QTY</STRONG></td>
        <!-- <td width="3%"><font color="#ffffff" align="left"><center><STRONG>URGENT</STRONG></center></td> -->
	 </tr>
       <%
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          MTID=(String)lineArr.get("LNNO");
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
         
         //TotalString=(String)lineArr.get("TRAVELER")+","+(String)lineArr.get("SO")+","+(String)lineArr.get("RELEASE")+","+(String)lineArr.get("QTY");
         TotalString=(String)lineArr.get("TRAVELER")+","+(String)lineArr.get("QTY");
       %>

       
       <TR bgcolor = "<%=bgcolor%>">
             <!--<TD align="right" width="12%"><%=iIndex%></TD> -->
            <!-- <TD BGCOLOR="#eeeeee" align="CENTER"><font color="black"> <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="chkdDoNo" value="<%=TotalString%>"></font></TD> -->
            <TD BGCOLOR="#eeeeee" align="CENTER"><font color="black"> <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="chkdDoNo" value="<%=TotalString%>"></font></TD>
            
             <TD align=="CENTER" width="8%"><%=(String)lineArr.get("TRAVELER")%></TD>
             <TD align=="CENTER" width="8%"><%=(String)lineArr.get("FIELD11")%></TD>
             <TD align=="CENTER" width="8%"><%=(String)lineArr.get("QTY")%></TD> 
            <!-- <TD BGCOLOR="#eeeeee" align="CENTER"><font color="black"> <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="chkdUrgent" value="<%=TotalString%>"></font></TD> -->
 
         </TR>
       <%}%>
    
    </TABLE>
    <P>&nbsp;
    </P>
    	<TR>
	<a href="javascript:SetChecked(1);">Check&nbsp;All</a> - <a  href="javascript:SetChecked(0);">Clear&nbsp;All</a>
	</tr>

    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

    
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <TR>  <input type="button" value="Back" onClick="window.location.href='indexPage.jsp'">&nbsp
                                          <input type="button" value="Remove" onClick="javascript:return onRemove();"/>
                                          <!-- added one more click event to disable the confirm button till the response comes for duplicate traveler issue #WO0000000202247 -->
                                          <input type="button" value="Confirm" onClick="show();this.disabled='disabled';javascript:return onConfirm();" /></TR>
                                       
    </P>
      
 
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <tr>
      <%=fieldDesc%>
      </tr>
      
       <tr align="center">
      <%=StrLot%>
      </tr>
      <!-- added tr tag to show the progress message #WO0000000202453 -->
      <tr id="hideTR" align="center" >
         	<td align="center">
         	Delivery is in progress. Please wait......
         	<br>
         	<br>
         	<img src="images/spinner_tra.gif">
         	</td>
       </tr> 
    </table>    
    </font>
    <script type="text/javascript">
    	window.onload = hide();
    </script>
    
 </FORM>
<%@ include file="footer.jsp"%>