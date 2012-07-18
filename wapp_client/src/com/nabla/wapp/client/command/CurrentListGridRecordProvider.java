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
package com.nabla.wapp.client.command;

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.wapp.client.model.IRecordFactory;
import com.nabla.wapp.client.ui.ListGrid;
import com.smartgwt.client.data.Record;

/**
 * @author FNorais
 *
 */
public class CurrentListGridRecordProvider<R extends Record> implements ICurrentRecordProvider<R> {

	private final ListGrid			list;
	private final IRecordFactory<R>	factory;

	public CurrentListGridRecordProvider(final ListGrid list, final IRecordFactory<R> factory) {
		this.list = list;
		this.factory = factory;
	}

	@Override
	public R getCurrentRecord() {
		final JavaScriptObject record = list.getCurrentRecordData();
		return (record == null) ? null : factory.get(record);
	}

}
