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
package com.nabla.wapp.shared.general;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The <code>IntegerSet</code> object is used to
 *
 */
public class IntegerSet extends HashSet<Integer> implements IsSerializable {

	private static final long serialVersionUID = 1L;

	public IntegerSet() {}

	public IntegerSet(final Collection<Integer> values) {
		super(values);
	}

	public IntegerSet(final Integer... values) {
		super(Arrays.asList(values));
	}

}
