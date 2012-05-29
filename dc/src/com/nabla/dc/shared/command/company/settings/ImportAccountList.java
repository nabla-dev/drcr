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
package com.nabla.dc.shared.command.company.settings;

import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;



/**
 * @author nabla
 *
 */
public class ImportAccountList implements IAction<StringResult> {

	private static final long serialVersionUID = 1L;
	private Integer		companyId;
	private Boolean		rowHeader;
	private String		overwrite;
	
	ImportAccountList() {}
		
	public ImportAccountList(final Integer companyId, final Boolean rowHeader, final String overwrite) {
		this.companyId = companyId;
		this.rowHeader = rowHeader;
		this.overwrite = overwrite;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public Boolean getRowHeader() {
		return rowHeader;
	}
	
	public String getOverwrite() {
		return overwrite;
	}
}
