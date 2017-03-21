/* Created By: Ranjana
 * Purpose: To implement the functionality of System Blocking Master
 * under ticket WO0000001221106
 */

package com.murho.db.utils;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.murho.dao.DestinationLotBlockDAO;
import com.murho.utils.MLogger;

public class DestinationLotBlockUtil {

	public int insertDestination(Hashtable ht) throws Exception {
		MLogger.log(-1, getClass() + " insertDestination()");
		int inserted = 0;
		try {
			DestinationLotBlockDAO destinationDao = new DestinationLotBlockDAO();
			inserted = destinationDao.insertIntoDestLotBlock(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	public Map getDestinationDetails(String aDESTINATION, String aPrdclsid) throws Exception {
		Map array = new HashMap();
		try {
			DestinationLotBlockDAO destinationDao = new DestinationLotBlockDAO();
			array = destinationDao.selectRowForDest(aDESTINATION, aPrdclsid);
		} catch (Exception e) {
			throw e;
		}
		return array;
	}
	
	public boolean deleteDestination(Hashtable ht) throws Exception {
		MLogger.log(-1, getClass() + " deleteDestination()");
		boolean deleted = false;
		try {
			DestinationLotBlockDAO destinationDao = new DestinationLotBlockDAO();
			deleted = destinationDao.deleteDestLotBlock(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}
}

