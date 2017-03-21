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
<title>LOT Restriction</title>
</head>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}

function onNew(){
 document.form1.action  = "RestrictLot.jsp?action=NEW";
   document.form1.submit();
}
function onSave(){
   var LOT   = document.form1.LOT.value;
 if( LOT == "" || LOT == null ){
 alert(" Please Enter LOT ");
 document.form1.LOT.focus();
  return false;
 } 
 var   STATUS  = document.form1.STATUS.value;
if(  STATUS=="" || STATUS == null ){
 alert(" Please Enter STATUS ");
 document.form1.STATUS.focus();
  return false;
 }
  var REASON   = document.form1.REASON.value;
  if(  REASON=="" || REASON == null ){
 alert(" Please Enter REASON  ");
 document.form1.REASON.focus();
  return false;
 }
  document.form1.action  = "RestrictLot.jsp?action=SAVE";
   document.form1.submit();
}

function onUpdate(){
   var LOT   = document.form1.LOT.value;
 if( LOT == "" || LOT == null ){
 alert(" Please Enter LOT ");
  document.form1.LOT.focus();
  return false;
 } 
 var   STATUS  = document.form1.STATUS.value;
if(  STATUS=="" || STATUS == null ){
 alert(" Please Enter STATUS ");
 document.form1.STATUS.focus();
  return false;
 }
  var REASON   = document.form1.REASON.value;
  if(  REASON=="" || REASON == null ){
 alert(" Please Enter REASON ");
  document.form1.REASON.focus();
  return false;
 }
  document.form1.action  = "RestrictLot.jsp?action=UPDATE";
   document.form1.submit();
}

function onClear(){
   var LOT   = document.form1.LOT.value;
   document.form1.action  = "RestrictLot.jsp?action=CLEAR";
   document.form1.submit();
}
function onView(){
    var LOT   = document.form1.LOT.value;
    if(LOT == "" || LOT == null) {
	    alert("Please Enter LOT to view details "); 
	    document.form1.LOT.focus(); 
	    return false;
     }

   document.form1.action  = "RestrictLot.jsp?action=VIEW";
   document.form1.submit();
}

//For Clearing the Reason Field on Changing the Dropdown
function show(){
		 
	 document.form1.action  = "RestrictLot.jsp?action=CHANGE";
     document.form1.submit();
	}

</SCRIPT>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sNewEnb="enabled";
String sClearEnb="enabled";
String sUpdateEnb="disabled";
String sSaveEnb="enabled";

String action     =   "";
String res        = "";
String sLOT="",sReason="",sStatus="",user_id="", sSKU="";

StrUtils strUtils = new StrUtils();
RestrictLotUtil lotUtil = new RestrictLotUtil(); 
action  = strUtils.fString(request.getParameter("action"));

sLOT    = strUtils.fString(request.getParameter("LOT"));
sReason = strUtils.fString(request.getParameter("REASON"));
sStatus = strUtils.fString(request.getParameter("STATUS"));
user_id = session.getAttribute("LOGIN_USER").toString().toUpperCase();

//1. >> New
if(action.equalsIgnoreCase("NEW")){
      sLOT ="";
      sReason="";
      sStatus="";
      sSaveEnb="enabled";
}

//2. >> Add
else if(action.equalsIgnoreCase("SAVE")){
		 
	 Hashtable ht = new Hashtable();
	 ht.put("LOT",sLOT);
	 ht.put("STATUS",sStatus);
     ht.put("REASON",sReason);    
     ht.put("LOGIN_USER",sUserId);
     	    
		   boolean lotInserted = lotUtil.insertLOT(ht);
		   if(lotInserted) {
			   mv.insertMovHisLogger1(sUserId, "LOT_Restriction","LOT  :"  + sLOT +" added Successfully", sLOT ); 
			   res = "<font class = "+MDbConstant.SUCCESS_COLOR +"> LOT"+ sLOT +"Added Successfully</font>";
               sReason="";
               sLOT="";
               sStatus="";
               sSaveEnb  = "disabled"; 
             
             
     } else {
               res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to add New LOT </font>";
               sSaveEnb  = "enabled";          
     }   
	   }

