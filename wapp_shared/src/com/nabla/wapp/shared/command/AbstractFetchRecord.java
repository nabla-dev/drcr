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

import com.nabla.wapp.shared.general.IntegerSet;
import com.nabla.wapp.shared.general.Nullable;



public abstract class AbstractFetchRecord extends AbstractFetch {

	private IntegerSet	ids;

	protected AbstractFetchRecord() {}	// for serialization only

	protected AbstractFetchRecord(final Integer id) {
		this.ids = new IntegerSet(id);
	}

	protected AbstractFetchRecord(final Collection<Integer> ids) {
		this.ids = new IntegerSet(ids);
	}

	protected AbstractFetchRecord(final Integer... ids) {
		this.ids = new IntegerSet(ids);
	}

	public IntegerSet getIds() {
		return ids;
	}

	public @Nullable Integer getId() {
		return ids.isEmpty() ? null : ids.iterator().next();
	}

}
