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
package com.nabla.dc.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Singleton;
import com.nabla.wapp.server.general.UserSession;
import com.nabla.wapp.shared.auth.ILoginUserRemoteService;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.model.IUser;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
@Singleton
public class DummyLoginUserService extends RemoteServiceServlet implements ILoginUserRemoteService {

	private static final long		serialVersionUID = 1L;

	public DummyLoginUserService() {}

	@Override
	public String execute(String userName, String password) {
		final ValidationException x = new ValidationException();
		try {
			IUser.NAME_CONSTRAINT.validate("user name", userName, x);
			IUser.PASSWORD_CONSTRAINT.validate("password", password, x);
		} catch (DispatchException e) {}
		if (x.isEmpty())
			return UserSession.save(this.getThreadLocalRequest(), 1, "root");
		return null;
	}

	@Override
	public Void reset() {
		UserSession.clear(this.getThreadLocalRequest());
		return null;
	}

}
