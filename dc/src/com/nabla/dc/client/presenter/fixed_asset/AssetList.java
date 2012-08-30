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

import com.nabla.dc.client.presenter.ITabManager;
import com.nabla.dc.client.ui.fixed_asset.AssetListUi;
import com.nabla.dc.shared.IPrivileges;
import com.nabla.wapp.client.command.Command;
import com.nabla.wapp.client.command.HideableCommand;
import com.nabla.wapp.client.command.IBasicCommandSet;
import com.nabla.wapp.client.command.IRequiredRole;
import com.nabla.wapp.client.mvp.AbstractTabPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.print.IPrintCommandSet;
import com.nabla.wapp.shared.slot.ISlot;

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
		/*		@IRequiredRole(IPrivileges.ASSET_EDIT) HideableCommand editAssetCategoryList();
		@IRequiredRole(IPrivileges.ASSET_EDIT) HideableCommand editBalanceSheetCategoryList();

		Command userReportList();
*/
	}

	public interface IDisplay extends ITabDisplay {
//		void removeSelectedRecords(final IListGridConfirmAction confirmUi);
		void reload();
		void savePreferences();
		ICommandSet getCommands();
/*		void addRecord(final Record record);
		void updateRecord(final Record record);*/
	}
/*
	public interface IWizardDataFactory {
		WizardData get(final Record record);
	}
*/
	private final ITabManager		tabs;
/*	private final Integer			assetRegisterId;
	private final String			assetRegisterName;
	private final ITabManager		workspace;
	@Inject private IMessageBox		msgBox;
	@Inject private PrintManager	printerManager;
	@Inject private IServer			server;
	@Inject private AssetRegisterAssetCategoryListDialog.IFactory			assetCategoryListDialogFactory;
	@Inject private AssetRegisterBalanceSheetCategoryListDialog.IFactory	bsCategoryListDialogFactory;
	@Inject private AssetWizard.IModelFactory								wizardModelFactory;
	@Inject private AssetWizard.IFactory									assetWizardFactory;
	@Inject private AssetDisposalDialog.IFactory							assetDisposalWizardFactory;
	@Inject private AssetSplitWizard.IFactory								assetSplitWizardFactory;
	@Inject private ImportAssetWizard.IFactory								importAssetWizardFactory;
	@Inject private UserReportList.IFactory									userReportListFactory;
	@Inject private ViewAssetDialog.IFactory								viewAssetDialogFactory;
	@Inject private TransactionList.IFactory								transactionListFactory;
*/
	public AssetList(final IDisplay display, final ITabManager tabs) {
		super(display);
		this.tabs = tabs;
	}

	public AssetList(final Integer companyId, final ITabManager tabs) {
		this(new AssetListUi(companyId), tabs);
	}

	@Override
	protected void onBind() {
		final ICommandSet cmd = getDisplay().getCommands();

		registerSlot(cmd.reload(), onReload);
		registerSlot(cmd.savePreferences(), onSavePreferences);
/*
		registerSlot(cmd.addRecord(), onAddRecord);
		registerSlot(cmd.removeRecord(), onRemoveRecord);
		registerSlot(cmd.userReportList(), onUserReportList);
		registerSlot(cmd.editAssetCategoryList(), onEditAssetCategoryList);
		registerSlot(cmd.editBalanceSheetCategoryList(), onEditBalanceSheetCategoryList);
		registerSlot(cmd.importAssets(), onImportAsset);

		final IRecordCommandSet rcmd = display.getRecordCommands();
		registerSlot(rcmd.edit(), onEditRecord);
		registerSlot(rcmd.view(), onViewRecord);
		registerSlot(rcmd.disposal(), onDisposeAsset);
		registerSlot(rcmd.split(), onSplitAsset);
		registerSlot(rcmd.transaction(), onTransactionList);
	*/
		cmd.updateUi();

//		printerManager.bind(cmd, this, BuiltInReports.ASSET_LIST, new IntegerSetReportParameterValue(ReportParameterTypes.RegisterIdListField.getDefaultParameterName(), assetRegisterId));
	}
/*
	private final ISlot onAddRecord = new ISlot() {
		@Override
		public void invoke() {
			wizardModelFactory.editNewRecord(assetRegisterId, new AbstractAsyncCallback<WizardData>() {
				@Override
				public void onSuccess(WizardData result) {
					assetWizardFactory.get(result, onRecordAdded).revealDisplay();
				}
			});
		}
	};

	private final ISlot1<Record> onRecordAdded = new ISlot1<Record>() {
		@Override
		public void invoke(final Record record) {
			display.addRecord(record);
		}
	};

	private final ISlot onRemoveRecord = new ISlot() {
		@Override
		public void invoke() {
			display.removeSelectedRecords(onConfirmRemoveRecord);
		}
	};

	private final IListGridConfirmAction onConfirmRemoveRecord = new IListGridConfirmAction() {
		@Override
		public void confirmRemoveRecords(final ListGridRecord[] records, final com.google.gwt.user.client.Command onSuccess) {
			final AssetRecord asset = new AssetRecord(records[0]);
			msgBox.ask(Resource.messages.confirmRemoveAssets(records.length, asset.getName()), onSuccess);
		}
	};
*/
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
/*
	private final ISlot onUserReportList = new ISlot() {
		@Override
		public void invoke() {
			workspace.addTab(userReportListFactory.get(assetRegisterId, assetRegisterName));
		}
	};

	private final ISlot onEditAssetCategoryList = new ISlot() {
		@Override
		public void invoke() {
			assetCategoryListDialogFactory.get(assetRegisterId, assetRegisterName).revealDisplay();
		}
	};

	private final ISlot onEditBalanceSheetCategoryList = new ISlot() {
		@Override
		public void invoke() {
			bsCategoryListDialogFactory.get(assetRegisterId, assetRegisterName).revealDisplay();
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

	private final ISlot1<Record> onViewRecord = new ISlot1<Record>() {
		@Override
		public void invoke(Record record) {
			viewAssetDialogFactory.get(new AssetRecord(record), assetRegisterId).revealDisplay();
		}
	};

	private final ISlot1<Record> onEditRecord = new ISlot1<Record>() {
		@Override
		public void invoke(Record record) {
			final AssetRecord asset = new AssetRecord(record);
			if (asset.isDisposed())
				msgBox.info(Resource.strings.editDisposedAssetNotAllowed());
			else {
				wizardModelFactory.editRecord(assetRegisterId, asset.getId(), new AbstractAsyncCallback<WizardData>() {
					@Override
					public void onSuccess(WizardData result) {
						assetWizardFactory.get(result, onRecordUpdated).revealDisplay();
					}
				});
			}
		}
	};

	private final ISlot1<Record> onRecordUpdated = new ISlot1<Record>() {
		@Override
		public void invoke(final Record record) {
			display.updateRecord(record);
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
