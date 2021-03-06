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
package com.nabla.dc.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.nabla.wapp.client.mvp.IWizardDisplay;
import com.nabla.wapp.client.mvp.binder.BindedWizardDialog;
import com.nabla.wapp.client.ui.ModalDialog;


public class ImportAccountWizardUi extends BindedWizardDialog implements IWizardDisplay {

	interface Binder extends UiBinder<ModalDialog, ImportAccountWizardUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	public ImportAccountWizardUi() {
		create(uiBinder, this);
	}

}
