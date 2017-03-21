<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.db.utils.*"%>
<%//@ page import="com.murho.gates.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>
<link rel="stylesheet" href="css/style.css">

<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Customer Master</title>


<SCRIPT LANGUAGE="JavaScript">


function popWin(URL) {
 subWin = window.open(URL, 'CUSTOMER', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onNew(){
   // modifed by Arun for #1848
   //document.form1.action  = "view_cust_mst.jsp?action=NEW";
   document.form1.action  = "view_cust_mst.jsp?action=NEW&TYPE=<- Choose ->>";
   document.form1.submit();
}
function onAdd(){
   var SHIP_TO   = document.form1.SHIP_TO.value;
    var TYPE   = document.form1.TYPE.value;
   
   if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter/Choose Ship To"); return false; }
   if(TYPE == "" || TYPE == null || TYPE == '<- Choose ->>') {alert("Please Enter Type "); return false; }
 
   document.form1.action  = "view_cust_mst.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
   var SHIP_TO   = document.form1.SHIP_TO.value;
    var TYPE   = document.form1.TYPE.value;
   
   if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter/Choose Ship To"); return false; }
   if(TYPE == "" || TYPE == null || TYPE == '<- Choose ->>') {alert("Please Enter Type "); return false; }
   
   document.form1.action  = "view_cust_mst.jsp?action=UPDATE";
   document.form1.submit();
}
function onDelete(){
   var SHIP_TO   = document.form1.SHIP_TO.value;
    var TYPE   = document.form1.TYPE.value;
   
   if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter/Choose Ship To"); return false; }
   if(TYPE == "" || TYPE == null || TYPE == '<- Choose ->>') {alert("Please Enter Type "); return false; }
 
   document.form1.action  = "view_cust_mst.jsp?action=DELETE";
   document.form1.submit();
}
function onView(){
    var SHIP_TO   = document.form1.SHIP_TO.value;
    var TYPE   = document.form1.TYPE.value;
   
   if(SHIP_TO == "" || SHIP_TO == null) {alert("Please Enter/Choose Ship To"); return false; }
   if(TYPE == "" || TYPE == null || TYPE == '<- Choose ->>') {alert("Please Enter Type "); return false; }
 
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
      //sCity="",
      sZip="",
      sPhone="",
      sFax="";
//StringBuffer strB = new StringBuffer("");


//StrUtils strUtils = new StrUtils();
CustMstUtil custUtil = new CustMstUtil();
action                   = StrUtils.fString(request.getParameter("action"));

com.murho.gates.TableList tl = new com.murho.gates.TableList();


sShipTo  = StrUtils.fString(request.getParameter("SHIP_TO"));

if(sShipTo.length() <= 0) sShipTo  = StrUtils.fString(request.getParameter("SHIP_TO1"));
sType  = StrUtils.fString(request.getParameter("TYPE"));
if(sType.length() <= 0) sType  = StrUtils.fString(request.getParameter("TYPE1"));

// below lines commented by Arun for #1848 for handling quotes issue
//sContactName  = StrUtils.fString(request.getParameter("CONTACT_NAME"));
//sCompanyName  = StrUtils.fString(request.getParameter("COMPANY_NAME"));
//sAddr1  = StrUtils.fString(request.getParameter("ADDR1"));
//sAddr2  = StrUtils.fString(request.getParameter("ADDR2"));
//sAddr3  = StrUtils.fString(request.getParameter("ADDR3"));
//sCountry  = StrUtils.fString(request.getParameter("COUNTRY"));
//sZip  = StrUtils.fString(request.getParameter("ZIP"));
//sPhone  = StrUtils.fString(request.getParameter("PHONE"));
//sFax  = StrUtils.fString(request.getParameter("FAX"));


// below lines added by Arun for #1848 for handling quotes issue

//sContactName   =  request.getParameter("CONTACT_NAME");
//	if (null != sContactName){
//		sContactName = StrUtils.forHTMLTag(sContactName);
//}else{
//	sContactName = "";
//}
//sCompanyName   = request.getParameter("COMPANY_NAME");
//if(null != sCompanyName){
//	sCompanyName = StrUtils.formatForDoubleQuote(sCompanyName);
//}else{
//	sCompanyName = "";
//}










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
      //sCity="";
      sZip="";
      sPhone="";
      sFax="";
      sAddEnb    = "enabled";
      
     
}

else if(action.equalsIgnoreCase("ADD")){
 
     Hashtable addHt = new Hashtable();
     
     // Below block added by Arun for #1848
     //------------------------------------------------
     String formattedShipTo = "";
     if (null != sShipTo && "" != sShipTo){
    	 formattedShipTo = tl.formatString(sShipTo);
     }
     
     //ht.put(MDbConstant.SHIP_TO,sShipTo);
     addHt.put(MDbConstant.SHIP_TO,formattedShipTo);
     //---------------------------
     addHt.put(MDbConstant.CUST_TYPE,sType);
   //if(!(custUtil.isExistsCustMst(ht))) // if the cust exists already
   if(!(custUtil.isExistsCustMst(addHt)))
    {
	   
	 //  below lines added by Arun for #1848 
	 sContactName  = request.getParameter("CONTACT_NAME");
	// out.print("sContactName"+sContactName);
	 sCompanyName  = request.getParameter("COMPANY_NAME");
	 sAddr1  = request.getParameter("ADDR1");
	 sAddr2  = request.getParameter("ADDR2");
	 sAddr3  = request.getParameter("ADDR3");
	 sCountry  =request.getParameter("COUNTRY");
	 sZip  = request.getParameter("ZIP");
	 sPhone  = request.getParameter("PHONE");
	 sFax  = request.getParameter("FAX");

	 Hashtable ht = new Hashtable();
	      ht.put(MDbConstant.SHIP_TO,sShipTo);
	      ht.put(MDbConstant.CUST_TYPE,sType);
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
                    
//                  Below if condition added by Arun for #1848
                    if(null !=sCompanyName){
                    	sCompanyName = StrUtils.forHTMLTag(sCompanyName);
                    }
                   mv.insertMovHisLogger(sUserId, "Customer_Master","Customer   :"  + sCompanyName +"  Added Successfully " ); 
//                 call added by Arun for #1848
                   response.sendRedirect("view_cust_mst.jsp?action=VIEW&SHIP_TO="+sShipTo+"&TYPE="+sType+"&newFlag=true");

                 
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
	sContactName   = request.getParameter("CONTACT_NAME");
	sCompanyName   = request.getParameter("COMPANY_NAME");
	sAddr1   = request.getParameter("ADDR1");
	sAddr2   = request.getParameter("ADDR2");
	sAddr3   = request.getParameter("ADDR3");
	sCountry   = request.getParameter("COUNTRY");
	sZip   = request.getParameter("ZIP");
	sPhone   = request.getParameter("PHONE");
	sFax   = request.getParameter("FAX");
   sAddEnb  = "disabled";

    Hashtable ht = new Hashtable();

    // Below block added by Arun for #1848
    //------------------------------------------------
    String formattedShipTo = "";
    if (null != sShipTo && "" != sShipTo){
   	 formattedShipTo = tl.formatString(sShipTo);
    }
    
    //ht.put(MDbConstant.SHIP_TO,sShipTo);
    ht.put(MDbConstant.SHIP_TO,formattedShipTo);
    //---------------------------
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
          //htCondition.put(MDbConstant.SHIP_TO,sShipTo);
          htCondition.put(MDbConstant.SHIP_TO,formattedShipTo);
          htCondition.put(MDbConstant.CUST_TYPE,sType);
          boolean Updated = custUtil.updateCustMst(htUpdate,htCondition);
          if(Updated) {
                    res = "<font class = "+MDbConstant.SUCCESS_COLOR +" >Customer Updated Successfully</font>";
                    
                    // Below if condition added by Arun for #1848
                    if(null !=sCompanyName){
                    	sCompanyName = StrUtils.forHTMLTag(sCompanyName);
                    }
                    mv.insertMovHisLogger(sUserId, "Customer_Master","Customer   :"  + sCompanyName +"  Updated Successfully " ); 
                    
                    // call added by Arun for #1848
                    response.sendRedirect("view_cust_mst.jsp?action=VIEW&SHIP_TO="+sShipTo+"&TYPE="+sType+"&updateFlag=true");

          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +" >Failed to Update Customer</font>";
          }
    }else{
           //res = "<font class = "+MDbConstant.FAILED_COLOR +">Customer doesn't not Exists. Try again</font>";
    	res = "<font class = "+MDbConstant.FAILED_COLOR +">Customer does not exist.</font>";

    }
 //  mv.insertMovHisLogger(sUserId, "Customer_Master", res); 
}

//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
     Hashtable htDelete = new Hashtable();
 
     // Below block added by Arun for #1848
     //------------------------------------------------
     String formattedShipTo = "";
     if (null != sShipTo && "" != sShipTo){
    	 formattedShipTo = tl.formatString(sShipTo);
     }
     
     //htDelete.put(MDbConstant.SHIP_TO,sShipTo);
     htDelete.put(MDbConstant.SHIP_TO,formattedShipTo);
     //---------------------------
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
                  // sCity="";
                   sZip="";
                  sPhone="";
                  sFax="";


          } else {
                    res = "<font class = "+MDbConstant.FAILED_COLOR +">Failed to Delete Customer</font>";
                    sAddEnb = "enabled";
          }
    }else{
           //res = "<font class = "+MDbConstant.FAILED_COLOR +">Customer doesn't not Exists. Try again</font>";
           res = "<font class = "+MDbConstant.FAILED_COLOR +">Customer does not exist.</font>";
    }
  //   mv.insertMovHisLogger(sUserId, "Customer_Master", res); 
}

