package com.murho.gates;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.murho.dao.BaseDAO;
import com.murho.db.utils.MovHisUtil;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;

/********************************************************************************************************
 *   PURPOSE           :    A class for controlling the  User Activitites
 *******************************************************************************************************/

public class userBean {
  String Quo = "\"";

  sqlBean sb;
  Generator gn;
  //StrUtils strUtils = null;
   
  public userBean() throws Exception {

    sb = new sqlBean();
    gn = new Generator();
    //strUtils = new StrUtils();
  }

  /********************************************************************************************************
   *   PURPOSE           :   Method for getting a database connection from ConnectionPool thru static method of DbBean Class
   *   PARAMETER 1 :   Nil
   *   RETURNS          :  Database Connection Object
   *******************************************************************************************************/
  public Connection getDBConnection() throws Exception {
    Connection con = DbBean.getConnection();
    return con;
  }

 
  
  /********************************************************************************************************
   *   PURPOSE           :   A method  for Validating the User name for the given User Id and Password
   *   PARAMETER 1 :   User ID
   *   PARAMETER 2 :   Password
   *   RETURNS          :   An integer identifying the User Status
   *******************************************************************************************************/

  public int isValidUser(String userid, String pwd) throws Exception {
    String sql;

    Connection con = getDBConnection();
    ResultSet rs = null;
    int returnCode = 1;
    try {

      Statement stmt = con.createStatement();
      // Query modified by Arun for #25046: to get User_status 
      //sql = "select USER_NAME,ACCESS_COUNTER,EFFECTIVE_DATE,AUTHORISE_BY,PASSWORD from USER_INFO where USER_ID = '" +
          //userid + "' and PASSWORD = '" + pwd + "' ";
      Map sysParams = getSystemParams();
      int int_loginAttempts=0;
  	  String loginAttempts = (String)sysParams.get("LOGIN_ATTEMPTS");
  	  if(null != loginAttempts || loginAttempts==""){
  		int_loginAttempts = Integer.parseInt(loginAttempts);
      }else{
          throw new Exception("Technical error occured: LOGIN_ATTEMPTS system parameter is not defined, please contact System Administrator.");
      }
  	  //Query modified to select USER_ACTIVE field on 9-june-2014 for user active/inactive ticket #WO0000000041526
      sql = "select USER_NAME,ACCESS_COUNTER,EFFECTIVE_DATE,AUTHORISE_BY,PASSWORD,USER_STATUS,USER_ACTIVE from USER_INFO where USER_ID = '" +
      userid + "' and PASSWORD = '" + pwd + "' ";
      
     MLogger.query(" "+sql.toString());
      rs = stmt.executeQuery(sql);

      if (rs != null) {
        if (rs.next()) {
        	
        	//get the user_active and check 
        	String str_userActive = rs.getString("USER_ACTIVE").toString().trim();
        	String active = "INACTIVE";
        	if(active.equals(str_userActive)) 
        	{
        		returnCode = 115;
        	}
        	
        	       	
        	else{
        		System.out.println("inside else");
        	// valid User name and password, get the access_counter
        	String str_accCounter = rs.getString("ACCESS_COUNTER");
        	int int_accCounter =0;
        	if(null != str_accCounter || str_accCounter==""){
        		int_accCounter = Integer.parseInt(str_accCounter);
	        }
        	
        	if(int_accCounter < int_loginAttempts){
        		
        		// valid login, reset counter to zero 
        		//int m = sb.insertRecords(
        	          //  "Update USER_INFO Set ACCESS_COUNTER = '0' where USER_ID = '" +
        	           // userid + "'");
        		
        		// 	  validate group authorization
           	    // Below if condition adde by Arun on 5 Oct 2011 to validate user group authorization #25046
        		returnCode = getUserLevelStatus(getUserLevel(userid));
                
                if (returnCode == 113){  // 113 return code is for user group not authorised
              	  return returnCode;
                }
                  
                //Below blcok commented by Arun for #25046 to use user_status instead of access_counter 
                /*String pwd_counter = rs.getString("ACCESS_COUNTER").trim();          
                int counter = 0;
                boolean updateFlag = true;
                try {
                  counter = Integer.parseInt(pwd_counter);
                  if (counter >=3) {
                    returnCode = 103;
                    return returnCode;//      Password Blocked
                  }
                }
                catch (NumberFormatException nfe) {
                  updateFlag = false;
              	returnCode = 104;
                }*/

                
//              Code added by Arun for #25046 to check user_status: Return 104- first time login, return 110- reset password
                String userStatus = rs.getString("USER_STATUS").trim();
                if(userStatus!= null){
              	  if(userStatus.equalsIgnoreCase("C")){// First time lgoin
              		  returnCode = 104;
              	  }else if (userStatus.equalsIgnoreCase("R")){// Reset by Admin
              		  returnCode = 110;
              	  }
          	    }
               
                // Check for user authorization
                String authoriser = rs.getString("AUTHORISE_BY");
                if (authoriser == null || authoriser.trim().equalsIgnoreCase("0")) {
                  returnCode = 102; //      User Not Authorised
                }
                
//              Below block commented by Arun for #25046 since code is not reachable here
                /*String password = rs.getString("PASSWORD").trim();
                if ( (!password.equalsIgnoreCase(pwd)) && (updateFlag == true)) {
                  returnCode = 105; //      Wrong Password
                  if (counter == 9) {
                    counter = 8; //     Preventing SQL insert error as the field length is 1 in database

                  }
                  String q = "Update USER_INFO Set ACCESS_COUNTER = '" + (++counter) +
                      "' where USER_ID = '" + userid + "'";

                  int n = sb.insertRecords(q);
                  if (n != 1) {
                    returnCode = 106; //      Error in updating the access counter
                  }
                }
                else if (!password.equalsIgnoreCase(pwd)) {
                  returnCode = 105; //      Wrong Password

                }*/
        	}else{  // Access_counter>=5, account blocked
        		returnCode = 103;
        	}
        }
        }
        else{ // Invalid pasword
        	 //  Code added by Arun for #25046: Track unsuccessful login attempts,increment access_counter
        	  String str_counter = getAccessCounter(userid);
              //System.out.println("str_counter..."+str_counter);
              int counter=0;
	          if(null != str_counter || str_counter==""){
	        	  counter = Integer.parseInt(str_counter);
	        	  counter++;
	          }
	          System.out.println("Unsucessful login attempt No. --- >"+counter);
        	  if(counter >=int_loginAttempts){
        		 returnCode = 103;
	          }else{
	        		returnCode = 101; //  Invalid Password
	          }
	        	  
	        	  int m = sb.insertRecords(
	        	            "Update USER_INFO Set ACCESS_COUNTER = '"+counter+"' where USER_ID = '" +
	        	            userid + "'");
        }
      }

     /* if (returnCode == 1) {
    	  //set ACCESS_COUNTER = 0 in case of valid login
        int m = sb.insertRecords(
            "Update USER_INFO Set ACCESS_COUNTER = '0' where USER_ID = '" +
            userid + "'");
        if (m != 1) {
          returnCode = 107; //      Error in updating the access counter
        }
      }*/

    } //   End of try
    catch (Exception e) {
      DbBean.writeError("userBean", "isValidUser()", e);
      MLogger.log(" Exception occured: isValidUser()" + e );
      throw new Exception("Technical error occured, please contact System Administrator.");

    }
    finally {
      rs.close();
      DbBean.closeConnection(con);
    }

    return returnCode;
  }

  
  /********************************************************************************************************
   *   PURPOSE           :   Method added by Arun on 5 Oct to get user group status for #25046
   *   PARAMETER 1 :   The User level
   *   RETURNS          :   status code
   *******************************************************************************************************/
  public int getUserLevelStatus(String userLevel) throws Exception {

    Connection con = getDBConnection();
    String authoriseBy = "", authoriseOn="";
    int statusCode=1;
    try {
      Statement stmt = con.createStatement();

      String q = "select distinct AUTHORISE_BY,AUTHORISE_ON from USER_LEVEL where level_name ='"+userLevel+"'";

      //System.out.println("Query.. "+q);
      ResultSet rs = stmt.executeQuery(q);
      if (rs != null) {
        while (rs.next()) {
          authoriseBy = rs.getString("AUTHORISE_BY");
          authoriseOn = rs.getString("AUTHORISE_ON");
        }
        if (authoriseBy == null || authoriseBy == "" || authoriseBy.trim().equalsIgnoreCase("0") || authoriseOn == null || authoriseOn=="" || authoriseOn.trim().equalsIgnoreCase("0")) {
        	statusCode = 113;
        }
      }
      //System.out.println("statusCode.. "+statusCode);
    } //   End of try
    catch (Exception e) {
      DbBean.writeError("userBean", "getUserLevelStatus()", e);
      MLogger.log(" Exception occured: getUserLevelStatus()" + e );
      throw new Exception("Technical error occured, please contact System Administrator.");
    }
    finally {
      DbBean.closeConnection(con);
    }

    return statusCode;
  }
  
  
  // New Method added by Arun on 10 Oct 2011 for #25046
  public String getAccessCounter(String userid) throws Exception {
	  MLogger.log(" Enter : getAccessCounter()" );
	  Connection con = getDBConnection();
	  String ac_counter="";
	    try {
	    Statement stmt = con.createStatement();
	      
	    String query = "select ACCESS_COUNTER from USER_INFO where USER_ID = '" +userid + "'";
	    ResultSet rs = stmt.executeQuery(query);
	    if (rs.next()) {
	    	ac_counter = rs.getString(1);
	     }
	        
	    } //   End of try
	    catch (SQLException e) {
	      DbBean.writeError("userBean", "getAccessCounter()", e);
	      MLogger.log(" Exception occured: getAccessCounter()" + e );
	      throw new Exception("Technical error occured, please contact System Administrator.");
	    }
	    finally {
	      DbBean.closeConnection(con);
	    }
	    MLogger.log(" Exit : getAccessCounter()" );
	    return ac_counter;
	  }
  
  
  
