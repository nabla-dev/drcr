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
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
public class ValidationErrorRecord extends Record {

	public static final String		ERROR_ROW = "errorRow";
	public static final String		ERROR_MESSAGE = "errorMessage";
	
	public ValidationErrorRecord(final Integer row, final String message) {
		setRow(row);
		setMessage(message);
	}
	
	public ValidationErrorRecord(JavaScriptObject jsObj) {
		super(jsObj);
	}
	
	public String getMessage() {
		return this.getAttributeAsString(ERROR_MESSAGE);
	}
	
	public void setMessage(final String message) {
		this.setAttribute(ERROR_MESSAGE, message);
	}
	
	public Integer getRow() {
		return this.getAttributeAsInt(ERROR_ROW);
	}
	
	public void setRow(final Integer row) {
		this.setAttribute(ERROR_ROW, row);
	}
	
}
