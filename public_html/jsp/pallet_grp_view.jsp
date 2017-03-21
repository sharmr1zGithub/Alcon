<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>

<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Pallet Group  Master</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">
function popWin(URL) {
 subWin = window.open(URL, 'Pallet', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "pallet_grp_view.jsp?action=NEW";
   document.form1.submit();
}
function onAdd(){
 
   var SHIP_TO   = document.form1.SHIP_TO.value;
   var TRAV_PFX   = document.form1.TRAV_PFX.value;
    var PALLET_GRP   = document.form1.PALLET_GRP.value;
    var PRD_CLS_ID = document.form1.PRD_CLS_ID.value;
  
  
   if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter Ship To"); return false; }
    if(TRAV_PFX == "" || TRAV_PFX == null) {alert("Please Enter Traveler Prefix"); return false; }
     if(PALLET_GRP == "" || PALLET_GRP == null) {alert("Please Enter Pallet Group "); return false; }
       if(PRD_CLS_ID == "" || PRD_CLS_ID == null) {alert("Please Enter Product Class "); return false; }

   document.form1.action  = "pallet_grp_view.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
     var SHIP_TO   = document.form1.SHIP_TO.value;
     var TRAV_PFX   = document.form1.TRAV_PFX.value;
     var PALLET_GRP   = document.form1.PALLET_GRP.value;
  var PRD_CLS_ID = document.form1.PRD_CLS_ID.value;
  
  
     if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter Ship To"); return false; }
     if(TRAV_PFX == "" || TRAV_PFX == null) {alert("Please Enter Traveler Prefix"); return false; }
     if(PALLET_GRP == "" || PALLET_GRP == null) {alert("Please Enter Pallet Group "); return false; }
   if(PRD_CLS_ID == "" || PRD_CLS_ID == null) {alert("Please Enter Product Class "); return false; }
   document.form1.action  = "pallet_grp_view.jsp?action=UPDATE";
   document.form1.submit();
}
function onDelete(){
    var SHIP_TO   = document.form1.SHIP_TO.value;
    var TRAV_PFX   = document.form1.TRAV_PFX.value;
    var PALLET_GRP   = document.form1.PALLET_GRP.value;
  var PRD_CLS_ID = document.form1.PRD_CLS_ID.value;
  
   if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter Ship To"); return false; }
    if(TRAV_PFX == "" || TRAV_PFX == null) {alert("Please Enter Traveler Prefix"); return false; }
     if(PALLET_GRP == "" || PALLET_GRP == null) {alert("Please Enter Pallet Group "); return false; }
      if(PRD_CLS_ID == "" || PRD_CLS_ID == null) {alert("Please Enter Product Class "); return false; }

   document.form1.action  = "pallet_grp_view.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
    var SHIP_TO   = document.form1.SHIP_TO.value;
   var TRAV_PFX   = document.form1.TRAV_PFX.value;
    var PALLET_GRP   = document.form1.PALLET_GRP.value;
    var PRD_CLS_ID = document.form1.PRD_CLS_ID.value;
  
   if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter Ship To"); return false; }
    if(TRAV_PFX == "" || TRAV_PFX == null) {alert("Please Enter Traveler Prefix"); return false; }
     if(PALLET_GRP == "" || PALLET_GRP == null) {alert("Please Enter Pallet Group "); return false; }
 if(PRD_CLS_ID == "" || PRD_CLS_ID == null) {alert("Please Enter Product Class "); return false; }
   document.form1.action  = "pallet_grp_view.jsp?action=VIEW";
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
String sPalletGrp ="",sShipTo  =   "",sPrdClsId,
       sTravPfx  = "";


StrUtils strUtils = new StrUtils();
PalletGrpUtil palletGrpUtil = new PalletGrpUtil();
action            = strUtils.fString(request.getParameter("action"));

sPalletGrp  = strUtils.fString(request.getParameter("PALLET_GRP"));
sShipTo  = strUtils.fString(request.getParameter("SHIP_TO"));
if(sShipTo.length() <= 0) sShipTo  = strUtils.fString(request.getParameter("SHIP_TO1"));
//if(sLocGrpId.length() <= 0) sLocGrpId  = strUtils.fString(request.getParameter("LOC_GRP_ID1"));
sPrdClsId  = strUtils.fString(request.getParameter("PRD_CLS_ID"));
if(sPrdClsId.length() <= 0) sPrdClsId  = strUtils.fString(request.getParameter("PRD_CLS_ID1"));
sTravPfx  = strUtils.fString(request.getParameter("TRAV_PFX"));



//1. >> New
if(action.equalsIgnoreCase("NEW")){
      sPalletGrp  = "";
      sShipTo  = "";
      sTravPfx ="";
      sPrdClsId ="";
      sAddEnb    = "enabled";
  
}

//2. >> Add
else if(action.equalsIgnoreCase("ADD")){

    PrdClsUtil prdClsUtil =  new PrdClsUtil();
    Hashtable htprdCls = new Hashtable();
    htprdCls.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
    if((prdClsUtil.isExistsPrdClsId(htprdCls))){
    
    Hashtable ht = new Hashtable();
    ht.put(MDbConstant.PALLET_GRP,sPalletGrp);
    ht.put(MDbConstant.SHIP_TO,sShipTo);
    ht.put(MDbConstant.TRAV_PFX,sTravPfx);
    ht.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
    if(!(palletGrpUtil.isExistsPalletGrp(ht))) // if the Loc  exists already
    {
          ht.put(MDbConstant.CREATED_AT,new DateUtils().getDateTime());
          ht.put(MDbConstant.LOGIN_USER,sUserId);
          boolean Inserted = palletGrpUtil.insertPalletGrp(ht);
          if(Inserted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Pallet Group  Added Successfully</font>";
                    sAddEnb  = "disabled";
                    
                    mv.insertMovHisLogger(sUserId, "Pallet_Group","Pallet_Group   :"  + sPalletGrp +"  Added Successfully " ); 

                 
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to add New Pallet Group </font>";
                    sAddEnb  = "enabled";
                   
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Pallet Group  Exists already. Try again</font>";
           sAddEnb = "enabled";
          
    }
       }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter Valid Product Class  . Try again</font>";
           sAddEnb = "enabled";
          
    }
    
    mv.insertMovHisLogger(sUserId, "Pallet_Group", res); 
}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
   
    sAddEnb  = "disabled";
    PrdClsUtil prdClsUtil =  new PrdClsUtil();
    Hashtable htprdCls = new Hashtable();
    htprdCls.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
    if((prdClsUtil.isExistsPrdClsId(htprdCls))){
    
    Hashtable ht = new Hashtable();
    ht.put(MDbConstant.PALLET_GRP,sPalletGrp);
    ht.put(MDbConstant.SHIP_TO,sShipTo);
    ht.put(MDbConstant.TRAV_PFX,sTravPfx);
     ht.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
    if((palletGrpUtil.isExistsPalletGrp(ht)))
    {
         
       //   ht.put(MDbConstant.UPDATED_AT,new DateUtils().getDateTime());
         // ht.put(MDbConstant.UPDATED_BY,sUserId);
          
          Hashtable htCondition = new Hashtable();
         
          htCondition.put(MDbConstant.PALLET_GRP,sPalletGrp);
          htCondition.put(MDbConstant.SHIP_TO,sShipTo);
          htCondition.put(MDbConstant.TRAV_PFX,sTravPfx);
           htCondition.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
          boolean Updated = palletGrpUtil.updatePalletGrp(ht,htCondition);
          if(Updated) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +" >Pallet Group  Updated Successfully</font>";
                    
          mv.insertMovHisLogger(sUserId, "Pallet_Group","Pallet_Group   :"  + sPalletGrp +"  Updated Successfully " ); 

          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +" >Failed to Update Pallet Group </font>";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Pallet Group  doesn't not Exists. Try again</font>";

    }
     }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter Valid Product Class  . Try again</font>";

    }
    mv.insertMovHisLogger(sUserId, "Pallet_Group", res); 
}