//4. >> View
else if(action.equalsIgnoreCase("VIEW")){
  
//	 Below block added by Arun for #1848
    //------------------------------------------------
    String formattedShipTo = "";
    if (null != sShipTo && "" != sShipTo){
   	 formattedShipTo = tl.formatString(sShipTo);
    }
    
  //Map map = custUtil.getCustMstDetails(sShipTo,sType);
  Map map = custUtil.getCustMstDetails(formattedShipTo,sType);
  //---------------------------------------------------
  
    if (map.size()>0) {
    	// Commented by Arun for #1848 to accept and display special characters including double quotes 
    //sContactName   = strUtils.fString((String)map.get(MDbConstant.CONTACT_NAME));
    //sCompanyName   = strUtils.fString((String)map.get(MDbConstant.COMPANY_NAME));
    //sAddr1   = strUtils.fString((String)map.get(MDbConstant.ADDR1));
    //sAddr2   = strUtils.fString((String)map.get(MDbConstant.ADDR2));
    //sAddr3   = strUtils.fString((String)map.get(MDbConstant.ADDR3));
    //sCountry   = strUtils.fString((String)map.get(MDbConstant.COUNTRY));
    //sZip   = strUtils.fString((String)map.get(MDbConstant.ZIP));
    //sPhone   = strUtils.fString((String)map.get(MDbConstant.PHONE));
    //sFax   = strUtils.fString((String)map.get(MDbConstant.FAX));
    
    // Added by Arun for #1848 to accept and display special characters including double quotes 

   	sShipTo   = (String)map.get(MDbConstant.SHIP_TO);
      	if (null != sShipTo){
      		sShipTo = StrUtils.forHTMLTag(sShipTo);
       }else{
    	   sShipTo = "";
       }
       	
   	sContactName   = (String)map.get(MDbConstant.CONTACT_NAME);
   	if (null != sContactName){
   		sContactName = StrUtils.forHTMLTag(sContactName);
    }else{
    	sContactName = "";
    }
   	
   	sCompanyName   = (String)map.get(MDbConstant.COMPANY_NAME);
   	if (null != sCompanyName){
   		sCompanyName = StrUtils.forHTMLTag(sCompanyName);
    }else{
    	sCompanyName = "";
    }
   	
   
    sAddr1   = (String)map.get(MDbConstant.ADDR1);
    if (null != sAddr1){
    	sAddr1 = StrUtils.forHTMLTag(sAddr1);
    }else{
    	sAddr1 = "";
    }
    
    sAddr2   = (String)map.get(MDbConstant.ADDR2);
    if (null != sAddr2){
    	sAddr2 = StrUtils.forHTMLTag(sAddr2);
    }else{
    	sAddr2 = "";
    }
    
    
	sAddr3   = (String)map.get(MDbConstant.ADDR3);
	if (null != sAddr3){
	    sAddr3 = StrUtils.forHTMLTag(sAddr3);
	}else{
	    	sAddr3 = "";
	}
    
    
	sCountry   = (String)map.get(MDbConstant.COUNTRY);
	if (null != sCountry){
	    sCountry = StrUtils.forHTMLTag(sCountry);
	}else{
	    sCountry = "";
    }
    
	sZip   = (String)map.get(MDbConstant.ZIP);
	if (null != sZip){
	    sZip = StrUtils.forHTMLTag(sZip);
	    
    }else{
    	sZip = "";
    }
    
    sPhone   = (String)map.get(MDbConstant.PHONE);
    if (null != sPhone){
	    sPhone = StrUtils.forHTMLTag(sPhone);
    }else{
	    	sPhone = "";
    }
    
    
	sFax   = (String)map.get(MDbConstant.FAX);
	if (null != sFax){
	    sFax = StrUtils.forHTMLTag(sFax);
	}else{
	    	sFax = "";
    }
    
    
    String updateFlag = request.getParameter("updateFlag");
    String newFlag = request.getParameter("newFlag");    
    if(null != updateFlag && updateFlag.equalsIgnoreCase("true")){
	    res = "<font class = "+MDbConstant.SUCCESS_COLOR +" >Customer Updated Successfully</font>";
    }
    if(null != newFlag && newFlag.equalsIgnoreCase("true")){
	    res = "<font class = "+MDbConstant.SUCCESS_COLOR +" >Customer Added Successfully</font>";
    }
    
    }else{
 //res = "<font class = "+MDbConstant.FAILED_COLOR +"> Customer doesn't not Exists. Try again</font>";
 res = "<font class = "+MDbConstant.FAILED_COLOR +"> Customer does not exist.</font>";
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
          <TH WIDTH="33%" ALIGN="RIGHT" >* Type : </TH>
         <TD width="51%">
          <INPUT type = "hidden" name="TYPE1" value = "">
         <p align="left">
            <SELECT NAME ="TYPE" size="1" >
            <!-- Added by Arun for #1848 -->
            <%if (null != request.getParameter("TYPE") && request.getParameter("TYPE").equalsIgnoreCase("CONSIGNEE")){ %>
				<option selected value="CONSIGNEE">CONSIGNEE</option>
			<%} else{%>
			   <OPTION selected value='<- Choose ->>'><- Choose ->> </OPTION>
			   <option value="CONSIGNEE">CONSIGNEE</option>
			<%} %>
			<%if (null != request.getParameter("TYPE") && request.getParameter("TYPE").equalsIgnoreCase("HANDLING_AGENT")){ %>
	            <option selected value="HANDLING_AGENT">HANDLING_AGENT</option>
            <%} else{%>
			   <option value="HANDLING_AGENT">HANDLING_AGENT</option>
			<%} %>
            <%if (null != request.getParameter("TYPE") && request.getParameter("TYPE").equalsIgnoreCase("BILL_TO")){ %>
				<option selected value="BILL_TO">BILL_TO</option>
			<%} else{%>
			   <option value="BILL_TO">BILL_TO</option>
			<%} %>
			<%if (null != request.getParameter("TYPE") && request.getParameter("TYPE").equalsIgnoreCase("SHIP_TO")){ %>
				<option selected value="SHIP_TO">SHIP_TO</option>
			<%} else{%>
			   <option value="SHIP_TO">SHIP_TO</option>
			<%} %>
			<%if (null != request.getParameter("TYPE") && request.getParameter("TYPE").equalsIgnoreCase("MFG_SHIPPER")){ %>
				<option selected value="MFG_SHIPPER">MFG_SHIPPER</option>
			<%} else{%>
			   <option value="MFG_SHIPPER">MFG_SHIPPER</option>
			<%} %>
		          </SELECT>&nbsp
            </p>
            </TD>
    </TR>
 
    <TR>
        <!-- for #1848 <TH WIDTH="33%" ALIGN="RIGHT" >* Contact Name : </TH> -->
        <TH WIDTH="33%" ALIGN="RIGHT" >Contact Name : </TH>
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
         <INPUT name="ADDR3" type="TEXT" value="<%=sAddr3%>"  size="30" MAXLENGTH="80"/>
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
       <!-- Modifed by Arun for #1848 : DB size is 20 
         <INPUT name="ZIP" type="TEXT" value="<%=sZip%>" size="30" MAXLENGTH="80"/>-->
          <INPUT name="ZIP" type="TEXT" value="<%=sZip%>" size="30" MAXLENGTH="20"/>
       </TD>
       </TR>
       <TR>
       <TH WIDTH="33%" ALIGN="RIGHT">Phone :</TH>
       <TD width="51%">
       <!-- Modifed by A4un for #1848 : DB size is 50 
         <INPUT name="PHONE" type="TEXT" value="<%=sPhone%>" size="30" MAXLENGTH="80"/>-->
         <INPUT name="PHONE" type="TEXT" value="<%=sPhone%>" size="30" MAXLENGTH="50"/>
       </TD>

    </TR>
      <TR>
       <TH WIDTH="33%" ALIGN="RIGHT">Fax :</TH>
       <TD width="51%">
       <!-- Modifed by Arun for #1848 : DB size is 50 
         <INPUT name="FAX" type="TEXT" value="<%=sFax%>" size="30" MAXLENGTH="80"/>-->
          <INPUT name="FAX" type="TEXT" value="<%=sFax%>" size="30" MAXLENGTH="50"/>
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

