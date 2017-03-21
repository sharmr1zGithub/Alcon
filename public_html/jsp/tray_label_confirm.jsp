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

function onSave()
{

 	if(form1.GROSS_WEIGHT.value=="" || form1.GROSS_WEIGHT.value.length==0)
	{
		alert("Please enter Gross Weight ");
		form1.GROSS_WEIGHT.focus();
		return false;
	}else if(form1.LENGTH.value=="" || form1.LENGTH.value.length==0)
	{
		alert("Please enter Length ");
		form1.LENGTH.focus();
		return false;
	}else if(form1.WIDTH.value=="" || form1.WIDTH.value.length==0)
	{
		alert("Please enter Width ");
		form1.WIDTH.focus();
		return false;
	}
  else if(form1.HEIGHT.value=="" || form1.HEIGHT.value.length==0)
	{
		alert("Please enter Height ");
		form1.HEIGHT.focus();
		return false;
	}/*else if(form1.REMARKS1.value=="" || form1.REMARKS1.value.length==0)
	{
		alert("Please enter remarks ");
		form1.REMARKS1.focus();
		return false;
	}*/else
  document.form1.action = "tray_label_confirm.jsp?action=Save";
  document.form1.submit();
  return true;

}


function onUpdate()
{

 	if(form1.GROSS_WEIGHT.value=="" || form1.GROSS_WEIGHT.value.length==0)
	{
		alert("Please enter Gross Weight ");
		form1.GROSS_WEIGHT.focus();
		return false;
	}else if(form1.LENGTH.value=="" || form1.LENGTH.value.length==0)
	{
		alert("Please enter Length ");
		form1.LENGTH.focus();
		return false;
	}else if(form1.WIDTH.value=="" || form1.WIDTH.value.length==0)
	{
		alert("Please enter Width ");
		form1.WIDTH.focus();
		return false;
	}
  else if(form1.HEIGHT.value=="" || form1.HEIGHT.value.length==0)
	{
		alert("Please enter Height ");
		form1.HEIGHT.focus();
		return false;
	}else
  
  document.form1.action = "tray_label_confirm.jsp?action=Update";
  form1.HiddenView.value="";
  document.form1.submit();
  return true;

}

function  onPrintPallet() {
  document.form1.action = "tray_label_confirm.jsp?action=PrintPallet";
  form1.HiddenView.value="";
  document.form1.submit();
  return true;
}

</script>
    <title>Tray Labelling Confirm</title>
  </head>
  <link rel="stylesheet" href="css/style.css"/>
  <% 

StrUtils strUtils     = new StrUtils();
ShipDetDAO  shipDetDAO = new ShipDetDAO();
TrayLabelingUtil trayUtil=new TrayLabelingUtil();
//RecvDetDAO dao= new RecvDetDAO();
ArrayList invQryList  = new ArrayList();
MLogger logger = new MLogger();

String GROSS_WEIGHT ="",  LENGTH = "", WIDTH ="", HEIGHT ="", REMARKS1 ="",REMARKS2 ="" , REMARKS3 ="",TRAVELER="",PALLET="",TRAYID="",fieldDesc="",actionApprove="";
String html = "";

String HiddenView="",HiddenAprove="";

boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
boolean isSaveBtn =false;
UserTransaction ut=null ;


String PLANT          = (String)session.getAttribute("PLANT");if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("action")).trim();


HiddenView            = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove          = strUtils.fString(request.getParameter("HiddenAprove"));

TRAVELER              = strUtils.fString(request.getParameter("TRAVELERNO"));
PALLET                 = strUtils.fString(request.getParameter("PALLET"));

//TRAYID                  = strUtils.fString(request.getParameter("TRAYID"));
GROSS_WEIGHT                = strUtils.fString(request.getParameter("GROSS_WEIGHT"));

LENGTH                   = strUtils.fString(request.getParameter("LENGTH"));

WIDTH                   = strUtils.fString(request.getParameter("WIDTH"));
HEIGHT                 = strUtils.fString(request.getParameter("HEIGHT"));



REMARKS1                = strUtils.fString(request.getParameter("REMARKS1"));
REMARKS2                = strUtils.fString(request.getParameter("REMARKS2"));
REMARKS3                = strUtils.fString(request.getParameter("REMARKS3"));

MLogger.log("TRAVELER_NO :"+TRAVELER);
MLogger.log("PALLET :"+PALLET);
MLogger.log("GROSS_WEIGHT :"+GROSS_WEIGHT);
MLogger.log("LENGTH :"+LENGTH);
MLogger.log("WIDTH :"+WIDTH);
MLogger.log("HEIGHT :"+HEIGHT);
MLogger.log("REMARKS1 :"+REMARKS1);
MLogger.log("REMARKS2 :"+REMARKS2);
MLogger.log("REMARKS3 :"+REMARKS3);

