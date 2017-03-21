package com.murho.DataDownloader;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


public class POIReadExcel {
    
    /** Creates a new instance of ReadExcel */
    
    private int _sheetNo=0;
    private String _fileName="";
    private String _sheetName="";
    
   
    public POIReadExcel(String file) {
    }
    
     public POIReadExcel(String file,String sheetName) {
     this._fileName=file;
     this._sheetName=sheetName;
    }
    
     public POIReadExcel(String file,int sheetNo) {
     this._fileName=file;
     this._sheetNo=sheetNo;
    }
     
    
    public double getQty() {
        
      double qty=0.0;
      try 
      {
      
        InputStream inp = new FileInputStream(_fileName);
        HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inp));
        HSSFSheet sheet; 
        if(_sheetName=="")
        {
          System.out.println("sheetNo : " + _sheetNo);
          sheet = wb.getSheetAt(_sheetNo);
        }
        else
        {
          System.out.println("SheetName : " + _sheetName);
          sheet = wb.getSheet(_sheetName);
        }
   //    System.out.println("total record : " + sheet.getPhysicalNumberOfRows());   
        int dataStartRow=65536;
        for (int i=0; i <= sheet.getPhysicalNumberOfRows(); i++) {
        
        HSSFRow row = sheet.getRow(i);
        HSSFCell cell = row.getCell((short)0);
        try{
          String data = cell.toString();
          if(data=="1.0" || data.equalsIgnoreCase("1.0"))
          {
            dataStartRow=i;
          }
          if(data.equalsIgnoreCase("") && i > dataStartRow )
          {
           System.out.println("Data picking end at row no : " + i);
            cell = row.getCell((short)6);
            qty = cell.getNumericCellValue();
            System.out.println("Data " + i + " :" + qty);
            break;
           }
              System.out.println("Data " + i + " :" + data);
         }catch (NullPointerException ex)
          {
                   // System.out.println("NullPointerException");
                    continue; 
          }
     }
   }
   catch (Exception ex) {
         ex.printStackTrace();
  }
  return qty;
}
  
    public static void main(String[] args)
    {
   //   POIReadExcel ex =new POIReadExcel("c:\\CVAML1997,2000-2002,2005-2009.xls","CV-AML2001");
      POIReadExcel ex =new POIReadExcel("D:\\MURHO\\WMS_UPLOAD\\CVAML 2593,2594,2596-2598.xls","CV-AML2598");
      System.out.println("Qty : " + ex.getQty());
    }
    
}

