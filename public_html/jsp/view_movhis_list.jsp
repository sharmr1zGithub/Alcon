<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.gates.Generator"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="gn"  class="com.murho.gates.Generator" />
<jsp:useBean id="sl"  class="com.murho.gates.selectBean" />
<jsp:useBean id="mvt" class="com.murho.dao.MovHisDAO"/>


<html>
<head>
<script language="javascript">
function onGo(){

   var flag    = "false";

   var FROM_DATE    = document.form1.FROM_DATE.value;
   var TO_DATE      = document.form1.TO_DATE.value;
   var DIRTYPE      = document.form1.DIRTYPE.value;
   var BATCH         = document.form1.BATCH.value;
   var USER         = document.form1.USER.value;
   var ITEM         = document.form1.ITEM.value;
   var MTID         = document.form1.MTID.value;
   var TRNO         = document.form1.TRNO.value;


   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}
   if(BATCH != null     && BATCH != "") { flag = true;}
   if(USER != null    && USER != "") { flag = true;}
   if(ITEM != null     && ITEM != "") { flag = true;}
    if(MTID != null     && MTID != "") { flag = true;}
     if(TRNO != null     && TRNO != "") { flag = true;}

   if(flag == "false"){ alert("Please define any one search criteria"); return false;}


document.form1.submit();
}
</script>
</script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<title>Movement History List</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
ReportUtil movHisUtil       = new ReportUtil();
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();


String action         = _strUtils.fString(request.getParameter("action")).trim();

String FROM_DATE ="",  TO_DATE = "", DIRTYPE ="",BATCH ="",USER="",ITEM="",fdate="",tdate="",MTID="",TRNO="";

String html = "",cntRec ="false";

FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));


if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
if (FROM_DATE.length()>5)
fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

MLogger.printInput("From Date : "+fdate);

if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

MLogger.printInput("To tdate : " + tdate);
MLogger.printInput("To fdate : " + fdate);

DIRTYPE       = _strUtils.fString(request.getParameter("DIRTYPE"));
BATCH          = _strUtils.fString(request.getParameter("BATCH"));
USER          = _strUtils.fString(request.getParameter("USER"));
ITEM          = _strUtils.fString(request.getParameter("ITEM"));
MTID          = _strUtils.fString(request.getParameter("MTID"));
TRNO          = _strUtils.fString(request.getParameter("TRNO"));

