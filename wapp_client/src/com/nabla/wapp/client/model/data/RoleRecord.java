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
package com.nabla.wapp.client.model.data;

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.wapp.client.model.IRecordFactory;
import com.nabla.wapp.shared.model.IRole;
import com.smartgwt.client.data.Record;


public class RoleRecord extends BasicListGridRecord implements IRole {

	public static final IRecordFactory<RoleRecord>	factory = new IRecordFactory<RoleRecord>() {

		@Override
		public RoleRecord get(JavaScriptObject data) {
			return new RoleRecord(data);
		}

	};

	public RoleRecord(Record impl) {
		super(impl);
	}

	public RoleRecord(JavaScriptObject js) {
		super(js);
	}

	public String getName() {
		return getAttributeAsString(NAME);
	}

}
