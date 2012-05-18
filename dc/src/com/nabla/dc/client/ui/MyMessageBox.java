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
package com.nabla.dc.client.ui;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.nabla.wapp.client.ui.DetailedMessageBox;
import com.nabla.wapp.client.ui.IDetailedMessageBox;
import com.nabla.wapp.client.ui.MessageBox;

/**
 * The <code></code> object is used to
 *
 */
public class MyMessageBox extends MessageBox {

	public MyMessageBox() {
		super(Resource.strings.applicationTitle(), new IDetailedMessageBoxProvider() {

			@Override
			public void get(final Callback<? super IDetailedMessageBox, ? super Throwable> callback) {
				GWT.runAsync(new RunAsyncCallback() {

					@Override
					public void onSuccess() {
						callback.onSuccess(new DetailedMessageBox(new DetailedMessageBoxUi()));
					}

					@Override
					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}

				});
			}

		});
	}

}
