<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ include file="header.jsp"%>

<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Reason  Master</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "ReasonMaster.jsp?action=NEW";
   document.form1.submit();
}
function onAdd(){
   var ITEM_ID   = document.form1.ITEM_ID.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Reason Code "); return false; }
   document.form1.action  = "ReasonMaster.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
 
    var ITEM_ID   = document.form1.ITEM_ID.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Reason Code "); return false; }

   
   document.form1.action  = "ReasonMaster.jsp?action=UPDATE";
   document.form1.submit();
}
function onDelete(){
      var ITEM_ID   = document.form1.ITEM_ID.value;
      if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Reason Code "); return false; }
   document.form1.action  = "ReasonMaster.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
    var ITEM_ID   = document.form1.ITEM_ID.value;
   if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Reason Code "); return false; }

   document.form1.action  = "ReasonMaster.jsp?action=VIEW";
   document.form1.submit();
}

-->
</script>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "";

String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "disabled";
String sUpdateEnb = "enabled";

String action     =   "";
String sItemId ="",sPrdClsId  =   "",
       sItemDesc  = "";


StrUtils strUtils = new StrUtils();
RsnMstUtil rsnUtil = new RsnMstUtil();
action            = strUtils.fString(request.getParameter("action"));

sItemId  = strUtils.fString(request.getParameter("ITEM_ID"));
sItemDesc  = strUtils.fString(request.getParameter("ITEM_DESC"));
if(sItemId.length() <= 0) sItemId  = strUtils.fString(request.getParameter("ITEM_ID1"));



//1. >> New
if(action.equalsIgnoreCase("NEW")){
      sItemId  = "";
      sItemDesc  = "";
      sPrdClsId ="";
      sAddEnb    = "enabled";
     


}

//2. >> Add
else if(action.equalsIgnoreCase("ADD")){

   Hashtable ht = new Hashtable();
   ht.put(MDbConstant.PLANT,sPlant);
   ht.put(MDbConstant.RSNCODE,sItemId);
    if(!(rsnUtil.isExistsResaonCode(ht))) // if the Item  exists already
    {
          
          ht.put(MDbConstant.RSNCODE,sItemId);
          ht.put(MDbConstant.RSNDESC,sItemDesc);      
          ht.put(MDbConstant.CREATED_AT,new DateUtils().getDateTime());
          ht.put(MDbConstant.LOGIN_USER,sUserId);
         
         
          boolean itemInserted = rsnUtil.insertRsnMst(ht);
          if(itemInserted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Reason  Added Successfully</font>";
                    sAddEnb  = "disabled";
                 
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to add New Reason </font>";
                    sAddEnb  = "enabled";
                   
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">ReasonCode  Exists already. Try again</font>";
           sAddEnb = "enabled";
          
    }
 
}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
   
    sAddEnb  = "disabled";
  

    Hashtable ht = new Hashtable();
    ht.put(MDbConstant.RSNCODE,sItemId);
    if((rsnUtil.isExistsResaonCode(ht)))
    {
          Hashtable htUpdate = new Hashtable();
          htUpdate.put(MDbConstant.RSNCODE,sItemId);
          htUpdate.put(MDbConstant.RSNDESC,sItemDesc);   
          htUpdate.put(MDbConstant.UPDATED_AT,new DateUtils().getDateTime());
          htUpdate.put(MDbConstant.UPDATED_BY,sUserId);
          
          Hashtable htCondition = new Hashtable();
          htCondition.put(MDbConstant.RSNCODE,sItemId);     
          boolean Updated = rsnUtil.updateReasonMst(htUpdate,htCondition);
          if(Updated) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +" >ReasonCode  Updated Successfully</font>";
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +" >Failed to Update ReasonCode </font>";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">ReasonCode  doesn't not Exists. Try again</font>";

    }
    
    
}

//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
    Hashtable htDelete = new Hashtable();
   htDelete.put(MDbConstant.RSNCODE,sItemId);
    if(rsnUtil.isExistsResaonCode(htDelete))
    {
          boolean itemDeleted = rsnUtil.deleteReasonMstId(htDelete);
          if(itemDeleted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">ReasonCode  Deleted Successfully</font>";
                    sAddEnb    = "disabled";
                    sItemId  = "";
                    sItemDesc  = "";
                    sPrdClsId ="";


          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to Delete ReasonCode </font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">ReasonCode doesn't not Exists. Try again</font>";
    }
}

//4. >> View
else if(action.equalsIgnoreCase("VIEW")){
    Map map  = rsnUtil.getReasonDetails(sItemId);
   if(map.size()>0){
    sItemId   = strUtils.fString((String)map.get("RSNCODE"));
    sItemDesc   = strUtils.fString((String)map.get("RSNDESC"));
  
    }else{
     res = "<font class = "+MDbConstant.FAILED_COLOR +">Item doesn't not Exists. Try again</font>";
    }

}

%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">REASON MASTER</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Reason Code: </TH>
         <TD>
                <INPUT name="ITEM_ID" type = "TEXT" value="<%=sItemId%>" size="50"  MAXLENGTH=20 >
                <INPUT type = "hidden" name="ITEM_ID1" value = "" >
				<a href="#" onClick="javascript:popWin('listView/ReasonMstList.jsp?ITEM_ID='+form1.ITEM_ID.value);"><img src="images/populate.gif" border="0"></a> 
                <INPUT class="Submit" type="BUTTON" value="View" onClick="onView();">
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Reason Description : </TH>
         <TD>
             <INPUT name="ITEM_DESC" type = "TEXT" value="<%=sItemDesc%>" size="50"  MAXLENGTH=80>

         </TD>

    </TR>
   

    <TR>
         <TD COLSPAN = 2><BR><B><CENTER><%=res%></B></TD>
    </TR>
    <TR>
         <TD COLSPAN = 2><center>
                <INPUT class="Submit" type="BUTTON" value="Cancel" onClick="window.location.href='indexPage.jsp'">&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="New" onClick="onNew();" <%=sNewEnb%>>&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="Save" onClick="onAdd();" <%=sAddEnb%>>&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="Update" onClick="onUpdate();" <%=sUpdateEnb%>>&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="Delete" onClick="onDelete();" <%=sDeleteEnb%>>
         </TD>
    </TR>
</TABLE>
</CENTER>

</FORM>
</BODY>
</HTML>
<%@ include file="footer.jsp"%>