//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
   
    Hashtable ht = new Hashtable();
    ht.put(MDbConstant.PALLET_GRP,sPalletGrp);
    ht.put(MDbConstant.SHIP_TO,sShipTo);
    ht.put(MDbConstant.TRAV_PFX,sTravPfx);
     ht.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
    if(palletGrpUtil.isExistsPalletGrp(ht))
    {
          boolean Deleted = palletGrpUtil.deletePalletGrp(ht);
          if(Deleted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Pallet Group  Deleted Successfully</font>";
                    sAddEnb    = "disabled";
                    
                    mv.insertMovHisLogger(sUserId, "Pallet_Group","Pallet_Group   :"  + sPalletGrp +"  Deleted Successfully " ); 


                    sPalletGrp  = "";
                    sShipTo  = "";
                    sPrdClsId ="";
                    sTravPfx ="";


          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to Delete Pallet Group </font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Pallet Group  doesn't not Exists. Try again</font>";
    }
    mv.insertMovHisLogger(sUserId, "Pallet_Group", res);
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
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">PALLET GROUP MASTER</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
    
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Ship To : </TH>
         <TD>
             <INPUT name="SHIP_TO" type = "TEXT" value="<%=sShipTo%>" size="50"  MAXLENGTH=80>       
<a href="#" onClick="javascript:popWin('listView/ship_to_list.jsp?SHIP_TO='+form1.SHIP_TO.value);"><img src="images/populate.gif" border="0"></a> 
<INPUT type = "hidden" name="SHIP_TO1" value = "">
              <INPUT type = "hidden" name="DESTINATION" value = "">
       
 <!--<INPUT class="Submit" type="BUTTON" value="View" onClick="onView();">-->
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
      <TH WIDTH="35%" ALIGN="RIGHT">* Pallet Group :</TH>
      <TD>
        <INPUT name="PALLET_GRP" type="TEXT" value="<%=sPalletGrp%>" size="50" MAXLENGTH="20"/>
        <!--        &lt;a href=&quot;#&quot; onClick=&quot;javascript:popWin('listView/dest_mst_list.jsp?DESTINATION='+form1.DESTINATION.value);&quot;&gt;&lt;img src=&quot;images/populate.gif&quot; border=&quot;0&quot;&gt;&lt;/a&gt; -->
      </TD>
    </TR>
<TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Product Class : </TH>
         <TD>
                <INPUT name="PRD_CLS_ID" type = "TEXT" value="<%=sPrdClsId%>" size="50"  MAXLENGTH=20 >
                     <a href="#" onClick="javascript:popWin('listView/prd_cls_list.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><img src="images/populate.gif" border="0"></a> 
                         <INPUT type = "hidden" name="PRD_CLS_ID1" value = ""> 
                 <INPUT type = "hidden" name="PRD_CLS_DESC" value = "">
                  <INPUT type = "hidden" name="PRD_GRP_ID" value = "">
               
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

