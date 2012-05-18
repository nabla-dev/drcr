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
package com.nabla.wapp.shared.model;

/**
 * @author nabla
 *
 */
public enum FilterOperators {

	equals("{0} = {1}"),
	notEqual("{0} != {1}"),
	greaterThan("{0} > {1}"),
	lessThan("{0} < {1}"),
	greaterOrEqual("{0} >= {1}"),
	lessOrEqual("{0} <= {1}"),
	between(false, 2, "{0} BETWEEN {1} AND {2}"),

	iEquals("{0} LIKE ''{1}''"),
	iNotEqual("{0} NOT LIKE ''{1}''"),
	contains("{0} LIKE BINARY ''%{1}%''"),
	startsWith("{0} LIKE BINARY ''{1}%''"),
	endsWith("{0} LIKE BINARY ''%{1}''"),
	iContains("{0} LIKE ''%{1}%''"),
	iStartsWith("{0} LIKE ''{1}%''"),
	iEndsWith("{0} LIKE ''%{1}''"),
	notContains("{0} NOT LIKE BINARY ''%{1}%''"),
	notStartsWith("{0} NOT LIKE BINARY ''{1}%''"),
	notEndsWith("{0} NOT LIKE BINARY ''%{1}''"),
	iNotContains("{0} NOT LIKE ''%{1}%''"),
	iNotStartsWith("{0} NOT LIKE ''{1}%''"),
	iNotEndsWith("{0} NOT LIKE ''%{1}''"),

	isNull(false, 0,"{0} IS NULL"),
	notNull(false, 0, "{0} IS NOT NULL"),
	equalsField("{0} = {1}"),
	notEqualField("{0} != {1}"),
	and(true, "AND"),
	not(true, "NOT ({0})"),
	or(true, "OR");

	private final boolean	applyToCreterion;
	private final int		parameterCount;
	private final String	sqlFormat;

	FilterOperators(boolean applyToCreterion, int parameterCount, final String sqlFormat) {
		this.applyToCreterion = applyToCreterion;
		this.parameterCount = parameterCount;
		this.sqlFormat = sqlFormat;
	}

	FilterOperators(boolean applyToCreterion, final String sqlFormat) {
		this(applyToCreterion, 1, sqlFormat);
	}

	FilterOperators(final String sqlFormat) {
		this(false, sqlFormat);
	}

	public boolean getApplyToCreterion() {
		return applyToCreterion;
	}

	public int getParameterCount() {
		return parameterCount;
	}

	public String getSqlFormat() {
		return sqlFormat;
	}

}
