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
import com.google.gwt.uibinder.client.UiField;
import com.nabla.dc.client.model.fixed_asset.SplitAssetModel.Fields;
import com.nabla.dc.client.presenter.fixed_asset.SplitAssetWizard;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.mvp.binder.BindedBasicWizardPageDisplay;
import com.nabla.wapp.client.ui.WizardPage;
import com.nabla.wapp.shared.general.IHasValue;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;

/**
 * @author nabla
 *
 */
public class SplitAssetWizardCostPageUi extends BindedBasicWizardPageDisplay implements SplitAssetWizard.ICostPage {

	interface Binder extends UiBinder<WizardPage, SplitAssetWizardCostPageUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	private static final int		DEFAULT_COST_B = 1;

	@UiField
	Fields	fields;

	private final IHasValue<Integer>	valueB;
	private final Integer				totalValue;


	public SplitAssetWizardCostPageUi(final ValuesManager model) {
		super(model);
		create(uiBinder, this);

		totalValue = form.getIntegerField(fields.total()).getValue();
		valueB = form.getIntegerField(fields.costB());
		valueB.setValue(DEFAULT_COST_B);

		final IntegerItem valueA = (IntegerItem)form.getItem(fields.costA());
		Assert.notNull(valueA);
		valueA.setValue(totalValue - DEFAULT_COST_B);
		valueA.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				onValueChanged(event.getValue());
			}
		});
	}

	private void onValueChanged(final Object value) {
		Integer valueA;
		try {
			valueA = (value == null) ? 0 : Integer.valueOf(value.toString());
		} catch (Exception __) {
			valueA = 0;
		}
		if (valueA > totalValue)
			valueA = totalValue;
		valueB.setValue(totalValue - valueA);
	}

}
