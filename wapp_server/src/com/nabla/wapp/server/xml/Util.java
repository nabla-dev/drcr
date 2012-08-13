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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.core.PersistenceException;

/**
 * @author nabla64
 *
 */
public abstract class Util {

	private static final String	ROW_COL = "[row,col]:[";
	private static final Log		log = LogFactory.getLog(Util.class);

	public static String extractFieldName(final PersistenceException e) {
		final String message = e.getMessage();
		if (message != null && !message.isEmpty()) {
			// looking for ...'field name'...
			final String[] matches = e.getMessage().split("'");
			if (matches.length > 2)
				return matches[1];
		}
		return null;
	}

	public static Integer extractLine(final Exception e) {
		final String message = e.getMessage();
		if (message != null && !message.isEmpty()) {
			// looking for [row,col]:[x,x]
			int from = message.indexOf(ROW_COL);
			if (from >= 0) {
				from += ROW_COL.length();
				int to = message.indexOf(',', from);
				if (to > from) {
					final String row = message.substring(from, to);
					try {
						return Integer.valueOf(row);
					} catch (Throwable x) {
						log.warn("fail to convert " + row + " to a row", x);
					}
				}
			}
		}
		return null;
	}

}