  //method added by Arun on 5 Aug for Login sync change in .net modules #25046
  public boolean isValidUserExists(String userid) throws Exception {

	  boolean flag=false;
	   java.sql.Connection con=null;

	   // connection
	   try
	   {
	   con=com.murho.gates.DbBean.getConnection();

	   //query
	   StringBuffer sql = new StringBuffer(" SELECT ");
	   sql.append(" 1 ");
	   sql.append(" ");
	   sql.append(" FROM  USER_INFO " );
	   sql.append(" WHERE  USER_ID = '"+userid +"'  " );

	   MLogger.query(sql.toString());
	   BaseDAO base = new BaseDAO();
	    flag= base.isExists(con,sql.toString());
	    
	   }catch(Exception e) {
	          MLogger.log(0,"######################### Exception :: isValidUserExists() : ######################### \n");
	          MLogger.log(0,""+ e.getMessage());
	          MLogger.log(0,"######################### Exception :: isValidUserExists() : ######################### \n");
	          throw e;
	    }
	    finally{
	        if (con != null) {
	           DbBean.closeConnection(con);
	    }
	   }//
	   return flag;
	 }
  
  
  //method added by Arun for Login sync change in .net modules #25046
  /********************************************************************************************************
   *   PURPOSE           :   A method  for validating Password age
   *   PARAMETER 1 :   User ID
   *   RETURNS          :   An integer identifying the password Status
   *******************************************************************************************************/

  public int isValidPasswordAge(String userid) throws Exception {
		int returnCode = 1;
		try {
	//		String result = "";
			String levelname = getUserLevel(userid);
			//if (!levelname.equalsIgnoreCase("ADMINISTRATOR")) { // check password aging for only non-Adminstartor groups
				String strDate = getPasswordStartDate(userid, levelname);
				int daysRem = getDaysRemaining(strDate, levelname);
				int minRange = getMinRange();
				int agePrd = getAgePrd();

				if (daysRem > agePrd) { // Account is blocked
					String sql = " update user_info set user_status = '1' where user_id ='"
							+ userid + "' ";
					updateData(sql);
					//result = "Your account is Blocked,To reset Contact Administrator";
					returnCode = 111;
				} else if ((agePrd - daysRem) <= minRange) { // Password will expire in (agePrd - daysRem) days
					//int remday = agePrd - daysRem;
					//result = "<font <font  class=maingreen>Password will Expire in "+ remday + "  days,</font>";
					returnCode = 112;
				} else {
					// response.sendRedirect("indexPage.jsp");
				}
			//}
		} catch (Exception e) {
		      MLogger.log(" Exception occured: isValidPasswordAge()" + e );
		      throw new Exception("Technical error occured, please contact System Administrator.");

		}

    return returnCode;
  }

  /***************************************************************************
	 * PURPOSE : A Method for getting the User Level for the given User ID
	 * PARAMETER 1 : The User ID RETURNS : The User level
	 **************************************************************************/

  public String getUserLevel(String userid) throws Exception {

    String name = "";
    String sql;
    Connection con = getDBConnection();

    try {
      Statement stmt = con.createStatement();
      sql = "select USER_NAME,USER_LEVEL from USER_INFO where USER_ID = '" +userid + "' ";
      ResultSet rs = stmt.executeQuery(sql);

      if (rs != null) {
        if (rs.next()) {
          name = rs.getString("USER_LEVEL").trim();
        }
      }
    } //   End of try
    catch (Exception e) {
      DbBean.writeError("userBean", "getUserLevel()", e);
    }
    finally {
      DbBean.closeConnection(con);
    }
    return name;
  }

