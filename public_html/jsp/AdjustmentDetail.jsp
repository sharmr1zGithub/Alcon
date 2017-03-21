<!--<%@ page import="com.murho.gates.DbBean"%> -->
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Stock Adjustment Detail</title>
<link rel="stylesheet" href="css/style.css">
<script language="javascript">
<!--

function onUpdate(){
 var flag    = "false";
document.form1.MTID.value;
var aqty=document.form1.AQTY.value;
var remark=document.form1.REMARK.value;
document.form1.SKU.value;
document.form1.HiddenUpdate.value="Update";
document.form1.action = "AdjustmentDetail.jsp";
/*
if(aqty != null   && aqty != "")
{
flag = true;
}
if(flag == "false"){ alert("Please enter the adjust quantity"); return false;}

flag = false;
if(remark != null   && remark != ""){ flag = true;}
if(flag == "false"){ alert("Please enter the remark"); return false;}

*/
if(aqty == "" || aqty == null) {alert("Please enter the adjust quantity"); document.form1.AQTY.focus(); return false; }
if(remark == "" || remark == null) {alert("Please enter the remark"); document.form1.REMARK.focus(); return false; }
document.form1.submit();

}
function onApprove(){
document.form1.action = "TraySummary.jsp";
document.form1.submit();
}


-->
</script>
<!--script language ="javascript" src="js/vendor.js"></script-->
<%@ include file="body.jsp"%>
<%

StrUtils strUtils     = new StrUtils();
ReportUtil invUtil       = new ReportUtil();
InvMstDAO invdao = new InvMstDAO();
MovHisDAO movHisDAO=new MovHisDAO();
ArrayList invQryList  = new ArrayList();
DateUtils dateUtil           = new DateUtils();

String PLANT = (String)session.getAttribute("PLANT");
if(PLANT == null) PLANT = CibaConstants.cibacompanyName;
 
String MTID="",OBOUND="",QTY="",SKU="",fieldDesc="",HiddenApprove="",ITEM="",TRAVELNO="",LOT="",LOC="",AQTY=""; 


String newqty = "",HiddenUpdate="";
String list = "view";

boolean isupdated = false;
 
HiddenApprove=strUtils.fString(request.getParameter("HiddenApprove"));
TRAVELNO=strUtils.fString(request.getParameter("TRAVELNO"));
HiddenUpdate=strUtils.fString(request.getParameter("HiddenUpdate"));

MTID=strUtils.fString(request.getParameter("MTID"));
SKU=strUtils.fString(request.getParameter("SKU"));
AQTY=strUtils.fString(request.getParameter("AQTY"));

String LOGIN_USER   = (String)session.getAttribute("LOGIN_USER");

String tranDate=dateUtil.getDateinyyyy_mm_dd(dateUtil.getDate());

if(list.equalsIgnoreCase("view")){
 try{
    
      
     invQryList = invUtil.getStockDetails(PLANT,TRAVELNO); 
      
      if(invQryList.size() < 1)
      {
       fieldDesc = "<tr><td><B><h3>SKU not found<h3></td></tr>";
      }else{
      for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
          int iIndex = iCnt + 1;
          SKU = (String)lineArr.get("ITEM");
          LOT = (String)lineArr.get("LOT");
          LOC = (String)lineArr.get("LOC");
          QTY = (String)lineArr.get("QTY");
        
                
      }    
      }
 }catch(Exception e) {System.out.println("Exception :getInvList"+e.toString()); }
}


 if(HiddenUpdate.equalsIgnoreCase("Update"))
 {
   System.out.println(":::::: inisde action approve :::::::::::::");
 
   isupdated=invdao.updateInventory(MTID,SKU,AQTY);
 
  if(isupdated){
  
    isupdated=movHisDAO.RecordRejectDetail(CibaConstants.cibacompanyName,TRAVELNO,MTID,SKU,LOT,AQTY,"STOCKADJUSTMENT",LOGIN_USER,tranDate);
    
  }
  if(isupdated){ 
  fieldDesc = "<tr><td><B><h3>Inventory Updated Successfully<h3></td></tr>";
  }
  }

%>

<FORM name="form1" method="post" action="">
<INPUT type="hidden" name="HiddenUpdate" value="">


  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">STOCK ADJUSTMENT DETAIL</font></TH>
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
                      <TH WIDTH="35%" ALIGN="RIGHT" > MTID : </TH>
                      <TD><INPUT name="MTID" readonly type = "TEXT" value="<%=TRAVELNO%>" size="50"  MAXLENGTH=20 >
                      </TD>
                    </TR>
 
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" > SKU : </TH>
                      <TD><INPUT name="SKU"  readonly type = "TEXT" value="<%=SKU%>" size="50"  MAXLENGTH=20  >
                      </TD>
                    </TR>
                    
                         <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" > LOT : </TH>
                      <TD><INPUT name="LOT"  readonly type = "TEXT" value="<%=LOT%>" size="50"  MAXLENGTH=20  >
                      </TD>
                    </TR>
                    
                         <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" > SKU : </TH>
                      <TD><INPUT name="LOC" readonly  type = "TEXT" value="<%=LOC%>" size="50"  MAXLENGTH=20  >
                      </TD>
                    </TR>
                    
                      <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" > Quantity : </TH>
                      <TD><INPUT name="QTY" readonly type = "TEXT" value="<%=QTY%>" size="50"  MAXLENGTH=20  <%=""%> >
                      </TD>
                    </TR>
                    
                     <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" > Adjust Quantity : </TH>
                      <TD><INPUT name="AQTY" type = "TEXT" value="<%=AQTY%>" size="50"  MAXLENGTH=20  <%=""%> >
                      </TD>
                    </TR>
                    
                       <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" > Remarks  : </html>
                      <TD><INPUT name="REMARK" type = "TEXT" value="" size="50"  MAXLENGTH=20  <%=""%> >
                      </TD>
                    </TR>
                    
                      <TH WIDTH="35%" ALIGN="RIGHT" >&nbsp;</TH>
                      <TD></TD>
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
                   <input type="button" value="Update" onClick="javascript:return onUpdate();">&nbsp;
                </td>
              </tr>
            </table>
          </center>
        </div>
   </td>
    </tr>
  </table>
</FORM>
<font face="Times New Roman">
    <table  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="">
      <%=fieldDesc%>
    </table>
    </font>
    
<%@ include file="footer.jsp"%>


