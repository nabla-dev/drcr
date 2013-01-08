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

import java.util.Set;

import com.google.gwt.user.client.Command;
import com.nabla.wapp.shared.general.StringSet;
import com.nabla.wapp.shared.slot.ISlot1;


public interface IAuthSessionManager {

	boolean isValidSession();

	void IsUserInRole(final Integer objectId, final StringSet roles, final ISlot1<Set<String>> handler);
	boolean isRoot();

	void onLoginRequired(final Command callback);

}
