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

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.export.JROriginExporterFilter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.type.BandTypeEnum;

/**
 * The <code>ExportReportToXml</code> object is used to export Jasper report to XML
 *
 */
public class ExportReportToXml extends JRXmlExporter {

	public ExportReportToXml() {
		final JROriginExporterFilter filter = new JROriginExporterFilter();
		for (BandTypeEnum band : BandTypeEnum.values()) {
			switch (band) {
			case DETAIL:
				break;
			default:
				filter.addOrigin(new JROrigin(null, null, band));
				break;
			}
		}
		setParameter(JRExporterParameter.FILTER, filter);
	}

}