if(action.equalsIgnoreCase("Go")){
 try{
        Hashtable ht = new Hashtable();
       // if(_strUtils.fString(FROM_DATE).length() > 0)   ht.put(IConstants.CREATED_AT,FROM_DATE);
       // if(_strUtils.fString(TO_DATE).length() > 0)     ht.put(IConstants.CREATED_AT,TO_DATE);


        if(_strUtils.fString(DIRTYPE).length() > 0)     ht.put("MOVTID",DIRTYPE);
        if(_strUtils.fString(BATCH).length() > 0)       ht.put("LOT",BATCH);
        if(_strUtils.fString(USER).length() > 0)        ht.put("CRBY",USER);
        if(_strUtils.fString(ITEM).length() > 0)        ht.put("SKU",ITEM);
        if(_strUtils.fString(MTID).length() > 0)       ht.put("MTID",MTID);
        if(_strUtils.fString(TRNO).length() > 0)       ht.put("TRAVELER",TRNO);

        movQryList = movHisUtil.getMovHisList(ht,fdate,tdate);
		if(movQryList.size()<=0)
			cntRec ="true";



 }catch(Exception e) { }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">MOVEMENT HISTORY LIST</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%" height = "20%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
          <TH ALIGN="left" >FROM_DATE : </TH>
          <TD><INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="20"  MAXLENGTH=20 class = inactive READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form1.FROM_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
          <TH ALIGN="left">TO_DATE : </TH>
          <TD><INPUT name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="20"  MAXLENGTH=20 class = inactive READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form1.TO_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
		 
    </TR>
	
    <TR>
          <TH ALIGN="left" >MOVEMENT : </TH>
                        <TD>
                        <SELECT NAME ="DIRTYPE" size="1">
                          <OPTION selected value=''>< -- Choose -- > </OPTION>
                          <%=mvt.getMOVTID()%>
                        </SELECT>
                      </TD>
                   
          <TH ALIGN="left">USER : </TH>
          <TD><INPUT name="USER" type = "TEXT" value="<%=USER%>" size="20"  MAXLENGTH=20></TD>
    </TR>
	
    <TR>
          <TH ALIGN="left" >LOT NO : </TH>
          <TD><INPUT name="BATCH" type = "TEXT" value="<%=BATCH%>" size="20"  MAXLENGTH=20></TD>
          <TH ALIGN="left">SKU : </TH>
          <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=20></TD>

		  <TD ALIGN="left" ><TD ALIGN="left" ><TD ALIGN="left" ><input type="submit" value="Go" name="action" onClick="javascript:return onGo();"></TD>
		          
    </TR>
    
    <TR>
    <TH ALIGN="left" >MTID : </TH>
          <TD><INPUT name="MTID" type = "TEXT" value="<%=MTID%>" size="20"  MAXLENGTH=20></TD>
          
    <TH ALIGN="left" >Delivery&nbsp; NO. : </TH>
          <TD><INPUT name="TRNO" type = "TEXT" value="<%=TRNO%>" size="20"  MAXLENGTH=20></TD>
    </TR>
	
  </TABLE>
  <br>
  <TABLE WIDTH="85%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
         <TH><font color="#ffffff" align="center">SNO</TH>
         <TH><font color="#ffffff" align="left"><b>Date&Time</TH>
         <TH><font color="#ffffff" align="left"><b>Movement</TH>
         <TH><font color="#ffffff" align="left"><b>Delivery No.</TH>
         <TH><font color="#ffffff" align="left"><b>MTID</TH>
         <TH><font color="#ffffff" align="left"><b>SKU</TH>
         <TH><font color="#ffffff" align="left"><b>LOC</TH>
	       <TH><font color="#ffffff" align="left"><b>Lot No</TH>
         <TH><font color="#ffffff" align="left"><b>Quantity</TH>
         <TH><font color="#ffffff" align="left"><b>Remarks</TH>
          <TH><font color="#ffffff" align="left"><b>ReasonCode</TH>
         <TH><font color="#ffffff" align="left"><b>User</TH>
       </tr>
       <%
	      if(movQryList.size()<=0 && cntRec=="true" ){ %>
		  <TR><TD colspan=15 align=center>No Data For This criteria</TD></TR>
		  <%}%>

		  <%
          for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
			    Map lineArr = (Map) movQryList.get(iCnt);

          String trDate= "";
          trDate=(String)lineArr.get("CRAT");
		  if (trDate.length()>8)
      
          trDate    = trDate.substring(8,10)+"-"+ trDate.substring(5,7)+"-"+trDate.substring(0,4);
          trDate=trDate+" "+(String)lineArr.get("crtime");
          
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
       
       
      
          <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=iIndex%></TD>
            <TD><%=trDate%></TD>
            <TD align= "right"><%=(String)lineArr.get("MOVTID")%></TD>
            <TD align= "right"><%=(String)lineArr.get("TRAVELER")%></TD>
            <TD align= "right"><%=(String)lineArr.get("MTID")%></TD>
            <TD align= "right"><%=(String)lineArr.get("SKU") %></TD>
            <TD align= "right"><%=(String)lineArr.get("loc")%></TD>
            <TD align= "right"><%=(String)lineArr.get("LOT") %></TD>
            <TD align= "right"><%=(String)lineArr.get("QTY") %></TD>
            <TD align= "right"><%=(String)lineArr.get("remark")%></TD>
             <TD align= "right"><%=(String)lineArr.get("REASONCODE")%></TD>
			      <TD align= "right"><%=(String)lineArr.get("CRBY")%></TD>
           
          
           </TR>
       <%}%>


    </TABLE>
  </FORM>
<%@ include file="footer.jsp"%>