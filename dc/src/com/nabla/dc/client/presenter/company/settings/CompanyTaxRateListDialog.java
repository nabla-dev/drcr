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
package com.nabla.dc.client.presenter.company.settings;

import com.nabla.dc.client.ui.company.settings.CompanyTaxRateListDialogUi;
import com.nabla.wapp.client.mvp.AbstractTopPresenter;
import com.nabla.wapp.client.mvp.ITopDisplay;
import com.nabla.wapp.shared.slot.ISlot;

/**
 * @author nabla
 *
 */
public class CompanyTaxRateListDialog extends AbstractTopPresenter<CompanyTaxRateListDialog.IDisplay> {

	public interface IDisplay extends ITopDisplay {
		void savePreferences();
	}

	public CompanyTaxRateListDialog(final IDisplay ui) {
		super(ui);
	}

	public CompanyTaxRateListDialog(final Integer companyId, final String companyName) {
		super(new CompanyTaxRateListDialogUi(companyId, companyName));
	}

	@Override
	protected void onBind() {
		display.getHideSlots().connect(new ISlot() {
			@Override
			public void invoke() {
				display.savePreferences();
			}
		});
	}

}
