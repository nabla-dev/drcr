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
package com.nabla.wapp.client.general;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.nabla.wapp.client.server.IDispatchAsync;
import com.nabla.wapp.client.server.IServer;
import com.nabla.wapp.client.server.IUserSessionManager;
import com.nabla.wapp.client.ui.IMessageBox;
import com.nabla.wapp.shared.general.CommonServerErrors;

/**
 * @author nabla
 *
 */
public abstract class Application implements IApplication, GWT.UncaughtExceptionHandler {

	private static final Logger			logger = LoggerFactory.getLog(Application.class);
	private static IApplication			application;
	private final IMessageBox			msgBox;
	private final ConstantsWithLookup	serverErrors;
	private final IServer				server;

	protected Application(final IMessageBox msgBox, final IServer server, final ConstantsWithLookup serverErrors) {
		this.msgBox = msgBox;
		this.serverErrors = serverErrors;
		this.server = server;
		application = this;
		GWT.setUncaughtExceptionHandler(this);
	}

	/**
	 * Get global application instance
	 * @return
	 */
	public static IApplication getInstance() {
		return application;
	}

	/**
	 * Get default dispatch server
	 * @return IDispatchAsync instance
	 */
	@Override
	public IDispatchAsync getDispatcher() {
		return server;
	}

	/**
	 * Get default user session manager
	 * @return IUserSessionManager instance
	 */
	@Override
	public IUserSessionManager getUserSessionManager() {
		return server;
	}

	/**
	 * Get message box
	 * @return MessageBox instance
	 */
	@Override
	public IMessageBox getMessageBox() {
		return msgBox;
	}

	/**
	 * Convert server error code to localized user message
	 * @param errorCode - server error code
	 * @return user message
	 */
	@Override
	public String getLocalizedError(final String errorCode) {
		if (errorCode == null || errorCode.isEmpty() || errorCode.length() >= 256) {
			logger.log(Level.SEVERE,"no exception error message! Show internal error message instead");
			return serverErrors.getString(CommonServerErrors.INTERNAL_ERROR.toString());
		}
		try {
			return serverErrors.getString(errorCode);
		} catch (final Throwable e) {
			logger.log(Level.SEVERE,"'" + errorCode + "' is not a valid server error code");
			return serverErrors.getString(CommonServerErrors.INTERNAL_ERROR.toString());
		}
	}

	/**
	 * Convert server error code to localized user message
	 * @param errorCode - server error code
	 * @return user message
	 */
	@Override
	public <E extends Enum<E>> String getLocalizedError(final E error) {
		return getLocalizedError(error.toString());
	}

	/**
	 * Get user message associated to server error codes
	 * @return resource
	 */
	@Override
	public ConstantsWithLookup getServerErrorResource() {
		return serverErrors;
	}

	/**
	 * Log uncaught exception for debug purposes
	 */
	@Override
	public void onUncaughtException(Throwable e) {
		logger.log(Level.SEVERE, "uncaught exception", e);
	}

}
