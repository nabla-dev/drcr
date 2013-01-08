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
package com.nabla.dc.server.handler.company;

import com.nabla.dc.shared.command.company.RestoreAccount;
import com.nabla.dc.shared.model.company.IAccount;
import com.nabla.wapp.server.model.AbstractRestoreHandler;


public class RestoreAccountHandler extends AbstractRestoreHandler<RestoreAccount> {

	public RestoreAccountHandler() {
		super(IAccount.TABLE);
	}

}
