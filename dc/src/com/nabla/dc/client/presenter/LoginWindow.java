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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nabla.dc.client.ui.LoginWindowUi;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.general.AutoLogin;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.mvp.AbstractCanvasPresenter;
import com.nabla.wapp.client.mvp.ICanvasDisplay;
import com.nabla.wapp.client.server.UserSession;
import com.nabla.wapp.client.ui.ILoginUi;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.slot.ISlot;


public class LoginWindow extends AbstractCanvasPresenter<LoginWindow.IDisplay> {

	public interface IDisplay extends ICanvasDisplay, ILoginUi {}

	private static final Logger		log = LoggerFactory.getLog(LoginWindow.class);
	private static final AutoLogin	autoLogin = new AutoLogin();
	private final ISlot				onSuccess;

	public LoginWindow(final ISlot onSuccess) {
		super(new LoginWindowUi());
		this.onSuccess = onSuccess;
	}

	@Override
	public void bind() {
		super.bind();
		registerHandler(getDisplay().getSubmitSlots().connect(new ISlot() {
			@Override
			public void invoke() {
				onLogin();
			}
		}));
		if (autoLogin.get()) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					getDisplay().getUserName().setValue("root");
					getDisplay().getPassword().setValue("allaccess");
					onLogin();
				}
			});
		}
	}

	private void onLogin() {
		Application.getInstance().getUserSessionManager().createSession(getDisplay().getUserName().getValue(), getDisplay().getPassword().getValue(), new AsyncCallback<UserSession>() {

			@Override
			public void onFailure(final Throwable caught) {
				log.log(Level.WARNING, "login failed", caught);
				getDisplay().showError(CommonServerErrors.INVALID_USER_PASSWORD);
				getDisplay().setSaving(false);
			}

			@Override
			public void onSuccess(@SuppressWarnings("unused") final UserSession result) {
				onSuccess.invoke();
			}

		});
	}

}
