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

import com.google.gwt.core.client.GWT;
import com.nabla.wapp.client.ui.form.Form;

/**
 * @author nabla
 *
 */
public class Resource {

	public static final IResource					bundle = GWT.create(IResource.class);
	public static final ITextResource				strings = GWT.create(ITextResource.class);
	public static final IMessageResource			messages = GWT.create(IMessageResource.class);
	public static final IServerErrorStrings		serverErrors = GWT.create(IServerErrorStrings.class);

	public static final Resource					instance = new Resource();

	public Resource() {
		bundle.style().ensureInjected();
		Form.setDefaultMargin(bundle.style().DIALOG_MARGIN());
	}
}
