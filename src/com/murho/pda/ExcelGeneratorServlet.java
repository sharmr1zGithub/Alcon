package com.murho.pda;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.murho.dao.InvMstDAO;
import com.murho.dao.LocMstDAO;
import com.murho.db.utils.ExcelGenerationUtil;
import com.murho.db.utils.StockTakeUtil;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;


/**
 * <p>Title: Generate Excel </p>
 * <p>Description: This servlet is used for Generating Excel</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Murho</p>
 * @author not attributable
 * @version 1.0
 */

public class ExcelGeneratorServlet
    extends HttpServlet {

  Context ctx = null;
  XMLUtils xu = new XMLUtils();
  MLogger logger = new MLogger();
  StrUtils strUtils = new StrUtils();
  StockTakeUtil _StockTakeUtil=null;
  InvMstDAO _InvMstDAO= null; 
  LocMstDAO _LocMstDAO= null; 
  ExcelGenerationUtil excelGenerationUtil=new ExcelGenerationUtil();
  // UserTransaction ut;
  String PLANT = "", REFTYPE = "", REFLNNO = "", USERNAME = "",
      BINNO = "", COMPANY = "";

  String xmlStr = "";
  String action = "";


  private static final String CONTENT_TYPE = "text/xml";

  public void init() throws ServletException {
   _InvMstDAO=new InvMstDAO();
   _LocMstDAO= new LocMstDAO();
   _StockTakeUtil=new StockTakeUtil();
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException 
  {
    response.setContentType(CONTENT_TYPE);
  //  PrintWriter out1 = response.getWriter();
    MLogger.log(1, "_______________________ " + this.getClass() + " doGet()");
    try 
    {
      action = request.getParameter("action").trim();
      MLogger.log(0,"------------------ Action = " + action + " ----------------- **** ");
    if (action.equalsIgnoreCase("GenerateExcel")) {
        xmlStr = "";
        xmlStr = generateexcel(request, response);

      }
     if (action.equalsIgnoreCase("GenerateTrayExcel")) {
        xmlStr = "";
        xmlStr = GenerateTrayExcel(request, response);

      }
      
       if (action.equalsIgnoreCase("GenerateStockCount")) {
        xmlStr = "";
        GenerateStockCountExcelReport(request, response);

      }
      
       if (action.equalsIgnoreCase("GenerateInv")) {
        xmlStr = "";
        xmlStr = GenerateInvExcelReport(request, response);

      }
       
       if (action.equalsIgnoreCase("GenerateInboundExcel")) {
           xmlStr = "";
           xmlStr = GenerateInboundExcel(request, response);

         }
    }
    catch (Exception e) 
    {
      MLogger.exception(" Exception :: doGet() : ", e);
      xmlStr = xu.getXMLMessage(1,"Error : " + e.getMessage());
    }
    MLogger.log( 0, "XML :" + xmlStr);
    MLogger.log( -1, "_______________________ " + this.getClass() + " doGet()");
 //   out1.write(xmlStr);
  //  out1.close();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException {
    doGet(request, response);
  }

  public void destroy() {
  }

  private String generateexcel(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", 
      ITEM="";
      String  slcount="";
      int slcounter = 0;
     try {
    
   //   plant = strUtils.fString(request.getParameter("PLANT"));
      ITEM = StrUtils.fString(request.getParameter("ITEM"));
      slcount=StrUtils.fString(request.getParameter("slcount"));
      
      slcounter=Integer.parseInt(slcount);
     
      MLogger.info("************* generateexcel "+slcounter);
      
      if(slcounter > 0)
      {
         excelGenerationUtil.generateExcelStatement(ITEM,response.getOutputStream(), response);
      }else if(slcounter <= 0)
      {
         excelGenerationUtil.generateExcelStatement_01(ITEM,response.getOutputStream(), response);
      }
      
     
      
       MLogger.info("************* " +  str);
      
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
                  
       throw e;
    }
    return str;
  }
  
        private String GenerateTrayExcel(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "", 
      ITEM="",REFNO="",LOT="",SKU="";
     try {
    
      ITEM = StrUtils.fString(request.getParameter("ITEM"));
      REFNO = StrUtils.fString(request.getParameter("REFNO"));
      
      /*   Updated by Ranjana
       *   Purpose:Add the Filter for Discrepancy Report for outbound.
       *   */
      
      LOT = StrUtils.fString(request.getParameter("LOT"));
      SKU = StrUtils.fString(request.getParameter("SKU"));
      if(LOT==null || LOT == ""){
    	  LOT ="";
      }else{
    	  LOT = StrUtils.fString(request.getParameter("LOT"));
      }
      
      if(SKU == null || SKU == ""){
    	  SKU ="";
      }else{
    	  SKU = StrUtils.fString(request.getParameter("SKU"));
      }
     
      MLogger.info("************* generateexcel ");
/*   Updated by Ranjana
 *   Purpose:Add the Filter for Discrepancy Report for outbound.
 *   
 *   excelGenerationUtil.GenerateTrayExcelReport(ITEM,REFNO,response.getOutputStream(), response);*/
      
      excelGenerationUtil.GenerateTrayExcelReport(ITEM,REFNO,LOT,SKU,response.getOutputStream(), response);
      
       MLogger.info("************* " +  str);
      
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
                  
       throw e;
    }
   // if (str!="")throw new Exception("DDDDDDDDDDDDD");
    return str;
  }
  
        private void GenerateStockCountExcelReport(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      //String str  = "";
     try {
      System.out.println("************* GenerateStockCountExcelReport ");
      //String PLANT = StrUtils.fString(request.getParameter("PLANT"));
      String SKU = StrUtils.fString(request.getParameter("SKU"));
      String LOTNO = StrUtils.fString(request.getParameter("LOTNO"));
      String LOC = StrUtils.fString(request.getParameter("LOC"));
      String LOCGRP = StrUtils.fString(request.getParameter("LOCGRP"));
      
      	StringBuffer query = new StringBuffer();
       if (SKU != null && !SKU.trim().equals(""))
				query.append(" AND A.ITEM LIKE '" + SKU.trim() + "%'");
      if (LOTNO != null && !LOTNO.trim().equals(""))
				query.append(" AND A.BATCH LIKE '" + LOTNO.trim() + "%'");
      if (LOC != null && !LOC.trim().equals(""))
				query.append(" AND A.LOC LIKE '" + LOC.trim() + "%'");
			if (LOCGRP != null && !LOCGRP.trim().equals(""))
				query.append(" AND A.LOC IN(select  LOC_ID  from locmst where LOC_GRP_ID = '"+LOCGRP+"') ");
     System.out.println("query................................."+query);
      excelGenerationUtil.GenerateStockCountExcelReport(query.toString(),response.getOutputStream(), response);
      String Result ="Stock Report Generated Sucessfully";
      response.sendRedirect("jsp/StockTake.jsp?Res="+Result);
   //    System.out.println("************* " +  str);
      
    }
    catch (Exception e) {
      MLogger.exception(this,e);           
       throw e;
    }
 
   
  }
  
    private String GenerateInvExcelReport(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
      String str  = "";
     try {
    
      String ITEM = StrUtils.fString(request.getParameter("ITEM"));
     String BATCH = StrUtils.fString(request.getParameter("BATCH"));
     String LOC = StrUtils.fString(request.getParameter("LOC"));
      
     
      MLogger.info("************* GenerateInvExcelReport ");
      excelGenerationUtil.GenerateInvExcelReport(ITEM,BATCH,LOC,response.getOutputStream(), response);
      
       MLogger.info("************* " +  str);
      
    }
    catch (Exception e) {
      MLogger.exception(this,e);;
                  
       throw e;
    }
 
    return str;
  }
    
    
    /* Method added by Ranjana
     * Purpose: To generate the Inbound Discrepancy Report. */
    
    private String GenerateInboundExcel(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", ITEM = "",LOT="",SKU="";
		try {

			ITEM = StrUtils.fString(request.getParameter("ITEM"));
			LOT = StrUtils.fString(request.getParameter("LOT"));
			SKU = StrUtils.fString(request.getParameter("SKU"));
			if(LOT == null || LOT == ""){
				LOT = "";
			}else{
				LOT = StrUtils.fString(request.getParameter("LOT"));
			}
			if(SKU == null || SKU ==""){
				SKU="";
			}else{
				SKU = StrUtils.fString(request.getParameter("SKU"));
			}
			
			MLogger.info("************* generateexcel ");
			
			excelGenerationUtil.GenerateInboundExcelReport(ITEM,LOT,SKU,response
					.getOutputStream(), response);

			MLogger.info("************* " + str);

		} catch (Exception e) {
			MLogger.exception(this, e);
			throw e;
		}
		return str;
	}
} //end of class
