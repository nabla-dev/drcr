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

import java.util.HashMap;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;


@Root(strict=false)
public class ListProperty extends HashMap<String, String> {

	private static final long serialVersionUID = 1L;

	@Attribute
	String					name;
	@ElementList(entry="structure", required=false, inline=true, empty=false)
	List<UserProperty>		userProperties;

	@Commit
	public void commit() {
		for (UserProperty e : userProperties)
			put(e.getName(), e.getValue());
		userProperties.clear();
	}

	public String getName() {
		return name;
	}

}
