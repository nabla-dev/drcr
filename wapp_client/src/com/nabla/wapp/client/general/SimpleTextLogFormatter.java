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
package com.nabla.wapp.client.general;

import java.util.Date;
import java.util.logging.LogRecord;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.logging.impl.FormatterImpl;

/**
 * The <code>SimpleTextLogFormatter</code> object is used to output log message as text
 * in the following format: {HH:mm:ss} [{level}] ({class name}): {message}
 *
 */
public class SimpleTextLogFormatter extends FormatterImpl {

	private final boolean			showStackTraces;
	private final DateTimeFormat	dtFormat = DateTimeFormat.getFormat("HH:mm:ss");

	public SimpleTextLogFormatter(boolean showStackTraces) {
		this.showStackTraces = showStackTraces;
	}

	@Override
	public String format(LogRecord event) {
		StringBuilder message = new StringBuilder();
		message.append(dtFormat.format(new Date(event.getMillis())));
		message.append(" [");
		message.append(event.getLevel().getName());
		message.append("] (");
		final String className = event.getLoggerName();
		if (className == null || className.isEmpty())
			message.append("ROOT");
		else {
			final String[] tokens = className.split("\\.");
			if (tokens.length == 0)
				message.append(className);
			else
				message.append(tokens[tokens.length - 1]);
		}
		message.append("): ");
		message.append(event.getMessage());
		if (showStackTraces || event.getThrown() != null)
			message.append(getStackTraceAsString(event.getThrown(), "\n", "\t"));
		return message.toString();
	}

}
