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
   var BATCH    = document.form1.BATCH.value;
   var ITEM    = document.form1.ITEM.value;
   var LOC   = document.form1.LOC.value;
   var TRAVELNO   = document.form1.TRAVELNO.value;
   var TRNO   = document.form1.TRNO.value;
    
   var GO   = document.form1.GO.value="Go";
   if(BATCH != null    && BATCH != "") { flag = true;}
   if(ITEM != null    && ITEM != "") { flag = true;}
   if(LOC != null   && LOC != "") { flag = true;}
   if(TRAVELNO != null   && TRAVELNO != ""){ flag = true;}
    if(TRNO != null   && TRNO != ""){ flag = true;}
   
   
 
   if(flag == "false"){ alert("Please define any one search criteria"); return false;}

document.form1.action = "StockAdjustment.jsp";
document.form1.submit();
}
function onSubmit(mtid){

var mtid=mtid;
document.form1.TRAVELNO.value=mtid;
document.form1.action = "AdjustmentDetail.jsp";
document.form1.submit();
}
</script>
<title>Stock Adjustment</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();
ArrayList invQryList  = new ArrayList();
String PLANT        = (String)session.getAttribute("PLANT");

if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("action")).trim();
String WHID ="",  ITEM = "", BATCH ="", BINNO ="", QTY ="",QTYALLOC ="" , PKTYPE ="",LOC="",MTID="",TRNO="";
String html = "",GO="";

GO    = strUtils.fString(request.getParameter("GO"));
BATCH    = strUtils.fString(request.getParameter("BATCH"));
ITEM    = strUtils.fString(request.getParameter("ITEM"));
LOC   = strUtils.fString(request.getParameter("LOC"));
MTID   = strUtils.fString(request.getParameter("TRAVELNO"));
TRNO   = strUtils.fString(request.getParameter("TRNO"));

if(GO.equalsIgnoreCase("Go")){
 try{
      invQryList = invUtil.getInvList(PLANT, ITEM,BATCH, LOC,MTID,TRNO);
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}


%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="">
  <INPUT type="hidden" name="PLANT" value="<%=PLANT%>">
   <INPUT type="hidden" name="GO" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">STOCK ADJUSTMENT</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

          <TR>
		   <TH ALIGN="RIGHT" >Batch&nbsp;&nbsp;</TH>
                      <TD><INPUT name="BATCH" type = "TEXT" value="<%=BATCH%>" size="20"  MAXLENGTH=20></TD>
                      <TH ALIGN="RIGHT" >SKU&nbsp;&nbsp;</TH>
                      <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=20></TD>
		
                     
		</TR>

		<TR>
                       <TH ALIGN="RIGHT" >Location &nbsp;&nbsp;</TH>
                      <TD><INPUT name="LOC" type = "TEXT" value="<%=LOC%>" size="20"  MAXLENGTH=20></TD>
                      <TH ALIGN="RIGHT" >MTID&nbsp;&nbsp;</TH>
                      <TD><INPUT name="TRAVELNO" type = "TEXT" value="<%=MTID%>" size="20"  MAXLENGTH=20></TD>
					             <TD ALIGN="left" >
                    <!--   &nbsp;&nbsp;<input type="submit" value="Go" name="action" onClick="javascript:return onGo();">&nbsp; -->
                     &nbsp;&nbsp;<input type="button" value="Go"  onClick="javascript:return onGo();">&nbsp; 
                      </TD>
                     </TR>
                     
         <TR>
                       <TH ALIGN="RIGHT" >Travel No &nbsp;&nbsp;</TH>
                      <TD><INPUT name="TRNO" type = "TEXT" value="<%=TRNO%>" size="20"  MAXLENGTH=20></TD>            
                     
          </TR>            
  </TABLE>
  <br>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <th><font color="#ffffff" align="center">SNO</th>
         <td><font color="#ffffff" align="left"><center><b>MTID</center></td>
         <td><font color="#ffffff" align="left"><center><b>Item</center></td>
		     <td><font color="#ffffff" align="left"><center><b>Batch</center></td>
         <td><font color="#ffffff" align="left"><center><b>Qty</center></td>
         <td><font color="#ffffff" align="left"><center><b>Loc</center></td>
		
	 </tr>
       <%
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
         // Map lineArr = new ArrayList();
		   Map lineArr = (Map) invQryList.get(iCnt);
          //lineArr = (ArrayList)invQryList.get(iCnt);
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
          <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=iIndex%></TD>
             <TD width="13%"> <a href="javaScript:onSubmit('<%=(String)lineArr.get("MTID")%>');"><%=(String)lineArr.get("MTID")%></a></font></TD>
             <TD><%=(String)lineArr.get("ITEM")%></TD>
			       <TD><%=(String)lineArr.get("LOT")%></TD>
			       <TD><%=(String)lineArr.get("QTY")%></TD>
			       <TD><%=(String)lineArr.get("LOC")%></TD>
            
          
           </TR>
       <%}%>

    </TABLE>
  </FORM>
<%@ include file="footer.jsp"%>