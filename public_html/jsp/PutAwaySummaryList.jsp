<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="com.murho.utils.MLogger"%>
<%@ include file="header.jsp"%>

<html>
<head>
<script language="javascript">

function popUpWin(URL) {
 subWin = window.open(URL, 'PackSlip', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onGo(){
 
 var val = 0;
  for( i = 0; i < document.form1.group1.length; i++ )
 {
 if( document.form1.group1[i].checked == true )
 val = document.form1.group1[i].value;
 }
//alert( "val = " + val );
document.form1.filestatus.value =val;
document.form1.HiddenView.value = "Go";
 document.form1.action = "PutAwaySummaryList.jsp";   
document.form1.submit();
}

function onAssign(){

if(form1.PONUM.value=="" || form1.PONUM.value.length==0)
	{
		alert("Enter PO Number");
		form1.PONUM.focus();
		return false;
	}
  

  var val = 0;
  for( i = 0; i < document.form1.group1.length; i++ )
 {
 if( document.form1.group1[i].checked == true )
 val = document.form1.group1[i].value;
 }
 
 document.form1.filestatus.value =val;
  document.form1.HiddenView.value="Go";


 var chkdDoNo =  document.form1.chkdDoNo.value;
 
// document.form1.chkdDoNo.value=chkdDoNo;

 var ischeck = false;
 var Traveler ;
 var concatTraveler="";
 
   var i = 0;
   var noofcheckbox=1;
   
   
    var noofcheckbox = document.form1.chkdDoNo.length;
    
    
    if(form1.chkdDoNo.length == undefined)
    {
            
            if(form1.chkdDoNo.checked)
            {
            document.form1.PONUM.value;
            document.form1.TRAVELER.value=form1.chkdDoNo.value+",";
            document.form1.HiddenGen.value = "Generate";
            document.form1.submit();
            }
    
    }else
    {
             for (i = 0; i < noofcheckbox; i++ )
              {
               ischeck = document.form1.chkdDoNo[i].checked;
                   if(ischeck)
                    {
                    Traveler=document.form1.chkdDoNo[i].value;
                    concatTraveler=concatTraveler+Traveler+",";
                    }
    
               }
              document.form1.TRAVELER.value=concatTraveler;
              document.form1.PONUM.value;
              document.form1.HiddenGen.value = "Generate";
              document.form1.submit();  
    }
 


}


function onApprove(){
document.form1.HiddenAprove.value = "Approve";
var MTID    = document.form1.MTID.value;
 document.form1.submit();
}

function onSubmit(MTID){

var ITEM    = document.form1.ITEM.value;
document.form1.ITEM.value;
document.form1.MTID.value=MTID;
document.form1.action = "ReceivingAprroval.jsp";
//alert(MTID);
document.form1.submit();
}
</script>
<title>PutAway Summary</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();
InvMstDAO invMstDAO = new InvMstDAO();
RecvDetDAO recvDetDAO=new RecvDetDAO();
ArrayList invQryList  = new ArrayList();
MLogger logger = new MLogger();
PutAwayUtil putAwayUtil=new PutAwayUtil ();
boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
UserTransaction ut=null ;
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT =CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("usrHiddenAction")).trim();
String user_id          = session.getAttribute("LOGIN_USER").toString().toUpperCase();
String WHID ="",  ITEM = "", BATCH ="", BINNO ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="";
String html = "",filesstatus="",PONUM="",HiddenGen="",TRAVELER="";
boolean isupdated=false;

String HiddenView="",HiddenAprove="";
ITEM    = strUtils.fString(request.getParameter("ITEM"));
PONUM    = strUtils.fString(request.getParameter("PONUM"));
TRAVELER         = strUtils.fString(request.getParameter("TRAVELER"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenGen   = strUtils.fString(request.getParameter("HiddenGen"));
filesstatus     = strUtils.fString(request.getParameter("filestatus"));

MLogger.info("filesstatus  :"+filesstatus);
MLogger.info("HiddenView   :"+HiddenView);
MLogger.info("ITEM         :"+ITEM );

if(HiddenView.equalsIgnoreCase("Go")){
 try{
       invQryList = recvDetDAO.getPutAwayList(PLANT,ITEM,filesstatus);
       
      if(invQryList.size() < 1)
      {
     //  fieldDesc = "<tr><td><B><h3>Currently no traveller summary found to display<h3></td></tr>";
    
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}

if(HiddenGen.equalsIgnoreCase("Generate")){
PutAwayUtil  pwUtil = new PutAwayUtil ();
boolean flag = false;
try{

Map pmap = new HashMap();
pmap.put("PLANT",PLANT);
pmap.put("TRAVELER",TRAVELER);
pmap.put("filesstatus",filesstatus);
pmap.put("PONUM",PONUM);
pmap.put(MDbConstant.LOGIN_USER,user_id);
flag =  pwUtil.Process_PutAway_GenTxtFile(pmap);
if(flag){
fieldDesc = "<font class = "+MDbConstant.SUCCESS_COLOR +"> Generated txt file Sucessfully</font>";
}else{
fieldDesc = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to Generate txt file </font>";
}
}catch(Exception e){
flag = false;
}
         
      
}
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="" >
  <INPUT type="hidden" name="MTID" value="<%=MTID%>">
   <INPUT type="hidden" name="ITEM" value="<%=ITEM%>">
  <INPUT type="hidden" name="TRAVELER" value="<%=""%>">
  <INPUT type="hidden" name="filestatus" value="<%=filesstatus%>">
  <INPUT type="hidden" name="HiddenView" value="">
  <INPUT type="hidden" name="HiddenGen" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">PutAway Summary</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="60%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

          <TR>
          <% if(filesstatus.equalsIgnoreCase("")) {%>
             <TD> <input type="radio" name="group1" value="" checked> All </TD>
             <TD><input type="radio" name="group1" value="Y"> File Generated </TD>
              <TD><input type="radio" name="group1" value="N" >File Not Generated </TD>
          
             <%}else if(filesstatus.equalsIgnoreCase("Y")){%>
              <TD> <input type="radio" name="group1" value="" > All </TD>
               <TD><input type="radio" name="group1" value="Y" checked> File Generated </TD>
              <TD><input type="radio" name="group1" value="N" >File Not Generated </TD>
          
              <%}else if(filesstatus.equalsIgnoreCase("N")){%>
              <TD> <input type="radio" name="group1" value="" > All </TD>
               <TD><input type="radio" name="group1" value="Y" > File Generated </TD>
              <TD><input type="radio" name="group1" value="N" checked>File Not Generated </TD>
              <%}%>
           
          <TD><input type="button" value="View" onClick="javascript:return onGo();"/> </TD>
          </TR>

  </TABLE>
  <br>
  <TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	    <tr bgcolor="navy">
         <th width="3%"><font color="#ffffff" align="center">CHK</th>
         <td width="13%"><font color="#ffffff" align="left"><center>
           <STRONG>DELIVERY NO.</STRONG>
         </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>RECEIVE STATUS</STRONG> </center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>PUTAWAY STATUS</STRONG></center></td>
         <td width="13%"><font color="#ffffff" align="left"><center><STRONG>FILE GENERATED</STRONG></center></td>
        
	 </tr>
       <%
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          MTID=(String)lineArr.get("LNNO");
          int iIndex = iCnt + 1;
        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>

       
       <TR bgcolor = "<%=bgcolor%>">
             <!-- <TD align="center" width="12%"><%=iIndex%></TD> -->
             <!-- <TD align="center" width="13%"><%=(String)lineArr.get("traveler")%></TD> -->
              <TD BGCOLOR="#eeeeee" align="CENTER"><font color="black"> <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="chkdDoNo" value="<%=(String)lineArr.get("traveler")%>"></font></TD>
              <TD align="center" width="13%"><%=(String)lineArr.get("traveler")%></TD>
              <TD align="center" width="13%"><%=(String)lineArr.get("receivestatus")%></TD>
              <TD align="center" width="13%"><%=(String)lineArr.get("putawaystatus")%></TD>
              <TD align="center" width="13%"><%=(String)lineArr.get("filegenerated")%></TD>
         
           </TR>
       <%}%>
    
    </TABLE>
    
    <TABLE WIDTH="50%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <tr>
         <TH ALIGN="RIGHT" > PO Number :</TH>
         <TD><INPUT name="PONUM" type = "TEXT" value="<%=PONUM%>" size="20"  MAXLENGTH=20  >
      
    </tr>
    </TABLE>
    
    </P>
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <TR>  <input type="button" value="Back" onClick="window.location.href='PutAwaySummary.jsp?HiddenView=Go&ITEM=<%=ITEM%>'">&nbsp
  <!--   <input type="button" value="GenerateExcel" onClick="window.location.href='PutAwaySummaryExcel.jsp?HiddenView=Go&ITEM=<%=ITEM%>&filestatus=<%=filesstatus%>'"> -->
      <input type="button" value="Generate" onClick="javascript:return onAssign(); ">
    </P>
      
  </FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>