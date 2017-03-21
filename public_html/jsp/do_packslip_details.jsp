<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>

<%@ page import="java.util.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ include file="header.jsp"%>

<html>
<head>

<title>SO Item Details</title>
</head>
<%
String SONO           = request.getParameter("DONO");
String PACKNUM           = request.getParameter("PACKNUM");
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
ShipmentUtil spUtil       = new ShipmentUtil();
UserTransaction ut    = null;
String action= "";
String res    = "";
action = strUtils.fString(request.getParameter("action"));
String PLANT = (String)session.getAttribute("PLANT");
ArrayList arrList =  new ArrayList();
arrList = spUtil.getDoHdrList(PLANT,SONO,"","",PACKNUM);
 Map ma  = null;
 for (int iCnt =0; iCnt<arrList.size(); iCnt++){
           ma = (Map) arrList.get(iCnt);
 }

/*if(action.equalsIgnoreCase("Delete")){
     List qryFields   = new ArrayList();
     qryFields.add(IConstants.ITEM);     // item
     qryFields.add(IConstants.USERFLD1); // Descriotion
     qryFields.add(IConstants.QTY_ORDER); // Qty order
     qryFields.add(IConstants.QTY_ISSUE); // Qty Pick
     qryFields.add(IConstants.USERTIME2); // Remarks
     qryFields.add(IConstants.LOC);
     qryFields.add(IConstants.DONO);
     Hashtable htCondition1 = new Hashtable();
     htCondition1.put(IConstants.DONO,SONO);
     try{
         ut = DbBean.getUserTranaction();
     }catch(Exception e){ }
     boolean result = false;
     try{
        ut.begin();
        result= soUtil.rollBackSoPicking(qryFields,htCondition1,"");
     }catch(Exception e){ result = false;}

    if (result == false){
       try{ut.rollback();}catch(Exception e){ }
       res = "<font class=\"mainred\">Failed to Reverse Picking for Order :  '"+SONO+"'  </font>";
    }else if(result == true){
       try{ut.commit();}catch(Exception e){ }
       res = "<font class=\"maingreen\">Reverse Picking Completed Sucessfully</font>";
   }
}*/

%>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">

/*function onDelete(){
     var con = confirm ("Are you sure want to RollBack the Order ?");
     if(con) {
     document.form1.action  = "so_item_details.jsp?action=Delete";
     document.form1.submit();
     }else {
         return false;
     }
}*/
-->
</script>
<%

ArrayList soList = new ArrayList();
soList = spUtil.getSlipList4PackNum(PLANT,SONO,PACKNUM);
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
<!--input type = "hidden" name = "SONO" value = "<!--%=SONO%>"-->
  <br>
  <TABLE border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066"><font color="white">DO SLIPNO DETAILS</font></TH>
    </TR>
  </TABLE>
<br>
<TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
       <TH ALIGN="left" >SONO : </TH>
       <TD><INPUT name="DONO" type = "TEXT" value="<%=SONO%>" size="20"  MAXLENGTH=40 class = "inactive" readonly></TH>
        <TH ALIGN="left">PACKING SLIP NO: </TH>
	<TD><INPUT name="PACKNUM" type = "TEXT" value="<%=PACKNUM%>" size="20"  MAXLENGTH=20 class = "inactive" readonly></TD>
    </TR>
    <TR>
       <TH ALIGN="left" >CUST NO : </TH>
       <TD><INPUT name="CUSTNO" type = "TEXT" value="<%=(String) ma.get("CUSTNO")%>" size="20"  MAXLENGTH=40 class = "inactive" readonly ></TH>
        <TH ALIGN="left">CUST NAME : </TH>
        <TD><INPUT name="CUSTNAME" type = "TEXT" value="<%=(String) ma.get("COMMENT1")%>" size="60"  MAXLENGTH=60 class = "inactive" readonly></TD>
    </TR>
    <TR>
    <TH ALIGN="left" >SHIP DATE : </TH>
    <TD><INPUT name="SHIPDATE" type = "TEXT" value="<%=(String) ma.get("DELDATE")%>" size="20"  MAXLENGTH=20 class = "inactive" readonly></TH>
    </TR>
  </TABLE>
  <br>

  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
         <TH><font color="#ffffff" align="center">LN NO</TH>
         <TH><font color="#ffffff" align="center">PART CODE</TH>
         <TH><font color="#ffffff" align="left"><b>DESCRIPTION</TH>
         <TH><font color="#ffffff" align="left"><b>SHIP QTY</TH>
         <TH><font color="#ffffff" align="left"><b>WHS</TH>
         <TH><font color="#ffffff" align="left"><b>BIN</TH>
       </tr>
       <%
          for (int iCnt =0; iCnt<soList.size(); iCnt++){
          Map map = (Map) soList.get(iCnt);
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
          <TR bgcolor = "<%=bgcolor%>">
           
			 <TD align="center"><a href = "view_lot_details.jsp?DONO=<%=SONO%>&PACKNUM=<%=PACKNUM%>&DOLNNO=<%=(String)map.get("DOLNNO")%>"><%=(String)map.get("DOLNNO")%></a></TD>
	

            <TD><%=(String) map.get("ITEM")%></TD>
            <TD><%=(String) map.get("COMMENT1")%></TD>
            <TD><%=(String) map.get("QTYOR")%></TD>
            <TD><%=(String) map.get("WHID")%></TD>
            <TD><%=(String) map.get("LOC")%></TD>
             </TR>
       <%}%>
       <tr><td colspan= 10>&nbsp;</td></tr>
       <tr><td colspan= 10><center><b><%=res%></b></td></tr>
		<div align="center">
          <center>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
				<input type="Button" value="Cancel" onClick="window.location.href='javascript:history.back()'">
                <!--   <input type="Submit"  value="Delete" onClick="onDelete();"> -->
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