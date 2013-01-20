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

import com.google.gwt.core.client.GWT;
import com.nabla.dc.client.MyApplication;
import com.nabla.dc.client.model.fixed_asset.AssetRecord;
import com.nabla.dc.client.presenter.ITabManager;
import com.nabla.dc.client.presenter.report.UserReportList;
import com.nabla.dc.client.ui.Resource;
import com.nabla.dc.client.ui.fixed_asset.AssetListUi;
import com.nabla.dc.shared.IPrivileges;
import com.nabla.dc.shared.command.fixed_asset.RevertAssetDisposal;
import com.nabla.dc.shared.report.BuiltInReports;
import com.nabla.dc.shared.report.CompanyParameterValue;
import com.nabla.dc.shared.report.ReportCategories;
import com.nabla.wapp.client.command.Command;
import com.nabla.wapp.client.command.HideableCommand;
import com.nabla.wapp.client.command.IBasicCommandSet;
import com.nabla.wapp.client.command.ICurrentRecordProvider;
import com.nabla.wapp.client.command.IRequiredRole;
import com.nabla.wapp.client.general.AbstractAsyncCallback;
import com.nabla.wapp.client.general.AbstractRunAsyncCallback;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.mvp.AbstractTabPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.print.IPrintCommandSet;
import com.nabla.wapp.client.ui.ListGrid.IListGridConfirmAction;
import com.nabla.wapp.shared.dispatch.VoidResult;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.widgets.grid.ListGridRecord;


public class AssetList extends AbstractTabPresenter<AssetList.IDisplay> {

	public interface ICommandSet extends IPrintCommandSet, IBasicCommandSet {
		@IRequiredRole(IPrivileges.FA_ASSET_ADD) HideableCommand addRecord();
		@IRequiredRole(IPrivileges.FA_ASSET_REMOVE) HideableCommand removeRecord();
		Command reload();
		Command savePreferences();
		@IRequiredRole(IPrivileges.FA_ASSET_ADD) HideableCommand importAssets();
		@IRequiredRole(IPrivileges.FA_ASSET_EDIT) AssetRecordCommand edit();
		@IRequiredRole(IPrivileges.FA_ASSET_VIEW) AssetRecordCommand view();
		@IRequiredRole(IPrivileges.FA_ASSET_VIEW) AssetRecordCommand transaction();
		@IRequiredRole(IPrivileges.FA_ASSET_EDIT) AssetRecordCommand disposal();
		@IRequiredRole(IPrivileges.FA_ASSET_EDIT) AssetRecordCommand split();
		Command reportList();
	}

	public interface IDisplay extends ITabDisplay {
		void removeSelectedRecords(final IListGridConfirmAction confirmUi);
		void reload();
		void savePreferences();
		ICommandSet getCommands();
		ICurrentRecordProvider<AssetRecord> getCurrentRecordProvider();
		void addRecord(final Integer recordId);
		void updateRecord(final Integer recordId);
	}

	private final Integer		companyId;
	private final ITabManager	tabs;

	public AssetList(final Integer companyId, final IDisplay display, final ITabManager tabs) {
		super(display);
		this.companyId = companyId;
		this.tabs = tabs;
	}

	public AssetList(final Integer companyId, final ITabManager tabs) {
		this(companyId, new AssetListUi(companyId), tabs);
	}

	@Override
	public void bind() {
		super.bind();
		final ICommandSet cmd = getDisplay().getCommands();

		registerSlot(cmd.reload(), onReload);
		registerSlot(cmd.savePreferences(), onSavePreferences);
		registerSlot(cmd.addRecord(), onAddRecord);
		registerSlot(cmd.edit(), onEditRecord);
		cmd.edit().setRecordProvider(getDisplay().getCurrentRecordProvider());
		registerSlot(cmd.removeRecord(), onRemoveRecord);
		registerSlot(cmd.view(), onViewRecord);
		cmd.view().setRecordProvider(getDisplay().getCurrentRecordProvider());
		registerSlot(cmd.disposal(), onDisposeAsset);
		cmd.disposal().setRecordProvider(getDisplay().getCurrentRecordProvider());
		registerSlot(cmd.split(), onSplitAsset);
		cmd.split().setRecordProvider(getDisplay().getCurrentRecordProvider());
		registerSlot(cmd.importAssets(), onImportAsset);
		registerSlot(cmd.transaction(), onTransactionList);
		cmd.transaction().setRecordProvider(getDisplay().getCurrentRecordProvider());
	  	registerSlot(cmd.reportList(), onReportList);
		cmd.updateUi();

		MyApplication.getInstance().getPrintManager().bind(cmd, this, BuiltInReports.ASSET_REGISTER_BY_CATEGORY, new CompanyParameterValue(companyId));
	}

