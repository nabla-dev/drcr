/**
* Copyright 2010 nabla
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
package com.nabla.fixed_assets.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.nabla.fixed_assets.client.presenter.AssetWizard;
import com.nabla.fixed_assets.shared.AcquisitionTypes;
import com.nabla.fixed_assets.shared.model.IAsset;
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
public class AssetWizardAcquisitionPageUi extends AssetWizardBasicPageUi implements AssetWizard.IAcquisitionPage {

	interface Binder extends UiBinder<WizardPage, AssetWizardBasicPageUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	@Inject
	public AssetWizardAcquisitionPageUi(@Assisted final ValuesManager model, @Assisted Integer maxDepPeriod) {
		super(model, uiBinder);
		// limit initial depreciation period using asset category depreciation period range
		final SpinnerItem depPeriod = (SpinnerItem)form.getField(IAsset.INITIAL_DEP_PERIOD);
		Assert.notNull(depPeriod);
		depPeriod.setMin(0);
		depPeriod.setMax(maxDepPeriod);
		final Integer value = (Integer)depPeriod.getValue();
		if (value == null)
			depPeriod.setValue(0);
		else if (value < 0 || value > maxDepPeriod)
			depPeriod.setValue(0);
		// show / hide initial accumulated depreciation and period according to acquisition type
		form.getItem(IAsset.INITIAL_ACCUM_DEP).setShowIfCondition(onAcquisitionTypeChanged);
		form.getItem(IAsset.INITIAL_DEP_PERIOD).setShowIfCondition(onAcquisitionTypeChanged);
	}

	@Override
	public boolean canCreateTransaction() {
		return form.getValueAsString(IAsset.ACQUISITION_DATE) != null;
	}

	private final FormItemIfFunction onAcquisitionTypeChanged = new FormItemIfFunction() {
        @Override
		public boolean execute(@SuppressWarnings("unused") FormItem item, @SuppressWarnings("unused") Object value, @SuppressWarnings("unused") DynamicForm f) {
        	return AcquisitionTypes.valueOf(form.getValueAsString(IAsset.ACQUISITION_TYPE)) == AcquisitionTypes.TRANSFER;
        }
    };

}
