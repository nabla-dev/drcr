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
package com.nabla.wapp.shared.general;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The <code></code> object is used to
 *
 */
public class SelectionDelta implements IsSerializable {

	private IntegerSet	additions;
	private IntegerSet	removals;

	public SelectionDelta() {}	// for serialization only

	public SelectionDelta(IntegerSet additions, IntegerSet removals) {
		this.additions = additions;
		this.removals = removals;
	}

	public IntegerSet getAdditions() {
		return additions;
	}

	public IntegerSet getRemovals() {
		return removals;
	}

	public void add(final Integer id) {
		if (removals == null || !removals.remove(id)) {
			if (additions == null)
				additions = new IntegerSet();
			additions.add(id);
		}
	}

	public void remove(final Integer id) {
		if (additions == null || !additions.remove(id)) {
			if (removals == null)
				removals = new IntegerSet();
			removals.add(id);
		}
	}

	public boolean isEmpty() {
		return (additions == null || additions.isEmpty()) && (removals == null || removals.isEmpty());
	}

	public boolean isAdditions() {
		return additions != null && !additions.isEmpty();
	}

	public boolean isRemovals() {
		return removals != null && !removals.isEmpty();
	}

}
