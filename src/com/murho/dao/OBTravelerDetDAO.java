package com.murho.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.DO.TransactionDTO;
import com.murho.DO.TravelDet_DO;
import com.murho.gates.DbBean;
import com.murho.utils.CibaConstants;
import com.murho.utils.DateUtils;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;

public class OBTravelerDetDAO extends BaseDAO {
	public OBTravelerDetDAO() {
	}

	public static final String TABLE_NAME = "OB_TRAVEL_DET";

	public Map selectRow(String query, Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " select()");
		Map map = new HashMap();

		java.sql.Connection con = null;

		// connection
		try {
			con = com.murho.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ TABLE_NAME);
			sql.append(" WHERE ");

			String conditon = formCondition(ht);

			sql.append(conditon);

			MLogger.query(sql.toString());

			map = getRowOfData(con, sql.toString());
		} catch (Exception e) {
			MLogger
					.log(
							0,
							"######################### Exception :: select() : ######################### \n");
			MLogger.log(0, "" + e.getMessage());
			MLogger
					.log(
							0,
							"######################### Exception :: select() : ######################### \n");
			MLogger.log(-1, "");
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		MLogger.log(-1, this.getClass() + " select()");
		return map;
	}

	public Map selectCOOForSL(String querylot, Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " select()");
		Map map = new HashMap();

		java.sql.Connection con = null;

		// connection
		try {
			con = com.murho.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + querylot
					+ " from COO_CNFG_TBL");
			sql.append(" WHERE ");

			String conditon = formCondition(ht);

			sql.append(conditon);

			MLogger.query(sql.toString());

			map = getRowOfData(con, sql.toString());
		} catch (Exception e) {
			MLogger
					.log(
							0,
							"######################### Exception :: selectCOOForSL() : ######################### \n");
			MLogger.log(0, "" + e.getMessage());
			MLogger
					.log(
							0,
							"######################### Exception :: selectCOOForSL() : ######################### \n");
			MLogger.log(-1, "");
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		MLogger.log(-1, this.getClass() + " selectCOOForSL()");
		return map;
	}

	public boolean isExisit(Hashtable ht, String extraCon) throws Exception {

		boolean flag = false;

		// connection
		java.sql.Connection con = null;

		try {
			con = com.murho.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + TABLE_NAME);
			sql.append(" WHERE  " + formCondition(ht));
			sql.append(" " + extraCon);

			MLogger.query(" " + sql.toString());

			flag = isExists(con, sql.toString());
		} catch (Exception e) {
			MLogger
					.log(
							0,
							"######################### Exception :: isExisit() : ######################### \n");
			MLogger.log(0, "" + e.getMessage());
			MLogger
					.log(
							0,
							"######################### Exception :: isExisit() : ######################### \n");
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;

	}

	public boolean update(String query, Hashtable map, String extCond)
			throws Exception {
		MLogger.log(1, this.getClass() + " update()");
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			// connection
			con = com.murho.gates.DbBean.getConnection();

			// inputs
			StringBuffer sql = new StringBuffer(" UPDATE " + TABLE_NAME);
			sql.append(" ");
			sql.append(query);
			sql.append(" WHERE ");

			String conditon = formCondition(map);

			// query
			sql.append(conditon);

			if (extCond.length() != 0) {

				sql.append(extCond);
			}

			MLogger.query(" " + sql.toString());

			flag = updateData(con, sql.toString());
		} catch (Exception e) {
			MLogger
					.log(
							0,
							"######################### Exception :: update() : ######################### \n");
			MLogger.log(0, "" + e.getMessage());
			MLogger
					.log(
							0,
							"######################### Exception :: update() : ######################### \n");
			MLogger.log(-1, "");
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		MLogger.log(-1, this.getClass() + " update()");

		return flag;

	}

	public boolean isExist(Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " isExisit()");
		boolean flag = false;
		java.sql.Connection con = null;

		try {
			// connection
			con = com.murho.gates.DbBean.getConnection();
			// query
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + TABLE_NAME);
			sql.append(" WHERE  " + formCondition(ht));

			MLogger.query(" " + sql.toString());

			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			MLogger
					.log(
							0,
							"######################### Exception :: isExisit() : ######################### \n");
			MLogger.log(0, "" + e.getMessage());
			MLogger
					.log(
							0,
							"######################### Exception :: isExisit() : ######################### \n");
			MLogger.log(-1, "");
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		} //
		MLogger.log(-1, this.getClass() + " isExisit()");
		return flag;

	}

	public ArrayList getPalletListForTraveler(String aTravelId, String aStatus)
			throws Exception {
		MLogger.log(1, this.getClass() + " getPalletListForTraveler()");
		ArrayList al = new ArrayList();
		// connection
		java.sql.Connection con = null;
		try {
			con = com.murho.gates.DbBean.getConnection();
			// query
			StringBuffer sql = new StringBuffer(" SELECT ");
			
			//6.0 TRAYLABELLING ENHANCEMENT
			//sql.append(" distinct Palletid as Pallet,isnull(Pallet_Desc,'') as PDESC ");
			
			sql
			.append(" distinct Palletid as Pallet,Pallet as PALLETNO ");
			sql.append(" ");
			sql.append(" FROM " + TABLE_NAME);
			sql.append(" WHERE  Traveler_Id = '" + aTravelId
					+ "' and TrayStatus ='" + aStatus + "'  ");

			MLogger.query(sql.toString());

			al = selectData(con, sql.toString());

		} catch (Exception e) {
			MLogger.exception(this, e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		MLogger.log(-1, this.getClass() + " getPalletListForTraveler()");
		return al;

	}

	public ArrayList getTrayListForPallet(String aRefno, String aTravelId,
			String aPallet) throws Exception {
		MLogger.log(1, this.getClass() + " getTrayListForPallet()");
		ArrayList al = new ArrayList();
		// connection
		java.sql.Connection con = null;
		try {
			con = com.murho.gates.DbBean.getConnection();

			/*
			 * StringBuffer sql = new StringBuffer(" select
			 * TRAYID,SKU,SUM(TRAYLABLEQTY) as
			 * QTY,SKU_DESC,TRAVELER_ID,PALLETID,(Select distinct destination
			 * from rules_mst where ship_to in( select country from
			 * ob_travel_hdr where traveler_id = '"+aTravelId+"' )) as DESTN ");
			 * sql.append(" ,LOTTYPE,FULLTRAY from ob_travel_det a ");
			 * sql.append(" where refno ='"+aRefno+"' and traveler_id =
			 * '"+aTravelId+"' and palletid = '"+aPallet+"' and traystatus ='C'
			 * "); sql.append(" group by
			 * TRAYID,SKU,SKU_DESC,TRAVELER_ID,PALLETID,LOTTYPE,FULLTRAY ");
			 */

			// Modified By Samatha on 20 Jan 2010 for Reprint TrayiD SKU UPC
			// CODE
			/*
			 * StringBuffer sql = new StringBuffer(" select TRAYID,SKU,(SELECT
			 * REFNO FROM ITEMMST WHERE ITEM=a.SKU) AS SKUUPCCODE,
			 * SUM(TRAYLABLEQTY) as QTY,SKU_DESC,TRAVELER_ID,PALLETID,(Select
			 * distinct destination from rules_mst where ship_to in( select
			 * country from ob_travel_hdr where traveler_id = '"+aTravelId+"' ))
			 * as DESTN, "); sql.append(" (select distinct(A.prd_grp_id) from
			 * prd_class_mst a,ob_travel_det b where a.prd_cls_id=b.prd_class
			 * and b.Traveler_id ='"+aTravelId+"' and b.refno ='"+aRefno+"' and
			 * b.palletid = '"+aPallet+"') AS PRDGRPID"),; sql.append("
			 * LOTTYPE,FULLTRAY from ob_travel_det a "); sql.append(" where
			 * refno ='"+aRefno+"' and traveler_id = '"+aTravelId+"' and
			 * palletid = '"+aPallet+"' and traystatus ='C' "); sql.append("
			 * group by
			 * TRAYID,SKU,SKU_DESC,TRAVELER_ID,PALLETID,LOTTYPE,FULLTRAY ");
			 */

			// Modified on Aug 12 2010
			// Modified query for #35550
			// commented for #35550
			/*
			 * StringBuffer sql = new StringBuffer(" select
			 * a.TRAYID,a.LOT,A.SKU,C.Refno as
			 * SKUUPCCODE,a.QTY,a.SKU_DESC,a.TRAVELER_ID,a.PALLETID,.a.DESTN,
			 * "); sql.append(" b.prd_grp_id,a.LOTTYPE,a.FULLTRAY from ");
			 * sql.append(" (Select refno,lot,traveler_id, prd_class as
			 * prd_class,TRAYID,SKU,SUM(TRAYLABLEQTY) as
			 * QTY,SKU_DESC,PALLETID,LOTTYPE,FULLTRAY, "); sql.append(" (Select
			 * distinct destination from rules_mst where ship_to in( select
			 * country from ob_travel_hdr "); sql.append(" where traveler_id =
			 * '"+aTravelId+"' )) as DESTN "); sql.append(" from ob_travel_det
			 * where refno ='"+aRefno+"' and palletid = '"+aPallet+"' ");
			 * sql.append(" and Traveler_id ='"+aTravelId+"' and traystatus ='C'
			 * "); sql.append(" group by
			 * refno,lot,TRAYID,SKU,SKU_DESC,TRAVELER_ID,PALLETID,prd_class,LOTTYPE,FULLTRAY)
			 * a,"); // sql.append(" )a, "); sql.append(" (Select
			 * prd_grp_id,prd_cls_id from prd_class_mst) b,"); sql.append("
			 * (SELECT REFNO,ITEM FROM ITEMMST) C "); sql.append(" where
			 * a.prd_class =b.prd_cls_id and a.Traveler_id ='"+aTravelId+"' and
			 * "); sql.append(" a.refno ='"+aRefno+"' and a.palletid =
			 * '"+aPallet+"' and a.sku=c.item");
			 */

			// Modified for #35550
			// Added by Ranjana To fetch the GTIN2 from table as per the
			// WO0000000471852
			StringBuffer sql = new StringBuffer(
					" select a.TRAYID,a.LOT,A.SKU,C.Refno as SKUUPCCODE,a.QTY,a.SKU_DESC,a.TRAVELER_ID,a.PALLETID,a.DESTN, ");
			sql
					.append(" b.prd_grp_id,a.LOTTYPE,a.FULLTRAY,(select count(a1.trayid) from ");
			sql
					.append("(Select  refno,lot,traveler_id, prd_class as prd_class,TRAYID,SKU,SUM(TRAYLABLEQTY)");
			sql.append(" as QTY,SKU_DESC,PALLETID,LOTTYPE,FULLTRAY,");
			sql
					.append("(Select distinct destination from rules_mst where ship_to in( select country  from ob_travel_hdr ");
			sql.append(" where  traveler_id = '" + aTravelId
					+ "'  )) as DESTN from  ob_travel_det ");
			sql.append(" where refno ='" + aRefno + "' and palletid = '"
					+ aPallet + "' and Traveler_id ='" + aTravelId + "'");
			sql.append(" and traystatus ='C'");
			sql
					.append(" group by refno,lot,TRAYID,SKU,SKU_DESC,TRAVELER_ID,PALLETID,prd_class,LOTTYPE,FULLTRAY) a1,");
			sql
					.append("(Select prd_grp_id,prd_cls_id from  prd_class_mst) b1,(SELECT REFNO,ITEM FROM ITEMMST) C1");
			sql
					.append(" where a1.prd_class =b1.prd_cls_id and a1.Traveler_id ='"
							+ aTravelId + "' and");
			sql.append(" a1.refno ='" + aRefno + "'  and a1.palletid = '"
					+ aPallet + "'  and a1.sku=c1.item");
			sql.append(" and a1.trayid = a.trayid");
			sql.append(" group by a1.trayid");
			
			// Added by Ranjana To fetch the GTIN2 from table as per the
			// WO0000000471852
			
			sql.append(") as tray_cnt, C.GTIN2 ");
			sql.append(" from");
			sql
					.append(" (Select  refno,lot,traveler_id, prd_class as prd_class,TRAYID,SKU,SUM(TRAYLABLEQTY) as QTY,SKU_DESC,PALLETID,LOTTYPE,FULLTRAY, ");
			sql
					.append(" (Select distinct destination from rules_mst where ship_to in( select country  from ob_travel_hdr ");
			sql.append(" where  traveler_id = '" + aTravelId
					+ "'  )) as DESTN  ");
			sql.append(" from  ob_travel_det where refno ='" + aRefno
					+ "' and palletid = '" + aPallet + "'  ");
			sql.append(" and Traveler_id ='" + aTravelId
					+ "' and traystatus ='C'   ");
			sql
					.append(" group by refno,lot,TRAYID,SKU,SKU_DESC,TRAVELER_ID,PALLETID,prd_class,LOTTYPE,FULLTRAY) a,");
			// sql.append(" )a, ");
			sql
					.append(" (Select prd_grp_id,prd_cls_id from  prd_class_mst) b,");
			
			// Added by Ranjana To fetch the GTIN2 from table as per the
			// WO0000000471852
			
			sql.append(" (SELECT REFNO,ITEM,GTIN2  FROM ITEMMST) C ");
			sql.append(" where a.prd_class =b.prd_cls_id and a.Traveler_id ='"
					+ aTravelId + "'  and ");
			sql.append(" a.refno ='" + aRefno + "'  and a.palletid = '"
					+ aPallet + "'  and a.sku=c.item");

			MLogger.query(sql.toString());

			al = selectData(con, sql.toString());

		}

		catch (Exception e) {
			MLogger.exception(this, e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		MLogger.log(-1, this.getClass() + " getTrayListForPallet()");
		return al;

	}

	public ArrayList getPalletGroupForTrayListPallet(String aRefno,
			String aTravelId, String aPallet, String aPrdClass)
			throws Exception {
		MLogger.log(1, this.getClass() + " getTrayListForPallet()");
		ArrayList al = new ArrayList();
		// connection
		java.sql.Connection con = null;
		try {
			con = com.murho.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(
					"select distinct(A.prd_grp_id) from prd_class_mst a,ob_travel_det b where a.prd_cls_id=b.prd_class ");
			sql.append(" and  b.Traveler_id ='" + aTravelId
					+ "' and b.refno ='" + aRefno + "' and b.palletid = '"
					+ aPallet + "'");

			// StringBuffer sql = new StringBuffer("select distinct(prd_grp_id)
			// from prd_class_mst where prd_cls_id='"+aPrdClass+"' ");

			MLogger.query(sql.toString());

			al = selectData(con, sql.toString());

		} catch (Exception e) {
			MLogger.exception(this, e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		MLogger.log(-1, this.getClass() + "getPalletGroupForTrayListPallet()");
		return al;

	}

	public ArrayList getSkuListForTray(String TrayId) throws Exception {
		MLogger.log(1, this.getClass() + " getSkuListForTray()");
		ArrayList al = new ArrayList();
		// connection
		java.sql.Connection con = null;
		try {
			con = com.murho.gates.DbBean.getConnection();
			
			StringBuffer sql = new StringBuffer(
					" select TRAYID,SKU,LOT,QTY,SKU_DESC,TRAVELER_ID,PALLETID,(Select distinct destination from rules_mst where ship_to in( select country  from ob_travel_hdr where  Traveler_id =a.Traveler_id  and trayid ='"
							+ TrayId + "' )) as DESTN ");
			sql.append(" ,LOTTYPE,FULLTRAY from  ob_travel_det a  ");
			sql.append(" where  trayid = '" + TrayId
					+ "' and traystatus ='C'  ");
			
			MLogger.query(sql.toString());

			al = selectData(con, sql.toString());

		} catch (Exception e) {
			MLogger.exception(this, e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return al;

	}

	/*
	 * public Map getTrayId(String aTravelId,String aPallet,String aMtid) throws
	 * Exception { MLogger.log(1, this.getClass() + " getTrayId()"); Map
	 * map=null; // connection java.sql.Connection con=null; try {
	 * con=com.murho.gates.DbBean.getConnection(); //query
	 * 
	 * StringBuffer sql = new StringBuffer(" select distinct TrayId from
	 * OB_travel_det "); sql.append(" where Traveler_id ='"+aTravelId+"' and
	 * mtid = '"+aMtid+"' ");//(mtid = '"+aMtid+"' or sino ='"+aMtid+"') ");
	 * sql.append(" and palletId ='"+aPallet+"' and traystatus ='N' order by
	 * TrayId desc");
	 * 
	 * 
	 * MLogger.query(sql.toString());
	 * 
	 * map= getRowOfData(con,sql.toString()); } catch(Exception e) {
	 * MLogger.exception(this,e); throw e; } finally{ if (con != null) {
	 * DbBean.closeConnection(con); } } MLogger.log( -1, this.getClass() + "
	 * getTrayId()"); return map; }
	 */

	// CCODE-03
	// Changed By Samatha for URSA-29 on Jan 19 2010
	public Map getTrayId(String aTravelId, String aPallet, String aMtid)
			throws Exception {
		MLogger.log(1, this.getClass() + " getTrayId()");
		Map map = null;
		// connection
		java.sql.Connection con = null;
		try {
			con = com.murho.gates.DbBean.getConnection();
			// query

			StringBuffer sql = new StringBuffer(
					" select distinct DummyTrayId from OB_travel_det  ");
			sql.append("  where Traveler_id ='" + aTravelId + "' and  mtid = '"
					+ aMtid + "' ");// (mtid = '"+aMtid+"' or sino ='"+aMtid+"')
			// ");
			sql.append("  and palletId ='" + aPallet
					+ "'  and traystatus ='N' order by DummyTrayId desc");

			MLogger.query(sql.toString());

			map = getRowOfData(con, sql.toString());

		} catch (Exception e) {
			MLogger.exception(this, e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		MLogger.log(-1, this.getClass() + " getTrayId()");
		return map;

	}

	/*
	 * public Map getMtidDetails(String aTravelId,String aPallet,String
	 * aMtid,String TrayId) throws Exception { MLogger.log(1, this.getClass() + "
	 * getMtidDetails()"); Map map=null; // connection java.sql.Connection
	 * con=null; try { con=com.murho.gates.DbBean.getConnection(); //query
	 * 
	 * StringBuffer sql = new StringBuffer(" select distinct SINO,
	 * MTID,SKU,LOT,(select sum(PICKQTY) from ob_travel_det where Traveler_id
	 * ='"+aTravelId+"' and mtid='"+aMtid+"' and trayid ='"+TrayId+"' group by
	 * mtid) as QTY,FULLTRAY,LOTTYPE,SKU_DESC ,(Select distinct destination from
	 * rules_mst where ship_to in( select country from ob_travel_hdr where
	 * Traveler_id =a.Traveler_id)) as DESTN from OB_TRAVEL_DET a where
	 * Traveler_id ='"+aTravelId+"' "); sql.append( " and mtid = '"+aMtid+"' and
	 * trayid ='"+TrayId+"' and traystatus <>'C' ");
	 * 
	 * MLogger.query(sql.toString());
	 * 
	 * map= getRowOfData(con,sql.toString()); } catch(Exception e) { //
	 * MLogger.info("######################### Exception :: getMtidDetails() :
	 * ######################### \n"); MLogger.exception(this,e); //
	 * MLogger.info("######################### Exception :: getMtidDetails() :
	 * ######################### \n"); // MLogger.log(-1,""); throw e; }
	 * finally{ if (con != null) { DbBean.closeConnection(con); } } MLogger.log(
	 * -1, this.getClass() + " getMtidDetails()"); return map; }
	 */

	// Changed By Samatha for URSA-29 on Jan 19 2010
	public Map getMtidDetails(String aTravelId, String aPallet, String aMtid,
			String TrayId) throws Exception {
		MLogger.log(1, this.getClass() + " getMtidDetails()");
		Map maplot = null;
		Map map = null;
		// connection
		java.sql.Connection con = null;
		try {
			con = com.murho.gates.DbBean.getConnection();
			// query

			// Modified By Samatha to retrive COO from COO_CNFG_TBL on Feb 01
			/*
			 * StringBuffer sql = new StringBuffer(" select distinct SINO,
			 * MTID,SKU,LOT,(select sum(PICKQTY) from ob_travel_det where
			 * Traveler_id ='"+aTravelId+"' and mtid='"+aMtid+"' and Dummytrayid
			 * ='"+TrayId+"' group by mtid) as QTY,FULLTRAY,LOTTYPE,SKU_DESC
			 * ,(Select distinct destination from rules_mst where ship_to in(
			 * select country from ob_travel_hdr where Traveler_id
			 * =a.Traveler_id)) as DESTN from OB_TRAVEL_DET a where Traveler_id
			 * ='"+aTravelId+"' "); sql.append( " and mtid = '"+aMtid+"' and
			 * Dummytrayid ='"+TrayId+"' and traystatus <>'C' ");
			 */
			// int lotlen=Lot.length();
			StringBuffer sqllot = new StringBuffer(
					" select distinct LOT from OB_TRAVEL_DET   where Traveler_id ='"
							+ aTravelId + "' ");
			sqllot.append("  and  mtid = '" + aMtid + "'  and Dummytrayid ='"
					+ TrayId + "' and traystatus <>'C' ");
			MLogger.query(sqllot.toString());
			maplot = getRowOfData(con, sqllot.toString());

			String lot = (String) maplot.get("LOT");
			String strBatam = "BATAM";
			int lenlot = lot.length();

			if (lenlot == 7) {
				// updated the below query on 11-feb-2015 to provide sum of
				// order qty for an MTID for ticket #WO0000000202454
				// StringBuffer sql = new StringBuffer(" select distinct SINO,
				// MTID,SKU,LOT,(select coo from COO_CNFG_TBL where
				// LOTSTARTWITH='"+ strBatam +"') AS COO,(select sum(PICKQTY)
				// from ob_travel_det where Traveler_id ='"+aTravelId+"' and
				// mtid='"+aMtid+"' and Dummytrayid ='"+TrayId+"' group by mtid)
				// as QTY,FULLTRAY,LOTTYPE,SKU_DESC ,(Select distinct
				// destination from rules_mst where ship_to in( select country
				// from ob_travel_hdr where Traveler_id =a.Traveler_id)) as
				// DESTN,");
				StringBuffer sql = new StringBuffer(
						" select distinct SINO, MTID,SKU,LOT,(select coo from COO_CNFG_TBL where LOTSTARTWITH='"
								+ strBatam
								+ "') AS COO,(select sum(QTY) from ob_travel_det  where Traveler_id ='"
								+ aTravelId
								+ "' and mtid='"
								+ aMtid
								+ "' and Dummytrayid ='"
								+ TrayId
								+ "' group by mtid) as QTY,FULLTRAY,LOTTYPE,SKU_DESC ,(Select distinct destination from rules_mst where ship_to in( select country  from ob_travel_hdr where  Traveler_id =a.Traveler_id)) as DESTN,");
				sql
						.append("(select A.prd_grp_id from prd_class_mst a,ob_travel_det b where a.prd_cls_id=b.prd_class and  b.Traveler_id ='"
								+ aTravelId
								+ "' and b.mtid='"
								+ aMtid
								+ "' and b.Dummytrayid ='"
								+ TrayId
								+ "') PRDGRPID");
				sql.append("  from OB_TRAVEL_DET a  where Traveler_id ='"
						+ aTravelId + "' ");
				// StringBuffer sql = new StringBuffer(" select distinct SINO,
				// MTID,SKU,LOT,BATAM AS COO,(select sum(PICKQTY) from
				// ob_travel_det where Traveler_id ='"+aTravelId+"' and
				// mtid='"+aMtid+"' and Dummytrayid ='"+TrayId+"' group by mtid)
				// as QTY,FULLTRAY,LOTTYPE,SKU_DESC ,(Select distinct
				// destination from rules_mst where ship_to in( select country
				// from ob_travel_hdr where Traveler_id =a.Traveler_id)) as
				// DESTN from OB_TRAVEL_DET a where Traveler_id ='"+aTravelId+"'
				// ");
				sql.append("  and  mtid = '" + aMtid + "'  and Dummytrayid ='"
						+ TrayId + "' and traystatus <>'C' ");
				MLogger.query(sql.toString());
				map = getRowOfData(con, sql.toString());
			} else {
				// updated the below query on 11-feb-2015 to provide sum of
				// order qty for an MTID for ticket #WO0000000202454.
				// StringBuffer sql = new StringBuffer("select distinct SINO,
				// MTID,SKU,LOT,(select coo from COO_CNFG_TBL where
				// LOTSTARTWITH=SUBSTRING(a.LOT,1,1)) AS COO,(select
				// sum(PICKQTY) from ob_travel_det where Traveler_id
				// ='"+aTravelId+"' and mtid='"+aMtid+"' and Dummytrayid
				// ='"+TrayId+"' group by mtid) as QTY,FULLTRAY,LOTTYPE,SKU_DESC
				// ,(Select distinct destination from rules_mst where ship_to
				// in( select country from ob_travel_hdr where Traveler_id
				// =a.Traveler_id)) as DESTN,");
				StringBuffer sql = new StringBuffer(
						"select distinct SINO, MTID,SKU,LOT,(select coo from COO_CNFG_TBL where LOTSTARTWITH=SUBSTRING(a.LOT,1,1)) AS COO,(select sum(QTY) from ob_travel_det  where Traveler_id ='"
								+ aTravelId
								+ "' and mtid='"
								+ aMtid
								+ "' and Dummytrayid ='"
								+ TrayId
								+ "' group by mtid) as QTY,FULLTRAY,LOTTYPE,SKU_DESC ,(Select distinct destination from rules_mst where ship_to in( select country  from ob_travel_hdr where  Traveler_id =a.Traveler_id)) as DESTN,");
				sql
						.append("(select A.prd_grp_id from prd_class_mst a,ob_travel_det b where a.prd_cls_id=b.prd_class and  b.Traveler_id ='"
								+ aTravelId
								+ "' and b.mtid='"
								+ aMtid
								+ "' and b.Dummytrayid ='"
								+ TrayId
								+ "')PRDGRPID");
				sql.append("  from OB_TRAVEL_DET a  where Traveler_id ='"
						+ aTravelId + "' ");
				sql.append("  and  mtid = '" + aMtid + "'  and Dummytrayid ='"
						+ TrayId + "' and traystatus <>'C' ");
				MLogger.query(sql.toString());
				map = getRowOfData(con, sql.toString());
			}

		} catch (Exception e) {
			// MLogger.info("######################### Exception ::
			// getMtidDetails() : ######################### \n");
			MLogger.exception(this, e);
			// MLogger.info("######################### Exception ::
			// getMtidDetails() : ######################### \n");
			// MLogger.log(-1,"");
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		MLogger.log(-1, this.getClass() + " getMtidDetails()");
		return map;

	}

	public ArrayList getTrayLabelSummary(String aPlant, String travelId,
			String pallet) {
		MLogger.log(1, this.getClass() + " getTrayLabelSummary()");
		ArrayList arrList = new ArrayList();
		try {
			InvMstDAO invdao = new InvMstDAO();
			Hashtable ht = new Hashtable();
			MLogger.log(0, "travelID" + travelId);

			StringBuffer sql = new StringBuffer(
					/*
					 * Commented the below code for 6.0 requirement
					 * " select traveler_id,pallet as pallet_id,palletid as pallet,count(trayid) as nooftray,palletstatus ");
					 */
					
			" select traveler_id,pallet as pallet_id,palletid as pallet,count(trayid) as nooftray,palletstatus ");

			sql.append(" from ob_travel_det where traveler <> '' ");

			if (travelId.length() > 0)

				sql.append(" and traveler_id='" + travelId + "'  ");

			if (pallet.length() > 0)

				sql.append(" and palletid ='" + pallet + "'");

			sql
					.append(" group by pallet, palletid,traveler_id,palletstatus  order by cast(pallet as integer) ");

			MLogger.log(0, "getTrayLabelSummary(aQuery)::" + sql);

			arrList = invdao.selectForReport(sql.toString(), ht);
		} catch (Exception e) {
			MLogger
					.log("Exception :getTrayLabelSummary :: getTrayLabelSummary:"
							+ e.toString());
			MLogger.log(-1, "");
		}
		MLogger.log(-1, this.getClass() + " getTrayLabelSummary()");
		return arrList;
	}

	// Added new mehtod for #1848 by hemant for UAT issue in ver 2.6

	public ArrayList getPrintShipmarkSummary(String aPlant, String travelId,
			String pallet) {
		MLogger.log(1, this.getClass() + " getPrintShipmarkSummary()");
		ArrayList arrList = new ArrayList();
		try {
			InvMstDAO invdao = new InvMstDAO();
			Hashtable ht = new Hashtable();
			MLogger.log(0, "travelID" + travelId);

			StringBuffer sql = new StringBuffer(
					" select traveler_id,pallet,palletid ,count(trayid) as nooftray,max(palletstatus) as palletstatus ");

			sql.append(" from ob_travel_det where traveler <> '' ");

			if (travelId.length() > 0)

				sql.append(" and traveler_id='" + travelId + "'  ");

			if (pallet.length() > 0)

				sql.append(" and palletid ='" + pallet + "'");

			sql
					.append(" group by pallet, palletid,traveler_id  order by cast(pallet as integer) ");

			MLogger.log(0, "getPrintShipmarkSummary(aQuery)::" + sql);

			arrList = invdao.selectForReport(sql.toString(), ht);
		} catch (Exception e) {
			MLogger
					.log("Exception :getPrintShipmarkSummary :: getPrintShipmarkSummary:"
							+ e.toString());
			MLogger.log(-1, "");
		}
		MLogger.log(-1, this.getClass() + " getPrintShipmarkSummary()");
		return arrList;
	}

	// Methdo added by Arun for #1848
	public ArrayList getCurrentPallet(String travelId, String palletId) {
		MLogger.log(1, this.getClass() + " getCurrentPallet()");
		ArrayList arrList = new ArrayList();
		try {
			InvMstDAO invdao = new InvMstDAO();
			Hashtable ht = new Hashtable();
			MLogger.log(0, "travelID" + travelId);

			StringBuffer sql = new StringBuffer(
					" select distinct pallet from ob_travel_det where traveler_id='"
							+ travelId + "' and palletid='" + palletId + "' ");
			System.out.println("Pallet Query*****************************"
					+ sql);
			arrList = invdao.selectForReport(sql.toString(), ht);
		} catch (Exception e) {
			MLogger.log("Exception :getCurrentPallet :: getCurrentPallet:"
					+ e.toString());
			MLogger.log(-1, "");
		}
		MLogger.log(-1, this.getClass() + " getCurrentPallet()");
		return arrList;
	}

	public ArrayList getSummaryList(String travelId, String sku, String lot,
			String user) {
		MLogger.log(1, this.getClass() + " getSummaryList()");
		ArrayList arrList = new ArrayList();
		try {
			InvMstDAO invdao = new InvMstDAO();
			Hashtable ht = new Hashtable();
			MLogger.log(0, "travelID" + travelId);

			StringBuffer sql = new StringBuffer(
					" select distinct traveler_id,cast(pallet as integer) as pallet,sku,lot,");
			// sql.append(" loc,(select sum(qty) from ob_travel_det where
			// traveler_id = a.traveler_id and pallet=a.pallet and sku =a.sku
			// and lot =a.lot and loc=a.loc) as qty ");
			// Check for Isnull for Loc as we are retriving loc from invmst
			// while genrating picker. By samatha on Jan 13 2009
			sql
					.append(" isnull(loc,'') as loc,(select sum(qty) from ob_travel_det where  traveler_id = a.traveler_id and pallet=a.pallet and sku =a.sku and lot =a.lot and isnull(loc,'')=isnull(a.loc,''))  as qty ");
			sql
					.append("  , sum(fulltray) as fulltray,(select sum(partialqty) from ob_travel_det where  traveler_id = a.traveler_id and pallet=a.pallet and sku =a.sku and lot =a.lot and  isnull(loc,'')=isnull(a.loc,'') )  partialqty,isnull(assigneduser,'') as assigneduser from ob_travel_det a ");

			sql.append("  where traveler_id ='" + travelId + "' ");

			if (sku.length() > 0)

				sql.append(" and sku ='" + sku + "'");

			if (lot.length() > 0)

				sql.append(" and lot ='" + lot + "'");

			if (user.length() > 0)

				sql.append(" and assigneduser ='" + user + "'");

			sql
					.append(" group by traveler_id,pallet,sku,lot,loc,assigneduser ");
			sql.append(" order by cast(pallet as integer),loc ");

			MLogger.log(0, "getTrayLabelSummary(aQuery)::" + sql);

			arrList = invdao.selectForReport(sql.toString(), ht);
		} catch (Exception e) {
			MLogger
					.log("Exception :getTrayLabelSummary :: getTrayLabelSummary:"
							+ e.toString());
			MLogger.log(-1, "");
		}
		MLogger.log(-1, this.getClass() + " getTrayLabelSummary()");
		return arrList;
	}

	public ArrayList getJobProgressList(String travelId, String sku,
			String lot, String user) {
		ArrayList arrList = new ArrayList();
		try {
			InvMstDAO invdao = new InvMstDAO();
			Hashtable ht = new Hashtable();
			MLogger.log(0, "travelID" + travelId);

			StringBuffer sql = new StringBuffer(
					" select a.traveler_id as traveler_id,");

			sql.append("  isnull(b.assigneduser,'') as assigneduser,");

			sql
					.append(" isnull((select min(c.crtime) from ob_travel_det c where c.traveler_id=a.traveler_id and c.assigneduser = b.assigneduser),'') as sttime,  ");

			sql
					.append(" isnull((select max(d.crtime) from ob_travel_det d where d.traveler_id=a.traveler_id and d.assigneduser =b.assigneduser),'') as endtime, ");

			sql
					.append(" sum(b.qty) as qty ,sum(pickqty) as pickqty,100-cast( cast((sum(b.qty)-sum(b.pickqty)) as decimal)/cast(sum(b.qty) as decimal)*100 as integer) as picked");

			sql
					.append(" from ob_travel_hdr a,ob_travel_det b  where  a.traveler_id=b.traveler_id  ");

			if (travelId.length() > 0)

				sql.append(" and a.traveler_id='" + travelId + "'  ");

			if (sku.length() > 0)

				sql.append(" and b.sku ='" + sku + "'");

			if (lot.length() > 0)

				sql.append(" and b.lot ='" + lot + "'");

			if (user.length() > 0)

				sql.append(" and a.user ='" + user + "'");

			sql.append(" group by b.assigneduser, a.traveler_id ");

			MLogger.log(0, "getTrayLabelSummary(aQuery)::" + sql);

			arrList = invdao.selectForReport(sql.toString(), ht);
		} catch (Exception e) {
			MLogger
					.log("Exception :getTrayLabelSummary :: getTrayLabelSummary:"
							+ e.toString());
			MLogger.log(-1, "");
		}
		MLogger.log(-1, this.getClass() + " getTrayLabelSummary()");
		return arrList;
	}

	public ArrayList TrayLabelingReport(String aPlant, String travelId,
			String pallet) {
		MLogger.log(1, this.getClass() + " TrayLabelingReport()");
		ArrayList arrList = new ArrayList();
		try {
			InvMstDAO invdao = new InvMstDAO();
			Hashtable ht = new Hashtable();
			MLogger.log(0, "travelID" + travelId);

			/*Query updated by Ranjana under Tray Labeling Report format change for ticket .
			 * StringBuffer sql = new StringBuffer(
					" select traveler_id,palletid as pallet,trayid,mtid,sku,isnull(pickqty,0) as pickqty,isnull(traylableqty,0) as traylableqty,status");
			*/
			StringBuffer sql = new StringBuffer(
			" select traveler_id,palletid as pallet,trayid,mtid,lot,isnull(pickqty,0) as pickqty,isnull(traylableqty,0) as traylableqty,palletstatus");

			sql.append(" from ob_travel_det where traveler <> '' ");

			if (travelId.length() > 0)

				sql.append(" and traveler_id='" + travelId + "'  ");

			if (pallet.length() > 0)

				sql.append(" and palletid ='" + pallet + "'");

			MLogger.log(0, "TrayLabelingReport(aQuery)::" + sql);

			arrList = invdao.selectForReport(sql.toString(), ht);
		} catch (Exception e) {
			MLogger.log("Exception :TrayLabelingReport " + e.toString());
			MLogger.log(-1, "");
		}
		MLogger.log(-1, this.getClass() + " TrayLabelingReport()");
		return arrList;
	}
	
	
	/*Method updated by Ranjana 
	 * Purpose: To add the functionality of filters.
	 * 	public ArrayList TrayDescrepencyReport(String aPlant, String travelId) */	
//	Commented to remove the filter condition of LOT and SKU from discrepancy report
	public ArrayList TrayDescrepencyReport(String aPlant, String travelId,
			String lot, String sku){
		MLogger.log(1, this.getClass() + " TrayDescrepencyReport()");
		ArrayList arrList = new ArrayList();
		try {
			InvMstDAO invdao = new InvMstDAO();
			Hashtable ht = new Hashtable();
			MLogger.log(0, "travelID" + travelId);
			
			/* StringBuffer sql =
			 * new StringBuffer(" select
			 * traveler,pallet,traveler_id,sku,lot,loc,sum(qty) as
			 * qty,sum(pickqty) as pickqty,sum(traylableqty) as traylableqty,
			 * "); sql.append(" sum(qty)- sum(pickqty) as pickdiff,sum(pickqty)-
			 * sum(traylableqty) as traydifffrom ob_travel_det "); sql.append("
			 * where traveler_id='"+travelId+"' and status = 'C' group by
			 * traveler,pallet,traveler_id,sku,lot,loc having sum(qty)-
			 * sum(pickqty) > 0 or sum(pickqty)- sum(traylableqty) > 0 order by
			 * cast(pallet as integer) ");
			 * 
			 */
			
			/* Updated by Ranjana for the changes of LOT Restriction*/
			
			StringBuffer sql = new StringBuffer(
					" select D.traveler,D.pallet,D.traveler_id,D.sku,D.lot,D.loc,sum(D.qty) as qty,sum(D.pickqty) as pickqty,sum(D.traylableqty) as traylableqty,  ");
			sql
					.append(" sum(D.qty)- sum(D.pickqty) as pickdiff,sum(D.pickqty)- sum(D.traylableqty) as traydiff,(CASE WHEN L.STATUS ='BLOCK' THEN L.STATUS ELSE '' END )AS BlockStatus,(CASE WHEN L.STATUS ='BLOCK' THEN L.REASON ELSE '' END )AS BlockReason ");
			sql
					.append("from ob_travel_det D LEFT OUTER JOIN TBL_LOTRESTRICTION L ON D.LOT=L.LOT");
			sql.append(" where D.traveler_id='" + travelId + "' AND D.LOT = CASE WHEN '" + lot + "' <> '' THEN '"
					+ lot + "' ELSE D.LOT END  AND  D.SKU  = CASE WHEN '" + sku
					+ "' <> ''  THEN '" + sku + "' ELSE D.SKU END ");
			sql
					.append("and  D.status = 'C' group by D.traveler,D.pallet,D.traveler_id,D.sku,D.lot,D.loc,L.STATUS,L.REASON having  sum(D.qty)- sum(D.pickqty) > 0 or sum(D.pickqty)- sum(D.traylableqty) > 0  ");
			sql.append("or (L.STATUS ='BLOCK') order by cast(D.pallet as integer) ");

			arrList = invdao.selectForReport(sql.toString(), ht);
		} catch (Exception e) {
			MLogger.log("Exception :TrayDescrepencyReport " + e.toString());
			MLogger.log(-1, "");
		}
		MLogger.log(-1, this.getClass() + " TrayDescrepencyReport()");
		return arrList;
	}

	public ArrayList getPalletDetails(String traveler, String pallet) {
		MLogger.log(1, this.getClass() + " getPalletDetails()");
		ArrayList arrList = new ArrayList();
		try {
			InvMstDAO invdao = new InvMstDAO();
			Hashtable ht = new Hashtable();
			MLogger.log(0, "travelID" + traveler);

			StringBuffer sql = new StringBuffer(
					" select palletid as pallet,trayid,sum(qty) as qty,palletstatus ");

			sql.append(" from ob_travel_det where traveler_id='" + traveler
					+ "' and palletid='" + pallet
					+ "' group by  palletid,trayid,palletstatus");

			MLogger.log(0, "getPalletDetails(aQuery)::" + sql);

			arrList = invdao.selectForReport(sql.toString(), ht);
		} catch (Exception e) {
			MLogger.log("Exception :getPalletDetails :::" + e.toString());
			MLogger.log(-1, "");
		}
		MLogger.log(-1, this.getClass() + " getPalletDetails()");
		return arrList;
	}

	public ArrayList getTrayDetails(String traveler, String trayid) {
		MLogger.log(1, this.getClass() + " getTrayDetails()");
		ArrayList arrList = new ArrayList();
		try {
			InvMstDAO invdao = new InvMstDAO();
			Hashtable ht = new Hashtable();
			MLogger.log(0, "travelID" + traveler);

			StringBuffer sql = new StringBuffer(
					" select mtid,sku,lot,qty,traystatus ");

			sql.append(" from ob_travel_det where traveler_id='" + traveler
					+ "' and trayid='" + trayid + "' ");

			MLogger.log(0, "getTrayLabelSummary(aQuery)::" + sql);

			arrList = invdao.selectForReport(sql.toString(), ht);
		} catch (Exception e) {
			MLogger.log("Exception :getTrayDetails :::" + e.toString());
		}
		MLogger.log(-1, this.getClass() + " getTrayDetails()");
		return arrList;
	}

	public boolean updateTrayQuantity(String query, Hashtable htCondition,
			String extCond) throws Exception {
		MLogger.log(1, this.getClass() + " updateInv()");
		boolean flag = false;
		java.sql.Connection con = null;
		// connection
		try {
			con = com.murho.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" UPDATE " + TABLE_NAME);
			sql.append(" ");
			sql.append(query);
			sql.append(" WHERE  " + formCondition(htCondition));

			if (extCond.length() != 0) {
				sql.append(extCond);
			}
			flag = updateData(con, sql.toString());
		} catch (Exception e) {
			MLogger
					.log(
							0,
							"######################### Exception :: updateInv() : ######################### \n");
			MLogger.log(0, "" + e.getMessage());
			MLogger
					.log(
							0,
							"######################### Exception :: updateInv() : ######################### \n");
			MLogger.log(-1, "");
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;

	}

	public boolean updateUser(String query, Hashtable map, String extCond)
			throws Exception {
		MLogger.log(1, this.getClass() + " update()");
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			// connection
			con = com.murho.gates.DbBean.getConnection();

			// inputs
			StringBuffer sql = new StringBuffer(" UPDATE " + TABLE_NAME);
			sql.append(" ");
			sql.append(query);
			sql.append(" WHERE ");

			String conditon = formCondition(map);
			// query

			sql.append(conditon);

			if (extCond.length() != 0) {

				sql.append(extCond);
			}

			MLogger.query(" " + sql.toString());

			flag = updateData(con, sql.toString());
		} catch (Exception e) {
			MLogger
					.log(
							0,
							"######################### Exception :: update() : ######################### \n");
			MLogger.log(0, "" + e.getMessage());
			MLogger
					.log(
							0,
							"######################### Exception :: update() : ######################### \n");
			MLogger.log(-1, "");
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;

	}

	public boolean RemoveTraveler(String travelId, String user_id,
			String TRN_DATE) throws Exception {
		boolean deleted = false;
		String Query = "";
		// String sepratedtoken1="";
		BaseDAO _BaseDAO = new BaseDAO();
		java.sql.Connection con = null;
		java.sql.Connection con1 = null;
		// Map mp = null;mp = new HashMap();
		try {

			con1 = DbBean.getConnection();
			con = DbBean.getConnection();
			Query = "delete from ob_travel_hdr where traveler_id='" + travelId
					+ "'  ";
			deleted = _BaseDAO.DeleteRow(con1, Query);
			Query = "delete from ob_travel_det where traveler_id='" + travelId
					+ "'  ";
			deleted = _BaseDAO.DeleteRow(con, Query);

			if (deleted) {
				Map mp1 = null;
				mp1 = new HashMap();
				mp1.put(MDbConstant.PLANT, CibaConstants.cibacompanyName);
				mp1.put(MDbConstant.TRAVELER_NUM, travelId);
				mp1.put(MDbConstant.MTID, "");
				mp1.put(MDbConstant.ITEM, "");
				mp1.put(MDbConstant.LOT_NUM, "");
				mp1.put(MDbConstant.PALLET, "");
				mp1.put(MDbConstant.MOVHIS_QTY, "");
				mp1.put("CRBY", user_id);
				mp1.put("CRAT", TRN_DATE);
				mp1.put(MDbConstant.MOVHIS_REF_NUM, "REMOVE_CON_TRAVELER");
				mp1.put("CRTIME", DateUtils.Time());
				mp1.put("REMARK", "");
				mp1.put("QTY", "0");
				deleted = processMoveHis(mp1);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
				DbBean.closeConnection(con1);
			}
		}
		return deleted;
	}

	private boolean processMoveHis(Map mp) throws Exception {
		boolean flag = false;
		// TblControlDAO tblConDao=new TblControlDAO();

		MovHisDAO movHisDao = new MovHisDAO();
		try {
			MLogger.log(0, "Getting next seq no .");
			Hashtable htRecvHis = new Hashtable();

			htRecvHis.put(MDbConstant.PLANT, CibaConstants.cibacompanyName);
			htRecvHis.put(MDbConstant.TRAVELER_NUM, mp
					.get(MDbConstant.TRAVELER_NUM));
			htRecvHis.put(MDbConstant.PALLET, mp.get(MDbConstant.PALLET));
			htRecvHis.put(MDbConstant.MTID, mp.get(MDbConstant.MTID));
			htRecvHis.put(MDbConstant.ITEM, mp.get(MDbConstant.ITEM));
			htRecvHis.put(MDbConstant.LOT_NUM, mp.get(MDbConstant.LOT_NUM));
			htRecvHis.put(MDbConstant.LOGIN_USER, mp
					.get(MDbConstant.LOGIN_USER));
			htRecvHis.put("CRAT", mp.get(MDbConstant.MOVEHIS_CR_DATE));
			htRecvHis.put("MOVTID", mp.get(MDbConstant.MOVHIS_REF_NUM));
			htRecvHis.put("CRTIME", mp.get(MDbConstant.CRTIME));
			htRecvHis.put("REMARK", mp.get(MDbConstant.REMARK));
			htRecvHis.put(MDbConstant.MOVHIS_QTY, mp
					.get(MDbConstant.MOVHIS_QTY));
			flag = movHisDao.insertIntoMovHis(htRecvHis);
		} catch (Exception e) {
			MLogger.exception(this, e);
			throw e;
		}
		return flag;
	}

	public String getTotalTraysPerPallet(String travelId) {
		MLogger.log(1, this.getClass() + " getTotalTraysPerPallet()");
		Map m = new HashMap();
		String trayCnt = "";
		try {

			Hashtable ht = new Hashtable();
			ht.put("TRAVELER_ID", travelId);
			// StringBuffer sql = new StringBuffer(" count(distinct trayid) as
			// CNTTRAY ");
			// Modified By Samatha on Jan 12 2009 since we don't genearte TrayId
			// while Generating Picklist and heep reference of trays in TrayID
			StringBuffer sql = new StringBuffer(
					"     count(distinct Dummytrayid) as CNTTRAY    ");
			MLogger.log(0, "getTotalTraysPerPallet(aQuery)::" + sql);

			m = selectRow(sql.toString(), ht);
			trayCnt = (String) m.get("CNTTRAY");
		} catch (Exception e) {
			MLogger
					.log("Exception :getTotalTraysPerPallet :: getTotalTraysPerPallet:"
							+ e.toString());
			MLogger.log(-1, "");
		}
		MLogger.log(-1, this.getClass() + " getTotalTraysPerPallet()");
		return trayCnt;
	}

	public String getSumOfLotQty(Map mp) throws Exception {
		MLogger.log(1, this.getClass() + " getSumOfLotQty()");
		String costMethod = "";
		String query = " sum(QTY) as QTY ";
		// condintion
		Hashtable ht = new Hashtable();

		ht.put("Traveler_id", mp.get(MDbConstant.TRAVELER_NUM));
		ht.put("LOT", mp.get(MDbConstant.LOT));
		ht.put("PALLET", mp.get(MDbConstant.PALLET));
		ht.put("STATUS", "N");

		Map m = selectRow(query, ht);

		costMethod = (String) m.get("QTY");

		MLogger.info("getSumOfLotQty : " + costMethod);

		MLogger.log(-1, this.getClass() + " getSumOfLotQty()");

		return costMethod;

	}

	public String getCountofMultiLot(String travelId) {
		MLogger.log(1, this.getClass() + " getCountofMultiLot()");
		Map m = new HashMap();
		String trayCnt = "";
		try {

			Hashtable ht = new Hashtable();
			ht.put("TRAVELER_ID", travelId);
			ht.put("LOTTYPE", "SL");

			MLogger.log(0, "travelID" + travelId);

			StringBuffer sql = new StringBuffer("  count(*) as mlcount ");

			MLogger.log(0, "getCountofMultiLot(aQuery)::" + sql);

			m = selectRow(sql.toString(), ht);
			trayCnt = (String) m.get("mlcount");
		} catch (Exception e) {
			MLogger
					.log("Exception :getTotalTraysPerPallet :: getTotalTraysPerPallet:"
							+ e.toString());
			MLogger.log(-1, "");
		}
		MLogger.log(-1, this.getClass() + " getTotalTraysPerPallet()");
		return trayCnt;
	}

	public ArrayList getPickerListHistory(String refno, String DeliveryNo) {
		MLogger.log(1, this.getClass() + " getPickerListHistory()");
		ArrayList arrList = new ArrayList();
		try {

			InvMstDAO invdao = new InvMstDAO();
			Hashtable ht = new Hashtable();

			StringBuffer sql = new StringBuffer(
					" SELECT a.TRAVELER,b.TRAVELER_ID,SO, ");

			sql
					.append("  SOLINE,RELEASE,SUM(CAST(a.QTY AS Integer)) AS QTY,MAX(DESTINATION) AS  FIELD11,CUSTDO ");

			sql
					.append(" FROM PICKER_LIST_HISTORY a,ob_travel_hdr b WHERE a.traveler =b.traveler and a.REFNO =b.refno  and a.REFNO like'"
							+ refno
							+ "%' and a.Traveler like'"
							+ DeliveryNo
							+ "%' ");

			sql
					.append("  GROUP BY a.TRAVELER,b.traveler_id,SO,CUSTDO,SOLINE,RELEASE ORDER BY cast(a.TRAVELER as bigint)");

			MLogger.log(0, "getPickerListHistory(aQuery)::" + sql);

			arrList = invdao.selectForReport(sql.toString(), ht);
		} catch (Exception e) {
			MLogger.log("Exception :getPickerListHistory ::" + e.toString());
			MLogger.log(-1, "");
		}
		MLogger.log(-1, this.getClass() + " getPickerListHistory()");
		return arrList;
	}

	public ArrayList getTravelerView() {
		MLogger.log(1, this.getClass() + " getTravelerView()");
		ArrayList arrList = new ArrayList();
		try {
			InvMstDAO invdao = new InvMstDAO();
			Hashtable ht = new Hashtable();

			StringBuffer sql = new StringBuffer("SELECT TRAVELER,");

			sql
					.append(" SUM(CAST(QTY AS integer)) AS QTY,MAX(DESTINATION) AS  FIELD11");

			sql
					.append(" FROM TEMPDATATABLE  GROUP BY TRAVELER order by  TRAVELER ");

			MLogger.log(0, "getReceiveDetails(aQuery)::" + sql);

			arrList = invdao.selectForReport(sql.toString(), ht);
		} catch (Exception e) {
			MLogger.log("Exception :getTravelerView ::" + e.toString());
			MLogger.log(-1, "");
		}
		MLogger.log(-1, this.getClass() + " getTravelerView()");
		return arrList;
	}

	public ArrayList getTravelerViewByTraveler(String Traveler) {
		MLogger.log(1, this.getClass() + " getTravelerViewByTraveler()");
		ArrayList arrList = new ArrayList();
		try {
			InvMstDAO invdao = new InvMstDAO();
			Hashtable ht = new Hashtable();

			// StringBuffer sql = new StringBuffer("SELECT TRAVELER,");

			// sql.append(" SOLINE,RELEASE,SUM(CAST(QTY AS integer)) AS
			// QTY,MAX(DESTINATION) AS FIELD11");

			// sql.append(" FROM TEMPDATATABLE WHERE TRAVELER like
			// '"+Traveler+"%' GROUP BY TRAVELER,SO,SOLINE,RELEASE order by
			// cast(TRAVELER as integer) ");
			StringBuffer sql = new StringBuffer("SELECT TRAVELER,");

			sql
					.append(" SUM(CAST(QTY AS integer)) AS QTY,MAX(DESTINATION) AS  FIELD11");

			sql.append(" FROM TEMPDATATABLE WHERE TRAVELER like '" + Traveler
					+ "%'  GROUP BY TRAVELER ORDER BY TRAVELER  ");//

			MLogger.log(0, "getTravelerViewByTraveler(aQuery)::" + sql);

			arrList = invdao.selectForReport(sql.toString(), ht);
		} catch (Exception e) {
			MLogger.log("Exception :getTravelerViewByTraveler ::"
					+ e.toString());
			MLogger.log(-1, "");
		}
		MLogger.log(-1, this.getClass() + " getTravelerView()");
		return arrList;
	}

	public boolean Call_Proc_Confirm(String mtid) throws Exception {
		MLogger.log(1, this.getClass() + " Proc_PickListGeneration_new_3()");
		boolean flag = false;
		Connection con = null;
		try {
			// connection
			con = DbBean.getConnection();
			Hashtable ht = new Hashtable();
			ht.put("FUNC", "PLTID");
			ht.put("RECSTAT", "N");
			TblControlDAO tblDao = new TblControlDAO();
			boolean isPresent = tblDao.isExists(ht);
			if (!isPresent) {
				throw new Exception(
						"Please Create PLTID/TRAYID FUNC in TblControl Master to Confirm as Per SAP format");
			}
			CallableStatement colStmt = null;
			String sp = "";
			// sp = "exec Proc_PickerListGeneration_new_3 '' "; //Commented on
			// 20091218 due to Change PickerListSp
			sp = "exec Proc_IRO_CONFIRM '' ";
			System.out.println("Executing procedure : " + sp);
			colStmt = con.prepareCall(sp);

			int iCnt = 0;

			try {
				iCnt = colStmt.executeUpdate();
				MLogger.info("Proc_IRO_CONFIRM  : " + iCnt);

				if (iCnt > 0) {
					flag = true;

				} else {
					flag = false;
				}

			} catch (Exception e) {

				MLogger.exception(this, e);
				throw e;
			}

		} catch (Exception e) {
			MLogger
					.log(
							0,
							"######################### Exception :: Call_Proc_Confirm() : ######################### \n");
			MLogger.log(0, "" + e.getMessage());
			MLogger
					.log(
							0,
							"######################### Exception :: Call_Proc_Confirm() : ######################### \n");
			MLogger.log(-1, "");
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		MLogger.log(-1, this.getClass() + " Call_Proc_Confirm()");

		return flag;

	}

	public ArrayList get_Qc_ReportDetails(String aPlant, String travelId) {
		MLogger.log(1, this.getClass() + " get_Qc_ReportDetails()");
		ArrayList arrList = new ArrayList();
		try {
			InvMstDAO invdao = new InvMstDAO();
			Hashtable ht = new Hashtable();
			MLogger.log(0, "travelID" + travelId);

			StringBuffer sql = new StringBuffer("SELECT ");

			sql
					.append("  trayid,DummyTrayID,sku,sku_desc,lot,expdate,custpo,isnull(traylableqty,0) as qty,pallet,palletid,traveler_id as traveler,sino,substring(expdate,1,2) as mm ,substring(expdate,9,2) as yy,isnull(upby,'') as upby ");

			sql.append("FROM ob_travel_det ");

			sql.append(" WHERE TRAVELER_ID='" + travelId
					+ "' order by cast(pallet as integer),trayid ,sku ");

			MLogger.log(0, "getReceiveDetails(aQuery)::" + sql);

			arrList = invdao.selectForReport(sql.toString(), ht);
		} catch (Exception e) {
			MLogger
					.log("Exception :get_Qc_ReportDetails :: get_Qc_ReportDetails:"
							+ e.toString());
			MLogger.log(-1, "");
		}
		MLogger.log(-1, this.getClass() + " get_Qc_ReportDetails()");
		return arrList;
	}

	// CCODE-03
	// By Samatha on Jan 18 2010 for URS A29
	public Map getTrayIdForDummyTrayId(String aTravelId, String aPallet,
			String dummyTrayId) throws Exception {
		MLogger.log(1, this.getClass() + " getTrayIdForDummyTrayId()");
		Map map = null;
		// connection
		java.sql.Connection con = null;
		try {
			con = com.murho.gates.DbBean.getConnection();
			// query

			StringBuffer sql = new StringBuffer(
					" select distinct TrayId from OB_travel_det  ");
			sql.append("  where Traveler_id ='" + aTravelId
					+ "' and  dummyTrayId = '" + dummyTrayId + "' ");// (mtid
			// =
			// '"+aMtid+"'
			// or
			// sino
			// ='"+aMtid+"')
			// ");
			sql.append("  and palletId ='" + aPallet
					+ "'  and traystatus ='N' and trayid <>''");

			MLogger.query(sql.toString());

			map = getRowOfData(con, sql.toString());

		} catch (Exception e) {
			MLogger.exception(this, e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		MLogger.log(-1, this.getClass() + " getTrayId()");
		return map;

	}

	// CCODE-04
	// By Samatha on Jan 18 2010 for URS A-39

	public String getGeneratedTrayId(String Plant) throws Exception {
		MLogger.log(1, getClass() + " getGeneratedTrayId()");
		// StrUtils _strUtils = new StrUtils();
		String genTrayId = "";

		Connection con = null;
		try {
			// ResultSet rs = null;
			con = DbBean.getConnection();
			CallableStatement cstmt = con
					.prepareCall("{call dbo.GENERATE_TRAYID(?,?, ?)}");
			cstmt.setString(1, Plant);
			// cstmt.setString(2, "18");

			cstmt.setString(2, "1"); // We passed 1 as dummy prefix
			// parameter,we have get and process
			// real prefix from sp
			// dbo.GENERATE_TRAYID.
			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
			cstmt.execute();
			System.out.println("GeneratedTrayiD: " + cstmt.getString(3));
			genTrayId = cstmt.getString(3);
			System.out.println("......................trayid" + genTrayId);

		} catch (Exception e) {
			MLogger.log("Exception :GeneratedTrayiD ::" + e.toString());
			throw e;
		} finally {
			if (con != null)
				DbBean.closeConnection(con);
		}

		MLogger.log(-1, getClass() + " getGeneratedTrayId()");
		return genTrayId;
	}

	// Method Added by Arun for #1848
	public int getPendingTLCount(String travelerId, String palletId)
			throws Exception {
		MLogger.log(1, getClass() + " getPendingTLCount()");
		int pendingTLCount = 0;

		Connection con = null;
		try {
			ResultSet rs = null;
			Statement stmt = null;
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT  ");
			sql
					.append("  ((select count(*) from ob_travel_det where traveler_id='"
							+ travelerId
							+ "' and palletid='"
							+ palletId
							+ "')- ");
			sql
					.append("  (select count(*) from ob_travel_det where  traveler_id='"
							+ travelerId
							+ "' and palletid='"
							+ palletId
							+ "' and traystaTus='C')) DIFF");

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			while (rs.next()) {
				pendingTLCount = rs.getInt("DIFF");
			}

			MLogger.query(sql.toString());

		} catch (Exception e) {
			MLogger.log("Exception :getPendingTLCount ::" + e.toString());
			throw e;
		} finally {
			if (con != null)
				DbBean.closeConnection(con);
		}

		MLogger.log(-1, getClass() + " getPendingTLCount()");
		return pendingTLCount;
	}

	// ********************************************************
	// Below function is added by jyoti for TIBCO-INC000002484471(WMS 2.8)
	// This function will return the count of MTIDs in a delivery
	// ********************************************************
	public int getCountofMTIDbyTravelerWise(TransactionDTO trDTO) {
		MLogger.log(1, this.getClass() + " getCountofMTIDbyTravelerWise()");
		int count = 0;
		String travelerID = "";
		try {
			travelerID = trDTO.getTraveler();
			MLogger.log(travelerID);
			SQLTravelDet_DAO _SQLTravelDet_DAO = new SQLTravelDet_DAO();
			TravelDet_DO _TravelDet_DO = new TravelDet_DO();
			TransactionDTO _TransactionDTO = new TransactionDTO();
			_TransactionDTO.setTraveler(travelerID);
			_TravelDet_DO.setTraveler(_TransactionDTO.getTraveler());
			count = _SQLTravelDet_DAO.getMTIDCount(_TravelDet_DO);

		} catch (Exception e) {
			MLogger.log("Exception :getCountofMTIDbyTravelerWise :: "
					+ e.toString());
			MLogger.log(-1, "");
		}
		MLogger.log(-1, this.getClass() + " getCountofMTIDbyTravelerWise()");
		return count;
	}
	
	//	 ********************************************************
	// Below function is added by Ranjana (WMS 6.0)
	// This function is added for RefNo removal from Traylabeling Module
	// ********************************************************
	public ArrayList getTravelerListForRefNo(String aRefNo) throws Exception {
	   MLogger.log(1, this.getClass() + " getTravelerListForRefNo()");
	   ArrayList al=new ArrayList();
	   // connection
	   java.sql.Connection con=null;
	   try
	   {
	    con=com.murho.gates.DbBean.getConnection();
	   //query
	   StringBuffer sql = new StringBuffer(" SELECT ");
	   sql.append(" distinct traveler_id ,isnull(Traveler_Desc,'') as TRAVDESC ");
	   sql.append(" ");
	   sql.append(" FROM " + TABLE_NAME  );
	   sql.append(" WHERE  REFNO = '" + aRefNo + "'   ");
	  
	   MLogger.query(sql.toString());
	       
	   al=selectData(con,sql.toString());
	   
	   }
	   catch(Exception e) {
	  //        MLogger.info("######################### Exception :: getTravelerListForRefNo() : ######################### \n");
	           MLogger.exception(this,e);
	 //         MLogger.info("######################### Exception :: getTravelerListForRefNo() : ######################### \n");
	 //        MLogger.log(-1,"");
	          throw e;
	        }
	        finally{
	        if (con != null) {
	          DbBean.closeConnection(con);
	        }
	   }
	   MLogger.log( -1, this.getClass() + " getTravelerListForRefNo()");
	   return al;

	 } 
}
