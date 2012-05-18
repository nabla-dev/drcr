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
package com.nabla.wapp.server.general;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author nabla
 *
 */
public final class Assert {

	private static final Log	log = LogFactory.getLog(Assert.class);

	/**
	 * Test if object is not null
	 * @param ref - object to test
	 * @throws NullPointerException if object is null
	 */
	public static <T> void notNull(final T ref) throws NullPointerException {
		if (log.isDebugEnabled()) {
			if (ref == null)
				throw new NullPointerException("assertNotNull failed");
		}
	}

	/**
	 * Test if object is not null
	 * @param ref - object to test
	 * @param message - exception message
	 * @throws NullPointerException if object is null
	 */
	public static <T> void notNull(final T ref, final String message) throws NullPointerException {
		if (log.isDebugEnabled()) {
			if (ref == null)
				throw new NullPointerException(message);
		}
	}

	/**
	 * Test if object (which should be a method argument) is not null
	 * @param ref - object to test
	 * @throws IllegalArgumentException if object is null
	 */
	public static <T> void argumentNotNull(final T ref) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			if (ref == null)
				throw new IllegalArgumentException("assertArgumentNotNull failed");
		}
	}

	/**
	 * Test if object (which should be a method argument) is not null
	 * @param ref - object to test
	 * @param message - exception message
	 * @throws IllegalArgumentException if object is null
	 */
	public static <T> void argumentNotNull(final T ref, final String message) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			if (ref == null)
				throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Test argument state is true
	 * @param state - boolean to test
	 * @throws IllegalArgumentException if state is false
	 */
	public static void argument(final boolean state) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			if (!state)
				throw new IllegalArgumentException("assertArgument failed");
		}
	}

	/**
	 * Test argument state is true
	 * @param state - boolean to test
	 * @param message - exception message
	 * @throws IllegalArgumentException if state is false
	 */
	public static void argument(final boolean state, final String message) throws IllegalArgumentException {
		if (log.isDebugEnabled()) {
			if (!state)
				throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Test condition is true
	 * @param state - condition to test
	 * @throws IllegalStateException if condition is false
	 */
	public static void state(final boolean state) throws IllegalStateException {
		if (log.isDebugEnabled()) {
			if (!state)
				throw new IllegalStateException("assertState failed");
		}
	}

	/**
	 * Test condition is true
	 * @param state - condition to test
	 * @param message - exception message
	 * @throws IllegalStateException if condition is false
	 */
	public static void state(final boolean state, final String message) throws IllegalStateException {
		if (log.isDebugEnabled()) {
			if (!state)
				throw new IllegalStateException(message);
		}
	}

}
