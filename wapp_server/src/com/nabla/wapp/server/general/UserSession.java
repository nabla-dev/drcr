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
package com.nabla.wapp.server.general;

import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nabla.wapp.shared.auth.IRootUser;

/**
 * The <code></code> object is used to
 *
 */
public class UserSession {

	private static final Log		log = LogFactory.getLog(UserSession.class);
	public static final String	SESSION_PARAMETER_NAME = "user_session";

	private final Integer	userId;
	private final boolean	isRoot;
	private Locale			locale;
	private final String	sessionId;

	public UserSession(final Integer userId, boolean isRoot, final String locale) {
		this.userId = userId;
		this.isRoot = isRoot;
		try {
			this.locale = LocaleUtils.toLocale(locale);
		} catch (IllegalArgumentException __) {
			if (log.isErrorEnabled())
				log.error("unsupported locale '" + locale + "'");
			this.locale = Locale.UK;
		}
		this.sessionId = UUID.randomUUID().toString();

	}

	public Integer getUserId() {
		return userId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public Locale getLocale() {
		return locale;
	}

	public static String save(final HttpServletRequest request, final Integer userId, final String userName, final String locale) {
		final UserSession session = new UserSession(userId, IRootUser.NAME.equalsIgnoreCase(userName), locale);
		request.getSession().setAttribute(SESSION_PARAMETER_NAME, session);
		return session.getSessionId();
	}

	public static UserSession load(final HttpServletRequest request) {
		return (UserSession)request.getSession().getAttribute(SESSION_PARAMETER_NAME);
	}

	public static void clear(final HttpServletRequest request) {
		request.getSession().removeAttribute(SESSION_PARAMETER_NAME);
	}

}
