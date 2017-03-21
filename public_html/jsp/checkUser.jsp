<%@ page import="com.murho.utils.*"%>
<%@ include file="header.jsp" %>
<META HTTP-EQUIV="REFRESH" CONTENT="02;URL=indexPage.jsp">
<title>User Validation</title>
</head>
<jsp:useBean id="ub"  class="com.murho.gates.userBean" />
<jsp:useBean id="eb"  class="com.murho.gates.encryptBean" />
<jsp:useBean id="sb"  class="com.murho.gates.sqlBean" />
<!-- commented by Arun for #25046 -->
<!-- <jsp:useBean id="df"  class="com.murho.gates.defaultsBean" />-->
<!--<jsp:useBean id="su"  class="com.murho.utils.StrUtils" />-->
<!--<jsp:useBean id="ml"  class="com.murho.utils.MLogger" />-->
<!-- commented by Arun on 5 Aug for #25046  -->
<!--<jsp:useBean id="im"  class="com.murho.dao.ItemMstDAO" />-->
<jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>
<body >
<form>
  <br>
  <br>
  <br>
  <center>
    <p>
    <h3><b>Validating User...</b></h3>
<%
    String user_id   = StrUtils.fString(request.getParameter("name")).trim().toUpperCase();
    String pwd       = StrUtils.fString(request.getParameter("pwd")).trim();
    String url       = StrUtils.fString(request.getParameter("PDA")).trim();
    String encrPwd   = eb.encrypt(pwd);
    String result ="";
    boolean isExists=false;
    int statusCode=0;
 %>
<%
    //  modified by Arun on 5 Aug for #25046, used UserBean instead of ItemMstDAO
    //isExists=im.isValidUserExists(user_id);
