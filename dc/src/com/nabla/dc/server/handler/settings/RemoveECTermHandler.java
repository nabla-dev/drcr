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
package com.nabla.dc.server.handler.settings;

import com.nabla.dc.shared.command.settings.RemoveECTerm;
import com.nabla.dc.shared.model.IECTerm;
import com.nabla.wapp.server.model.AbstractSoftRemoveHandler;

/**
 * @author nabla
 *
 */
public class RemoveECTermHandler extends AbstractSoftRemoveHandler<RemoveECTerm> {

	public RemoveECTermHandler() {
		super(IECTerm.TABLE);
	}

}
