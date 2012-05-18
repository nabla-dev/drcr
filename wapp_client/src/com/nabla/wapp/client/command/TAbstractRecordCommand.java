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
package com.nabla.wapp.client.command;

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.wapp.shared.signal.Signal1;
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
public abstract class TAbstractRecordCommand<R extends Record> extends Signal1<R> implements ITRecordCommand<R> {

	private final CommandUiManager	manager = new CommandUiManager();

	@Override
	public void addUi(final ICommandUi ui) {
		manager.addUi(ui);
	}

	@Override
	public void removeUi(final ICommandUi ui) {
		manager.removeUi(ui);
	}

	@Override
	public void setChecked(final boolean value) {
		manager.setChecked(value);
	}

	@Override
	public void setEnabled(final boolean value) {
		manager.setEnabled(value);
	}

	@Override
	public void setText(final String value) {
		manager.setText(value);
	}

	@Override
	public void setVisible(final boolean value) {
		manager.setVisible(value);
	}

	@Override
	public boolean getEnabled() {
		return manager.getEnabled();
	}

	@Override
	public void execute(JavaScriptObject jsRecord) {
		fire(getRecord(jsRecord));
	}

}
