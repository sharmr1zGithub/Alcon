 /*  Author: Ranjana 
  *  Purpose: LOT restriction in PDA under ticket WO0000000284867 
  *           for the process of Lot Restriction Module
  *  Version: 1.0*/

package com.murho.db.utils;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.murho.dao.RestrictLotDAO;
import com.murho.utils.MDbConstant;
import com.murho.utils.MLogger;

public class RestrictLotUtil {

	public boolean insertLOT(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {
			RestrictLotDAO lotDao = new RestrictLotDAO();
			inserted = lotDao.insertIntoLotRestriction(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	public boolean updatelot(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {
			RestrictLotDAO lotDao = new RestrictLotDAO();
			inserted = lotDao.updateIntoLotRestriction(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	public Map getLotDetails(String aLOT) throws Exception {
		Map map = new HashMap();
		try {
			RestrictLotDAO lotDao = new RestrictLotDAO();
			map = lotDao.selectRow(aLOT);
		} catch (Exception e) {
			throw e;
		}
		return map;

	}
}