if(HiddenView.equalsIgnoreCase("Go"))
{

 Hashtable htpallet=  new Hashtable();
 htpallet.put("TRAVELER_ID", TRAVELER);
 htpallet.put("PALLET", PALLET);
  String Query = " GROSS_WEIGHT,LENGTH,WIDTH,HEIGHT " ; 
   Map map = shipDetDAO.selectRow(Query,htpallet);
  if (map.size()>0){
      GROSS_WEIGHT= (String) map.get("GROSS_WEIGHT");
      LENGTH= (String) map.get("LENGTH");
      WIDTH= (String) map.get("WIDTH");
      HEIGHT = (String) map.get("HEIGHT");
  }else{
      isSaveBtn = true;
  }
 
}

if(action.equalsIgnoreCase("Save"))
{
boolean isInserted=false;
Hashtable ht = null;
ht = new Hashtable();

ht.put("TRAVELER_ID", TRAVELER);
ht.put("PALLET", PALLET);
ht.put("GROSS_WEIGHT", GROSS_WEIGHT);
ht.put("LENGTH", LENGTH);
ht.put("WIDTH", WIDTH);
ht.put("HEIGHT", HEIGHT);
ht.put("REMARKS1", "NO");
ht.put("REMARKS2", "NO");
ht.put("REMARKS3", "NO");
ht.put("CRAT", new DateUtils().getDateinyyyy_mm_dd(DateUtils.getDate()));

isInserted=trayUtil.insertShipDet(ht);

if(isInserted){
fieldDesc = "<tr><td><B><h3>Tray Labelling Confirmed successfully<h3></td></tr>";
}else
{
 fieldDesc = "<tr><td><B><h3>Unable to save the Tray Details<h3></td></tr>";
}
 
}



if(action.equalsIgnoreCase("Update"))
{
boolean isInserted=false;
Hashtable ht = null;
ht = new Hashtable();

ht.put("TRAVELER_ID", TRAVELER);
 ht.put("PALLET", PALLET);
String sUpQry = "  SET GROSS_WEIGHT ='"+GROSS_WEIGHT+"',LENGTH='"+LENGTH+"',WIDTH='"+WIDTH+"',HEIGHT='"+HEIGHT+"'";
isInserted=shipDetDAO.update(sUpQry,ht,"");

if(isInserted){
fieldDesc = "<tr><td><B><h3>Tray Labeling Updated successfully<h3></td></tr>";
}else
{
 fieldDesc = "<tr><td><B><h3>Unable to Update the Tray Labeling<h3></td></tr>";
}
 
}


if(action.equalsIgnoreCase("PrintPallet"))
{
boolean isPrinted=false;
isPrinted=shipDetDAO.Print_Pallet(PALLET);

if(isPrinted){
fieldDesc = "<tr><td><B><h3>Pallet Printed successfully<h3></td></tr>";
}else
{
 fieldDesc = "<tr><td><B><h3>Unable to Print Label<h3></td></tr>";
}
 
}


%>
  <%@ include file="body.jsp"%>
  <FORM name="form1" method="post" action="tray_label_confirm.jsp" >
  <INPUT type="hidden" name="HiddenView" value="Go"/>
  <INPUT type="hidden" name="HiddenAprove" value=""/>
  <INPUT type="hidden" name="TRAVELERNO" value="<%=TRAVELER%>"/>
  <INPUT type="hidden" name="PALLET" value="<%=PALLET%>"/>
  <br/>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">TRAY LABELING CONFIRM</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br/>
  <br/>
  <br/>
  <br/>
  <table cellspacing="5" cellpadding="0" border="0" width="60%" align="center" BGCOLOR="#dddddd">
    <tr>
      <td>Gross&nbsp;Weight :</td>
      <td>
        <INPUT name="GROSS_WEIGHT" type="TEXT" value="<%=GROSS_WEIGHT%>" size="15" MAXLENGTH="20"/>
      </td>
    </tr>
    <tr>
      <td>Length :</td>
      <td>
        <INPUT name="LENGTH" type="TEXT" value="<%=LENGTH%>" size="15" MAXLENGTH="20"/>
      </td>
    </tr>
    <tr>
      <td>Width :</td>
      <td>
        <INPUT name="WIDTH" type="TEXT" value="<%=WIDTH%>" size="15" MAXLENGTH="20"/>
      </td>
    </tr>
    <tr>
      <td>Height :</td>
      <td>
        <INPUT name="HEIGHT" type="TEXT" value="<%=HEIGHT%>" size="15" MAXLENGTH="20"/>
      </td>
    </tr>
   
  </table>
  <P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" value="Back" onClick="window.location.href='TrayLabelingReport.jsp?HiddenView=Go&ITEM=<%=TRAVELER%>&PALLET=<%=PALLET%>'"/>&nbsp;&nbsp;&nbsp;&nbsp;
    <% if (isSaveBtn==true){%>
<input type="button" value="Save" onClick="javascript:return onSave();  "/>

<%}else{%>
<input type="button" value="Update" onClick="javascript:return onUpdate();  "/>

 <%}%>
   <input type="button" value="Print Pallet Label" onClick="javascript:return onPrintPallet();  "/>
  </P>
  <TR>
    <TD>
      <DIV align="center"/>
    </TD>
  </TR>
  <font face="Times New Roman">
    <table border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%= fieldDesc%>
    </table>
  </font>
  <%@ include file="footer.jsp"%>
</html>

