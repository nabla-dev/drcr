package com.nabla.dc.server.xml.settings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.model.fixed_asset.FixedAssetCategoryTypes;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.xml.XmlNode;
import com.nabla.wapp.server.xml.XmlString;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;

@Root
@IRecordTable(name=IFixedAssetCategory.TABLE)
class XmlAssetCategory {
	@Element
	@IRecordField
	XmlString				name;
	@IRecordField
	String					uname;
	@Element(name="visible",required=false)
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

	public XmlAssetCategory() {}

	public XmlAssetCategory(final ResultSet rs) throws SQLException {
		name = new XmlString(rs.getString(1));
		active = rs.getBoolean(2);
		type = FixedAssetCategoryTypes.valueOf(rs.getString(3));
		min_depreciation_period = rs.getInt(4);
		max_depreciation_period = rs.getInt(5);
	}

	@Validate
	public void validate(Map session) throws DispatchException {
		final ICsvErrorList errors = XmlNode.getErrorList(session);
		errors.setLine(name.getRow());
		final String n = name.getValue();
		if (IFixedAssetCategory.NAME_CONSTRAINT.validate("name", n, errors)) {
			if (XmlNode.<ImportContext>getContext(session).getNameList().add(n))
				uname = n.toUpperCase();
			else
				errors.add("name", CommonServerErrors.DUPLICATE_ENTRY);
		}
		IFixedAssetCategory.DEPRECIATION_PERIOD_CONSTRAINT.validate("min_depreciation_period", min_depreciation_period, ServerErrors.INVALID_DEPRECIATION_PERIOD, errors);
		if (max_depreciation_period == null)
			max_depreciation_period = min_depreciation_period;
		else if (IFixedAssetCategory.DEPRECIATION_PERIOD_CONSTRAINT.validate("max_depreciation_period", max_depreciation_period, ServerErrors.INVALID_DEPRECIATION_PERIOD, errors) &&
			max_depreciation_period < min_depreciation_period)
			errors.add("max_depreciation_period", ServerErrors.INVALID_MAX_DEPRECIATION_PERIOD);
		if (active == null)
			active = false;
		if (type == null)
			type = FixedAssetCategoryTypes.TANGIBLE;
	}

}