<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="com.murho.utils.MLogger"%>
<%@ include file="header.jsp"%>

<html>
<head>
<script language="javascript">

function validateFile(form)
{
 return true;
}

function onGo()
{
 
  var frmRoot=document.form1;
  
 	if(frmRoot.ImportFile.value=="" || frmRoot.ImportFile.value.length==0 || frmRoot.ImportFile.value.indexOf(".txt") == -1)
	{
		alert("Please Select Import text file!");
		frmRoot.ImportFile.focus();
		return false;
	}
  else{
      var con = confirm ("Process will take few minutes  to download");
      if(con) {
         
          var file=frmRoot.ImportFile.value;
          var sheet="test";
          frmRoot.action = "/CibaVisionWms/InboundsDataDownloaderServlet?submit=import" + "&ImportFile=" + file +"&SheetName=" + sheet;
          frmRoot.submit();
          return true;
          }else
          { return false;}
    } 
 
}
</script>
<title>Import Delivery No.</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
String path = request.getContextPath();

StrUtils strUtils     = new StrUtils();
DataDownloaderUtil _DataDownloaderUtil = new DataDownloaderUtil();


ArrayList invQryList  = new ArrayList();
MLogger logger = new MLogger();
boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
String fieldDesc="";
String Strlot = "";
UserTransaction ut=null;
String PLANT       = (String)session.getAttribute("PLANT");
String LOGIN_USER        = (String)session.getAttribute("LOGIN_USER");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;

String ServerName =  strUtils.fString(request.getRemoteHost()).trim();
System.out.println("RemoteHost Name :"+ServerName);

String action         = strUtils.fString(request.getParameter("action")).trim();
//System.out.println("Action *** :" + action);
 MLogger.log(1, "_______________________ " + this.getClass() + "");
 
 String StrFileName    = strUtils.fString(request.getParameter("ImportFile"));
//String StrSheetName    = strUtils.fString(request.getParameter("SheetName"));	   

if(action.equalsIgnoreCase("Import")){
	
}
else if(action.equalsIgnoreCase("resultinerr1")){
 fieldDesc=(String)request.getSession().getAttribute("RESULTINERR1");
      
}
else if(action.equalsIgnoreCase("resultinerr2")){
 fieldDesc=(String)request.getSession().getAttribute("RESULTINERR2");     
}

else if(action.equalsIgnoreCase("resultinnoerr")){  
   fieldDesc=(String)request.getSession().getAttribute("RESULTINNOERR"); 

// Added For displaying and clearing LOT restriction message on UI.

   if((String)request.getSession().getAttribute("Strlot")!=null && (String)request.getSession().getAttribute("Strlot")!=""){
   Strlot = (String)request.getSession().getAttribute("Strlot");}
   session.removeAttribute("Strlot");
}
%>
<%@ include file="body.jsp"%>
 <!--- <FORM name="form1" method="post" enctype="multipart/form-data" action="/CibaVisionWms/InboundDataDownloaderServlet" >-->
  <form name="form1" method="post" enctype="multipart/form-data" action="/CibaVisionWms/InboundsDataDownloaderServlet">
 <input type="hidden" value="" name="download">
 <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">IMPORT DELIVERY NO</FONT>..</TH>
    </TR>
  </TABLE>
    <font face="Times New Roman" size="4">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    
      <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=Strlot%>
      </table>
    </font>
  <br>
  <TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

       <TR>
		   <TH ALIGN="RIGHT" width="3%">&nbsp;</TH>
                       <TD width="0%">&nbsp;</TD>
                       <TH ALIGN="RIGHT" width="27%">&nbsp;Import Delivery No. File:</TH>
                      
                      <TD width="30%">
                         <INPUT name="ImportFile" type = "file"  size="20"  MAXLENGTH=20>
                      </TD>
                                  
                      <TD width="27%">
                          <input type="Button" name="Submit" value="Import"  onclick="javascript:return onGo();"/>
                      </TD>
		  </TR>
     
  </TABLE>
  <br>
 
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <!--   <input type="button" value="Approve" onClick="javascript:return onApprove();"/></TD> -->
      <TR>&nbsp;  <input type="button" value="Back" onClick="window.location.href='indexPage.jsp'">
       
   
     </td>
    </td>
    </P>
      
  </FORM>
   
<%@ include file="footer.jsp"%>
<% MLogger.log(-1, "_______________________ " + this.getClass() + ""); %>