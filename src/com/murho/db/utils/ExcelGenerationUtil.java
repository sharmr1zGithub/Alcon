package com.murho.db.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.model.Workbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.omg.CORBA.Request;

import com.murho.dao.OBTravelerDetDAO;
import com.murho.dao.RecvDetDAO;
import com.murho.utils.CibaConstants;
import com.murho.utils.DateUtils;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import java.util.Hashtable;
public class ExcelGenerationUtil 
{
  public ExcelGenerationUtil()
  {
  }
  
  
  public void generateExcelStatement(String ITEM,ServletOutputStream out, HttpServletResponse response) throws Exception
  {
  	  SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        Date currTime = new Date();
        String dateString = formatter.format(currTime);

        String fileName = dateString + ".xls";
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        FileInputStream in = null;
        ArrayList invQryList  = new ArrayList();
       RecvDetDAO recvdao = new RecvDetDAO();
        
        String path = "",yymm="";
        String TRN_DATE=DateUtils.getDate();
        int TotCnt=0;
        
        try
        {
              FileOutputStream fout = new FileOutputStream(fileName);
        
        System.out.println("################ FileOutputStream ##############3" );

         

            // create a new workbook
            HSSFWorkbook wb = new HSSFWorkbook();

   
            HSSFSheet s = wb.createSheet();
            System.out.println("HSSFSheet craeted");       // create a new sheet
            // declare a row object reference
            HSSFRow r = null;

            // declare a cell object reference
            HSSFCell c = null;
            HSSFCell c1 = null;
            
            
            
             // create 3 cell styles
            HSSFCellStyle cs = wb.createCellStyle();
            cs.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
            HSSFCellStyle cs2 = wb.createCellStyle();
            cs2.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
            
            //use this for double
            HSSFCellStyle cs3 = wb.createCellStyle();
            HSSFCellStyle cs4 = wb.createCellStyle();
            cs4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            
            
            cs3.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
            //Data Format index 4 holds "#,##0.00"
            //SAF NO. HKG-2003370
        
            String doubleFormat = new HSSFDataFormat(new Workbook()).getFormat((short) 4);
            
            cs3.setDataFormat(new HSSFDataFormat(new Workbook()).getFormat(doubleFormat));
          

            // create 2 fonts objects
            HSSFFont f = wb.createFont();
            HSSFFont f2 = wb.createFont();

            //set font 1 to 12 point type
            f.setFontHeightInPoints((short) 10);
            //make it red
            f.setColor(HSSFFont.COLOR_NORMAL);
            // make it bold
            //arial is the default font
            f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

            //set font 2 to 10 point type
            f2.setFontHeightInPoints((short) 10);
            f2.setColor(HSSFFont.COLOR_NORMAL);

            //set cell stlye
            cs.setFont(f);

            // set the font
            cs2.setFont(f2);
            cs3.setFont(f2);

            // set the sheet name to HSSF Test
            wb.setSheetName(0, "Trav");
            short rowNum = 0;

            // create a row
            r = s.createRow(rowNum);
            c = r.createCell((short) 3);
            c.setCellStyle(cs);
            c.setCellValue("Traveler No :");
            
            c = r.createCell((short) 4);
            c.setCellStyle(cs);
            c.setCellValue(ITEM);
            
             // create a row
            r = s.createRow(rowNum+2);
            c = r.createCell((short) 0);
            c.setCellStyle(cs);
            c.setCellValue("Date :");
            
            c = r.createCell((short) 1);
            c.setCellStyle(cs);
            c.setCellValue(TRN_DATE);
            
            
            /**
             * setting the column header
             */
             
             
            rowNum = (short) 5;
            r = s.createRow(rowNum);
            c = r.createCell((short) 0);
            c.setCellStyle(cs);
            c.setCellValue("NO_TRAY");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
             c = r.createCell((short) 1);
            c.setCellStyle(cs);
            c.setCellValue("NO_PART");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
            c = r.createCell((short) 2);
            c.setCellStyle(cs);
            c.setCellValue("DESCRIPTION");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
           c = r.createCell((short) 3);
            c.setCellStyle(cs);
            c.setCellValue("LOT#");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
            c = r.createCell((short) 4);
            c.setCellStyle(cs);
            c.setCellValue("EXP_DATE");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
           c = r.createCell((short) 5);
            c.setCellStyle(cs);
            c.setCellValue("PO#");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
            c = r.createCell((short) 6);
            c.setCellStyle(cs3);
            c.setCellValue("QTY");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
              c = r.createCell((short) 7);
            c.setCellStyle(cs);
            c.setCellValue("PALLET");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
     
           c = r.createCell((short) 8);
            c.setCellStyle(cs);
            c.setCellValue("TL_PLT_ID");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
     
            c = r.createCell((short) 9);
            c.setCellStyle(cs);
            c.setCellValue("TRAVELER");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
            c = r.createCell((short) 10);
            c.setCellStyle(cs);
            c.setCellValue("EXPIRY");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
            c = r.createCell((short) 11);
            c.setCellStyle(cs);
            c.setCellValue("USER");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
             c = r.createCell((short) 12);
            c.setCellStyle(cs);
            c.setCellValue(" ");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
          String strayid ="";
          int cntSlno =0;
          invQryList = recvdao.get_Qc_ReportDetails(CibaConstants.cibacompanyName,ITEM);
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
           String sno ="";
            String tray = (String)lineArr.get("trayid");
            TotCnt = TotCnt + new Integer((String)lineArr.get("qty")).intValue(); 
            if(strayid.equalsIgnoreCase(tray)){
                sno = new Integer(cntSlno).toString();  
                
               }else
               {
               cntSlno = cntSlno+1;
                  sno = new Integer(cntSlno).toString();  
               }
          r = s.createRow( ++rowNum);

         
           yymm=(String)lineArr.get("yy")+(String)lineArr.get("mm"); 
         
          int iIndex = iCnt + 1;
          
          

                    c = r.createCell((short) 0);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("trayid"));
                    
                      c = r.createCell((short) 1);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("sku"));
                  
                  
                    c = r.createCell((short) 2);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("sku_desc"));
                  
                  
                    c = r.createCell((short) 3);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("lot"));
                  
                  
                    c = r.createCell((short) 4);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("expdate"));
                  
                    c = r.createCell((short) 5);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("custpo"));
                  
                    c = r.createCell((short) 6);
                    c.setCellStyle(cs2);
                   // c.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    int qty = new Integer((String)lineArr.get("qty")).intValue(); 
                    c.setCellValue(qty);
        
                    c = r.createCell((short) 7);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("pallet"));
                  
                    c = r.createCell((short) 8);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("palletid"));
                  
                    c = r.createCell((short) 9);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("traveler"));
                  
                    c = r.createCell((short) 10);
                    c.setCellStyle(cs2);
                    c.setCellValue(yymm);
                  
                    c = r.createCell((short) 11);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("upby"));
                  
                    c = r.createCell((short) 12);
                    c.setCellStyle(cs2);
                    c.setCellValue(sno);
                  
                    
                strayid =tray;     

                 
       
          }
                  /*  r = s.createRow( ++rowNum);
                    c = r.createCell((short) 6);
                    c.setCellStyle(cs2);
                    c.setCellValue(TotCnt);*/
            // write the workbook to the output stream
            // close our file
            wb.write(fout);
            fout.close();

            //return fileName;

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=TravelerReport.xls"  );

            in = new FileInputStream(fileName);

            // Use Buffered Stream for reading/writing.
            bis = new BufferedInputStream((InputStream) in);
            bos = new BufferedOutputStream(out);

            byte[] buff = new byte[2048];
            int bytesRead;

            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
          
        }catch(Exception e)
        {
          
        }
        finally
        {
            if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();

                in.close();
                File file = new File(fileName);
                System.out.println("Is File Deleted : "+file.delete());

        }
        
    
  }
  
   public void generateExcelStatement_01(String ITEM,ServletOutputStream out, HttpServletResponse response) throws Exception
  {
  	  SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        Date currTime = new Date();
        String dateString = formatter.format(currTime);

        String fileName = dateString + ".xls";
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        FileInputStream in = null;
        ArrayList invQryList  = new ArrayList();
        RecvDetDAO recvdao=new RecvDetDAO();
        
        String path = "",yymm="";
        String TRN_DATE=DateUtils.getDate();
        int TotCnt = 0;
        
        try
        {
         System.out.println("path" + path);
        System.out.println("fileName" + fileName);
        FileOutputStream fout = new FileOutputStream(fileName);
        
        System.out.println("################ FileOutputStream ##############3" );

         

            // create a new workbook
            HSSFWorkbook wb = new HSSFWorkbook();

   
            HSSFSheet s = wb.createSheet();
            System.out.println("HSSFSheet craeted");       // create a new sheet
            // declare a row object reference
            HSSFRow r = null;

            // declare a cell object reference
            HSSFCell c = null;
            HSSFCell c1 = null;
            
            
            
             // create 3 cell styles
            HSSFCellStyle cs = wb.createCellStyle();
            cs.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
            HSSFCellStyle cs2 = wb.createCellStyle();
            cs2.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
            
            //use this for double
            HSSFCellStyle cs3 = wb.createCellStyle();
            HSSFCellStyle cs4 = wb.createCellStyle();
            cs4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            
            
            cs3.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
            //Data Format index 4 holds "#,##0.00"
            //SAF NO. HKG-2003370
        
            String doubleFormat = new HSSFDataFormat(new Workbook()).getFormat((short) 4);
            
            cs3.setDataFormat(new HSSFDataFormat(new Workbook()).getFormat(doubleFormat));
          

            // create 2 fonts objects
            HSSFFont f = wb.createFont();
            HSSFFont f2 = wb.createFont();

            //set font 1 to 12 point type
            f.setFontHeightInPoints((short) 10);
            //make it red
            f.setColor(HSSFFont.COLOR_NORMAL);
            // make it bold
            //arial is the default font
            f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

            //set font 2 to 10 point type
            f2.setFontHeightInPoints((short) 10);
            f2.setColor(HSSFFont.COLOR_NORMAL);

            //set cell stlye
            cs.setFont(f);

            // set the font
            cs2.setFont(f2);
            cs3.setFont(f2);

            // set the sheet name to HSSF Test
            wb.setSheetName(0, "Trav");
            short rowNum = 0;

            // create a row
            r = s.createRow(rowNum);
            c = r.createCell((short) 3);
            c.setCellStyle(cs);
            c.setCellValue("Traveler No :");
            
            c = r.createCell((short) 4);
            c.setCellStyle(cs);
            c.setCellValue(ITEM);
            
             // create a row
            r = s.createRow(rowNum+2);
            c = r.createCell((short) 0);
            c.setCellStyle(cs);
            c.setCellValue("Date :");
            
            c = r.createCell((short) 1);
            c.setCellStyle(cs);
            c.setCellValue(TRN_DATE);
            
            
            /**
             * setting the column header
             */
             
             
            rowNum = (short) 5;
            r = s.createRow(rowNum);
            c = r.createCell((short) 0);
            c.setCellStyle(cs);
            c.setCellValue("NO_TRAY");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
             c = r.createCell((short) 1);
            c.setCellStyle(cs);
            c.setCellValue("NO_PART");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
            c = r.createCell((short) 2);
            c.setCellStyle(cs);
            c.setCellValue("DESCRIPTION");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
           c = r.createCell((short) 3);
            c.setCellStyle(cs);
            c.setCellValue("LOT#");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
            c = r.createCell((short) 4);
            c.setCellStyle(cs);
            c.setCellValue("EXP_DATE");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
          /*  c = r.createCell((short) 5);
            c.setCellStyle(cs);
            c.setCellValue("PO#");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));*/
     
     
            c = r.createCell((short) 5);
            c.setCellStyle(cs3);
            c.setCellValue("QTY");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
              c = r.createCell((short) 6);
            c.setCellStyle(cs);
            c.setCellValue("PALLET");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
     
           c = r.createCell((short) 7);
            c.setCellStyle(cs);
            c.setCellValue("TL_PLT_ID");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
     
            c = r.createCell((short) 8);
            c.setCellStyle(cs);
            c.setCellValue("TRAVELER");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
            c = r.createCell((short) 9);
            c.setCellStyle(cs);
            c.setCellValue("EXPIRY");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
            c = r.createCell((short) 10);
            c.setCellStyle(cs);
            c.setCellValue("USER");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
             c = r.createCell((short) 11);
            c.setCellStyle(cs);
            c.setCellValue(" ");
            s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
          String strayid ="";
          int cntSlno =0;
          invQryList = recvdao.get_Qc_ReportDetails(CibaConstants.cibacompanyName,ITEM);
          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
          Map lineArr = (Map) invQryList.get(iCnt);
           String sno ="";
            String tray = (String)lineArr.get("trayid");
             TotCnt = TotCnt + new Integer((String)lineArr.get("qty")).intValue(); 
            if(strayid.equalsIgnoreCase(tray)){
                sno = new Integer(cntSlno).toString();  
                
               }else
               {
               cntSlno = cntSlno+1;
                  sno = new Integer(cntSlno).toString();  
               }
          r = s.createRow( ++rowNum);

         
           yymm=(String)lineArr.get("yy")+(String)lineArr.get("mm"); 
         
          int iIndex = iCnt + 1;
          
          

                    c = r.createCell((short) 0);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("trayid"));
                    
                      c = r.createCell((short) 1);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("sku"));
                  
                  
                    c = r.createCell((short) 2);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("sku_desc"));
                  
                  
                    c = r.createCell((short) 3);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("lot"));
                  
                  
                    c = r.createCell((short) 4);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("expdate"));
                  
               /*     c = r.createCell((short) 5);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("custpo"));*/
                  
                    c = r.createCell((short) 5);
                    c.setCellStyle(cs2);
                    int qty = new Integer((String)lineArr.get("qty")).intValue(); 
                    c.setCellValue(qty);
        
                    c = r.createCell((short) 6);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("pallet"));
                  
                    c = r.createCell((short) 7);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("palletid"));
                  
                    c = r.createCell((short) 8);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("traveler"));
                  
                    c = r.createCell((short) 9);
                    c.setCellStyle(cs2);
                    c.setCellValue(yymm);
                  
                    c = r.createCell((short) 10);
                    c.setCellStyle(cs2);
                    c.setCellValue((String)lineArr.get("upby"));
                  
                    c = r.createCell((short) 11);
                    c.setCellStyle(cs2);
                    c.setCellValue(sno);
                  
                    
                    strayid =tray;     

                 
       
          }
                   /*  r = s.createRow( ++rowNum);
                    c = r.createCell((short) 5);
                    c.setCellStyle(cs2);
                  
                    c.setCellValue(TotCnt);*/
            // write the workbook to the output stream
            // close our file
            wb.write(fout);
            fout.close();

            //return fileName;

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=TravelerReport.xls"  );

            in = new FileInputStream(fileName);

            // Use Buffered Stream for reading/writing.
            bis = new BufferedInputStream((InputStream) in);
            bos = new BufferedOutputStream(out);

            byte[] buff = new byte[2048];
            int bytesRead;

            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
          
        }catch(Exception e)
        {
          
        }
        finally
        {
            if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();

                in.close();
                File file = new File(fileName);
                System.out.println("Is File Deleted : "+file.delete());

        }
        
    
  } 
  
   
   /*Updated by Ranjana 
    * Purpose: Add the filter condition for outbound discrepancy report.
    * 
    * public void GenerateTrayExcelReport(String ITEM,String REFNO,ServletOutputStream out, HttpServletResponse response) throws Exception*/
   
	public void GenerateTrayExcelReport(String ITEM,String REFNO,String LOT,String SKU,ServletOutputStream out, HttpServletResponse response) throws Exception
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
		Date currTime = new Date();
		String dateString = formatter.format(currTime);

		String fileName = dateString + ".xls";
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		FileInputStream in = null;
		ArrayList invQryList  = new ArrayList();
		OBTravelerDetDAO obTravelDetDAO=new OBTravelerDetDAO();
        
		String path = "",yymm="";
		String TRN_DATE=DateUtils.getDate();
        
		try
		{
			System.out.println("path" + path);
			System.out.println("fileName" + fileName);
			FileOutputStream fout = new FileOutputStream(fileName);
        
			System.out.println("################ FileOutputStream ##############3" );

         

			// create a new workbook
			HSSFWorkbook wb = new HSSFWorkbook();

   
			HSSFSheet s = wb.createSheet();
			System.out.println("HSSFSheet craeted");       // create a new sheet
			// declare a row object reference
			HSSFRow r = null;

			// declare a cell object reference
			HSSFCell c = null;
            
			// create 3 cell styles
			HSSFCellStyle cs = wb.createCellStyle();
			cs.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
			HSSFCellStyle cs2 = wb.createCellStyle();
			cs2.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
			//use this for double
			HSSFCellStyle cs3 = wb.createCellStyle();
			cs3.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
			//Data Format index 4 holds "#,##0.00"
			//SAF NO. HKG-2003370
			String doubleFormat = new HSSFDataFormat(new Workbook()).getFormat((short) 4);
			cs3.setDataFormat(new HSSFDataFormat(new Workbook()).getFormat(doubleFormat));


			// create 2 fonts objects
			HSSFFont f = wb.createFont();
			HSSFFont f2 = wb.createFont();

			//set font 1 to 12 point type
			f.setFontHeightInPoints((short) 10);
			//make it red
			f.setColor(HSSFFont.COLOR_NORMAL);
			// make it bold
			//arial is the default font
			f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			//set font 2 to 10 point type
			f2.setFontHeightInPoints((short) 10);
			f2.setColor(HSSFFont.COLOR_NORMAL);

			//set cell stlye
			cs.setFont(f);

			// set the font
			cs2.setFont(f2);
			cs3.setFont(f2);

			// set the sheet name to HSSF Test
			wb.setSheetName(0, "Discrepancy Report");
			short rowNum = 0;

			
			/**
			 * setting the column header
			 */
             
             
			rowNum = (short) 0;
			r = s.createRow(rowNum);
			c = r.createCell((short) 0);
			c.setCellStyle(cs);
			c.setCellValue("SNO");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
			c = r.createCell((short) 1);
			c.setCellStyle(cs);
			c.setCellValue("TRAVELER");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
              
			c = r.createCell((short) 2);
			c.setCellStyle(cs);
			c.setCellValue("SKU");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
			c = r.createCell((short) 3);
			c.setCellStyle(cs);
			c.setCellValue("LOT");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
			c = r.createCell((short) 4);
			c.setCellStyle(cs);
			c.setCellValue("LOC");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
			c = r.createCell((short) 5);
			c.setCellStyle(cs);
			c.setCellValue("ORD QTY");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
			c = r.createCell((short) 6);
			c.setCellStyle(cs);
			c.setCellValue("PICK QTY");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
			c = r.createCell((short) 7);
			c.setCellStyle(cs);
			c.setCellValue("TRAY QTY");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
			c = r.createCell((short) 8);
			c.setCellStyle(cs);
			c.setCellValue("PICK DIFF");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
			c = r.createCell((short) 9);
			c.setCellStyle(cs);
			c.setCellValue("TRAY DIFF");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
			
			/*Added two new columns for Status and Reason of LOT.*/
			
			c = r.createCell((short) 10);
			c.setCellStyle(cs);
			c.setCellValue("BLOCK STATUS");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
			
			c = r.createCell((short) 11);
			c.setCellStyle(cs);
			c.setCellValue("BLOCK REASON");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
			invQryList = obTravelDetDAO.TrayDescrepencyReport(CibaConstants.cibacompanyName,ITEM,LOT,SKU);
			for (int iCnt =0; iCnt<invQryList.size(); iCnt++)
			{
				Map lineArr = (Map) invQryList.get(iCnt);
          
				r = s.createRow( ++rowNum);

         
				yymm=(String)lineArr.get("yy")+(String)lineArr.get("mm"); 
         
				int iIndex = iCnt + 1;
          
          

				c = r.createCell((short) 0);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("traveler"));
                    
				c = r.createCell((short) 1);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("traveler_id"));
                  
       
				c = r.createCell((short) 2);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("sku"));
                  
                  
				c = r.createCell((short) 3);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("lot"));
                  
                  
				c = r.createCell((short) 4);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("loc"));
                  
				c = r.createCell((short) 5);
				c.setCellStyle(cs2);
        int qty = new Integer((String)lineArr.get("qty")).intValue();
				c.setCellValue(qty);
                    
                  
				c = r.createCell((short) 6);
				c.setCellStyle(cs2);
        int pickqty = new Integer((String)lineArr.get("pickqty")).intValue();
				c.setCellValue(pickqty);
                  
				c = r.createCell((short) 7);
				c.setCellStyle(cs2);
        int traylableqty = new Integer((String)lineArr.get("traylableqty")).intValue();
				c.setCellValue(traylableqty);
                    
				c = r.createCell((short) 8);
				c.setCellStyle(cs2);
        int pickdiff = new Integer((String)lineArr.get("pickdiff")).intValue();
				c.setCellValue(pickdiff);
                    
				c = r.createCell((short) 9);
				c.setCellStyle(cs2);
         int traydiff = new Integer((String)lineArr.get("traydiff")).intValue();
				c.setCellValue(traydiff);
				
				/*Added two new columns for Status and Reason of LOT.*/
				
				c = r.createCell((short) 10);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("BlockStatus"));
				
				c = r.createCell((short) 11);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("BlockReason"));
                  
                 
         
			}
          
			// write the workbook to the output stream
			// close our file
			wb.write(fout);
			fout.close();

			//return fileName;

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=TrayDiscrepencyReport.xls"  );

			in = new FileInputStream(fileName);

			// Use Buffered Stream for reading/writing.
			bis = new BufferedInputStream((InputStream) in);
			bos = new BufferedOutputStream(out);

			byte[] buff = new byte[2048];
			int bytesRead;

			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) 
			{
				bos.write(buff, 0, bytesRead);
			}
          
		}
		catch(Exception e)
		{
          
		}
		finally
		{
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();

			in.close();
			File file = new File(fileName);
			System.out.println("Is File Deleted : "+file.delete());

		}
        
    
	}
  
  
  
  
  //For Stock Count
  
  //Modified By Samatha based on URS-A-40
  
  
 //For Stock Count
  
  
  public void GenerateStockCountExcelReport(String QryCond,ServletOutputStream out, HttpServletResponse response) throws Exception
	{
  String sheetNo ="0";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
		Date currTime = new Date();
		String dateString = formatter.format(currTime);

		String fileName = "StockTake"+dateString + ".xls";
		
/************Commented by Ranjana on 5/5/2015 for the stock take generation under the ticket WO0000000380031*************/
   // String fileName = "testing.xls";
	/*	BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		FileInputStream in = null;
		ArrayList QryList  = new ArrayList();*/
	  StockTakeUtil stockTakeUtil       = new StockTakeUtil();
        
	//	String path = "D:\\APPS\\MURHO\\WMS_UPLOAD\\",yymm="";
 	String path = CibaConstants.CWriteStktakeFileToFolderPath,yymm="";
  
    //	String path = "C:\\",yymm="";
		//String TRN_DATE=DateUtils.getDate();
        
		try
		{
			System.out.println("path" + path);
			System.out.println("fileName" + fileName);
		//	FileOutputStream fout = new FileOutputStream(path+fileName);
        
			System.out.println("################ FileOutputStream ##############3" );

/************Commented by Ranjana on 5/5/2015 for the stock take generation under the ticket WO0000000380031*************/
         /*

			// create a new workbook
			HSSFWorkbook wb = new HSSFWorkbook();

   
			HSSFSheet s = null;
			System.out.println("HSSFSheet created");       // create a new sheet
			// declare a row object reference
			HSSFRow r = null;

			// declare a cell object reference
			HSSFCell c = null;
            
			// create 3 cell styles
			HSSFCellStyle cs = wb.createCellStyle();
			cs.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
			HSSFCellStyle cs2 = wb.createCellStyle();
			cs2.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
			//use this for double
			HSSFCellStyle cs3 = wb.createCellStyle();
			cs3.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
			//Data Format index 4 holds "#,##0.00"
			//SAF NO. HKG-2003370
			String doubleFormat = new HSSFDataFormat(new Workbook()).getFormat((short) 4);
			cs3.setDataFormat(new HSSFDataFormat(new Workbook()).getFormat(doubleFormat));


			// create 2 fonts objects
			HSSFFont f = wb.createFont();
			HSSFFont f2 = wb.createFont();

			//set font 1 to 12 point type
			f.setFontHeightInPoints((short) 10);
			//make it red
			f.setColor(HSSFFont.COLOR_NORMAL);
			// make it bold
			//arial is the default font
			f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			//set font 2 to 10 point type
			f2.setFontHeightInPoints((short) 10);
			f2.setColor(HSSFFont.COLOR_NORMAL);

			//set cell stlye
			cs.setFont(f);

			// set the font
			cs2.setFont(f2);
			cs3.setFont(f2);


	   int rowNum = 0;

	
            
            
			/**
			 * setting the column header
			 */
             
            /* 
			rowNum = (short) 0;
			
     
  
           
     for (int Cnt =0; Cnt<10; Cnt++){
        int num =  new Integer(sheetNo).intValue()+1;
        sheetNo = new Integer(num).toString();
        s = wb.createSheet("Sheet"+sheetNo);
        rowNum = (int) 0;
      
       
        r = s.createRow(rowNum);
        c = r.createCell((short) 0);
        c.setCellStyle(cs);
        c.setCellValue("SNO");
        s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
			c = r.createCell((short) 1);
			c.setCellStyle(cs);
			c.setCellValue("LOC");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
      
       c = r.createCell((short) 2);
			c.setCellStyle(cs);
			c.setCellValue("LOT");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
      
            
			c = r.createCell((short) 3);
			c.setCellStyle(cs);
			c.setCellValue("SKU");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
			c = r.createCell((short) 4);
			c.setCellStyle(cs);
			c.setCellValue("DESCRIPTION");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
      
     
      c = r.createCell((short) 5);
			c.setCellStyle(cs);
			c.setCellValue("MTID");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
		
     
     
			c = r.createCell((short) 6);
			c.setCellStyle(cs);
			c.setCellValue("INVQTY");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
		  c = r.createCell((short) 7);
			c.setCellStyle(cs);
			c.setCellValue("STKQTY");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
			c = r.createCell((short) 8);
			c.setCellStyle(cs);
			c.setCellValue("QTYDIFF");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
			c = r.createCell((short) 9);
			c.setCellStyle(cs);
			c.setCellValue("USER");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
			c = r.createCell((short) 10);
			c.setCellStyle(cs);
			c.setCellValue("DATE");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
    			
      }
   
   

          
			// write the workbook to the output stream
			// close our file
			wb.write(fout);
			fout.close();
      */
     // fileName


     stockTakeUtil.getGeneratedExcel(path+fileName+";",QryCond);
			//return fileName;

		/*	response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=StockCountReport.xls"  );

			in = new FileInputStream(fileName);

			// Use Buffered Stream for reading/writing.
			bis = new BufferedInputStream((InputStream) in);
			bos = new BufferedOutputStream(out);

			byte[] buff = new byte[2048];
			int bytesRead;

			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) 
			{
				bos.write(buff, 0, bytesRead);
			}*/
          
		}
		catch(Exception e)
		{
          System.out.println(e.toString());
		}
	}
  
  
   public void GenerateInvExcelReport(String ITEM,String BATCH,String LOC,ServletOutputStream out, HttpServletResponse response) throws Exception
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
		Date currTime = new Date();
		String dateString = formatter.format(currTime);

		String fileName = dateString + ".xls";
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		FileInputStream in = null;
		ArrayList QryList  = new ArrayList();
	 ReportUtil repUtil       = new ReportUtil();
        
		String path = "",yymm="";
		String TRN_DATE=DateUtils.getDate();
        
		try
		{
			System.out.println("path" + path);
			System.out.println("fileName" + fileName);
			FileOutputStream fout = new FileOutputStream(fileName);
        
			System.out.println("################ FileOutputStream ##############3" );

         

			// create a new workbook
			HSSFWorkbook wb = new HSSFWorkbook();

   
			HSSFSheet s = wb.createSheet();
			System.out.println("HSSFSheet craeted");       // create a new sheet
			// declare a row object reference
			HSSFRow r = null;

			// declare a cell object reference
			HSSFCell c = null;
            
			// create 3 cell styles
			HSSFCellStyle cs = wb.createCellStyle();
			cs.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
			HSSFCellStyle cs2 = wb.createCellStyle();
			cs2.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
			//use this for double
			HSSFCellStyle cs3 = wb.createCellStyle();
			cs3.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
			//Data Format index 4 holds "#,##0.00"
			//SAF NO. HKG-2003370
			String doubleFormat = new HSSFDataFormat(new Workbook()).getFormat((short) 4);
			cs3.setDataFormat(new HSSFDataFormat(new Workbook()).getFormat(doubleFormat));


			// create 2 fonts objects
			HSSFFont f = wb.createFont();
			HSSFFont f2 = wb.createFont();

			//set font 1 to 12 point type
			f.setFontHeightInPoints((short) 10);
			//make it red
			f.setColor(HSSFFont.COLOR_NORMAL);
			// make it bold
			//arial is the default font
			f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			//set font 2 to 10 point type
			f2.setFontHeightInPoints((short) 10);
			f2.setColor(HSSFFont.COLOR_NORMAL);

			//set cell stlye
			cs.setFont(f);

			// set the font
			cs2.setFont(f2);
			cs3.setFont(f2);

			// set the sheet name to HSSF Test
			wb.setSheetName(0, "Sheet1");
			short rowNum = 0;

	
            
            
			/**
			 * setting the column header
			 */
             
             
			rowNum = (short) 0;
			r = s.createRow(rowNum);
			c = r.createCell((short) 0);
			c.setCellStyle(cs);
			c.setCellValue("SNO");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
          
          
      
			c = r.createCell((short) 1);
			c.setCellStyle(cs);
			c.setCellValue("LOC");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
      
			c = r.createCell((short) 2);
			c.setCellStyle(cs);
			c.setCellValue("LOT");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
			c = r.createCell((short) 3);
			c.setCellStyle(cs);
			c.setCellValue("SKU");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
		   
     
     	c = r.createCell((short) 4);
			c.setCellStyle(cs);
			c.setCellValue("DESCRIPTION");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
  
     
			c = r.createCell((short) 5);
			c.setCellStyle(cs);
			c.setCellValue("QTY");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
      
      
			c = r.createCell((short) 6);
			c.setCellStyle(cs);
			c.setCellValue("UOM");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
   
     
			QryList = repUtil.getInvListReport(CibaConstants.cibacompanyName, ITEM,BATCH, LOC);
      System.out.println("QryList.size () "+QryList.size());
			for (int iCnt =0; iCnt<QryList.size(); iCnt++)
			{
				Map lineArr = (Map) QryList.get(iCnt);
   /*      if (rowNum >= 65536) { 
            System.out.println("Entered..............Into ");
            s = wb.createSheet("Sheet" + "_" 
                        + wb.getSheetIndex("Sheet2") + 1);
          
       
            rowNum = (short) 0;
            } */

          
				r = s.createRow( ++rowNum);
        

     
				int iIndex = iCnt + 1;
       
       int qty = new Integer((String)lineArr.get("QTY")).intValue();
               

				c = r.createCell((short) 0);
				c.setCellStyle(cs2);
				c.setCellValue(iIndex);
        
        c = r.createCell((short) 1);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("LOC"));
                  
                    
				c = r.createCell((short) 2);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("LOT"));
                  
                  
				c = r.createCell((short) 3);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("SKU"));
                  
                  
				
                  
				c = r.createCell((short) 4);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("SKU_DESC"));
                  
				c = r.createCell((short) 5);
				c.setCellStyle(cs2);
				c.setCellValue(qty);
                     
				c = r.createCell((short) 6);
				c.setCellStyle(cs2);
        c.setCellValue((String)lineArr.get("UOM"));
       
                 
         
			}
          
			// write the workbook to the output stream
			// close our file
			wb.write(fout);
			fout.close();

			//return fileName;

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=InvemtoryReport.xls"  );

			in = new FileInputStream(fileName);

			// Use Buffered Stream for reading/writing.
			bis = new BufferedInputStream((InputStream) in);
			bos = new BufferedOutputStream(out);

			byte[] buff = new byte[2048];
			int bytesRead;

			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) 
			{
				bos.write(buff, 0, bytesRead);
			}
          
		}
		catch(Exception e)
		{
          
		}
		finally
		{
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();

			in.close();
			File file = new File(fileName);
			System.out.println("Is File Deleted : "+file.delete());

		}      
	}


   /* Method added by Ranjana
    * Purpose: To generate the Inbound Discrepancy Report. */
   
