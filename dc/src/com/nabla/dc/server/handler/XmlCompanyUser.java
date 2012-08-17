package com.nabla.dc.server.handler;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

@Root class XmlCompanyUser {
	@Element
	String			name;
	@Element(required=false)
	Boolean			active;
	@ElementList(entry="role", required=false)
	List<String>	roles;

	@Validate
	public void validate() {
		if (active == null)
			active = false;
	}
}