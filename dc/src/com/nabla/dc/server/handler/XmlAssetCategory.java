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

import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.model.fixed_asset.FixedAssetCategoryTypes;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
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
@IRecordTable(name=IFixedAssetCategory.TABLE) class XmlAssetCategory implements IFixedAssetCategory {
	@Element
	@IRecordField
	XmlString				name;
	@IRecordField
	String					uname;
	@Element(name="active",required=false)
	@IRecordField
	Boolean					active;
	@Element(required=false)
	@IRecordField
	FixedAssetCategoryTypes	type;
	@Element
	@IRecordField
	Integer					min_depreciation_period;
	@Element(required=false)
	@IRecordField
	Integer					max_depreciation_period;

	@Validate
	public void validate(Map session) throws DispatchException {
		final ICsvErrorList errors = Importer.getErrors(session);
		errors.setLine(name.getRow());
		if (NAME_CONSTRAINT.validate("name", name.getValue(), errors))
			uname = name.getValue().toUpperCase();
		DEPRECIATION_PERIOD_CONSTRAINT.validate(MIN_DEPRECIATION_PERIOD, min_depreciation_period, ServerErrors.INVALID_DEPRECIATION_PERIOD, errors);
		if (max_depreciation_period == null)
			max_depreciation_period = min_depreciation_period;
		else if (DEPRECIATION_PERIOD_CONSTRAINT.validate(MAX_DEPRECIATION_PERIOD, max_depreciation_period, ServerErrors.INVALID_DEPRECIATION_PERIOD, errors) &&
			max_depreciation_period < min_depreciation_period)
				errors.add(MAX_DEPRECIATION_PERIOD, ServerErrors.INVALID_MAX_DEPRECIATION_PERIOD);
		if (active == null)
			active = false;
		if (type == null)
			type = FixedAssetCategoryTypes.TANGIBLE;
	}

	public static void saveAll(final List<XmlAssetCategory> list, final Connection conn, final SqlInsertOptions option, final ICsvErrorList errors) throws SQLException, DispatchException {
		if (validateAll(list, errors)) {
			final SqlInsert<XmlAssetCategory> sql = new SqlInsert<XmlAssetCategory>(XmlAssetCategory.class, option);
			final BatchInsertStatement<XmlAssetCategory> batch = new BatchInsertStatement<XmlAssetCategory>(conn, sql);
			try {
				for (XmlAssetCategory e : list)
					batch.add(e);
				batch.execute();
			} finally {
				batch.close();
			}
		}
	}

	private static boolean validateAll(final List<XmlAssetCategory> list, final ICsvErrorList errors) throws DispatchException {
		final Set<String> names = new HashSet<String>();
		for (XmlAssetCategory e : list) {
			if (names.contains(e.name.getValue())) {
				errors.setLine(e.name.getRow());
				errors.add("name", CommonServerErrors.DUPLICATE_ENTRY);
			} else
				names.add(e.name.getValue());
		}
		return names.size() == list.size();
	}
}