<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.util.*"%>
<%@ page import="com.murho.util.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<head>
<script language="javascript">


function onGo(){
   var flag    = "false";
   var SONO     = document.form1.SONO.value;
   var PKSTAT     = document.form1.PKSTAT.value;
   
   if(SONO != null     && SONO != "") { flag = true;}
   if(PKSTAT.length > 1 ) { flag = true;}
   if(flag == "false"){ alert("Please define any one search criteria"); return false;}


document.form1.action = "so_list.jsp?action=Go&SONO="+SONO;
document.form1.submit();
}
</script>
<title>SO List</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
SOUtil soUtil       = new SOUtil();
ArrayList soList  = new ArrayList();
//soList = soUtil.getSOSummary();
String action = "";
String res    = "";
String sSono  = "",SONO ="",PKSTAT = "" ;
action = strUtils.fString(request.getParameter("action"));
sSono=strUtils.fString(request.getParameter("SONO"));
SONO = strUtils.fString(request.getParameter("SONO"));
PKSTAT       = strUtils.fString(request.getParameter("PKSTAT"));


if(action.equalsIgnoreCase("Delete")){
     boolean result = false;
     try{
        result= soUtil.updateBlkOrCloseStatus(sSono,"","");
     }catch(Exception e){ result = false;}

    if (result == false){
       res = "<font class=\"mainred\">Block failed to Release  </font>";
    }else if(result == true){
       res = "<font class=\"maingreen\">Block Released Successfully</font>";
   }
}

if(action.equalsIgnoreCase("Go")){
 try{
Hashtable htCondition = new Hashtable();
htCondition.put("DONO",SONO);
String extraCon = PKSTAT;

System.out.println("SONOextraCon  : "+SONO );

System.out.println("extraCon  : "+extraCon );


//soList = pckUtil.viewPickListSummary(DONO,CUSTNO,RELDATE,CUSTNAME,CONO);
soList = soUtil.viewPickListSummary1(htCondition,extraCon);
if (soList.size()==0)
res = "No Records found for this Criteria";
 }catch(Exception e) { }
}

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">SO SUMMARY</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
          <TH ALIGN="left" >SONO : </TH>
          <TD><INPUT name="SONO" type = "TEXT" value="<%=SONO%>" size="20"  MAXLENGTH=20 ></TH>
          <TH  ALIGN="left" >STATUS :</TH>
          <TD > 
			<p align="left">
            <SELECT NAME ="PKSTAT" size="1" >
            <OPTION selected value=''>< - Choose - > </OPTION>
			<option value="ALL">ALL</option>
     			<option value="NEW">NEW</option>
			<option value="PARTIAL">PARTIAL</option>
    			<option value="COMPLETED">COMPLETED</option>
            </SELECT>&nbsp
            </p>
    
   
			<td><td><input type="Submit" value="Go"  onClick="javascript:return onGo();"></TD>
    </TR>
  </TABLE>
  <br>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
         <TH><font color="#ffffff" align="center">#</TH>
         <TH><font color="#ffffff" align="center">SONO</TH>
         <TH><font color="#ffffff" align="left"><b>User Name</TH>
         <TH><font color="#ffffff" align="left"><b>Customer Name</TH>
         <TH><font color="#ffffff" align="left"><b>Status</TH>
         <TH><font color="#ffffff" align="left"><b>Carton</TH>
         <TH><font color="#ffffff" align="left"><b>Comments</TH>
         <TH><font color="#ffffff" align="left"><b>Blk Status</TH>
       </tr>
       <%
          for (int iCnt =0; iCnt<soList.size(); iCnt++){
          ArrayList lineArr = new ArrayList();
          lineArr = (ArrayList)soList.get(iCnt);
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
          <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=iIndex%></TD>
            <TD><a href = "so_item_details.jsp?SONO=<%=(String)lineArr.get(0)%>&CUST=<%=(String)lineArr.get(2)%>"><%=(String)lineArr.get(0)%></a></TD>
            <TD><%=(String)lineArr.get(1)%></TD>
            <TD><%=(String)lineArr.get(2)%></TD>
            <TD><%=(String)lineArr.get(3)%></TD>
            <TD><%=(String)lineArr.get(4)%></TD>
            <TD><%=(String)lineArr.get(5)%></TD>
            <TD><a href = "so_list.jsp?SONO=<%=(String)lineArr.get(0)%>&action=Delete"><%=(String)lineArr.get(6)%></a></TD>
          </TR>
       <%}%>
       <tr><td colspan= 10>&nbsp;</td></tr>
       <tr><td colspan= 10><center><b><%=res%></b></td></tr>
    </TABLE>
  </FORM>
<%@ include file="footer.jsp"%>