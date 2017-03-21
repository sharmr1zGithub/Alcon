<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>
 
<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Rules Master</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">


function popWin(URL) {
 subWin = window.open(URL, 'List', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "rules_mst_view.jsp?action=NEW";
   document.form1.submit();
}
function onAdd(){
   
    var SHIP_TO          = document.form1.SHIP_TO.value;
    var TRAV_PFX           = document.form1.TRAV_PFX.value;
    var PALLET_GRP       = document.form1.PALLET_GRP.value;
    var PRD_CLS_ID       = document.form1.PRD_CLS_ID.value;
    var TRAY_GRP_ID      = document.form1.TRAY_GRP_ID.value;
    var CATEGORY     = document.form1.CATEGORY.value;

    
    if(SHIP_TO == "" || SHIP_TO == null)                 {alert("Please Enter Ship To. "); return false; }
    if(TRAV_PFX == "" || TRAV_PFX == null)                    {alert("Please Enter Prefix"); return false; }
    if(PRD_CLS_ID == "" || PRD_CLS_ID == null)             {alert("Please Enter Prd Cls Id");  return false; }
    if(PALLET_GRP == "" || PALLET_GRP == null)             {alert("Please Enter Pallet grp");  return false; }
    if(TRAY_GRP_ID == "" || TRAY_GRP_ID == null)             {alert("Please Enter Tray grp");  return false; }
     if(CATEGORY == "" || CATEGORY == null)             {alert("Please Enter Category");  return false; }
   
   

   document.form1.action  = "rules_mst_view.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
    var SHIP_TO          = document.form1.SHIP_TO.value;
    var TRAV_PFX           = document.form1.TRAV_PFX.value;
    var PALLET_GRP       = document.form1.PALLET_GRP.value;
    var PRD_CLS_ID       = document.form1.PRD_CLS_ID.value;
     var TRAY_GRP_ID = document.form1.TRAY_GRP_ID.value;
var CATEGORY     = document.form1.CATEGORY.value;

    
    if(SHIP_TO == "" || SHIP_TO == null)                 {alert("Please Enter Ship To. "); return false; }
    if(TRAV_PFX == "" || TRAV_PFX == null)                    {alert("Please Enter Prefix"); return false; }
    if(PRD_CLS_ID == "" || PRD_CLS_ID == null)             {alert("Please Enter Prd Cls Id");  return false; }
    if(PALLET_GRP == "" || PALLET_GRP == null)             {alert("Please Enter Pallet grp");  return false; }
      if(TRAY_GRP_ID == "" || TRAY_GRP_ID == null)             {alert("Please Enter Tray grp");  return false; }
       if(CATEGORY == "" || CATEGORY == null)             {alert("Please Enter Category");  return false; }
    document.form1.action  = "rules_mst_view.jsp?action=UPDATE";
    document.form1.submit();
}
function onDelete(){
  var SHIP_TO           = document.form1.SHIP_TO.value;
    var PALLET_GRP       = document.form1.PALLET_GRP.value;
    var PRD_CLS_ID          = document.form1.PRD_CLS_ID.value;
     var TRAY_GRP_ID          = document.form1.TRAY_GRP_ID.value;
var CATEGORY     = document.form1.CATEGORY.value;
    
    if(SHIP_TO == "" || SHIP_TO == null)                 {alert("Please Enter Ship To. "); return false; }
    if(PRD_CLS_ID == "" || PRD_CLS_ID == null)             {alert("Please Enter Prd cls Id");  return false; }
    if(PALLET_GRP == "" || PALLET_GRP == null)             {alert("Please Enter Pallet grp");  return false; }
        if(TRAY_GRP_ID == "" || TRAY_GRP_ID == null)             {alert("Please Enter Tray grp");  return false; }
 if(CATEGORY == "" || CATEGORY == null)             {alert("Please Enter Category");  return false; }
   document.form1.action  = "rules_mst_view.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
    var SHIP_TO   = document.form1.SHIP_TO.value;
    if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter/Select Ship To to view ");  return false; }
 var PRD_CLS_ID          = document.form1.PRD_CLS_ID.value;
   if(PRD_CLS_ID == "" || PRD_CLS_ID == null)             {alert("Please Enter Prd cls Id");  return false; }
   document.form1.action  = "rules_mst_view.jsp?action=VIEW";
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
String sShipTo ="",
       sPrefix  =   "",
       sPalletGrp  = "",
       sPrdClsId  =   "",
       sTrayGrpId= "",
       sDestn ="",
       sCategory ="",
       sType  = "";

StrUtils strUtils = new StrUtils();
//PrdGrpMstUtil prdGrpUtil = new PrdGrpMstUtil();
RulesUtil ruleUtil = new RulesUtil();
action            = strUtils.fString(request.getParameter("action"));

sShipTo  = strUtils.fString(request.getParameter("SHIP_TO"));
if(sShipTo.length() <= 0) sShipTo  = strUtils.fString(request.getParameter("SHIP_TO1"));
sDestn  = strUtils.fString(request.getParameter("DESTINATION"));
sPrefix   = strUtils.fString(request.getParameter("TRAV_PFX"));
//if(sLocId.length() <= 0) sLocId  = strUtils.fString(request.getParameter("LOC_ID1"));
sPrdClsId  = strUtils.fString(request.getParameter("PRD_CLS_ID"));
if(sPrdClsId.length() <= 0) sPrdClsId  = strUtils.fString(request.getParameter("PRD_CLS_ID1"));
sPalletGrp  = strUtils.fString(request.getParameter("PALLET_GRP"));
if(sPalletGrp.length() <= 0) sPalletGrp  = strUtils.fString(request.getParameter("PALLET_GRP1"));
sTrayGrpId  = strUtils.fString(request.getParameter("TRAY_GRP_ID"));
if(sTrayGrpId.length() <= 0) sTrayGrpId  = strUtils.fString(request.getParameter("TRAY_GRP_ID1"));
sType  = strUtils.fString(request.getParameter("TYPE"));
sCategory =strUtils.fString(request.getParameter("CATEGORY"));

//if(sRule.length() <= 0) sRule  = strUtils.fString(request.getParameter("RULE1"));


//1. >> New
if(action.equalsIgnoreCase("NEW")){
     sShipTo ="";
       sPrefix  =   "";
       sPalletGrp  = "";
       sPrdClsId  =   "";
       sTrayGrpId ="";
       sType  = "";
      sAddEnb    = "enabled";
      sUpdateEnb = "disabled";
      sDeleteEnb = "disabled";
     


}

//2. >> Add
else if(action.equalsIgnoreCase("ADD")){
    
    sUpdateEnb="enabled";
    sDeleteEnb ="enabled";
    PrdClsUtil prdclsUtil =  new PrdClsUtil();
     Hashtable ht = new Hashtable();
     ht.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
     if((prdclsUtil.isExistsPrdClsId(ht))){
    
    PalletGrpUtil palletGrpUtil =  new PalletGrpUtil();
    DestinationUtil destU = new DestinationUtil(); 
    Hashtable htpalletGrp = new Hashtable();
    htpalletGrp.put(MDbConstant.PALLET_GRP,sPalletGrp);
    if((palletGrpUtil.isExistsPalletGrp(htpalletGrp))){
    
      TrayGrpUtil trayGrpUtil =  new TrayGrpUtil();
    Hashtable httrayGrp = new Hashtable();
    httrayGrp.put(MDbConstant.TRAY_GRP,sTrayGrpId);
    if((trayGrpUtil.isExistsTrayGrp(httrayGrp))){
    
    
    Hashtable htRule = new Hashtable();
    //htRule.put(MDbConstant.RULE_ID,sRule);
   //
    htRule.put(MDbConstant.SHIP_TO,sShipTo);
    if (sDestn.length()<=0){
    String destn ="";
  List listQry = destU.getShipToListStartsWith(sShipTo);
 
    for (int i =0; i<listQry.size(); i++){
    Map map = (Map) listQry.get(i);
   
      destn         = (String) map.get(MDbConstant.DESTINATION);
  }
  sDestn = destn;
  }
    htRule.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
    htRule.put(MDbConstant.PALLET_GRP,sPalletGrp);
    htRule.put(MDbConstant.TRAY_GRP_ID,sTrayGrpId);
  //
    if(!(ruleUtil.isExistsRules(htRule))) // if the Rule exists already
    {
          htRule.put(MDbConstant.DESTINATION,sDestn); 
           htRule.put("CATEGORY",sCategory); 
          htRule.put(MDbConstant.PREFIX,sPrefix);
          htRule.put(MDbConstant.TYPE,sType);
          htRule.put(MDbConstant.CREATED_AT,new DateUtils().getDateTime());
          htRule.put(MDbConstant.LOGIN_USER,sUserId);
          boolean Inserted = ruleUtil.insertRules(htRule);
          if(Inserted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Rules Added Successfully</font>";
                    sAddEnb  = "disabled";
                    
                    mv.insertMovHisLogger(sUserId, "Rules_Master","Rules for  :"  + sShipTo +"  Added Successfully " ); 

                 
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to add  Rules</font>";
                    sAddEnb  = "enabled";
                   
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +"> Rules Exists already. Try again</font>";
           sAddEnb = "enabled";
          
    }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter a valid Tray Grp Id. Try again</font>";
           sAddEnb = "enabled";
    }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter a valid Pallet Grp Id. Try again</font>";
           sAddEnb = "enabled";
    }
    }else{
     res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter a valid Product Class. Try again</font>";
           sAddEnb = "enabled";
    }
 /*  }else{
     res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter a valid Product Class. Try again</font>";
           sAddEnb = "enabled";
    }*/
  //   mv.insertMovHisLogger(sUserId, "Rules_Master", res);
}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
   
    sAddEnb  = "disabled";
   PrdClsUtil prdclsUtil =  new PrdClsUtil();
     Hashtable ht = new Hashtable();
     ht.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
     if((prdclsUtil.isExistsPrdClsId(ht))){
    
 
      TrayGrpUtil trayGrpUtil =  new TrayGrpUtil();
    Hashtable httrayGrp = new Hashtable();
    httrayGrp.put(MDbConstant.TRAY_GRP,sTrayGrpId);
    if((trayGrpUtil.isExistsTrayGrp(httrayGrp))){
    
     PalletGrpUtil palletGrpUtil =  new PalletGrpUtil();
    Hashtable htpalletGrp = new Hashtable();
    htpalletGrp.put(MDbConstant.PALLET_GRP,sPalletGrp);
    if((palletGrpUtil.isExistsPalletGrp(htpalletGrp))){
    
    Hashtable htRule = new Hashtable();
    htRule.put(MDbConstant.SHIP_TO,sShipTo);
     htRule.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
     htRule.put(MDbConstant.PALLET_GRP,sPalletGrp); 
     htRule.put(MDbConstant.TRAY_GRP_ID,sTrayGrpId);
    if((ruleUtil.isExistsRules(htRule)))
    {
          Hashtable htUpdate = new Hashtable();
        //  htUpdate.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
        //  htUpdate.put(MDbConstant.PALLET_GRP,sPalletGrp); 
          htUpdate.put(MDbConstant.PREFIX,sPrefix);
          htUpdate.put(MDbConstant.TYPE,sType);
          //htUpdate.put(MDbConstant.UPDATED_AT,new DateUtils().getDateTime());
          //htUpdate.put(MDbConstant.UPDATED_BY,sUserId);
          
          Hashtable htCondition = new Hashtable();
          htCondition.put(MDbConstant.SHIP_TO,sShipTo);
            htCondition.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
             htCondition.put(MDbConstant.TRAY_GRP_ID,sTrayGrpId);
            htCondition.put(MDbConstant.PALLET_GRP,sPalletGrp); 
        
          htCondition.put("CATEGORY",sCategory); 
         
      
          boolean Updated = ruleUtil.updateRules(htUpdate,htCondition);
          if(Updated) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +" > Rules Updated Successfully</font>";
                    
          mv.insertMovHisLogger(sUserId, "Rules_Master","Rules for  :"  + sShipTo +"  Updated Successfully " ); 
          
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +" >Failed to Update  Rules,Please Check the Data</font>";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +"> Rules doesn't not Exists. Try again</font>";

    }
    }else{
      res = "<font class = "+MDbConstant.FAILED_COLOR +"> Enter a valid Pallet Grp. Try again</font>";
    }
    }else{
      res = "<font class = "+MDbConstant.FAILED_COLOR +"> Enter a valid Tray Grp. Try again</font>";
    }
    }else{
    res = "<font class = "+MDbConstant.FAILED_COLOR +"> Enter a valid Prd Cls Id. Try again</font>";
    }
 //    mv.insertMovHisLogger(sUserId, "Rules_Master", res);
}

//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
    Hashtable htDelete = new Hashtable();
    htDelete.put(MDbConstant.SHIP_TO,sShipTo);
    htDelete.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
    htDelete.put(MDbConstant.PALLET_GRP,sPalletGrp); 
      htDelete.put(MDbConstant.TRAY_GRP_ID,sTrayGrpId); 
           htDelete.put("CATEGORY",sCategory); 

    if(ruleUtil.isExistsRules(htDelete))
    {
          boolean locAsgnDeleted = ruleUtil.deleteRules(htDelete);
          if(locAsgnDeleted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +"> Rule Deleted Successfully</font>";
                    sAddEnb    = "disabled";
                    
                    mv.insertMovHisLogger(sUserId, "Rules_Master","Rules for  :"  + sShipTo +"  Updated Successfully " ); 
         
                      sShipTo  = "";
                      sPrefix  = "";
                      sPrdClsId  = "";
                      sPalletGrp  = "";
                      sTrayGrpId  = "";
                      sType  = "";
                 


          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to Delete  Rule</font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +"> Rule doesn't not Exists. Try again</font>";
    }
 //   mv.insertMovHisLogger(sUserId, "Rules_Master", res);
}

//4. >> View
else if(action.equalsIgnoreCase("VIEW")){
  
                      sPalletGrp  = "";
                      sTrayGrpId  = "";
                      sType  = "";
       Map map = ruleUtil.getRuleDetails(sShipTo,sPrdClsId);
 if (map.size()>0){
       sShipTo   = strUtils.fString((String)map.get(MDbConstant.SHIP_TO));
       sPrefix   = strUtils.fString((String)map.get(MDbConstant.PREFIX));
       sPrdClsId   = strUtils.fString((String)map.get(MDbConstant.PRD_CLS_ID)); 
       sPalletGrp   = strUtils.fString((String)map.get(MDbConstant.PALLET_GRP));
       sTrayGrpId   = strUtils.fString((String)map.get(MDbConstant.TRAY_GRP_ID));
       sType   = strUtils.fString((String)map.get(MDbConstant.TYPE));
        sCategory   = strUtils.fString((String)map.get("CATEGORY"));
}else{
 res = "<font class = "+MDbConstant.FAILED_COLOR +"> Rule doesn't not Exists. Try again</font>";
}

}

%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white"> RULE MASTER</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
  
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Ship To : </TH>
         <TD>
                <INPUT name="SHIP_TO" type = "TEXT" value="<%=sShipTo%>" size="50"  MAXLENGTH=20 >
                <INPUT type = "hidden" name="SHIP_TO1" value = "">
                <INPUT type = "hidden" name="DESTINATION" value = "">
           
				    
              <a href="#" onClick="javascript:popWin('listView/ship_to_list.jsp?SHIP_TO='+form1.SHIP_TO.value);"><img src="images/populate.gif" border="0"></a> 
                <INPUT class="Submit" type="BUTTON" value="View" onClick="onView();">
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > Prefix : </TH>
         <TD>
             <INPUT name="TRAV_PFX" type = "TEXT" value="<%=sPrefix%>" size="50"  MAXLENGTH=80>

         </TD>

    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Product Class: </TH>
         <TD>
             <INPUT name="PRD_CLS_ID" type = "TEXT" value="<%=sPrdClsId%>" size="50"  MAXLENGTH=80>
             <INPUT type = "hidden" name="PRD_CLS_ID1" value = "">
              <INPUT type = "hidden" name="PRD_CLS_DESC" value = "">
               <INPUT type = "hidden" name="PRD_GRP_ID" value = "">

	        <a href="#" onClick="javascript:popWin('listView/prd_cls_list.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><img src="images/populate.gif" border="0"></a>
       <!--  <a href="#" onClick="javascript:popWin('prd_loc_grp_list.jsp?LOC_GRP_ID='+form1.LOC_GRP_ID.value);"><img src="images/populate.gif" border="0"></a> -->
         </TD>

    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Type  : </TH>
         <TD>
                <INPUT name="TYPE" type = "TEXT" value="<%=sType%>" size="50"  MAXLENGTH=20 >
                              
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Tray Group : </TH>
         <TD>
                <INPUT name="TRAY_GRP_ID" type = "TEXT" value="<%=sTrayGrpId%>" size="50"  MAXLENGTH=20 >
                 <INPUT type = "hidden" name="TRAY_GRP_ID1" value = "">
                     <a href="#" onClick="javascript:popWin('listView/traygrp_mst_list.jsp?TRAY_GRP_ID='+form1.TRAY_GRP_ID.value);"><img src="images/populate.gif" border="0"></a> 
               
         </TD>
    </TR>
   <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Pallet Grp  : </TH>
         <TD>
                <INPUT name="PALLET_GRP" type = "TEXT" value="<%=sPalletGrp%>" size="50"  MAXLENGTH=20 >
                <INPUT type = "hidden" name="PALLET_GRP1" value = "">
          <a href="#" onClick="javascript:popWin('listView/pallet_grp_list.jsp?PALLET_GRP='+form1.PALLET_GRP.value);"><img src="images/populate.gif" border="0"></a> 
               
         </TD>
    </TR>
   <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >CATEGORY :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <SELECT NAME ="CATEGORY" size="1">
                           <OPTION selected value='NRML'>NORMAL</OPTION>
                           <option value="ISC">ISC</option></SELECT>
                           
                      </TD>
                     
                    <TR>

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

