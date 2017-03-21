<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.seiko.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ include file="header.jsp"%>

<html>
<head>

<title>PO Lot Details</title>
</head>
<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
ReceiptEntryUtil recvUtil       = new ReceiptEntryUtil();

String COMMENT1="";
String LOC="";
String PONO="";
String PSLIPNUM="";
String PACKLINE="";
String ITEM="";
String WHID="";
String BIN="";
String POLNNO="";
String QTYOR="";
String LNSTAT="";
String PURPOINT="";
String VENDNO="";
boolean Reversed = false;
boolean deleted = false;

 PONO              = strUtils.fString(request.getParameter("PONO"));
 PSLIPNUM          = strUtils.fString(request.getParameter("PSLIPNUM"));
 POLNNO            = strUtils.fString(request.getParameter("POLNNO"));
 PACKLINE          = strUtils.fString(request.getParameter("PACKLINE"));
 ITEM              = strUtils.fString(request.getParameter("ITEM"));
 WHID              = strUtils.fString(request.getParameter("WHID"));
 BIN               = strUtils.fString(request.getParameter("BIN")); 
 COMMENT1          = strUtils.fString(request.getParameter("COMMENT1")); 
 QTYOR             = strUtils.fString(request.getParameter("QTYOR")); 
 LNSTAT            = strUtils.fString(request.getParameter("LNSTAT"));
 PURPOINT          = strUtils.fString(request.getParameter("PURPOINT"));
 VENDNO            = strUtils.fString(request.getParameter("VENDNO"));
 String[] lots     = request.getParameterValues("lot");

//MLogger.log(0,"#####################  PONO     :"+PONO); 
//MLogger.log(0,"####################  PONO     :"+PONO);
//MLogger.log(0,"##################### PSLIPNUM :"+PSLIPNUM);
//MLogger.log(0,"##################### POLNNO   :"+POLNNO);
//MLogger.log(0,"##################### PCKLINE  :" +PACKLINE);
//MLogger.log(0,"##################### ITEM     :"+ITEM);
//MLogger.log(0,"##################### COMMENT1 :"+COMMENT1);
//MLogger.log(0,"##################### WHID     :"+WHID);
//MLogger.log(0,"##################### BIN      :"+BIN);
//MLogger.log(0,"##################### QTYOR    :"+QTYOR);
//MLogger.log(0,"##################### LNSTAT   :"+LNSTAT);
//MLogger.log(0,"##################### PURPOINT  :"+PURPOINT);
//MLogger.log(0,"##################### VENDNO    :"+VENDNO);



UserTransaction ut    = null;
String action= "";
String res    = "";

action = strUtils.fString(request.getParameter("action"));
String PLANT = (String)session.getAttribute("PLANT");
String sUserId = (String) session.getAttribute("LOGIN_USER");
ArrayList arrList =  new ArrayList();
arrList = recvUtil.getDetailsForPoLine(PLANT,PONO,POLNNO,PSLIPNUM,PACKLINE);

Map map = null;
Map ma  = null;
int length = 0;
if (arrList.size() > 0) {
for (int iCnt =0; iCnt<arrList.size(); iCnt++){
     ma = (Map) arrList.get(iCnt);

 }
 } 

