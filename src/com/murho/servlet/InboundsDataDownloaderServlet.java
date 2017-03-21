package com.murho.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import com.murho.DO.TransactionDTO;
import com.murho.dao.BaseDAO;
import com.murho.dao.RecvDetDAO;
import com.murho.dao.RestrictLotDAO;
import com.murho.dao.SQLRecvDet_DAO;
import com.murho.db.utils.DataDownloaderUtil;
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
public class InboundsDataDownloaderServlet extends HttpServlet 
{

  XMLUtils xu = new XMLUtils();
  MLogger logger = new MLogger();
  StrUtils strUtils = new StrUtils();
  Generator generator = new Generator();
  DataDownloaderUtil  _DataDownloaderUtil = null;
  RecvDetDAO _RecvDetDAO=null;
  
  String action = "";
	String PLANT = "";
	String login_user = "";
	String sys_date = "";
	String StrFileName = "";
  
   
  String xmlStr = "";
  String strLoginUser = "";
  String fieldDesc="";
  String fileName="";

  int arraySize;
  
  private static final String CONTENT_TYPE = "text/html; charset=windows-1252";

  public void init(ServletConfig config) throws ServletException
  {
    super.init(config);
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
   response.setContentType(CONTENT_TYPE);
     PrintWriter out = response.getWriter();

		try {

			action = StrUtils.fString(request.getParameter("submit")).trim();
      
			StrFileName = StrUtils.fString(request.getParameter("ImportFile"));
			System.out.println("Import File  *********:" + StrFileName);
		

			PLANT = (String) request.getSession().getAttribute("PLANT");
			login_user = (String) request.getSession().getAttribute(
					"LOGIN_USER");
	
			MLogger.log(0, "action : " + action);

			if (action.equalsIgnoreCase("Import")) {

				 Import_inbound_text_file(request, response);

			}

		} catch (Exception ex) {

		
		}

  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
  response.setContentType(CONTENT_TYPE);
		doGet(request, response);
  }
  
  private void onImport(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
   
    
		MLogger.info("********************* onImport **************************");
   
   
		try {

			try {
				MultipartRequest mreq = new MultipartRequest(request,
		       CibaConstants.CReadFromServerFolder,2048000);
			} catch (Exception eee) {
			///	MLogger.info("Exception :: ImportSalesOrder :: onImport" + eee.getMessage());
			throw eee;
			}


		
			// StrFileName = "D:\\MURHO\\WMS_UPLOAD\\aaa.xls";
			// StrFileName = "S";

			// get the sheet name
			String filename = "";
			File f = new File(StrFileName);
			filename = f.getName();
			f = null;
			// hard code the sheet name along with the d:\\working\\data_in\\
			// folder
			StrFileName =  CibaConstants.CReadFromServerFolder + filename; //"D:\\MURHO\\WMS_UPLOAD\\" + filename;

			System.out.println("After conversion Import File  *********:"
					+ StrFileName);

		} catch (Exception e) {
			throw e;
		}

	//	request.getSession().setAttribute("soData", al_data);

	//	response.sendRedirect("jsp/Import_so_picking.jsp?action=GO");

	}
  
