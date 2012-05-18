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
package com.nabla.dc.client.ui;

import com.google.gwt.i18n.client.Constants;


/**
 * @author nabla
 *
 */
public interface ITextResource extends Constants {

	@DefaultStringValue("Dr | (Cr)")
	String applicationTitle();

	@DefaultStringValue("Duplicate records are not allowed")
	String duplicateEntry();

	@DefaultStringValue("You need to select only deleted users to run this command")
	String noDeletedUserSelected();

	@DefaultStringValue("You need to select a deleted company to run this command")
	String noDeletedCompanySelected();

	@DefaultStringValue("You need to select a deleted tax code to run this command")
	String noDeletedTaxRateSelected();
}
