package com.nabla.dc.server.handler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.dc.shared.model.fixed_asset.IFinancialStatementCategory;
import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.BatchInsertStatement;
import com.nabla.wapp.server.database.SqlInsert;
import com.nabla.wapp.server.xml.Importer;
import com.nabla.wapp.server.xml.XmlString;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.database.SqlInsertOptions;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;

@Root
@IRecordTable(name=IFinancialStatementCategory.TABLE) class XmlFinancialStatementCategory implements IFinancialStatementCategory {
	@Element
	@IRecordField
	XmlString	name;
	@IRecordField
	String		uname;
	@Element(name="visible", required=false)
	@IRecordField
	Boolean		active;

	@Validate
	public void validate(Map session) throws DispatchException {
		final ICsvErrorList errors = Importer.getErrors(session);
		errors.setLine(name.getRow());
		if (NAME_CONSTRAINT.validate(NAME, name.getValue(), errors))
			uname = name.getValue().toUpperCase();
		if (active == null)
			active = false;
	}

	public static void saveAll(final List<XmlFinancialStatementCategory> list, final Connection conn, final SqlInsertOptions option, final ICsvErrorList errors) throws SQLException, DispatchException {
		if (validateAll(list, errors)) {
			final SqlInsert<XmlFinancialStatementCategory> sql = new SqlInsert<XmlFinancialStatementCategory>(XmlFinancialStatementCategory.class, option);
			final BatchInsertStatement<XmlFinancialStatementCategory> batch = new BatchInsertStatement<XmlFinancialStatementCategory>(conn, sql);
			try {
				for (XmlFinancialStatementCategory e : list)
					batch.add(e);
				batch.execute();
			} finally {
				batch.close();
			}
		}
	}

	private static boolean validateAll(final List<XmlFinancialStatementCategory> list, final ICsvErrorList errors) throws DispatchException {
		final Set<String> names = new HashSet<String>();
		for (XmlFinancialStatementCategory e : list) {
			if (names.contains(e.name.getValue())) {
				errors.setLine(e.name.getRow());
				errors.add("name", CommonServerErrors.DUPLICATE_ENTRY);
			} else
				names.add(e.name.getValue());
		}
		return names.size() == list.size();
	}
}