   private void  Import_inbound_text_file(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
        UserTransaction ut = null;
        userBean ub = null;
        Map sysParams = null;
     try 
     {
     	 SQLRecvDet_DAO _SQLRecvDet = new SQLRecvDet_DAO();	
         //below line is commented by jyoti for TIBCO-INC000002484471(WMS 2.8)
        // MultipartRequest mreq = new MultipartRequest(request,	CibaConstants.CReadFromServerFolder, 2048000);
             	  
         fileName  = request.getParameter("ImportFile").trim();
         String strHdrfileName = fileName.substring(fileName.lastIndexOf('\\')+1, fileName.length());
         
         //below line is commented by jyoti for TIBCO-INC000002484471(WMS 2.8)
         //String StrFileName= CibaConstants.CReadFromServerFolder+ strHdrfileName; 
         
         //below lines is added by jyoti for  TIBCO-INC000002484471(WMS 2.8)
         ub = new userBean();
    	 sysParams = ub.getSystemParams();
    	 String filepath = (String)sysParams.get("UPLOAD_FOLDER_PATH");
    	 MultipartRequest mreq = new MultipartRequest(request,	filepath, 2048000);
         String StrFileName= filepath+ strHdrfileName;
         
         HttpSession LoginUser = request.getSession();
         String strLoginUser=LoginUser.getAttribute("LOGIN_USER").toString();
         
         DataDownloaderUtil  _DataDownloaderUtil=new   DataDownloaderUtil();
        
        _DataDownloaderUtil.downloadTextData(StrFileName);
         
         //To get array size from inbound file
         ArrayList aList=new ArrayList();
         
         aList= _DataDownloaderUtil.fileList;
        
         
         String Id="";
         int inFlag=0;
         //boolean inFlagMsg=false;
         boolean transFlag=false;
         boolean chkPUV=false;
        
         
         //To get Transaction DO
         TransactionDTO  trnDTO;
         ArrayList alTransactionData=new ArrayList();
         ArrayList eList=null;
         eList=new ArrayList();
         ArrayList totList=new ArrayList();
         int sno=0;     
         String arrtraveler="";
         String arrhu1="";
         String arrhu2="";
         String arrhu3="";
         String arrhu4="";
         String arrmatrial="";
         String arrbatch="";
         String arrqty="";
         String arruom="";
         String arrcreatedon="";
         int putawayqty= Integer.parseInt("0");
         int recvqty= Integer.parseInt("0");
         //int iCnt=0;
         
         int qty=0;
         String qtystr="";
              
            
      /*   for(int k=0;k<aList.size();k++)
         {
            System.out.println("aList......."+aList.get(k)+ "<br>");
            ArrayList tempLists = (ArrayList)aList.get(k);
            
            for(int j = 0; j < tempLists.size(); j++ ) { 
               if(tempLists.get(j).equals(""))
               {
               //  System.out.println("Derwerwer......."+tempLists.get(j)+ "<br>");
              //   throw new Exception("Unable Download Data's,Pls check the inbound text file");
               }
            }
         }    */
                   
        //To set object for HdrRecv Table
       
         if(aList.size()>0)
         {
          
             for(int i = 0; i < aList.size(); i++) {
                
                ArrayList tempLists = (ArrayList)aList.get(i);
              
               
               // sno=i-1;
                for(int j = 0; j < tempLists.size(); j++ ) 
                { 
                
                 if(tempLists.size() <=0){
                  
                    throw new Exception("Unable Download Data,Please check the inbound delivery file");
                 }
                
                 
                 if(tempLists.size()>5  && tempLists.get(1).equals("E"))
                  {
                     throw new Exception("Unable Download Data,Please check the inbound delivery file");
                  }
                  
                  
                  if(tempLists.size()<=5)
                  {
                    if(tempLists.get(1).equals("E"))
                    {
                      sno=sno+1;
                      arrtraveler=tempLists.get(2).toString();
                      arrhu1=tempLists.get(4).toString();
                     
                       j=5;
                      
                    }
                 
                 }
               
                  if(tempLists.size()>6  && tempLists.get(1).equals("1"))
                  {
                     throw new Exception("Unable Download Data,Please check the inbound delivery file");
                  }
                  
                //  System.out.println("arrhu1........");
               //  System.out.println("Temp........"+tempLists.get(i)+"</br>");
                  if(tempLists.size()<=6)
                  {
                      
                  if(tempLists.get(1).toString().trim().equals("1"))
                   {
                    
                     arrhu2=tempLists.get(5).toString();
                   
                          
                   }
                  j=6;
                 }
               
                 
                  if(tempLists.size()>6  && tempLists.get(1).equals("2"))
                  {
                     throw new Exception("Unable Download Data,Please check the inbound delivery file");
                  }
                  
                  if(tempLists.size()<=6)
                 {
                  if(tempLists.get(1).toString().trim().equals("2"))
                  {
                    arrhu3=tempLists.get(3).toString();
                    arrhu4=tempLists.get(5).toString();
                  }
                 j=6;
                 }
                
                  //System.out.println("arrhu1........"+arrhu1);
                  if(tempLists.size()==10){
                    if(tempLists.get(1).toString().trim().equals("3"))
                    {
                      eList.add(arrtraveler);
                      eList.add(arrhu1);
                     
                      eList.add(arrhu2);
                      eList.add(arrhu3);
                      eList.add(arrhu4);
                      arrmatrial=tempLists.get(5).toString().trim();
                      eList.add(arrmatrial);
                      arrbatch=tempLists.get(6).toString().trim();
                      eList.add(arrbatch);
                      arrqty=tempLists.get(7).toString().trim();
                      eList.add(arrqty);
                      arruom=tempLists.get(8).toString().trim();
                      eList.add(arruom);
                      arrcreatedon=tempLists.get(9).toString().trim();
                      eList.add(arrcreatedon);
                    
                      totList.add(eList);
                      eList=new ArrayList();
                   
                      j=10;
                    }
                     j=10;
                  }
                  else//(aList.size()==9)
                  {
                  if(tempLists.get(1).toString().trim().equals("3"))
                  {
                      eList.add(arrtraveler);
                      eList.add(arrhu1);
                      eList.add(arrhu2);
                      eList.add(arrhu3);
                      eList.add(arrhu4);
                      arrmatrial=tempLists.get(5).toString().trim();
                      eList.add(arrmatrial);
                      arrbatch=tempLists.get(6).toString().trim();
                      eList.add(arrbatch);
                      arrqty=tempLists.get(7).toString().trim();
                      eList.add(arrqty);
                      arruom=tempLists.get(8).toString().trim();
                      eList.add(arruom);
                      eList.add(arrcreatedon);
                  
                    
                      totList.add(eList);
                      eList=new ArrayList();
               
                    }
                     j=10;
                  }
                 
                }
             }
         }
      
      
      /*  for(int k=0;k<totList.size();k++)
         {
            ArrayList tempLists = (ArrayList)totList.get(k);
            for(int j = 0; j < tempLists.size(); j++ ) { 
               if(tempLists.get(j).equals(""))
               {
                 System.out.println("Derwerwer......."+tempLists.get(j)+ "<br>");
              //   throw new Exception("Unable Download Data's,Pls check the inbound text file");
               }
            }
         }*/
           ut = com.murho.gates.DbBean.getUserTranaction();
           ut.begin();
           
          // Variable declared to check the Restricted LOT 
           String StrblockLot = "",TravelerId="";
                   
           for(int i = 0; i < totList.size(); i++) {
        	   String chkLOT = "";
           ArrayList tempLists = (ArrayList)totList.get(i);
            for(int j = 0; j < tempLists.size(); j++ ) { 
              trnDTO =new TransactionDTO();
             // alTransactionData.clear();
              String travelerid=tempLists.get(0).toString();
              Id=travelerid;
              //long pallet=Long.parseLong(((String)tempLists.get(1)).trim().toString());
              String pallet=tempLists.get(1).toString();
              String linenos=tempLists.get(2).toString();
              String mtid=tempLists.get(3).toString();
              String sku=tempLists.get(5).toString();
              String lot=tempLists.get(6).toString();
              
/* Calling a method for excluding the Restricted LOT from inserting into the Database */ 
              chkLOT =_SQLRecvDet.checkLOT(lot);            
                                       
              qtystr=tempLists.get(7).toString().replaceAll(",","");
              qty= Integer.parseInt(((String)qtystr).trim().toString());
             // int qty= Integer.parseInt(((String)tempLists.get(7)).trim().toString());
              chkPUV= _SQLRecvDet.checkPUV(sku);
                 
              if(chkPUV==true)
              {
                 qty=qty * 6;
              }
              String uom=tempLists.get(8).toString();
              String createdon=tempLists.get(9).toString();
         
              trnDTO.setReceivestatus("N");
              trnDTO.setPutawaystatus("N");
              trnDTO.setFilegenerated("N");
              trnDTO.setStatus("N");
              trnDTO.setCrat(DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()) );
              trnDTO.setCrby(strLoginUser);
              trnDTO.setFilename(strHdrfileName);
        
              trnDTO.setTraveler(travelerid);
              trnDTO.setPallet(pallet);
              trnDTO.setMtid(mtid);
              trnDTO.setSku(sku);
              trnDTO.setLot(lot);
              trnDTO.setOrdqty(qty);
              trnDTO.setRecvqty(recvqty);
              trnDTO.setPutawayqty(putawayqty);
              trnDTO.setPutawaystatus("N");
              
              trnDTO.setUserfld1(uom);
              trnDTO.setReceivestatus("N");
              trnDTO.setCreatedon(createdon);
              trnDTO.setLinenos(linenos);
              
      /* Condition for Adding the value to arraylist if lot is not Blocked.
       * TravelerId: Variable for adding the traveler to check the number of deliveries imported successfully. */             
              MLogger.log(0, "proc return : " + chkLOT);
           
              if (chkLOT.equals("1")) {
						StrblockLot += lot + " ";
						MLogger.log(0, "lot value...." + StrblockLot);
					} 
              alTransactionData.add(trnDTO);	                    
              j=10;
             }             
           }  
           
     // Checking for non duplicate values of LOT 
           
           MLogger.log(0,"StrblockLot..."+StrblockLot);
           
           String Strblocklot = "";
           if (StrblockLot != null && StrblockLot != "") {
				String[] BlockLot = StrblockLot.split(" ");
				Set hs = new HashSet();
				hs.addAll(Arrays.asList(BlockLot));
				Strblocklot = String.valueOf(hs);				
			}
           
    /* Check for the no. of deliveries imported properly in the system */ 
           MLogger.log(0,"TravelerId..."+TravelerId);
          
           if (TravelerId != null && TravelerId != "") {
				String[] Traveler = TravelerId.split(";");			
				Set hs = new HashSet();
				hs.addAll(Arrays.asList(Traveler));
				sno = hs.size();			
			} 
           
         inFlag = _DataDownloaderUtil.process_import(alTransactionData);
           int iCalcCnt=0;
           
           //Modified on 3-Apr-2014 for ticket Import Inbound File Process #INC000003097020
            iCalcCnt =_DataDownloaderUtil.getCalcAllocation(alTransactionData);
            if( iCalcCnt==0)
            {
             throw new Exception("Error in Space Allocation");
              /*  DbBean.RollbackTran(ut);
                request.getSession().setAttribute("RESULTINERR1", "Error in Space Allocation");
                response.sendRedirect("jsp/ImportTraveler.jsp?action=resultinerr1");*/
           
            }
            BaseDAO _BaseDAO = new BaseDAO();
            java.sql.Connection con=null;
          
            StringBuffer sbMovHis=new StringBuffer("");
            sbMovHis.append("INSERT INTO MOVHIS(PLANT,MOVTID,CRAT,CRTIME,CRBY,REMARK)VALUES(");
            sbMovHis.append("'" +CibaConstants.cibacompanyName +"',");
            sbMovHis.append("'" + TransactionTypes.Import_inbound_file_tran_type +"',");
            sbMovHis.append("'" + DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()) +"',");
            sbMovHis.append("'" + DateUtils.Time() +"',");
            sbMovHis.append("'" + strLoginUser +"',");
            sbMovHis.append("'" + strHdrfileName+"'");
            sbMovHis.append(")");
         
