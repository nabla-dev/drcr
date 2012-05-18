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

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.nabla.wapp.client.general.LoggerFactory;
import com.smartgwt.client.core.DataClass;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.SimpleType;
import com.smartgwt.client.data.SimpleTypeFormatter;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.DataBoundComponent;

/**
 * @author nabla
 *
 */
public class DateTimeType extends SimpleType implements SimpleTypeFormatter {

	private static final Logger			logger = LoggerFactory.getLog(DateTimeType.class);
	public static final DateTimeType	instance = new DateTimeType();

	public DateTimeType() {
		super("dateTimeLocale", FieldType.DATETIME);
		this.setNormalDisplayFormatter(this);
		this.setShortDisplayFormatter(this);
		this.register();
	}

	@Override
	public String format(Object value, @SuppressWarnings("unused") DataClass field, @SuppressWarnings("unused") DataBoundComponent component, @SuppressWarnings("unused") Record record) {
		if(value == null)
			return null;
		try {
			return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format((Date)value);
		} catch (Throwable e) {
			final String s = value.toString();
			logger.log(Level.WARNING, "failed to format date time = " + s, e);
			return s;
		}
	}
}
