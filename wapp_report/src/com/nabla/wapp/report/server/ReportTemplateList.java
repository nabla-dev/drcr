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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopyFields;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.InternalErrorException;
import com.nabla.wapp.shared.print.ReportFormats;

public class ReportTemplateList extends LinkedList<ReportTemplate> {

	private static final long serialVersionUID = 1L;

	public Report mergeToPdf(final Connection conn, final Map<String, Object> parameters, final Locale locale) throws InternalErrorException {
		final ByteArrayOutputStream tmp = new ByteArrayOutputStream();
		try {
			final PdfCopyFields pdf = new PdfCopyFields(tmp);
			try {
				for (ReportTemplate template : this)
					pdf.addDocument(template.generatePdf(conn, parameters, locale));
			} finally {
				pdf.close();
			}
		} catch (DocumentException e) {
			throw new InternalErrorException(Util.formatInternalErrorDescription(e));
		} catch (IOException e) {
			throw new InternalErrorException(Util.formatInternalErrorDescription(e));
		}
		return new Report("name", new ByteArrayInputStream(tmp.toByteArray()), ReportFormats.PDF);
	}

}
