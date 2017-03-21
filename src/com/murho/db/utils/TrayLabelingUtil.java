package com.murho.db.utils;

//import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.murho.DO.TransactionDTO;
import com.murho.dao.InvMstDAO;
import com.murho.dao.ItemMstDAO;
import com.murho.dao.MovHisDAO;
import com.murho.dao.OBTravelerDetDAO;
import com.murho.dao.OBTravelerHdrDAO;
import com.murho.dao.ShipDetDAO;
import com.murho.gates.DbBean;
import com.murho.tran.WmsTran;
import com.murho.tran.WmsTrayLabeling;
import com.murho.utils.CibaConstants;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;
import com.murho.utils.StrUtils;
import com.murho.utils.XMLUtils;

public class TrayLabelingUtil {
	com.murho.utils.XMLUtils xu = null;

	com.murho.utils.StrUtils strUtils = null;

	public TrayLabelingUtil() {
		xu = new XMLUtils();
		strUtils = new StrUtils();

	}

	public String getReferenceList() throws Exception {
		String xmlStr = "";
		ArrayList al = null;
		OBTravelerHdrDAO dao = new OBTravelerHdrDAO();
		try {
			al = dao.getReferenceList();
			MLogger.info("Record size() :: " + al.size());
			if (al.size() > 0) {
				xmlStr += xu.getXMLHeader();
				xmlStr += xu.getStartNode("ReferenceList total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += xu.getStartNode("record");
					xmlStr += xu.getXMLNode("Reference", (String) map
							.get("RefNo"));
					xmlStr += xu.getEndNode("record");
				}
				xmlStr += xu.getEndNode("ReferenceList");
			}
		} catch (Exception e) {

			throw new Exception("getReferenceList() " + e.getMessage());
		}
		return xmlStr;
	}


	public String getTravelerListForRef(String refNum) throws Exception {

		String xmlStr = "";
		ArrayList al = null;
		OBTravelerHdrDAO dao = new OBTravelerHdrDAO();
		try {
			al = dao.getTravelerListForRefNo(refNum);

			MLogger.info("Record size() :: " + al.size());
			if (al.size() > 0) {
				xmlStr += xu.getXMLHeader();
				xmlStr += xu.getStartNode("TravelerDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += xu.getStartNode("record");
					xmlStr += xu.getXMLNode("Traveler", (String) map
							.get("Traveler"));
					xmlStr += xu.getEndNode("record");

					/*
					 * Commented by Ranjana for 6.0 tray labelling screen xmlStr +=
					 * xu.getXMLNode("Desc", (String) map.get("TRAVDESC"));
					 */
				}
				xmlStr += xu.getEndNode("TravelerDetails");
			}

		} catch (Exception e) {

			throw new Exception("getTravelerListForRef() " + e.getMessage());
		}
		return xmlStr;
	}

	public String getTravelersList() throws Exception {
		String xmlStr = "";
		ArrayList al = null;
		OBTravelerHdrDAO dao = new OBTravelerHdrDAO();
		try {
			al = dao.getTravelerList();
			MLogger.info("Record size() :: " + al.size());
			if (al.size() > 0) {
				xmlStr += xu.getXMLHeader();
				xmlStr += xu.getStartNode("TravelerDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += xu.getStartNode("record");
					xmlStr += xu.getXMLNode("Traveler", (String) map
							.get("Traveler_Id"));
					xmlStr += xu.getXMLNode("Desc", (String) map
							.get("TRAVDESC"));
					xmlStr += xu.getEndNode("record");
				}
				xmlStr += xu.getEndNode("TravelerDetails");
			}

		} catch (Exception e) {

			throw new Exception("getTravelersList() " + e.getMessage());
		}
		return xmlStr;
	}

