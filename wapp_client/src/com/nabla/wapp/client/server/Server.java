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
package com.nabla.wapp.client.server;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.AsyncProvider;
import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nabla.wapp.client.auth.SecureRequestBuilder;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.ui.ModalDialog;
import com.nabla.wapp.shared.auth.IDispatchRemoteServiceAsync;
import com.nabla.wapp.shared.auth.ILoginUserRemoteServiceAsync;
import com.nabla.wapp.shared.auth.RoleSetResult;
import com.nabla.wapp.shared.auth.command.IsUserInRole;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.IResult;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.general.StringSet;
import com.nabla.wapp.shared.signal.Signal2;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;
import com.nabla.wapp.shared.slot.ISlotManager2;

/**
 * @author nabla
 *
 */
public class Server implements IServer {

	public interface ILoginDialogUiProvider extends AsyncProvider<ILoginDialogUi, Throwable> {}

	private static final Logger							logger = LoggerFactory.getLog(Server.class);

	private final IDispatchRemoteServiceAsync			dispatcher;
	private final ILoginUserRemoteServiceAsync			login;
	private final ILoginDialogUiProvider				reLoginDialogUiFactory;
	private UserSession									session = null;
	private final Signal2<UserSession, UserSession>		sigUserSessionChanged = new Signal2<UserSession, UserSession>();

	public Server(final IDispatchRemoteServiceAsync dispatcher, final ILoginUserRemoteServiceAsync login, final ILoginDialogUiProvider reLoginDialogUiFactory) {
		this.dispatcher = dispatcher;
		this.login = login;
		this.reLoginDialogUiFactory = reLoginDialogUiFactory;
		Assert.unique(Server.class);
	}

	@Override
	public void clearSession() {
		if (session != null) {
			logger.log(Level.FINE, "clearing user session");
			saveSession(null);
			login.reset(new AsyncCallback<Void>() {
				@Override
				public void onFailure(@SuppressWarnings("unused") Throwable __) {}

				@Override
				public void onSuccess(@SuppressWarnings("unused") Void __) {}
			});
		}
	}

	@Override
	public UserSession getSession() {
		return session;
	}

	@Override
	public boolean isValidSession() {
		return session != null;
	}

	@Override
	public void createSession(final String userName, final String password, final AsyncCallback<UserSession> callback) {
		Assert.argumentNotNull(callback);

		login.execute(userName, password, new AsyncCallback<String> () {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(String sessionId) {
				if (sessionId != null) {
					logger.log(Level.FINE, "saving session");
					saveSession(new UserSession(userName, sessionId));
					callback.onSuccess(getSession());
				} else
					callback.onFailure(null);
			}
		});
	}

	@Override
	public <C extends IAction<R>, R extends IResult> void execute(final C cmd, final AsyncCallback<R> callback) {
		new SecureRequestBuilder(this, dispatcher.execute(session.getSessionId(), cmd, new AsyncCallback<IResult>() {

			@Override
			public void onFailure(final Throwable caught) {
				if (callback != null)
					callback.onFailure(caught);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(final IResult result) {
				if (callback != null)
					callback.onSuccess((R)result);
			}

		})).send();
	}

	@Override
	public void onLoginRequired(final Command callback) {
		reLoginDialogUiFactory.get(new Callback<ILoginDialogUi, Throwable>() {
			@Override
			public void onSuccess(final ILoginDialogUi ui) {
				ui.getSubmitSlots().connect(new ISlot() {
					@Override
					public void invoke() {
						final String oldUserName = new String(getSession().getName());
						createSession(ui.getUserName().getValue(), ui.getPassword().getValue(), new AsyncCallback<UserSession>() {
							@Override
							public void onFailure(@SuppressWarnings("unused") final Throwable caught) {
								ui.showError(CommonServerErrors.INVALID_USER_PASSWORD);
								ui.setSaving(false);
							}

							@Override
							public void onSuccess(final UserSession result) {
								ui.hide();
								logger.fine("password re-entered successfully");
								if (result.getName().equals(oldUserName)) {
									logger.fine("user has not changed. Simply re-run command");
									callback.execute();
								} else {
									logger.fine("user has changed. We need to reload client area");
									ModalDialog.hideAll();
								}
							}
						});
					}
				});
				ui.show();
			}

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.WARNING, "fail to download ReLoginDialogUi", caught);
			}
		});
	}

	@Override
	public ISlotManager2<UserSession, UserSession> getUserSessionChangedSlots() {
		return sigUserSessionChanged;
	}

	void saveSession(final UserSession userSession) {
		final UserSession oldSession = session;
	    session = userSession;
		sigUserSessionChanged.fire(oldSession, session);
	}

	@Override
	public void IsUserInRole(final Integer objectId, final StringSet roles, final ISlot1<Set<String>> handler) {
		Assert.notNull(session);

		execute(new IsUserInRole(objectId, roles), new AsyncCallback<RoleSetResult>() {
				@Override
				public void onFailure(final Throwable caught) {
					logger.log(Level.WARNING, "fail to find out if current user's permission. Assume none", caught);
					handler.invoke(null);
				}

				@Override
				public void onSuccess(final RoleSetResult result) {
					Assert.argumentNotNull(result);

					handler.invoke(result.get());
				}
		});
	}

	@Override
	public boolean isRoot() {
		return session != null && session.isRoot();
	}

}
