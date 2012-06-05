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
package com.nabla.wapp.server.model;

import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * The <code></code> object is used to
 *
 */
public abstract class AbstractFetchHandler<A extends AbstractFetch> extends AbstractHandler<A, FetchResult> {

	protected AbstractFetchHandler() {
		super(false);
	}

}