	private final ISlot onAddRecord = new ISlot() {
		@Override
		public void invoke() {
			AssetWizard.editNewRecord(companyId, onRecordAdded);
		}
	};

	private final ISlot1<Integer> onRecordAdded = new ISlot1<Integer>() {
		@Override
		public void invoke(final Integer recordId) {
			getDisplay().addRecord(recordId);
		}
	};

	private final ISlot1<AssetRecord> onEditRecord = new ISlot1<AssetRecord>() {
		@Override
		public void invoke(AssetRecord asset) {
			if (asset.isDisposed())
				Application.getInstance().getMessageBox().info(Resource.strings.editDisposedFixedAssetNotAllowed());
			else
				AssetWizard.editRecord(companyId, asset.getId(), onRecordUpdated);
		}
	};

	private final ISlot1<Integer> onRecordUpdated = new ISlot1<Integer>() {
		@Override
		public void invoke(final Integer recordId) {
			getDisplay().updateRecord(recordId);
		}
	};

	private final ISlot onRemoveRecord = new ISlot() {
		@Override
		public void invoke() {
			getDisplay().removeSelectedRecords(onConfirmRemoveRecord);
		}
	};

	private final IListGridConfirmAction onConfirmRemoveRecord = new IListGridConfirmAction() {
		@Override
		public void confirmRemoveRecords(final ListGridRecord[] records, final com.google.gwt.user.client.Command onSuccess) {
			final AssetRecord asset = new AssetRecord(records[0]);
			Application.getInstance().getMessageBox().ask(Resource.messages.confirmRemoveAssets(records.length, asset.getName()), onSuccess);
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

	private final ISlot1<AssetRecord> onViewRecord = new ISlot1<AssetRecord>() {
		@Override
		public void invoke(AssetRecord record) {
			ViewAssetDialog.viewRecord(record.getId());
		}
	};

	private final ISlot1<AssetRecord> onDisposeAsset = new ISlot1<AssetRecord>() {
		@Override
		public void invoke(AssetRecord record) {
			if (record.isDisposed())
				revertAssetDisposal(record);
			else
				disposeAsset(record);
		}
	};

	private void revertAssetDisposal(final AssetRecord asset) {
		Application.getInstance().getMessageBox().ask(Resource.strings.revertFixedAssetDisposal(), new BooleanCallback() {
			@Override
			public void execute(Boolean value) {
				if (value) {
					Application.getInstance().getDispatcher().execute(new RevertAssetDisposal(asset.getId()), new AbstractAsyncCallback<VoidResult>() {
						@Override
						public void onSuccess(@SuppressWarnings("unused") final VoidResult __) {
							getDisplay().updateRecord(asset.getId());
						}
					});
				} else {
					disposeAsset(asset);
				}
			}
		});
	}

	private void disposeAsset(final AssetRecord asset) {
		new AssetDisposalDialog(asset.getId(), onRecordUpdated).revealDisplay();
	}

	private final ISlot1<AssetRecord> onSplitAsset = new ISlot1<AssetRecord>() {
		@Override
		public void invoke(final AssetRecord record) {
			if (record.isDisposed())
				Application.getInstance().getMessageBox().info(Resource.strings.editDisposedFixedAssetNotAllowed());
			else
				SplitAssetWizard.editRecord(record.getId(), onRecordUpdated, onRecordAdded);
		}
	};

	private final ISlot onImportAsset = new ISlot() {
		@Override
		public void invoke() {
			GWT.runAsync(new AbstractRunAsyncCallback() {
				@Override
				public void onSuccess() {
					new ImportAssetsWizard(companyId, onReload).revealDisplay();
				}
			});
		}
	};

	private final ISlot1<AssetRecord> onTransactionList = new ISlot1<AssetRecord>() {
		@Override
		public void invoke(final AssetRecord record) {
			tabs.addTab(new TransactionList(record.getId(), record.getName()));
		}
	};

	private final ISlot onReportList = new ISlot() {
		@Override
		public void invoke() {
			GWT.runAsync(new AbstractRunAsyncCallback() {
				@Override
				public void onSuccess() {
					tabs.addTab(new UserReportList(ReportCategories.ASSET, new CompanyParameterValue(companyId)));
				}
			});
		}
	};

}
