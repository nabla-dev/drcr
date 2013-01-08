/**
* Copyright 2012 nabla
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License. You may obtain a copy of
* the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations under
* the License.
*
*/
package org.eclipse.birt.report.engine.emitter.xml;

import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;

public class XmlRenderOption extends RenderOption implements IXmlRenderOption {

	public static final String	ROOT_NODE = "XmlRenderOption.rootNode";
	public static final String	RECORD_NODE = "XmlRenderOption.recordNode";

	private static final String	DEFAULT_ROOT_NODE = "data";
	private static final String	DEFAULT_RECORD_NODE = "record";

	public XmlRenderOption() {
		super();
	}

	public XmlRenderOption(IRenderOption options) {
		super(options);
	}

	@Override
	public String getRootNode() {
		return getNonNullStringOption(ROOT_NODE, DEFAULT_ROOT_NODE);
	}

	@Override
	public void setRootNode(String name) {
		setOption(ROOT_NODE, name);
	}

	@Override
	public String getRecordNode() {
		return getNonNullStringOption(RECORD_NODE, DEFAULT_RECORD_NODE);
	}

	@Override
	public void setRecordNode(String name) {
		setOption(RECORD_NODE, name);
	}

	protected String getNonNullStringOption(final String name, final String defaultValue) {
		final String value = getStringOption(name);
		return (value == null) ? defaultValue : value;
	}
}
