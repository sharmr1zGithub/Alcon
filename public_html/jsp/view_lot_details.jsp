<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.seiko.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ include file="header.jsp"%>

<html>
<head>

<title>DO Lot Details</title>
</head>
<%
String SONO="",PACKNUM="",PACKLINE="",DOLNNO="",ITEM="",WHID="",BIN="",COMMENT1="",PLANT="",sUserId="",action="",res="",QTYOR="";
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
ShipmentUtil spUtil   = new ShipmentUtil();
UserTransaction ut    = null;
boolean Reversed = false;
boolean deleted = false;

 SONO          = request.getParameter("DONO");
 PACKNUM       = request.getParameter("PACKNUM");
 DOLNNO        = request.getParameter("DOLNNO");
 ITEM          = request.getParameter("PART");
 WHID          = request.getParameter("WHID");
 BIN           = request.getParameter("BIN");
String[] lots  = request.getParameterValues("lot");




action = strUtils.fString(request.getParameter("action"));
PLANT = (String)session.getAttribute("PLANT");
sUserId = (String) session.getAttribute("LOGIN_USER");
ArrayList arrList =  new ArrayList();
arrList = spUtil.getDetailsForLine(PLANT,SONO,DOLNNO,PACKNUM);

Map map  = null;
Map ma  = null;
int length = 0;
 if (arrList.size() > 0) {
 for (int iCnt =0; iCnt<arrList.size(); iCnt++){
           ma  = (Map) arrList.get(iCnt);

 DOLNNO       =(String)ma.get("DOLNNO");
 ITEM         =(String)ma.get("ITEM");
 WHID         =(String)ma.get("WHID");
 COMMENT1     =(String)ma.get("COMMENT1");
 WHID         =(String)ma.get("WHID");
 BIN          =(String)ma.get("LOC");
 QTYOR        =(String)ma.get("QTYOR");
 PACKLINE     =(String)ma.get("PACKLINE");

 }
 }

 if(action.equalsIgnoreCase("Delete")){

     Map shipReversal_HM  = new HashMap();
     boolean result = false;

        shipReversal_HM.put(SConstant.COMPANY, PLANT);
		    shipReversal_HM.put(SConstant.DO_NUM, SONO);
		    shipReversal_HM.put(SConstant.DO_LINE_NUM, DOLNNO);
		    shipReversal_HM.put(SConstant.MATERIAL_CODE, ITEM);
			  shipReversal_HM.put(SConstant.DO_PACK_NUM, PACKNUM);
			  shipReversal_HM.put(SConstant.DO_PACK_LINE,PACKLINE);
		    shipReversal_HM.put(SConstant.FR_WHSE, WHID);
			  shipReversal_HM.put(SConstant.FR_BIN, BIN);
				 
      
      try{ 
          result= spUtil.DeletePackline(shipReversal_HM);
	   
     }catch(Exception e){
      System.out.println("::::::ShipmentReversal Exception (jsp)      ::::       ::::"+e);
     result = false;}
     
     if (result == false){
			   deleted = true;
			   res = "<font class=\"mainred\">Failed to delete the packline  :  '"+PACKNUM+"'  </font>";
    }else if(result == true){
		   deleted = true;
       res = "<font class=\"maingreen\">Packline deleted  sucessfully</font>";
   }
   action="";
 }

if(action.equalsIgnoreCase("Reverse")){
	
	 Map shipReversal_HM  = new HashMap();
	 String issLot = null;
	 boolean result = false;
	 action = "";
	 if (lots != null) { length = lots.length; }
	 
      if (length > 0) {
		  for (int i = 0; i < length; i++) {
			  issLot = lots[i];
       
        System.out.println("&&&&&&&&&&&&&&&&&&&"+issLot);
       
          if (issLot != null) {

      StringTokenizer str = new StringTokenizer(issLot, ",");

      String batno = str.nextToken();
      String lotQty = str.nextToken();
			shipReversal_HM.put(SConstant.COMPANY, PLANT);
		    shipReversal_HM.put(SConstant.DO_NUM, SONO);
		    shipReversal_HM.put(SConstant.DO_LINE_NUM, DOLNNO);
		    shipReversal_HM.put(SConstant.MATERIAL_CODE, ITEM);
			shipReversal_HM.put(SConstant.DO_PACK_NUM, PACKNUM);
		    shipReversal_HM.put(SConstant.FR_WHSE, WHID);
			shipReversal_HM.put(SConstant.FR_BIN, BIN);
			shipReversal_HM.put(SConstant.LOT_NUM, batno);
			shipReversal_HM.put(SConstant.LOGIN_USER, sUserId);
			shipReversal_HM.put(SConstant.TRAN_QTY, lotQty);
		 
     
     try{ 
        result= spUtil.process_Wms_ShipmentReversal(shipReversal_HM);
     }catch(Exception e){ result = false;}
 }
		  }
		   if (result == false){
			    Reversed = true;
            res = "<font class=\"mainred\">Failed to reverse the shipment for order :  '"+SONO+"'  </font>";
    }else if(result == true){
		      Reversed = true;
             res = "<font class=\"maingreen\">Reverse shipment  completed sucessfully</font>";
   }
	  }  
   
   action ="";


}

