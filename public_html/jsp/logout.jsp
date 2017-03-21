<%@ include file="header.jsp" %>
<META HTTP-EQUIV="REFRESH" CONTENT="02;URL=index.jsp">
<title>Thank You</title>
<%@ include file="body.jsp" %>
<jsp:useBean id="df"  class="com.murho.gates.defaultsBean" />
<jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>
<%

   String sUserId = (String) session.getAttribute("LOGIN_USER");
    session = request.getSession();
   // df.insertToLog(session, "User Logout Information", "Logged out Successfully");   //  Inserting into the user log
    mv.insertMovHisLogger(sUserId, "User_Logging","User : " +sUserId + "  Logged out Successfully"); 
    
    session.invalidate();
    Runtime.getRuntime().gc();
%>
<br><br><br><br><br><br><br><br>
<center>
<img src="images/logging_out.gif" border="0">
</center>
<%@ include file="footer.jsp" %>
