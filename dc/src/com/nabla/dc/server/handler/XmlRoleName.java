package com.nabla.dc.server.handler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.simpleframework.xml.Root;

import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.xml.XmlString;
import com.nabla.wapp.shared.auth.IRootUser;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IRole;

@Root
class XmlRoleName extends XmlString {

	public static final String FIELD = "name";

	@Override
	protected void doValidate(final ICsvErrorList errors, final Map session) throws DispatchException {
		super.doValidate(errors, session);
		if (IRootUser.NAME.equalsIgnoreCase(value))	// ROOT name not allowed
			errors.add(FIELD, CommonServerErrors.INVALID_VALUE);
		IRole.NAME_CONSTRAINT.validate(FIELD, value, errors);
	}

	public boolean save(final Connection conn) throws SQLException {
		return Database.executeUpdate(conn,
"INSERT IGNORE INTO role (name,uname) VALUES(?,?);", value, value.toUpperCase());
	}

}