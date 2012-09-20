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
package com.nabla.wapp.client.ui.form;

import java.util.logging.Logger;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.nabla.wapp.client.general.LoggerFactory;
import com.smartgwt.client.widgets.form.fields.DateItem;

/**
 * @author nabla
 *
 */
public class SmartGwtDateItem extends DateItem {

	private static final Logger	log = LoggerFactory.getLog(SmartGwtDateItem.class);

	@Override
	public void setUseMask(Boolean useMask) {
		super.setUseMask(useMask);
		if (useMask) {
			// TODO: when SmartGWT supports mask correctly then remove this class
			final String pattern = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT).getPattern().toUpperCase();
			String inputFormat = "";
			char charToFind[] = {'D', 'M', 'Y' };
			int len = pattern.length();
			for (int i = 0; i < len; ++i) {
				for (int j = 0; j < charToFind.length; ++j) {
					if (pattern.charAt(i) == charToFind[j]) {
						inputFormat += charToFind[j];
						charToFind[j] = '\0';
						break;
					}
				}
			}
			if (inputFormat.length() == 3) {
				log.info("use input format '" + inputFormat + "' for date pattern '" + pattern + "'");
				setInputFormat(inputFormat);
			} else {
				log.warning("failed to find input format for date pattern '" + pattern + "'");
			}
		}
	}
}
