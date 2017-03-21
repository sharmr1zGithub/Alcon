<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<head>
<script language="javascript">
function onGo(){

   var flag    = "false";
   var WHID    = document.form1.WHID.value;
   var ITEM    = document.form1.ITEM.value;
   var BATCH   = document.form1.BATCH.value;
   var BINNO   = document.form1.BINNO.value;
   var OPSEQ   = document.form1.OPSEQ.value;
   var ISSUEDBY   = document.form1.ISSUEDBY.value;
  
   if(WHID != null    && WHID != "") { flag = true;}
   if(ITEM != null    && ITEM != "") { flag = true;}
   if(BATCH != null   && BATCH != "") { flag = true;}
   if(BINNO != null   && BINNO != ""){ flag = true;}
   if(OPSEQ != null   && OPSEQ != ""){ flag = true;}
   if(ISSUEDBY != null   && ISSUEDBY != ""){ flag = true;}
 
   if(flag == "false"){ alert("Please define any one search criteria"); return false;}


document.form1.submit();
}
</script>
<title>Wip List</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
ReportUtil repUtil       = new ReportUtil();
ArrayList wipQryList  = new ArrayList();
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("action")).trim();
String WHID ="",  ITEM = "", BATCH ="", BINNO ="", QTY ="",OPSEQ ="" , ISSUEDBY ="";
String html = "";
WHID    = strUtils.fString(request.getParameter("WHID"));
ITEM    = strUtils.fString(request.getParameter("ITEM"));
BATCH   = strUtils.fString(request.getParameter("BATCH"));
BINNO   = strUtils.fString(request.getParameter("BINNO"));
OPSEQ   = strUtils.fString(request.getParameter("OPSEQ"));
ISSUEDBY   = strUtils.fString(request.getParameter("ISSUEDBY"));


if(action.equalsIgnoreCase("Go")){
 try{
      wipQryList = repUtil.getWipList(PLANT, WHID,BINNO, ITEM, BATCH,OPSEQ,ISSUEDBY);
 }catch(Exception e) {System.out.println("Exception :getWipList"+e.toString()); }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <INPUT type="hidden" name="PLANT" value="<%=PLANT%>">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">WORK IN PROGRESS LIST</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

          <TR>
		   <TH ALIGN="RIGHT" >Batch&nbsp;&nbsp;</TH>
                      <TD><INPUT name="BATCH" type = "TEXT" value="<%=BATCH%>" size="20"  MAXLENGTH=20></TD>
                      <TH ALIGN="RIGHT" >Item&nbsp;&nbsp;</TH>
                      <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=20></TD>
		
                     
		</TR>
		<TR>
		   <TH ALIGN="RIGHT" >Warehouse &nbsp;&nbsp;</TH>
                      <TD><INPUT name="WHID" type = "TEXT" value="<%=WHID%>" size="20"  MAXLENGTH=20></TD>
                      <TH ALIGN="RIGHT" >Bin No&nbsp;&nbsp;</TH>
                      <TD><INPUT name="BINNO" type = "TEXT" value="<%=BINNO%>" size="20"  MAXLENGTH=20></TD>
                     
		</TR>

		<TR>
                       <TH ALIGN="RIGHT" >OpSeq &nbsp;&nbsp;</TH>
                      <TD><INPUT name="OPSEQ" type = "TEXT" value="<%=OPSEQ%>" size="20"  MAXLENGTH=20></TD>
                      <TH ALIGN="RIGHT" >Issued By &nbsp;&nbsp;</TH>
                      <TD><INPUT name="ISSUEDBY" type = "TEXT" value="<%=ISSUEDBY%>" size="20"  MAXLENGTH=20></TD>
					 <TD ALIGN="left" >
                             &nbsp;&nbsp;<input type="submit" value="Go" name="action" onClick="javascript:return onGo();">&nbsp;
                      </TD>
                     </TR>
  </TABLE>
  <br>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <th><font color="#ffffff" align="center">SNO</th>
         <td><font color="#ffffff" align="left"><center><b>WH</center></td>
         <td><font color="#ffffff" align="left"><center><b>Bin</center></td>
         <td><font color="#ffffff" align="left"><center><b>Item</center></td>
         <td><font color="#ffffff" align="left"><center><b>Batch</center></td>
		 <td><font color="#ffffff" align="left"><center><b>OpSeq</center></td>
	     <td><font color="#ffffff" align="left"><center><b>Crby</center></td>
         <td><font color="#ffffff" align="left"><center><b>Qty</center></td>
         <td><font color="#ffffff" align="left"><center><b>UOM</center></td>
		
	 </tr>
       <%
          for (int iCnt =0; iCnt<wipQryList.size(); iCnt++){
        
		   Map lineArr = (Map) wipQryList.get(iCnt);
        
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
          <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=iIndex%></TD>
            <TD><%=(String)lineArr.get("WHID")%></TD>
            <TD><%=(String)lineArr.get("BINNO")%></TD>
			<TD><%=(String)lineArr.get("ITEM")%></TD>
			<TD><%=(String)lineArr.get("BATCH")%></TD>
			<TD><%=(String)lineArr.get("OPSEQ")%></TD>
			<TD><%=(String)lineArr.get("CRBY")%></TD>
            <TD align= "right"><%=(String)lineArr.get("QTY")%></TD>
            <TD align= "right"><%=(String)lineArr.get("UOM")%></TD>
          
           </TR>
       <%}%>

    </TABLE>
  </FORM>
<%@ include file="footer.jsp"%>