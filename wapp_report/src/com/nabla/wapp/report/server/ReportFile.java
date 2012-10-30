/**
* Copyright 2011 nabla
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nabla.wapp.report.shared.IReport;
import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.InternalErrorException;


/**
 * The <code></code> object is used to
 *
 */
public class ReportFile extends ReportFileName {

	public interface IHeader {
		String getTitle();
		String getPermission();
		JRParameter[] getParameters();
	}

	private static final Log	log = LogFactory.getLog(ReportFile.class);

	private final String		folder;

	public ReportFile(final String folder) {
		this.folder = folder;
	}

	public ReportFile(final String folder, final String fileName) {
		super(fileName);
		this.folder = folder;
	}

	public String getFolder() {
		return folder;
	}

	public String getPath() {
		return folder + getFileName();
	}

	public String getCompiledPath() {
		return folder + getCompiledFileName();
	}

	public String getSourcePath() {
		return folder + getSourceFileName();
	}

	public boolean needToBeCompiled() {
		if (!isSource())
			return false;
		final File file = new File(getCompiledPath());
		return !file.exists();
	}

	public JasperDesign loadSource() throws InternalErrorException {
		if (log.isDebugEnabled())
			log.debug("loading report source '" + getFileName() + "'");
		try {
			return JRXmlLoader.load(getSourcePath());
		} catch (JRException e) {
			if (log.isErrorEnabled())
				log.error("failed to load report", e);
			Util.throwInternalErrorException(e);
		}
		return null;
	}

	public JasperReport loadTemplate() throws InternalErrorException {
		if (log.isDebugEnabled())
			log.debug("loading report template '" + getFileName() + "'");
		try {
			return (JasperReport) JRLoader.loadObjectFromFile(getCompiledPath());
		} catch (final JRException e) {
			if (log.isErrorEnabled())
				log.error("failed to load report template", e);
			Util.throwInternalErrorException(e);
		}
		return null;
	}

	public JRBaseReport load() throws InternalErrorException {
		if (isSource())
			return loadSource();
		if (isCompiled())
			return loadTemplate();
		if (log.isDebugEnabled())
			log.debug("can only load source or compiled report, not '" + getFileName() + "'");
		return null;
	}

	public void compile() throws InternalErrorException {
		if (log.isDebugEnabled())
			log.debug("compiling report source '" + getFileName() + "'");
		try {
			JasperCompileManager.compileReportToFile(getSourcePath(), getCompiledPath());
		} catch (JRException e) {
			if (log.isErrorEnabled())
				log.error("failed to compile report", e);
			Util.throwInternalErrorException(e);
		}
	}

	public File generate(final Map<String, Object> parameters, final Connection conn) throws InternalErrorException {
		Assert.argumentNotNull(parameters);

		if (needToBeCompiled())
			compile();
		final File rslt = createReportFile();
		try {
			JasperFillManager.fillReportToFile(getCompiledPath(), rslt.getAbsolutePath(), parameters, conn);
		} catch (final JRException e) {
			if (log.isErrorEnabled())
				log.error("failed to generate report '" + getFileName() + "'", e);
			rslt.delete();
			Util.throwInternalErrorException(e);
		}
		return rslt;
	}

	public File saveToDisk(final InputStream data) throws InternalErrorException {
		return saveToDisk(data, getFolder());
	}

	public File saveToDisk(final InputStream data, final String destFolder) throws InternalErrorException {
		if (log.isTraceEnabled())
			log.trace("saving file '" + getFileName() + "' to folder '" + destFolder + "'");
		try {
			final File dest = new File(destFolder, getFileName());
			if (dest.exists())
				dest.delete();
			dest.createNewFile();
			final FileOutputStream os = new FileOutputStream(dest);
			IOUtils.copy(data, os);
//			is.close();
			os.close();
			return dest;
		} catch (Throwable e) {
			if (log.isErrorEnabled())
				log.error("failed to create report file", e);
			Util.throwInternalErrorException(e);
		}
		return null;
	}

	public IHeader getHeader() throws InternalErrorException {
		final JRBaseReport report = load();
		if (report == null)
			throw new InternalErrorException("report template file not found");
		return new IHeader() {
			@Override
			public String getTitle() {
				return report.getName();
			}

			@Override
			public String getPermission() {
				return report.getProperty(IReport.HEADER_PERMISSION);
			}

			@Override
			public JRParameter[] getParameters() {
				return report.getParameters();
			}
		};
	}

	private File createReportFile() throws InternalErrorException {
		try {
			return File.createTempFile(IReport.REPORT_DOCUMENT_PREFIX, IReport.REPORT_DOCUMENT_EXT);
		} catch (final IOException e) {
			if (log.isErrorEnabled())
				log.error("failed to create temporary file for report '" + getFileName() + "'", e);
			Util.throwInternalErrorException(e);
		}
		return null;
	}

}
