//************* Created by Ranjana for UDI Part2 implementation  under ticket no. WO0000000471852 **********//

package com.murho.servlet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.murho.gates.userBean;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;
import com.oreilly.servlet.MultipartRequest;

public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	String action = "";

	String xmlStr = "";

	String strLoginUser = "";

	XMLUtils xu = new XMLUtils();

	String fileName = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
			try {
				action = StrUtils.fString(request.getParameter("submit"))
						.trim();
				MLogger.log(0, "action : " + action);
				if (action.equalsIgnoreCase("Import")) {
					Import_item_excel_file(request, response);

					fileName = request.getParameter("ImportFile").trim();
					String strHdrfileName = fileName.substring(fileName
							.lastIndexOf('\\') + 1, fileName.length());

					MLogger.log(0, "filename : " + strHdrfileName);
					response
							.sendRedirect("jsp/FileUploader.jsp?action=ImportItem&ImportFile="
									+ strHdrfileName);

				}
			} catch (Exception e) {
				MLogger.exception(" Exception :: doGet() : ", e);
				request.getSession().setAttribute("RESULTOUTERR1",e.getMessage());
				response.sendRedirect("jsp/FileUploader.jsp?action=resulterror1");

			}

		MLogger.log(-1, strLoginUser + " : " + this.getClass()
				+ " doGet() ##################");
		out.write(xmlStr);
		out.close();
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);

	}

	// Used to Save the uploaded file at the specified path ie:
	// "UPLOAD_FOLDER_PATH" using MultipartRequest
	private void Import_item_excel_file(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		userBean ub = null;
		Map sysParams = null;

		ub = new userBean();
		sysParams = ub.getSystemParams();
		String filepath = (String) sysParams.get("UPLOAD_FOLDER_PATH");
		MultipartRequest mreq = new MultipartRequest(request, filepath, 5242880);
		}
	}
