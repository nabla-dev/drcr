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
package com.nabla.wapp.client.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.model.Model;
import com.nabla.wapp.client.model.data.ListGridFilterRecord;
import com.nabla.wapp.client.server.IDispatchAsync;
import com.nabla.wapp.shared.command.AddListGridFilter;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IntegerResult;
import com.nabla.wapp.shared.model.IListGridFilter;
import com.nabla.wapp.shared.model.ValidationException;
import com.smartgwt.client.data.AdvancedCriteria;
import com.smartgwt.client.util.JSON;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.FilterBuilder;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.menu.IMenuButton;
import com.smartgwt.client.widgets.menu.events.ItemClickEvent;
import com.smartgwt.client.widgets.menu.events.ItemClickHandler;

/**
 * @author nabla
 *
 */
public class ListGridFilter extends VLayout {

	class FilterList extends Menu {

		public FilterList() {
		//	setDataSource(new ListGridFilterListModel(getDispatcher(), getSavedFilterReference()));
			setWidth(260);
			addItemClickHandler(new ItemClickHandler() {
				@Override
				public void onItemClick(ItemClickEvent event) {
					restoreFilter(event.getItem());
				}
			});
		}

	}

	private static final Logger		logger = LoggerFactory.getLog(ListGridFilter.class);

	final FilterBuilder		filter = new FilterBuilder();
	private final HLayout	hp = new HLayout();
	private ListGrid		list = null;
	private String			savedFilterReference = null;
	private IDispatchAsync	dispatcher;
	private IMenuButton		restoreButton = null;

	public ListGridFilter() {
		hp.setMembersMargin(0);

		final IButton applyFilter = new IButton();
		new TitleDecoder(Resource.strings.filterBuilderFilterButton()).apply(applyFilter);
		applyFilter.setIcon("[SKIN]actions/filter.png");
		applyFilter.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(@SuppressWarnings("unused") final ClickEvent event) {
				applyCurrentFilter();
			}
		});
		hp.addMember(applyFilter);

		final IButton clearFilter = new IButton();
		new TitleDecoder(Resource.strings.filterBuilderClearButton()).apply(clearFilter);
		clearFilter.setIcon("clear_filter_small.png");
		clearFilter.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(@SuppressWarnings("unused") final ClickEvent event) {
				clearFilter();
			}
		});
		hp.addMember(clearFilter);

		this.addMember(hp);
	}

	@Override
	public void setMembersMargin(final int membersMargin) {
		super.setMembersMargin(membersMargin);
		hp.setMembersMargin(membersMargin);
	}

	public ListGrid getListGrid() {
		return list;
	}

	public AdvancedCriteria getCriteria() {
		return filter.getCriteria();
	}

	public String getSavedFilterReference() {
		return savedFilterReference;
	}

	public void setSavedFilterReference(String reference) {
		this.savedFilterReference = reference;
	}

	@Override
	public void onCreate() {
		// IMPORTANT: need to set datasource to filter before adding it to layout
		// otherwise: crash!!!
		list = (ListGrid) this.getMembers()[1];
		Assert.notNull(list);
		final Model model = (Model) list.getDataSource();
		dispatcher = model.getDispatcher();
		Assert.notNull(dispatcher);
		filter.setDataSource(model);
		this.addMember(filter, 0);

		if (getSavedFilterReference() != null) {
			final IButton saveFilter = new IButton();
			new TitleDecoder(Resource.strings.filterBuilderSaveButton()).apply(saveFilter);
			saveFilter.setIcon("[SKIN]actions/save.png");
			saveFilter.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(@SuppressWarnings("unused") final ClickEvent event) {
					saveFilter(filter.getCriteria());
				}
			});
			hp.addMember(saveFilter);

			restoreButton = new IMenuButton(Resource.strings.filterBuilderRestoreButton(), new FilterList());
			restoreButton.setWidth(100);
			hp.addMember(restoreButton);

			final IButton editButton = new IButton();
			new TitleDecoder(Resource.strings.filterBuilderFilterEditFilters()).apply(editButton);
			editButton.setIcon("[SKIN]actions/edit.png");
			editButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(@SuppressWarnings("unused") ClickEvent event) {
				/*	final ListGridFilterListDialog dlg = new ListGridFilterListDialog(new ListGridFilterListDialogUi(new ListGridFilterListModel(getDispatcher(), getSavedFilterReference())));
					dlg.getCloseSlots().connect(new ISlot() {
						@Override
						public void invoke() {
							refreshSavedFilterList();
						}
					});
					dlg.revealDisplay();*/
				}
			});
			hp.addMember(editButton);
		}
		hp.addMember(new HLayoutSpacer());
		super.onCreate();
	}


	public void applyCurrentFilter() {
		list.fetchData(filter.getCriteria());
	}

	public void clearFilter() {
		filter.clearCriteria();
		applyCurrentFilter();
	}

	protected void saveFilter(final AdvancedCriteria criteria) {
		Application.getInstance().getMessageBox().prompt(Resource.strings.filterBuilderFilterNameLabel(), new ValueCallback() {
			@Override
			public void execute(String name) {
				if (name != null && !name.isEmpty()) {
					final String value = JSON.encode(criteria.getJsObj());
					final ValidationException x = new ValidationException();
					try {
						IListGridFilter.NAME_CONSTRAINT.validate(IListGridFilter.NAME, name, x);
						IListGridFilter.VALUE_CONSTRAINT.validate(IListGridFilter.VALUE, value, x);
					} catch (DispatchException _) {}
					if (x.isEmpty()) {
						saveFilter(name, value);
					} else {
						logger.log(Level.WARNING, "fail to save filter ListGrid '" + getSavedFilterReference() + "'", x);
						Application.getInstance().getMessageBox().error(x);
					}
				}
			}
		});
	}

	protected void saveFilter(final String name, final String value) {
		getDispatcher().execute(new AddListGridFilter(getSavedFilterReference(), name, value), new AsyncCallback<IntegerResult>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.WARNING, "fail to save filter ListGrid '" + getSavedFilterReference() + "'", caught);
			}

			@Override
			public void onSuccess(@SuppressWarnings("unused") IntegerResult __) {
				logger.fine("filter saved for ListGrid '" + getSavedFilterReference() + "'");
				refreshSavedFilterList();
			}
		});
	}

	protected void restoreFilter(final ListGridRecord record) {
		if (record != null)
			restoreFilter(new ListGridFilterRecord(record));
		else
			clearFilter();
	}

	protected void restoreFilter(final ListGridFilterRecord record) {
		filter.setCriteria(record.getCriteria());
		applyCurrentFilter();
	}

	protected void refreshSavedFilterList() {
		restoreButton.setMenu(new FilterList());
	}

	public IDispatchAsync getDispatcher() {
		return dispatcher;
	}

}
