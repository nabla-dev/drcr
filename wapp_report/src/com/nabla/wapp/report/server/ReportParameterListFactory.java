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
package com.nabla.wapp.report.server;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefn;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IParameterGroupDefn;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;

import com.nabla.wapp.report.shared.parameter.IParameter;
import com.nabla.wapp.report.shared.parameter.IntegerComboBoxParameter;
import com.nabla.wapp.report.shared.parameter.IntegerParameterValueMap;
import com.nabla.wapp.report.shared.parameter.ParameterGroup;
import com.nabla.wapp.shared.dispatch.InternalErrorException;

public class ReportParameterListFactory {

	public enum DataType {
		STRING, INTEGER, FLOAT, BOOLEAN, DATE
	}

	public enum ControlType {
		TEXT_BOX, CHECK_BOX, COMBO_BOX, RADIO_BUTTON
	}

	private static final Log					log = LogFactory.getLog(ReportParameterListFactory.class);
	private final IGetParameterDefinitionTask	impl;

	ReportParameterListFactory(final IGetParameterDefinitionTask impl) {
		this.impl = impl;
	}

	public void close() {
		impl.close();
	}

	public ParameterGroup get(final ParameterGroup parameters, final Map<String, Object> defaultParameterValues) throws InternalErrorException {
		return addAll(parameters, impl.getParameterDefns(true).iterator(), defaultParameterValues);
	}

	protected ParameterGroup addAll(final ParameterGroup parameters, final Iterator iter, final Map<String, Object> defaultParameterValues) throws InternalErrorException {
		while (iter.hasNext()) {
			final IParameterDefnBase parameter = (IParameterDefnBase) iter.next();
			if (log.isDebugEnabled())
				log.debug("parameter " + parameter.getName() + " type=" + parameter.getParameterType());
			switch (parameter.getParameterType()) {
				case IParameterDefnBase.CASCADING_PARAMETER_GROUP:
					addGroup(parameters, (IParameterGroupDefn) parameter, true, defaultParameterValues);
					break;
				case IParameterDefnBase.PARAMETER_GROUP:
					addGroup(parameters, (IParameterGroupDefn) parameter, false, defaultParameterValues);
					break;
				case IParameterDefnBase.SCALAR_PARAMETER:
					addScalar(parameters, (IScalarParameterDefn) parameter, defaultParameterValues);
					break;
				default:
					break;
			}
		}
		return parameters;
	}

	protected void addGroup(final ParameterGroup parameters, final IParameterGroupDefn p, boolean cascading, final Map<String, Object> defaultParameterValues) throws InternalErrorException {
		final ParameterGroup group = addAll(new ParameterGroup(p.getName(), p.getPromptText(), cascading), p.getContents().iterator(), defaultParameterValues);
		if (parameters.getCascading() || group.needUserInput())
			parameters.add(group);
	}

	protected boolean addScalar(final ParameterGroup parameters, final IScalarParameterDefn p, final Map<String, Object> defaultParameterValues) throws InternalErrorException {
		if (p.isHidden() || !p.isRequired())
			return false;
		final ControlType control = getParameterControlType(p);
		final DataType dataType = getParameterDataType(p);
		if (defaultParameterValues.containsKey(p.getName())) {
			if (!parameters.getCascading())
				return false;

		} else {
			switch (control) {
			case TEXT_BOX:
				break;
			case CHECK_BOX:
				break;
			case COMBO_BOX:
				switch (dataType) {
					case BOOLEAN:
						break;
					case DATE:
						break;
					case FLOAT:
						break;
					case INTEGER:
						parameters.add(createIntegerComboBoxParameter(p));
						return true;
					case STRING:
						break;
					default:
						break;
				}
				break;
			case RADIO_BUTTON:
				break;
			default:
				break;
			}
		}
		return false;
	}

	private static DataType getParameterDataType(final IScalarParameterDefn p) throws InternalErrorException {
		switch (p.getDataType()) {
		case IParameterDefn.TYPE_BOOLEAN:
			return DataType.BOOLEAN;
		case IParameterDefn.TYPE_DATE:
			return DataType.DATE;
		case IParameterDefn.TYPE_DECIMAL:
		case IParameterDefn.TYPE_FLOAT:
			return DataType.FLOAT;
		case IParameterDefn.TYPE_INTEGER:
			return DataType.INTEGER;
		case IParameterDefn.TYPE_STRING:
			return DataType.STRING;
		default:
			break;
		}
		throw new InternalErrorException("report paramater type '" + p.getDataType() + "' unsupported for parameter '" + p.getName() + "'");

	}

	private static ControlType getParameterControlType(final IScalarParameterDefn p) throws InternalErrorException {
		if (p.getDataType() == IParameterDefn.TYPE_BOOLEAN)
			return ControlType.CHECK_BOX;
		switch (p.getControlType()) {
		case IScalarParameterDefn.TEXT_BOX:
			return ControlType.TEXT_BOX;
		case IScalarParameterDefn.LIST_BOX:
			return ControlType.COMBO_BOX;
		case IScalarParameterDefn.RADIO_BUTTON:
			return ControlType.RADIO_BUTTON;
		default:
			break;
		}
		throw new InternalErrorException("report paramater type '" + p.getDataType() + "' unsupported for parameter '" + p.getName() + "'");
	}

	private IParameter createIntegerComboBoxParameter(final IScalarParameterDefn p) {
		final IntegerParameterValueMap valueMap = new IntegerParameterValueMap();
		for (Iterator iter = impl.getSelectionList(p.getName()).iterator(); iter.hasNext(); ) {
			final IParameterSelectionChoice e = (IParameterSelectionChoice) iter.next();
			valueMap.put((Integer)e.getValue(), e.getLabel());
		}
		return new IntegerComboBoxParameter(p.getName(), p.getPromptText(), valueMap);
	}

}
