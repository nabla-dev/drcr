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

import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;

public class CsvRenderOption extends RenderOption implements ICsvRenderOption {

	private static final String	WRITE_HEADER = "CsvRenderOption.WriteHeader";
	private static final Boolean	DEFAULT_WRITE_HEADER = true;

	private static final String	QUOTE_CHARACTER = "CsvRenderOption.QuoteCharacter";
	private static final char		DEFAULT_QUOTE_CHARACTER = '"';

	private static final String	DELIMITED_CHARACTER = "CsvRenderOption.DelimiterCharacter";
	private static final char		DEFAULT_DELIMITED_CHARACTER = ',';

	private static final String	END_OF_LINE_TAG = "CsvRenderOption.EndOfLineTag";
	private static final String	DEFAULT_END_OF_LINE_TAG = "\r\n";

	private static final String	SURROUNDING_SPACES_NEED_QUOTES = "CsvRenderOption.SurroundingSpacesNeedQuotes";
	private static final Boolean	DEFAULT_SURROUNDING_SPACES_NEED_QUOTES = false;

	public CsvRenderOption() {
		super();
	}

	public CsvRenderOption(IRenderOption options) {
		super(options);
	}

	protected String getNonNullStringOption(final String name, final String defaultValue) {
		final String value = getStringOption(name);
		return (value == null) ? defaultValue : value;
	}

	protected char getCharOption(final String name, final char defaultValue) {
		final String value = getStringOption(name);
		return (value == null || value.length() < 1) ? defaultValue : value.charAt(0);
	}

	@Override
	public Boolean getWriteHeader() {
		return getBooleanOption(WRITE_HEADER, DEFAULT_WRITE_HEADER);
	}

	@Override
	public void setWriteHeader(Boolean value) {
		setOption(WRITE_HEADER, value);
	}

	@Override
	public char getQuoteCharacter() {
		return getCharOption(QUOTE_CHARACTER, DEFAULT_QUOTE_CHARACTER);
	}

	@Override
	public void setQuoteCharacter(String value) {
		setOption(QUOTE_CHARACTER, value);
	}

	@Override
	public char getDelimiterCharacter() {
		return getCharOption(DELIMITED_CHARACTER, DEFAULT_DELIMITED_CHARACTER);
	}

	@Override
	public void setDelimiterCharacter(String value) {
		setOption(DELIMITED_CHARACTER, value);
	}

	@Override
	public String getEndOfLineTag() {
		return getNonNullStringOption(END_OF_LINE_TAG, DEFAULT_END_OF_LINE_TAG);
	}

	@Override
	public void setEndOfLineTag(String value) {
		setOption(END_OF_LINE_TAG, value);
	}

	@Override
	public Boolean getSurroundingSpacesNeedQuotes() {
		return getBooleanOption(SURROUNDING_SPACES_NEED_QUOTES, DEFAULT_SURROUNDING_SPACES_NEED_QUOTES);
	}

	@Override
	public void setSurroundingSpacesNeedQuotes(Boolean value) {
		setOption(SURROUNDING_SPACES_NEED_QUOTES, value);
	}
}
