<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ include file="header.jsp"%>

<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Department Master</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">
<!-- Begin

var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'DEPARTMENTS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "department_view.jsp?action=NEW";
   document.form1.submit();
}
function onAdd(){
   var DEPT_CODE   = document.form1.DEPT_CODE.value;
   if(DEPT_CODE == "" || DEPT_CODE == null) {alert("Please Choose Department Code"); return false; }

   document.form1.action  = "department_view.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
   var DEPT_CODE   = document.form1.DEPT_CODE.value;
   if(DEPT_CODE == "" || DEPT_CODE == null) {alert("Please Enter Department Code"); return false; }

   document.form1.action  = "department_view.jsp?action=UPDATE";
   document.form1.submit();
}
function onDelete(){
   var DEPT_CODE   = document.form1.DEPT_CODE.value;
   if(DEPT_CODE == "" || DEPT_CODE == null) {alert("Please Enter Department Code");  return false; }

   document.form1.action  = "department_view.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
   var DEPT_CODE   = document.form1.DEPT_CODE.value;
   if(DEPT_CODE == "" || DEPT_CODE == null) {alert("Please Enter Department Code"); return false; }

   document.form1.action  = "department_view.jsp?action=VIEW";
   document.form1.submit();
}

-->
</script>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String res        = "";

String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "disabled";
String sUpdateEnb = "enabled";
String sCustEnb   = "enabled";
String action     = "";
String sDeptCode  = "",
       sDeptDesc  = "";


StrUtils strUtils = new StrUtils();
DeptUtil deptUtil = new DeptUtil();
action            = strUtils.fString(request.getParameter("action"));


sDeptCode  = strUtils.fString(request.getParameter("DEPT_CODE"));
if(sDeptCode.length() <= 0) sDeptCode  = strUtils.fString(request.getParameter("DEPT_CODE1"));
sDeptDesc  = strUtils.fString(request.getParameter("DEPT_DESC"));


//1. >> New
if(action.equalsIgnoreCase("NEW")){
      sDeptCode  = "";
      sDeptDesc  = "";
      sAddEnb    = "enabled";
      sCustEnb   = "enabled";


}

//2. >> Add
else if(action.equalsIgnoreCase("ADD")){
    if(!(deptUtil.isExistDepartment(sDeptCode))) // if the Department exists already
    {
          Hashtable ht = new Hashtable();
          ht.put("PLANT",sDeptCode);
          ht.put("PLNTDESC",sDeptDesc);
          ht.put("CRAT",new DateUtils().getDateTime());
          ht.put("CRBY",sUserId);
          boolean deptInserted = deptUtil.insertDepartment(ht);
          if(deptInserted) {
                    res = "<font class = maingreen>Department Added Successfully</font>";
                    sAddEnb  = "disabled";
                    sCustEnb = "disabled";
          } else {
                    res = "<font class = mainred>Failed to add New Department</font>";
                    sAddEnb  = "enabled";
                    sCustEnb = "enabled";
          }
    }else{
           res = "<font class = mainred>Department Exists already. Try again</font>";
           sAddEnb = "enabled";
           sCustEnb = "enabled";
    }
}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
    sCustEnb = "disabled";
    sAddEnb  = "disabled";
    if((deptUtil.isExistDepartment(sDeptCode)))
    {
          Hashtable htUpdate = new Hashtable();
          htUpdate.put("PLANT",sDeptCode);
          htUpdate.put("PLNTDESC",sDeptDesc);
          htUpdate.put("UPAT",new DateUtils().getDateTime());
          htUpdate.put("UPBY",sUserId);

          Hashtable htCondition = new Hashtable();
          htCondition.put("PLANT",sDeptCode);
          boolean deptUpdated = deptUtil.updateDepartment(htUpdate,htCondition);
          if(deptUpdated) {
                    res = "<font class = maingreen>Department Updated Successfully</font>";
          } else {
                    res = "<font class = mainred>Failed to Update Department</font>";
          }
    }else{
           res = "<font class = mainred>Department doesn't not Exists. Try again</font>";

    }
}

//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
    sCustEnb = "disabled";
    if(deptUtil.isExistDepartment(sDeptCode))
    {
          boolean deptDeleted = deptUtil.deleteDepartment(sDeptCode);
          if(deptDeleted) {
                    res = "<font class = maingreen>Department Deleted Successfully</font>";
                    sAddEnb    = "disabled";
                    sDeptCode  = "";
                    sDeptDesc  = "";


          } else {
                    res = "<font class = mainred>Failed to Delete Department</font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = mainred>Department doesn't not Exists. Try again</font>";
    }
}

//4. >> View
else if(action.equalsIgnoreCase("VIEW")){
    ArrayList arrDept = deptUtil.getDepartmentDetails(sDeptCode);
    for(int i =0; i<arrDept.size(); i++) {
    sDeptCode   = strUtils.fString((String)arrDept.get(0));
    sDeptDesc   = strUtils.fString((String)arrDept.get(1));
    }

}

%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">DEPARTMENT MASTER</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Department Code : </TH>
         <TD>
                <INPUT name="DEPT_CODE" type = "TEXT" value="<%=sDeptCode%>" size="50"  MAXLENGTH=20 <%=sCustEnb%>>
                <INPUT type = "hidden" name="DEPT_CODE1" value = <%=sDeptCode%>>
				<a href="#" onClick="javascript:popUpWin('department_list.jsp?DEPT_CODE='+form1.DEPT_CODE.value);"><img src="images/populate.gif" border="0"></a>
                <INPUT class="Submit" type="BUTTON" value="View" onClick="onView();">
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > Description : </TH>
         <TD>
             <INPUT name="DEPT_DESC" type = "TEXT" value="<%=sDeptDesc%>" size="50"  MAXLENGTH=80>

         </TD>

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

