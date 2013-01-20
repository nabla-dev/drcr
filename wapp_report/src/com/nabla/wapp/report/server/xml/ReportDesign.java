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
import org.simpleframework.xml.core.Validate;
import org.simpleframework.xml.core.ValueRequiredException;

@Root(name="report",strict=false)
public class ReportDesign {

	public static final String	TITLE = "title";
	public static final String	USER_PROPERTIES = "userProperties";
	public static final String	ROLE = "role";
	public static final String	CATEGORY = "report_category";

	private String		title;
	private String		role;
	private String		category;

	@ElementMap(entry="text-property", key="name", attribute=true, required=false, inline=true, empty=false)
	Map<String, String>		textProperties;
	@ElementList(entry="list-property", required=false, inline=true, empty=false)
	List<ListProperty>		listProperties;

	@Validate
	public void validate() throws ValueRequiredException {
		title = textProperties.get(TITLE);
		textProperties.clear();
		for (ListProperty e : listProperties) {
			if (USER_PROPERTIES.equalsIgnoreCase(e.getName())) {
				role = e.get(ROLE);
				category = e.get(CATEGORY);
				break;
			}
		}
		listProperties.clear();
		if (title == null)
			throw new ValueRequiredException(TITLE);
		if (role == null)
			throw new ValueRequiredException(ROLE);
		if (category == null)
			throw new ValueRequiredException(CATEGORY);
	}

	public String getTitle() {
		return title;
	}

	public String getRole() {
		return role;
	}

	public String getCategory() {
		return category;
	}

}
