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

import org.simpleframework.xml.Text;

/**
 * @author nabla64
 *
 */
public class XmlString extends XmlValue<String> {

	@Text
	protected String	value;

	public XmlString() {}

	public XmlString(final String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

	public boolean isEmpty() {
		return value == null || value.isEmpty();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof XmlString) {
			XmlString e = (XmlString) obj;
			return e.getValue().equals(this.getValue());
		}
		return false;
   }

}
