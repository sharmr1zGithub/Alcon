package com.murho.pda;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.murho.db.utils.InvMstUtil;
import com.murho.gates.DbBean;
import com.murho.gates.Generator;
import com.murho.utils.CibaConstants;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;

public class InboundServlet
    extends HttpServlet {

  XMLUtils xu = new XMLUtils();
  MLogger logger = new MLogger();
  StrUtils strUtils = new StrUtils();
  Generator generator = new Generator();
  InvMstUtil _InvMstUtil = null;

  String xmlStr = "";
  String action = "";
  String strLoginUser = "";

  private static final String CONTENT_TYPE = "text/xml";

  public void init() throws ServletException {
    _InvMstUtil = new InvMstUtil();
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException {

    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();
    try {

      action = request.getParameter("action").trim();
      strLoginUser = strUtils.fString(request.getParameter("LOGIN_USER"));


       MLogger.log(1,strLoginUser + " : " + this.getClass() + " doGet() ##################");
       MLogger.log(0, "Loggin user : " + strLoginUser);
       MLogger.log(0, "Action      : " + action);
      
      if (action.equalsIgnoreCase("load_inv_det")) {

        xmlStr = "";
        xmlStr = load_inv_det(request, response);

      }
    }

    catch (Exception e) {

      MLogger.exception(" Exception :: doGet() : ", e);
       xmlStr = xu.getXMLMessage(1, e.getMessage());

    }
    
    MLogger.log( -1,
                strLoginUser + " : " + this.getClass() + " doGet() ##################");
    out.write(xmlStr);
    out.close();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException {
    doGet(request, response);
  }

  public void destroy() {
  }
  
  /*
  private String load_inv_det(HttpServletRequest request,
                                    HttpServletResponse response) throws
      IOException, ServletException, Exception {
    String str = "";
    try {

      String plant = strUtils.fString(request.getParameter("PLANT"));
      String loc = strUtils.fString(request.getParameter("LOC"));
      String mtid = strUtils.fString(request.getParameter("MTID"));
     // String item = strUtils.fString(request.getParameter("SKU"));
      
      String lot = strUtils.fString(request.getParameter("LOT"));
      
      MLogger.printInput("PLANT :" + plant);
      MLogger.printInput("LOC   :" + loc);
      MLogger.printInput("MTID  :" + mtid);
   //   MLogger.printInput("SKU  :" + item);
      
      MLogger.printInput("LOT  :" + lot);
      
   
      Hashtable htCond=new Hashtable();
      htCond.put("PLANT",CibaConstants.cibacompanyName);
      
      String query=" LOC,MTID,SKU,LOT,QTY ";
      
      if(loc.length()>0) {   htCond.put("LOC",loc);   }
      
      if(lot.length()>0){   htCond.put("LOT",lot);  }
      if(mtid.length()>0){   htCond.put("MTID",mtid); }
   //   if(item.length()>0){   htCond.put("SKU",item);  }
    
      str = _InvMstUtil.Load_Inv_Det(query,htCond);

    }
    catch (Exception e) {
      throw e;
    }

    return str;
  }
 */
  
  //6.0 enhancement
  private String load_inv_det(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";
		try {

			String plant = strUtils.fString(request.getParameter("PLANT"));
			String loc = strUtils.fString(request.getParameter("LOC"));
			String mtid = strUtils.fString(request.getParameter("MTID"));
			String lot = strUtils.fString(request.getParameter("LOT"));

			MLogger.printInput("PLANT :" + plant);
			MLogger.printInput("LOT   :" + lot);
			MLogger.printInput("MTID  :" + mtid);
			
			str = _InvMstUtil.Load_Inv_Detail(loc, mtid, lot);

		} catch (Exception e) {
			throw e;
		}

		return str;
	}
}