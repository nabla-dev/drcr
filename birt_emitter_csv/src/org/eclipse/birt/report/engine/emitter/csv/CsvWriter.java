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

import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

public class CsvWriter extends CsvListWriter {

	public CsvWriter(final OutputStream out, final ICsvRenderOption options){
		super(new OutputStreamWriter(out),
				new CsvPreference.Builder(options.getQuoteCharacter(), options.getDelimiterCharacter(), options.getEndOfLineTag()).surroundingSpacesNeedQuotes(options.getSurroundingSpacesNeedQuotes()).build());
	}

}
