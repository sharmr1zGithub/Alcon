<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>

<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Product Group Master</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">


function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "prod_grp_mst_view.jsp?action=NEW";
   document.form1.submit();
}
function onAdd(){
   var PRD_GRP_ID   = document.form1.PRD_GRP_ID.value;
   var NO_TRAY      = document.form1.NO_TRAY.value;
   if(PRD_GRP_ID == "" || PRD_GRP_ID == null) {alert("Please Enter Product group Id"); return false; }
   if(NO_TRAY == "" || NO_TRAY == null) {alert("Please Enter No. of Tray/Bay "); return false; }
   
     if(isNaN(document.form1.NO_TRAY.value)) {alert("Please enter valid NO_TRAY."); document.form1.NO_TRAY.focus(); return false;}

   document.form1.action  = "prod_grp_mst_view.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
  var PRD_GRP_ID   = document.form1.PRD_GRP_ID.value;
 
  var NO_TRAY      = document.form1.NO_TRAY.value;
   if(PRD_GRP_ID == "" || PRD_GRP_ID == null) {alert("Please Enter Product group Id"); return false; }
  
   if(NO_TRAY == "" || NO_TRAY == null) {alert("Please Hashtable of Tray/Bay "); return false; }
   if(isNaN(document.form1.NO_TRAY.value)) {alert("Please enter valid NO_TRAY."); document.form1.NO_TRAY.focus(); return false;}
   document.form1.action  = "prod_grp_mst_view.jsp?action=UPDATE";
   document.form1.submit();
}
function onDelete(){
   var PRD_GRP_ID   = document.form1.PRD_GRP_ID.value;
   if(PRD_GRP_ID == "" || PRD_GRP_ID == null) {alert("Please Enter Product group Id");  return false; }

   document.form1.action  = "prod_grp_mst_view.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
    var PRD_GRP_ID   = document.form1.PRD_GRP_ID.value;
   
    if(PRD_GRP_ID == "" || PRD_GRP_ID == null) {alert("Please Enter Product group Id");  return false; }

   document.form1.action  = "prod_grp_mst_view.jsp?action=VIEW";
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
       sProdGrpDesc  = "",
       sTotalTray ="";


StrUtils strUtils = new StrUtils();
PrdGrpMstUtil prdGrpUtil = new PrdGrpMstUtil();
action            = strUtils.fString(request.getParameter("action"));

sProdGrpId  = strUtils.fString(request.getParameter("PRD_GRP_ID"));
if(sProdGrpId.length() <= 0) sProdGrpId  = strUtils.fString(request.getParameter("PRD_GRP_ID1"));

//sLocId   = strUtils.fString(request.getParameter("LOC_ID"));
//if(sLocId.length() <= 0) sLocId  = strUtils.fString(request.getParameter("LOC_ID1"));
sProdGrpDesc  = strUtils.fString(request.getParameter("PRD_GRP_DESC"));
sTotalTray  = strUtils.fString(request.getParameter("NO_TRAY"));


//1. >> New
if(action.equalsIgnoreCase("NEW")){
      sProdGrpId ="";
      sLocId  = "";
      sProdGrpDesc  = "";
      sTotalTray ="";
      sAddEnb    = "enabled";
     


}

//2. >> Add
else if(action.equalsIgnoreCase("ADD")){
  LocMstUtil locUtil =  new LocMstUtil();
   Hashtable ht = new Hashtable();
   ht.put(MDbConstant.PRD_GRP_ID,sProdGrpId);
   if(!(prdGrpUtil.isExistsPrdGrpId(ht))) // if the Prod Group exists already
    {
          // get total bay for Location grp Id
        //  String total_Bay =prdGrpUtil.GetTotalBayForLocGrpId(sLocGrpId);
        System.out.println("sTotalTray"+ sTotalTray);
          double trayPerBay =  new Integer(100).doubleValue()/ new Integer(sTotalTray).doubleValue();
          System.out.println("trayPerBay"+trayPerBay);
           double spaceRem = (100 - trayPerBay) ;
           System.out.println("spaceRem"+spaceRem);
          ht.put(MDbConstant.PRD_GRP_DESC,sProdGrpDesc);
          ht.put(MDbConstant.TRAYS_PER_BAY,sTotalTray);
          ht.put(MDbConstant.PCNT_SPACE_PER_TRAY,new Double(trayPerBay).toString());
        //  ht.put(MDbConstant.PCNT_SPACE_REMAINING,new Double(spaceRem).toString());
          ht.put(MDbConstant.CREATED_AT,new DateUtils().getDateTime());
          ht.put(MDbConstant.LOGIN_USER,sUserId);
          boolean prdGrpInserted = prdGrpUtil.insertPrdGrpMst(ht);
          if(prdGrpInserted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Loc Product Group Added Successfully</font>";
                    sAddEnb  = "disabled";
                    
                      mv.insertMovHisLogger(sUserId, "Product_Group","Loc Product Group   :"  + sProdGrpId +"  Added Successfully " ); 
       
                 
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to add New Loc Product Group</font>";
                    sAddEnb  = "enabled";
                   
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Loc Product Group Exists already. Try again</font>";
           sAddEnb = "enabled";
          
    }
    
//    mv.insertMovHisLogger(sUserId, "Product_Group", res);
    
}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
   
    sAddEnb  = "disabled";
 /*    LocMstUtil locGrpUtil =  new LocMstUtil();
    Hashtable htloc = new Hashtable();
    htloc.put(MDbConstant.LOC_ID,sLocId);
    if((locGrpUtil.isExistsLocId(htloc))){*/
 
    Hashtable ht = new Hashtable();
     ht.put(MDbConstant.PRD_GRP_ID,sProdGrpId);
   //  ht.put(MDbConstant.LOC_ID,sLocId);
    if((prdGrpUtil.isExistsPrdGrpId(ht)))
    {
    
          String  sTotal ="";
          
         
      //    sTotal=(100/Float.parseFloat(sTotalTray));
      double spacePerTray =  new Integer(100).doubleValue()/ new Integer(sTotalTray).doubleValue();
          
          Hashtable htUpdate = new Hashtable();
          htUpdate.put(MDbConstant.PRD_GRP_DESC,sProdGrpDesc);
          htUpdate.put(MDbConstant.TRAYS_PER_BAY,sTotalTray);
          htUpdate.put("pcnt_space_per_tray",new Double(spacePerTray).toString());
         
          //htUpdate.put(MDbConstant.UPDATED_AT,new DateUtils().getDateTime());
          //htUpdate.put(MDbConstant.UPDATED_BY,sUserId);
          
          
          Hashtable htCondition = new Hashtable();
          htCondition.put(MDbConstant.PRD_GRP_ID,sProdGrpId);
         // htCondition.put(MDbConstant.LOC_ID,sLocId);
      
          boolean Updated = prdGrpUtil.updatePrdGrpId(htUpdate,htCondition);
          if(Updated) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +" >Loc Product group Updated Successfully</font>";
                  
                     mv.insertMovHisLogger(sUserId, "Product_Group","Loc Product Group   :"  + sProdGrpId +"  Updated Successfully " ); 
        
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +" >Failed to Update Loc Product group</font>";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Loc Product group doesn't not Exists. Try again</font>";

    }
  //   mv.insertMovHisLogger(sUserId, "Product_Group", res);
   /* }/*else{
      res = "<font class = "+MDbConstant.FAILED_COLOR +"> Enter a valid Loc Grp Id. Try again</font>";
    }*/
}

//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
    Hashtable htDelete = new Hashtable();
    htDelete.put(MDbConstant.PRD_GRP_ID,sProdGrpId);
   
    if(prdGrpUtil.isExistsPrdGrpId(htDelete))
    {
          boolean prdGrpDeleted = prdGrpUtil.deletePrdGrpId(htDelete);
          if(prdGrpDeleted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Loc Product group Deleted Successfully</font>";
                    sAddEnb    = "disabled";
                   
                    mv.insertMovHisLogger(sUserId, "Product_Group","Loc Product Group   :"  + sProdGrpId +"  Deleted Successfully " ); 
        
                    sProdGrpId  = "";
                    sLocId  = "";
                    sProdGrpDesc  = "";
                    sTotalTray ="";
                    
                    


          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to Delete Loc Product group</font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Loc Product group doesn't not Exists. Try again</font>";
    }
  //  mv.insertMovHisLogger(sUserId, "Product_Group", res);
}

//4. >> View
else if(action.equalsIgnoreCase("VIEW")){
    Map map = prdGrpUtil.getPrdGrpIdDetails(sProdGrpId);
    if (map.size()>0) {
    sProdGrpId   = strUtils.fString((String)map.get("PRD_GRP_ID"));
    sProdGrpDesc   = strUtils.fString((String)map.get("PRD_GRP_DESC"));
    sTotalTray   = strUtils.fString((String)map.get("TRAYS_PER_BAY"));
    }

}

%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white"><font color="white">LOC</font> PRODUCT  GROUP MASTER</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
   
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Loc Product  Grp Id : </TH>
         <TD>
                <INPUT name="PRD_GRP_ID" type = "TEXT" value="<%=sProdGrpId%>" size="50"  MAXLENGTH=20 >
                <INPUT type = "hidden" name="PRD_GRP_ID1" value = " ">
           
				     <!--   <a href="#" onClick="javascript:popWin('prod_grp_list.jsp?PRD_GRP_ID='+form1.PRD_GRP_ID.value);"><img src="images/populate.gif" border="0"></a>-->
             <a href="#" onClick="javascript:popWin('listView/prod_grp_list.jsp?PRD_GRP_ID='+form1.PRD_GRP_ID.value);"><img src="images/populate.gif" border="0"></a> 
                <INPUT class="Submit" type="BUTTON" value="View" onClick="onView();">
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > Description : </TH>
         <TD>
             <INPUT name="PRD_GRP_DESC" type = "TEXT" value="<%=sProdGrpDesc%>" size="50"  MAXLENGTH=80>

         </TD>

    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* No. Of Tray Per Bay : </TH>
         <TD>
             <INPUT name="NO_TRAY" type = "TEXT" value="<%=sTotalTray%>" size="50"  MAXLENGTH=80>

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

