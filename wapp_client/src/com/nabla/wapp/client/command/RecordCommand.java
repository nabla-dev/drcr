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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.shared.slot.ISlot1;
import com.nabla.wapp.shared.slot.ISlotIterator;
import com.nabla.wapp.shared.slot.SlotManager1;
import com.smartgwt.client.data.Record;

/**
 * @author FNorais
 *
 */
public class RecordCommand<R extends Record> extends SlotManager1<R> implements IRecordCommand<R> {

	private static final Logger		log = LoggerFactory.getLog(RecordCommand.class);
	private final CommandUiManager		manager = new CommandUiManager();
	private ICurrentRecordProvider<R>	provider;

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
	public void setRecordProvider(final ICurrentRecordProvider<R> provider) {
		this.provider = provider;
	}

	@Override
	public void fire() {
		if (provider != null) {
			final R record = provider.getCurrentRecord();
			if (record != null) {
				final ISlotIterator<ISlot1<R>> iter = iterator();
				while (iter.hasNext())
					iter.next().invoke(record);
			} else {
				log.fine("no current record");
			}
		} else {
			log.log(Level.WARNING, "no current record provider defined");
		}
	}

}
