<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
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
  var IsNumber=true;
   var Char;

 if(form1.Invoice.value=="" || form1.Invoice.value.length==0)
	{
		alert("Please enter Invoice num ");
		form1.Invoice.focus();
    return false;
 
 	}
 else if(form1.Traveler.value=="" || form1.Traveler.value.length==0)
	{
		alert("Please enter Traveler ");
		form1.Traveler.focus();
    return false;
 
 	}else if(form1.Pallet.value=="" || form1.Pallet.value.length==0)
	{
		alert("Please enter Pallet ");
		form1.Pallet.focus();
    return false;
 
 	}else if(form1.Tray.value=="" || form1.Tray.value.length==0)
	{
		alert("Please enter Number of Tray ");
		form1.Tray.focus();
    return false;
 
 	}
  else if(form1.Length.value=="" || form1.Length.value.length==0)
	{
		alert("Please enter Length ");
		form1.Length.focus();
    return false;
 
 	}else if(form1.Width.value=="" || form1.Width.value.length==0)
	{
		alert("Please enter Width ");
		form1.Width.focus();
    return false;
 
 	}else if(form1.Height.value=="" || form1.Height.value.length==0)
	{
		alert("Please enter Height ");
		form1.Height.focus();
    return false;
 
 	}else if(form1.Gross.value=="" || form1.Gross.value.length==0)
	{
		alert("Please enter Gross Weight ");
		form1.Gross.focus();
    return false;
 
 	}/*else if(form1.Remarks1.value=="" || form1.Remarks1.value.length==0)
	{
		alert("Please enter Remarks1 ");
		form1.Remarks1.focus();
    return false;
 
 	}else if(form1.Remarks2.value=="" || form1.Remarks2.value.length==0)
	{
		alert("Please enter Remarks2 ");
		form1.Remarks2.focus();
    return false;
 
 	}*/
 else
  document.form1.action = "ISC_AddPallet.jsp?action=Update";
//  alert(document.form1.Traveler.value);
  document.form1.submit();
  return true;

}


function onSave()
{
  var IsNumber=true;
   var Char;

 if(form1.Invoice.value=="" || form1.Invoice.value.length==0)
	{
		alert("Please enter Invoice num ");
		form1.Invoice.focus();
    return false;
 
 	}
 else if(form1.Traveler.value=="" || form1.Traveler.value.length==0)
	{
		alert("Please enter Traveler ");
		form1.Traveler.focus();
    return false;
 
 	}else if(form1.Pallet.value=="" || form1.Pallet.value.length==0)
	{
		alert("Please enter Pallet ");
		form1.Pallet.focus();
    return false;
 
 	}
  else if(form1.Tray.value=="" || form1.Tray.value.length==0)
	{
		alert("Please enter Number of Tray ");
		form1.Tray.focus();
    return false;
 
 	}else if(form1.Length.value=="" || form1.Length.value.length==0)
	{
		alert("Please enter Length ");
		form1.Length.focus();
    return false;
 
 	}else if(form1.Width.value=="" || form1.Width.value.length==0)
	{
		alert("Please enter Width ");
		form1.Width.focus();
    return false;
 
 	}else if(form1.Height.value=="" || form1.Height.value.length==0)
	{
		alert("Please enter Height ");
		form1.Height.focus();
    return false;
 
 	}else if(form1.Gross.value=="" || form1.Gross.value.length==0)
	{
		alert("Please enter Gross Weight ");
		form1.Gross.focus();
    return false;
 
 	}/*else if(form1.Remarks1.value=="" || form1.Remarks1.value.length==0)
	{
		alert("Please enter Remarks1 ");
		form1.Remarks1.focus();
    return false;
 
 	}else if(form1.Remarks2.value=="" || form1.Remarks2.value.length==0)
	{
		alert("Please enter Remarks2 ");
		form1.Remarks2.focus();
    return false;
 
 	}*/
 else
  document.form1.action = "ISC_AddPallet.jsp?action=Save";
//  alert(document.form1.Traveler.value);
  document.form1.submit();
  return true;

}
function onADD()
{

var HiddenAdd=document.form1.HiddenAdd.value;
 if(form1.Invoice.value=="" || form1.Invoice.value.length==0)
	{
		alert("Please enter Invoice Number ");
		form1.Invoice.focus();
    return false;
 
 	}
 document.form1.submit();
}

</script>
<title>Add Pallet</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%

StrUtils strUtils     = new StrUtils();
ShipDetDAO shipDetDAO=new ShipDetDAO();
ArrayList invQryList  = new ArrayList();
MLogger logger = new MLogger();


String html = "";
String HiddenView="",HiddenAprove="";

