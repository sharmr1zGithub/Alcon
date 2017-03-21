package com.murho.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.murho.DO.TransactionDTO;
import com.murho.dao.BaseDAO;
import com.murho.dao.RecvDetDAO;
import com.murho.db.utils.DataDownloaderUtil;
import com.murho.db.utils.InvMstUtil;
import com.murho.gates.DbBean;
import com.murho.gates.Generator;
import com.murho.utils.CibaConstants;
import com.murho.utils.DateUtils;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;
import com.oreilly.servlet.MultipartRequest;


public class InboundDataDownloaderServlet 
  extends HttpServlet
{
 // public InboundDataDownloaderServlet()
 // {
 // }
 
  XMLUtils xu = new XMLUtils();
  MLogger logger = new MLogger();
  StrUtils strUtils = new StrUtils();
  Generator generator = new Generator();
  InvMstUtil _InvMstUtil = null;
  DataDownloaderUtil  _DataDownloaderUtil = null;
  RecvDetDAO _RecvDetDAO=null;
  private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
  
  String xmlStr = "";
  String action = "";
  String strLoginUser = "";
  String fieldDesc="";
  String fileName="";
   String StrFileName="";
  int arraySize;

  //private static final String CONTENT_TYPE = "text/xml";
  	

 
  public void init(ServletConfig config) throws ServletException {
  //  _InvMstUtil = new InvMstUtil();
  super.init(config);
  }
  
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException {
      response.setContentType(CONTENT_TYPE);
      PrintWriter out = response.getWriter();
     
      try
      
      {
        action = request.getParameter("submit").trim();
        StrFileName = StrUtils.fString(request.getParameter("ImportFile"));
        System.out.println(" ********************** Entering");
       
        
        
        System.out.println(" ********************** Entering Action = " + action );
        
     /*  if(action.equalsIgnoreCase("import"))
       {
      
         System.out.println("Action = ");
         Import_inbound_text_file(request, response);
         
       }*/
      
      }
    catch (Exception e) 
    {
      MLogger.exception(" Exception :: doGet() : ", e);
      xmlStr = xu.getXMLMessage(1,"Error : " + e.getMessage());
      System.out.println("error msg........." + e.getMessage());
    }
     MLogger.log( -1,   strLoginUser + " : " + this.getClass() + " doGet() ##################");
      out.write(xmlStr);
      out.close();
    
  }

  
   public void doPost(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException {
     response.setContentType("text/html"); 
      PrintWriter out = response.getWriter(); 
    doGet(request, response);
    
  }
  
    private void  Import_inbound_text_file(HttpServletRequest request,
                              HttpServletResponse response) throws
      IOException, ServletException , Exception{
    
     try 
     {
     
     
        String filename = "";
		  	File f = new File(StrFileName);
			  filename = f.getName();
			  f = null;
        
          System.out.println("InboundDataDowloaderServlet : Import_inbound_text_file()");
         
		  		MultipartRequest mreq = new MultipartRequest(request,	"D:\\MURHO\\WMS_UPLOAD\\", 2048000);
          System.out.println("Come Out...");
            
         fileName  = request.getParameter("ImportFile").trim();
         
         String strfileName = fileName.substring(fileName.lastIndexOf('\\')+1, fileName.length());
       
         HttpSession LoginUser = request.getSession();
         String strLoginUser=LoginUser.getAttribute("LOGIN_USER").toString();
     
        
          DataDownloaderUtil  _DataDownloaderUtil=new   DataDownloaderUtil();
          
         String StrFileName="D:\\MURHO\\WMS_UPLOAD\\" + fileName ;
      
         _DataDownloaderUtil.downloadTextData(StrFileName);
         
         //To get array size from inbound file
         ArrayList aList=new ArrayList();
         
         aList= _DataDownloaderUtil.fileList;
         
         String Id="";
         int inFlag=0;
         boolean inFlagMsg=false;
         boolean transFlag=false;
        
         
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
         int iCnt=0;
         
  
                 
        //To set object for HdrRecv Table
                  
         if(aList.size()>0)
         {
             for(int i = 0; i < aList.size(); i++) {
                ArrayList tempLists = (ArrayList)aList.get(i);
                
               
               // sno=i-1;
                for(int j = 0; j < tempLists.size(); j++ ) { 
                  if(tempLists.size()<=5)
                  {
                     if(tempLists.get(1).equals("E"))
                    {
                      arrtraveler=tempLists.get(2).toString();
                      arrhu1=tempLists.get(4).toString();
                    }
                  j=5;
                 }
                 
                  if(tempLists.size()<=6)
                  {
                  if(tempLists.get(1).toString().trim().equals("1"))
                   {
                    arrhu2=tempLists.get(5).toString();
                   }
                  j=6;
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
                      arrcreatedon="";
                      eList.add(arrcreatedon);
                  
                    
                      totList.add(eList);
                      eList=new ArrayList();
               
                    }
                     j=10;
                  }
                 
                }
             }
         }
         
         //for(int k=0;  k < totList.size(); k++) {
         
            //System.out.println("K...."+totList.get(k)+"</br>");
         
       //  }
          
           for(int i = 0; i < totList.size(); i++) {
           ArrayList tempLists = (ArrayList)totList.get(i);
           sno=i+1;
           for(int j = 0; j < tempLists.size(); j++ ) { 
              trnDTO =new TransactionDTO();
              String travelerid=tempLists.get(0).toString();
              Id=travelerid;
              long pallet=Long.parseLong(((String)tempLists.get(1)).trim().toString());
              String linenos=tempLists.get(2).toString();
              String mtid=tempLists.get(3).toString();
              String sku=tempLists.get(5).toString();
              String lot=tempLists.get(6).toString();
              int qty= Integer.parseInt(((String)tempLists.get(7)).trim().toString());
              String uom=tempLists.get(8).toString();
              String createdon=tempLists.get(9).toString();
         
              trnDTO.setReceivestatus("N");
              trnDTO.setPutawaystatus("N");
              trnDTO.setFilegenerated("N");
              trnDTO.setStatus("N");
              trnDTO.setCrat(DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()) );
              trnDTO.setCrby(strLoginUser);
              trnDTO.setFilename(strfileName);
        
              trnDTO.setTraveler(travelerid);
        //      trnDTO.setPallet(pallet);
              trnDTO.setMtid(mtid);
              trnDTO.setSku(sku);
              trnDTO.setLot(lot);
              trnDTO.setOrdqty(qty);
              trnDTO.setPutawayqty(putawayqty);
              trnDTO.setPutawaystatus("N");
              
              trnDTO.setUserfld1(uom);
              trnDTO.setReceivestatus("N");
              trnDTO.setCreatedon(createdon);
              trnDTO.setLinenos(linenos);
              
                
              alTransactionData.add(trnDTO);
             inFlag = _DataDownloaderUtil.process_import(alTransactionData);
              
              j=10;
             }
              
           }
              if(sno<=0)
           {
              request.getSession().setAttribute("RESULTINERR1", "Delivery No's, Import Failed");
              response.sendRedirect("jsp/ImportTraveler.jsp?action=resultinerr1");
           }
           else if( _DataDownloaderUtil.detflag)
           {
            request.getSession().setAttribute("RESULTINERR2", _DataDownloaderUtil.rtnMsg);
            response.sendRedirect("jsp/ImportTraveler.jsp?action=resultinerr2");
           }
           else
           {
          
        //   iCnt= _DataDownloaderUtil.getCalcApproval();
        
                  
          int iCalcCnt=0;
          ArrayList arrList=new ArrayList();
          arrList =_DataDownloaderUtil.getCalcTraveler();
         
           try
           {
            for(int i=0;i<arrList.size();i++)
            {
               ArrayList alCalcTransactionData=new ArrayList();
               trnDTO =new TransactionDTO();
               trnDTO.setTraveler(arrList.get(0).toString());
          
               alCalcTransactionData.add(trnDTO);
             //  System.out.println("alCalcTransactionData........."+alCalcTransactionData);
               iCalcCnt =_DataDownloaderUtil.getCalcAllocation(alTransactionData);
          
            }
           }
            catch (Exception e) {
             MLogger.exception(this,e);;
             System.out.print("Get Msg   "+ e.getMessage());
              throw e;
           }
           
           BaseDAO _BaseDAO = new BaseDAO();
           java.sql.Connection con=null;
           con=DbBean.getConnection();
           
        
           StringBuffer sbMovHis=new StringBuffer("");
          // sbMovHis.append("INSERT INTO MOVHIS(PLANT,TRAVELER,MOVTID,QTY,CRAT,CRTIME,CRBY,HDRFILENAME)VALUES(");
           sbMovHis.append("INSERT INTO MOVHIS(PLANT,MOVTID,CRAT,CRTIME,CRBY,HDRFILENAME)VALUES(");
           sbMovHis.append("'" + CibaConstants.cibacompanyName +"',");
          // sbMovHis.append("'" + " " +"',");
           sbMovHis.append("'" + "IMPORT-TRAVELER" +"',");
          // sbMovHis.append("'" + 0 +"',");
           sbMovHis.append("'" + DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()) +"',");
           sbMovHis.append("'" + DateUtils.Time() +"',");
           sbMovHis.append("'" + strLoginUser +"',");
           //System.out.println("GetFileName...."+ _strfileName);
           sbMovHis.append("'" + strfileName +"'");
           sbMovHis.append(")");
         
           transFlag=_BaseDAO.insertData(con,sbMovHis.toString());
           if (con != null) {
                 DbBean.closeConnection(con);
            }
           
           request.getSession().setAttribute("RESULTINNOERR", sno+ " " + "Rows of Data's Import Successfully");
           response.sendRedirect("jsp/ImportTraveler.jsp?action=resultinnoerr");
           }
                
     }
    catch (Exception e) {
       MLogger.exception(this,e);;
       System.out.print("Get Msg   "+ e.getMessage());
       throw e;
    }
  
  }


}