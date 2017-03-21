<%@ include file="header.jsp" %>
<%@ include file="body.jsp" %>

<jsp:useBean id="ub"  class="com.murho.gates.userBean" />
<jsp:useBean id="eb"  class="com.murho.gates.encryptBean" />
<jsp:useBean id="df"  class="com.murho.gates.defaultsBean" />
<!--added by Arun on 29 Sept 2011 for Audit Trail track requrement in #25046-->
 <jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>

<%! String result; %>
<%

    String cancel=  "<input type=\"button\" value=\" OK \" name=\"cancelBtn\" onClick=\"window.location.href='indexPage.jsp'\">";
    String nextPage="<input type=\"button\" value=\"Back\" name=\"nextBtn\" onClick=\"window.location.href='javascript:history.back()'\"> "+cancel;

    String newPwd,user_id,admin;

    admin  = (String)session.getAttribute("LOGIN_USER");
    newPwd   = request.getParameter("NPASSWORD");
    user_id   = request.getParameter("USERID");

    newPwd   = eb.encrypt(newPwd);
    //oldPwd   = eb.encrypt(oldPwd);

    // below mnethod call added by Arun for #25046 change: First check if valid user exists

    boolean flag = ub.isValidUserExists(user_id);
    if(flag){
	    int n = ub.resetPassword(admin,newPwd, user_id);
	    if(n==1){		result = "<font class=maingreen>Password has been changed successfully</font><br><br><center>"+cancel;
	    }
	   // else if(n==-2)      result = "<font class=mainred>Your Old Password is not right...Try again <br><br><center>"+nextPage;
	    else
	    	result = "<font class=mainred>Error in changing the password <br> Please try again</font><br><br><center> "+nextPage;
    }else{
    	result = "<font class=mainred>Invalid UserID : "+user_id+" Please Try Again with Correct UserID</font><br><br><center> "+nextPage;
    }
		//  added by Arun on 29 Sept 2011 for Audit Trail track requrement in #25046
		mv.insertMovHisLogger(admin,"Reset_Pwd", result);

        df.insertToLog(session, "Changing User Password", result);   //  Inserting into the user log

        result = "<h3><b>"+result;

        session.setAttribute("RESULT",result);
        response.sendRedirect("displayResult2User.jsp");
%>

<%@ include file="footer.jsp" %>

