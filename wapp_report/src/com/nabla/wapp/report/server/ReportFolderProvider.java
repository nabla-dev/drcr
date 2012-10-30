/**
* Copyright 2011 nabla
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
package com.nabla.wapp.report.server;

import javax.servlet.ServletContext;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.nabla.wapp.report.shared.IReport;
import com.nabla.wapp.server.general.ServerParameterProvider;

/**
 * The <code></code> object is used to
 *
 */
public class ReportFolderProvider extends ServerParameterProvider implements Provider<String> {

	@Inject
	ReportFolderProvider(final ServletContext serverContext) {
		super(serverContext, IReport.REPORT_FOLDER);
	}

}
