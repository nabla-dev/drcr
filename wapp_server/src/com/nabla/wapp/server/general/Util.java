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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public abstract class Util {

	private static final Log	log = LogFactory.getLog(Util.class);

	public static Class getFirstGenericDeclaration(Class clazz) {
		return getGenericDeclaration(clazz, 0);
	}

	public static Class getGenericDeclaration(Class clazz, int position) {
	    Assert.argumentNotNull(clazz);
	    Assert.state(position >= 0);

	    Type classGenType = clazz.getGenericSuperclass();

	    // special CGLIB workaround -- get generic superclass of superclass
	    if (clazz.getName().contains("$$EnhancerByCGLIB$$")) {
	        classGenType = clazz.getSuperclass().getGenericSuperclass();
	    }

	    if (classGenType instanceof ParameterizedType) {
	        Type[] params = ((ParameterizedType)classGenType).getActualTypeArguments();

	        if ((params != null) && (params.length > position)) {
	            return (Class) params[position];
	        }
	    }

	    for (Type ifGenType : clazz.getGenericInterfaces()) {
	        if (ifGenType instanceof ParameterizedType) {
	            Type[] params = ((ParameterizedType)ifGenType).getActualTypeArguments();

	            if ((params != null) && (params.length > position)) {
	                return (Class) params[position];
	            }
	        }
	    }

	    if (log.isErrorEnabled())
	    	log.error("fail to find class of generic parameter " + position + " of class '" + clazz.getSimpleName() + "'");

	    return null;
	}

	public static String formatInternalErrorDescription(final Throwable x) {
		if (x == null)
			return null;
		final Writer buffer = new StringWriter();
	    final PrintWriter pw = new PrintWriter(buffer);
	    x.printStackTrace(pw);
	    return buffer.toString();
	}

	public static String formatInternalErrorDescription(final String cause) {
		return Thread.currentThread().getStackTrace()[2].toString() + ": " + cause;
	}

	public static Calendar dateToCalendar(final Date dt) {
		final Calendar ret = new GregorianCalendar();
		ret.setTime(dt);
		return ret;
	}

	public static java.sql.Date calendarToSqlDate(final Calendar dt) {
		return new java.sql.Date(dt.getTime().getTime());
	}
}
