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

/**
 * The <code>ActionException</code> object is used to throw
 * a server error exception with an error code which will be
 * converted to a user error message on the client side
 * @author nabla
 */
public class ActionException extends DispatchException {

	private static final long serialVersionUID = 1L;

	public ActionException() {}	// for serialization only

	/**
	 * Constructor for the <code>ActionException</code> object
	 * @param error	- error code
	 */
	public ActionException(final String error) {
		super(error);
	}

	/**
	 * Constructor for the <code>ActionException</code> object
	 * @param error	- error code
	 */
	public <E extends Enum<E>> ActionException(final E error) {
		super(error);
	}

	/**
	 * Constructor for the <code>ActionException</code> object
	 * @param error		- error code
	 * @param details	- more details error information
	 */
	public ActionException(final String error, final String details) {
		super(error, details);
	}

	/**
	 * Constructor for the <code>ActionException</code> object
	 * @param error		- error code
	 * * @param details	- more details error information
	 */
	public <E extends Enum<E>> ActionException(final E error, final String details) {
		super(error, details);
	}

}
