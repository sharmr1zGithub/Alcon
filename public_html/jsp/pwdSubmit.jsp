<%@ include file="header.jsp" %>
<%

//Code added by Arun for #25046
String resetFlag = (String)session.getAttribute("resetPwdByAdmin");

if( null == resetFlag || resetFlag.equalsIgnoreCase("false")){ %>
	<%@ include file="body.jsp" %>
<%} %>

<jsp:useBean id="ub"  class="com.murho.gates.userBean" />
<jsp:useBean id="eb"  class="com.murho.gates.encryptBean" />
<jsp:useBean id="df"  class="com.murho.gates.defaultsBean" />
 <jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>
<%! String result; %>
<%

    String cancel=  "<input type=\"button\" value=\" OK \" name=\"cancelBtn\" onClick=\"window.location.href='indexPage.jsp'\">";
    String nextPage="<input type=\"button\" value=\"Back\" name=\"nextBtn\" onClick=\"window.location.href='javascript:history.back()'\"> "+cancel;

    String newPwd,user_id,oldPwd, oldPwdFlag;

    user_id  = (String)session.getAttribute("LOGIN_USER");
    newPwd   = request.getParameter("NPASSWORD");
    oldPwd   = request.getParameter("PASSWORD");
    // Modified by arun for #25046
    oldPwdFlag = request.getParameter("OLD_PASSWORD_FLAG");

    newPwd   = eb.encrypt(newPwd);
    // if condition added by Arun for #25046 to avoid null pointer
    if(null!= oldPwd){
	    oldPwd   = eb.encrypt(oldPwd);
    }

   
//  Modified by arun for #25046
//int n = ub.changePassword(user_id, newPwd, oldPwd);
    int n = ub.changePassword(user_id, newPwd, oldPwd, oldPwdFlag);

    if(n==1)
    {result = "<font class=maingreen>Password has been changed successfully</font><br><br><center>"+cancel;
    mv.insertMovHisLogger(user_id,"Change_Pwd", result);
    }
    else if(n==-2)
    {result = "<font class=mainred>Your old password is not right...Try again <br><br><center>"+nextPage;
    	// Added by Arun for #25046 URS-8.5: any attempt of change passowrd should be audit trailed
    	mv.insertMovHisLogger(user_id,"Change_Pwd", result);
    }
    else
    {result = "<font class=mainred>Error in changing the password <br> Please try again</font><br><br><center> "+nextPage;
//  Added by Arun for #25046 URS-8.5: any attempt of change passowrd should be audit trailed
    mv.insertMovHisLogger(user_id,"Change_Pwd", result);
     }
     //   df.insertToLog(session, "Changing User Password", result);   //  Inserting into the user log
       
    //    mv.insertMovHisLogger(user_id,"Changing User Password", result);
        
        
        result = "<h3><b>"+result;

        session.setAttribute("RESULT",result);
        response.sendRedirect("displayResult2User.jsp");
%>

<%@ include file="footer.jsp" %>

