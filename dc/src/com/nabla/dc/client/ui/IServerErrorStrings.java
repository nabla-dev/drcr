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

import com.nabla.wapp.shared.general.ICommonServerErrorStrings;


/**
 * @author nabla
 *
 */
public interface IServerErrorStrings extends ICommonServerErrorStrings {

	@DefaultStringValue("*** too many errors ***")
	String TOO_MANY_ERRORS();

	@DefaultStringValue("no data found")
	String NO_DATA_TO_IMPORT();
	
	@DefaultStringValue("unsupported column")
	String UNSUPPORTED_IMPORT_COLUMN();
	
	@DefaultStringValue("invalid tax code rate")
	String INVALID_TAX_CODE_RATE();
}