  public String getUserLevel1(String userid,String deptid) throws Exception {

   String name = "";
   String sql;
   Connection con = getDBConnection();

   try {
     Statement stmt = con.createStatement();
     sql = "select USER_NAME,USER_LEVEL from USER_INFO where USER_ID = '" +userid + "' and DEPT = '" +deptid + "' ";
     ResultSet rs = stmt.executeQuery(sql);

     if (rs != null) {
       if (rs.next()) {
         name = rs.getString("USER_LEVEL").trim();
       }
     }
   } //   End of try
   catch (Exception e) {
     DbBean.writeError("userBean", "getUserLevel()", e);
   }
   finally {
     DbBean.closeConnection(con);
   }
   return name;
 }


// TO get Department
  public String getDepartment(String userid) throws Exception {

  String name = "";
  String sql;
  Connection con = getDBConnection();

  try {
    Statement stmt = con.createStatement();
    sql = "select DEPT from USER_INFO where USER_ID = '" +
        userid + "'";
    ResultSet rs = stmt.executeQuery(sql);

    if (rs != null) {
      if (rs.next()) {
        name = rs.getString("DEPT").trim();
      }
    }
  } //   End of try
  catch (Exception e) {
    DbBean.writeError("userBean", "getDepartment()", e);
  }
  finally {
    DbBean.closeConnection(con);
  }
  return name;
}

  public ArrayList getUserLevelAndDept(String userid)  throws Exception{
    //String name = "";
    String sql;
    ArrayList arrList = new ArrayList();
    Connection con = getDBConnection();

    try {
      Statement stmt = con.createStatement();
      sql = "select USER_NAME,USER_LEVEL,DEPT from USER_INFO where USER_ID = '" +
          userid + "'";
      ResultSet rs = stmt.executeQuery(sql);
      while(rs.next()){
         arrList.add(0,StrUtils.fString((String)rs.getString(1))); // userName
         arrList.add(1,StrUtils.fString((String)rs.getString(2))); // user_level
         arrList.add(2,StrUtils.fString((String)rs.getString(3))); // dept

         break;
     }
 }catch(Exception e) { }
 finally
 {
 DbBean.closeConnection(con);
 }
 return arrList;

 }

  /********************************************************************************************************
   *   PURPOSE           :   A method  for  getting all the URLs associated with a user level
   *   PARAMETER 1 :   The User level name
   *   RETURNS          :   Arraylist with URLs
   *******************************************************************************************************/

  public ArrayList getUserLevelLinks(String level_name) throws Exception {

    Connection con = getDBConnection();
    ArrayList tempList = new ArrayList();

    try {
      //group active
    	Statement stmt = con.createStatement();
      String q = "select ul.URL_NAME,ul.REMARKS,ul.AUTHORISE_BY,ul.AUTHORISE_ON,ul.GROUP_ACTIVE, ul.REASON from USER_LEVEL ul ,USER_MENU um where ul.LEVEL_NAME = '" +
          level_name + "' AND ul.URL_NAME = um.URL_NAME AND um.STATUS=1";

      ResultSet rs = stmt.executeQuery(q);
      if (rs != null) {
        String remark = "", authorise_by = "", authorise_on = "";
        //group active
        String group_active="", reason="";
        String element;
        while (rs.next()) {
          element = "";
          element = rs.getString(1);
          if (element == null) {
            element = "";
          }
          else {
            element = element.trim();
          }
          boolean b = tempList.add(element);
          remark = rs.getString(2);
          authorise_by = rs.getString(3);
          authorise_on = rs.getString(4);
          //group active
          group_active = rs.getString(5);
          reason = rs.getString(6);
        }
        if (remark == null) {
          remark = "";
        }
        else {
          remark = remark.trim();
        }
        if (authorise_by == null) {
          authorise_by = "";
        }
        else {
          authorise_by = authorise_by.trim();
        }
        if (authorise_on == null) {
          authorise_on = "";
        }
        else {
          authorise_on = gn.getDB2UserDateTime(authorise_on);
        }
        //group active
        if(group_active == null)
        {
        	group_active = "";
        }
        if(reason==null)
        {
        	reason = "";
        }
        tempList.add(remark);
        tempList.add(authorise_by);
        tempList.add(authorise_on);
        //group active
        tempList.add(group_active);
        tempList.add(reason);
      }
    } //   End of try
    catch (Exception e) {
      DbBean.writeError("userBean", "getUserLevelLinks()", e);
    }
    finally {
      DbBean.closeConnection(con);
    }

    return tempList;
  }

  /********************************************************************************************************
   *   PURPOSE           :   A method  for getting the User Menu Drop Downs
   *   PARAMETER 1 :   Level Name
   *   RETURNS          :   Arraylist containing
   *******************************************************************************************************/

  public ArrayList getDropDownMenu(String level_name) throws Exception {

    Connection con = getDBConnection();
    Statement stmt = con.createStatement();
    ArrayList al = new ArrayList();
    try {

      for (int i = 1; i <= 9; i++) {
        String q = "select TEXT,URL from USER_MENU where URL_NAME in (select URL_NAME from USER_LEVEL where LEVEL_NAME = '" +
            level_name +
            "' and AUTHORISE_BY <> '0' and URL_NAME not like 'pda%') and (COL=" +
            i + ") AND STATUS=1 order by COL,ROW";

        Hashtable menu = new Hashtable();
        ResultSet rs = stmt.executeQuery(q);
        if (rs != null) {
          String url_text, url;
          int m = 0;
          while (rs.next()) {
            Hashtable subHash = new Hashtable();

            url_text = rs.getString("TEXT").trim();
            url = rs.getString("URL").trim();

            subHash.put(url_text, url);
            menu.put(new Integer(m++), subHash);
          }
        }
        rs.close();
        al.add(menu);

      }
    } //   End of try
    catch (Exception e) {
      DbBean.writeError("userBean", "getDropDownMenu()", e);
    }
    finally {
      DbBean.closeConnection(con);
    }

    return al;
  }

  public String getPdaMenu(String level_name) throws Exception {
    Connection con = getDBConnection();
    String result = "";
    String link = "";
    Statement stmt = con.createStatement();
    try {

      String q = "select TEXT,URL from USER_MENU where URL_NAME in (select URL_NAME from USER_LEVEL where LEVEL_NAME = '" +
          level_name +
          "' and AUTHORISE_BY <> '0' and URL_NAME like 'pda%' ) order by COL,ROW";
      ResultSet rs = stmt.executeQuery(q);
      while (rs.next()) {
        String text = rs.getString("TEXT").trim();
        String url = rs.getString("URL").trim();
        link = "<input class=\"pdabutton\" type=" + Quo + "button" + Quo +
            " value=" + Quo + text + Quo + "onClick=" + Quo +
            "window.location.href='" + url + "'" + Quo + ">";
        result += "<tr>" +
            "<td align=\"center\">" + link + "</td></tr>";
      }
    }
    catch (Exception e) {
      DbBean.writeError("userBean", "getPdaMenu(String level_name)", e);
    }
    finally {
      DbBean.closeConnection(con);
    }
//         System.out.println(result);
    return result;
  }

