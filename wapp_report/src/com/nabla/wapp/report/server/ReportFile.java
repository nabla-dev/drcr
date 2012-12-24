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



/**
 * The <code></code> object is used to
 *
 */
public class ReportFile {


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
