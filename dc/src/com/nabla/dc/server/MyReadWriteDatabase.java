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
package com.nabla.dc.server;

import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nabla.wapp.server.auth.AuthDatabase;

/**
 * @author nabla
 *
 */
@Singleton
public class MyReadWriteDatabase extends AuthDatabase {

	private static final Log	log = LogFactory.getLog(MyReadWriteDatabase.class);

	@Inject
	public MyReadWriteDatabase(final ServletContext serverContext/*, @IReportFolder final String reportFolder, @IReCompileReports boolean reCompileReports*/) throws SQLException, ClassNotFoundException {
		super(serverContext.getInitParameter("rw_database_name"), serverContext.getInitParameter("database_driver_name"), IRoles.class, serverContext.getInitParameter("root_password"));

	//	ReportDatabase.initialize(getConnection(), reportFolder, BuiltInReports.class, DefaultReports.class, reCompileReports);
	}

}