  /********************************************************************************************************
   *   PURPOSE           :   A Method  for  checking if the User Level  or User Name is already available
   *   PARAMETER 1 :   User Level  or User Name
   *   PARAMETER 2 :   0 - User ID, Other than 0 for Level Name
   *   RETURNS          :   True if already userName/userLevel is available else false
   *******************************************************************************************************/
  public boolean isAlreadyAvail(String name, int n) throws Exception {

    Connection con = getDBConnection();
    boolean b = false;
    try {
      Statement stmt = con.createStatement();
      String sql; //  Switch sql statement based on integer parameter 'n'
      if (n == 0) {
        sql = "select USER_NAME  from USER_INFO where USER_ID='" + name + "'";
      }
      else {
        sql = "select LEVEL_NAME from USER_LEVEL where LEVEL_NAME='" + name +
            "'";

      }
      ResultSet rs = stmt.executeQuery(sql);
      b = rs.next();
    } //   End of try
    catch (Exception e) {
      DbBean.writeError("userBean", "isAlreadyAvail()", e);
    }
    finally {
      DbBean.closeConnection(con);
    }
    return b;
  }

  /********************************************************************************************************
   *   PURPOSE           :   A Method  for  deleting  the user levels
   *   PARAMETER 1 :   Level Name to be deleted
   *   PARAMETER 2 :   LevelUser is set to 1 if any user using the level name need to be checked
   *   RETURNS          :   Number of records deleted in that User Level
   *******************************************************************************************************/
  public int deletePreviousRecords(String level_name, String levelUser) throws
      Exception {
	  //group active
    if (levelUser.equalsIgnoreCase("1")) {
    	String user_active ="ACTIVE";
    	Connection con = getDBConnection();
      boolean b = false;
      try {
        Statement stmt = con.createStatement();

        String sql = "select user_id  from USER_INFO where USER_LEVEL='" +
            level_name + "' and USER_ACTIVE='" + user_active +"'";
        ResultSet rs = stmt.executeQuery(sql);
        b = rs.next();

      } //   End of try
      catch (Exception e) {
        DbBean.writeError("userBean", "deletePreviousRecords()", e);
      }
      finally {
        DbBean.closeConnection(con);
      }
      if (b) {
        return -2;
      }

    }

    String q = "delete from USER_LEVEL where LEVEL_NAME = '" + level_name + "'";
    int n = sb.insertRecords(q);

    return n;
  }

  /********************************************************************************************************
       *   PURPOSE           :   A method  for building an sql string to delete user
   *   PARAMETER 1 :   User ID to be deleted
   *   RETURNS          :   Sql String for deleting the User ID
   *******************************************************************************************************/
  public String getDeleteUserString(String user_id) throws Exception {

    String q = "delete from USER_INFO where USER_ID = '" + user_id + "'";
    return q;
  }

  /********************************************************************************************************
   *   PURPOSE           :   A method  for   getting the user details based on User ID or User Name
   *   PARAMETER 1 :   User Name or User ID is passed
   *   PARAMETER 2 :   Option specifying whether it is Name or ID ( 1 for Name, Other than 1 for ID )
   *   RETURNS          :   User Details in a Arraylist
   *******************************************************************************************************/
  public ArrayList getUserDetails(String user_ref, int opt) throws Exception {

    Connection con = getDBConnection();
    ArrayList tempList = new ArrayList();
    String q = "";
    try {

      Statement stmt = con.createStatement();
      //Below queries are modified on 27/May/2014 for fetching ACTIVE field from table
      if (opt == 1) {
        q = "select USER_ID,PASSWORD,USER_NAME,RANK,REMARKS,EFFECTIVE_DATE,USER_LEVEL,DEPT,AUTHORISE_BY,AUTHORISE_ON,DEPT,USER_ACTIVE,REASON from USER_INFO where USER_NAME = '" +
            user_ref + "'";
      }
      else {
        q = "select USER_ID,PASSWORD,USER_NAME,RANK,REMARKS,EFFECTIVE_DATE,USER_LEVEL,AUTHORISE_BY,AUTHORISE_ON,DEPT,USER_ACTIVE,REASON from USER_INFO where USER_ID = '" +
            user_ref + "'";

      }
      ResultSet rs = stmt.executeQuery(q);
      int maxlen = rs.getMetaData().getColumnCount();
      int n = 1;
      if (rs != null) {
        String element;
        if (rs.next()) {
          while (n <= maxlen) {
            element = "";
            element = rs.getString(n++);
            if (element == null) {
              element = "";
            }
            else {
              element = element.trim();
            }
            boolean b = tempList.add(element);
          }
        }
      }
    } //   End of try
    catch (Exception e) {
      DbBean.writeError("userBean", "getUserDetails()", e);
    }
    finally {
      DbBean.closeConnection(con);
    }

    return tempList;
  }

  /********************************************************************************************************
   *   PURPOSE           :   A method  for  changing  the user password
   *   PARAMETER 1 :   The User Id
   *   PARAMETER 2 :   New Password
   *   PARAMETER 3 :   Old Password
   *   RETURNS          :   1 if password is changed Successfully else - 2
   *******************************************************************************************************/
// Commented and modifed by Arun on 11 Aug 2011 for #25046
  /*public int changePassword(String user_id, String newPwd, String oldPwd) throws
      Exception {

    Connection con = getDBConnection();
    int n;
    boolean b = false;
    String q;

    try {
      Statement stmt = con.createStatement();
      q = "select USER_NAME from USER_INFO where PASSWORD = '" + oldPwd +
          "' and USER_ID = '" + user_id + "'";

      ResultSet rs = stmt.executeQuery(q);
      b = rs.next();

    } //   End of try
    catch (Exception e) {
      DbBean.writeError("userBean", "changePassword()", e);
    }
    finally {
      DbBean.closeConnection(con);
    }

    if (b) {
    String effDt = gn.getDate();
    effDt            = gn.getDBDateShort(effDt);
      q = "update USER_INFO set PASSWORD = '" + newPwd +
          "',ACCESS_COUNTER = '0',EFFECTIVE_DATE = '"+effDt+"',USER_STATUS = '0' where USER_ID  = '" + user_id + "'";
      n = sb.insertRecords(q);
      return n;
    }
    else {
      return -2;
    }
  }*/
  
//Modified by Arun on 11 Aug 2011 for #25046
		  public int changePassword(String user_id, String newPwd, String oldPwd, String oldPwdFlag) throws
		  Exception {

				Connection con = getDBConnection();
				int n;
				boolean b = false;
				String q;
		
//				 Old password check required: Change Password manually and Change password prompted upon expiry
				if (oldPwdFlag!= null && oldPwdFlag.equalsIgnoreCase("Y")){   
					try {
						Statement stmt = con.createStatement();
						q = "select USER_NAME from USER_INFO where PASSWORD = '" + oldPwd
								+ "' and USER_ID = '" + user_id + "'";
			
						ResultSet rs = stmt.executeQuery(q);
						b = rs.next();
			
					} // End of try
					catch (Exception e) {
						DbBean.writeError("userBean", "changePassword()", e);
					      MLogger.log(" Exception occured: changePassword()" + e );
					      throw new Exception("Technical error occured, please contact System Administrator.");
					} finally {
						DbBean.closeConnection(con);
					}
				}else{  // Old password check not required: First time login and Admin reset password
					b= true;
				}
		
				if (b) {
					String effDt = gn.getDate();
					effDt = gn.getDBDateShort(effDt);
					q = "update USER_INFO set PASSWORD = '" + newPwd
							+ "',ACCESS_COUNTER = '0',EFFECTIVE_DATE = '" + effDt
							+ "',USER_STATUS = '0' where USER_ID  = '" + user_id + "'";
					n = sb.insertRecords(q);
					return n;
				} else {
					return -2;
				}
	}
  
