<!--
   Created By:Bruhanudeen
   Create date: 20100305
   For:Cibavavision WMS SAP Integration  PhaseII 
   Description:New Page for  PrintShipmark based on DeliveryNo and Pallet -->

<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="com.murho.utils.MLogger"%>
<%@ include file="header.jsp"%>
<%@ page import="java.awt.print.*"%>

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

function onCheckrdxBox()
{
  var noofcheckbox=1; 
  var noofcheckbox =document.form1.rdbox.length;
  //alert(noofcheckbox);
  for (i = 0; i < noofcheckbox; i++ )
   {
     ischeck = document.form1.rdbox[i].checked;
     ischekcpallet=document.form1.rdpallet[i].checked;
     if(ischeck)
     {
        
       document.form1.rdpallet[i].checked=false;
    
     }
  
     
   }
}

function onCheckrdxPallet()
{
  var noofcheckbox=1; 
  var noofcheckbox =document.form1.rdpallet.length;
  //alert(noofcheckbox);
  for (i = 0; i < noofcheckbox; i++ )
   {
     ischeck = document.form1.rdpallet[i].checked;
     if(ischeck)
     {
        
       document.form1.rdbox[i].checked=false;
   
     }
   }
}

 
function onPrint(){
 var ischeck = false;
 var ischeckbox = false;
 var ischeckpallet=false;
 var Traveler ;
 var concatTraveler="";
 var val;

 var i = 0;
 var noofcheckbox=1;  
    
 var noofcheckbox =document.form1.chkdDoNo.length;
 document.form1.HiddenPrint.value="Print";
     
   if(form1.chkdDoNo.length == undefined)
    {       
        if(form1.chkdDoNo.checked)
         {
            ischeckbox=document.form1.rdbox.checked;
            ischekcpallet=document.form1.rdpallet.checked;
            if(ischeckbox)
            {
              
              document.form1.TRAVELER.value=form1.chkdDoNo.value+","+"Box"+"=";
              
            }
           else 
             {
                
                   Traveler=document.form1.chkdDoNo[i].value+","+"Pallet";
                   concatTraveler=concatTraveler+Traveler+"=";
               
              }
           
           
            document.form1.action = "PrintShippingMark.jsp";
            document.form1.submit();
           
          }
    }else
    {
             for (i = 0; i < noofcheckbox; i++ )
             {
           
               ischeck = document.form1.chkdDoNo[i].checked;
               ischeckbox=document.form1.rdbox[i].checked;
               ischekcpallet=document.form1.rdpallet[i].checked;
              
              
                   if(ischeck)
                    {
                        if(ischeckbox)
                        {
                          
                           Traveler=document.form1.chkdDoNo[i].value+","+"Box";
                           concatTraveler=concatTraveler+Traveler+"=";
                          
                        }
                        else 
                        {
                          
                             Traveler=document.form1.chkdDoNo[i].value+","+"Pallet";
                             concatTraveler=concatTraveler+Traveler+"=";
                         
                      
                        }
                   }
              
              document.form1.TRAVELER.value=concatTraveler;
              document.form1.action = "PrintShippingMark.jsp";
              document.form1.submit();  
   }
  
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

/*function onSubmit(MTID){

var ITEM    = document.form1.ITEM.value;
document.form1.ITEM.value;
document.form1.MTID.value=MTID;
document.form1.action = "PrintShippingMark.jsp";
//alert(MTID);
document.form1.submit();
}*/

</script>
<title>Print Shipping Mark</title>
</head>

<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();
InvMstDAO invMstDAO = new InvMstDAO();
OBTravelerDetDAO obTravelDetDAO=new OBTravelerDetDAO();
String TotalString="";
ShipDetDAO _ShipDetDAO=new ShipDetDAO();
ArrayList invQryList  = new ArrayList();
MLogger logger = new MLogger();
String TRN_DATE=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
UserTransaction ut=null ;
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("usrHiddenAction")).trim();
String user_id          = session.getAttribute("LOGIN_USER").toString().toUpperCase();
String WHID ="",  ITEM = "", BATCH ="", BINNO ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="";
String html = "",PALLET="",REFNO="";

String HiddenView="",HiddenAprove="",HiddenPrint="",TRAVELER="";
ITEM    = strUtils.fString(request.getParameter("ITEM"));
REFNO    = strUtils.fString(request.getParameter("REFNO"));
PALLET    = strUtils.fString(request.getParameter("PALLET"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove    = strUtils.fString(request.getParameter("HiddenAprove"));
HiddenPrint   = strUtils.fString(request.getParameter("HiddenPrint"));
TRAVELER     = strUtils.fString(request.getParameter("TRAVELER"));

session.setAttribute("LABELSUMMARY", ITEM );
session.setAttribute("LABELPALLET", PALLET);
session.setAttribute("LABELREFNO",REFNO);

if(HiddenView.equalsIgnoreCase("Go")){
 try{
   
       invQryList = obTravelDetDAO.getTrayLabelSummary(PLANT,ITEM,PALLET);
      //  response.sendRedirect("TrayLabelingSummary.jsp?HiddenView=Go&PLANT="+PLANT+"&ITEM="+ITEM+"&PALLET="+PALLET+"");
      if(invQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no tray labeling summary found to list <h3></td></tr>";
     
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}

/*if(HiddenAprove.equalsIgnoreCase("Labeling")){
 try{
    response.sendRedirect("TrayLabelingReport.jsp?HiddenView=Go&ITEM="+ITEM+"&PALLET="+PALLET+"");
 }catch(Exception e) {System.out.println("Exception :Labeling Report"+e.toString()); }
}*/

if(HiddenPrint.equalsIgnoreCase("Print")){
// try{
    //out.println(iIndex);
    _ShipDetDAO.GetPrintShippingMarkDetail(TRAVELER,user_id,TRN_DATE,invQryList.size());
   
//}catch(Exception e) { }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="PrintShippingMark.jsp" >
  <INPUT type="hidden" name="MTID" value="<%=MTID%>">
  <INPUT type="hidden" name="HiddenView" value="Go">
  <INPUT type="hidden" name="HiddenAprove" value="">
   <INPUT type="hidden" name="HiddenPrint" value="">
    <INPUT type="hidden" name="TRAVELER" value=""/>
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Print Shipping Mark</FONT>&nbsp;
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
                      <!--<TH ALIGN="RIGHT" > &nbsp;&nbsp;Pallet :</TH>-->
                      <TD><!--<INPUT name="PALLET" type = "TEXT" value="<%=PALLET%>" size="20"  MAXLENGTH=20>-->
                      <input type="button" value="View" onClick="javascript:return onGo();"/></TD>
		                     
		</TR>

  </TABLE>
  <br>
  <TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
        <td width="6%"><font color="#ffffff" align="left"><center><STRONG>CHK</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>DELIVERY NO.</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PALLET</STRONG> </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>NO OF TRAYS</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG></STRONG> </center></td>
        
	 </tr>
       <%
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          MTID=(String)lineArr.get("LNNO");
         int iIndex = iCnt + 1;
          PALLET=(String)lineArr.get("pallet");
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
          TotalString=(String)lineArr.get("traveler_id")+","+(String)lineArr.get("pallet")+","+iIndex;
         
       %>

       
       <TR bgcolor = "<%=bgcolor%>">
        <TD BGCOLOR="#eeeeee" width="4%" align="CENTER">
        <font color="black">
       
          <INPUT name="chkdDoNo" Type="Checkbox" style="border:0;background=#dddddd"  value="<%=TotalString%>"/>
        </font>
      </TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("traveler_id")%></TD>
             <TD align="center"> <a href = "PalletDetails.jsp?HiddenView=View&PALLET=<%=(String)lineArr.get("pallet")%>&ITEM=<%=(String)lineArr.get("traveler_id")%>")%><%=(String)lineArr.get("pallet")%></a></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("nooftray")%></TD>
            <!-- <TD align="center" width="13%"><%=(String)lineArr.get("palletstatus")%></TD>-->
           <TD align="center" width="13%">
              <STRONG>
              Box <INPUT name="rdbox"  style="border:0;background=#dddddd" Type="Checkbox" onClick="onCheckrdxBox();" value=Box> &nbsp;&nbsp;
              Pallet   <INPUT name="rdpallet"  style="border:0;background=#dddddd"   Type="Checkbox"  onClick="onCheckrdxPallet();" value=Value>
              </STRONG>
             <!--onClick="onCheckrdxPallet();"-->
             <!--onClick="onCheckrdxBox();"-->
              </TD>
                   
           </TR>
       <%}%>
     <P>&nbsp;</P>
 
    </TABLE>
     
    <P>&nbsp;
    </P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <TR>  <input type="button" value="Back" onClick="window.location.href='indexPage.jsp'">&nbsp
   <!-- <input type="button" value="Labeling Report" onClick="javascript:return onLabeling();"/><!--onClick="window.location.href='TrayLabelingReport.jsp?HiddenView=Go&ITEM=<%=ITEM%>&PALLET=<%=PALLET%>'"-->
    <input type="button" value="Print" onClick="onPrint();">
     <!--<A HREF="javascript:window.print()">Click to Print This Page</A>-->
    </P>

    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
      
    </table>
    <!--<table  border="0" cellspacing="1" cellpadding="2">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     <TR>
        <td><a href="javascript:SetChecked(1);">Check&nbsp;All</a></td> 
        <td><a href="javascript:SetChecked(0);">Clear&nbsp;All</a></td> 
     </TR>-->
  
    </font>
 <%@ include file="footer.jsp"%> 
<!--</FORM>-->



 