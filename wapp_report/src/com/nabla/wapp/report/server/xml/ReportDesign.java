/**
* Copyright 2013 nabla
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
package com.nabla.wapp.report.server.xml;

import java.util.List;
import java.util.Map;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

@Root(name="report",strict=false)
public class ReportDesign {

	public static final String	TITLE = "title";

	@ElementList(entry="text-property", required=false, inline=true, empty=false)
	List<TextProperty>		textProperties;
	@ElementMap(entry="property", key="name", attribute=true, inline=true, empty=false)
	Map<String, String>		properties;

	public String getTitle() {
		for (TextProperty e : textProperties) {
			if (TITLE.equalsIgnoreCase(e.getName()))
				return e.getValue();
		}
		return null;
	}

	public String getProperty(final String name) {
		return properties.get(name);
	}

	public boolean isProperty(final String name) {
		return properties.containsKey(name);
	}

}