  // New method added by Arun for Login Synch change on 30 June 2011 : #25046
  /********************************************************************************************************
   *   PURPOSE           :   A method  for  setting Password Age and Range To Remind for all the groupd
   *   PARAMETER 1 :    Password Age
   *   PARAMETER 2 :   Range To Remind 
   *   RETURNS          :   no. of rows updated if update is Successful
   *******************************************************************************************************/

//	Below method is updated by Ranjana for ticket WO0000000356180
 public boolean setSystemParams(String pwdAge, String minRange, String login_attempts, String sAP_filePath, String uploadFol_Path, String delivery_No,String Pallet_id,String mtid,HttpSession session) throws
		  
//Below method is updated by jyoti for TIBCO-INC000002484471 Confirmation file automatic saved on TIBCO
 /* Commented to be taken under 3.0 for ticket WO0000000356180 */
 //public boolean setSystemParams(String pwdAge, String minRange, String login_attempts, String sAP_filePath, String uploadFol_Path,HttpSession session) throws
      Exception {
	  System.out.println("Eneterd :::::setSystemParams ");
	  ArrayList sqllist = new ArrayList();
    boolean status=false;
    String pwdAgeQuery, minRangeQuery, loginAttemptsQuery,oldVal=null,sAPFilePathQuery,uploadFilePathQuery, updateResult="",deliveryno,palletid,Mtid;

    try {
    	if(null != session.getAttribute("PWD_AGE")){
    		oldVal = (String)session.getAttribute("PWD_AGE");
    	}
    	if(null != oldVal && !oldVal.equalsIgnoreCase(pwdAge)){
	    	pwdAgeQuery = "update CONFIG_PARAM set PARAM_VALUE = " + pwdAge +" where PARAM_NAME='PWD_AGE'";
		    sqllist.add(pwdAgeQuery);
		    updateResult = " PWD_AGE:  Old Value:"+oldVal +" New Value: "+pwdAge;
    	}
    	if(null != session.getAttribute("RANGE_TO_REMIND")){
    		oldVal =  (String)session.getAttribute("RANGE_TO_REMIND");
    	}
    	if(null != oldVal && !oldVal.equalsIgnoreCase(minRange)){
		    minRangeQuery = "update CONFIG_PARAM set PARAM_VALUE = " + minRange +" where PARAM_NAME='RANGE_TO_REMIND'";
		    sqllist.add(minRangeQuery);
		    updateResult = updateResult+" RANGE_TO_REMIND:  Old Value:"+oldVal +" New Value: "+minRange;
    	}
    	if(null != session.getAttribute("LOGIN_ATTEMPTS")){
    		oldVal =  (String)session.getAttribute("LOGIN_ATTEMPTS");
    	}
    	if(null != oldVal && !oldVal.equalsIgnoreCase(login_attempts)){
		    loginAttemptsQuery = "update CONFIG_PARAM set PARAM_VALUE = " + login_attempts +" where PARAM_NAME='LOGIN_ATTEMPTS'";
		    sqllist.add(loginAttemptsQuery);
		    updateResult = updateResult+ " LOGIN_ATTEMPTS:  Old Value:"+oldVal +" New Value: "+login_attempts;
    	}
//    	Below if condition is added by jyoti for TIBCO-INC000002484471 SAP confirmation file path
    	if(null != session.getAttribute("SAP_FILE_PATH"))
    	{
    		oldVal = (String)session.getAttribute("SAP_FILE_PATH");
    	}
    	if(null != oldVal && !oldVal.equalsIgnoreCase(sAP_filePath))
    	{
    		sAPFilePathQuery = "update CONFIG_PARAM set PARAM_VALUE = '" + sAP_filePath +"' where PARAM_NAME='SAP_FILE_PATH'";
    		sqllist.add(sAPFilePathQuery);
    		updateResult = updateResult+ "SAP_FILE_PATH: Old Value:"+oldVal+"New Value: "+ sAP_filePath;
    	}
    	//Below if block is added by jyoti for TIBCO-INC000002484471
    	if(null != session.getAttribute("UPLOAD_FOLDER_PATH"));
    	{
    		oldVal =(String) session.getAttribute("UPLOAD_FOLDER_PATH");
    	}
    	if(null != oldVal && !oldVal.equalsIgnoreCase(uploadFol_Path))
    	{
    		uploadFilePathQuery = "update CONFIG_PARAM set PARAM_VALUE = '"+ uploadFol_Path +"' where PARAM_NAME='UPLOAD_FOLDER_PATH'";
    		sqllist.add(uploadFilePathQuery);
    		updateResult = updateResult+ "UPLOAD_FOLDER_PATH: Old Value:"+oldVal+"New Value: "+ uploadFol_Path;
    	}
    	//Added  by Ranjana for Inbound file generation under ticket WO0000000356180 to be taken under 3.0
    	
    	if(null!=session.getAttribute("DELIVERY_NO"))
    	{
    	oldVal=(String) session.getAttribute("DELIVERY_NO");
    	}
    	if(null!=oldVal && !oldVal.equalsIgnoreCase(delivery_No))
    	{
    		deliveryno = "update CONFIG_PARAM set PARAM_VALUE = '"+ delivery_No +"' where PARAM_NAME='DELIVERY_NO'";
    		sqllist.add(deliveryno);
    		updateResult = updateResult+ "DELIVERY_NO: Old Value:"+oldVal+"New Value: "+ delivery_No;
    	}
    	
    	if(null!=session.getAttribute("PALLET_ID"))
    	{
    	oldVal=(String) session.getAttribute("PALLET_ID");
    	}
    	if(null!=oldVal && !oldVal.equalsIgnoreCase(Pallet_id))
    	{
    		palletid = "update CONFIG_PARAM set PARAM_VALUE = '"+ Pallet_id +"' where PARAM_NAME='PALLET_ID'";
    		sqllist.add(palletid);
    		updateResult = updateResult+ "PALLET_ID: Old Value:"+oldVal+"New Value: "+ Pallet_id;
    	}
    	
    	if(null!=session.getAttribute("MTID"))
    	{
    	oldVal=(String) session.getAttribute("MTID");
    	}
    	if(null!=oldVal && !oldVal.equalsIgnoreCase(mtid))
    	{
    		Mtid = "update CONFIG_PARAM set PARAM_VALUE = '"+ mtid +"' where PARAM_NAME='MTID'";
    		sqllist.add(Mtid);
    		updateResult = updateResult+ "MTID: Old Value:"+oldVal+"New Value: "+ mtid;
    	}
    	
    	
	    status = sb.insertBatchRecords(sqllist);
	    if(status){
	    	String result ="System parameters set successfully. "+updateResult;
	    	MovHisUtil mv = new MovHisUtil();
	    	mv.insertMovHisLogger((String)session.getAttribute("LOGIN_USER"), "Set_System_Parameters",result); 
	    }
	  
    } 
    catch (Exception e) {
      DbBean.writeError("userBean", "setSystemParams()", e);
      MLogger.log(" Exception occured: setSystemParams()" + e );
      throw new Exception("Technical error occured, please contact System Administrator.");
    }
    System.out.println("Exit :::::setSystemParams ");
    return status;
    
  }


