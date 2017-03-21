package com.murho.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;

// class BaseDAO is a super class that will be extended by other classes
    public class BaseDAO {
        StrUtils strUtils = null;
        public BaseDAO() {
        StrUtils strUtils = new StrUtils();
    } 
  
    public ArrayList selectData(Connection conn, String query) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        Map map = null;
        ArrayList arrayList = new ArrayList();
          try {
              stmt = conn.createStatement();
              rs = stmt.executeQuery(query);
                while (rs.next()) {
                  map = new HashMap();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
                    }
                arrayList.add(map);
              }
          }
          catch (Exception e) {
              throw e;
          }
          finally {
              if (rs != null) {
                 rs.close();
              }
              if (stmt != null) {
                  stmt.close();
              }
          }
          return arrayList;
    }
    
     public long CountofData(Connection conn, String query) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
       long count =0; 
      
          try {
              stmt = conn.createStatement();
              rs = stmt.executeQuery(query);
                while (rs.next()) {
                count= rs.getInt(1);
              }
          }
          catch (Exception e) {
              throw e;
          }
          finally {
              if (rs != null) {
                 rs.close();
              }
              if (stmt != null) {
                  stmt.close();
              }
          }
          return count;
    }
  
    public boolean insertData(Connection conn, String query) throws Exception {
        boolean flag = false;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
          try {
              pStmt = conn.prepareStatement(query);
              int iCnt = pStmt.executeUpdate();
              if (iCnt > 0) {
                flag = true;
              }
          }
          catch (Exception e) {
              throw e;
          }
          finally {
              try {
                  if (rs != null) {
                    rs.close();
                  }
                  if (pStmt != null) {
                    pStmt.close();
                  }
              }
              catch (Exception e) {
                throw e;
              }
          }
    
        return flag;
    }

    public boolean DeleteRow(Connection conn, String query) throws Exception {
        boolean flag = false;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            pStmt = conn.prepareStatement(query);
            int iCnt = pStmt.executeUpdate();
            if (iCnt > 0) {
              flag = true;
            }
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            try {
                if (rs != null) {
                  rs.close();
                }
                if (pStmt != null) {
                  pStmt.close();
                }
            }
            catch (Exception e) {
              throw e;
            }
        }
  
      return flag;
    }

    public boolean updateData(Connection conn, String sql) throws Exception {
      boolean flag = false;
      Statement stmt = null;
      int updateCount = 0;
      try {
        stmt = conn.createStatement();
        updateCount = stmt.executeUpdate(sql);
          if (updateCount <= 0) {
              flag = false;
              throw new Exception("Unable to update!");
          }
          else {
             flag = true;
          }
      }
      catch (Exception e) {
          throw e;
      }
      finally {
          try {
              if (stmt != null) {
                stmt.close();
              }
          }
          catch (Exception e) {
             throw e;
          }
      }
      return flag;
    }


    public boolean updateDataWithCond(Connection conn, String sql) throws Exception {
      boolean flag = false;
      Statement stmt = null;
      int updateCount = 0;
        try {
            stmt = conn.createStatement();
            updateCount = stmt.executeUpdate(sql);
         }
        catch (Exception e) {
            throw e;
        }
        finally {
            try {
                if (stmt != null) {
                  stmt.close();
                }
            }
            catch (Exception e) {
               throw e;
            }
        }
      return flag;
    }

  public Map getRowOfData(Connection conn, String query) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    Map map = null;
    map = new HashMap();

      try {
          stmt = conn.createStatement();
          rs = stmt.executeQuery(query);
            while (rs.next()) {
              map = new HashMap();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                  //  System.out.println("getrow.........."+rs.getMetaData().getColumnLabel(i)+","+rs.getString(i));
                    map.put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
                }
            }MLogger.log(1,  " size of the map" + map.size() );
      }
      catch (Exception e) {
           throw e;
      }
      finally {
          if (rs != null) {
            rs.close();
          }
          if (stmt != null) {
            stmt.close();
          }
      }
    return map;
  }

   public String formCondition(Hashtable ht) {
    String sCondition = "";
    Enumeration enum1 = ht.keys();
    for (int i = 0; i < ht.size(); i++) {
      String key = strUtils.fString( (String) enum1.nextElement());
      String value = strUtils.fString( (String) ht.get(key));
      sCondition += key.toUpperCase() + " = '" + value.toUpperCase() + "' AND ";
    }
    sCondition = (sCondition.length() > 0) ?
        sCondition.substring(0, sCondition.length() - 4) : "";
    return sCondition;
  }
  
    public String formConditionLike(Hashtable ht) {
    String sCondition = "";
    Enumeration enum1 = ht.keys();
    for (int i = 0; i < ht.size(); i++) {
      String key = strUtils.fString( (String) enum1.nextElement());
      String value = strUtils.fString( (String) ht.get(key));
      sCondition += key.toUpperCase() + " LIKE '" + value.toUpperCase() + "' AND ";
    }
    sCondition = (sCondition.length() > 0) ?
        sCondition.substring(0, sCondition.length() - 4) : "";
    return sCondition;
  }
  
  public boolean isExists(Connection conn, String sql) throws Exception {
    PreparedStatement pStmt = null;
    ResultSet rs = null;
    boolean exists = false;
    try {
      pStmt = conn.prepareStatement(sql);
      rs = pStmt.executeQuery();
      while (rs.next()) {
          if (rs.getInt(1) > 0) {
          exists = true;
        }
      }
    }
    catch (Exception e) {
      throw e;
    }
    finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (pStmt != null) {
          pStmt.close();
        }
      }
      catch (Exception e) {
        throw e;
      }
    }
    return exists;
  }
} //end of clas
