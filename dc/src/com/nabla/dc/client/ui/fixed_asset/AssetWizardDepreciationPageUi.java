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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.nabla.dc.client.presenter.fixed_asset.AssetWizard;
import com.nabla.dc.shared.model.fixed_asset.DepreciationPeriodRange;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.ui.WizardPage;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;

/**
 * @author nabla
 *
 */
public class AssetWizardDepreciationPageUi extends AssetWizardBasicPageUi implements AssetWizard.IDepreciationPage {

	interface Binder extends UiBinder<WizardPage, AssetWizardBasicPageUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	public AssetWizardDepreciationPageUi(final ValuesManager model, final DepreciationPeriodRange range) {
		super(model, uiBinder);
		// limit depreciation period using asset category depreciation period range
		final SpinnerItem depPeriod = (SpinnerItem)form.getField(IAsset.DEPRECIATION_PERIOD);
		Assert.notNull(depPeriod);
		depPeriod.setMin(range.getMin());
		depPeriod.setMax(range.getMax());
		final Integer value = (Integer)depPeriod.getValue();
		if (value == null)
			depPeriod.setValue(range.getMin());
		else if (value < range.getMin() || value > range.getMax())
			depPeriod.setValue(range.getMin());
		// show/hide opening details
		form.getItem(IAsset.OPENING_YEAR).setShowIfCondition(onOpeningChanged);
		form.getItem(IAsset.OPENING_MONTH).setShowIfCondition(onOpeningChanged);
		form.getItem(IAsset.OPENING_ACCUMULATED_DEPRECIATION).setShowIfCondition(onOpeningChanged);
		form.getItem(IAsset.OPENING_DEPRECIATION_PERIOD).setShowIfCondition(onOpeningChanged);
	}

	private final FormItemIfFunction onOpeningChanged = new FormItemIfFunction() {
        @Override
		public boolean execute(@SuppressWarnings("unused") FormItem item, @SuppressWarnings("unused") Object value, @SuppressWarnings("unused") DynamicForm f) {
        	return (Boolean)form.getValue(IAsset.OPENING);
        }
    };

}