	public String getPalletList(String Traveler, String aStatus)
			throws Exception {
		String xmlStr = "";
		ArrayList al = null;
		OBTravelerDetDAO dao = new OBTravelerDetDAO();
		try {
			al = dao.getPalletListForTraveler(Traveler, aStatus);
			MLogger.info("Record size() :: " + al.size());
			if (al.size() > 0) {
				xmlStr += xu.getXMLHeader();
				xmlStr += xu.getStartNode("TravelerDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += xu.getStartNode("record");
					xmlStr += xu.getXMLNode("Pallet", (String) map
							.get("Pallet"));
					// xmlStr += xu.getXMLNode("desc",  strUtils.replaceCharacters2Send((String) map.get("SKU_DESC")).toString());
					
					/*
					 * BELOW LINE COMMENTED FOR 6.0 TRAYLABELLING ENHANCEMENT
					*xmlStr += xu.getXMLNode("Desc", strUtils.replaceCharacters2Send((String) map.get("PDESC"))
					*		.toString());
					*/
					
					// xmlStr += xu.getXMLNode("Desc", (String) map.get("PDESC"));
					
					xmlStr += xu.getXMLNode("PALLETNO", (String) map.get("PALLETNO"));
					xmlStr += xu.getEndNode("record");
				}
				xmlStr += xu.getEndNode("TravelerDetails");
			} else {
				xmlStr = xu.getXMLMessage(1, "No Pallet data found");
			}
		} catch (Exception e) {

			throw new Exception("getPalletList() " + e.getMessage());
		}
		return xmlStr;
	}

	public String getTrayListForPallet(String aRefno, String Traveler,
			String aPallet, String aTrayId) throws Exception {
		String xmlStr = "";
		ArrayList al = null;
		//ArrayList alPallet = null;
		OBTravelerDetDAO dao = new OBTravelerDetDAO();
		try {
			if (aTrayId.length() > 0) {
				al = dao.getSkuListForTray(aTrayId);
			} else {
				al = dao.getTrayListForPallet(aRefno, Traveler, aPallet);
			}

			MLogger.info("Record size() :: " + al.size());
			if (al.size() > 0) {
				xmlStr += xu.getXMLHeader();
				xmlStr += xu.getStartNode("TrayDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {

					Map map = (Map) al.get(i);
					xmlStr += xu.getStartNode("record");
					xmlStr += xu.getXMLNode("trayid", (String) map
							.get("TRAYID"));
					//Added below line for #35550
					xmlStr += xu.getXMLNode("lot", (String) map.get("LOT"));
					xmlStr += xu.getXMLNode("sku", (String) map.get("SKU"));
					// Added By samatha on 20 Jan 2010 for Reprint Trayid in New Format to Include SKU UPC CODE
					xmlStr += xu.getXMLNode("skuupccode", (String) map
							.get("SKUUPCCODE"));
					xmlStr += xu.getXMLNode("desc", strUtils
							.replaceCharacters2Send(
									(String) map.get("SKU_DESC")).toString());
					xmlStr += xu.getXMLNode("qty", (String) map.get("QTY"));
					xmlStr += xu.getXMLNode("dest", (String) map.get("DESTN"));
					xmlStr += xu.getXMLNode("lottype", (String) map
							.get("LOTTYPE"));
					xmlStr += xu.getXMLNode("fulltray", (String) map
							.get("FULLTRAY"));

					xmlStr += xu.getXMLNode("traveler_id", (String) map
							.get("TRAVELER_ID"));
					xmlStr += xu.getXMLNode("pallet", (String) map
							.get("PALLETID"));
					xmlStr += xu.getXMLNode("PRDGRPID", (String) map
							.get("PRDGRPID"));

					//        added below line to get tray count for #35550
					xmlStr += xu.getXMLNode("tray_cnt", (String) map
							.get("tray_cnt"));
					
//					Added by Ranjana To fetch the GTIN2 from table as per the WO0000000471852 
					xmlStr += xu.getXMLNode("GTIN2", (String) map
							.get("GTIN2"));

					xmlStr += xu.getEndNode("record");
				}
				xmlStr += xu.getEndNode("TrayDetails");
			} else {
				xmlStr = xu.getXMLMessage(1,
						"Tray Details not Found for Traveler_id");
			}
		} catch (Exception e) {
			throw new Exception("getTrayListForPallet() " + e.getMessage());
		}
		return xmlStr;
	}

