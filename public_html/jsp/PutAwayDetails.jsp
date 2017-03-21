<!--<%@ page import="com.murho.gates.DbBean"%> -->
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
  <head>
    <script language="javascript">
function onApprove(){
var act     = document.form1.HiddenApprove.value = "Approve";
var MTID    = document.form1.MTID.value;
var ITEM    = document.form1.ITEM.value;
var SKU    = document.form1.SKU.value;
var QTY    = document.form1.QTY.value;
var LOT    = document.form1.LOT.value;
document.form1.action = "PutAwayDetails.jsp";

document.form1.submit();
}

function onBack(){
var act     = document.form1.HiddenView.value = "Go";
var MTID    = document.form1.MTID.value;
var ITEM    = document.form1.ITEM.value;
var SKU    = document.form1.SKU.value;
var QTY    = document.form1.QTY.value;
var LOT    = document.form1.LOT.value;
document.form1.action = "PutAwayApprove.jsp";

document.form1.submit();
}

function onReject(){
var act     = document.form1.HiddenReject.value = "Reject";
var MTID    = document.form1.MTID.value;
var ITEM    = document.form1.ITEM.value;
document.form1.action = "PutAwayDetails.jsp";
document.form1.submit();
}

function onReasign(){
var MTID    = document.form1.MTID.value;
var ITEM    = document.form1.ITEM.value;
var SHOW    = document.form1.SHOW.value="Y";
document.form1.action = "PutAwayDetails.jsp";
document.form1.submit();
}
</script>
    <title>Putaway Detail</title>
  </head>
  <link rel="stylesheet" href="css/style.css"/>
  <% 
StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();
PoDetDAO doDetDAO       = new PoDetDAO();
MovHisDAO movHisDAO=new MovHisDAO();
ArrayList invQryList  = new ArrayList();
DateUtils dateUtil           = new DateUtils();

String WHID ="",TRAVEL_NO="",  ITEM = "", BATCH ="", BINNO ="", QTY ="";
String QTYALLOC ="" , PKTYPE ="",TRID="",MTID="",USERACTION="",SKU="",LOT="",LOC="",tranDate="";
String NEWLOC ="";
String SHOW="";
String html = "",HiddenApprove="",HiddenReject="";
boolean isupdated=false;
boolean getdetails = true;
boolean newLoc = false;
double quantity = 0.0;
String fieldDesc="";
MTID    = strUtils.fString(request.getParameter("MTID"));
TRID    = strUtils.fString(request.getParameter("ITEM"));

SKU    = strUtils.fString(request.getParameter("SKU"));
LOT    = strUtils.fString(request.getParameter("LOT"));
LOC    = strUtils.fString(request.getParameter("LOC"));
QTY    = strUtils.fString(request.getParameter("QTY"));
NEWLOC    = strUtils.fString(request.getParameter("NEWLOC"));
SHOW    = strUtils.fString(request.getParameter("SHOW"));

String LOGIN_USER   = (String)session.getAttribute("LOGIN_USER");

tranDate=dateUtil.getDateinyyyy_mm_dd(dateUtil.getDate()); 

