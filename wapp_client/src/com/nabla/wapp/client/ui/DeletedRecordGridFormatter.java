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
package com.nabla.wapp.client.ui;

import com.nabla.wapp.client.ui.ListGrid.ICellCSSTextFormatter;
import com.nabla.wapp.shared.model.IFieldReservedNames;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * @author nabla
 *
 */
public class DeletedRecordGridFormatter implements ICellCSSTextFormatter {

	@Override
	public String format(ListGridRecord record, @SuppressWarnings("unused") int rowNum, @SuppressWarnings("unused") int colNum) {
		return isRecordDeleted(record) ? "text-decoration: line-through" : null;
	}

	static public boolean isRecordDeleted(final ListGridRecord record) {
		return record != null && record.getAttributeAsBoolean(IFieldReservedNames.RECORD_DELETED) == true;
	}
}
