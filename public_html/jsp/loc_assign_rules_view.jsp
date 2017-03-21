<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>

<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Loc Assigning Rule Master</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">


function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "loc_assign_rules_view.jsp?action=NEW";
   document.form1.submit();
}
function onAdd(){
   
    var RULE         = document.form1.RULE.value;
    var PRD_GRP_ID   = document.form1.PRD_GRP_ID.value;
    var LOC_ID       = document.form1.LOC_ID.value;
    
    if(RULE == "" || RULE == null)                 {alert("Please Enter Rule No. "); return false; }
    if(PRD_GRP_ID == "" || PRD_GRP_ID == null)     {alert("Please Enter Product group Id"); return false; }
    if(LOC_ID == "" || LOC_ID == null)             {alert("Please Enter Loc Id");  return false; }
   

   document.form1.action  = "loc_assign_rules_view.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
    var RULE         = document.form1.RULE.value;
    var PRD_GRP_ID   = document.form1.PRD_GRP_ID.value;
    var LOC_ID       = document.form1.LOC_ID.value;
    
    if(RULE == "" || RULE == null)                 {alert("Please Enter Rule No. "); return false; }
    if(PRD_GRP_ID == "" || PRD_GRP_ID == null)     {alert("Please Enter Product group Id"); return false; }
    if(LOC_ID == "" || LOC_ID == null)             {alert("Please Enter Loc Id");  return false; }
    
    document.form1.action  = "loc_assign_rules_view.jsp?action=UPDATE";
    document.form1.submit();
}
function onDelete(){
   var RULE   = document.form1.RULE.value;
   if(RULE == "" || RULE == null) {alert("Please Enter/Select Rule ");  return false; }

   document.form1.action  = "loc_assign_rules_view.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
    var RULE   = document.form1.RULE.value;
    if(RULE == "" || RULE == null) {alert("Please Enter/Select Rule to view ");  return false; }

   document.form1.action  = "loc_assign_rules_view.jsp?action=VIEW";
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
String sProdGrpId ="",
       sLocId  =   "",
       sRuleDesc  = "",
       sRule ="";


StrUtils strUtils = new StrUtils();
PrdGrpMstUtil prdGrpUtil = new PrdGrpMstUtil();
LocAssignRulesUtil locAsgnUtil = new LocAssignRulesUtil();


action            = strUtils.fString(request.getParameter("action"));

sProdGrpId  = strUtils.fString(request.getParameter("PRD_GRP_ID"));
if(sProdGrpId.length() <= 0) sProdGrpId  = strUtils.fString(request.getParameter("PRD_GRP_ID1"));

sLocId   = strUtils.fString(request.getParameter("LOC_ID"));
if(sLocId.length() <= 0) sLocId  = strUtils.fString(request.getParameter("LOC_ID1"));
sRuleDesc  = strUtils.fString(request.getParameter("RULE_DESC"));
sRule  = strUtils.fString(request.getParameter("RULE"));
if(sRule.length() <= 0) sRule  = strUtils.fString(request.getParameter("RULE1"));


//1. >> New
if(action.equalsIgnoreCase("NEW")){
      sProdGrpId ="";
      sLocId  = "";
      sRuleDesc  = "";
      sRule ="";
      sAddEnb    = "enabled";
   


}

//2. >> Add
else if(action.equalsIgnoreCase("ADD")){
    
     Hashtable ht = new Hashtable();
     ht.put(MDbConstant.PRD_GRP_ID,sProdGrpId);
     if((prdGrpUtil.isExistsPrdGrpId(ht))){
    
    LocMstUtil locUtil =  new LocMstUtil();
    Hashtable htloc = new Hashtable();
    htloc.put(MDbConstant.LOC_ID,sLocId);
    if((locUtil.isExistsLocId(htloc))){
    
    Hashtable htRule = new Hashtable();
    //htRule.put(MDbConstant.RULE_ID,sRule);
   //
    htRule.put(MDbConstant.PRD_GRP_ID,sProdGrpId);
    htRule.put(MDbConstant.LOC_ID,sLocId);
  //
    if(!(locAsgnUtil.isExistsRulesID(htRule))) // if the Rule not exists already
    {
          //htRule.put("RULESID",sRule);
          htRule.put(MDbConstant.RULE_DESC,sRuleDesc);
          htRule.put(MDbConstant.PRD_GRP_ID,sProdGrpId);
          htRule.put(MDbConstant.LOC_ID,sLocId);
          htRule.put(MDbConstant.CREATED_AT,new DateUtils().getDateTime());
          htRule.put(MDbConstant.LOGIN_USER,sUserId);
          boolean locAsgnInserted = locAsgnUtil.insertRulesID(htRule);
          if(locAsgnInserted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Loc Assigned Rules Added Successfully</font>";
                    sAddEnb  = "disabled";
                    
          mv.insertMovHisLogger(sUserId, "Loc_Assign_rules","Loc Assigned Rules for    :"  + sRule +"  Added Successfully " ); 

                 
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to add Loc Assigned Rules</font>";
                    sAddEnb  = "enabled";
                   
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Loc Assigned Rules Exists already. Try again</font>";
           sAddEnb = "enabled";
          
    }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter a valid Loc Grp Id. Try again</font>";
           sAddEnb = "enabled";
    }
    }else{
     res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter a valid Product Group. Try again</font>";
           sAddEnb = "enabled";
    }
 //    mv.insertMovHisLogger(sUserId, "Loc_Assign_rules", res); 
    
}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
   
    sAddEnb  = "disabled";
     Hashtable ht = new Hashtable();
     ht.put(MDbConstant.PRD_GRP_ID,sProdGrpId);
     if((prdGrpUtil.isExistsPrdGrpId(ht))){
    
    LocMstUtil locUtil =  new LocMstUtil();
    Hashtable htloc = new Hashtable();
    htloc.put(MDbConstant.LOC_ID,sLocId);
    if((locUtil.isExistsLocId(htloc))){
 
    Hashtable htRule = new Hashtable();
    htRule.put(MDbConstant.RULE_ID,sRule);
  
    if((locAsgnUtil.isExistsRulesID(htRule)))
    {
          Hashtable htUpdate = new Hashtable();
          htUpdate.put(MDbConstant.RULE_DESC,sRuleDesc);
          htUpdate.put(MDbConstant.PRD_GRP_ID,sProdGrpId);
          htUpdate.put(MDbConstant.LOC_ID,sLocId);
          //htUpdate.put(MDbConstant.UPDATED_AT,new DateUtils().getDateTime());
          //htUpdate.put(MDbConstant.UPDATED_BY,sUserId);
         
          Hashtable htCondition = new Hashtable();
          htCondition.put(MDbConstant.RULE_ID,sRule);
         
      
          boolean Updated = locAsgnUtil.updateRulesID(htUpdate,htCondition);
          if(Updated) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +" >Loc Assigned Rules Updated Successfully</font>";
                    
          mv.insertMovHisLogger(sUserId, "Loc_Assign_rules","Loc Assigned Rules for    :"  + sRule +"  Updated Successfully " ); 

          
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +" >Failed to Update Loc Assigned Rules</font>";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Loc Assigned Rules doesn't not Exists. Try again</font>";

    }
    }else{
      res = "<font class = "+MDbConstant.FAILED_COLOR +"> Enter a valid Loc  Id. Try again</font>";
    }
    }else{
    res = "<font class = "+MDbConstant.FAILED_COLOR +"> Enter a valid Prd Grp Id. Try again</font>";
    }
//     mv.insertMovHisLogger(sUserId, "Loc_Assign_rules", res); 
}

//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
    Hashtable htDelete = new Hashtable();
    htDelete.put(MDbConstant.RULE_ID,sRule);
   

    if(locAsgnUtil.isExistsRulesID(htDelete))
    {
          boolean locAsgnDeleted = locAsgnUtil.deleteRulesID(htDelete);
          if(locAsgnDeleted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Loc Assigned Rule Deleted Successfully</font>";
                    sAddEnb    = "disabled";
                    
                     mv.insertMovHisLogger(sUserId, "Loc_Assign_rules","Loc Assigned Rules for    :"  + sRule +"  Updated Successfully " ); 


                    sProdGrpId  = "";
                    sLocId  = "";
                    sRuleDesc  = "";
                    sRule ="";


          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to Delete Loc Assigned Rule</font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Loc Assigned Rule doesn't not Exists. Try again</font>";
    }
//    mv.insertMovHisLogger(sUserId, "Loc_Assign_rules", res);
}

//4. >> View
else if(action.equalsIgnoreCase("VIEW")){
  
       Map map = locAsgnUtil.getLocAsignedRuleDetails(sRule);
 if (map.size()>0){
       sRule   = strUtils.fString((String)map.get("RulesId"));
       sRuleDesc   = strUtils.fString((String)map.get("Description"));
       sProdGrpId   = strUtils.fString((String)map.get("PRD_GRP_ID")); 
       sLocId   = strUtils.fString((String)map.get("LOC_ID"));
}else{
	// Added for Location Assigining Rule 
	sRuleDesc="";
	sProdGrpId="";
	sLocId="";
 res = "<font class = "+MDbConstant.FAILED_COLOR +">Loc Assigned Rule doesn't not Exists. Try again</font>";
}

}

%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">LOC ASSIGNING RULE</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Rule Id : </TH>
         <TD>
                <INPUT name="RULE" type = "TEXT" value="<%=sRule%>" size="50"  MAXLENGTH=20 >
                <INPUT type = "hidden" name="RULE1" value = "">
           
				    
              <a href="#" onClick="javascript:popWin('listView/loc_asgn_list.jsp?RULE='+form1.RULE.value);"><img src="images/populate.gif" border="0"></a> 
                <INPUT class="Submit" type="BUTTON" value="View" onClick="onView();">
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > Description : </TH>
         <TD>
             <INPUT name="RULE_DESC" type = "TEXT" value="<%=sRuleDesc%>" size="50"  MAXLENGTH=80>

         </TD>

    </TR>
   <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Product Grp Id : </TH>
         <TD>
                <INPUT name="PRD_GRP_ID" type = "TEXT" value="<%=sProdGrpId%>" size="50"  MAXLENGTH=20 >
                <INPUT type = "hidden" name="PRD_GRP_ID1" value = "">
             <a href="#" onClick="javascript:popWin('listView/prd_list_grp.jsp?PRD_GRP_ID='+form1.PRD_GRP_ID.value);"><img src="images/populate.gif" border="0"></a> 
               
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Loc Id : </TH>
         <TD>
             <INPUT name="LOC_ID" type = "TEXT" value="<%=sLocId%>" size="50"  MAXLENGTH=80>
             <INPUT type = "hidden" name="LOC_ID1" value = "">

	         <a href="#" onClick="javascript:popWin('listView/loc_list.jsp?LOC_ID='+form1.LOC_ID.value);"><img src="images/populate.gif" border="0"></a>
       <!--  <a href="#" onClick="javascript:popWin('prd_loc_grp_list.jsp?LOC_GRP_ID='+form1.LOC_GRP_ID.value);"><img src="images/populate.gif" border="0"></a> -->
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

