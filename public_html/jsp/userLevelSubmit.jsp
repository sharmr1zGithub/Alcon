<%@ include file="header.jsp" %>
<%@ include file="body.jsp" %>

<jsp:useBean id="gn"  class="com.murho.gates.Generator" />
<jsp:useBean id="sb"  class="com.murho.gates.sqlBean" />
<jsp:useBean id="tl"  class="com.murho.gates.TableList" />
<jsp:useBean id="ub"  class="com.murho.gates.userBean" />
<jsp:useBean id="df"  class="com.murho.gates.defaultsBean" />
 <jsp:useBean id="mv" class="com.murho.db.utils.MovHisUtil"/>
<br><br>
<center><h3>
<%! String okBtn,nextPage; %>
<%
    String urls[]     = request.getParameterValues("URL");
    String level_name = request.getParameter("LEVEL_NAME");
    String remarks    = request.getParameter("REMARKS");
    String pwdage    = request.getParameter("PWDAGE");
    String minRange    = request.getParameter("MINRANGE");
    String action     = request.getParameter("Action");
//  added on 5-june-2014 for user group active/inactive ticket #WO0000000041526
    String group_active = request.getParameter("GROUP_ACTIVE");
    String reason = request.getParameter("REASON");
    String old_status = request.getParameter("OLD_STATUS");
    
    String user = (String) session.getAttribute("LOGIN_USER");
    String result="";
    String result1="";
    String result2="";
    okBtn = "<br><br><center><input type=\"button\" value=\"  OK  \" onClick=\"window.location.href='indexPage.jsp'\">";
    nextPage = "<br><br><center><input type=\"button\" value=\"Back\" name=\"nextBtn\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                "<input type=\"button\" value=\"Cancel\" name=\"cancelBtn\" onClick=\"window.location.href='indexPage.jsp'\">";

    if(action.equalsIgnoreCase("delete"))
    {
        int n = ub.deletePreviousRecords(level_name,"1");
        if(n==-2) //  User available for selected level
        {
              result = "<font  class=mainred>One or more users are using the level - '"+level_name+"'<br><font class=mainred> Could not delete level..</font> "+
                "<br><br><center><input type=\"button\" value=\"Cancel\" name=\"nextBtn\" onClick=\"window.location.href='indexPage.jsp'\"> ";
        }
        else if(n>0)  result = "<font  class=maingreen>The '"+level_name+"' User Level has been successfully deleted from database</font>"+okBtn;

       // df.insertToLog(session, "Deleting User Level", result);   //  Inserting into the user log
       mv.insertMovHisLogger(user, "User_Group", result);  

    }
    else if(urls==null || urls.length==1)
	{
    	result =" <font class=mainred> No levels selected to add/update the database..."+nextPage;
    }
    else if((ub.isAlreadyAvail(level_name,1)) && (!action.equalsIgnoreCase("update")))    //  Checking if the level name exists already
    {
        result =" <font  class=mainred> The Level Name '"+level_name+"'already exists in database ...<br>Please choose a different name"+nextPage;
        // df.insertToLog(session, "Adding User Level", result);   //  Inserting into the user log
        //    mv.insertMovHisLogger(user, "Adding User Level", result);  
    }
    else
    {
		//  Building  USER_LEVEL field string
        int success = 0; int failure =0;
        ArrayList userField = tl.getTableArray("USER_LEVEL");
		//  Declaring  arraylists for value Strings
        String createdBy = (String)session.getAttribute("LOGIN_USER");
        String updatedBy = createdBy;
        String createdOn;
        String updatedOn;
        ArrayList userValue = new ArrayList();    //      Arraylist   for building value string for USER_LEVEL
        ArrayList sqllist = new ArrayList();      //      ArrayList  for holding all the insert statements
        ArrayList sqlpwdag = new ArrayList(); 
        for( int i=0; i< urls.length; i++)
        {
            String url_name = urls[i];
            createdOn = gn.getDateTime();
            updatedOn = createdOn;
			// Building Insert Strings
            if(!userValue.isEmpty()) userValue.clear();
            userValue.add(level_name);
            userValue.add(url_name);
            userValue.add(remarks);
            userValue.add(createdBy);
            userValue.add(createdOn);
            userValue.add(updatedBy);
            userValue.add(updatedOn);
            userValue.add("0");     //      Authorise_by
            userValue.add("0");     //      Authorise_on
			//below line is added on 4-july-2014 for user group active/inactive ticket #WO0000000041526
			userValue.add(group_active);
            if(group_active.equals("ACTIVE"))
            {
            	userValue.add("");
            }
            else{			
            userValue.add(reason);}
           
            
            
            String sqlInsert = "insert into USER_LEVEL("+tl.getFieldString(userField)+") values ("+tl.getValueString(userValue)+")";
            sqllist.add(sqlInsert); //  Inserting records in USER_LEVEL
        } //    End of for
        boolean b = false;
        int n = 0;
        if(action.equalsIgnoreCase("update"))
        {
        	
		//below line is added on 4-july-2014 for user group active/inactive ticket #WO0000000041526
            if(group_active.equals("INACTIVE"))
            {
            	
            	n = ub.deletePreviousRecords(level_name,"1");
            	if(n==-2) //  User available for selected level
                { result2 = "<font  class=mainred>User group cannot be INACTIVE. - '"+level_name+"'<br><font class=mainred> There are some users still in ACTIVE status...</font> "+
                "<br><br><center><input type=\"button\" value=\"Cancel\" name=\"nextBtn\" onClick=\"window.location.href='indexPage.jsp'\"> ";
            
                }
                else
                {
                	b = sb.insertBatchRecords(sqllist);
                }
            }
		//END
            else{
        	n = ub.deletePreviousRecords(level_name,"0");
            if(n>0)  b = sb.insertBatchRecords(sqllist);
            }
//          below line is added on 4-july-2014 for user group active/inactive ticket #WO0000000041526
            if((!old_status.equals(group_active))&&(n!=-2))
            {
         	   String remark = "The User Group "+level_name+" has been "+group_active;
         	   mv.insertMovHisLogger(user,"GROUP_STATUS",remark);
            }
         }
        else b = sb.insertBatchRecords(sqllist);
        
		// Following block commented by Arun for #25046 : New table CONFIG_PARAM will be used to set pwdage and minRange
        //	if(b)
		// {
				// String pwdAgeInsert ="insert into PA_CON_TAB(LEVEL_NAME,AGE_PRD,MIN_RANGE)values('"+level_name+"','"+pwdage+"','"+minRange+"')";
				// Modified by Arun for Login synch change # 25046 on 21 July 2011  
				// Do not use pwdage, minRange or set 0,0 since column type is int- will set from 'Set System Parameters' screen
       			// String pwdAgeInsert ="insert into PA_CON_TAB(LEVEL_NAME)values('"+level_name+"')";
       			// sqlpwdag.add(pwdAgeInsert);
				// boolean c=  sb.insertBatchRecords(sqlpwdag);
		        //   System.out.println("insertBatchRecords :: "+c);
		        //   c = true;
        if(b)
        {
  			//if (c){
            result = "<font <font  class=maingreen>The User Group '"+level_name+"' has been added/updated to the <br>database.."+
                                "Authorisation is required for further processing</font>"+okBtn;
//          result1 added by Arun on 29 Sept for #25046 : To include remark field in audit trail while new group creation
            result1 = result + " Remarks:"+ remarks;
            mv.insertMovHisLogger(createdBy, "User_Group", result1);
			
			//}
		}
		else 
		{
        	if(n==-2)
        	result = result2;
        	else
			result = "<font <font  class=mainred>Error occured in performing database operation.. <br> Please try again</font>"+nextPage;
		}
        // df.insertToLog(session, "Adding/Updating User Level", result);   //  Inserting into the user log
    }   //      End of else

    result = "<h3>"+result;
    session.setAttribute("RESULT",result);
    response.sendRedirect("displayResult2User.jsp");
%>

<%@ include file="footer.jsp" %>