HiddenApprove=strUtils.fString(request.getParameter("HiddenApprove"));
HiddenReject=strUtils.fString(request.getParameter("HiddenReject"));


 try{
 if(getdetails){
  invQryList = recvHisDao.getPutAwayLineDetail(CibaConstants.cibacompanyName,TRID,MTID);
  getdetails=false;
  for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
           Map lineArr = (Map) invQryList.get(iCnt);
           
          
           TRAVEL_NO=(String)lineArr.get("TRAVEL_NO");
           MTID=(String)lineArr.get("MTID");
           SKU=(String)lineArr.get("PART");
           LOT=(String)lineArr.get("LOT");
           LOC=(String)lineArr.get("LOCID");
           QTY=(String)lineArr.get("QTY");
 }
 }
     
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
 
 if(HiddenApprove.equalsIgnoreCase("Approve"))
 {
   System.out.println(":::::: inisde action approve :::::::::::::");
 
   isupdated=doDetDAO.updatePutAwayLineDet(TRID,MTID,SKU,LOC,LOT,TRAVEL_NO);
 
  if(isupdated){
  
    isupdated=movHisDAO.RecordRejectDetail(CibaConstants.cibacompanyName,TRID,MTID,SKU,LOT,QTY,"PUTAWAY-APPROVE",LOGIN_USER,tranDate);
    
  }
  if(isupdated){ 
  fieldDesc = "<tr><td><B><h3>Details Updated Successfully<h3></td></tr>"; 
  }
  }
  
   if(HiddenReject.equalsIgnoreCase("Reject"))
 {
   System.out.println(":::::: inisde action approve :::::::::::::");
  
 /*   isupdated=doDetDAO.RejectPutAwayLineDetial(TRID,MTID);
   if(isupdated)
   {
    isupdated=movHisDAO.RecordRejectDetail(CibaConstants.cibacompanyName,TRID,MTID,SKU,LOT,QTY,"PUTAWAY-REJECT","ADMIN1",tranDate);
   }*/
  
  if(true){
    fieldDesc = "<tr><td><B><h3>Re-assigned successfully,Can do location transfer <h3></td></tr>";
   
  }
   
   
  }
 %>
  <%@ include file="body.jsp"%>
  <FORM name="form1" method="post" >
  <INPUT type="hidden" name="HiddenApprove" value="">
  <INPUT type="hidden" name="HiddenReject" value="">
  <INPUT type="hidden" name="HiddenView" value="">
  <INPUT type="hidden" name="MTID"  value="<%=MTID%>">
  <INPUT type="hidden" name="ITEM"  value="<%=TRID%>">
  <INPUT type="hidden" name="SKU"  value="<%=SKU%>">
  <INPUT type="hidden" name="LOT"  value="<%=LOT%>">
  <INPUT type="hidden" name="LOC"  value="<%=LOC%>">
  <INPUT type="hidden" name="QTY"  value="<%=QTY%>">
  <INPUT type="hidden" name="SHOW"  value="">
    <br/>
    <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11">
          <FONT color="#ffffff">PUTAWAY LINE DETAILS</FONT>&nbsp;
        </TH>
      </TR>
    </TABLE>
    <P>&nbsp;</P>
    <table cellspacing="1" cellpadding="3" border="1" width="61%" align="center" height="173"    <table cellspacing="1" cellpadding="3" border="1" width="61%" align="center" height="173" bgcolor="#dddddd">
    <%
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
           Map lineArr = (Map) invQryList.get(iCnt);
           System.out.println("lineArr size"+lineArr.size());
      %>
    
    <!--  <tr>
        <td width="47%">
          <P>
            <STRONG><FONT color="#333399">TRAVEL_NO</FONT></STRONG>
          </P>
        </td>
        <td width="53%"><%=TRAVEL_NO%></td>
      </tr>-->
      
      <tr>
        <td width="47%">
          <STRONG><FONT color="#333399">MTID</FONT></STRONG>
        </td>
        <td width="53%"><%=MTID%></td>
      </tr> 
      <tr>
        <td width="47%">
          <STRONG><FONT color="#333399">PART</FONT></STRONG>
        </td>
        <td width="53%"><%=SKU%></td>
      </tr>
      <tr>
        <td width="47%">
          <STRONG><FONT color="#333399">LOT</FONT></STRONG>
        </td>
        <td width="53%"><%=LOT%></td>
      </tr>
      <tr>
        <td width="47%" height="28">
          <STRONG><FONT color="#000099">LOCID</FONT></STRONG>
        </td>
        <td width="53%" height="28"><%=LOC%></td>
      </tr>
      <tr>
         
         
         <%if(NEWLOC.length()>0){%>
         
          <td width="47%" height="28">
          <STRONG><FONT color="#000099">NEW LOC</FONT></STRONG>
        </td>
        <td width="53%" height="28"><%=NEWLOC%></td>
        </tr>
         
         <%}%>
       
        <tr>
         <tr>
         
         
         <%if(SHOW.equalsIgnoreCase("Y")){%>
         
          <td width="47%" height="28">
          <STRONG><FONT color="#000099">NEW LOC</FONT></STRONG>
        </td>
        <td width="53%" height="28"><INPUT name="NEWLOC" type = "TEXT" value="<%=NEWLOC%>" size="20"  MAXLENGTH=20> </td>
        </tr>
         
         <%}%>
       
        <tr>
        <td width="47%" height="28">
          <STRONG><FONT color="#000099">QTY</FONT></STRONG>
        </td>
        <td width="53%" height="28"><%=QTY%></td>
      </tr>
       <%}%>
    </table>
    <P>&nbsp;</P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" value="Approve" onClick="javascript: onApprove();"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <%if(SHOW.equalsIgnoreCase("N")){%>
    <input type="button" value="Re-asaign" onClick="javascript: onReasign();"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="button" value="Back" onClick="window.location.href='javascript:history.back()'"/>
    <%}%>
   <%if(SHOW.equalsIgnoreCase("Y")){%>
     <input type="button" value="Print" onClick="javascript: onReject();"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       <input type="button" value="Back" onClick="window.location.href='javascript:history.back()'"/>
    <%}%>
     <%if(NEWLOC.length()>0){%>
           <input type="button" value="Back" onClick="javascript: onBack();"/>
    <%}%>
     
     
  
    </P>
    <P>&nbsp;</P>
    <TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding="2" align="center"/>
  </FORM>
  
  <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
    
  <%@ include file="footer.jsp"%>
</html>
