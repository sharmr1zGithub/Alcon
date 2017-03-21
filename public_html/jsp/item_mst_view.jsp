<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>

<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">  
<script src="//code.jquery.com/jquery-1.10.2.js"></script>  
<script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>

<title>Item  Master</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">


function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "item_mst_view.jsp?action=NEW";
   document.form1.submit();
}
function onAdd(){
   var ITEM_ID   = document.form1.ITEM_ID.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Item Id "); return false; }
    
      var UOM   = document.form1.UOM.value;
    if(UOM == "" || UOM == null) {alert("Please Enter Uom "); return false; }
  //Added By Ranjana Sharma on 22/05/2015 WO0000000471852 -UDI Part 2 Implementation
      var GTIN2   = document.form1.GTIN2.value;
    if(GTIN2 == "" || GTIN2 == null) {alert("Please Enter GTIN2 "); return false; }
    
   document.form1.action  = "item_mst_view.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
 
    var ITEM_ID   = document.form1.ITEM_ID.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Item Id "); return false; }
    
    var UOM   = document.form1.UOM.value;
    if(UOM == "" || UOM == null) {alert("Please Enter Uom "); return false; }
 //Added By Ranjana Sharma on 22/05/2015 WO0000000471852 -UDI Part 2 Implementation
      var GTIN2   = document.form1.GTIN2.value;
    if(GTIN2 == "" || GTIN2 == null) {alert("Please Enter GTIN2 "); return false; }
   
   document.form1.action  = "item_mst_view.jsp?action=UPDATE";
   document.form1.submit();
}
function onDelete(){
      var ITEM_ID   = document.form1.ITEM_ID.value;
      if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Item Id "); return false; }
   document.form1.action  = "item_mst_view.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
    var ITEM_ID   = document.form1.ITEM_ID.value;
   if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Item Id "); return false; }

   document.form1.action  = "item_mst_view.jsp?action=VIEW";
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
String sItemId ="",sPrdClsId  =   "",sUom="",
       sItemDesc  = "",sBarcode="",
 //Added By Ranjana Sharma on 22/05/2015 WO0000000471852 -UDI Part 2 Implementation
       sGTIN2="";


StrUtils strUtils = new StrUtils();
ItemMstUtil itemUtil = new ItemMstUtil();
action            = strUtils.fString(request.getParameter("action"));

sItemId  = strUtils.fString(request.getParameter("ITEM_ID"));
sPrdClsId  = strUtils.fString(request.getParameter("PRD_CLS_ID"));
sUom  = strUtils.fString(request.getParameter("UOM"));
if(sItemId.length() <= 0) sItemId  = strUtils.fString(request.getParameter("ITEM_ID1"));
if(sPrdClsId.length() <= 0) sPrdClsId  = strUtils.fString(request.getParameter("PRD_CLS_ID1"));
sItemDesc  = strUtils.fString(request.getParameter("ITEM_DESC"));
sBarcode = strUtils.fString(request.getParameter("BARCODE"));
//Added By Ranjana Sharma on 22/05/2015 WO0000000471852 -UDI Part 2 Implementation
sGTIN2 = strUtils.fString(request.getParameter("GTIN2"));



//1. >> New
if(action.equalsIgnoreCase("NEW")){
      sItemId  = "";
      sItemDesc  = "";
      sPrdClsId ="";
      sUom      ="";
      sBarcode  ="";
//    Added By Ranjana Sharma on 22/05/2015 WO0000000471852 -UDI Part 2 Implementation
      sGTIN2="";
      sAddEnb    = "enabled";
     

}

//2. >> Add
else if(action.equalsIgnoreCase("ADD")){
   PrdClsUtil prdClsUtil = new PrdClsUtil();
   Hashtable htPrdCls = new Hashtable();
   htPrdCls.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
   if((prdClsUtil.isExistsPrdClsId(htPrdCls))){
   Hashtable ht = new Hashtable();
   ht.put(MDbConstant.PLANT,sPlant);
   ht.put(MDbConstant.ITEMMST_ITEM,sItemId);
    if(!(itemUtil.isExistsItemId(ht))) // if the Item  exists already
    {
          
          ht.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
          ht.put(MDbConstant.ITEMMST_UOM,sUom);
          ht.put(MDbConstant.ITEMMST_DESC,sItemDesc);      
          ht.put(MDbConstant.CREATED_AT,new DateUtils().getDateTime());
          ht.put(MDbConstant.LOGIN_USER,sUserId);
          ht.put("REFNO",sBarcode);      
//        Added By Ranjana Sharma on 22/05/2015 WO0000000471852 -UDI Part 2 Implementation
          ht.put(MDbConstant.ITEMMST_GTIN2,sGTIN2);
         
         
          boolean itemInserted = itemUtil.insertItemMst(ht);
          if(itemInserted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Item  Added Successfully</font>";
                    sAddEnb  = "disabled";
                    
                    
              mv.insertMovHisLogger(sUserId, "Item_Master","Sku  :"  + sItemId +"  Item  Added Successfully " ); 
           
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to add New Item </font>";
                    sAddEnb  = "enabled";
                   
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Item  Exists already. Try again</font>";
           sAddEnb = "enabled";
          
    }
    }else{
    res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter a valid  Prod Cls ID. Try again</font>";
           sAddEnb = "enabled";
    }
    
  //   mv.insertMovHisLogger(sUserId, "Item_Master", sItemId + ":"+ res); 
}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
   
   sAddEnb  = "disabled";
   PrdClsUtil prdClsUtil = new PrdClsUtil();
   Hashtable htprdCls = new Hashtable();
   htprdCls.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
   if((prdClsUtil.isExistsPrdClsId(htprdCls))){

    Hashtable ht = new Hashtable();
    ht.put(MDbConstant.ITEMMST_ITEM,sItemId);
    if((itemUtil.isExistsItemId(ht)))
    {
          Hashtable htUpdate = new Hashtable();
          htUpdate.put(MDbConstant.ITEMMST_DESC,sItemDesc);
          htUpdate.put(MDbConstant.PRD_CLS_ID,sPrdClsId);
          htUpdate.put(MDbConstant.ITEMMST_UOM,sUom);
          htUpdate.put("REFNO",sBarcode);  
//        Added By Ranjana Sharma on 22/05/2015 WO0000000471852 -UDI Part 2 Implementation
          htUpdate.put(MDbConstant.ITEMMST_GTIN2,sGTIN2);
          htUpdate.put(MDbConstant.UPDATED_AT,new DateUtils().getDateTime());
          htUpdate.put(MDbConstant.UPDATED_BY,sUserId);
          
          Hashtable htCondition = new Hashtable();
          htCondition.put(MDbConstant.ITEMMST_ITEM,sItemId);     
          boolean Updated = itemUtil.updateItemId(htUpdate,htCondition);
          if(Updated) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +" >Item  Updated Successfully</font>";
                    
                mv.insertMovHisLogger(sUserId, "Item_Master","Sku  :"  + sItemId +"  Updated Successfully " ); 
       
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +" >Failed to Update Item </font>";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Item  doesn't not Exists. Try again</font>";

    }
    }else{
               res = "<font class = "+MDbConstant.FAILED_COLOR +">Enter valid Prd Cls Id. Try again</font>";
    }
//     mv.insertMovHisLogger(sUserId, "Item_Master", sItemId +":"+res); 
    
}

//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
    Hashtable htDelete = new Hashtable();
    htDelete.put(MDbConstant.ITEMMST_ITEM,sItemId);
    if(itemUtil.isExistsItemId(htDelete))
    {
          boolean itemDeleted = itemUtil.deleteItemId(htDelete);
          if(itemDeleted) {
                    mv.insertMovHisLogger(sUserId, "Item_Master","Sku  :"  + sItemId +" Delete Successfully " ); 
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Item  Deleted Successfully</font>";
                    sAddEnb    = "disabled";
                    sItemId  = "";
                    sItemDesc  = "";
                    sPrdClsId ="";
                    sUom="";
                    sBarcode="";
//                  Added By Ranjana Sharma on 22/05/2015 WO0000000471852 -UDI Part 2 Implementation
                    sGTIN2="";

         
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to Delete Item </font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Item doesn't Exists. Try again</font>";
    }
   //  mv.insertMovHisLogger(sUserId, "Item_Master",sItemId +":"+ res); 
}

//4. >> View
else if(action.equalsIgnoreCase("VIEW")){
    Map map  = itemUtil.getItemDetails(sItemId);
   if(map.size()>0){
    sItemId   = strUtils.fString((String)map.get("ITEM"));
    sItemDesc   = strUtils.fString((String)map.get("ITEMDESC"));
    sPrdClsId   = strUtils.fString((String)map.get("CLASSID"));
     sUom   = strUtils.fString((String)map.get("STKUOM"));
      sBarcode   = strUtils.fString((String)map.get("REFNO"));
//    Added By Ranjana Sharma on 22/05/2015 WO0000000471852 -UDI Part 2 Implementation
      sGTIN2 = strUtils.fString((String)map.get("GTIN2"));
     
    }else{
     res = "<font class = "+MDbConstant.FAILED_COLOR +">Item does not Exists. Try again</font>";
    }

}

%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">ITEM MASTER</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Item Id : </TH>
         <TD>
                <INPUT name="ITEM_ID" type = "TEXT" value="<%=sItemId%>" size="50"  MAXLENGTH=20 >
                <INPUT type = "hidden" name="ITEM_ID1" value = "" >
				<a href="#" onClick="javascript:popWin('listView/item_mst_list.jsp?ITEM_ID='+form1.ITEM_ID.value);"><img src="images/populate.gif" border="0"></a> 
                <INPUT class="Submit" type="BUTTON" value="View" onClick="onView();">
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > Description : </TH>
         <TD>
             <INPUT name="ITEM_DESC" type = "TEXT" value="<%=sItemDesc%>" size="50"  MAXLENGTH=80>

         </TD>

    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Uom : </TH>
         <TD>
             <INPUT name="UOM" type = "TEXT" value="<%=sUom%>" size="50"  MAXLENGTH=80>

         </TD>

    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Prd Class Id : </TH>
         <TD>
             <INPUT name="PRD_CLS_ID" type = "TEXT" value="<%=sPrdClsId%>" size="50"  MAXLENGTH=80>
   <INPUT type = "hidden" name="PRD_CLS_ID1" value = "" >
   <INPUT type = "hidden" name="PRD_CLS_DESC" value = "" >
   <INPUT type = "hidden" name="PRD_GRP_ID" value = "" >
   	<a href="#" onClick="javascript:popWin('listView/prd_cls_list.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><img src="images/populate.gif" border="0"></a> 
         </TD>

    </TR>
    
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > Barcode : </TH>
         <TD>
             <INPUT name="BARCODE" type = "TEXT" value="<%=sBarcode%>" size="50"  MAXLENGTH=80>

         </TD>

    </TR>
<!--Added By Ranjana Sharma on 22/05/2015 WO0000000471852 -UDI Part 2 Implementation  -->
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > *GTIN2 : </TH>
         <TD>
             <INPUT name="GTIN2" type = "TEXT" value="<%=sGTIN2%>" size="50"  MAXLENGTH=80>

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