public void GenerateInboundExcelReport(String item,String lot,String sku,ServletOutputStream out, HttpServletResponse response) 
throws IOException,Exception{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
		Date currTime = new Date();
		String dateString = formatter.format(currTime);
		
		String fileName = dateString + ".xls";
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		FileInputStream in = null;
		ArrayList invQryList  = new ArrayList();
		RecvDetDAO recvDetDAO=new RecvDetDAO();
		Hashtable ht = new Hashtable();
		ht.put("ITEM",item);
		ht.put("LOT",lot);
		ht.put("SKU",sku);
			        
		String path = "",yymm="";
		
		String TRN_DATE=DateUtils.getDate();
		        
		try
		{
					
			System.out.println("path" + path);
			System.out.println("fileName" + fileName);
			FileOutputStream fout = new FileOutputStream(fileName);
        
			System.out.println("################ FileOutputStream ##############3" );

         

			// create a new workbook
			HSSFWorkbook wb = new HSSFWorkbook();

   
			HSSFSheet s = wb.createSheet();
			System.out.println("HSSFSheet craeted");       // create a new sheet
			// declare a row object reference
			HSSFRow r = null;

			// declare a cell object reference
			HSSFCell c = null;
            
			// create 3 cell styles
			HSSFCellStyle cs = wb.createCellStyle();
			cs.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
			HSSFCellStyle cs2 = wb.createCellStyle();
			cs2.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
			//use this for double
			HSSFCellStyle cs3 = wb.createCellStyle();
			cs3.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
			//Data Format index 4 holds "#,##0.00"
			//SAF NO. HKG-2003370
			String doubleFormat = new HSSFDataFormat(new Workbook()).getFormat((short) 4);
			cs3.setDataFormat(new HSSFDataFormat(new Workbook()).getFormat(doubleFormat));


			// create 2 fonts objects
			HSSFFont f = wb.createFont();
			HSSFFont f2 = wb.createFont();

			//set font 1 to 12 point type
			f.setFontHeightInPoints((short) 10);
			//make it red
			f.setColor(HSSFFont.COLOR_NORMAL);
			// make it bold
			//arial is the default font
			f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			//set font 2 to 10 point type
			f2.setFontHeightInPoints((short) 10);
			f2.setColor(HSSFFont.COLOR_NORMAL);

			//set cell stlye
			cs.setFont(f);

			// set the font
			cs2.setFont(f2);
			cs3.setFont(f2);

			// set the sheet name to HSSF Test
			wb.setSheetName(0, "InboundDiscrepancy Report");
			short rowNum = 0;
			/**
			 * setting the column header
			 */
             
             
			rowNum = (short) 0;
			r = s.createRow(rowNum);
			c = r.createCell((short) 0);
			c.setCellStyle(cs);
			c.setCellValue("SNO");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
			c = r.createCell((short) 1);
			c.setCellStyle(cs);
			c.setCellValue("TRAVELER");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
      
            
			c = r.createCell((short) 2);
			c.setCellStyle(cs);
			c.setCellValue("SKU");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
			c = r.createCell((short) 3);
			c.setCellStyle(cs);
			c.setCellValue("LOT");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
			c = r.createCell((short) 4);
			c.setCellStyle(cs);
			c.setCellValue("LOC");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
			c = r.createCell((short) 5);
			c.setCellStyle(cs);
			c.setCellValue("ORDER QTY");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
			c = r.createCell((short) 6);
			c.setCellStyle(cs);
			c.setCellValue("RECV QTY");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
			c = r.createCell((short) 7);
			c.setCellStyle(cs);
			c.setCellValue("PUTAWAY QTY");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
            
			c = r.createCell((short) 8);
			c.setCellStyle(cs);
			c.setCellValue("RECV DIFF");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
			c = r.createCell((short) 9);
			c.setCellStyle(cs);
			c.setCellValue("PUTAWAY DIFF");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
			
			c = r.createCell((short) 10);
			c.setCellStyle(cs);
			c.setCellValue("BLOCK STATUS");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
			
			c = r.createCell((short) 11);
			c.setCellStyle(cs);
			c.setCellValue("BLOCK REASON");
			s.setColumnWidth((short) 3, (short) ((25 * 8) / ((double) 1 / 20)));
     
     
			invQryList = recvDetDAO.TrayDescrepencyReport(ht);
			
			MLogger.info("************* generateexcel " +  invQryList.size());
			for (int iCnt =0; iCnt<invQryList.size(); iCnt++)
			{
				Map lineArr = (Map) invQryList.get(iCnt);
          
				r = s.createRow( ++rowNum);
				yymm=(String)lineArr.get("yy")+(String)lineArr.get("mm"); 
         
				int iIndex = iCnt + 1;
				
				c = r.createCell((short) 0);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("traveler"));
                    
				c = r.createCell((short) 1);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("traveler"));
        
				c = r.createCell((short) 2);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("sku"));                 
                  
				c = r.createCell((short) 3);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("lot"));
                  
                  
				c = r.createCell((short) 4);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("LOC"));
                  
				c = r.createCell((short) 5);
				c.setCellStyle(cs2);
        int qty = new Integer((String)lineArr.get("orderqty")).intValue();
				c.setCellValue(qty);
                    
                  
				c = r.createCell((short) 6);
				c.setCellStyle(cs2);
        int pickqty = new Integer((String)lineArr.get("receiveqty")).intValue();
				c.setCellValue(pickqty);
                  
				c = r.createCell((short) 7);
				c.setCellStyle(cs2);
        int traylableqty = new Integer((String)lineArr.get("putawayqty")).intValue();
				c.setCellValue(traylableqty);
                    
				c = r.createCell((short) 8);
				c.setCellStyle(cs2);
        int pickdiff = new Integer((String)lineArr.get("recevdiff")).intValue();
				c.setCellValue(pickdiff);
                    
				c = r.createCell((short) 9);
				c.setCellStyle(cs2);
         int traydiff = new Integer((String)lineArr.get("putawaydiff")).intValue();
				c.setCellValue(traydiff);
				
				c = r.createCell((short) 10);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("BlockStatus"));
				
				c = r.createCell((short) 11);
				c.setCellStyle(cs2);
				c.setCellValue((String)lineArr.get("BlockReason"));               
         
			}
          
			// write the workbook to the output stream
			// close our file
			wb.write(fout);
			fout.close();

			//return fileName;

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=InboundDiscrepencyReport.xls"  );

			in = new FileInputStream(fileName);

			// Use Buffered Stream for reading/writing.
			bis = new BufferedInputStream((InputStream) in);
			bos = new BufferedOutputStream(out);

			byte[] buff = new byte[2048];
			int bytesRead;

			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) 
			{
				bos.write(buff, 0, bytesRead);
			}         
		}
		catch(Exception e)
		{
          
		}
		finally
		{
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();

			in.close();
			File file = new File(fileName);
			System.out.println("Is File Deleted : "+file.delete());

		}      
}
}