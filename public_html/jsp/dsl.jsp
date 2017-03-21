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
<title>DSL MASTER</title>
</head>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}

function onNew(){
 document.form1.action  = "dsl.jsp?action=NEW";
   document.form1.submit();
}
function onSave(){
   var COUNTRY   = document.form1.COUNTRY.value;
 if( COUNTRY == "" || COUNTRY == null ){
 alert(" Please Enter Country ");
 document.form1.COUNTRY.focus();
  return false;
 } 
 var   PRD_CLS_ID  = document.form1.PRD_CLS_ID.value;
if(  PRD_CLS_ID=="" || PRD_CLS_ID == null ){
 alert(" Please Enter PRD_CLS_ID ");
 document.form1.PRD_CLS_ID.focus();
  return false;
 }
  var STATUS   = document.form1.STATUS.value;
  if(  STATUS=="" || STATUS == null ){
 alert(" Please Enter Status  ");
 document.form1.STATUS.focus();
  return false;
 }
  document.form1.action  = "dsl.jsp?action=SAVE";
   document.form1.submit();
}

function onUpdate(){
   var COUNTRY   = document.form1.COUNTRY.value;
 if( COUNTRY == "" || COUNTRY == null ){
 alert(" Please Enter COUNTRY ");
  document.form1.COUNTRY.focus();
  return false;
 } 
 var   PRD_CLS_ID  = document.form1.PRD_CLS_ID.value;
if(  PRD_CLS_ID=="" || PRD_CLS_ID == null ){
 alert(" Please Enter PRD_CLS_ID ");
 document.form1.PRD_CLS_ID.focus();
  return false;
 }
  var STATUS   = document.form1.STATUS.value;
  if(  STATUS=="" || STATUS == null ){
 alert(" Please Enter STATUS ");
  document.form1.STATUS.focus();
  return false;
 }
  document.form1.action  = "dsl.jsp?action=UPDATE";
   document.form1.submit();
}

function onClear(){
   var COUNTRY   = document.form1.COUNTRY.value;
   document.form1.action  = "dsl.jsp?action=CLEAR";
   document.form1.submit();
}

function onView(){
    var COUNTRY   = document.form1.COUNTRY.value;
    if(COUNTRY == "" || COUNTRY == null) {
	    alert("Please Enter COUNTRY To View Details. "); 
	    document.form1.COUNTRY.focus(); 
	    return false;
     }
      var   PRD_CLS_ID  = document.form1.PRD_CLS_ID.value;
if(  PRD_CLS_ID=="" || PRD_CLS_ID == null ){
 alert(" Please Enter PRD_CLS_ID To View Details.");
 document.form1.PRD_CLS_ID.focus();
  return false;
 }

   document.form1.action  = "dsl.jsp?action=VIEW";
   document.form1.submit();
}


</SCRIPT>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sNewEnb="enabled";
String sClearEnb="enabled";
String sUpdateEnb="disabled";
String sSaveEnb="disabled";

ArrayList invQryList  = new ArrayList();
String action     =   "";
String res        = "", fieldDesc = "";
String sCOUNTRY="",sPRD_CLS_ID="",sSTATUS="",user_id="";

StrUtils strUtils = new StrUtils();
DSLUtil dslutil = new DSLUtil();

DestinationLotBlockUtil destutil = new DestinationLotBlockUtil();

sCOUNTRY    = strUtils.fString(request.getParameter("COUNTRY"));
sPRD_CLS_ID     = strUtils.fString(request.getParameter("PRD_CLS_ID"));
sSTATUS  = strUtils.fString(request.getParameter("STATUS"));

action  = strUtils.fString(request.getParameter("action"));

user_id = session.getAttribute("LOGIN_USER").toString().toUpperCase();

//1. >> New
if(action.equalsIgnoreCase("NEW")){
	 sCOUNTRY="";
	   sPRD_CLS_ID="";
      sSaveEnb="enabled";
}

