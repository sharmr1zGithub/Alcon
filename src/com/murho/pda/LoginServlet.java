package com.murho.pda;

//import com.murho.dao.ItemMstDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.murho.db.utils.MovHisUtil;
import com.murho.gates.DbBean;
import com.murho.gates.defaultsBean;
import com.murho.gates.encryptBean;
import com.murho.gates.sqlBean;
import com.murho.gates.userBean;
import com.murho.utils.Generator;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;

public class LoginServlet   extends HttpServlet  implements SingleThreadModel{
  encryptBean eb;
  defaultsBean db;
  userBean ub;
  userBean userBeanobj = null;
  String action    = "";
  String username  = "";
  String dept      = "";
  String password  = "";
  String xmlStr    = "";
  int status       = 0;
  int passwordStatus       = 0;
  MovHisUtil mv = null;
  //to load prop
  DbBean _DbBean=new DbBean();
  private static final String CONTENT_TYPE = "text/html";
  XMLUtils xmlUtil  = new XMLUtils();
  //StrUtils strUtils = new StrUtils();
  public void init() throws ServletException {
    try {
      eb     = new encryptBean();
      db     = new defaultsBean();
      ub     = new userBean();
      userBeanobj  = new userBean();
      // Added by Arun on 29 Sept 2011 for #25046: URS8-10 
      mv = new MovHisUtil();
    }
    catch (Exception e) {}
  }
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException {
    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();
    response.setContentType("text/xml");
    boolean isExists;
    String result ="";
    try {

      action = request.getParameter("action").trim();
      if (action.equalsIgnoreCase("check_user")) {
    	  username = request.getParameter("username").trim();
    	 
    	  //password = eb.encrypt(request.getParameter("password").trim());
    	  // modified by arun ticket #25046 
    	  password = eb.encrypt(StrUtils.fString(request.getParameter("password")).trim());
    	  
    	  //condition added by Arun on 8 July 2011: ticket #25046 
    	  isExists=ub.isValidUserExists(username);
    	  
    	  if(isExists){ // If valid user_id exists
    		  status = ub.isValidUser(username, password);
    	  }else{
    		  status = 109; // User_id wrong
    	  }
    	  String levelname = ub.getUserLevel(username);
    	  
    	  
    	  if(status == 115){// Added on 10-june-2014 for active/inactive user ticket #WO0000000041526
    		  result = "Your account has been deactivated. Please contact the Administrator.";
    		  xmlStr = xmlUtil.getXMLMessage(115, result);
    		  mv.insertMovHisLogger(username, "User_Logging",result);
    	  }
    	  
    	  else if (status == 103) {// Added by Arun on 29 Sept 2011 for #25046: URS:8-15: Block user account after 5 unscuccessful attempts
    		  //result= "Your account is blocked, to reset contact Administrator.";
    		  result = "Account blocked. You have exceeded maximum no. of unsuccessful login attempts. Contact Administrator to reset.";
    		  xmlStr = xmlUtil.getXMLMessage(103, result);
    		  mv.insertMovHisLogger(username, "User_Logging",result); 
    		  //validUser = false;
    	  }else if (status == 109 ) {
    		  result = "Invalid UserID : " +username+ " " + " Please try again with correct UserID.";
    		  xmlStr = xmlUtil.getXMLMessage(109,result); 
    		  // Added by Arun on 29 Sept 2011 for #25046: URS8-10 Audit trail all attempts of login
    		  mv.insertMovHisLogger(username, "User_Logging",result); 
    		  //validUser = false;
    	  }else if (status == 113 ) { // Added by Arun on 29 Sept 2011 for #25046: For user level authorization check
    		  result = "User Group : " +levelname+ " " + " is not authorized. Contact your administrator.";
    		  xmlStr = xmlUtil.getXMLMessage(113, result);
    		  // Added by Arun on 29 Sept 2011 for #25046: URS8-10 Audit trail all attempts of login
    		  mv.insertMovHisLogger(username, "User_Logging",result); 
    		  //validUser = false;
    	  }
    	  /*if (status == 1 ) { // Valid  user/password
    		  String dept = ub.getDepartment(username);
    		  xmlStr = xmlUtil.getXMLMessage(0, dept);
    	  }*/
    	  //condition added by Arun on 8 July 2011: ticket #25046 
    	  else if (status == 104 ) { // First time login
    		  xmlStr = xmlUtil.getXMLMessage(104, "This is your first login to the system. Please change your password.");
    		  //validUser = false;
     	  }
    	  else if (status == 102 ) { // User id not authorized
    		  result = "User ID : "+username+ " not authorized. Please Contact Administrator.";
    		  xmlStr = xmlUtil.getXMLMessage(102, result);
    		  // Added by Arun on 29 Sept 2011 for #25046: URS8-10 Audit trail all attempts of login
    		  mv.insertMovHisLogger(username, "User_Logging",result);
    		  //validUser = false;
    	  }else if (status == 110 ) {
    		  xmlStr = xmlUtil.getXMLMessage(110, "Your password has been reset by Admin. Please change your password.");
    		  //validUser = false;
    	  }
        
    	  // modified by Arun for Login Synch for .net modules #25046
    	  
    	  //if(validUser){
    	  if(status == 1 || status == 101){  // Check for Account Block  in both cases: valid or invalid password
    		 // if (!levelname.equalsIgnoreCase("ADMINISTRATOR")){
    		  		String dept = ub.getDepartment(username);
    		  		xmlStr = xmlUtil.getXMLMessage(0, dept);
    		        String strDate =ub.getPasswordStartDate(username,levelname);
			        passwordStatus = ub.isValidPasswordAge(username);
			        if (passwordStatus == 111){  // Display blocked message in both cases: valid or invalid password
			        	//result =  "Your account is blocked, to reset contact Administrator.";
			        	result = "Your account is blocked due to password expiry. Contact Adminstrator to reset.";
			        	xmlStr = xmlUtil.getXMLMessage(111,result);
//			        	 Added by Arun on 29 Sept 2011 for #25046: URS8-10 Audit trail all attempts of login
			        	mv.insertMovHisLogger(username, "User_Logging",result); 
			        }else if (passwordStatus == 112 && status != 101){ // Display reminder message in only valid password case
			        	
			        	int daysRem = ub.getDaysRemaining(strDate,levelname);
		                int agePrd =ub.getAgePrd();
			        	int remday = agePrd-daysRem ;
			        	if (remday==0){
			        		xmlStr = xmlUtil.getXMLMessage(112, "Your Password will expire today. ");
			        	}else{
			        		xmlStr = xmlUtil.getXMLMessage(112, "Your Password will expire in "+remday+" days. ");
			        	}
			        }else if (status == 101 ) {
			    		  result= "Invalid Password : Please try again with correct password.";
			    		  xmlStr = xmlUtil.getXMLMessage(101, result);
			    		  // Added by Arun on 29 Sept 2011 for #25046: URS8-10 Audit trail all attempts of login
			    		  mv.insertMovHisLogger(username, "User_Logging",result); 
			    		  //validUser = false;
			    	 }
			        else{ // Audit trail successful login :Added by Arun on 29 Sept 2011 for #25046: URS8-10 Audit trail all attempts of login
		            	 mv.insertMovHisLogger(username, "User_Logging","User : "+username +"  Logged in Successfully "); 
		             }
			        if(status == 1 && passwordStatus != 111){
//			        	set ACCESS_COUNTER = 0 in case of valid login
			        	sqlBean sb = new sqlBean();
			            int m = sb.insertRecords(
			                "Update USER_INFO Set ACCESS_COUNTER = '0' where USER_ID = '" +
			                username + "'");
			            
			        }
		       // }
	        }else if(status == 104  || status == 110){
	        	passwordStatus = ub.isValidPasswordAge(username);
		        if (passwordStatus == 111){
		        	//result =  "Your account is blocked, to reset contact Administrator.";
		        	result = "Your account is blocked due to password expiry. Contact Adminstrator to reset.";
		        	xmlStr = xmlUtil.getXMLMessage(111,result);
//		        	 Added by Arun on 29 Sept 2011 for #25046: URS8-10 Audit trail all attempts of login
		        	mv.insertMovHisLogger(username, "User_Logging",result); 
		        }
	        }
      }
      
      else if (action.equalsIgnoreCase("load_date")) {
        xmlStr = xmlUtil.getXMLHeader();
        xmlStr = xmlStr + xmlUtil.getStartNode("serverDate");
        xmlStr = xmlStr + xmlUtil.getXMLNode("date", Generator.getDate());
        xmlStr = xmlStr + xmlUtil.getEndNode("serverDate");
      }
      
      else  if (action.equalsIgnoreCase("User_Access_mainmenu")) { // check if the destination bin found
       username      = StrUtils.fString(request.getParameter("USER"));
       dept      = StrUtils.fString(request.getParameter("DEPTID"));
       String level1 = StrUtils.fString(request.getParameter("LEVEL1"));
       String level2 = StrUtils.fString(request.getParameter("LEVEL2"));
       //String url = "";
       List menuList = userBeanobj.getPdaUser_Level(username,dept,level1, level2);

       String btnRecvPO = "false";
       String btnTrasferIn = "false";
       String BtnMiscRecv = "false";
       String BtnOrderPicking = "false";
       String btnOrderIssue = "false";
       String btnStockTake = "false";
       String btnLocTrans = "false";
       String BtnQueryInv = "false";
       // Added by Arun on 8th Aug for #25046
       String btnChangePwd = "false";
       String btnLogout = "True";
       
       if (menuList.size() > 0) {
         if (level1.equalsIgnoreCase("1") && level2.equalsIgnoreCase("1")) {
           //****************************Main Menu*************************
        	 
        	 //Belwo lines commented by Arun for #25046 and added above IF block: To return default values to PDA/Pc-Cleint if none of the menu access is provided
           /*String btnRecvPO = "false";
           String btnTrasferIn = "false";
           String BtnMiscRecv = "false";
           String BtnOrderPicking = "false";
           String btnOrderIssue = "false";
           String btnStockTake = "false";
           String btnLocTrans = "false";
           String BtnQueryInv = "false";
           // Added by Arun on 8th Aug for #25046
           String btnChangePwd = "false";*/
         
          
           xmlStr = "";
           if (menuList.contains(new String("pdaRecv")))
             btnRecvPO = "True";
           if (menuList.contains(new String("pdaPutAway")))
             btnTrasferIn = "True";
           if (menuList.contains(new String("pdaBlindRcpt")))
             BtnMiscRecv = "True";
           if (menuList.contains(new String("pdaPicking")))
             BtnOrderPicking = "True";
           if (menuList.contains(new String("pdaPickOut")))
             btnOrderIssue = "True";
           if (menuList.contains(new String("pdaStkCount")))
             btnStockTake = "True";
           if (menuList.contains(new String("pdaStkTransfer")))
             btnLocTrans = "True";
           if (menuList.contains(new String("pdaQuery")))
             BtnQueryInv = "True";
           // Added by Arun on 8th Aug for #25046
           if (menuList.contains(new String("pdaChangePassword")))
        	   btnChangePwd = "True";
         
           // Below lines commented by Arun for #25046 and added outside IF block: To return default values to PDA/Pc-Cleint if none of the menu access is provided
          /* xmlStr = xmlUtil.getXMLHeader();
           xmlStr += xmlUtil.getStartNode("record");
           xmlStr += xmlUtil.getXMLNode("M_RECV", btnRecvPO);
           xmlStr += xmlUtil.getXMLNode("M_PUTAWAY", btnTrasferIn);
           xmlStr += xmlUtil.getXMLNode("M_BLINDRECV", BtnMiscRecv);
           xmlStr += xmlUtil.getXMLNode("M_ORDPICK", BtnOrderPicking);
           xmlStr += xmlUtil.getXMLNode("M_ORDISSUE", btnOrderIssue);
           xmlStr += xmlUtil.getXMLNode("M_STOCKTAKE", btnStockTake);
           xmlStr += xmlUtil.getXMLNode("M_LOCTRAN", btnLocTrans);
           xmlStr += xmlUtil.getXMLNode("M_QUERYINV", BtnQueryInv);
//         Added by Arun on 8th Aug for #25046
           xmlStr += xmlUtil.getXMLNode("M_CHANGEPASSWORD", btnChangePwd);
           xmlStr += xmlUtil.getXMLNode("M_LOGOUT", btnLogout);           
           xmlStr += xmlUtil.getXMLNode("TranDate", Generator.getDate());
           xmlStr += xmlUtil.getEndNode("record");*/

         }
       }
       xmlStr = xmlUtil.getXMLHeader();
       xmlStr += xmlUtil.getStartNode("record");
       xmlStr += xmlUtil.getXMLNode("M_RECV", btnRecvPO);
       xmlStr += xmlUtil.getXMLNode("M_PUTAWAY", btnTrasferIn);
       xmlStr += xmlUtil.getXMLNode("M_BLINDRECV", BtnMiscRecv);
       xmlStr += xmlUtil.getXMLNode("M_ORDPICK", BtnOrderPicking);
       xmlStr += xmlUtil.getXMLNode("M_ORDISSUE", btnOrderIssue);
       xmlStr += xmlUtil.getXMLNode("M_STOCKTAKE", btnStockTake);
       xmlStr += xmlUtil.getXMLNode("M_LOCTRAN", btnLocTrans);
       xmlStr += xmlUtil.getXMLNode("M_QUERYINV", BtnQueryInv);
//     Added by Arun on 8th Aug for #25046
       xmlStr += xmlUtil.getXMLNode("M_CHANGEPASSWORD", btnChangePwd);
       xmlStr += xmlUtil.getXMLNode("M_LOGOUT", btnLogout);           
       xmlStr += xmlUtil.getXMLNode("TranDate", Generator.getDate());
       xmlStr += xmlUtil.getEndNode("record");
     }

    }catch (Exception e) {
        e.printStackTrace();
        xmlStr = xmlUtil.getXMLMessage(1, "Please check the inputs !!!");
      }
      out.write(xmlStr);
      out.close();
    }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException {
    doGet(request, response);
  }

  public void destroy() {
  }
}
