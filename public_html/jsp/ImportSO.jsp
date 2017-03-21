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

/*function onGo()
{
 	var frmRoot=document.form1;
  
 	if(frmRoot.ImportFile.value == "" || frmRoot.ImportFile.value.length==0 || frmRoot.ImportFile.value.indexOf(".txt") == -1)
	{
		alert("Please select valid import text file!");
		frmRoot.ImportFile.focus();
		return false;
	}
  
 
}*/

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
      var con = confirm ("Process will take few minutes to download");
      if(con) {
         
          var file=frmRoot.ImportFile.value;
          var sheet="test";
          frmRoot.action = "/CibaVisionWms/OutboundDataDownloaderServlet?submit=import" + "&ImportFile=" + file +"&SheetName=" + sheet;
          frmRoot.submit();
          return true;
          }else
          { return false;}
    } 
 
}
</script>

<title>Import SO</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
SoDownloaderUtil _SoDownloaderUtil =  new com.murho.db.utils.SoDownloaderUtil();
OBTravelerDetDAO obTravelDetDAO=new OBTravelerDetDAO();
ArrayList invQryList  = new ArrayList();
MLogger logger = new MLogger();
boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
String fieldDesc="",StrLot="";
UserTransaction ut=null ;
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;

String ServerName =  strUtils.fString(request.getRemoteHost()).trim();
System.out.println("RemoteHost Name :"+ServerName);

String action         = strUtils.fString(request.getParameter("action")).trim();
MLogger.log(1, "_______________________ " + this.getClass() + "");

String StrFileName    = strUtils.fString(request.getParameter("ImportFile"));

//out.print(CibaConstants.cibacompanyName);
System.out.println("Import File :" + StrFileName);
//String path = request.getContextPath();
boolean fLoad=false;

if(action.equalsIgnoreCase("Import")){
 
 //fieldDesc=(String)request.getSession().getAttribute("RESULTOUT");
}
else if(action.equalsIgnoreCase("result")){
    
    response.sendRedirect("./TravelerView.jsp");
    
//  Added For displaying and clearing LOT restriction message on UI.

    if ((String)request.getSession().getAttribute("BlockLot") != null && (String)request.getSession().getAttribute("BlockLot") != "") {
    StrLot = (String)request.getSession().getAttribute("BlockLot");
    }
}
else if(action.equalsIgnoreCase("resulterror1")){
   
    fieldDesc=(String)request.getSession().getAttribute("RESULTOUTERR1");
   
}
/*else if(action.equalsIgnoreCase("resulterror2")){
   
    fieldDesc=(String)request.getSession().getAttribute("RESULTOUTERR2");
 
}*/

%>
<%@ include file="body.jsp"%>
  <FORM name="form1" method="post"  enctype="multipart/form-data" action="/CibaVisionWms/OutboundDataDownloaderServlet" >
  <input type="hidden" value="" name="download">
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Import SO</FONT>
      </TH>
    </TR>
    </TABLE>
    <font face="Times New Roman" size="4">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
     <%=fieldDesc%>
    </TABLE>
    </font>
  <br>
  <TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

       <TR>
		   <TH ALIGN="RIGHT" width="3%">&nbsp;</TH>
           <TD width="0%">&nbsp;</TD>
           <TH ALIGN="RIGHT" width="27%">&nbsp;Import Delivery File:</TH>
          <TD width="30%">
            <INPUT name="ImportFile" type = "file"  size="20"  MAXLENGTH=20>
          </TD>
                                  
           <TD width="27%">
               <input type="Button" name="Submit" value="Import"  onclick="javascript:return onGo();"/>
           </TD>
		  </TR>
     
  </TABLE>
   
    <P>  </P>  
 
    <CENTER><TR>&nbsp;  <input type="button" value="Back" onClick="window.location.href='indexPage.jsp'"></TR></CENTER>
     
  </TABLE>
  
  </FORM>

   
<%@ include file="footer.jsp"%>
