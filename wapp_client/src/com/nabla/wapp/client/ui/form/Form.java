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
package com.nabla.wapp.client.ui.form;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.general.Util;
import com.nabla.wapp.client.model.AsyncGetDefaultValuesCallback;
import com.nabla.wapp.client.model.Model;
import com.nabla.wapp.client.ui.IHasWidgets;
import com.nabla.wapp.client.ui.IPostCreateProcessing;
import com.nabla.wapp.client.ui.SubmitButton;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.IHasValue;
import com.nabla.wapp.shared.signal.Signal;
import com.nabla.wapp.shared.slot.ISlotManager;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.events.SubmitValuesEvent;
import com.smartgwt.client.widgets.form.events.SubmitValuesHandler;
import com.smartgwt.client.widgets.form.fields.FormItem;

/**
 * @author nabla
 *
 */
public class Form extends com.smartgwt.client.widgets.form.DynamicForm implements IHasWidgets, IPostCreateProcessing {

	public enum Operations {
		ADD, UPDATE, VALIDATE, ALL;

		public static Operations from(final DSOperationType smartType) {
			switch (smartType) {
				case ADD:
					return Operations.ADD;
				case UPDATE:
					return Operations.UPDATE;
				case VALIDATE:
					return Operations.VALIDATE;
				case FETCH:
				case REMOVE:
				case CUSTOM:
				default:
					return Operations.ALL;
			}
		}
	}

	private static final Logger					logger = LoggerFactory.getLog(Form.class);
	private Model									model;
	private List<IFormItemSpeudoWidget>				children = new LinkedList<IFormItemSpeudoWidget>();
	private Map<String, IFormItemExtensionList>		extensions = new HashMap<String, IFormItemExtensionList>();
	private final Signal							sigSubmit = new Signal();
	final Map<Operations, Signal>					sigSuccess = new HashMap<Operations, Signal>();
	final Map<Operations, Signal>					sigFailure = new HashMap<Operations, Signal>();
	private boolean								saving = false;

	public Form() {
		this.setHiliteRequiredFields(false);
		this.setSelectOnFocus(true);
	}

	// TODO: remove when SmartGWT bug corrected!
	@SuppressWarnings("unchecked")
	@Override
	public Record getValuesAsRecord() {
		final Map values = getValues();
		final Map m2 = new HashMap();
		m2.putAll(values);
		m2.remove("__ref");
		return new Record(JSOHelper.convertMapToJavascriptObject(m2));
	}

	public Model getModel() {
		return model;
	}

	public void setModel(final Model model) {
		this.model = model;
		this.setDataSource(model);
	}

	@Override
	public void setValuesManager(final ValuesManager values) {
		Assert.argumentNotNull(values);
		Assert.state(model == null);

		super.setValuesManager(values);
		final DataSource ds = values.getDataSource();
		if (ds instanceof Model)
			model = (Model)ds;
		this.setDataSource(ds);
	}

	public void setColumnWidths(final String widths) {
		Assert.argumentNotNull(widths);

		final String[] aWidth = widths.split(",");
		this.setNumCols(aWidth.length);
		final Object[] args = new Object[aWidth.length];
		for (int i = 0; i < aWidth.length; ++i) {
			try {
				args[i] = Integer.valueOf(aWidth[i]);
			} catch (final NumberFormatException _) {
				args[i] = aWidth[i].trim();
			}
		}
		this.setColWidths(args);
	}

	public void setUseAllModelFields(final Boolean value) {
		super.setUseAllDataSourceFields(value);
	}

	public ISlotManager getSuccessSlots(final Operations type) {
		return getSlots(sigSuccess, type);
	}

	public ISlotManager getFailureSlots(final Operations type) {
		return getSlots(sigFailure, type);
	}

	private static ISlotManager getSlots(final Map<Operations, Signal> sigMap, final Operations type) {
		Assert.argumentNotNull(sigMap);

		ISlotManager rslt = sigMap.get(type);
		if (rslt == null) {
			final Signal sig = new Signal();
			sigMap.put(type, sig);
			rslt = sig;
		}
		return rslt;
	}

