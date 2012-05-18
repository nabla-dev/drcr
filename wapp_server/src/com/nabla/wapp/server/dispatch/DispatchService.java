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
package com.nabla.wapp.server.dispatch;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.auth.IUserSessionContextProvider;
import com.nabla.wapp.server.general.UserSession;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.auth.IDispatchRemoteService;
import com.nabla.wapp.shared.auth.LoginRequiredException;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.IResult;
import com.nabla.wapp.shared.dispatch.UnsupportedActionException;

/**
 * @author nabla
 *
 */
@Singleton
public class DispatchService extends RemoteServiceServlet implements IDispatchRemoteService {

	private static final long	serialVersionUID = 1L;
	private static final Log	log = LogFactory.getLog(DispatchService.class);

	private final ActionHandlerRegister			handlers;
	private final IUserSessionContextProvider	ctxFactory;

	@Inject
	public DispatchService(final ActionHandlerRegister handlers, final IUserSessionContextProvider ctxFactory) {
		this.handlers = handlers;
		this.ctxFactory = ctxFactory;
	}

	@Override
	public IResult execute(String sessionId, final IAction<?> action) throws DispatchException {
		final UserSession session = UserSession.load(this.getThreadLocalRequest());
		if (session == null || !session.getSessionId().equals(sessionId))
			throw new LoginRequiredException();
		return dispatch(action, session);
	}

	private <A extends IAction<R>, R extends IResult> R dispatch(A action, final UserSession session) throws DispatchException {
		if (log.isTraceEnabled())
			log.trace("handling command '" + action.getClass().getSimpleName() + "'");
		IActionHandler<A, R> handler = handlers.findHandler(action);
		if (handler == null)
			throw new UnsupportedActionException(action);
		try {
			final IUserSessionContext ctx = ctxFactory.get(session, handler.requireWriteContext());
			try {
			/*	if (!handler.isAuthorized(action, ctx))
					throw new AccessDeniedException();*/
				return handler.execute(action, ctx);
			} finally {
				ctx.close();
			}
		} catch (final SQLException e) {
			if (log.isErrorEnabled())
				log.error("SQL error " + e.getErrorCode() + "-"	+ e.getSQLState(), e);
			Util.throwInternalErrorException(e);
		} catch (DispatchException e) {
			if (log.isErrorEnabled())
				log.error("internal error", e);
			throw e;
		} catch (Throwable e) {
			if (log.isErrorEnabled())
				log.error("internal error", e);
			Util.throwInternalErrorException(e);
		}
		return null;
    }

}
