/**
* Copyright 2013 nabla
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

import com.nabla.wapp.shared.validator.TextLengthConstraint;

/**
 * @author nabla
 *
 */
public interface IReport extends IReportTable {

	static final TextLengthConstraint		NAME_CONSTRAINT = new TextLengthConstraint(1, 255, true);

	static final String					REPORT_FILE = "report_id";

	static final String					REPORT_FOLDER = "reports_folder";
	static final String					REPORT_DOCUMENT_PREFIX = "report";
	static final String					REPORT_DOCUMENT_EXT = ".jprint";

	static final String					NEW_REPORT_PREFERENCE_GROUP = "new_report";
	static final String					PRINT_REPORT_PREFERENCE_GROUP = "report_parameter";

	static final String					EXTENSION_COMPILED_REPORT = "jasper";
	static final String					EXTENSION_SOURCE_REPORT = "jrxml";
	static final String					EXTENSION_STYLE_LIBRARY = "jrtx";
	static final String					EXTENSION_SCRIPT = "jar";

	static final String					HEADER_PERMISSION = "auth";
	static final String					HEADER_PARAMETER_TYPE = "model";

}