 public int resetPassword(String admin, String newPwd, String userid) throws
      Exception {

    Connection con = getDBConnection();
    int n;
    boolean b = false;
    String q;
  System.out.println("Eneterd :::::resetPassword ");
    try {
      Statement stmt = con.createStatement();
      q = "select USER_NAME from USER_INFO where   USER_ID = '" + userid + "'";

      ResultSet rs = stmt.executeQuery(q);
      b = rs.next();

    } //   End of try
    catch (Exception e) {
      DbBean.writeError("userBean", "resetPassword()", e);
      MLogger.log(" Exception occured: resetPassword()" + e );
      throw new Exception("Technical error occured, please contact System Administrator.");
    }
    finally {
      DbBean.closeConnection(con);
    }

    if (b) {
     System.out.println("Eneterd ::::: b "+ b );
     String effDt = gn.getDate();
    effDt            = gn.getDBDateShort(effDt);
    // Modified by Arun on 11 Aug 2011 for #25046 -- set user_status as 'R' when admin resets the password
      /*q = "update USER_INFO set PASSWORD = '" + newPwd +
          "',ACCESS_COUNTER = '0',EFFECTIVE_DATE = '"+effDt+"',UPDATED_BY = '"+admin+"',USER_STATUS ='0' where USER_ID  = '" + userid + "'";*/
    q = "update USER_INFO set PASSWORD = '" + newPwd +
    "',ACCESS_COUNTER = '0',EFFECTIVE_DATE = '"+effDt+"',UPDATED_BY = '"+admin+"',USER_STATUS ='R' where USER_ID  = '" + userid + "'";
    
          System.out.println("Query :::::"+q);
      n = sb.insertRecords(q);
      return n;
    }
    else {
      return -2;
    }
  }
  /********************************************************************************************************
   *   PURPOSE           :   A method  for  getting all the unauthorised Users for authorisation
   *   PARAMETER 1       :   User Name
   *   RETURNS           :   Returns String containing result of all un-authorised Users ( except those created by this user )
   *******************************************************************************************************/
  public String getUsersToAuthorise(String user) throws Exception {

    Connection con = getDBConnection();
    String userStr = "";
    try {
      Statement stmt = con.createStatement();

      String q = "select USER_ID,USER_NAME,USER_LEVEL,DEPT,EFFECTIVE_DATE,UPDATED_BY from USER_INFO where UPDATED_BY <> '" +
          user +
          "' and (AUTHORISE_BY ='null' or AUTHORISE_BY ='0') order by UPDATED_ON";
      ResultSet rs = stmt.executeQuery(q);
      int n = 1;
      if (rs != null) {
        while (rs.next()) {
          String user_id = rs.getString("USER_ID").trim();
          String uname = rs.getString("USER_NAME").trim();
          String ulev = rs.getString("USER_LEVEL").trim();
          String udept = rs.getString("DEPT").trim();
          String eff_dt = rs.getString("EFFECTIVE_DATE").trim();
          String upby = rs.getString("UPDATED_BY").trim();
          eff_dt = gn.getDB2UserDate(eff_dt); //  Converting to user readable format
          userStr += "<tr valign=\"middle\"><td align=\"center\" width=\"7%\">" +
              n++ +". </td>" +
              "<td width=\"3%\" align=\"center\"><input type=\"checkbox\" name=\"USER_ID\" value=\"" +
              user_id + "\"></td>" +
              "<td width=\"25%\"><b><a href=maintUser.jsp?view=&USER_ID=" +
              user_id.replace(' ', '+') + ">" + user_id + "</a></td>" +
              "<td width=\"25%\"><b>" + uname + "</td>" +
              "<td width=\"20%\"><b>" + ulev + "</td>" +
               "<td width=\"20%\"><b>" + udept + "</td>" +
              "<td width=\"15%\"><b>" + eff_dt + "</td>" +
              "<td width=\"15%\"><b>" + upby + "</td></tr>";

        }

      }
    } //   End of try
    catch (Exception e) {
      System.out.println(e.toString());

      DbBean.writeError("userBean", "getUsersToAuthorise()", e);
    }
    finally {
      DbBean.closeConnection(con);
    }
    return userStr;
  }

