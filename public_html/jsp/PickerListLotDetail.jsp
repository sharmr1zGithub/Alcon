
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
function onUpdate()
{


  var con = confirm ("Are you sure want to Update the lot status to close ");
   if(con){
  document.form1.LotUpdate.value="Update";
  document.form1.action = "PickerListLotDetail.jsp";
   document.form1.submit();
   }else{
   return false;
   }
   

}

function onDelete(){
  var sFunctionName = document.form1.FUNCTION_NAME.value;
      var sPreFix   = document.form1.PREFIX.value;
     if(sFunctionName == "" || sFunctionName == null) {alert("Please Enter the Function Name"); document.form1.FUNCTION_NAME.focus(); return false; }
     if(sPreFix == "" || sPreFix == null) {alert("Please Enter Prefix"); document.form1.PREFIX.focus(); return false; }
   var con = confirm ("Are you sure want to delete the Table Control Master?");
   if(con){
   document.form1.action  = "view_tblControl.jsp?action=DELETE";
   document.form1.submit();
   }else{
   return false;
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

</script>
<title>Lot Details</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();

PickingUtil pUtil=new PickingUtil();
ArrayList invQryList  = new ArrayList();
MLogger logger = new MLogger();
RecevingUtil recevingUtil=new RecevingUtil();

String PLANT        = (String)session.getAttribute("PLANT");if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String user_id          = session.getAttribute("LOGIN_USER").toString().toUpperCase();


String ITEM="",SKU="",LOT="",USER="",TRAVELER="",PALLET ="",TRN_DATE="",tranTime="";
String HiddenView="",HiddenUpdate="";
String fieldDesc="";


ITEM    = strUtils.fString(request.getParameter("ITEM"));
SKU    = strUtils.fString(request.getParameter("SKU"));
LOT    = strUtils.fString(request.getParameter("LOT"));
USER    = strUtils.fString(request.getParameter("USER"));
TRAVELER    = strUtils.fString(request.getParameter("TRAVELER"));
PALLET    = strUtils.fString(request.getParameter("PALLET"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenUpdate    = strUtils.fString(request.getParameter("LotUpdate"));

TRN_DATE=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
tranTime=DateUtils.Time();

if(HiddenView.equalsIgnoreCase("view")){
 try{
      MLogger.log("HiddenView :::::::::::"+HiddenView);
       invQryList = pUtil.getLotDetail(LOT,TRAVELER,PALLET);
     if(invQryList.size() < 1)
      {
    fieldDesc = "<tr><td><B><h3><centre>" +"Details Not found for lot :"+ LOT +"</centre><h3></td></tr>";   
    
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString());
 
 fieldDesc = "<tr><td><B><h3><centre>" + e.toString() + "!</centre><h3></td></tr>";
 }
}

if(HiddenUpdate.equalsIgnoreCase("Update"))
{
boolean isupdated=false;
try{
Hashtable ht = null;
ht = new Hashtable();


ht.put(MDbConstant.TRAVELER_NUM,TRAVELER);
ht.put(MDbConstant.PALLET,PALLET);
ht.put(MDbConstant.MTID,"");
ht.put(MDbConstant.ITEM,"");
ht.put(MDbConstant.LOT_NUM,LOT);
//ht.put(MDbConstant.RECV_QTY,"0");
ht.put(MDbConstant.LOGIN_USER,user_id);
ht.put(MDbConstant.ReceiveStatus,"");
ht.put(MDbConstant.REMARK,"");
ht.put(MDbConstant.MOVHIS_REF_NUM,"CLOSE_LOT");
ht.put(MDbConstant.MOVEHIS_CR_DATE,TRN_DATE);
ht.put(MDbConstant.CRTIME,tranTime);
ht.put(MDbConstant.MOVHIS_QTY,"0");


isupdated=recevingUtil.UpdateLot(ht);


if(isupdated){
response.sendRedirect("PickerListSummary.jsp?HiddenView=View&ITEM="+TRAVELER+"");
}else
{
 fieldDesc = "<tr><td><B><h3>Unable to Close the Lots <h3></td></tr>";
}
}
 catch(Exception e){ fieldDesc = "<tr><td><B><h3> Picking Status Closed already  <h3></td></tr>"; }
}

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="" >
  
  <INPUT type="hidden" name="HiddenView" value="Go">
  <INPUT type="hidden" name="HiddenAprove" value="">
  <INPUT type="hidden" name="LotUpdate" value="">
  <INPUT type="hidden" name="LOT"      value="<%=LOT%>">
  <INPUT type="hidden" name="TRAVELER" value="<%=TRAVELER%>">
  <INPUT type="hidden" name="PALLET"   value="<%=PALLET%>">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Lot Details</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
   <TABLE WIDTH="100%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	       <tr bgcolor="navy">
         
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>LOT</STRONG></center></td>
         
          <td width="10%"><font color="#ffffff" align="left"><center><STRONG>MTID</STRONG></center></td>
         
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>SKU</STRONG></center></td>
         
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>QTY</STRONG></center></td>
         
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>LOC</STRONG></center></td>
          
           <td width="10%"><font color="#ffffff" align="left"><center><STRONG>STATUS</STRONG></center></td>
         
         
        	
	 </tr>
       <%
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          
          int iIndex = iCnt + 1;
        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>

      
       <TR bgcolor = "<%=bgcolor%>">
           
            
             <TD align="center" width="12%"><%=(String)lineArr.get("lot")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("mtid")%></TD>
              <TD align="center" width="13%"><%=(String)lineArr.get("sku")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("qty")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("loc")%></TD>
             <TD align="center" width="13%"><%=(String)lineArr.get("status")%></TD>
                    
           </TR>
       <%}%>
    
    </TABLE>
    <P>&nbsp;
    </P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <!--   <input type="button" value="Approve" onClick="javascript:return onApprove();"/></TD> -->
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
     <TR>  <input type="button" value="Back" onClick="window.location.href='javascript:history.back()'">&nbsp
          <input type="button" value="CloseLot" onClick="javascript:return onUpdate();"/>         
         
    </P>
      
  </FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>