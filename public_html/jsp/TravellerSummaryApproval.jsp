<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.utils.ReportUtil"%>
<%@ page import="com.murho.utils.StrUtils"%>
<%@ page import="com.murho.utils.MLogger"%>
<%@ page import="com.murho.dao.InvMstDAO"%>
<%@ page import="com.murho.dao.RecvDetDAO"%>
<%@ page import="com.murho.DO.TransactionDTO"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>

<%@ page import="java.util.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ include file="header.jsp"%>


<html>
<head>
<script language="javascript">

function popUpWin(URL) {
 subWin = window.open(URL, 'PackSlip', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onGo(){

 /* if(form1.FILENAME.value=="" || form1.FILENAME.value.length==0)
	{
   alert("Select filename ");
		form1.FILENAME.focus();
		return false;
	}
  
  if(form1.ITEM.value=="" || form1.ITEM.value.length==0)
	{
    alert("Select traveler no ");
		form1.ITEM.focus();
		return false;
	}*/
document.form1.HiddenAprove.value = "View";
   
document.form1.submit();
}

function onApprove(){
document.form1.HiddenAprove.value = "Approve";
var MTID    = document.form1.MTID.value;
 document.form1.submit();
}

function SetChecked(val)
{
dml=document.form1;
len = dml.elements.length;
var i=0;

for( i=0; i<len; i++)
 {
	 dml.elements[i].checked=val;

 }
}
 
function onRemove(){
 var ischeck = false;
 var Traveler ;
 var concatTraveler="";
 
   var i = 0;
   var noofcheckbox=1;
   
    var noofcheckbox = document.form1.chkdDoNo.length;
    
    document.form1.HiddenRemove.value="Remove";
   
    
    if(form1.chkdDoNo.length == undefined)
    {            
            if(form1.chkdDoNo.checked)
            {
            document.form1.TRAVELER.value=form1.chkdDoNo.value+"=";
            document.form1.action = "TravellerSummaryApproval.jsp";
            document.form1.submit();
            }
    }else
    {
             for (i = 0; i < noofcheckbox; i++ )
              {
               ischeck = document.form1.chkdDoNo[i].checked;
               // alert(ischeck);
                   if(ischeck)
                    {
                    Traveler=document.form1.chkdDoNo[i].value;
                    concatTraveler=concatTraveler+Traveler+"=";
                    }
    
               }
              document.form1.TRAVELER.value=concatTraveler;
              document.form1.action = "TravellerSummaryApproval.jsp";
              document.form1.submit();  
    }
  
}

function onSubmit(MTID){

var ITEM    = document.form1.ITEM.value;
document.form1.ITEM.value;
document.form1.MTID.value=MTID;
document.form1.action = "TravellerSummaryApproval.jsp";
//alert(MTID);
document.form1.submit();
}
</script>
<title>DeliveryNo's Summary</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%

StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();
InvMstDAO invMstDAO = new InvMstDAO();
RecvDetDAO recvDAO = new RecvDetDAO();
ArrayList invQryList  = new ArrayList();
TransactionDTO  trnDTO=new TransactionDTO();
MLogger logger = new MLogger();
boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
UserTransaction ut=null ;
String PLANT        = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT =CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("usrHiddenAction")).trim();
String user_id          = session.getAttribute("LOGIN_USER").toString().toUpperCase();
String WHID ="",  ITEM = "", BATCH ="", BINNO ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="",FILENAME="";
String html = "";

String TotalString="",TRAVELER="",HiddenRemove="";
TravelerSummaryUtil travelerSummaryUtil =new TravelerSummaryUtil();
String TRN_DATE=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());


int orderQty =0;
int recvQty =0;
boolean confirm=false;
      

ITEM    = strUtils.fString(request.getParameter("ITEM"));
FILENAME   = strUtils.fString(request.getParameter("FILENAME"));


String HiddenView="Go",HiddenAprove="";
//HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove    = strUtils.fString(request.getParameter("HiddenAprove"));
HiddenRemove     = strUtils.fString(request.getParameter("HiddenRemove"));
TRAVELER     = strUtils.fString(request.getParameter("TRAVELER"));



