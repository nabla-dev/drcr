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

import com.google.gwt.user.client.Command;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.ValueCallback;

/**
 * The <code></code> object is used to
 *
 */
public interface IMessageBox {

	void setDefaultTitle(final String title);
	String getDefaultTitle();

	void info(final String message);
	void info(final String title, final String message);

	void error(final String message);
	<E extends Enum<E>> void error(final E error);
	void error(final String title, final String message);
	void error(final Throwable caught);

	void confirm(final String message, final BooleanCallback callback);
	void confirm(final String title, final String message, final BooleanCallback callback);
	void confirm(final String message, final Command callback);
	void confirm(final String title, final String message, final Command callback);

	void ask(final String message, final BooleanCallback callback);
	void ask(final String title, final String message, final BooleanCallback callback);
	void ask(final String message, final Command callback);
	void ask(final String title, final String message, final Command callback);

	void prompt(final String message, final ValueCallback callback);
	void prompt(final String title, final String message, final ValueCallback callback);

}
