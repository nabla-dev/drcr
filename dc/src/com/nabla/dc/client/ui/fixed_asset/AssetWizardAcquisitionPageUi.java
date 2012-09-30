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
import com.nabla.dc.shared.model.fixed_asset.AcquisitionTypes;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.mvp.binder.BindedBasicWizardPageDisplay;
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
public class AssetWizardAcquisitionPageUi extends BindedBasicWizardPageDisplay implements AssetWizard.IAcquisitionPage {

	interface Binder extends UiBinder<WizardPage, AssetWizardAcquisitionPageUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	public AssetWizardAcquisitionPageUi(final ValuesManager model, Integer maxDepPeriod) {
		super(model);
		create(uiBinder, this);
		// limit initial depreciation period using asset category depreciation period range
		final SpinnerItem depPeriod = (SpinnerItem)form.getField(IAsset.INITIAL_DEPRECIATION_PERIOD);
		Assert.notNull(depPeriod);
		depPeriod.setMin(0);
		depPeriod.setMax(maxDepPeriod);
		final Integer value = (Integer)depPeriod.getValue();
		if (value == null)
			depPeriod.setValue(0);
		else if (value < 0 || value > maxDepPeriod)
			depPeriod.setValue(0);
		// show / hide initial accumulated depreciation and period if TRANSFER
		form.getItem(IAsset.INITIAL_ACCUMULATED_DEPRECIATION).setShowIfCondition(onAcquisitionTypeChanged);
		form.getItem(IAsset.INITIAL_DEPRECIATION_PERIOD).setShowIfCondition(onAcquisitionTypeChanged);
	}

	private final FormItemIfFunction onAcquisitionTypeChanged = new FormItemIfFunction() {
        @Override
		public boolean execute(@SuppressWarnings("unused") FormItem item, @SuppressWarnings("unused") Object value, @SuppressWarnings("unused") DynamicForm f) {
        	return AcquisitionTypes.valueOf(form.getValueAsString(IAsset.ACQUISITION_TYPE)) == AcquisitionTypes.TRANSFER;
        }
    };

}
