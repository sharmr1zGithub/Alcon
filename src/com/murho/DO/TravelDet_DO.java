package com.murho.DO;

public class TravelDet_DO 
{
  public TravelDet_DO()
  {
  }
  
  private String refno="";
  private int sino;
  private int sinoln;
  private String traveler="";
  private String travelerid="";
  private String pallet;
  private String palletid;
  private String palletgrp;
  private String palletdesc;
  private String trayid;
  private String traygroup;
  private String prdclass;
  private String sonum;
  private int soline;
  private int release;
  private String custpo;
  private String sku="";
  private String skudesc="";
  private String uom="";
  private String loc="";
  private String mtid="";
  private int qty;
  private String lot="";
  private String lottype="";
  private int fulltray;
  private int partialqty;
  private int pickqty;
  private int traylabelqty;
  private String assigneduser;
  private String status;
  private String pickerlistgenerationstatus;
  private String palletstatus;
  private String traystatus;
  private String expdate;
  private String crtime;
  private String crat;
  private String crby;
  private String upat;
  private String upby;
  private String dummytrayid;
  
  
  
  public String getAssigneduser() {
		return assigneduser;
	}
	public void setAssigneduser(String assigneduser) {
		this.assigneduser = assigneduser;
	}
	public String getCrat() {
		return crat;
	}
	public void setCrat(String crat) {
		this.crat = crat;
	}
	public String getCrby() {
		return crby;
	}
	public void setCrby(String crby) {
		this.crby = crby;
	}
	public String getCrtime() {
		return crtime;
	}
	public void setCrtime(String crtime) {
		this.crtime = crtime;
	}
	public String getCustpo() {
		return custpo;
	}
	public void setCustpo(String custpo) {
		this.custpo = custpo;
	}
	public String getDummytrayid() {
		return dummytrayid;
	}
	public void setDummytrayid(String dummytrayid) {
		this.dummytrayid = dummytrayid;
	}
	public String getExpdate() {
		return expdate;
	}
	public void setExpdate(String expdate) {
		this.expdate = expdate;
	}
	public int getFulltray() {
		return fulltray;
	}
	public void setFulltray(int fulltray) {
		this.fulltray = fulltray;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public String getLot() {
		return lot;
	}
	public void setLot(String lot) {
		this.lot = lot;
	}
	public String getLottype() {
		return lottype;
	}
	public void setLottype(String lottype) {
		this.lottype = lottype;
	}
	public String getMtid() {
		return mtid;
	}
	public void setMtid(String mtid) {
		this.mtid = mtid;
	}
	public String getPallet() {
		return pallet;
	}
	public void setPallet(String pallet) {
		this.pallet = pallet;
	}
	public String getPalletdesc() {
		return palletdesc;
	}
	public void setPalletdesc(String palletdesc) {
		this.palletdesc = palletdesc;
	}
	public String getPalletgrp() {
		return palletgrp;
	}
	public void setPalletgrp(String palletgrp) {
		this.palletgrp = palletgrp;
	}
	public String getPalletid() {
		return palletid;
	}
	public void setPalletid(String palletid) {
		this.palletid = palletid;
	}
	public String getPalletstatus() {
		return palletstatus;
	}
	public void setPalletstatus(String palletstatus) {
		this.palletstatus = palletstatus;
	}
	public int getPartialqty() {
		return partialqty;
	}
	public void setPartialqty(int partialqty) {
		this.partialqty = partialqty;
	}
	public String getPickerlistgenerationstatus() {
		return pickerlistgenerationstatus;
	}
	public void setPickerlistgenerationstatus(String pickerlistgenerationstatus) {
		this.pickerlistgenerationstatus = pickerlistgenerationstatus;
	}
	public int getPickqty() {
		return pickqty;
	}
	public void setPickqty(int pickqty) {
		this.pickqty = pickqty;
	}
	public String getPrdclass() {
		return prdclass;
	}
	public void setPrdclass(String prdclass) {
		this.prdclass = prdclass;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getRefno() {
		return refno;
	}
	public void setRefno(String refno) {
		this.refno = refno;
	}
	public int getRelease() {
		return release;
	}
	public void setRelease(int release) {
		this.release = release;
	}
	public int getSino() {
		return sino;
	}
	public void setSino(int sino) {
		this.sino = sino;
	}
	public int getSinoln() {
		return sinoln;
	}
	public void setSinoln(int sinoln) {
		this.sinoln = sinoln;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getSkudesc() {
		return skudesc;
	}
	public void setSkudesc(String skudesc) {
		this.skudesc = skudesc;
	}
	public int getSoline() {
		return soline;
	}
	public void setSoline(int soline) {
		this.soline = soline;
	}
	public String getSonum() {
		return sonum;
	}
	public void setSonum(String sonum) {
		this.sonum = sonum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTraveler() {
		return traveler;
	}
	public void setTraveler(String traveler) {
		this.traveler = traveler;
	}
	public String getTravelerid() {
		return travelerid;
	}
	public void setTravelerid(String travelerid) {
		this.travelerid = travelerid;
	}
	public String getTraygroup() {
		return traygroup;
	}
	public void setTraygroup(String traygroup) {
		this.traygroup = traygroup;
	}
	public String getTrayid() {
		return trayid;
	}
	public void setTrayid(String trayid) {
		this.trayid = trayid;
	}
	public int getTraylabelqty() {
		return traylabelqty;
	}
	public void setTraylabelqty(int traylabelqty) {
		this.traylabelqty = traylabelqty;
	}
	public String getTraystatus() {
		return traystatus;
	}
	public void setTraystatus(String traystatus) {
		this.traystatus = traystatus;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getUpat() {
		return upat;
	}
	public void setUpat(String upat) {
		this.upat = upat;
	}
	public String getUpby() {
		return upby;
	}
	public void setUpby(String upby) {
		this.upby = upby;
	}
  
  
  

}