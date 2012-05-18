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
package com.nabla.wapp.client.model.field;

import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.logging.client.LogConfiguration;
import com.nabla.wapp.client.general.LoggerFactory;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.util.DateDisplayFormatter;
import com.smartgwt.client.util.DateParser;
import com.smartgwt.client.util.DateUtil;

/**
 * @author nabla64
 *
 */
public class DateField extends DataSourceDateField {

	private static final Logger	logger = LoggerFactory.getLog(DateField.class);

	static {
		if (LogConfiguration.loggingIsEnabled()) {
			logger.info("current locale = " + LocaleInfo.getCurrentLocale().getLocaleName());
			logger.info("short date pattern = " + DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).getPattern());
			final Date today = new Date();
			logger.info("toString= " + today.toString());
			logger.info("short= " + DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(today));
			logger.info("medium= " + DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(today));
			logger.info("long= " + DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(today));
			logger.info("short time= " + DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.TIME_SHORT).format(today));
			logger.info("long time= " + DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.TIME_LONG).format(today));
			logger.info("short date time= " + DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(today));
			logger.info("medium date time= " + DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM).format(today));
			logger.info("long date time= " + DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_LONG).format(today));
		}
		DateUtil.setShortDateDisplayFormatter(new DateDisplayFormatter() {
			@Override
			public String format(final Date date) {
				return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT).format(date);
			}
		});
		DateUtil.setNormalDateDisplayFormatter(new DateDisplayFormatter() {
			@Override
			public String format(final Date date) {
				return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(date);
			}
		});
		DateUtil.setDateParser(new DateParser() {
			@Override
			public Date parse(String dateString) {
				return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT).parse(dateString);
			}
		});
/*		DateUtil.setDateInputFormatter(new DateInputFormatter() {
			@Override
			public Date parse(final String dateString) {
				return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT).parse(dateString);
			}
		});*/
	}

	public DateField(final String name, final FieldAttributes... attributes) {
		super(name);
		this.setUseTextField(true);
		FieldAttributes.applyAll(this, attributes);
	}

}
