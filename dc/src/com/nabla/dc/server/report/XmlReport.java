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
package com.nabla.dc.server.report;

import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import com.nabla.dc.server.Ledgers;
import com.nabla.dc.shared.report.BuiltInReports;


/**
 * @author nabla64
 *
 */
@Root
public class XmlReport {

	@Element
	String					file_name;
	@Element
	Ledgers					ledger;
	@Element
	BuiltInReports			internal_name;
	@ElementMap(entry="name", key="locale", attribute=true, inline=true)
	Map<String, String>		names;

	public String getFileName() {
		return file_name;
	}

	public Ledgers getLedger() {
		return ledger;
	}

	public BuiltInReports getInternalName() {
		return internal_name;
	}

	public Map<String, String> getLocalizedNames() {
		return names;
	}

	public String getDefaultName() {
		return names.values().iterator().next();
	}
}