  /********************************************************************************************************
   *   PURPOSE           :   A method  for  getting all the unauthorised User Levels for authorisation
   *   PARAMETER 1 :   The User Name
   *   RETURNS          :   Returns String containing result of all un-authorised levels ( except those created by this user )
   *******************************************************************************************************/
  public String getUserLevelsToAuthorise(String user) throws Exception {

    Connection con = getDBConnection();
    String userStr = "";
    try {
      Statement stmt = con.createStatement();

      String q = "select distinct LEVEL_NAME,UPDATED_BY,UPDATED_ON from USER_LEVEL where UPDATED_BY <> '" +
          user +
          "' and (AUTHORISE_BY ='null' or AUTHORISE_BY ='0') order by UPDATED_ON";

      ResultSet rs = stmt.executeQuery(q);
      int n = 1;
      if (rs != null) {
        while (rs.next()) {
          String levelname = rs.getString("LEVEL_NAME");
          String updated_by = rs.getString("UPDATED_BY");
          String updated_on = rs.getString("UPDATED_ON");
          userStr += "<tr valign=\"middle\"><td align=\"center\" width=\"9%\">" +
              n++ +". </td>" +
              "<td width=\"1%\" align=\"center\"><input type=\"checkbox\" name=\"LEVEL_NAME\" value=\"" +
              levelname + "\"></td>" +
              "<td width=\"40%\"><b><a href=maintUserLevel.jsp?view=&LEVEL_NAME=" +
              levelname.replace(' ', '+') + ">" + levelname + "</a></td>" +
              "<td width=\"25%\"><b>" + updated_by + "</td>" +
              "<td width=\"25%\"><b>" + updated_on + "</td></tr>";
        }
      }
    } //   End of try
    catch (Exception e) {
      DbBean.writeError("userBean", "getUserLevelsToAuthorise()", e);
    }
    finally {
      DbBean.closeConnection(con);
    }

    return userStr;
  }

  /********************************************************************************************************
   *   PURPOSE     :  A method  for  getting the record count from the USER_LEVEL table for the given level name
   *   Usage       :  Used in deleting or updation of user level  --  That is if the count = 0 then the level can be deleted
   *                  else it is assumed that some user is using this level and hence can not be deleted
   *   PARAMETER 1 :  The Level Name
   *   RETURNS     :  Number of User levels using this level name
   *******************************************************************************************************/

  public int getRecordCount(String level_name) throws Exception {

    Connection con = getDBConnection();
    int count = 0;
    try {
      Statement stmt = con.createStatement();

      String q = "select count(*)  from USER_LEVEL where LEVEL_NAME = '" +
          level_name + "'";
//        System.out.println(q);
      ResultSet rs = stmt.executeQuery(q);
      if (rs != null) {
        if (rs.next()) {
          count = rs.getInt(1);
//                System.out.println(count);
        }
      }
    } //   End of try
    catch (Exception e) {
      DbBean.writeError("userBean", "getRecordCount()", e);
    }
    finally {
      DbBean.closeConnection(con);
    }

    return count;
  }
  public List getPdaUser_Level(String level_name,String DeptId,String level1, String level2) throws Exception {
   Connection con  = null;
   //String result   = "";
   //String link     = "";
   Statement stmt  = null;
   List menuList   = new ArrayList();
   try {
     con  = getDBConnection();
     stmt = con.createStatement();
     String q = "SELECT URL FROM USER_MENU WHERE URL_NAME IN "
         +"(SELECT URL_NAME FROM USER_LEVEL WHERE LEVEL_NAME IN ("
         +"SELECT USER_LEVEL FROM USER_INFO WHERE DEPT = '"+DeptId+"' AND USER_ID = '"+level_name+"')"
         +"AND URL_NAME LIKE 'PDA%' AND COL ='"+level1+"' AND ROW = '"+level2+"')";
     //System.out.println("q :"+q);
     ResultSet rs = stmt.executeQuery(q);
     while (rs.next()) {
       String url    = StrUtils.fString(rs.getString("URL"));
       menuList.add(new String(url));
     }
   }
   catch (Exception e) {
     DbBean.writeError("userBean", "getPdaMenu(String level_name)", e);
   }
   finally {
     DbBean.closeConnection(con);
   }
//         System.out.println(result);
   return menuList;
 }


public String  getuserplantcode(String username) throws Exception{

 ResultSet rs = null;
 Connection con = null;
 //String doDetails = null;
 String result = null;
 try {
   con = DbBean.getConnection();
   Statement stmt = con.createStatement();

 String sQry= "SELECT DEPT FROM USER_INFO WHERE USER_ID = '"+username+"' ";
 System.out.println("Entered1....");

   rs = stmt.executeQuery(sQry);
    String dept ="";
   result = "<?xml version='1.0' encoding='utf-8'?>";
   while (rs.next()) {

    dept    = StrUtils.fString(rs.getString("DEPT"));
     }
     result += "<userplant>" + dept + "</userplant>";
    System.out.println("Out Side entered in to resultSet");

}catch(Exception e){e.printStackTrace();}
finally {
        DbBean.closeConnection(con);
       }

return result;

}

  public String getPasswordStartDate(String user,String levelname) throws Exception {

  Connection con = getDBConnection();  
  String StrDate ="";
  try { 
   
      Statement stmt = con.createStatement();

      String q = "select effective_date  from USER_INFO where USER_ID = '" +
          user + "' and USER_LEVEL ='"+levelname+"'";
//        System.out.println(q);
      ResultSet rs = stmt.executeQuery(q);
   
        if (rs.next()) {
          StrDate = rs.getString(1);
          System.out.println(" effective_date :"+StrDate);
        }
    } //   End of try
    catch (Exception e) {
      DbBean.writeError("userBean", "getPasswordStartDate()", e);
    }
    finally {
      DbBean.closeConnection(con);
    }

    return StrDate;
  }

public int getDaysRemaining(String strDate,String levelname) throws Exception {

  Connection con = getDBConnection();
   //boolean flagAllow = false;     

  int cnt =0;
    try {
       String strDiff = " select DATEDIFF(day, '"+strDate+"', getDate()) as DIFF  ";
       System.out.println("strDiff >>"+strDiff); 
       Statement stmt1 = con.createStatement();
         ResultSet rs2 = stmt1.executeQuery(strDiff);
         if (rs2.next()) {
          System.out.println("Entered rs2"); 
          cnt = rs2.getInt("DIFF");
           System.out.println("cnt >"+cnt);
        }
      
       
    } //   End of try
    catch (Exception e) {
      DbBean.writeError("userBean", "getDaysRemaining()", e);
    }
    finally {
      DbBean.closeConnection(con);
    }
    return cnt;
  }

//original method commented by Arun on 25 July 2011 for #25046
/*
public int getMinRange(String levelname) throws Exception {

  Connection con = getDBConnection();
  boolean flagAllow = false;     
  String StrDate ="";
  int ageprd = 0;
  int minR = 0;
  int cnt =0;
    try {
    Statement stmt = con.createStatement();
      
    String strCount = "select AGE_PRD,MIN_RANGE  from PA_CON_TAB where  LEVEL_NAME ='"+levelname+"'";
    ResultSet rs1 = stmt.executeQuery(strCount);
    if (rs1.next()) {
          ageprd = rs1.getInt(1);
          minR = rs1.getInt(2);
     }
        
    } //   End of try
    catch (Exception e) {
      DbBean.writeError("userBean", "getMinRange()", e);
    }
    finally {
      DbBean.closeConnection(con);
    }
    return minR;
  }*/



//Method modified by Arun on 25 July 2011 for #25046
public int getMinRange() throws Exception {
	MLogger.log(" Enter : getMinRange()" );
	  Connection con = getDBConnection();
	  String minRange ;
	  int intMinRange = 0;
	    try {
	    Statement stmt = con.createStatement();
	      
	    String strCount = "select param_value from CONFIG_PARAM where param_name='RANGE_TO_REMIND'";
	    ResultSet rs1 = stmt.executeQuery(strCount);
	    if (rs1.next()) {
	    	minRange = rs1.getString(1);
	          intMinRange = Integer.parseInt(minRange);
	     }
	        
	    } //   End of try
	    catch (Exception e) {
		      DbBean.writeError("userBean", "getMinRange()", e);
		      MLogger.log(" Exception occured: getMinRange()" + e );
		      throw new Exception("Technical error occured, please contact System Administrator.");
		}
	    finally {
	      DbBean.closeConnection(con);
	    }
	    MLogger.log(" Exit : getMinRange()" );
	    return intMinRange;
	  }





