package com.murho.dao;

import java.util.ArrayList;

import com.murho.DO.TravelDet_DO;

interface ITravelDet_DAO 
{
  public ArrayList  getWriteFileDetById(TravelDet_DO  _TravelDet_DO) throws Exception;
  public boolean IsExistsOBDeliveryDetById(TravelDet_DO  _TravelDet_DO) throws Exception;
 
}