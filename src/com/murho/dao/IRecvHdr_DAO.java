package com.murho.dao;

import java.util.ArrayList;

import com.murho.DO.RecvHdr_DO;

 interface  IRecvHdr_DAO 
{
  public static final String TABLE_NAME="RECVHDR";
   
  public int insertRecvHdr(RecvHdr_DO  _RecvHdr) throws Exception;
  
  public boolean updateRecvHdr(RecvHdr_DO  _RecvHdr);
  
  public boolean deleteRecvHdr(RecvHdr_DO  _RecvHdr);
  
  public boolean findByRecvHdrId(String traveler) throws Exception; 
  
  public ArrayList  getFileView(RecvHdr_DO  _RecvHdr) throws Exception;
  

  
 // public RecvHdr_DO findRecvDet() throws Exception;
}