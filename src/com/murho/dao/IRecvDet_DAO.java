package com.murho.dao;

import java.util.ArrayList;

import com.murho.DO.RecvDet_DO;

 interface IRecvDet_DAO 
{
  public static final String TABLE_NAME="RECVDET";
   
  public int insertRecvDet(RecvDet_DO  _RecvDet) throws Exception;
  
  public boolean updateRecvDet(RecvDet_DO  _RecvDet);
  
  public boolean deleteRecvDet(RecvDet_DO  _RecvDet);
  
  public boolean findByRecvDetId(String traveler,String mtid) throws Exception;
  
 public ArrayList getRecvDetById(RecvDet_DO  _RecvDet) throws Exception;
  
 public ArrayList getRecvDet() throws Exception;
   
 public ArrayList getRecvTravelerDetById(RecvDet_DO  _RecvDet) throws Exception;
   
 public ArrayList getRecvTravelerDet() throws Exception;
  
 public int CalcAllocation(String traveler) throws Exception;
  
 public int deCalcAllocation(RecvDet_DO  _RecvDet) throws Exception;

  
 public ArrayList getCalcTraveler() throws Exception;
  
 public boolean getRecvDetCount(String travelId) throws Exception;
  
 public boolean findRecvStatus(String traveler) throws Exception;
  
 public boolean checkPUV(String sku)  throws Exception;
  
  
//  public RecvDet_DO findRecvDet() throws Exception;
}