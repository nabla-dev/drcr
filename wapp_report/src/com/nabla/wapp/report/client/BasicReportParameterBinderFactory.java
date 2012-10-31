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
package com.nabla.wapp.report.client;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.report.shared.ReportParameter;

/**
 * The <code></code> object is used to
 *
 */
public class BasicReportParameterBinderFactory implements IReportParameterBinderFactory {

	private static final Logger		log = LoggerFactory.getLog(BasicReportParameterBinderFactory.class);

	private final Map<String, IReportParameterBinderFactory>	factories = new HashMap<String, IReportParameterBinderFactory>();

	@Override
	public IReportParameterBinder create(ReportParameter parameter, Map<String, Object> defaultParameterValues) {
		final IReportParameterBinderFactory factory = factories.get(parameter.getModel());
		if (factory == null) {
			log.warning("fail to find factory for parameter model '" + parameter.getModel() + "'. Have you forgotten to register factory?");
			return null;
		}
		return factory.create(parameter, defaultParameterValues);
	}

	public <ModelType extends Enum<ModelType>>
	void registerFactory(final ModelType model, IReportParameterBinderFactory factory) {
		log.fine("registering report parameter binder factory for model '" + model.toString() + "'");
		factories.put(model.toString(), factory);
	}

}