%>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">


function onDelete(){

    
     
     var con = confirm ("Are you sure,you want delete the packline ?");
     if(con) {
     document.form1.action  = "view_lot_details.jsp?action=Delete";
     document.form1.submit();
     }else {
         return false;
     }
}


function onReverse(){

     var con = confirm ("Are you sure want to reverse the Lot ?");
     if(con) {
     document.form1.action  = "view_lot_details.jsp?action=Reverse";
     document.form1.submit();
     }else {
         return false;
     }
}


-->
</script>
<%

ArrayList soList = new ArrayList();
soList = spUtil.getLotDetailsList4Line(PLANT,SONO,DOLNNO,PACKNUM);
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post"> 
<input type="hidden" name="passPallet" >
<input type="hidden" name="hdAction" value="<%=soList.size()%>">
<input type="hidden" name="PACKLINE" value="<%=PACKLINE%>">
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
       <TH ALIGN="left" >SO NO : </TH>
       <TD><INPUT name="DONO" type = "TEXT" value="<%=SONO%>" size="20"  MAXLENGTH=40 class = "inactive" readonly></TH>
        <TH ALIGN="left">PACKING SLIPNO: </TH>
	<TD><INPUT name="PACKNUM" type = "TEXT" value="<%=PACKNUM%>" size="20"  MAXLENGTH=20 class = "inactive" readonly></TD>
    </TR>
    <TR>
       <TH ALIGN="left" >LN NO : </TH>
       <TD><INPUT name="DOLNNO" type = "TEXT" value="<%=DOLNNO%>" size="20"  MAXLENGTH=40 class = "inactive" readonly></TH>
        <TH ALIGN="left">PART CODE: </TH>
	<TD><INPUT name="PART" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=20 class = "inactive" readonly></TD>
    </TR>
    <TR>
       <TH ALIGN="left" >WAREHOUSE : </TH>
       <TD><INPUT name="WHID" type = "TEXT" value="<%=WHID%>" size="20"  MAXLENGTH=40 class = "inactive" readonly ></TH>
        <TH ALIGN="left">DESCRIPTION : </TH>
        <TD><INPUT name="DESC" type = "TEXT" value="<%=COMMENT1%>" size="60"  MAXLENGTH=60 class = "inactive" readonly></TD>
    </TR>
    <TR>
      <TH ALIGN="left" >BIN : </TH>
      <TD><INPUT name="BIN" type = "TEXT" value="<%=BIN%>" size="20"  MAXLENGTH=20 class = "inactive" readonly></TH>
      <TH ALIGN="left" >SHIP QTY : </TH>
      <TD><INPUT name="SHIPQTY" type = "TEXT" value="<%=QTYOR%>" size="20"  MAXLENGTH=20 class = "inactive" readonly></TH>
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
          for (int iCnt =0; iCnt<soList.size(); iCnt++){
           map = (Map) soList.get(iCnt);
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
          <TR bgcolor = "<%=bgcolor%>">

		  <td class="main2"><input type="checkbox" name="lot" checked="true"  value ="<%=(String) map.get("BATNO")%>,<%=(String) map.get("QTY")%>" onClick="addLOTNO()";><%=(String) map.get("BATNO")%></td>
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
				 <input class="Submit" type="Button" value="Back" onClick="window.location.href='indexPage.jsp'">

                 <% if(!Reversed && soList.size() > 0 ){ %>
                 <input type="button"  value="Reverse" onClick="onReverse();" > 
		        <%}%>
            
            <% if(soList.size()<=0 && deleted == false && Reversed== false ){ %>
                <input type="button"  value="Delete" onClick="onDelete();" >
		        <%}%> 

               </td>
              </tr>
            </table>
          </center>
        </div>
    </TABLE>

  </FORM>
  <form action="" method="post">
  </body>
</html>

<%@ include file="footer.jsp"%>