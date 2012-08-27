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
package com.nabla.dc.server.xml.settings;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author nabla64
 *
 */
public class ImportContext {

	private final Set<String>		names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	private final Set<String>		companyNames = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	private final Set<String>		accountCodes = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

	public Set<String> getNameList() {
		return names;
	}

	public Set<String> getCompanyNameList() {
		return companyNames;
	}

	public Set<String> getAccountCodeList() {
		return accountCodes;
	}

}
