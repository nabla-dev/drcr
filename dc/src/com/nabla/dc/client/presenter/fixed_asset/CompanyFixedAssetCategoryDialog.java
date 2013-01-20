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

import java.util.List;
import java.util.logging.Logger;

import com.nabla.dc.client.MyApplication;
import com.nabla.dc.client.model.fixed_asset.CompanyFixedAssetCategoryRecord;
import com.nabla.dc.client.model.fixed_asset.FixedAssetCategoryRecord;
import com.nabla.dc.client.ui.fixed_asset.CompanyFixedAssetCategoryDialogUi;
import com.nabla.dc.shared.report.BuiltInReports;
import com.nabla.dc.shared.report.ReportParameterTypes;
import com.nabla.wapp.client.command.IBasicCommandSet;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.mvp.AbstractTopPresenter;
import com.nabla.wapp.client.mvp.ITopDisplay;
import com.nabla.wapp.client.print.IPrintCommandSet;
import com.nabla.wapp.report.shared.parameter.IntegerParameterValue;
import com.nabla.wapp.shared.slot.ISlot1;
import com.nabla.wapp.shared.slot.ISlot2;
import com.nabla.wapp.shared.slot.ISlotManager1;
import com.nabla.wapp.shared.slot.ISlotManager2;


public class CompanyFixedAssetCategoryDialog extends AbstractTopPresenter<CompanyFixedAssetCategoryDialog.IDisplay> {

	private static final Logger	log = LoggerFactory.getLog(CompanyFixedAssetCategoryDialog.class);

	public interface ICommandSet extends IPrintCommandSet, IBasicCommandSet {}

	public interface IDisplay extends ITopDisplay {
		ISlotManager2<CompanyFixedAssetCategoryRecord, List<FixedAssetCategoryRecord>> getCategoryDropSlots();
		ISlotManager2<CompanyFixedAssetCategoryRecord, List<CompanyFixedAssetCategoryRecord>> getCategoryReparentSlots();
		ISlotManager1<List<CompanyFixedAssetCategoryRecord>> getAvailableCategoryDropSlots();
		ICommandSet getCommands();

		void removeCategory(final CompanyFixedAssetCategoryRecord record);
		void removeCategory(final FixedAssetCategoryRecord record);
		void addCategory(final CompanyFixedAssetCategoryRecord record);
		void addCategory(final FixedAssetCategoryRecord record);
	}

	interface CategoryDropSlot extends ISlot2<CompanyFixedAssetCategoryRecord, List<FixedAssetCategoryRecord>> {}
	interface CategoryReparentSlot extends ISlot2<CompanyFixedAssetCategoryRecord, List<CompanyFixedAssetCategoryRecord>> {}
	interface AvailableCategoryDropSlot extends ISlot1<List<CompanyFixedAssetCategoryRecord>> {}

	private final Integer	companyId;

	public CompanyFixedAssetCategoryDialog(final Integer companyId) {
		super(new CompanyFixedAssetCategoryDialogUi(companyId));
		this.companyId = companyId;
	}

	@Override
	public void bind() {
		super.bind();
		registerSlot(getDisplay().getCategoryDropSlots(), onCategoryDrop);
		registerSlot(getDisplay().getCategoryReparentSlots(), onCategoryReparent);
		registerSlot(getDisplay().getAvailableCategoryDropSlots(), onAvailableCategoryDrop);

		MyApplication.getInstance().getPrintManager().bind(getDisplay().getCommands(), this, BuiltInReports.COMPANY_FIXED_ASSET_CATEGORY_LIST, new IntegerParameterValue(ReportParameterTypes.CompanyId.getParameterName(), companyId));
	}

	private AvailableCategoryDropSlot onAvailableCategoryDrop = new AvailableCategoryDropSlot() {
		@Override
		public void invoke(final List<CompanyFixedAssetCategoryRecord> records) {
			if (isLeaf(records)) {
				for (CompanyFixedAssetCategoryRecord record : records) {
					log.fine("removing '" + record.getName() + "' from list of company asset categories");
					getDisplay().removeCategory(record);
					getDisplay().addCategory(new FixedAssetCategoryRecord(record));
				}
			} else {
				log.fine("stop removing fs category");
			}
		}
	};

	private CategoryDropSlot onCategoryDrop = new CategoryDropSlot() {
		@Override
		public void invoke(final CompanyFixedAssetCategoryRecord parent, final List<FixedAssetCategoryRecord> records) {
			if (parent.isNew()) {
				log.fine("stop dropping category to ROOT folder");
			} else {
				for (FixedAssetCategoryRecord record : records) {
					final CompanyFixedAssetCategoryRecord child = new CompanyFixedAssetCategoryRecord(parent, record);
					log.fine("dropping category '" + child.getName() + "' into '" + parent.getName() + "'");
					getDisplay().removeCategory(record);
					getDisplay().addCategory(child);
				}
			}
		}
	};

	private CategoryReparentSlot onCategoryReparent = new CategoryReparentSlot() {
		@Override
		public void invoke(final CompanyFixedAssetCategoryRecord parent, final List<CompanyFixedAssetCategoryRecord> records) {
			if (parent.isNew()) {
				log.fine("stop dropping category to ROOT folder");
			} else {
				if (isLeaf(records)) {
					for (CompanyFixedAssetCategoryRecord record : records) {
						log.fine("moving category '" + record.getName() + "' into '" + parent.getName() + "'");
						final CompanyFixedAssetCategoryRecord child = new CompanyFixedAssetCategoryRecord(parent, record);
						getDisplay().removeCategory(record);
						getDisplay().addCategory(child);
					}
				} else {
					log.fine("stop moving a folder");
				}
			}
		}
	};

	private boolean isLeaf(final List<CompanyFixedAssetCategoryRecord> records) {
		for (CompanyFixedAssetCategoryRecord record : records) {
			if (record.isFolder())
				return false;
		}
		return true;
	}

}
