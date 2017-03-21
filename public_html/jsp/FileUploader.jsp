<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.murho.utils.MLogger"%>
<%@ include file="header.jsp"%>

<html>
<head>
<script language="javascript">

function onGo()
{

	var frmRoot=document.form1;
		
    
 	if(frmRoot.ImportFile.value=="" || frmRoot.ImportFile.value.length==0 || (frmRoot.ImportFile.value.indexOf(".csv") == -1 && frmRoot.ImportFile.value.indexOf(".CSV") == -1))
	{
		alert("Please Select Import CSV file!");
		frmRoot.ImportFile.focus();
		return false;
	}

  else{
    var con = confirm ("Process will take few minutes  to download");
      if(con) {
         
          var file=frmRoot.ImportFile.value;
          frmRoot.action = "/CibaVisionWms/FileUploadServlet?submit=Import" + "&ImportFile=" + file ;
          frmRoot.submit();
          return true;
          }else
          { return false;}
  }

}
</script>
<title>Import Items</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
FileUploadUtil fileUtil = new FileUploadUtil();
ArrayList QryList  = new ArrayList();
String fieldDesc="";
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;

String ServerName =  strUtils.fString(request.getRemoteHost()).trim();
System.out.println("RemoteHost Name :"+ServerName);

String action         = strUtils.fString(request.getParameter("action")).trim();
MLogger.log(1, "_______________________ " + this.getClass() + "");

String StrFileName    = strUtils.fString(request.getParameter("ImportFile"));
System.out.println("Import File :" + StrFileName);

if(action.equalsIgnoreCase("resulterror1")){ 
	
    fieldDesc=(String)request.getSession().getAttribute("RESULTOUTERR1");
    
  }

else if(action.equalsIgnoreCase("ImportItem") && (StrFileName !="")){
			
	QryList=fileUtil.ItemUploadSummary(StrFileName);
	if(QryList.size()>0){
		fieldDesc="File Upload Failed Reason Being The Error Message";
	}else{
		fieldDesc="File Upload Successful";
	}
	
}	

%>

<%@ include file="body.jsp"%>
   <form name="form1" method="post" enctype="multipart/form-data" action="/CibaVisionWms/FileUploadServlet">
 <input type="hidden" value="" name="download">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">IMPORT ITEMS</FONT>&nbsp;
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
		   <TH ALIGN="RIGHT" >&nbsp;</TH>
                      <TD>&nbsp;</TD>
                      <TH ALIGN="RIGHT" >&nbsp;Import Item File:</TH>
                       <TD width="30%">&nbsp;&nbsp;&nbsp;
            <INPUT name="ImportFile" type = "file"  size="25"  MAXLENGTH=20>
          </TD>
                                  
           <TD width="27%">
               <input type="Button" name="Submit" value="Import" style="height:20px;width:70px" onclick="javascript:return onGo();"/>
           </TD>
		  </TR>
     
  </TABLE>
   
   <P>  </P>  
     
    <CENTER><TR>&nbsp;  <input type="button" value="Back" style="height:20px;width:70px" onClick="window.location.href='indexPage.jsp'"></TR></CENTER>
   
  </TABLE>
  
  <br>
  <br>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
    <%
    if(QryList.size()>0){
    %>
           <TH align="center"><font color="#ffffff" >ITEM</TH>
           <TH align="left"><font color="#ffffff" align="left"><b>ITEMDESC</TH>
           <TH align="left"><font color="#ffffff" align="left"><b>UOM</TH>
           <TH align="left"><font color="#ffffff" ><b>PRD_CLS_ID</TH>
         <TH align="left"><font color="#ffffff" align="left"><b>REFNO</TH>
         <TH align="left"><font color="#ffffff" align="left"><b>GTIN2</TH>
          <TH align="left"><font color="#ffffff" align="left"><b>ERROR MSG</TH>
      </tr>
      <%	}  %>
       <%
          for (int iCnt =0; iCnt<QryList.size(); iCnt++){
        
          Map lineArr = (Map) QryList.get(iCnt);
         
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
          <TR bgcolor = "<%=bgcolor%>">
         
            <TD align= "left"><%=(String)lineArr.get("ITEM")%></TD>
            <TD align= "left"><%=(String)lineArr.get("ITEMDESC")%></TD>
            <TD align= "left"><%=(String)lineArr.get("UOM")%></TD>
            <TD align= "left"><%=(String)lineArr.get("PRD_CLS_ID")%></TD>
            <TD align= "left"><%=(String)lineArr.get("REFNO")%></TD>
            <TD align= "right"><%=(String)lineArr.get("GTIN2")%></TD>
            <TD align= "right"><%=(String)lineArr.get("ErroMsg")%></TD>
           
          
           </TR>
       <%}%>

    </TABLE>
            
  </FORM>

   
<%@ include file="footer.jsp"%>