	/* public String getMTIDDetails(String TravelId,String PalletNo,String mtid) throws Exception{
	 MLogger.log(1, this.getClass() + " getMTIDDetails()");
	 String xmlStr = "";
	 Map map = null;
	 OBTravelerDetDAO dao = new OBTravelerDetDAO(); 
	 try{
	 map =dao.getTrayId(TravelId,PalletNo,mtid);
	 if (map.size() > 0) {
	 String aTrayId ="";
	 aTrayId= (String) map.get("TrayId");
	 Map detMap = null;
	 xmlStr = xu.getXMLHeader();
	 xmlStr = xmlStr + xu.getStartNode("PickDetails");
	 
	 detMap=dao.getMtidDetails(TravelId,PalletNo,mtid,aTrayId);
	 if (detMap.size() > 0) {
	 
	 xmlStr = xmlStr + xu.getXMLNode("status", "0");
	 xmlStr = xmlStr + xu.getXMLNode("description", "");
	 xmlStr = xmlStr + xu.getXMLNode("MTID", (String) detMap.get("MTID"));
	 xmlStr = xmlStr + xu.getXMLNode("SLNO", (String) detMap.get("SINO"));
	 xmlStr = xmlStr + xu.getXMLNode("TRAYID", aTrayId);
	 xmlStr = xmlStr + xu.getXMLNode("SKU", (String) detMap.get("SKU"));
	 xmlStr = xmlStr + xu.getXMLNode("SKU_DESC", (String) detMap.get("SKU_DESC"));
	 xmlStr = xmlStr + xu.getXMLNode("DESTN", (String) detMap.get("DESTN"));
	 xmlStr = xmlStr + xu.getXMLNode("LOT", (String) detMap.get("LOT"));
	 xmlStr = xmlStr + xu.getXMLNode("QTY", (String) detMap.get("QTY"));
	 String skuDesc=(String) detMap.get("SKU_DESC");
	 xmlStr = xmlStr + xu.getXMLNode("PRDGRP",skuDesc.substring(0,3));
	 xmlStr = xmlStr + xu.getXMLNode("FULLTRAY",(String) detMap.get("FULLTRAY"));
	 xmlStr = xmlStr + xu.getXMLNode("LOTTYPE",(String) detMap.get("LOTTYPE"));
	 String lottype = (String) detMap.get("LOTTYPE");
	 String fTray = (String) detMap.get("FULLTRAY");
	 if (fTray.equalsIgnoreCase("0") && lottype.equalsIgnoreCase("ML")){
	 xmlStr = xmlStr + xu.getXMLNode("NoOfSampleScanning",(String) detMap.get("QTY"));
	 }else
	 {
	 xmlStr = xmlStr + xu.getXMLNode("NoOfSampleScanning", CibaConstants.CTrayLabelScanningSample);   
	 }
	 
	 }
	 else
	 {
	 throw new Exception("Details not found for Delivery No : "  + TravelId +" MTID : " + mtid +" TRAYID : "+aTrayId);
	 }
	 }else
	 {
	 throw new Exception("Details not found for Delivery No : "  + TravelId +" MTID/SlNo : " + mtid );
	 }
	 
	 xmlStr = xmlStr + xu.getEndNode("PickDetails");
	 MLogger.log(0, "Value of xml : " + xmlStr);
	 }
	 catch (Exception e) {
	 MLogger.exception("getMTIDDetails()", e);
	 throw e;
	 }
	 MLogger.log( -1, this.getClass() + " getMTIDDetails()");
	 return xmlStr;
	 }
	 */

