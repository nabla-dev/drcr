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
package com.nabla.dc.shared.command.fixed_asset;

import java.util.Collection;

import com.nabla.wapp.shared.command.AbstractFetchRecord;

/**
 * The <code></code> object is used to
 *
 */
public class FetchAssetRecord extends AbstractFetchRecord {

	FetchAssetRecord() {}

	public FetchAssetRecord(final Integer id) {
		super(id);
	}

	public FetchAssetRecord(final Collection<Integer> ids) {
		super(ids);
	}

	public FetchAssetRecord(final Integer... ids) {
		super(ids);
	}

}
