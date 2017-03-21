
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
function onView(){

	if(form1.ITEM.value=="" || form1.ITEM.value.length==0)
	{
		alert("Select Delivery No ");
		form1.ITEM.focus();
		return false;
	}
document.form1.HiddenView.value = "View";
   
document.form1.submit();
}

function JobAssign(){
document.form1.HiddenUpdate.value = "Update";
//alert(document.form1.Traveler_Update.value);
document.form1.action = "AssignJob.jsp?HiddenUpdate=Update";
document.form1.submit();
}

</script>
<title>Assign Job</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();

PickingUtil pUtil=new PickingUtil();
ArrayList invQryList  = new ArrayList();
MLogger logger = new MLogger();
List listline = new ArrayList(); 


String PLANT        = (String)session.getAttribute("PLANT");if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
String user_id          = session.getAttribute("LOGIN_USER").toString().toUpperCase();


String ITEM="",SKU="",LOT="",USER="",TRAVELER="",Traveler_Update="",PALLET="";
String HiddenView="",HiddenUpdate="";
String fieldDesc="";

String TRN_DATE=DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate());

ITEM    = strUtils.fString(request.getParameter("ITEM"));
PALLET    = strUtils.fString(request.getParameter("PALLET"));
SKU    = strUtils.fString(request.getParameter("SKU"));
LOT    = strUtils.fString(request.getParameter("LOT"));
USER    = strUtils.fString(request.getParameter("USER"));

HiddenView    = strUtils.fString(request.getParameter("HiddenView"));
HiddenUpdate    = strUtils.fString(request.getParameter("HiddenUpdate"));


TRAVELER    = strUtils.fString(request.getParameter("TRAVELER"));
Traveler_Update= strUtils.fString(request.getParameter("Traveler_Update"));


MLogger.log(" TRAVELER          : "+ITEM);
MLogger.log(" TRAVELER          : "+TRAVELER);
MLogger.log(" USER          : "+USER);
MLogger.log(" ITEM          : "+ITEM);
MLogger.log(" LOT          : "+LOT);
MLogger.log(" HiddenUpdate  : "+HiddenUpdate);


List list = new ArrayList();
if(TRAVELER.length() > 0)
{
//TRAVELER=TRAVELER.substring(0,TRAVELER.length()-1);
StringTokenizer parser = new StringTokenizer(TRAVELER,",");
//List list = new LinkedList();    // Doubly-linked list
   int i =0;
   
while (parser.hasMoreTokens()) {


list.add(parser.nextToken());     

}

}

MLogger.log(" TRAVELER          : "+TRAVELER);

/*if(HiddenView.equalsIgnoreCase("AssignJob")){
 try{
 
   
    invQryList = pUtil.getAssignJobDet(TRAVELER,SKU,LOT,USER);
     if(invQryList.size() < 1)
      {
    fieldDesc = "<tr><td><B><h3><centre>" +"Details Not found for Traveler :" + ITEM + "</centre><h3></td></tr>";   
    
      }
 
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString());
 
 fieldDesc = "<tr><td><B><h3><centre>" + e.toString() + "!</centre><h3></td></tr>";
 }

}*/
if(HiddenUpdate.equalsIgnoreCase("Update"))
{
try{
boolean isupdated=false;
Hashtable ht = null;
ht = new Hashtable();
 String tranTime=DateUtils.Time();
MLogger.log("ITEM"+ITEM);
MLogger.log("USER"+USER);
MLogger.log("TRN_DATE"+TRN_DATE);
MLogger.log("CRTIME"+tranTime);
MLogger.log("Traveler_Update ::"+Traveler_Update);
MLogger.log("user_id"+user_id);


ht.put(MDbConstant.TRAVELER_NUM,ITEM);
ht.put(MDbConstant.ASSIGNEDUSER,USER);

ht.put(MDbConstant.MOVHIS_REF_NUM,"ASSIGN_JOB");
ht.put(MDbConstant.MOVEHIS_CR_DATE,TRN_DATE);
ht.put(MDbConstant.MOVEHIS_CR_TIME,tranTime);
ht.put(MDbConstant.LOGIN_USER,user_id);
ht.put("Traveler_Update",Traveler_Update);


isupdated=pUtil.UpdateAssaignUser(ht);

if(isupdated){

response.sendRedirect("PickerListSummary.jsp?HiddenView=View&ITEM="+ITEM+"");
}else
{
//fieldDesc = "<tr><td><B><h3><centre>" "Unable to update User for Traveler :" + ITEM + "!</centre><h3></td></tr>";
}

}catch(Exception e){fieldDesc = "<tr><td><B><h3><centre>" + e.toString() + "!</centre><h3></td></tr>";}

 
}


