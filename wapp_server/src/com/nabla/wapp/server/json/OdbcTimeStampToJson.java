package com.nabla.wapp.server.json;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import net.minidev.json.JSONObject;

public class OdbcTimeStampToJson extends AbstractOdbcToJsonEncoder {

	private static final SimpleDateFormat	format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public OdbcTimeStampToJson(final String label) {
		super(label);
	}

	@Override
	public void encode(ResultSet rs, int column, JSONObject record) throws SQLException {
		final Timestamp tm = rs.getTimestamp(column);
		if (rs.wasNull())
			super.encode(rs, column, record);
		else
			record.put(label, format.format(tm));
	}

}