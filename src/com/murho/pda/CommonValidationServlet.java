package com.murho.pda;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.murho.db.utils.ItemMstUtil;
import com.murho.db.utils.MovHisUtil;
import com.murho.gates.encryptBean;
import com.murho.gates.userBean;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;

//import com.seiko.utils.*;

/**
 * <p>
 * Title: Common Validation Servlet using PDA
 * </p>
 * <p>
 * Description: This servlet is used for Common Validation using PDA
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Murho
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class CommonValidationServlet extends HttpServlet {

	Context ctx = null;

	XMLUtils _XMLUtils = null;

	MLogger logger = null;

	//StrUtils strUtils = null;
  
  ItemMstUtil itemUtil =null;


	String PLANT = "", REFTYPE = "", REFLNNO = "", USERNAME = "", 
			COMPANY = "";

	String xmlStr = "";

	String action = "";
	
	MovHisUtil mv = null;
	encryptBean eb = null;

	private static final String CONTENT_TYPE = "text/xml";

	public void init() throws ServletException {
		_XMLUtils = new XMLUtils();
		logger = new MLogger();
		//strUtils = new StrUtils();
    itemUtil = new ItemMstUtil();
     mv = new MovHisUtil();
     
     //Code added by Arun for #25046
     try{
      eb = new encryptBean();
     }catch(Exception e){
    	 System.out.println("Exception in EncryptBean.. "+e.getMessage());
     }
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		try {
			action = request.getParameter("action").trim();
			MLogger.log(1, "********** " + this.getClass()
					+ " doGet() :: Action : " + action);


			if (action.equalsIgnoreCase("validate_item")) {
				xmlStr = "";
				xmlStr = validate_item(request, response);
			}
//			 
			if (action.equalsIgnoreCase("change_password")) {
				xmlStr = "";
				xmlStr = change_password(request, response);
			}
  
		} catch (Exception e) {
			MLogger.exception(" Exception :: doGet() : ", e);
			xmlStr = _XMLUtils.getXMLMessage(1, "Unable to process? "
					+ e.getMessage());
		}
		MLogger.log(0, "XML : \n " + xmlStr);
		MLogger.log(-1, "_______________________ " + this.getClass()
				+ " doGet()");
		out.write(xmlStr);
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

private String validate_item (HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
        String str = "", skuRef = "";
        
     try {
      skuRef  = StrUtils.fString(request.getParameter("SKUCODE"));
      MLogger.printInput("SKU UPC CODE :"+ skuRef);
     
      
      Hashtable ht = new Hashtable();
      ht.put("REFNO",skuRef);
      str=itemUtil.GetSKUForRefNo(ht);
      MLogger.info("************* " +  str);
      
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
                 
       throw e;
       
    }
   
    return str;
  }
    
	/*private static void sop(String str) {
		System.out.println(str);
	}*/
	
	// new method Added by Arun for #25046 to change password from PDA and PC-Client
	private String change_password (HttpServletRequest request,
            HttpServletResponse response) throws
 IOException, ServletException,
			Exception {
		MLogger.info("Entered-------- change_password() of CommonValidationServlet" );
		String newPwd,user_id,oldPwd, result, oldPwdFlag;
		try {
			MLogger.log("before try");
			user_id  = StrUtils.fString(request.getParameter("LOGIN_USER"));
		    newPwd   = StrUtils.fString(request.getParameter("NPASSWORD"));
		    oldPwd   = StrUtils.fString(request.getParameter("PASSWORD"));
		    oldPwdFlag   = StrUtils.fString(request.getParameter("OLD_PASSWORD_FLAG"));
		    MLogger.log("new password+++++++++++++++++++++++"+newPwd);
		   
		    newPwd   = eb.encrypt(newPwd);
		    oldPwd   = eb.encrypt(oldPwd);

		    userBean ub = new userBean();
		    int n = ub.changePassword(user_id, newPwd, oldPwd, oldPwdFlag);
		 
		    if(n==1){
		    	result = "Password has been changed successfully.";
		    	xmlStr = _XMLUtils.getXMLMessage(0, result);
		    	mv.insertMovHisLogger(user_id,"Change_Pwd", result);
		    }
		    else if(n==-2){
		    	result = "Your old password is not right, try again.";
		    	xmlStr = _XMLUtils.getXMLMessage(1,result);
		    	mv.insertMovHisLogger(user_id,"Change_Pwd", result);
		    }
		    else {
		    	result = "Error in changing the password,Please try again.";
		    	xmlStr = _XMLUtils.getXMLMessage(1,result);
		    	mv.insertMovHisLogger(user_id,"Change_Pwd", result);
		     }
			MLogger.info("************* " +result);

		} catch (Exception e) {
			MLogger.exception(this, e);
			throw e;
			
		}
		MLogger.info("Exit-------- change_password() of CommonValidationServlet" );
		return xmlStr;
}
	
} // end of class
