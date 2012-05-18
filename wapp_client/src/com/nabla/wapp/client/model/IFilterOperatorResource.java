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
package com.nabla.wapp.client.model;

import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * @author nabla
 *
 */
public interface IFilterOperatorResource extends ConstantsWithLookup {

	@DefaultStringValue("{0} = {1}")
	String equals();

	@DefaultStringValue("{0} = (disregard case) {1}")
	String iEquals();

	@DefaultStringValue("{0} != {1}")
	String notEqual();

	@DefaultStringValue("{0} != (disregard case) {1}")
	String iNotEqual();

	@DefaultStringValue("{0} > {1}")
	String greaterThan();

	@DefaultStringValue("{0} < {1}")
	String lessThan();

	@DefaultStringValue("{0} >= {1}")
	String greaterOrEqual();

	@DefaultStringValue("{0} <= {1}")
	String lessOrEqual();

	@DefaultStringValue("{0} contains {1}")
	String contains();

	@DefaultStringValue("{0} starts with {1}")
	String startsWith();

	@DefaultStringValue("{0} ends with {1}")
	String endsWith();

	@DefaultStringValue("iContains {1}")
	String iContains();

	@DefaultStringValue("{0} iStarts with {1}")
	String iStartsWith();

	@DefaultStringValue("{0} iEnds with {1}")
	String iEndsWith();

	@DefaultStringValue("{0} doesn't contain {1}")
	String notContains();

	@DefaultStringValue("{0} doesn't start with {1}")
	String notStartsWith();

	@DefaultStringValue("{0} doesn't end with {1}")
	String notEndsWith();

	@DefaultStringValue("{0} doesn't iContain {1}")
	String iNotContains();

	@DefaultStringValue("{0} doesn't iStart with {1}")
	String iNotStartsWith();

	@DefaultStringValue("{0} doesn't iEnd with {1}")
	String iNotEndsWith();

	@DefaultStringValue("{0} is NULL")
	String isNull();

	@DefaultStringValue("{0} isn't NULL")
	String notNull();

	@DefaultStringValue("{0} = {1}")
	String equalsField();

	@DefaultStringValue("{0} != {1}")
	String notEqualField();

	@DefaultStringValue("{0} between {1} and {2}")
	String between();

	@DefaultStringValue("and")
	String and();

	@DefaultStringValue("not ({0})")
	String not();

	@DefaultStringValue("or")
	String or();

}
