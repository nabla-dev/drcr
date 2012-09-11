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
package com.nabla.fixed_assets.client.model;

import com.nabla.wapp.client.model.Model;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.form.ValuesManager;

/**
 * @author nabla
 *
 */
public class WizardData extends ValuesManager {

	private Record	editedRecord;

	public WizardData(final Model model) {
		this(model, null);
	}

	public WizardData(final Model model, final Record record) {
		this.setDataSource(model);
		if (record == null)
			editNewRecord();
		else
			editRecord(record);
	}

	public Record getEditedRecord() {
		return editedRecord;
	}

	@Override
	public void editRecord(final Record record) {
		this.editedRecord = record;
		super.editRecord(record);
	}

	public Model getModel() {
		return (Model)getDataSource();
	}
}
