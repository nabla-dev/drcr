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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.InternalErrorException;

public class ReportZipFile {

	private static final Log		log = LogFactory.getLog(ReportZipFile.class);
	public static String			CONTENT_TYPE = "application/zip";

	static interface IFileFilter {
		boolean include(final String fileName);
	}

	static class EnumerationZipEntry implements Enumeration<ZipArchiveEntry> {

		private final ZipFile							zip;
		private final Enumeration<ZipArchiveEntry>		iter;
		private final IFileFilter						filter;
		private ZipArchiveEntry							current;

		public EnumerationZipEntry(final ZipFile zip, final IFileFilter filter) {
			this.zip = zip;
			this.iter = zip.getEntries();
			this.filter = filter;
			current = next();
		}

		@Override
		public boolean hasMoreElements() {
			return current != null;
		}

		@Override
		public ZipArchiveEntry nextElement() {
			final ZipArchiveEntry rslt = current;
			current = next();
			return rslt;
		}

		protected ZipArchiveEntry next() {
			while (iter.hasMoreElements()) {
				final ZipArchiveEntry e = iter.nextElement();
				if (!e.isDirectory() && zip.canReadEntryData(e) && filter.include(e.getName()))
					return e;
			}
			return null;
		}
	}

	private File		zipFile;
	private ZipFile		impl;

	public ReportZipFile(final InputStream zip) throws InternalErrorException {
		try {
			zipFile = File.createTempFile("wapp_report", null);
		} catch (IOException e) {
			if (log.isErrorEnabled())
				log.error("fail to create temporary file to hold zip stream");
			Util.throwInternalErrorException(e);
			zipFile = null;
		}
		try {
			final FileOutputStream out = new FileOutputStream(zipFile);
			IOUtils.copy(zip, out);
			out.close();
		} catch (Throwable e) {
			zipFile.delete();
			Util.throwInternalErrorException(e);
		}
		try {
			impl = new ZipFile(zipFile);
		} catch (IOException e) {
			if (log.isErrorEnabled())
				log.error("fail to open temporary zip file");
			zipFile.delete();
			Util.throwInternalErrorException(e);
		}
	}

	public void close() {
		try {
			impl.close();
		} catch (IOException __) {}
		zipFile.delete();
	}

	public InputStream getInputStream(final ZipArchiveEntry ze) throws InternalErrorException {
		try {
			return impl.getInputStream(ze);
		} catch (ZipException e) {
			Util.throwInternalErrorException(e);
		} catch (IOException e) {
			Util.throwInternalErrorException(e);
		}
		return null;
	}

	public ZipArchiveEntry getReportDesign() {
		for (Enumeration<ZipArchiveEntry> iter = impl.getEntries(); iter.hasMoreElements();) {
			final ZipArchiveEntry entry = iter.nextElement();
			if (!entry.isDirectory() && impl.canReadEntryData(entry) &&
					FilenameUtils.isExtension(entry.getName(), ReportManager.REPORT_FILE_EXTENSION))
				return entry;
		}
		return null;
	}

	public Enumeration<ZipArchiveEntry> getEntries(final String fileExtension) {
		return new EnumerationZipEntry(impl, new IFileFilter() {

			@Override
			public boolean include(String fileName) {
				return FilenameUtils.isExtension(fileName, fileExtension);
			}

		});
	}

	public Enumeration<ZipArchiveEntry> getEntries(final String[] fileExtensions) {
		return new EnumerationZipEntry(impl, new IFileFilter() {

			@Override
			public boolean include(String fileName) {
				return FilenameUtils.isExtension(fileName, fileExtensions);
			}

		});
	}

	public static boolean acceptContentType(final String contentType) {
		return CONTENT_TYPE.equalsIgnoreCase(contentType);
	}

}
