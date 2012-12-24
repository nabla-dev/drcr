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
package com.nabla.wapp.report.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nabla.wapp.shared.print.ReportFormats;

/**
 * @author nabla64
 *
 */
public class ReportOptions implements IsSerializable {

	private ReportFormats		format;
	private Boolean				outputAsFile;
	private String				locale;

	public ReportOptions() {}	// for serialization only

	public ReportOptions(final ReportFormats format, final Boolean outputAsFile, final String locale) {
		this.format = format;
		this.outputAsFile = outputAsFile;
		this.locale = locale;
	}

	public ReportFormats getFormat() {
		return format;
	}

	public Boolean getOutputAsFile() {
		return outputAsFile;
	}

	public String getLocale() {
		return locale;
	}

}