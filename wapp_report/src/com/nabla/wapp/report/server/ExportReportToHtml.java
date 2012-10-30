/**
* Copyright 2010 nabla
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

import java.io.IOException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

/**
 * @author nabla
 *
 */
public class ExportReportToHtml extends JRHtmlExporter {

	private String	title = null;

	public ExportReportToHtml() {
		super();
		setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.FALSE);
		setParameter(JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
	}

	@Override
	protected void exportReportToWriter() throws JRException, IOException {
		if (title != null) {
			htmlHeader = "<html>\n"+
			"<head>\n" +
			"  <title>" + title + "</title>\n" +
			"  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + encoding + "\"/>\n" +
			"  <style type=\"text/css\">\n" +
			"    a {text-decoration: none}\n" +
			"  </style>\n" +
			"</head>\n" +
			"<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\">\n" +
			"<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
			"<tr><td width=\"50%\">&nbsp;</td><td align=\"center\">\n" +
			"\n";
		}
		super.exportReportToWriter();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

}
