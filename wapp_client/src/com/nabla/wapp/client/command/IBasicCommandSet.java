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
package com.nabla.wapp.client.command;

import java.util.Set;

import com.nabla.wapp.client.auth.IAuthSessionManager;
import com.nabla.wapp.shared.general.StringSet;


public interface IBasicCommandSet {
	StringSet getRequiredRoles();
	void applyRoles(final Set<String> userRoles, boolean isRoot);

	void updateUi(final IAuthSessionManager sessionManager);
	void updateUi();

	void setObjectId(final Integer objectId);
}
