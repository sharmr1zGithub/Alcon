package com.murho.DO;

public class TransactionDTO 
{
  //public TransactionDTO()
  //{
 // }
 //..............RecTables
  private String traveler="";
  //private long pallet;
  private String pallet;
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
  private String plant;
  private String linenos="";
  private String mode="";
  
  private String filegenerated="";
  private String filename;
  private String loginuser;
  //................RecTables End
  
//................ Temp Table
 private int sno;
 private String custdo="";
 private String lotno="";
 private int qty;
 private String uom="";
 private String loc="";
 private String field8="";
 private String field9="";
 private String destination="";
 private String field11="";
 private String so="";
 private int release;
 private int soline;
 private int field15;
 private String productclass="";
 private String field17="";
 private String field18="";
 private String field19="";
 private String lottype="";
 private String shipdate="";
 private String pt="";
 private String shipparty="";
 private String material="";
 // Temp Table End
 
 //TraveHdr
   private String refno="";
   private String travelerid="";
   private String travelerdesc="";
   private String country="";
   private String shipto="";
   private String pickerlistgenerationstatus;
 
 //TravelDet
  private int sino;
  private int sinoln;
  private String palletid;
  private String palletgrp;
  private String palletdesc;
  private String trayid;
  private String traygroup;
  private String prdclass;
  private String sonum;
  private String custpo;
  private String skudesc="";
  private int fulltray;
  private int partialqty;
  private int pickqty;
  private int traylabelqty;
  private String palletstatus;
  private String traystatus;
  private String expdate;
  private String dummytrayid;
  
 
  //RecvTables Start
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
	public String getTraveler() {
		return traveler;
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
  
  public String getFilegenerated() {
		return filegenerated;
	}
	public void setFilegenerated(String filegenerated) {
		this.filegenerated = filegenerated;
	}
  
  public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
  public String getLoginuser() {
		return loginuser;
	}
	public void setLoginuser(String loginuser) {
		this.loginuser = loginuser;
	}
  
   public String getPlant() {
		return plant;
	}
	public void setPlant(String plant) {
		this.plant = plant;
  }

public String getLinenos() {
		return linenos;
	}
	public void setLinenos(String linenos) {
		this.linenos = linenos;
  }  
  
  
  //.............RecvTables  End 
  
  //.............Temp Table Start
  
  public int getSno() {
		return sno;
	}
 public void setSno(int sno) {
		this.sno = sno;
	}
  
   public String getCustdo() {
		return custdo ;
	}
	public void setCustdo(String custdo) {
		this.custdo = custdo;
	}
  public String getLotno() {
		return lotno ;
	}
	public void setLotno(String lotno) {
		this.lotno = lotno;
	}
 public int getQty() {
		return qty;
	}
 public void setQty(int qty) {
		this.qty = qty;
	}
   
   public String getUom() {
		return uom ;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
  
  public String getLoc() {
		return loc ;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
  
  public String getField8() {
		return field8 ;
	}
	public void setField8(String field8) {
		this.field8 = field8;
	}
  
  public String getField9() {
		return field9 ;
	}
	public void setField9(String field9) {
		this.field9 = field9;
	}
  
  public String getDestination() {
		return destination ;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
  
  public String getField11() {
		return field11 ;
	}
	public void setField11(String field11) {
		this.field11 = field11;
	}
  
 public String getSo() {
		return so ;
	}
	public void setSo(String so) {
		this.so= so;
	}
  
   public int getRelease() {
		return release;
	}
 public void setRelease(int release) {
		this.release= release;
	}
  
   public int getsoLine() {
		return soline;
	}
 public void setsoLine(int soline) {
		this.soline= soline;
	}
  
  public int getField15() {
		return field15;
	}
 public void setField15(int field15) {
		this.field15= field15;
	}
  
  public String Productclass() {
		return productclass ;
	}
	public void setProductclass(String productclass) {
		this.productclass= productclass;
	}
  
   public String getField17() {
		return field17 ;
	}
	public void setField17(String field17) {
		this.field17 = field17;
	}
  
   public String getField18() {
		return field18 ;
	}
	public void setField18(String field18) {
		this.field18 = field18;
	}
  
   public String getField19() {
		return field19 ;
	}
	public void setField19(String field19) {
		this.field19 = field19;
	}
  
  
  public String getLottype() {
		return lottype ;
	}
	public void setLottype(String lottype) {
		this.lottype = lottype;
	}
  

  
   public String getShipdate() {
		return shipdate ;
	}
	public void setShipdate(String gidate) {
		this.shipdate = gidate;
	}
   public String getPt() {
		return pt ;
	}
	public void setPt(String pt) {
		this.pt = pt;
	}
    public String getShipparty() {
		return shipparty ;
	}
	public void setShipparty(String shipparty) {
		this.shipparty = shipparty;
	}
   public String getMaterial() {
		return material ;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
   public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
  }  
  //.....................Temp Table End
  
  
  //....TravelHdr Start
  
  
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	public String getPickerlistgenerationstatus() {
		return pickerlistgenerationstatus;
	}
	public void setPickerlistgenerationstatus(String pickerlistgenerationstatus) {
		this.pickerlistgenerationstatus = pickerlistgenerationstatus;
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
	    
  //....TravelDet End
  
  //TravelDet Start......
   	
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

  ///TravelDet End

}