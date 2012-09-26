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

import com.nabla.dc.client.model.fixed_asset.AssetRecord;
import com.nabla.dc.client.presenter.ITabManager;
import com.nabla.dc.client.ui.Resource;
import com.nabla.dc.client.ui.fixed_asset.AssetListUi;
import com.nabla.dc.shared.IPrivileges;
import com.nabla.wapp.client.command.Command;
import com.nabla.wapp.client.command.HideableCommand;
import com.nabla.wapp.client.command.IBasicCommandSet;
import com.nabla.wapp.client.command.ICurrentRecordProvider;
import com.nabla.wapp.client.command.IRequiredRole;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.mvp.AbstractTabPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.print.IPrintCommandSet;
import com.nabla.wapp.client.ui.ListGrid.IListGridConfirmAction;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * @author nabla
 *
 */
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
/*
		Command userReportList();
*/
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
/*
	@Inject private PrintManager	printerManager;
*/
	public AssetList(final Integer companyId, final IDisplay display, final ITabManager tabs) {
		super(display);
		this.companyId = companyId;
		this.tabs = tabs;
	}

	public AssetList(final Integer companyId, final ITabManager tabs) {
		this(companyId, new AssetListUi(companyId), tabs);
	}

	@Override
	protected void onBind() {
		final ICommandSet cmd = getDisplay().getCommands();

		registerSlot(cmd.reload(), onReload);
		registerSlot(cmd.savePreferences(), onSavePreferences);

		registerSlot(cmd.addRecord(), onAddRecord);
		registerSlot(cmd.edit(), onEditRecord);
		cmd.edit().setRecordProvider(getDisplay().getCurrentRecordProvider());
		registerSlot(cmd.removeRecord(), onRemoveRecord);
		registerSlot(cmd.view(), onViewRecord);
		cmd.view().setRecordProvider(getDisplay().getCurrentRecordProvider());
/*		registerSlot(cmd.userReportList(), onUserReportList);
		registerSlot(cmd.editAssetCategoryList(), onEditAssetCategoryList);
		registerSlot(cmd.editBalanceSheetCategoryList(), onEditBalanceSheetCategoryList);
		registerSlot(cmd.importAssets(), onImportAsset);

		final IRecordCommandSet rcmd = display.getRecordCommands();


		registerSlot(rcmd.disposal(), onDisposeAsset);
		registerSlot(rcmd.split(), onSplitAsset);
		registerSlot(rcmd.transaction(), onTransactionList);
	*/
		cmd.updateUi();

//		printerManager.bind(cmd, this, BuiltInReports.ASSET_LIST, new IntegerSetReportParameterValue(ReportParameterTypes.RegisterIdListField.getDefaultParameterName(), assetRegisterId));
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

		}
	};

	/*
	private final ISlot onUserReportList = new ISlot() {
		@Override
		public void invoke() {
			workspace.addTab(userReportListFactory.get(assetRegisterId, assetRegisterName));
		}
	};

	private final ISlot onImportAsset = new ISlot() {
		@Override
		public void invoke() {
			importAssetWizardFactory.get(assetRegisterId, new ISlot() {
				@Override
				public void invoke() {
					display.reload();
				}
			}).revealDisplay();
		}
	};


	private final ISlot1<Record> onDisposeAsset = new ISlot1<Record>() {
		@Override
		public void invoke(Record record) {
			final AssetRecord asset = new AssetRecord(record);
			if (asset.isDisposed()) {
				msgBox.ask(Resource.strings.revertAssetDisposal(), new BooleanCallback() {
					@Override
					public void execute(Boolean value) {
						if (value) {
							server.execute(new RevertAssetDisposal(asset.getId()), new AbstractAsyncCallback<VoidResult>() {
								@Override
								public void onSuccess(@SuppressWarnings("unused") final VoidResult __) {
									asset.clearDisposal();
									display.updateRecord(asset.getImpl());
								}
							});
						} else {
							disposeAsset(asset);
						}
					}
				});
			} else {
				disposeAsset(asset);
			}
		}
	};

	private void disposeAsset(final AssetRecord asset) {
		final AssetDisposalDialog dlg = assetDisposalWizardFactory.get(asset.getId(), asset.getName());
		dlg.getSuccessSlots().connect(new ISlot1<Record>() {
			@Override
			public void invoke(Record disposal) {
				asset.setDisposal(disposal);
				display.updateRecord(asset.getImpl());
			}
		});
		dlg.revealDisplay();
	}

	private final ISlot1<Record> onSplitAsset = new ISlot1<Record>() {
		@Override
		public void invoke(final Record record) {
			final AssetRecord asset = new AssetRecord(record);
			if (asset.isDisposed())
				msgBox.info(Resource.strings.editDisposedAssetNotAllowed());
			else {
				assetSplitWizardFactory.get(asset, new ISlot2<ListGridRecord, ListGridRecord>() {
					@Override
					public void invoke(final ListGridRecord assetA, final ListGridRecord assetB) {
						display.updateRecord(assetA);
						display.addRecord(assetB);
					}
				}).revealDisplay();
			}
		}
	};

	private final ISlot1<Record> onTransactionList = new ISlot1<Record>() {
		@Override
		public void invoke(final Record record) {
			final AssetRecord asset = new AssetRecord(record);
			workspace.addTab(transactionListFactory.get(asset.getId(), asset.getName(), asset.isDisposed()));
		}
	};*/
}
