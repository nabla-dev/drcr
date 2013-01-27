/**
* Copyright 2013 nabla
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
package com.nabla.wapp.report.client.parameter;

import java.util.HashMap;
import java.util.Map;

import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.Util;
import com.nabla.wapp.report.shared.parameter.IParameter;
import com.nabla.wapp.report.shared.parameter.ParameterValueList;

/**
 * The <code></code> object is used to
 *
 */
public class ParameterBinderFactoryRegister {

	private final Map<Class, IParameterBinderFactory<? extends IParameter>>	factories = new HashMap<Class, IParameterBinderFactory<? extends IParameter>>();

	public ParameterBinderFactoryRegister() {
//		register(new StringEditBoxParameterBinderFactory());
//		register(new DateEditBoxParameterBinderFactory());
		register(new IntegerComboBoxParameterBinderFactory());
		register(new ParameterGroupBinderFactory());
		register(new CascadingParameterGroupBinderFactory());
	}

	public IParameterBinder create(final IParameter parameter, final ParameterValueList parameterValues) {
		return doCreate(parameter, parameterValues);
	}

	private <P extends IParameter> void register(final IParameterBinderFactory<P> factory) {
		factories.put(factory.getParameterClass(), factory);
	}

	private <P extends IParameter> IParameterBinder doCreate(final P parameter, final ParameterValueList parameterValues) {
		final IParameterBinderFactory<P> factory = getFactory(parameter);
		Assert.notNull(factory, "fail to find factory for parameter '" + Util.getClassSimpleName(parameter.getClass()) + "'. Have you forgotten to register factory?");
		return factory.create(parameter, parameterValues);
	}

	@SuppressWarnings("unchecked")
	private <F extends IParameterBinderFactory<P>, P extends IParameter> F getFactory(final P parameter) {
		final IParameterBinderFactory<?> factory = factories.get(parameter.getClass());
		return (F) factory;
	}

}
