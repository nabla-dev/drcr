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

import com.nabla.wapp.shared.dispatch.FetchResult;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.general.Nullable;


public abstract class AbstractFetch implements IAction<FetchResult> {

	private @Nullable Integer	startRow;
	private @Nullable Integer	endRow;
	private @Nullable String	orderBy;
	private @Nullable String	filter;

	protected AbstractFetch() {}	// for serialization only

	public boolean setRange(@Nullable final Integer startRow, @Nullable final Integer endRow) {
		if (startRow != null && startRow >= 0 && endRow != null && endRow >= startRow) {
			this.startRow = startRow;
			this.endRow = endRow;
			return true;
		}
		return false;
	}

	public boolean isRange() {
		return startRow != null;
	}

	public @Nullable Integer getStartRow() {
		return startRow;
	}

	public @Nullable Integer getEndRow() {
		return endRow;
	}

	public @Nullable String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(@Nullable final String orderBy) {
		this.orderBy = orderBy;
	}

	public void setFilter(@Nullable final String filter) {
		this.filter = filter;
	}

	public @Nullable String getFilter() {
		return filter;
	}

}
