package com.nabla.dc.server.handler;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

@Root class XmlAccount {
	@Element
	String		code;
	@Element
	String		name;
	@Element(required=false)
	Boolean		visible;
	@Element(required=false)
	String		cc;
	@Element(required=false)
	String		dep;
	@Element
	Boolean		bs;

	@Validate
	public void validate() {
		if (visible == null)
			visible = true;
	}
}