%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="AssignJob.jsp" >
  
  <INPUT type="hidden" name="HiddenView" value="Go">
  <INPUT type="hidden" name="HiddenUpdate" value="">
  <INPUT type="hidden" name="Traveler_Update" value="<%=TRAVELER%>">
   <INPUT type="hidden" name="ITEM" value="<%=ITEM%>">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Assign Job</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
   <TABLE WIDTH="100%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   
	       <tr bgcolor="navy">
         
      <!--   <td width="10%"><font color="#ffffff" align="left"><center><STRONG>SINO</STRONG></center></td>-->
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>DELIVERY NO</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>PALLET</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>LOT NO</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>SKU</STRONG></center></td>
         <td width="10%"><font color="#ffffff" align="left"><center><STRONG>QTY</STRONG></center></td>
         <!--<td width="10%"><font color="#ffffff" align="left"><center><STRONG>USER</STRONG></center></td>-->
        	
	 </tr>
       <%
          for (int iCnt =0; iCnt<list.size(); iCnt++){
         // Map lineArr = (Map) list.get(iCnt);
          String strList=(String)list.get(iCnt);
           StringTokenizer parser = new StringTokenizer(strList,"||");
   // Doubly-linked list
  listline.clear(); 
while (parser.hasMoreTokens()) {
Hashtable ht = new Hashtable();
listline.add(parser.nextToken());                     
}


          int iIndex = iCnt + 1;
        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>

      
       <TR bgcolor = "<%=bgcolor%>">
           
            
             <!--<TD align="center" width="12%"><%=(String)listline.get(0)%></TD>-->
             <TD align="center" width="13%"><%=(String)listline.get(0)%></TD> <INPUT type="hidden" name="ITEM" value="<%=(String)listline.get(2)%>">
             <TD align="center" width="13%"><%=(String)listline.get(1)%></TD>
             <TD align="center" width="13%"><%=(String)listline.get(2)%></TD>
             <TD align="center" width="13%"><%=(String)listline.get(3)%></TD>
             <TD align="center" width="13%"><%=(String)listline.get(4)%></TD>
            <!-- <TD align="center" width="13%"></TD>-->
      
       
           </TR>
       <%}%>
    
    </TABLE>
    <BR><BR>

     
      <table  cellspacing="0" cellpadding="0" border="0" width="40%" align="center" bgcolor="#dddddd" >
<tr>
     <TH ALIGN="RIGHT" > &nbsp;&nbsp;Select User :</TH>
     <TD align="center"><INPUT name="USER" type = "TEXT" value="<%=USER%>" size="20"  MAXLENGTH=20>
     <a href="#" onClick="javascript:popUpWin('UserPopUp.jsp?USER='+form1.USER.value);"><img src="images/populate.gif" border="0"></a> </TD>
    </tr>
</table> 
       
    <P> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <!--   <input type="button" value="Approve" onClick="javascript:return onApprove();"/></TD> -->
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
     <TR>  <input type="button" value="Back" onClick="window.location.href='javascript:history.back()'">&nbsp
          <input type="button" value="Assign" onClick="javascript:return JobAssign();"> 
    </P>
      
  </FORM>
  
    <font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
<%@ include file="footer.jsp"%>
