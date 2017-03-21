<%@ include file="header.jsp" %>

<script language="JavaScript" type="text/javascript" src="js/userLevel.js"></script>
<title>User Level Maintenance</title>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp" %>
<form name="form" method="POST" action="userLevelSubmit.jsp" onSubmit="return validateLevel()">
  <br>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">CREATE NEW GROUP</font>
  </table>
  <br>
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
                        <input MAXLENGTH=20 type="text" name="LEVEL_NAME" size="50">
                    <TR>
                      <TH>Remarks
                      <TD>
                        <INPUT size="50" MAXLENGTH=60 name="REMARKS">
                        <TR>
                     <!--  Commented by Arun for Login Synch change on 29 June 2011 : #25046: Added new screen to define Password age -->
                     <!--   <TH>Password Age
                      <TD>
                        <INPUT size="50" MAXLENGTH=60 name="PWDAGE">
                        <TR>
                      <TH>Range To Remind
                      <TD>
                        <INPUT size="50" MAXLENGTH=60 name="MINRANGE">
					-->
                  </TABLE>
                  <BR>
                  <BR>
                  <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
                    <TR>
                      <TH BGCOLOR="#000066" COLSPAN="3"><font color="white">USER ADMINISTRATION</font>
                    <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaNewLevel">
                        &nbsp; Create New Group
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaMaintLevel">
                        &nbsp; Maintain Group
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaDelLevel">
                        &nbsp; Delete Group
                    <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaNewAcct">
                        &nbsp; Create New User
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaMaintAcct">
                        &nbsp; Maintain User
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <input Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaDelAcct">
                        &nbsp; Delete User
                    <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaChngPwd">
                        &nbsp; Change Password
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="auUserLevel">
                        &nbsp; Authorise Group
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="auUserAcct">
                        &nbsp; Authorise User
                        
                      <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="renewPwd" >
                        &nbsp; Reset Password
                        
                     <!-- Added by Arun for Login Synchronization change on 29 June 2011 -->
                     	<TH WIDTH="25%" ALIGN = "LEFT">
                    	 <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="setSystemParams" >
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
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msLocAsgnRule">
                        &nbsp; Location Assign Rule
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msRules">
                        &nbsp; Palletizing Rules 
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msRulesSummary">
                        &nbsp; Palletizing Summary
                     <TR>

                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="tblControl">
                        &nbsp; Table Control
                    
                      </TABLE>
                  <BR>
                  <BR>
                  <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
                    <TR>
                      <TH BGCOLOR="#000066" COLSPAN="3"><font color="white">MASTER</font>
                   <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msItemMst">
                        &nbsp; Item Master
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msProdGroup">
                        &nbsp; Product Group
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msLocMst">
                        &nbsp; Location Master
                     
                      <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msLocGrp">
                        &nbsp; Location group 
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msPrdCls">
                        &nbsp; Product Class
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msDestination">
                        &nbsp; Destination Master
                       <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msPalletGroup">
                        &nbsp; Pallet Group
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msTrayGrp">
                        &nbsp; Tray Group Master
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="custMst">
                        &nbsp;  Customer Master
                      <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="rsnmst" >
                        &nbsp; Reason Master
                  </TABLE>
                  <BR>
                  <BR>
                  <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
                    <TR>
                      <TH BGCOLOR="#000066" COLSPAN="3"><font color="white">INBOUND</font>
                      <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="import">
                        &nbsp; Import Delivery No.
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="travelerapprv">
                        &nbsp;Delivery No. Summary
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="recapprove">
                        &nbsp; Receiving Approval
                    <TR>
                    <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="putAwaySummList">
                        &nbsp; PutAway Summary
                      
                 
                  </TABLE>
                   <BR>
                  <BR>
                  <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
                    <TR>
                      <TH BGCOLOR="#000066" COLSPAN="3"><font color="white">OUTBOUND</font>
                      <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="importso">
                        &nbsp; Import SO
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="obTravelerSummary">
                        &nbsp;SO Summary
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pickerHis" >
                        &nbsp; Pick List History
                    <TR>
                    <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msPickListSummary">
                        &nbsp; Pick List Summary
                        <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="trayLabelingSummary">
                        &nbsp; Tray Label Summary
                      
                 
                  </TABLE>
                <BR>
                  <BR>
                  <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
                    <TR>
                      <TH BGCOLOR="#000066" COLSPAN="3"><font color="white">SHIPPING</font>
					  <TR>
                  <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="Shipping" >
                        &nbsp; Global Shipping
                      <TH WIDTH="25%" ALIGN = "LEFT">
                      <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msImportShipping" >
                      &nbsp; Import ZShipping9
                      <TH WIDTH="25%" ALIGN = "LEFT">
                      <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="iscAddPallet" >
                      &nbsp;ISC Add Pallet
                      <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                      <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="iscShipping">
                      &nbsp;ISC Shipping
                      
                  </TABLE>
                  <BR>
                  <BR>
                  <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
                    <TR>
                      <TH BGCOLOR="#000066" COLSPAN="4"><font color="white">REPORTS</font>
                    <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="rptStVsINV">
                        &nbsp; Stock Take
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msInvList">
                        &nbsp; Inventory Enquiry
                         <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="msPOList">
                        &nbsp; Movement History
                        
                      <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="mstrayDescrepency">
                        &nbsp; Discrepancy Report
                        
                        <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="qcreport">
                        &nbsp;Traveler Report
                        <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="rptStkTransfer">
                        &nbsp;Stock Transfer
                        
                         <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="rptStkAdj">
                        &nbsp; Stock Adjustment
                  </TABLE>
				  <BR>
                  <BR>
                  <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
                    <TR>
                      <TH BGCOLOR="#000066" COLSPAN="3"><font color="white">PDA -  MENU</font>
                    <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaRecv">
                        &nbsp; Receving
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaPutAway">
                        &nbsp; PutAway
                     <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaBlindRcpt">
                        &nbsp; Blind Receipt In
						<TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaPicking">
                        &nbsp; Picking
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaPickOut">
                        &nbsp; PickOut
                    
					  <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaStkCount">
                        &nbsp; Stock Count
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaStkTransfer">
                        &nbsp; Stock Transfer
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaQuery">
                        &nbsp; Query Inventory
						<TR>
                      <!-- Added by Arun for Login Synchronization change on 15 July 2011 :#25046-->
                     	<TH WIDTH="25%" ALIGN = "LEFT">
                    	 <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="pdaChangePassword">
                     	&nbsp; Change Password
                     	<TR>
                     <!-- end -->
                    </TABLE>
                  <BR>
                  <BR>
                     <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
                    <TR>
                      <TH BGCOLOR="#000066" COLSPAN="3"><font color="white">LABEL PRINTING</font>
                    <TR>
                    <!-- Added by Arun for Login Synchronization change on 15 July 2011 :#25046-->
                     	<TH WIDTH="25%" ALIGN = "LEFT">
                    	 <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printChangePassword">
                     	&nbsp; CHANGE PASSWORD
                     	<TR>
                     <!-- end -->
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printconfig" >
                        &nbsp; PRINT_CONFIG
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printmtid" >
                        &nbsp; RE-PRINT MTID
                     <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printtraylabel" >
                        &nbsp; TRAY_LABELING
                         <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printGlobalShip" >
                        &nbsp; GLOBAL SHIPPING
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printIscShip" >
                        &nbsp; ISC SHIPPING
						<TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printtrayid" >
                        &nbsp; RE-PRINT TRAYID
                      <TH WIDTH="25%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="printpalletlabel" >
                        &nbsp; PRINT_PALLET_LABEL
                     					 
						<TR>
                      
                    </TABLE>
                 

                  <BR>
                  <BR>
                      <INPUT Type=Hidden name="URL" value="logout">
                      <INPUT Type=Hidden name="GROUP_ACTIVE" value="ACTIVE">
                      <INPUT Type=Hidden name="REASON" value="">
                      
                      
                  <BR>
                  <BR>
                  <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
                    <TR>
                      <TH WIDTH="30%" ALIGN = "CENTER">
                        <INPUT Type=Submit name="Action" Value=" Save " Size = 20>
                        &nbsp;
                        <INPUT Type=Button name="C" Value="Cancel" Size = 10 onClick="window.location.href='indexPage.jsp'">
                  </TABLE>
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
</form>
<%@ include file="footer.jsp" %>
