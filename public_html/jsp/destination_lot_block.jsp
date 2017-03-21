<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script language="JavaScript" type="text/javascript" src="js/pwd.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SYSTEM BLOCKING MASTER</title>
</head>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}

function onNew(){
 document.form1.action  = "destination_lot_block.jsp?action=NEW";
   document.form1.submit();
}
function onSave(){
   var SHIP_TO   = document.form1.SHIP_TO.value;
 if( SHIP_TO == "" || SHIP_TO == null ){
 alert(" Please Enter Destination ");
 document.form1.SHIP_TO.focus();
  return false;
 } 
 var   PRD_CLS_ID  = document.form1.PRD_CLS_ID.value;
if(  PRD_CLS_ID=="" || PRD_CLS_ID == null ){
 alert(" Please Enter PRD_CLS_ID ");
 document.form1.PRD_CLS_ID.focus();
  return false;
 }
  var LOT_START_WITH   = document.form1.LOT_START_WITH.value;
  if(  LOT_START_WITH=="" || LOT_START_WITH == null ){
 alert(" Please Enter LOT START WITH  ");
 document.form1.LOT_START_WITH.focus();
  return false;
 }
 
 //6.0 ENHANCEMENT
  var   SKU  = document.form1.SKU.value;
if(  SKU=="" || SKU == null ){
 alert(" Please Enter SKU ");
 document.form1.SKU.focus();
  return false;
 }
 
  document.form1.action  = "destination_lot_block.jsp?action=SAVE";
   document.form1.submit();
}

function onUpdate(){
   var SHIP_TO   = document.form1.SHIP_TO.value;
 if( SHIP_TO == "" || SHIP_TO == null ){
 alert(" Please Enter Destination ");
  document.form1.SHIP_TO.focus();
  return false;
 } 
 var   PRD_CLS_ID  = document.form1.PRD_CLS_ID.value;
if(  PRD_CLS_ID=="" || PRD_CLS_ID == null ){
 alert(" Please Enter PRD_CLS_ID ");
 document.form1.PRD_CLS_ID.focus();
  return false;
 }
  var LOT_START_WITH   = document.form1.LOT_START_WITH.value;
  if(  LOT_START_WITH=="" || LOT_START_WITH == null ){
 alert(" Please Enter LOT_START_WITH ");
  document.form1.LOT_START_WITH.focus();
  return false;
 }
 
  //6.0 ENHANCEMENT
  var   SKU  = document.form1.SKU.value;
if(  SKU=="" || SKU == null ){
 alert(" Please Enter SKU ");
 document.form1.SKU.focus();
  return false;
 }
 
  document.form1.action  = "destination_lot_block.jsp?action=UPDATE";
   document.form1.submit();
}

function onClear(){
   var SHIP_TO   = document.form1.SHIP_TO.value;
   document.form1.action  = "destination_lot_block.jsp?action=CLEAR";
   document.form1.submit();
}

function onDelete(){
   var SHIP_TO   = document.form1.SHIP_TO.value;
   document.form1.action  = "destination_lot_block.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
  var SHIP_TO   = document.form1.SHIP_TO.value;
 if( SHIP_TO == "" || SHIP_TO == null ){
 alert(" Please Enter Destination ");
  document.form1.SHIP_TO.focus();
  return false;
 } 
 
    var PRD_CLS_ID   = document.form1.PRD_CLS_ID.value;
    if(PRD_CLS_ID == "" || PRD_CLS_ID == null) {
	    alert("Please Enter PRD_CLS_ID To View Details "); 
	    document.form1.PRD_CLS_ID.focus(); 
	    return false;
     }

   document.form1.action  = "destination_lot_block.jsp?action=VIEW";
   document.form1.submit();
}


</SCRIPT>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sNewEnb="enabled";
String sDeleteEnb="disabled";
String sSaveEnb="disabled";
String sClearEnb = "enabled";

String action     =   "";
String res        = "";
String sDESTINATION="", sPRD_CLS_ID="", sLOTSTARTWITH="", user_id="", INSERT="", UPDATE="",destinationname="",prd_grp_id="";

//6.0 ENHANCEMENT
String sSKU ="";

ArrayList dest = new ArrayList();

StrUtils strUtils = new StrUtils();
DestinationLotBlockUtil destutil = new DestinationLotBlockUtil();

sDESTINATION    = strUtils.fString(request.getParameter("SHIP_TO"));
sPRD_CLS_ID     = strUtils.fString(request.getParameter("PRD_CLS_ID"));
sLOTSTARTWITH   = strUtils.fString(request.getParameter("LOT_START_WITH"));

//6.0 ENHANCEMENT
sSKU =strUtils.fString(request.getParameter("SKU"));

action  = strUtils.fString(request.getParameter("action"));
user_id = session.getAttribute("LOGIN_USER").toString().toUpperCase();

//1. >> New
if(action.equalsIgnoreCase("NEW")){
	sDESTINATION ="";
	sPRD_CLS_ID="";
	sLOTSTARTWITH="";

	//6.0 ENHANCEMENT
	sSKU="";
	
      sSaveEnb="enabled";
}

