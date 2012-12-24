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
package org.eclipse.birt.report.engine.emitter.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.ir.TableItemDesign;


/**
 * @author nabla64
 *
 */
public class XmlReportEmitter extends ContentEmitterAdapter {

	private static final Log		log = LogFactory.getLog(XmlReportEmitter.class);
	private static final String	DEFAULT_OUTPUT_FILE = "report.xml";

	protected IEmitterServices		services;
	private IXmlRenderOption		options;
	private XmlWriter				writer;
	private Integer					columnCount;
	private String[]				columns;
	private Integer					currentColumn;
	private boolean				processingHeader = true;

	public XmlReportEmitter() {}

	@Override
	public void initialize(final IEmitterServices services) {
		this.services = services;
	}

	@Override
	public void start(final IReportContent report) throws EngineException {
		if (log.isDebugEnabled())
			log.debug("starting exporting report - " + report.getReportContext().getRenderOption().getOutputFormat());
		options = new XmlRenderOption(report.getReportContext().getRenderOption());
		writer = new XmlWriter(EmitterUtil.getOuputStream(services, DEFAULT_OUTPUT_FILE));
		writer.startWriter();
		writer.openTag(options.getRootNode());
	}

	@Override
	public void end(@SuppressWarnings("unused") final IReportContent report) {
		if (log.isDebugEnabled())
			log.debug("ending exporting report");
		writer.closeTag(options.getRootNode());
		writer.endWriter();
		writer.close();
		writer = null;
		options = null;
	}

	@Override
	public void startTable(final ITableContent table) {
		if (!isTable(table))
			return;
		if (log.isDebugEnabled())
			log.debug("starting exporting table '" + table.getName() + "' - columns = " + table.getColumnCount());
		columnCount = table.getColumnCount();
		columns = new String[table.getColumnCount()];
	}

	@Override
	public void endTable(@SuppressWarnings("unused") final ITableContent table) {
		if (columnCount == null)
			return;
		if (log.isDebugEnabled())
			log.debug("ending exporting table");
		columnCount = null;
		columns = null;
	}

	@Override
	public void startRow(final IRowContent row) {
		if (getWriteData()) {
			currentColumn = 0;
			processingHeader = isRowInHeader(row);
			if (!getProcessingHeader())
				writer.openTag(options.getRecordNode());
		}
     }

	@Override
	public void endRow(@SuppressWarnings("unused") final IRowContent row) {
		if (getWriteData()) {
			if (!getProcessingHeader())
				writer.closeTag(options.getRecordNode());
			currentColumn = null;
		}
	}

	@Override
	public void startCell(@SuppressWarnings("unused") final ICellContent cell) {
   	 	if (getWriteRecord() && !getProcessingHeader())
   	 		writer.openTag(columns[currentColumn]);
	}

    @Override
	public void startText(final ITextContent text) {
		if (getWriteRecord()) {
			final String textValue = text.getText( );
			if (getProcessingHeader())
				columns[currentColumn] = textValue;
			else
				writer.text(textValue);
		}
	}

    @Override
    public void endCell(@SuppressWarnings("unused") final ICellContent cell) {
    	if (getWriteRecord()) {
    		if (!getProcessingHeader())
   	 			writer.closeTag(columns[currentColumn]);
    		currentColumn++;
    	}
    }

    protected boolean isTable(final ITableContent table) {
    	return table.getGenerateBy() instanceof TableItemDesign;
    }

    protected boolean isRowInHeader(final IRowContent row) {
		return row.getBand().getBandType() == IBandContent.BAND_HEADER;
	}

    protected boolean getWriteData() {
    	return columns != null;
    }

    protected boolean getProcessingHeader() {
    	return processingHeader;
    }

    protected boolean getWriteRecord() {
    	return currentColumn != null && currentColumn < columnCount;
    }
}
