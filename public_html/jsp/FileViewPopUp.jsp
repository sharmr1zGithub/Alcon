<%@ page import="com.murho.db.utils.*"%>
<%@ page import="com.murho.utils.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="com.murho.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.murho.dao.*"%>
<%@ page import="com.murho.DO.TransactionDTO"%>
<%@ page import="java.text.SimpleDateFormat"%>

<html>
  <head>
<title>File List</title>
<link rel="stylesheet" href="css/style.css">
 
  </head>
  <body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="center"><font color="white">File List</font></TH>
     
    </TR>
<%
    StrUtils strUtils   = new StrUtils();
    DateUtils  dateUtil = new DateUtils();
    TRHeadDAO trHeadDAO=new TRHeadDAO();
    ArrayList soList    = new ArrayList();
    TransactionDTO  trnDTO=new TransactionDTO();
    MLogger mLogger = new MLogger();
    
    String FILENAME = strUtils.fString(request.getParameter("FILENAME"));
     
     System.out.println("FileName in PopUp......"+FILENAME );
    
     mLogger.log("FILE slip :"+FILENAME);
     String sBGColor = "";
    
     ArrayList alTransactionData=new ArrayList();
     trnDTO.setFilename(FILENAME);
     alTransactionData.add(trnDTO);
      System.out.println(" alTransactionData. in PopUp......"+ alTransactionData );
    // invQryList=recvDAO.getReceiveDetails1(alTransactionData);
   

   try{
    List listQry = trHeadDAO.getFileView(alTransactionData);
      System.out.println(" Listt...."+ listQry);
    for (int i =0; i<listQry.size(); i++){
    Map map = (Map) listQry.get(i);
    sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
    String filename         = (String) map.get("HDRFILENAME");
    
     %>
    <TR bgcolor="<%=sBGColor%>">
      <td align ="center" class="main2">
      <a href="#" onClick="window.opener.form1.FILENAME.value='<%=filename%>';
      
       window.close();"><%=filename%></td>
      

</TR>
<%
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
   <TR>
        <TH COLSPAN="8">&nbsp;</TH>
   </TR>
    <TR>
      <TH COLSPAN="8" align="center"><a href="#" onclick="window.close();"><input type="submit" value="Close"></a></TH>
    </TR>
  </table>

  </body>
</html>
