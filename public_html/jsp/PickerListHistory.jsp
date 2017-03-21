<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="com.murho.utils.MLogger"%>


<html>
<head>
<script language="javascript">

function popUpWin(URL) {
 subWin = window.open(URL, 'PackSlip', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}

function onGo(){

 var flag    = "false";
   var REFNO    = document.form1.REFNO.value;
   var ITEM    = document.form1.ITEM.value;

   if(REFNO != null    && REFNO != "") { flag = true;}
   if(ITEM != null    && ITEM != "") { flag = true;}
  
 
   if(flag == "false"){ alert("Please define any one search criteria"); return false;}


document.form1.HiddenAprove.value ="View";
   
document.form1.submit();
}


</script>
<title>Pick List History</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();
InvMstDAO invMstDAO = new InvMstDAO();
OBTravelerDetDAO obDetDAO=new OBTravelerDetDAO();
TravelerSummaryUtil travelerSummaryUtil =new TravelerSummaryUtil();
ArrayList invQryList  = new ArrayList();
MLogger logger = new MLogger();
boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
boolean confirm=false;
UserTransaction ut=null ;
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("usrHiddenAction")).trim();
String user_id          = session.getAttribute("LOGIN_USER").toString().toUpperCase();
String TRN_DATE=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
String WHID ="",  ITEM = "", BATCH ="", BINNO ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="",HiddenRemove="";
String html = "";
String TotalString="",TRAVELER="",REFNO="";

String HiddenView="Go",HiddenAprove="",HiddenConfirm="";


//HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove     = strUtils.fString(request.getParameter("HiddenAprove"));
HiddenConfirm    = strUtils.fString(request.getParameter("HiddenConfirm"));
HiddenRemove     = strUtils.fString(request.getParameter("HiddenRemove"));
REFNO     = strUtils.fString(request.getParameter("REFNO"));
ITEM    = strUtils.fString(request.getParameter("ITEM"));
//TRAVELER     = strUtils.fString(request.getParameter("TRAVELER"));




if(HiddenAprove.equalsIgnoreCase("View")){
 try{
    
      invQryList = obDetDAO.getPickerListHistory(REFNO,ITEM);
      if(invQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no Delivery No. found to display<h3></td></tr>";
     
      }
 }catch(Exception e) {System.out.println("Exception :getPickerListHistory"+e.toString()); }
}


%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="PickerListHistory.jsp" >
  <INPUT type="hidden" name="MTID" value="<%=MTID%>">
  <INPUT type="hidden" name="HiddenView" value="Go">
  <INPUT type="hidden" name="HiddenRemove" value="">
  <INPUT type="hidden" name="HiddenAprove" value="">
  <INPUT type="hidden" name="HiddenConfirm" value="">
  
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Pick List History </FONT></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

          <TR>
          
		   <TH ALIGN="RIGHT" >&nbsp;</TH>
                      <TD>&nbsp;</TD>
                      <TH ALIGN="RIGHT" > &nbsp;&nbsp; Reference No :</TH>
                      <TD><INPUT name="REFNO" type = "TEXT" value="<%=REFNO%>" size="20"  MAXLENGTH=20>
                      <a href="#" onClick="javascript:popUpWin('listView/PickerHistoryView.jsp?REFNO='+form1.REFNO.value);"><img src="images/populate.gif" border="0"></a>
                     
		   <TH ALIGN="RIGHT" > &nbsp;&nbsp; Delivery No :</TH>
                      <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=20>
                     <a href="#" onClick="javascript:popUpWin('listView/OB_TravelerView.jsp?ITEM='+form1.ITEM.value+'&REFNO=' +form1.REFNO.value);"><img src="images/populate.gif" border="0"></a>
                       <input type="button" value="Go" onClick="javascript:return onGo();"/></TD>
		
                     
		</TR>

  </TABLE>
  <br>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
      <!--   <th width="3%"><font color="#ffffff" align="center">CHK</th> -->
         <td width="13%" align="left" ><font color="#ffffff" ><STRONG>S/No</STRONG>  </td>
         <td width="13%" align="left"><font color="#ffffff" ><STRONG>DELIVERY NO.</STRONG></font></td>
        <!--  <td width="13%"><font color="#ffffff" align="left"><center><STRONG>CUSTPO</STRONG></center></td> -->
        <!--   <td width="13%"><font color="#ffffff" align="left"><center><STRONG>SO</STRONG></center></td> -->
		    <!--   <td width="13%"><font color="#ffffff" align="left"><center><STRONG>RELEASE</STRONG></center></td>-->
        <!--   <td width="13%"><font color="#ffffff" align="left"><center><STRONG>SOLINE</STRONG></center></td>-->
         <td width="13%" align="left"><font color="#ffffff" align="left"><STRONG>SHIP TO</STRONG></font></td>
         <td width="13%" align="right"><font color="#ffffff" align="left"><STRONG>QTY</STRONG></font></td>
        <!-- <td width="3%"><font color="#ffffff" align="left"><center><STRONG>URGENT</STRONG></center></td> -->
	 </tr>
       <%
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          MTID=(String)lineArr.get("LNNO");
          int iIndex = iCnt + 1;
         String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
         
         TotalString=(String)lineArr.get("TRAVELER")+","+(String)lineArr.get("SO")+","+(String)lineArr.get("RELEASE")+","+(String)lineArr.get("QTY");
      //   int qty =new Integer((String)lineArr.get("QTY")).intValue();
   
       %>

       
       <TR bgcolor = "<%=bgcolor%>">
             <!--<TD align="right" width="12%"><%=iIndex%></TD> -->
            <!-- <TD BGCOLOR="#eeeeee" align="CENTER"><font color="black"> <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="chkdDoNo" value="<%=TotalString%>"></font></TD> -->
        <!--     <TD BGCOLOR="#eeeeee" align="CENTER"><font color="black"> <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="chkdDoNo" value="<%=TotalString%>"></font></TD> -->
           
             <!--<TD align="left" width="13%"><%=(String)lineArr.get("TRAVELER")%></TD>-->
             <TD align="left" width="13%"><%=iIndex%></TD>
             <TD align="left" width="13%"><%=(String)lineArr.get("TRAVELER_ID")%></TD>
             <!-- <TD align="right" width="13%"><%=(String)lineArr.get("CUSTDO")%></TD>-->
            <!--  <TD align="right" width="13%"><%=(String)lineArr.get("SO")%></TD>-->
             <!-- <TD align="right" width="13%"><%=(String)lineArr.get("RELEASE")%></TD>-->
            <!--  <TD align="right" width="13%"><%=(String)lineArr.get("SOLINE")%></TD>-->
             <TD align="left" width="13%"><%=(String)lineArr.get("FIELD11")%></TD>
             <TD align="right" width="13%"><%=(String)lineArr.get("QTY")%></TD> 
            <!-- <TD BGCOLOR="#eeeeee" align="CENTER"><font color="black"> <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="chkdUrgent" value="<%=TotalString%>"></font></TD> -->
          
       
       
          
        
           </TR>
       <%}%>
    
    </TABLE>
    <P>&nbsp;
    </P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <!--   <input type="button" value="Approve" onClick="javascript:return onApprove();"/></TD> -->
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <TR>  <input type="button" value="Back" onClick="window.location.href='indexPage.jsp'">&nbsp
                                      <!--    <input type="button" value="Remove" onClick="javascript:return onRemove();"/> -->
                                       <!--  <input type="button" value="Confirm" onClick="javascript:return onConfirm();"/></TR> -->
    </P>
      
  </FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>
