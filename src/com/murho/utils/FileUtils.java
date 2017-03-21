package com.murho.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils 
{
  public FileUtils()
  {
  }

  /**
   * 
   * @param args
   */
  public static void main(String[] args)
  {
    FileUtils fileUtils = new FileUtils();
  }
  
  public static boolean createFile(String fileName) throws IOException
  {
      boolean success =false;
    try {
        File file = new File(fileName);
    
        // Create file if it does not exist
        success = file.createNewFile();
        if (success) {
            // File did not exist and was created
        } else {
            success=false;
        }
    } catch (IOException e) {
      throw e;
    }
    return success;
  }
  
   public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
    
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
    
    public static String readFromFile(String fileName)
    {
        System.out.println("Reading from file : "+  fileName);
        String str="";
        String str1="";
        try {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
       
        while ((str = in.readLine()) != null) {
        //   System.out.println(str);
           str1=str1+ "\n"+ str;
        }
        in.close();
    } catch (IOException e) {
    System.out.println("Exception : readFromFile() : " + e.getMessage() );
    }
    return str1;
    }
  



}