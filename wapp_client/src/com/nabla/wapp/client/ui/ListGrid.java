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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.command.ICommandUi;
import com.nabla.wapp.client.command.ICommandUiManager;
import com.nabla.wapp.client.command.ICurrentListGridRecordProvider;
import com.nabla.wapp.client.general.AbstractAsyncCallback;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.general.Util;
import com.nabla.wapp.client.model.Model;
import com.nabla.wapp.client.model.data.BasicListGridRecord;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.server.IDispatchAsync;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.shared.command.AbstractRestore;
import com.nabla.wapp.shared.command.LoadListGridState;
import com.nabla.wapp.shared.command.SaveListGridState;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.dispatch.VoidResult;
import com.nabla.wapp.shared.model.IFieldReservedNames;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.RowEndEditAction;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.JSON;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.grid.HeaderSpan;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.GroupByEvent;
import com.smartgwt.client.widgets.grid.events.GroupByHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

/**
 * @author nabla
 *
 */
public class ListGrid extends com.smartgwt.client.widgets.grid.ListGrid implements HasWidgets, IPostCreateProcessing, ICurrentListGridRecordProvider {

	public interface IListGridConfirmAction {
		void confirmRemoveRecords(final ListGridRecord[] records, final Command onSuccess);
	}

	public interface ICellCSSTextFormatter {
		String format(final ListGridRecord record, final int rowNum, final int colNum);
	}

	private static final Logger					logger = LoggerFactory.getLog(ListGrid.class);
	// only valid during initialization
	private List<HeaderSpan>						headers = new LinkedList<HeaderSpan>();
	private List<ListGridColumn>					children = new LinkedList<ListGridColumn>();
	// support for CSS text formatter
	private ICellCSSTextFormatter					cssTextFormatter;
	// support for deleted record
	private String									recordDeletedProperty  = IFieldReservedNames.RECORD_DELETED;
	// support for expansion component
	private Form									editForm;
	private HandlerRegistration						drawFormHandler;
	private HandlerRegistration						cancelFormHandler;
	// support for
	private String									viewStateReference;
	private String[]								groupByFields;
	// support for checkbox selection
	private Integer									lastSelectedRecord;
	// support for listgrid toolbar
	private final Map<String, ListGridToolbar>		toolbars = new HashMap<String, ListGridToolbar>();
	private BasicListGridRecord						currentRecord;
	private final Canvas							defaultToolbar = new ListGridToolbar();

	public ListGrid() {
		setAlternateRecordStyles(true);
	//	setEditEvent(ListGridEditEvent.CLICK);
		setSelectionType(SelectionStyle.MULTIPLE);
		setShowHover(null);
		setGroupStartOpen(GroupStartOpen.ALL);
		// TODO: to be removed when SmartGWT supports it
		setAttribute("selectionProperty", IFieldReservedNames.RECORD_SELECTED, true);
		this.setRecordEnabledProperty(IFieldReservedNames.RECORD_ENABLED);
		this.setRecordDeletedProperty(IFieldReservedNames.RECORD_DELETED);

		addDrawHandler(new DrawHandler() {
			@Override
			public void onDraw(@SuppressWarnings("unused") final DrawEvent event) {
				if (viewStateReference != null)
					loadViewState();
			}
		});
		addGroupByHandler(new GroupByHandler() {
			@Override
			public void onGroupBy(final GroupByEvent event) {
				groupByFields = event.getFields();
			}
		});
	}

	public void setModel(final DataSource model) {
		setAutoFetchData(true);
		setDataSource(model);
	}

	public void setSummaryModel(final DataSource model) {
		this.setShowGridSummary(true);
		this.setSummaryRowDataSource(model);
	}

	public void setShowCheckbox(final boolean show) {
		setSelectionAppearance(show ? SelectionAppearance.CHECKBOX : SelectionAppearance.ROW_STYLE);
		if (show) {
			addRecordClickHandler(new RecordClickHandler() {
				@Override
				public void onRecordClick(final RecordClickEvent event) {
					int row = event.getRecordNum();
					if (event.getFieldNum() > 0) {
						final com.smartgwt.client.widgets.grid.ListGrid list = event.getViewer();
						switch (getSelectionType()) {
						case MULTIPLE:
							if (EventHandler.ctrlKeyDown()) {
								list.selectRecord(row);
							} else if (EventHandler.shiftKeyDown()) {
								if (lastSelectedRecord == null)
									list.selectSingleRecord(row);
								else {
									if (lastSelectedRecord < row) {
										for (int i = lastSelectedRecord + 1; i <= row; ++i)
											list.selectRecord(i);
									} else {
										for (int i = row; i < lastSelectedRecord; ++i)
											list.selectRecord(i);
									}
								}
							} else {
								list.selectSingleRecord(row);
							}
							break;
						case SINGLE:
						default:
							list.selectRecord(row);
							break;
						}
					}
					lastSelectedRecord = row;
				}
			});
		}
	}

