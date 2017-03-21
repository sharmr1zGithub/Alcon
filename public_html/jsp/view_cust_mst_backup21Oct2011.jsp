<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>


<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Customer Master</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">


function popWin(URL) {
 subWin = window.open(URL, 'CUSTOMER', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "view_cust_mst.jsp?action=NEW";
   document.form1.submit();
}
function onAdd(){
   var SHIP_TO   = document.form1.SHIP_TO.value;
    var TYPE   = document.form1.TYPE.value;
   
   if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter/Choose Ship To"); return false; }
   if(TYPE == "" || TYPE == null) {alert("Please Enter Type "); return false; }
 
   document.form1.action  = "view_cust_mst.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
   var SHIP_TO   = document.form1.SHIP_TO.value;
    var TYPE   = document.form1.TYPE.value;
   
   if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter/Choose Ship To"); return false; }
   if(TYPE == "" || TYPE == null) {alert("Please Enter Type "); return false; }
   
   document.form1.action  = "view_cust_mst.jsp?action=UPDATE";
   document.form1.submit();
}
function onDelete(){
   var SHIP_TO   = document.form1.SHIP_TO.value;
    var TYPE   = document.form1.TYPE.value;
   
   if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter/Choose Ship To"); return false; }
   if(TYPE == "" || TYPE == null) {alert("Please Enter Type "); return false; }
 
   document.form1.action  = "view_cust_mst.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
    var SHIP_TO   = document.form1.SHIP_TO.value;
    var TYPE   = document.form1.TYPE.value;
   
   if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter/Choose Ship To"); return false; }
   if(TYPE == "" || TYPE == null) {alert("Please Enter Type "); return false; }
 
   document.form1.action  = "view_cust_mst.jsp?action=VIEW";
   document.form1.submit();
}


</script>

<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String res        = "";

String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "disabled";
String sUpdateEnb = "enabled";


String action     =   "";
String sShipTo  =   "",
       sType  = "",
       sContactName ="",
      sCompanyName ="",
        sAddr1 ="",
         sAddr2="",
          sAddr3 ="",
      sCountry ="",
      sCity="",
      sZip="",
      sPhone="",
      sFax="";


StrUtils strUtils = new StrUtils();
CustMstUtil custUtil = new CustMstUtil();
action                   = strUtils.fString(request.getParameter("action"));

sShipTo  = strUtils.fString(request.getParameter("SHIP_TO"));
if(sShipTo.length() <= 0) sShipTo  = strUtils.fString(request.getParameter("SHIP_TO1"));
sType  = strUtils.fString(request.getParameter("TYPE"));
if(sType.length() <= 0) sType  = strUtils.fString(request.getParameter("TYPE1"));

sContactName  = strUtils.fString(request.getParameter("CONTACT_NAME"));
sCompanyName  = strUtils.fString(request.getParameter("COMPANY_NAME"));
sAddr1  = strUtils.fString(request.getParameter("ADDR1"));
sAddr2  = strUtils.fString(request.getParameter("ADDR2"));
sAddr3  = strUtils.fString(request.getParameter("ADDR3"));
sCountry  = strUtils.fString(request.getParameter("COUNTRY"));
sZip  = strUtils.fString(request.getParameter("ZIP"));
sPhone  = strUtils.fString(request.getParameter("PHONE"));
sFax  = strUtils.fString(request.getParameter("FAX"));




//1. >> New
if(action.equalsIgnoreCase("NEW")){
       sShipTo ="";
       sType  = "";
       sContactName ="";
       sCompanyName ="";
       sAddr1 ="";
       sAddr2="";
       sAddr3 ="";
      sCountry ="";
      sCity="";
      sZip="";
      sPhone="";
      sFax="";
      sAddEnb    = "enabled";
     
}

else if(action.equalsIgnoreCase("ADD")){
 
     Hashtable ht = new Hashtable();
     ht.put(MDbConstant.SHIP_TO,sShipTo);
     ht.put(MDbConstant.CUST_TYPE,sType);
   if(!(custUtil.isExistsCustMst(ht))) // if the cust exists already
    {
          ht.put(MDbConstant.CONTACT_NAME,sContactName);
          ht.put(MDbConstant.COMPANY_NAME,sCompanyName);
          ht.put(MDbConstant.ADDR1,sAddr1);
          ht.put(MDbConstant.ADDR2,sAddr2);
          ht.put(MDbConstant.ADDR3,sAddr3);
          ht.put(MDbConstant.COUNTRY,sCountry);
          ht.put(MDbConstant.ZIP,sZip);
          ht.put(MDbConstant.PHONE,sPhone);
          ht.put(MDbConstant.FAX,sFax);
        
          ht.put(MDbConstant.CREATED_AT,new DateUtils().getDateTime());
          ht.put(MDbConstant.LOGIN_USER,sUserId);
          boolean Inserted = custUtil.insertCustMst(ht);
          if(Inserted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Customer  Added Successfully</font>";
                    sAddEnb  = "disabled";
                    
                    
                   mv.insertMovHisLogger(sUserId, "Customer_Master","Customer   :"  + sCompanyName +"  Added Successfully " ); 

                 
          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to add New Customer</font>";
                    sAddEnb  = "enabled";
                   
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Customer Exists already. Try again</font>";
           sAddEnb = "enabled";
          
    }
    

   //   mv.insertMovHisLogger(sUserId, "Customer_Master", res); 
    
}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
   
   sAddEnb  = "disabled";

    Hashtable ht = new Hashtable();
     ht.put(MDbConstant.SHIP_TO,sShipTo);
     ht.put(MDbConstant.CUST_TYPE,sType);
    if((custUtil.isExistsCustMst(ht)))
    {
          Hashtable htUpdate = new Hashtable();
          htUpdate.put(MDbConstant.CONTACT_NAME,sContactName);
          htUpdate.put(MDbConstant.COMPANY_NAME,sCompanyName);
          htUpdate.put(MDbConstant.ADDR1,sAddr1);
          htUpdate.put(MDbConstant.ADDR2,sAddr2);
          htUpdate.put(MDbConstant.ADDR3,sAddr3);
          htUpdate.put(MDbConstant.COUNTRY,sCountry);
          htUpdate.put(MDbConstant.ZIP,sZip);
          htUpdate.put(MDbConstant.PHONE,sPhone);
          htUpdate.put(MDbConstant.FAX,sFax);
          htUpdate.put(MDbConstant.UPDATED_AT,new DateUtils().getDateTime());
          htUpdate.put(MDbConstant.UPDATED_BY,sUserId);
          
          Hashtable htCondition = new Hashtable();
          htCondition.put(MDbConstant.SHIP_TO,sShipTo);
          htCondition.put(MDbConstant.CUST_TYPE,sType);
          boolean Updated = custUtil.updateCustMst(htUpdate,htCondition);
          if(Updated) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +" >Customer Updated Successfully</font>";
                    
                    mv.insertMovHisLogger(sUserId, "Customer_Master","Customer   :"  + sCompanyName +"  Updated Successfully " ); 

          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +" >Failed to Update Customer</font>";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Customer doesn't not Exists. Try again</font>";

    }
 //  mv.insertMovHisLogger(sUserId, "Customer_Master", res); 
}

//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
     Hashtable htDelete = new Hashtable();
    htDelete.put(MDbConstant.SHIP_TO,sShipTo);
    htDelete.put(MDbConstant.CUST_TYPE,sType);
   
    if(custUtil.isExistsCustMst(htDelete))
    {
          boolean prdGrpDeleted = custUtil.deleteCustMst(htDelete);
          if(prdGrpDeleted) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +">Customer Deleted Successfully</font>";
                    sAddEnb    = "disabled";
                    
                     mv.insertMovHisLogger(sUserId, "Customer_Master","Customer   :"  + sCompanyName +"  Deleted Successfully " ); 


                    sShipTo ="";
                    sType  = "";
                    sContactName ="";
                    sCompanyName ="";
                    sAddr1 ="";
                    sAddr2="";
                    sAddr3 ="";
                   sCountry ="";
                   sCity="";
                   sZip="";
                  sPhone="";
                  sFax="";


          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to Delete Customer</font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Customer doesn't not Exists. Try again</font>";
    }
  //   mv.insertMovHisLogger(sUserId, "Customer_Master", res); 
}