if(action.equalsIgnoreCase("Reverse")){
	 Map recvReversal_HM  = new HashMap();
	 
	 boolean result = false;
   
	 	    recvReversal_HM.put(SConstant.COMPANY, PLANT);
		    recvReversal_HM.put(SConstant.PO_NUM, PONO);
		    recvReversal_HM.put(SConstant.PO_LINE_NUM, POLNNO);
		    recvReversal_HM.put(SConstant.MATERIAL_CODE, ITEM);
			recvReversal_HM.put(SConstant.PACK_SLIP_NUM, PSLIPNUM);
		    recvReversal_HM.put(SConstant.FR_WHSE, WHID);
			recvReversal_HM.put(SConstant.FR_BIN, BIN);
			recvReversal_HM.put(SConstant.LOGIN_USER, sUserId);
		    recvReversal_HM.put(SConstant.PACK_LN_NO,PACKLINE);
			recvReversal_HM.put(SConstant.PURCHASE_POINT,PURPOINT);
            recvReversal_HM.put(SConstant.VENDER_NUM,VENDNO);
 
			
     
     try{
     MLogger.log(0," @@@@@@@@@@@@@@@@  process_ReceiveingReversal BEFORE@@@@@@@@@@@@@@@@@@@");
     result= recvUtil.process_ReceiveingReversal(recvReversal_HM);
     MLogger.log(0," @@@@@@@@@@@@@@@@  process_ReceiveingReversal AFTER @@@@@@@@@@@@@@@@@@@");
     }catch(Exception e){
     result = false;}
 
		 
		   if (result == false){
			 Reversed = true;
			 res = "<font class=\"mainred\">Failed to R e v e r s e Receving for Order :  '"+PONO+"'  </font>";
    }else if(result == true){
		   Reversed = true;
       res = "<font class=\"maingreen\">Receving Reversal Completed Sucessfully</font>";
   }
   
   
	
   
   action ="";
}

if(action.equalsIgnoreCase("Delete")){

    Map recvReversal_HM  = new HashMap();
     boolean result = false;

      recvReversal_HM.put(SConstant.COMPANY, PLANT);
		  recvReversal_HM.put(SConstant.PO_NUM, PONO);
		  recvReversal_HM.put(SConstant.PO_LINE_NUM, POLNNO);
		  recvReversal_HM.put(SConstant.MATERIAL_CODE, ITEM);
			recvReversal_HM.put(SConstant.PACK_SLIP_NUM, PSLIPNUM);
		  recvReversal_HM.put(SConstant.FR_WHSE, WHID);
			recvReversal_HM.put(SConstant.FR_BIN, BIN);
			recvReversal_HM.put(SConstant.LOGIN_USER, sUserId);
			recvReversal_HM.put(SConstant.PACK_LN_NO,PACKLINE);
			recvReversal_HM.put(SConstant.PURCHASE_POINT,PURPOINT);
      recvReversal_HM.put(SConstant.VENDER_NUM,VENDNO);
  
				 
      
      try{ 
          result= recvUtil.DeletePackline(recvReversal_HM);
	   
     }catch(Exception e){
     MLogger.log(0,"::::::ShipmentReversal Exception (jsp)      ::::       ::::"+e);
     result = false;}
     
     if (result == false){
			   deleted = true;
			   res = "<font class=\"mainred\">Failed to delete the packline  :  '"+PSLIPNUM+"'  </font>";
    }else if(result == true){
		   deleted = true;
       res = "<font class=\"maingreen\">Packline deleted  sucessfully</font>";
   }
   action="";
 }

%>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">

function onReverse(){
     var con = confirm ("Are you sure want to Reverse the Lot ?");
     if(con) {
     document.form1.action  = "view_po_lot_details.jsp?action=Reverse";
     document.form1.submit();
     }else {
         return false;
     }
}

function onDelete(){

    
     
     var con = confirm ("Are you sure,you want delete the packline ?");
     if(con) {
     document.form1.action  = "view_po_lot_details.jsp?action=Delete";
     document.form1.submit();
     }else {
         return false;
     }
}


-->
</script>
<%

ArrayList poList = new ArrayList();
poList = recvUtil.getLotDetailsList4PoLine(PLANT,PONO,POLNNO,PSLIPNUM,PACKLINE);
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action = ""> 
<input type="hidden" name="LNSTAT" value="<%=LNSTAT%>">
<input type="hidden" name="PURPOINT" value="<%=PURPOINT%>">
<input type="hidden" name="VENDNO" value="<%=VENDNO%>">
<input type="hidden" name="POLNNO" value="<%=POLNNO%>">
<input type="hidden" name="hdAction" value="<%=poList.size()%>">
<!--input type = "hidden" name = "SONO" value = "<!--%=SONO%>"-->
  <br>
  <TABLE border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066"><font color="white">LOT DETAILS</font></TH>
    </TR>
  </TABLE>
<br>

<TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
 <TR>
       <TH ALIGN="left" >PO NO : </TH>
       <TD><INPUT name="PONO" type = "TEXT" value="<%=PONO%>" size="20"  MAXLENGTH=40 class = "inactive" readonly></TH>
        <TH ALIGN="left">PACKING SLIPNO: </TH>
	<TD><INPUT name="PSLIPNUM" type = "TEXT" value="<%=PSLIPNUM%>" size="20"  MAXLENGTH=20 class = "inactive" readonly></TD>
    </TR>
    <TR>
       <TH ALIGN="left" >PACKLINE : </TH>
       <TD><INPUT name="PACKLINE" type = "TEXT" value="<%=PACKLINE%>" size="20"  MAXLENGTH=40 class = "inactive" readonly></TH>
        <TH ALIGN="left">PART CODE: </TH>
	<TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=20 class = "inactive" readonly></TD>
    </TR>
    <TR>
       <TH ALIGN="left" >WAREHOUSE : </TH>
       <TD><INPUT name="WHID" type = "TEXT" value="<%=WHID%>" size="20"  MAXLENGTH=40 class = "inactive" readonly ></TH>
        <TH ALIGN="left">DESCRIPTION : </TH>
        <TD><INPUT name="COMMENT1" type = "TEXT" value="<%=COMMENT1%>" size="60"  MAXLENGTH=60 class = "inactive" readonly></TD>
    </TR>
    <TR>
      <TH ALIGN="left" >BIN : </TH>
      <TD><INPUT name="BIN" type = "TEXT" value="<%=BIN%>" size="20"  MAXLENGTH=20 class = "inactive" readonly></TH>
      <TH ALIGN="left" >RECV QTY : </TH>
      <TD><INPUT name="QTYOR" type = "TEXT" value="<%=QTYOR%>" size="20"  MAXLENGTH=20 class = "inactive" readonly></TH>
   </TR>
  </TABLE> 
  <br>


  <TABLE WIDTH="50%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
  <TR>
	<a href="javascript:SetChecked(1);">Check&nbsp;All</a> - <a  href="javascript:SetChecked(0);">Clear&nbsp;All</a>
	</tr>

    <TR BGCOLOR="#000066">
        
         <TH><font color="#ffffff" align="center">LOT NO</TH>
         <TH><font color="#ffffff" align="left"><b>QTY</TH>

       </tr>
       <% if(!Reversed){
          for (int iCnt =0; iCnt<poList.size(); iCnt++){
           map = (Map) poList.get(iCnt);
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
          <TR bgcolor = "<%=bgcolor%>">

		  <td class="main2"><input type="checkbox" name="lot" checked="true"  disabled="true"  value ="<%=(String) map.get("BATCH")%>,<%=(String) map.get("QTY")%>" onClick="addLOTNO()";><%=(String) map.get("BATCH")%></td>
		 <TD><%=(String) map.get("QTY")%></TD>
                 </TR>
       <%}}%>
       <tr><td colspan= 10>&nbsp;</td></tr>
       <tr><td colspan= 10><center><b><%=res%></b></td></tr>
		<div align="center">
          <center>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
				 <input class="Submit" type="Button" value="Back" onClick="window.location.href='pohdr_list.jsp'">
      
             <% if(!Reversed && poList.size() > 0 ){ %>
                 <input type="button"  value="Reverse" onClick="onReverse();" > 
		     <%}%>
            
            <% if(poList.size()<=0 && deleted == false && Reversed== false ){ %>
                <input type="button"  value="Delete" onClick="onDelete(form1);" >
		        <%}%> 
 
               </td>
              </tr>
            </table>
          </center>
        </div>
    </TABLE>
<!-- <%if(poList.size() <= 0){
	String  LotMessage= "<font class=\"mainred\">No records found for the PO :  '"+PONO+"'  </font>";
	%>
	<tr><td colspan= 10><center><b><%=LotMessage%></b></td></tr>
	<%}%> -->
    </FORM>
  <form action="" method="post">
  </body>
</html>

<%@ include file="footer.jsp"%>