String fieldDesc="";
String Traveler="",Pallet="",Length="",Width="",Gross="",Height="",Remarks1="",Remarks2="",Invoice="",Tray="",HiddenAdd="";
String enablesave="readonly";
boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
boolean addPallet=false;
boolean isSaveBtn=false;
UserTransaction ut=null ;


String PLANT          = (String)session.getAttribute("PLANT");if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("action")).trim();
fieldDesc         = strUtils.fString(request.getParameter("fieldDesc")).trim();
String user_id        = session.getAttribute("LOGIN_USER").toString().toUpperCase();
String TRN_DATE=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());

Traveler  = strUtils.fString(request.getParameter("Traveler"));
Pallet  = strUtils.fString(request.getParameter("Pallet"));
Length  = strUtils.fString(request.getParameter("Length"));

Width  = strUtils.fString(request.getParameter("Width"));
Gross  = strUtils.fString(request.getParameter("Gross"));
Height  = strUtils.fString(request.getParameter("Height"));

Remarks1  = strUtils.fString(request.getParameter("Remarks1"));
Remarks2  = strUtils.fString(request.getParameter("Remarks2"));
Invoice  = strUtils.fString(request.getParameter("Invoice"));
Tray  = strUtils.fString(request.getParameter("Tray"));

HiddenView            = strUtils.fString(request.getParameter("HiddenView"));
HiddenAdd            = strUtils.fString(request.getParameter("HiddenAdd"));

MLogger.log("Traveler :"+Traveler);
MLogger.log("Pallet :"+Pallet);
MLogger.log("Length :"+Length);
MLogger.log("Width :"+Width);
MLogger.log("Gross :"+Gross);
MLogger.log("Height :"+Height);
MLogger.log("Remarks1 :"+Remarks1);
MLogger.log("Remarks2 :"+Remarks2);
MLogger.log("Invoice :"+Invoice);
MLogger.log("Tray :"+Tray);


if(action.equalsIgnoreCase("Save"))
{
boolean isupdated=false;
Hashtable ht = null;
try{
ht = new Hashtable();


ht.put(MDbConstant.TRAVELER_ID,Traveler);
ht.put(MDbConstant.INVOICE,Invoice);
ht.put(MDbConstant.PALLET,Pallet);
ht.put(MDbConstant.NO_OF_TRAYS,Tray);
ht.put(MDbConstant.GROSS_WEIGHT,Gross);
ht.put(MDbConstant.LENGTH,Length);
ht.put(MDbConstant.WIDTH,Width);
ht.put(MDbConstant.HEIGHT,Height);
ht.put(MDbConstant.REMARKS1,"NO");
ht.put(MDbConstant.REMARKS2,"NO");
ht.put("CRAT", new DateUtils().getDateinyyyy_mm_dd(DateUtils.getDate()));
//ht.put(MDbConstant.LOGIN_USER,user_id);
//ht.put(MDbConstant.ReceiveStatus,"");
//ht.put(MDbConstant.MOVHIS_REF_NUM,"TRAYQTY-UPDATE");
//ht.put(MDbConstant.MOVEHIS_CR_DATE,TRN_DATE);

 
isupdated=shipDetDAO.insertIntoShipDet(ht);

if(isupdated){
fieldDesc = "<tr><td><B><h3>Pallet Added successfully<h3></td></tr>";
response.sendRedirect("ISC_AddPallet.jsp?HiddenView=View&ITEM="+Traveler+"&TRAYID="+Traveler+"&fieldDesc="+fieldDesc+"");
}else
{
 fieldDesc = "<tr><td><B><h3>Unable to Add Pallet<h3></td></tr>";
}
 

} catch(Exception e){
System.out.println(e.toString());
 fieldDesc = "<tr><td><B><h3>Unable to Add Pallet<h3></td></tr>";
}
}


if(action.equalsIgnoreCase("Update"))
{
boolean isupdated=false;
Hashtable ht = null;
try{
ht = new Hashtable();

ht.put(MDbConstant.INVOICE,Invoice);

 String sUpQry = "  SET TRAVELER_ID ='"+Traveler+"',PALLET ='"+Pallet+"',NO_OF_TRAY='"+Tray+"',GROSS_WEIGHT ='"+Gross+"',LENGTH= '"+Length+"',WIDTH='"+Width+"',HEIGHT='"+Height+"'";
isupdated=shipDetDAO.update(sUpQry,ht,"");

if(isupdated){
fieldDesc = "<tr><td><B><h3>Pallet Details Updated successfully<h3></td></tr>";
response.sendRedirect("ISC_AddPallet.jsp?HiddenView=View&ITEM="+Traveler+"&TRAYID="+Traveler+"&fieldDesc="+fieldDesc+"");
}else
{
 fieldDesc = "<tr><td><B><h3>Unable to Update Pallet Details<h3></td></tr>";
}
 

} catch(Exception e){
System.out.println(e.toString());
 fieldDesc = "<tr><td><B><h3>Unable to Update Pallet Details<h3></td></tr>";
}
}
if(HiddenView.equalsIgnoreCase("View"))
{
addPallet=false;
HiddenAdd="";
Traveler="";
Pallet="";
Length="";
Width="";
Gross="";
Height="";
Remarks1="";
Remarks2="";
Invoice="";
Tray="";
//fieldDesc = "<tr><td><B><h3>Pallet Added successfully<h3></td></tr>";
}

