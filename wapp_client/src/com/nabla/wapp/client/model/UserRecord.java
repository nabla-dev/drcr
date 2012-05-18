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
package com.nabla.wapp.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.wapp.shared.model.IUser;
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
public class UserRecord extends BasicListGridRecord implements IUser {

	public static final IRecordFactory<UserRecord>	factory = new IRecordFactory<UserRecord>() {

		@Override
		public UserRecord get(JavaScriptObject data) {
			return new UserRecord(data);
		}

	};

	public UserRecord(Record impl) {
		super(impl);
	}

	public UserRecord(JavaScriptObject js) {
		super(js);
	}

	public String getName() {
		return getAttributeAsString(NAME);
	}

	public String getPassword() {
		return getAttributeAsString(PASSWORD);
	}

	public Boolean getActive() {
		return getBoolean(ACTIVE);
	}

}
