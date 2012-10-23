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

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.transform.Transform;

/**
 * @author nabla
 *
 */
public class JavaSqlDateTransform implements Transform<Date> {

	static final DateFormat		dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	public Date read(String arg) throws PersistenceException {
		try {
			return new Date(dateFormatter.parse(arg).getTime());
		} catch (final Exception e) {
			throw new PersistenceException("unsupported date format: " + arg);
		}
	}

	@Override
	public String write(Date arg) {
		return arg.toString();
	}

}
