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

import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.general.Util;
import com.nabla.wapp.client.model.Model;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.validator.MatchesFieldValidator;


public class ConfirmPasswordEditBox extends PasswordEditBox {

	private static final Logger	logger = LoggerFactory.getLog(ConfirmPasswordEditBox.class);
	private String				buddyItem = null;
	private String				errorMessage = null;

	public ConfirmPasswordEditBox() {
		super();
		impl.setRequired(true);
		impl.setShouldSaveValue(false);
	}

	public void setBuddy(final String buddyItem) {
		this.buddyItem = buddyItem;
	}

	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public FormItem getItem(final Model model) {
		if (buddyItem != null && errorMessage != null) {
			// create confirm password validator
			final MatchesFieldValidator validator = new MatchesFieldValidator();
			validator.setOtherField(buddyItem);
			validator.setErrorMessage(errorMessage);
			impl.setValidators(validator);
			// set confirm password length using buddy length
			final DataSourceField field = model.getField(buddyItem);
			Assert.notNull(field);
			logger.fine("setting " + Util.getClassSimpleName(this.getClass()) + " length to buddy: " + field.getLength());
			impl.setLength(field.getLength());
		}
		return super.getItem(model);
	}
}
