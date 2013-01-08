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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.general.Util;
import com.nabla.wapp.client.ui.form.Form;


public class ListGridForm extends UiBinderSpeudoWidgetList {

	private static final Logger	logger = LoggerFactory.getLog(ListGridForm.class);
	private Form				form;

	public Form getForm() {
		Assert.notNull(form);

		form.onCreate();
		return form;
	}

	@Override
	public void add(final Widget w) {
		Assert.argumentNotNull(w);

		if (form != null)
			logger.log(Level.SEVERE,"only one widget can be added to a " + Util.getClassSimpleName(this.getClass()));
		if (w instanceof Form)
			form = (Form)w;
		else
			super.add(w);
	}

}
