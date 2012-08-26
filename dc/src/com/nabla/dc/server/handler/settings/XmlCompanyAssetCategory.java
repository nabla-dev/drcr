package com.nabla.dc.server.handler.settings;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import com.nabla.dc.shared.model.fixed_asset.IFinancialStatementCategory;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.xml.TXmlNode;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;

@Root
class XmlCompanyAssetCategory extends TXmlNode<ImportContext> {
	@Attribute
	String		financial_statement_category;
	@Text
	String		asset_category;

	@Override
	protected void doValidate(final ImportContext ctx, final ICsvErrorList errors) throws DispatchException {
		if (IFixedAssetCategory.NAME_CONSTRAINT.validate("asset_category", asset_category, errors) &&
			!ctx.getNameList().add(asset_category))
			errors.add("asset_category", CommonServerErrors.DUPLICATE_ENTRY);
		IFinancialStatementCategory.NAME_CONSTRAINT.validate("financial_statement_category", financial_statement_category, errors);
	}

	public String getFinancialStatementCategory() {
		return financial_statement_category;
	}

	public String getAssetCategory() {
		return asset_category;
	}
}