<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>


<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Product Class Master</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">


function popWin(URL) {
 subWin = window.open(URL, 'CLASS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "prd_cls_mst_view.jsp?action=NEW";
   document.form1.submit();
}
function onAdd(){
   var PRD_CLS_ID   = document.form1.PRD_CLS_ID.value;
    var PRD_GRP_ID   = document.form1.PRD_GRP_ID.value;
   
   if(PRD_CLS_ID == "" || PRD_CLS_ID == null) {alert("Please Enter/Choose Prd Cls Id"); return false; }
   if(PRD_GRP_ID == "" || PRD_GRP_ID == null) {alert("Please Enter Loc Prd Grp Id "); return false; }
   
      
   if(isNaN(document.form1.PRICE.value)) {alert("Please enter valid PRICE."); document.form1.PRICE.focus(); return false;}
   if(isNaN(document.form1.QTY_PER_COLUMN.value)) {alert("Please enter valid QTY_PER_COLUMN.");document.form1.QTY_PER_COLUMN.focus(); return false;}
   if(isNaN(document.form1.NO_OF_COLUMN.value)) {alert("Please enter valid No_OF_COLUMN.");document.form1.NO_OF_COLUMN.focus(); return false;}
   if(isNaN(document.form1.QTY_PER_TRAY.value)) {alert("Please enter valid QTY_PER_TRAY.");document.form1.QTY_PER_TRAY.focus(); return false;}
   if(isNaN(document.form1.NO_OF_TRAY_PER_LAYER.value)) {alert("Please enter valid NO_OF_TRAY_PER_LAYER.");document.form1.NO_OF_TRAY_PER_LAYER.focus(); return false;}
   if(isNaN(document.form1.NO_OF_LAYERS.value)) {alert("Please enter valid NO_OF_LAYERS.");document.form1.NO_OF_LAYERS.focus(); return false;}
   if(isNaN(document.form1.NO_OF_TRAY_PER_PALLET.value)) {alert("Please enter valid NO_OF_TRAY_PER_PALLET.");document.form1.NO_OF_TRAY_PER_PALLET.focus(); return false;}
   if(isNaN(document.form1.KFACTOR.value)) {alert("Please enter valid KFACTOR."); document.form1.KFACTOR.focus(); return false;}
   
  
 
   document.form1.action  = "prd_cls_mst_view.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
  var PRD_CLS_ID   = document.form1.PRD_CLS_ID.value;
    var PRD_GRP_ID   = document.form1.PRD_GRP_ID.value;
   if(PRD_CLS_ID == "" || PRD_CLS_ID == null) {alert("Please Enter/Choose Prd Cls Id"); return false; }
   if(PRD_GRP_ID == "" || PRD_GRP_ID == null) {alert("Please Enter Loc Prd Grp Id "); return false; }
   
      
   if(isNaN(document.form1.PRICE.value)) {alert("Please enter valid PRICE."); document.form1.PRICE.focus(); return false;}
   if(isNaN(document.form1.QTY_PER_COLUMN.value)) {alert("Please enter valid QTY_PER_COLUMN.");document.form1.QTY_PER_COLUMN.focus(); return false;}
   if(isNaN(document.form1.NO_OF_COLUMN.value)) {alert("Please enter valid No_OF_COLUMN.");document.form1.NO_OF_COLUMN.focus(); return false;}
   if(isNaN(document.form1.QTY_PER_TRAY.value)) {alert("Please enter valid QTY_PER_TRAY.");document.form1.QTY_PER_TRAY.focus(); return false;}
   if(isNaN(document.form1.NO_OF_TRAY_PER_LAYER.value)) {alert("Please enter valid NO_OF_TRAY_PER_LAYER.");document.form1.NO_OF_TRAY_PER_LAYER.focus(); return false;}
   if(isNaN(document.form1.NO_OF_LAYERS.value)) {alert("Please enter valid NO_OF_LAYERS.");document.form1.NO_OF_LAYERS.focus(); return false;}
   if(isNaN(document.form1.NO_OF_TRAY_PER_PALLET.value)) {alert("Please enter valid NO_OF_TRAY_PER_PALLET.");document.form1.NO_OF_TRAY_PER_PALLET.focus(); return false;}
   if(isNaN(document.form1.KFACTOR.value)) {alert("Please enter valid KFACTOR."); document.form1.KFACTOR.focus(); return false;}
   
  
  
   document.form1.action  = "prd_cls_mst_view.jsp?action=UPDATE";
   document.form1.submit();
}
function onDelete(){
   var PRD_CLS_ID   = document.form1.PRD_CLS_ID.value; 
   if(PRD_CLS_ID == "" || PRD_CLS_ID == null) {alert("Please Enter/Choose Prd Cls Id"); return false; }
 
   document.form1.action  = "prd_cls_mst_view.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
    var PRD_CLS_ID   = document.form1.PRD_CLS_ID.value; 
    if(PRD_CLS_ID == "" || PRD_CLS_ID == null) {alert("Please Enter/Choose Prd Cls Id"); return false; }

   document.form1.action  = "prd_cls_mst_view.jsp?action=VIEW";
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
String sPrdClsId  =   "",
       sPrdClsDesc  = "",
       sPrdGrpId ="",
       sPrice ="",
       sPrdClsDesc1 ="",
      //sTrayGrpId ="",
        sUom ="",
        sKfactor= "",
         sQtyPerCol="",
          sNoOfCol ="",
      sQtyPerTray ="",
      sNoOfTrayPerLayer="",
      sNoOfLayers="",
      sNoOfTrayPerPallet="";


StrUtils strUtils = new StrUtils();
PrdClsUtil prdclsUtil = new PrdClsUtil();
action                   = strUtils.fString(request.getParameter("action"));


sPrdClsId  = strUtils.fString(request.getParameter("PRD_CLS_ID"));
if(sPrdClsId.length() <= 0) sPrdClsId  = strUtils.fString(request.getParameter("PRD_CLS_ID1"));
sPrdClsDesc  = strUtils.fString(request.getParameter("PRD_CLS_DESC"));
sPrdGrpId  = strUtils.fString(request.getParameter("PRD_GRP_ID"));
if(sPrdGrpId.length() <= 0) sPrdGrpId  = strUtils.fString(request.getParameter("PRD_GRP_ID1"));
//sTrayGrpId  = strUtils.fString(request.getParameter("TRAY_GRP_ID"));
//if(sTrayGrpId.length() <= 0) sTrayGrpId  = strUtils.fString(request.getParameter("TRAY_GRP_ID1"));
sPrdClsDesc1  = strUtils.fString(request.getParameter("PRD_CLS_DESC1"));
sPrice  = strUtils.fString(request.getParameter("PRICE"));
sUom  = strUtils.fString(request.getParameter("UOM"));
sKfactor =strUtils.fString(request.getParameter("KFACTOR"));
sQtyPerCol  = strUtils.fString(request.getParameter("QTY_PER_COLUMN"));
sNoOfCol  = strUtils.fString(request.getParameter("NO_OF_COLUMN"));
sQtyPerTray  = strUtils.fString(request.getParameter("QTY_PER_TRAY"));
sNoOfTrayPerLayer  = strUtils.fString(request.getParameter("NO_OF_TRAY_PER_LAYER"));
sNoOfLayers  = strUtils.fString(request.getParameter("NO_OF_LAYERS"));
sNoOfTrayPerPallet  = strUtils.fString(request.getParameter("NO_OF_TRAY_PER_PALLET"));




//1. >> New
if(action.equalsIgnoreCase("NEW")){
      sPrdClsId  = "";
      sPrdClsDesc  = "";
      sPrdGrpId ="";
      //sTrayGrpId ="";
      sPrdClsDesc1 ="";
      sPrice ="";
      sUom ="";
      sQtyPerCol="";
       sKfactor ="";
          sNoOfCol ="";
      sQtyPerTray ="";
      sNoOfTrayPerLayer="";
      sNoOfLayers="";
      sNoOfTrayPerPallet="";
      sAddEnb    = "enabled";
     


}

//2. >> Add
else if(action.equalsIgnoreCase("ADD")){
	System.out.println("Add"+action);
  PrdGrpMstUtil prdGrpUtil =  new PrdGrpMstUtil();
    Hashtable htprdGrp = new Hashtable();
    htprdGrp.put(MDbConstant.PRD_GRP_ID,sPrdGrpId);
    if((prdGrpUtil.isExistsPrdGrpId(htprdGrp))){

/* TrayGrpUtil trayGrpUtil =  new TrayGrpUtil();
    Hashtable httrayGrp = new Hashtable();
    httrayGrp.put(MDbConstant.TRAY_GRP,sTrayGrpId);
    if((trayGrpUtil.isExistsTrayGrp(httrayGrp))){*/

   Hashtable ht = new Hashtable();
   ht.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
    if(!(prdclsUtil.isExistsPrdClsId(ht))) // if the Prd Cls  exists already
    {
          ht.put(MDbConstant.PRD_CLS_DESC,sPrdClsDesc);
          ht.put(MDbConstant.PRD_GRP_ID,sPrdGrpId);
         // ht.put(MDbConstant.TRAY_GRP_ID,sTrayGrpId);
          ht.put(MDbConstant.PRD_CLS_DESC1,sPrdClsDesc1);
          ht.put(MDbConstant.PRICE,sPrice);
          ht.put(MDbConstant.KFACTOR,sKfactor);
          ht.put(MDbConstant.PRD_CLS_UOM,sUom);
          ht.put(MDbConstant.QTY_PER_COL,sQtyPerCol);
          ht.put(MDbConstant.NO_OF_COLUMN,sNoOfCol);
          ht.put(MDbConstant.QTY_PER_TRAY,sQtyPerTray);
          ht.put(MDbConstant.NO_OF_TRAY_PER_LAYER,sNoOfTrayPerLayer);
          ht.put(MDbConstant.NO_OF_LAYERS,sNoOfLayers);
          ht.put(MDbConstant.NO_OF_LAYERS_PER_PALLET,sNoOfTrayPerPallet);
          
          ht.put(MDbConstant.CREATED_AT,new DateUtils().getDateTime());
          ht.put(MDbConstant.LOGIN_USER,sUserId);
          boolean PrdClsInserted = prdclsUtil.insertPrdClsMst(ht);
          if(PrdClsInserted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Product Class Added Successfully</font>";
                    sAddEnb  = "disabled";

                  mv.insertMovHisLogger(sUserId, "Product_Class","Product Class   :"  + sPrdClsId +"  Added Successfully " ); 


          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to add New Product Class</font>";
                    sAddEnb  = "enabled";
                   
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Product Class Exists already. Try again</font>";
           sAddEnb = "enabled";
          
    }
    }else{
    res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter Valid Loc Product Group. Try again</font>";
           sAddEnb = "enabled";
    }
    
 //   mv.insertMovHisLogger(sUserId, "Product_Class", res); 

}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
   
    sAddEnb  = "disabled";
     PrdGrpMstUtil prdGrpUtil =  new PrdGrpMstUtil();
    Hashtable htprdGrp = new Hashtable();
    htprdGrp.put(MDbConstant.PRD_GRP_ID,sPrdGrpId);
    if((prdGrpUtil.isExistsPrdGrpId(htprdGrp))){
    
 /*    TrayGrpUtil trayGrpUtil =  new TrayGrpUtil();
    Hashtable httrayGrp = new Hashtable();
    httrayGrp.put(MDbConstant.TRAY_GRP,sTrayGrpId);
    if((trayGrpUtil.isExistsTrayGrp(httrayGrp))){*/


    Hashtable ht = new Hashtable();
    ht.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
    if((prdclsUtil.isExistsPrdClsId(ht)))
    {
          Hashtable htUpdate = new Hashtable();
          htUpdate.put(MDbConstant.PRD_CLS_DESC,sPrdClsDesc);
          htUpdate.put(MDbConstant.PRD_GRP_ID,sPrdGrpId);
          //  ht.put(MDbConstant.TRAY_GRP_ID,sTrayGrpId);
          htUpdate.put(MDbConstant.PRD_CLS_DESC1,sPrdClsDesc1);
          htUpdate.put(MDbConstant.PRICE,sPrice);
           htUpdate.put(MDbConstant.KFACTOR,sKfactor);
          htUpdate.put(MDbConstant.PRD_CLS_UOM,sUom);
          htUpdate.put(MDbConstant.QTY_PER_COL,sQtyPerCol);
          htUpdate.put(MDbConstant.NO_OF_COLUMN,sNoOfCol);
          htUpdate.put(MDbConstant.QTY_PER_TRAY,sQtyPerTray);
          htUpdate.put(MDbConstant.NO_OF_TRAY_PER_LAYER,sNoOfTrayPerLayer);
          htUpdate.put(MDbConstant.NO_OF_LAYERS,sNoOfLayers);
          htUpdate.put(MDbConstant.NO_OF_LAYERS_PER_PALLET,sNoOfTrayPerPallet);
  
     //     htUpdate.put(MDbConstant.UPDATED_AT,new DateUtils().getDateTime());
      //    htUpdate.put(MDbConstant.UPDATED_BY,sUserId);
          
          Hashtable htCondition = new Hashtable();
          htCondition.put(MDbConstant.PRD_CLS_ID,sPrdClsId);     
          boolean Updated = prdclsUtil.updatePrdClsId(htUpdate,htCondition);
          if(Updated) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +" >Product Class Updated Successfully</font>";
                    
                      mv.insertMovHisLogger(sUserId, "Product_Class","Product Class   :"  + sPrdClsId +"  Updated Successfully " ); 

          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +" >Failed to Update Product Class</font>";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Product Class doesn't not Exists. Try again</font>";

    }
    }else{
    res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter valid  Loc Product Group. Try again</font>";
    } 
    
   //  mv.insertMovHisLogger(sUserId, "Product_Class", res); 
    
}

//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
    Hashtable htDelete = new Hashtable();
    htDelete.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
    if(prdclsUtil.isExistsPrdClsId(htDelete))
    {
          boolean PrdClsDeleted = prdclsUtil.deletePrdClsId(htDelete);
          if(PrdClsDeleted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Product Class Deleted Successfully</font>";
                    sAddEnb    = "disabled";
                    
                    mv.insertMovHisLogger(sUserId, "Product_Class","Product Class   :"  + sPrdClsId +"  Deleted Successfully " ); 
 
                    sPrdClsId  = "";
                    sPrdClsDesc  = "";
                    sPrdGrpId ="";
                     // sTrayGrpId ="";
                     sPrdClsDesc1 ="";
                     sPrice ="";
      sUom ="";
      sQtyPerCol="";
                    sNoOfCol ="";
      sQtyPerTray ="";
      sNoOfTrayPerLayer="";
      sNoOfLayers="";
      sNoOfTrayPerPallet="";


          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to Delete Product Class</font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Product Class doesn't not Exists. Try again</font>";
    }
    
  //   mv.insertMovHisLogger(sUserId, "Product_Class", res); 
}

//4. >> View
else if(action.equalsIgnoreCase("VIEW")){
    Map map = prdclsUtil.getPrdClsIdDetails(sPrdClsId);
   if(map.size()>0) {
    sPrdClsId   = strUtils.fString((String)map.get("PRD_CLS_ID"));
    sPrdClsDesc   = strUtils.fString((String)map.get("PRD_CLS_DESC"));
    sPrdGrpId   = strUtils.fString((String)map.get("PRD_GRP_ID"));
    sPrdClsDesc1   = strUtils.fString((String)map.get("PRD_CLS_DESC1"));
    sPrice   = strUtils.fString((String)map.get("PRICE"));
    sKfactor   = strUtils.fString((String)map.get("KFACTOR"));
    //  sTrayGrpId   = strUtils.fString((String)map.get("TRAY_GRP_ID"));
    sUom   = strUtils.fString((String)map.get("UOM"));
    sQtyPerCol   = strUtils.fString((String)map.get("QTY_PER_COLUMN"));
    sNoOfCol   = strUtils.fString((String)map.get("NO_OF_COLUMN"));
    sQtyPerTray   = strUtils.fString((String)map.get("QTY_PER_TRAY"));
    sNoOfTrayPerLayer   = strUtils.fString((String)map.get("NO_OF_TRAY_PER_LAYER"));
    sNoOfLayers   = strUtils.fString((String)map.get("NO_OF_LAYERS"));
    sNoOfTrayPerPallet   = strUtils.fString((String)map.get("NO_OF_LAYERS_PER_PALLET"));
      
    }else{
     res = "<font class = "+MDbConstant.FAILED_COLOR +">Product Class doesn't not Exists. Try again</font>";
    }

}

%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">PRODUCT CLASS MASTER</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
    <TR>
         <TH WIDTH="13%" ALIGN="RIGHT" > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* Prd Cls Id : </TH>
         <TD width="41%">
           <INPUT name="PRD_CLS_ID" type="TEXT" value="<%=sPrdClsId%>" size="25" MAXLENGTH="20"/>
           <a href="#" onClick="javascript:popWin('listView/prd_cls_list.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><img src="images/populate.gif" border="0"></a> 
             <INPUT type = "hidden" name="PRD_CLS_ID1" value = "">
           <INPUT class="Submit" type="BUTTON" value="View" onClick="onView();"/>
         </TD>
        <TH WIDTH="14%" ALIGN="RIGHT" > Description : </TH>
         <TD width="42%">
           <INPUT name="PRD_CLS_DESC" type="TEXT" value="<%=sPrdClsDesc%>" size="25" MAXLENGTH="80"/>
         </TD>
    </TR>
    
    <TR>
        <TH WIDTH="13%" ALIGN="RIGHT" >* Loc Prd Grp Id : </TH>
         <TD width="41%">
           <INPUT name="PRD_GRP_ID" type="TEXT" value="<%=sPrdGrpId%>" size="25" MAXLENGTH="80"/>
           <INPUT type = "hidden" name="PRD_GRP_ID1" value = "">
             <a href="#" onClick="javascript:popWin('listView/prd_list_grp.jsp?PRD_GRP_ID='+form1.PRD_GRP_ID.value);"><img src="images/populate.gif" border="0"></a> 
         </TD>
         <TH WIDTH="13%" ALIGN="RIGHT">Uom :</TH>
         <TD width="41%">
           <INPUT name="UOM" type="TEXT" value="<%=sUom%>" size="25" MAXLENGTH="80"/>
         </TD>

    </TR>
     <TR>
        <TH WIDTH="13%" ALIGN="RIGHT" >* Detail Desc : </TH>
         <TD width="41%">
           <INPUT name="PRD_CLS_DESC1" type="TEXT" value="<%=sPrdClsDesc1%>" size="25" MAXLENGTH="80"/>
        </TD>
         <TH WIDTH="13%" ALIGN="RIGHT">* Price :</TH>
         <TD width="41%">
           <INPUT name="PRICE" type="TEXT" value="<%=sPrice%>" size="25" MAXLENGTH="80"/>
         </TD>

    </TR>
    <TR>
      <TH WIDTH="14%" ALIGN="RIGHT">Qty Per Column :</TH>
      <TD width="32%">
        <INPUT name="QTY_PER_COLUMN" type="TEXT" value="<%=sQtyPerCol%>" size="25" MAXLENGTH="80"/>
      </TD>
      <TH WIDTH="13%" ALIGN="RIGHT">No Of Column :</TH>
      <TD width="41%">
        <INPUT name="NO_OF_COLUMN" type="TEXT" value="<%=sNoOfCol%>" size="25" MAXLENGTH="80"/>
      </TD>

    </TR>
     <TR>
       <TH WIDTH="14%" ALIGN="RIGHT">Qty Per Tray :</TH>
       <TD width="32%">
         <INPUT name="QTY_PER_TRAY" type="TEXT" value="<%=sQtyPerTray%>" size="25" MAXLENGTH="80"/>
       </TD>
       <TH WIDTH="13%" ALIGN="RIGHT">No Of Tray Per Layer :</TH>
       <TD width="41%">
         <INPUT name="NO_OF_TRAY_PER_LAYER" type="TEXT" value="<%=sNoOfTrayPerLayer%>" size="25" MAXLENGTH="80"/>
       </TD>

    </TR>
     <TR>
       <TH WIDTH="14%" ALIGN="RIGHT">No Of Layers :</TH>
       <TD width="32%">
         <INPUT name="NO_OF_LAYERS" type="TEXT" value="<%=sNoOfLayers%>" size="25" MAXLENGTH="80"/>
       </TD>
       <TH WIDTH="13%" ALIGN="RIGHT">No Of Tray Per Pallet :</TH>
       <TD width="41%">
         <INPUT name="NO_OF_TRAY_PER_PALLET" type="TEXT" value="<%=sNoOfTrayPerPallet%>" size="25" MAXLENGTH="80"/>
       </TD>

    </TR>
      <TR>
      
       <TH WIDTH="13%" ALIGN="RIGHT">* KFactor:</TH>
       <TD width="41%">
         <INPUT name="KFACTOR" type="TEXT" value="<%=sKfactor%>" size="25" MAXLENGTH="80"/>
       </TD>

    </TR>
<TR>
         <TH WIDTH="13%" ALIGN="RIGHT" >&nbsp;</TH>
         <TD width="41%">&nbsp;
         </TD>
         

    </TR>
    <TR>
         <TD COLSPAN = 4><BR><B><CENTER><%=res%></B></TD>
    </TR>
    <TR>
         <TD COLSPAN = 4><center>
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