//2. >> Add
else if(action.equalsIgnoreCase("SAVE")){
		 
	 Hashtable ht = new Hashtable();
	 ht.put("SHIP_TO",sDESTINATION);
	 ht.put("PRD_CLS_ID",sPRD_CLS_ID);
     ht.put("LOT_START_WITH",sLOTSTARTWITH); 
     
//   6.0 ENHANCEMENT
 	 ht.put("SKU",sSKU); 
     
     ht.put("LOGIN_USER",sUserId);
     	    
		   int destInserted = destutil.insertDestination(ht);
		   if(destInserted==1) {
			    res = "<font class = "+MDbConstant.FAILED_COLOR +">Prd_Cls_Id does not exist in System </font>";
                          
     } else if(destInserted==2) {
               res = "<font class = "+MDbConstant.FAILED_COLOR +">Destination does not exist in System </font>";
         
	   }else  {
		   mv.insertMovHisLogger(sUserId, "Block Lot","Destination "+ sDESTINATION +" Added Successfully" ); 
		   res = "<font class = "+MDbConstant.SUCCESS_COLOR +"> Record Saved Successfully</font>";
		   sDESTINATION="";
		   sPRD_CLS_ID="";
		   sLOTSTARTWITH="";
           sSaveEnb  = "disabled"; 
           sSKU="";
         
	   }
}
		   
//3. VIEW
else if(action.equalsIgnoreCase("VIEW")){
	
	 sDeleteEnb  = "enabled";
	 sNewEnb="disabled";
	 sSaveEnb  = "disabled";
	  	   
			   Map map  = destutil.getDestinationDetails(sDESTINATION,sPRD_CLS_ID);
			   if(map.size()>0){
				   sDESTINATION   = strUtils.fString((String)map.get("DESTINATION_CODE"));
				   sPRD_CLS_ID   = strUtils.fString((String)map.get("PRDCLSID"));
				   sLOTSTARTWITH   = strUtils.fString((String)map.get("LOT_START_WITH"));
				   
//				   6.0 ENHANCEMENT
				   sSKU  = strUtils.fString((String)map.get("SKU"));

		   }else{
			     res = "<font class = "+MDbConstant.FAILED_COLOR +">Destination Does Not Exists. Try Again</font>";
		    }
}

//4. CLEAR
else if(action.equalsIgnoreCase("CLEAR")){
	 sDESTINATION="";
	  sPRD_CLS_ID="";
     sLOTSTARTWITH="";
     
//	   6.0 ENHANCEMENT
	sSKU="";
}

//5. DELETE
else if(action.equalsIgnoreCase("DELETE")){
	 Hashtable ht = new Hashtable();
	 ht.put("SHIP_TO",sDESTINATION);
	 ht.put("PRD_CLS_ID",sPRD_CLS_ID);
     	    
		   boolean destdeleted = destutil.deleteDestination(ht);
		   if(destdeleted) {
			   mv.insertMovHisLogger(sUserId, "Block Lot","Destination "+ sDESTINATION +" Deleted Successfully" ); 
			   res = "<font class = "+MDbConstant.SUCCESS_COLOR +"> Record Deleted Successfully</font>";
			   sDESTINATION="";
			   sPRD_CLS_ID="";
			   sLOTSTARTWITH="";
			   
//			   6.0 ENHANCEMENT
				sSKU="";

               sSaveEnb  = "disabled";
               sDeleteEnb = "disabled";
                          
     } else {
               res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to Delete Destination </font>";
               sDeleteEnb = "enabled";
	   } 

}
%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="DestinationLotBlockUtil.java" >

  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">System Blocking Master</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  
   <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
      
   <tr>
   <th WIDTH="35%" ALIGN="RIGHT">*DESTINATION&nbsp;: &nbsp;&nbsp;&nbsp;&nbsp; </th>
   <td>
   <input name="SHIP_TO" type="TEXT" value="<%=sDESTINATION %>" size="50" >&nbsp;&nbsp;
   </td>
   </tr>
   
   <tr>
   <th WIDTH="35%" ALIGN="RIGHT">*PRD_CLS_ID&nbsp;: &nbsp;&nbsp;&nbsp;&nbsp;</th>
   <td>
   <input name="PRD_CLS_ID" type="TEXT" value="<%=sPRD_CLS_ID %>"  size="50" >
    <INPUT class="Submit" type="BUTTON" value="View" onClick="onView();">
   </td>
   </tr>
   
   <tr>
   <th WIDTH="35%" ALIGN="RIGHT">*LOT START WITH&nbsp;: &nbsp;&nbsp;&nbsp;&nbsp;</th>
   <td>
   <input name="LOT_START_WITH" type="TEXT" value="<%=sLOTSTARTWITH %>" size="50" maxlength="1">
   </td>
   </tr>
   
   <!-- Added for system blocking functionality under 6.0 -->
     <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" > *SKU :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
        <TD>
           	<input  size="50" maxlength="60" name="SKU" value="<%=sSKU %>">
        </TD>
   </TR>
     
   </TABLE>
   
   <TABLE WIDTH="100%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
          
     <TR>
         <TD COLSPAN = 2><BR><B> <CENTER> <%=res%> </B></TD>
    </TR>
    
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

     <TR align = "center">
         <TD COLSPAN = 2 align = "center">
                 <INPUT class="Submit" type="BUTTON" value="Cancel" onClick="window.location.href='indexPage.jsp'">&nbsp;&nbsp;
                  <INPUT class="Submit" type="BUTTON" value="Clear" onClick="onClear();" <%=sClearEnb%>>&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="Delete" onClick="onDelete();" <%=sDeleteEnb%>>&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="New" onClick="onNew();" <%=sNewEnb%>>&nbsp;&nbsp;  
                <INPUT class="Submit" type="BUTTON" value="Save" onClick="onSave();" <%=sSaveEnb%>>&nbsp;&nbsp; 
                
                
         </TD>
    </TR>
    </TABLE>
<%@ include file="footer.jsp"%>