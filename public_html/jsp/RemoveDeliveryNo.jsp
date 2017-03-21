<!--
    Created By:Bruhanudeen
    Create date: 20100225
    For:Cibavavision WMS SAP Integration  PhaseII 
    Description:New Page for RemovOutBound DeliveryNo  -->

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


function JobProgress(){
	
if(form1.ITEM.value=="" || form1.ITEM.value.length==0)
	{
		alert("Select Delivery No ");
		form1.ITEM.focus();
		return false;
	}

  document.form1.action = "JobProgress.jsp?HiddenView=JobProgress";


document.form1.submit();
}

function onRemove(){
 if(form1.ITEM.value=="" || form1.ITEM.value.length==0)
	{
		alert("Select Delivery No ");
		form1.ITEM.focus();
		return false;
	}
    var con = confirm ("Are you sure want to Remove the Delivery No ? ");
     if(con) {
  document.form1.HiddenRemove.value="Remove";
  document.form1.action = "RemoveDeliveryNo.jsp";
  document.form1.submit();  
  }else {
         return false;
     }
}

/*function onAssign(){

  var val = 0;
  var ischeck = false;
  var Traveler ;
  var concatTraveler="";
  var i = 0;
  
  var chkdDoNo =  document.form1.chkdDoNo.value;
  var noofcheckbox = document.form1.chkdDoNo.length;
 
   document.form1.HiddenView.value="AssignJob";
 
   for (i = 0; i < noofcheckbox; i++ )
   {
    ischeck = document.form1.chkdDoNo[i].checked;
    if(ischeck)
    {
    Traveler=document.form1.chkdDoNo[i].value;
    concatTraveler=concatTraveler+Traveler+",";
    }
    
    }
    document.form1.TRAVELER.value=concatTraveler;
    document.form1.action = "AssignJob.jsp";
   
    document.form1.submit();  


}*/

function onAssign(){
 var ischeck = false;
 var Traveler ;
 var concatTraveler="";
 
   var i = 0;
   var noofcheckbox=1;
   
    ischeck = document.form1.chkdDoNo.checked;
   
    
    var noofcheckbox = document.form1.chkdDoNo.length;
    document.form1.HiddenView.value="AssignJob";
    
    if(form1.chkdDoNo.length == undefined)
    {
             if(form1.chkdDoNo.checked)
            {
            document.form1.TRAVELER.value=form1.chkdDoNo.value+",";
            document.form1.action = "AssignJob.jsp";
            document.form1.submit();
            }else if(!form1.chkdDoNo.checked)
            {
            alert("check the checkbox");
            }
    
    }else
    {
             for (i = 0; i < noofcheckbox; i++ )
              {
               ischeck = document.form1.chkdDoNo[i].checked;
                   if(ischeck)
                    {
                    Traveler=document.form1.chkdDoNo[i].value;
                    concatTraveler=concatTraveler+Traveler+",";
                    }
                }
              document.form1.TRAVELER.value=concatTraveler;
             
              document.form1.action = "AssignJob.jsp";
              document.form1.submit();  
    }
}

</script>
<title>RemoveDeliveryNo</title>
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
boolean confirm=false;
UserTransaction ut=null ;
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT =CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("usrHiddenAction")).trim();

String TRN_DATE=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
String user_id          = session.getAttribute("LOGIN_USER").toString().toUpperCase();
String WHID ="",  ITEM = "", BATCH ="", BINNO ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="";
String html = "",chkString ="";
String LOT="",USER="",SKU="",REFNO="",res ="";;

