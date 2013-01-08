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

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;


public interface IResource extends ClientBundle {

	public interface IApplicationStyle extends CssResource {
		int DIALOG_SPACING();
		int DIALOG_MARGIN();
		int DIALOG_BUTTONS_TOP_MARGIN();

		@ClassName("login-message")
		String loginMessage();
	}

	@Source("application.css")
	IApplicationStyle style();

}
