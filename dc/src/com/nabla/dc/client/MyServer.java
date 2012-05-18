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
package com.nabla.dc.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.nabla.dc.client.ui.ReLoginDialogUi;
import com.nabla.wapp.client.server.ILoginDialogUi;
import com.nabla.wapp.client.server.Server;
import com.nabla.wapp.shared.auth.IDispatchRemoteService;
import com.nabla.wapp.shared.auth.IDispatchRemoteServiceAsync;
import com.nabla.wapp.shared.auth.ILoginUserRemoteService;
import com.nabla.wapp.shared.auth.ILoginUserRemoteServiceAsync;

/**
 * The <code></code> object is used to
 *
 */
public class MyServer extends Server {

	public MyServer() {
		super(getDispatchService(), getLoginService(), loginDialogUiProvider);
	};

	private static IDispatchRemoteServiceAsync getDispatchService() {
		return GWT.create(IDispatchRemoteService.class);
	}

	private static ILoginUserRemoteServiceAsync getLoginService() {
		return GWT.create(ILoginUserRemoteService.class);
	}

	private final static ILoginDialogUiProvider loginDialogUiProvider = new ILoginDialogUiProvider() {

		@Override
		public void get(final Callback<? super ILoginDialogUi, ? super Throwable> callback) {
			GWT.runAsync(new RunAsyncCallback() {

				@Override
				public void onSuccess() {
					callback.onSuccess(new ReLoginDialogUi());
				}

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}

			});
		}

	};
}

