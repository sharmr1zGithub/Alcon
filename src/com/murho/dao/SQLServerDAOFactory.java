package com.murho.dao;

public class SQLServerDAOFactory  extends DAOFactory
{
  public SQLServerDAOFactory()
  {
  }
  
   public SQLRecvHdr_DAO getRecvHdrDAO(){
      return new SQLRecvHdr_DAO();
    }
    
    public SQLRecvDet_DAO getRecvDetDAO(){
        return new SQLRecvDet_DAO();
    }
    
     public SQLTempDet_DAO geTempDetDAO(){
        return new SQLTempDet_DAO();
    }
    public SQLTravelDet_DAO geTravelDetDAO(){
        return new SQLTravelDet_DAO();
    }
}