	@Override
	public void add(final Widget w) {
		Assert.notNull(children);
		Assert.notNull(extensions);

		Helper.onCreate(w);
		if (w instanceof IFormItemSpeudoWidget)
			children.add((IFormItemSpeudoWidget)w);
		else if (w instanceof IFormItemExtensionList) {
			final IFormItemExtensionList ext = (IFormItemExtensionList)w;
			extensions.put(ext.getBuddy(), ext);
		} else if (w instanceof ListGridItem)
			children.add(new ListGridWrapper((ListGridItem)w));
		 else if (w instanceof TreeGridItem)
			children.add(new TreeGridWrapper((TreeGridItem)w));
		 else if (w instanceof ColumnTreeItem)
			children.add(new ColumnTreeWrapper((ColumnTreeItem)w));
		else
			logger.log(Level.SEVERE,"adding a widget of type '" + w.getClass().toString() + "' to a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
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
		logger.log(Level.SEVERE,"removing children widget from a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
        return false;
	}

	@Override
	public <T> T findChild(final Class<T> type, final boolean recursive) {
		if (children != null) {
			T rslt = IHasWidgets.Helper.findChild(children, type, recursive);
			if (rslt != null)
				return rslt;
		}
		return IHasWidgets.Helper.findChild(this.getChildren(), type, recursive);
	}

	public ISlotManager getSubmitSlots() {
		return sigSubmit;
	}

	public IHasValue<String> getStringField(final String name) {
		return new IHasValue<String>() {
			@Override
			public String getValue() {
				return Form.this.getValueAsString(name);
			}

			@Override
			public void setValue(final String value) {
				Form.this.setValue(name, value);
			}
		};
	}

	public IHasValue<Integer> getIntegerField(final String name) {
		return new IHasValue<Integer>() {
			@Override
			public Integer getValue() {
				return (Integer) Form.this.getValue(name);
			}

			@Override
			public void setValue(final Integer value) {
				Form.this.setValue(name, value);
			}
		};
	}

	public boolean isSaving() {
		return saving;
	}

	public void setSaving(final boolean saving) {
		this.saving = saving;
	}

	@Override
	public void saveData() {
		if (!isSaving()) {
			if (validate()) {
				sendNotification(sigSuccess, Operations.VALIDATE);
				setSaving(true);
				if (sigSubmit.isEmpty()) {
					super.saveData(new DSCallback() {
						@Override
						public void execute(final DSResponse response, @SuppressWarnings("unused") final Object rawData, final DSRequest request) {
							setSaving(false);
							sendNotification((response.getStatus() == DSResponse.STATUS_SUCCESS) ? sigSuccess : sigFailure, Operations.from(request.getOperationType()));
						}
					});
				} else {
					sigSubmit.fire();
				}
			} else {
				sendNotification(sigFailure, Operations.VALIDATE);
			}
		} else {
			logger.warning("prevented form to save data while already in the process of saving data");
		}
	}

	@Override
	public void saveData(@SuppressWarnings("unused") final DSCallback callback) {
		Assert.state(false);
	}

	@Override
	public void onCreate() {
		Assert.notNull(children);
		Assert.notNull(extensions);

		final List<FormItem> fields = new LinkedList<FormItem>();
		for (final IFormItemSpeudoWidget w : children) {
			final FormItem item = w.getItem(model);
			final IFormItemExtensionList ext = extensions.get(item.getName());
			if (ext != null)
				ext.onCreate(item);
			fields.add(item);
		}
		extensions = null;
		if (hasRequiredField(fields)) {
			final int i = getFirstInputFieldIndex();
			if (LogConfiguration.loggingIsEnabled()) {
				final FormItem field = fields.get(i);
				logger.fine("insert 'required information' hint field before " + field.getType() + " '" + field.getName() + "'");
			}
			fields.add(i, new RequiredFieldHint());
		}
		setFields(fields.toArray(new FormItem[0]));

		this.setAutoFocus(true);

		final SubmitButton submit = findChild(SubmitButton.class, true);
		if (submit != null) {
			logger.info("found SubmitButton. installing handlers to submit data");
			this.setSaveOnEnter(true);
			this.addSubmitValuesHandler(new SubmitValuesHandler() {
				@Override
				public void onSubmitValues(@SuppressWarnings("unused") final SubmitValuesEvent event) {
					if (!submit.isDisabled())
						saveData();
				}
			});
			submit.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(@SuppressWarnings("unused") final ClickEvent event) {
					saveData();
				}
			});
		} else
			logger.warning("no SubmitButton found");

		children = null;
	}

	public <A extends IAction<StringResult>> void editNewRecordWithDefault(final A getDefaultValuesCommand) {
		Assert.notNull(model.getDispatcher());

		model.getDispatcher().execute(getDefaultValuesCommand, new AsyncGetDefaultValuesCallback() {
			@Override
			public void onFailure(final Throwable caught) {
				logger.log(Level.WARNING, "fail to load parameter default values", caught);
				editNewRecord();
			}

			@Override
			public void onDefaultValues(final Map values) {
				if (values != null)
					editNewRecord(values);
				else
					editNewRecord();
			}
		});
	}

	private static boolean hasRequiredField(final List<FormItem> fields) {
		Assert.argumentNotNull(fields);

		for (final FormItem field : fields) {
			if (field.getRequired() && !field.getDisabled())
				return true;
		}
		return false;
	}

	private int getFirstInputFieldIndex() {
		int i = 0;
		for (final IFormItemSpeudoWidget w : children) {
			if (w.isInputField())
				break;
			++i;
		}
		return i;
	}

	protected void sendNotification(final Map<Operations, Signal> sigMap, final Operations type) {
		Assert.argumentNotNull(sigMap);

		Signal sig = sigMap.get(type);
		if (sig == null)
			sig = sigMap.get(Operations.ALL);
		if (sig != null)
			sig.fire();
	}

}
