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
package com.nabla.dc.client.presenter.company;

import com.nabla.dc.client.ui.company.PeriodEndListUi;
import com.nabla.dc.shared.IPrivileges;
import com.nabla.wapp.client.command.Command;
import com.nabla.wapp.client.command.CommandUiManager;
import com.nabla.wapp.client.command.IBasicCommandSet;
import com.nabla.wapp.client.command.IRequiredRole;
import com.nabla.wapp.client.mvp.AbstractTabPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.shared.slot.ISlot;

/**
 * @author nabla
 *
 */
public class PeriodEndList extends AbstractTabPresenter<PeriodEndList.IDisplay> {

	public interface ICommandSet extends IBasicCommandSet {
		Command reload();
		Command savePreferences();
		@IRequiredRole(IPrivileges.PERIOD_END_EDIT) CommandUiManager edit();
	}

	public interface IDisplay extends ITabDisplay {
		void reload();
		ICommandSet getCommands();
	}

	public PeriodEndList(final IDisplay ui) {
		super(ui);
	}

	public PeriodEndList(final Integer companyId) {
		this(new PeriodEndListUi(companyId));
	}

	public PeriodEndList(final Integer companyId, final String companyName) {
		this(new PeriodEndListUi(companyId, companyName));
	}

	@Override
	public void bind() {
		super.bind();
		final ICommandSet cmd = getDisplay().getCommands();
		registerSlot(cmd.reload(), onReload);
		cmd.updateUi();
	}

	private final ISlot onReload = new ISlot() {
		@Override
		public void invoke() {
			getDisplay().reload();
		}
	};
}
