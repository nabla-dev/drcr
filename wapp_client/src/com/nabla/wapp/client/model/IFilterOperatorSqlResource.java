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
public interface IFilterOperatorSqlResource extends ConstantsWithLookup {

	@DefaultStringValue("{0} = {1}")
	String equals();

	@DefaultStringValue("{0} LIKE '{1}'")
	String iEquals();

	@DefaultStringValue("{0} != {1}")
	String notEqual();

	@DefaultStringValue("{0} NOT LIKE '{1}'")
	String iNotEqual();

	@DefaultStringValue("{0} > {1}")
	String greaterThan();

	@DefaultStringValue("{0} < {1}")
	String lessThan();

	@DefaultStringValue("{0} >= {1}")
	String greaterOrEqual();

	@DefaultStringValue("{0} <= {1}")
	String lessOrEqual();

	@DefaultStringValue("{0} LIKE BINARY '{1}'")
	String contains();

	@DefaultStringValue("{0} LIKE BINARY '{1}%'")
	String startsWith();

	@DefaultStringValue("{0} LIKE BINARY '%{1}'")
	String endsWith();

	@DefaultStringValue("{0} LIKE '%{1}%'")
	String iContains();

	@DefaultStringValue("{0} LIKE '{1}%'")
	String iStartsWith();

	@DefaultStringValue("{0} LIKE '%{1}'")
	String iEndsWith();

	@DefaultStringValue("{0} NOT LIKE BINARY '%{1}%'")
	String notContains();

	@DefaultStringValue("{0} NOT LIKE BINARY '{1}%'")
	String notStartsWith();

	@DefaultStringValue("{0} NOT LIKE BINARY '%{1}'")
	String notEndsWith();

	@DefaultStringValue("{0} NOT LIKE '%{1}%'")
	String iNotContains();

	@DefaultStringValue("{0} NOT LIKE '{1}%'")
	String iNotStartsWith();

	@DefaultStringValue("{0} NOT LIKE '%{1}'")
	String iNotEndsWith();

	@DefaultStringValue("{0} IS NULL")
	String isNull();

	@DefaultStringValue("{0} IS NOT NULL")
	String notNull();

	@DefaultStringValue("{0} = {1}")
	String equalsField();

	@DefaultStringValue("{0} != {1}")
	String notEqualField();

	@DefaultStringValue("{0} BETWEEN {1} AND {2}")
	String between();

	@DefaultStringValue("AND")
	String and();

	@DefaultStringValue("NOT ({0})")
	String not();

	@DefaultStringValue("OR")
	String or();

}
