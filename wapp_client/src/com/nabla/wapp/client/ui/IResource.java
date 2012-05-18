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

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * @author nabla
 *
 */
public interface IResource extends ClientBundle {

	public interface IStyle extends CssResource {

		int DIALOG_DEFAULT_SPACING();

		@ClassName("form-required-info-message")
		String formRequiredInfoMessage();

		@ClassName("form-required-info-tag")
		String formRequiredInfoTag();

		@ClassName("wizard-navigation-bar")
		String wizardNavigationBar();
	}

	@Source("ui.css")
	IStyle style();

}
