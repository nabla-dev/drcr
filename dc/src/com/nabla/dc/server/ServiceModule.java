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

import com.google.inject.servlet.ServletModule;
import com.nabla.wapp.server.auth.LoginUserService;
import com.nabla.wapp.server.basic.general.ExportService;
import com.nabla.wapp.server.basic.general.ImportService;
import com.nabla.wapp.server.dispatch.DispatchService;

/**
 * @author nabla
 *
 */
public class ServiceModule extends ServletModule {

	@Override
	public void configureServlets() {
		serve("/dc/login").with(LoginUserService.class);
		serve("/dc/dispatch").with(DispatchService.class);
		serve("/dc/export").with(ExportService.class);
		serve("/dc/image").with(ImageService.class);
		serve("*.gupld").with(ImportService.class);
	}

}
