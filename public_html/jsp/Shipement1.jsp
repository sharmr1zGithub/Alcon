<!--<%@ page import="com.murho.gates.DbBean"%> -->
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Shipemnt</title>
<link rel="stylesheet" href="css/style.css">
<script language="javascript">
<!--



-->
</script>
<!--script language ="javascript" src="js/vendor.js"></script-->
<%@ include file="body.jsp"%>
<%

    String PLANT = (String)session.getAttribute("PLANT");
    if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
  
    String REFNO = "", LINENO  ="", MVNT = "", PARTNO = "", DESC = "",REFLNNO="",
         RANK = "", WID = "", LOC = "", QTY = "",ITEM = "",REMARK = "",remarks = "",ADDITEM = "",EDIITEM = "",
         addNew = "",addLine ="" ;
%>

<FORM name="form1" method="post" action="">

  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">SHIPMENT</font></TH>
    </TR>
  </table>
  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
      <td width="100%">&nbsp;
        <center>
          <table border="0" width="90%">
            <tr>
              <td width="100%"> <CENTER>
                  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >* Ref No : </TH>
                      <TD><INPUT name="REFNO" type = "TEXT" value="<%=REFNO%>" size="50"  MAXLENGTH=20 class="inactive" READONLY >
                      </TD>
                    </TR>
      
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >* Line No : </TH>
                      <TD><INPUT name="LINENO" type = "TEXT" value="<%=LINENO%>" size="50"  MAXLENGTH=20 class="inactive" READONLY >
                      </TD>
                    </TR>
 
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >* Rank : </TH>
                      <TD><INPUT name="RANK" type = "TEXT" value="<%=RANK%>" size="50"  MAXLENGTH=20  <%=""%> >
                      <a href="#" onClick="javascript:popUpWin('rankDetails.jsp?PARTNO='+form1.PARTNO.value+'&VIEW_STATUS=A');"><img src="images/populate.gif" border="0"></a>
                       </TD>
                    </TR>
                    
                      <TH WIDTH="35%" ALIGN="RIGHT" >&nbsp;</TH>
                      <TD>* Mandatory Fields</TD>
                    </TR>
                  </TABLE>
                </CENTER></td>
            </tr>

          </table>
          <br>
        </center>
        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <div align="center">
          <center>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
                  <input type="button" value="Back" onClick="window.location.href='javascript:history.back()'">&nbsp;
               
                </td>
              </tr>
            </table>
          </center>
        </div>
   </td>
    </tr>
  </table>
</FORM>

<%@ include file="footer.jsp"%>