	public void setAddCommand(final ICommandUiManager cmd) {
		Assert.argumentNotNull(cmd);

		cmd.addUi(new ICommandUi() {
			@Override
			public void setVisible(@SuppressWarnings("unused") final boolean value) {
			}

			@Override
			public void setText(@SuppressWarnings("unused") final String value) {
			}

			@Override
			public void setEnabled(final boolean value) {
				setListEndEditAction(value ? RowEndEditAction.NEXT : RowEndEditAction.NONE);
			}

			@Override
			public void setChecked(@SuppressWarnings("unused") final boolean value) {
			}

			@Override
			public boolean getEnabled() {
				return false;
			}
		});
	}

	public void setEditCommand(final ICommandUiManager cmd) {
		Assert.argumentNotNull(cmd);

		cmd.addUi(new ICommandUi() {
			@Override
			public void setVisible(@SuppressWarnings("unused") final boolean value) {
			}

			@Override
			public void setText(@SuppressWarnings("unused") final String value) {
			}

			@Override
			public void setEnabled(final boolean value) {
				setCanEdit(value);
			//	setCanExpandRecords(value);
			}

			@Override
			public void setChecked(@SuppressWarnings("unused") final boolean value) {
			}

			@Override
			public boolean getEnabled() {
				return false;
			}
		});
	}

