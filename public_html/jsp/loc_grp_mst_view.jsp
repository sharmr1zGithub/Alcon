<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>

<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Location Group Master</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">


function popWin(URL) {
 subWin = window.open(URL, 'LOCATION', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "loc_grp_mst_view.jsp?action=NEW";
   document.form1.submit();
}
function onAdd(){
   var LOC_GRP_ID   = document.form1.LOC_GRP_ID.value;
    var TOTAL_BAY   = document.form1.TOTAL_BAY.value;
   if(LOC_GRP_ID == "" || LOC_GRP_ID == null) {alert("Please Choose Loc group Id"); return false; }
   if(TOTAL_BAY == "" || TOTAL_BAY == null) {alert("Please Enter Total Bay "); return false; }
   if(isNaN(document.form1.TOTAL_BAY.value)) {alert("Please enter valid TOTAL_BAY."); document.form1.TOTAL_BAY.focus(); return false;}
   
   document.form1.action  = "loc_grp_mst_view.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
   var LOC_GRP_ID   = document.form1.LOC_GRP_ID.value;
   var TOTAL_BAY   = document.form1.TOTAL_BAY.value;
   if(LOC_GRP_ID == "" || LOC_GRP_ID == null) {alert("Please Enter Loc group Id"); return false; }
   if(TOTAL_BAY == "" || TOTAL_BAY == null) {alert("Please Enter Total Bay "); return false; }
    if(isNaN(document.form1.TOTAL_BAY.value)) {alert("Please enter valid TOTAL_BAY."); document.form1.TOTAL_BAY.focus(); return false;}
    
   document.form1.action  = "loc_grp_mst_view.jsp?action=UPDATE";
   document.form1.submit();
}
function onDelete(){
   var LOC_GRP_ID   = document.form1.LOC_GRP_ID.value;
   if(LOC_GRP_ID == "" || LOC_GRP_ID == null) {alert("Please Enter Loc group Id");  return false; }

   document.form1.action  = "loc_grp_mst_view.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
   var LOC_GRP_ID   = document.form1.LOC_GRP_ID.value;
   if(LOC_GRP_ID == "" || LOC_GRP_ID == null) {alert("Please Enter Loc group Id "); return false; }

   document.form1.action  = "loc_grp_mst_view.jsp?action=VIEW";
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
String sLocGrpId  =   "",
       sLocGrpDesc  = "",
       sTotalBay ="";


StrUtils strUtils = new StrUtils();
LocGrpMstUtil locGrpUtil = new LocGrpMstUtil();
action                   = strUtils.fString(request.getParameter("action"));


sLocGrpId  = strUtils.fString(request.getParameter("LOC_GRP_ID"));
if(sLocGrpId.length() <= 0) sLocGrpId  = strUtils.fString(request.getParameter("LOC_GRP_ID1"));
sLocGrpDesc  = strUtils.fString(request.getParameter("LOC_GRP_DESC"));
sTotalBay  = strUtils.fString(request.getParameter("TOTAL_BAY"));


//1. >> New
if(action.equalsIgnoreCase("NEW")){
      sLocGrpId  = "";
      sLocGrpDesc  = "";
      sTotalBay ="";
      sAddEnb    = "enabled";
     


}

//2. >> Add
else if(action.equalsIgnoreCase("ADD")){
   Hashtable ht = new Hashtable();
   ht.put(MDbConstant.LOC_GRP_ID,sLocGrpId);
    if(!(locGrpUtil.isExistsLocGrpId(ht))) // if the Loc Group exists already
    {
          ht.put(MDbConstant.LOC_GRP_DESC,sLocGrpDesc);
          ht.put(MDbConstant.TOTAL_BAY,sTotalBay);
          ht.put(MDbConstant.CREATED_AT,new DateUtils().getDateTime());
          ht.put(MDbConstant.LOGIN_USER,sUserId);
          boolean LocGrpInserted = locGrpUtil.insertLocGrpMst(ht);
          if(LocGrpInserted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Location Group Added Successfully</font>";
                    sAddEnb  = "disabled";

          mv.insertMovHisLogger(sUserId, "Location_Group_Master","Location Group  :"  + sLocGrpId +"  Added Successfully " ); 
           
                 
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to add New Location Group</font>";
                    sAddEnb  = "enabled";
                   
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Location Group Exists already. Try again</font>";
           sAddEnb = "enabled";
          
    }
 //   mv.insertMovHisLogger(sUserId, "Location_Group_Master", res); 
}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
   
    sAddEnb  = "disabled";
    Hashtable ht = new Hashtable();
    ht.put(MDbConstant.LOC_GRP_ID,sLocGrpId);
    if((locGrpUtil.isExistsLocGrpId(ht)))
    {
          Hashtable htUpdate = new Hashtable();
          htUpdate.put(MDbConstant.LOC_GRP_DESC,sLocGrpDesc);
          htUpdate.put(MDbConstant.TOTAL_BAY,sTotalBay);
          htUpdate.put(MDbConstant.UPDATED_AT,new DateUtils().getDateTime());
          htUpdate.put(MDbConstant.UPDATED_BY,sUserId);
          
          Hashtable htCondition = new Hashtable();
          htCondition.put(MDbConstant.LOC_GRP_ID,sLocGrpId);     
          boolean Updated = locGrpUtil.updateLocGrpId(htUpdate,htCondition);
          if(Updated) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +" >Location group Updated Successfully</font>";
       
            mv.insertMovHisLogger(sUserId, "Location_Group_Master","Location Group  :"  + sLocGrpId +"  Updated Successfully " ); 
       
          
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +" >Failed to Update Location group</font>";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Location group doesn't not Exists. Try again</font>";

    }
    // mv.insertMovHisLogger(sUserId, "Location_Group", res);
}

//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
    Hashtable htDelete = new Hashtable();
    htDelete.put(MDbConstant.LOC_GRP_ID,sLocGrpId);
    if(locGrpUtil.isExistsLocGrpId(htDelete))
    {
          boolean LocGrpDeleted = locGrpUtil.deleteLocGrpId(htDelete);
          if(LocGrpDeleted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Location group Deleted Successfully</font>";
                    sAddEnb    = "disabled";
                    
                   mv.insertMovHisLogger(sUserId, "Location_Group_Master","Location Group  :"  + sLocGrpId +"  Deleted Successfully " ); 
              
                    sLocGrpId  = "";
                    sLocGrpDesc  = "";
                    sTotalBay ="";


          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to Delete Location group</font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Location group doesn't not Exists. Try again</font>";
    }
    
 //   mv.insertMovHisLogger(sUserId, "Location_Group", res);
}

//4. >> View
else if(action.equalsIgnoreCase("VIEW")){
    Map map = locGrpUtil.getLocGrpIdDetails(sLocGrpId);
    if(map.size()>0){
    sLocGrpId   = strUtils.fString((String)map.get("LOC_GRP_ID"));
    sLocGrpDesc   = strUtils.fString((String)map.get("LOC_GRP_DESC"));
    sTotalBay   = strUtils.fString((String)map.get("TOTAL_BAY"));
    }else{
    res = "<font class = "+MDbConstant.FAILED_COLOR +">Location group doesn't not Exists. Try again</font>";
    }

}

%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">LOCATION GROUP MASTER</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Loc Grp Id : </TH>
         <TD>
                <INPUT name="LOC_GRP_ID" type = "TEXT" value="<%=sLocGrpId%>" size="50"  MAXLENGTH=20 >
           <INPUT type = "hidden" name="LOC_GRP_ID1" value = "">
			
    
       <a href="#" onClick="javascript:popWin('listView/loc_grp_list.jsp?LOC_GRP_ID='+form1.LOC_GRP_ID.value);"><img src="images/populate.gif" border="0"></a>   
       
                <INPUT class="Submit" type="BUTTON" value="View" onClick="onView();">
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > Description : </TH>
         <TD>
             <INPUT name="LOC_GRP_DESC" type = "TEXT" value="<%=sLocGrpDesc%>" size="50"  MAXLENGTH=80>

         </TD>

    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Total Bay : </TH>
         <TD>
             <INPUT name="TOTAL_BAY" type = "TEXT" value="<%=sTotalBay%>" size="50"  MAXLENGTH=80>

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

