/* Created By: Ranjana
 * Purpose: To implement the functionality of DSL
 * under ticket WO0000001220930
 */

package com.murho.db.utils;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.DSLDAO;
import com.murho.utils.MLogger;

public class DSLUtil {


	public int insertCountry(Hashtable ht) throws Exception {
		MLogger.log(-1, getClass() + " insertCountry()");
		int inserted = 0;
		try {
			DSLDAO dslDao = new DSLDAO();
			inserted = dslDao.insertIntoDsl(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	public boolean updateDestination(Hashtable ht) throws Exception {
		MLogger.log(-1, getClass() + "UpdateCountry()");
		boolean updated = false;
		try {
			DSLDAO dSLDao = new DSLDAO();
			updated = dSLDao.updateIntoDsl(ht);
		} catch (Exception e) {
			throw e;
		}
		return updated;
	}

	public Map getDestinationDetails(String sCOUNTRY,String sPRD_CLS_ID) throws Exception {
		MLogger.log(-1, getClass() + "SelectCountry()");
		Map view = new HashMap();
		try {
			DSLDAO dSLDao = new DSLDAO();
			view = dSLDao.selectRowForDsl(sCOUNTRY,sPRD_CLS_ID);
		} catch (Exception e) {
			throw e;
		}
		return view;

	}
}
