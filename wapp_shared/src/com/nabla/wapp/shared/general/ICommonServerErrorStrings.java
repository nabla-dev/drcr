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
package com.nabla.wapp.shared.general;

import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * @author nabla64
 *
 */
public interface ICommonServerErrorStrings extends ConstantsWithLookup {

	@DefaultStringValue("Internal error. Please notify your administor if it happens again")
	String INTERNAL_ERROR();

	@DefaultStringValue("Invalid XML request. Please notify this error to your administor")
	String INVALID_XML_REQUEST();

	@DefaultStringValue("Access denied")
	String ACCESS_DENIED();

	@DefaultStringValue("Invalid username or password")
	String INVALID_USER_PASSWORD();

	@DefaultStringValue("Field is required")
	String REQUIRED_VALUE();

	@DefaultStringValue("Duplicate entry")
	String DUPLICATE_ENTRY();

	@DefaultStringValue("Entry in use")
	String RECORD_IN_USE();

	@DefaultStringValue("Record has been removed by another user. You should reload the data")
	String RECORD_HAS_BEEN_REMOVED();

	@DefaultStringValue("Field contains invalid character(s)")
	String INVALID_CHARACTER();

	@DefaultStringValue("Value too short")
	String TEXT_TOO_SHORT();

	@DefaultStringValue("Value too long")
	String TEXT_TOO_LONG();

	@DefaultStringValue("Invalid value")
	String INVALID_VALUE();

	@DefaultStringValue("Invalid currency value")
	String INVALID_CURRENCY_VALUE();

	@DefaultStringValue("Invalid pound value")
	String INVALID_POUND_VALUE();
	
	@DefaultStringValue("no data found")
	String NO_DATA();
	
	@DefaultStringValue("unsupported column")
	String UNSUPPORTED_FIELD();
	
	@DefaultStringValue("wrong number of columns")
	String INVALID_FIELD_COUNT();
}
