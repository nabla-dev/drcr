package com.nabla.wapp.server.json;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.minidev.json.JSONObject;

public class OdbcBooleanToJson extends AbstractOdbcToJsonEncoder {

	public OdbcBooleanToJson(final String label) {
		super(label);
	}

	@Override
	public void encode(ResultSet rs, int column, JSONObject record) throws SQLException {
		record.put(label, rs.getBoolean(column));
		super.encode(rs, column, record);
	}

}