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

import java.lang.reflect.Method;
import java.sql.SQLException;

import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.server.json.JsonResponse;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * The <code></code> object is used to
 *
 */
public abstract class AbstractAddHandler<A extends IAction<StringResult>> extends AbstractHandler<A, StringResult> {

	private Method		validateRecord;

	@SuppressWarnings("unchecked")
	protected AbstractAddHandler() {
		super(true);
		try {
			final Class<A> recordClass = (Class<A>)Util.getFirstGenericDeclaration(this.getClass());
			validateRecord = recordClass.getMethod("validate", IErrorList.class);
		} catch (Throwable __) {
			validateRecord = null;
		}
	}

	@Override
	public StringResult execute(final A record, final IUserSessionContext ctx) throws DispatchException, SQLException {
		validate(record, ctx);
		final JsonResponse json = new JsonResponse();
		json.putId(add(record, ctx));
		return json.toStringResult();
	}

	protected void validate(final A record, @SuppressWarnings("unused") final IUserSessionContext ctx) throws DispatchException {
		if (validateRecord != null) {
			final ValidationException x = new ValidationException();
			try {
				validateRecord.invoke(record, x);
			} catch (Exception e) {
				Util.throwInternalErrorException(e);
			}
			if (!x.isEmpty())
				throw x;
		}
	}

	abstract protected int add(final A record, final IUserSessionContext ctx) throws DispatchException, SQLException;

}
