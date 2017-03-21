<%@ include file="header.jsp"%>
<html>
  <head>
    <title>Saving to the database</title>
    <link rel="stylesheet" href="css/style.css"/>
  </head>
  <%@ include file="body.jsp"%>
  <body>
    <form name="form1">
      <jsp:useBean id="misc" class="com.murho.gates.miscBean"/>
      <jsp:useBean id="gn" class="com.murho.gates.Generator"/>
      <jsp:useBean id="sb" class="com.murho.gates.sqlBean"/>
      <jsp:useBean id="ub" class="com.murho.gates.userBean"/>
      <jsp:useBean id="eb" class="com.murho.gates.encryptBean"/>
      <jsp:useBean id="tl" class="com.murho.gates.TableList"/>
      <jsp:useBean id="df" class="com.murho.gates.defaultsBean"/>
      <jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>
      <% 
// Getting the parameters of USER_INFO from external file
    //String action  = request.getParameter("Submit").trim();
    String action  = request.getParameter("Submit").trim();
    String user_id = request.getParameter("USER_ID");
     String user_login=(String)session.getAttribute("LOGIN_USER");
     // Added by Arun on 29 Sept for #25046 : To include remark field in audit trail
     String remarks=request.getParameter("REMARKS");
    ArrayList alUserInfo  = tl.getTableArray("USER_INFO");
    ArrayList ralUserInfo = new ArrayList();
    String result ="", result1 ="", result2="", sql="";
    int n;
   

    if(action.equalsIgnoreCase("delete"))
    {
     sql = ub.getDeleteUserString(user_id);
     n = sb.insertRecords(sql);
     if(n==1){ result = "<font class=maingreen>The "+user_id+" User ID has been successfully deleted from database</font><br><br><center>"+
                              "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='indexPage.jsp'\">";
      mv.insertMovHisLogger(user_login, "DELETE_USER", result);
     // df.insertToMovLog(user_login, "CREATE_USER", result);
      
     }else{     result = "<font  class=mainred> Error in deleting..Could not delete User - "+user_id+" <br><br><center>"+
                        "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                        "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='indexPage.jsp'\">";

            //  df.insertToLog(session, "Deleting User Account", result);   //  Inserting into the user log
                
              }
           //   mv.insertMovHisLogger(user_login, "DELETE_USER", result);
%>
      <jsp:forward page="displayResult.jsp">
        <jsp:param name="RESULT" value="<%=result%>"/>
      </jsp:forward>
      <% 
    }

// Getting the result list size as main list - 8 eliminating au_by/on, up_by/on, en_by/on, access_counter,user_status

    int arrlength = alUserInfo.size() - 8;
    for(int i=0; i < arrlength; i++)
    {
        String str = request.getParameter((String)alUserInfo.get(i));
        ralUserInfo.add(str);

    }


    String pwd       = request.getParameter("PASSWORD");
    String user_name = request.getParameter("USER_NAME").trim().toUpperCase();
    String effDt     = request.getParameter("EFFECTIVE_DATE");
    String encrflag  = request.getParameter("ENCRYPT_FLAG");
	//added on 5-june-2014 for user active/inactive ticket #WO0000000041526
	String active = request.getParameter("USER_ACTIVE");
	String old_status = request.getParameter("OLD_STATUS");
	String accessCtr = "0";

    if(encrflag.equalsIgnoreCase("1"))
    {
        pwd          = eb.encrypt(pwd);             //  Encrypting the password
        // Below line commented by Arun for #25046: Use User_status column instead access_counter for first time login, insert access_counter as '0'
        //accessCtr    = "C";
    }
	//Commented on 6-june-2014 for user active/deactive ticket #WO0000000041526
	//no need to check effDt is null or < 8, it is now disabled field
    //if ((effDt == null ) || (effDt.length()< 8)) 
    effDt = gn.getDate();
    effDt = gn.getDBDateShort(effDt);
	//Commented on 6-june-2014 for user active/deactive ticket #WO0000000041526
	//to insert effDt as default date 
    // boolean b = misc.isValid(effDt,"0");    //  Check if the effective date is valid
	//System.out.println("b..."+b);
     ///   if(b == false)
      //  {
      //      String redirect =  " Error Occurred ..!! Effective Date can not be less than current date <br><br><center>";
                              // "<input type=button value=Back onClick=window.location.href='javascript:history.back()'>";
      //      df.insertToLog(session, "Updating User Account", redirect);
		//	System.out.println("redirect..."+redirect);
			//response.sendRedirect("displayResult.jsp?RESULT="+redirect);
%> <%-- 
     		 <input type="hidden" name="RESULT" value="<%=redirect%>"/>
      		<script language="JavaScript">
    		document.form1.action = "displayResult.jsp";
    		document.form1.submit();
			</script>
