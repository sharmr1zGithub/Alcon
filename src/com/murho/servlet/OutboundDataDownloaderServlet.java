package com.murho.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import com.murho.DO.TransactionDTO;
import com.murho.dao.BaseDAO;
import com.murho.dao.SQLRecvDet_DAO;
import com.murho.dao.SQLTempDet_DAO;
import com.murho.db.utils.DataDownloaderUtil;
import com.murho.db.utils.InvMstUtil;
import com.murho.db.utils.SoDownloaderUtil;
import com.murho.gates.DbBean;
import com.murho.gates.Generator;
import com.murho.gates.userBean;
import com.murho.utils.CibaConstants;
import com.murho.utils.DateUtils;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.TransactionTypes;
import com.murho.utils.XMLUtils;
import com.oreilly.servlet.MultipartRequest;

public class OutboundDataDownloaderServlet extends HttpServlet {

	XMLUtils xu = new XMLUtils();

	MLogger logger = new MLogger();

	StrUtils strUtils = new StrUtils();

	Generator generator = new Generator();

	InvMstUtil _InvMstUtil = null;

	DataDownloaderUtil _DataDownloaderUtil = null;

	String xmlStr = "";

	String action = "";

	String strLoginUser = "";

	String fieldDesc = "";

	String fileName = "";

	String StrFileName = "";

	String PLANT = "";

	String login_user = "";

	int arraySize;

	int inFlag = 0;