if(HiddenAdd.equalsIgnoreCase("AddPAllet"))
{
addPallet=true;
enablesave="enabled";
}
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="ISC_AddPallet.jsp" >
  
   <INPUT type="hidden" name="ITEM" value="<%=""%>">
  <INPUT type="hidden" name="HiddenView" value="Go">
  <INPUT type="hidden" name="HiddenAdd" value="AddPAllet">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Add Pallet</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
  
    <TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

      <TR>
		   <TH ALIGN="RIGHT" >&nbsp;</TH>
                      <TD>&nbsp;</TD>
                      <TH ALIGN="RIGHT" > &nbsp;&nbsp;Invoice No:</TH>
                      <TD><INPUT name="Invoice" type = "TEXT" value="<%=Invoice%>" size="20"  MAXLENGTH=20 readonly >
                      <a href="#" onClick="javascript:popUpWin('listView/InvoicePopUp.jsp?ITEM='+form1.Invoice.value);"><img src="images/populate.gif" border="0"></a>
                       <input type="button" value="AddPallet" onClick="javascript:return onADD();"/></TD>
                            
		</TR>

  </TABLE>
  <br>  
  
  <%if(addPallet){
  // Check isExists Invoice In Ship_det if exists displays the details
  Hashtable htpallet=  new Hashtable();
  htpallet.put("INVOICE",Invoice);
  String Query = " TRAVELER_ID,PALLET,NO_OF_TRAY,GROSS_WEIGHT,LENGTH,WIDTH,HEIGHT " ; 
   Map map = shipDetDAO.selectRow(Query,htpallet);
  if (map.size()>0){
      Traveler= (String) map.get("TRAVELER_ID");
      Pallet= (String) map.get("PALLET");
      Tray= (String) map.get("NO_OF_TRAY");
      Gross= (String) map.get("GROSS_WEIGHT");
      Length= (String) map.get("LENGTH");
      Width= (String) map.get("WIDTH");
      Height = (String) map.get("HEIGHT");
  }else{
      isSaveBtn = true;
  }
  
  
  
  
  %>
 <table  cellspacing="5" cellpadding="0" border="0" width="50%" align="center" BGCOLOR="#dddddd">
<tr>
    <td>Delivery&nbsp;No. :</td>
    <td><INPUT name="Traveler" type = "TEXT"  value="<%=Traveler%>" size="20"  MAXLENGTH=20  ></td>
    
</tr>

<tr>
    <td>Pallet :</td>
    <td><INPUT name="Pallet" type = "TEXT"  value="<%=Pallet%>" size="20"  MAXLENGTH=20  ></td>
   
</tr>

<tr>
    <td>No Of Trays :</td>
    <td><INPUT name="Tray" type = "TEXT"  value="<%=Tray%>" size="20"  MAXLENGTH=20  ></td>
    
</tr>

<tr>
    <td>Length :</td>
    <td><INPUT name="Length" type = "TEXT"  value="<%=Length%>" size="20"  MAXLENGTH=20  ></td>
  
</tr>

<tr>
    <td>Width :</td>
    <td><INPUT name="Width" type = "TEXT"  value="<%=Width%>" size="20"  MAXLENGTH=20  ></td>
    
</tr>

<tr>
    <td>Height :</td>
    <td><INPUT name="Height" type = "TEXT"  value="<%=Height%>" size="20"  MAXLENGTH=20  ></td>
    
</tr>

<tr>
    <td>Gross :</td>
    <td><INPUT name="Gross" type = "TEXT"  value="<%=Gross%>" size="20"  MAXLENGTH=20  ></td>
    
</tr>

</table>

<br><br>
<table width="60%"  align="center" >
<tr cellspacing="10" border="4" align="center" >
<td>
<input type="button" value="Back" onClick="window.location.href='indexPage.jsp' ">
<% if (isSaveBtn==true){%>
<input type="button" value="Save" onClick="javascript:return onSave();  "/>
<%}else{%>
<input type="button" value="Update" onClick="javascript:return onUpdate();  "/>

 <%}%>
</tr>
</table>

<%}%>

</FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="" >
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>