	//CCODE -05
	//Modified On Jan 19 2010
	// By samatha for URS A39
	public String getMTIDDetails(String TravelId, String PalletNo, String mtid)
			throws Exception {
		MLogger.log(1, this.getClass() + " getMTIDDetails()");
		String xmlStr = "";
		Map map = null;
		OBTravelerDetDAO dao = new OBTravelerDetDAO();
		InvMstDAO invDao = new InvMstDAO();
		ItemMstDAO _ItemMstDAO = new ItemMstDAO();
		try {
			map = dao.getTrayId(TravelId, PalletNo, mtid);
			if (map.size() > 0) {
				String aTrayId = "";
				aTrayId = (String) map.get("DummyTrayId");
				Map detMap = null;
				xmlStr = xu.getXMLHeader();
				xmlStr = xmlStr + xu.getStartNode("PickDetails");

				detMap = dao.getMtidDetails(TravelId, PalletNo, mtid, aTrayId);
				if (detMap.size() > 0) {

					xmlStr = xmlStr + xu.getXMLNode("status", "0");
					xmlStr = xmlStr + xu.getXMLNode("description", "");
					xmlStr = xmlStr
							+ xu
									.getXMLNode("MTID", (String) detMap
											.get("MTID"));
					xmlStr = xmlStr
							+ xu
									.getXMLNode("SLNO", (String) detMap
											.get("SINO"));
					String lottype = (String) detMap.get("LOTTYPE");
					String fTray = (String) detMap.get("FULLTRAY");
					String genTrayId = "";
					StringBuffer sql = new StringBuffer(" SET ");

					if (fTray.equalsIgnoreCase("1")) {
						xmlStr = xmlStr
								+ xu.getXMLNode("TRAYID", (String) detMap
										.get("MTID"));
						xmlStr = xmlStr + xu.getXMLNode("PRINTFLG", "N");
						sql.append(" Trayid = '" + (String) detMap.get("MTID")
								+ "' ");
					} else if (fTray.equalsIgnoreCase("0")
							&& lottype.equalsIgnoreCase("SL")) {
						Hashtable ht = new Hashtable();
						ht.put("SKU", (String) detMap.get("SKU"));
						ht.put("LOT", (String) detMap.get("LOT"));
						ht.put("MTID", (String) detMap.get("MTID"));
						boolean isDataPresent = invDao.isExists(ht);
						// Check if Exists in Inventory else genearate New Trayid

						if (isDataPresent) {
							xmlStr = xmlStr
									+ xu.getXMLNode("TRAYID", (String) detMap
											.get("MTID"));
							xmlStr = xmlStr + xu.getXMLNode("PRINTFLG", "Y");

							sql.append(" Trayid = '"
									+ (String) detMap.get("MTID") + "' ");

						} else {
							genTrayId = dao
									.getGeneratedTrayId(CibaConstants.cibacompanyName);
							xmlStr = xmlStr
									+ xu.getXMLNode("TRAYID", genTrayId);
							xmlStr = xmlStr + xu.getXMLNode("PRINTFLG", "Y");
							sql.append(" Trayid = '" + genTrayId + "' ");
						}
					} else if (fTray.equalsIgnoreCase("0")
							&& lottype.equalsIgnoreCase("ML")) {
						//  genearate New Trayid
						Map m = dao.getTrayIdForDummyTrayId(TravelId, PalletNo,
								aTrayId);
						if (m.size() > 0) {
							genTrayId = (String) m.get("TrayId");
							xmlStr = xmlStr
									+ xu.getXMLNode("TRAYID", genTrayId);
							xmlStr = xmlStr + xu.getXMLNode("PRINTFLG", "Y");
						} else {
							// genTrayId= dao.getGeneratedTrayId(CibaConstants.cibacompanyName);
							genTrayId = dao
									.getGeneratedTrayId(CibaConstants.cibacompanyName);
							xmlStr = xmlStr
									+ xu.getXMLNode("TRAYID", genTrayId);
							xmlStr = xmlStr + xu.getXMLNode("PRINTFLG", "Y");
						}
						sql.append(" Trayid = '" + genTrayId + "' ");
					}

					Hashtable htobTravdet = new Hashtable();
					htobTravdet.put(MDbConstant.TRAVELER_ID, TravelId);
					htobTravdet.put("PALLETID", PalletNo);
					//   htobTravdet.put(MDbConstant.MTID,mtid);
					htobTravdet.put("DummyTrayID", aTrayId);

					boolean flag = dao.update(sql.toString(), htobTravdet, "");
					if (!flag) {
						throw new Exception("Failed to Assign MTID to TrayID");
					}

					//insertEscp
					xmlStr = xmlStr
							+ xu.getXMLNode("SKU", (String) detMap.get("SKU"));

					xmlStr = xmlStr
							+ xu.getXMLNode("SKU_DESC", (String) strUtils
									.replaceCharacters2Send(detMap.get(
											"SKU_DESC").toString()));
					// xmlStr = xmlStr + xu.getXMLNode("SKU_DESC",  (String)detMap.get("SKU_DESC").toString());
					//xmlStr = xmlStr + xu.getXMLNode("SKU_DESC",  (String) detMap.get("SKU_DESC"));
					// xmlStr = xmlStr + xu.getXMLNode("DESTN", (String) detMap.get("DESTN"));

					xmlStr = xmlStr
							+ xu.getXMLNode("DESTN", (String) strUtils
									.replaceCharacters2Send(detMap.get("DESTN")
											.toString()));

					xmlStr = xmlStr
							+ xu.getXMLNode("LOT", (String) detMap.get("LOT"));
					xmlStr = xmlStr
							+ xu.getXMLNode("QTY", (String) detMap.get("QTY"));

					String skuDesc = (String) strUtils
							.replaceCharacters2Send(detMap.get("SKU_DESC")
									.toString());
					xmlStr = xmlStr
							+ xu.getXMLNode("PRDGRP", skuDesc.substring(0, 3));
					xmlStr = xmlStr
							+ xu.getXMLNode("PRDGRPID", (String) strUtils
									.replaceCharacters2Send(detMap.get(
											"PRDGRPID").toString()));
					System.out.println("COme 1 in asdfsadfds");
					xmlStr = xmlStr
							+ xu.getXMLNode("FULLTRAY", (String) detMap
									.get("FULLTRAY"));
					xmlStr = xmlStr
							+ xu.getXMLNode("LOTTYPE    ", (String) detMap
									.get("LOTTYPE"));
					//Modified to Retrive ExpDate By Samatha on Jan 19 2010
					String expDate = invDao.getExpiryDateForLot(
							CibaConstants.cibacompanyName, (String) detMap
									.get("SKU"), (String) detMap.get("LOT"));
					xmlStr = xmlStr + xu.getXMLNode("EXPDATE", expDate);
					//Added to Retrieve Type of Product on 7-Apr-2014 for UDI implementation
					String prdType = invDao.getTypeofProduct(mtid);
					xmlStr = xmlStr + xu.getXMLNode("PRODUCTTYPE",prdType);
					
					System.out.println("COme 2in asdfsadfds");
					String upcCode = _ItemMstDAO.getUPCCode(
							CibaConstants.cibacompanyName, (String) detMap
									.get("SKU"));
					xmlStr = xmlStr + xu.getXMLNode("UPCCODE    ", upcCode);

					if (fTray.equalsIgnoreCase("0")
							&& lottype.equalsIgnoreCase("ML")) {
						xmlStr = xmlStr
								+ xu.getXMLNode("NoOfSampleScanning",
										(String) detMap.get("QTY"));
					} else {
						xmlStr = xmlStr
								+ xu.getXMLNode("NoOfSampleScanning",
										CibaConstants.CTrayLabelScanningSample);
					}
					xmlStr = xmlStr
							+ xu.getXMLNode("COO", (String) strUtils
									.replaceCharacters2Send(detMap.get("COO")
											.toString()));

				} else {
					throw new Exception("Details not found for Delivery No : "
							+ TravelId + " MTID : " + mtid + " TRAYID : "
							+ aTrayId);
				}
			} else {
				throw new Exception("Details not found for Delivery No : "
						+ TravelId + " MTID/SlNo : " + mtid);
			}

			xmlStr = xmlStr + xu.getEndNode("PickDetails");
			MLogger.log(0, "Value of xml : " + xmlStr);
		} catch (Exception e) {
			MLogger.exception("getMTIDDetails()", e);
			throw e;
		}
		MLogger.log(-1, this.getClass() + " getMTIDDetails()");
		return xmlStr;
	}

