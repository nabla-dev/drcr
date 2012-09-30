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
package com.nabla.wapp.client.model;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.shared.command.GetFormDefaultValues;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.slot.ISlot;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.form.ValuesManager;

/**
 * @author nabla
 *
 */
public class WizardValuesManager<T extends Record> extends ValuesManager {

	private static final Logger	log = LoggerFactory.getLog(WizardValuesManager.class);

	public WizardValuesManager(final CModel<T> model) {
		setDataSource(model);
	}

	public void editNewRecord(@Nullable final Integer objectId, final String defaultValuesGroup, final ISlot callback) {
		Application.getInstance().getDispatcher().execute(new GetFormDefaultValues(objectId, defaultValuesGroup), new AsyncGetDefaultValuesCallback() {

			@Override
			public void onFailure(Throwable caught) {
				log.log(Level.WARNING, "fail to load '" + defaultValuesGroup + "' default values", caught);
				editNewRecord();
				callback.invoke();
			}

			@Override
			public void onDefaultValues(Map values) {
				if (values != null)
					editNewRecord(values);
				else {
					log.log(Level.FINE, "no asset default values");
					editNewRecord();
				}
				callback.invoke();
			}
		});
	}

	public void editRecord(final ISlot callback) {
		fetchData(null, new DSCallback() {
			@Override
			public void execute(DSResponse response, @SuppressWarnings("unused") Object rawData, @SuppressWarnings("unused") DSRequest request) {
				if (response.getStatus() == DSResponse.STATUS_SUCCESS)
					callback.invoke();
			}
		});
	}

	public T getRecord() {
		@SuppressWarnings("unchecked")
		CModel<T> model = (CModel<T>) this.getDataSource();
		return model.getRecordFactory().get(this.getJsObj());
	}

}
