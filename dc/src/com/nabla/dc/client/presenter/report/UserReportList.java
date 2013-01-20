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
package com.nabla.dc.client.presenter.report;

import java.util.Set;

import com.nabla.dc.client.MyApplication;
import com.nabla.dc.client.ui.report.UserReportListUi;
import com.nabla.dc.shared.report.ReportCategories;
import com.nabla.wapp.client.command.Command;
import com.nabla.wapp.client.command.IBasicCommandSet;
import com.nabla.wapp.client.mvp.AbstractTabPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.print.IPrintCommandSet;
import com.nabla.wapp.report.client.IReportGetter;
import com.nabla.wapp.report.shared.parameter.IParameterValue;
import com.nabla.wapp.shared.slot.ISlot;

public class UserReportList extends AbstractTabPresenter<UserReportList.IDisplay> implements IReportGetter {

	public interface ICommandSet extends IPrintCommandSet, IBasicCommandSet {
		Command reload();
		Command savePreferences();
	}

	public interface IDisplay extends ITabDisplay {
		void reload();
		void savePreferences();
		ICommandSet getCommands();
		Set<Integer> getSelectedRecords();
	}

	private final IParameterValue	parameter;

	public UserReportList(final ReportCategories category, final IParameterValue parameter) {
		super(new UserReportListUi(category));
		this.parameter = parameter;
	}

	public UserReportList(final ReportCategories category) {
		this(category, null);
	}

	@Override
	public void bind() {
		super.bind();
		final ICommandSet cmd = getDisplay().getCommands();
		registerSlot(cmd.reload(), onReload);
		registerSlot(cmd.savePreferences(), onSavePreferences);
		cmd.updateUi();

		MyApplication.getInstance().getPrintManager().bind(cmd, this, this);
	}

	private final ISlot onReload = new ISlot() {
		@Override
		public void invoke() {
			getDisplay().reload();
		}
	};

	private final ISlot onSavePreferences = new ISlot() {
		@Override
		public void invoke() {
			getDisplay().savePreferences();
		}
	};

	@Override
	public IParameterValue getParameter() {
		return parameter;
	}

	@Override
	public Set<Integer> getReportIds() {
		return getDisplay().getSelectedRecords();
	}

}
