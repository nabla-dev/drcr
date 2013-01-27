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
package com.nabla.dc.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.nabla.dc.client.presenter.MainWindow;
import com.nabla.dc.client.presenter.report.ParameterWizardDisplayFactory;
import com.nabla.dc.client.ui.MyMessageBox;
import com.nabla.dc.client.ui.Resource;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.report.client.PrintManager;

/**
 * The <code></code> object is used to
 *
 */
public class MyApplication extends Application {

	private PrintManager	printManager;

	public MyApplication() {
		super(new MyMessageBox(), new MyServer(), Resource.serverErrors);
	}

	@Override
	public void execute(HasWidgets container) {
		container.clear();
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				new MainWindow().revealDisplay();
			}

			@Override
			public void onFailure(Throwable reason) {
				getMessageBox().error(reason);
			}

		});
	}

	/**
	 * Get global application instance
	 * @return MyApplication instance
	 */
	public static MyApplication getInstance() {
		return (MyApplication)Application.getInstance();
	}

	/**
	 * Get global printer manager instance
	 * @return PrintManager instance
	 */
	public PrintManager getPrintManager() {
		if (printManager == null)
			printManager = new PrintManager(new ParameterWizardDisplayFactory());
		return printManager;
	}

}
