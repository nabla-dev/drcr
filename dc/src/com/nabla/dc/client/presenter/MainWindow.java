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
package com.nabla.dc.client.presenter;

import com.google.gwt.core.client.GWT;
import com.nabla.dc.client.ui.MainWindowUi;
import com.nabla.wapp.client.general.AbstractRunAsyncCallback;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.mvp.AbstractMainPresenter;
import com.nabla.wapp.client.mvp.AbstractPresenter;
import com.nabla.wapp.client.mvp.ICanvasDisplay;
import com.nabla.wapp.client.mvp.IMainDisplay;
import com.nabla.wapp.client.mvp.IPresenter;
import com.nabla.wapp.client.server.IUserSessionManager;
import com.nabla.wapp.client.server.UserSession;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot2;

/**
 * @author nabla
 *
 */
public class MainWindow extends AbstractMainPresenter<MainWindow.IDisplay> {

	public interface IDisplay extends IMainDisplay {
		void setClientArea(final ICanvasDisplay client);
	}

	private final IUserSessionManager	sessionManager;
	private IPresenter					client = null;

	public MainWindow(final IDisplay ui) {
		super(ui);
		sessionManager = Application.getInstance().getUserSessionManager();
		Assert.unique(MainWindow.class);
	}

	public MainWindow() {
		this(new MainWindowUi());
	}

	@Override
	public void bind() {
		super.bind();
		sessionManager.getUserSessionChangedSlots().connect(onUserSessionChanged);
		login();
	}

	@Override
	public void unbind() {
		if (this.client != null)
			this.client.unbind();
		super.unbind();
	}

	private void login() {
		sessionManager.clearSession();
		GWT.runAsync(new AbstractRunAsyncCallback() {

			@Override
			public void onSuccess() {
				final LoginWindow wnd = new LoginWindow();
				wnd.getLoginSlots().connect(new ISlot() {
					@Override
					public void invoke() {
						loadWorkspace();
					}
				});
				setClientArea(wnd);
			}

		});
	}

	private void loadWorkspace() {
		GWT.runAsync(new AbstractRunAsyncCallback() {

			@Override
			public void onSuccess() {
				final Workspace wnd = new Workspace(sessionManager.getSession().getName());
				wnd.getLogoutSlots().connect(new ISlot() {
					@Override
					public void invoke() {
						login();
					}
				});
				setClientArea(wnd);
			}

		});
	}

	private <D extends ICanvasDisplay> void setClientArea(final AbstractPresenter<D> client) {
		Assert.argumentNotNull(client);

		client.bind();
		getDisplay().setClientArea(client.getDisplay());
		if (this.client != null)
			this.client.unbind();
		this.client = client;
	}

	private final ISlot2<UserSession, UserSession> onUserSessionChanged = new ISlot2<UserSession, UserSession>() {
		@Override
		public void invoke(UserSession oldSession, UserSession newSession) {
			if (newSession == null)
				login();
			else if (oldSession != null && !oldSession.getName().equals(newSession.getName()))
				loadWorkspace();
		}
	};
}
