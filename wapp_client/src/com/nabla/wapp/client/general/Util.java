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
package com.nabla.wapp.client.general;

/**
 * @author nabla64
 *
 */
public abstract class Util {

	/**
	 * Get simple class name (on client side)
	 * @param clazz - class (cannot be null)
	 * @return Class name
	 */
	public static <T> String getClassSimpleName(Class<T> clazz) {
		Assert.argumentNotNull(clazz);

		final String name = clazz.getName();
		int lastDot = name.lastIndexOf('.');
		return (lastDot > -1) ? name.substring(lastDot + 1) : name;
	}

}
