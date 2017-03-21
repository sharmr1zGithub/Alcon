package com.murho.DO;

public class RecvDet_DO 
{
 // public RecvDet_DO()
 // {
//  }
  private String traveler="";
 // private long pallet;
 private String pallet="";
  private String mtid="";
  private String sku="";
  private String description="";
  private String lot="";
  private int ordqty;
  private int recvqty;
  private int putawayqty;
  private String lOC="";
  private String lOC1="";
  private String expiredate="";
  private String assigneduser="";
  private String receivestatus="";
  private String putawaystatus="";
  private String status="";
  private String remark="";
  private String crat="";
  private String crby="";
  private String crtime="";
  private String upat="";
  private String upby="";
  private String sled="";
  private String qed="";
  private String stktype="";
  private String recstat="";
  private String userfld1="";
  private String userfld2="";
  private String userfld3="";
  private String userfld4="";
  private String userfld5="";
  private String userfld6="";
  private String userflg1="";
  private String userflg2="";
  private String userflg3="";
  private String userflg4="";
  private String userflg5="";
  private String userflg6="";
  private String usertime1="";
  private String usertime2="";
  private String usertime3="";
  private double userdbll;
  private double userdbl2;
  private double userdbl3;
  private double userdbl4;
  private double userdbl5;
  private double userdbl6;
  private String createdon="";
  private String plant="";
  private String filename="";
  private String linenos="";
  private String mode="";
  
  public String getTraveler()
 	{
 	  return traveler;
	 }
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
	public String getCreatedon() {
		return createdon;
	}
	public void setCreatedon(String createdon) {
		this.createdon = createdon;
	}
	public String getCrtime() {
		return crtime;
	}
	public void setCrtime(String crtime) {
		this.crtime = crtime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getExpiredate() {
		return expiredate;
	}
	public void setExpiredate(String expiredate) {
		this.expiredate = expiredate;
	}
	public String getLOC() {
		return lOC;
	}
	public void setLOC(String loc) {
		lOC = loc;
	}
	public String getLOC1() {
		return lOC1;
	}
	public void setLOC1(String loc1) {
		lOC1 = loc1;
	}
	public String getLot() {
		return lot;
	}
	public void setLot(String lot) {
		this.lot = lot;
	}
	public String getMtid() {
		return mtid;
	}
	public void setMtid(String mtid) {
		this.mtid = mtid;
	}
	public int getOrdqty() {
		return ordqty;
	}
	public void setOrdqty(int ordqty) {
		this.ordqty = ordqty;
	}
	/*public long getPallet() {
		return pallet;
	}
	public void setPallet(long pallet) {
		this.pallet = pallet;
	}*/
  
  public String  getPallet() {
		return pallet;
	}
	public void setPallet(String pallet) {
		this.pallet = pallet;
	}
  
	public int getPutawayqty() {
		return putawayqty;
	}
	public void setPutawayqty(int putawayqty) {
		this.putawayqty = putawayqty;
	}
	public String getPutawaystatus() {
		return putawaystatus;
	}
	public void setPutawaystatus(String putawaystatus) {
		this.putawaystatus = putawaystatus;
	}
	public String getQed() {
		return qed;
	}
	public void setQed(String qed) {
		this.qed = qed;
	}
	public String getReceivestatus() {
		return receivestatus;
	}
	public void setReceivestatus(String receivestatus) {
		this.receivestatus = receivestatus;
	}
	public String getRecstat() {
		return recstat;
	}
	public void setRecstat(String recstat) {
		this.recstat = recstat;
	}
	public int getRecvqty() {
		return recvqty;
	}
	public void setRecvqty(int recvqty) {
		this.recvqty = recvqty;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getSled() {
		return sled;
	}
	public void setSled(String sled) {
		this.sled = sled;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStktype() {
		return stktype;
	}
	public void setStktype(String stktype) {
		this.stktype = stktype;
	}
	
	public void setTraveler(String traveler) {
		this.traveler = traveler;
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
	public double getUserdbl2() {
		return userdbl2;
	}
	public void setUserdbl2(double userdbl2) {
		this.userdbl2 = userdbl2;
	}
	public double getUserdbl3() {
		return userdbl3;
	}
	public void setUserdbl3(double userdbl3) {
		this.userdbl3 = userdbl3;
	}
	public double getUserdbl4() {
		return userdbl4;
	}
	public void setUserdbl4(double userdbl4) {
		this.userdbl4 = userdbl4;
	}
	public double getUserdbl5() {
		return userdbl5;
	}
	public void setUserdbl5(double userdbl5) {
		this.userdbl5 = userdbl5;
	}
	public double getUserdbl6() {
		return userdbl6;
	}
	public void setUserdbl6(double userdbl6) {
		this.userdbl6 = userdbl6;
	}
	public double getUserdbll() {
		return userdbll;
	}
	public void setUserdbll(double userdbll) {
		this.userdbll = userdbll;
	}
	public String getUserfld1() {
		return userfld1;
	}
	public void setUserfld1(String userfld1) {
		this.userfld1 = userfld1;
	}
	public String getUserfld2() {
		return userfld2;
	}
	public void setUserfld2(String userfld2) {
		this.userfld2 = userfld2;
	}
	public String getUserfld3() {
		return userfld3;
	}
	public void setUserfld3(String userfld3) {
		this.userfld3 = userfld3;
	}
	public String getUserfld4() {
		return userfld4;
	}
	public void setUserfld4(String userfld4) {
		this.userfld4 = userfld4;
	}
	public String getUserfld5() {
		return userfld5;
	}
	public void setUserfld5(String userfld5) {
		this.userfld5 = userfld5;
	}
	public String getUserfld6() {
		return userfld6;
	}
	public void setUserfld6(String userfld6) {
		this.userfld6 = userfld6;
	}
	public String getUserflg1() {
		return userflg1;
	}
	public void setUserflg1(String userflg1) {
		this.userflg1 = userflg1;
	}
	public String getUserflg2() {
		return userflg2;
	}
	public void setUserflg2(String userflg2) {
		this.userflg2 = userflg2;
	}
	public String getUserflg3() {
		return userflg3;
	}
	public void setUserflg3(String userflg3) {
		this.userflg3 = userflg3;
	}
	public String getUserflg4() {
		return userflg4;
	}
	public void setUserflg4(String userflg4) {
		this.userflg4 = userflg4;
	}
	public String getUserflg5() {
		return userflg5;
	}
	public void setUserflg5(String userflg5) {
		this.userflg5 = userflg5;
	}
	public String getUserflg6() {
		return userflg6;
	}
	public void setUserflg6(String userflg6) {
		this.userflg6 = userflg6;
	}
	public String getUsertime1() {
		return usertime1;
	}
	public void setUsertime1(String usertime1) {
		this.usertime1 = usertime1;
	}
	public String getUsertime2() {
		return usertime2;
	}
	public void setUsertime2(String usertime2) {
		this.usertime2 = usertime2;
	}
	public String getUsertime3() {
		return usertime3;
	}
	public void setUsertime3(String usertime3) {
		this.usertime3 = usertime3;
	}
  public String getPlant() {
		return plant;
	}
	public void setPlant(String plant) {
		this.plant =plant;
	}
  
  	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
  
  public String getLinenos() {
		return linenos;
	}
	public void setLinenos(String linenos) {
		this.linenos = linenos;
  }  
   public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
  }  
  
}