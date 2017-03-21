<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>


<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Destination  Master</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">
function popWin(URL) {
 subWin = window.open(URL, 'Destination', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "destination_mst_view.jsp?action=NEW";
   document.form1.submit();
}
function onAdd(){
   var DESTINATION   = document.form1.DESTINATION.value;
   var SHIP_TO   = document.form1.SHIP_TO.value;
   var TRAV_PFX   = document.form1.TRAV_PFX.value;
  
   if(DESTINATION == "" || DESTINATION == null) {alert("Please Enter Destination "); return false; }
   if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter Ship To"); return false; }
    if(TRAV_PFX == "" || TRAV_PFX == null) {alert("Please Enter Traveler Prefix"); return false; }

   document.form1.action  = "destination_mst_view.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
    var DESTINATION   = document.form1.DESTINATION.value;
   var SHIP_TO   = document.form1.SHIP_TO.value;
   var TRAV_PFX   = document.form1.TRAV_PFX.value;
  
   if(DESTINATION == "" || DESTINATION == null) {alert("Please Enter Destination "); return false; }
   if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter Ship To"); return false; }
    if(TRAV_PFX == "" || TRAV_PFX == null) {alert("Please Enter Traveler Prefix"); return false; }
  
   document.form1.action  = "destination_mst_view.jsp?action=UPDATE";
   document.form1.submit();
}
function onDelete(){
    var DESTINATION   = document.form1.DESTINATION.value;
    var SHIP_TO   = document.form1.SHIP_TO.value;
    var TRAV_PFX   = document.form1.TRAV_PFX.value;
  
     if(DESTINATION == "" || DESTINATION == null) {alert("Please Enter Destination "); return false; }
     if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter Ship To"); return false; }
     if(TRAV_PFX == "" || TRAV_PFX == null) {alert("Please Enter Traveler Prefix"); return false; }

   document.form1.action  = "destination_mst_view.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
   var DESTINATION   = document.form1.DESTINATION.value;
    var SHIP_TO   = document.form1.SHIP_TO.value;
    var TRAV_PFX   = document.form1.TRAV_PFX.value;
  
     if(DESTINATION == "" || DESTINATION == null) {alert("Please Enter Destination "); return false; }
     if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter Ship To"); return false; }
     if(TRAV_PFX == "" || TRAV_PFX == null) {alert("Please Enter Traveler Prefix"); return false; }

   document.form1.action  = "destination_mst_view.jsp?action=VIEW";
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
String sDestn ="",sShipTo  =   "",
       sTravPfx  = "";


StrUtils strUtils = new StrUtils();
LocMstUtil locUtil = new LocMstUtil();
action            = strUtils.fString(request.getParameter("action"));

sDestn  = strUtils.fString(request.getParameter("DESTINATION"));
sShipTo  = strUtils.fString(request.getParameter("SHIP_TO"));
if(sDestn.length() <= 0) sDestn  = strUtils.fString(request.getParameter("DESTINATION1"));
//if(sLocGrpId.length() <= 0) sLocGrpId  = strUtils.fString(request.getParameter("LOC_GRP_ID1"));
sTravPfx  = strUtils.fString(request.getParameter("TRAV_PFX"));



//1. >> New
if(action.equalsIgnoreCase("NEW")){
      sDestn  = "";
      sShipTo  = "";
      sTravPfx ="";
      sAddEnb    = "enabled";
  
}

//2. >> Add
else if(action.equalsIgnoreCase("ADD")){
    DestinationUtil destUtil =  new DestinationUtil();
    
    Hashtable ht = new Hashtable();
    ht.put(MDbConstant.DESTINATION,sDestn);
    ht.put(MDbConstant.SHIP_TO,sShipTo);
    ht.put(MDbConstant.TRAV_PFX,sTravPfx);
    if(!(destUtil.isExistsDest(ht))) // if the Loc  exists already
    {
          ht.put(MDbConstant.CREATED_AT,new DateUtils().getDateTime());
          ht.put(MDbConstant.LOGIN_USER,sUserId);
          boolean Inserted = destUtil.insertDest(ht);
          if(Inserted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Destination  Added Successfully</font>";
                    sAddEnb  = "disabled";
         
         
               mv.insertMovHisLogger(sUserId, "Destination_Master","Destination   :"  + sDestn +"  Added Successfully " ); 
        
         
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to add New Destination </font>";
                    sAddEnb  = "enabled";
                   
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Destination  Exists already. Try again</font>";
           sAddEnb = "enabled";
          
    }
  //  mv.insertMovHisLogger(sUserId, "Destination_Master", res); 
}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
   
    sAddEnb  = "disabled";
   DestinationUtil destUtil =  new DestinationUtil();
   
    Hashtable ht = new Hashtable();
    ht.put(MDbConstant.DESTINATION,sDestn);
    ht.put(MDbConstant.SHIP_TO,sShipTo);
    ht.put(MDbConstant.TRAV_PFX,sTravPfx);
    if((destUtil.isExistsDest(ht)))
    {
         
       //   ht.put(MDbConstant.UPDATED_AT,new DateUtils().getDateTime());
         // ht.put(MDbConstant.UPDATED_BY,sUserId);
          
          Hashtable htCondition = new Hashtable();
         
          htCondition.put(MDbConstant.DESTINATION,sDestn);
          htCondition.put(MDbConstant.SHIP_TO,sShipTo);
          htCondition.put(MDbConstant.TRAV_PFX,sTravPfx);
          boolean Updated = destUtil.updateDest(ht,htCondition);
          if(Updated) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +" >Destination  Updated Successfully</font>";
                    
          mv.insertMovHisLogger(sUserId, "Destination_Master","Destination   :"  + sDestn +"  Updated Successfully " ); 
        
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +" >Failed to Update Destination </font>";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Destination  doesn't not Exists. Try again</font>";

    }
 //  mv.insertMovHisLogger(sUserId, "Destination_Master", res);
}

//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
    DestinationUtil destUtil =  new DestinationUtil();
    Hashtable ht = new Hashtable();
    ht.put(MDbConstant.DESTINATION,sDestn);
    ht.put(MDbConstant.SHIP_TO,sShipTo);
    ht.put(MDbConstant.TRAV_PFX,sTravPfx);
    if(destUtil.isExistsDest(ht))
    {
          boolean Deleted = destUtil.deleteLocId(ht);
          if(Deleted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Destination  Deleted Successfully</font>";
                    sAddEnb    = "disabled";
                    
                     mv.insertMovHisLogger(sUserId, "Destination_Master","Destination   :"  + sDestn +"  Deleted Successfully " ); 
        
                    sDestn  = "";
                    sShipTo  = "";
                    sTravPfx ="";


          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to Delete Destination </font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Destination  doesn't not Exists. Try again</font>";
    }
 //   mv.insertMovHisLogger(sUserId, "Destination_Master", res);
}

//4. >> View
/*else if(action.equalsIgnoreCase("VIEW")){
    Map map = locUtil.getLocIdDetails(sLocId);
  if(map.size()>0){
    sLocId   = strUtils.fString((String)map.get("LOC_ID"));
    sLocDesc   = strUtils.fString((String)map.get("LOC_DESC"));
    sLocGrpId   = strUtils.fString((String)map.get("LOCGRPID"));
    }

}*/

%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">DESTINATION MASTER</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Destination : </TH>
         <TD>
                <INPUT name="DESTINATION" type = "TEXT" value="<%=sDestn%>" size="50"  MAXLENGTH=20 >
               <INPUT type = "hidden" name="DESTINATION1" value = "">
              <!--   <INPUT type = "hidden" name="LOC_GRP_ID1" value = "">
				<a href="#" onClick="javascript:popUpWin('loc_mst_list.jsp?LOC_ID='+form1.LOC_ID.value);"><img src="images/populate.gif" border="0"></a>-->
        <a href="#" onClick="javascript:popWin('listView/destination_list.jsp?DESTINATION='+form1.DESTINATION.value);"><img src="images/populate.gif" border="0"></a> 
                <!--<INPUT class="Submit" type="BUTTON" value="View" onClick="onView();">-->
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Ship To : </TH>
         <TD>
             <INPUT name="SHIP_TO" type = "TEXT" value="<%=sShipTo%>" size="50"  MAXLENGTH=80>

         </TD>

    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Traveler Prefix : </TH>
         <TD>
             <INPUT name="TRAV_PFX" type = "TEXT" value="<%=sTravPfx%>" size="50"  MAXLENGTH=80>
 <!--  <a href="#" onClick="javascript:popWin('listView/prd_loc_grp_list.jsp?LOC_GRP_ID='+form1.LOC_GRP_ID.value);"><img src="images/populate.gif" border="0"></a> -->
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