  // original method commented by Arun on 25 July 2011 for #25046
 /* public int getAgePrd(String levelname) throws Exception {

  Connection con = getDBConnection();
  boolean flagAllow = false;     
  String StrDate ="";
  int ageprd = 0;
  int minR = 0;
  int cnt =0;
    try {
    Statement stmt = con.createStatement();
      
    String strCount = "select AGE_PRD,MIN_RANGE  from PA_CON_TAB where  LEVEL_NAME ='"+levelname+"'";
    ResultSet rs1 = stmt.executeQuery(strCount);
    if (rs1.next()) {
          ageprd = rs1.getInt(1);
          minR = rs1.getInt(2);
     }
        
    } //   End of try
    catch (Exception e) {
      DbBean.writeError("userBean", "getMinRange()", e);
    }
    finally {
      DbBean.closeConnection(con);
    }
    return ageprd;
  }
  */
// Method modified by Arun on 25 July 2011 for #25046
  public int getAgePrd() throws Exception {
	  MLogger.log(" Enter : getAgePrd()" );
	  Connection con = getDBConnection();
	  String ageprd;
	  int intAgePrd=0;
	    try {
	    Statement stmt = con.createStatement();
	      
	    String strCount = "select PARAM_VALUE from CONFIG_PARAM where param_name='PWD_AGE'";
	    ResultSet rs1 = stmt.executeQuery(strCount);
	    if (rs1.next()) {
	          ageprd = rs1.getString(1);
	          intAgePrd = Integer.parseInt(ageprd);
	     }
	        
	    } //   End of try
	    catch (SQLException e) {
	      DbBean.writeError("userBean", "getAgePrd()", e);
	      MLogger.log(" Exception occured: getAgePrd()" + e );
	      throw new Exception("Technical error occured, please contact System Administrator.");
	    }
	    finally {
	      DbBean.closeConnection(con);
	    }
	    MLogger.log(" Exit : getAgePrd()" );
	    return intAgePrd;
	  }
  
  public int getPasswordChangeCount(String user,String levelname) throws Exception {

  Connection con = getDBConnection();
   //boolean flagAllow = false;     
  String StrDate ="";
  int ageprd = 0;
  int minR = 0;
  int cnt =0;
    int status =0;
    try {
      Statement stmt = con.createStatement();

      String q = "select effective_date  from USER_INFO where USER_ID = '" +
          user + "' and USER_LEVEL ='"+levelname+"'";
//        System.out.println(q);
      ResultSet rs = stmt.executeQuery(q);
   
        if (rs.next()) {
          StrDate = rs.getString(1);
        }
        
    String strCount = "select AGE_PRD,MIN_RANGE  from PA_CON_TAB where  LEVEL_NAME ='"+levelname+"'";
    ResultSet rs1 = stmt.executeQuery(strCount);
    if (rs1.next()) {
          ageprd = rs1.getInt(1); 
           minR = rs1.getInt(2);
         }
        
         String strDiff = " select DATEDIFF(day, '"+StrDate+"', getDate()) as DIFF  ";
         Statement stmt1 = con.createStatement();
         ResultSet rs2 = stmt1.executeQuery(strDiff);
         if (rs2.next()) {
           cnt = rs2.getInt("DIFF");
        }
   
        if((cnt<=ageprd )&& (ageprd-cnt<=minR))
       
        {
         status = 1;
        }
         if(ageprd > cnt)
        {
         status = 2;
        }
         if( cnt > ageprd)
        {
         status = 3;
         String sql =" update user_info set user_status = '1' where user_id ='"+user+"' " ;
         boolean updated = updateData(sql);
        }
    } //   End of try
    catch (Exception e) {
 e.printStackTrace();
    }
    finally {
      DbBean.closeConnection(con);
    }

    return status;
  }


  public boolean updateData( String sql) throws Exception {
  Connection con = getDBConnection();
    boolean flag = false;
    Statement stmt = null;
    int updateCount = 0;
    try {
      stmt = con.createStatement();
      //     MLogger.log(" Sql : "+ sql );
      updateCount = stmt.executeUpdate(sql);
      //     MLogger.log(" ************** After updation : "+ updateCount );
      if (updateCount <= 0) {
        flag = false;
        throw new Exception("Unable to update!");
      }
      else {
        flag = true;
      }
    }
    catch (Exception e) {
      //   MLogger.exception(this,e);
      throw e;
    }
    finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
        if (con != null) {
          DbBean.closeConnection(con);
        }
      }
      catch (Exception e) {
        throw e;
      }
    }
    return flag;
  }
  
  /********************************************************************************************************
   *   PURPOSE           :   A method  for getting system parameters
   *   RETURNS          :   Arraylist of parameter values
   *******************************************************************************************************/
// Method added by Arun on 13 Sept for #25046: Login ticket
  public Map getSystemParams() throws Exception {

    Connection con = getDBConnection();
    Statement stmt = con.createStatement();
    Map params = Collections.EMPTY_MAP;   // Use empty_map, no need to handle null case from caller
 
    try {
        String paramQuery = "select PARAM_NAME, PARAM_VALUE from CONFIG_PARAM";
        ResultSet rs = stmt.executeQuery(paramQuery);
        if (rs != null) {
          String param_name, param_value;
          params = new HashMap();
          while (rs.next()) {
            param_name = rs.getString("PARAM_NAME").trim();
            param_value = rs.getString("PARAM_VALUE").trim();
            params.put(param_name, param_value);
          }
        }
        rs.close();
    } //   End of try
    catch (Exception e) {
      DbBean.writeError("userBean", "getSystemParams()", e);
      MLogger.log(" Exception occured: setSystemParams()" + e );
      throw new Exception("Technical error occured, please contact System Administrator.");

    }
    finally {
      DbBean.closeConnection(con);
    }
    return params;
  }
}