	@Override
	public void add(final Widget w) {
		Assert.argumentNotNull(w);
		Assert.notNull(children);

		if (w instanceof ListGridColumn)
			children.add((ListGridColumn)w);
		else if (w instanceof ListGridForm)
			editForm = ((ListGridForm)w).getForm();
		else if (w instanceof ListGridToolbar) {
			final ListGridToolbar tb = (ListGridToolbar)w;
			if (!tb.isEmpty()) {
				if (tb.getWidth() <= 1)
					tb.setWidth(17 * tb.getMemberCount());
				toolbars.put(tb.getField(), tb);
				this.setShowRollOverCanvas(true);
				this.setUseCellRollOvers(toolbars.size() > 1 || tb.getField() != null);
			}
		} else if (w instanceof ListGridHeaderSpan) {
			final ListGridHeaderSpan header = (ListGridHeaderSpan)w;
			children.addAll(header.getColumns());
			headers.add(header.getImpl());
		} else if (w instanceof Menu) {
			this.setContextMenu((Menu)w);
		} else {
			logger.log(Level.SEVERE,"adding a widget of type '" + w.getClass().toString() + "' to a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
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

		final List<com.smartgwt.client.widgets.grid.ListGridField> fields = new LinkedList<com.smartgwt.client.widgets.grid.ListGridField>();
		int dataField = -1;
		for (int i = 0; i < children.size(); ++i) {
			final ListGridColumn c = children.get(i);
			if (dataField == -1)
				dataField = i;
			else
				dataField = -2;
			fields.add(c.getField(this));
		}
		if (dataField >= 0) {
			// set following default because only 1 column of data
			final ListGridColumn c = children.get(dataField);
			c.setCanFreeze(false);
			c.setCanGroupBy(false);
		}
		this.setFields(fields.toArray(new com.smartgwt.client.widgets.grid.ListGridField[0]));
		children = null;
		if (!headers.isEmpty())
			this.setHeaderSpans(headers.toArray(new HeaderSpan[0]));
		headers = null;
	}

	public void reload() {
		discardAllEdits();
		invalidateCache();
	}

	public void addRecord() {
		startEditingNew();
	}

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

	public void removeSelectedRecords() {
		removeSelectedData();
	}

	public void removeSelectedRecords(final IListGridConfirmAction confirmUi) {
		if (confirmUi == null)
			removeSelectedData();
		else {
			final ListGridRecord[] records = getSelectedRecords();
			if (records != null && records.length > 0) {
				confirmUi.confirmRemoveRecords(records, new Command() {
					@Override
					public void execute() {
						logger.fine("removing records");
						removeSelectedData();
					}
				});
			} else {
				logger.fine("no records to remove");
			}
		}
	}

	public void setCellCSSTextFormatter(final ICellCSSTextFormatter cssTextFormatter) {
		this.cssTextFormatter = cssTextFormatter;
	}

	public ICellCSSTextFormatter getCellCSSTextFormatter() {
		return cssTextFormatter;
	}

	@Override
	protected String getCellCSSText(final ListGridRecord record, final int rowNum, final int colNum) {
		if (cssTextFormatter != null) {
			final String rslt = cssTextFormatter.format(record, rowNum, colNum);
			if (rslt != null)
				return rslt;
		}
		return super.getCellCSSText(record, rowNum, colNum);
    }

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

		dispatcher.execute(new LoadListGridState(getViewStateReference()), new AsyncCallback<StringResult>() {
			@Override
			public void onFailure(@SuppressWarnings("unused") final Throwable caught) {
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
									ListGrid.this.groupBy(groupBy);
								logger.fine("view preferences restored for ListGrid '" + getViewStateReference() + "'");
							}
						});
					}
				}
			}
		});
	}

	public void saveViewState() {
		final Model model = (Model) getDataSource();
		saveViewState(model.getDispatcher());
	}

	public void saveViewState(final IDispatchAsync dispatcher) {
		Assert.argumentNotNull(dispatcher);
		Assert.notNull(getViewStateReference());

		final Record state = new Record();
		state.setAttribute("viewState", getViewState());
		state.setAttribute("groupBy", groupByFields);
		dispatcher.execute(new SaveListGridState(getViewStateReference(), JSON.encode(state.getJsObj())), new AsyncCallback<VoidResult>() {
			@Override
			public void onFailure(@SuppressWarnings("unused") final Throwable caught) {
				logger.warning("fail to save view preferences for ListGrid '" + getViewStateReference() + "'");
			}

			@Override
			public void onSuccess(@SuppressWarnings("unused") final VoidResult __) {
				logger.fine("view preferences saved for ListGrid '" + getViewStateReference() + "'");
			}

		});
 	}

	@Override
    protected Canvas getExpansionComponent(final ListGridRecord record) {
		if (drawFormHandler != null)
			drawFormHandler.removeHandler();
		if (cancelFormHandler != null)
			cancelFormHandler.removeHandler();
		if (editForm == null)
			return super.getExpansionComponent(record);
		drawFormHandler = editForm.addDrawHandler(new DrawHandler() {
			@Override
			public void onDraw(@SuppressWarnings("unused") final DrawEvent event) {
				editForm.editRecord(record);
			}
		});
		final CancelButton cancelButton = editForm.findChild(CancelButton.class, true);
		if (cancelButton != null) {
			cancelFormHandler = cancelButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(@SuppressWarnings("unused") final ClickEvent event) {
					collapseRecord(record);
				}
			});
		}
		return editForm;
	}

	public String getRecordDeletedProperty() {
		return recordDeletedProperty;
	}

	public void setRecordDeletedProperty(final String value) {
		recordDeletedProperty = value;
	}

	public boolean onRestore(final AbstractRestore cmd) {
		Assert.argumentNotNull(cmd);

		final ListGridRecord[] records = getSelectedRecords();
		if (records == null || records.length <= 0)
			return false;
		for (final ListGridRecord record : records) {
			if (!record.getAttributeAsBoolean(getRecordDeletedProperty()))
				return false;
			cmd.add(record.getAttributeAsInt(IdField.NAME));
		}
		final Model model = (Model) getDataSource();
		model.getDispatcher().execute(cmd, new AbstractAsyncCallback<VoidResult>() {
			@Override
			public void onSuccess(@SuppressWarnings("unused") final VoidResult __) {
				for (final ListGridRecord record : records)
					record.setAttribute(getRecordDeletedProperty(), false);
				markForRedraw();
			}
		});
		return true;
	}

	@Override
    protected Canvas getRollOverCanvas(final Integer rowNum, final Integer colNum) {
		if (!toolbars.isEmpty()) {
			final ListGridRecord record = this.getRecord(rowNum);
			if (ListGridColumn.isDataRecord(record)) {
				currentRecord = new BasicListGridRecord(record);
				if (getUseCellRollOvers()) {
					final ListGridToolbar rslt = toolbars.get(getFieldName(colNum));
					if (rslt != null) {
					//	logger.finest("showing listgrid toolbar for field '" + getFieldName(colNum) + "'");
						return rslt;
					}
				} else {
					final ListGridToolbar rslt = toolbars.get(null);
					Assert.notNull(rslt != null);
					return rslt;
				}
			} else
				currentRecord = null;
			return defaultToolbar;
		}
		return super.getRollOverCanvas(rowNum, colNum);
	}

	@Override
	public BasicListGridRecord getCurrentRecord() {
		if (currentRecord != null)
			return currentRecord;
		final ListGridRecord[] records = getSelectedRecords();
		return (records != null && records.length == 1) ? new BasicListGridRecord(records[0]) : null;
	}

	public JavaScriptObject getCurrentRecordData() {
		if (currentRecord != null)
			return currentRecord.getJsObj();
		final ListGridRecord[] records = getSelectedRecords();
		return (records != null && records.length == 1) ? records[0].getJsObj() : null;
	}

}
