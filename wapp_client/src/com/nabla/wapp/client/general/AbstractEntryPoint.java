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
package com.nabla.wapp.client.general;

import java.util.logging.Handler;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.Page;


abstract public class AbstractEntryPoint implements EntryPoint {

	private static final Logger	logger = LoggerFactory.getLog(AbstractEntryPoint.class);

	@Override
	public void onModuleLoad() {
		if (LogConfiguration.loggingIsEnabled()) {
			for (Handler handler : Logger.getLogger("").getHandlers())
				handler.setFormatter(new SimpleTextLogFormatter(false));
		}
		logger.info("starting application");
		try {
			Page.setAppImgDir("[APP]public/images/");
			createApplication().execute(RootLayoutPanel.get());
			hideLoading();
			logger.info("application started");
		} catch (Exception e) {
			e.printStackTrace();
			String msg = e.getMessage();
			if (msg == null || msg.isEmpty())
				msg = "Fail to load main window.";
			hideLoading();
			Window.alert(msg);
		} catch (Throwable x) {
			x.printStackTrace();
			hideLoading();
			Window.alert("MainEntryPoint::onModuleLoad " + x.getLocalizedMessage());
		}
	}

	abstract protected IApplication createApplication();

	private static void hideLoading() {
		RootPanel.getBodyElement().removeChild(RootPanel.get("loading").getElement());
	}

}