String HiddenView="",HiddenAprove="",HiddenRemove;
REFNO    = strUtils.fString(request.getParameter("REFNO"));
ITEM    = strUtils.fString(request.getParameter("ITEM"));
SKU    = strUtils.fString(request.getParameter("SKU"));
LOT    = strUtils.fString(request.getParameter("LOT"));
USER    = strUtils.fString(request.getParameter("USER"));
fieldDesc    = strUtils.fString(request.getParameter("res"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove    = strUtils.fString(request.getParameter("HiddenAprove"));
HiddenRemove    = strUtils.fString(request.getParameter("HiddenRemove"));

if(HiddenView.equalsIgnoreCase("View")){
 try{
    
     invQryList = obTravelDetDAO.getSummaryList(ITEM,SKU,LOT,USER);
     if(invQryList.size() < 1)
      {
    fieldDesc = "<tr><td><B><h3><centre>" +"Details Not found for Delivery No. :" + ITEM + "</centre><h3></td></tr>";   
    
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString());
 
 fieldDesc = "<tr><td><B><h3><centre>" + e.toString() + "!</centre><h3></td></tr>";
 }
}

if(HiddenRemove.equalsIgnoreCase("Remove")){

     try{
     
       Hashtable ht = null;
       ht = new Hashtable();

       confirm = obTravelDetDAO.RemoveTraveler(ITEM,user_id,TRN_DATE);
        if(confirm)
        {
           fieldDesc = "<tr><td><B><h3>Delivery No. Deleted Sucessfully <h3></td></tr>";
       response.sendRedirect("RemoveDeliveryNo.jsp?res="+fieldDesc+"");
        }
        else
        {
        fieldDesc = "<tr><td><B><h3>Error in deleting Delivery details <h3></td></tr>";
       
       }
   }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }

}
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="RemoveDeliveryNo.jsp" >
  <INPUT type="hidden" name="MTID" value="<%=MTID%>">
  <INPUT type="hidden" name="HiddenView" value="">
  <INPUT type="hidden" name="HiddenAprove" value="">
  <INPUT type="hidden" name="HiddenRemove" value="">
  <INPUT type="hidden" name="TRAVELER" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Remove DeliveryNo.</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%" cellspacing="0" cellpadding="0"  align = "center"  bgcolor="#dddddd">

          <TR>
		   <TH ALIGN="RIGHT" >&nbsp;</TH>
                      
                      <TH ALIGN="RIGHT" >Reference No :</TH>
                      <TD><INPUT name="REFNO" type = "TEXT" value="<%=REFNO%>" size="20"  MAXLENGTH=20>
                      <a href="#" onClick="javascript:popUpWin('listView/OB_Travel_Ref_View.jsp?REFNO='+form1.REFNO.value);"><img src="images/populate.gif" border="0"></a>
                   
                      <TH ALIGN="RIGHT" >Delivery No. :</TH>
                      <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=20>
                      <a href="#" onClick="javascript:popUpWin('listView/OB_TravelerView.jsp?ITEM='+form1.ITEM.value+'&REFNO=' +form1.REFNO.value);"><img src="images/populate.gif" border="0"></a>
                      
		
                      <TH ALIGN="RIGHT" >Lot No :</TH>
                      <TD><INPUT name="LOT" type = "TEXT" value="<%=LOT%>" size="20"  MAXLENGTH=20>
                      
    
                     
		</TR>

      <TR>
		   <TH ALIGN="RIGHT" >&nbsp;</TH>
                      
                      <TH ALIGN="RIGHT" >SKU :</TH>
                      <TD><INPUT name="SKU" type = "TEXT" value="<%=SKU%>" size="20"  MAXLENGTH=20>
                      
                      
		
                      <TH ALIGN="RIGHT" >Assigned User :</TH>
                      <TD><INPUT name="USER" type = "TEXT" value="<%=USER%>" size="20"  MAXLENGTH=20>
                      <TD>
                       <input type="button" value="View" onClick="javascript:return onView();"/></TD>
                      </TD>
    
                     
		</TR>
  </TABLE>
  <br>
  <TABLE WIDTH="100%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	       <tr bgcolor="navy">
         
         <th width="3%"><font color="#ffffff" align="center">CHK</th>      
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>DELIVERY NO.</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>PALLET</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>SKU</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>LOT NO</STRONG></center></td>
          <td width="10%"><font color="#ffffff" align="left"><center><STRONG>LOC</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>QTY</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left" nowrap ><center><STRONG>Full Tray</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left" nowrap ><center><STRONG>Partial Qty</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left" nowrap ><center><STRONG>Assigned User</STRONG></center></td>
         
    
		
	 </tr>
       <%
           int i=0;
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          MTID=(String)lineArr.get("LNNO");
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
         String trav = (String)lineArr.get("traveler_id") ; 
          String pallet = (String)lineArr.get("pallet") ; 
          String lotno = (String)lineArr.get("lot") ; 
          String sku = (String)lineArr.get("sku") ; 
         // String sino = (String)lineArr.get("sino") ; 
          String qty =  (String)lineArr.get("qty") ; 
           i=i+Integer.parseInt(((String)qty).trim().toString());
         // String assigneduser =  (String)lineArr.get("assigneduser") ; 
         // chkString  =sino+ "||"+trav+"||"+pallet+"||"+lotno+"||"+sku+"||"+qty;
          chkString  =trav+"||"+pallet+"||"+lotno+"||"+sku+"||"+qty;
      
       %>

      
       <TR bgcolor = "<%=bgcolor%>">
           
              <TD BGCOLOR="#eeeeee" align="CENTER"><font color="black"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="chkdDoNo" value="<%=chkString%>"></font></TD>
              <TD align="center" width="10%"><%=(String)lineArr.get("traveler_id")%></TD>
              <TD align="center" width="10%"><%=(String)lineArr.get("pallet")%></TD>
              <TD align="center" width="10%"><%=(String)lineArr.get("sku")%></TD>
              <TD align="center"> <a href = "PickerListLotDetail.jsp?HiddenView=view&LOT=<%=(String)lineArr.get("lot")%>&TRAVELER=<%=(String)lineArr.get("traveler_id")%>&PALLET=<%=(String)lineArr.get("pallet")%>")%><%=(String)lineArr.get("lot")%></a></TD>
              <TD align="center" width="10%"><%=(String)lineArr.get("loc")%></TD>
              <TD align="center" width="10%"><%=(String)lineArr.get("qty")%></TD>
              <TD align="right" width="10%"><%=(String)lineArr.get("fulltray")%></TD>
              <TD align="right" width="10%"><%=(String)lineArr.get("partialqty")%></TD>
             <TD align="right" width="10%"><%=(String)lineArr.get("assigneduser")%></TD>
      
           </TR>
       <%}%>
      <TR  >
     <TD align="center" width="10%"><font color="white">Test</font></TD>
     <TD align="center" width="10%"><font color="white">Test</font></TD>
     <TD align="center" width="10%"><font color="white">Test</font></TD>
     <TD align="center" width="10%"><font color="white">Test</font></TD>
     <TD align="center" width="10%"><font color="white">Test</font></TD>
      <TD align="right" width="10%"><font   color="Black"><STRONG>Total Qty:</STRONG></font></TD>
      <TD align="center" width="10%"><font   color="Black"><STRONG><%=i%></STRONG></font></TD>
      <TD align="center" width="10%"><font color="white">Test</font></TD>
       <TD align="center" width="10%"><font color="white">Test</font></TD>
        <TD align="center" width="10%"><font color="white">Test</font></TD>
     </TR>
    </TABLE>
    <P>&nbsp;
    </P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <!--   <input type="button" value="Approve" onClick="javascript:return onApprove();"/></TD> -->
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
      	<TR>
	<a href="javascript:SetChecked(1);">Check&nbsp;All</a> - <a  href="javascript:SetChecked(0);">Clear&nbsp;All</a>
	</tr>
     <TR>  <input type="button" value="Back" onClick="window.location.href='indexPage.jsp'">&nbsp
           <input type="button" value="Assign Job" onClick="javascript:return onAssign();">
           <input type="button" value="Job Progress" onClick="javascript:return JobProgress();">
            <input type="button" value="Remove Delivery No" onClick="javascript:return onRemove();"/>
    </P>
      
  </FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>