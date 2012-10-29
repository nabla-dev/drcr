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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.i18n.client.NumberFormat;
import com.nabla.wapp.client.general.LoggerFactory;
import com.smartgwt.client.core.DataClass;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.SimpleTypeFormatter;
import com.smartgwt.client.widgets.DataBoundComponent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemValueFormatter;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * The <code></code> object is used to
 *
 */
public class PoundFormatter implements CellFormatter, SimpleTypeFormatter, FormItemValueFormatter {

	public static final String	FORMAT_ACCOUNTING_WITH_ZERO = "#,##0;(#,##0);0";
	public static final String	FORMAT_ACCOUNTING_WITHOUT_ZERO = "#,##0;(#,##0);";

	private static final Logger	log = LoggerFactory.getLog(PoundFormatter.class);
	private final NumberFormat		format;
	private final String			zeroPattern;

	public PoundFormatter(final NumberFormat format, final String zeroPattern) {
		this.format = format;
		this.zeroPattern = zeroPattern;
	}

	public PoundFormatter(final String format) {
		if (format == null || format.isEmpty()) {
			this.format = null;
			this.zeroPattern = null;
		} else {
			final String[] tokens = format.split(";");
			if (tokens.length < 2) {
				this.format = NumberFormat.getFormat(format);
				this.zeroPattern = null;
			} else {
				this.format = NumberFormat.getFormat(tokens[0] + ";" + tokens[1]);
				if (tokens.length < 3)
					this.zeroPattern = (format.charAt(format.length() - 1) == ';') ? "" : null;
				else
					this.zeroPattern = tokens[2];
			}
		}
	}

	public PoundFormatter(final String format, final String zeroPattern) {
		this.format = NumberFormat.getFormat(format);
		this.zeroPattern = zeroPattern;
	}

	public PoundFormatter() {
		this(FORMAT_ACCOUNTING_WITH_ZERO);
	}

	@Override
	public String format(Object value, @SuppressWarnings("unused") ListGridRecord record, @SuppressWarnings("unused") int rowNum, @SuppressWarnings("unused") int colNum) {
		return valueToDisplay(value);
	}

	@Override
	public String format(Object value, @SuppressWarnings("unused") DataClass field, @SuppressWarnings("unused") DataBoundComponent component, @SuppressWarnings("unused") Record record) {
		return valueToDisplay(value);
	}

	@Override
	public String formatValue(Object value, @SuppressWarnings("unused") Record record, @SuppressWarnings("unused") DynamicForm form, @SuppressWarnings("unused") FormItem item) {
		return valueToDisplay(value);
	}

	public String valueToDisplay(Object value) {
		if (value == null || format == null)
			return "";
		try {
			// value must be an Integer
			final Integer nValue = (Integer)value;
			if (nValue == 0 && zeroPattern != null)
				return zeroPattern;
			return format.format(nValue);
		} catch (Throwable e) {
			log.log(Level.WARNING, "failed to format pound = " + value.toString(), e);
			return value.toString();
		}
	}

}
