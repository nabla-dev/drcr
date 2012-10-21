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
package com.nabla.dc.client.ui.fixed_asset;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.nabla.dc.shared.model.fixed_asset.DepreciationPeriodRange;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.mvp.IWizardPageDisplay;
import com.nabla.wapp.client.mvp.binder.BindedBasicWizardPageDisplay;
import com.nabla.wapp.client.ui.WizardPage;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

/**
 * @author nabla
 *
 */
public class AssetWizardDepreciationPageUi extends BindedBasicWizardPageDisplay implements IWizardPageDisplay {

	interface Binder extends UiBinder<WizardPage, AssetWizardDepreciationPageUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	public AssetWizardDepreciationPageUi(final ValuesManager model, final DepreciationPeriodRange range, final Date acquisitionDate) {
		super(model);
		create(uiBinder, this);
		// limit depreciation period using asset category depreciation period range
		SpinnerItem depPeriod = (SpinnerItem)form.getField(IAsset.DEPRECIATION_PERIOD);
		Assert.notNull(depPeriod);
		depPeriod.setMin(range.getMin());
		depPeriod.setMax(range.getMax());
		Integer value = (Integer)depPeriod.getValue();
		if (value == null)
			depPeriod.setValue(range.getMin());
		else if (value < range.getMin() || value > range.getMax())
			depPeriod.setValue(range.getMin());
		// limit opening depreciation period using asset category depreciation period range
		depPeriod = (SpinnerItem)form.getField(IAsset.OPENING_DEPRECIATION_PERIOD);
		Assert.notNull(depPeriod);
		depPeriod.setMin(0);
		depPeriod.setMax(range.getMax());
		value = (Integer)depPeriod.getValue();
		if (value != null && (value < 0 || value > range.getMax()))
			depPeriod.setValue(0);
		// limit From date to acquisition date
		final DateItem dt = (DateItem)form.getField(IAsset.DEPRECIATION_FROM_DATE);
		dt.setStartDate(acquisitionDate);
		// show/hide depreciation details
		final CheckboxItem create = (CheckboxItem)form.getItem(IAsset.CREATE_TRANSACTIONS);
		create.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				onCreateTransactionsChanged((Boolean)event.getValue());
			}
		});
		onCreateTransactionsChanged(create.getValueAsBoolean());
	}

	private void onCreateTransactionsChanged(Boolean show) {
		showItem(IAsset.DEPRECIATION_FROM_DATE, show);
		showItem(IAsset.OPENING_ACCUMULATED_DEPRECIATION, show);
		showItem(IAsset.OPENING_DEPRECIATION_PERIOD, show);
		showItem(IAsset.RESIDUAL_VALUE, show);
	}

	private void showItem(final String name, Boolean show) {
		final FormItem item = form.getItem(name);
		if (show)
			item.show();
		else
			item.hide();
	}
}
