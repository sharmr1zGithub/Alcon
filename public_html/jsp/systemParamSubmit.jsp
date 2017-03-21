<!-- New page adde by Arun on 08 July 2011 for ticket #25046 -->
<%@ include file="header.jsp" %>
<%@ include file="body.jsp" %>

<jsp:useBean id="ub"  class="com.murho.gates.userBean" />
 <jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>
<%! String result; %>
<%

    String cancel=  "<input type=\"button\" value=\" OK \" name=\"cancelBtn\" onClick=\"window.location.href='indexPage.jsp'\">";
    String nextPage="<input type=\"button\" value=\"Back\" name=\"nextBtn\" onClick=\"window.location.href='javascript:history.back()'\"> "+cancel;

    String user_id,pwdAge,minRange,login_attempts, sAP_FilePath, uploadFolder_Path,delivery_No,Pallet_id,mtid;

    user_id  = (String)session.getAttribute("LOGIN_USER");
    pwdAge  = request.getParameter("PWDAGE");
    minRange   = request.getParameter("MINRANGE");
    login_attempts   = request.getParameter("LOGIN_ATTEMPTS");
    
//  Added by jyoti for TIBCO INC000002484471 SAP Confirmation file automatic saved on TIBCO
    sAP_FilePath = request.getParameter("CONF_PATH");
    uploadFolder_Path = request.getParameter("UPLOAD_PATH");
    
//  Added by Ranjana for generation of inbound file under ticket WO0000000356180 to be considered in to 3.0

    delivery_No=request.getParameter("DELIVERY_NO");
    Pallet_id=request.getParameter("PALLET_ID");
    mtid=request.getParameter("MTID");

    boolean status = ub.setSystemParams( pwdAge, minRange, login_attempts, sAP_FilePath, uploadFolder_Path,delivery_No,Pallet_id,mtid,session);

// boolean status = ub.setSystemParams( pwdAge, minRange, login_attempts, sAP_FilePath, uploadFolder_Path,session);
    if(status){
    	result = "<font class=maingreen>System parameters set successfully</font><br><br><center>"+cancel;
	   // mv.insertMovHisLogger(user_id,"Set_System_Parameters", result);
    }
    else{
    	result = "<font class=mainred>Error in setting the system parameters<br> Please try again</font><br><br><center> "+nextPage;
    	mv.insertMovHisLogger(user_id,"Set_System_Parameters", result);
    }
   
        result = "<h3><b>"+result;
        session.setAttribute("RESULT",result);
        response.sendRedirect("displayResult2User.jsp");
%>

<%@ include file="footer.jsp" %>

