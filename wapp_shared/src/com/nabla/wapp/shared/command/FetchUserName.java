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
package com.nabla.wapp.shared.command;

import java.util.Collection;



public class FetchUserName extends AbstractFetchRecord {

	protected FetchUserName() {}	// for serialization only

	public FetchUserName(final Integer id) {
		super(id);
	}

	public FetchUserName(final Collection<Integer> ids) {
		super(ids);
	}

	public FetchUserName(final Integer... ids) {
		super(ids);
	}

}
