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
package com.nabla.wapp.server.xml;

import java.sql.Timestamp;

import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;


public class SimpleMatcher implements Matcher  {

	@Override
	public Transform match(Class type) throws Exception {
		if (type != null) {
			if (type.equals(java.sql.Date.class))
				return new JavaSqlDateTransform();
			if (type.equals(java.util.Date.class))
				return new JavaUtilDateTransform();
			if (type.equals(Timestamp.class))
				return new SimpleTimestampTransform();
		}
		return null;
	}

}
