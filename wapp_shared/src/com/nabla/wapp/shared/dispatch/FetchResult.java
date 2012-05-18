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
package com.nabla.wapp.shared.dispatch;


/**
 * The <code></code> object is used to
 *
 */
public class FetchResult implements IResult {

	private Integer		startRow;
	private Integer		endRow;
	private Integer		totalRows;
	private String		jsonRecords;

	protected FetchResult() {}	// for serialization only

	public FetchResult(final Integer startRow, final Integer endRow, final Integer totalRows, final String records) {
		this.startRow = startRow;
		this.endRow = endRow;
		this.totalRows = totalRows;
		this.jsonRecords = records;
	}

	public String getRecords() {
		return jsonRecords;
	}

	public boolean isRange() {
		return startRow != null;
	}

	public Integer getTotalRows() {
		return totalRows;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public Integer getEndRow() {
		return endRow;
	}

}
