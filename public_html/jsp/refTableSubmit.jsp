<%@ include file="header.jsp" %>

<%@ include file="body.jsp" %>

<%! String result; %>

<jsp:useBean id="gn"  class="com.murho.gates.Generator" />
<jsp:useBean id="sb"  class="com.murho.gates.sqlBean" />
<jsp:useBean id="tl"  class="com.murho.gates.TableList" />
<jsp:useBean id="df"  class="com.murho.gates.defaultsBean" />

<%

    String okBtn = "<input type=\"button\" value=\" OK \" name=\"cancelBtn\" onClick=\"window.location.href='indexPage.jsp'\">";
    String backBtn   ="<input type=\"button\" value=\"Back\" name=\"backBtn\" onClick=\"window.location.href='javascript:history.back()'\">";

    ArrayList al = tl.getTableArray("REF_TABLE");
    ArrayList ral = new ArrayList();

    int arrlength = al.size() - 5;

    for(int i=0; i < arrlength; i++)
    {
        String str = request.getParameter((String)al.get(i));
        ral.add(str);
    }
        String action     = request.getParameter("submitBtn").trim();
        String field_name = request.getParameter("FIELD_NAME");
        String code       = request.getParameter("OPTION_CODE");
        String old_code   = request.getParameter("OLD_CODE");
        String show_order = request.getParameter("SHOW_ORDER");

        int order;
        try {
	     		order         =  Integer.parseInt(show_order);
            }
            catch (NumberFormatException ne)
                {
                    result = "<font class=mainred>Display Order could not be parsed as a number <br> Please try again..!! <br><br><center>"+backBtn;
%>
     	   <jsp:forward page="displayResult.jsp" >
	        	<jsp:param name="RESULT" value="<%=result%>" />
                    </jsp:forward>
<%            }

    String sql; int n;

    if("save".equalsIgnoreCase(action))
    {
        ral.add((String)session.getAttribute("LOGIN_USER"));   //   Created By
	    ral.add(gn.getDateTime());                           //    Created On
        ral.add((String)session.getAttribute("LOGIN_USER"));  //    Updated By
	    ral.add(gn.getDateTime());                          //      Updated On

        sql = "insert into REF_TABLE ("+tl.getFieldString(al)+") values ("+tl.getValueString(ral)+","+order+")";

        n = sb.insertRecords(sql);
	if(n==1) result = "<font class=maingreen>Successfully added "+field_name+" - "+code+" to the database</font><br><br><center>"+okBtn;
	else   	result = "<font class=mainred> Error in adding "+field_name+" - "+code+" to database <br> please try again</font><br><br><center>"+backBtn+
                        " <input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='indexPage.jsp'\">";

                 df.insertToLog(session, "Adding Fields in REF_TABLE", result);   //  Inserting into the user log
    }
    else if("update".equalsIgnoreCase(action))
    {
        al.remove(3); // Removing Created By
        al.remove(3); // Removing Created On

        ral.add((String)session.getAttribute("LOGIN_USER"));  //    Updated By
	ral.add(gn.getDateTime());                          //      Updated On

        sql = "update REF_TABLE set "+tl.buildUpdateString(al,ral)+", SHOW_ORDER = "+order+" where FIELD_NAME='"+field_name+"' and OPTION_CODE='"+old_code+"'";

        n = sb.insertRecords(sql);
        if(n>=1) result = "<font class=maingreen>"+field_name+" - "+old_code+" has been successfully updated in database</font><br><br><center>"+okBtn;

        else     result = "<font class=mainred>Could not update "+field_name+" - "+old_code+"  <br><br><center>"+backBtn+
                        " <input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='indexPage.jsp'\">";

              df.insertToLog(session, "Updating fieldsm in Reference Table", result);   //  Inserting into the user log
    }
    else if("delete".equalsIgnoreCase(action))
    {

        sql = "delete from REF_TABLE where FIELD_NAME='"+field_name+"' and OPTION_CODE='"+old_code+"'";

        n = sb.insertRecords(sql);
        if(n>=1) result = "<font class=maingreen>"+field_name+" - "+old_code+" has been successfully deleted from database</font><br><br><center>"+okBtn;

        else     result = "<font class=mainred> Could not delete "+field_name+" - "+old_code+" <br><br><center>"+backBtn+
                        " <input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='indexPage.jsp'\">";

              df.insertToLog(session, "Deleting fields in Reference Table", result);   //  Inserting into the user log
    }

                result = "<h3><b>"+result;

                session.setAttribute("RESULT",result);
                response.sendRedirect("displayResult2User.jsp");

%>

<%@ include file="footer.jsp" %>
