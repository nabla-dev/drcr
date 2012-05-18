package com.nabla.wapp.server.json;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.minidev.json.JSONObject;

public class OdbcDateToJson extends AbstractOdbcToJsonEncoder {

	public OdbcDateToJson(final String label) {
		super(label);
	}

	@Override
	public void encode(ResultSet rs, int column, JSONObject record) throws SQLException {
		record.put(label, rs.getDate(column));
		super.encode(rs, column, record);
	}

}