if(HiddenView.equalsIgnoreCase("Go")){

 try{
    
     /*invQryList = recvDAO.getReceiveDetails1();
      if(invQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no traveller summary found to display<h3></td></tr>";
     
      }*/
       ArrayList alTransactionData=new ArrayList();
      trnDTO.setTraveler(PLANT);
      trnDTO.setTraveler(ITEM);
      trnDTO.setFilename(FILENAME);
      alTransactionData.add(trnDTO);
      invQryList=recvDAO.getReceiveDetailsById(alTransactionData);
      
      session.setAttribute("IDELIVERYITEM", ITEM );
      session.setAttribute("IDELIVERYFILENAME", FILENAME);
      
      
   
      /*if(invQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no DeliveryNo's summary found to display<h3></td></tr>";
      }*/
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}
 if(HiddenAprove.equalsIgnoreCase("View")){
 try{
     
      ArrayList alTransactionData=new ArrayList();
      trnDTO.setPlant(PLANT);
      trnDTO.setTraveler(ITEM);
      trnDTO.setFilename(FILENAME);
      alTransactionData.add(trnDTO);
      invQryList=recvDAO.getReceiveDetailsById(alTransactionData);
   
      if(invQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no Delivery No. summary found to display<h3></td></tr>";
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}
else if(HiddenRemove.equalsIgnoreCase("Remove")){
     
     try{
       String strMsg="<tr><td><B><h3>Delivery No. Details Removed Successfully<h3></td></tr>";
       Hashtable ht = null;
       ht = new Hashtable();
       
       confirm = travelerSummaryUtil.RemoveRecvDet(TRAVELER,user_id,TRN_DATE);
      
      if(confirm)
      {
       // fieldDesc = "<tr><td><B><h3>DeliveryNo Details Removed Successfully<h3></td></tr>";
         response.sendRedirect("TravellerSummaryApproval.jsp?HiddenAprove=View&PLANT="+PLANT+"&ITEM="+ITEM+"&FILENAME="+FILENAME+" &fieldDesc="+strMsg+"");
         fieldDesc = strMsg;
    
      }
      else if(!travelerSummaryUtil.isReceiveStatus)
      {
          fieldDesc = "<tr><td><B><h3>Cannot Delete  Delivery No., Receiving Started  <h3></td></tr>";
      }
      else
      {
         fieldDesc = "<tr><td><B><h3>Error in deleting receive details <h3></td></tr>";
     
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }

}

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="TravellerSummaryApproval.jsp">
  <INPUT type="hidden" name="MTID" value="<%=MTID%>"/>
  <INPUT type="hidden" name="HiddenView" value="Go"/>
  <INPUT type="hidden" name="HiddenAprove" value=""/>
  <INPUT type="hidden" name="HiddenRemove" value=""/>
  <INPUT type="hidden" name="TRAVELER" value=""/>
  
  <br/>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Delivery No. Summary</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br/>
  <TABLE border="0" width="62%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH ALIGN="Right" width="10%">File Name :</TH>
      <TD width="30%">
        <INPUT name="FILENAME" type="TEXT" value="<%=FILENAME%>" size="20" MAXLENGTH="20"/>
        <a href="#" onClick="javascript:popUpWin('FileViewPopUp.jsp?FILENAME='+form1.FILENAME.value);">
          <img src="images/populate.gif" border="0"/>
        </a>
      </TD>
      <TH ALIGN="Right" width="18%">Inbound Delivery No :</TH>
       <TD width="25%" align="left">
        <INPUT name="ITEM" type="TEXT" value="<%=ITEM%>" size="20" MAXLENGTH="20"/>
          <a href="#" onClick="javascript:popUpWin('TravellerPopUp.jsp?ITEM='+form1.ITEM.value+'&FILENAME='+form1.FILENAME.value);"><img src="images/populate.gif" border="0"/></a>
          <input type="button" value="Go" onClick="javascript:return onGo();"/>
      </TD>
      <!-- &lt;a href=&quot;#&quot; onClick=&quot;javascript:popUpWin('listView/OB_TravelerView.jsp?ITEM='+form1.ITEM.value+'&amp;REFNO=' +form1.REFNO.value);&quot;&gt;&lt;img src=&quot;images/populate.gif&quot; border=&quot;0&quot;&gt;&lt;/a&gt;-->
    </TR>
  </TABLE>
  <br/>
  <TABLE WIDTH="74%" border="0" cellspacing="1" cellpadding="2" align="right" height="90">
    <tr bgcolor="navy">
      <td width="4%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>CHK</STRONG>
          </center>
        </font>
      </td>
      <th width="12%">
        <font color="#ffffff" align="center">SNO</font>
      </th>
      <td width="13%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>Delivery No.</STRONG> 
          </center>
        </font>
      </td>
      <td width="13%">
        <font color="#ffffff" align="left">
          <center>
            <STRONG>ORDER QTY</STRONG> 
          </center>
        </font>
      </td>
      <td width="13%">
        <font color="#ffffff" align="left">
          <center>
            <b>RECV QTY</b>
          </center>
        </font>
      </td>
      <td width="13%">
        <font color="#ffffff" align="left">
          <center>
            <b>Receiving Status</b>
          </center>
        </font>
      </td>
    </tr>
    <% 
         for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
         Map lineArr = (Map) invQryList.get(iCnt);
         MTID=(String)lineArr.get("LNNO");
         int iIndex = iCnt + 1;
         String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
         int orqty=new Integer((String)lineArr.get("ORDQTY")).intValue();
         int rcvqty=new Integer((String)lineArr.get("RECVQTY")).intValue();
         orderQty = orderQty+orqty;
         recvQty = recvQty +rcvqty;
         TotalString=(String)lineArr.get("TRAVELER")+","+(String)lineArr.get("LOT");
       %>
    <TR bgcolor="<%=bgcolor%>">
      <TD BGCOLOR="#eeeeee" width="4%" align="CENTER">
        <font color="black">
         
          <INPUT Type="Checkbox" style="border:0;background=#dddddd" name="chkdDoNo" value="<%=TotalString%>"/>
        </font>
      </TD>
      <TD align="center" width="12%">
        <%= iIndex%>
      </TD>
      <TD align="center" width="13%">
        <a href="TravelerSummaryApprovalDelNoDetail.jsp?action=view&ITEM=<%=(String)lineArr.get("TRAVELER")%>">
          <%= (String)lineArr.get("TRAVELER")%>
        </a>
      </TD>
      <!---&lt;TD align=&quot;center&quot; width=&quot;13%&quot;&gt;&lt;%=(String)lineArr.get(&quot;TRAVELER&quot;)%&gt;&lt;/TD&gt;-->
      <TD align="center" width="13%">
        <%= (String)lineArr.get("ORDQTY")%>
      </TD>
      <TD align="center" width="13%">
        <%= (String)lineArr.get("RECVQTY")%>
      </TD>
      <TD align="center" width="13%">
        <%= (String)lineArr.get("RECEIVESTATUS")%>
      </TD>
    </TR>
    <%  }
       
       %>
    <TR>
      <TD/>
      <TD align="center"/>
      <TD align="center">
        <B>Total :</B> 
      </TD>
      <TD align="center">
        <B>
          <%=     orderQty%>
        </B>
      </TD>
      <TD align="center">
        <B>
          <%=      recvQty%>
        </B>
      </TD>
      <TD align="right">&nbsp;</TD>
      <TD align="right">&nbsp;</TD>
    </TR>
  </TABLE>
  <P>&nbsp;</P>
  <P>&nbsp;</P>
  <P>&nbsp;</P>
  <P>&nbsp;</P>
  <P>&nbsp;</P>
  <P>&nbsp;</P>
  <P>&nbsp;</P>
  <TR>
    <a href="javascript:SetChecked(1);">Check&nbsp;All</a> 
    <a href="javascript:SetChecked(0);">Clear&nbsp;All</a> 
  </TR>
  <P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
    <!--   &lt;input type=&quot;button&quot; value=&quot;Approve&quot; onClick=&quot;javascript:return onApprove();&quot;/&gt;&lt;/TD&gt; -->&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</P>
  <TR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TR>
  <TR>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" value="Back" onClick="window.location.href='indexPage.jsp'"/>
    <input type="button" value="Remove" onClick="javascript:return onRemove();"/>
    
  </TR>
  <font face="Times New Roman">
    <table border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%= fieldDesc%>
    </table>
  </font>
  <%@ include file="footer.jsp"%>
</FORM>
