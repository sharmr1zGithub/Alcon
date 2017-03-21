package com.murho.servlet;

import com.murho.DO.TransactionDTO;

import com.murho.dao.OBTravelerDetDAO;

import com.murho.db.utils.DataDownloaderUtil;

import com.murho.gates.DbBean;

import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;

import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.transaction.UserTransaction;


public class TrayLabelingWriteFileServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
    MLogger logger = new MLogger();
    StrUtils strUtils = new StrUtils();
    String action = "";
    String PLANT = "";
    String login_user = "";
    String sys_date = "";
    String StrDeliveryNo = "";
    String xmlStr = "";
    XMLUtils xu = new XMLUtils();

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);

        PrintWriter out = response.getWriter();

        try {
            action = StrUtils.fString(request.getParameter("submit")).trim();
            PLANT = (String) request.getSession().getAttribute("PLANT");
            login_user = (String) request.getSession().getAttribute("LOGIN_USER");

            MLogger.log(0, "action : " + action);

            // *****************************************
            // Added by jyoti for  TIBCO-INC000002484471(WMS 2.8)
            // To get the count of MTIDs in a delivery
            // *****************************************
            if (action.equalsIgnoreCase("WriteFile")) {
                getCountMTID(request, response);
            }
            // **********************************************
            // Added by jyoti for  TIBCO-INC000002484471(WMS 2.8)
            // To generate SAP file automatically from .Net on scan of last tray
            // of delivery
            // *************************************************
            else if (action.equalsIgnoreCase("WriteFileFromPDA")) {
                System.out.println("calling writefilefromPDA funtion");
                xmlStr = onWrite_SAPfileFromPDA(request, response);
                System.out.println("inside else if " + xmlStr);
            }

            //********************************************************
            //below else if commented by jyoti for  TIBCO-INC000002484471(WMS 2.8)
            /*
            else if (action.equalsIgnoreCase("WriteFile")) {
            
                    onWrite_TrayLabelFile(request, response);
            }*/
        } catch (Exception ex) {
            xmlStr = xu.getXMLMessage(1, ex.getMessage());
            System.out.println("inside catch1" + xmlStr);
            System.out.println(ex.getMessage());
        }

        //		Below condition is added by jyoti for  TIBCO-INC000002484471 1850
        if (xmlStr.equalsIgnoreCase("")) {
            xmlStr = xu.getXMLMessage(1,
                    "Some inputs were missing,Please check once again!!!");
            System.out.println("inside if1" + xmlStr);
        }

        MLogger.log(-1,
            "_______________________ " + this.getClass() + " doGet()");
        out.write(xmlStr);
        out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        doGet(request, response);
    }

    private void onWrite_TrayLabelFile(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException, Exception {
        HttpSession label = request.getSession();

        // StrDeliveryNo =
        // label.getAttribute("LABELSUMMARY").toString();//StrUtils.fString(request.getParameter("ITEM"));
        StrDeliveryNo = request.getParameter("WriteFile").trim();

        String StrRefno = label.getAttribute("LABELREFNO").toString();
        String StrPallet = label.getAttribute("LABELPALLET").toString();
        TransactionDTO trnDTO = new TransactionDTO();
        DataDownloaderUtil _DataDownloaderUtil = new DataDownloaderUtil();

        // System.out.println("StrDeliveryNo...................."+StrDeliveryNo);
        ArrayList invQryList = new ArrayList();

        UserTransaction ut = null;
        boolean flag = false;

        try {
            ut = com.murho.gates.DbBean.getUserTranaction();
            ut.begin();

            ArrayList alTransactionData = new ArrayList();
            trnDTO.setTraveler(StrDeliveryNo);
            // trnDTO.setPalletid(StrPallet);
            alTransactionData.add(trnDTO);

            invQryList = _DataDownloaderUtil.getTrayLabelingWriteFileById(alTransactionData);
            System.out.println("arrrya................" + invQryList.size());
            
            /*Added by Ranjana for LOT restriction under ticket WO0000000284867*/
            if(invQryList.size()==0){
            	 MLogger.log(0, " getting within if " );
            	 throw new Exception("Error in writing file. LOT is restricted. ");
                 	            }
            else  if (invQryList.size() > 0) {
                flag = _DataDownloaderUtil.process_writefile(invQryList);

                if (flag == true) {
                    DbBean.CommitTran(ut);
                    // request.getParameter("WriteFile").trim());
                    // request.getSession().setAttribute("ITEM",StrDeliveryNo);
                    request.getSession()
                           .setAttribute("RESULT", "File Write Successfully");

                    response.sendRedirect(
                        "jsp/TrayLabelingSummary.jsp?action=result&ITEM=" +
                        StrDeliveryNo + "&PALLET=" + StrPallet + "&REFNO=" +
                        StrRefno);

                    // response.sendRedirect("jsp/TrayLabelingSummary.jsp?action=result");
                } else {
                    throw new Exception("Error in writing file");
                }
            }else {
                throw new Exception("Delivery No. not exists");
            }
        } catch (Exception e) {
            DbBean.RollbackTran(ut);
            MLogger.log(0, e.getMessage() );
            request.getSession().setAttribute("RESULTERR", e.getMessage());
            response.sendRedirect(
                "jsp/TrayLabelingSummary.jsp?action=resulterr");
            
            /*Commented by Ranjana for LOT restriction under ticket WO0000000284867 to avoid NULL POINTER EXCEPTION*/
            //throw e;
        }
    }

    // ************************************************************
    // Below function is added by Jyoti forTIBCO-INC000002484471(WMS 2.8)
    // This function will return the count of MTIDs in a delivery.
    // ************************************************************
    private int getCountMTID(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException, Exception {
        int count = 0;
        MLogger.info(
            "*************** getCountMTID() << STARTS >> ***************");

        Connection con = null;
        OBTravelerDetDAO _OBTravelerDetDAO = null;
        TransactionDTO trnDTO = null;

        try {
            trnDTO = new TransactionDTO();
            _OBTravelerDetDAO = new OBTravelerDetDAO();
            StrDeliveryNo = request.getParameter("WriteFile").trim();
            trnDTO.setTraveler(StrDeliveryNo);
            count = _OBTravelerDetDAO.getCountofMTIDbyTravelerWise(trnDTO);

            if (count > 0) {
                response.sendRedirect(
                    "jsp/TrayLabelingSummary.jsp?action=resultcount");
            } else {
                onWrite_TrayLabelFile(request, response);
            }
        } catch (Exception e) {
            request.getSession().setAttribute("RESULTERR", e.getMessage());
            response.sendRedirect(
                "jsp/TrayLabelingSummary.jsp?action=resulterr");
            throw e;
        } finally {
            if (con != null) {
                DbBean.closeConnection(con);
            }
        }

        MLogger.info(
            "*************** getCountMTID() << ENDS >> ***************");

        return count;
    }

    // ************************************************************
    // Below function is added by Jyoti for TIBCO-INC000002484471(WMS 2.8)
    // This function will generate SAP confirmation file automatically
    // when request comes from .Net side.
    // ************************************************************
    private String onWrite_SAPfileFromPDA(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException, Exception {
        MLogger.info(
            "*************** onWrite_SAPfileFromPDA() << STARTS >> ***************");
        StrDeliveryNo = request.getParameter("StrDeliveryNo").trim();
        System.out.println(StrDeliveryNo + " delivery from PDA");

        TransactionDTO trnDTO = new TransactionDTO();
        DataDownloaderUtil _DataDownloaderUtil = new DataDownloaderUtil();
        ArrayList invQryList = new ArrayList();
        UserTransaction ut = null;
        boolean flag = false;

        try {
            ut = com.murho.gates.DbBean.getUserTranaction();
            ut.begin();

            ArrayList alTransactionData = new ArrayList();
            trnDTO.setTraveler(StrDeliveryNo);
            alTransactionData.add(trnDTO);
            invQryList = _DataDownloaderUtil.getTrayLabelingWriteFileById(alTransactionData);
            MLogger.log(1, " size of the arraylist" + invQryList.size());

            /*Added by Ranjana for LOT restriction under ticket WO0000000284867*/
            if (invQryList.size() == 0) {
                xmlStr = xu.getXMLMessage(1, "Lot Is Blocked");
                throw new Exception("Error in writing file. LOT Is Blocked.");
            }
            else if (invQryList.size() > 0) {
                flag = _DataDownloaderUtil.process_writefile(invQryList);

                if (flag == true) {
                    DbBean.CommitTran(ut);
                    xmlStr = xu.getXMLMessage(0, "File Write Successfully");
                } else {
                    xmlStr = xu.getXMLMessage(1,
                            "File Write not Successful....flag is false");

                    throw new Exception("Error in writing file");
                }
            }
            else {
                System.out.println("before throwing ");
                throw new Exception("Delivery No. not exists");
            }
        } catch (Exception e) {
            MLogger.exception(this, e);

            response.sendRedirect(
                "jsp/TrayLabelingSummary.jsp?action=resultcount");
            throw new Exception("Unable to write file ::::::: Exception : " +
                e.getMessage());
        }

        MLogger.info(
            "*************** onWrite_SAPfileFromPDA() << ENDS >> ***************");

        return xmlStr;
    }
}
