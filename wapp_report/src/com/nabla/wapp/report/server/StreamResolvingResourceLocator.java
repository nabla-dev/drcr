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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;

import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.shared.general.Nullable;

/**
 * @author nabla64
 *
 */
public class StreamResolvingResourceLocator implements IResourceLocator {

	private static final Log	log = LogFactory.getLog(StreamResolvingResourceLocator.class);

	private final Connection	conn;
	private final Integer		reportId;

	public StreamResolvingResourceLocator(final Connection conn, final Integer reportId) {
		this.conn = conn;
		this.reportId = reportId;
	}

	@Override
    public URL findResource(ModuleHandle module, String filename, int type) {
    	return findResource(module, filename, type, Collections.emptyMap());
    }

    @Override
    public @Nullable URL findResource(@SuppressWarnings("unused") final ModuleHandle moduleHandle, final String fileName, final int type, @SuppressWarnings({ "rawtypes", "unused" }) final Map appContext) {
    	if (log.isDebugEnabled())
    		log.debug("requesting report resource '" + fileName + "' of type " + type);
    	try {
    		final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT content" +
" FROM report_resource" +
" WHERE name=? AND (report_id=? OR report_id IS NULL);", fileName, reportId);
	    	try {
	    		final ResultSet rs = stmt.executeQuery();
	    		try {
	    			if (!rs.next()) {
	    				if (log.isDebugEnabled())
							log.debug("failed to find report resource '" + fileName + "'");
	    				return null;
	    			}
	    			try {
						return new URL(null, "birtres://" + reportId + "//" + fileName, getURLStreamHandler(rs.getBinaryStream(1)));
					} catch (MalformedURLException e) {
						if (log.isDebugEnabled())
							log.debug("wrong URL created for report resource '" + fileName + "'", e);
					}
	    		} finally {
	    			rs.close();
	    		}
	    	} finally {
	    		stmt.close();
	    	}
		} catch (SQLException e) {
			if (log.isErrorEnabled())
				log.error("fail to find report resource '" + fileName + "'", e);
		}
		return null;
    }

    private static URLStreamHandler getURLStreamHandler(final InputStream resource) {
    	return new URLStreamHandler() {
			@Override
			protected URLConnection openConnection(URL url) throws IOException {
				return getURLConnection(url, resource);
			}
    	};
    }

    private static URLConnection getURLConnection(URL url, final InputStream resource) {
    	return new URLConnection(url) {
            @Override
            public void connect() throws IOException {}

            @Override
            public InputStream getInputStream() throws IOException {
            	return resource;
            }
    	};
    }

}
