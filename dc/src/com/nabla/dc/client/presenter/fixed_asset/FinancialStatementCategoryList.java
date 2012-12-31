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
package com.nabla.dc.client.presenter.fixed_asset;

import com.nabla.dc.client.MyApplication;
import com.nabla.dc.client.model.fixed_asset.FinancialStatementCategoryRecord;
import com.nabla.dc.client.ui.Resource;
import com.nabla.dc.client.ui.fixed_asset.FinancialStatementCategoryListUi;
import com.nabla.dc.shared.IPrivileges;
import com.nabla.dc.shared.command.fixed_asset.RestoreFinancialStatementCategory;
import com.nabla.dc.shared.report.BuiltInReports;
import com.nabla.wapp.client.command.Command;
import com.nabla.wapp.client.command.CommandUiManager;
import com.nabla.wapp.client.command.HideableCommand;
import com.nabla.wapp.client.command.IBasicCommandSet;
import com.nabla.wapp.client.command.IRequireRootRole;
import com.nabla.wapp.client.command.IRequiredRole;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.mvp.AbstractTabPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.print.IPrintCommandSet;
import com.nabla.wapp.client.ui.ListGrid.IListGridConfirmAction;
import com.nabla.wapp.shared.command.AbstractRestore;
import com.nabla.wapp.shared.slot.ISlot;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * @author nabla
 *
 */
public class FinancialStatementCategoryList extends AbstractTabPresenter<FinancialStatementCategoryList.IDisplay> {

	public interface ICommandSet extends IPrintCommandSet, IBasicCommandSet {
		@IRequiredRole(IPrivileges.FA_FINANCIAL_STATEMENT_CATEGORY_ADD) HideableCommand addRecord();
		@IRequiredRole(IPrivileges.FA_FINANCIAL_STATEMENT_CATEGORY_REMOVE) HideableCommand removeRecord();
		@IRequireRootRole HideableCommand restoreRecord();
		Command reload();
		Command savePreferences();
		@IRequiredRole(IPrivileges.FA_FINANCIAL_STATEMENT_CATEGORY_EDIT) CommandUiManager edit();
		@IRequiredRole(IPrivileges.FA_FINANCIAL_STATEMENT_CATEGORY_ADD) CommandUiManager add();
	}

	public interface IDisplay extends ITabDisplay {
		void addRecord();
		void removeRecord(final IListGridConfirmAction confirmUi);
		boolean restoreRecord(final AbstractRestore command);
		void reload();
		void savePreferences();
		ICommandSet getCommands();
	}

	public FinancialStatementCategoryList(final IDisplay display) {
		super(display);
	}

	public FinancialStatementCategoryList() {
		this(new FinancialStatementCategoryListUi());
	}

	@Override
	public void bind() {
		super.bind();
		final ICommandSet cmd = getDisplay().getCommands();
		registerSlot(cmd.addRecord(), onAddRecord);
		registerSlot(cmd.removeRecord(), onRemoveRecord);
		registerSlot(cmd.reload(), onReload);
		registerSlot(cmd.savePreferences(), onSavePreferences);
		registerSlot(cmd.restoreRecord(), onRestoreRecord);
		cmd.updateUi();

		MyApplication.getInstance().getPrintManager().bind(cmd, this, BuiltInReports.FINANCIAL_STATEMENT_CATEGORY_LIST);
	}

	private final ISlot onAddRecord = new ISlot() {
		@Override
		public void invoke() {
			getDisplay().addRecord();
		}
	};

	private final ISlot onRemoveRecord = new ISlot() {
		@Override
		public void invoke() {
			getDisplay().removeRecord(onConfirmRemoveRecord);
		}
	};

	private final IListGridConfirmAction onConfirmRemoveRecord = new IListGridConfirmAction() {
		@Override
		public void confirmRemoveRecords(final ListGridRecord[] records, final com.google.gwt.user.client.Command onSuccess) {
			final FinancialStatementCategoryRecord record = new FinancialStatementCategoryRecord(records[0]);
			Application.getInstance().getMessageBox().ask(
					Resource.messages.confirmRemoveBalanceSheetCategories(records.length, record.getName()),
					onSuccess);
		}
	};

	private final ISlot onRestoreRecord = new ISlot() {
		@Override
		public void invoke() {
			if (!getDisplay().restoreRecord(new RestoreFinancialStatementCategory()))
				Application.getInstance().getMessageBox().error(Resource.strings.noDeletedBalanceSheetCategorySelected());
		}
	};

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

}
