<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>


<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Tray Group  Master</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">
function popWin(URL) {
 subWin = window.open(URL, 'Tray', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "tray_grp_view.jsp?action=NEW";
   document.form1.submit();
}
function onAdd(){
 
   var TRAY_GRP_ID   = document.form1.TRAY_GRP_ID.value;
    var SHIP_TO   = document.form1.SHIP_TO.value;
   
   
   if(TRAY_GRP_ID == "" || TRAY_GRP_ID == null) {alert("Please Enter Tray Grp Id"); return false; }
    if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter Ship To"); return false; }
   document.form1.action  = "tray_grp_view.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
    var TRAY_GRP_ID   = document.form1.TRAY_GRP_ID.value;
    var SHIP_TO   = document.form1.SHIP_TO.value;
    if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter Ship To"); return false; }
    if(TRAY_GRP_ID == "" || TRAY_GRP_ID == null) {alert("Please Enter Tray Grp Id"); return false; }
  
   document.form1.action  = "tray_grp_view.jsp?action=UPDATE";
   document.form1.submit();
}
function onDelete(){
    var SHIP_TO   = document.form1.SHIP_TO.value;
    var TRAY_GRP_ID   = document.form1.TRAY_GRP_ID.value;
    if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter Ship To"); return false; }
   if(TRAY_GRP_ID == "" || TRAY_GRP_ID == null) {alert("Please Enter Tray Grp Id"); return false; }

   document.form1.action  = "tray_grp_view.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
     var TRAY_GRP_ID   = document.form1.TRAY_GRP_ID.value;
   
   if(TRAY_GRP_ID == "" || TRAY_GRP_ID == null) {alert("Please Enter Tray Grp Id"); return false; }

   document.form1.action  = "tray_grp_view.jsp?action=VIEW";
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
String sTrayGrp ="",sDesc  =   "",sShipTo ="",sPrdClsId ="";


StrUtils strUtils = new StrUtils();
TrayGrpUtil trayGrpUtil = new TrayGrpUtil();
action            = strUtils.fString(request.getParameter("action"));

sShipTo  = strUtils.fString(request.getParameter("SHIP_TO"));
if(sShipTo.length() <= 0) sShipTo  =  strUtils.fString(request.getParameter("SHIP_TO1"));

sTrayGrp  = strUtils.fString(request.getParameter("TRAY_GRP_ID"));
sDesc  = strUtils.fString(request.getParameter("TRAY_DESC"));
if(sTrayGrp.length() <= 0) sTrayGrp  = strUtils.fString(request.getParameter("TRAY_GRP_ID1"));
sPrdClsId  = strUtils.fString(request.getParameter("PRD_CLS_ID"));
if(sPrdClsId.length() <= 0) sPrdClsId  = strUtils.fString(request.getParameter("PRD_CLS_ID1"));



//1. >> New
if(action.equalsIgnoreCase("NEW")){
sShipTo ="";
      sTrayGrp  = "";
      sDesc  = "";
      sPrdClsId="";
      sAddEnb    = "enabled";
  
}

//2. >> Add
else if(action.equalsIgnoreCase("ADD")){

   PrdClsUtil prdClsUtil =  new PrdClsUtil();
    Hashtable htprdCls = new Hashtable();
    htprdCls.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
    if((prdClsUtil.isExistsPrdClsId(htprdCls))){
    
   DestinationUtil destU = new DestinationUtil(); 
   Hashtable hts = new Hashtable();
   hts.put(MDbConstant.SHIP_TO,sShipTo);
 
 String Destn ="";
  List listQry = destU.getShipToListStartsWith(sShipTo);
 
    for (int i =0; i<listQry.size(); i++){
    Map map = (Map) listQry.get(i);
   
      Destn         = (String) map.get(MDbConstant.DESTINATION);
  }
   hts.put(MDbConstant.DESTINATION,Destn);
   if((destU.isExistsDest(hts))){
    
    Hashtable ht = new Hashtable();
    ht.put(MDbConstant.TRAY_GRP,sTrayGrp);
   ht.put(MDbConstant.SHIP_TO,sShipTo);
   ht.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
   ht.put(MDbConstant.DESTINATION,Destn);
    if(!(trayGrpUtil.isExistsTrayGrp(ht))) // if the Tray  exists already
    {
          ht.put(MDbConstant.TRAY_DESC,sDesc  );
          ht.put(MDbConstant.CREATED_AT,new DateUtils().getDateTime());
          ht.put(MDbConstant.LOGIN_USER,sUserId);
          boolean Inserted = trayGrpUtil.insertTrayGrp(ht);
          if(Inserted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Tray Group  Added Successfully</font>";
                    sAddEnb  = "disabled";
                    
                    mv.insertMovHisLogger(sUserId, "Tray_Group","Tray Group   :"  + sTrayGrp +"  Added Successfully " ); 

                 
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to add New Tray Group </font>";
                    sAddEnb  = "enabled";
                   
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Tray Group  Exists already. Try again</font>";
           sAddEnb = "enabled";
          
    }
     }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter Valid Ship To. Try again</font>";
           sAddEnb = "enabled";
          
    }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter Valid Product Class. Try again</font>";
           sAddEnb = "enabled";
          
    }
    
    
  //  mv.insertMovHisLogger(sUserId, "Tray_Group", res); 
    
}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
   
    sAddEnb  = "disabled";
     PrdClsUtil prdClsUtil =  new PrdClsUtil();
    Hashtable htprdCls = new Hashtable();
    htprdCls.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
    if((prdClsUtil.isExistsPrdClsId(htprdCls))){
 
  DestinationUtil destU = new DestinationUtil(); 
   Hashtable hts = new Hashtable();
   hts.put(MDbConstant.SHIP_TO,sShipTo);
   if((destU.isExistsDest(hts))){
    
    Hashtable ht = new Hashtable();
     ht.put(MDbConstant.SHIP_TO,sShipTo);
    ht.put(MDbConstant.TRAY_GRP,sTrayGrp);
     ht.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
    if((trayGrpUtil.isExistsTrayGrp(ht)))
    {
         
          ht.put(MDbConstant.TRAY_DESC,sDesc);
       //   ht.put(MDbConstant.UPDATED_AT,new DateUtils().getDateTime());
         // ht.put(MDbConstant.UPDATED_BY,sUserId);
          
          Hashtable htCondition = new Hashtable();
          htCondition.put(MDbConstant.SHIP_TO,sShipTo);
          htCondition.put(MDbConstant.TRAY_GRP,sTrayGrp);
            htCondition.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
            boolean Updated = trayGrpUtil.updateTrayGrp(ht,htCondition);
          if(Updated) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +" >Tray Group  Updated Successfully</font>";
                    
          mv.insertMovHisLogger(sUserId, "Tray_Group","Tray Group   :"  + sTrayGrp +"  Updated Successfully " ); 

          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +" >Failed to Update Tray Group </font>";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Tray Group  doesn't not Exists. Try again</font>";

    }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter Valid ship To. Try again</font>";

    }
     }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter Valid Product Class. Try again</font>";

    }
    
//     mv.insertMovHisLogger(sUserId, "Tray_Group", res); 
}