	public String getSumOfSkuQty(Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " getSumOfSkuQty()");
		String skuTotQty = "";
		String xmlStr = "";
		String query = " sum(TRAYLABLEQTY) as QTY ";
		//condintion

		OBTravelerDetDAO dao = new OBTravelerDetDAO();
		ht.put("STATUS", "C");

		Map m = dao.selectRow(query, ht);
		skuTotQty = (String) m.get("QTY");

		xmlStr = xu.getXMLHeader();
		xmlStr = xmlStr + xu.getStartNode("PrintQty");
		try {

			if (skuTotQty.length() > 0) {
				xmlStr = xmlStr + xu.getXMLNode("status", "0");
				xmlStr = xmlStr + xu.getXMLNode("description", "");
				xmlStr = xmlStr + xu.getXMLNode("QTYTOPRINT", skuTotQty);
			} else {
				throw new Exception("QTY_TO_PRINT Not found for Item :"
						+ ht.get(MDbConstant.SKU));
			}
			xmlStr = xmlStr + xu.getEndNode("PrintQty");
			MLogger.log(0, "Value of xml : " + xmlStr);
		} catch (Exception e) {
			MLogger.exception("Getqtypertray()", e);
			throw e;
		}

		MLogger.info("getSumOfSkuQty : " + skuTotQty);

		MLogger.log(-1, this.getClass() + " getSumOfSkuQty()");