// surrounded by Try Catch block by Arun for #25046 change
	try{
		isExists=ub.isValidUserExists(user_id);
	
		if(isExists){
			statusCode   = ub.isValidUser(user_id,encrPwd);
	    }else{
	        //statusCode =  "Invalid UserID :"+user_id+ " Please Try Again with Correct UserID"  ;
	        statusCode = 109;
	    }
		}catch(Exception e){
			String error =e.getMessage();
	    	%>
	        <jsp:forward page="index.jsp" >
	        <jsp:param name="warn" value="<%=error%>" />
	        </jsp:forward>
	     <%
		}
		MLogger.info("statusCode : " + statusCode);
    String level_name = ub.getUserLevel(user_id);
    String backBtn   = "<br><br><center><input type=\"button\" value=\"Back\" name=\"nextBtn\" onClick=\"window.location.href='javascript:history.back()'\"> ";
    
    ArrayList menulist = ub.getDropDownMenu(level_name);
	if((statusCode ==1 || statusCode ==104)&&(url.equalsIgnoreCase("Y")))  //      Valid User
	{
		String strmenulist  = ub.getPdaMenu(level_name);
		session = request.getSession();
		session.setAttribute("VALID_USER", "HSBC");
		session.setAttribute("LOGIN_USER", user_id);
		session.setAttribute("DROPDOWN_MENU",strmenulist);
		session.setAttribute("PLANT",CibaConstants.cibacompanyName);
		response.sendRedirect("pdaIndexPage.jsp?USER_ID="+user_id);
	}
	//else if block is added on 10-june-2014 for active/inactive ticket #WO0000000041526
	else if(statusCode ==115)
	{
		result = "Your account has been deactivated. Please contact the Administrator.";
   	    //out.write("<font color=\"red\"><h4><br>"+result+"</h4></font>");
		mv.insertMovHisLogger(user_id, "User_Logging",result); 
		%>
			<jsp:forward page="index.jsp" >
			<jsp:param name="warn" value="<%=result%>" />
		    </jsp:forward>
	   <%
	}	
	
	else if(statusCode ==113){  //      This else block added by Arun on 4 Oct for User level authorization check for #25046
		result = "User Group : " +level_name+ " is not authorized. Contact your administrator.";
   	    //out.write("<font color=\"red\"><h4><br>"+result+"</h4></font>");
		mv.insertMovHisLogger(user_id, "User_Logging",result); 
		%>
			<jsp:forward page="index.jsp" >
			<jsp:param name="warn" value="<%=result%>" />
		    </jsp:forward>
	   <%
	}else if(statusCode ==1 || statusCode ==101 || statusCode ==104){  //      Valid User logging in from website
		// commented by Arun for #25046 to log user name entry in logger
        //ml.info(" User : " +(String)session.getAttribute("LOGIN_USER")+ " has logged in from pc");
		if(menulist.size()< 8){
%>           <jsp:forward page="index.jsp" >
             <jsp:param name="warn" value="User Level Not Found (or) Not Authorised " />
             </jsp:forward>
<%      }
        session = request.getSession();
        session.setAttribute("VALID_USER", "HSBC");
        session.setAttribute("LOGIN_USER", user_id);
        session.setAttribute("DROPDOWN_MENU",menulist);
        session.setAttribute("PLANT",CibaConstants.cibacompanyName);
        //df.insertToLog(session, "User Logging Information","Logged in Successfully");   //  Inserting into the user log
        // commented by Arun for #25046 and added belwo if login scussful
        //mv.insertMovHisLogger(user_id, "User_Logging","User : "+user_id +"  Logged in Successfully "); 
        if(statusCode == 104){  // First time login redirect to change password if password has not expired.
        	int passwordStatus = ub.isValidPasswordAge(user_id);
        	if(passwordStatus == 111){ // account blocked due to password expiry
        		//result = "Your account is blocked, to reset contact Adminstrator";
        		result = "Your account is blocked due to password expiry. Contact Adminstrator to reset.";
        		mv.insertMovHisLogger(user_id, "User_Logging",result);
                out.write("<font color=\"red\"><h4><br>"+result+"</h4></font>");
                %>
	                <jsp:forward page="index.jsp" >
	                <jsp:param name="warn" value="<%=result%>" />
	                </jsp:forward>
   	           <%
        	}else{
        		%>
        		<jsp:forward page="firstEntry.jsp" >
            	<jsp:param name="pwd" value="<%=pwd%>" />
	            </jsp:forward>
				<%
        	}
         }else{  // not first time login, check for password aging
        	 try{
        		 String levelname = ub.getUserLevel(user_id);
        		 boolean blocked = false;
        		 //Below line commented by Arun on 26 Aug for #25046: To apply all login auth rules for all groups including ADMIN
	           //  if (!levelname.equalsIgnoreCase("ADMINISTRATOR")){
	            	 String strDate =ub.getPasswordStartDate(user_id,levelname);
		             int daysRem = ub.getDaysRemaining(strDate,levelname);
		             // changed by Arun on 27 July 2011 for #25046
		             //int minRange =ub.getMinRange(levelname);
		             int minRange =ub.getMinRange();
		             //int AgePrd =ub.getAgePrd(levelname);
		             int AgePrd =ub.getAgePrd();
		             if(daysRem > AgePrd ){
		            	 String sql =" update user_info set user_status = '1' where user_id ='"+user_id+"' " ;
		    	         ub.updateData(sql);
		    	         // message modified by Arun for #25046
		    	         //result = "Your account is Blocked,To reset Contact Adminstrator";
//		        	     result = "Your account is blocked, to reset contact Adminstrator";
						result = "Your account is blocked due to password expiry. Contact Adminstrator to reset.";
		                 //System.out.println("result :"+result);
		                 mv.insertMovHisLogger(user_id, "User_Logging",result);
		                 out.write("<font color=\"red\"><h4><br>"+result+"</h4></font>");
		                 blocked = true;
		                 %>
			                <jsp:forward page="index.jsp" >
			                <jsp:param name="warn" value="<%=result%>" />
			                </jsp:forward>
		    	         <%
		             }else if((AgePrd-daysRem) <= minRange && statusCode !=101){// Check for password expiry
		            	 int remday = AgePrd-daysRem ;
			             System.out.println("Entered::::::::::::::::::::::daysRem <= minRange ");
			             //code added by arun for #25046
			             if(remday == 0){
			            	 result = "<font <font  class=maingreen>Your password will expire today, Do you want to change password?</font>";
			             }else{
			            	 result = "<font <font  class=maingreen>Your password will expire in "+remday+ " days, Do you want to change password?</font>";
			             }
			             int m = sb.insertRecords(
					                "Update USER_INFO Set ACCESS_COUNTER = '0' where USER_ID = '" +
					                user_id + "'");
			             
			             session.setAttribute("RESULT",result);
//			           Modified by Arun for #25046
			             //response.sendRedirect("displayResult2User.jsp");
			             %>
				 		 	<jsp:forward page="displayResult2User.jsp" >
						 	<jsp:param name="pwdAboutToExpire" value="true"/>
						    </jsp:forward>
						 <%	

		             }else if(statusCode ==101){
		            	 result = "Invalid Password : Please try again with correct password ";
		            	 mv.insertMovHisLogger(user_id, "User_Logging",result); 
		                 %>
		     			<jsp:forward page="index.jsp" >
		     			<jsp:param name="warn" value="<%=result%>" />
		     		    </jsp:forward>
		     		   <%
		             }else{
		            	 MLogger.info(" User : " +user_id+ " has logged in to java website");
		            	 mv.insertMovHisLogger(user_id, "User_Logging","User : "+user_id +"  Logged in successfully from website."); 
		            	 response.sendRedirect("indexPage.jsp");  // password not expired, redirect to index page
		             }
		             
		             if(statusCode == 1 && !blocked){
//				        	set ACCESS_COUNTER = 0 in case of valid login
				            int m = sb.insertRecords(
				                "Update USER_INFO Set ACCESS_COUNTER = '0' where USER_ID = '" +
				                user_id + "'");
				            
				        }
	            // }
	            // response.sendRedirect("indexPage.jsp");
                }catch(Exception e){
                	e.printStackTrace();
                	String error =e.getMessage();
                	//out.write("Error:"+error);
                	%>
	                <jsp:forward page="index.jsp" >
	                <jsp:param name="warn" value="<%=error%>" />
	                </jsp:forward>
    	         <%
                }
          }
	}
	else{
		switch (statusCode){
			case 100: result = "Could not get Database Connection..Please Check if the DSN is correctly configured"; break;
            //case 101: result = "Invalid User ID : "+user_id+" ..  Please Try Again with Correct User ID "; break;
            // modified by Arun for #25046
            //case 101: result = "Invalid Password : Please Try Again with Correct Password "; break;
            case 101: result = "Invalid Password : Please try again with correct password."; break;
            //case 102: result = "User ID : "+user_id+ " not authorized..  Please Contact the administrator "; break;
            case 102: result = "User ID : "+user_id+ " is not authorized. Please contact the administrator."; break;
            // Message modified by Arun for #25046
            //case 103: result = "Password blocked for the User ID : "+user_id+ " ..  Please Contact the administrator "; break;
            //case 103: result = "Your account is blocked, to reset contact Administrator"; break;  // Blocked due to more than unsuccessful login attempts
            case 103: result = "Account blocked. You have exceeded maximum no. of unsuccessful login attempts. Contact Administrator to reset."; break;  // Blocked due to more than 5 unsuccessful login attempts
            case 104: result = "First Login for User ID - "+user_id+" .. Password need to be changed"; break;
            case 105: result = "Wrong Password"; break;
            case 106: result = "Access Counter Incrementing Error"; break;
            case 107: result = "Access Counter Update Error"; break;
            case 108: result = "Effective Date Not Reached"; break;
            //case 109: result = "Invalid UserID : " +user_id+ " " + " Please Try Again with Correct UserID" ; break;
            case 109: result = "Invalid UserID : " +user_id+ " " + " Please try again with correct UserID." ; break;
            // Added by Arun on 11 Aug 2011  for #25046
            case 110: result = "Your password has been reset by Admin. Please change your password." ; break;
        }
        out.write("<font color=\"red\"><h4><br>"+result+"</h4></font>");
        out.write(backBtn);
        // if condition added by Arun for #25046 : Change password after admin reset
        if(statusCode==110){
        	int passwordStatus = ub.isValidPasswordAge(user_id);
        	if(passwordStatus == 111){ // account blocked due to password expiry
        		//result = "Your account is blocked, to reset contact Adminstrator";
        		result = "Your account is blocked due to password expiry. Contact Adminstrator to reset.";
        		mv.insertMovHisLogger(user_id, "User_Logging",result);
                out.write("<font color=\"red\"><h4><br>"+result+"</h4></font>");
                %>
	                <jsp:forward page="index.jsp" >
	                <jsp:param name="warn" value="<%=result%>" />
	                </jsp:forward>
   	           <%
        	}else{
	        	 session.setAttribute("LOGIN_USER", user_id);
	        	 session.setAttribute("VALID_USER", "HSBC");
	        	 session.setAttribute("DROPDOWN_MENU",menulist);
	     		 session.setAttribute("PLANT",CibaConstants.cibacompanyName);
	        	 result = "Your password has been reset by Admin. Please change your password." ;
	        	 session.setAttribute("RESULT",result);%>
	        	 <INPUT type="hidden" name="PASSWORD" value=<%=pwd %>>
			 	<!-- response.sendRedirect("chngPwd.jsp?resetPwdByAdmin=true"); -->
			 	<jsp:forward page="chngPwd.jsp" >
			 	<jsp:param name="resetPwdByAdmin" value="true"/>
				<jsp:param name="password" value="<%=pwd%>" />
			    </jsp:forward>
        <%	}

        } else if(statusCode!=100){
        	// df.insertToLog(session, "User Logging Information",result);   //  Inserting into the user log
            mv.insertMovHisLogger(user_id, "User_Logging",result); 
            %>
			<jsp:forward page="index.jsp" >
			<jsp:param name="warn" value="<%=result%>" />
		    </jsp:forward>
		<%
        }
        
			
        
  	}
%>
</center>
</form>
<%@ include file="footer.jsp" %>