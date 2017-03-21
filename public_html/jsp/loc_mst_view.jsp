<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>

<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Location  Master</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">
function popWin(URL) {
 subWin = window.open(URL, 'lOCATION', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "loc_mst_view.jsp?action=NEW";
   document.form1.submit();
}
function onAdd(){
   var LOC_GRP_ID   = document.form1.LOC_GRP_ID.value;
  var LOC_ID   = document.form1.LOC_ID.value;
  
   if(LOC_ID == "" || LOC_ID == null) {alert("Please Enter Loc ID "); return false; }
   if(LOC_GRP_ID == "" || LOC_GRP_ID == null) {alert("Please Choose Loc group Id"); return false; }
   if(isNaN(document.form1.SPACE_REM.value)) {alert("Please enter valid SPACE_REM."); document.form1.SPACE_REM.focus(); return false;}
   document.form1.action  = "loc_mst_view.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
   var LOC_GRP_ID   = document.form1.LOC_GRP_ID.value;
   var LOC_ID   = document.form1.LOC_ID.value;
    if(LOC_ID == "" || LOC_ID == null) {alert("Please Enter Loc ID "); return false; }
   if(LOC_GRP_ID == "" || LOC_GRP_ID == null) {alert("Please Enter Loc group Id"); return false; }
   if(isNaN(document.form1.SPACE_REM.value)) {alert("Please enter valid SPACE_REM."); document.form1.SPACE_REM.focus(); return false;}
   
   document.form1.action  = "loc_mst_view.jsp?action=UPDATE";
   document.form1.submit();
}
function onDelete(){
   var LOC_ID   = document.form1.LOC_ID.value;
   if(LOC_ID == "" || LOC_ID == null) {alert("Please Enter Loc  Id");  return false; }

   document.form1.action  = "loc_mst_view.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
    var LOC_ID   = document.form1.LOC_ID.value;
   if(LOC_ID == "" || LOC_ID == null) {alert("Please Enter Loc Id "); return false; }

   document.form1.action  = "loc_mst_view.jsp?action=VIEW";
   document.form1.submit();
}

</script>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String res        = "";

String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "disabled";
String sUpdateEnb = "enabled";

String action     =   "";
String sLocId ="",sLocGrpId  =   "",
       sLocDesc  = "",space_rem="";


StrUtils strUtils = new StrUtils();
LocMstUtil locUtil = new LocMstUtil();
action            = strUtils.fString(request.getParameter("action"));

sLocId  = strUtils.fString(request.getParameter("LOC_ID"));
sLocGrpId  = strUtils.fString(request.getParameter("LOC_GRP_ID"));
if(sLocId.length() <= 0) sLocId  = strUtils.fString(request.getParameter("LOC_ID1"));
if(sLocGrpId.length() <= 0) sLocGrpId  = strUtils.fString(request.getParameter("LOC_GRP_ID1"));
sLocDesc  = strUtils.fString(request.getParameter("LOC_DESC"));
space_rem  = strUtils.fString(request.getParameter("SPACE_REM"));


//1. >> New
if(action.equalsIgnoreCase("NEW")){
      sLocId  = "";
      sLocDesc  = "";
      sLocGrpId ="";
      space_rem="100";
      sAddEnb    = "enabled";
  
}

//2. >> Add
else if(action.equalsIgnoreCase("ADD")){
    LocGrpMstUtil locGrpUtil =  new LocGrpMstUtil();
    Hashtable htlocGrp = new Hashtable();
    htlocGrp.put(MDbConstant.LOC_GRP_ID,sLocGrpId);
    if((locGrpUtil.isExistsLocGrpId(htlocGrp))){
   
   Hashtable ht = new Hashtable();
   ht.put(MDbConstant.LOC_ID,sLocId);
    if(!(locUtil.isExistsLocId(ht))) // if the Loc  exists already
    {
          ht.put(MDbConstant.LOC_GRP_ID,sLocGrpId);
          ht.put(MDbConstant.LOC_DESC,sLocDesc);      
           ht.put(MDbConstant.SPACE_REM,"100");
          ht.put(MDbConstant.CREATED_AT,new DateUtils().getDateTime());
          ht.put(MDbConstant.LOGIN_USER,sUserId);
          boolean LocInserted = locUtil.insertLocMst(ht);
          if(LocInserted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Location  Added Successfully</font>";
                    sAddEnb  = "disabled";
 
                    mv.insertMovHisLogger(sUserId, "Location_Master","Location  :"  + sLocId +"  Added Successfully " ); 

                 
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to add New Location </font>";
                    sAddEnb  = "enabled";
                   
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Location  Exists already. Try again</font>";
           sAddEnb = "enabled";
          
    }
    }else{
    res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter Valid Loc Grp Id. Try again</font>";
           sAddEnb = "enabled";
          
    }
    
 //   mv.insertMovHisLogger(sUserId, "Location_Master", res); 
}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
   
    sAddEnb  = "disabled";
    LocGrpMstUtil locGrpUtil =  new LocGrpMstUtil();
    Hashtable htlocGrp = new Hashtable();
    htlocGrp.put(MDbConstant.LOC_GRP_ID,sLocGrpId);
    if((locGrpUtil.isExistsLocGrpId(htlocGrp))){
    Hashtable ht = new Hashtable();
    ht.put(MDbConstant.LOC_ID,sLocId);
    if((locUtil.isExistsLocId(ht)))
    {
          Hashtable htUpdate = new Hashtable();
          htUpdate.put(MDbConstant.LOC_DESC,sLocDesc);
          htUpdate.put(MDbConstant.LOC_GRP_ID,sLocGrpId);
          htUpdate.put(MDbConstant.UPDATED_AT,new DateUtils().getDateTime());
          htUpdate.put(MDbConstant.UPDATED_BY,sUserId);
          htUpdate.put(MDbConstant.SPACE_REM,space_rem);
          
          
          Hashtable htCondition = new Hashtable();
          htCondition.put(MDbConstant.LOC_ID,sLocId);     
          boolean Updated = locUtil.updateLocId(htUpdate,htCondition);
          if(Updated) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +" >Location  Updated Successfully</font>";
         
          mv.insertMovHisLogger(sUserId, "Location_Master","Location  :"  + sLocId +"  Updated Successfully " ); 

         
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +" >Failed to Update Location </font>";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Location  doesn't not Exists. Try again</font>";

    }
    }else{
     res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter Valid Loc Grp Id . Try again</font>";
    }
    
  //   mv.insertMovHisLogger(sUserId, "Location_Master", res); 
}

//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
    Hashtable htDelete = new Hashtable();
    htDelete.put(MDbConstant.LOC_ID,sLocId);
    if(locUtil.isExistsLocId(htDelete))
    {
          boolean LocDeleted = locUtil.deleteLocId(htDelete);
          if(LocDeleted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Location  Deleted Successfully</font>";
                    sAddEnb    = "disabled";
                    
           mv.insertMovHisLogger(sUserId, "Location_Master","Location  :"  + sLocId +"  Deleted Successfully " ); 
       
                    sLocId  = "";
                    sLocDesc  = "";
                    sLocGrpId ="";


          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to Delete Location </font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Location  doesn't not Exists. Try again</font>";
    }
 //   mv.insertMovHisLogger(sUserId, "Location_Master", res); 
}

//4. >> View
else if(action.equalsIgnoreCase("VIEW")){
    Map map = locUtil.getLocIdDetails(sLocId);
  if(map.size()>0){
    sLocId   = strUtils.fString((String)map.get("LOC_ID"));
    sLocDesc   = strUtils.fString((String)map.get("LOC_DESC"));
    sLocGrpId   = strUtils.fString((String)map.get("LOCGRPID"));
    space_rem   = strUtils.fString((String)map.get("SPACE_REM"));
    }

}

%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">LOCATION MASTER</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Loc Id : </TH>
         <TD>
                <INPUT name="LOC_ID" type = "TEXT" value="<%=sLocId%>" size="50"  MAXLENGTH=20 >
                <INPUT type = "hidden" name="LOC_ID1" value = "">
                 <INPUT type = "hidden" name="LOC_GRP_ID1" value = "">
			<!--	<a href="#" onClick="javascript:popUpWin('loc_mst_list.jsp?LOC_ID='+form1.LOC_ID.value);"><img src="images/populate.gif" border="0"></a>-->
         <a href="#" onClick="javascript:popWin('listView/loc_mst_list.jsp?LOC_ID='+form1.LOC_ID.value);"><img src="images/populate.gif" border="0"></a> 
                <INPUT class="Submit" type="BUTTON" value="View" onClick="onView();">
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > Description : </TH>
         <TD>
             <INPUT name="LOC_DESC" type = "TEXT" value="<%=sLocDesc%>" size="50"  MAXLENGTH=80>

         </TD>

    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Loc Grp Id : </TH>
         <TD>
             <INPUT name="LOC_GRP_ID" type = "TEXT" value="<%=sLocGrpId%>" size="50"  MAXLENGTH=80>
  <a href="#" onClick="javascript:popWin('listView/prd_loc_grp_list.jsp?LOC_GRP_ID='+form1.LOC_GRP_ID.value);"><img src="images/populate.gif" border="0"></a> 
         </TD>

    </TR>


    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Space Remaining : </TH>
         <TD>
           <INPUT name="SPACE_REM" type = "TEXT" value="<%=space_rem%>" size="50"  MAXLENGTH=80>
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