//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
   
    Hashtable ht = new Hashtable();
    ht.put(MDbConstant.SHIP_TO,sShipTo);
    ht.put(MDbConstant.TRAY_GRP,sTrayGrp);
    ht.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
    
     if(trayGrpUtil.isExistsTrayGrp(ht))
    {
          boolean Deleted = trayGrpUtil.deleteTrayGrp(ht);
          if(Deleted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Tray Group  Deleted Successfully</font>";
                    sAddEnb    = "disabled";

                    mv.insertMovHisLogger(sUserId, "Tray_Group","Tray Group   :"  + sTrayGrp +"  Deleted Successfully " ); 


                    sShipTo ="";
                    sTrayGrp  = "";
                    sDesc ="";
                    sPrdClsId="";
                    


          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to Delete Tray Group </font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Tray Group  doesn't not Exists. Try again</font>";
    }
    
 //    mv.insertMovHisLogger(sUserId, "Tray_Group", res); 
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
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">TRAY GROUP MASTER</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">* Ship To</TH>
      <TD>
        <INPUT name="SHIP_TO" type="TEXT" value="<%=sShipTo%>" size="50" MAXLENGTH="80"/>
            <a href="#" onClick="javascript:popWin('listView/ship_to_list.jsp?SHIP_TO='+form1.SHIP_TO.value);"><img src="images/populate.gif" border="0"></a>
         <INPUT type = "hidden" name="TRAV_PFX" value = "">
          <INPUT type = "hidden" name="SHIP_TO1" value = "">
           <INPUT type = "hidden" name="DESTINATION" value = "">
      </TD>
    </TR>
    
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Tray group Id : </TH>
         <TD>
             <INPUT name="TRAY_GRP_ID" type = "TEXT" value="<%=sTrayGrp%>" size="50"  MAXLENGTH=80>
<a href="#" onClick="javascript:popWin('listView/traygrp_mst_list.jsp?TRAY_GRP_ID='+form1.TRAY_GRP_ID.value);"><img src="images/populate.gif" border="0"></a> 
<INPUT type = "hidden" name="TRAY_GRP_ID1" value = "">
 <!--<INPUT class="Submit" type="BUTTON" value="View" onClick="onView();">-->
         </TD>

    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Description : </TH>
         <TD>
             <INPUT name="TRAY_DESC" type = "TEXT" value="<%=sDesc%>" size="50"  MAXLENGTH=80>
 
         </TD>

    </TR>
<TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Product Class : </TH>
         <TD>
                <INPUT name="PRD_CLS_ID" type = "TEXT" value="<%=sPrdClsId%>" size="50"  MAXLENGTH=20 >
                 <INPUT type = "hidden" name="PRD_CLS_ID1" value = ""> 
                 <INPUT type = "hidden" name="PRD_CLS_DESC" value = "">
                  <INPUT type = "hidden" name="PRD_GRP_ID" value = "">
                     <a href="#" onClick="javascript:popWin('listView/prd_cls_list.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><img src="images/populate.gif" border="0"></a> 
               
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

