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


/**
 * The <code>ArgumentList</code> object is used to
 * build list comma separated list of values
 * @author nabla
 */
public class ArgumentList {

	private String	separator;
	private String	result = "";

	/**
	 * Constructor for <code>ArgumentList</code> object
	 * default separator is a comma ','
	 */
	public ArgumentList() {
		this.setSeparator(", ");
	}

	/**
	 * Constructor for <code>ArgumentList</code> object
	 * @param separator	- separator string to use
	 */
	public ArgumentList(final String separator) {
		this.setSeparator(separator);
	}

	/**
	 * Add argument to end of list
	 * @param value
	 */
	public void add(final String value) {
		if (result.isEmpty())
			result = value;
		else
			result += separator + value;
	}

	/**
	 * Convert list of arguments to a string i.e. arg1, arg2, arg3...
	 * @return Arguments string
	 */
	@Override
	public String toString() {
		return result;
	}

	/**
	 * Set separator string to use
	 * @param separator	- separator string to use
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * Get current serator string
	 * @return Seperator string or null if none setup
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * Test if list is empty
	 * @return 'true' is empty, 'false' otherwise
	 */
	public boolean isEmpty() {
		return result.isEmpty();
	}

}
