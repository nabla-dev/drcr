/**
* Copyright 2013 nabla
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
package com.nabla.wapp.report.server.xml;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import com.nabla.wapp.shared.general.Nullable;


@Root(name="report",strict=false)
public class ReportDesign {

	private static final Log		log = LogFactory.getLog(ReportDesign.class);
	private static final String	TITLE = "title";
	private static final String	PROPERTY_LOCALE = "locale";

	@ElementList(entry="text-property", required=false, inline=true, empty=false)
	List<TextProperty>		textProperties;
	@ElementMap(entry="property", key="name", attribute=true, inline=true, empty=false)
	Map<String, String>		properties;

	public String getDefaultTitle(final String defaultValue) {
		final TextProperty title = getTitle();
		return (title != null) ? title.getValue() : defaultValue;
	}

	public String getTitleKey() {
		final TextProperty title = getTitle();
		return (title != null) ? title.getKey(TITLE) : TITLE;
	}

	public Locale getDefaultLocale() {
		final String locale = properties.get(PROPERTY_LOCALE);
		if (locale != null) {
			try {
				return LocaleUtils.toLocale(locale);
			} catch (IllegalArgumentException __) {
			}
		}
		if (log.isErrorEnabled())
			log.error("unsupported locale '" + locale + "'");
		return Locale.UK;
	}

	public String getProperty(final String name, final String defaultValue) {
		final String value = properties.get(name);
		return (value != null) ? value : defaultValue;
	}

	public String getProperty(final String name) {
		return properties.get(name);
	}

	public boolean isProperty(final String name) {
		return properties.containsKey(name);
	}

	protected @Nullable TextProperty getTitle() {
		for (TextProperty e : textProperties) {
			if (TITLE.equalsIgnoreCase(e.getName()))
				return e;
		}
		return null;
	}

}
