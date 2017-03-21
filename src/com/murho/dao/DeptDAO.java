package com.murho.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.murho.gates.DbBean;
import com.murho.utils.StrUtils;


public class DeptDAO  extends BaseDAO{
  StrUtils strUtils = new StrUtils();
  private String tblName = "PLNTMST";
  Connection con = null;

  public DeptDAO() {
  }

  public boolean insertIntoDepartment(Hashtable ht) throws Exception
   {
        boolean insertedDept = false;
        PreparedStatement ps     = null;
        try{
            con = DbBean.getConnection();
            String FIELDS = "", VALUES = "";
            Enumeration enum1 = ht.keys();
            for (int i = 0; i < ht.size(); i++) {
                String key   = strUtils.fString((String) enum1.nextElement());
                String value =  strUtils.fString((String)ht.get(key));
                FIELDS += key.toUpperCase() + ",";
                VALUES += "'" + value + "',";
            }
            String sQry = "INSERT INTO "+tblName+" ("+FIELDS.substring(0,FIELDS.length()-1)+") VALUES ("+VALUES.substring(0,VALUES.length() -1) +")";
            ps = con.prepareStatement(sQry);
            int iCnt = ps.executeUpdate();
            if(iCnt > 0) insertedDept = true;

        }catch(Exception e) { }
        finally
        {
          DbBean.closeConnection(con, ps);
        }
        return insertedDept;
   }
   /**
    * method  : deleteDepartment(String aDeptno)
    * description : Delete the existing record  from DeptNO
    * @param : String aDeptNo
    * @return : boolean - true / false
    * @throws Exception
    */
    public boolean deleteDepartment(String aDeptno) throws Exception
    {
      boolean deleteDept = false;
      PreparedStatement ps     = null;
      try{
          con = DbBean.getConnection();
          String sQry = "DELETE FROM "+tblName+" WHERE PLANT ='"+aDeptno.toUpperCase()+"'";
          ps = con.prepareStatement(sQry);
          int iCnt = ps.executeUpdate();
          if(iCnt > 0) deleteDept = true;
      }catch(Exception e) { }
      finally
      {
        DbBean.closeConnection(con, ps);
      }

      return deleteDept;
    }
    /**
     * @method : isExistsDeptartment(String aDept)
     * @param aDept
     * @return boolean
     * @throws Exception
     */
    public boolean isExistsDeptartment(String aDept) throws Exception {
       PreparedStatement ps     = null;
       ResultSet         rs = null;
       boolean isExists     = false;
       try{
          con = DbBean.getConnection();
          String sQry = "SELECT COUNT(*) FROM "+ tblName+" WHERE PLANT = '"+ aDept.toUpperCase()+"'";
          ps = con.prepareStatement(sQry);
          rs   =  ps.executeQuery();
          while(rs.next()){
               if( rs.getInt(1) > 0 ) isExists = true;
          }
        }catch(Exception e) { }
       finally
       {
          DbBean.closeConnection(con, ps);
       }
    return isExists;
   }


   /**
    * @method : getDeptList()
    * @description : get the Department list
    * @return ArrayList
    * @throws Exception
    */
   public ArrayList getDeptList() throws Exception {
       PreparedStatement ps = null;
       ResultSet rs         = null;
       ArrayList arrList    = new ArrayList();
       try{
          con = DbBean.getConnection();
          String sQry = "SELECT PLANT,PLNTDESC FROM PLNTMST";
          //System.out.println(sQry);
          ps = con.prepareStatement(sQry);
          rs   =  ps.executeQuery();
          while(rs.next()){
               ArrayList arrLine    = new ArrayList();
               arrLine.add(0,strUtils.fString((String)rs.getString(1))); // plant
               arrLine.add(1,strUtils.fString((String)rs.getString(2))); //plant desc
               arrList.add(arrLine);
          }
        }catch(Exception e) { }
       finally
       {
          DbBean.closeConnection(con, ps);
       }
      return arrList;
   }
   
   /**
    * @method : getDeptList4(String adeptNo)
    * @return ArrayList
    * @throws Exception
    */
    public ArrayList getDeptDetails(String adeptNo) throws Exception {
      PreparedStatement ps = null;
      ResultSet rs         = null;
      ArrayList arrCust    = new ArrayList();
      try{
         con = DbBean.getConnection();
         String sQry = "SELECT PLANT,PLNTDESC FROM PLNTMST WHERE PLANT = '"+adeptNo+"'";
         ps = con.prepareStatement(sQry);
         rs   =  ps.executeQuery();
         while(rs.next()){
           arrCust.add(0,strUtils.fString((String)rs.getString(1))); // Dept
           arrCust.add(1,strUtils.fString((String)rs.getString(2))); //Deptdesc

        }
     }catch(Exception e) { }
        finally
        {
           DbBean.closeConnection(con, ps);
        }
        return arrCust;
     }

     public boolean updateDepartment(Hashtable htUpdate,Hashtable htCondition) throws Exception
     {
       boolean update      = false;
       PreparedStatement ps = null;

       try{
         con = DbBean.getConnection();
         String sUpdate = " ", sCondition = " ";


         // generate the condition string
         Enumeration enumUpdate = htUpdate.keys();
         for (int i = 0; i < htUpdate.size(); i++) {
              String key   = strUtils.fString((String) enumUpdate.nextElement());
              String value =  strUtils.fString((String)htUpdate.get(key));
                  sUpdate += key.toUpperCase() +" = '" + value + "',";
         }

         // generate the update string
         Enumeration enumCondition = htCondition.keys();
         for (int i = 0; i < htCondition.size(); i++) {
             String key   = strUtils.fString((String) enumCondition.nextElement());
             String value =  strUtils.fString((String)htCondition.get(key));
                 sCondition += key.toUpperCase() +" = '" + value.toUpperCase() + "' AND ";

         }
         sUpdate = (sUpdate.length() > 0) ? " SET " + sUpdate.substring(0,sUpdate.length() - 1): "";
         sCondition = (sCondition.length() > 0) ? " WHERE  "+sCondition.substring(0,sCondition.length() - 4): "";

         String stmt = "UPDATE "+tblName + sUpdate +sCondition;
        // System.out.println(stmt);
         ps = con.prepareStatement(stmt);
         int iCnt = ps.executeUpdate();
         if(iCnt > 0) update = true;

       }catch(Exception e) { }
       finally
       {
         DbBean.closeConnection(con, ps);
       }

       return update;
     }

     public ArrayList getDeptStartsWith(String adeptNo) throws Exception{
         PreparedStatement ps = null;
         ResultSet rs         = null;
         ArrayList arrList    = new ArrayList();
         try{
            con = DbBean.getConnection();
            String sQry = "SELECT PLANT,PLNTDESC FROM "+ tblName+" WHERE PLANT LIKE '"+adeptNo+"%'";
            ps = con.prepareStatement(sQry);
            rs   =  ps.executeQuery();
            while(rs.next()){
                 ArrayList arrLine    = new ArrayList();
                 arrLine.add(0,strUtils.fString((String)rs.getString(1))); // deptno
                 arrLine.add(1,strUtils.fString((String)rs.getString(2))); //desc
                 arrList.add(arrLine);
            }
          }catch(Exception e) { }
         finally
         {
            DbBean.closeConnection(con, ps);
         }
        return arrList;

       }

}