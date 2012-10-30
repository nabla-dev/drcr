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
package com.nabla.wapp.report.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The <code></code> object is used to
 *
 */
public class ReportParameter implements IsSerializable {

	String	name;
	String	description;
	String	model;

	ReportParameter() {}	// for serialization only

	public ReportParameter(final String name, final String description, final String model) {
		this.name = name;
		this.description = description;
		this.model = model;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getModel() {
		return model;
	}

}
