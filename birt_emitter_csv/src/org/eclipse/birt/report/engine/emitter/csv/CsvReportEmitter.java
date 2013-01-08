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
package org.eclipse.birt.report.engine.emitter.csv;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.core.exception.BirtException;
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

public class CsvReportEmitter extends ContentEmitterAdapter {

	private static final Log		log = LogFactory.getLog(CsvReportEmitter.class);
	private static final String	DEFAULT_OUTPUT_FILE = "report.csv";

	protected IEmitterServices		services;
	private ICsvRenderOption		options;
	private CsvWriter				writer;
	private Integer					columnCount;
	private String[]				record;
	private Integer					currentColumn;
	private boolean				processingHeader = true;

	public CsvReportEmitter() {	}

	@Override
	public void initialize(final IEmitterServices services) {
		this.services = services;
	}

	@Override
	public void start(final IReportContent report) throws EngineException {
		if (log.isDebugEnabled())
			log.debug("starting exporting report - " + report.getReportContext().getRenderOption().getOutputFormat());
		options = new CsvRenderOption(report.getReportContext().getRenderOption());
		writer = new CsvWriter(EmitterUtil.getOuputStream(services, DEFAULT_OUTPUT_FILE), options);
	}

	@Override
	public void end(@SuppressWarnings("unused") final IReportContent report) throws BirtException {
		if (log.isDebugEnabled())
			log.debug("ending exporting report");
		try {
			writer.close();
		} catch (IOException __) {
			throw new BirtException("CsvReportEmitter: fail to close writer");
		}
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
		record = new String[columnCount];
	}

	@Override
	public void endTable(@SuppressWarnings("unused") final ITableContent table) {
		if (columnCount == null)
			return;
		if (log.isDebugEnabled())
			log.debug("ending exporting table");
		record = null;
		columnCount = null;
	}

	@Override
	public void startRow(final IRowContent row) {
		if (getWriteData()) {
			processingHeader = isRowInHeader(row);
			if (!getProcessingHeader() || options.getWriteHeader())
				currentColumn = 0;
		}
     }

	@Override
	public void endRow(@SuppressWarnings("unused") final IRowContent row) throws BirtException {
		if (getWriteData() && currentColumn != null) {
			try {
				writer.write(record);
			} catch (IOException __) {
				throw new BirtException("CsvReportEmitter: fail to write record");
			}
			currentColumn = null;
		}
	}

    @Override
	public void startText(final ITextContent text) {
		if (getWriteRecord())
			record[currentColumn] = formatValue(text.getText());
	}

    @Override
    public void endCell(@SuppressWarnings("unused") final ICellContent cell) {
    	if (getWriteRecord())
    		currentColumn++;
    }

    protected boolean isTable(final ITableContent table) {
    	return table.getGenerateBy() instanceof TableItemDesign;
    }

    protected boolean isRowInHeader(final IRowContent row) {
		return row.getBand().getBandType() == IBandContent.BAND_HEADER;
	}

    protected boolean getWriteData() {
    	return columnCount != null;
    }

    protected boolean getProcessingHeader() {
    	return processingHeader;
    }

    protected boolean getWriteRecord() {
    	return currentColumn != null && currentColumn < columnCount;
    }

    protected String formatValue(final String value) {
    	final String ret = (value == null) ? "" : value;
    	return getProcessingHeader() ? ret.toLowerCase().replace(' ', '_') : ret;
    }
}
