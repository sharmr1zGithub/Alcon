package com.murho.DataDownloader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.murho.utils.StrUtils;

public class ExcelDownloader 
{
  public ExcelDownloader()
  {
  }

  /**
   * 
   * @param args
   */
   
  public static String[] readExcelFile(String filename,String sheetName,int NumberOfColumn)  throws Exception 
  {
    Connection c = null;
    Statement stmnt = null;
    ResultSet rs=null;
    int _record=0;
    ArrayList _alRecord=new ArrayList();
    StrUtils _strUtils = new StrUtils();
    String[] array=null;
    
    System.out.println("File Name      : " + filename);
    System.out.println("Sheet Name     : " + sheetName);
    System.out.println("NumberOfColumn : " + NumberOfColumn);
    try {
       Class.forName( "sun.jdbc.odbc.JdbcOdbcDriver" );
       c = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Excel Driver (*.xls)};DBQ=" + filename);
       // c = DriverManager.getConnection("jdbc:odbc:trav","","");
       stmnt = c.createStatement();
       String query = "Select * from [" + sheetName+ "$]" ;
       //  String query = "Select * from [1338TRAV$]" ;
       rs = stmnt.executeQuery( query );
       //    System.out.println("Stage 1");
       while( rs.next() ) {
          _record++;
      //    System.out.println("Record : " + _record );
          StringBuffer data=new StringBuffer("");
          for(int i=1; i<=NumberOfColumn;i++){
         // String traveler=rs.getString(i);
          //  System.out.println("Stage 3");
          data.append(StrUtils.fString(rs.getString(i)));
          data.append(",");
        }//end for for
        data.replace(data.length()-1,data.length(),"");
        //    System.out.println("data : " +  data) ;
        _alRecord.add(_strUtils.InsertQuotes(data.toString()));
        }
            //    StringArr = new String[_alRecord.size()];
            
            array = (String[])_alRecord.toArray(new String[_alRecord.size()]);
            //   Arrays.fill(StringArr, _alRecord.toString());
            System.out.println("_record"+_record);
            
        } catch( Exception e ) {
            System.err.println( e );
            
            System.out.println("Exception " + e.getMessage());
        } finally{
            try {
                System.out.println("Closing connection");
                rs.close();
                stmnt.close();
                c.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
         }
  }
  return array;
 }
 
 
   
   
    
   
  public static void main(String[] args)
  {
    ExcelDownloader excelDownloader = new ExcelDownloader();
  }
}