//4. >> View
else if(action.equalsIgnoreCase("VIEW")){
  
  Map map = custUtil.getCustMstDetails(sShipTo,sType);
    if (map.size()>0) {
    sContactName   = strUtils.fString((String)map.get(MDbConstant.CONTACT_NAME));
    sCompanyName   = strUtils.fString((String)map.get(MDbConstant.COMPANY_NAME));
    sAddr1   = strUtils.fString((String)map.get(MDbConstant.ADDR1));
    sAddr2   = strUtils.fString((String)map.get(MDbConstant.ADDR2));
    sAddr3   = strUtils.fString((String)map.get(MDbConstant.ADDR3));
    sCountry   = strUtils.fString((String)map.get(MDbConstant.COUNTRY));
    sZip   = strUtils.fString((String)map.get(MDbConstant.ZIP));
    sPhone   = strUtils.fString((String)map.get(MDbConstant.PHONE));
    sFax   = strUtils.fString((String)map.get(MDbConstant.FAX));
    }else{
 res = "<font class = "+MDbConstant.FAILED_COLOR +"> Customer doesn't not Exists. Try again</font>";
}
}
%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">CUSTOMER MASTER</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
    <TR>
         <TH WIDTH="33%" ALIGN="RIGHT" > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* Ship To : </TH>
         <TD width="51%">
           <INPUT name="SHIP_TO" type="TEXT" value="<%=sShipTo%>" size="30" MAXLENGTH="20"/>
           <a href="#" onClick="javascript:popWin('listView/ship_to_list.jsp?SHIP_TO='+form1.SHIP_TO.value);"><img src="images/populate.gif" border="0"></a> 
             <INPUT type = "hidden" name="SHIP_TO1" value = "">
              <INPUT type = "hidden" name="DESTINATION" value = "">
               <INPUT type = "hidden" name="TRAV_PFX" value = "">
           <INPUT class="Submit" type="BUTTON" value="View" onClick="onView();"/>
         </TD>
         </TR>
         <TR>
     <!--   <TH WIDTH="33%" ALIGN="RIGHT" > Type : </TH>
         <TD width="51%">
           <INPUT name="TYPE" type="TEXT" value="<%=sType%>" size="30" MAXLENGTH="80"/>
            <a href="#" onClick="javascript:popWin('listView/view_type_list.jsp?');"><img src="images/populate.gif" border="0"></a> 
             <INPUT type = "hidden" name="TYPE1" value = "">
         </TD>-->
          <TH WIDTH="33%" ALIGN="RIGHT" > Type : </TH>
         <TD width="51%">
          <INPUT type = "hidden" name="TYPE1" value = "">
         <p align="left">
            <SELECT NAME ="TYPE" size="1" >
            <OPTION selected value=''><- Choose ->> </OPTION>
			<option value="CONSIGNEE">CONSIGNEE</option>
       <option value="HANDLING_AGENT">HANDLING_AGENT</option>
			<option value="BILL_TO">BILL_TO</option>
			<option value="SHIP_TO">SHIP_TO</option>
			<option value="MFG_SHIPPER">MFG_SHIPPER</option>
		          </SELECT>&nbsp
            </p>
            </TD>
    </TR>
    <TR>
        <TH WIDTH="33%" ALIGN="RIGHT" >* Contact Name : </TH>
         <TD width="51%">
           <INPUT name="CONTACT_NAME" type="TEXT" value="<%=sContactName%>" size="30" MAXLENGTH="80"/>            
         </TD>
         </TR>
         <TR>
         <TH WIDTH="33%" ALIGN="RIGHT">Company Name :</TH>
         <TD width="51%">
           <INPUT name="COMPANY_NAME" type="TEXT" value="<%=sCompanyName%>" size="30" MAXLENGTH="80"/>
         </TD>

    </TR>
    <TR>
      <TH WIDTH="33%" ALIGN="RIGHT">Address :</TH>
      <TD width="51%">
        <input type="text" name="ADDR1" value="<%=sAddr1%>" maxlength="80" size="30"/>
      </TD>
      </TR>
      <TR>
      <TH WIDTH="33%" ALIGN="RIGHT">City :</TH>
      <TD width="51%">
        <INPUT name="ADDR2" type="TEXT" value="<%=sAddr2%>" size="30" MAXLENGTH="80"/>
      </TD>

    </TR>
     <TR>
       <TH WIDTH="33%" ALIGN="RIGHT">Region :</TH>
       <TD width="51%">
         <INPUT name="ADDR3" type="TEXT" value="<%=sAddr3%>" size="30" MAXLENGTH="80"/>
       </TD>
       </TR>
       <TR>
       <TH WIDTH="33%" ALIGN="RIGHT">Country :</TH>
       <TD width="51%">
         <INPUT name="COUNTRY" type="TEXT" value="<%=sCountry%>" size="30" MAXLENGTH="80"/>
       </TD>

    </TR>
     <TR>
       <TH WIDTH="33%" ALIGN="RIGHT">Zip :</TH>
       <TD width="51%">
         <INPUT name="ZIP" type="TEXT" value="<%=sZip%>" size="30" MAXLENGTH="80"/>
       </TD>
       </TR>
       <TR>
       <TH WIDTH="33%" ALIGN="RIGHT">Phone :</TH>
       <TD width="51%">
         <INPUT name="PHONE" type="TEXT" value="<%=sPhone%>" size="30" MAXLENGTH="80"/>
       </TD>

    </TR>
      <TR>
       <TH WIDTH="33%" ALIGN="RIGHT">Fax :</TH>
       <TD width="51%">
         <INPUT name="FAX" type="TEXT" value="<%=sFax%>" size="30" MAXLENGTH="80"/>
       </TD>
      

    </TR>
<TR>
         <TH WIDTH="33%" ALIGN="RIGHT" >&nbsp;</TH>
         <TD width="51%">&nbsp;
         </TD>
         

    </TR>
    <TR>
         <TD COLSPAN = 4><BR><B><CENTER><%=res%></B></TD>
    </TR>
    <TR>
         <TD COLSPAN = 4><center>
                <INPUT class="Submit" type="BUTTON" value="Cancel" onClick="window.location.href='indexPage.jsp'">&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="New" onClick="onNew();" <%=sNewEnb%>>&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="Save" onClick="onAdd();" <%=sAddEnb%>>&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="Update" onClick="onUpdate();" <%=sUpdateEnb%>>&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="Delete" onClick="onDelete();" <%=sDeleteEnb%>>
         </TD>
    </TR>
</TABLE>
</CENTER>

</FORM>
</BODY>
</HTML>
<%@ include file="footer.jsp"%>

