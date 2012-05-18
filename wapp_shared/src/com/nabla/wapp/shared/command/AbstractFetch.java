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

/**
 * @author nabla
 *
 */
public abstract class AbstractFetch implements IAction<FetchResult> {

	private Integer		startRow;
	private Integer		endRow;
	private String		orderBy;
	private String		filter;

	protected AbstractFetch() {}	// for serialization only

	public boolean setRange(final Integer startRow, final Integer endRow) {
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

	public Integer getStartRow() {
		return startRow;
	}

	public Integer getEndRow() {
		return endRow;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	public void setFilter(final String filter) {
		this.filter = filter;
	}

	public String getFilter() {
		return filter;
	}

}
