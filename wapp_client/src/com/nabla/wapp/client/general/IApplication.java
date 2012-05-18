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

import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.ui.HasWidgets;
import com.nabla.wapp.client.server.IDispatchAsync;
import com.nabla.wapp.client.server.IUserSessionManager;
import com.nabla.wapp.client.ui.IMessageBox;

/**
 * @author nabla64
 *
 */
public interface IApplication {

	/**
	 * Application entry point
	 * @param container	- main window
	 */
	void execute(final HasWidgets container);

	/**
	 * Get default dispatch server
	 * @return IDispatchAsync instance
	 */
	IDispatchAsync getDispatcher();

	/**
	 * Get default user session manager
	 * @return IUserSessionManager instance
	 */
	IUserSessionManager getUserSessionManager();

	/**
	 * Get message box
	 * @return IMessageBox instance
	 */
	IMessageBox getMessageBox();

	/**
	 * Convert server error code to localized user message
	 * @param errorCode - server error code
	 * @return user message
	 */
	String getLocalizedError(final String errorCode);

	/**
	 * Convert server error code to localized user message
	 * @param error - server error code
	 * @return user message
	 */
	<E extends Enum<E>> String getLocalizedError(final E error);

	/**
	 * Get user message associated to server error codes
	 * @return resource
	 */
	ConstantsWithLookup getServerErrorResource();

}