--%>
      <% 
       // }

    ralUserInfo.set(1,pwd);   //    Setting Encrypted password
    ralUserInfo.set(2,user_name);   //    Capitalizing the User Name
    ralUserInfo.set(5,effDt); //    User Effective Date
    ralUserInfo.add(accessCtr);    //     Access Counter
    // Below line modified by Arun for #25046: Set user status as 'C' for new user
    //ralUserInfo.add("0");    //     User Status
    ralUserInfo.add("C");
    
	//  Below lines are added on 11-june-2014 for user active/inactive ticket #WO0000000041526
	ralUserInfo.set(8, active);
	
	
    String enrolledBy,enrolledOn,updatedBy,updatedOn;
    enrolledBy = (String)session.getAttribute("LOGIN_USER");
    enrolledOn = gn.getDateTime();
    updatedBy  = enrolledBy;
    updatedOn  = enrolledOn;

    if("save".equalsIgnoreCase(action))
    {
//      Add  enrolled_by/on    and   updated_by/on

    ralUserInfo.add(enrolledBy);    ralUserInfo.add(enrolledOn);
    ralUserInfo.add(updatedBy);     ralUserInfo.add(updatedOn);
    ralUserInfo.add("0"); ralUserInfo.add("0"); //      Setting the Authorisation to null
    ralUserInfo.set(9,"");
    
        if(!ub.isAlreadyAvail(user_id,0))
        {
         sql = "insert into USER_INFO("+tl.getFieldString(alUserInfo)+") values ("+tl.getValueString(ralUserInfo)+")";
         n = sb.insertRecords(sql);
         if(n==1) {
         
       //  df.insertToLog(enrolledBy, "CREATE_USER", result);
          result = "<font class=maingreen>The "+user_id+" User ID has been successfully added in database</font><br><br><center>"+
                           "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='indexPage.jsp'\">";
         // result1 added by Arun on 29 Sept for #25046 : To include remark field in audit trail while new user creation
          result1 = result + " Remarks:"+ remarks;   	
		  mv.insertMovHisLogger(enrolledBy, "CREATE_USER", result1);
		//Below condition is added on 6-june-2014 for user active/inactive ticket #WO0000000041526
		result2 = result + " Status: Active";  
		mv.insertMovHisLogger(user_login,"USER_STATUS",result2);
                   }
         else{     result = "<font  class=mainred> Could not add User - "+user_id+" <br><br><center>"+
                        "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                        "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='indexPage.jsp'\">";
}
        }
        else result =  "<font  class=mainred>User ID already available..Please Choose a different USER ID <br>  Could not add User - "+user_id+" <br><br><center>"+
                        "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                        "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='indexPage.jsp'\">";

              // df.insertToLog(session, "Adding User Account", result);   //  Inserting into the user log
           //   mv.insertMovHisLogger(enrolledBy, "CREATE_USER", result);
    }
    else if("update".equalsIgnoreCase(action))
    {
//      Add   up_by/on

    ralUserInfo.add(updatedBy);
    ralUserInfo.add(updatedOn);
    ralUserInfo.add("0"); ralUserInfo.add("0"); //      Setting the Authorisation to null
//      Remove  cr_by/on

     //alUserInfo.remove(9); alUserInfo.remove(9);
      
     alUserInfo.remove(10); alUserInfo.remove(10);
//   Below condition is added on 6-june-2014 for user active/inactive ticket #WO0000000041526
		
		if((ralUserInfo.get(8).toString()).equals("ACTIVE"))
		{
			ralUserInfo.set(9,"");
		}
	System.out.println("before updating the table");
	for(int i=0;i<ralUserInfo.size();i++)
	{
		System.out.println("values+"+ ralUserInfo.get(i));		
	}
     sql = "update USER_INFO set "+tl.buildUpdateString(alUserInfo,ralUserInfo)+" where USER_ID='"+user_id+"'";
     n = sb.insertRecords(sql);
     if(n==1){ result = "<font color=\"green\">The "+user_id+" User ID has been successfully updated in database</font><br><br><center>"+
                        "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='indexPage.jsp'\">";

           mv.insertMovHisLogger(user_login, "UPDATE_USER", result);
//         Below condition is added on 6-june-2014 for user active/inactive ticket #WO0000000041526
           if(!old_status.equals(ralUserInfo.get(8).toString()))
           {
        	   String remark = "The user "+user_id+" has been "+ralUserInfo.get(8);
        	   mv.insertMovHisLogger(user_login,"USER_STATUS",remark);
           }
           
           
          //     df.insertToMovLog(enrolledBy, "CREATE_USER", result);
               
     }else{     result = "<font color=\"red\"> Could not update User - "+user_id+" <br><br><center>"+
                        "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                        "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='indexPage.jsp'\">";

            //  df.insertToLog(session, "Updating User Account", result);   //  Inserting into the user log
               
}
   //   mv.insertMovHisLogger(user_login, "UPDATE_USER", result);
    }
             result = "Saving the User Information to the database...<br><h3>"+result;
             session.setAttribute("RESULT",result);
            // response.sendRedirect("displayResult2User.jsp");
            // System.out.println("result : " +result);
%>
      <script language="JavaScript">
    document.form1.action = "displayResult2User.jsp";
    document.form1.submit();

</script>
      <%@ include file="footer.jsp"%>
    </form>
  </body>
</html>
