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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.general.Util;
import com.nabla.wapp.client.model.Model;
import com.nabla.wapp.client.server.IDispatchAsync;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.viewer.DetailViewerField;


public class TileGrid extends com.smartgwt.client.widgets.tile.TileGrid implements HasWidgets, IPostCreateProcessing {

	private static final Logger		logger = LoggerFactory.getLog(TileGrid.class);
	// only valid during initialization
	private List<TileGridField>		children = new LinkedList<TileGridField>();
	// support for
	private String					viewStateReference;

	public TileGrid() {
		setSelectionType(SelectionStyle.SINGLE);
		setWrapValues(true);
/*		addDrawHandler(new DrawHandler() {
			@Override
			public void onDraw(@SuppressWarnings("unused") DrawEvent event) {
				if (viewStateReference != null)
					loadViewState();
			}
		});*/
	}

	public void setModel(final DataSource model) {
		setAutoFetchData(true);
		setDataSource(model);
	}

	@Override
	public void add(final Widget w) {
		Assert.argumentNotNull(w);
		Assert.notNull(children);

		if (w instanceof TileGridField) {
			children.add((TileGridField)w);
		} else if (w instanceof Menu)
			this.setContextMenu((Menu)w);
		else {
			logger.log(Level.SEVERE, "adding a widget of type '" + w.getClass().toString() + "' to a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
		}
	}

	@Override
	public void clear() {
	}

	@Override
	public Iterator<Widget> iterator() {
        return null;
	}

	@Override
	public boolean remove(@SuppressWarnings("unused") final Widget w) {
		logger.log(Level.SEVERE, "removing children widget from a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
        return false;
	}

	@Override
	public void onCreate() {
		Assert.notNull(children);

		final List<DetailViewerField> fields = new LinkedList<DetailViewerField>();
		for (TileGridField c : children)
			fields.add(c.getField(this));
		children = null;
		this.setFields(fields.toArray(new DetailViewerField[0]));
	}

	public void reload() {
		logger.fine("reloading tile grid");
		invalidateCache();
		this.fetchData();
	}
/*
	public int getSelectedRecordCount() {
		//final ListGridRecord[] selectedRecords = getSelection();
		final ListGridRecord[] selectedRecords = getSelectedRecords();
		return (selectedRecords != null) ? selectedRecords.length : 0;
	}

	public List<Integer> getSelectedRecordIds() {
		final List<Integer> ids = new LinkedList<Integer>();
		final ListGridRecord[] records = getSelectedRecords();
		if (records != null && records.length > 0) {
			for (final ListGridRecord record : records)
				ids.add(record.getAttributeAsInt(IdField.NAME));
		}
		return ids;
	}
*/
	public String getViewStateReference() {
		return viewStateReference;
	}

	public void setViewStateReference(final String reference) {
		this.viewStateReference = reference;
	}

	public void loadViewState() {
		final Model model = (Model) getDataSource();
		loadViewState(model.getDispatcher());
	}

	public void loadViewState(final IDispatchAsync dispatcher) {
		Assert.argumentNotNull(dispatcher);
		Assert.notNull(getViewStateReference());
/*
		dispatcher.execute(new LoadListGridState(getViewStateReference()), new AsyncCallback<StringResult>() {
			@Override
			public void onFailure(@SuppressWarnings("unused") Throwable caught) {
				logger.warning("fail to load view preferences for ListGrid '" + getViewStateReference() + "'");
			}

			@Override
			public void onSuccess(final StringResult result) {
				if (result != null) {
					final String state = result.get();
					if (state != null) {
						Scheduler.get().scheduleDeferred(new ScheduledCommand() {
							@Override
							public void execute() {
								final Record values = new Record(JSON.decode(state));
								setViewState(values.getAttribute("viewState"));
								String[] groupBy = values.getAttributeAsStringArray("groupBy");
								if (groupBy != null)
									TileGrid.this.groupBy(groupBy);
								logger.fine("view preferences restored for ListGrid '" + getViewStateReference() + "'");
							}
						});
					}
				}
			}
		});*/
	}

	public void saveViewState() {
		final Model model = (Model) getDataSource();
		saveViewState(model.getDispatcher());
	}

	public void saveViewState(final IDispatchAsync dispatcher) {
		Assert.argumentNotNull(dispatcher);
		Assert.notNull(getViewStateReference());
/*
		final Record state = new Record();
		state.setAttribute("viewState", getViewState());
		state.setAttribute("groupBy", groupByFields);
		dispatcher.execute(new SaveListGridState(getViewStateReference(), JSON.encode(state.getJsObj())), new AsyncCallback<VoidResult>() {
			@Override
			public void onFailure(@SuppressWarnings("unused") Throwable caught) {
				logger.warning("fail to save view preferences for ListGrid '" + getViewStateReference() + "'");
			}

			@Override
			public void onSuccess(@SuppressWarnings("unused") VoidResult __) {
				logger.fine("view preferences saved for ListGrid '" + getViewStateReference() + "'");
			}

		});*/
 	}

}
