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
 var part=document.form1.SKU01.value;
 var lot=document.form1.LOTNO01.value;
 var qty=document.form1.QTY01.value;
 var remark=document.form1.REMARK.value;
 
 	if(form1.SKU01.value=="" || form1.SKU01.value.length==0)
	{
		alert("Please enter SKU ");
		form1.SKU01.focus();
		return false;
	}else if(form1.LOTNO01.value=="" || form1.LOTNO01.value.length==0)
	{
		alert("Please enter Lot No ");
		form1.LOTNO01.focus();
		return false;
	}else if(form1.QTY01.value=="" || form1.QTY01.value.length==0)
	{
		alert("Please enter quantity ");
		form1.QTY01.focus();
		return false;
	}else if(form1.REMARK.value=="" || form1.REMARK.value.length==0)
	{
		alert("Please enter remarks ");
		form1.REMARK.focus();
		return false;
	}else
  document.form1.action = "TravellerDetailUpdate.jsp?action=Update";
  document.form1.submit();
  return true;

}

</script>
<title>Inbound Traveller Summary</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%

StrUtils strUtils     = new StrUtils();
RecvDetDAO recvDetDAO=new RecvDetDAO();
RecevingUtil recevingUtil=new RecevingUtil();
RecvDetDAO dao= new RecvDetDAO();
ArrayList invQryList  = new ArrayList();
MLogger logger = new MLogger();

String WHID ="",  ITEM = "", BATCH ="", BINNO ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="";
String html = "";
String TRAVELER_NO="",PART="",DESCRIPTION="",LOC="",LOT="",PLT="",REMARK="";
String PART01="",LOT01="",QTY01="";
String HiddenView="",HiddenAprove="";

boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
UserTransaction ut=null ;


String PLANT          = (String)session.getAttribute("PLANT");if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("action")).trim();
ITEM                  = strUtils.fString(request.getParameter("ITEM"));

HiddenView            = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove          = strUtils.fString(request.getParameter("HiddenAprove"));

TRAVELER_NO           = strUtils.fString(request.getParameter("TRAVELERNO"));
MTID                  = strUtils.fString(request.getParameter("MTID"));

PART                  = strUtils.fString(request.getParameter("SKU"));
PART01                = strUtils.fString(request.getParameter("SKU01"));

PLT                   = strUtils.fString(request.getParameter("PLT"));

LOT                   = strUtils.fString(request.getParameter("LOTNO"));
LOT01                 = strUtils.fString(request.getParameter("LOTNO01"));

QTY                   = strUtils.fString(request.getParameter("QTY"));
QTY01                 = strUtils.fString(request.getParameter("QTY01"));

REMARK                = strUtils.fString(request.getParameter("REMARK"));


if(action.equalsIgnoreCase("Update"))
{
boolean isupdated=false;
Hashtable ht = null;
ht = new Hashtable();

ht.put("TRAVELER", TRAVELER_NO);
ht.put("MTID", MTID);
ht.put("PART01", PART01);
ht.put("LOT01", LOT01);
ht.put("QTY01", QTY01);
ht.put("REMARK", REMARK);

isupdated=recevingUtil.UpdateTravelerDetail(ht);

if(isupdated){
//fieldDesc = "<tr><td><B><h3>MTID updated successfully<h3></td></tr>";
response.sendRedirect("TravellerSummary.jsp?HiddenView=Go&ITEM="+TRAVELER_NO+"");
}else
{
 fieldDesc = "<tr><td><B><h3>Unable to update MTID details<h3></td></tr>";
}
 
}

if(action.equalsIgnoreCase("View"))
{

 try{
      invQryList = recvDetDAO.getMTIDDetails(PLANT,ITEM);
      if(invQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no  Delivery No. summary found to display<h3></td></tr>";
     //  fieldDesc=fieldDesc+ITEM;
      }else{
       for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          TRAVELER_NO=(String)lineArr.get("traveler");
          MTID=(String)lineArr.get("mtid");
          PART=(String)lineArr.get("sku");
          DESCRIPTION=(String)lineArr.get("description");
          LOC=(String)lineArr.get("loc");
          LOT=(String)lineArr.get("lot");
          PLT=(String)lineArr.get("pallet");
          QTY=(String)lineArr.get("qty");
          
          
          }
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}




%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="TravellerSummary.jsp" >
  <INPUT type="hidden" name="MTID" value="<%=MTID%>">
  <INPUT type="hidden" name="HiddenView" value="Go">
  <INPUT type="hidden" name="HiddenAprove" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">DELIVERYNO SUMMARY</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br><br><br><br>
  <table  cellspacing="5" cellpadding="0" border="0" width="60%" align="center" BGCOLOR="#dddddd">
<tr>
    <td>Traveler No :</td>
    <td><INPUT name="TRAVELERNO" type = "TEXT"  value="<%=TRAVELER_NO%>" size="20"  MAXLENGTH=20></td>
    <td><INPUT name="TRAVELERNO01" type = "TEXT"  value="<%=TRAVELER_NO%>" size="20"  MAXLENGTH=20></td>
</tr>

<tr>
    <td>MTID No :</td>
    <td><INPUT name="MTID" type = "TEXT"  value="<%=MTID%>" size="20"  MAXLENGTH=20></td>
    <td><INPUT name="MTID01" type = "TEXT"  value="<%=MTID%>" size="20"  MAXLENGTH=20></td>
</tr>

<tr>
    <td>Pallet No :</td>
    <td><INPUT name="PLT" type = "TEXT"  value="<%=PLT%>" size="20"  MAXLENGTH=20></td>
    <td><INPUT name="PLT01" type = "TEXT"  value="<%=PLT%>" size="20"  MAXLENGTH=20></td>
</tr>

<tr>
    <td>SKU No :</td>
    <td><INPUT name="SKU" type = "TEXT"  value="<%=PART%>" size="20"  MAXLENGTH=20></td>
    <td><INPUT name="SKU01" type = "TEXT"  value="<%=PART01%>" size="20"  MAXLENGTH=20></td>
</tr>

<tr>
    <td>Lot No :</td>
    <td><INPUT name="LOTNO" type = "TEXT"  value="<%=LOT%>" size="20"  MAXLENGTH=20></td>
    <td><INPUT name="LOTNO01" type = "TEXT"  value="<%=LOT01%>" size="20"  MAXLENGTH=20></td>
</tr>

<tr>
    <td>Quantity :</td>
    <td><INPUT name="QTY" type = "TEXT"  value="<%=QTY%>" size="20"  MAXLENGTH=20></td>
    <td><INPUT name="QTY01" type = "TEXT"  value="<%=QTY01%>" size="20"  MAXLENGTH=20></td>
</tr>
<tr>
<TD>Remarks :</TD>
<td><INPUT name="REMARK" type = "TEXT"  value="<%=REMARK%>" size="20"  MAXLENGTH=20></td>
<TD></TD>
</tr>

</table>

   <P>&nbsp;
    </P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     
      <input type="button" value="Back" onClick="window.location.href='indexPage.jsp'">&nbsp
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <TR> <input type="button" value="Update" onClick="javascript:return onUpdate();"/></TD>   
    </P> 
</FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>