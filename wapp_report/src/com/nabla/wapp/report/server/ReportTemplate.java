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
package com.nabla.wapp.report.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.report.data.oda.jdbc.IConnectionFactory;
import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;

import com.lowagie.text.pdf.PdfReader;
import com.nabla.wapp.report.shared.parameter.ParameterGroup;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.InternalErrorException;
import com.nabla.wapp.shared.print.ReportFormats;

public class ReportTemplate {

	private final Integer			id;
	private final IReportRunnable	impl;

	ReportTemplate(final Integer id, final IReportRunnable impl) {
		this.id = id;
		this.impl = impl;
	}

	public String getName() {
		return impl.getReportName();
	}

	public Report generate(final Connection conn, final Map<String, Object> parameters, final ReportFormats format, final Locale locale) throws InternalErrorException {
		return new Report(getFileName(format), render(conn, parameters, format, locale), format);
	}

	public PdfReader generatePdf(final Connection conn, final Map<String, Object> parameters, final Locale locale) throws InternalErrorException {
		try {
			return new PdfReader(render(conn, parameters, ReportFormats.PDF, locale));
		} catch (IOException e) {
			throw new InternalErrorException(Util.formatInternalErrorDescription(e));
		}
	}

	public ParameterGroup getParameters(final Map<String, Object> defaultParameterValues) throws InternalErrorException {
		final ParameterGroup parameters = new ParameterGroup(id.toString(), getName());
		final ReportParameterListFactory task = new ReportParameterListFactory(id, getReportEngine().createGetParameterDefinitionTask(impl));
		try {
			task.generate(parameters, defaultParameterValues);
		} finally {
			task.close();
		}
		return parameters;
	}

	public List<SelectionValue> getParameterValueMap(final String parameterName, final Map<String, Object> otherParameterValues) {
		final ReportParameterListFactory task = new ReportParameterListFactory(id, getReportEngine().createGetParameterDefinitionTask(impl));
		try {
			task.setParameterValues(otherParameterValues);
			return task.getParameterValueMap(parameterName);
		} finally {
			task.close();
		}
	}

	private String getFileName(final ReportFormats format) {
		return getName() + "." + format.getFileExtension();
	}

	@SuppressWarnings("unchecked")
	private InputStream render(final Connection conn, final Map<String, Object> parameters, final ReportFormats format, final Locale locale) throws InternalErrorException {
		final IRunAndRenderTask t = getReportEngine().createRunAndRenderTask(impl);
		try {
			t.setLocale(locale);
			t.getAppContext().put("OdaJDBCDriverPassInConnection", conn);
			t.getAppContext().put(IConnectionFactory.CLOSE_PASS_IN_CONNECTION, new Boolean(false));
			if (!parameters.isEmpty()) {
				for (Map.Entry<String, Object> parameter : parameters.entrySet())
					t.setParameterValue(parameter.getKey(), parameter.getValue());
				if (!t.validateParameters()) {
					final List<EngineException> errors = t.getErrors();
					throw new InternalErrorException(errors.isEmpty() ? ("invalid parameters for report '" + getName() + "'") : errors.get(0).getLocalizedMessage());
				}
			}
			final IRenderOption options = getRenderOptions(format);
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			options.setOutputStream(out);
			t.setRenderOption(options);
			t.run();
			return new ByteArrayInputStream(out.toByteArray());
		} catch (EngineException e) {
			throw new InternalErrorException(Util.formatInternalErrorDescription(e));
		} finally {
			t.close();
		}
	}

	private IRenderOption getRenderOptions(final ReportFormats format) {
		switch (format) {
			case XLS: {
				final EXCELRenderOption options = new EXCELRenderOption();
				options.setOutputFormat(format.getFileExtension());
				return options;
			}
			case CSV:
			case XML:
			case DOC: {
				final IRenderOption options = new RenderOption();
				options.setOutputFormat(format.getFileExtension());
				return options;
			}
			case PDF:
			default: {
				final PDFRenderOption options = new PDFRenderOption();
				//	options.setOption(IPDFRenderOption.PAGE_OVERFLOW, new Integer(IPDFRenderOption.FIT_TO_PAGE_SIZE) );
				options.setOutputFormat(PDFRenderOption.OUTPUT_FORMAT_PDF);
				return options;
			}
		}
	}

	private IReportEngine getReportEngine() {
		return impl.getReportEngine();
	}
}
