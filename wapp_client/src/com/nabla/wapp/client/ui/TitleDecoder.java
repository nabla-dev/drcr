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
package com.nabla.wapp.client.ui;

import java.util.logging.Logger;

import com.nabla.wapp.client.general.LoggerFactory;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.fields.FormItem;

/**
 * @author nabla
 *
 */
public class TitleDecoder {

	private static final Logger	logger = LoggerFactory.getLog(TitleDecoder.class);
	public static final int		ACCESS_KEY_TAG = '*';

	private String				title;
	private String				accessKey;

	public TitleDecoder(final String title) {
		if (!decode(title)) {
			this.title = title;
			this.accessKey = null;
		}
	}

	public String getTitle() {
		return title;
	}

	public boolean hasAccessKey() {
		return accessKey != null;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void apply(final FormItem item) {
		item.setTitle(getTitle());
		if (hasAccessKey())
			item.setAccessKey(getAccessKey());
	}

	public void apply(final Canvas item) {
		item.setTitle(getTitle());
		if (hasAccessKey())
			item.setAccessKey(getAccessKey());
	}

	private boolean decode(final String text) {
		if (text == null)
			return false;
		String s = text;
		int p = s.indexOf(ACCESS_KEY_TAG);
		while (p >= 0) {
			int pp = p + 1;
			if (pp >= s.length())
				break;
			String key = s.substring(pp, pp + 1).toLowerCase();
			if (key.matches("^[a-z0-9]$")) {
				this.accessKey = key;
				this.title = removeCharAt(s, p);
				logger.fine("access key '" + this.accessKey + "' for " + this.title);
				return true;
			}
			if (key.charAt(0) == ACCESS_KEY_TAG)
				s = removeCharAt(s, p);
			p = s.indexOf(ACCESS_KEY_TAG, pp);
		}
		return false;
	}

	public static String removeCharAt(final String s, int pos) {
		return s.substring(0, pos) + s.substring(pos+1);
	}

}
