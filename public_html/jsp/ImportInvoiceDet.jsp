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

function onGo()
{
 
	var frmRoot=document.form1;
  
 	if(frmRoot.ImportFile.value=="" || frmRoot.ImportFile.value.length==0)
	{
		alert("Please Select Import file!");
		frmRoot.ImportFile.focus();
		return false;
	}
  else if(frmRoot.SheetName.value=="" || frmRoot.SheetName.value.length==0)
	{
		alert("Please Enter Sheet Name!");
		frmRoot.SheetName.focus();
		return false;
	}
  else{
  // document.form1.action = "ImportTraveler.jsp?action=import";
  frmRoot.action = "ImportInvoiceDet.jsp?action=import";
  frmRoot.submit();
  // document.form1.submit();
  return true;
  }
  
 
 
 //remove later
 /* testing
  frmRoot.action = "ImportSO.jsp?action=import";
  frmRoot.submit();
   return true;
   */
}
</script>
<title>Import Invoice Det</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
//DataDownloaderUtil _DataDownloaderUtil = new DataDownloaderUtil();
InvoiceDownloadUtil _invoiceDownloaderUtil =  new com.murho.db.utils.InvoiceDownloadUtil();
ArrayList invQryList  = new ArrayList();
MLogger logger = new MLogger();
boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
String fieldDesc="";
UserTransaction ut=null ;
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;

String ServerName =  strUtils.fString(request.getRemoteHost()).trim();
System.out.println("RemoteHost Name :"+ServerName);

String action         = strUtils.fString(request.getParameter("action")).trim();
//System.out.println("Action *** :" + action);
 MLogger.log(1, "_______________________ " + this.getClass() + "");

String StrFileName    = strUtils.fString(request.getParameter("ImportFile"));
String StrSheetName    = strUtils.fString(request.getParameter("SheetName"));

System.out.println("Import File :" + StrFileName);
System.out.println("Import sheet :" + StrSheetName);

if(action.equalsIgnoreCase("Import")){
 try{
  
  
   //  failedline= new com.murho.db.utils.SoDownloaderUtil().downloadData(StrFileName,StrSheetName);
    failedline= _invoiceDownloaderUtil.downloadData(StrFileName,StrSheetName);
    MLogger.info("SoDownloaderUtil.downloadData()  :" + failedline);
   
    if(failedline)
    {
    
   
    fieldDesc = "<tr><td><B><h3><centre>Data downloaded successfully!</centre><h3></td></tr>";
    
   // response.sendRedirect("./TravelerView.jsp");
    }
    else{
     fieldDesc = "<tr><td><B><h3><centre>Unable to download data!</centre><h3></td></tr>";
    }
    
  }catch(Exception e) {
   System.out.println("Excecuting Exception part");
    MLogger.log(0,"Exception :getInvList"+e.toString() );  
  fieldDesc = "<tr><td><B><h3><centre>" + e.toString() + "!</centre><h3></td></tr>";
 }
  System.out.println("fieldDesc : " + fieldDesc);
}

%>
<%@ include file="body.jsp"%>
  <FORM name="form1" method="post" action="" >
  <input type="hidden" value="" name="download">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Import &nbsp;ZShipping9</FONT>      </TH>
    </TR>
  </TABLE>
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
  <br>
  <TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

       <TR>
		   <TH ALIGN="RIGHT" >&nbsp;</TH>
                      <TD>&nbsp;</TD>
                      <TH ALIGN="RIGHT" >&nbsp;Impot Delivery&nbsp;No. File:</TH>
                      <TD>
                         <INPUT name="ImportFile" type = "file"  size="20"  MAXLENGTH=20>
                      </td>
                     
                      <TH ALIGN="RIGHT" >&nbsp;Enter Sheet Name:</TH>
                      <TD>
                      
                        <INPUT name="SheetName" type = "text" value="ZSHIPPING9" size="20"  MAXLENGTH=20>
                      </td>
                     
                     
                      <td>
                         <input type="button" name="aa" value="Import" onClick="javascript:return onGo();"/>
                      </TD>
		  </TR>
     

  </TABLE>
  <br>
 
   
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <!--   <input type="button" value="Approve" onClick="javascript:return onApprove();"/></TD> -->
      <TR>&nbsp;  <input type="button" value="Back" onClick="window.location.href='indexPage.jsp'">
       
   
     </td>
    </td>
    </P>
      
  </FORM>
   
<%@ include file="footer.jsp"%>
<% MLogger.log(-1, "_______________________ " + this.getClass() + ""); %>