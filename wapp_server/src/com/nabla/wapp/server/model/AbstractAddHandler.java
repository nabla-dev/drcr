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

import java.sql.SQLException;

import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.server.json.JsonResponse;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * The <code></code> object is used to
 *
 */
public abstract class AbstractAddHandler<A extends IAction<StringResult>> extends AbstractHandler<A, StringResult> {

	protected AbstractAddHandler() {
		super(true);
	}

	@Override
	public StringResult execute(final A record, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final JsonResponse json = new JsonResponse();
		json.putId(add(record, ctx));
		return json.toStringResult();
	}

	abstract protected int add(final A record, final IUserSessionContext ctx) throws DispatchException, SQLException;

}
