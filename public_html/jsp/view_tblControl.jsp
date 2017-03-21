<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>
 
<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Table Control Master</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">
function popUpWin(URL) {
 subWin = window.open(URL, 'TBLCONTROL', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "view_tblControl.jsp?action=NEW";
   document.form1.submit();
}
function onAdd(){

   var sFunctionName = document.form1.FUNCTION_NAME.value;
     var sMinSeqNo = document.form1.MIN_SEQ_NO.value;
     var sMaxSeqNo = document.form1.MAX_SEQ_NO.value;
     var sPreFix   = document.form1.PREFIX.value;
  
   if(sFunctionName == "" || sFunctionName == null) {alert("Please Enter the Function Name"); document.form1.FUNCTION_NAME.focus(); return false; }
     if(sPreFix == "" || sPreFix == null) {alert("Please Enter Prefix"); document.form1.PREFIX.focus(); return false; }
     if(sMinSeqNo == "" || sMinSeqNo == null) {alert("Please Enter the Minimum Sequence number"); document.form1.MIN_SEQ_NO.focus(); return false; }
     if(sMaxSeqNo == "" || sMaxSeqNo == null) {alert("Please Enter the Maximum Sequence number"); document.form1.MAX_SEQ_NO.focus(); return false; }

   document.form1.action  = "view_tblControl.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
    var sFunctionName = document.form1.FUNCTION_NAME.value;
      var sPreFix   = document.form1.PREFIX.value;
     if(sFunctionName == "" || sFunctionName == null) {alert("Please Enter the Function Name"); document.form1.FUNCTION_NAME.focus(); return false; }
     if(sPreFix == "" || sPreFix == null) {alert("Please Enter Prefix"); document.form1.PREFIX.focus(); return false; }

   document.form1.action  = "view_tblControl.jsp?action=UPDATE";
   document.form1.submit();
}
function onDelete(){
  var sFunctionName = document.form1.FUNCTION_NAME.value;
      var sPreFix   = document.form1.PREFIX.value;
     if(sFunctionName == "" || sFunctionName == null) {alert("Please Enter the Function Name"); document.form1.FUNCTION_NAME.focus(); return false; }
     if(sPreFix == "" || sPreFix == null) {alert("Please Enter Prefix"); document.form1.PREFIX.focus(); return false; }
   var con = confirm ("Are you sure want to delete the Table Control Master?");
   if(con){
   document.form1.action  = "view_tblControl.jsp?action=DELETE";
   document.form1.submit();
   }else{
   return false;
   }
}
function onView(){
     var sFunctionName = document.form1.FUNCTION_NAME.value;
     var sPreFix   = document.form1.PREFIX.value;
     if(sFunctionName == "" || sFunctionName == null) {alert("Please Enter the Function Name"); document.form1.FUNCTION_NAME.focus(); return false; }
     if(sPreFix == "" || sPreFix == null) {alert("Please Enter Prefix"); document.form1.PREFIX.focus(); return false; }
 
   document.form1.action  = "view_tblControl.jsp?action=VIEW";
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
String sTblEnb   = "enabled";
String action     = "";
String sFunc  = "",
       sDesc  = "",
       sPrefix     = "",
       sSuffix     = "",
       sMinSeq     = "",
       sMaxSeq     = "",
       sNextSeq    = "";

StrUtils strUtils = new StrUtils();
TblControlUtil tblUtil = new TblControlUtil();
action            = strUtils.fString(request.getParameter("action"));


sFunc  = strUtils.fString(request.getParameter("FUNCTION_NAME"));
if(sFunc.length() <= 0) sFunc  = strUtils.fString(request.getParameter("FUNCTION_NAME1"));
sDesc     = strUtils.fString(request.getParameter("FN_DESC"));
sPrefix   = strUtils.fString(request.getParameter("PREFIX"));
//sSuffix   = strUtils.fString(request.getParameter("POSTFIX"));
sMinSeq   = strUtils.fString(request.getParameter("MIN_SEQ_NO"));
sMaxSeq   = strUtils.fString(request.getParameter("MAX_SEQ_NO"));
sNextSeq  = strUtils.fString(request.getParameter("NXT_SEQ_NO"));

//1. >> New
if(action.equalsIgnoreCase("NEW")){

        sFunc   = "";
       sDesc    = "";
       sPrefix  = "";
       sSuffix  = "";
       sMinSeq  = "";
       sMaxSeq  = "";
       sNextSeq = "";
      sAddEnb   = "enabled";
      sTblEnb   = "enabled";
}

//2. >> Add
else if(action.equalsIgnoreCase("ADD")){
Hashtable htx = new Hashtable();     
      htx.put(MDbConstant.TBL_FUNCTION,sFunc);
      htx.put(MDbConstant.TBL_PREFIX,sPrefix);
    if(!(tblUtil.isExistInTblControl(htx)))
    {
          Hashtable ht = new Hashtable();
          ht.put(MDbConstant.PLANT,CibaConstants.cibacompanyName);
          ht.put(MDbConstant.TBL_FUNCTION,sFunc);
          ht.put(MDbConstant.DESCRIPTION,sDesc);
          ht.put(MDbConstant.TBL_PREFIX,sPrefix);
          ht.put(MDbConstant.TBL_POSTFIX,sSuffix);
          ht.put(MDbConstant.TBL_MIN_SEQ,sMinSeq);
          ht.put(MDbConstant.TBL_MAX_SEQ,sMaxSeq);
          ht.put(MDbConstant.TBL_NEXT_SEQ,sNextSeq);
          ht.put("RECSTAT","N");
          ht.put(MDbConstant.CREATED_AT,new DateUtils().getDateTime());
          ht.put(MDbConstant.LOGIN_USER,sUserId);
          boolean tblInserted = tblUtil.insertTblControl(ht);
          if(tblInserted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR+">Function Added Successfully</font>";
                    sAddEnb  = "disabled";
                    sTblEnb = "disabled";
                    
                    mv.insertMovHisLogger(sUserId, "Table_Control","Function :"  + sFunc +"  and Prifix : " +  sPrefix +  "  Added Successfully " ); 

          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR+">Failed to add New Function</font>";
                    sAddEnb  = "enabled";
                    sTblEnb = "enabled";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR+">Function Exists already. Try again</font>";
           sAddEnb = "enabled";
           sTblEnb = "enabled";
    }
    
 //    mv.insertMovHisLogger(sUserId, "TableControl", res);  
}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
    sTblEnb = "disabled";
    sAddEnb  = "disabled";
      Hashtable htex = new Hashtable();     
      htex.put(MDbConstant.TBL_FUNCTION,sFunc);
      htex.put(MDbConstant.TBL_PREFIX,sPrefix);
    if((tblUtil.isExistInTblControl(htex)))
    {
          Hashtable htUpdate = new Hashtable();
          //htUpdate.put(MDbConstant.PLANT,IConstants.PLANT_VAL);
          htUpdate.put(MDbConstant.TBL_FUNCTION,sFunc);
          htUpdate.put(MDbConstant.DESCRIPTION,sDesc);
          htUpdate.put(MDbConstant.TBL_PREFIX,sPrefix);
          htUpdate.put(MDbConstant.TBL_POSTFIX,sSuffix);
          htUpdate.put(MDbConstant.TBL_MIN_SEQ,sMinSeq);
          htUpdate.put(MDbConstant.TBL_MAX_SEQ,sMaxSeq);
          htUpdate.put(MDbConstant.TBL_NEXT_SEQ,sNextSeq);
          htUpdate.put("RECSTAT","N");
          htUpdate.put(MDbConstant.UPDATED_AT,new DateUtils().getDateTime());
          htUpdate.put(MDbConstant.UPDATED_BY,sUserId);

          Hashtable htCondition = new Hashtable();
          htCondition.put(MDbConstant.TBL_FUNCTION,sFunc);
          htCondition.put(MDbConstant.TBL_PREFIX,sPrefix);
          boolean tblUpdated = tblUtil.updateTblControl(htUpdate,htCondition);
          if(tblUpdated) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR+">Function Updated Successfully</font>";
                    
           mv.insertMovHisLogger(sUserId, "Table_Control","Function :"  + sFunc +"  and Prifix : " +  sPrefix +  " details  Updated Successfully " ); 

          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR+">Failed to the </font>";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR+">Function doesn't not Exists. Try again</font>";

    }
 //   mv.insertMovHisLogger(sUserId, "TableControl", res);  
}
//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
    sTblEnb = "disabled";
       Hashtable ht = new Hashtable();
        //  ht.put(MDbConstant.PLANT,MDbConstant.PLANT_VAL);
          ht.put(MDbConstant.TBL_FUNCTION,sFunc);
          ht.put(MDbConstant.TBL_PREFIX,sPrefix);
    if(tblUtil.isExistInTblControl(ht))
    {
          boolean tblDeleted = tblUtil.deleteTblControl(ht);
          if(tblDeleted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR+">Function Deleted Successfully</font>";
                    sAddEnb    = "disabled";
                    
                    mv.insertMovHisLogger(sUserId, "Table_Control","Function :"  + sFunc +"  and Prifix : " +  sPrefix +  " details  Deleted Successfully " ); 

                    sFunc   = "";
                    sDesc    = "";
                    sPrefix  = "";
                    sSuffix  = "";
                    sMinSeq  = "";
                    sMaxSeq  = "";
                    sNextSeq = "";

          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR+">Failed to Delete Function</font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR+">Function doesn't not Exists. Try again</font>";
    }
 //   mv.insertMovHisLogger(sUserId, "TableControl", res);  
}
//4. >> View
//4. >> View
else if(action.equalsIgnoreCase("VIEW")){
    Map m = tblUtil.getTblControlDetails(sFunc,sPrefix);
   if (m.size()>0){
    sFunc    = (String)m.get(MDbConstant.TBL_FUNCTION);
    sDesc    = (String)m.get(MDbConstant.DESCRIPTION);
    sPrefix  =  (String)m.get(MDbConstant.TBL_PREFIX);
  // sSuffix  = (String)m.get(3);
    sMinSeq  = (String)m.get(MDbConstant.TBL_MIN_SEQ);
    sMaxSeq  = (String)m.get(MDbConstant.TBL_MAX_SEQ);
    sNextSeq = (String)m.get(MDbConstant.TBL_NEXT_SEQ);
    }
}
%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">TABLE CONTROL MASTER</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
     <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >* Function Name : </TH>
                      <TD><INPUT name="FUNCTION_NAME" type = "TEXT" value="<%=sFunc%>" size="50"  MAXLENGTH=20>
                      <a href="#" onClick="javascript:popUpWin('tblControl_list.jsp?FUNCTION_NAME='+form1.FUNCTION_NAME.value);"><img src="images/populate.gif" border="0"></a>
                      <INPUT class="Submit" type="BUTTON" value="View" onClick="onView();">
                     <INPUT name="FUNCTION_NAME1" type = "HIDDEN"  VALUE="<%=sFunc%>">
                    </TD>
                    </TR>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Description : </TH>
                      <TD><INPUT name="FN_DESC" type = "TEXT" value="<%=sDesc%>" size="50"  MAXLENGTH=80 > </TD>
                    </TR>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >* Prefix : </TH>
                      <TD><INPUT name="PREFIX" type = "TEXT" value="<%=sPrefix%>" size="50"  MAXLENGTH=20 > </TD>
                    </TR>
                  <!--  <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Suffix : </TH>
                      <TD><INPUT name="POSTFIX" type = "TEXT" value="<%=sSuffix%>" size="50"  MAXLENGTH=20 > </TD>
                    </TR>-->
                     <INPUT name="POSTFIX" type = "HIDDEN"  VALUE=" ">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >* Minimum Sequence Number : </TH>
                      <TD><INPUT name="MIN_SEQ_NO" type = "TEXT" value="<%=sMinSeq%>" size="50"  MAXLENGTH=20 > </TD>
                    </TR>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >* Maximum Sequence Number : </TH>
                      <TD><INPUT name="MAX_SEQ_NO" type = "TEXT" value="<%=sMaxSeq%>" size="50"  MAXLENGTH=20 > </TD>
                    </TR>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >* Current Sequence Number : </TH>
                      <TD><INPUT name="NXT_SEQ_NO" type = "TEXT" value="<%=sNextSeq%>" size="50"  MAXLENGTH=20 > </TD>
                    </TR>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >&nbsp;</TH>
                      <TD>* Mandatory Fields</TD>
                    </TR>
    <TR>
         <TD COLSPAN = 2><BR><B><CENTER><%=res%></B></TD>
    </TR>
    <TR>
         <TD COLSPAN = 2><center>
                <INPUT class="Submit" type="BUTTON" value="Cancel" onClick="window.location.href='indexPage.jsp'">&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="New" onClick="onNew();" <%=sNewEnb%>>&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="Add" onClick="onAdd();" <%=sAddEnb%>>&nbsp;&nbsp;
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

