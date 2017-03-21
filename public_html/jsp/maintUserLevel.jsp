<%@ include file="header.jsp" %>
<title>GROUP Maintenance</title>
<link rel="stylesheet" href="css/style.css">
<script language="JavaScript" type="text/javascript" src="js/maintGroup.js"></script>
<%@ include file="body.jsp" %>
<jsp:useBean id="sl"  class="com.murho.gates.selectBean" />
<jsp:useBean id="ub"  class="com.murho.gates.userBean" />
<%! ArrayList al;
//          Method for selecting the CheckBox as 'Checked'
       public String checkOption(String str)
                {
                    for(int i=0; i< al.size();i++)
                    {
                    if(str.equalsIgnoreCase(al.get(i).toString())) return "checked";
                    }
                    return "";
                }
%>
<%
        String caption = "Maintain";
        String delParam="";
        String disabled="";
        String disabledInView="";

        Enumeration e = request.getParameterNames();
        while(e.hasMoreElements())
        {
            String s = e.nextElement().toString();

            if(s.equalsIgnoreCase("view"))
                {
                     caption = "View";
                     disabledInView = "disabled";
                }

            else if(s.equalsIgnoreCase("del"))
                {
                     caption = "Delete";
                     delParam = "<input type=\"Hidden\" name=\"del\" value=\"\">"; //  To Indicate the delete function
                     disabled = "disabled";
                }
        }
%>
<FORM METHOD="post" action="maintUserLevel.jsp">
  <br>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">MAINTAIN GROUPS</font>
  </table>
  <br>
