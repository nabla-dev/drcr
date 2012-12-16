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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;

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
//		JRParameter[] getParameters();
	}

	private static final Log	log = LogFactory.getLog(ReportFile.class);

	private final String		folder;

	private static IReportEngine engine;

	static {
		final EngineConfig config = new EngineConfig( );
	//	config.setResourcePath("/home/nabla64/MyProjects/dev/workspace 4.2/dr-cr/dc");
	//	config.setResourceLocator(new StreamResolvingResourceLocator());
		try {
			Platform.startup( config );
			IReportEngineFactory factory = (IReportEngineFactory) Platform
					.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
			engine = factory.createReportEngine( config );
			engine.changeLogLevel( Level.WARNING );
		} catch (BirtException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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

/*
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
*/
	@SuppressWarnings("unchecked")
	public File generate(final Map<String, Object> parameters, final Connection conn) throws InternalErrorException {
		Assert.argumentNotNull(parameters);

		final File rslt = createReportFile();
		try{
final FileOutputStream out = new FileOutputStream("/home/nabla64/MyProjects/dev/report_test.pdf");
final FileInputStream in = new FileInputStream(getPath());
/*			final EngineConfig config = new EngineConfig( );
			config.setResourcePath("/home/nabla64/MyProjects/dev/workspace 4.2/dr-cr/dc");
			Platform.startup( config );
			IReportEngineFactory factory = (IReportEngineFactory) Platform
					.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
			IReportEngine engine = factory.createReportEngine( config );*/

			final IReportRunnable design = null/*engine.openReportDesign("1234", in, new StreamResolvingResourceLocator())*/;

			PDFRenderOption options = new PDFRenderOption();
		//	options.setOption(IPDFRenderOption.PAGE_OVERFLOW, new Integer(IPDFRenderOption.FIT_TO_PAGE_SIZE) );
		//	options.setOption(IPDFRenderOption. , new Boolean(true) );
	       // options.setOutputFileName("/home/nabla64/MyProjects/dev/report_test.pdf");
	        options.setOutputStream(out);
	        options.setOutputFormat("pdf");
//options.setOption(IPDFRenderOption.CLOSE_OUTPUTSTREAM_ON_EXIT, new Boolean(true));

	        IRunAndRenderTask t = engine.createRunAndRenderTask(design);
	        t.getAppContext().put("OdaJDBCDriverPassInConnection", conn);
	        t.setRenderOption(options);

	        t.run();
	        t.close();
/*
	        IDataExtractionTask td = engine.createDataExtractionTask(arg0);
	        td.selectResultSet("dsCsv");
	        for (int i=0; i< resultItem.getResultMetaData().getColumnCount( ); i++)

            {

                System.out.print( resultItem.getResultMetaData().getColumnName(i) + " , " );

            }



            System.out.println("");

            IExtractionResults iExtractResults = td.extract();


            IDataIterator iData = null;





               if ( iExtractResults != null )

               {



                   iData = iExtractResults.nextResultIterator( );



                       //iterate through the results

                        if ( iData != null  )

                        {



                             int n = 0;

                             StringBuffer sb = new StringBuffer();

                             String new_pair = "";



                             while ( iData.next( ) )

                            {

                                sb.append( new_pair );

                                                Object objColumn;



                                       try{

                                        objColumn = iData.getValue(n++);

                                        sb.append(objColumn);

                                        sb.append(" , ");

                                        }catch(Exception e){

                                                     objColumn = new String("");

                                }



                   Gang:                 IResultMetaData resultMeta = iExtractResults.getResultMetaData( );

                                                        for ( int i = 0; i < resultMeta.getColumnCount( ); i++ )

                                                        {

                                                                 objColumn = iData.getValue( resultMeta

                                                                                    .getColumnName( i ) );

                                                                 sb.append(objColumn);

                                                                 sb.append(" , ");

                                                        }

                                                        sb.append("\n");



                                         }

                             System.out.println( sb.toString() );



                             iData.close();

                        }



               }



              iDataExtract.close();*/
	        /*
			final IRunTask task = engine.createRunTask(design);
			task.getAppContext().put("OdaJDBCDriverPassInConnection", conn);
			task.run(rslt.getAbsolutePath());
			task.close();

			IReportDocument report = engine.openReportDocument(rslt.getAbsolutePath());
			final IRenderTask t = engine.createRenderTask(report);

	        t.setRenderOption(options);
	       // task.setPageRange("1-2");
	        t.render();
	        t.close();
report.close();*/

out.flush();
out.close();
		/*	engine.destroy();
			Platform.shutdown();*/
		} catch( Exception ex){
			ex.printStackTrace();
			rslt.delete();
		}

/*
        task.setRenderOption(options);
        task.run();
        task.close();*/
/*		if (needToBeCompiled())
			compile();
		final File rslt = createReportFile();
		try {
			JasperFillManager.fillReportToFile(getCompiledPath(), rslt.getAbsolutePath(), parameters, conn);
		} catch (final JRException e) {
			if (log.isErrorEnabled())
				log.error("failed to generate report '" + getFileName() + "'", e);
			rslt.delete();
			Util.throwInternalErrorException(e);
		}*/
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



	/**
     * Extract zip file at the specified destination path.
     * NB:archive must consist of a single root folder containing everything else
     *
     * @param archivePath path to zip file
     * @param destinationPath path to extract zip file to. Created if it doesn't exist.
     */
/*    public static void extractZip(String archivePath, String destinationPath) {
        File archiveFile = new File(archivePath);
        File unzipDestFolder = null;

        try {
            unzipDestFolder = new File(destinationPath);
            String[] zipRootFolder = new String[]{null};
            unzipFolder(archiveFile, archiveFile.length(), unzipDestFolder, zipRootFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 */
    /**
     * Unzips a zip file into the given destination directory.
     *
     * The archive file MUST have a unique "root" folder. This root folder is
     * skipped when unarchiving.
     *
     * @return true if folder is unzipped correctly.
     */
/*
 *
 File temp = File.createTempFile("folder-name","");
temp.delete();
temp.mkdir();
 *    @SuppressWarnings("unchecked")
    private static boolean unzipFolder(File archiveFile,
            long compressedSize,
            File zipDestinationFolder,
            String[] outputZipRootFolder) {

        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(archiveFile);
            byte[] buf = new byte[65536];

            Enumeration entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry zipEntry = entries.nextElement();
                String name = zipEntry.getName();
                name = name.replace('\\', '/');
                int i = name.indexOf('/');
                if (i > 0) {
                        outputZipRootFolder[0] = name.substring(0, i);
                    }
                    name = name.substring(i + 1);
                }

                File destinationFile = new File(zipDestinationFolder, name);
                if (name.endsWith("/")) {
                    if (!destinationFile.isDirectory() && !destinationFile.mkdirs()) {
                        log("Error creating temp directory:" + destinationFile.getPath());
                        return false;
                    }
                    continue;
                } else if (name.indexOf('/') != -1) {
                    // Create the the parent directory if it doesn't exist
                    File parentFolder = destinationFile.getParentFile();
                    if (!parentFolder.isDirectory()) {
                        if (!parentFolder.mkdirs()) {
                            log("Error creating temp directory:" + parentFolder.getPath());
                            return false;
                        }
                    }
                }

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(destinationFile);
                    int n;
                    InputStream entryContent = zipFile.getInputStream(zipEntry);
                    while ((n = entryContent.read(buf)) != -1) {
                        if (n > 0) {
                            fos.write(buf, 0, n);
                        }
                    }
                } finally {
                    if (fos != null) {
                        fos.close();
                    }
                }
            }
            return true;

        } catch (IOException e) {
            log("Unzip failed:" + e.getMessage());
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    log("Error closing zip file");
                }
            }
        }

        return false;
    }*/

}
