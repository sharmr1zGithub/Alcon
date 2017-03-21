<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ page import="com.murho.DO.TransactionDTO"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="com.murho.utils.MLogger"%>
<%@ page import="com.murho.utils.CibaConstants"%>
<%@ include file="header.jsp"%>

<html>
<head>
<script language="javascript">

function popUpWin(URL) {
 subWin = window.open(URL, 'PackSlip', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onGo(){
document.form1.HiddenView.value = "Go";
   
document.form1.submit();
}

function IsNumeric(sText)
{
var ValidChars = "0123456789";
for (i = 0; i < sText.length; i++) { 
if (ValidChars.indexOf(sText.charAt(i)) == -1) {
return false;
}
}
return true;
}

function onUpdate()
{
 var part=document.form1.SKU01.value;
 var lot=document.form1.LOTNO01.value;
 var qty=document.form1.QTY01.value;
 var remark=document.form1.REMARK.value;
 
   var ValidChars = document.form1.QTY01.value;
   var IsNumber=true;
   var Char;


 	if(form1.SKU01.value=="" || form1.SKU01.value.length==0)
	{
		alert("Please enter SKU ");
		form1.SKU01.focus();
		return false;
	}else if(form1.LOTNO01.value=="" || form1.LOTNO01.value.length==0)
	{
		alert("Please enter Lot No ");
		form1.LOTNO01.focus();
		return false;
	}else if(form1.QTY01.value=="" || form1.QTY01.value.length==0)
	{
		alert("Please enter quantity ");
		form1.QTY01.focus();
    return false;
 
 	}/*else if(ValidChars.Length() > 0)
  {
  
   for (i = 0; i < ValidChars.length ; i++) 
      { 
      Char = ValidChars.charAt(i); 
      if (ValidChars.indexOf(Char) == -1) 
         {
          alert("enter numeric value");
         return false;
        
         }else{
         
         return true;
         }
      }
      
  }*/
 else if(form1.REMARK.value=="" || form1.REMARK.value.length==0)
	{
		alert("Please enter remarks ");
		form1.REMARK.focus();
		return false;
	
   }
   else if(!IsNumeric(form1.QTY01.value))
   {
   alert(" Please Enter valid Qty !");
   form1.QTY01.focus();return false;
   } /*else if(form1.QTY01.value > form1.QTY.value)
   {
   alert(" Qty Should not be greater than Order qty !");
   form1.QTY01.focus();return false;
   }*/else
   document.form1.action = "ReceivingUpdate.jsp?action=Update";
   document.form1.submit();
   return true;

}

function onReject()
{
 var part=document.form1.SKU01.value;
 var lot=document.form1.LOTNO01.value;
 var qty=document.form1.QTY01.value;
 var remark=document.form1.REMARK.value;
 
/* 	if(form1.SKU01.value=="" || form1.SKU01.value.length==0)
	{
		alert("Please enter SKU ");
		form1.SKU01.focus();
		return false;
	}else if(form1.LOTNO01.value=="" || form1.LOTNO01.value.length==0)
	{
		alert("Please enter Lot No ");
		form1.LOTNO01.focus();
		return false;
	}else if(form1.QTY01.value=="" || form1.QTY01.value.length==0)
	{
		alert("Please enter quantity ");
		form1.QTY01.focus();
		return false;
	}else*/
  if(form1.REMARK.value=="" || form1.REMARK.value.length==0)
	{
		alert("Please enter remarks ");
		form1.REMARK.focus();
		return false;
	}else
  document.form1.action = "ReceivingUpdate.jsp?action=Reject";
  document.form1.submit();
  return true;

}

</script>
<title>Receiving update </title>
</head>
<link rel="stylesheet" href="css/style.css">
<%

StrUtils strUtils     = new StrUtils();
RecevingUtil recevingUtil=new RecevingUtil();
RecvDetDAO dao= new RecvDetDAO();
InvMstDAO _InvMstDAO=new InvMstDAO();
DataDownloaderUtil  _DataDownloaderUtil=new   DataDownloaderUtil();
//CibaConstants _CibaConstants=new CibaConstants();
ArrayList invQryList  = new ArrayList();
MLogger logger = new MLogger();

String WHID ="",  ITEM = "", BATCH ="", BINNO ="", QTY ="",QTYALLOC ="" , PKTYPE ="",MTID="",fieldDesc="",actionApprove="";
String html = "";
String TRAVELER_NO="",PART="",DESCRIPTION="",LOC="",LOT="",PLT="",REMARK="",RECVQTY="",tranTime="",TRN_DATE="";
String PART01="",LOT01="",QTY01="";
String HiddenView="",HiddenAprove="",RECEIVESTATUS="";

boolean approve = false;
boolean failedline=false;
boolean invinsert = false;
UserTransaction ut=null ;



String PLANT          = (String)session.getAttribute("PLANT");if(PLANT == null) PLANT =CibaConstants.cibacompanyName;
String action         = strUtils.fString(request.getParameter("action")).trim();
String user_id        = session.getAttribute("LOGIN_USER").toString().toUpperCase();

TRN_DATE=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());
tranTime=DateUtils.Time();

LOT                  = strUtils.fString(request.getParameter("LOT"));
MTID                  = strUtils.fString(request.getParameter("MTID"));


HiddenView            = strUtils.fString(request.getParameter("HiddenView"));
HiddenAprove          = strUtils.fString(request.getParameter("HiddenAprove"));

TRAVELER_NO           = strUtils.fString(request.getParameter("TRAVELERNO"));
MTID                  = strUtils.fString(request.getParameter("MTID"));

PART                  = strUtils.fString(request.getParameter("SKU"));
PART01                = strUtils.fString(request.getParameter("SKU01"));

PLT                   = strUtils.fString(request.getParameter("PLT"));

//LOT                   = strUtils.fString(request.getParameter("LOTNO"));
LOT01                 = strUtils.fString(request.getParameter("LOTNO01"));

QTY                   = strUtils.fString(request.getParameter("QTY"));
QTY01                 = strUtils.fString(request.getParameter("QTY01"));

REMARK                = strUtils.fString(request.getParameter("REMARK"));

RECEIVESTATUS         = strUtils.fString(request.getParameter("RECEIVESTATUS"));

MLogger.log("TRAVELER_NO :"+TRAVELER_NO);
MLogger.log("MTID :"+MTID);
MLogger.log("PART01 :"+PART01);
MLogger.log("LOT01 :"+LOT01);
MLogger.log("QTY01 :"+QTY01);
MLogger.log("REMARK :"+REMARK);
boolean isrecvstatus=false;
TransactionDTO  trnDTO;
//To Check Receiving status
ArrayList alStatus=new ArrayList();

trnDTO =new TransactionDTO();
trnDTO.setTraveler(TRAVELER_NO);
trnDTO.setMtid(MTID );
                        
alStatus.add(trnDTO);
                            
isrecvstatus =_DataDownloaderUtil.getRecvStatusforUpdate(alStatus);
//Check  Receiving Status End

if(action.equalsIgnoreCase("Update"))
{

int trayqty=dao.chkUpdatedQty(PART01);
int  chkqty= Integer.parseInt(((String)QTY01.toString()));
int chkordqty=Integer.parseInt(((String)QTY.toString()));
int chkupdqty=Integer.parseInt(((String)QTY01.toString()));
 int moreqty=chkordqty+5;


SQLRecvDet_DAO _SQLRecvDet = new SQLRecvDet_DAO();	
boolean chkPUV=false;
chkPUV= _SQLRecvDet.checkPUV(PART01);


                 

boolean isupdated=false;


try{
Hashtable ht = null;
ht = new Hashtable();

/*ht.put("PLANT",CibaConstants.cibacompanyName);
ht.put("TRAVELER", TRAVELER_NO);
ht.put("MTID", MTID);
ht.put("PART01", PART01);
ht.put("LOT01", LOT01);
ht.put("QTY01", QTY01);
ht.put("REMARK", REMARK);
ht.put("CRBY","BASEER");
ht.put("CRAT", "2007-08-31");*/

ht.put(MDbConstant.TRAVELER_NUM,TRAVELER_NO);
ht.put(MDbConstant.PALLET,PLT);
ht.put(MDbConstant.MTID,MTID);
ht.put(MDbConstant.ITEM,PART01);
ht.put(MDbConstant.LOT_NUM,LOT01);
ht.put(MDbConstant.RECV_QTY,QTY01);
ht.put(MDbConstant.LOGIN_USER,user_id);
ht.put(MDbConstant.ReceiveStatus,"");
ht.put(MDbConstant.REMARK,REMARK);
ht.put(MDbConstant.MOVHIS_REF_NUM,"RECV-UPDATE");
ht.put(MDbConstant.MOVEHIS_CR_DATE,TRN_DATE);
ht.put(MDbConstant.CRTIME,tranTime);
ht.put(MDbConstant.MOVHIS_QTY,QTY01);
 
//isupdated=recevingUtil.UpdateReceiveprocess(ht);
System.out.println("chkpuv........."+chkPUV);
if(trayqty < chkqty)
{
  fieldDesc= "<tr><td><B><h3>Updated Qty should less than Tray qty"+ " " + trayqty+"<h3></td></tr>";
  LOT=LOT01 ;
}
/*else if(isrecvstatus==false)
{
   fieldDesc= "<tr><td><B><h3>MTID is already received,Unable to update details<h3></td></tr>";
   //fieldDesc= "<tr><td><B><h3>MTID is already received,Unable to update details"<h3></td></tr>";
}*/
else if(chkPUV==false && chkupdqty >chkordqty)
{
    fieldDesc= "<tr><td><B><h3>Qty Should not be greater than Order qty !<h3></td></tr>";
   LOT=LOT01 ;
 }
else if(chkPUV==true  &&  chkupdqty > moreqty)
{
    fieldDesc= "<tr><td><B><h3>Qty Should not be greater than allowed puv  qty !"+ " " +moreqty+"<h3></td></tr>";
     LOT=LOT01 ;
 } 
else
{
   
  isupdated=recevingUtil.UpdateReceiveprocess(ht);
  if(isupdated){
      response.sendRedirect("ReceivingAprroval.jsp?HiddenView=Go&ITEM="+TRAVELER_NO+"");
  }else
  {
    fieldDesc = "<tr><td><B><h3>Unable to update MTID details<h3></td></tr>";
  }
 }
 }catch(Exception e){ fieldDesc = "<tr><td><B><h3>MTID is not received ,Cannot Update<h3></td></tr>"; }
 
}
if(action.equalsIgnoreCase("Reject"))
{
boolean isupdated=false;
Hashtable ht=new Hashtable();
Hashtable ht1=new Hashtable();

/*ht.put("TRAVELER", TRAVELER_NO);
ht.put("MTID", MTID);
ht.put("PART01", PART01);
ht.put("LOT01", LOT01);
ht.put("QTY01", QTY01);
ht.put("REMARK", REMARK);
ht.put("CRBY","BASEER");
ht.put("CRAT", "2007-08-31");
ht.put("MOVTID", "REJECT");*/

ht.put(MDbConstant.TRAVELER_NUM,TRAVELER_NO);
ht.put(MDbConstant.PALLET,PLT);
ht.put(MDbConstant.MTID,MTID);
ht.put(MDbConstant.ITEM,PART01);
ht.put(MDbConstant.LOT_NUM,LOT01);
ht.put(MDbConstant.RECV_QTY,QTY01);
ht.put(MDbConstant.LOGIN_USER,user_id);
ht.put(MDbConstant.ReceiveStatus,"");
ht.put(MDbConstant.MOVHIS_REF_NUM,"RECV-REJECT");
ht.put(MDbConstant.MOVEHIS_CR_DATE,TRN_DATE);
ht.put(MDbConstant.CRTIME,tranTime);
ht.put(MDbConstant.MOVHIS_QTY,QTY01);
ht.put(MDbConstant.REMARK,REMARK);


ht1.put(MDbConstant.MTID,MTID);
ht1.put(MDbConstant.ITEM,PART);

isupdated=_InvMstDAO.isExists(ht1);
if(isupdated){

isupdated=recevingUtil.RejectReceiveDetail(ht);
/* if(isrecvstatus==false)
{
   fieldDesc= "<tr><td><B><h3>>MTID is already received,Unable to update details<h3></td></tr>";
   //fieldDesc= "<tr><td><B><h3>MTID is already received,Unable to update details"<h3></td></tr>";
}

else */if(isupdated){
//fieldDesc = "<tr><td><B><h3>MTID updated successfully<h3></td></tr>";
response.sendRedirect("ReceivingAprroval.jsp?HiddenView=Go&ITEM="+TRAVELER_NO+"");
}else
{
 fieldDesc = "<tr><td><B><h3>Unable to Reject MTID details<h3></td></tr>";
}
}else{

 fieldDesc = "<tr><td><B><h3>SKU not received ,cannot reject before receiving <h3></td></tr>";

}
 
 
}

if(action.equalsIgnoreCase("View"))
{

 try{
      invQryList = dao.getReceivedDetails(PLANT,LOT,MTID);
      if(invQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>Currently no Delivery No. summary found to display<h3></td></tr>";
     //  fieldDesc=fieldDesc+ITEM;
      }else{
       for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          TRAVELER_NO=(String)lineArr.get("TRAVELER");
          MTID=(String)lineArr.get("MTID");
          PART=(String)lineArr.get("SKU");
          LOT=(String)lineArr.get("LOT");
          PLT=(String)lineArr.get("PALLET");
          QTY=(String)lineArr.get("ORDQTY");
        //  REMARK=strUtils.fString((String)lineArr.get("REMARK"));
         // System.out.println("REMARK :: "+REMARK);
          QTY01=(String)lineArr.get("RECVQTY");
        
          }
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}


%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="TravellerSummary.jsp" >
  <INPUT type="hidden" name="MTID" value="<%=MTID%>">
  <INPUT type="hidden" name="HiddenView" value="Go">
  <INPUT type="hidden" name="HiddenAprove" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">DELIVERY NO. UPDATE</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <table  cellspacing="5" cellpadding="0" border="0" width="60%" align="center" BGCOLOR="#dddddd">
<tr>
    <td>Delivery No. :</td>
    <td><INPUT name="TRAVELERNO" type = "TEXT"  value="<%=TRAVELER_NO%>" size="20"  MAXLENGTH=20  readonly ></td>
    <td><INPUT name="TRAVELERNO01" type = "TEXT"  value="<%=TRAVELER_NO%>" size="20"   MAXLENGTH=20  readonly ></td>
</tr>

<tr>
    <td>Pallet No :</td>
    <td><INPUT name="PLT" type = "TEXT"  value="<%=PLT%>" size="20"  MAXLENGTH=20 readonly ></td>
    <td><INPUT name="PLT01" type = "TEXT"  value="<%=PLT%>" size="20"  MAXLENGTH=20 readonly ></td>
</tr>

<tr>
    <td>MTID No :</td>
    <td><INPUT name="MTID" type = "TEXT"  value="<%=MTID%>" size="20"  MAXLENGTH=20 readonly ></td>
    <td><INPUT name="MTID01" type = "TEXT"  value="<%=MTID%>" size="20"  MAXLENGTH=20 readonly ></td>
</tr>

<tr>
    <td>SKU No :</td>
    <td><INPUT name="SKU" type = "TEXT"  value="<%=PART%>" size="20"  MAXLENGTH=20 readonly ></td>
    <td><INPUT name="SKU01" type = "TEXT"  value="<%=PART%>" size="20"  MAXLENGTH=20 readonly ></td>
</tr>

<tr>
    <td>Lot No :</td>
    <td><INPUT name="LOTNO" type = "TEXT"  value="<%=LOT%>" size="20"  MAXLENGTH=20 readonly ></td>
    <td><INPUT name="LOTNO01" type = "TEXT"  value="<%=LOT%>" size="20"  MAXLENGTH=20 readonly ></td>
</tr>

<tr>
    <td>Quantity :</td>
    <td><INPUT name="QTY" type = "TEXT"  value="<%=QTY%>" size="20"  MAXLENGTH=20 readonly ></td>
    <td><INPUT name="QTY01" type = "TEXT"  value="<%=QTY01%>" size="20"  MAXLENGTH=20></td>
</tr>
<tr>
<TD>Remarks :</TD>
<td><INPUT name="REMARK" type = "TEXT"  value="<%=REMARK%>" size="20"  MAXLENGTH=20></td>
<TD></TD>
</tr>

</table>
<br><br>
<table width="60%"  align="center" >
<tr cellspacing="10" border="4" align="center" >
<td>
<input type="button" value="Back" onClick="window.location.href='ReceivingAprroval.jsp?HiddenView=Go&ITEM=<%=TRAVELER_NO%>'">
 <input type="button" value="Update" onClick="javascript:return onUpdate();"/>
<input type="button" value="Reject" onClick="javascript:return onReject();"/></TD>   
</tr>
</table>
</FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%> 