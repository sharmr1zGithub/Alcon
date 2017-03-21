package com.murho.DO;

public class TravelHdr_DO 
{
  public TravelHdr_DO()
  {
  }
  
  
   private String refno="";
   private String traveler="";
   private String travelerid="";
   private String travelerdesc="";
   private String country="";
   private String shipto="";
   private int qty;
   private String status="";
   private String pickerlistgenerationstatus;
   private String assigneduser;
   private String crat;
   private String crby;
   private String upat;
   private String upby;
   
   public String getAssigneduser() {
		return assigneduser;
	}
	public void setAssigneduser(String assigneduser) {
		this.assigneduser = assigneduser;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
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
	public String getPickerlistgenerationstatus() {
		return pickerlistgenerationstatus;
	}
	public void setPickerlistgenerationstatus(String pickerlistgenerationstatus) {
		this.pickerlistgenerationstatus = pickerlistgenerationstatus;
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
	public String getShipto() {
		return shipto;
	}
	public void setShipto(String shipto) {
		this.shipto = shipto;
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
	public String getTravelerdesc() {
		return travelerdesc;
	}
	public void setTravelerdesc(String travelerdesc) {
		this.travelerdesc = travelerdesc;
	}
	public String getTravelerid() {
		return travelerid;
	}
	public void setTravelerid(String travelerid) {
		this.travelerid = travelerid;
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