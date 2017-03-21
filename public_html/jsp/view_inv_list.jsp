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
  
  
   if(BATCH != null    && BATCH != "") { flag = true;}
   if(ITEM != null    && ITEM != "") { flag = true;}
   if(LOC != null   && LOC != "") { flag = true;}
 
   if(flag == "false"){ alert("Please define any one search criteria"); return false;}

document.form1.VIEW_INV.value="Go";
document.form1.action = "view_inv_list.jsp";
document.form1.submit();
}

function onGenerate(){

	  var flag    = "false";
   var BATCH    = document.form1.BATCH.value;
   var ITEM    = document.form1.ITEM.value;
   var LOC   = document.form1.LOC.value;
 
  
   if(BATCH != null    && BATCH != "") { flag = true;}
   if(ITEM != null    && ITEM != "") { flag = true;}
   if(LOC != null   && LOC != "") { flag = true;}
 
   if(flag == "false"){ alert("Please define any one search criteria"); return false;}

//  alert("onGo1");
document.form1.VIEW_INV.value = "Go";
document.form1.action='/CibaVisionWms/ExcelGeneratorServlet?action=GenerateInv';   
document.form1.submit();


}
</script>
<title>Inventory List</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();
ArrayList invQryList  = new ArrayList();
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("action")).trim();
String   ITEM = "", BATCH ="",  QTY ="", LOC="",HiddenView=  "",view_Inv="";
String html = "";
BATCH    = strUtils.fString(request.getParameter("BATCH"));
ITEM    = strUtils.fString(request.getParameter("ITEM"));
LOC   = strUtils.fString(request.getParameter("LOC"));
HiddenView    = strUtils.fString(request.getParameter("hiddenView"));
view_Inv      = strUtils.fString(request.getParameter("VIEW_INV"));


if(view_Inv.equalsIgnoreCase("Go")){
 try{
      invQryList = invUtil.getInvListReport(PLANT, ITEM,BATCH, LOC);
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}

if(HiddenView.equalsIgnoreCase("GenerateExcel")){



 try{
    response.sendRedirect("inv_list_excel.jsp?HiddenView=GenerateExcel&BATCH="+BATCH+"&ITEM="+ITEM+"&LOC="+LOC+"");
 }catch(Exception e) {System.out.println("Exception :inv_list_excel"+e.toString()); }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="view_inv_list.jsp" >
  <INPUT type="hidden" name="PLANT" value="<%=PLANT%>">
  <INPUT type="hidden" name="VIEW_INV" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">INVENTORY LIST</font></TH>
    </TR>
  </TABLE>
  <br>
   <INPUT type="hidden" name="hiddenView" value="">
  <TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

          <TR>
		   <TH ALIGN="RIGHT" >Lot&nbsp;No.&nbsp;&nbsp;</TH>
                      <TD><INPUT name="BATCH" type = "TEXT" value="<%=BATCH%>" size="20"  MAXLENGTH=20></TD>
                      <TH ALIGN="RIGHT" >SKU&nbsp;&nbsp;</TH>
                      <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=20></TD>
		
                     
		</TR>

		<TR>
                      <TH ALIGN="RIGHT" >Location &nbsp;&nbsp;</TH>
                      <TD><INPUT name="LOC" type = "TEXT" value="<%=LOC%>" size="20"  MAXLENGTH=20></TD>
                     <TD></TD>
					             <TD ALIGN="left" >
                      <!-- &nbsp;&nbsp;<input type="submit" value="Go" name="action" onClick="javascript:return onGo();">&nbsp; -->
                      &nbsp;&nbsp; <input type="button" value="Go" onClick="javascript:return onGo();"/></TD>
                      </TD>
                     </TR>
       
  </TABLE>
  <br>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <th><font color="#ffffff" align="center">SNO</th>
         <td><font color="#ffffff" align="left"><center><b>LOC</center></td>
		     <td><font color="#ffffff" align="left"><center><b>LOT</center></td>
         <td><font color="#ffffff" align="left"><center><b>SKU</center></td>
         <td><font color="#ffffff" align="left"><center><b>SKU_DESC</center></td>
         <td><font color="#ffffff" align="left"><center><b>QTY</center></td>
         <td><font color="#ffffff" align="left"><center><b>UOM</center></td>
		
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
            <TD><%=(String)lineArr.get("LOC")%></TD>
			 <TD><%=(String)lineArr.get("LOT")%></TD>
			 <TD><%=(String)lineArr.get("SKU")%></TD>
       <TD><%=(String)lineArr.get("SKU_DESC")%></TD>
			 <TD><%=(String)lineArr.get("QTY")%></TD> 
       <TD><%=(String)lineArr.get("UOM")%></TD>
       </TR>
       <%}%>
       
    </TABLE>
  <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" value="Cancel" onClick="window.location.href='indexPage.jsp'"/>
    <!--<input type="button" value="Generate Excel" onClick="javascript:return onGenerate();"/></TD> -->
     <input type="button" value="Generate Excel" onClick="javascript:return onGenerate();">
  
    
</P>
  </FORM>
<%@ include file="footer.jsp"%>