	private static final String CONTENT_TYPE = "text/xml";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		try {
			action = StrUtils.fString(request.getParameter("submit")).trim();
			StrFileName = StrUtils.fString(request.getParameter("ImportFile"));

			PLANT = (String) request.getSession().getAttribute("PLANT");
			login_user = (String) request.getSession().getAttribute(
					"LOGIN_USER");

			if (action.equalsIgnoreCase("Import")) {

				Import_outbound_text_file(request, response);

			}

		} catch (Exception e) {
			MLogger.exception(" Exception :: doGet() : ", e);
			xmlStr = xu.getXMLMessage(1, "Error : " + e.getMessage());

		}
		MLogger.log(-1, strLoginUser + " : " + this.getClass()
				+ " doGet() ##################");
		out.write(xmlStr);
		out.close();

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// response.setContentType("text/html");
		// PrintWriter out = response.getWriter();
		doGet(request, response);

	}

	private void Import_outbound_text_file(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		UserTransaction ut = null;

		boolean transFlag = false;
		userBean ub = null;

		Map sysParams = null;
		boolean flag = false;

		try {
			SQLRecvDet_DAO _SQLRecvDet = new SQLRecvDet_DAO();
			SQLTempDet_DAO _SQLTempDet = new SQLTempDet_DAO();
			// below if condition is added to check if traveler is available in
			// temporary table
			// duplicate delivery issue #WO0000000202453 on 31-10-2014
			flag = _SQLTempDet.checkTempDet();

			if (flag) {
				throw new Exception(
						"Import failed. Error: Another file is in progress of import. Please try after some time!");
			}

			// below line commented by Arun on 21 June 2011 for #1851-- to
			// increase file restriction from 2 MB to 5 MB .. More than 5 MB
			// exceeds trans time
			// MultipartRequest mreq = new MultipartRequest(request,
			// CibaConstants.CReadFromServerFolder, 2048000);

			// below line is added by jyoti for TIBCO-INC000002484471(WMS 2.8)
			// MultipartRequest mreq = new MultipartRequest(request,
			// CibaConstants.CReadFromServerFolderOutbound, 5242880);

			fileName = request.getParameter("ImportFile").trim();
			String strHdrfileName = fileName.substring(fileName
					.lastIndexOf('\\') + 1, fileName.length());

			ub = new userBean();
			sysParams = ub.getSystemParams();
			String filepath = (String) sysParams.get("UPLOAD_FOLDER_PATH");
			MultipartRequest mreq = new MultipartRequest(request, filepath,
					5242880);

			// below line is commented by jyoti for TIBCO-INC000002484471(WMS
			// 2.8)
			// String StrFileName= CibaConstants.CReadFromServerFolder+
			// strHdrfileName;
			// below line is added by jyoti for TIBCO-INC000002484471(WMS 2.8)
			String StrFileName = filepath + strHdrfileName;

			HttpSession LoginUser = request.getSession();
			String strLoginUser = LoginUser.getAttribute("LOGIN_USER")
					.toString();

			SoDownloaderUtil _SoDownloaderUtil = new SoDownloaderUtil();
			_SoDownloaderUtil.downloadTextData(StrFileName);

			// To get array size from inbound file
			ArrayList aList = new ArrayList();
			aList = _SoDownloaderUtil.outList;

			// To get Transaction DO
			TransactionDTO trnDTO;
			ArrayList alTransactionData = new ArrayList();

			// To set object for Temp Table
			int sno = 0;
			int qty = 0;
			int lineno = 0;
			String qtystr = "";
			// delete temptable
			// below line Added by Arun for #1851 on 15 June 2011
			_SQLTempDet.deleteTempDet();

			ut = com.murho.gates.DbBean.getUserTranaction();
			ut.begin();

			// Variable declared to check the Restricted LOT
			String StrblockLot = "";
			String Systemblock = "";
			String destination = "";
			
			if (aList.size() > 0) {

				for (int i = 1; i < aList.size(); i++) {
					String chkLOT = "";
					
					ArrayList tempLists = (ArrayList) aList.get(i);

					System.out.println("ArraySize........" + tempLists
							+ "</br>");

					sno = i;

					for (int j = 0; j < tempLists.size(); j++) {

						// System.out.println("j..."+tempLists.get(j)+"</br>");
						/*
						 * if(tempLists.get(j).equals("")) { throw new
						 * Exception("Unable Download Data's,Please check the
						 * outbound text file"); }
						 */
						trnDTO = new TransactionDTO();
						// alTransactionData.clear();
						String travelerid = tempLists.get(1).toString();
						String shipdate = tempLists.get(2).toString();
						String strshipdate = shipdate.substring(0, 2)
								+ shipdate.substring(3, 5)
								+ shipdate.substring(6, 10);
						destination = tempLists.get(3).toString();
						String pt = tempLists.get(4).toString();
						String shipparty = tempLists.get(5).toString();
						String sku = tempLists.get(6).toString();
						lineno = Integer.parseInt(((String) tempLists.get(6))
								.trim().toString());
						String material = tempLists.get(7).toString();
						String lot = tempLists.get(8).toString();
												
						qtystr = tempLists.get(9).toString()
								.replaceAll(",", "");
						// qty=
						// Integer.parseInt(((String)tempLists.get(9)).trim().toString());
						qty = Integer.parseInt(((String) qtystr).trim()
								.toString());

						// Commented by Arun for #1851 -- Qty will be updated
						// after proper validations

						// chkPUV= _SQLRecvDet.checkPUV(material);
						// if(chkPUV==true)
						// {
						// qty=qty * 6;
						// }
						String uom = tempLists.get(10).toString();
						String description = tempLists.get(11).toString();
						
						

						/* Added by Ranjana
						 * Calling a method for checking the System Blocked LOT
						 * for the destination from the Database
						 * Ticket WO0000001221106
						 */
						MLogger.log(0, "destination value..." + destination);
						MLogger.log(0, "sku value..." + material);
						MLogger.log(0, "lot value..." + lot);
						Systemblock = _SQLRecvDet.blockLOT(destination, material,
								lot);
						
						
					
						trnDTO.setSno(sno);
						trnDTO.setTraveler(travelerid);
						trnDTO.setShipdate(strshipdate);
						trnDTO.setDestination(destination);
						trnDTO.setPt(pt);
						trnDTO.setShipparty(shipparty);
						trnDTO.setSku(sku);
						trnDTO.setMaterial(material);
						trnDTO.setLotno(lot);
						trnDTO.setQty(qty);
						trnDTO.setUom(uom);
						trnDTO.setDescription(description);
						trnDTO.setField15(lineno);

						/* Added by Ranjana
						 * Calling a method for checking the Restricted LOT
						 * from the Database
						 */

						chkLOT = _SQLRecvDet.checkLOT(lot);

						// Adding the LOT to arraylist if its not Blocked. 

						if (chkLOT.equals("1")) {
							StrblockLot += lot + " ";
						}

						// inFlag =
						// _SoDownloaderUtil.process_import(alTransactionData);
						
						/*Throw Exception if combination occurs for destination, prd_cls_id and lot under ticket WO0000001221106
						 * Added by Ranjana 
						 */
						if (Systemblock.equals("0")) {							
							throw new Exception("Import failed. Error: Regulatory Restriction is Detected with this DO.");
						}
						alTransactionData.add(trnDTO);

						j = 11;
					}

				}

				/* Added by Ranjana for Checking for non duplicate values of LOT */

				MLogger.log(0, "StrblockLot..." + StrblockLot);
				String Strblocklot = "";
				if (StrblockLot != null && StrblockLot != "") {
					String[] BlockLot = StrblockLot.split(" ");
					Set hs = new HashSet();
					hs.addAll(Arrays.asList(BlockLot));
					Strblocklot = String.valueOf(hs);

				}
				
				
				// commented by Arun for #1851
				// inFlag = _SoDownloaderUtil.process_import(alTransactionData);

				// added by Arun on 15 June 2011 for #1851 -- to insert data
				// from arrayList into TEMPDATATABLE before calling proc
				
				_SoDownloaderUtil.insert_IROTempData(alTransactionData);

				// call to Stored Proc to do all validations and update
				// TEMPDATATABLE
				// code to call SP
				// added by Arun on 15 June 2011 for #1851
				MLogger.log("Calling SP: Call_Proc_IRO_Import");
				_SQLTempDet.Call_Proc_IRO_Import();
				MLogger.log("After SP: Call_Proc_IRO_Import");

				BaseDAO _BaseDAO = new BaseDAO();
				java.sql.Connection con = null;

				StringBuffer sbMovHis = new StringBuffer("");
				sbMovHis
						.append("INSERT INTO MOVHIS(PLANT,MOVTID,CRAT,CRTIME,CRBY)VALUES(");
				sbMovHis.append("'" + CibaConstants.cibacompanyName + "',");
				sbMovHis.append("'"
						+ TransactionTypes.Import_outbound_file_tran_type
						+ "',");
				sbMovHis.append("'"
						+ DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate())
						+ "',");
				sbMovHis.append("'" + DateUtils.Time() + "',");
				sbMovHis.append("'" + strLoginUser + "'");
				// sbMovHis.append("'" + strHdrfileName+"'");
				sbMovHis.append(")");

				con = DbBean.getConnection();
				transFlag = _BaseDAO.insertData(con, sbMovHis.toString());

				if (con != null) {
					DbBean.closeConnection(con);
				}

				if (transFlag) {
					DbBean.CommitTran(ut);
				} else {
					throw new Exception(" Failed to Import Delivery No");
				}

				request.getSession().setAttribute("RESULTOUT",
						sno - 1 + " " + "Rows of Data's Import Successfully");
				response.sendRedirect("jsp/ImportSO.jsp?action=result");
				
				/*
				 * Added by Ranjana Purpose: To display the message of success
				 * on the UI along with the restricted LOTs.
				 */

				if (Strblocklot != null && Strblocklot != "") {
					request.getSession().setAttribute("BlockLot",
							Strblocklot + " " + "Blocked ");
				}
			}
			else {
				throw new Exception(
						"Unable Download Data's,Please check the outbound text file");
			}
		}

		// Catch block commented and added new block by Arun on 15 June 2011 for
		// #1851
		/*
		 * catch (Exception e) {
		 * 
		 * DbBean.RollbackTran(ut);
		 * request.getSession().setAttribute("RESULTOUTERR1", e.getMessage());
		 * response.sendRedirect("jsp/ImportSO.jsp?action=resulterror1");
		 * 
		 * throw e; }
		 */

		catch (Exception e) {

			DbBean.RollbackTran(ut);
			String message = "";
			// Code added by Arun for #1851 on 15 June 2011 - to remove SQL
			// driver error message from the exception
			if (null != e.getMessage() && !e.getMessage().equals("")
					&& e.getMessage().length() > 54) {
				message = e.getMessage();
				if (message.contains("Unable to commit")) { // Commit and
															// Timeout occured
															// at same time
					message = "Import failed. Error: Transaction timed out.";
				} else if (message.contains("Import failed")) {
					// below line is commented by jyoti for Migration of sql2000
					// to sql2008
					// message = e.getMessage().substring(54); // Shows message
					// set in proc_IRO_IMPORT
					message = e.getMessage();
				} else if (message.contains("Another file is processing")) {
					message = e.getMessage();
				} else {
					message = "Import failed. Error: " + e.getMessage(); // cases
																			// other
																			// than
																			// transaction
																			// time
																			// out
				}
			} else {
				message = e.getMessage();
				if (message.contains("Already marked for rollback")) { // Commit
																		// after
																		// transaction
																		// timeout
					message = "Import failed. Error: Transaction timed out.";
				} else {
					message = "Import failed. Error: " + e.getMessage(); // cases
																			// other
																			// than
																			// transaction
																			// time
																			// out
				}
			}
			request.getSession().setAttribute("RESULTOUTERR1", message);
			response.sendRedirect("jsp/ImportSO.jsp?action=resulterror1");
			throw e;
		}
	}

	public static String replaceCharInString(char toReplace,
			String replaceWith, String inString) {
		String cleanedString = "";
		for (int i = 0; i < inString.length(); i++) {

			if (inString.charAt(i) == toReplace) {
				cleanedString += replaceWith;
			} else {
				cleanedString += inString.charAt(i);
			}

		}// end for

		return cleanedString;
	}// end replace
}