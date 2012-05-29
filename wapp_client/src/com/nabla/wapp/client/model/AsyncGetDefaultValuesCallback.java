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
import java.util.logging.Logger;

import com.nabla.wapp.client.general.AbstractAsyncCallback;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.JSOHelper;

/**
 * The <code></code> object is used to
 *
 */
public abstract class AsyncGetDefaultValuesCallback extends AbstractAsyncCallback<StringResult> {

	private static final Logger	logger = LoggerFactory.getLog(AsyncGetDefaultValuesCallback.class);

	@Override
	public void onSuccess(final StringResult result) {
		if (result != null) {
logger.fine("default values = \n" + result.get());
			final Record[] records = CModel.jsonToRecords(result.get());
			if (records.length == 1)
				onDefaultValues(JSOHelper.convertToMap(records[0].getJsObj()));
			else
				onDefaultValues(null);
		} else
			onDefaultValues(null);
	}

	public abstract void onDefaultValues(Map values);
}
