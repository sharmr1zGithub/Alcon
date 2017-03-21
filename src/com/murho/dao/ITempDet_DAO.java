package com.murho.dao;

import com.murho.DO.TempDet_DO;

interface ITempDet_DAO 
{
   public static final String TABLE_NAME="TEMPDATATABLE";
	
   public int insertTempDet(TempDet_DO  _TempDet) throws Exception;
  
   public boolean updateTempDet(TempDet_DO  _TempDet);
  
   public boolean deleteTempDetById(TempDet_DO  _TempDet);
  
  
   public boolean deleteTempDet() throws Exception; 
  
   public boolean findByTempDetById(int sno) throws Exception; 
   
   public String findProductClassByItem(String  item) throws Exception; 
  
   public String findLotTypeByProductClass(String  productclass,String prefix) throws Exception; 
}