            con=DbBean.getConnection();
            transFlag=_BaseDAO.insertData(con,sbMovHis.toString());
           
            if (con != null) {
                 DbBean.closeConnection(con);
            }
            
            if(transFlag)
            {
              DbBean.CommitTran(ut);
            }else
            {
              throw new Exception(" Failed to Import Delivery No");
            }
            
            
            //request.getSession().setAttribute("RESULTINNOERR", sno+ " " + "Delivery No.'s Import Successfully");
            
            /* Updated by Ranjana
             * Purpose: To display the message of success on the UI 
             * along with the restricted LOTs. */
            
           
            	request.getSession().setAttribute("RESULTINNOERR", sno+ " " + "Delivery No.'s Imported Successfully");
            
            
            if(Strblocklot!=null && Strblocklot!=""){
         	   request.getSession().setAttribute("Strlot" ,Strblocklot+ " " + "Blocked"); 
            }
            response.sendRedirect("jsp/ImportTraveler.jsp?action=resultinnoerr");
            
                                    
     }
    catch (Exception e) {
     DbBean.RollbackTran(ut);
     request.getSession().setAttribute("RESULTINERR2", e.getMessage());
     response.sendRedirect("jsp/ImportTraveler.jsp?action=resultinerr2");
  /*     DbBean.RollbackTran(ut);
       MLogger.exception(this,e);;
       System.out.print("Get Msg   "+ e.getMessage());
       
       request.getSession().setAttribute("RESULTINERR1", "Import Inbound Delivery No.Failed Err:"+ e.getMessage());
       response.sendRedirect("jsp/ImportTraveler.jsp?action=resultinerr1");*/
       throw e;
    }
     
 
  
  }

}