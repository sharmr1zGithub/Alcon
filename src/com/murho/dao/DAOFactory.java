package com.murho.dao;

public abstract class DAOFactory 
{
   public static final int SQLSERVER=1;
  public DAOFactory()
  {
  }
  
   public static DAOFactory getDAOFactory(int whichFactory){
        
        switch(whichFactory){
            case  SQLSERVER :
            return new SQLServerDAOFactory();
            default : return null;
        }
    }
}