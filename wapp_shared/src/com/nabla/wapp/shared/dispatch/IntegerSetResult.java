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
package com.nabla.wapp.shared.dispatch;

import com.nabla.wapp.shared.general.IntegerSet;


/**
 * The <code>IntegerResult</code> object is used to return an Integer
 * from a call to the server
 * @author nabla
 *
 */
public class IntegerSetResult extends BasicResult<IntegerSet> {

	IntegerSetResult() {}	// for serialization only

	/**
	 * Constructor for the <code>IntegerResult</code> object
	 * @param value	- value to transmit
	 */
	public IntegerSetResult(final IntegerSet value) {
		super(value);
	}
}
