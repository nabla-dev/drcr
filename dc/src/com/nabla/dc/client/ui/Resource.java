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
package com.nabla.dc.client.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.nabla.dc.shared.ServerErrors;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.ui.Dialog;
import com.nabla.wapp.shared.general.CommonServerErrors;

/**
 * @author nabla
 *
 */
public class Resource {

	private static final Logger					logger = LoggerFactory.getLog(Resource.class);

	public static final IResource					bundle = GWT.create(IResource.class);
	public static final ITextResource				strings = GWT.create(ITextResource.class);
	public static final IMessageResource			messages = GWT.create(IMessageResource.class);
	public static final IServerErrorStrings		serverErrors = GWT.create(IServerErrorStrings.class);

	public static final Resource					instance = new Resource();

	public Resource() {
		bundle.style().ensureInjected();
		Dialog.setDefaultMargin(bundle.style().DIALOG_MARGIN());
		if (logger.isLoggable(Level.WARNING)) {
			// check that all error strings have been defined
			for (CommonServerErrors e : CommonServerErrors.values()) {
				try {
					Resource.serverErrors.getString(e.toString());
				} catch (Exception x) {
					logger.warning("error string '" + e + "' not defined");
				}
			}
			for (ServerErrors e : ServerErrors.values()) {
				try {
					Resource.serverErrors.getString(e.toString());
				} catch (Exception x) {
					logger.warning("error string '" + e + "' not defined");
				}
			}
		}
	}
}
