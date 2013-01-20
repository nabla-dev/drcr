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

import java.util.Map;

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

@Root(strict=false)
public class UserProperty {

	public static final String	NAME = "name";
	public static final String	VALUE = "default";

	private String		name;
	private String		value;

	@ElementMap(entry="property", key="name", attribute=true, required=false, inline=true, empty=false)
	Map<String, String>		properties;
	@ElementMap(entry="expression", key="name", attribute=true, required=false, inline=true, empty=false)
	Map<String, String>		expressions;

	@Validate
	public void validate() {
		name = properties.get(NAME);
		properties.clear();
		value = expressions.get(VALUE);
		expressions.clear();
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
