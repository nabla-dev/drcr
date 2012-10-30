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

import com.nabla.wapp.report.shared.IReport;

/**
 * The <code></code> object is used to
 *
 */
public class ReportFileName {

	private String	fileName;
	private String	extension;

	public ReportFileName() {}

	public ReportFileName(final String fileName) {
		setFileName(fileName);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
		extension = extractFileExtension(fileName);
	}

	public String getExtension() {
		return extension;
	}

	public boolean isCompiled() {
		return IReport.EXTENSION_COMPILED_REPORT.equalsIgnoreCase(extension);
	}

	public boolean isSource() {
		return IReport.EXTENSION_SOURCE_REPORT.equalsIgnoreCase(extension);
	}

	public boolean isStyleLibrary() {
		return IReport.EXTENSION_STYLE_LIBRARY.equalsIgnoreCase(extension);
	}

	public boolean isScript() {
		return IReport.EXTENSION_SCRIPT.equalsIgnoreCase(extension);
	}

	public String getCompiledFileName() {
		return replaceExtension(IReport.EXTENSION_COMPILED_REPORT);
	}

	public String getSourceFileName() {
		return replaceExtension(IReport.EXTENSION_SOURCE_REPORT);
	}

	public String replaceExtension(final String with) {
		final String tmp = removeExtension();
		return (with == null) ? tmp : tmp + "." + with;
	}

	public String removeExtension() {
		return removeFileExtension(fileName);
	}

	public static String removeFileExtension(final String fileName) {
		if (fileName == null)
			return null;
		int dotInd = fileName.lastIndexOf('.');
		return (dotInd > 0 && dotInd < fileName.length()) ? fileName.substring(0, dotInd) : fileName;
	}

	public static String extractFileExtension(final String fileName) {
		if (fileName == null)
			return null;
		int dotInd = fileName.lastIndexOf('.');
		return (dotInd > 0 && dotInd < fileName.length()) ? fileName.substring(dotInd + 1) : null;
	}

}
