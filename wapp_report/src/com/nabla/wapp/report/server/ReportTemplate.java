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

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
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

import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.InternalErrorException;
import com.nabla.wapp.shared.print.ReportFormats;

/**
 * @author nabla64
 *
 */
public class ReportTemplate {

	private final IReportRunnable	impl;

	ReportTemplate(final IReportRunnable impl) {
		this.impl = impl;
	}

	public String getName() {
		return impl.getReportName();
	}

	@SuppressWarnings("unchecked")
	public Report generate(final Connection conn, final Map<String, Object> parameters, final ReportFormats format, final Locale locale) throws InternalErrorException {
		final IRunAndRenderTask t = getReportEngine().createRunAndRenderTask(impl);
		try {
			t.setLocale(locale);
			t.getAppContext().put("OdaJDBCDriverPassInConnection", conn);
			t.getAppContext().put(IConnectionFactory.CLOSE_PASS_IN_CONNECTION, new Boolean(false));
			if (!parameters.isEmpty()) {
				for (Map.Entry<String, Object> parameter : parameters.entrySet())
					t.setParameterValue(parameter.getKey(), parameter.getValue());
				if (!t.validateParameters())
					Util.throwInternalErrorException("invalid parameters for report '" + getName() + "'");
			}
			final IRenderOption options = getRenderOptions(format);
			final ByteArrayOutputStream tmp = new ByteArrayOutputStream();
			options.setOutputStream(tmp);
			t.setRenderOption(options);
			t.run();
			return new Report(getFileName(format), tmp, format);
		} catch (EngineException e) {
			Util.throwInternalErrorException(e);
		} finally {
			t.close();
		}
		return null;
	}

	private String getFileName(final ReportFormats format) {
		return getName() + "." + format.getFileExtension();
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
/*
	public Set<Integer> saveReports(final Connection conn, final Map<String, InputStream> reports, final ReportFormats format, final Boolean outputAsFile) throws SQLException, InternalErrorException {
		final Set<Integer> ids = new HashSet<Integer>();
		if (format == ReportFormats.PDF) {
			// merge PDFs into one

		} else {
			for (Map.Entry<String, InputStream> report : reports.entrySet())
				ids.add(saveReport(conn, report.getKey(), report.getValue(), format, outputAsFile));
		}
		return ids;
	}
*/

	private IReportEngine getReportEngine() {
		return impl.getReportEngine();
	}
}