		return xmlStr;

	}

	public String getLotForSL(Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " getLotForSL()");
		String lotforSku = "", lotbarcodeVal = "", coo = "";
		String xmlStr = "";
		//BaseDAO _BaseDAO=new BaseDAO();
		// Connection con=null;
		// con = DbBean.getConnection();
		// PreparedStatement pStmt = null;
		//  Statement stmt = null;
		//  ResultSet rs = null;
		int lotlen = 0;
		//Map maplot=null;
		String query = " LOT ,LOT_BARCODE,(select coo from COO_CNFG_TBL where LOTSTARTWITH=SUBSTRING(ob_travel_det.LOT,1,1)) AS COO ";
		//condintion

		OBTravelerDetDAO dao = new OBTravelerDetDAO();
		ht.put("TRAYSTATUS", "C");
		Map m = dao.selectRow(query, ht);
		lotforSku = (String) m.get("LOT");
		lotbarcodeVal = (String) m.get("LOT_BARCODE");
		lotlen = lotforSku.length();

		if (lotlen == 7) {
			/* System.out.println("lot length"+lotlen);
			 // StringBuffer lotsql = new StringBuffer("select  COO from COO_CNFG_TBL WHERE LOTLENGTH='"+MDbConstant.TRAYPRINTLOTLENGTH +"'");
			 String sql = "select  isnull(COO,'') as COO from COO_CNFG_TBL WHERE LOTLENGTH="+MDbConstant.TRAYPRINTLOTLENGTH +"";
			 MLogger.query(sql);
			 stmt = con.createStatement();
			 //  try
			 // {
			 rs = stmt.executeQuery(sql);  
			 // System.out.println("System......."+ rs.getString(1).toString());
			 //Hashtable htlot=null;
			 if (rs != null) {
			 System.out.println("Come in............");
			 coo= rs.getString("COO");
			 System.out.println("Come in............"+coo);
			 }
			 //   }
			 //   catch(Exception ex)
			 //       {
			 //        System.out.println("System......."+ ex.getMessage());
			 //    }
			 //---------------------
			 /* // rs.close();
			 //  pStmt.close();
			 //  DbBean.closeConnection(con);
			 //htlot.put("LOTLENGTH",MDbConstant.TRAYPRINTLOTLENGTH.toString());
			 //String lotsql = "coo as COO" ;
			 
			 //  MLogger.query(lotsql.toString());
			 //  maplot= _BaseDAO.getRowOfData(con,lotsql.toString());
			 //  Map lotmap= dao.selectCOOForSL(lotsql,htlot);
			 // System.out.println(" lotmap........."+ maplot.size());
			 // coo=(String)maplot.get("COO");*/
			coo = "ID";
		} else {
			coo = (String) m.get("COO");
		}

		xmlStr = xu.getXMLHeader();
		xmlStr = xmlStr + xu.getStartNode("PrintLOT");
		try {

			if (lotforSku.length() > 0) {
				xmlStr = xmlStr + xu.getXMLNode("status", "0");
				xmlStr = xmlStr + xu.getXMLNode("description", "");
				xmlStr = xmlStr + xu.getXMLNode("LOT", lotforSku);
				xmlStr = xmlStr + xu.getXMLNode("LOTBCODEVAL", lotbarcodeVal);
				xmlStr = xmlStr + xu.getXMLNode("COO", coo);
				String expDate = new InvMstDAO().getExpiryDateForLot(
						CibaConstants.cibacompanyName, (String) ht
								.get(MDbConstant.SKU), lotforSku);
				xmlStr = xmlStr + xu.getXMLNode("EXPDATE", expDate);
			} else {
				throw new Exception("LOT Not found for SKU :"
						+ ht.get(MDbConstant.SKU));
			}

			xmlStr = xmlStr + xu.getEndNode("PrintLOT");
			MLogger.log(0, "Value of xml : " + xmlStr);
		} catch (Exception e) {
			MLogger.exception("getLotForSL()", e);
			throw e;
		}

		MLogger.info("getLotForSL : " + lotforSku);
		return xmlStr;

	}

	public synchronized String Process_Tray_Labeling(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.murho.gates.DbBean.getUserTranaction();
			ut.begin();
			flag = process_Wms_Tray_Labeling(obj);

			if (flag == true) {
				DbBean.CommitTran(ut);
				flag = true;
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			throw e;
		}

		String xmlStr = "";
		if (flag == true) {

			// Added by Arun for #1848 to get Last tray labeling for the pallet

			Map map = obj;
			String travelerId = (String) map.get(MDbConstant.TRAVELER_ID);
			String palletId = (String) map.get(MDbConstant.PALLET);

			//Added below 4 line for ticket #35550
			String ordQtyTemp = (String) map.get(MDbConstant.ORD_QTY);
			int ordQty = Integer.parseInt(ordQtyTemp);
			String recQtyTemp = (String) map.get(MDbConstant.RECV_QTY);
			int recQty = Integer.parseInt(recQtyTemp);

			// get tray label count
			OBTravelerDetDAO dao = new OBTravelerDetDAO();
			int pendingTLCount = dao.getPendingTLCount(travelerId, palletId);
			if (pendingTLCount == 0) {
				//******************************************
				//Below function call and if condition is 
				//added by Jyoti for TIBCO-INC000002484471(WMS 2.8)
				//******************************************
				TransactionDTO trnDTO = new TransactionDTO();
				trnDTO.setTraveler(travelerId);
				int countOfMTIDDel = dao.getCountofMTIDbyTravelerWise(trnDTO);
				if (countOfMTIDDel == 0) {				
					xmlStr = xu.getXMLMessage(0,
							"Tray Labeled Successfully! PrintShipMark LastMTIDofDelivery");
				} else 
				xmlStr = xu.getXMLMessage(0,
						"Tray Labeled Successfully! PrintShipMark");
			}
			//Below else if block is added for #35550
			else if (recQty < ordQty) {
				xmlStr = xu.getXMLMessage(0, "Qty:" + recQty + " is updated"
						+ '\n' + "Tray Labeled Successfully!");
			} else {
				xmlStr = xu.getXMLMessage(0, "Tray Labeled Successfully!");
			}

		} else {
			xmlStr = xu.getXMLMessage(1, "Error in Tray Labeling");
		}
		return xmlStr;

	}

	private boolean process_Wms_Tray_Labeling(Map map) throws Exception {
		MLogger.info(" process_Wms_Tray_Labeling() starts");
		boolean flag = false;
		flag = true;
		WmsTran tran = new WmsTrayLabeling();
		flag = tran.processWmsTran(map);
		MLogger.info(" process_Wms_Tray_Labeling() ends");
		return flag;
	}

	public boolean UpdateMTIDQty(Map mp) throws Exception {
		boolean inserted = false;
		try {
			OBTravelerDetDAO dao = new OBTravelerDetDAO();
			Hashtable ht = new Hashtable();
			ht.put("TRAVELER_ID", mp.get(MDbConstant.TRAVELER_NUM));
			ht.put("MTID", mp.get(MDbConstant.MTID));
			ht.put("sku", mp.get(MDbConstant.ITEM));
			ht.put("TRAYID", mp.get(MDbConstant.TRAYID));

			String Query = "set TRAYLABLEQTY ='" + (String) mp.get("QTY") + "'";
			inserted = dao.update(Query, ht, "");
		} catch (Exception e) {
			throw e;
		}

		if (inserted) {
			inserted = processMoveHis(mp);
		}
		return inserted;
	}

	private boolean processMoveHis(Map mp) throws Exception {
		boolean flag = false;
		//TblControlDAO tblConDao=new TblControlDAO();

		MovHisDAO movHisDao = new MovHisDAO();
		try {
			MLogger.log(0, "Getting next seq no .");
			Hashtable htMovHis = new Hashtable();

			htMovHis.put(MDbConstant.PLANT, CibaConstants.cibacompanyName);
			htMovHis.put(MDbConstant.TRAVELER_NUM, mp
					.get(MDbConstant.TRAVELER_NUM));
			htMovHis.put(MDbConstant.PALLET, "");
			htMovHis.put(MDbConstant.MTID, mp.get(MDbConstant.MTID));
			htMovHis.put(MDbConstant.ITEM, mp.get(MDbConstant.ITEM));
			htMovHis.put(MDbConstant.LOT_NUM, mp.get(MDbConstant.LOT_NUM));
			htMovHis
					.put(MDbConstant.LOGIN_USER, mp.get(MDbConstant.LOGIN_USER));
			htMovHis.put("CRAT", mp.get(MDbConstant.MOVEHIS_CR_DATE));
			htMovHis.put("CRTIME", mp.get(MDbConstant.CRTIME));
			htMovHis.put("REMARK", "");
			htMovHis.put("MOVTID", mp.get(MDbConstant.MOVHIS_REF_NUM));
			htMovHis.put(MDbConstant.MOVHIS_QTY, mp.get(MDbConstant.QTY));

			flag = movHisDao.insertIntoMovHis(htMovHis);
			MLogger.log(0, "insertIntoMovHis Transaction ::  " + flag);
		} catch (Exception e) {
			MLogger.exception(this, e);
			throw e;
		}
		return flag;
	}

	public boolean insertShipDet(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {

			ShipDetDAO dao = new ShipDetDAO();
			inserted = dao.insert(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	//Method added by Arun for #1848
	public String Process_Print_Shipping_Mark(String plant, String travelerNum,
			String palletId) throws Exception {
		String xmlStr = "";
		ArrayList invQryList = new ArrayList();
		ArrayList dataList = new ArrayList();
		StrUtils su = new StrUtils();
		int listSize = 0;
		String pallet = null, companyName = "", addr1 = "", addr2 = "", addr3 = "", country = "", contactName = "";
		try {
			ShipDetDAO shipDetDao = new ShipDetDAO();
			OBTravelerDetDAO obTravelDetDAO = new OBTravelerDetDAO();
			invQryList = obTravelDetDAO.getPrintShipmarkSummary(plant,
					travelerNum, ""); // Palletid sent as blank to get total no. of pallets for a traveler

			if (null != invQryList) {
				listSize = invQryList.size();
			}
			ArrayList currentPallet = obTravelDetDAO.getCurrentPallet(
					travelerNum, palletId);
			for (int iCnt = 0; iCnt < currentPallet.size(); iCnt++) {
				Map lineArr = (Map) currentPallet.get(iCnt);
				pallet = (String) lineArr.get("pallet");
			}
			dataList = shipDetDao.getPrintingMarkDet(travelerNum);

			xmlStr = xu.getXMLHeader();
			xmlStr = xmlStr + xu.getStartNode("PrintShipMark");
			xmlStr = xmlStr + xu.getXMLNode("status", "0");
			xmlStr = xmlStr + xu.getXMLNode("description", "");

			for (int iCnt = 0; iCnt < dataList.size(); iCnt++) {

				Map lineArr = (Map) dataList.get(iCnt);

				companyName = (su.replaceSpace2Send((String) lineArr
						.get("COMPANYNAME")));
				companyName = StrUtils.forHTMLTag(companyName);
				xmlStr = xmlStr + xu.getXMLNode("COMPANYNAME", companyName);

				addr1 = (su.replaceSpace2Send((String) lineArr.get("ADDR1")));
				addr1 = StrUtils.forHTMLTag(addr1);
				xmlStr = xmlStr + xu.getXMLNode("ADDR1", addr1);

				addr2 = (su.replaceSpace2Send((String) lineArr.get("ADDR2")));
				addr2 = StrUtils.forHTMLTag(addr2);
				xmlStr = xmlStr + xu.getXMLNode("ADDR2", addr2);

				addr3 = (su.replaceSpace2Send((String) lineArr.get("ADDR3")));
				addr3 = StrUtils.forHTMLTag(addr3);
				xmlStr = xmlStr + xu.getXMLNode("ADDR3", addr3);

				country = (su
						.replaceSpace2Send((String) lineArr.get("COUNTRY")));
				country = StrUtils.forHTMLTag(country);
				xmlStr = xmlStr + xu.getXMLNode("COUNTRY", country);

				contactName = (su.replaceSpace2Send((String) lineArr
						.get("CONTACTNAME")));
				contactName = StrUtils.forHTMLTag(contactName);
				xmlStr = xmlStr + xu.getXMLNode("CONTACTNAME", contactName);

			}

			xmlStr = xmlStr + xu.getXMLNode("CURRENTPALLET", pallet);
			xmlStr = xmlStr
					+ xu.getXMLNode("TOTALPALLETS", Integer.toString(listSize));
			xmlStr = xmlStr + xu.getEndNode("PrintShipMark");

			MLogger.log(0, "Value of xml : " + xmlStr);
		} catch (Exception e) {
			MLogger.exception(this, e);
			throw e;
		}
		return xmlStr;
	}
}
