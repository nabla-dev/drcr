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
package com.nabla.wapp.server.xml;

import java.util.Map;

import org.simpleframework.xml.core.Validate;

import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla64
 *
 */
public abstract class TXmlNode<T> extends XmlNode {

	@Validate
	public void validate(Map session) throws DispatchException {
		final T ctx = getContext(session);
		doValidate(ctx, getErrorList(session));
	}

	protected abstract void doValidate(final T ctx, final IErrorList<Integer> errors) throws DispatchException;

}
