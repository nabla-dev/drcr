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
package com.nabla.wapp.client.auth;

import java.util.logging.Logger;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Command;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.shared.auth.LoginRequiredException;


public class SecureRequestBuilder implements RequestCallback {

	private static final Logger			logger = LoggerFactory.getLog(SecureRequestBuilder.class);

	private final IAuthSessionManager	sessionManager;
	private final RequestBuilder		requestFactory;
	private final RequestCallback		callback;

	public SecureRequestBuilder(final IAuthSessionManager sessionManager, final RequestBuilder requestFactory) {
		Assert.argumentNotNull(sessionManager);
		Assert.argumentNotNull(requestFactory);

		this.sessionManager = sessionManager;
		this.requestFactory = requestFactory;
		this.callback = requestFactory.getCallback();
		requestFactory.setCallback(this);
	}

	public void send() {
		try {
			requestFactory.send();
		} catch (final RequestException e) {
			if (sessionManager.isValidSession()) {
				if (callback != null)
					callback.onError(null, e.getCause());
			} else {
				logger.warning("user logged out before request to server returned!!!");
				// drop request
			}
		}
	}

	@Override
	public void onError(final Request request, final Throwable exception) {
		if (sessionManager.isValidSession()) {
			if (exception instanceof LoginRequiredException)
				onLoginRequired();
			else if (callback != null)
				callback.onError(request, exception);
		} else {
			logger.warning("user logged out before request to server returned!!!");
			// drop request
		}
	}

	@Override
	public void onResponseReceived(final Request request, final Response response) {
		if (sessionManager.isValidSession()) {
			if (isResponseException(response)) {
				logger.warning(response.getText());
				onLoginRequired();
			} else if (callback != null)
				callback.onResponseReceived(request, response);
		} else {
			logger.warning("user logged out before request to server returned!!!");
			// drop request
		}
	}

	private static boolean isResponseException(final Response response) {
		final String data = response.getText();
		return data.startsWith("//EX[") && data.contains(".LoginRequiredException");
	}

	private void onLoginRequired() {
		logger.fine("session timeout. Need to prompt user with password again.");
		sessionManager.onLoginRequired(new Command() {
			@Override
			public void execute() {
				send();
			}
		});
	}
}
