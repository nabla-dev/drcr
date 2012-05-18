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

import java.util.LinkedList;
import java.util.List;

/**
 * The <code></code> object is used to
 *
 */
public class CommandUiManager implements ICommandUi, ICommandUiManager {

	private final List<ICommandUi>	children = new LinkedList<ICommandUi>();

	@Override
	public void addUi(final ICommandUi ui) {
		children.add(ui);
	}

	@Override
	public void removeUi(final ICommandUi ui) {
		children.remove(ui);
	}

	@Override
	public void setChecked(final boolean value) {
		for (final ICommandUi e : children)
			e.setChecked(value);
	}

	@Override
	public void setEnabled(final boolean value) {
		for (final ICommandUi e : children)
			e.setEnabled(value);
	}

	@Override
	public void setText(final String value) {
		for (final ICommandUi e : children)
			e.setText(value);
	}

	@Override
	public void setVisible(final boolean value) {
		for (final ICommandUi e : children)
			e.setVisible(value);
	}

	@Override
	public boolean getEnabled() {
		for (final ICommandUi e : children)
			return e.getEnabled();
		return false;
	}

}