<%=delParam%>
<table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
  <tr>
    <td width="100%">&nbsp;
      <font face="Times New Roman" size="2">
      <center>
        <table border="0" width="90%">
          <tr>
            <td width="100%">
              <CENTER>
                <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
                  <TR>
                    <TH WIDTH="18%" >Group
                    <TD>
                      <SELECT NAME ="LEVEL_NAME" size="1" <%=disabledInView%>>
                        <OPTION selected value=''>< -- Choose -- > </OPTION>
                        <%=sl.getUserLevels("1")%>
                      </SELECT>
                      &nbsp;
                      <input type="submit" value="Go" <%=disabledInView%>>
                      &nbsp;
                      <input type="Button" value="Cancel" <%=disabledInView%> onClick ="window.location.href='indexPage.jsp'">
                    </TD>
                  </TR>
                </TABLE>
                <%
    String level_name = request.getParameter("LEVEL_NAME");

    if (level_name.length()<1)
    {
        out.write("<br><table width=\"100%\"><tr><td align=\"center\"><b>Please Select a GROUP</b></td></tr></table></FORM>");
    }
    else
    {
         al  = ub.getUserLevelLinks(level_name);

            //String authorise_by = al.get(al.size()-2).toString();
            String authorise_by = al.get(al.size()-4).toString();
            if((authorise_by==null) || (authorise_by.length()<=1)) authorise_by="Not Authorised";
            //below line is added on 4-july-2014 for user group active/inactive ticket #WO0000000041526
            String old_status = al.get(al.size()-2).toString().trim();
%></FORM>

  <FORM name="levelForm" method="POST" action="userLevelSubmit.jsp" onSubmit="return validateGroup(document.levelForm)">
	    <INPUT type="Hidden" name="LEVEL_NAME" value="<%=level_name%>">
        <P><font face="Verdana" color="blue" size="2"> <b>GROUPS</b></font><font face="Verdana" color="black" size="2"><b>
        - <%=new String(level_name).toUpperCase()%></b></font> <BR><BR>
          <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
            <TR>
             <TH BGCOLOR="#000066" COLSPAN="3"><font color="white">USER ADMINISTRATION</font>
            <TR>
             <TH WIDTH="25%" ALIGN = "LEFT">
             <INPUT Type=Checkbox style="border:0;background=#dddddd" name="URL" value="uaNewAcct" <%=disabled%> <%=checkOption("uaNewAcct")%>>
             &nbsp; Create New Group
             <TH WIDTH="25%" ALIGN = "LEFT">
             <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaMaintAcct" <%=disabled%> <%=checkOption("uaMaintAcct")%>>
             &nbsp; Maintain Group
             <TH WIDTH="25%" ALIGN = "LEFT">
             <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaDelAcct" <%=disabled%> <%=checkOption("uaDelAcct")%>>
             &nbsp; Delete Group
            <TR>
             <TH WIDTH="25%" ALIGN = "LEFT">
             <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaNewLevel" <%=disabled%> <%=checkOption("uaNewLevel")%>>
             &nbsp; Create New User
             <TH WIDTH="25%" ALIGN = "LEFT">
             <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaMaintLevel" <%=disabled%> <%=checkOption("uaMaintLevel")%>>
             &nbsp; Maintain User
             <TH WIDTH="25%" ALIGN = "LEFT">
             <input Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaDelLevel" <%=disabled%> <%=checkOption("uaDelLevel")%>>
             &nbsp; Delete User
            <TR>
             <TH WIDTH="25%" ALIGN = "LEFT">
             <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaChngPwd" <%=disabled%> <%=checkOption("uaChngPwd")%>>
             &nbsp; Change Password
             <TH WIDTH="25%" ALIGN = "LEFT">
             <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="auUserLevel" <%=disabled%> <%=checkOption("auUserLevel")%>>
             &nbsp; Authorise Group
             <TH WIDTH="25%" ALIGN = "LEFT">
             <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="auUserAcct" <%=disabled%> <%=checkOption("auUserAcct")%>>
             &nbsp; Authorise User
            <TR>
             <TH WIDTH="25%" ALIGN = "LEFT">
             <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="renewPwd" <%=disabled%> <%=checkOption("renewPwd")%>>
             &nbsp; Reset Password
             <!-- Added by Arun for Login Synchronization change on 29 June 2011 :#25046-->
           	 <TH WIDTH="25%" ALIGN = "LEFT">
             <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="setSystemParams" <%=disabled%> <%=checkOption("setSystemParam")%>>
             &nbsp; Set System Parameters
             <!-- end -->
          </TABLE>
                  <BR>
                  <BR>
          <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
             <TR>
               <TH BGCOLOR="#000066" COLSPAN="3"><font color="white">SYSTEM ADMINISTRATION</font>
             <TR>
               <TH WIDTH="25%" ALIGN = "LEFT">
               <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msLocAsgnRule" <%=disabled%> <%=checkOption("msLocAsgnRule")%>>
               &nbsp; Location Assign Rule
               <TH WIDTH="25%" ALIGN = "LEFT">
               <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msRules" <%=disabled%> <%=checkOption("msRules")%>>
               &nbsp; Palletizing Rules 
               <TH WIDTH="25%" ALIGN = "LEFT">
               <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msRulesSummary" <%=disabled%> <%=checkOption("msRulesSummary")%>>
               &nbsp; Palletizing Summary
             <TR>
               <TH WIDTH="25%" ALIGN = "LEFT">
               <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="tblControl" <%=disabled%> <%=checkOption("tblControl")%>>
               &nbsp; Table Control
          </TABLE>
                  <BR>
                  <BR>
          <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
              <TR>
                <TH BGCOLOR="#000066" COLSPAN="3"><font color="white">MASTER</font>
              <TR>
                <TH WIDTH="25%" ALIGN = "LEFT">
                <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msItemMst" <%=disabled%> <%=checkOption("msItemMst")%>>
                &nbsp; Item Master
                <TH WIDTH="25%" ALIGN = "LEFT">
                <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msProdGroup" <%=disabled%> <%=checkOption("msProdGroup")%>>
                &nbsp; Product Group
                <TH WIDTH="25%" ALIGN = "LEFT">
                <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msLocMst" <%=disabled%> <%=checkOption("msLocMst")%>>
                &nbsp; Location Master
              <TR>
                <TH WIDTH="25%" ALIGN = "LEFT">
                <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msLocGrp" <%=disabled%> <%=checkOption("msLocGrp")%>>
                &nbsp; Location group 
                <TH WIDTH="25%" ALIGN = "LEFT">
                <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msPrdCls" <%=disabled%> <%=checkOption("msPrdCls")%>>
                &nbsp; Product Class
                <TH WIDTH="25%" ALIGN = "LEFT">
                <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msDestination" <%=disabled%> <%=checkOption("msDestination")%>>
                &nbsp; Destination Master
              <TR>
                <TH WIDTH="25%" ALIGN = "LEFT">
                <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msPalletGroup" <%=disabled%> <%=checkOption("msPalletGroup")%>>
                &nbsp; Pallet Group
                <TH WIDTH="25%" ALIGN = "LEFT">
                <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msTrayGrp" <%=disabled%> <%=checkOption("msTrayGrp")%>>
                &nbsp; Tray Group Master
                <TH WIDTH="25%" ALIGN = "LEFT">
                <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="custMst" <%=disabled%> <%=checkOption("custMst")%>>
                &nbsp;  Customer Master
              <TR>
                <TH WIDTH="25%" ALIGN = "LEFT">
                <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="rsnmst" <%=disabled%> <%=checkOption("rsnmst")%>>
                &nbsp; Reason Master
                
                 <%--Added by Ranjana for the functionality of LOT Restriction under ticket WO0000000284867--%>                
                <TH WIDTH="25%" ALIGN = "LEFT">
                <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="lotrestriction" <%=disabled%> <%=checkOption("lotrestriction")%>>
                &nbsp; LOT Restriction
                
                <%--Added by Ranjana for the functionality of UDI implementation under ticket WO0000000471852--%>                
                <TH WIDTH="25%" ALIGN = "LEFT">
                <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="itmupload" <%=disabled%> <%=checkOption("itmupload")%>>
                &nbsp; Item Upload
                
                <TR>
                 <%--Added by Ranjana for the functionality of System Blocking--%>                
                <TH WIDTH="25%" ALIGN = "LEFT">
                <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="blocklot" <%=disabled%> <%=checkOption("blocklot")%>>
                &nbsp; System Blocking
                
                 <%--Added by Ranjana for the functionality of DSL--%>                
                <TH WIDTH="25%" ALIGN = "LEFT">
                <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="dsl" <%=disabled%> <%=checkOption("dsl")%>>
                &nbsp; DSL
                
          </TABLE>
                  <BR>
                  <BR>
          <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
              <TR>
               <TH BGCOLOR="#000066" COLSPAN="3"><font color="white">INBOUND</font>
              <TR>
               <TH WIDTH="25%" ALIGN = "LEFT">
               <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="import" <%=disabled%> <%=checkOption("import")%>>
               &nbsp; Import Delivery No.
               <TH WIDTH="25%" ALIGN = "LEFT">
               <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="travelerapprv" <%=disabled%> <%=checkOption("travelerapprv")%>>
               &nbsp;Delivery No. Summary
               <TH WIDTH="25%" ALIGN = "LEFT">
               <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="recapprove" <%=disabled%> <%=checkOption("recapprove")%>>
               &nbsp; Receiving Approval
             <TR>
             <TR>
               <TH WIDTH="25%" ALIGN = "LEFT">
               <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="putAwaySummList" <%=disabled%> <%=checkOption("putAwaySummList")%>>
               &nbsp; PutAway Summary
          </TABLE>
                  <BR>
                  <BR>
          <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
              <TR>
                <TH BGCOLOR="#000066" COLSPAN="3"><font color="white">OUTBOUND</font>
              <TR>
                <TH WIDTH="25%" ALIGN = "LEFT">
                <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="importso" <%=disabled%> <%=checkOption("importso")%>>
                &nbsp; Import SO
                <TH WIDTH="25%" ALIGN = "LEFT">
                <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="obTravelerSummary" <%=disabled%> <%=checkOption("obTravelerSummary")%>>
                &nbsp;SO Summary
                <TH WIDTH="25%" ALIGN = "LEFT">
                <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pickerHis" <%=disabled%> <%=checkOption("pickerHis")%>>
                &nbsp; Pick List History
              <TR>
              <TR>
                 <TH WIDTH="25%" ALIGN = "LEFT">
                 <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msPickListSummary" <%=disabled%> <%=checkOption("msPickListSummary")%>>
                 &nbsp; Pick List Summary
                 <TH WIDTH="25%" ALIGN = "LEFT">
                 <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="removeDeliveryNo" <%=disabled%> <%=checkOption("removeDeliveryNo")%>>
                 &nbsp; Remove Delivery No.
                 <TH WIDTH="25%" ALIGN = "LEFT">
                 <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="trayLabelingSummary" <%=disabled%> <%=checkOption("trayLabelingSummary")%>>
                 &nbsp; Tray Label Summary
          </TABLE>
                 <BR>
                 <BR>
          <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
                <TR>
                 <TH BGCOLOR="#000066" COLSPAN="3"><font color="white">SHIPPING</font>
	     	    <TR>
                 <TH WIDTH="25%" ALIGN = "LEFT">
                 <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="Shipping" <%=disabled%> <%=checkOption("Shipping")%>>
                 &nbsp; Global Shipping
                 <TH WIDTH="25%" ALIGN = "LEFT">
                 <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msImportShipping" <%=disabled%> <%=checkOption("msImportShipping")%>>
                 &nbsp; Import ZShipping9
                 <TH WIDTH="25%" ALIGN = "LEFT">
                 <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="iscAddPallet" <%=disabled%> <%=checkOption("iscAddPallet")%>>
                 &nbsp;ISC Add Pallet
                <TR>
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="iscShipping" <%=disabled%> <%=checkOption("iscShipping")%>>
                  &nbsp;ISC Shipping
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printShippingMark" <%=disabled%> <%=checkOption("printShippingMark")%>>
                  &nbsp;Print Shipping Mark
           </TABLE>
                  <BR>
                  <BR>
           <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
                 <TR>
                  <TH BGCOLOR="#000066" COLSPAN="3"><font color="white">REPORTS</font>
                 <TR>
                   <TH WIDTH="25%" ALIGN = "LEFT">
                   <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="rptStVsINV" <%=disabled%> <%=checkOption("rptStVsINV")%>>
                   &nbsp; Stock Take
                   <TH WIDTH="25%" ALIGN = "LEFT">
                   <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msInvList" <%=disabled%> <%=checkOption("msInvList")%>>
                   &nbsp; Inventory Enquiry
                   <TH WIDTH="25%" ALIGN = "LEFT">
                   <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msPOList" <%=disabled%> <%=checkOption("msPOList")%>>
                   &nbsp; Movement History
                 <TR>
                   <TH WIDTH="25%" ALIGN = "LEFT">
                   <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="mstrayDescrepency" <%=disabled%> <%=checkOption("mstrayDescrepency")%>>
                   &nbsp;Discrepancy Report
                   
                    <!-- For adding inbound discrepancy report -->
                   <TH WIDTH="50%" ALIGN = "LEFT">
                   <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="inboundDiscrepancy" <%=disabled%> <%=checkOption("inboundDiscrepancy")%>>
                   &nbsp;InboundDiscrepancy Report
                 
                   <TH WIDTH="25%" ALIGN = "LEFT">
                   <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="rptStkTransfer" <%=disabled%> <%=checkOption("rptStkTransfer")%>>
                   &nbsp;Stock Transfer 
                 <TR>
                   <TH WIDTH="25%" ALIGN = "LEFT">
                   <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="rptStkAdj" <%=disabled%> <%=checkOption("rptStkAdj")%>>
                   &nbsp;Stock Adjustment
                     
                   <TH WIDTH="25%" ALIGN = "LEFT">
                   <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="qcreport" <%=disabled%> <%=checkOption("qcreport")%>>
                   &nbsp;Delivery No. Report 
                   
                   <TH WIDTH="25%" ALIGN = "LEFT">
                   <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="lotmovhis" <%=disabled%> <%=checkOption("lotmovhis")%>>
                   &nbsp;LotRestriction MovementHistory
                   
                  </TABLE>
                  <BR>
                  <BR>
                  <BR>
           <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
                <TR>
                  <TH BGCOLOR="#000066" COLSPAN="3"><font color="white">PDA -  MENU</font>
                <TR>
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaRecv" <%=disabled%> <%=checkOption("pdaRecv")%>>
                  &nbsp; Receving
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaPutAway" <%=disabled%> <%=checkOption("pdaPutAway")%>>
                  &nbsp; PutAway
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaBlindRcpt" <%=disabled%> <%=checkOption("pdaBlindRcpt")%>>
                  &nbsp; Blind Receipt In
			    <TR>
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaPicking" <%=disabled%> <%=checkOption("pdaPicking")%>>
                  &nbsp; Picking
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaPickOut" <%=disabled%> <%=checkOption("pdaPickOut")%>>
                  &nbsp; PickOut
                <TR>
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaStkCount" <%=disabled%> <%=checkOption("pdaStkCount")%>>
                  &nbsp; Stock Count
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaStkTransfer" <%=disabled%> <%=checkOption("pdaStkTransfer")%>>
                  &nbsp; Stock Transfer
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaQuery" <%=disabled%> <%=checkOption("pdaQuery")%>>
                  &nbsp; Query Inventory
				<TR>
                  <!-- Added by Arun for Login Synchronization change on 15 July 2011 :#25046-->
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaChangePassword" <%=disabled%> <%=checkOption("pdaChangePassword")%>>
                  &nbsp; Change Password
                <TR>
                   <!-- end -->
            </TABLE>
            <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
                <TR>
                 <TH BGCOLOR="#000066" COLSPAN="3"><font color="white">LABEL PRINTING</font>
                <TR>
                 <!-- Added by Arun for Login Synchronization change on 15 July 2011 :#25046-->
               	 <TH WIDTH="25%" ALIGN = "LEFT">
                 <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printChangePassword" <%=disabled%> <%=checkOption("printChangePassword")%>>
                 &nbsp; CHANGE PASSWORD
                <TR>
                  <!-- end -->
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printconfig" <%=disabled%> <%=checkOption("printconfig")%>>
                  &nbsp; PRINT_CONFIG
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printmtid" <%=disabled%> <%=checkOption("printmtid")%>>
                  &nbsp; RE-PRINT MTID
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printtraylabel" <%=disabled%> <%=checkOption("printtraylabel")%>>
                  &nbsp; TRAY_LABELING
                <TR>
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printGlobalShip" <%=disabled%> <%=checkOption("printGlobalShip")%>>
                  &nbsp; GLOBAL SHIPPING
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printIscShip" <%=disabled%> <%=checkOption("printIscShip")%>>
                  &nbsp; ISC SHIPPING
                    
                  <%--Added by Ranjana for the functionality of generating inbound file under ticket WO0000000356180--%>
                  
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printgenInboundFile" <%=disabled%> <%=checkOption("printgenInboundFile")%>>
                  &nbsp; GENERATE_INBOUND_FILE
				<TR>
				  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printtrayid" <%=disabled%> <%=checkOption("printtrayid")%>>
                  &nbsp; RE-PRINT TRAYID
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printpalletlabel" <%=disabled%> <%=checkOption("printpalletlabel")%>>
                  &nbsp; PRINT_PALLET_LABEL
               
                 <%--Added by Ranjana for the functionality of printing tray label manually under ticket WO0000000304687--%>
                  <TH WIDTH="25%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printmanualtraylabel" <%=disabled%> <%=checkOption("printmanualtraylabel")%>>
                  &nbsp; MANUAL_TRAY_LABEL
                <TR>
                
        </TABLE>
               <INPUT Type=Hidden name="URL" value="logout">
               <BR>
               <BR>
        <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
               <TR>
                 <TH WIDTH="18%" >Remarks
                 <TD>
                 <INPUT size="50" MAXLENGTH=60 name="REMARKS" <%=disabled%> value="<%=al.get(al.size()-5).toString()%>">
               <TR>
                 <TH WIDTH="18%" >Authorised By
                 <TD>
                 <INPUT size="50" MAXLENGTH=60 disabled value="<%=authorise_by%>">
               <TR>
                 <TH WIDTH="18%" >Authorised On
                 <TD>
                 <INPUT size="50" MAXLENGTH=60 disabled value="<%=al.get(al.size()-3).toString()%>">
                 <!-- //below line is added on 4-july-2014 for user group active/inactive ticket #WO0000000041526 -->
                 <!-- start -->
               <TR>
                 <TH WIDTH="18%" >Status
                 <TD>
                 <SELECT NAME ="GROUP_ACTIVE" size="1" onchange="show(this.options[this.selectedIndex].value)" <%=disabled%>>
                  <OPTION selected value="<%=al.get(al.size()-2).toString().trim()%>"><%=al.get(al.size()-2).toString().trim()%></OPTION>
           		<% String flag = al.get(al.size()-2).toString().trim();
           		if(flag.equals("INACTIVE")) {%>
           		<option value="ACTIVE">ACTIVE</option>
           		<%}else{ %>
           		<option value="INACTIVE">INACTIVE</option>
           		<%} %>
           		</SELECT>
           		</TD>
           		</TR>
           		
               <TR id="hideTR">
                   <TH WIDTH="18%" >Reason </TH>
                   <TD>
                   <INPUT type="text" size="50" MAXLENGTH=60  name="REASON"  value="<%=al.get(al.size()-1).toString().trim()%>">
                   </TD>
               </TR>
               <script type="text/javascript">
					window.onload = hide('<%=al.get(al.size()-2).toString().trim()%>');		
					</script>
                <input type="hidden" name ="OLD_STATUS" value="<%=old_status %>">
                <!-- end -->	
         </TABLE>
                 <BR>
         <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
               <TR>
               <TD  ALIGN = "CENTER">
                        <%
        		if(caption.equalsIgnoreCase("delete"))
				//below line is added on 4-july-2014 for user group active/inactive ticket #WO0000000041526
        		//out.write("<INPUT Type=\"Submit\" name=\"Action\" Value=\"Delete\" onClick=\"return confirm('Are you sure to delete "+level_name+" GROUP permanently ?');\">&nbsp;");
        		out.write("<INPUT type=\"Button\" Value=\"Delete\">&nbsp;"); 
        		else if(caption.equalsIgnoreCase("view"))
      			out.write("<INPUT type=\"Button\" Value=\"Back to List\" onClick=\"window.location.href='javascript:history.back()'\"> ");
       			else
       			 out.write("<INPUT type=\"Submit\" name=\"Action\" Value=\"Update\">&nbsp;");
%>             <INPUT Type=Button name="C" Value="Cancel" Size = 10 onClick="window.location.href='indexPage.jsp'">
               </TD>
         </TABLE>
	</form>
	</center>
    </tr>
   </table>
 </center>
   <div align="center">
    <center>
       <p>&nbsp;</p>
    </center>
      </div>
      </font></td>
</tr>
</table>
<%
    }   //  Closing else
 %></form>
<%@ include file="footer.jsp" %>
