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

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Tercio F. Gaudencio Filho
 * modified by Nabla
 *
 */
public abstract class JSHelper {

	public static final String	OBJECT_OPEN = "{";
	public static final String	OBJECT_CLOSE = "}";

	public static final String	FIELD_OPEN = "";
	public static final String	FIELD_CLOSE = ",";

	public static final String	ARRAY_OPEN = "[";
	public static final String	ARRAY_CLOSE = "]";

	/**
	 * Dump a JavaScriptObject. This method don't dump functions inside the JavaScriptObject
	 *
	 * @param objToDump		Object to dump
	 * @return String Dumped JavaScript Object
	 */
	public static String dump(final JavaScriptObject objToDump)
	{
		return dump(objToDump, false);
	}

	/**
	 * Dump a JavaScriptObject.
	 *
	 * @param objToDump		Object to dump
	 * @param dumpFunction	If true will dump it's functions
	 * @return String Dumped JavaScript Object
	 */
	public static String dump(final JavaScriptObject objToDump, final boolean dumpFunction)
	{
		return OBJECT_OPEN + "\n" + dump(objToDump, "", dumpFunction) + OBJECT_CLOSE + "\n";
	}

	private static native String dump(JavaScriptObject objToDump, String ident, boolean dumpFunction) /*-{
		if(ident.length >= 20)
		{
			return "** Too much recursion **";
		}

		var result = "";
		ident += "\t";

		if(typeof(objToDump) == 'object')
		{
			for(var field in objToDump)
			{
				var value = objToDump[field];

				if(value == null)
				{
					result += ident + @com.nabla.wapp.client.general.JSHelper::FIELD_OPEN + field + "(?): null" + @com.nabla.wapp.client.general.JSHelper::FIELD_CLOSE + "\n";
				}
				else if(typeof(value) == 'object')
				{
					var typeDef = "";
					if(value.length)
					{
						 typeDef += "(array)";
						 var separatorOpen = @com.nabla.wapp.client.general.JSHelper::ARRAY_OPEN;
						 result += ident + field + typeDef + ":" + separatorOpen + "\n";
					}

					var separatorClose = @com.nabla.wapp.client.general.JSHelper::ARRAY_CLOSE;

					if(objToDump.length)
					{
						field = "Index: " + field;
						separatorOpen = @com.nabla.wapp.client.general.JSHelper::OBJECT_OPEN;
						result += ident + separatorOpen + field + typeDef + ":\n";
						separatorClose = @com.nabla.wapp.client.general.JSHelper::OBJECT_CLOSE;
					}

					result += @com.nabla.wapp.client.general.JSHelper::dump(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;Z)(value, ident, dumpFunction);
					result += ident + separatorClose + ",\n";
				}
				else
				{
					if(((typeof(value) == 'function') && dumpFunction) || (typeof(value) != 'function'))
					{
						result += ident + @com.nabla.wapp.client.general.JSHelper::FIELD_OPEN + field + "(" + typeof(value) + "): '";
						value = String(value).replace(/\n/gi, "\n" + ident + "\t");
						result += value + "'" + @com.nabla.wapp.client.general.JSHelper::FIELD_CLOSE + "\n";
					}
				}
			}
		}
		else
		{
			result += ident + @com.nabla.wapp.client.general.JSHelper::FIELD_OPEN + typeof(objToDump) + ": '?'" + @com.nabla.wapp.client.general.JSHelper::FIELD_CLOSE + "\n";
		}
		return result;
	}-*/;

	/**
	 * Copy attribute
	 * @param src - object source (cannot be null)
	 * @param dest - object destination (cannot be null)
	 * @param attr - attribute name
	 */
	public static native void copyAttribute(JavaScriptObject src, JavaScriptObject dest, String attr) /*-{
		dest[attr] = src[attr];
	}-*/;

	/**
     * Test if attribute exists
     *
     * @param jsObj - js object
     * @param attribute - name of attribute
     * @return true if attribute exists or false otherwise
     */
	public static native boolean isAttribute(final JavaScriptObject jsObj, final String attribute) /*-{
		if (jsObj == null && jsObject === undefined)
			return false;
		var value = jsObj[attribute];
		return (value == null || value === undefined) ? false : true;
	}-*/;

	/**
     * SmartGWT getAttributeAsBoolean() is buddy i.e. always return false if attribute not set!
     *
     * @param jsObj - js object
     * @param attribute - name of attribute
     * @return null or the boolean value
     */
	public static Boolean getAttributeAsBoolean(JavaScriptObject jsObj, String attribute) {
		return isAttribute(jsObj, attribute) ? getAttributeAsBooleanImpl(jsObj, attribute) : null;
	}

	/**
     * Get boolean value orSmartGWT getAttributeAsBoolean() is buddy i.e. always return false if attribute not set!
     *
     * @param jsObj - js object
     * @param attribute - name of attribute
     * @return null or the boolean value
     */
	private static native boolean getAttributeAsBooleanImpl(final JavaScriptObject jsObj, final String attribute) /*-{
		return jsObj[attribute];
	}-*/;
}
