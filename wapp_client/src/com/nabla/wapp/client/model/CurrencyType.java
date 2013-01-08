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

import com.nabla.wapp.client.model.validator.CurrencyValidator;
import com.smartgwt.client.data.SimpleType;
import com.smartgwt.client.types.FieldType;



public class CurrencyType extends SimpleType {

	public static final String		EDITOR_CLASSNAME = "CurrencyItem";
	public static final CurrencyType	instance = new CurrencyType();

	protected CurrencyType() {
		super("currencyUK", FieldType.INTEGER);

		createEditor(EDITOR_CLASSNAME);
		setAttribute("editorType", EDITOR_CLASSNAME, false);
		setValidators(new CurrencyValidator(true));
		final CurrencyFormatter defaultFormat = new CurrencyFormatter();
		setNormalDisplayFormatter(defaultFormat);
		setShortDisplayFormatter(defaultFormat);
	}

	private static native void createEditor(String name) /*-{
		$wnd.isc.ClassFactory.defineClass(name, "TextItem");

		$wnd.isc.ClassFactory.getClass(name).addProperties( {
	        mapDisplayToValue : function(displayValue) {
	            return @com.nabla.wapp.client.model.CurrencyType::displayToValue(Ljava/lang/String;)(displayValue);
	        },
	    	mapValueToDisplay : function(value) {
	        	if (value == null)
	            	return "";
	        	else if (isNaN(value))
	            	return @com.nabla.wapp.client.model.CurrencyType::anyValueToDisplay(Ljava/lang/String;)(value);
	        	else {
					return @com.nabla.wapp.client.model.CurrencyType::valueToDisplay(I)(value);
	        	}
	    	}
		});
	}-*/;

	private static int displayToValue(String value) {
		try {
			if (value == null)
				return 0;
			final Float fValue = Float.valueOf(value);
			final Integer nValue = Math.round(fValue * 100);
			return nValue.intValue();
		} catch (NumberFormatException __) {
			return 0;
		}
	}

	private static String anyValueToDisplay(String value) {
		return value;
	}

	private static String valueToDisplay(int nValue) {
		final Float fValue = new Float(nValue) / 100;
		return fValue.toString();
	}

}
