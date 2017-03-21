<!--<%@ page import="com.murho.gates.DbBean"%> -->
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Shipemnt</title>
<link rel="stylesheet" href="css/style.css">
<script language="javascript">
<!--
function onApprove(){

var flag    = "false";
var act     = document.form1.HiddenApprove.value = "Save";
var MTID    = document.form1.MTID.value;
var SKU    = document.form1.SKU.value;
//var QTY    = document.form1.QTY.value;
var OBOUND    = document.form1.OBOUND.value;
var ITEM    = document.form1.ITEM.value;
var HiddenView    = document.form1.HiddenView.value="Go";
var Export    = document.form1.Export.value="YES";

document.form1.action = "TraySummary.jsp";

 if(OBOUND == "" || OBOUND == null) {alert("OBOUND should not be Empty"); document.form1.OBOUND.focus(); return false; }
 if(MTID == "" || MTID == null) {alert("MTID should not be Empty"); document.form1.MTID.focus(); return false; }
 if(SKU == "" || SKU == null) {alert("SKU should not be Empty"); document.form1.SKU.focus(); return false; }
// if(QTY == "" || QTY == null) {alert("QTY should not be Empty"); document.form1.QTY.focus(); return false; }

alert("Packing list generated successfully!");
 
document.form1.submit();

}


-->
</script>
<!--script language ="javascript" src="js/vendor.js"></script-->
<%@ include file="body.jsp"%>
<%

StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();

MovHisDAO movHisDAO=new MovHisDAO();
ArrayList invQryList  = new ArrayList();
DateUtils dateUtil           = new DateUtils();



    String PLANT = (String)session.getAttribute("PLANT");
    if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
 
 String MTID="",OBOUND="",QTY="",SKU="",fieldDesc="",HiddenApprove="",OutBoundTrNo=""; 
 
 HiddenApprove=strUtils.fString(request.getParameter("HiddenApprove"));
 OutBoundTrNo=strUtils.fString(request.getParameter("ITEM"));
 
  
  if(HiddenApprove.equalsIgnoreCase("Save"))
 {
   System.out.println(":::::: inisde action approve :::::::::::::");
  fieldDesc = "<tr><td><B><h3>Details Saved Successfully<h3></td></tr>"; 
   
  }
%>

<FORM name="form1" method="post" action="">
<INPUT type="hidden" name="HiddenApprove" value="">
<INPUT type="hidden" name="Export" value="">
<INPUT type="hidden" name="ITEM" value="<%=OutBoundTrNo%>">
<INPUT type="hidden" name="HiddenView" value="">

  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">GLOBAL SHIPING</font></TH>
    </TR>
  </table>
  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
      <td width="100%">&nbsp;
        <center>
          <table border="0" width="90%">
            <tr>
              <td width="100%"> <CENTER>
                  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" > OutBound No : </TH>
                      <TD><INPUT name="OBOUND" type = "TEXT" value="<%=OutBoundTrNo%>" size="50"  MAXLENGTH=20 >
                      </TD>
                    </TR>
                   
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" > Height : </TH>
                      <TD><INPUT name="MTID" type = "TEXT" value="<%=MTID%>" size="50"  MAXLENGTH=20 >
                      </TD>
                    </TR>
 
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" > Weight : </TH>
                      <TD><INPUT name="SKU" type = "TEXT" value="<%=SKU%>" size="50"  MAXLENGTH=20  >
                      </TD>
                    </TR>
                    
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" > Remark#1: </TH>
                      <TD><INPUT name="remark1" type = "TEXT" value="<%=QTY%>" size="50"  MAXLENGTH=20  <%=""%> >
                      </TD>
                    </TR>
                      <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" > Remark#2: </TH>
                      <TD><INPUT name="remark2" type = "TEXT" value="<%=QTY%>" size="50"  MAXLENGTH=20  <%=""%> >
                      </TD>
                    </TR>
                    
                      <TH WIDTH="35%" ALIGN="RIGHT" >&nbsp;</TH>
                      <TD></TD>
                    </TR>
                  </TABLE>
                </CENTER></td>
            </tr>

          </table>
          <br>
        </center>
        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <div align="center">
          <center>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
                 
                   <input type="button" value="Back" onClick="window.location.href='javascript:history.back()'">&nbsp;
                   <input type="button" value="Generate Packing List" onClick="javascript:return onApprove();">&nbsp;
                </td>
              </tr>
            </table>
          </center>
        </div>
   </td>
    </tr>
  </table>
</FORM>

 <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
    
<%@ include file="footer.jsp"%>