//3.Update
else if(action.equalsIgnoreCase("UPDATE")){

	Hashtable ht = new Hashtable();
	ht.put("LOT",sLOT);
    ht.put("REASON",sReason);
    ht.put("STATUS",sStatus);      
    ht.put("LOGIN_USER",sUserId);
	    
		   boolean lotupdate = lotUtil.updatelot(ht);
		   if(lotupdate) {
			  mv.insertMovHisLogger1(sUserId, "LOT_Restriction","LOT  :"  + sLOT +" updated as "+ sStatus +" Successfully ", sLOT ); 
			  res = "<font class = "+MDbConstant.SUCCESS_COLOR +">LOT"+ sLOT +" Updated Successfully</font>";
			  sLOT="";
              sReason="";
              sStatus="";
              sUpdateEnb  = "disabled";  
              
    } else {
              res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to Update LOT </font>";
              sUpdateEnb  = "enabled";          
    }
}		   
//4. VIEW
else if(action.equalsIgnoreCase("VIEW")){

	  Map map  = lotUtil.getLotDetails(sLOT);
	   if(map.size()>0){
	    sLOT   = strUtils.fString((String)map.get("LOT"));
	    sStatus   = strUtils.fString((String)map.get("STATUS"));
	    sReason   = strUtils.fString((String)map.get("REASON"));
	    
		   sUpdateEnb  = "enabled";   
		   sSaveEnb  = "disabled";
              
         } else {
           res = "<font class = "+MDbConstant.FAILED_COLOR +">LOT Details Not Found</font>";        
 }
}

//5. CLEAR
else if(action.equalsIgnoreCase("CLEAR")){
	sLOT="";
	sStatus="";
	sReason="";
	
}

//6. CHANGE
else if(action.equalsIgnoreCase("CHANGE")){
	sReason="";
    sUpdateEnb  = "enabled"; 
}
%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="RestrictLotUtil.java" >

  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">LOT RESTRICTION</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  
   <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > *LOT No&nbsp;: &nbsp;&nbsp;&nbsp;&nbsp;</TH>
         <TD>
                <INPUT name="LOT" type = "TEXT" value="<%=sLOT%>" size="50"  MAXLENGTH=8 >&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="View" onClick="onView();">
         </TD>
    </TR>
    
  <TR>       
       <TH WIDTH="35%" ALIGN="RIGHT" >*Status&nbsp; :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD>
            <SELECT NAME ="STATUS" value="<%=sStatus %>" size="1" onchange="show()">
             <% String flag = sStatus;
            if(flag.equals(null) || flag.equals("")){%>
               <OPTION selected value="BLOCK">BLOCK </OPTION>
                <% }else if(flag.equals("BLOCK")) {%>
                <OPTION selected value="BLOCK">BLOCK </OPTION>
                <option value="RELEASED">RELEASED</option>

              <% }else if(flag.equals("RELEASED")) {%>
                <OPTION selected value="RELEASED">RELEASED </OPTION>
                <option value="BLOCK">BLOCK</option>
              <%} %>
            </SELECT>
        </TD>
  </TR>
          
     <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" > *Reason :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
        <TD>
           	<input  size="50" maxlength="60" name="REASON" value="<%=sReason %>">
        </TD>
   </TR>
    
    <TR>
         <TD COLSPAN = 2><BR><B><CENTER><%=res%></B></TD>
    </TR>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     <TR>
         <TD COLSPAN = 2><center>
                 <INPUT class="Submit" type="BUTTON" value="Cancel" onClick="window.location.href='indexPage.jsp'">&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="Clear" onClick="onClear();" <%=sClearEnb%>>&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="New" onClick="onNew();" <%=sNewEnb%>>&nbsp;&nbsp;  
                <INPUT class="Submit" type="BUTTON" value="Update" onClick="onUpdate();" <%=sUpdateEnb%>>&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="Save" onClick="onSave();" <%=sSaveEnb%>>&nbsp;&nbsp; 
         </TD>
    </TR>
    </TABLE>
<%@ include file="footer.jsp"%>
