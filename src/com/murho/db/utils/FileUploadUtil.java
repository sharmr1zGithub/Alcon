package com.murho.db.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.murho.gates.DbBean;

public class FileUploadUtil {

	public ArrayList ItemUploadSummary(String Fname) throws Exception {
		CallableStatement cs = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();

			cs = con.prepareCall("{call dbo.[PROC_UPLOADBULKITEM](?)}");
			cs.setString(1, Fname);
			System.out.println("Uploadede file"+Fname);
			rs = cs.executeQuery();
			while (rs.next()) {

				Map map = new HashMap();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					map.put(rs.getMetaData().getColumnLabel(i), rs
									.getString(i));
				}
				arrList.add(map);
			}

			System.out.println("array Size" + arrList.size());

			System.out.println("PROC_UPLOADBULKITEM :: arrList.size "
					+ arrList.size());
		}catch (Exception e) {
			System.out
					.println("FileUploader ::ItemUploadSummary :  Exception :: "
							+ e.toString());
		} finally {
			DbBean.closeConnection(con, cs);
		}
		return arrList;
	}

}
