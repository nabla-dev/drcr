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
import com.nabla.wapp.shared.signal.Signal;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlotManager;

/**
 * @author nabla
 *
 */
public class LoginWindow extends AbstractCanvasPresenter<LoginWindow.IDisplay> {

	public interface IDisplay extends ICanvasDisplay, ILoginUi {}

	private static final Logger		logger = LoggerFactory.getLog(LoginWindow.class);
	private static final AutoLogin	autoLogin = new AutoLogin();
	private final Signal			sigLogin = new Signal();

	public LoginWindow(final IDisplay display) {
		super(display);

	}

	public LoginWindow() {
		this(new LoginWindowUi());
	}

	public ISlotManager getLoginSlots() {
		return sigLogin;
	}

	@Override
	protected void onBind() {
		registerHandler(display.getSubmitSlots().connect(new ISlot() {
			@Override
			public void invoke() {
				onLogin();
			}
		}));
		if (autoLogin.get()) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					display.getUserName().setValue("root");
					display.getPassword().setValue("allaccess");
					onLogin();
				}
			});
		}
	}

	private void onLogin() {
		Application.getInstance().getUserSessionManager().createSession(display.getUserName().getValue(), display.getPassword().getValue(), new AsyncCallback<UserSession>() {

			@Override
			public void onFailure(final Throwable caught) {
				logger.log(Level.WARNING, "login failed", caught);
				display.showError(CommonServerErrors.INVALID_USER_PASSWORD);
				display.setSaving(false);
			}

			@Override
			public void onSuccess(@SuppressWarnings("unused") final UserSession result) {
				sigLogin.fire();
			}

		});
	}

}
