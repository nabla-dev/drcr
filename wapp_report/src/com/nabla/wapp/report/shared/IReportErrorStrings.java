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

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface IReportErrorStrings extends ConstantsWithLookup {

	@DefaultStringValue("invalid role defined for report")
	String REPORT_DESIGN_INVALID_ROLE();

	@DefaultStringValue("invalid category defined for report")
	String REPORT_DESIGN_INVALID();

	@DefaultStringValue("no report design found in zip file")
	String ADD_REPORT_NO_REPORT_DESIGN_FOUND();

	@DefaultStringValue("invalid report parameter model")
	String INVALID_REPORT_PARAMETER_MODEL();
}
