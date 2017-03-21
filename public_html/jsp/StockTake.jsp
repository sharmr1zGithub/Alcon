<%@ page import="com.murho.gates.DbBean"%>
<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<head>
<script language="javascript">

function popUpWin(URL) {
 subWin = window.open(URL, 'PackSlip', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function onGo(){

   var flag    = "false";
   var SKU    = document.form1.SKU.value;
   var LOTNO    = document.form1.LOTNO.value;
   var LOC   = document.form1.LOC.value;
   var LOCGRP   = document.form1.LOCGRP.value;
  
  
  /* if(SKU != null    && SKU != "") { flag = true;}
   if(LOTNO != null    && LOTNO != "") { flag = true;}
   if(LOC != null   && LOC != "") { flag = true;}
   if(LOCGRP != null    && LOCGRP != "") { flag = true;}

   if(flag == "false"){ alert("Please define any one search criteria"); return false;} */

document.form1.VIEW_INV.value="Go";
document.form1.action = "StockTake.jsp";
document.form1.submit();
}

function onExport(){
  var flag    = "false";
   var SKU    = document.form1.SKU.value;
   var LOTNO    = document.form1.LOTNO.value;
   var LOC   = document.form1.LOC.value;
   var LOCGRP   = document.form1.LOCGRP.value;
   var PLANT   = document.form1.PLANT.value;
  
   /*
   if(SKU != null    && SKU != "") { flag = true;}
   if(LOTNO != null    && LOTNO != "") { flag = true;}
   if(LOC != null   && LOC != "") { flag = true;}
   if(LOCGRP != null    && LOCGRP != "") { flag = true;}
   
   if(flag == "false"){ alert("Please define any one search criteria"); return false;}*/
   
document.form1.action='/CibaVisionWms/ExcelGeneratorServlet?action=GenerateStockCount&PLANT='+PLANT+'&SKU='+SKU+'&LOTNO='+LOTNO+'&LOC='+LOC+'&LOCGRP='+LOCGRP;   
document.form1.submit();
}

</script>
<title>Stock Take</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
String Res    = strUtils.fString(request.getParameter("Res"));
String PLANT        = (String)session.getAttribute("PLANT");
String   SKU = "", LOTNO ="",  LOCGRP ="", LOC="",view_Inv="";
String html = "";
LOTNO         = strUtils.fString(request.getParameter("LOTNO"));
SKU           = strUtils.fString(request.getParameter("SKU"));
LOC           = strUtils.fString(request.getParameter("LOC"));
LOCGRP        = strUtils.fString(request.getParameter("LOCGRP"));
view_Inv      = strUtils.fString(request.getParameter("VIEW_INV"));
String currentPage        = strUtils.fString(request.getParameter("cur_page"));
String listRecSize =strUtils.fString(request.getParameter("listRecSize")); 
if(listRecSize.length()==0){
listRecSize ="0";
}

long listRec=0;
ArrayList QryList  = new ArrayList();
StockTakeUtil stockTakeUtil       = new StockTakeUtil();

    int curPage      = 1;
    int recPerPage   = Integer.parseInt(DbBean.STKTAKE_NUMOFREC);
    long totalRec     = 0;
    String isDisabled ="disabled";

if(view_Inv.equalsIgnoreCase("Go")){
 try{
     currentPage ="1";
     listRec = stockTakeUtil.getRecCountForStockTake(PLANT,SKU,LOTNO,LOC,LOCGRP);
      StringBuffer query = new StringBuffer();
      if (SKU != null && !SKU.trim().equals(""))
				query.append(" AND A.ITEM LIKE '" + SKU.trim() + "%'");
      if (LOTNO != null && !LOTNO.trim().equals(""))
				query.append(" AND A.BATCH LIKE '" + LOTNO.trim() + "%'");
      if (LOC != null && !LOC.trim().equals(""))
				query.append(" AND A.LOC LIKE '" + LOC.trim() + "%'");
			if (LOCGRP != null && !LOCGRP.trim().equals(""))
				query.append(" AND A.LOC IN(select  LOC_ID  from locmst where LOC_GRP_ID = '"+LOCGRP+"') ");
        
      QryList= stockTakeUtil.getStockTakeSummary(query.toString(),currentPage,new Integer(recPerPage).toString());
     if (listRec >0 )  {         totalRec = listRec; listRecSize=new Long(totalRec).toString();
     isDisabled="";
     }
     if (currentPage.length() > 0)
     {
     
    try                   {   curPage = (new Integer(currentPage)).intValue(); System.out.println("curPage :: "+curPage); }
    catch (Exception e)   {   curPage = 1;                                      }
    }
     
   
   
 }catch(Exception e) {System.out.println("Exception :getStockTakeList"+e.toString()); }
}


if(view_Inv.equalsIgnoreCase("PREVORNEXT")){
 try{
  
   //  QryList = stockTakeUtil.getStockTakeDetails(PLANT,SKU,LOTNO,LOC,LOCGRP,currentPage,recPerPage);
      StringBuffer query = new StringBuffer();
      if (SKU != null && !SKU.trim().equals(""))
				query.append(" AND A.ITEM LIKE '" + SKU.trim() + "%'");
      if (LOTNO != null && !LOTNO.trim().equals(""))
				query.append(" AND A.BATCH LIKE '" + LOTNO.trim() + "%'");
      if (LOC != null && !LOC.trim().equals(""))
				query.append(" AND A.LOC LIKE '" + LOC.trim() + "%'");
			if (LOCGRP != null && !LOCGRP.trim().equals(""))
				query.append(" AND A.LOC IN(select  LOC_ID  from locmst where LOC_GRP_ID = '"+LOCGRP+"') ");
        
      QryList= stockTakeUtil.getStockTakeSummary(query.toString(),currentPage,new Integer(recPerPage).toString());
     if (QryList != null)  
     {         
    	 totalRec = QryList.size();
     	 isDisabled="";
     }
     if (currentPage.length() > 0)
     {
    	try {   
    			curPage = (new Integer(currentPage)).intValue(); 
    			System.out.println("curPage :: "+curPage); 
    		}
   	    catch (Exception e){ 
    			curPage = 1;                                      
    	}
    }   
 }
 catch(Exception e) {
	 				System.out.println("Exception :getStockTakeList"+e.toString()); }
					}


   
    int totalPages = (Integer.parseInt(listRecSize) + recPerPage -1)/recPerPage;
    if (curPage > Integer.parseInt(listRecSize)) curPage = 1;                    // Out of range

    System.out.println("totalPages :: "+totalPages);
    System.out.println("curPage :: "+curPage);



%>


<SCRIPT LANGUAGE="JavaScript">

    var cur_page      = <%=curPage%>;                            // Current display page
    var total_pages   = <%=totalPages%>;                         // The total number of records

    // Display previous page of user list
    function onPrev()
    {
	if (cur_page <= 1)  return false;
	cur_page = parseInt(cur_page) -1;
	document.form1.cur_page.value = cur_page;
  document.form1.VIEW_INV.value="PREVORNEXT";
  document.form1.action = "StockTake.jsp";
	document.form1.submit();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // Display next page of user list
    function onNext()
    {
    if (cur_page >= total_pages)  {
      return false;
    }else{
      cur_page = parseInt(cur_page) + 1;
      document.form1.cur_page.value = cur_page;
      document.form1.VIEW_INV.value="PREVORNEXT";
      document.form1.action = "StockTake.jsp";
      document.form1.submit();
      }
    }
 </SCRIPT>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="StockTake.jsp">
<INPUT type="hidden" name="PLANT" value="<%=PLANT%>">
<INPUT type="hidden" name="VIEW_INV" value="">
<INPUT type="hidden" name="listRecSize" value="<%=listRecSize%>">



  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">STOCK TAKE</font></TH>
    </TR>
  </TABLE>
  <br>

<TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

          <TR>
          <TH ALIGN="RIGHT" >Location Id &nbsp;&nbsp;</TH>
         <TD><INPUT name="LOC" type = "TEXT" value="<%=LOC%>" size="20"  MAXLENGTH=20>
         <a href="#" onClick="javascript:popUpWin('listView/view_loc_list.jsp?LOC='+form1.LOC.value);"><img src="images/populate.gif" border="0"></a></TD>

         <TH ALIGN="RIGHT" >Lot&nbsp;No.&nbsp;&nbsp;</TH>
             <TD><INPUT name="LOTNO" type = "TEXT" value="<%=LOTNO%>" size="20"  MAXLENGTH=20>
             <a href="#" onClick="javascript:popUpWin('listView/view_invlot_list.jsp?LOTNO='+form1.LOTNO.value);"><img src="images/populate.gif" border="0"></a> </TD>    		
                     
		</TR>

		<TR>
        <TH ALIGN="RIGHT" >Sku&nbsp;&nbsp;</TH>
         <TD><INPUT name="SKU" type = "TEXT" value="<%=SKU%>" size="20"  MAXLENGTH=20>
          <a href="#" onClick="javascript:popUpWin('listView/view_invsku_list.jsp?SKU='+form1.SKU.value);"><img src="images/populate.gif" border="0"></a></TD>
		   
         <TH ALIGN="RIGHT" >Location Group &nbsp;&nbsp;</TH>
        <TD><INPUT name="LOCGRP" type = "TEXT" value="<%=LOCGRP%>" size="20"  MAXLENGTH=20>
         <a href="#" onClick="javascript:popUpWin('listView/view_locGrp_list.jsp?LOCGRP='+form1.LOCGRP.value);"><img src="images/populate.gif" border="0"></a></TD>
                

         <TD ALIGN="left" >
              &nbsp;&nbsp; <input type="button" value="Go" onClick="javascript:return onGo();"/></TD>
        </TD>
        </TR>
       
  </TABLE>
  <br>
  <br>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
           <TH align="center"><font color="#ffffff" >SNO</TH>
           <TH align="left"><font color="#ffffff" align="left"><b>LOC</TH>
           <TH align="left"><font color="#ffffff" align="left"><b>LOT</TH>
           <TH align="left"><font color="#ffffff" ><b>SKU</TH>
           <TH align="left"><font color="#ffffff" align="left"><b>DESCRIPTION</TH>
           <TH align="left"><font color="#ffffff" align="left"><b>MTID</TH>
         <TH align="right"><font color="#ffffff" align="left"><b>INVQTY</TH>
         <TH align="right"><font color="#ffffff" align="left"><b>STKQTY</TH>
         <TH align="right"><font color="#ffffff" align="left"><b>QTYDIFF</TH>
         <TH align="left"><font color="#ffffff" align="left"><b>USER</TH>
         <TH align="left"><font color="#ffffff" align="left"><b>DATE</TH>
       </tr>
       <%
          for (int iCnt =0; iCnt<QryList.size(); iCnt++){
        
          Map lineArr = (Map) QryList.get(iCnt);
          
         /*  String strDate =(String)lineArr.get("CRAT"); 
           if(strDate.length()>0){
           strDate =  new DateUtils().getDatemmddyy((String)lineArr.get("CRAT"));
           }*/
          int iIndex = iCnt + 1;
          int Curpg = ((Integer.parseInt(currentPage)-1)*recPerPage)+iIndex ;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
          <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=Curpg%></TD>
            <TD align= "left"><%=(String)lineArr.get("LOC")%></TD>
            <TD align= "left"><%=(String)lineArr.get("BATCH")%></TD>
            <TD align= "left"><%=(String)lineArr.get("ITEM")%></TD>
            <TD align= "left"><%=(String)lineArr.get("DESCRIP")%></TD>
            <TD align= "left"><%=(String)lineArr.get("MTID")%></TD>
            <TD align= "right"><%=(String)lineArr.get("INVQTY")%></TD>
            <TD align= "right"><%=(String)lineArr.get("STKQTY")%></TD>
            <TD align= "right"><%=(String)lineArr.get("DIFFQTY")%></TD>
            <TD align= "left"><%=(String)lineArr.get("CRBY")%></TD>
            <TD align= "left"><%=(String)lineArr.get("CRAT")%></TD>
           
           
          
           </TR>
       <%}%>

    </TABLE>
   
    <br>
       <table align="center" >
     <TR>
   <td>  <input type="button" value="        Back      " onClick="window.location.href='indexPage.jsp'">&nbsp; </td>
  <td>   <input maxlength="100"  type="button" value="GenerateExcel" onClick="javascript:return onExport();"/> </td> 
   <td><input type = "submit" value= "Prev" onclick="return onPrev();" <%=isDisabled%> >&nbsp;
   <input type = "submit" value= "Next" onclick="return onNext();" <%=isDisabled%> >&nbsp;
           Page :<%=curPage%>
             <input type="hidden" name="cur_page" size="4" maxlength="4" value="<%=curPage%>" readonly >&nbsp;&nbsp;
	    /&nbsp;&nbsp;
            <%=totalPages%>&nbsp;&nbsp;
   </td>
   </TR>
    <font class = "+MDbConstant.SUCCESS_COLOR +" align =center><%=Res%></font>
    </table>
    
  </FORM>
<%@ include file="footer.jsp"%>