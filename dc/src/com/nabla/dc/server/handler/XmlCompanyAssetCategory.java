package com.nabla.dc.server.handler;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import com.nabla.wapp.server.xml.XmlNode;

@Root class XmlCompanyAssetCategory extends XmlNode {
	@Attribute
	String		financial_statement_category;
	@Text
	String		asset_category;
}