//2. >> Add
else if(action.equalsIgnoreCase("SAVE")){
		 	 
	 Hashtable ht = new Hashtable();
	 ht.put("COUNTRY",sCOUNTRY);
	 ht.put("PRD_CLS_ID",sPRD_CLS_ID);
    ht.put("STATUS",sSTATUS);    
    ht.put("LOGIN_USER",sUserId);
 	    
		   int dslInserted = dslutil.insertCountry(ht);
		   
		   if(dslInserted==1) {
			    res = "<font class = "+MDbConstant.FAILED_COLOR +">Prd_Cls_Id does not exist in System </font>";
                         
   		   } else if(dslInserted==2) {
              res = "<font class = "+MDbConstant.FAILED_COLOR +">Country does not exist in System </font>";
        
	      } else {
			   mv.insertMovHisLogger(sUserId, "DSL","COUNTRY  :"  + sCOUNTRY +" added Successfully" ); 
			   res = "<font class = "+MDbConstant.SUCCESS_COLOR +"> Record Added Successfully</font>";
			   sCOUNTRY="";
			   sPRD_CLS_ID="";
			   sSTATUS="";
              sSaveEnb  = "disabled";  
            
            
   	 } 
}
//3.Update
else if(action.equalsIgnoreCase("UPDATE")){
	
	 Hashtable ht = new Hashtable();
	 ht.put("COUNTRY",sCOUNTRY);
	 ht.put("PRD_CLS_ID",sPRD_CLS_ID);
     ht.put("STATUS",sSTATUS);    
     ht.put("LOGIN_USER",sUserId);		

	   boolean lotupdate = dslutil.updateDestination(ht);
	   if(lotupdate) {
		  mv.insertMovHisLogger(sUserId, "DSL","COUNTRY  :"  + sCOUNTRY +" Updated As "+ sSTATUS +" Successfully " ); 
		  res = "<font class = "+MDbConstant.SUCCESS_COLOR +">COUNTRY "+ sCOUNTRY +" Updated Successfully</font>";
		  sCOUNTRY="";
		  sPRD_CLS_ID="";
		  sSTATUS="";
        sUpdateEnb  = "disabled";                          
      
} else {
        res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to Update LOT </font>";
        sUpdateEnb  = "enabled";          
}
}		   
//4. VIEW
else if(action.equalsIgnoreCase("VIEW")){
	 Hashtable ht = new Hashtable();
	  Map map  = dslutil.getDestinationDetails(sCOUNTRY,sPRD_CLS_ID);
	   if(map.size()>0){
		   sCOUNTRY   = strUtils.fString((String)map.get("COUNTRYNAME"));
		   sPRD_CLS_ID   = strUtils.fString((String)map.get("PRD_CLS_ID"));
		   sSTATUS   = strUtils.fString((String)map.get("STATUS"));
		   
		   sUpdateEnb  = "enabled";   
		   sSaveEnb  = "disabled";
             
        } else {
          res = "<font class = "+MDbConstant.FAILED_COLOR +">Country Details Not Found</font>";        
}
}

//5. CLEAR
else if(action.equalsIgnoreCase("CLEAR")){
	   sCOUNTRY   = "";
	   sPRD_CLS_ID   = "";
	   sSTATUS = "";
}

%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="DSLUtil.java" >
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">DSL MASTER</FONT>&nbsp;
      </TH>
    </TR>
  
   <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > *Country&nbsp;: &nbsp;&nbsp;&nbsp;&nbsp;</TH>
         <TD>
                <INPUT name="COUNTRY" type = "TEXT" value="<%=sCOUNTRY%>" size="50" >&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="View" onClick="onView();">
         </TD>
    </TR>
    
     <TR>
   <TH WIDTH="35%" ALIGN="RIGHT">*Prd_Cls_Id&nbsp;: &nbsp;&nbsp;&nbsp;&nbsp;</th>
   <TD>
   <input name="PRD_CLS_ID" type="TEXT" value="<%=sPRD_CLS_ID %>"  size="50" >
   </TD>
   </TR>
    
    <TR>       
       <TH WIDTH="35%" ALIGN="RIGHT" >*Status&nbsp; :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <TD>
            <SELECT name ="STATUS" value="<%=sSTATUS %>" size="1" onchange="show()">
             <% String flag = sSTATUS;
            if(flag.equals(null) || flag.equals("")){%>
               <OPTION selected value="INCOMPLETE">INCOMPLETE </OPTION>
                <% }else if(flag.equals("INCOMPLETE")) {%>
                <OPTION selected value="INCOMPLETE">INCOMPLETE </OPTION>
                <option value="COMPLETED">COMPLETED </option>

              <% }else if(flag.equals("COMPLETED")) {%>
                <OPTION selected value="COMPLETED">COMPLETED </OPTION>
                <option value="INCOMPLETE">INCOMPLETE</option>
              <%} %>
            </SELECT>
        </TD>
   </TR>
  
    <TR>
         <TD COLSPAN = 2><BR><B> <CENTER> <%=res%> </B></TD>
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