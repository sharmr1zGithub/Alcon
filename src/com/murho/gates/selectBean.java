package com.murho.gates;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

/********************************************************************************************************
*   PURPOSE           :   Class used in forming the strings of Select Item ( List Box ) of the HTML
*******************************************************************************************************/
public class selectBean {


    Properties obpr;
    InputStream obip;

    public selectBean() throws Exception {

      obip = new FileInputStream( new File("C:/props/OBJECTS_SQL.properties"));
      obpr = new Properties();
      obpr.load(obip);
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
*   PURPOSE           :   Method for forming the Select String ( List Box )  for all the items specified in the Array List
*   PARAMETER 1 :   Arraylist with field names for which select strings have to be built
*   RETURNS          :   Arraylist with select strings for each field in the incoming Arraylist
*******************************************************************************************************/
    public ArrayList getSelectString(ArrayList al) throws Exception {

        ArrayList ral = new ArrayList();
        Connection con = getDBConnection();

        try
        {
	Statement stmt = con.createStatement();

        for(int i=0; i< al.size();i++)
        {
        String field = al.get(i).toString().trim();
        String sql = "select distinct OPTION_CODE,OPTION_DESC,SHOW_ORDER from REF_TABLE where FIELD_NAME = '"+field+"' ORDER BY SHOW_ORDER";
	ResultSet rs   = stmt.executeQuery(sql);
        String selectStr="";
        if(rs!=null)
        {
            String val;
            while(rs.next())
            {
                val=rs.getString("OPTION_CODE").trim();
                selectStr+="<option value="+val+">"+rs.getString("OPTION_DESC").trim()+"</option>";
            }

        }

        ral.add(selectStr);

        }

        } //   End of try
        catch (Exception e)
            {
                DbBean.writeError("selectBean","getSelectString()",e);
            }
        finally
            {
               DbBean.closeConnection(con);
            }

        return ral;
       }
/********************************************************************************************************
*   PURPOSE           :   Method for getting the SMS Code - And its Descriptiuon in the following example format ( for List Box )
                                :   30600 - BIRTHDAY GREETING
*   PARAMETER 1 :   Nil
*   RETURNS          :   Select string for SMS Code drop down list
*******************************************************************************************************/
       public String getSMSCodeSelect() throws Exception {

        String selectStr="";
        Connection con = getDBConnection();
        try{

	Statement stmt = con.createStatement();

        String q = "select distinct OPTION_CODE,OPTION_DESC,SHOW_ORDER from REF_TABLE where FIELD_NAME = 'SMS_CODE' ORDER BY SHOW_ORDER";
	ResultSet rs   = stmt.executeQuery(q);

        if(rs!=null)
        {
            String val;
            while(rs.next())
            {
                val=rs.getString("OPTION_CODE").trim();
                selectStr+="<option value="+val+">"+val+" - "+rs.getString("OPTION_DESC").trim()+"</option>";
            }
        }
        } //   End of try
        catch (Exception e)
            {
                DbBean.writeError("selectBean","getSMSCodeSelect()",e);
            }
        finally
            {
               DbBean.closeConnection(con);
            }
        return selectStr;
	}
/********************************************************************************************************
*   PURPOSE           :   A Method  for getting all the user levels both authorised and un-authorised
*   PARAMETER 1 :   String to specify ( authorised  - 1, un-authorised - anyvalue other than 1 )
*   RETURNS          :   Select String for User levels
*******************************************************************************************************/
        public String getUserLevels(String unAuthorisedlevels) throws Exception {

        String selectStr="";
        Connection con = getDBConnection();
        try{

	Statement stmt = con.createStatement();
        String q;
        if(unAuthorisedlevels.equalsIgnoreCase("1"))
            q = "select distinct LEVEL_NAME from USER_LEVEL WHERE  LEVEL_NAME <>'ADMINISTRATOR'";
        else
            //Modified below query, added group_active condition to check active group on 2-july-2014 for user active/inactive ticket
        	q = "select distinct LEVEL_NAME from USER_LEVEL where (AUTHORISE_BY <>'null' and AUTHORISE_BY <>'0' and GROUP_ACTIVE='ACTIVE' )";
	ResultSet rs   = stmt.executeQuery(q);

        if(rs!=null)
        {
            String val;
            while(rs.next())
            {
                val=rs.getString("LEVEL_NAME").trim();
                selectStr+="<option value='"+val+"'>"+val+"</option>";
            }
        }
        } //   End of try
        catch (Exception e)
            {
                DbBean.writeError("selectBean","getUserLevels()",e);
            }
        finally
            {
               DbBean.closeConnection(con);
            }
        return selectStr;
	}

/********************************************************************************************************
*   PURPOSE           :   A method  for  getting all the User IDs in a List Box
*   PARAMETER 1 :   Nil
*   RETURNS          :   Select String for User ID's
*******************************************************************************************************/

        public String getUserIDs() throws Exception {

        String selectStr="";
        Connection con = getDBConnection();
        try{

	Statement stmt = con.createStatement();

        String q = "select distinct USER_ID from USER_INFO";
	    ResultSet rs   = stmt.executeQuery(q);

        if(rs!=null)
        {
            String val;
            while(rs.next())
            {
                val=rs.getString("USER_ID").trim();
                selectStr+="<option value='"+val+"'>"+val+"</option>";
            }

        }
        } //   End of try
        catch (Exception e)
            {
                DbBean.writeError("selectBean","getUserIDs()",e);
            }
        finally
            {
               DbBean.closeConnection(con);
            }
        return selectStr;
	}

        public String getDeptIDs() throws Exception {

          String selectStr="";
          Connection con = getDBConnection();
          try{

            Statement stmt = con.createStatement();
            String q;
            q = "select distinct PLANT from PLNTMST";
            ResultSet rs   = stmt.executeQuery(q);

            if(rs!=null)
            {
              String val;
              while(rs.next())
              {
                val=rs.getString("PLANT").trim();
                selectStr+="<option value='"+val+"'>"+val+"</option>";
              }
            }
          } //   End of try
          catch (Exception e)
          {
            DbBean.writeError("selectBean","getDeptIDs()",e);
          }
          finally
          {
            DbBean.closeConnection(con);
          }
          return selectStr;
        }

}

