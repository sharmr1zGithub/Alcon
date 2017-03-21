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
document.form1.HiddenView.value = "Go";
   
document.form1.submit();
}

function onUpdate()
{

 var qty=document.form1.QTY.value;
 
 if(qty < 0 )
 {
 alert("Enter Valid Quantity ")
 }
  
   var IsNumber=true;
   var Char;


if(form1.QTY.value=="" || form1.QTY.value.length==0)
	{
		alert("Please enter quantity ");
		form1.QTY.focus();
    return false;
 
 	}
 else
  document.form1.action = "MTIDQtyUpdate.jsp?action=Update";
  document.form1.submit();
  return true;

}


</script>
<title>MTID Quantity Update</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%

StrUtils strUtils     = new StrUtils();
RecevingUtil recevingUtil=new RecevingUtil();
RecvDetDAO dao= new RecvDetDAO();
TrayLabelingUtil trayLabelingUtil=new TrayLabelingUtil();
ArrayList invQryList  = new ArrayList();
OBTravelerDetDAO obTravelDetDAO=new OBTravelerDetDAO();
MLogger logger = new MLogger();

String WHID ="",  ITEM = "", BATCH ="", BINNO ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="";
String html = "";
String TRAVELER_NO="",PART="",DESCRIPTION="",LOC="",LOT="",PLT="",REMARK="",RECVQTY="",TRAYID="",SKU="";
String PART01="",LOT01="",QTY01="";
String HiddenView="",HiddenAprove="";

boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
UserTransaction ut=null ;


String PLANT          = (String)session.getAttribute("PLANT");if(PLANT == null) PLANT =CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("action")).trim();

String user_id        = session.getAttribute("LOGIN_USER").toString().toUpperCase();
String TRN_DATE=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
String time=DateUtils.Time();

ITEM                  = strUtils.fString(request.getParameter("ITEM"));
TRAYID                  = strUtils.fString(request.getParameter("TRAYID"));
MTID                  = strUtils.fString(request.getParameter("MTID"));
SKU                  = strUtils.fString(request.getParameter("SKU"));
LOT                  = strUtils.fString(request.getParameter("LOT"));
QTY                  = strUtils.fString(request.getParameter("QTY"));



HiddenView            = strUtils.fString(request.getParameter("HiddenView"));

MLogger.log("TRAVELER_NO :"+ITEM);

if(action.equalsIgnoreCase("Update"))
{
boolean isupdated=false;
Hashtable ht = null;
ht = new Hashtable();


ht.put(MDbConstant.TRAVELER_NUM,ITEM);
ht.put(MDbConstant.TRAYID,TRAYID);
ht.put(MDbConstant.MTID,MTID);
ht.put(MDbConstant.ITEM,SKU);
ht.put(MDbConstant.LOT_NUM,LOT);
ht.put(MDbConstant.QTY,QTY);
ht.put(MDbConstant.LOGIN_USER,user_id);
ht.put(MDbConstant.ReceiveStatus,"");
ht.put(MDbConstant.MOVHIS_REF_NUM,"TRAYQTY-UPDATE");
ht.put(MDbConstant.MOVEHIS_CR_DATE,TRN_DATE);
ht.put(MDbConstant.CRTIME,time);

MLogger.info("TRAVELER_NUM "+ht.get("Traveler"));
MLogger.info("TRAYID "+ht.get("TRAYID"));
MLogger.info("MTID "+ht.get("MTID"));

MLogger.info("LOT "+ht.get("LOT"));
MLogger.info("QTY "+ht.get("QTY"));
MLogger.info("CRBY "+ht.get("CRBY"));

MLogger.info("ITEM "+ht.get("ITEM"));
MLogger.info("MOVTID "+ht.get("MOVTID"));
MLogger.info("CRAT "+ht.get("CRAT"));

isupdated=trayLabelingUtil.UpdateMTIDQty(ht);

 /*  if(isupdated)
      {
        isupdated=processMoveHis(ht);
      }*/

 


if(isupdated){
//fieldDesc = "<tr><td><B><h3>MTID updated successfully<h3></td></tr>";
response.sendRedirect("TrayIDDetails.jsp?HiddenView=View&ITEM="+ITEM+"&TRAYID="+TRAYID+"");
}else
{
 fieldDesc = "<tr><td><B><h3>Unable to update MTID details<h3></td></tr>";
}
 
}







%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="TravellerSummary.jsp" >
  <INPUT type="hidden" name="MTID" value="<%=MTID%>">
   <INPUT type="hidden" name="ITEM" value="<%=ITEM%>">
  <INPUT type="hidden" name="HiddenView" value="Go">
  <INPUT type="hidden" name="HiddenAprove" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">MTID Quantity Update</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br><br><br><br>
  <table  cellspacing="5" cellpadding="0" border="0" width="60%" align="center" BGCOLOR="#dddddd">
<tr>
    <td>Tray ID :</td>
    <td><INPUT name="TRAYID" type = "TEXT"  value="<%=TRAYID%>" size="20"  MAXLENGTH=20  readonly ></td>
    
</tr>

<tr>
    <td>MTID:</td>
    <td><INPUT name="PLT" type = "TEXT"  value="<%=MTID%>" size="20"  MAXLENGTH=20 readonly ></td>
   
</tr>

<tr>
    <td>SKU</td>
    <td><INPUT name="SKU" type = "TEXT"  value="<%=SKU%>" size="20"  MAXLENGTH=20 readonly ></td>
  
</tr>

<tr>
    <td>Lot No :</td>
    <td><INPUT name="LOT" type = "TEXT"  value="<%=LOT%>" size="20"  MAXLENGTH=20 readonly ></td>
    
</tr>

<tr>
    <td>Quantity :</td>
    <td><INPUT name="QTY" type = "TEXT"  value="<%=QTY%>" size="20"  MAXLENGTH=20  ></td>
    
</tr>


</table>
<br><br>
<table width="60%"  align="center" >
<tr cellspacing="10" border="4" align="center" >
<td>
<input type="button" value="Back" onClick="window.location.href='TrayLabelingSummary.jsp?HiddenView=Go&ITEM=<%=TRAVELER_NO%>'">
 <input type="button" value="Update" onClick="javascript:return onUpdate();"/>
 
</tr>
</table>
</FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>